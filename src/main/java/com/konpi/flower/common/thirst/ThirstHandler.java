package com.konpi.flower.common.thirst;

import com.konpi.flower.api.Capabilities;
import com.konpi.flower.api.state.StateHandlerBase;
import com.konpi.flower.api.state.capability.IThirst;
import com.konpi.flower.common.network.UpdateStateMessage;

import javax.vecmath.Vector3d;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ThirstHandler extends StateHandlerBase implements IThirst {

	private int thirstLevel;
	private int prevThirstLevel;
	private float thirstHydrationLevel;
	private float thirstExhaustionLevel;

	/**
	 * Used to time the seconds passed since thirst damage was last dealt to the
	 * player
	 */
	private int thirstTimer;

	private Vector3d movementVec;

	public ThirstHandler() {
		this.thirstLevel = 20;
		this.thirstHydrationLevel = 5.0F;
	}

	@Override
	public void update(EntityPlayer player, World world, Phase phase) {
		if (player.isCreative())
			return;

		if (phase == Phase.START) {
			if (movementVec != null) {
				Vector3d movement = new Vector3d(player.posX, player.posY, player.posZ);
				movement.sub(movementVec);
				movement.absolute();
				int distance = (int) Math.round(movement.length() * 100.0F);

				if (distance > 0)
					applyMovementExhaustion(player, distance);
			}
		} else if (phase == Phase.END) {
			this.movementVec = new Vector3d(player.posX, player.posY, player.posZ);

			EnumDifficulty enumdifficulty = world.getDifficulty();

			if (this.thirstExhaustionLevel > 4.0F) {
				this.thirstExhaustionLevel -= 4.0F;

				if (this.thirstHydrationLevel > 0.0F) {
					this.thirstHydrationLevel = Math.max(this.thirstHydrationLevel - 1.0F, 0.0F);
				} else if (enumdifficulty != EnumDifficulty.PEACEFUL) {
					this.thirstLevel = Math.max(this.thirstLevel - 1, 0);
				}
			}

			if (this.thirstLevel <= 0) {
				++this.thirstTimer;

				// Inflict thirst damage every 4 seconds
				if (this.thirstTimer >= 80) {
					if ((enumdifficulty == EnumDifficulty.PEACEFUL && player.getHealth() > 10.0F)
							|| (enumdifficulty == EnumDifficulty.EASY && player.getHealth() > 10.0F)
							|| (enumdifficulty == EnumDifficulty.NORMAL && player.getHealth() > 1.0F)
							|| enumdifficulty == EnumDifficulty.HARD) {
						player.attackEntityFrom(DamageSource.STARVE, 1.0F);
						// TODO:damagesource
					}

					this.thirstTimer = 0;
				}
			} else {
				this.thirstTimer = 0;
			}

			// If thirst is too low, prevent the player from sprinting
			if (player.isSprinting() && thirstLevel <= 6) {
				player.setSprinting(false);
			}
		}
	}

	private void applyMovementExhaustion(EntityPlayer player, int distance) {
		if (player.isInsideOfMaterial(Material.WATER)) {
			this.addExhaustion(0.015F * (float) distance * 0.01F);
		} else if (player.isInWater()) {
			this.addExhaustion(0.015F * (float) distance * 0.01F);
		} else if (player.onGround) {
			if (player.isSprinting()) {
				this.addExhaustion(0.099999994F * (float) distance * 0.01F);
			} else {
				this.addExhaustion(0.01F * (float) distance * 0.01F);
			}
		}
	}

	@Override
	public boolean hasChanged() {
		return this.prevThirstLevel != this.thirstLevel;
	}

	@Override
	public void onSendClientUpdate() {
		this.prevThirstLevel = this.thirstLevel;
	}

	@Override
	public IMessage createUpdateMessage() {
		NBTTagCompound data = (NBTTagCompound) Capabilities.THIRST.getStorage().writeNBT(Capabilities.THIRST, this,
				null);
		return new UpdateStateMessage(Capabilities.THIRST, data);
	}

	@Override
	public void setThirst(int thirst) {
		this.thirstLevel = thirst;
	}

	@Override
	public void setHydration(float hydration) {
		this.thirstHydrationLevel = hydration;
	}

	@Override
	public void setExhaustion(float exhaustion) {
		this.thirstExhaustionLevel = exhaustion;
	}

	@Override
	public int getThirst() {
		return this.thirstLevel;
	}

	@Override
	public float getHydration() {
		return this.thirstHydrationLevel;
	}

	@Override
	public float getExhaustion() {
		return this.thirstExhaustionLevel;
	}

	@Override
	public void setChangeTime(int ticks) {
		this.thirstTimer = ticks;
	}

	@Override
	public int getChangeTime() {
		return this.thirstTimer;
	}

	@Override
	public void addStats(int thirst, float hydration) {
		this.thirstLevel = Math.min(thirst + this.thirstLevel, 20);
		this.thirstHydrationLevel = Math.min(this.thirstHydrationLevel + (float) thirst * hydration * 2.0F,
				(float) this.thirstLevel);
	}

	public void addExhaustion(float amount) {
		this.thirstExhaustionLevel = Math.min(this.thirstExhaustionLevel + amount, 40.0F);
	}

	public boolean isThirsty() {
		return this.thirstLevel < 20;
	}

}

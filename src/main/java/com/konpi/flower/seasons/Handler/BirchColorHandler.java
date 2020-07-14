package com.konpi.flower.seasons.Handler;

import javax.annotation.Nullable;

import com.konpi.flower.seasons.intefaces.ISeasonColor;
import com.konpi.flower.seasons.savedata.SeasonTime;

import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeColorHelper;

/**
 * according to sereneseason
 *
 */
public class BirchColorHandler {

	public static void init() {
		Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(new IBlockColor() {
			public int colorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos,
					int tintIndex) {
				BlockPlanks.EnumType plankstype = (BlockPlanks.EnumType) state.getValue(BlockOldLeaf.VARIANT);

				if (plankstype == BlockPlanks.EnumType.SPRUCE) {
					return ColorizerFoliage.getFoliageColorPine();
				} else if (plankstype == BlockPlanks.EnumType.BIRCH) {
					int birchColor = ColorizerFoliage.getFoliageColorBirch();
					int dimension = Minecraft.getMinecraft().player.dimension;

					if (worldIn != null && pos != null && dimension == 0) {
						Biome biome = worldIn.getBiome(pos);

						SeasonTime calendar = SeasonHandler.getClientSeasonTime();
						ISeasonColor colorProvider = biome.canRain() ? calendar.getSeasonState()
								: calendar.getTropicalSeasonState();
						birchColor = colorProvider.getBirchColor();
					}

					return birchColor;
				} else {
					return worldIn != null && pos != null ? BiomeColorHelper.getFoliageColorAtPos(worldIn, pos)
							: ColorizerFoliage.getFoliageColorBasic();
				}
			}
		}, Blocks.LEAVES);
	}
}
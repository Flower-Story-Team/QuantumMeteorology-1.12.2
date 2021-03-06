package com.konpi.quantummeteorology.common.item;

import com.konpi.quantummeteorology.common.init.ModItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * 碗里的食物，不能把碗给吃了
 */
public class ItemFoodInBowl extends ItemFoodBase
{
    /**
     * @param registryName 注册名
     * @param amount       回复饥饿值
     * @param saturation   回复饱和度
     * @param isWolfFood   是否能给狼吃
     */
    public ItemFoodInBowl(String registryName, int amount, float saturation, boolean isWolfFood) {
        super(registryName, amount, saturation, isWolfFood);
        this.setMaxStackSize(1);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
    {
        super.onItemUseFinish(stack, worldIn,entityLiving);
        return new ItemStack(ModItems.ItemHolder.BOWL);
    }

}
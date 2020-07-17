package com.konpi.flower.common.block;

import com.konpi.flower.Flower;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

/**
 * 普通方块的基础类
 */
public class BlockBase extends Block
{

    public BlockBase(String registryName, Material blockMaterialIn, MapColor blockMapColorIn)
    {
        super(blockMaterialIn, blockMapColorIn);
        this.setRegistryName(registryName);
        this.setTranslationKey(Flower.MODID + "." + registryName);
    }

}

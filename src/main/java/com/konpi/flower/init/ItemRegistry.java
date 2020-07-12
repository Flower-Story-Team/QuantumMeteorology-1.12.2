package com.konpi.flower.init;

import com.konpi.flower.Flower;
import com.konpi.flower.item.ModFoodBase;
import com.konpi.flower.item.ModItemBlockBase;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

import java.util.ArrayList;

@Mod.EventBusSubscriber
public class ItemRegistry
{

    //一些普通的物品，它们只有一个模型
    public static final ArrayList<Item> simpleItemList = new ArrayList<>();

    static
    {
        //向一些固定类型的集合里添加一些东西
        Flower.logger.info("initializing normal items");
        simpleItemList.add(new ModFoodBase("rice", 1, 1, false));
    }

    /**
     * 注册Item
     */
    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event)
    {
        //实例化方块物品
        Flower.logger.info("initializing block items");
        for (Block b : BlockRegistry.simpleBlockList)
        {
            event.getRegistry().register(new ModItemBlockBase(b));
        }

        //注册普通物品
        Flower.logger.info("registering items");
        event.getRegistry().registerAll(simpleItemList.toArray(new Item[0]));

    }

    @ObjectHolder(Flower.MODID)
    public static class ItemHolder
    {

        //注册物品后这个会自动变成对应物品的引用
        @ObjectHolder("rice")
        public static final Item RICE = null;

    }

}
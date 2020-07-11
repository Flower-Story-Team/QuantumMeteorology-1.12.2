package com.konpi.flowerofhua.flower.init;

import com.konpi.flowerofhua.flower.Flower;
import com.konpi.flowerofhua.flower.item.ModFoodBase;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(Flower.MODID)
@Mod.EventBusSubscriber
public class ItemRegistry {

    //注册物品后这个会自动变成对应物品的引用
    @ObjectHolder("rice")
    public static final Item  RICE = null;

    /**
     * 注册Item
     */
    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event) {
        Flower.logger.info("registering items");
        event.getRegistry().register(new ModFoodBase("rice", 1, 1, false));
    }

}
package com.kadirergun.monstergolem;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MonsterGolemMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCreativeTabEvents {

    @SubscribeEvent
    public static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) { // ✅ ResourceKey karşılaştırması
            event.accept(ModItems.MONSTER_GOLEM_SPAWN_EGG.get());
        }
    }
}
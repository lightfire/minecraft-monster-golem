package com.kadirergun.monstergolem;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Event subscriber that adds mod items to Minecraft creative mode tabs during
 * the BuildCreativeModeTabContentsEvent. Registered on the MOD event bus.
 *
 * Current behavior: adds the Monster Golem spawn egg to the SPAWN_EGGS tab.
 */
@Mod.EventBusSubscriber(modid = MonsterGolemMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCreativeTabEvents {

    /**
     * Adds items to the specified creative tab when its contents are being built.
     * This method listens for BuildCreativeModeTabContentsEvent and, if the
     * event corresponds to the SPAWN_EGGS tab, accepts the mod's spawn egg.
     *
     * @param event the BuildCreativeModeTabContentsEvent fired while creating a creative tab's contents
     */
    @SubscribeEvent
    public static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) { // ✅ ResourceKey comparison
            event.accept(ModItems.MONSTER_GOLEM_SPAWN_EGG.get());
        }
    }
}
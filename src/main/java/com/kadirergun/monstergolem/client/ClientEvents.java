package com.kadirergun.monstergolem.client;

import com.kadirergun.monstergolem.ModEntities;
import com.kadirergun.monstergolem.MonsterGolemMod;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Client-side event subscriber that registers client-only renderers and other
 * client initialization tasks for the MonsterGolem mod. This class is only
 * loaded on the client distribution (Dist.CLIENT).
 */
@Mod.EventBusSubscriber(modid = MonsterGolemMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {

    /**
     * Handles client setup. Registers renderers and other client-specific handlers.
     *
     * @param event the FMLClientSetupEvent fired during mod loading on the client
     */
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        EntityRenderers.register(ModEntities.MONSTER_GOLEM.get(), MonsterGolemRenderer::new);
    }
}
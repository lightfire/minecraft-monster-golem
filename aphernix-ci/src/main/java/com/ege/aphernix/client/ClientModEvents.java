package com.ege.aphernix.client;

import com.ege.aphernix.AphernixMod;
import com.ege.aphernix.registry.ModEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AphernixMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientModEvents {
    private ClientModEvents() {}
    @SubscribeEvent public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) { event.registerEntityRenderer(ModEntities.APHERNIX.get(), AphernixRenderer::new); }
}

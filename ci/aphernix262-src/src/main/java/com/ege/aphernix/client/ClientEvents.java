package com.ege.aphernix.client;

import com.ege.aphernix.registry.ModEntities;
import net.minecraft.client.renderer.entity.ChickenRenderer;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraftforge.client.event.EntityRenderersEvent;

public final class ClientEvents {
    private ClientEvents() {}

    public static void init() {
        EntityRenderersEvent.RegisterRenderers.BUS.addListener(ClientEvents::registerRenderers);
    }

    private static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.APHERNIX.get(), ZombieRenderer::new);
        event.registerEntityRenderer(ModEntities.APHERNIX_TRANSFORMED.get(), ZombieRenderer::new);
        event.registerEntityRenderer(ModEntities.APHERNIX_OLD.get(), ZombieRenderer::new);
        event.registerEntityRenderer(ModEntities.YUSUFTE.get(), ZombieRenderer::new);
        event.registerEntityRenderer(ModEntities.YUSUFTE_DIAMOND.get(), ZombieRenderer::new);
        event.registerEntityRenderer(ModEntities.YUSUFTE_OLD.get(), ZombieRenderer::new);
        event.registerEntityRenderer(ModEntities.OSMAN_TUS.get(), ZombieRenderer::new);
        event.registerEntityRenderer(ModEntities.HELLO_TUS.get(), ZombieRenderer::new);
        event.registerEntityRenderer(ModEntities.KAZIM_USTA.get(), ZombieRenderer::new);
        event.registerEntityRenderer(ModEntities.YUSUFTE_CHICKEN.get(), ChickenRenderer::new);
    }
}

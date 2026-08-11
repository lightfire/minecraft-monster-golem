package com.ege.aphernix.client;

import com.ege.aphernix.registry.ModEntities;
import net.minecraftforge.client.event.EntityRenderersEvent;

public final class ClientEvents {
    private ClientEvents() {}

    public static void init() {
        EntityRenderersEvent.RegisterRenderers.BUS.addListener(ClientEvents::registerRenderers);
    }

    private static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.APHERNIX.get(), c -> new TexturedZombieRenderer(c, "aphernix.png"));
        event.registerEntityRenderer(ModEntities.APHERNIX_TRANSFORMED.get(), c -> new TexturedZombieRenderer(c, "aphernix_transformed.png"));
        event.registerEntityRenderer(ModEntities.APHERNIX_OLD.get(), c -> new TexturedZombieRenderer(c, "aphernix_old.png"));
        event.registerEntityRenderer(ModEntities.YUSUFTE.get(), c -> new TexturedZombieRenderer(c, "yusufte.png"));
        event.registerEntityRenderer(ModEntities.YUSUFTE_DIAMOND.get(), c -> new TexturedZombieRenderer(c, "yusufte_diamond.png"));
        event.registerEntityRenderer(ModEntities.YUSUFTE_OLD.get(), c -> new TexturedZombieRenderer(c, "yusufte_old.png"));
        event.registerEntityRenderer(ModEntities.OSMAN_TUS.get(), c -> new TexturedZombieRenderer(c, "osman_tus.png"));
        event.registerEntityRenderer(ModEntities.HELLO_TUS.get(), c -> new TexturedZombieRenderer(c, "hello_tus.png"));
        event.registerEntityRenderer(ModEntities.KAZIM_USTA.get(), c -> new TexturedZombieRenderer(c, "hello_tus.png"));
        event.registerEntityRenderer(ModEntities.YUSUFTE_CHICKEN.get(), TexturedChickenRenderer::new);
    }
}

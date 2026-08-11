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

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.APHERNIX.get(), AphernixRenderer::new);
        event.registerEntityRenderer(ModEntities.APHERNIX_TRANSFORMED.get(), AphernixTransformedRenderer::new);
        event.registerEntityRenderer(ModEntities.APHERNIX_OLD.get(), AphernixOldRenderer::new);
        event.registerEntityRenderer(ModEntities.YUSUFTE.get(), YusufteRenderer::new);
        event.registerEntityRenderer(ModEntities.YUSUFTE_CHICKEN.get(), YusufteChickenRenderer::new);
        event.registerEntityRenderer(ModEntities.YUSUFTE_DIAMOND.get(), YusufteDiamondRenderer::new);
        event.registerEntityRenderer(ModEntities.YUSUFTE_OLD.get(), YusufteOldRenderer::new);
        event.registerEntityRenderer(ModEntities.OSMAN_TUS.get(), OsmanTusRenderer::new);
        event.registerEntityRenderer(ModEntities.HELLO_TUS.get(), HelloTusRenderer::new);
        event.registerEntityRenderer(ModEntities.KAZIM_USTA.get(), KazimUstaRenderer::new);
    }
}

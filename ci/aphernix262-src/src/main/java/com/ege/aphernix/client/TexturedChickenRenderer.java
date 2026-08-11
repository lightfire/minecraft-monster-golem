package com.ege.aphernix.client;

import com.ege.aphernix.AphernixMod;
import net.minecraft.client.renderer.entity.ChickenRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ChickenRenderState;
import net.minecraft.resources.Identifier;

public final class TexturedChickenRenderer extends ChickenRenderer {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(
            AphernixMod.MOD_ID, "textures/entity/yusufte_chicken.png");

    public TexturedChickenRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public Identifier getTextureLocation(ChickenRenderState state) {
        return TEXTURE;
    }
}

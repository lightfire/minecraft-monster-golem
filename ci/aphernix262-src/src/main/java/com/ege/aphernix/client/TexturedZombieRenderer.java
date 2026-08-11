package com.ege.aphernix.client;

import com.ege.aphernix.AphernixMod;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import net.minecraft.resources.Identifier;

public final class TexturedZombieRenderer extends ZombieRenderer {
    private final Identifier texture;

    public TexturedZombieRenderer(EntityRendererProvider.Context context, String textureName) {
        super(context);
        this.texture = Identifier.fromNamespaceAndPath(AphernixMod.MOD_ID, "textures/entity/" + textureName);
    }

    @Override
    public Identifier getTextureLocation(ZombieRenderState state) {
        return texture;
    }
}

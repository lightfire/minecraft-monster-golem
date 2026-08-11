package com.ege.aphernix.client;

import com.ege.aphernix.entity.AphernixEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public abstract class BaseTusRenderer<T extends AphernixEntity> extends MobRenderer<T, PlayerModel<T>> {
    private final ResourceLocation texture;

    protected BaseTusRenderer(EntityRendererProvider.Context context, String texturePath) {
        super(context, new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER), false), 0.5F);
        this.texture = new ResourceLocation("aphernix", texturePath);
        this.model.hat.visible = true;
        this.model.jacket.visible = true;
        this.model.leftPants.visible = true;
        this.model.rightPants.visible = true;
        this.model.leftSleeve.visible = true;
        this.model.rightSleeve.visible = true;
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight) {
        this.model.crouching = entity.isCrouching();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return texture;
    }
}

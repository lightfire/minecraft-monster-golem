package com.ege.aphernix.client;

import com.ege.aphernix.AphernixMod;
import com.ege.aphernix.entity.AphernixEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.Items;

public class AphernixRenderer extends MobRenderer<AphernixEntity, PlayerModel<AphernixEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(AphernixMod.MOD_ID, "textures/entity/aphernix.png");

    public AphernixRenderer(EntityRendererProvider.Context context) {
        super(context, new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER_SLIM), true), 0.5F);
        this.model.hat.visible = true;
        this.model.jacket.visible = true;
        this.model.leftSleeve.visible = true;
        this.model.rightSleeve.visible = true;
        this.model.leftPants.visible = true;
        this.model.rightPants.visible = true;

        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
        this.addLayer(new HumanoidArmorLayer<>(
                this,
                new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)),
                new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)),
                context.getModelManager()
        ));
    }

    @Override
    public void render(AphernixEntity entity, float entityYaw, float partialTicks, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight) {
        this.model.crouching = entity.isCrouching();
        this.model.rightArmPose = HumanoidModel.ArmPose.EMPTY;
        this.model.leftArmPose = HumanoidModel.ArmPose.EMPTY;

        if (!entity.getMainHandItem().isEmpty()) {
            setArmPose(entity.getMainArm(), HumanoidModel.ArmPose.ITEM);
        }

        if (entity.isUsingItem()
                && entity.getUsedItemHand() == InteractionHand.OFF_HAND
                && entity.getUseItem().is(Items.SHIELD)) {
            setArmPose(entity.getMainArm().getOpposite(), HumanoidModel.ArmPose.BLOCK);
        }

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    private void setArmPose(HumanoidArm arm, HumanoidModel.ArmPose pose) {
        if (arm == HumanoidArm.RIGHT) {
            this.model.rightArmPose = pose;
        } else {
            this.model.leftArmPose = pose;
        }
    }

    @Override
    public ResourceLocation getTextureLocation(AphernixEntity entity) {
        return TEXTURE;
    }
}

package com.kadirergun.monstergolem.client;

import com.kadirergun.monstergolem.entity.MonsterGolemEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class GolemArmorItemsLayer extends RenderLayer<MonsterGolemEntity, net.minecraft.client.model.IronGolemModel<MonsterGolemEntity>> {

    private final ItemRenderer itemRenderer;

    public GolemArmorItemsLayer(RenderLayerParent<MonsterGolemEntity, net.minecraft.client.model.IronGolemModel<MonsterGolemEntity>> parent,
                                ItemRenderer itemRenderer) {
        super(parent);
        this.itemRenderer = itemRenderer;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                       MonsterGolemEntity golem,
                       float limbSwing, float limbSwingAmount, float partialTick,
                       float ageInTicks, float netHeadYaw, float headPitch) {

        // HEAD (kask)
        renderAsMountedItem(poseStack, buffer, packedLight, golem.getItemBySlot(EquipmentSlot.HEAD),
                0.0f, 2.35f, 0.0f,  // x,y,z (golem üstü)
                0.9f);

        // CHEST (göğüs zırhı)
        renderAsMountedItem(poseStack, buffer, packedLight, golem.getItemBySlot(EquipmentSlot.CHEST),
                0.0f, 1.55f, -0.25f,
                1.2f);

        // LEGS (pantolon) - bel hizası
        renderAsMountedItem(poseStack, buffer, packedLight, golem.getItemBySlot(EquipmentSlot.LEGS),
                0.0f, 1.05f, -0.20f,
                1.1f);

        // FEET (bot) - ayak hizası (tek item gibi)
        renderAsMountedItem(poseStack, buffer, packedLight, golem.getItemBySlot(EquipmentSlot.FEET),
                0.0f, 0.35f, -0.15f,
                1.0f);
    }

    private void renderAsMountedItem(PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                                     ItemStack stack,
                                     float x, float y, float z,
                                     float scale) {
        if (stack.isEmpty()) return;

        poseStack.pushPose();
        poseStack.translate(x, y, z);
        poseStack.scale(scale, scale, scale);

        // Item gibi render (zırh parçaları net görünür)
        itemRenderer.renderStatic(
                stack,
                ItemDisplayContext.FIXED,
                packedLight,
                net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY,
                poseStack,
                buffer,
                null,
                0
        );

        poseStack.popPose();
    }
}

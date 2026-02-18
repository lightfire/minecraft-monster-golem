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
import org.jetbrains.annotations.NotNull;

/**
 * A custom render layer for displaying armor/item pieces on a MonsterGolemEntity.
 * This layer uses an ItemRenderer to draw ItemStacks found in equipment slots
 * (HEAD, CHEST, LEGS, FEET) at fixed positions and scales on the model.
 * <p>
 * Usage:
 * - Use this layer to add visual accessories to the golem model (e.g. helmet,
 *   chestplate, leggings, boots). Items are rendered at specified offsets and
 *   scales rather than being attached to specific model bones.
 */
public class GolemArmorItemsLayer extends RenderLayer<MonsterGolemEntity, net.minecraft.client.model.IronGolemModel<MonsterGolemEntity>> {

    /** Helper ItemRenderer used to draw items. */
    private final ItemRenderer itemRenderer;

    /**
     * Creates a new GolemArmorItemsLayer.
     *
     * @param parent       the RenderLayerParent this layer is attached to
     * @param itemRenderer the ItemRenderer used to render ItemStacks
     */
    public GolemArmorItemsLayer(RenderLayerParent<MonsterGolemEntity, net.minecraft.client.model.IronGolemModel<MonsterGolemEntity>> parent,
                                ItemRenderer itemRenderer) {
        super(parent);
        this.itemRenderer = itemRenderer;
    }

    /**
     * Renders the layer. Draws the items from the golem's equipment slots at fixed
     * positions and with predefined scales.
     *
     * @param poseStack      the PoseStack used for transformations
     * @param buffer         the MultiBufferSource to draw into
     * @param packedLight    packed light information
     * @param golem          the MonsterGolemEntity being rendered
     * @param limbSwing      limb swing (animation parameter, unused here)
     * @param limbSwingAmount limb swing amount (animation parameter)
     * @param partialTick    partial tick for interpolation
     * @param ageInTicks     age in ticks (animation parameter)
     * @param netHeadYaw     head yaw (animation parameter)
     * @param headPitch      head pitch (animation parameter)
     */
    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight,
                       MonsterGolemEntity golem,
                       float limbSwing, float limbSwingAmount, float partialTick,
                       float ageInTicks, float netHeadYaw, float headPitch) {

        // HEAD (helmet)
        renderAsMountedItem(poseStack, buffer, packedLight, golem.getItemBySlot(EquipmentSlot.HEAD),
                0.0f, 2.35f, 0.0f,  // x,y,z (above the golem)
                0.9f);

        // CHEST (chestplate)
        renderAsMountedItem(poseStack, buffer, packedLight, golem.getItemBySlot(EquipmentSlot.CHEST),
                0.0f, 1.55f, -0.25f,
                1.2f);

        // LEGS (leggings) - waist area
        renderAsMountedItem(poseStack, buffer, packedLight, golem.getItemBySlot(EquipmentSlot.LEGS),
                0.0f, 1.05f, -0.20f,
                1.1f);

        // FEET (boots) - foot area (rendered as a single item)
        renderAsMountedItem(poseStack, buffer, packedLight, golem.getItemBySlot(EquipmentSlot.FEET),
                0.0f, 0.35f, -0.15f,
                1.0f);
    }

    /**
     * Renders the given ItemStack as if mounted to the model at the provided offset
     * and scale. This helper applies translations and scaling to the PoseStack and
     * then delegates to the ItemRenderer.
     * <p>
     * Notes:
     * - If the stack is empty, the method returns without rendering.
     * - Items are rendered using the provided offset and scale, not attached to
     *   model bones.
     *
     * @param poseStack  the PoseStack for render transformations
     * @param buffer     the MultiBufferSource to draw into
     * @param packedLight packed light information
     * @param stack      the ItemStack to render (no-op if empty)
     * @param x          X-axis offset in model coordinates
     * @param y          Y-axis offset in model coordinates
     * @param z          Z-axis offset in model coordinates
     * @param scale      scale factor (1.0 = normal size)
     */
    private void renderAsMountedItem(PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                                     ItemStack stack,
                                     float x, float y, float z,
                                     float scale) {
        if (stack.isEmpty()) return;

        poseStack.pushPose();
        poseStack.translate(x, y, z);
        poseStack.scale(scale, scale, scale);

        // Render the item (armor pieces will appear clearly)
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

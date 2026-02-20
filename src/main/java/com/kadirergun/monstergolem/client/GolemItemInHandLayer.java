package com.kadirergun.monstergolem.client;

import com.kadirergun.monstergolem.entity.MonsterGolemEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Quaternionf;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.model.IronGolemModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

/**
 * Custom held-item render layer for golems using the IronGolemModel.
 * Attempts to anchor the item to the model's right arm using reflection so the
 * bow appears in the golem's hand. If reflection fails, falls back to tuned
 * manual transforms.
 */
public class GolemItemInHandLayer extends RenderLayer<MonsterGolemEntity, IronGolemModel<MonsterGolemEntity>> {

    private final ItemRenderer itemRenderer;

    // Reflection cache for the private rightArm field
    private static Field RIGHT_ARM_FIELD = null;
    private static boolean RIGHT_ARM_FIELD_TRIED = false;

    public GolemItemInHandLayer(RenderLayerParent<MonsterGolemEntity, IronGolemModel<MonsterGolemEntity>> parent, ItemRenderer itemRenderer) {
        super(parent);
        this.itemRenderer = itemRenderer;
    }

    private static void tryInitRightArmField(IronGolemModel<?> model) {
        if (RIGHT_ARM_FIELD_TRIED) return;
        RIGHT_ARM_FIELD_TRIED = true;
        Class<?> cls = model.getClass();
        try {
            Field f = cls.getDeclaredField("rightArm");
            f.setAccessible(true);
            RIGHT_ARM_FIELD = f;
        } catch (NoSuchFieldException e) {
            // Field not found - leave RIGHT_ARM_FIELD null so we use fallback
        }
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight,
                       @NotNull MonsterGolemEntity entity, float limbSwing, float limbSwingAmount, float partialTicks,
                       float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack stack = entity.getItemBySlot(EquipmentSlot.MAINHAND);
        if (stack.isEmpty()) return;

        poseStack.pushPose();

        IronGolemModel<MonsterGolemEntity> model = this.getParentModel();

        // Try to anchor to the model's right arm via reflection for precise placement
        tryInitRightArmField(model);
        boolean anchored = false;
        if (RIGHT_ARM_FIELD != null) {
            try {
                Object raw = RIGHT_ARM_FIELD.get(model);
                if (raw instanceof ModelPart) {
                    ModelPart rightArm = (ModelPart) raw;
                    // Apply the arm transform so subsequent transforms are relative to the hand
                    rightArm.translateAndRotate(poseStack);
                    anchored = true;
                }
            } catch (IllegalAccessException ignored) {
                anchored = false;
            }
        }

        if (!anchored) {
            // Fallback: tuned translation values that position the item near the golem's right hand
            poseStack.translate(0.35D, -0.9D, -0.25D);
            Quaternionf rot = new Quaternionf();
            rot.rotateY((float) Math.toRadians(-40.0));
            rot.rotateX((float) Math.toRadians(-10.0));
            rot.rotateZ((float) Math.toRadians(-5.0));
            poseStack.mulPose(rot);
            poseStack.scale(0.85F, 0.85F, 0.85F);
        } else {
            // Small local adjustments after anchoring to the hand pivot
            poseStack.translate(-0.05D, 0.2D, -0.1D);
            Quaternionf rot2 = new Quaternionf();
            rot2.rotateY((float) Math.toRadians(-10.0));
            rot2.rotateX((float) Math.toRadians(-15.0));
            poseStack.mulPose(rot2);
            poseStack.scale(0.9F, 0.9F, 0.9F);
        }

        // Render the item using third person right hand transform
        this.itemRenderer.renderStatic(
                stack,
                ItemDisplayContext.THIRD_PERSON_RIGHT_HAND,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                poseStack,
                bufferSource,
                null,
                0
        );

        poseStack.popPose();
    }
}

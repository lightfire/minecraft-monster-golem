package com.kadirergun.monstergolem.client;

import com.kadirergun.monstergolem.entity.MonsterGolemEntity;
import net.minecraft.client.model.IronGolemModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

/**
 * Renderer for the MonsterGolemEntity. Sets up the model, shadow size, and
 * additional render layers (e.g. armor/item layers).
 */
public class MonsterGolemRenderer extends LivingEntityRenderer<MonsterGolemEntity, IronGolemModel<MonsterGolemEntity>> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.parse("customgolem:textures/entity/monster_golem.png");

    /**
     * Constructs the renderer and attaches custom render layers.
     *
     * @param context the EntityRendererProvider.Context provided by the client
     */
    public MonsterGolemRenderer(EntityRendererProvider.Context context) {
        super(context, new IronGolemModel<>(context.bakeLayer(ModelLayers.IRON_GOLEM)), 0.7F);
        this.addLayer(new GolemArmorItemsLayer(this, context.getItemRenderer()));
    }

    /**
     * Returns the texture resource location used to render the entity.
     *
     * @param entity the MonsterGolemEntity instance
     * @return the ResourceLocation pointing to the entity texture
     */
    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull MonsterGolemEntity entity) {
        return TEXTURE;
    }
}

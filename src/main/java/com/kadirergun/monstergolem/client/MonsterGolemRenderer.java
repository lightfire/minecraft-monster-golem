package com.kadirergun.monstergolem.client;

import com.kadirergun.monstergolem.entity.MonsterGolemEntity;
import net.minecraft.client.model.IronGolemModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;

public class MonsterGolemRenderer extends LivingEntityRenderer<MonsterGolemEntity, IronGolemModel<MonsterGolemEntity>> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.parse("customgolem:textures/entity/monster_golem.png");

    public MonsterGolemRenderer(EntityRendererProvider.Context context) {
        super(context, new IronGolemModel<>(context.bakeLayer(ModelLayers.IRON_GOLEM)), 0.7F);
        this.addLayer(new GolemArmorItemsLayer(this, context.getItemRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(MonsterGolemEntity entity) {
        return TEXTURE;
    }
}

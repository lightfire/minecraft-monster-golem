package com.ege.aphernix.client;

import com.ege.aphernix.entity.OsmanTusEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class OsmanTusRenderer extends BaseTusRenderer<OsmanTusEntity> {
    public OsmanTusRenderer(EntityRendererProvider.Context context) {
        super(context, "textures/entity/osman_tus.png");
    }
}

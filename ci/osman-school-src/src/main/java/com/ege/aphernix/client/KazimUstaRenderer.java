package com.ege.aphernix.client;

import com.ege.aphernix.entity.KazimUstaEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class KazimUstaRenderer extends BaseTusRenderer<KazimUstaEntity> {
    public KazimUstaRenderer(EntityRendererProvider.Context context) {
        super(context, "textures/entity/hello_tus.png");
    }
}

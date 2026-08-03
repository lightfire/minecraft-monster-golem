package com.ege.aphernix.client;

import com.ege.aphernix.entity.HelloTusEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class HelloTusRenderer extends BaseTusRenderer<HelloTusEntity> {
    public HelloTusRenderer(EntityRendererProvider.Context context) {
        super(context, "textures/entity/hello_tus.png");
    }
}

package com.ege.aphernix.event;

import com.ege.aphernix.entity.AphernixEntity;
import com.ege.aphernix.registry.ModEntities;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;

public final class ModEvents {
    private ModEvents() {}

    public static void createAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.APHERNIX.get(), AphernixEntity.createAphernixAttributes().build());
        event.put(ModEntities.APHERNIX_TRANSFORMED.get(), AphernixEntity.createAphernixAttributes().build());
        event.put(ModEntities.APHERNIX_OLD.get(), AphernixEntity.createAphernixAttributes().build());
        event.put(ModEntities.YUSUFTE.get(), AphernixEntity.createAphernixAttributes().build());
        event.put(ModEntities.YUSUFTE_DIAMOND.get(), AphernixEntity.createAphernixAttributes().build());
        event.put(ModEntities.YUSUFTE_OLD.get(), AphernixEntity.createAphernixAttributes().build());
        event.put(ModEntities.OSMAN_TUS.get(), AphernixEntity.createAphernixAttributes().build());
        event.put(ModEntities.HELLO_TUS.get(), AphernixEntity.createAphernixAttributes().build());
        event.put(ModEntities.KAZIM_USTA.get(), AphernixEntity.createAphernixAttributes().build());
        event.put(ModEntities.YUSUFTE_CHICKEN.get(), Chicken.createAttributes().build());
    }
}

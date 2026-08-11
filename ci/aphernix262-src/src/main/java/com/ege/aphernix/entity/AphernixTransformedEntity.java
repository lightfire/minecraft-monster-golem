package com.ege.aphernix.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class AphernixTransformedEntity extends AphernixEntity {
    public AphernixTransformedEntity(EntityType<? extends AphernixTransformedEntity> type, Level level) {
        super(type, level);
    }
}

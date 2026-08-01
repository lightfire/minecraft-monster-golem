package com.ege.aphernix.registry;

import com.ege.aphernix.AphernixMod;
import com.ege.aphernix.entity.AphernixEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, AphernixMod.MOD_ID);
    public static final RegistryObject<EntityType<AphernixEntity>> APHERNIX = ENTITY_TYPES.register("aphernix", () -> EntityType.Builder.of(AphernixEntity::new, MobCategory.CREATURE).sized(0.6F, 1.8F).clientTrackingRange(8).build("aphernix:aphernix"));
    private ModEntities() {}
    public static void register(IEventBus eventBus) { ENTITY_TYPES.register(eventBus); }
}

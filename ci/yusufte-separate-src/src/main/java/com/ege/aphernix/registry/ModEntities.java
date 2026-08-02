package com.ege.aphernix.registry;

import com.ege.aphernix.AphernixMod;
import com.ege.aphernix.entity.AphernixEntity;
import com.ege.aphernix.entity.AphernixOldEntity;
import com.ege.aphernix.entity.AphernixTransformedEntity;
import com.ege.aphernix.entity.YusufteChickenEntity;
import com.ege.aphernix.entity.YusufteDiamondEntity;
import com.ege.aphernix.entity.YusufteEntity;
import com.ege.aphernix.entity.YusufteOldEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, AphernixMod.MOD_ID);

    public static final RegistryObject<EntityType<AphernixEntity>> APHERNIX =
            ENTITY_TYPES.register("aphernix", () -> EntityType.Builder
                    .of(AphernixEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.8F).clientTrackingRange(8)
                    .build("aphernix:aphernix"));

    public static final RegistryObject<EntityType<AphernixTransformedEntity>> APHERNIX_TRANSFORMED =
            ENTITY_TYPES.register("aphernix_transformed", () -> EntityType.Builder
                    .of(AphernixTransformedEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.8F).clientTrackingRange(8)
                    .build("aphernix:aphernix_transformed"));

    public static final RegistryObject<EntityType<AphernixOldEntity>> APHERNIX_OLD =
            ENTITY_TYPES.register("aphernix_old", () -> EntityType.Builder
                    .of(AphernixOldEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.8F).clientTrackingRange(8)
                    .build("aphernix:aphernix_old"));

    public static final RegistryObject<EntityType<YusufteEntity>> YUSUFTE =
            ENTITY_TYPES.register("yusufte", () -> EntityType.Builder
                    .of(YusufteEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.8F).clientTrackingRange(8)
                    .build("aphernix:yusufte"));

    public static final RegistryObject<EntityType<YusufteChickenEntity>> YUSUFTE_CHICKEN =
            ENTITY_TYPES.register("yusufte_chicken", () -> EntityType.Builder
                    .of(YusufteChickenEntity::new, MobCategory.CREATURE)
                    .sized(0.52F, 1.35F).clientTrackingRange(10)
                    .build("aphernix:yusufte_chicken"));

    public static final RegistryObject<EntityType<YusufteDiamondEntity>> YUSUFTE_DIAMOND =
            ENTITY_TYPES.register("yusufte_diamond", () -> EntityType.Builder
                    .of(YusufteDiamondEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.8F).clientTrackingRange(8)
                    .build("aphernix:yusufte_diamond"));

    public static final RegistryObject<EntityType<YusufteOldEntity>> YUSUFTE_OLD =
            ENTITY_TYPES.register("yusufte_old", () -> EntityType.Builder
                    .of(YusufteOldEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.8F).clientTrackingRange(8)
                    .build("aphernix:yusufte_old"));

    private ModEntities() {}

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}

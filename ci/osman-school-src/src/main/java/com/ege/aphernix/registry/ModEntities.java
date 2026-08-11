package com.ege.aphernix.registry;

import com.ege.aphernix.AphernixMod;
import com.ege.aphernix.entity.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, AphernixMod.MOD_ID);

    public static final RegistryObject<EntityType<AphernixEntity>> APHERNIX = entity("aphernix", AphernixEntity::new);
    public static final RegistryObject<EntityType<AphernixTransformedEntity>> APHERNIX_TRANSFORMED = entity("aphernix_transformed", AphernixTransformedEntity::new);
    public static final RegistryObject<EntityType<AphernixOldEntity>> APHERNIX_OLD = entity("aphernix_old", AphernixOldEntity::new);
    public static final RegistryObject<EntityType<YusufteEntity>> YUSUFTE = entity("yusufte", YusufteEntity::new);

    public static final RegistryObject<EntityType<YusufteChickenEntity>> YUSUFTE_CHICKEN =
            ENTITY_TYPES.register("yusufte_chicken", () -> EntityType.Builder
                    .of(YusufteChickenEntity::new, MobCategory.CREATURE)
                    .sized(0.52F, 1.35F).clientTrackingRange(10)
                    .build("aphernix:yusufte_chicken"));

    public static final RegistryObject<EntityType<YusufteDiamondEntity>> YUSUFTE_DIAMOND = entity("yusufte_diamond", YusufteDiamondEntity::new);
    public static final RegistryObject<EntityType<YusufteOldEntity>> YUSUFTE_OLD = entity("yusufte_old", YusufteOldEntity::new);
    public static final RegistryObject<EntityType<OsmanTusEntity>> OSMAN_TUS = entity("osman_tus", OsmanTusEntity::new);
    public static final RegistryObject<EntityType<HelloTusEntity>> HELLO_TUS = entity("hello_tus", HelloTusEntity::new);
    public static final RegistryObject<EntityType<KazimUstaEntity>> KAZIM_USTA = entity("kazim_usta", KazimUstaEntity::new);

    private static <T extends AphernixEntity> RegistryObject<EntityType<T>> entity(
            String name, EntityType.EntityFactory<T> factory) {
        return ENTITY_TYPES.register(name, () -> EntityType.Builder
                .of(factory, MobCategory.CREATURE)
                .sized(0.6F, 1.8F).clientTrackingRange(8)
                .build("aphernix:" + name));
    }

    private ModEntities() {}

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}

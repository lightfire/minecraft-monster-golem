package com.ege.aphernix.registry;

import com.ege.aphernix.AphernixMod;
import com.ege.aphernix.entity.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, AphernixMod.MOD_ID);

    private static <T extends AphernixEntity> RegistryObject<EntityType<T>> humanoid(String name, EntityType.EntityFactory<T> factory) {
        return ENTITY_TYPES.register(name, () -> EntityType.Builder.of(factory, MobCategory.CREATURE)
                .sized(0.6F, 1.8F).clientTrackingRange(8).build(ENTITY_TYPES.key(name)));
    }

    public static final RegistryObject<EntityType<AphernixEntity>> APHERNIX = humanoid("aphernix", AphernixEntity::new);
    public static final RegistryObject<EntityType<AphernixTransformedEntity>> APHERNIX_TRANSFORMED = humanoid("aphernix_transformed", AphernixTransformedEntity::new);
    public static final RegistryObject<EntityType<AphernixOldEntity>> APHERNIX_OLD = humanoid("aphernix_old", AphernixOldEntity::new);
    public static final RegistryObject<EntityType<YusufteEntity>> YUSUFTE = humanoid("yusufte", YusufteEntity::new);
    public static final RegistryObject<EntityType<YusufteDiamondEntity>> YUSUFTE_DIAMOND = humanoid("yusufte_diamond", YusufteDiamondEntity::new);
    public static final RegistryObject<EntityType<YusufteOldEntity>> YUSUFTE_OLD = humanoid("yusufte_old", YusufteOldEntity::new);
    public static final RegistryObject<EntityType<OsmanTusEntity>> OSMAN_TUS = humanoid("osman_tus", OsmanTusEntity::new);
    public static final RegistryObject<EntityType<HelloTusEntity>> HELLO_TUS = humanoid("hello_tus", HelloTusEntity::new);
    public static final RegistryObject<EntityType<KazimUstaEntity>> KAZIM_USTA = humanoid("kazim_usta", KazimUstaEntity::new);

    public static final RegistryObject<EntityType<YusufteChickenEntity>> YUSUFTE_CHICKEN = ENTITY_TYPES.register("yusufte_chicken", () ->
            EntityType.Builder.of(YusufteChickenEntity::new, MobCategory.CREATURE)
                    .sized(0.5F, 0.9F).clientTrackingRange(10).build(ENTITY_TYPES.key("yusufte_chicken")));

    private ModEntities() {}
}

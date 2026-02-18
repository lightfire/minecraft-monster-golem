package com.kadirergun.monstergolem;

import com.kadirergun.monstergolem.entity.MonsterGolemEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Centralized entity registration for the mod. Exposes a DeferredRegister and
 * RegistryObjects for custom entity types.
 */
public class ModEntities {

    /** Deferred register for entity types. */
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MonsterGolemMod.MODID);

    /**
     * The Monster Golem entity type RegistryObject. This is registered with
     * specific size parameters and a factory for creating instances.
     */
    public static final RegistryObject<EntityType<MonsterGolemEntity>> MONSTER_GOLEM =
            ENTITIES.register("monster_golem",
                    () -> EntityType.Builder.of(MonsterGolemEntity::new, MobCategory.MISC)
                            .sized(1.4f, 2.9f)
                            .build("monster_golem"));
}

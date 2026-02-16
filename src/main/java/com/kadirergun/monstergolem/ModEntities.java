package com.kadirergun.monstergolem;

import com.kadirergun.monstergolem.entity.MonsterGolemEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MonsterGolemMod.MODID);

    public static final RegistryObject<EntityType<MonsterGolemEntity>> MONSTER_GOLEM =
            ENTITIES.register("monster_golem",
                    () -> EntityType.Builder.of(MonsterGolemEntity::new, MobCategory.MISC)
                            .sized(1.4f, 2.9f)
                            .build("monster_golem"));
}

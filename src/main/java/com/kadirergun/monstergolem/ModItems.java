package com.kadirergun.monstergolem;

import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Holds DeferredRegister instances and RegistryObjects for mod items.
 * This class centralizes item registration for the mod and exposes
 * RegistryObject references for use elsewhere in the codebase.
 */
public class ModItems {

    /** Deferred register for items associated with this mod. */
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MonsterGolemMod.MODID);

    /**
     * Spawn egg item for the Monster Golem entity. The RegistryObject allows
     * lazy access to the registered Item instance.
     */
    public static final RegistryObject<Item> MONSTER_GOLEM_SPAWN_EGG =
            ITEMS.register("monster_golem_spawn_egg",
                    () -> new ForgeSpawnEggItem(
                            ModEntities.MONSTER_GOLEM, // RegistryObject provided as supplier
                            0x2F2F2F,
                            0xFF0000,
                            new Item.Properties()
                    )
            );
}

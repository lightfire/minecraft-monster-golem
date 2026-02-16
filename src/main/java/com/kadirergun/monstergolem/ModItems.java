package com.kadirergun.monstergolem;

import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MonsterGolemMod.MODID);

    public static final RegistryObject<Item> MONSTER_GOLEM_SPAWN_EGG =
            ITEMS.register("monster_golem_spawn_egg",
                    () -> new ForgeSpawnEggItem(
                            ModEntities.MONSTER_GOLEM, // ✅ RegistryObject verilir, supplier gibi davranır
                            0x2F2F2F,
                            0xFF0000,
                            new Item.Properties()
                    )
            );
}

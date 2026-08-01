package com.ege.aphernix.registry;

import com.ege.aphernix.AphernixMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AphernixMod.MOD_ID);
    public static final RegistryObject<Item> APHERNIX_SPAWN_EGG = ITEMS.register("aphernix_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.APHERNIX, 0x241338, 0xC43CFF, new Item.Properties()));
    private ModItems() {}
    public static void register(IEventBus eventBus) { ITEMS.register(eventBus); }
}

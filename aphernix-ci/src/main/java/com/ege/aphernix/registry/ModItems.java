package com.ege.aphernix.registry;

import com.ege.aphernix.AphernixMod;
import com.ege.aphernix.item.EnderBiteItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AphernixMod.MOD_ID);

    public static final RegistryObject<Item> APHERNIX_SPAWN_EGG = ITEMS.register(
            "aphernix_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.APHERNIX, 0x241338, 0xC43CFF, new Item.Properties())
    );

    public static final RegistryObject<Item> ENDER_EYE_DUST = ITEMS.register(
            "ender_eye_dust",
            () -> new Item(new Item.Properties())
    );

    public static final RegistryObject<Item> ENDER_BITE = ITEMS.register(
            "ender_bite",
            () -> new EnderBiteItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE))
    );

    private ModItems() {}

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}

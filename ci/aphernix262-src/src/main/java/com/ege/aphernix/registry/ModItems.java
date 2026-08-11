package com.ege.aphernix.registry;

import com.ege.aphernix.AphernixMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AphernixMod.MOD_ID);

    public static final RegistryObject<Item> ENDER_BITE = ITEMS.register("ender_bite",
            () -> new Item(new Item.Properties().setId(ITEMS.key("ender_bite"))));

    private ModItems() {}
}

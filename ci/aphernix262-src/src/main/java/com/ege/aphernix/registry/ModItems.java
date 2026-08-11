package com.ege.aphernix.registry;

import com.ege.aphernix.AphernixMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AphernixMod.MOD_ID);

    private static RegistryObject<Item> simple(String name) {
        return ITEMS.register(name, () -> new Item(new Item.Properties().setId(ITEMS.key(name))));
    }

    public static final RegistryObject<Item> ENDER_BITE = simple("ender_bite");
    public static final RegistryObject<Item> ENDER_EYE_DUST = simple("ender_eye_dust");
    public static final RegistryObject<Item> DIAMOND_ENDER_BITE = simple("diamond_ender_bite");
    public static final RegistryObject<Item> OLDS_BITE = simple("olds_bite");

    private ModItems() {}
}

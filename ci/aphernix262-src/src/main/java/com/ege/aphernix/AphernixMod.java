package com.ege.aphernix;

import com.ege.aphernix.registry.ModItems;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(AphernixMod.MOD_ID)
public final class AphernixMod {
    public static final String MOD_ID = "aphernix";

    public AphernixMod(FMLJavaModLoadingContext context) {
        // Forge 26.2 uses the mod BusGroup for deferred registrations.
        ModItems.ITEMS.register(context.getModBusGroup());
    }
}

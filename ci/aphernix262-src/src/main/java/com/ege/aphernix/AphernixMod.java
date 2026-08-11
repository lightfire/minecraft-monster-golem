package com.ege.aphernix;

import com.ege.aphernix.event.InteractionEvents;
import com.ege.aphernix.event.ModEvents;
import com.ege.aphernix.event.SchoolBattleEvents;
import com.ege.aphernix.registry.ModEntities;
import com.ege.aphernix.registry.ModItems;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(AphernixMod.MOD_ID)
public final class AphernixMod {
    public static final String MOD_ID = "aphernix";

    public AphernixMod(FMLJavaModLoadingContext context) {
        var modBusGroup = context.getModBusGroup();
        ModItems.ITEMS.register(modBusGroup);
        ModEntities.ENTITY_TYPES.register(modBusGroup);

        EntityAttributeCreationEvent.BUS.addListener(ModEvents::createAttributes);
        PlayerInteractEvent.EntityInteractSpecific.BUS.addListener(InteractionEvents::onEntityInteract);
        PlayerInteractEvent.RightClickBlock.BUS.addListener(SchoolBattleEvents::onRightClickBlock);
        LivingDeathEvent.BUS.addListener(SchoolBattleEvents::onDeath);
    }
}

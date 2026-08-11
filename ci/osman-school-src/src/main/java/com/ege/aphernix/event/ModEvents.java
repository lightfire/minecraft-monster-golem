package com.ege.aphernix.event;

import com.ege.aphernix.AphernixMod;
import com.ege.aphernix.entity.AphernixEntity;
import com.ege.aphernix.entity.YusufteChickenEntity;
import com.ege.aphernix.registry.ModEntities;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AphernixMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModEvents {
    private ModEvents() {}

    @SubscribeEvent
    public static void createAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.APHERNIX.get(), AphernixEntity.createAttributes().build());
        event.put(ModEntities.APHERNIX_TRANSFORMED.get(), AphernixEntity.createAttributes().build());
        event.put(ModEntities.APHERNIX_OLD.get(), AphernixEntity.createAttributes().build());
        event.put(ModEntities.YUSUFTE.get(), AphernixEntity.createAttributes().build());
        event.put(ModEntities.YUSUFTE_CHICKEN.get(), YusufteChickenEntity.createAttributes().build());
        event.put(ModEntities.YUSUFTE_DIAMOND.get(), AphernixEntity.createAttributes().build());
        event.put(ModEntities.YUSUFTE_OLD.get(), AphernixEntity.createAttributes().build());
        event.put(ModEntities.OSMAN_TUS.get(), AphernixEntity.createAttributes().build());
        event.put(ModEntities.HELLO_TUS.get(), AphernixEntity.createAttributes().build());
        event.put(ModEntities.KAZIM_USTA.get(), AphernixEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
        event.register(ModEntities.APHERNIX.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules,
                SpawnPlacementRegisterEvent.Operation.REPLACE);
    }
}

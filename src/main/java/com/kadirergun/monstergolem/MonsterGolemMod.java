package com.kadirergun.monstergolem;

import com.kadirergun.monstergolem.entity.MonsterGolemEntity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;

/**
 * Main mod entry point for the MonsterGolem mod. Responsible for registering
 * deferred registries and hooking into mod lifecycle events.
 *
 * This class is instantiated by the Forge mod loader during startup.
 */
@Mod(MonsterGolemMod.MODID)
public class MonsterGolemMod {

    public static final String MODID = "customgolem";

    /**
     * Mod constructor. Registers entity and item registries and attaches
     * the entity attribute creation listener to the mod event bus.
     */
    @SuppressWarnings({"deprecation", "removal"})
    public MonsterGolemMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModEntities.ENTITIES.register(bus);
        ModItems.ITEMS.register(bus);
        bus.addListener(this::onEntityAttributes);
    }

    /**
     * Handles the EntityAttributeCreationEvent and registers attribute
     * suppliers for custom entities.
     *
     * @param event the EntityAttributeCreationEvent fired by the mod event bus
     */
    private void onEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.MONSTER_GOLEM.get(), MonsterGolemEntity.createAttributes().build());
    }


}
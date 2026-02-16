package com.kadirergun.monstergolem;

import com.kadirergun.monstergolem.entity.MonsterGolemEntity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;

@Mod(MonsterGolemMod.MODID)
public class MonsterGolemMod {

    public static final String MODID = "customgolem";

    public MonsterGolemMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModEntities.ENTITIES.register(bus);
        ModItems.ITEMS.register(bus);
        bus.addListener(this::onEntityAttributes);
    }

    private void onEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.MONSTER_GOLEM.get(), MonsterGolemEntity.createAttributes().build());
    }


}
package com.kadirergun.monstergolem;

import com.kadirergun.monstergolem.entity.MonsterGolemEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Forge event handlers for gameplay events. This class listens on the FORGE
 * event bus and implements behavior that reacts to world events (e.g. entity death).
 */
@Mod.EventBusSubscriber(modid = MonsterGolemMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {

    /**
     * Listens for living entity death events. If a MonsterGolem instance dealt the
     * killing blow, award it XP. Runs on the server side.
     *
     * @param event the LivingDeathEvent containing information about the death
     */
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof MonsterGolemEntity golem && !golem.level().isClientSide) {
            golem.addXP(5);
        }
    }
}
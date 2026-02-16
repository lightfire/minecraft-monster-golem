package com.kadirergun.monstergolem;

import com.kadirergun.monstergolem.entity.MonsterGolemEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MonsterGolemMod.MODID)
public class ModEvents {

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        LivingEntity victim = event.getEntity();
        DamageSource source = event.getSource();

        if (source.getEntity() instanceof MonsterGolemEntity golem) {
            golem.addXP(5); // public yapacağız
        }
    }
}
package com.ege.aphernix.event;

import com.ege.aphernix.AphernixMod;
import com.ege.aphernix.entity.AphernixEntity;
import com.ege.aphernix.entity.AphernixOldEntity;
import com.ege.aphernix.entity.AphernixTransformedEntity;
import com.ege.aphernix.entity.YusufteDiamondEntity;
import com.ege.aphernix.entity.YusufteEntity;
import com.ege.aphernix.entity.YusufteOldEntity;
import com.ege.aphernix.registry.ModEntities;
import com.ege.aphernix.registry.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AphernixMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class AphernixInteractionEvents {
    private AphernixInteractionEvents() {}

    @SubscribeEvent
    public static void transformAphernix(PlayerInteractEvent.EntityInteract event) {
        if (!(event.getTarget() instanceof AphernixEntity source)) {
            return;
        }

        Player player = event.getEntity();
        ItemStack heldItem = player.getItemInHand(event.getHand());
        boolean diamondBite = heldItem.is(ModItems.DIAMOND_ENDER_BITE.get());
        boolean oldsBite = heldItem.is(ModItems.OLDS_BITE.get());
        if (!diamondBite && !oldsBite) {
            return;
        }

        boolean isYusufte = source instanceof YusufteEntity;
        if (isYusufte) {
            if (diamondBite && source instanceof YusufteDiamondEntity) return;
            if (oldsBite && source instanceof YusufteOldEntity) return;
        } else {
            if (diamondBite && source instanceof AphernixTransformedEntity) return;
            if (oldsBite && source instanceof AphernixOldEntity) return;
        }

        event.setCancellationResult(InteractionResult.sidedSuccess(player.level().isClientSide));
        event.setCanceled(true);
        if (!(player.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        AphernixEntity replacement;
        if (isYusufte) {
            replacement = diamondBite
                    ? ModEntities.YUSUFTE_DIAMOND.get().create(serverLevel)
                    : ModEntities.YUSUFTE_OLD.get().create(serverLevel);
        } else {
            replacement = diamondBite
                    ? ModEntities.APHERNIX_TRANSFORMED.get().create(serverLevel)
                    : ModEntities.APHERNIX_OLD.get().create(serverLevel);
        }

        if (replacement == null) {
            return;
        }

        copyAphernixData(source, replacement);
        serverLevel.addFreshEntity(replacement);

        if (diamondBite) {
            serverLevel.sendParticles(
                    ParticleTypes.REVERSE_PORTAL,
                    replacement.getX(), replacement.getY() + 1.0D, replacement.getZ(),
                    90, 0.7D, 1.0D, 0.7D, 0.2D
            );
            serverLevel.sendParticles(
                    ParticleTypes.ELECTRIC_SPARK,
                    replacement.getX(), replacement.getY() + 1.0D, replacement.getZ(),
                    45, 0.55D, 0.8D, 0.55D, 0.12D
            );
            serverLevel.playSound(null, replacement.blockPosition(), SoundEvents.TOTEM_USE,
                    SoundSource.PLAYERS, 1.0F, 1.15F);
        } else {
            serverLevel.sendParticles(
                    ParticleTypes.SMOKE,
                    replacement.getX(), replacement.getY() + 1.0D, replacement.getZ(),
                    65, 0.6D, 0.9D, 0.6D, 0.08D
            );
            serverLevel.sendParticles(
                    ParticleTypes.PORTAL,
                    replacement.getX(), replacement.getY() + 1.0D, replacement.getZ(),
                    45, 0.5D, 0.8D, 0.5D, 0.12D
            );
            serverLevel.playSound(null, replacement.blockPosition(), SoundEvents.ZOMBIE_VILLAGER_CURE,
                    SoundSource.PLAYERS, 0.8F, 0.75F);
        }

        source.discard();
        if (!player.getAbilities().instabuild) {
            heldItem.shrink(1);
        }
        player.displayClientMessage(Component.translatable(
                diamondBite ? "message.aphernix.transformed" : "message.aphernix.transformed_old"), true);
        player.swing(event.getHand(), true);
    }

    private static void copyAphernixData(AphernixEntity source, AphernixEntity target) {
        target.moveTo(source.getX(), source.getY(), source.getZ(), source.getYRot(), source.getXRot());
        target.setYBodyRot(source.yBodyRot);
        target.setYHeadRot(source.getYHeadRot());
        target.setHealth(Math.min(target.getMaxHealth(), source.getHealth()));
        target.setDeltaMovement(source.getDeltaMovement());
        target.fallDistance = source.fallDistance;

        if (source.hasCustomName()) {
            target.setCustomName(source.getCustomName());
            target.setCustomNameVisible(source.isCustomNameVisible());
        }

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack equipped = source.getItemBySlot(slot);
            if (!equipped.isEmpty()) {
                target.setItemSlot(slot, equipped.copy());
            }
        }

        target.setPersistenceRequired();
        target.stopUsingItem();
        target.setTarget(null);
        target.getNavigation().stop();
    }
}

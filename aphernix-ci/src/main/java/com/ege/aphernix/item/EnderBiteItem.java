package com.ege.aphernix.item;

import com.ege.aphernix.entity.AphernixEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Comparator;
import java.util.List;

public class EnderBiteItem extends Item {
    private static final double SEARCH_RANGE = 512.0D;

    public EnderBiteItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!(level instanceof ServerLevel serverLevel)) {
            return InteractionResultHolder.sidedSuccess(stack, true);
        }

        List<AphernixEntity> aphernixes = serverLevel.getEntitiesOfClass(
                AphernixEntity.class,
                player.getBoundingBox().inflate(SEARCH_RANGE),
                AphernixEntity::isAlive
        );

        AphernixEntity aphernix = aphernixes.stream()
                .min(Comparator.comparingDouble(player::distanceToSqr))
                .orElse(null);

        if (aphernix == null) {
            player.displayClientMessage(Component.translatable("message.aphernix.no_aphernix"), true);
            player.getCooldowns().addCooldown(this, 20);
            return InteractionResultHolder.fail(stack);
        }

        double oldX = aphernix.getX();
        double oldY = aphernix.getY() + 1.0D;
        double oldZ = aphernix.getZ();

        double angle = Math.toRadians(player.getYRot() + 90.0F);
        double targetX = player.getX() + Math.cos(angle) * 1.8D;
        double targetY = player.getY();
        double targetZ = player.getZ() + Math.sin(angle) * 1.8D;

        aphernix.stopUsingItem();
        aphernix.setTarget(null);
        aphernix.getNavigation().stop();
        aphernix.teleportTo(targetX, targetY, targetZ);
        aphernix.setDeltaMovement(0.0D, 0.0D, 0.0D);
        aphernix.fallDistance = 0.0F;

        serverLevel.sendParticles(ParticleTypes.PORTAL, oldX, oldY, oldZ, 45, 0.45D, 0.8D, 0.45D, 0.18D);
        serverLevel.sendParticles(ParticleTypes.PORTAL, aphernix.getX(), aphernix.getY() + 1.0D, aphernix.getZ(), 60, 0.55D, 0.9D, 0.55D, 0.22D);
        serverLevel.playSound(null, oldX, oldY, oldZ, SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
        serverLevel.playSound(null, aphernix.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.15F);

        player.getCooldowns().addCooldown(this, 60);
        player.displayClientMessage(Component.translatable("message.aphernix.called"), true);
        return InteractionResultHolder.success(stack);
    }
}

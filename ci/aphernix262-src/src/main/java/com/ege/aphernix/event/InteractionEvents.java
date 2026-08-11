package com.ege.aphernix.event;

import com.ege.aphernix.entity.*;
import com.ege.aphernix.registry.ModEntities;
import com.ege.aphernix.registry.ModItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public final class InteractionEvents {
    private InteractionEvents() {}

    public static boolean onEntityInteract(PlayerInteractEvent.EntityInteractSpecific event) {
        Entity target = event.getTarget();
        Player player = event.getEntity();
        ItemStack held = player.getItemInHand(event.getHand());

        if (!(player.level() instanceof ServerLevel level)) return false;

        if (target.getType() == ModEntities.YUSUFTE.get() && isSeed(held)) {
            YusufteChickenEntity chicken = new YusufteChickenEntity(ModEntities.YUSUFTE_CHICKEN.get(), level);
            replace((LivingEntity) target, chicken, level);
            consume(player, held);
            event.setCancellationResult(InteractionResult.SUCCESS);
            return true;
        }

        if (target.getType() == ModEntities.APHERNIX.get()) {
            if (held.is(ModItems.DIAMOND_ENDER_BITE.get())) {
                replace((LivingEntity) target, new AphernixTransformedEntity(ModEntities.APHERNIX_TRANSFORMED.get(), level), level);
            } else if (held.is(ModItems.OLDS_BITE.get())) {
                replace((LivingEntity) target, new AphernixOldEntity(ModEntities.APHERNIX_OLD.get(), level), level);
            } else return false;
            consume(player, held);
            event.setCancellationResult(InteractionResult.SUCCESS);
            return true;
        }

        if (target.getType() == ModEntities.YUSUFTE.get()) {
            if (held.is(ModItems.DIAMOND_ENDER_BITE.get())) {
                replace((LivingEntity) target, new YusufteDiamondEntity(ModEntities.YUSUFTE_DIAMOND.get(), level), level);
            } else if (held.is(ModItems.OLDS_BITE.get())) {
                replace((LivingEntity) target, new YusufteOldEntity(ModEntities.YUSUFTE_OLD.get(), level), level);
            } else return false;
            consume(player, held);
            event.setCancellationResult(InteractionResult.SUCCESS);
            return true;
        }

        return false;
    }

    private static boolean isSeed(ItemStack stack) {
        return stack.is(Items.WHEAT_SEEDS) || stack.is(Items.MELON_SEEDS) || stack.is(Items.PUMPKIN_SEEDS) || stack.is(Items.BEETROOT_SEEDS);
    }

    private static void replace(LivingEntity source, LivingEntity replacement, ServerLevel level) {
        replacement.absSnapTo(source.getX(), source.getY(), source.getZ(), source.getYRot(), source.getXRot());
        replacement.setCustomName(source.getCustomName());
        replacement.setCustomNameVisible(source.isCustomNameVisible());
        level.addFreshEntity(replacement);
        source.discard();
    }

    private static void consume(Player player, ItemStack stack) {
        if (!player.getAbilities().instabuild) stack.shrink(1);
    }
}

package com.ege.aphernix.event;

import com.ege.aphernix.AphernixMod;
import com.ege.aphernix.entity.AphernixEntity;
import com.ege.aphernix.entity.HelloTusEntity;
import com.ege.aphernix.entity.KazimUstaEntity;
import com.ege.aphernix.entity.OsmanTusEntity;
import com.ege.aphernix.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Mod.EventBusSubscriber(modid = AphernixMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class SchoolBattleEvents {
    private static final String SCHOOL_STARTED = "aphernix_school_battle_started";

    private SchoolBattleEvents() {}

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.level instanceof ServerLevel level)) {
            return;
        }

        long gameTime = level.getGameTime();
        if (gameTime % 20L == 0L) {
            for (ServerPlayer player : level.players()) {
                checkNearbySchoolSigns(level, player);
            }
        }

        if (gameTime % 10L == 0L) {
            keepBattleTargets(level);
        }
    }

    private static void checkNearbySchoolSigns(ServerLevel level, ServerPlayer player) {
        BlockPos center = player.blockPosition();
        for (BlockPos mutablePos : BlockPos.betweenClosed(center.offset(-8, -4, -8), center.offset(8, 4, 8))) {
            BlockPos pos = mutablePos.immutable();
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (!(blockEntity instanceof SignBlockEntity sign) || !isGreenSchoolSign(sign)) {
                continue;
            }
            if (sign.getPersistentData().getBoolean(SCHOOL_STARTED)) {
                continue;
            }

            sign.getPersistentData().putBoolean(SCHOOL_STARTED, true);
            sign.setChanged();
            startSchoolBattle(level, pos, player);
            return;
        }
    }

    private static boolean isGreenSchoolSign(SignBlockEntity sign) {
        return isGreenSchoolText(sign.getFrontText()) || isGreenSchoolText(sign.getBackText());
    }

    private static boolean isGreenSchoolText(SignText text) {
        if (text.getColor() != DyeColor.GREEN) {
            return false;
        }
        for (int line = 0; line < 4; line++) {
            String value = text.getMessage(line, false).getString().toLowerCase(Locale.ROOT).trim();
            if (value.contains("okul")) {
                return true;
            }
        }
        return false;
    }

    private static void startSchoolBattle(ServerLevel level, BlockPos signPos, ServerPlayer player) {
        OsmanTusEntity osman = ModEntities.OSMAN_TUS.get().create(level);
        AphernixEntity aphernix = ModEntities.APHERNIX.get().create(level);
        AphernixEntity yusufte = ModEntities.YUSUFTE.get().create(level);
        if (osman == null || aphernix == null || yusufte == null) {
            return;
        }

        prepare(osman, signPos.offset(0, 1, 3), "Osman Tuş");
        prepare(aphernix, signPos.offset(3, 1, 0), "Aphernix");
        prepare(yusufte, signPos.offset(-3, 1, 0), "Yusufte");

        level.addFreshEntity(osman);
        level.addFreshEntity(aphernix);
        level.addFreshEntity(yusufte);

        List<AbstractMinecart> carts = new ArrayList<>(level.getEntitiesOfClass(
                AbstractMinecart.class,
                new AABB(signPos).inflate(16.0D, 8.0D, 16.0D),
                cart -> !cart.isVehicle()
        ));

        // Sadece Yusufte ve Aphernix vagona oturur. Osman Tuş ayakta kalıp savaşır.
        seatInNearestCart(aphernix, carts);
        seatInNearestCart(yusufte, carts);

        osman.setTarget(closer(osman, aphernix, yusufte));
        aphernix.setTarget(osman);
        yusufte.setTarget(osman);

        player.displayClientMessage(Component.literal("Okul savaşı başladı!"), false);
    }

    private static void prepare(Mob mob, BlockPos pos, String name) {
        mob.moveTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 0.0F, 0.0F);
        mob.setCustomName(Component.literal(name));
        mob.setCustomNameVisible(true);
        mob.setPersistenceRequired();
    }

    private static void seatInNearestCart(Mob mob, List<AbstractMinecart> carts) {
        AbstractMinecart nearest = carts.stream()
                .filter(cart -> !cart.isVehicle())
                .min(Comparator.comparingDouble(mob::distanceToSqr))
                .orElse(null);
        if (nearest != null && mob.startRiding(nearest, true)) {
            carts.remove(nearest);
        }
    }

    private static LivingEntity closer(Mob source, LivingEntity first, LivingEntity second) {
        return source.distanceToSqr(first) <= source.distanceToSqr(second) ? first : second;
    }

    private static void keepBattleTargets(ServerLevel level) {
        for (ServerPlayer player : level.players()) {
            AABB area = player.getBoundingBox().inflate(64.0D);
            for (OsmanTusEntity osman : level.getEntitiesOfClass(OsmanTusEntity.class, area)) {
                targetNearestOpponent(level, osman);
            }
            for (HelloTusEntity hello : level.getEntitiesOfClass(HelloTusEntity.class, area)) {
                targetNearestOpponent(level, hello);
            }
        }
    }

    private static void targetNearestOpponent(ServerLevel level, Mob fighter) {
        AphernixEntity opponent = findOpponent(level, fighter);
        if (opponent != null) {
            fighter.setTarget(opponent);
            opponent.setTarget(fighter);
        }
    }

    private static AphernixEntity findOpponent(ServerLevel level, Mob fighter) {
        return level.getEntitiesOfClass(
                        AphernixEntity.class,
                        fighter.getBoundingBox().inflate(32.0D),
                        entity -> entity.isAlive()
                                && entity != fighter
                                && !(entity instanceof OsmanTusEntity)
                                && !(entity instanceof HelloTusEntity)
                                && !(entity instanceof KazimUstaEntity))
                .stream()
                .min(Comparator.comparingDouble(fighter::distanceToSqr))
                .orElse(null);
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (!(event.getEntity().level() instanceof ServerLevel level)) {
            return;
        }

        if (event.getEntity() instanceof OsmanTusEntity osman) {
            HelloTusEntity hello = ModEntities.HELLO_TUS.get().create(level);
            if (hello == null) {
                return;
            }
            prepare(hello, osman.blockPosition(), "Helllo Tuş");
            hello.setYRot(osman.getYRot());
            level.addFreshEntity(hello);
            targetNearestOpponent(level, hello);
            level.getServer().getPlayerList().broadcastSystemMessage(
                    Component.literal("Osman Tuş yenildi! Helllo Tuş geldi!"), false);
        } else if (event.getEntity() instanceof HelloTusEntity hello) {
            KazimUstaEntity kazim = ModEntities.KAZIM_USTA.get().create(level);
            if (kazim == null) {
                return;
            }
            prepare(kazim, hello.blockPosition(), "Bıyıklı Kazım Usta");
            kazim.setYRot(hello.getYRot());
            level.addFreshEntity(kazim);
            level.getServer().getPlayerList().broadcastSystemMessage(
                    Component.literal("Helllo Tuş yenildi! Bıyıklı Kazım Usta oldu."), false);
        }
    }
}

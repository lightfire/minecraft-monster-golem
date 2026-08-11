package com.ege.aphernix.event;

import com.ege.aphernix.entity.*;
import com.ege.aphernix.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public final class SchoolBattleEvents {
    private static final Set<Long> STARTED_SIGNS = new HashSet<>();
    private SchoolBattleEvents() {}

    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        BlockPos pos = event.getPos();
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof SignBlockEntity sign) || !isGreenSchoolSign(sign)) return;
        if (!STARTED_SIGNS.add(pos.asLong())) return;
        startSchoolBattle(level, pos);
    }

    private static boolean isGreenSchoolSign(SignBlockEntity sign) {
        return isGreenSchoolText(sign.getFrontText()) || isGreenSchoolText(sign.getBackText());
    }

    private static boolean isGreenSchoolText(SignText text) {
        if (text.getColor() != DyeColor.GREEN) return false;
        for (int line = 0; line < 4; line++) {
            String value = text.getMessage(line, false).getString().toLowerCase(Locale.ROOT).trim();
            if (value.contains("okul")) return true;
        }
        return false;
    }

    private static void startSchoolBattle(ServerLevel level, BlockPos signPos) {
        OsmanTusEntity osman = new OsmanTusEntity(ModEntities.OSMAN_TUS.get(), level);
        AphernixEntity aphernix = new AphernixEntity(ModEntities.APHERNIX.get(), level);
        YusufteEntity yusufte = new YusufteEntity(ModEntities.YUSUFTE.get(), level);
        prepare(osman, signPos.offset(0, 1, 3), "Osman Tuş");
        prepare(aphernix, signPos.offset(3, 1, 0), "Aphernix");
        prepare(yusufte, signPos.offset(-3, 1, 0), "Yusufte");
        level.addFreshEntity(osman);
        level.addFreshEntity(aphernix);
        level.addFreshEntity(yusufte);

        List<AbstractMinecart> carts = new ArrayList<>(level.getEntitiesOfClass(AbstractMinecart.class,
                new AABB(signPos).inflate(16.0D, 8.0D, 16.0D), cart -> !cart.isVehicle()));
        seat(aphernix, carts);
        seat(yusufte, carts);
        aphernix.setSchoolRideTicks(60);
        yusufte.setSchoolRideTicks(60);

        osman.setTarget(closer(osman, aphernix, yusufte));
        aphernix.setTarget(osman);
        yusufte.setTarget(osman);
    }

    private static void prepare(Mob mob, BlockPos pos, String name) {
        mob.absSnapTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 0.0F, 0.0F);
        mob.setCustomName(Component.literal(name));
        mob.setCustomNameVisible(true);
        mob.setPersistenceRequired();
    }

    private static void seat(AphernixEntity mob, List<AbstractMinecart> carts) {
        AbstractMinecart nearest = carts.stream().filter(c -> !c.isVehicle())
                .min(Comparator.comparingDouble(mob::distanceToSqr)).orElse(null);
        if (nearest != null && mob.startRiding(nearest)) carts.remove(nearest);
    }

    private static AphernixEntity closer(Mob source, AphernixEntity a, AphernixEntity b) {
        return source.distanceToSqr(a) <= source.distanceToSqr(b) ? a : b;
    }

    public static void onDeath(LivingDeathEvent event) {
        if (!(event.getEntity().level() instanceof ServerLevel level)) return;
        if (event.getEntity() instanceof OsmanTusEntity osman) {
            HelloTusEntity hello = new HelloTusEntity(ModEntities.HELLO_TUS.get(), level);
            prepare(hello, osman.blockPosition(), "Helllo Tuş");
            level.addFreshEntity(hello);
            AphernixEntity opponent = nearestOpponent(level, hello);
            if (opponent != null) { hello.setTarget(opponent); opponent.setTarget(hello); }
        } else if (event.getEntity() instanceof HelloTusEntity hello) {
            KazimUstaEntity kazim = new KazimUstaEntity(ModEntities.KAZIM_USTA.get(), level);
            prepare(kazim, hello.blockPosition(), "Bıyıklı Kazım Usta");
            level.addFreshEntity(kazim);
        }
    }

    private static AphernixEntity nearestOpponent(ServerLevel level, Mob fighter) {
        return level.getEntitiesOfClass(AphernixEntity.class, fighter.getBoundingBox().inflate(32.0D),
                e -> e.isAlive() && !(e instanceof OsmanTusEntity) && !(e instanceof HelloTusEntity) && !(e instanceof KazimUstaEntity))
                .stream().min(Comparator.comparingDouble(fighter::distanceToSqr)).orElse(null);
    }
}

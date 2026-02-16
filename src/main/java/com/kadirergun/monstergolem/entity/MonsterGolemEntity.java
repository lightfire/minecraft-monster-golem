package com.kadirergun.monstergolem.entity;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.world.BossEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MonsterGolemEntity extends IronGolem {

    private final ServerBossEvent bossBar =
            new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.GREEN, BossEvent.BossBarOverlay.PROGRESS);

    private int fireCooldown = 0;
    private int snowCooldown = 0;

    private int level = 1;
    private int xp = 0;

    public MonsterGolemEntity(EntityType<? extends IronGolem> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return IronGolem.createAttributes()
                .add(Attributes.MAX_HEALTH, 300.0D)
                .add(Attributes.ATTACK_DAMAGE, 35.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.30D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.FOLLOW_RANGE, 48.0D); // 🔥 algılama menzili;
    }



    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.25D, true));

        // Boşta gezerken etrafa bakıp devriye atsın
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 12.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        // 🔥 Daha geniş menzilden hedef bulsun
        this.targetSelector.addGoal(1,
                new NearestAttackableTargetGoal<>(this, Monster.class, 48, true, true, e -> true));
        this.targetSelector.addGoal(2,
                new NearestAttackableTargetGoal<>(this, Slime.class, 48, true, true, e -> true));
        this.targetSelector.addGoal(3,
                new NearestAttackableTargetGoal<>(this, Creeper.class, 64, true, true, e -> true));
    }

    @Override
    public boolean isAlliedTo(Entity entity) {
        return entity instanceof Player || super.isAlliedTo(entity);
    }

    @Override
    public boolean canAttackType(EntityType<?> type) {
        if (type == EntityType.CREEPER) return true;
        return super.canAttackType(type);
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        float base = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        if (target instanceof Creeper) base *= 2.5f;
        return target.hurt(this.damageSources().mobAttack(this), base);
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        bossBar.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        bossBar.removePlayer(player);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) {
            // 🔁 Hedef yolunu daha sık güncellesin (1 saniyede 1)
            if (this.tickCount % 20 == 0) {
                this.getNavigation().recomputePath();
            }
            bossBar.setName(this.getDisplayName());
            bossBar.setProgress(this.getHealth() / this.getMaxHealth());

            LivingEntity target = this.getTarget();
            if (target != null) {
                if (fireCooldown <= 0 && this.distanceTo(target) < 20) {
                    shootFireball(target);
                    fireCooldown = 80;
                } else {
                    fireCooldown--;
                }

                if (snowCooldown <= 0 && this.distanceTo(target) < 15) {
                    shootSnowball(target);
                    snowCooldown = 40;
                } else {
                    snowCooldown--;
                }
            }
        }
    }

    private void shootFireball(LivingEntity target) {
        LargeFireball fireball = new LargeFireball(level(), this,
                target.getX() - this.getX(),
                target.getY(0.5) - this.getY(0.5),
                target.getZ() - this.getZ(), 2);
        fireball.setPos(this.getX(), this.getY(1.5), this.getZ());
        level().addFreshEntity(fireball);
    }

    private void shootSnowball(LivingEntity target) {
        Snowball snowball = new Snowball(level(), this);
        double dx = target.getX() - this.getX();
        double dy = target.getEyeY() - this.getEyeY();
        double dz = target.getZ() - this.getZ();
        snowball.shoot(dx, dy, dz, 1.6F, 0);
        level().addFreshEntity(snowball);
    }

    @Override
    public Component getName() {
        return Component.literal("Ege'nin Süper Koruyucu Golemi [Lv." + level + "]");
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (stack.getItem() instanceof ArmorItem armor) {
            double bonus = 0;

            if (armor.getMaterial() == net.minecraft.world.item.ArmorMaterials.LEATHER) {
                bonus = 2;
            } else if (armor.getMaterial() == net.minecraft.world.item.ArmorMaterials.CHAIN) {
                bonus = 4;
            } else if (armor.getMaterial() == net.minecraft.world.item.ArmorMaterials.IRON) {
                bonus = 6;
            } else if (armor.getMaterial() == net.minecraft.world.item.ArmorMaterials.GOLD) {
                bonus = 5;
            } else if (armor.getMaterial() == net.minecraft.world.item.ArmorMaterials.DIAMOND) {
                bonus = 10;
            } else if (armor.getMaterial() == net.minecraft.world.item.ArmorMaterials.NETHERITE) {
                bonus = 15;
            }

            if (bonus > 0) {
                Objects.requireNonNull(this.getAttribute(Attributes.ARMOR)).setBaseValue(
                        this.getAttributeValue(Attributes.ARMOR) + bonus
                );

                if (!player.isCreative()) stack.shrink(1);
                return InteractionResult.SUCCESS;
            }
        }

        return super.mobInteract(player, hand);
    }


    public void addXP(int amount) {
        xp += amount;
        if (xp >= this.level * 20) {
            this.level++;
            xp = 0;

            Objects.requireNonNull(this.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(
                    this.getAttributeValue(Attributes.ATTACK_DAMAGE) + 3);
            Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(
                    this.getAttributeValue(Attributes.MAX_HEALTH) + 20);
            this.heal(20);
        }
    }
}

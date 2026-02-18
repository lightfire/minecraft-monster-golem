package com.kadirergun.monstergolem.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightBlock;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * Custom powerful guardian golem entity with:
 * - Boss bar
 * - Level & XP system
 * - Ranged fireball & snowball attacks
 * - Area lighting
 * - Armor interaction
 * - Fire immunity
 */
public class MonsterGolemEntity extends IronGolem {


    private final ServerBossEvent bossBar =
            new ServerBossEvent(Component.literal(""), BossEvent.BossBarColor.GREEN, BossEvent.BossBarOverlay.PROGRESS);

    private int fireCooldown = 0;
    private int snowCooldown = 0;

    private int level = 1;
    private int xp = 0;

    private BlockPos lastLightPos = null;

    /**
     * Constructor for Monster Golem entity.
     */
    public MonsterGolemEntity(EntityType<? extends IronGolem> type, Level level) {
        super(type, level);
    }

    /**
     * Defines base attributes of the Monster Golem.
     */
    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return IronGolem.createAttributes()
                .add(Attributes.MAX_HEALTH, 300.0D)
                .add(Attributes.ATTACK_DAMAGE, 35.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.30D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.FOLLOW_RANGE, 48.0D);
    }

    /**
     * Updates dynamic lighting around the golem.
     */
    @Override
    public void aiStep() {
        super.aiStep();
        if (level().isClientSide) return;

        BlockPos pos = this.blockPosition();

        if (lastLightPos == null || !lastLightPos.equals(pos)) {
            if (lastLightPos != null && level().getBlockState(lastLightPos).is(Blocks.LIGHT)) {
                level().removeBlock(lastLightPos, false);
            }

            if (level().isEmptyBlock(pos)) {
                level().setBlock(pos, Blocks.LIGHT.defaultBlockState()
                        .setValue(LightBlock.LEVEL, 12), 3);
                lastLightPos = pos;
            }
        }
    }

    /**
     * Registers AI goals and target priorities.
     */
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.6D, true));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.9D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 12.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1,
                new NearestAttackableTargetGoal<>(this, Monster.class, 48, true, true,
                        entity -> !(entity instanceof MonsterGolemEntity))
        );
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Slime.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Creeper.class, true));
    }

    /**
     * Prevents friendly fire between Monster Golems and players.
     */
    @Override
    public boolean isAlliedTo(@NotNull Entity entity) {
        if (entity instanceof MonsterGolemEntity) return true;
        return entity instanceof Player || super.isAlliedTo(entity);
    }

    /**
     * Allows targeting Creepers.
     */
    @Override
    public boolean canAttackType(@NotNull EntityType<?> type) {
        if (type == EntityType.CREEPER) return true;
        return super.canAttackType(type);
    }

    /**
     * Deals bonus damage to Creepers.
     */
    @Override
    public boolean doHurtTarget(@NotNull Entity target) {
        float base = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        if (target instanceof Creeper) base *= 2.5f;
        return target.hurt(this.damageSources().mobAttack(this), base);
    }

    /**
     * Adds boss bar when player sees the golem.
     */
    @Override
    public void startSeenByPlayer(@NotNull ServerPlayer player) {
        super.startSeenByPlayer(player);
        bossBar.addPlayer(player);
    }

    /**
     * Removes boss bar when player leaves view.
     */
    @Override
    public void stopSeenByPlayer(@NotNull ServerPlayer player) {
        super.stopSeenByPlayer(player);
        bossBar.removePlayer(player);
    }

    /**
     * Main tick update: handles targeting, attacks and boss bar updates.
     */
    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) {
            if (this.tickCount % 10 == 0) {
                this.getNavigation().recomputePath();
            }

            bossBar.setName(this.getDisplayName());
            bossBar.setProgress(this.getHealth() / this.getMaxHealth());

            LivingEntity target = this.getTarget();
            if (target != null) {
                if (fireCooldown <= 0 && this.distanceTo(target) < 20) {
                    shootFireball(target);
                    fireCooldown = 80;
                } else fireCooldown--;

                if (snowCooldown <= 0 && this.distanceTo(target) < 15) {
                    shootSnowball(target);
                    snowCooldown = 40;
                } else snowCooldown--;
            }
        }
    }

    /**
     * Initializes name, boss bar and navigation settings on spawn.
     */
    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty,
                                        @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnData,
                                        @Nullable CompoundTag dataTag) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
        this.setCustomName(buildDisplayName());
        this.setCustomNameVisible(true);
        this.bossBar.setName(buildDisplayName());
        this.getNavigation().setCanFloat(true);
        this.getNavigation().setSpeedModifier(1.3D);
        return data;
    }

    /**
     * Makes the golem visually hold a torch.
     */
    @Override
    public @NotNull ItemStack getItemBySlot(@NotNull EquipmentSlot slot) {
        if (slot == EquipmentSlot.MAINHAND) {
            return new ItemStack(Items.TORCH);
        }
        return super.getItemBySlot(slot);
    }

    /**
     * Shoots a fireball at the target.
     */
    private void shootFireball(LivingEntity target) {
        LargeFireball fireball = new LargeFireball(level(), this,
                target.getX() - this.getX(),
                target.getY(0.5) - this.getY(0.5),
                target.getZ() - this.getZ(), 2);
        fireball.setPos(this.getX(), this.getY(1.5), this.getZ());
        level().addFreshEntity(fireball);
    }

    /**
     * Shoots a snowball at the target.
     */
    private void shootSnowball(LivingEntity target) {
        Snowball snowball = new Snowball(level(), this);
        snowball.shoot(
                target.getX() - this.getX(),
                target.getEyeY() - this.getEyeY(),
                target.getZ() - this.getZ(),
                1.6F, 0);
        level().addFreshEntity(snowball);
    }

    /**
     * Builds the display name based on current level.
     */
    private Component buildDisplayName() {
        return Component.translatable(
                "entity.customgolem.monster_golem.name_format",
                this.level
        );
    }

    /**
     * Handles armor interaction and stat bonuses.
     */
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

            Objects.requireNonNull(this.getAttribute(Attributes.ARMOR))
                    .setBaseValue(this.getAttributeValue(Attributes.ARMOR) + bonus);

            if (!player.isCreative()) stack.shrink(1);
            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, hand);
    }

    /**
     * Makes the golem immune to fire damage.
     */
    @Override
    public boolean fireImmune() {
        return true;
    }

    /**
     * Saves custom level & XP to NBT.
     */
    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("GuardianLevel", this.level);
        tag.putInt("GuardianXP", this.xp);
    }

    /**
     * Restores custom level & XP from NBT.
     */
    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.level = tag.getInt("GuardianLevel");
        this.xp = tag.getInt("GuardianXP");
        this.setCustomName(buildDisplayName());
        this.setCustomNameVisible(true);
        this.bossBar.setName(buildDisplayName());
    }

    /**
     * Adds XP and handles level-up logic.
     */
    public void addXP(int amount) {
        xp += amount;
        if (xp >= this.level * 20) {
            this.level++;
            xp = 0;

            Objects.requireNonNull(this.getAttribute(Attributes.ATTACK_DAMAGE))
                    .setBaseValue(this.getAttributeValue(Attributes.ATTACK_DAMAGE) + 6);

            Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH))
                    .setBaseValue(this.getAttributeValue(Attributes.MAX_HEALTH) + 30);

            this.heal(40);

            this.setCustomName(buildDisplayName());
            this.setCustomNameVisible(true);
            this.bossBar.setName(buildDisplayName());
        }
    }
}

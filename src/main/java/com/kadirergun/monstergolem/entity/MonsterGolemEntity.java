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
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * A custom guardian golem entity with enhanced behavior and systems:
 * - Boss bar support
 * - Persistent level & XP (saved to NBT)
 * - Ranged attacks (fireball, snowball, wither skull, and arrow support like a
 * skeleton)
 * - Automatic torch lighting where it walks (Light Block)
 * - Armor interaction (players can give armor to increase armor stat)
 * - Fire immunity and improved movement in liquids
 *
 * This entity implements RangedAttackMob and provides a bow visual in its main
 * hand
 * so it can fire arrows using {@link #performRangedAttack(LivingEntity, float)}
 * and
 * the ranged bow AI goal.
 */
public class MonsterGolemEntity extends IronGolem implements RangedAttackMob {

    /**
     * Server-side boss bar used to display this entity's name and health to
     * players.
     */
    private final ServerBossEvent bossBar = new ServerBossEvent(Component.literal(""), BossEvent.BossBarColor.GREEN,
            BossEvent.BossBarOverlay.PROGRESS);

    /** Cooldown timer (ticks) until next fireball can be shot. */
    private int fireCooldown = 0;
    /** Cooldown timer (ticks) until next wither skull can be shot. */
    private int witherCooldown = 0;

    /** Current level of the guardian golem. */
    private int level = 1;
    /** Current XP stored for level progression. */
    private int xp = 0;

    /** Last position where a light block was placed to keep area lit. */
    private BlockPos lastLightPos = null;

    /**
     * Constructs a new MonsterGolemEntity.
     *
     * @param type  the EntityType instance
     * @param level the world level where the entity is created
     */
    public MonsterGolemEntity(EntityType<? extends IronGolem> type, Level level) {
        super(type, level);
        // Reduce pathfinding penalties for fire/lava so the golem can traverse them
        // easily
        this.setPathfindingMalus(BlockPathTypes.LAVA, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 0.0F);
    }

    /**
     * Creates and returns the base attribute supplier for this entity type.
     *
     * @return a configured AttributeSupplier.Builder for registering attributes
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
     * Periodic update executed each tick. Handles lighting, movement adjustments,
     * target-based ranged attacks, and boss bar synchronization (server-side).
     */
    @Override
    public void aiStep() {
        super.aiStep();
        if (level().isClientSide)
            return;

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
     * Registers the entity's AI goals and target priorities.
     * Includes melee, wandering, look-at-player, and improved targeting.
     */
    @Override
    protected void registerGoals() {
        // Priority 1: Ranged bow attack - golem prefers to strafe and shoot (like a
        // Pillager)
        // attackIntervalMin=15 ticks, maxShootRange=32 blocks
        this.goalSelector.addGoal(1, new RangedBowAttackGoal<>(this, 1.2D, 15, 32.0F));
        // Priority 3: Melee fallback - only runs when bow goal is not active (e.g.
        // target too close, no LOS)
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.6D, true));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.9D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 12.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        // Retaliate when hurt and prefer monsters as targets (excluding other
        // MonsterGolemEntity instances)
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1,
                new NearestAttackableTargetGoal<>(this, Monster.class, 48, true, true,
                        entity -> !(entity instanceof MonsterGolemEntity)));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Slime.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Creeper.class, true));
    }

    /**
     * Prevents friendly fire between Monster Golems and players by treating them as
     * allies.
     *
     * @param entity the other entity to check
     * @return true if the given entity is considered an ally
     */
    @Override
    public boolean isAlliedTo(@NotNull Entity entity) {
        if (entity instanceof MonsterGolemEntity)
            return true;
        return entity instanceof Player || super.isAlliedTo(entity);
    }

    /**
     * Allows this entity to consider Creepers as valid attack targets.
     *
     * @param type the entity type to check
     * @return true if the type can be attacked
     */
    @Override
    public boolean canAttackType(@NotNull EntityType<?> type) {
        if (type == EntityType.CREEPER)
            return true;
        return super.canAttackType(type);
    }

    /**
     * Deals damage to a target, with extra damage applied to Creepers.
     *
     * @param target the entity being attacked
     * @return true if the attack successfully dealt damage
     */
    @Override
    public boolean doHurtTarget(@NotNull Entity target) {
        float base = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        if (target instanceof Creeper)
            base *= 2.5f;
        return target.hurt(this.damageSources().mobAttack(this), base);
    }

    /**
     * Adds the boss bar to the player's view when the player starts seeing the
     * golem.
     *
     * @param player the server player who started seeing the entity
     */
    @Override
    public void startSeenByPlayer(@NotNull ServerPlayer player) {
        super.startSeenByPlayer(player);
        bossBar.addPlayer(player);
    }

    /**
     * Removes the boss bar from the player's view when the player stops seeing the
     * golem.
     *
     * @param player the server player who stopped seeing the entity
     */
    @Override
    public void stopSeenByPlayer(@NotNull ServerPlayer player) {
        super.stopSeenByPlayer(player);
        bossBar.removePlayer(player);
    }

    /**
     * Main tick update: handles navigation updates, boss bar sync, movement tweaks
     * in fluids, and ranged attack cooldowns.
     */
    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) {
            // Recompute path more frequently to make targeting feel responsive
            if (this.tickCount % 10 == 0) {
                this.getNavigation().recomputePath();
            }

            bossBar.setName(this.getDisplayName());
            bossBar.setProgress(this.getHealth() / this.getMaxHealth());

            // Don't slow down or get stuck in lava
            if (this.isInLava()) {
                this.setDeltaMovement(this.getDeltaMovement().multiply(1.4D, 1.0D, 1.4D));
            }

            // Avoid slowing down in water
            if (this.isInWater()) {
                this.setDeltaMovement(this.getDeltaMovement().multiply(1.2D, 1.0D, 1.2D));
            }

            // If a target exists, handle ranged attack cooldowns and firing
            LivingEntity target = this.getTarget();
            if (target != null) {
                double dist = this.distanceTo(target);

                // Long range - Wither Skull (most powerful)
                if (dist >= 20 && dist <= 30) {
                    if (witherCooldown <= 0) {
                        shootWitherSkull(target);
                        witherCooldown = 160; // 8 seconds
                    } else {
                        witherCooldown--;
                    }
                    fireCooldown--;
                }
                // Medium range - Fireball (entity damage only, no block damage)
                else if (dist >= 10 && dist < 20) {
                    if (fireCooldown <= 0) {
                        shootFireball(target);
                        fireCooldown = 80;
                    } else {
                        fireCooldown--;
                    }
                    witherCooldown--;
                }
                // Close range - only decrement cooldowns (snowball removed: deals no real
                // damage)
                else {
                    fireCooldown--;
                    witherCooldown--;
                }
            }
        }
    }

    private void shootWitherSkull(LivingEntity target) {
        WitherSkull skull = new WitherSkull(this.level(), this,
                target.getX() - this.getX(),
                target.getY(0.5) - this.getY(0.5),
                target.getZ() - this.getZ());
        skull.setPos(this.getX(), this.getEyeY() - 0.1, this.getZ());
        skull.setDangerous(false); // Does not break blocks
        this.level().addFreshEntity(skull);
    }

    /**
     * Initializes name, boss bar and navigation settings when the entity spawns.
     *
     * @param level      the server-level accessor
     * @param difficulty the difficulty instance
     * @param reason     spawn reason
     * @param spawnData  spawn group data
     * @param dataTag    optional NBT tag used during spawning
     * @return the spawn group data
     */
    @SuppressWarnings("deprecation")
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
     * Returns an ItemStack visually held in the given equipment slot.
     * The golem uses a bow visual in its main hand to match ranged bow AI.
     *
     * @param slot the equipment slot to query
     * @return an ItemStack representing the item the entity appears to hold
     */
    @Override
    public @NotNull ItemStack getItemBySlot(@NotNull EquipmentSlot slot) {
        if (slot == EquipmentSlot.MAINHAND) {
            return new ItemStack(Items.BOW);
        }
        return super.getItemBySlot(slot);
    }

    /**
     * Shoots a fireball that deals damage only to living entities and does NOT
     * destroy blocks. Uses a SmallFireball with a custom explosion override.
     *
     * @param target the target LivingEntity
     */
    private void shootFireball(LivingEntity target) {
        double dx = target.getX() - this.getX();
        double dy = target.getY(0.5) - this.getY(0.5);
        double dz = target.getZ() - this.getZ();

        // SmallFireball subclass that produces no block damage on explosion
        SmallFireball fireball = new SmallFireball(level(), this, dx, dy, dz) {
            @Override
            protected void onHitEntity(net.minecraft.world.phys.EntityHitResult result) {
                Entity hit = result.getEntity();
                if (hit instanceof LivingEntity livingHit) {
                    // Only damage if it is actually the golem's target (enemy)
                    LivingEntity owner = (LivingEntity) getOwner();
                    if (owner != null && owner.isAlliedTo(livingHit))
                        return;
                    livingHit.hurt(damageSources().fireball(this, owner), 10.0F);
                    livingHit.setSecondsOnFire(5);
                }
                this.discard();
            }

            @Override
            protected void onHitBlock(net.minecraft.world.phys.BlockHitResult result) {
                // No block interaction on direct block hit
                this.discard();
            }
        };
        fireball.setPos(this.getX(), this.getY(1.5), this.getZ());
        level().addFreshEntity(fireball);
    }

    /**
     * Builds a translatable display name based on the current level.
     *
     * @return a translatable Component used as entity display name
     */
    private Component buildDisplayName() {
        return Component.translatable(
                "entity.customgolem.monster_golem.name_format",
                this.level);
    }

    /**
     * Handles player interaction to give armor to the golem. Increases the golem's
     * armor attribute based on the provided armor material. Consumes the item
     * from the player's hand unless they are in creative mode.
     *
     * @param player the player interacting with the golem
     * @param hand   the hand used for interaction
     * @return the interaction result
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

            if (!player.isCreative())
                stack.shrink(1);
            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, hand);
    }

    /**
     * Returns true to indicate the golem is immune to fire damage.
     *
     * @return true if the entity is fire immune
     */
    @Override
    public boolean fireImmune() {
        return true;
    }

    /**
     * Adjusts the entity's jump power for improved mobility.
     *
     * @return the modified jump power
     */
    @Override
    protected float getJumpPower() {
        return super.getJumpPower() * 1.6F; // 60% more powerful jump for better mobility
    }

    /**
     * Prevents the golem from drowning in fluids.
     *
     * @param type the fluid type being checked
     * @return false indicating the golem cannot drown
     */
    @Override
    public boolean canDrownInFluidType(net.minecraftforge.fluids.FluidType type) {
        return false; // Do not drown in any fluid (including water)
    }

    /**
     * Saves custom level and XP values to the entity's NBT when the world is saved.
     *
     * @param tag the CompoundTag to write to
     */
    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("GuardianLevel", this.level);
        tag.putInt("GuardianXP", this.xp);
    }

    /**
     * Reads custom level and XP values from NBT when the entity is loaded.
     * Also updates the visible name and boss bar to reflect restored level.
     *
     * @param tag the CompoundTag to read from
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
     * Adds XP to the golem and handles level-up logic when the XP threshold is
     * reached.
     * On level up, increases attack damage and max health, heals the entity, and
     * updates the display name and boss bar.
     *
     * @param amount the amount of XP to add
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

    /**
     * Performs a ranged arrow attack toward the target, similar to a skeleton.
     * This method is used by the RangedBowAttackGoal so the golem will fire arrows.
     *
     * @param target         the target LivingEntity
     * @param distanceFactor multiplier used by projectile retrieval (provided by AI
     *                       goal)
     */
    /**
     * Performs a ranged arrow attack toward the target, similar to a skeleton.
     * This method is used by the RangedBowAttackGoal so the golem will fire arrows.
     *
     * @param target         the target LivingEntity
     * @param distanceFactor multiplier used by projectile retrieval (provided by AI
     *                       goal)
     */
    @Override
    public void performRangedAttack(@NotNull LivingEntity target, float distanceFactor) {
        // Use a spectral arrow (glowing effect) as an empowered arrow
        ItemStack arrowStack = new ItemStack(Items.SPECTRAL_ARROW);
        AbstractArrow arrow = ProjectileUtil.getMobArrow(this, arrowStack, distanceFactor);

        // Apply Power enchantment (level 5) to increase damage
        if (arrow instanceof Arrow regularArrow) {
            regularArrow.addEffect(new net.minecraft.world.effect.MobEffectInstance(
                    net.minecraft.world.effect.MobEffects.GLOWING, 100));
        }

        // Calculate the direction towards the target
        double d0 = target.getX() - this.getX();
        double d1 = target.getEyeY() - 3.0D - arrow.getY();
        double d2 = target.getZ() - this.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);

        // Position the arrow in front of the golem to avoid self-collision
        double spawnOffset = 1.5D;
        double lookX = -Math.sin(this.getYRot() * ((float) Math.PI / 180F));
        double lookZ = Math.cos(this.getYRot() * ((float) Math.PI / 180F));

        arrow.setPos(this.getX() + lookX * spawnOffset, this.getEyeY() - 0.5D, this.getZ() + lookZ * spawnOffset);

        // Higher speed (2.0F) and tighter accuracy for an empowered shot
        arrow.shoot(d0, d1 + d3 * 0.2D, d2, 2.0F, (float) (14 - this.level().getDifficulty().getId() * 4));

        // Boosted damage scaling: base 8 + level bonus
        arrow.setBaseDamage(8.0D + (this.level * 0.75D));
        arrow.setCritArrow(true); // Always a critical (flame particle trail)

        this.playSound(net.minecraft.sounds.SoundEvents.SKELETON_SHOOT, 1.0F,
                1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity(arrow);
    }
}

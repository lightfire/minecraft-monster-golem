package com.ege.aphernix.entity;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

public class AphernixEntity extends PathfinderMob {
    private static final double ITEM_SEARCH_RANGE = 12.0D;
    private ItemEntity wantedItem;
    private int itemSearchCooldown;
    private boolean monsterWasPresent;
    private int bowAnimationTicks;
    private int randomJumpCooldown;
    private int diamondCooldown;

    public AphernixEntity(EntityType<? extends AphernixEntity> entityType, Level level) {
        super(entityType, level);
        this.setPersistenceRequired();
        this.randomJumpCooldown = 50 + this.random.nextInt(90);
        this.diamondCooldown = 600 + this.random.nextInt(601);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.29D)
                .add(Attributes.FOLLOW_RANGE, 36.0D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.ARMOR, 1.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.20D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.18D, true));
        this.goalSelector.addGoal(4, new FollowNearestPlayerGoal(this, 1.08D, 6.0F, 3.0F));
        this.goalSelector.addGoal(6, new RandomStrollGoal(this, 0.85D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 10.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Monster.class, true));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.level().isClientSide) {
            return;
        }

        protectNearestPlayer();
        tickDiamondAppearance();
        tickRandomJump();

        if (this.tickCount % 5 == 0) {
            updateBattleState();
        }

        if (this.bowAnimationTicks > 0) {
            tickDoubleBow();
            return;
        }

        if (this.itemSearchCooldown > 0) {
            this.itemSearchCooldown--;
        }

        if (this.getTarget() == null || !this.getTarget().isAlive()) {
            seekAndEquipUsefulItem();
        } else {
            this.wantedItem = null;
        }
    }

    private void tickRandomJump() {
        if (this.randomJumpCooldown > 0) {
            this.randomJumpCooldown--;
            return;
        }

        this.randomJumpCooldown = 50 + this.random.nextInt(90);
        boolean canJump = this.onGround()
                && this.bowAnimationTicks <= 0
                && this.getTarget() == null
                && this.wantedItem == null;

        if (canJump) {
            this.jumpControl.jump();
        }
    }

    private void tickDiamondAppearance() {
        if (this.diamondCooldown > 0) {
            this.diamondCooldown--;
            return;
        }

        this.diamondCooldown = 600 + this.random.nextInt(601);
        double angle = this.random.nextDouble() * Math.PI * 2.0D;
        double distance = 1.0D + this.random.nextDouble() * 1.25D;
        double x = this.getX() + Math.cos(angle) * distance;
        double z = this.getZ() + Math.sin(angle) * distance;

        ItemEntity diamond = new ItemEntity(
                this.level(),
                x,
                this.getY() + 0.35D,
                z,
                new ItemStack(Items.DIAMOND)
        );
        diamond.setDeltaMovement(0.0D, 0.18D, 0.0D);
        this.level().addFreshEntity(diamond);
        this.playSound(SoundEvents.ITEM_PICKUP, 0.8F, 1.6F);
    }

    private void protectNearestPlayer() {
        Player player = this.level().getNearestPlayer(this, 24.0D);
        if (player == null) {
            return;
        }

        LivingEntity attacker = player.getLastHurtByMob();
        if (attacker instanceof Monster && attacker.isAlive() && this.distanceToSqr(attacker) <= 1024.0D) {
            this.setTarget(attacker);
            return;
        }

        LivingEntity playerTarget = player.getLastHurtMob();
        if (playerTarget instanceof Monster && playerTarget.isAlive() && this.distanceToSqr(playerTarget) <= 1024.0D) {
            this.setTarget(playerTarget);
        }
    }

    private void updateBattleState() {
        boolean monsterPresent = this.getTarget() instanceof Monster && this.getTarget().isAlive();
        if (!monsterPresent) {
            List<Monster> monsters = this.level().getEntitiesOfClass(
                    Monster.class,
                    this.getBoundingBox().inflate(24.0D),
                    monster -> monster.isAlive() && this.hasLineOfSight(monster)
            );
            monsterPresent = !monsters.isEmpty();
        }

        if (monsterPresent) {
            this.monsterWasPresent = true;
            if (this.bowAnimationTicks > 0) {
                this.bowAnimationTicks = 0;
                this.setPose(Pose.STANDING);
            }
        } else if (this.monsterWasPresent) {
            this.monsterWasPresent = false;
            this.bowAnimationTicks = 48;
            this.wantedItem = null;
            this.getNavigation().stop();
        }
    }

    private void tickDoubleBow() {
        this.getNavigation().stop();

        boolean firstBow = this.bowAnimationTicks <= 48 && this.bowAnimationTicks >= 37;
        boolean secondBow = this.bowAnimationTicks <= 28 && this.bowAnimationTicks >= 17;
        this.setPose(firstBow || secondBow ? Pose.CROUCHING : Pose.STANDING);

        this.bowAnimationTicks--;
        if (this.bowAnimationTicks <= 0) {
            this.setPose(Pose.STANDING);
        }
    }

    private void seekAndEquipUsefulItem() {
        if (this.wantedItem == null || !this.wantedItem.isAlive() || !isUseful(this.wantedItem.getItem())) {
            this.wantedItem = null;
            if (this.itemSearchCooldown > 0) {
                return;
            }
            this.itemSearchCooldown = 10;

            List<ItemEntity> items = this.level().getEntitiesOfClass(
                    ItemEntity.class,
                    this.getBoundingBox().inflate(ITEM_SEARCH_RANGE),
                    item -> item.isAlive() && this.hasLineOfSight(item) && isUseful(item.getItem())
            );

            this.wantedItem = items.stream()
                    .min(Comparator.comparingDouble(item -> this.distanceToSqr(item)))
                    .orElse(null);
        }

        if (this.wantedItem == null) {
            return;
        }

        if (this.distanceToSqr(this.wantedItem) > 2.25D) {
            this.getNavigation().moveTo(this.wantedItem, 1.15D);
        } else {
            equipFromGround(this.wantedItem);
            this.wantedItem = null;
            this.getNavigation().stop();
        }
    }

    private boolean isUseful(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }

        if (stack.getItem() instanceof SwordItem) {
            return swordScore(stack) > swordScore(this.getMainHandItem());
        }

        if (stack.getItem() instanceof ArmorItem armor) {
            EquipmentSlot slot = armor.getEquipmentSlot();
            return armorScore(stack) > armorScore(this.getItemBySlot(slot));
        }

        return false;
    }

    private static float swordScore(ItemStack stack) {
        if (!(stack.getItem() instanceof SwordItem sword)) {
            return -1.0F;
        }
        return sword.getDamage() + sword.getTier().getLevel() + (stack.isEnchanted() ? 0.5F : 0.0F);
    }

    private static float armorScore(ItemStack stack) {
        if (!(stack.getItem() instanceof ArmorItem armor)) {
            return -1.0F;
        }
        return armor.getDefense() + armor.getToughness() + (stack.isEnchanted() ? 0.5F : 0.0F);
    }

    private void equipFromGround(ItemEntity itemEntity) {
        ItemStack groundStack = itemEntity.getItem();
        if (!isUseful(groundStack)) {
            return;
        }

        ItemStack equippedStack = groundStack.copy();
        equippedStack.setCount(1);

        EquipmentSlot slot;
        if (equippedStack.getItem() instanceof SwordItem) {
            slot = EquipmentSlot.MAINHAND;
        } else if (equippedStack.getItem() instanceof ArmorItem armor) {
            slot = armor.getEquipmentSlot();
        } else {
            return;
        }

        ItemStack oldStack = this.getItemBySlot(slot);
        if (!oldStack.isEmpty()) {
            this.spawnAtLocation(oldStack.copy());
        }

        this.setItemSlot(slot, equippedStack);
        groundStack.shrink(1);
        if (groundStack.isEmpty()) {
            itemEntity.discard();
        }

        this.swing(InteractionHand.MAIN_HAND);
        this.playSound(SoundEvents.ITEM_PICKUP, 0.35F, 1.0F + this.getRandom().nextFloat() * 0.2F);
    }

    private static class FollowNearestPlayerGoal extends Goal {
        private final AphernixEntity aphernix;
        private final double speed;
        private final float startDistance;
        private final float stopDistance;
        private Player player;

        private FollowNearestPlayerGoal(AphernixEntity aphernix, double speed, float startDistance, float stopDistance) {
            this.aphernix = aphernix;
            this.speed = speed;
            this.startDistance = startDistance;
            this.stopDistance = stopDistance;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (this.aphernix.bowAnimationTicks > 0 || this.aphernix.getTarget() != null || this.aphernix.wantedItem != null) {
                return false;
            }
            this.player = this.aphernix.level().getNearestPlayer(this.aphernix, 24.0D);
            return this.player != null && this.aphernix.distanceToSqr(this.player) > this.startDistance * this.startDistance;
        }

        @Override
        public boolean canContinueToUse() {
            return this.player != null
                    && this.player.isAlive()
                    && this.aphernix.bowAnimationTicks <= 0
                    && this.aphernix.getTarget() == null
                    && this.aphernix.wantedItem == null
                    && this.aphernix.distanceToSqr(this.player) > this.stopDistance * this.stopDistance
                    && this.aphernix.distanceToSqr(this.player) < 900.0D;
        }

        @Override
        public void tick() {
            this.aphernix.getLookControl().setLookAt(this.player, 10.0F, this.aphernix.getMaxHeadXRot());
            this.aphernix.getNavigation().moveTo(this.player, this.speed);
        }

        @Override
        public void stop() {
            this.player = null;
            this.aphernix.getNavigation().stop();
        }
    }
}

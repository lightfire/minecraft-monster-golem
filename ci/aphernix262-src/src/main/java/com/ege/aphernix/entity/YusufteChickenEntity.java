package com.ege.aphernix.entity;

import com.ege.aphernix.registry.ModEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.entity.animal.cow.Cow;
import net.minecraft.world.entity.animal.equine.Horse;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class YusufteChickenEntity extends Chicken {
    private int formTicks = 300;

    public YusufteChickenEntity(EntityType<? extends YusufteChickenEntity> type, Level level) {
        super(type, level);
        setPersistenceRequired();
    }

    @Override
    public void tick() {
        super.tick();
        if (!(level() instanceof ServerLevel serverLevel)) return;

        formTicks--;
        fleeLargeAnimals();
        if (formTicks <= 0) {
            YusufteEntity yusufte = new YusufteEntity(ModEntities.YUSUFTE.get(), serverLevel);
            yusufte.moveTo(getX(), getY(), getZ(), getYRot(), getXRot());
            serverLevel.addFreshEntity(yusufte);
            discard();
        }
    }

    private void fleeLargeAnimals() {
        AABB area = getBoundingBox().inflate(6.0D);
        var animals = level().getEntities(this, area,
                e -> e instanceof Cow || e instanceof Horse || e instanceof Pig || e instanceof Sheep);
        if (animals.isEmpty()) return;

        var nearest = animals.getFirst();
        double dx = getX() - nearest.getX();
        double dz = getZ() - nearest.getZ();
        double len = Math.max(0.001D, Math.sqrt(dx * dx + dz * dz));
        getNavigation().moveTo(getX() + dx / len * 8.0D, getY(), getZ() + dz / len * 8.0D, 1.35D);
    }
}

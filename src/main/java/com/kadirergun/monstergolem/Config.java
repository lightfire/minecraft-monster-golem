package com.kadirergun.monstergolem;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Example configuration holder for the mod. Uses Forge's config system to
 * define configurable values and provides runtime access to resolved values
 * through static fields.
 */
@Mod.EventBusSubscriber(modid = MonsterGolemMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    // --- General Settings ---
    /** Whether to show the Monster Golem boss bar. */
    public static final ForgeConfigSpec.BooleanValue ENABLE_BOSS_BAR = BUILDER
            .comment("Whether the Monster Golem boss bar should be shown")
            .define("enableBossBar", true);

    /** Base health value for the Monster Golem. */
    public static final ForgeConfigSpec.IntValue BASE_HEALTH = BUILDER
            .comment("Base health value for the Monster Golem")
            .defineInRange("baseHealth", 300, 50, 5000);

    /** Base attack damage for the Monster Golem. */
    public static final ForgeConfigSpec.IntValue BASE_DAMAGE = BUILDER
            .comment("Base attack damage for the Monster Golem")
            .defineInRange("baseDamage", 35, 1, 1000);

    /** Damage multiplier applied when attacking Creepers. */
    public static final ForgeConfigSpec.DoubleValue CREEPER_DAMAGE_MULTIPLIER = BUILDER
            .comment("Damage multiplier applied to Creepers")
            .defineInRange("creeperDamageMultiplier", 2.5D, 1.0D, 10.0D);

    /** Fireball cooldown in ticks. */
    public static final ForgeConfigSpec.IntValue FIREBALL_COOLDOWN = BUILDER
            .comment("Fireball shooting cooldown in ticks")
            .defineInRange("fireballCooldown", 80, 10, 600);

    /** Snowball cooldown in ticks. */
    public static final ForgeConfigSpec.IntValue SNOWBALL_COOLDOWN = BUILDER
            .comment("Snowball shooting cooldown in ticks")
            .defineInRange("snowballCooldown", 40, 10, 600);

    /** XP rewarded per kill. */
    public static final ForgeConfigSpec.IntValue XP_PER_KILL = BUILDER
            .comment("XP gained per killed mob")
            .defineInRange("xpPerKill", 5, 1, 100);

    /** XP needed per level multiplier. */
    public static final ForgeConfigSpec.IntValue XP_PER_LEVEL = BUILDER
            .comment("XP multiplier required to level up (level * this value)")
            .defineInRange("xpPerLevel", 20, 1, 1000);

    /** Optional list of item resource names for logging or testing. */
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> ITEM_STRINGS = BUILDER
            .comment("Optional list of item resource names (for logging or testing)")
            .defineListAllowEmpty("item", List.of("minecraft:iron_ingot"), Config::validateItemName);

    @SuppressWarnings("unused")
    static final ForgeConfigSpec SPEC = BUILDER.build();

    // Runtime values populated from config
    public static boolean enableBossBar;
    public static int baseHealth;
    public static int baseDamage;
    public static double creeperDamageMultiplier;
    public static int fireballCooldown;
    public static int snowballCooldown;
    public static int xpPerKill;
    public static int xpPerLevel;
    public static Set<Item> items;

    private static boolean validateItemName(final Object obj) {
        if (!(obj instanceof final String itemName)) return false;
        ResourceLocation rl = ResourceLocation.parse(itemName);
        return ForgeRegistries.ITEMS.containsKey(rl);
    }

    /**
     * Loads runtime values from the resolved config when the mod config is loaded.
     *
     * @param event the ModConfigEvent fired during mod configuration loading
     */
    @SubscribeEvent
    public static void onLoad(final ModConfigEvent event) {
        enableBossBar = ENABLE_BOSS_BAR.get();
        baseHealth = BASE_HEALTH.get();
        baseDamage = BASE_DAMAGE.get();
        creeperDamageMultiplier = CREEPER_DAMAGE_MULTIPLIER.get();
        fireballCooldown = FIREBALL_COOLDOWN.get();
        snowballCooldown = SNOWBALL_COOLDOWN.get();
        xpPerKill = XP_PER_KILL.get();
        xpPerLevel = XP_PER_LEVEL.get();

        items = ITEM_STRINGS.get().stream()
                .map(ResourceLocation::parse)
                .map(ForgeRegistries.ITEMS::getValue)
                .collect(Collectors.toSet());
    }
}
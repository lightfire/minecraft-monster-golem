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

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = MonsterGolemMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    // --- Genel Ayarlar ---
    public static final ForgeConfigSpec.BooleanValue ENABLE_BOSS_BAR = BUILDER
            .comment("Monster Golem boss bar gosterilsin mi?")
            .define("enableBossBar", true);

    public static final ForgeConfigSpec.IntValue BASE_HEALTH = BUILDER
            .comment("Monster Golem temel can degeri")
            .defineInRange("baseHealth", 300, 50, 5000);

    public static final ForgeConfigSpec.IntValue BASE_DAMAGE = BUILDER
            .comment("Monster Golem temel saldiri gucu")
            .defineInRange("baseDamage", 35, 1, 1000);

    public static final ForgeConfigSpec.DoubleValue CREEPER_DAMAGE_MULTIPLIER = BUILDER
            .comment("Creeper'lara vurulan hasar carpani")
            .defineInRange("creeperDamageMultiplier", 2.5D, 1.0D, 10.0D);

    public static final ForgeConfigSpec.IntValue FIREBALL_COOLDOWN = BUILDER
            .comment("Fireball atma bekleme suresi (tick)")
            .defineInRange("fireballCooldown", 80, 10, 600);

    public static final ForgeConfigSpec.IntValue SNOWBALL_COOLDOWN = BUILDER
            .comment("Snowball atma bekleme suresi (tick)")
            .defineInRange("snowballCooldown", 40, 10, 600);

    public static final ForgeConfigSpec.IntValue XP_PER_KILL = BUILDER
            .comment("Bir mob oldurunce kazanilan XP")
            .defineInRange("xpPerKill", 5, 1, 100);

    public static final ForgeConfigSpec.IntValue XP_PER_LEVEL = BUILDER
            .comment("Level atlamak icin gereken XP katsayisi (level * bu deger)")
            .defineInRange("xpPerLevel", 20, 1, 1000);

    // İstersen ileride whitelist/blacklist mantığı ekleyebiliriz
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> ITEM_STRINGS = BUILDER
            .comment("Loglamak veya test icin item listesi (opsiyonel)")
            .defineListAllowEmpty("item", List.of("minecraft:iron_ingot"), Config::validateItemName);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    // Runtime değerler
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
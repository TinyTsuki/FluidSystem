package edivad.fluidsystem.setup;

import edivad.fluidsystem.Main;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class Config {

    public static void init() {
        var SERVER_BUILDER = new ForgeConfigSpec.Builder();
        SERVER_BUILDER.comment(Main.MODNAME + "'s config");
        General.registerServerConfig(SERVER_BUILDER);
        Tank.registerServerConfig(SERVER_BUILDER);

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_BUILDER.build());
    }

    public static class General {

        public static ForgeConfigSpec.BooleanValue FINITE_WATER_SOURCE;

        public static void registerServerConfig(ForgeConfigSpec.Builder SERVER_BUILDER) {
            SERVER_BUILDER.push("General");

            FINITE_WATER_SOURCE = SERVER_BUILDER
                    .comment("If set to true, it will not be possible to have infinite water source")
                    .define("enabled", false);

            SERVER_BUILDER.pop();
        }
    }

    public static class Tank {

        public static ForgeConfigSpec.IntValue BLOCK_CAPACITY;
        public static ForgeConfigSpec.IntValue NUMBER_OF_MODULES;

        public static void registerServerConfig(ForgeConfigSpec.Builder SERVER_BUILDER) {
            SERVER_BUILDER.push("Tank");

            Tank.BLOCK_CAPACITY = SERVER_BUILDER
                    .comment("Indicates how much liquid each Tank Block can hold")
                    .defineInRange("capacity", 160000, 1000, 300000);

            Tank.NUMBER_OF_MODULES = SERVER_BUILDER
                    .comment("Indicates the maximum size of each Tank")
                    .defineInRange("modules", 300, 10, 400);

            SERVER_BUILDER.pop();
        }
    }
}
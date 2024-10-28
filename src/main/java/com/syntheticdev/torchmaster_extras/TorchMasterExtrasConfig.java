package com.syntheticdev.torchmaster_extras;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = TorchMasterExtras.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TorchMasterExtrasConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final GeneralConfig GENERAL;
    static final ForgeConfigSpec SPEC;

    static {
        GENERAL = new GeneralConfig(BUILDER);
        SPEC = BUILDER.build();
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
    }

    public static class GeneralConfig {
        public final ForgeConfigSpec.ConfigValue<Integer> cobblemonTorchRadius;
        public final ForgeConfigSpec.ConfigValue<Boolean> logSpawnChecks;

        private GeneralConfig(ForgeConfigSpec.Builder builder) {
            builder.push("General");

            this.cobblemonTorchRadius = builder
                    .comment("The cubic radius of the Cobblemon Torch in each direction with the torch at the center")
                    .translation("torchmaster_extras.config.cobblemonTorchRadius.description")
                    .defineInRange("cobblemonTorchRadius", 128, 0, Integer.MAX_VALUE);
            this.logSpawnChecks = builder.comment("Print entity spawn checks to the debug log").translation("torchmaster_extras.config.logSpawnChecks.description").define("logSpawnChecks", false);

            builder.pop();
        }
    }
}

package com.syntheticdev.torchmaster_extras;

import com.syntheticdev.torchmaster_extras.common.ModBlocks;
import com.syntheticdev.torchmaster_extras.common.ModItems;
import com.syntheticdev.torchmaster_extras.common.entityblocking.ExtraEntityFilterRegistries;
import com.syntheticdev.torchmaster_extras.common.entityblocking.ExtraLightSerializers;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(TorchMasterExtras.MOD_ID)
public class TorchMasterExtras {

    public static final String MOD_ID = "torchmaster_extras";

    public TorchMasterExtras() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModBlocks.DEFERRED_REGISTER.register(modEventBus);
        ModItems.DEFERRED_REGISTER.register(modEventBus);
        ExtraLightSerializers.register();

        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(EventPriority.LOWEST, this::postInit);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, TorchMasterExtrasConfig.SPEC, "torchmaster-extras.toml");
    }

    private void postInit(FMLLoadCompleteEvent event) {
        ExtraEntityFilterRegistries.init();
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        ModItems.addCreative(event);
    }

}

package com.syntheticdev.torchmaster_extras.common.entityblocking;

import com.mojang.logging.LogUtils;
import com.syntheticdev.torchmaster_extras.TorchMasterExtras;
import com.syntheticdev.torchmaster_extras.TorchMasterExtrasConfig;
import com.syntheticdev.torchmaster_extras.common.ModCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.xalcon.torchmaster.Torchmaster;
import org.slf4j.Logger;


@Mod.EventBusSubscriber(modid = TorchMasterExtras.MOD_ID)
public class ExtraEntityBlockingEventHandler {

    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void onWorldAttachCapabilityEvent(AttachCapabilitiesEvent<Level> event) {
        event.addCapability(new ResourceLocation(TorchMasterExtras.MOD_ID, "registry"), new ExtraLightsRegistryCapability());
    }

    @SubscribeEvent
    public static void onGlobalTick(TickEvent.ServerTickEvent event) {
        if (event.side == LogicalSide.CLIENT) return;
        if (event.phase != TickEvent.Phase.END) return;
        if (Torchmaster.server == null) return;

        for (ServerLevel level : Torchmaster.server.getAllLevels()) {
            level.getProfiler().push("torchmaster_extras_" + level.dimension().registry());
            level.getCapability(ModCapabilities.TC_REGISTRY).ifPresent((reg) -> reg.onGlobalTick(level));
            level.getProfiler().pop();
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (event.isCanceled() || event.loadedFromDisk()) return;

        boolean log = TorchMasterExtrasConfig.GENERAL.logSpawnChecks.get();

        Level level = event.getLevel();
        Entity entity = event.getEntity();
        BlockPos pos = entity.blockPosition();
        level.getCapability(ModCapabilities.TC_REGISTRY).ifPresent(reg -> {
            if (reg.shouldBlockEntity(entity, pos)) {
                event.setCanceled(true);
                entity.remove(Entity.RemovalReason.DISCARDED);
                if (log) {
                    LOGGER.debug("Blocking spawn of {} at {}", EntityType.getKey(entity.getType()), pos);
                }
            }
        });
    }

}

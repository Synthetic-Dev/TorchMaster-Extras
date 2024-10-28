package com.syntheticdev.torchmaster_extras.common.entityblocking;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.INBTSerializable;
import net.xalcon.torchmaster.common.commands.TorchInfo;
import net.xalcon.torchmaster.common.logic.entityblocking.IEntityBlockingLight;

public interface ITorchCapabilityRegistry extends INBTSerializable<CompoundTag> {
    boolean shouldBlockEntity(Entity entity, BlockPos blockPos);

    void registerLight(String lightKey, IEntityBlockingLight entityBlockingLight);

    void unregisterLight(String lightKey);

    IEntityBlockingLight getLight(String lightKey);

    void onGlobalTick(Level level);

    TorchInfo[] getEntries();
}

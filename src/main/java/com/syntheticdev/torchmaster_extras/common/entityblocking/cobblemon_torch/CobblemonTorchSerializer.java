package com.syntheticdev.torchmaster_extras.common.entityblocking.cobblemon_torch;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.xalcon.torchmaster.common.logic.entityblocking.IEntityBlockingLight;
import net.xalcon.torchmaster.common.logic.entityblocking.ILightSerializer;

public class CobblemonTorchSerializer implements ILightSerializer {

    public static final String SERIALIZER_KEY = "cobblemon_torch";
    public static final CobblemonTorchSerializer INSTANCE = new CobblemonTorchSerializer();

    private CobblemonTorchSerializer() {}

    @Override
    public CompoundTag serializeLight(String lightKey, IEntityBlockingLight entityBlockingLight) {
        if (entityBlockingLight == null) {
            throw new IllegalArgumentException("Unable to serialize null");
        } else if (entityBlockingLight instanceof CobblemonTorchEntityBlockingLight light) {
            CompoundTag compoundTag = new CompoundTag();
            compoundTag.put("pos", NbtUtils.writeBlockPos(light.getPos()));
            return compoundTag;
        }

        throw new IllegalArgumentException("Unable to serialize '" + entityBlockingLight.getClass().getCanonicalName() + "', expected '" + CobblemonTorchEntityBlockingLight.class.getCanonicalName() + "'");
    }

    @Override
    public IEntityBlockingLight deserializeLight(String lightKey, CompoundTag compoundTag) {
        return new CobblemonTorchEntityBlockingLight(NbtUtils.readBlockPos(compoundTag.getCompound("pos")));
    }

    @Override
    public String getSerializerKey() {
        return SERIALIZER_KEY;
    }
}

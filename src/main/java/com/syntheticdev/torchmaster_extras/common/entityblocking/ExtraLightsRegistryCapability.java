package com.syntheticdev.torchmaster_extras.common.entityblocking;

import com.syntheticdev.torchmaster_extras.common.ModCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.xalcon.torchmaster.Torchmaster;
import net.xalcon.torchmaster.common.commands.TorchInfo;
import net.xalcon.torchmaster.common.logic.entityblocking.IEntityBlockingLight;
import net.xalcon.torchmaster.common.logic.entityblocking.ILightSerializer;
import net.xalcon.torchmaster.common.logic.entityblocking.LightSerializerRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ExtraLightsRegistryCapability implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {

    private final ITorchCapabilityRegistry container = new RegistryContainer();
    private final LazyOptional optional = LazyOptional.of(() -> this.container);

    public ExtraLightsRegistryCapability() { }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
        return capability == ModCapabilities.TC_REGISTRY ? this.optional : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return this.container.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag compoundTag) {
        this.container.deserializeNBT(compoundTag);
    }

    private static class RegistryContainer implements ITorchCapabilityRegistry {
        private final HashMap<String, IEntityBlockingLight> lights = new HashMap<>();
        private int tickCounter;

        private RegistryContainer() {
        }

        public CompoundTag serializeNBT() {
            CompoundTag nbt = new CompoundTag();
            ILightSerializer serializer = null;
            String cachedSerializerKey = null;

            for (Map.Entry<String, IEntityBlockingLight> lightEntry : this.lights.entrySet()) {
                IEntityBlockingLight light = lightEntry.getValue();
                String lightKey = lightEntry.getKey();
                String serializerKey = light.getLightSerializerKey();
                if (serializerKey == null) {
                    Torchmaster.Log.error("Unable to serialize light '{}', the serializer was null", lightKey);
                } else {
                    if (!serializerKey.equals(cachedSerializerKey)) {
                        serializer = LightSerializerRegistry.getLightSerializer(serializerKey);
                        cachedSerializerKey = serializerKey;
                    }

                    if (serializer == null) {
                        Torchmaster.Log.error("Unable to serialize light '{}', the serializer '{}' was not found", lightKey, serializerKey);
                    } else {
                        try {
                            CompoundTag lightNbt = serializer.serializeLight(lightKey, light);
                            if (lightNbt == null) {
                                Torchmaster.Log.error("Unable to serialize light '{}', the serializer '{}' returned null", lightKey, serializerKey);
                            } else {
                                lightNbt.putString("lightSerializerKey", serializerKey);
                                nbt.put(lightKey, lightNbt);
                            }
                        } catch (Exception exception) {
                            Torchmaster.Log.error("The serializer '{}' threw an error during serialization!", serializerKey);
                            Torchmaster.Log.error("Error", exception);
                        }
                    }
                }
            }

            return nbt;
        }

        public void deserializeNBT(CompoundTag nbt) {
            this.lights.clear();
            ILightSerializer serializer = null;
            String cachedSerializerKey = null;

            for (String lightKey : nbt.getAllKeys()) {
                CompoundTag lightNbt = nbt.getCompound(lightKey);
                String serializerKey = lightNbt.getString("lightSerializerKey");
                if (!serializerKey.equals(cachedSerializerKey)) {
                    serializer = LightSerializerRegistry.getLightSerializer(serializerKey);
                    cachedSerializerKey = serializerKey;
                }

                if (serializer == null) {
                    Torchmaster.Log.error("Unable to deserialize the light '{}', the serializer '{}' was not found", lightKey, serializerKey);
                } else {
                    try {
                        IEntityBlockingLight light = serializer.deserializeLight(lightKey, lightNbt);
                        if (light == null) {
                            Torchmaster.Log.error("Unable to deserialize the light '{}', the serializer returned null", lightKey);
                        } else {
                            this.lights.put(lightKey, light);
                        }
                    } catch (Exception exception) {
                        Torchmaster.Log.error("The serializer '{}' threw an error during deserialization!", serializerKey);
                        Torchmaster.Log.error("Error", exception);
                    }
                }
            }

        }

        public boolean shouldBlockEntity(Entity entity, BlockPos pos) {
            Iterator<Map.Entry<String, IEntityBlockingLight>> iterator = this.lights.entrySet().iterator();

            IEntityBlockingLight light;
            do {
                if (!iterator.hasNext()) {
                    return false;
                }

                Map.Entry<String, IEntityBlockingLight> lightEntry = iterator.next();
                light = lightEntry.getValue();
            } while(!light.shouldBlockEntity(entity, pos));

            return true;
        }

        public void registerLight(String lightKey, IEntityBlockingLight light) {
            if (lightKey == null) {
                throw new IllegalArgumentException("lightKey must not be null");
            } else if (light == null) {
                throw new IllegalArgumentException("light must not be null");
            } else {
                this.lights.put(lightKey, light);
            }
        }

        public void unregisterLight(String lightKey) {
            this.lights.remove(lightKey);
        }

        @Nullable
        public IEntityBlockingLight getLight(String lightKey) {
            return this.lights.get(lightKey);
        }

        public void onGlobalTick(Level level) {
            if (this.tickCounter++ >= 200) {
                this.tickCounter = 0;
                this.lights.entrySet().removeIf((l) -> l.getValue().cleanupCheck(level));
            }
        }

        public TorchInfo[] getEntries() {
            return this.lights.values().stream().map((x) -> new TorchInfo(x.getName(), x.getPos())).toArray(TorchInfo[]::new);
        }
    }
}

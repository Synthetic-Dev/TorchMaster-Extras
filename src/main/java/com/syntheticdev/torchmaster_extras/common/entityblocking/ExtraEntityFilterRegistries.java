package com.syntheticdev.torchmaster_extras.common.entityblocking;

import net.minecraftforge.registries.ForgeRegistries;
import net.xalcon.torchmaster.common.EntityFilterRegistry;

public final class ExtraEntityFilterRegistries {

    public static final EntityFilterRegistry COBBLEMON_TORCH_FILTER_REGISTRY = new EntityFilterRegistry();

    public static void init() {
        registerCobblemonEntities(COBBLEMON_TORCH_FILTER_REGISTRY);
    }

    private static void registerCobblemonEntities(EntityFilterRegistry registry) {
        ForgeRegistries.ENTITY_TYPES.getEntries().stream()
                .filter(entry -> entry.getValue() != null)
                .filter(entry -> entry.getKey().location().getNamespace().equals("cobblemon"))
                .forEach(entry -> registry.registerEntity(entry.getKey().location()));
    }

}

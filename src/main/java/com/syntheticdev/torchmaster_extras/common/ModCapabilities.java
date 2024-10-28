package com.syntheticdev.torchmaster_extras.common;

import com.syntheticdev.torchmaster_extras.common.entityblocking.ITorchCapabilityRegistry;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public final class ModCapabilities {

    public static final CapabilityToken<ITorchCapabilityRegistry> TC_REGISTRY_TOKEN = new CapabilityToken<>() {};
    public static final Capability<ITorchCapabilityRegistry> TC_REGISTRY = CapabilityManager.get(TC_REGISTRY_TOKEN);

}

package com.syntheticdev.torchmaster_extras.common.entityblocking;

import com.syntheticdev.torchmaster_extras.common.entityblocking.cobblemon_torch.CobblemonTorchSerializer;
import net.xalcon.torchmaster.common.logic.entityblocking.LightSerializerRegistry;

public final class ExtraLightSerializers {

    public static void register() {
        LightSerializerRegistry.registerLightSerializer(CobblemonTorchSerializer.INSTANCE);
    }

}

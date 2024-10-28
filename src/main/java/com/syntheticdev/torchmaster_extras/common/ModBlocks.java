package com.syntheticdev.torchmaster_extras.common;

import com.syntheticdev.torchmaster_extras.TorchMasterExtras;
import com.syntheticdev.torchmaster_extras.TorchMasterExtrasConfig;
import com.syntheticdev.torchmaster_extras.common.blocks.ExtraEntityBlockingLightBlock;
import com.syntheticdev.torchmaster_extras.common.entityblocking.cobblemon_torch.CobblemonTorchEntityBlockingLight;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModBlocks {

    public static final DeferredRegister<Block> DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, TorchMasterExtras.MOD_ID);

    public static final RegistryObject<ExtraEntityBlockingLightBlock> COBBLEMON_TORCH = DEFERRED_REGISTER.register(
            "cobblemon_torch",
            () -> new ExtraEntityBlockingLightBlock(
                    BlockBehaviour.Properties.of()
                        .sound(SoundType.WOOD)
                        .strength(1.0F, 1.0F)
                        .lightLevel((blockState) -> 15),
                    (pos) -> "CMT_" + pos.getX() + "_" + pos.getY() + "_" + pos.getZ(),
                    CobblemonTorchEntityBlockingLight::new,
                    1.0F,
                    CobblemonTorchEntityBlockingLight.SHAPE,
                    TorchMasterExtrasConfig.GENERAL.cobblemonTorchRadius
            )
    );

}

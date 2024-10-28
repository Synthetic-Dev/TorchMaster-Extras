package com.syntheticdev.torchmaster_extras.common.entityblocking.cobblemon_torch;

import com.syntheticdev.torchmaster_extras.TorchMasterExtrasConfig;
import com.syntheticdev.torchmaster_extras.common.ModBlocks;
import com.syntheticdev.torchmaster_extras.common.entityblocking.ExtraEntityFilterRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.xalcon.torchmaster.common.logic.DistanceLogics;
import net.xalcon.torchmaster.common.logic.entityblocking.IEntityBlockingLight;

public class CobblemonTorchEntityBlockingLight implements IEntityBlockingLight {
    public static final VoxelShape SHAPE = Shapes.join(
            Block.box(6.0, 0.0, 6.0, 10.0, 16.0, 10.0),
            Block.box(5.0, 0.0, 5.0, 11.0, 3.0, 11.0),
            BooleanOp.OR
    );
    private final BlockPos pos;

    public CobblemonTorchEntityBlockingLight(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public boolean shouldBlockEntity(Entity entity, BlockPos blockPos) {
        return ExtraEntityFilterRegistries.COBBLEMON_TORCH_FILTER_REGISTRY.containsEntity(EntityType.getKey(entity.getType()))
                && DistanceLogics.Cubic.isPositionInRange(blockPos.getX(), blockPos.getY(), blockPos.getZ(), this.pos, TorchMasterExtrasConfig.GENERAL.cobblemonTorchRadius.get());
    }

    @Override
    public boolean shouldBlockVillageSiege(BlockPos blockPos) {
        return false;
    }

    @Override
    public String getLightSerializerKey() {
        return CobblemonTorchSerializer.SERIALIZER_KEY;
    }

    @Override
    public String getName() {
        return "Cobblemon Torch";
    }

    @Override
    public BlockPos getPos() {
        return this.pos;
    }

    @Override
    public boolean cleanupCheck(Level level) {
        return level.isLoaded(this.pos) && level.getBlockState(this.pos).getBlock() != ModBlocks.COBBLEMON_TORCH.get();
    }
}

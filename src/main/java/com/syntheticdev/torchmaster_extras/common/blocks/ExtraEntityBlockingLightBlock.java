package com.syntheticdev.torchmaster_extras.common.blocks;

import com.syntheticdev.torchmaster_extras.common.ModCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PacketDistributor;
import net.xalcon.torchmaster.common.logic.entityblocking.IEntityBlockingLight;
import net.xalcon.torchmaster.common.network.ModMessageHandler;
import net.xalcon.torchmaster.common.network.volume.VolumeDisplayMessage;

import java.util.function.Function;
import java.util.function.Supplier;

public class ExtraEntityBlockingLightBlock extends Block {
    private final Function<BlockPos, String> keyFactory;
    private final Function<BlockPos, IEntityBlockingLight> lightFactory;
    private final float flameOffsetY;
    private final VoxelShape shape;
    private final Supplier<Integer> range;

    public ExtraEntityBlockingLightBlock(BlockBehaviour.Properties properties, Function<BlockPos, String> keyFactory, Function<BlockPos, IEntityBlockingLight> lightFactory, float flameOffsetY, VoxelShape shape, Supplier<Integer> rangeSupplier) {
        super(properties);
        this.keyFactory = keyFactory;
        this.lightFactory = lightFactory;
        this.flameOffsetY = flameOffsetY;
        this.shape = shape;
        this.range = rangeSupplier;
    }

    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext ctx) {
        return this.shape;
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (level.isClientSide()) {
            return super.use(state, level, pos, player, hand, result);
        }

        boolean show = !player.isShiftKeyDown();
        int color = 10551072;
        ItemStack itemStack = player.getItemInHand(InteractionHand.MAIN_HAND);
        Item itemInHand = itemStack.getItem();
        if (itemInHand instanceof DyeItem dye) {
            color = dye.getDyeColor().getTextColor();
        }

        player.displayClientMessage(Component.translatable(show ? "torchmaster.torch_volume.on_show" : "torchmaster.torch_volume.on_hide"), true);
        VolumeDisplayMessage msg = VolumeDisplayMessage.create(pos, this.range.get(), color, show, show);
        ModMessageHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)player), msg);
        return InteractionResult.SUCCESS;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource randomSource) {
        double d0 = (double)pos.getX() + 0.5;
        double d1 = (double)pos.getY() + (double)this.flameOffsetY;
        double d2 = (double)pos.getZ() + 0.5;
        level.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0, 0.0, 0.0);
        level.addParticle(ParticleTypes.FLAME, d0, d1, d2, 0.0, 0.0, 0.0);

        super.animateTick(state, level, pos, randomSource);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        level.getCapability(ModCapabilities.TC_REGISTRY).ifPresent(reg -> reg.registerLight(this.keyFactory.apply(pos), this.lightFactory.apply(pos)));
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        level.getCapability(ModCapabilities.TC_REGISTRY).ifPresent(reg -> reg.unregisterLight(this.keyFactory.apply(pos)));
        VolumeDisplayMessage msg = VolumeDisplayMessage.create(pos, this.range.get(), 10551072, false, false);
        ModMessageHandler.INSTANCE.send(PacketDistributor.DIMENSION.with(level::dimension), msg);
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }
}

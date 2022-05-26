package at.petrak.retrocandles.common.block;

import at.petrak.retrocandles.common.block.entity.BlockEntityTickAcceleratorCandle;
import it.unimi.dsi.fastutil.ints.IntIntImmutablePair;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BlockTickAcceleratorCandle extends BlockModCandle implements EntityBlock {
    public BlockTickAcceleratorCandle(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
            .setValue(CANDLES, 1)
            .setValue(LIT, Boolean.FALSE));
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(CANDLES)) {
            case 1 -> ONE_AABB;
            case 2 -> TWO_AABB;
            case 3 -> THREE_AABB;
            case 4 -> FOUR_AABB;
            default -> ONE_AABB;
        };
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CANDLES, LIT);
    }

    public int getTickCount(BlockState state) {
        if (!(state.getBlock() instanceof BlockTickAcceleratorCandle)) {
            return 0;
        }
        return state.getValue(CANDLES);
    }

    @Override
    public IntIntPair getRange(BlockState bs) {
        var candleCount = bs.getValue(CANDLES);
        return new IntIntImmutablePair(candleCount, candleCount / 2);
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityTickAcceleratorCandle(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
        BlockEntityType<T> blockEntityType) {
        if (state.getValue(LIT)) {
            return (world, pos, bs, tile) -> {
                if (world.isClientSide) {
                    BlockEntityTickAcceleratorCandle.clientTick(world, pos, bs,
                        (BlockEntityTickAcceleratorCandle) tile);
                } else {
                    BlockEntityTickAcceleratorCandle.serverTick(world, pos, bs,
                        (BlockEntityTickAcceleratorCandle) tile);
                }
            };
        } else {
            return null;
        }
    }
}

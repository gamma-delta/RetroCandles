package at.petrak.retrocandles.common.block;

import at.petrak.retrocandles.common.block.entity.BlockEntityTickAcceleratorCandle;
import it.unimi.dsi.fastutil.ints.IntIntImmutablePair;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BlockTickAcceleratorCandle extends BlockModCandle implements EntityBlock {

    public BlockTickAcceleratorCandle(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
            .setValue(CANDLES, 1)
            .setValue(LIT, false)
            .setValue(SEALED, false)
            .setValue(POWERED, false));
    }

    public int getTickCount(BlockState state) {
        if (state.getValue(SEALED)) {
            return state.getValue(CANDLES) + 1;
        } else {
            return 1;
        }
    }

    @Override
    public IntIntPair getRange(BlockState bs) {
        if (bs.getValue(SEALED)) {
            return new IntIntImmutablePair(1, 0);
        } else {
            int candleCount = bs.getValue(CANDLES);
            return new IntIntImmutablePair(candleCount, candleCount / 2);
        }
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

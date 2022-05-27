package at.petrak.retrocandles.common.block;

import at.petrak.retrocandles.common.block.entity.BlockEntityInterdictionCandle;
import it.unimi.dsi.fastutil.ints.IntIntImmutablePair;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class BlockInterdictionCandle extends BlockModCandle implements EntityBlock {

    public BlockInterdictionCandle(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
            .setValue(CANDLES, 1)
            .setValue(LIT, false)
            .setValue(SEALED, false)
            .setValue(POWERED, false));
    }

    @Override
    public IntIntPair getRange(BlockState bs) {
        int candleCount = bs.getValue(CANDLES);
        return new IntIntImmutablePair(1 + candleCount * 2, candleCount);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityInterdictionCandle(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
        BlockEntityType<T> blockEntityType) {
        if (state.getValue(LIT) && !level.isClientSide) {
            return (world, pos, bs, tile) ->
                BlockEntityInterdictionCandle.serverTick(world, pos, bs, (BlockEntityInterdictionCandle) tile);
        } else {
            return null;
        }
    }

    /**
     * Positive means go away, negative means go towards.
     */
    public int pushMagnitude(Entity target, Vec3 center, int radius, boolean isSealed) {
        if (target instanceof Enemy || (target instanceof Projectile proj && proj.getOwner() instanceof Enemy)) {
            var dsq = target.distanceToSqr(center);
            if (dsq <= radius * radius) {
                return isSealed ? -1 : 1;
            }
        }

        return 0;
    }
}

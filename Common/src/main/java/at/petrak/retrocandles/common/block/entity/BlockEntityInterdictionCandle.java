package at.petrak.retrocandles.common.block.entity;

import at.petrak.paucal.api.PaucalBlockEntity;
import at.petrak.retrocandles.common.block.BlockInterdictionCandle;
import at.petrak.retrocandles.lib.ModBlockEntities;
import at.petrak.retrocandles.lib.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class BlockEntityInterdictionCandle extends PaucalBlockEntity {
    public BlockEntityInterdictionCandle(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntities.INTERDICTION_CANDLE, worldPosition, blockState);
    }

    public static void serverTick(Level world, BlockPos pos, BlockState bs, BlockEntityInterdictionCandle tile) {
        boolean isSealed = bs.getValue(BlockInterdictionCandle.SEALED);
        var range = ModBlocks.INTERDICTION_CANDLE.getRange(bs);
        Vec3 centerPos = Vec3.atCenterOf(pos);
        int slop = isSealed ? 4 : 2;
        var aabb = AABB.ofSize(centerPos, range.firstInt() + slop, range.secondInt() + slop, range.firstInt() + slop);

        var interlopers = world.getEntitiesOfClass(Entity.class, aabb);
        for (var e : interlopers) {
            var magnitude = ModBlocks.INTERDICTION_CANDLE.pushMagnitude(
                e, centerPos, range.firstInt(), isSealed);
            if (magnitude == 0) {
                continue;
            }

            var entityDelta = new Vec3(e.getX() - centerPos.x, 0, e.getZ() - centerPos.z);
            var velSimilarity = Mth.clamp(e.getDeltaMovement().dot(entityDelta) / 2f, 0.05, 0.2);

            Vec3 proposedVel = entityDelta.scale(velSimilarity * magnitude);
            e.setDeltaMovement(new Vec3(proposedVel.x, e.getDeltaMovement().y, proposedVel.z));
        }
    }

    @Override
    protected void saveModData(CompoundTag tag) {
    }

    @Override
    protected void loadModData(CompoundTag tag) {
    }
}

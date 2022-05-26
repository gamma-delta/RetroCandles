package at.petrak.retrocandles.common.block.entity;

import at.petrak.paucal.api.PaucalBlockEntity;
import at.petrak.retrocandles.RetroCandlesConfig;
import at.petrak.retrocandles.common.block.BlockModCandle;
import at.petrak.retrocandles.lib.ModBlockEntities;
import at.petrak.retrocandles.lib.ModBlocks;
import at.petrak.retrocandles.xplat.IXplatAbstractions;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityTickAcceleratorCandle extends PaucalBlockEntity {
    public BlockEntityTickAcceleratorCandle(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntities.TICKING_CANDLE, worldPosition, blockState);
    }

    public static void clientTick(Level world, BlockPos pos, BlockState bs, BlockEntityTickAcceleratorCandle tile) {
        var tickCount = ModBlocks.TICKING_CANDLE.getTickCount(bs);
        if (tickCount == 0) {
            return;
        }
        var range = ModBlocks.TICKING_CANDLE.getRange(bs);
        var denylist = RetroCandlesConfig.common().tickableBlocksDenyList();
        BlockModCandle.rangeHelper(range, pos, posHere -> {
            var bsHere = world.getBlockState(posHere);
            Block blockHere = bsHere.getBlock();

            if (!denylist.contains(IXplatAbstractions.INSTANCE.getID(blockHere).toString())) {
                for (int i = 0; i < tickCount; i++) {
                    blockHere.animateTick(bsHere, world, posHere, world.getRandom());
                }
            }
        });
    }

    public static void serverTick(Level world, BlockPos pos, BlockState bs, BlockEntityTickAcceleratorCandle tile) {
        var tickCount = ModBlocks.TICKING_CANDLE.getTickCount(bs);
        if (tickCount == 0) {
            return;
        }
        var sworld = (ServerLevel) world;
        var range = ModBlocks.TICKING_CANDLE.getRange(bs);
        var denylist = RetroCandlesConfig.common().tickableBlocksDenyList();
        BlockModCandle.rangeHelper(range, pos, posHere -> {
            var bsHere = world.getBlockState(posHere);
            Block blockHere = bsHere.getBlock();

            if (!denylist.contains(IXplatAbstractions.INSTANCE.getID(blockHere).toString())) {
                if (bsHere.isRandomlyTicking()
                    && sworld.random.nextInt(64)
                    < tickCount * world.getGameRules().getInt(GameRules.RULE_RANDOMTICKING)) {
                    bsHere.randomTick(sworld, posHere, sworld.getRandom());
                }

                var tileHere = world.getBlockEntity(posHere);
                if (tileHere != null && blockHere instanceof EntityBlock tileBlock) {
                    var ticker = (BlockEntityTicker<BlockEntity>)
                        tileBlock.getTicker(sworld, bsHere, tileHere.getType());
                    if (ticker != null) {
                        for (int i = 0; !tileHere.isRemoved() && i < tickCount; i++) {
                            ticker.tick(world, posHere, bsHere, tileHere);
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void saveModData(CompoundTag tag) {

    }

    @Override
    protected void loadModData(CompoundTag tag) {

    }
}

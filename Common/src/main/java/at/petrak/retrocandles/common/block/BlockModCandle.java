package at.petrak.retrocandles.common.block;

import at.petrak.retrocandles.lib.ModItems;
import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.ToIntFunction;

abstract public class BlockModCandle extends Block {
    public BlockModCandle(Properties props) {
        super(props);
    }

    public static final IntegerProperty CANDLES = BlockStateProperties.CANDLES;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final BooleanProperty SEALED = BooleanProperty.create("sealed");
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public static final ToIntFunction<BlockState> LIGHT_EMISSION = bs ->
        bs.getValue(LIT) ? 3 * bs.getValue(CANDLES) : 0;

    // VanillaCopy
    public static final Int2ObjectMap<List<Vec3>> PARTICLE_OFFSETS = Util.make(() -> {
        Int2ObjectMap<List<Vec3>> int2objectmap = new Int2ObjectOpenHashMap<>();
        int2objectmap.defaultReturnValue(ImmutableList.of());
        int2objectmap.put(1, ImmutableList.of(new Vec3(0.5D, 0.5D, 0.5D)));
        int2objectmap.put(2, ImmutableList.of(new Vec3(0.375D, 0.44D, 0.5D), new Vec3(0.625D, 0.5D, 0.44D)));
        int2objectmap.put(3, ImmutableList.of(new Vec3(0.5D, 0.313D, 0.625D), new Vec3(0.375D, 0.44D, 0.5D),
            new Vec3(0.56D, 0.5D, 0.44D)));
        int2objectmap.put(4, ImmutableList.of(new Vec3(0.44D, 0.313D, 0.56D), new Vec3(0.625D, 0.44D, 0.56D),
            new Vec3(0.375D, 0.44D, 0.375D), new Vec3(0.56D, 0.5D, 0.375D)));
        return Int2ObjectMaps.unmodifiable(int2objectmap);
    });
    protected static final VoxelShape ONE_AABB = Block.box(7.0D, 0.0D, 7.0D, 9.0D, 6.0D, 9.0D);
    protected static final VoxelShape TWO_AABB = Block.box(5.0D, 0.0D, 6.0D, 11.0D, 6.0D, 9.0D);
    protected static final VoxelShape THREE_AABB = Block.box(5.0D, 0.0D, 6.0D, 10.0D, 6.0D, 11.0D);
    protected static final VoxelShape FOUR_AABB = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 6.0D, 10.0D);

    public List<Vec3> particleOffsets(BlockState bs) {
        return PARTICLE_OFFSETS.get(bs.getValue(CANDLES).intValue());
    }

    public ParticleOptions setAlightParticle(BlockState bs) {
        return ParticleTypes.FLAME;
    }

    public ParticleOptions litParticle(BlockState bs) {
        return ParticleTypes.SMALL_FLAME;
    }

    public ParticleOptions extinguishParticle(BlockState bs) {
        return ParticleTypes.LARGE_SMOKE;
    }

    /**
     * Return {@code (horiz, vert)}.
     */
    abstract public IntIntPair getRange(BlockState bs);

    public static void rangeHelper(IntIntPair range, BlockPos pos, Consumer<BlockPos> action) {
        for (var dx = -range.firstInt(); dx <= range.firstInt(); dx++) {
            for (var dz = -range.firstInt(); dz <= range.firstInt(); dz++) {
                for (var dy = -range.secondInt(); dy <= range.secondInt(); dy++) {
                    action.accept(pos.offset(dx, dy, dz));
                }
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CANDLES, LIT, SEALED, POWERED);
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext ctx) {
        return !ctx.isSecondaryUseActive()
            && ctx.getItemInHand().getItem() == this.asItem()
            && (state.getValue(CANDLES) < 4 || super.canBeReplaced(state, ctx));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState bs = ctx.getLevel().getBlockState(ctx.getClickedPos());
        if (bs.is(this)) {
            // Via canBeReplaced, this will never cycle this back around to 0.
            return bs.cycle(CANDLES);
        } else {
            return super.getStateForPlacement(ctx);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand,
        BlockHitResult hit) {
        var handStack = player.getItemInHand(hand);
        if (hand == InteractionHand.MAIN_HAND && handStack.isEmpty()) {
            if (player.isDiscrete()) {
                if (state.getValue(SEALED)) {
                    level.setBlockAndUpdate(pos, state.setValue(SEALED, false));
                    level.playSound(player, pos, SoundEvents.SLIME_SQUISH_SMALL, SoundSource.BLOCKS, 1f, 1.1f);

                    var dropWax = level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)
                        && !player.getAbilities().instabuild;
                    if (dropWax) {
                        DefaultDispenseItemBehavior.spawnItem(
                            level, new ItemStack(ModItems.SEALING_WAX), 3, Direction.UP, Vec3.atCenterOf(pos));
                    }
                    return InteractionResult.SUCCESS;
                } else {
                    return InteractionResult.PASS;
                }
            } else {
                var isLit = state.getValue(LIT);
                setLit(!isLit, state, pos, level, player);
                return InteractionResult.SUCCESS;
            }
        }

        if (handStack.is(ModItems.SEALING_WAX) && !state.getValue(SEALED)) {
            if (!player.getAbilities().instabuild) {
                handStack.shrink(1);
            }
            player.awardStat(Stats.ITEM_USED.get(ModItems.SEALING_WAX));
            level.setBlockAndUpdate(pos, state.setValue(SEALED, true));
            level.playSound(player, pos, SoundEvents.SLIME_SQUISH_SMALL, SoundSource.BLOCKS, 1f, 1.15f);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }


    @Override
    public void animateTick(BlockState bs, Level level, BlockPos pos, Random random) {
        if (bs.getValue(LIT)) {
            var anyFlame = false;
            for (Vec3 particleOffset : this.particleOffsets(bs)) {
                if (random.nextFloat() < 0.7f) {
                    anyFlame = true;

                    var particlePos = Vec3.atLowerCornerOf(pos).add(particleOffset);
                    level.addParticle(this.litParticle(bs), particlePos.x, particlePos.y, particlePos.z, 0, 0, 0);
                }
            }

            if (anyFlame && random.nextFloat() < 0.5f) {
                level.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    SoundEvents.CANDLE_AMBIENT, SoundSource.BLOCKS,
                    1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F,
                    false);
            }
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos,
        boolean isMoving) {
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);

        if (!level.isClientSide) {
            var isNowPowered = level.hasNeighborSignal(pos);
            if (isNowPowered != state.getValue(POWERED)) {
                if (state.getValue(LIT) != isNowPowered) {
                    this.setLit(isNowPowered, state, pos, level, null);
                }
                // this does sync twice but whatever
                level.setBlock(pos, level.getBlockState(pos).setValue(POWERED, isNowPowered), 2);
            }
        }
    }

    @Override
    public boolean canSurvive(BlockState $$0, LevelReader $$1, BlockPos $$2) {
        return Block.canSupportCenter($$1, $$2.below(), Direction.UP);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(CANDLES)) {
            case 1 -> ONE_AABB;
            case 2 -> TWO_AABB;
            case 3 -> THREE_AABB;
            case 4 -> FOUR_AABB;
            default -> ONE_AABB;
        };
    }

    public boolean setLit(boolean makeLit, BlockState original, BlockPos pos, Level world, @Nullable Player lighter) {
        var alreadyLit = original.getValue(LIT);
        if (alreadyLit == makeLit) {
            return false;
        }
        BlockState bs = original.setValue(LIT, makeLit);
        world.setBlockAndUpdate(pos, bs);

        var sound = makeLit ? SoundEvents.FLINTANDSTEEL_USE : SoundEvents.CANDLE_EXTINGUISH;
        world.playSound(lighter, pos, sound, SoundSource.BLOCKS, 1f, 1f);

        var particle = makeLit ? this.setAlightParticle(bs) : this.extinguishParticle(bs);
        for (var particleOffset : this.particleOffsets(original)) {
            for (int i = world.random.nextInt(3, 5); i >= 0; i--) {
                var particlePos = Vec3.atLowerCornerOf(pos).add(particleOffset);
                world.addParticle(particle, particlePos.x, particlePos.y, particlePos.z,
                    world.random.nextDouble(-0.01, 0.01),
                    world.random.nextDouble(0, 0.02),
                    world.random.nextDouble(-0.01, 0.01));
            }
        }

        return true;
    }
}

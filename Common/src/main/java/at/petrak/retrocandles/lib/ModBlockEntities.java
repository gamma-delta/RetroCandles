package at.petrak.retrocandles.lib;

import at.petrak.retrocandles.common.block.entity.BlockEntityInterdictionCandle;
import at.petrak.retrocandles.common.block.entity.BlockEntityTickAcceleratorCandle;
import at.petrak.retrocandles.xplat.IXplatAbstractions;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static at.petrak.retrocandles.api.RetroCandlesAPI.modLoc;

public class ModBlockEntities {
    public static void registerBlockEntities(BiConsumer<BlockEntityType<?>, ResourceLocation> r) {
        for (var e : BLOCK_ENTITIES.entrySet()) {
            r.accept(e.getValue(), e.getKey());
        }
    }

    private static final Map<ResourceLocation, BlockEntityType<?>> BLOCK_ENTITIES = new LinkedHashMap<>();

    public static final BlockEntityType<BlockEntityTickAcceleratorCandle> TICKING_CANDLE = register(
        "tick_accelerator_candle_tile",
        BlockEntityTickAcceleratorCandle::new, ModBlocks.TICKING_CANDLE);
    public static final BlockEntityType<BlockEntityInterdictionCandle> INTERDICTION_CANDLE = register(
        "interdiction_candle_tile",
        BlockEntityInterdictionCandle::new, ModBlocks.INTERDICTION_CANDLE);

    private static <T extends BlockEntity> BlockEntityType<T> register(String id,
        BiFunction<BlockPos, BlockState, T> func, Block... blocks) {
        var ret = IXplatAbstractions.INSTANCE.createBlockEntityType(func, blocks);
        var old = BLOCK_ENTITIES.put(modLoc(id), ret);
        if (old != null) {
            throw new IllegalArgumentException("Duplicate id " + id);
        }
        return ret;
    }
}

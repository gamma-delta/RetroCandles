package at.petrak.retrocandles.lib;

import at.petrak.retrocandles.common.block.BlockInterdictionCandle;
import at.petrak.retrocandles.common.block.BlockModCandle;
import at.petrak.retrocandles.common.block.BlockTickAcceleratorCandle;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static at.petrak.retrocandles.api.RetroCandlesAPI.modLoc;

public class ModBlocks {
    public static void registerBlocks(BiConsumer<Block, ResourceLocation> r) {
        for (var e : BLOCKS.entrySet()) {
            r.accept(e.getValue(), e.getKey());
        }
    }

    public static void registerBlockItems(BiConsumer<Item, ResourceLocation> r) {
        for (var e : BLOCK_ITEMS.entrySet()) {
            r.accept(new BlockItem(e.getValue().getFirst(), e.getValue().getSecond()), e.getKey());
        }
    }

    private static final Map<ResourceLocation, Block> BLOCKS = new LinkedHashMap<>();
    private static final Map<ResourceLocation, Pair<Block, Item.Properties>> BLOCK_ITEMS = new LinkedHashMap<>();

    private static BlockBehaviour.Properties candleProps(MaterialColor color) {
        return BlockBehaviour.Properties.of(Material.DECORATION, color)
            .noOcclusion()
            .strength(0.3F)
            .sound(SoundType.CANDLE)
            .lightLevel(BlockModCandle.LIGHT_EMISSION);
    }

    public static final BlockTickAcceleratorCandle TICKING_CANDLE = blockItem("tick_accelerator_candle",
        new BlockTickAcceleratorCandle(candleProps(MaterialColor.COLOR_BLUE)));
    public static final BlockInterdictionCandle INTERDICTION_CANDLE = blockItem("interdiction_candle",
        new BlockInterdictionCandle(candleProps(MaterialColor.COLOR_BLUE)));

    private static <T extends Block> T blockNoItem(String name, T block) {
        var old = BLOCKS.put(modLoc(name), block);
        if (old != null) {
            throw new IllegalArgumentException("Typo? Duplicate id " + name);
        }
        return block;
    }

    private static <T extends Block> T blockItem(String name, T block) {
        return blockItem(name, block, ModItems.props());
    }

    private static <T extends Block> T blockItem(String name, T block, Item.Properties props) {
        blockNoItem(name, block);
        var old = BLOCK_ITEMS.put(modLoc(name), new Pair<>(block, props));
        if (old != null) {
            throw new IllegalArgumentException("Typo? Duplicate id " + name);
        }
        return block;
    }
}

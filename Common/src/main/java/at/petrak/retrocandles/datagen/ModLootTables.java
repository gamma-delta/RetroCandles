package at.petrak.retrocandles.datagen;

import at.petrak.paucal.api.datagen.PaucalLootTableProvider;
import at.petrak.retrocandles.common.block.BlockModCandle;
import at.petrak.retrocandles.lib.ModBlocks;
import at.petrak.retrocandles.lib.ModItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.Map;

public class ModLootTables extends PaucalLootTableProvider {
    public ModLootTables(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void makeLootTables(Map<Block, LootTable.Builder> blockTables,
        Map<ResourceLocation, LootTable.Builder> lootTables) {
        candle(ModBlocks.TICKING_CANDLE, blockTables);
        candle(ModBlocks.INTERDICTION_CANDLE, blockTables);
    }

    protected void candle(BlockModCandle candle, Map<Block, LootTable.Builder> blockTables) {
        var table = LootTable.lootTable();
        for (int i = 1; i <= 4; i++) {
            var candlePool = LootPool.lootPool()
                .add(LootItem.lootTableItem(candle))
                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(candle)
                    .setProperties(
                        StatePropertiesPredicate.Builder.properties().hasProperty(BlockModCandle.CANDLES, i)))
                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(i)));
            table.withPool(candlePool);
        }
        var waxPool = LootPool.lootPool()
            .add(LootItem.lootTableItem(ModItems.SEALING_WAX))
            .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(candle)
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(BlockModCandle.SEALED, true)));
        table.withPool(waxPool);

        blockTables.put(candle, table);
    }
}

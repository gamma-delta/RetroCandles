package at.petrak.retrocandles.lib;

import at.petrak.retrocandles.xplat.IXplatAbstractions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static at.petrak.retrocandles.api.RetroCandlesAPI.modLoc;

public class ModItems {
    public static void registerItems(BiConsumer<Item, ResourceLocation> r) {
        for (var e : ITEMS.entrySet()) {
            r.accept(e.getValue(), e.getKey());
        }
    }

    private static final Map<ResourceLocation, Item> ITEMS = new LinkedHashMap<>(); // preserve insertion order

    public static Item.Properties props() {
        return new Item.Properties().tab(IXplatAbstractions.INSTANCE.getTab());
    }

    public static final Item SEALING_WAX = register("sealing_wax", new Item(props()));

    private static <T extends Item> T register(String id, T item) {
        var old = ITEMS.put(modLoc(id), item);
        if (old != null) {
            throw new IllegalArgumentException("Typo? Duplicate id " + id);
        }
        return item;
    }

    public static ItemStack tabIcon() {
        return new ItemStack(ModBlocks.TICKING_CANDLE);
    }
}

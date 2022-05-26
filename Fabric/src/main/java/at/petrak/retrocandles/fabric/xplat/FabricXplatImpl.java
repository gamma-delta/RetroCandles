package at.petrak.retrocandles.fabric.xplat;

import at.petrak.retrocandles.lib.ModItems;
import at.petrak.retrocandles.xplat.IXplatAbstractions;
import at.petrak.retrocandles.xplat.Platform;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiFunction;

import static at.petrak.retrocandles.api.RetroCandlesAPI.modLoc;

public class FabricXplatImpl implements IXplatAbstractions {
    @Override
    public Platform platform() {
        return Platform.FABRIC;
    }

    @Override
    public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BiFunction<BlockPos, BlockState, T> func,
        Block... blocks) {
        return FabricBlockEntityTypeBuilder.create(func::apply, blocks).build();
    }

    @Override
    public ResourceLocation getID(Block block) {
        return Registry.BLOCK.getKey(block);
    }

    private static CreativeModeTab TAB = null;

    @Override
    public CreativeModeTab getTab() {
        if (TAB == null) {
            TAB = FabricItemGroupBuilder.create(modLoc("creative_tab"))
                .icon(ModItems::tabIcon)
                .build();
        }

        return TAB;
    }
}

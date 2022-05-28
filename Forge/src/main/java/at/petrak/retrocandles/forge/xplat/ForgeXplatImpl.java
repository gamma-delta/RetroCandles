package at.petrak.retrocandles.forge.xplat;

import at.petrak.retrocandles.api.RetroCandlesAPI;
import at.petrak.retrocandles.lib.ModItems;
import at.petrak.retrocandles.xplat.IXplatAbstractions;
import at.petrak.retrocandles.xplat.Platform;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiFunction;

public class ForgeXplatImpl implements IXplatAbstractions {
    @Override
    public Platform platform() {
        return Platform.FORGE;
    }

    @Override
    public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BiFunction<BlockPos, BlockState, T> func,
        Block... blocks) {
        return BlockEntityType.Builder.of(func::apply, blocks).build(null);
    }

    @Override
    public ResourceLocation getID(Block block) {
        return block.getRegistryName();
    }

    private static CreativeModeTab TAB = null;

    @Override
    public CreativeModeTab getTab() {
        if (TAB == null) {
            TAB = new CreativeModeTab(RetroCandlesAPI.MOD_ID + ".creative_tab") {
                @Override
                public ItemStack makeIcon() {
                    return ModItems.tabIcon();
                }

                @Override
                public void fillItemList(NonNullList<ItemStack> p_40778_) {
                    super.fillItemList(p_40778_);
                }
            };
        }

        return TAB;
    }
}

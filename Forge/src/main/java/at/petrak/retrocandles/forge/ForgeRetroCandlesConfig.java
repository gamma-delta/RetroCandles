package at.petrak.retrocandles.forge;

import at.petrak.retrocandles.RetroCandlesConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;

public class ForgeRetroCandlesConfig implements RetroCandlesConfig.ConfigAccess {
    private static ForgeConfigSpec.ConfigValue<List<? extends String>> tickableBlocksDenyList;
    private static List<String> tickableBlocksDenyListButWithoutForgeBeingBad = null;

    public ForgeRetroCandlesConfig(ForgeConfigSpec.Builder builder) {
        tickableBlocksDenyList = builder.defineList("tickableBlocksDenyList",
            DEFAULT_TICKABLE_BLOCKS_DENYLIST,
            o -> o instanceof String s && ResourceLocation.isValidResourceLocation(s));
    }

    @Override
    public List<String> tickableBlocksDenyList() {
        if (tickableBlocksDenyListButWithoutForgeBeingBad == null) {
            tickableBlocksDenyListButWithoutForgeBeingBad = new ArrayList<>();
            tickableBlocksDenyListButWithoutForgeBeingBad.addAll(tickableBlocksDenyList.get());
        }
        return tickableBlocksDenyListButWithoutForgeBeingBad;
    }
}

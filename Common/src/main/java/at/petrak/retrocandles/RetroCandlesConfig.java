package at.petrak.retrocandles;


import at.petrak.retrocandles.api.RetroCandlesAPI;

import java.util.List;

public class RetroCandlesConfig {
    public interface ConfigAccess {
        List<String> tickableBlocksDenyList();

        List<String> DEFAULT_TICKABLE_BLOCKS_DENYLIST = List.of(
            RetroCandlesAPI.MOD_ID + ":tick_accelerator_candle"
        );
    }

    private static ConfigAccess common = null;

    public static ConfigAccess common() {
        return common;
    }

    public static void setCommon(ConfigAccess access) {
        if (common != null) {
            RetroCandlesAPI.LOGGER.warn("CommonConfigAccess was replaced! Old {} New {}", common.getClass().getName(),
                access.getClass().getName());
        }
        common = access;
    }
}

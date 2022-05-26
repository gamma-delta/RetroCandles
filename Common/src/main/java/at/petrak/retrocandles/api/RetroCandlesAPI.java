package at.petrak.retrocandles.api;

import com.google.common.base.Suppliers;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public interface RetroCandlesAPI {
    String MOD_ID = "retrocandles";
    Logger LOGGER = LogManager.getLogger(MOD_ID);

    Supplier<RetroCandlesAPI> INSTANCE = Suppliers.memoize(() -> {
        try {
            return (RetroCandlesAPI) Class.forName("at.petrak.retrocandles.common.impl.RetroCandlesAPIImpl")
                .getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            LogManager.getLogger().warn("Unable to find RetroCandlesAPIImpl, using a dummy");
            return new RetroCandlesAPI() {
            };
        }
    });

    static RetroCandlesAPI instance() {
        return INSTANCE.get();
    }

    static ResourceLocation modLoc(String s) {
        return new ResourceLocation(MOD_ID, s);
    }
}

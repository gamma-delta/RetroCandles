package at.petrak.retrocandles.fabric;

import at.petrak.retrocandles.RetroCandlesConfig;
import at.petrak.retrocandles.api.RetroCandlesAPI;
import io.github.fablabsmc.fablabs.api.fiber.v1.builder.ConfigTreeBuilder;
import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;

import java.io.*;
import java.nio.file.*;
import java.util.List;

public class FabricRetroCandlesConfig {
    private static Common COMMON = new Common();

    private static void writeDefaultConfig(ConfigTree config, Path path, JanksonValueSerializer serializer) {
        try (OutputStream s = new BufferedOutputStream(
            Files.newOutputStream(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW))) {
            FiberSerialization.serialize(config, s, serializer);
        } catch (FileAlreadyExistsException ignored) {
        } catch (IOException e) {
            RetroCandlesAPI.LOGGER.error("Error writing default config", e);
        }
    }

    private static void setupConfig(ConfigTree config, Path p, JanksonValueSerializer serializer) {
        writeDefaultConfig(config, p, serializer);

        try (InputStream s = new BufferedInputStream(
            Files.newInputStream(p, StandardOpenOption.READ, StandardOpenOption.CREATE))) {
            FiberSerialization.deserialize(config, s, serializer);
        } catch (IOException | ValueDeserializationException e) {
            RetroCandlesAPI.LOGGER.error("Error loading config from {}", p, e);
        }
    }

    public static void setup() {
        try {
            Files.createDirectory(Paths.get("config"));
        } catch (FileAlreadyExistsException ignored) {
        } catch (IOException e) {
            RetroCandlesAPI.LOGGER.warn("Failed to make config dir", e);
        }

        var serializer = new JanksonValueSerializer(false);
        var common = COMMON.configure(ConfigTree.builder());
        setupConfig(common, Paths.get("config", RetroCandlesAPI.MOD_ID + "-common.json5"), serializer);
        RetroCandlesConfig.setCommon(COMMON);
    }

    private static final class Common implements RetroCandlesConfig.ConfigAccess {
        private final PropertyMirror<List<String>> tickableBlocksDenyList = PropertyMirror.create(
            ConfigTypes.makeList(ConfigTypes.STRING));

        public ConfigTree configure(ConfigTreeBuilder bob) {
            bob
                .beginValue("tickableBlocksDenyList", ConfigTypes.makeList(ConfigTypes.STRING),
                    DEFAULT_TICKABLE_BLOCKS_DENYLIST)
                .finishValue(tickableBlocksDenyList::mirror);

            return bob.build();
        }

        @Override
        public List<String> tickableBlocksDenyList() {
            return tickableBlocksDenyList.getValue();
        }
    }
}

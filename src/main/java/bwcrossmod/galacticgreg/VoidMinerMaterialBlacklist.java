package bwcrossmod.galacticgreg;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.ShapeBlock;
import com.ruling_0.materiallib.api.StackResolver;

import bartworks.common.configs.Configuration;
import cpw.mods.fml.common.registry.GameRegistry;

/// Turns the void miner blacklist, configured by material name, into the `modid:blockname:meta` keys
/// [VoidMinerUtility.DropMap] filters drops against. Ore metadata is the material's registry index, which shifts
/// whenever the material set changes, so the config names materials instead.
///
/// Call no earlier than gregtech postInit.
public final class VoidMinerMaterialBlacklist {

    private static final Logger LOGGER = LogManager.getLogger(VoidMinerMaterialBlacklist.class);

    private static final String[] ORE_SHAPE_TOKENS = { "ore", "oreSmall" };

    private static Set<String> cached;

    private VoidMinerMaterialBlacklist() {}

    /// Returns the unmodifiable set of block keys blacklisted by
    /// [Configuration.Multiblocks#voidMinerBlacklistMaterials], resolved on the first call.
    public static Set<String> cached() {
        if (cached == null) cached = resolve();
        return cached;
    }

    private static Set<String> resolve() {
        String[] entries = Configuration.multiblocks.voidMinerBlacklistMaterials;
        if (entries.length == 0) return Collections.emptySet();

        Set<String> keys = new HashSet<>();
        int invalid = 0;

        for (String entry : entries) {
            if (!addEntry(entry, keys)) invalid++;
        }

        LOGGER.info(
            "Void miner material blacklist: {} entries resolved to {} block keys ({} invalid)",
            entries.length - invalid,
            keys.size(),
            invalid);
        return Collections.unmodifiableSet(keys);
    }

    /// Adds the keys of one config entry, either `<Material>` over the `ore` and `oreSmall` shapes or
    /// `<Material>:<shapeToken>` over one shape. Returns false when the entry names no registered material or matches
    /// no ore block.
    private static boolean addEntry(String entry, Set<String> keys) {
        int separator = entry.indexOf(':');
        Material material = StackResolver.getMaterial(separator == -1 ? entry : entry.substring(0, separator));
        if (material == null) return false;

        int matched = 0;
        if (separator == -1) {
            for (String token : ORE_SHAPE_TOKENS) matched += addShapeKeys(token, material, keys);
        } else {
            matched = addShapeKeys(entry.substring(separator + 1), material, keys);
        }

        if (matched == 0) {
            LOGGER.error("Void miner material blacklist entry \"{}\" matches no ore block", entry);
            return false;
        }
        return true;
    }

    private static int addShapeKeys(String shapeToken, Material material, Set<String> keys) {
        int matched = 0;
        for (ShapeBlock block : StackResolver.getBlockShapes(shapeToken)) {
            if (!serves(block, material)) continue;
            keys.add(
                String.format(
                    "%s:%d",
                    GameRegistry.findUniqueIdentifierFor(block)
                        .toString(),
                    material.getIndex()));
            matched++;
        }
        return matched;
    }

    private static boolean serves(ShapeBlock block, Material material) {
        for (Material served : block.getServedMaterials()) {
            if (served == material) return true;
        }
        return false;
    }
}

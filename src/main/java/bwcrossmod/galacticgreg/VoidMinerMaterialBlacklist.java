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

/// The void miner blacklist named by material rather than by block metadata, resolved into the
/// `modid:blockname:meta` keys [VoidMinerUtility.DropMap] filters drops against. An ore block's metadata is the
/// material's registry index, which moves whenever the material set changes, hence the indirection.
///
/// Resolution needs the MaterialLib registries, so the first call must come no earlier than the drop map build at
/// FMLLoadComplete.
public final class VoidMinerMaterialBlacklist {

    private static final Logger LOGGER = LogManager.getLogger(VoidMinerMaterialBlacklist.class);

    private static final String[] ORE_SHAPE_TOKENS = { "ore", "oreSmall" };

    private static Set<String> cached;

    private VoidMinerMaterialBlacklist() {}

    /// The blacklisted block keys from [Configuration.Multiblocks#voidMinerBlacklistMaterials], resolved on the
    /// first call and kept for the session.
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

    /// Adds the keys of one config entry, either `<Material>` over every ore shape or `<Material>:<shapeToken>`
    /// over one. False when the entry names no registered material or matches no ore block.
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

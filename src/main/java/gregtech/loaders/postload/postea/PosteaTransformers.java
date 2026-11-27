package gregtech.loaders.postload.postea;

import static gregtech.GTLoggers.GT_FML_LOGGER;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.Mods;
import gregtech.loaders.postload.ForeignPartTable;
import gregtech.loaders.postload.GtppItemCutoverTable;
import gregtech.loaders.postload.GtppOreCutoverTable;
import gregtech.loaders.postload.LegacyPipeCutoverTable;

public final class PosteaTransformers {

    private static final int MAX_REPORTED_UNRESOLVED_ROWS = 50;

    private PosteaTransformers() {}

    public static void register() {
        validateCutoverTables();
        GregTechTransformers.register();
        CoreModTransformers.register();
        BartworksTransformers.register();
        IC2Transformers.register();
        GTPlusPlusTransformers.register();
        ForeignPartTransformers.register();
        RailcraftTransformers.register();
        KubaTechTransformers.register();
        OreTransformers.register();
        if (Mods.ForgeMicroblocks.isModLoaded()) ForgeMicroblockTransformers.register();
        GT_FML_LOGGER
            .debug("PosteaTransformers: claimed missing mappings for {} legacy ids", Transformers.countClaimedIds());
    }

    /// Logs the gtPlusPlus item and ore, foreign part and pipe cutover rows that do not resolve to a MaterialLib
    /// stack or block. Every row is expected to resolve. A row that deliberately never resolves is documented as
    /// such in its table's javadoc.
    private static void validateCutoverTables() {
        List<String> unresolved = new ArrayList<>();

        for (GtppItemCutoverTable.Entry entry : GtppItemCutoverTable.ENTRIES) {
            if (GTPlusPlusTransformers.resolveGtppCutoverStack(entry) == null) unresolved.add(entry.registryName());
        }
        for (GtppOreCutoverTable.Entry entry : GtppOreCutoverTable.ENTRIES) {
            if (GTPlusPlusTransformers.resolveGtppOreCutoverStack(entry) == null) unresolved.add(entry.registryName());
        }
        for (ForeignPartTable.Entry entry : ForeignPartTable.ENTRIES) {
            if (ForeignPartTransformers.resolveForeignPartStack(entry) == null) {
                unresolved.add(entry.registryName() + "@" + entry.meta());
            }
        }
        for (Map.Entry<Integer, LegacyPipeCutoverTable.Entry> row : LegacyPipeCutoverTable.entries()
            .entrySet()) {
            LegacyPipeCutoverTable.Entry entry = row.getValue();
            if (MaterialLibAPI.getBlock(entry.shape()) == null || !entry.material()
                .hasShape(entry.shape())) {
                unresolved.add(
                    "pipe MTE id " + row.getKey()
                        + " ("
                        + entry.material()
                            .getName()
                        + " "
                        + entry.shape()
                            .getName()
                        + ")");
            }
        }

        if (!unresolved.isEmpty()) {
            GT_FML_LOGGER.error(
                "PosteaTransformers: {} cutover rows no longer resolve: {}",
                unresolved.size(),
                unresolved.subList(0, Math.min(unresolved.size(), MAX_REPORTED_UNRESOLVED_ROWS)));
        }
    }
}

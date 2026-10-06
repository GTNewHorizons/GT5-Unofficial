package gregtech.api.material;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.ruling_0.materiallib.api.IconSet;
import com.ruling_0.materiallib.api.MaterialLibClient;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/// Per-material art that GregTech draws itself and no item or block shape owns: tool heads and handles, pipe end
/// faces, and decorative cover faces. Each [IconSet] resolves through the material's texture set the same way a
/// shape's icons do.
///
/// The names are plain strings so common code can check them on a dedicated server, where MaterialLib's
/// client-only icon API must not classload. [#register] creates the [IconSet]s client-side.
public final class GTMaterialIconSets {

    private GTMaterialIconSets() {}

    /// The item-atlas names, one per tool part drawn from a material in a tool's NBT.
    // spotless:off
    public static final Set<String> ITEM_NAMES = Set.of(
        "toolWrench", "toolCrowbar", "toolWireCutter", "toolScoop", "toolBranchCutter", "toolKnife",
        "toolKnifeButchery", "toolPlunger", "toolJackHammer", "toolTurbine", "toolTrowel", "handleTrowel",
        "handleFile", "handleSaw", "handleScrewdriver", "handleMallet", "toolHeadMallet", "toolHeadScrewdriver",
        "toolHeadSoldering", "toolHeadAngleGrinder", "toolHeadElectricSnips", "pocketMultiToolClosed",
        "pocketMultiToolKnife", "pocketMultiToolSaw", "pocketMultiToolFile", "pocketMultiToolScrewdriver",
        "pocketMultiToolWireCutter", "pocketMultiToolBranchCutter", "toolProspector", "toolProspectorElectricLuV",
        "toolProspectorElectricZPM", "toolProspectorElectricUV", "toolProspectorElectricUHV");
    // spotless:on

    /// The block-atlas names: the pipe and cable end face, the two decorative cover faces, the block-atlas foil
    /// cover, and the two bartworks casing faces.
    public static final Set<String> BLOCK_NAMES = Set
        .of("pipeSide", "block1", "block2", "foil", "blockCasing", "blockCasingAdvanced");

    private static final Map<String, IconSet> itemSets = new HashMap<>();
    private static final Map<String, IconSet> blockSets = new HashMap<>();

    /// Creates every icon set named above. Runs during preInit, ahead of the first texture stitch.
    @SideOnly(Side.CLIENT)
    public static void register() {
        for (String name : ITEM_NAMES) {
            itemSets.put(name, MaterialLibClient.newIconSet("gregtech", name, IconSet.Atlas.ITEMS));
        }
        for (String name : BLOCK_NAMES) {
            blockSets.put(name, MaterialLibClient.newIconSet("gregtech", name, IconSet.Atlas.BLOCKS));
        }
    }

    /// The item-atlas icon set named `name`, or null when none was declared.
    @SideOnly(Side.CLIENT)
    public static IconSet item(String name) {
        return itemSets.get(name);
    }

    /// The block-atlas icon set named `name`, or null when none was declared.
    @SideOnly(Side.CLIENT)
    public static IconSet block(String name) {
        return blockSets.get(name);
    }
}

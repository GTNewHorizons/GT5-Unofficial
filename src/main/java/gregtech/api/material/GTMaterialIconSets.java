package gregtech.api.material;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.ruling_0.materiallib.api.IconSet;
import com.ruling_0.materiallib.api.MaterialLibClient;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/// The per-material art GregTech draws itself, filed in the material texture sets under a name no item or block
/// shape owns.
///
/// An assembled [gregtech.api.items.MetaGeneratedTool] picks its head and handle art at render time from the
/// materials in its NBT, and a pipe end face or a decorative cover is an extra icon of a block that already exists,
/// so neither can hang off a shape's own icons. A [IconSet] gives that art the full texture-set resolution -- chain,
/// fallbacks, unification alternatives, `_OVERLAY` -- with no game object behind it.
///
/// The names are declared here rather than on [IconSet] handles so that common code -- notably
/// [GTMaterialIcons#hasItemIcon] -- can ask whether a name exists on a dedicated server, where MaterialLib's
/// client-only icon API must never classload. [#register] creates the handles, and is reached only from GregTech's
/// client proxy.
public final class GTMaterialIconSets {

    private GTMaterialIconSets() {}

    /// The item-atlas names: one per tool part GregTech composites from a material in a tool's NBT, plus
    /// `nanite`, whose items [gregtech.common.items.MetaGeneratedItem03] hosts rather than a shape.
    // spotless:off
    public static final Set<String> ITEM_NAMES = Set.of(
        "nanite",
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

    /// Creates every icon set named above. Called from GregTech's client proxy during preInit, which is before the
    /// first texture stitch, so the sets bind on the first stitch and on every resource reload after it.
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

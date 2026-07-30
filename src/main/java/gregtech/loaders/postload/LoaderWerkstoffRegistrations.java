package gregtech.loaders.postload;

import java.util.List;

import gregtech.api.enums.materials2.BlockShapes;
import gregtech.api.enums.materials2.Materials;
import gregtech.api.enums.materials2.Shapes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.util.BWColorUtil;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.materials2.Materials2WerkstoffIndex;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.common.ores.BWOreAdapter;
import gregtech.common.ores.OreInfo;
import gregtech.loaders.materials.LegacyNameDomain;
import gregtech.loaders.preload.LoaderLegacyBartworksBlocks;

/// Unification entries for the materials that originated in the bartworks werkstoff pools. A werkstoff-origin
/// material owns part shapes that gregtech's own material set never declared, so its stacks need the same
/// oredict association and canonical-stack registration gregtech performs for its own materials, plus the tool
/// handle pairing the part recipes read.
public class LoaderWerkstoffRegistrations {

    private LoaderWerkstoffRegistrations() {}

    public static void run() {
        for (Material material : MaterialLibAPI.getMaterials()) {
            if (material.getProperty(GTMaterialProperties.WERKSTOFF_IDS) == null) continue;
            registerLocalization(material);
            registerHandleMaterial(material);
            registerAssociations(material);
            registerLegacyCasings(material, Materials2WerkstoffIndex.idOf(material));
            registerAdditionalOreDict(material);
            MaterialUtils.recordBridgeRegistration(material);
        }
        GTOreDictUnificator.registerOre(
            "craftingIndustrialDiamond",
            MaterialLibAPI.getStack(Materials.CubicZirconia, Shapes.gemExquisite, 1));
        BWOreAdapter.INSTANCE.registerOredict();
    }

    /// The oredict entries the werkstoff part set carried beyond its own prefix names: a gem material's lens
    /// joins the dye-keyed `craftingLens` group the laser engraver selects on, and a gem or ingot material's
    /// storage block joins the name-keyed block group. A merged declaration is excluded: gregtech's own part
    /// registration owns those materials' lenses and blocks.
    private static void registerAdditionalOreDict(Material material) {
        if (Materials2WerkstoffIndex.generatesPrefix(material, OrePrefixes.gem)
            && material.hasShape(Shapes.lens)) {
            short[] rgba = MaterialUtils.rgba(material);
            if (rgba != null) {
                OreDictionary.registerOre(
                    "craftingLens" + BWColorUtil.getDyeFromColor(rgba).mName.replace(" ", ""),
                    MaterialLibAPI.getStack(material, Shapes.lens, 1));
            }
        }
        if (material.hasShape(BlockShapes.block)
            && (Materials2WerkstoffIndex.generatesPrefix(material, OrePrefixes.gem)
                || Materials2WerkstoffIndex.generatesPrefix(material, OrePrefixes.ingot))) {
            GTOreDictUnificator.registerOre(
                OrePrefixes.block + MaterialUtils.internalName(material),
                MaterialLibAPI.getStack(material, BlockShapes.block, 1));
        }
    }

    private static void registerLocalization(Material material) {
        String key = "Material." + MaterialUtils.internalName(material)
            .toLowerCase(java.util.Locale.ENGLISH);
        if (!StatCollector.canTranslate(key)) {
            GTLanguageManager.addStringLocalization(key, MaterialUtils.localName(material));
        }
    }

    /// Mirrors the tool-handle tiering the werkstoff part recipes were built against: a burning or magical
    /// material takes its themed handle, otherwise durability picks the metal. A material in the legacy name
    /// domain is skipped: gregtech's own declaration owns its handle, which the mass-based tiering behind
    /// [GTMaterialProperties#HANDLE_MATERIAL] supplies instead.
    private static void registerHandleMaterial(Material material) {
        if (LegacyNameDomain.lookup(MaterialUtils.internalName(material)) == material) return;
        Material handle;
        if (MaterialUtils.hasSubTag(material, SubTag.BURNING.mName)) handle = Materials.Blaze;
        else if (MaterialUtils.hasSubTag(material, SubTag.MAGICAL.mName)) handle = Materials.Thaumium;
        else {
            int durability = MaterialUtils.durability(material);
            handle = durability > 5120 ? Materials.TungstenSteel
                : durability > 1280 ? Materials.Steel : Materials.Wood;
        }
        MaterialUtils.recordHandleMaterial(material, handle);
    }

    /// Registers the canonical stack for every part the werkstoff set names. This reads
    /// [GTMaterialProperties#WERKSTOFF_PREFIXES] directly rather than through
    /// [Materials2WerkstoffIndex#generatesPrefix]: a material declared in both families still needs its
    /// werkstoff-side parts associated, whereas that helper deliberately yields those prefixes to gregtech so
    /// the part recipe loaders do not double-generate them.
    private static void registerAssociations(Material material) {
        List<String> prefixes = material.getProperty(GTMaterialProperties.WERKSTOFF_PREFIXES);
        if (prefixes == null) return;
        for (OrePrefixes prefix : OrePrefixes.VALUES) {
            if (!prefixes.contains(prefix.name())) continue;
            ItemStack stack = resolve(prefix, material);
            if (stack == null || stack.getItem() == null) continue;
            GTOreDictUnificator.addAssociation(prefix, material, stack, false);
            GTOreDictUnificator.set(prefix, material, stack, true, true);
        }
    }

    /// The stack a part resolves to, in the order the werkstoff item lookup used: the MaterialLib shape first,
    /// then whatever already holds the oredict entry, and finally the ore adapters for the ore prefixes, whose
    /// blocks are placed and read through the adapters rather than carried as an item shape.
    private static ItemStack resolve(OrePrefixes prefix, Material material) {
        ItemStack stack = MaterialParts.stack(prefix, material, 1);
        if (stack != null) return stack;
        stack = GTOreDictUnificator.get(prefix, material, 1);
        if (stack != null) return stack;
        if (prefix != OrePrefixes.ore && prefix != OrePrefixes.oreSmall) return null;
        try (OreInfo info = OreInfo.getNewInfo()) {
            info.material = material;
            info.isSmall = prefix == OrePrefixes.oreSmall;
            return BWOreAdapter.INSTANCE.supports(info) ? BWOreAdapter.INSTANCE.getStack(info, 1) : null;
        }
    }

    /// The legacy bartworks casing blocks stay registered for world compatibility, so they keep their
    /// association: a recipe naming one resolves to the MaterialLib casing that supersedes it, and the
    /// materials with no MaterialLib casing shape keep resolving to the legacy block they still use.
    private static void registerLegacyCasings(Material material, int id) {
        if (LoaderLegacyBartworksBlocks.casings != null) {
            GTOreDictUnificator.addAssociation(
                OrePrefixes.blockCasing,
                material,
                new ItemStack(LoaderLegacyBartworksBlocks.casings, 1, id),
                false);
        }
        if (LoaderLegacyBartworksBlocks.casingsAdvanced != null) {
            GTOreDictUnificator.addAssociation(
                OrePrefixes.blockCasingAdvanced,
                material,
                new ItemStack(LoaderLegacyBartworksBlocks.casingsAdvanced, 1, id),
                false);
        }
    }
}

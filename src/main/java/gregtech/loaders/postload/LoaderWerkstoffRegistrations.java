package gregtech.loaders.postload;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.materials2.Materials2WerkstoffIndex;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MU;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.common.ores.BWOreAdapter;
import gregtech.common.ores.OreInfo;

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
            MU.recordBridgeRegistration(material);
        }
    }

    private static void registerLocalization(Material material) {
        String key = "Material." + MU.internalName(material)
            .toLowerCase(java.util.Locale.ENGLISH);
        if (!StatCollector.canTranslate(key)) {
            GTLanguageManager.addStringLocalization(key, MU.localName(material));
        }
    }

    /// Mirrors the tool-handle tiering the werkstoff part recipes were built against: a burning or magical
    /// material takes its themed handle, otherwise durability picks the metal.
    private static void registerHandleMaterial(Material material) {
        Materials handle;
        if (MU.hasSubTag(material, SubTag.BURNING.mName)) handle = Materials.Blaze;
        else if (MU.hasSubTag(material, SubTag.MAGICAL.mName)) handle = Materials.Thaumium;
        else {
            int durability = MU.durability(material);
            handle = durability > 5120 ? Materials.TungstenSteel : durability > 1280 ? Materials.Steel : Materials.Wood;
        }
        MU.recordHandleMaterial(material, MU.material(handle));
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
        ItemStack stack = MU.stack(prefix, material, 1);
        if (stack != null) return stack;
        stack = GTOreDictUnificator.get(prefix, material, 1);
        if (stack != null) return stack;
        if (prefix != OrePrefixes.ore && prefix != OrePrefixes.oreSmall) return null;
        try (OreInfo<Material> info = OreInfo.getNewInfo()) {
            info.material = material;
            info.isSmall = prefix == OrePrefixes.oreSmall;
            return BWOreAdapter.INSTANCE.supports(info) ? BWOreAdapter.INSTANCE.getStack(info, 1) : null;
        }
    }

    /// The legacy bartworks casing blocks stay registered for world compatibility, so they keep their
    /// association: a recipe naming one resolves to the MaterialLib casing that supersedes it, and the
    /// materials with no MaterialLib casing shape keep resolving to the legacy block they still use.
    private static void registerLegacyCasings(Material material, int id) {
        if (WerkstoffLoader.BWBlockCasings != null) {
            GTOreDictUnificator.addAssociation(
                OrePrefixes.blockCasing,
                material,
                new ItemStack(WerkstoffLoader.BWBlockCasings, 1, id),
                false);
        }
        if (WerkstoffLoader.BWBlockCasingsAdvanced != null) {
            GTOreDictUnificator.addAssociation(
                OrePrefixes.blockCasingAdvanced,
                material,
                new ItemStack(WerkstoffLoader.BWBlockCasingsAdvanced, 1, id),
                false);
        }
    }
}

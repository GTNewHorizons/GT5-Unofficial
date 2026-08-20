package gregtech.loaders.preload;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;
import com.ruling_0.materiallib.api.ShapeBlock;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TieredItems;
import gregtech.api.enums.materials.LegacyMaterialIDIndex;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.TEBlockShapes;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MaterialParts;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;

/// Hands the ore-dictionary unificator MaterialLib stacks for the item cutover: every (prefix,
/// material) pair [MaterialParts] maps to a MaterialLib shape gets unified the same way
/// [gregtech.api.items.MetaGeneratedItemX32]'s constructor unifies its own generated stacks (including the
/// soldering-metal and toolbox bookkeeping), run after that constructor so the MaterialLib stack becomes
/// the unificator's preferred one.
///
/// The membership-driven prefixes get a second pass over each shape block's served materials instead. The
/// pipe-family legacy items were meta tile entities rather than generated items, so
/// `OrePrefixes#doGenerateItem` cannot gate them and their material sets live only in the MaterialLib
/// registry; `sheetmetal` additionally serves bartworks-backed materials, which carry no
/// [LegacyMaterialIDIndex] sub-id.
///
/// Each `set` call makes the shape stack the prefix's unification target and adds the material association
/// that drives the auto-generated recycling recipes.
///
/// A material whose legacy name MaterialLib cannot accept as a registration name carries it as
/// [GTMaterialProperties#LEGACY_NAME] and registers its stacks under the sanitized name instead, so its
/// legacy-named entries get a pass of their own. [OrePrefixes#oreDictName] builds the legacy name, so that is
/// the name every [GTOreDictUnificator] lookup asks for.
///
/// The High Pressure (Redstone) fluid pipes additionally register under the tier-keyed
/// `pipeSmallUltimate`..`pipeLargeUltimate` names ([TieredItems#ZPM]'s ingredient names), the identity every
/// recipe referencing them uses.
public class LoaderMaterialLibCutover implements Runnable {

    @Override
    public void run() {
        for (OrePrefixes prefix : OrePrefixes.VALUES) {
            if (MaterialParts.shape(prefix) == null) continue;
            for (int id = 0; id < 1000; id++) {
                Material material = LegacyMaterialIDIndex.get(id);
                if (material == null || !prefix.doGenerateItem(material)) continue;
                ItemStack stack = MaterialParts.stack(prefix, material, 1);
                if (stack == null) continue;
                unify(prefix, material, stack);
                if ((prefix == OrePrefixes.stick || prefix == OrePrefixes.wireFine || prefix == OrePrefixes.ingot)
                    && (material == Materials.Lead || material == Materials.Tin
                        || material == Materials.SolderingAlloy)) {
                    GregTechAPI.sSolderingMetalList.add(stack);
                    GTModHandler.registerBoxableItemToToolBox(stack);
                }
            }
        }

        unifyMembershipDriven();
        registerLegacyNamedMaterials();
        registerHighPressureNames();
    }

    private static final OrePrefixes[] MEMBERSHIP_DRIVEN_PREFIXES = { OrePrefixes.wireGt01, OrePrefixes.wireGt02,
        OrePrefixes.wireGt04, OrePrefixes.wireGt08, OrePrefixes.wireGt12, OrePrefixes.wireGt16, OrePrefixes.cableGt01,
        OrePrefixes.cableGt02, OrePrefixes.cableGt04, OrePrefixes.cableGt08, OrePrefixes.cableGt12,
        OrePrefixes.cableGt16, OrePrefixes.pipeTiny, OrePrefixes.pipeSmall, OrePrefixes.pipeMedium,
        OrePrefixes.pipeLarge, OrePrefixes.pipeHuge, OrePrefixes.pipeQuadruple, OrePrefixes.pipeNonuple,
        OrePrefixes.pipeRestrictiveTiny, OrePrefixes.pipeRestrictiveSmall, OrePrefixes.pipeRestrictiveMedium,
        OrePrefixes.pipeRestrictiveLarge, OrePrefixes.pipeRestrictiveHuge, OrePrefixes.frameGt,
        OrePrefixes.sheetmetal };

    private static void unifyMembershipDriven() {
        for (OrePrefixes prefix : MEMBERSHIP_DRIVEN_PREFIXES) {
            for (Shape shape : MaterialParts.shapes(prefix)) {
                ShapeBlock block = (ShapeBlock) MaterialLibAPI.getBlock(shape);
                for (Material material : block.getServedMaterials()) {
                    unify(prefix, material, block.getStack(material, 1));
                }
            }
        }
    }

    private static void registerLegacyNamedMaterials() {
        for (Material material : MaterialLibAPI.getMaterials()) {
            if (material.getProperty(GTMaterialProperties.LEGACY_NAME) == null) continue;
            for (OrePrefixes prefix : OrePrefixes.VALUES) {
                ItemStack stack = MaterialParts.stack(prefix, material, 1);
                if (stack == null) continue;
                unify(prefix, material, stack);
            }
        }
    }

    private static void unify(OrePrefixes prefix, Material material, ItemStack stack) {
        if (prefix.isUnifiable()) {
            GTOreDictUnificator.set(prefix, material, stack);
        } else {
            GTOreDictUnificator.registerOre(prefix.oreDictName(material), stack);
        }
    }

    private static void registerHighPressureNames() {
        GTOreDictUnificator.registerOre(
            TieredItems.ZPM.getPipeSmallIngredient(),
            MaterialLibAPI.getStack(Materials.Redstone, TEBlockShapes.pipeSmall, 1));
        GTOreDictUnificator.registerOre(
            TieredItems.ZPM.getPipeMediumIngredient(),
            MaterialLibAPI.getStack(Materials.Redstone, TEBlockShapes.pipeMedium, 1));
        GTOreDictUnificator.registerOre(
            TieredItems.ZPM.getPipeLargeIngredient(),
            MaterialLibAPI.getStack(Materials.Redstone, TEBlockShapes.pipeLarge, 1));
    }
}

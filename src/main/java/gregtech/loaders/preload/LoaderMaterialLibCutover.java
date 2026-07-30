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
import gregtech.api.enums.materials.PipeShapes;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;

/// Hands the ore-dictionary unificator MaterialLib stacks for the item cutover: every (prefix,
/// material) pair [MaterialParts] maps to a MaterialLib shape gets unified the same way
/// [gregtech.api.items.MetaGeneratedItemX32]'s constructor unifies its own generated stacks (including the
/// soldering-metal and toolbox bookkeeping), run after that constructor so the MaterialLib stack becomes
/// the unificator's preferred one.
///
/// The membership-driven prefixes get a second, shape-membership pass over each shape block's served
/// materials, because the generic loop cannot reach their full material sets: the pipe-family legacy items
/// were meta tile entities, never generated items, so `OrePrefixes#doGenerateItem` cannot gate them, and
/// their material sets (superconductor markers included) live only in the MaterialLib registry; `sheetmetal`
/// additionally serves bartworks-backed materials, which have no legacy sub-id for the [LegacyMaterialIDIndex]
/// spine. Each
/// `set` call makes the shape stack the prefix's unification target and adds the material association that
/// drives the auto-generated recycling recipes.
///
/// The High Pressure (Redstone) fluid pipes additionally register under the tier-keyed
/// `pipeSmallUltimate`..`pipeLargeUltimate` names ([TieredItems#ZPM]'s ingredient names), the identity every
/// recipe referencing them uses.
public class LoaderMaterialLibCutover implements Runnable {

    @Override
    public void run() {
        for (OrePrefixes prefix : OrePrefixes.VALUES) {
            Shape shape = MaterialParts.shape(prefix);
            if (shape == null) continue;
            for (int id = 0; id < 1000; id++) {
                Material material = LegacyMaterialIDIndex.get(id);
                if (material == null || !prefix.doGenerateItem(material)) continue;
                ItemStack stack = MaterialParts.stack(prefix, material, 1);
                if (stack == null) continue;
                if (prefix.isUnifiable()) {
                    GTOreDictUnificator.set(prefix, material, stack);
                } else {
                    GTOreDictUnificator.registerOre(prefix.oreDictName(material), stack);
                }
                if ((prefix == OrePrefixes.stick || prefix == OrePrefixes.wireFine || prefix == OrePrefixes.ingot)
                    && (material == Materials.Lead || material == Materials.Tin
                        || material == Materials.SolderingAlloy)) {
                    GregTechAPI.sSolderingMetalList.add(stack);
                    GTModHandler.registerBoxableItemToToolBox(stack);
                }
            }
        }

        unifyMembershipDriven();
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
                    ItemStack stack = block.getStack(material, 1);
                    if (prefix.isUnifiable()) {
                        GTOreDictUnificator.set(prefix, material, stack);
                    } else {
                        GTOreDictUnificator
                            .registerOre(prefix.oreDictName(MaterialUtils.internalName(material)), stack);
                    }
                }
            }
        }
    }

    private static void registerHighPressureNames() {
        GTOreDictUnificator.registerOre(
            TieredItems.ZPM.getPipeSmallIngredient(),
            MaterialLibAPI.getStack(Materials.Redstone, PipeShapes.pipeSmall, 1));
        GTOreDictUnificator.registerOre(
            TieredItems.ZPM.getPipeMediumIngredient(),
            MaterialLibAPI.getStack(Materials.Redstone, PipeShapes.pipeMedium, 1));
        GTOreDictUnificator.registerOre(
            TieredItems.ZPM.getPipeLargeIngredient(),
            MaterialLibAPI.getStack(Materials.Redstone, PipeShapes.pipeLarge, 1));
    }
}

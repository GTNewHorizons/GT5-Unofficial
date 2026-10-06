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
import gregtech.api.material.MaterialData;
import gregtech.api.material.MaterialParts;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.OreDictUtils;

/// Registers MaterialLib part stacks under their oredict names and associates unifiable ones with their material.
/// Each pair [MaterialParts] maps to a shape is registered the way [gregtech.api.items.MetaGeneratedItemX32]'s
/// constructor registers its own stacks, including the soldering-metal and toolbox lists.
///
/// Wire, cable, pipe, frame and `sheetmetal` stacks come from each shape block's served materials instead, since
/// [OrePrefixes#doGenerateItem] does not cover them. Each GregTech material's minted parts then get a pass of their
/// own, since the oredict event cannot parse every name back to its pair. Names come from
/// [OrePrefixes#oreDictName], the name [OreDictUtils] lookups ask for.
///
/// The High Pressure fluid pipes of [TieredItems#ZPM] also register under the
/// `pipeSmallUltimate`..`pipeLargeUltimate` names their recipes use.
public class LoaderMaterialLibCutover implements Runnable {

    @Override
    public void run() {
        for (OrePrefixes prefix : OrePrefixes.VALUES) {
            if (MaterialParts.shape(prefix) == null) continue;
            for (int id = 0; id < 1000; id++) {
                Material material = LegacyMaterialIDIndex.get(id);
                if (material == null || !prefix.doGenerateItem(material)) continue;
                if (!MaterialParts.isMinted(prefix, material)) continue;
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
        unifyMintedParts();
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

    private static void unifyMintedParts() {
        for (Material material : MaterialLibAPI.getMaterials()) {
            if (!"gregtech".equals(material.getModId())) continue;
            for (OrePrefixes prefix : OrePrefixes.VALUES) {
                if (!MaterialParts.isMinted(prefix, material)) continue;
                ItemStack stack = MaterialParts.stack(prefix, material, 1);
                if (stack == null) continue;
                unify(prefix, material, stack);
            }
        }
    }

    private static void unify(OrePrefixes prefix, Material material, ItemStack stack) {
        if (prefix.isUnifiable()) {
            OreDictUtils.registerOre(prefix, material, stack);
            MaterialData.addAssociation(prefix, material, stack);
        } else {
            OreDictUtils.registerOre(prefix.oreDictName(material), stack);
        }
    }

    private static void registerHighPressureNames() {
        OreDictUtils.registerOre(TieredItems.ZPM.getPipeSmallIngredient(), TieredItems.ZPM.getPipeSmall(1));
        OreDictUtils.registerOre(TieredItems.ZPM.getPipeMediumIngredient(), TieredItems.ZPM.getPipeMedium(1));
        OreDictUtils.registerOre(TieredItems.ZPM.getPipeLargeIngredient(), TieredItems.ZPM.getPipeLarge(1));
    }
}

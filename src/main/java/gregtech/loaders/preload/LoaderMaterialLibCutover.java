package gregtech.loaders.preload;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Shape;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.material.MU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;

/// Hands the ore-dictionary unificator MaterialLib stacks for the stage-05 item cutover: every (prefix,
/// material) pair [MU] maps to a MaterialLib shape gets unified the same way
/// [gregtech.api.items.MetaGeneratedItemX32]'s constructor unifies its own generated stacks (including the
/// soldering-metal and toolbox bookkeeping), run after that constructor so the MaterialLib stack becomes
/// the unificator's preferred one.
///
/// Legacy stacks are untouched by this: nothing is deleted or deregistered yet.
public class LoaderMaterialLibCutover implements Runnable {

    @Override
    public void run() {
        for (OrePrefixes prefix : OrePrefixes.VALUES) {
            Shape shape = MU.shape(prefix);
            if (shape == null) continue;
            for (Materials material : GregTechAPI.sGeneratedMaterials) {
                if (material == null || !prefix.doGenerateItem(material)) continue;
                ItemStack stack = MU.stack(prefix, material, 1);
                if (stack == null) continue;
                if (prefix.isUnifiable()) {
                    GTOreDictUnificator.set(prefix, material, stack);
                } else {
                    GTOreDictUnificator.registerOre(prefix.get(material), stack);
                }
                if ((prefix == OrePrefixes.stick || prefix == OrePrefixes.wireFine || prefix == OrePrefixes.ingot)
                    && (material == Materials.Lead || material == Materials.Tin
                        || material == Materials.SolderingAlloy)) {
                    GregTechAPI.sSolderingMetalList.add(stack);
                    GTModHandler.registerBoxableItemToToolBox(stack);
                }
            }
        }
    }
}

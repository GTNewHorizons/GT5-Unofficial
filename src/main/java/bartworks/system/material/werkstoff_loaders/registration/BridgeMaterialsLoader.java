/*
 * Copyright (c) 2018-2020 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package bartworks.system.material.werkstoff_loaders.registration;

import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.enums.OrePrefixes.cell;
import static gregtech.api.enums.OrePrefixes.cellMolten;

import java.util.Set;

import net.minecraft.util.StatCollector;

import bartworks.system.material.Werkstoff;
import bartworks.system.material.WerkstoffReconstruction;
import bartworks.system.material.werkstoff_loaders.IWerkstoffRunnable;
import gregtech.api.enchants.EnchantmentRadioactivity;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SubTag;
import gregtech.api.material.MU;
import gregtech.api.util.GTLanguageManager;
import gregtech.loaders.materials.LegacyNameDomain;

public class BridgeMaterialsLoader implements IWerkstoffRunnable {

    /// Werkstoffe whose retired gregtech-side bridge was constructed by gtPlusPlus's reconstruction (its
    /// `Materials.get(name)` fallback claimed the name first) but whose handle-material computation stayed
    /// keyed to the werkstoff's own durability rather than gtpp's ported figure.
    private static final Set<String> GTPP_BRIDGED_DURABILITY = Set.of("Hafnium", "Zirconium", "Thorium232");

    @Override
    public void run(Werkstoff werkstoff) {
        // Derived from `werkstoff` rather than the bridge facade built below, and run unconditionally here
        // (not folded into the facade-construction branch), so this keeps registering the display name once
        // minting stops building that facade for a reconstructed werkstoff -- the key/value are identical to
        // what the facade would have carried (`Materials#mName`/`#mDefaultLocalName` are set from
        // `werkstoff#getVarName`/`#getDefaultName` below), and the guard makes this idempotent with
        // `Werkstoff`'s own constructor-time registration (`GregTechAPI#sAfterGTPreload`), which already wins
        // this race for every pool-declared werkstoff by running before bartworks' own preInit.
        if (!StatCollector.canTranslate(werkstoff.getLocalizedNameKey())) {
            GTLanguageManager.addStringLocalization(werkstoff.getLocalizedNameKey(), werkstoff.getDefaultName());
        }

        if (WerkstoffReconstruction.isReconstructed(werkstoff)) {
            // Replaces the retired facade's mHandleMaterial write with the equivalent MU-side record. The
            // facade-era write ran unconditionally, last, and keyed the handle on the FACADE's durability --
            // the werkstoff's own for a self-built facade, gtpp's ported figure for a dual-claimed name.
            // The DURABILITY property carries exactly that facade value for both populations
            // (census-verified via Tellurium, whose two durabilities straddle the TungstenSteel threshold).
            com.ruling_0.materiallib.api.Material ml = WerkstoffReconstruction.materialLibOf(werkstoff);
            Integer gtppDurability = ml.getProperty(gregtech.api.material.GTMaterialProperties.DURABILITY);
            boolean gtppClaimed = ml.getProperty(gregtech.api.material.GTMaterialProperties.GTPP_STATE) != null
                && !GTPP_BRIDGED_DURABILITY.contains(werkstoff.getVarName());
            int facadeDurability = gtppClaimed && gtppDurability != null ? gtppDurability : werkstoff.getDurability();
            Materials handle = getHandleMaterial(werkstoff, facadeDurability);
            MU.recordHandleMaterial(ml, MU.material(handle));
            // A dual-nature name (e.g. Tellurium) resolves to a LIVE canonical Materials constant, whose
            // mHandleMaterial field this loader used to overwrite in place; the record above is never
            // consulted for such names (the handle hybrid reads the live field), so the field write must
            // be reproduced directly.
            com.ruling_0.materiallib.api.Material liveMl = LegacyNameDomain.lookup(werkstoff.getVarName());
            Materials live = liveMl == null ? null : MU.materialOf(liveMl);
            if (live != null) {
                live.mHandleMaterial = handle;
            }
            MU.recordBridgeRegistration(ml);
            return;
        }

        final Werkstoff.Stats stats = werkstoff.getStats();

        // Proxies carry their wrapped MaterialLib material (set at construction); an external adder resolves by
        // name. Either way the retired facade mutations below land on the live GT constant the material maps to.
        com.ruling_0.materiallib.api.Material ml = werkstoff.getBridgeMaterial();
        if (ml == null) {
            ml = LegacyNameDomain.lookup(werkstoff.getVarName());
        }
        Materials werkstoffBridgeMaterial = MU.materialOf(ml);
        if (werkstoffBridgeMaterial == null) {
            // A third-party WerkstoffAdder with no MaterialLib/live counterpart: the retired MaterialBuilder
            // bridge is the accepted API break, so there is nothing to mint here.
            return;
        }

        if (werkstoff.hasItemType(cell)) {
            werkstoffBridgeMaterial.setHasCorrespondingFluid(true);
            werkstoffBridgeMaterial.setHasCorrespondingGas(true);
            werkstoffBridgeMaterial.mFluid = werkstoff.getFluidOrGas(1)
                .getFluid();
            werkstoffBridgeMaterial.mGas = werkstoff.getFluidOrGas(1)
                .getFluid();
        }

        if (werkstoff.hasItemType(cellMolten)) {
            werkstoffBridgeMaterial.mStandardMoltenFluid = werkstoff.getMolten(1)
                .getFluid();
        }
        werkstoffBridgeMaterial.mName = werkstoff.getVarName();
        werkstoffBridgeMaterial.mDefaultLocalName = werkstoff.getDefaultName();
        werkstoffBridgeMaterial.setChemicalFormula(werkstoff.getFormulaTooltip(), werkstoff.isFormulaNeededLocalized());
        if (Thaumcraft.isModLoaded()) {
            werkstoffBridgeMaterial.mAspects = werkstoff.getGTWrappedTCAspects();
        }
        werkstoffBridgeMaterial.mMaterialInto = werkstoffBridgeMaterial;
        werkstoffBridgeMaterial.mHandleMaterial = getHandleMaterial(werkstoff, werkstoffBridgeMaterial.mDurability);
        werkstoffBridgeMaterial.setProcessingMaterialTierEU(stats.getProcessingMaterialTierEU());

        if (stats.isRadioactive()) {
            werkstoffBridgeMaterial
                .setEnchantmentForArmors(EnchantmentRadioactivity.INSTANCE, stats.getEnchantmentlvl());
            werkstoffBridgeMaterial
                .setEnchantmentForTools(EnchantmentRadioactivity.INSTANCE, stats.getEnchantmentlvl());
        }

        werkstoff.setBridgeMaterial(ml);
    }

    private static Materials getHandleMaterial(Werkstoff werkstoff, int durability) {
        if (werkstoff.contains(SubTag.BURNING)) return Materials.Blaze;

        if (werkstoff.contains(SubTag.MAGICAL)) return Materials.Thaumium;

        if (durability > 5120) return Materials.TungstenSteel;

        if (durability > 1280) return Materials.Steel;

        return Materials.Wood;
    }
}

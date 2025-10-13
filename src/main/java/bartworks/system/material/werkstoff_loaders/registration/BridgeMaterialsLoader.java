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
import static gregtech.api.enums.OrePrefixes.dust;
import static gregtech.api.enums.OrePrefixes.values;

import java.util.ArrayList;

import bartworks.system.material.Werkstoff;
import bartworks.system.material.werkstoff_loaders.IWerkstoffRunnable;
import gregtech.api.enchants.EnchantmentRadioactivity;
import gregtech.api.enums.Element;
import gregtech.api.enums.MaterialBuilder;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTOreDictUnificator;

public class BridgeMaterialsLoader implements IWerkstoffRunnable {

    @Override
    public void run(Werkstoff werkstoff) {
        final short[] rgba = werkstoff.getRGBA();
        final int argb = (rgba[3] & 0xff) << 24 | (rgba[0] & 0xff) << 16 | (rgba[1] & 0xff) << 8 | rgba[2] & 0xff;
        final Werkstoff.Stats stats = werkstoff.getStats();

        Materials werkstoffBridgeMaterial = werkstoff.getBridgeMaterial() != null ? werkstoff.getBridgeMaterial()
            : Materials.get(werkstoff.getVarName()) != Materials._NULL ? Materials.get(werkstoff.getVarName())
                : new MaterialBuilder().setName(werkstoff.getVarName())
                    .setDefaultLocalName(werkstoff.getDefaultName())
                    .setIconSet(werkstoff.getTexSet())
                    .setARGB(argb)
                    .setTool(werkstoff.getDurability(), werkstoff.getToolQuality(), werkstoff.getToolSpeed())
                    .setMeltingPoint(stats.getMeltingPoint())
                    .setBlastFurnaceTemp(stats.getMeltingPoint())
                    .setBlastFurnaceRequired(stats.isBlastFurnace())
                    .constructMaterial();

        final Element[] ELEMENT_VALUES = Element.values();
        for (OrePrefixes prefixes : values()) {
            if (prefixes != cell || !Werkstoff.Types.ELEMENT.equals(werkstoff.getType())) {
                if (prefixes == dust && Werkstoff.Types.ELEMENT.equals(werkstoff.getType())) {
                    boolean ElementSet = false;
                    for (Element e : ELEMENT_VALUES) {
                        if (e.toString()
                            .equals(werkstoff.getToolTip())) {
                            if (!e.mLinkedMaterials.isEmpty()) break;
                            werkstoffBridgeMaterial = werkstoff.getBridgeMaterial() != null
                                ? werkstoff.getBridgeMaterial()
                                : Materials.get(werkstoff.getVarName()) != Materials._NULL
                                    ? Materials.get(werkstoff.getVarName())
                                    : new MaterialBuilder().setName(werkstoff.getVarName())
                                        .setDefaultLocalName(werkstoff.getDefaultName())
                                        .setIconSet(werkstoff.getTexSet())
                                        .setARGB(argb)
                                        .setTool(
                                            werkstoff.getDurability(),
                                            werkstoff.getToolQuality(),
                                            werkstoff.getToolSpeed())
                                        .setMeltingPoint(stats.getMeltingPoint())
                                        .setBlastFurnaceTemp(stats.getMeltingPoint())
                                        .setBlastFurnaceRequired(stats.isBlastFurnace())
                                        .constructMaterial();
                            werkstoffBridgeMaterial.mElement = e;
                            e.mLinkedMaterials = new ArrayList<>();
                            e.mLinkedMaterials.add(werkstoffBridgeMaterial);
                            if (werkstoff.hasItemType(dust)) {
                                GTOreDictUnificator
                                    .addAssociation(dust, werkstoffBridgeMaterial, werkstoff.get(dust), false);
                                GTOreDictUnificator.set(dust, werkstoffBridgeMaterial, werkstoff.get(dust), true, true);
                            }
                            ElementSet = true;
                            break;
                        }
                    }
                    if (!ElementSet) {
                        continue;
                    }
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
                werkstoffBridgeMaterial.mChemicalFormula = werkstoff.getToolTip();
                if ("null".equals(werkstoffBridgeMaterial.mLocalizedName))
                    // only reload from lang file if not localized already
                    werkstoffBridgeMaterial.mLocalizedName = GTLanguageManager.addStringLocalization(
                        "Material." + werkstoffBridgeMaterial.mName.toLowerCase(),
                        werkstoffBridgeMaterial.mDefaultLocalName);
                if (Thaumcraft.isModLoaded()) {
                    werkstoffBridgeMaterial.mAspects = werkstoff.getGTWrappedTCAspects();
                }
                werkstoffBridgeMaterial.mMaterialInto = werkstoffBridgeMaterial;
                werkstoffBridgeMaterial.mHandleMaterial = werkstoff.contains(SubTag.BURNING) ? Materials.Blaze
                    : werkstoff.contains(SubTag.MAGICAL) ? Materials.Thaumium
                        : werkstoffBridgeMaterial.mDurability > 5120 ? Materials.TungstenSteel
                            : werkstoffBridgeMaterial.mDurability > 1280 ? Materials.Steel : Materials.Wood;
                if (werkstoff.getStats()
                    .isRadioactive()) {
                    werkstoffBridgeMaterial.setEnchantmentForArmors(
                        EnchantmentRadioactivity.INSTANCE,
                        werkstoff.getStats()
                            .getEnchantmentlvl());
                    werkstoffBridgeMaterial.setEnchantmentForTools(
                        EnchantmentRadioactivity.INSTANCE,
                        werkstoff.getStats()
                            .getEnchantmentlvl());
                }
                werkstoff.setBridgeMaterial(werkstoffBridgeMaterial);
            }
        }
    }
}

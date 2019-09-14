/*
 * Copyright (c) 2019 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.system.material.processingLoaders;

import com.github.bartimaeusnek.bartworks.common.loaders.ItemRegistry;
import com.github.bartimaeusnek.bartworks.system.material.BW_NonMeta_MaterialItems;
import com.github.bartimaeusnek.bartworks.system.material.CircuitGeneration.BW_Meta_Items;
import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import com.github.bartimaeusnek.bartworks.util.BWRecipes;
import com.github.bartimaeusnek.bartworks.util.BW_Util;
import gregtech.api.GregTech_API;
import gregtech.api.enums.*;
import gregtech.api.util.*;
import gregtech.common.items.behaviors.Behaviour_DataOrb;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.Map;

import static com.github.bartimaeusnek.bartworks.util.BW_Util.CLEANROOM;
import static gregtech.api.enums.OrePrefixes.*;

public class AdditionalRecipes implements Runnable {

    @Override
    public void run() {
        //Thorium/Yttrium Glas
        GT_Values.RA.addBlastRecipe(WerkstoffLoader.YttriumOxide.get(dustSmall, 2), WerkstoffLoader.Thorianit.get(dustSmall, 2), Materials.Glass.getMolten(144), null, new ItemStack(ItemRegistry.bw_glasses[0], 1, 12), null, 800, BW_Util.getMachineVoltageFromTier(5), 3663);
        //Thorianit recipes
        GT_Values.RA.addSifterRecipe(WerkstoffLoader.Thorianit.get(crushedPurified),
                new ItemStack[]{
                        WerkstoffLoader.Thorianit.get(dust),
                        WerkstoffLoader.Thorianit.get(dust),
                        WerkstoffLoader.Thorianit.get(dust),
                        Materials.Thorium.getDust(1),
                        Materials.Thorium.getDust(1),
                        WerkstoffLoader.Thorium232.get(dust),
                }, new int[]{7000, 1300, 700, 600, 300, 100},
                400,
                BW_Util.getMachineVoltageFromTier(5)
        );
        GT_Values.RA.addChemicalRecipe(WerkstoffLoader.Thorianit.get(dust), Materials.Aluminium.getDust(1), Materials.Thorium.getDust(1), 1000);
        GT_Values.RA.addChemicalRecipe(WerkstoffLoader.Thorianit.get(dust), Materials.Magnesium.getDust(1), Materials.Thorium.getDust(1), 1000);
        GT_Values.RA.addChemicalRecipe(WerkstoffLoader.Thorianit.get(crushed), ItemList.Crop_Drop_Thorium.get(9), Materials.Water.getFluid(1000), Materials.Thorium.getMolten(144), WerkstoffLoader.Thorianit.get(crushedPurified, 4), 96, 24);

        //Prasiolite
        GT_Values.RA.addBlastRecipe(GT_OreDictUnificator.get(dust, Materials.Quartzite, 40L), Materials.Amethyst.getDust(10), GT_Values.NF, GT_Values.NF, WerkstoffLoader.Prasiolite.get(OrePrefixes.gemFlawed, 20), GT_Values.NI, 800, BW_Util.getMachineVoltageFromTier(2), 500);
        GT_Values.RA.addPrimitiveBlastRecipe(GT_OreDictUnificator.get(dust, Materials.Quartzite, 40L), Materials.Amethyst.getDust(10), 6, WerkstoffLoader.Prasiolite.get(OrePrefixes.gemFlawed, 20), GT_Values.NI, 800);
        //Cubic Circonia
        GT_Values.RA.addChemicalRecipe(Materials.Yttrium.getDust(2), GT_Utility.getIntegratedCircuit(11), Materials.Oxygen.getGas(3000), null, WerkstoffLoader.YttriumOxide.get(dust, 5), 64, BW_Util.getMachineVoltageFromTier(4));
        GT_Recipe.GT_Recipe_Map.sBlastRecipes.addRecipe(false, new ItemStack[]{WerkstoffLoader.Zirconium.get(dust, 10), WerkstoffLoader.YttriumOxide.get(dust)}, new ItemStack[]{WerkstoffLoader.YttriumOxide.get(dust), WerkstoffLoader.CubicZirconia.get(gemFlawed, 40)}, null, null, new FluidStack[]{Materials.Oxygen.getGas(20000)}, null, 14400, BW_Util.getMachineVoltageFromTier(4), 2953);
        //Tellurium
        GT_Values.RA.addBlastRecipe(GT_OreDictUnificator.get(crushed, Materials.Lead, 10L), GT_Utility.getIntegratedCircuit(17), GT_Values.NF, GT_Values.NF, Materials.Lead.getIngots(10), Materials.Tellurium.getNuggets(20), 800, BW_Util.getMachineVoltageFromTier(2), 722);
        GT_Values.RA.addFusionReactorRecipe(Materials.Plutonium.getMolten(16), Materials.Beryllium.getMolten(16), WerkstoffLoader.Californium.getMolten(16), 250, 49152, 480000000);
        GT_Values.RA.addFusionReactorRecipe(WerkstoffLoader.Californium.getMolten(16), WerkstoffLoader.Calcium.getMolten(16), WerkstoffLoader.Oganesson.getFluidOrGas(16), 500, 49152, 600000000);
        GT_Values.RA.addDistillationTowerRecipe(Materials.LiquidAir.getFluid(100000000), new FluidStack[]{Materials.Nitrogen.getGas(78084000), Materials.Oxygen.getGas(20946000), Materials.Argon.getGas(934000), Materials.CarbonDioxide.getGas(40700), WerkstoffLoader.Neon.getFluidOrGas(1818), Materials.Helium.getGas(524), Materials.Methane.getGas(180), WerkstoffLoader.Krypton.getFluidOrGas(114), Materials.Hydrogen.getGas(55), WerkstoffLoader.Xenon.getFluidOrGas(9)}, null, 7500, BW_Util.getMachineVoltageFromTier(4));
        GT_Values.RA.addAutoclaveRecipe(WerkstoffLoader.MagnetoResonaticDust.get(dust), WerkstoffLoader.Neon.getFluidOrGas(1000), WerkstoffLoader.MagnetoResonaticDust.get(gemChipped, 9), 9000, 4500, BW_Util.getMachineVoltageFromTier(5));
        GT_Values.RA.addAutoclaveRecipe(WerkstoffLoader.MagnetoResonaticDust.get(dust), WerkstoffLoader.Krypton.getFluidOrGas(1000), WerkstoffLoader.MagnetoResonaticDust.get(gem), 10000, 4500, BW_Util.getMachineVoltageFromTier(5));

        //Milk
        //GT_Values.RA.addFusionReactorRecipe(WerkstoffLoader.Californium.getMolten(16), Materials.Milk.getFluid(12000), WerkstoffLoader.Oganesson.getFluidOrGas(16), 500, 49152, 600000000);
        GT_Values.RA.addCentrifugeRecipe(GT_Utility.getIntegratedCircuit(1),null,Materials.Milk.getFluid(10000),Materials.Water.getFluid(8832),Materials.Sugar.getDustSmall(21),Materials.Calcium.getDustTiny(1),Materials.Magnesium.getDustTiny(1),Materials.Potassium.getDustTiny(1),Materials.Sodium.getDustTiny(4),Materials.Phosphor.getDustTiny(1),new int[]{10000,10000,1000,10000,1000,1000},50,120);

        for (int i = 0; i <= 6; i++) {
            GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes.add(
                    new BWRecipes.DynamicGTRecipe(false,
                            new ItemStack[]{
                                    BW_Meta_Items.getNEWCIRCUITS().getStack(3),
                                    WerkstoffLoader.MagnetoResonaticDust.get(gem),
                                    BW_Meta_Items.getNEWCIRCUITS().getStack(i + 3),
                                    ItemList.Circuit_Parts_DiodeSMD.get((i + 1) * 4),
                                    ItemList.Circuit_Parts_CapacitorSMD.get((i + 1) * 4),
                                    ItemList.Circuit_Parts_TransistorSMD.get((i + 1) * 4)
                            },
                            new ItemStack[]{
                                    BW_Meta_Items.getNEWCIRCUITS().getStack(i + 4)
                            }, null, null,
                            new FluidStack[]{
                                    Materials.SolderingAlloy.getMolten((i + 1) * 36)
                            }, null, (i + 1) * 750, BW_Util.getMachineVoltageFromTier((i + 1)), CLEANROOM));
        }
        for (int i = 7; i <= 10; i++) {
            GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes.add(
                    new BWRecipes.DynamicGTRecipe(false,
                            new ItemStack[]{
                                    BW_Meta_Items.getNEWCIRCUITS().getStack(3),
                                    WerkstoffLoader.MagnetoResonaticDust.get(gemExquisite, (2)),
                                    BW_Meta_Items.getNEWCIRCUITS().getStack(i + 3),
                                    ItemList.Circuit_Parts_DiodeSMD.get((i + 6) * 4),
                                    ItemList.Circuit_Parts_CapacitorSMD.get((i + 6) * 4),
                                    ItemList.Circuit_Parts_TransistorSMD.get((i + 6) * 4)
                            },
                            new ItemStack[]{
                                    BW_Meta_Items.getNEWCIRCUITS().getStack(i + 4)
                            }, null, null,
                            new FluidStack[]{
                                    Materials.SolderingAlloy.getMolten((i + 1) * 144)
                            }, null, (i + 1) * 1500, BW_Util.getMachineVoltageFromTier(i + 1), CLEANROOM));
        }
        GT_Recipe.GT_Recipe_Map.sSmallNaquadahReactorFuels.addRecipe(true, new ItemStack[]{WerkstoffLoader.Tiberium.get(bolt)}, new ItemStack[]{}, null, null, null, 0, 0, 12500);
        GT_Recipe.GT_Recipe_Map.sLargeNaquadahReactorFuels.addRecipe(true, new ItemStack[]{WerkstoffLoader.Tiberium.get(stick)}, new ItemStack[]{}, null, null, null, 0, 0, 62500);

        try{
            Class map = GT_Recipe.GT_Recipe_Map.class;
            GT_Recipe.GT_Recipe_Map sHugeNaquadahReactorFuels = (GT_Recipe.GT_Recipe_Map) FieldUtils.getField(map,"sHugeNaquadahReactorFuels").get(null);
            GT_Recipe.GT_Recipe_Map sExtremeNaquadahReactorFuels = (GT_Recipe.GT_Recipe_Map) FieldUtils.getField(map,"sExtremeNaquadahReactorFuels").get(null);
            GT_Recipe.GT_Recipe_Map sUltraHugeNaquadahReactorFuels = (GT_Recipe.GT_Recipe_Map) FieldUtils.getField(map,"sUltraHugeNaquadahReactorFuels").get(null);
            sHugeNaquadahReactorFuels.addRecipe(true, new ItemStack[]{WerkstoffLoader.Tiberium.get(stickLong)}, new ItemStack[]{}, null, null, null, 0, 0, 125000);
            sExtremeNaquadahReactorFuels.addRecipe(true, new ItemStack[]{WerkstoffLoader.Tiberium.get(stick)}, new ItemStack[]{}, null, null, null, 0, 0, 31250);
            sUltraHugeNaquadahReactorFuels.addRecipe(true, new ItemStack[]{WerkstoffLoader.Tiberium.get(stickLong)}, new ItemStack[]{}, null, null, null, 0, 0, 125000);
        }catch (NullPointerException | IllegalAccessException e){}

        new LoadItemContainers().run();

        GT_Values.RA.addCannerRecipe(ItemList.Large_Fluid_Cell_TungstenSteel.get(1L), WerkstoffLoader.Tiberium.get(dust,3), BW_NonMeta_MaterialItems.TiberiumCell_1.get(1L), null, 30, 16);
        GT_Values.RA.addAssemblerRecipe(BW_NonMeta_MaterialItems.TiberiumCell_1.get(2L), GT_OreDictUnificator.get(stick, Materials.TungstenSteel, 4L), BW_NonMeta_MaterialItems.TiberiumCell_2.get(1L), 100, 400);
        GT_Values.RA.addAssemblerRecipe(BW_NonMeta_MaterialItems.TiberiumCell_1.get(4L), GT_OreDictUnificator.get(stickLong, Materials.TungstenSteel, 6L), BW_NonMeta_MaterialItems.TiberiumCell_4.get(1L), 150, 400);
        GT_Values.RA.addAssemblerRecipe(BW_NonMeta_MaterialItems.TiberiumCell_2.get(2L), GT_OreDictUnificator.get(stick, Materials.TungstenSteel, 4L), BW_NonMeta_MaterialItems.TiberiumCell_4.get(1L), 100, 400);

        GregTech_API.sAfterGTPostload.add(new LuVTierEnhancer());
        AdditionalRecipes.oldGThelperMethod();
    }
    private static void oldGThelperMethod() {
        //manual override for older GT
        Werkstoff werkstoff = WerkstoffLoader.Oganesson;
        Materials werkstoffBridgeMaterial = null;
        boolean aElementSet = false;
        for (Element e : Element.values()) {
            if (e.toString().equals("Uuo")) {
                werkstoffBridgeMaterial = new Materials(-1, werkstoff.getTexSet(), 0, 0, 0, false, werkstoff.getDefaultName(), werkstoff.getDefaultName());
                werkstoffBridgeMaterial.mElement = e;
                e.mLinkedMaterials.add(werkstoffBridgeMaterial);
                aElementSet = true;
                break;
            }
        }
        if (!aElementSet)
            return;

        GT_OreDictUnificator.addAssociation(cell, werkstoffBridgeMaterial, werkstoff.get(cell), false);
        try {
            Field f = Materials.class.getDeclaredField("MATERIALS_MAP");
            f.setAccessible(true);
            Map<String, Materials> MATERIALS_MAP = (Map<String, Materials>) f.get(null);
            MATERIALS_MAP.remove(werkstoffBridgeMaterial.mName);
        } catch (NoSuchFieldException | IllegalAccessException | ClassCastException e) {
            e.printStackTrace();
        }
        ItemStack scannerOutput = ItemList.Tool_DataOrb.get(1L);
        Behaviour_DataOrb.setDataTitle(scannerOutput, "Elemental-Scan");
        Behaviour_DataOrb.setDataName(scannerOutput, werkstoff.getToolTip());
        GT_Recipe.GT_Recipe_Map.sScannerFakeRecipes.addFakeRecipe(false, new BWRecipes.DynamicGTRecipe(false, new ItemStack[]{werkstoff.get(cell)}, new ItemStack[]{scannerOutput}, ItemList.Tool_DataOrb.get(1L), null, null, null, (int) (werkstoffBridgeMaterial.getMass() * 8192L), 30, 0));
        GT_Recipe.GT_Recipe_Map.sReplicatorFakeRecipes.addFakeRecipe(false, new BWRecipes.DynamicGTRecipe(false, new ItemStack[]{Materials.Empty.getCells(1)}, new ItemStack[]{werkstoff.get(cell)}, scannerOutput, null, new FluidStack[]{Materials.UUMatter.getFluid(werkstoffBridgeMaterial.getMass())}, null, (int) (werkstoffBridgeMaterial.getMass() * 512L), 30, 0));
    }
}
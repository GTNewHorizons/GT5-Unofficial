/*
 * Copyright (c) 2018-2020 bartimaeusnek
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

import static com.github.bartimaeusnek.bartworks.util.BW_Util.CLEANROOM;
import static gregtech.api.enums.OrePrefixes.*;

import com.github.bartimaeusnek.bartworks.API.LoaderReference;
import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import com.github.bartimaeusnek.bartworks.common.loaders.BioCultureLoader;
import com.github.bartimaeusnek.bartworks.common.loaders.BioItemList;
import com.github.bartimaeusnek.bartworks.common.loaders.FluidLoader;
import com.github.bartimaeusnek.bartworks.common.loaders.ItemRegistry;
import com.github.bartimaeusnek.bartworks.system.material.BW_NonMeta_MaterialItems;
import com.github.bartimaeusnek.bartworks.system.material.CircuitGeneration.BW_Meta_Items;
import com.github.bartimaeusnek.bartworks.system.material.GT_Enhancement.LuVTierEnhancer;
import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import com.github.bartimaeusnek.bartworks.util.*;
import gregtech.api.GregTech_API;
import gregtech.api.enums.*;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.items.behaviors.Behaviour_DataOrb;
import ic2.core.item.ItemFluidCell;
import java.lang.reflect.Field;
import java.util.Map;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.reflect.FieldUtils;

public class AdditionalRecipes {

    private static BWRecipes.BW_Recipe_Map_LiquidFuel sAcidGenFuels =
            ((BWRecipes.BW_Recipe_Map_LiquidFuel) BWRecipes.instance.getMappingsFor((byte) 2));
    private static BWRecipes.BacteriaVatRecipeMap sBacteriaVat =
            ((BWRecipes.BacteriaVatRecipeMap) BWRecipes.instance.getMappingsFor((byte) 1));
    private static GT_Recipe.GT_Recipe_Map sBiolab = BWRecipes.instance.getMappingsFor((byte) 0);

    private static void runBWRecipes() {

        if (ConfigHandler.BioLab) {
            FluidStack[] dnaFluid = {
                LoaderReference.gendustry
                        ? FluidRegistry.getFluidStack("liquiddna", 1000)
                        : Materials.Biomass.getFluid(1000L)
            };

            for (ItemStack stack : BioItemList.getAllPetriDishes()) {
                BioData DNA =
                        BioData.getBioDataFromNBTTag(stack.getTagCompound().getCompoundTag("DNA"));
                if (DNA != null) {
                    ItemStack Detergent = BioItemList.getOther(1);
                    ItemStack DNAFlask = BioItemList.getDNASampleFlask(null);
                    ItemStack EthanolCell = Materials.Ethanol.getCells(1);
                    sBiolab.addFakeRecipe(
                            false,
                            new ItemStack[] {stack, DNAFlask, Detergent, EthanolCell},
                            new ItemStack[] {
                                BioItemList.getDNASampleFlask(BioDNA.convertDataToDNA(DNA)),
                                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1L)
                            },
                            BioItemList.mBioLabParts[0],
                            new int[] {DNA.getChance(), 10000},
                            new FluidStack[] {FluidRegistry.getFluidStack("ic2distilledwater", 1000)},
                            null,
                            500,
                            BW_Util.getMachineVoltageFromTier(3 + DNA.getTier()),
                            BW_Util.STANDART);
                }
            }

            for (ItemStack stack : BioItemList.getAllDNASampleFlasks()) {
                BioData DNA = BioData.getBioDataFromNBTTag(stack.getTagCompound());

                if (DNA != null) {
                    ItemStack Outp = ItemList.Tool_DataOrb.get(1L);
                    Behaviour_DataOrb.setDataTitle(Outp, "DNA Sample");
                    Behaviour_DataOrb.setDataName(Outp, DNA.getName());

                    sBiolab.addFakeRecipe(
                            false,
                            new ItemStack[] {
                                stack,
                                FluidLoader.BioLabFluidCells[0],
                                FluidLoader.BioLabFluidCells[3],
                                ItemList.Tool_DataOrb.get(1L)
                            },
                            new ItemStack[] {Outp, ItemList.Cell_Universal_Fluid.get(2L)},
                            BioItemList.mBioLabParts[1],
                            new int[] {DNA.getChance(), 10000},
                            dnaFluid,
                            null,
                            500,
                            BW_Util.getMachineVoltageFromTier(4 + DNA.getTier()),
                            BW_Util.STANDART);
                }
            }

            for (ItemStack stack : BioItemList.getAllPlasmidCells()) {
                BioData DNA = BioData.getBioDataFromNBTTag(stack.getTagCompound());

                if (DNA != null) {
                    ItemStack inp = ItemList.Tool_DataOrb.get(0L);
                    Behaviour_DataOrb.setDataTitle(inp, "DNA Sample");
                    Behaviour_DataOrb.setDataName(inp, DNA.getName());
                    ItemStack inp2 = ItemList.Tool_DataOrb.get(0L);
                    Behaviour_DataOrb.setDataTitle(inp2, "DNA Sample");
                    Behaviour_DataOrb.setDataName(inp2, BioCultureLoader.BIO_DATA_BETA_LACMATASE.getName());

                    sBiolab.addFakeRecipe(
                            false,
                            new ItemStack[] {
                                FluidLoader.BioLabFluidCells[1], BioItemList.getPlasmidCell(null), inp, inp2
                            },
                            new ItemStack[] {stack, ItemList.Cell_Universal_Fluid.get(1L)},
                            BioItemList.mBioLabParts[2],
                            new int[] {DNA.getChance(), 10000},
                            dnaFluid,
                            null,
                            500,
                            BW_Util.getMachineVoltageFromTier(4 + DNA.getTier()),
                            BW_Util.STANDART);
                }
            }

            for (ItemStack stack : BioItemList.getAllPetriDishes()) {
                BioData DNA =
                        BioData.getBioDataFromNBTTag(stack.getTagCompound().getCompoundTag("DNA"));
                BioData Plasmid =
                        BioData.getBioDataFromNBTTag(stack.getTagCompound().getCompoundTag("Plasmid"));
                if (DNA.getName() != Plasmid.getName()) {
                    sBiolab.addFakeRecipe(
                            true,
                            new ItemStack[] {
                                BioItemList.getPetriDish(BioCulture.getBioCulture(DNA.getName())),
                                BioItemList.getPlasmidCell(BioPlasmid.convertDataToPlasmid(Plasmid)),
                                FluidLoader.BioLabFluidCells[2],
                            },
                            new ItemStack[] {stack, ItemList.Cell_Universal_Fluid.get(1L)},
                            BioItemList.mBioLabParts[3],
                            new int[] {10000, 10000},
                            new FluidStack[] {FluidRegistry.getFluidStack("ic2distilledwater", 1000)},
                            null,
                            500,
                            BW_Util.getMachineVoltageFromTier(6),
                            BW_Util.STANDART);
                }
            }

            ItemStack Outp = ItemList.Tool_DataOrb.get(1L);
            Behaviour_DataOrb.setDataTitle(Outp, "DNA Sample");
            Behaviour_DataOrb.setDataName(Outp, "Any DNA");
            // Clonal Cellular Synthesis- [Liquid DNA] + Medium Petri Dish + Plasma Membrane + Stem Cells + Genome Data
            sBiolab.addFakeRecipe(
                    false,
                    new ItemStack[] {
                        BioItemList.getPetriDish(null),
                        BioItemList.getOther(4),
                        ItemList.Circuit_Chip_Stemcell.get(2L),
                        Outp
                    },
                    new ItemStack[] {
                        BioItemList.getPetriDish(null).setStackDisplayName("The Culture made from DNA"),
                    },
                    BioItemList.mBioLabParts[4],
                    new int[] {7500, 10000},
                    new FluidStack[] {new FluidStack(dnaFluid[0].getFluid(), 9000)},
                    null,
                    500,
                    BW_Util.getMachineVoltageFromTier(6),
                    BW_Util.STANDART);

            FluidStack[] easyFluids = {
                Materials.Water.getFluid(1000L), FluidRegistry.getFluidStack("ic2distilledwater", 1000)
            };
            for (FluidStack fluidStack : easyFluids) {
                for (BioCulture bioCulture : BioCulture.BIO_CULTURE_ARRAY_LIST) {
                    if (bioCulture.isBreedable() && bioCulture.getTier() == 0) {
                        sBacteriaVat.addRecipe(
                                // boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecialItems,
                                // int[] aChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration,
                                // int aEUt, int aSpecialValue
                                new BWRecipes.BacteriaVatRecipe(
                                        true,
                                        new ItemStack[] {
                                            GT_Utility.getIntegratedCircuit(0), new ItemStack(Items.sugar, 64)
                                        },
                                        null,
                                        BioItemList.getPetriDish(bioCulture),
                                        null,
                                        new FluidStack[] {fluidStack},
                                        new FluidStack[] {new FluidStack(bioCulture.getFluid(), 10)},
                                        1000,
                                        BW_Util.getMachineVoltageFromTier(3),
                                        BW_Util.STANDART),
                                true);
                        // aOptimize, aInputs, aOutputs, aSpecialItems, aChances, aFluidInputs, aFluidOutputs,
                        // aDuration, aEUt, aSpecialValue
                        sBiolab.addRecipe(new BWRecipes.DynamicGTRecipe(
                                false,
                                new ItemStack[] {
                                    BioItemList.getPetriDish(null),
                                    fluidStack.equals(Materials.Water.getFluid(1000L))
                                            ? Materials.Water.getCells(1)
                                            : ItemFluidCell.getUniversalFluidCell(
                                                    FluidRegistry.getFluidStack("ic2distilledwater", 1000))
                                },
                                new ItemStack[] {
                                    BioItemList.getPetriDish(bioCulture),
                                    fluidStack.equals(Materials.Water.getFluid(1000L))
                                            ? Materials.Empty.getCells(1)
                                            : ItemList.Cell_Universal_Fluid.get(1L)
                                },
                                null,
                                new int[] {bioCulture.getChance(), 10000},
                                new FluidStack[] {new FluidStack(bioCulture.getFluid(), 1000)},
                                null,
                                500,
                                BW_Util.getMachineVoltageFromTier(3),
                                BW_Util.STANDART));
                    }
                }
            }
        }

        sAcidGenFuels.addLiquidFuel(Materials.PhosphoricAcid, 36);
        sAcidGenFuels.addLiquidFuel(Materials.DilutedHydrochloricAcid, 14);
        sAcidGenFuels.addLiquidFuel(Materials.HypochlorousAcid, 30);
        sAcidGenFuels.addLiquidFuel(Materials.HydrofluoricAcid, 40);
        sAcidGenFuels.addLiquidFuel(Materials.HydrochloricAcid, 28);
        sAcidGenFuels.addLiquidFuel(Materials.NitricAcid, 24);
        sAcidGenFuels.addLiquidFuel(Materials.Mercury, 32);
        sAcidGenFuels.addLiquidFuel(Materials.DilutedSulfuricAcid, 9);
        sAcidGenFuels.addLiquidFuel(Materials.SulfuricAcid, 18);
        sAcidGenFuels.addLiquidFuel(Materials.AceticAcid, 11);
        sAcidGenFuels.addMoltenFuel(Materials.Redstone, 10);
    }

    @SuppressWarnings("deprecation")
    public static void run() {
        runBWRecipes();
        GT_Values.RA.addImplosionRecipe(
                WerkstoffLoader.RawAdemicSteel.get(dust), 4, WerkstoffLoader.AdemicSteel.get(dust), null);
        ((BWRecipes.BW_Recipe_Map_LiquidFuel) BWRecipes.instance.getMappingsFor((byte) 2))
                .addLiquidFuel(WerkstoffLoader.FormicAcid.getBridgeMaterial(), 40);
        // Thorium/Yttrium Glas
        GT_Values.RA.addBlastRecipe(
                WerkstoffLoader.YttriumOxide.get(dustSmall, 2),
                WerkstoffLoader.Thorianit.get(dustSmall, 2),
                Materials.Glass.getMolten(144),
                null,
                new ItemStack(ItemRegistry.bw_glasses[0], 1, 12),
                null,
                800,
                BW_Util.getMachineVoltageFromTier(5),
                3663);
        // Thorianit recipes
        GT_Values.RA.addSifterRecipe(
                WerkstoffLoader.Thorianit.get(crushedPurified),
                new ItemStack[] {
                    WerkstoffLoader.Thorianit.get(dust),
                    WerkstoffLoader.Thorianit.get(dust),
                    WerkstoffLoader.Thorianit.get(dust),
                    Materials.Thorium.getDust(1),
                    Materials.Thorium.getDust(1),
                    WerkstoffLoader.Thorium232.get(dust),
                },
                new int[] {7000, 1300, 700, 600, 300, 100},
                400,
                BW_Util.getMachineVoltageFromTier(5));
        // 3ThO2 + 4Al = 3Th + 2Al2O3
        GT_Values.RA.addChemicalRecipe(
                WerkstoffLoader.Thorianit.get(dust, 9),
                Materials.Aluminium.getDust(4),
                null,
                null,
                Materials.Thorium.getDust(3),
                Materials.Aluminiumoxide.getDust(10),
                1000);
        // ThO2 + 2Mg = Th + 2MgO
        GT_Values.RA.addChemicalRecipe(
                WerkstoffLoader.Thorianit.get(dust, 3),
                Materials.Magnesium.getDust(2),
                null,
                null,
                Materials.Thorium.getDust(1),
                Materials.Magnesia.getDust(4),
                1000);
        GT_Values.RA.addChemicalRecipe(
                WerkstoffLoader.Thorianit.get(crushed),
                ItemList.Crop_Drop_Thorium.get(9),
                Materials.Water.getFluid(1000),
                Materials.Thorium.getMolten(144),
                WerkstoffLoader.Thorianit.get(crushedPurified, 4),
                96,
                24);

        // Prasiolite
        GT_Values.RA.addBlastRecipe(
                GT_OreDictUnificator.get(dust, Materials.Quartzite, 40L),
                Materials.Amethyst.getDust(10),
                GT_Values.NF,
                GT_Values.NF,
                WerkstoffLoader.Prasiolite.get(OrePrefixes.gemFlawed, 20),
                GT_Values.NI,
                800,
                BW_Util.getMachineVoltageFromTier(2),
                500);
        GT_Values.RA.addPrimitiveBlastRecipe(
                GT_OreDictUnificator.get(dust, Materials.Quartzite, 40L),
                Materials.Amethyst.getDust(10),
                6,
                WerkstoffLoader.Prasiolite.get(OrePrefixes.gemFlawed, 20),
                GT_Values.NI,
                800);
        // Cubic Circonia
        // 2Y + 3O = Y2O3
        GT_Values.RA.addChemicalRecipe(
                Materials.Yttrium.getDust(2),
                GT_Utility.getIntegratedCircuit(5),
                Materials.Oxygen.getGas(3000),
                GT_Values.NF,
                WerkstoffLoader.YttriumOxide.get(dust, 5),
                4096,
                BW_Util.getMachineVoltageFromTier(1));
        // Zr + 2O =Y22O3= ZrO2
        GT_Recipe.GT_Recipe_Map.sBlastRecipes.addRecipe(
                false,
                new ItemStack[] {WerkstoffLoader.Zirconium.get(dust, 10), WerkstoffLoader.YttriumOxide.get(dust)},
                new ItemStack[] {
                    WerkstoffLoader.YttriumOxide.get(dust), WerkstoffLoader.CubicZirconia.get(gemFlawed, 40)
                },
                null,
                null,
                new FluidStack[] {Materials.Oxygen.getGas(20000)},
                null,
                57600,
                BW_Util.getMachineVoltageFromTier(3),
                2953);
        // Tellurium
        GT_Values.RA.addBlastRecipe(
                GT_OreDictUnificator.get(crushed, Materials.Lead, 10L),
                GT_Utility.getIntegratedCircuit(17),
                GT_Values.NF,
                GT_Values.NF,
                Materials.Lead.getIngots(10),
                Materials.Tellurium.getNuggets(20),
                800,
                BW_Util.getMachineVoltageFromTier(2),
                722);
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Plutonium.getMolten(48),
                Materials.Beryllium.getMolten(48),
                WerkstoffLoader.Californium.getMolten(48),
                240,
                49152,
                480000000);
        GT_Values.RA.addFusionReactorRecipe(
                WerkstoffLoader.Californium.getMolten(32),
                WerkstoffLoader.Calcium.getMolten(144),
                WerkstoffLoader.Oganesson.getFluidOrGas(144),
                420,
                49152,
                600000000);
        GT_Values.RA.addDistillationTowerRecipe(
                Materials.LiquidAir.getFluid(100000000),
                new FluidStack[] {
                    Materials.Nitrogen.getGas(78084000),
                    Materials.Oxygen.getGas(20946000),
                    Materials.Argon.getGas(934000),
                    Materials.CarbonDioxide.getGas(40700),
                    WerkstoffLoader.Neon.getFluidOrGas(1818),
                    Materials.Helium.getGas(524),
                    Materials.Methane.getGas(180),
                    WerkstoffLoader.Krypton.getFluidOrGas(114),
                    Materials.Hydrogen.getGas(55),
                    WerkstoffLoader.Xenon.getFluidOrGas(9)
                },
                null,
                7500,
                BW_Util.getMachineVoltageFromTier(4));
        GT_Values.RA.addAutoclaveRecipe(
                WerkstoffLoader.MagnetoResonaticDust.get(dust),
                WerkstoffLoader.Neon.getFluidOrGas(1000),
                WerkstoffLoader.MagnetoResonaticDust.get(gemChipped, 9),
                9000,
                4500,
                BW_Util.getMachineVoltageFromTier(5));
        GT_Values.RA.addAutoclaveRecipe(
                WerkstoffLoader.MagnetoResonaticDust.get(dust),
                WerkstoffLoader.Krypton.getFluidOrGas(1000),
                WerkstoffLoader.MagnetoResonaticDust.get(gem),
                10000,
                4500,
                BW_Util.getMachineVoltageFromTier(5));

        // Milk
        // GT_Values.RA.addFusionReactorRecipe(WerkstoffLoader.Californium.getMolten(16),
        // Materials.Milk.getFluid(12000), WerkstoffLoader.Oganesson.getFluidOrGas(16), 500, 49152, 600000000);
        GT_Values.RA.addCentrifugeRecipe(
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NI,
                Materials.Milk.getFluid(10000),
                Materials.Water.getFluid(8832),
                Materials.Sugar.getDustSmall(21),
                Materials.Calcium.getDustTiny(1),
                Materials.Magnesium.getDustTiny(1),
                Materials.Potassium.getDustTiny(1),
                Materials.Sodium.getDustTiny(4),
                Materials.Phosphor.getDustTiny(1),
                new int[] {10000, 10000, 1000, 10000, 1000, 1000},
                50,
                120);

        GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes.add(new BWRecipes.DynamicGTRecipe(
                false,
                new ItemStack[] {
                    BW_Meta_Items.getNEWCIRCUITS().getStack(3),
                    WerkstoffLoader.MagnetoResonaticDust.get(gem),
                    ItemList.NandChip.get(1),
                    ItemList.Circuit_Parts_DiodeSMD.get(4),
                    ItemList.Circuit_Parts_CapacitorSMD.get(4),
                    ItemList.Circuit_Parts_TransistorSMD.get(4)
                },
                new ItemStack[] {BW_Meta_Items.getNEWCIRCUITS().getStack(4)},
                null,
                null,
                new FluidStack[] {Materials.SolderingAlloy.getMolten(36)},
                null,
                750,
                BW_Util.getMachineVoltageFromTier(1),
                CLEANROOM));
        for (int i = 1; i <= 6; i++) {
            GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes.add(new BWRecipes.DynamicGTRecipe(
                    false,
                    new ItemStack[] {
                        BW_Meta_Items.getNEWCIRCUITS().getStack(3),
                        WerkstoffLoader.MagnetoResonaticDust.get(gem),
                        BW_Meta_Items.getNEWCIRCUITS().getStack(i + 3),
                        ItemList.Circuit_Parts_DiodeSMD.get((i + 1) * 4),
                        ItemList.Circuit_Parts_CapacitorSMD.get((i + 1) * 4),
                        ItemList.Circuit_Parts_TransistorSMD.get((i + 1) * 4)
                    },
                    new ItemStack[] {BW_Meta_Items.getNEWCIRCUITS().getStack(i + 4)},
                    null,
                    null,
                    new FluidStack[] {Materials.SolderingAlloy.getMolten((i + 1) * 36)},
                    null,
                    (i + 1) * 750,
                    BW_Util.getMachineVoltageFromTier((i + 1)),
                    CLEANROOM));
        }
        for (int i = 7; i <= 10; i++) {
            GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes.add(new BWRecipes.DynamicGTRecipe(
                    false,
                    new ItemStack[] {
                        BW_Meta_Items.getNEWCIRCUITS().getStack(3),
                        WerkstoffLoader.MagnetoResonaticDust.get(gemExquisite, (1)),
                        BW_Meta_Items.getNEWCIRCUITS().getStack(i + 3),
                        ItemList.Circuit_Parts_DiodeSMD.get((i + 6) * 4),
                        ItemList.Circuit_Parts_CapacitorSMD.get((i + 6) * 4),
                        ItemList.Circuit_Parts_TransistorSMD.get((i + 6) * 4)
                    },
                    new ItemStack[] {BW_Meta_Items.getNEWCIRCUITS().getStack(i + 4)},
                    null,
                    null,
                    new FluidStack[] {Materials.SolderingAlloy.getMolten((i + 1) * 144)},
                    null,
                    (i + 1) * 1500,
                    BW_Util.getMachineVoltageFromTier(i + 1),
                    CLEANROOM));
        }
        GT_Recipe.GT_Recipe_Map.sSmallNaquadahReactorFuels.addRecipe(
                true,
                new ItemStack[] {WerkstoffLoader.Tiberium.get(bolt)},
                new ItemStack[] {},
                null,
                null,
                null,
                0,
                0,
                12500);
        GT_Recipe.GT_Recipe_Map.sLargeNaquadahReactorFuels.addRecipe(
                true,
                new ItemStack[] {WerkstoffLoader.Tiberium.get(stick)},
                new ItemStack[] {},
                null,
                null,
                null,
                0,
                0,
                62500);

        try {
            Class<GT_Recipe.GT_Recipe_Map> map = GT_Recipe.GT_Recipe_Map.class;
            GT_Recipe.GT_Recipe_Map sHugeNaquadahReactorFuels = (GT_Recipe.GT_Recipe_Map)
                    FieldUtils.getField(map, "sHugeNaquadahReactorFuels").get(null);
            GT_Recipe.GT_Recipe_Map sExtremeNaquadahReactorFuels = (GT_Recipe.GT_Recipe_Map)
                    FieldUtils.getField(map, "sExtremeNaquadahReactorFuels").get(null);
            GT_Recipe.GT_Recipe_Map sUltraHugeNaquadahReactorFuels = (GT_Recipe.GT_Recipe_Map)
                    FieldUtils.getField(map, "sUltraHugeNaquadahReactorFuels").get(null);
            sHugeNaquadahReactorFuels.addRecipe(
                    true,
                    new ItemStack[] {WerkstoffLoader.Tiberium.get(stickLong)},
                    new ItemStack[] {},
                    null,
                    null,
                    null,
                    0,
                    0,
                    125000);
            sExtremeNaquadahReactorFuels.addRecipe(
                    true,
                    new ItemStack[] {WerkstoffLoader.Tiberium.get(stick)},
                    new ItemStack[] {},
                    null,
                    null,
                    null,
                    0,
                    0,
                    31250);
            sUltraHugeNaquadahReactorFuels.addRecipe(
                    true,
                    new ItemStack[] {WerkstoffLoader.Tiberium.get(stickLong)},
                    new ItemStack[] {},
                    null,
                    null,
                    null,
                    0,
                    0,
                    125000);
        } catch (NullPointerException | IllegalAccessException ignored) {
        }

        LoadItemContainers.run();

        GT_Values.RA.addCannerRecipe(
                ItemList.Large_Fluid_Cell_TungstenSteel.get(1L),
                WerkstoffLoader.Tiberium.get(dust, 3),
                BW_NonMeta_MaterialItems.TiberiumCell_1.get(1L),
                null,
                30,
                16);
        GT_Values.RA.addAssemblerRecipe(
                BW_NonMeta_MaterialItems.TiberiumCell_1.get(2L),
                GT_OreDictUnificator.get(stick, Materials.TungstenSteel, 4L),
                BW_NonMeta_MaterialItems.TiberiumCell_2.get(1L),
                100,
                400);
        GT_Values.RA.addAssemblerRecipe(
                BW_NonMeta_MaterialItems.TiberiumCell_1.get(4L),
                GT_OreDictUnificator.get(stickLong, Materials.TungstenSteel, 6L),
                BW_NonMeta_MaterialItems.TiberiumCell_4.get(1L),
                150,
                400);
        GT_Values.RA.addAssemblerRecipe(
                BW_NonMeta_MaterialItems.TiberiumCell_2.get(2L),
                GT_OreDictUnificator.get(stick, Materials.TungstenSteel, 4L),
                BW_NonMeta_MaterialItems.TiberiumCell_4.get(1L),
                100,
                400);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                    ItemList.NaquadahCell_1.get(32L),
                    GT_OreDictUnificator.get(stickLong, Materials.TungstenSteel, 64L),
                    GT_OreDictUnificator.get(stickLong, Materials.TungstenSteel, 64L),
                    GT_OreDictUnificator.get(stickLong, Materials.TungstenSteel, 64L),
                    WerkstoffLoader.Tiberium.get(dust, 64),
                    WerkstoffLoader.Tiberium.get(dust, 64)
                },
                null,
                BW_NonMeta_MaterialItems.TheCoreCell.get(1L),
                100,
                BW_Util.getMachineVoltageFromTier(6));

        GregTech_API.sAfterGTPostload.add(new LuVTierEnhancer());
        AdditionalRecipes.oldGThelperMethod();
    }

    @SuppressWarnings("unchecked")
    private static void oldGThelperMethod() {
        // manual override for older GT
        Werkstoff werkstoff = WerkstoffLoader.Oganesson;
        Materials werkstoffBridgeMaterial = null;
        boolean aElementSet = false;
        for (Element e : Element.values()) {
            if (e.toString().equals("Uuo")) {
                werkstoffBridgeMaterial = werkstoff.getBridgeMaterial() != null
                        ? werkstoff.getBridgeMaterial()
                        : new Materials(
                                -1,
                                werkstoff.getTexSet(),
                                0,
                                0,
                                0,
                                false,
                                werkstoff.getDefaultName(),
                                werkstoff.getDefaultName());
                werkstoffBridgeMaterial.mElement = e;
                e.mLinkedMaterials.add(werkstoffBridgeMaterial);
                aElementSet = true;
                werkstoff.setBridgeMaterial(werkstoffBridgeMaterial);
                break;
            }
        }
        if (!aElementSet) return;

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
        GT_Recipe.GT_Recipe_Map.sScannerFakeRecipes.addFakeRecipe(
                false,
                new BWRecipes.DynamicGTRecipe(
                        false,
                        new ItemStack[] {werkstoff.get(cell)},
                        new ItemStack[] {scannerOutput},
                        ItemList.Tool_DataOrb.get(1L),
                        null,
                        null,
                        null,
                        (int) (werkstoffBridgeMaterial.getMass() * 8192L),
                        30,
                        0));
        GT_Recipe.GT_Recipe_Map.sReplicatorFakeRecipes.addFakeRecipe(
                false,
                new BWRecipes.DynamicGTRecipe(
                        false,
                        new ItemStack[] {Materials.Empty.getCells(1)},
                        new ItemStack[] {werkstoff.get(cell)},
                        scannerOutput,
                        null,
                        new FluidStack[] {Materials.UUMatter.getFluid(werkstoffBridgeMaterial.getMass())},
                        null,
                        (int) (werkstoffBridgeMaterial.getMass() * 512L),
                        30,
                        0));
    }
}

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

package com.github.bartimaeusnek.bartworks.util;

import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import com.github.bartimaeusnek.bartworks.common.loaders.BioCultureLoader;
import com.github.bartimaeusnek.bartworks.common.loaders.BioItemList;
import com.github.bartimaeusnek.bartworks.common.loaders.FluidLoader;
import cpw.mods.fml.common.Loader;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.tileentity.IHasWorldObjectAndCoords;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.items.behaviors.Behaviour_DataOrb;
import ic2.core.Ic2Items;
import ic2.core.item.ItemFluidCell;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnegative;
import java.io.Serializable;
import java.util.*;

import static com.github.bartimaeusnek.bartworks.util.BW_Util.calculateSv;
import static com.github.bartimaeusnek.bartworks.util.BW_Util.specialToByte;

public class BWRecipes {

    public static final BWRecipes instance = new BWRecipes();
    public static final byte BIOLABBYTE = 0;
    public static final byte BACTERIALVATBYTE = 1;
    public static final byte ACIDGENMAPBYTE = 2;
    public static final byte CIRCUITASSEMBLYLINE = 3;

    private final GT_Recipe.GT_Recipe_Map sBiolab = new GT_Recipe.GT_Recipe_Map(
            new HashSet<GT_Recipe>(150),
            "bw.recipe.biolab",
            StatCollector.translateToLocal("tile.biolab.name"),
            null,
            "gregtech:textures/gui/basicmachines/BW.GUI.BioLab",
            6, 2, 1, 1, 1,
            "", 1, "", true, false //special handler
    );
    private final BacteriaVatRecipeMap sBacteriaVat = new BacteriaVatRecipeMap(
            new HashSet<GT_Recipe>(50),
            "bw.recipe.BacteriaVat",
            StatCollector.translateToLocal("tile.biovat.name"),
            null,
            "gregtech:textures/gui/basicmachines/Default",
            6, 2, 0, 1, 1,
            " Sievert: ", 1, " Sv", true, false //special handler
    );
    private final BW_Recipe_Map_LiquidFuel sAcidGenFuels = new BW_Recipe_Map_LiquidFuel(
            new HashSet<GT_Recipe>(10),
            "bw.fuels.acidgens",
            StatCollector.translateToLocal("tile.acidgenerator.name"),
            null,
            "gregtech:textures/gui/basicmachines/Default",
            1, 1, 1, 1, 1,
            "EU generated: ", 1000, "", false, true
    );
    private final BWRecipes.SpecialObjectSensitiveMap sCircuitAssemblyLineMap = new SpecialObjectSensitiveMap(
            new HashSet<GT_Recipe>(60),
            "bw.recipe.cal",
            "Circuit Assembly Line",
            null,
            "gregtech:textures/gui/basicmachines/Default",
            6, 6, 1, 1, 1,
            "", 1, "", true, true //special handler
    );


    public BWRecipes() {

        if (ConfigHandler.BioLab) {
            FluidStack[] dnaFluid = {Loader.isModLoaded("gendustry") ? FluidRegistry.getFluidStack("liquiddna", 1000) : Materials.Biomass.getFluid(1000L)};

            for (ItemStack stack : BioItemList.getAllPetriDishes()) {
                BioData DNA = BioData.getBioDataFromNBTTag(stack.getTagCompound().getCompoundTag("DNA"));
                if (DNA != null) {
                    ItemStack Detergent = BioItemList.getOther(1);
                    ItemStack DNAFlask = BioItemList.getDNASampleFlask(null);
                    ItemStack EthanolCell = Materials.Ethanol.getCells(1);
                    sBiolab.addFakeRecipe(false,
                            new ItemStack[]{
                                    stack,
                                    DNAFlask,
                                    Detergent,
                                    EthanolCell
                            },
                            new ItemStack[]{
                                    BioItemList.getDNASampleFlask(BioDNA.convertDataToDNA(DNA)),
                                    GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1L)
                            },
                            BioItemList.mBioLabParts[0],
                            new int[]{DNA.getChance(), 10000},
                            new FluidStack[]{
                                    FluidRegistry.getFluidStack("ic2distilledwater", 1000)
                            },
                            null,
                            500,
                            BW_Util.getMachineVoltageFromTier(3 + DNA.getTier()),
                            BW_Util.STANDART
                    );
                }
            }

            for (ItemStack stack : BioItemList.getAllDNASampleFlasks()) {
                BioData DNA = BioData.getBioDataFromNBTTag(stack.getTagCompound());

                if (DNA != null) {
                    ItemStack Outp = ItemList.Tool_DataOrb.get(1L);
                    Behaviour_DataOrb.setDataTitle(Outp, "DNA Sample");
                    Behaviour_DataOrb.setDataName(Outp, DNA.getName());

                    sBiolab.addFakeRecipe(false,
                            new ItemStack[]{
                                    stack,
                                    FluidLoader.BioLabFluidCells[0],
                                    FluidLoader.BioLabFluidCells[3],
                                    ItemList.Tool_DataOrb.get(1L)
                            },
                            new ItemStack[]{
                                    Outp,
                                    ItemList.Cell_Universal_Fluid.get(2L)
                            },
                            BioItemList.mBioLabParts[1],
                            new int[]{DNA.getChance(), 10000},
                            dnaFluid,
                            null,
                            500,
                            BW_Util.getMachineVoltageFromTier(4 + DNA.getTier()),
                            BW_Util.STANDART
                    );
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

                    sBiolab.addFakeRecipe(false,
                            new ItemStack[]{
                                    FluidLoader.BioLabFluidCells[1],
                                    BioItemList.getPlasmidCell(null),
                                    inp,
                                    inp2
                            },
                            new ItemStack[]{
                                    stack,
                                    ItemList.Cell_Universal_Fluid.get(1L)
                            },
                            BioItemList.mBioLabParts[2],
                            new int[]{DNA.getChance(), 10000},
                            dnaFluid,
                            null,
                            500,
                            BW_Util.getMachineVoltageFromTier(4 + DNA.getTier()),
                            BW_Util.STANDART
                    );
                }
            }

            //Transformation- [Distilled Water] + Culture () + Plasmids (Gene) Cell + Penicillin Cell= Culture (Gene) + Empty Cells
            sBiolab.addFakeRecipe(false,
                    new ItemStack[]{
                            BioItemList.getPetriDish(null).setStackDisplayName("The Culture to change"),
                            BioItemList.getPlasmidCell(null).setStackDisplayName("The Plasmids to Inject"),
                            FluidLoader.BioLabFluidCells[2],
                    },
                    new ItemStack[]{
                            BioItemList.getPetriDish(null).setStackDisplayName("The changed Culture"),
                            ItemList.Cell_Universal_Fluid.get(1L)
                    },
                    BioItemList.mBioLabParts[3],
                    new int[]{7500, 10000},
                    new FluidStack[]{
                            FluidRegistry.getFluidStack("ic2distilledwater", 1000)
                    },
                    null,
                    500,
                    BW_Util.getMachineVoltageFromTier(6),
                    BW_Util.STANDART
            );


            ItemStack Outp = ItemList.Tool_DataOrb.get(1L);
            Behaviour_DataOrb.setDataTitle(Outp, "DNA Sample");
            Behaviour_DataOrb.setDataName(Outp, "Any DNA");
            //Clonal Cellular Synthesis- [Liquid DNA] + Medium Petri Dish + Plasma Membrane + Stem Cells + Genome Data
            sBiolab.addFakeRecipe(false,
                    new ItemStack[]{
                            BioItemList.getPetriDish(null),
                            BioItemList.getOther(4),
                            ItemList.Circuit_Chip_Stemcell.get(2L),
                            Outp
                    },
                    new ItemStack[]{
                            BioItemList.getPetriDish(null).setStackDisplayName("The Culture made from DNA"),
                    },
                    BioItemList.mBioLabParts[4],
                    new int[]{7500, 10000},
                    new FluidStack[]{new FluidStack(dnaFluid[0].getFluid(), 9000)},
                    null,
                    500,
                    BW_Util.getMachineVoltageFromTier(6),
                    BW_Util.STANDART
            );

            FluidStack[] easyFluids = {Materials.Water.getFluid(1000L), FluidRegistry.getFluidStack("ic2distilledwater", 1000)};
            for (FluidStack fluidStack : easyFluids) {
                for (BioCulture bioCulture : BioCulture.BIO_CULTURE_ARRAY_LIST) {
                    if (bioCulture.isBreedable() && bioCulture.getTier() == 0) {
                        sBacteriaVat.addRecipe(
                                //boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecialItems, int[] aChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue
                                new BacteriaVatRecipe(
                                        true,
                                        new ItemStack[]{
                                                GT_Utility.getIntegratedCircuit(0),
                                                new ItemStack(Items.sugar, 64)
                                        },
                                        null,
                                        BioItemList.getPetriDish(bioCulture),
                                        null,
                                        new FluidStack[]{
                                                fluidStack
                                        },
                                        new FluidStack[]{
                                                new FluidStack(bioCulture.getFluid(), 10)
                                        },
                                        1000,
                                        BW_Util.getMachineVoltageFromTier(3),
                                        BW_Util.STANDART
                                ), true
                        );
                        //aOptimize, aInputs, aOutputs, aSpecialItems, aChances, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue
                        sBiolab.addRecipe(
                                new DynamicGTRecipe(
                                        false,
                                        new ItemStack[]{
                                                BioItemList.getPetriDish(null),
                                                fluidStack.equals(Materials.Water.getFluid(1000L)) ? Materials.Water.getCells(1) : ItemFluidCell.getUniversalFluidCell(FluidRegistry.getFluidStack("ic2distilledwater", 1000))
                                        },
                                        new ItemStack[]{
                                                BioItemList.getPetriDish(bioCulture),
                                                fluidStack.equals(Materials.Water.getFluid(1000L)) ? Materials.Empty.getCells(1) : ItemList.Cell_Universal_Fluid.get(1L)
                                        },
                                        null,
                                        new int[]{
                                                bioCulture.getChance(),
                                                10000
                                        },
                                        new FluidStack[]{
                                                new FluidStack(bioCulture.getFluid(), 1000)
                                        },
                                        null,
                                        500,
                                        BW_Util.getMachineVoltageFromTier(3),
                                        BW_Util.STANDART
                                ));
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


    /**
     * @param machine 0 = biolab; 1 = BacterialVat; 2 = sAcidGenFuels; 3 = circuitAssemblyLine
     */
    public GT_Recipe.GT_Recipe_Map getMappingsFor(byte machine) {
        switch (machine) {
            case 0:
                return sBiolab;
            case 1:
                return sBacteriaVat;
            case 2:
                return sAcidGenFuels;
            case 3:
                return sCircuitAssemblyLineMap;
            default:
                return null;
        }

    }

    public boolean addBioLabRecipe(ItemStack[] aInputs, ItemStack aOutput, ItemStack aSpecialItems, int[] aChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
        if (sBiolab.addRecipe(new DynamicGTRecipe(true, aInputs, new ItemStack[]{aOutput}, aSpecialItems, aChances, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue)) != null)
            return true;
        return false;
    }

    public boolean addBioLabRecipeIncubation(ItemStack aInput, BioCulture aOutput, int[] aChances, FluidStack[] aFluidInputs, int aDuration, int aEUt, int aSpecialValue) {
        if (sBiolab.addRecipe(new DynamicGTRecipe(true, new ItemStack[]{BioItemList.getPetriDish(null), aInput}, new ItemStack[]{BioItemList.getPetriDish(aOutput)}, null, aChances, aFluidInputs, new FluidStack[]{GT_Values.NF}, aDuration, aEUt, aSpecialValue)) != null)
            return true;
        return false;
    }

    public boolean addBioLabRecipeIncubation(ItemStack aInput, BioCulture aOutput, int[] aChances, FluidStack aFluidInputs, int aDuration, int aEUt, int aSpecialValue) {
        if (sBiolab.addRecipe(new DynamicGTRecipe(true, new ItemStack[]{BioItemList.getPetriDish(null), aInput}, new ItemStack[]{BioItemList.getPetriDish(aOutput)}, null, aChances, new FluidStack[]{aFluidInputs}, new FluidStack[]{GT_Values.NF}, aDuration, aEUt, aSpecialValue)) != null)
            return true;
        return false;
    }

    @Deprecated
    public boolean addBioLabRecipeDNAExtraction(ItemStack[] aInputs, ItemStack aOutput, int[] aChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
        if (sBiolab.addRecipe(new DynamicGTRecipe(true, aInputs, new ItemStack[]{aOutput}, BioItemList.mBioLabParts[0], aChances, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue)) != null)
            return true;
        return false;
    }

    @Deprecated
    public boolean addBioLabRecipePCRThermoclycling(ItemStack[] aInputs, ItemStack aOutput, int[] aChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
        if (sBiolab.addRecipe(new DynamicGTRecipe(true, aInputs, new ItemStack[]{aOutput}, BioItemList.mBioLabParts[1], aChances, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue)) != null)
            return true;
        return false;
    }

    @Deprecated
    public boolean addBioLabRecipePlasmidSynthesis(ItemStack[] aInputs, ItemStack aOutput, int[] aChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
        if (sBiolab.addRecipe(new DynamicGTRecipe(true, aInputs, new ItemStack[]{aOutput}, BioItemList.mBioLabParts[2], aChances, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue)) != null)
            return true;
        return false;
    }

    @Deprecated
    public boolean addBioLabRecipeTransformation(ItemStack[] aInputs, ItemStack aOutput, int[] aChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
        if (sBiolab.addRecipe(new DynamicGTRecipe(true, aInputs, new ItemStack[]{aOutput}, BioItemList.mBioLabParts[3], aChances, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue)) != null)
            return true;
        return false;
    }

    @Deprecated
    public boolean addBioLabRecipeClonalCellularSynthesis(ItemStack[] aInputs, ItemStack aOutput, int[] aChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
        if (sBiolab.addRecipe(new DynamicGTRecipe(true, aInputs, new ItemStack[]{aOutput}, BioItemList.mBioLabParts[4], aChances, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue)) != null)
            return true;
        return false;
    }

    @Deprecated
    public boolean addBacterialVatRecipe(ItemStack[] aInputs, ItemStack[] aOutputs, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, Materials material, @Nonnegative byte glasTier) {
        int aSievert = 0;
        if (material.getProtons() >= 83 || material.getProtons() == 61 || material.getProtons() == 43)
            aSievert += calculateSv(material);
        aSievert = aSievert << 6;
        aSievert = aSievert | glasTier;
        if (sBacteriaVat.addRecipe(new BacteriaVatRecipe(false, aInputs, aOutputs, null, new int[]{}, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSievert)) != null)
            return true;
        return false;
    }

    public boolean addBacterialVatRecipe(ItemStack[] aInputs, BioCulture aCulture, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, @Nonnegative int aDuration, @Nonnegative int aEUt, @Nonnegative int aSv, @Nonnegative int glasTier, int aSpecialValue, boolean exactSv) {
        byte gTier = (byte) glasTier;
        int aSievert = 0;
        if (aSv >= 83 || aSv == 61 || aSv == 43)
            aSievert += aSv;
        aSievert = aSievert << 1;
        aSievert = aSievert | (exactSv ? 1 : 0);
        aSievert = aSievert << 2;
        aSievert = aSievert | specialToByte(aSpecialValue);
        aSievert = aSievert << 4;
        aSievert = aSievert | gTier;
        if (sBacteriaVat.addRecipe(new BacteriaVatRecipe(false, aInputs, null, BioItemList.getPetriDish(aCulture), new int[]{}, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSievert)) != null)
            return true;
        return false;
    }

    public boolean addBacterialVatRecipe(ItemStack[] aInputs, BioCulture aCulture, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, Materials material, @Nonnegative int glasTier, int aSpecialValue, boolean exactSv) {
        byte gTier = (byte) glasTier;
        int aSievert = 0;
        if (material.getProtons() >= 83 || material.getProtons() == 61 || material.getProtons() == 43)
            aSievert += calculateSv(material);
        aSievert = aSievert << 1;
        aSievert = aSievert | (exactSv ? 1 : 0);
        aSievert = aSievert << 2;
        aSievert = aSievert | specialToByte(aSpecialValue);
        aSievert = aSievert << 4;
        aSievert = aSievert | gTier;
        if (sBacteriaVat.addRecipe(new BacteriaVatRecipe(false, aInputs, null, BioItemList.getPetriDish(aCulture), new int[]{}, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSievert)) != null)
            return true;
        return false;
    }

    @Deprecated
    public boolean addBacterialVatRecipe(ItemStack[] aInputs, ItemStack[] aOutputs, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, Materials material, boolean exactSv) {
        int aSievert = 0;
        if (material.getProtons() >= 83 || material.getProtons() == 61 || material.getProtons() == 43)
            aSievert += calculateSv(material);
        aSievert = aSievert << 1;
        aSievert = aSievert | (exactSv ? 1 : 0);
        aSievert = aSievert << 6;
        if (sBacteriaVat.addRecipe(new BacteriaVatRecipe(false, aInputs, aOutputs, null, new int[]{}, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSievert)) != null)
            return true;
        return false;
    }

    public boolean addBacterialVatRecipe(ItemStack[] aInputs, BioCulture culture, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, Materials material, int aSpecialValue, boolean exactSv) {
        int aSievert = 0;
        if (material.getProtons() >= 83 || material.getProtons() == 61 || material.getProtons() == 43)
            aSievert += calculateSv(material);
        aSievert = aSievert << 1;
        aSievert = aSievert | (exactSv ? 1 : 0);
        aSievert = aSievert << 2;
        aSievert = aSievert | specialToByte(aSpecialValue);
        aSievert = aSievert << 4;
        if (sBacteriaVat.addRecipe(new BacteriaVatRecipe(false, aInputs, null, BioItemList.getPetriDish(culture), new int[]{}, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSievert)) != null)
            return true;
        return false;
    }

    /**
     * Adds a Vat recipe without Rad requirements but with Glas requirements
     */
    public boolean addBacterialVatRecipe(ItemStack[] aInputs, BioCulture culture, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, byte glasTier) {
        int aSievert = 0;
        aSievert = aSievert << 7;
        aSievert = aSievert | glasTier;
        if (sBacteriaVat.addRecipe(new BacteriaVatRecipe(false, aInputs, null, BioItemList.getPetriDish(culture), new int[]{}, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSievert)) != null)
            return true;
        return false;
    }

    /**
     * Adds a Vat recipe without Rad or Glas requirements
     */
    public boolean addBacterialVatRecipe(ItemStack[] aInputs, FluidStack[] aFluidInputs, BioCulture culture, FluidStack[] aFluidOutputs, int aDuration, int aEUt) {
        if (sBacteriaVat.addRecipe(new BacteriaVatRecipe(false, aInputs, null, BioItemList.getPetriDish(culture), new int[]{}, aFluidInputs, aFluidOutputs, aDuration, aEUt, 0)) != null)
            return true;
        return false;
    }


    public boolean addTrimmedBacterialVatRecipe(ItemStack[] aInputs, BioCulture aCulture, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, Materials material, @Nonnegative int glasTier, int aSpecialValue, boolean exactSv) {
        byte gTier = (byte) glasTier;
        int aSievert = 0;
        if (material.getProtons() >= 83 || material.getProtons() == 61 || material.getProtons() == 43)
            aSievert += material.getProtons();
        aSievert = aSievert << 1;
        aSievert = aSievert | (exactSv ? 1 : 0);
        aSievert = aSievert << 2;
        aSievert = aSievert | specialToByte(aSpecialValue);
        aSievert = aSievert << 4;
        aSievert = aSievert | gTier;
        if (sBacteriaVat.addRecipe(new BacteriaVatRecipe(true, aInputs, null, BioItemList.getPetriDish(aCulture), new int[]{}, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSievert)) != null)
            return true;
        return false;
    }

    public static class DynamicGTRecipe extends GT_Recipe implements Serializable {
        public DynamicGTRecipe(boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecialItems, int[] aChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
            super(aOptimize, aInputs, aOutputs, aSpecialItems, aChances, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue);
        }
    }

    public static class BW_Recipe_Map_LiquidFuel extends GT_Recipe.GT_Recipe_Map_Fuel {
        public BW_Recipe_Map_LiquidFuel(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName, String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount, int aMinimalInputItems, int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre, int aNEISpecialValueMultiplier, String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI, boolean aNEIAllowed) {
            super(aRecipeList, aUnlocalizedName, aLocalName, aNEIName, aNEIGUIPath, aUsualInputCount, aUsualOutputCount, aMinimalInputItems, aMinimalInputFluids, aAmperage, aNEISpecialValuePre, aNEISpecialValueMultiplier, aNEISpecialValuePost, aShowVoltageAmperageInNEI, aNEIAllowed);
        }

        public GT_Recipe addLiquidFuel(Materials M, int burn) {
            return super.addFuel(M.getCells(1), Materials.Empty.getCells(1), burn);
        }

        public GT_Recipe addMoltenFuel(Materials M, int burn) {
            return super.addFuel(ItemFluidCell.getUniversalFluidCell(M.getMolten(144L)), Ic2Items.FluidCell.copy(), burn);
        }

        public GT_Recipe addLiquidFuel(FluidStack fluidStack, int burn) {
            return super.addFuel(ItemFluidCell.getUniversalFluidCell(fluidStack), Ic2Items.FluidCell.copy(), burn);
        }


    }

    public static class BacteriaVatRecipe extends GT_Recipe {
        protected BacteriaVatRecipe(boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, ItemStack aSpecialItems, int[] aChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
            super(aOptimize, aInputs, aOutputs, aSpecialItems, aChances, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue);
        }
    }

    public static class BacteriaVatRecipeMap extends BWRecipes.SpecialObjectSensitiveMap {

        public BacteriaVatRecipeMap(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName, String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount, int aMinimalInputItems, int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre, int aNEISpecialValueMultiplier, String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI, boolean aNEIAllowed) {
            super(aRecipeList, aUnlocalizedName, aLocalName, aNEIName, aNEIGUIPath, aUsualInputCount, aUsualOutputCount, aMinimalInputItems, aMinimalInputFluids, aAmperage, aNEISpecialValuePre, aNEISpecialValueMultiplier, aNEISpecialValuePost, aShowVoltageAmperageInNEI, aNEIAllowed);
        }

        protected GT_Recipe addRecipe(GT_Recipe aRecipe, boolean aCheckForCollisions, boolean aFakeRecipe, boolean aHidden) {
            aRecipe.mHidden = aHidden;
            aRecipe.mFakeRecipe = aFakeRecipe;
            GT_Recipe isthere = this.findRecipe((IHasWorldObjectAndCoords) null, false, false, 9223372036854775807L, aRecipe.mFluidInputs, aRecipe.mInputs);

            if (aRecipe.mFluidInputs.length < this.mMinimalInputFluids && aRecipe.mInputs.length < this.mMinimalInputItems) {
                return null;
            } else {
                return aCheckForCollisions && isthere != null && BW_Util.areStacksEqualOrNull((ItemStack) isthere.mSpecialItems, (ItemStack) aRecipe.mSpecialItems) ? null : this.add(aRecipe);
            }
        }

        public GT_Recipe addRecipe(GT_Recipe aRecipe, boolean VanillaGT) {
            if (VanillaGT)
                return addRecipe(aRecipe, true, false, false);
            else
                return addRecipe(aRecipe);
        }

        public GT_Recipe addRecipe(GT_Recipe aRecipe) {
            if (aRecipe.mInputs.length > 0 && GT_Utility.areStacksEqual(aRecipe.mInputs[aRecipe.mInputs.length - 1], GT_Utility.getIntegratedCircuit(32767)))
                return aRecipe;
            else {
                ItemStack[] nu1 = Arrays.copyOf(aRecipe.mInputs, aRecipe.mInputs.length + 1);
                nu1[nu1.length - 1] = GT_Utility.getIntegratedCircuit(9 + nu1.length);
                aRecipe.mInputs = nu1;
            }
            if (this.findRecipe((IHasWorldObjectAndCoords) null, false, 9223372036854775807L, aRecipe.mFluidInputs, aRecipe.mInputs) != null) {
                ItemStack[] nu = Arrays.copyOf(aRecipe.mInputs, aRecipe.mInputs.length + 1);
                int i = 9 + nu.length;
                do {
                    nu[nu.length - 1] = GT_Utility.getIntegratedCircuit(i);
                    i++;
                    aRecipe.mInputs = nu;
                    if (i > 24)
                        i = 1;
                    if (i == 9 + nu.length)
                        return null;
                }
                while (this.findRecipe((IHasWorldObjectAndCoords) null, false, 9223372036854775807L, aRecipe.mFluidInputs, aRecipe.mInputs) != null);
            }
            return this.addRecipe(aRecipe, false, false, false);
        }
    }

    private static class SpecialObjectSensitiveMap extends GT_Recipe.GT_Recipe_Map{

        private SpecialObjectSensitiveMap(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName, String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount, int aMinimalInputItems, int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre, int aNEISpecialValueMultiplier, String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI, boolean aNEIAllowed) {
            super(aRecipeList, aUnlocalizedName, aLocalName, aNEIName, aNEIGUIPath, aUsualInputCount, aUsualOutputCount, aMinimalInputItems, aMinimalInputFluids, aAmperage, aNEISpecialValuePre, aNEISpecialValueMultiplier, aNEISpecialValuePost, aShowVoltageAmperageInNEI, aNEIAllowed);
        }

        /**
         * finds a Recipe matching the aFluid, aSpecial and ItemStack Inputs.
         *
         * @param aTileEntity          an Object representing the current coordinates of the executing Block/Entity/Whatever. This may be null, especially during Startup.
         * @param aRecipe              in case this is != null it will try to use this Recipe first when looking things up.
         * @param aNotUnificated       if this is T the Recipe searcher will unificate the ItemStack Inputs
         * @param aDontCheckStackSizes if set to false will only return recipes that can be executed at least once with the provided input
         * @param aVoltage             Voltage of the Machine or Long.MAX_VALUE if it has no Voltage
         * @param aFluids              the Fluid Inputs
         * @param aSpecialSlot         the content of the Special Slot, the regular Manager doesn't do anything with this, but some custom ones do. Like this one.
         * @param aInputs              the Item Inputs
         * @return the Recipe it has found or null for no matching Recipe
         */
        public GT_Recipe findRecipe(IHasWorldObjectAndCoords aTileEntity, GT_Recipe aRecipe, boolean aNotUnificated, boolean aDontCheckStackSizes, long aVoltage, FluidStack[] aFluids, ItemStack aSpecialSlot, ItemStack... aInputs) {
            // No Recipes? Well, nothing to be found then.
            if (mRecipeList.isEmpty()) return null;

            // Some Recipe Classes require a certain amount of Inputs of certain kinds. Like "at least 1 Fluid + 1 Stack" or "at least 2 Stacks" before they start searching for Recipes.
            // This improves Performance massively, especially if people leave things like Circuits, Molds or Shapes in their Machines to select Sub Recipes.
            if (GregTech_API.sPostloadFinished) {
                if (mMinimalInputFluids > 0) {
                    if (aFluids == null) return null;
                    int tAmount = 0;
                    for (FluidStack aFluid : aFluids) if (aFluid != null) tAmount++;
                    if (tAmount < mMinimalInputFluids) return null;
                }
                if (mMinimalInputItems > 0) {
                    if (aInputs == null) return null;
                    int tAmount = 0;
                    for (ItemStack aInput : aInputs) if (aInput != null) tAmount++;
                    if (tAmount < mMinimalInputItems) return null;
                }
            }

            // Unification happens here in case the Input isn't already unificated.
            if (aNotUnificated) aInputs = GT_OreDictUnificator.getStackArray(true, (Object[]) aInputs);

            // Check the Recipe which has been used last time in order to not have to search for it again, if possible.
            if (aRecipe != null)
                if (!aRecipe.mFakeRecipe && aRecipe.mCanBeBuffered && aRecipe.isRecipeInputEqual(false, aDontCheckStackSizes, aFluids, aInputs) && BW_Util.areStacksEqualOrNull((ItemStack) aRecipe.mSpecialItems, aSpecialSlot))
                    return aRecipe.mEnabled && aVoltage * mAmperage >= aRecipe.mEUt ? aRecipe : null;

            // Now look for the Recipes inside the Item HashMaps, but only when the Recipes usually have Items.
            if (mUsualInputCount > 0 && aInputs != null)
                for (ItemStack tStack : aInputs)
                    if (tStack != null) {
                        Collection<GT_Recipe> tRecipes = mRecipeItemMap.get(new GT_ItemStack(tStack));
                        if (tRecipes != null)
                            for (GT_Recipe tRecipe : tRecipes)
                                if (!tRecipe.mFakeRecipe && tRecipe.isRecipeInputEqual(false, aDontCheckStackSizes, aFluids, aInputs) && BW_Util.areStacksEqualOrNull((ItemStack) tRecipe.mSpecialItems, aSpecialSlot))
                                    return tRecipe.mEnabled && aVoltage * mAmperage >= tRecipe.mEUt ? tRecipe : null;
                        tRecipes = mRecipeItemMap.get(new GT_ItemStack(GT_Utility.copyMetaData(GT_Values.W, tStack)));
                        if (tRecipes != null)
                            for (GT_Recipe tRecipe : tRecipes)
                                if (!tRecipe.mFakeRecipe && tRecipe.isRecipeInputEqual(false, aDontCheckStackSizes, aFluids, aInputs) && BW_Util.areStacksEqualOrNull((ItemStack) tRecipe.mSpecialItems, aSpecialSlot))
                                    return tRecipe.mEnabled && aVoltage * mAmperage >= tRecipe.mEUt ? tRecipe : null;
                    }

            // If the minimal Amount of Items for the Recipe is 0, then it could be a Fluid-Only Recipe, so check that Map too.
            if (mMinimalInputItems == 0 && aFluids != null)
                for (FluidStack aFluid : aFluids)
                    if (aFluid != null) {
                        Collection<GT_Recipe> tRecipes = mRecipeFluidMap.get(aFluid.getFluid());
                        if (tRecipes != null) for (GT_Recipe tRecipe : tRecipes)
                            if (!tRecipe.mFakeRecipe && tRecipe.isRecipeInputEqual(false, aDontCheckStackSizes, aFluids, aInputs) && BW_Util.areStacksEqualOrNull((ItemStack) tRecipe.mSpecialItems, aSpecialSlot))
                                return tRecipe.mEnabled && aVoltage * mAmperage >= tRecipe.mEUt ? tRecipe : null;
                    }

            // And nothing has been found.
            return null;
        }
    }

    public static class BWNBTDependantCraftingRecipe implements IRecipe {

        ItemStack result;
        Map<Character, ItemStack> charToStackMap = new HashMap<>(9,1);
        String[] shape;

        public BWNBTDependantCraftingRecipe(ItemStack result, Object... recipe) {
            this.result = result;
            this.shape = new String[3];
            System.arraycopy(recipe,0, this.shape,0,3);
            this.charToStackMap.put(' ', null);
            for (int i = 3; i < recipe.length; i+=2) {
                this.charToStackMap.put((char)recipe[i],((ItemStack)recipe[i+1]).copy());
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof BWRecipes.BWNBTDependantCraftingRecipe)) return false;

            BWRecipes.BWNBTDependantCraftingRecipe that = (BWRecipes.BWNBTDependantCraftingRecipe) o;

            if (!Objects.equals(this.result, that.result)) return false;
            if (!Objects.equals(this.charToStackMap, that.charToStackMap))
                return false;
            // Probably incorrect - comparing Object[] arrays with Arrays.equals
            return Arrays.equals(this.shape, that.shape);
        }

        @Override
        public int hashCode() {
            int result1 = this.result != null ? this.result.hashCode() : 0;
            result1 = 31 * result1 + (this.charToStackMap != null ? this.charToStackMap.hashCode() : 0);
            result1 = 31 * result1 + Arrays.hashCode(this.shape);
            return result1;
        }

        @Override
        public boolean matches(InventoryCrafting p_77569_1_, World p_77569_2_) {
            for (int x = 0; x < 3; x++) {
                for (int y = 0; y < 3; y++) {
                    ItemStack toCheck = p_77569_1_.getStackInRowAndColumn(y,x);
                    ItemStack ref = this.charToStackMap.get(this.shape[x].toCharArray()[y]);
                    if (!BW_Util.areStacksEqualOrNull(toCheck,ref))
                        return false;
                }
            }
            return true;
        }

        @Override
        public ItemStack getCraftingResult(InventoryCrafting p_77572_1_) {
            return this.result.copy();
        }

        @Override
        public int getRecipeSize() {
            return 10;
        }

        @Override
        public ItemStack getRecipeOutput() {
            return this.result;
        }
    }
}

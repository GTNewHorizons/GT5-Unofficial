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

package com.github.bartimaeusnek.bartworks.common.tileentities.tiered;

import com.github.bartimaeusnek.bartworks.common.items.LabModule;
import com.github.bartimaeusnek.bartworks.common.items.LabParts;
import com.github.bartimaeusnek.bartworks.common.loaders.BioCultureLoader;
import com.github.bartimaeusnek.bartworks.common.loaders.BioItemList;
import com.github.bartimaeusnek.bartworks.common.loaders.FluidLoader;
import com.github.bartimaeusnek.bartworks.util.*;
import cpw.mods.fml.common.Loader;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.objects.XSTR;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.items.behaviors.Behaviour_DataOrb;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class GT_MetaTileEntity_BioLab extends GT_MetaTileEntity_BasicMachine {

    private static final String MGUINAME = "BW.GUI.BioLab.png";

    public GT_MetaTileEntity_BioLab(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 1, (String) null, 6, 2, MGUINAME, null, new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/fluid_extractor/OVERLAY_SIDE_ACTIVE")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/fluid_extractor/OVERLAY_SIDE")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/microwave/OVERLAY_FRONT_ACTIVE")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/microwave/OVERLAY_FRONT")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/chemical_reactor/OVERLAY_FRONT_ACTIVE")/*this is topactive*/), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/chemical_reactor/OVERLAY_FRONT")/*this is top*/), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/polarizer/OVERLAY_BOTTOM_ACTIVE")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/polarizer/OVERLAY_BOTTOM")));
    }

    public GT_MetaTileEntity_BioLab(String aName, int aTier, int aAmperage, String aDescription, ITexture[][][] aTextures, String aNEIName) {
        super(aName, aTier, aAmperage, aDescription, aTextures, 6, 2, MGUINAME, aNEIName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new GT_MetaTileEntity_BioLab(this.mName, this.mTier, this.mAmperage, this.mDescription, this.mTextures, this.mNEIName);
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeList() {
        return BWRecipes.instance.getMappingsFor(BWRecipes.BIOLABBYTE);
    }

    @Override
    public int getCapacity() {
        return this.mTier * 1000;
    }

    @Override
    public int checkRecipe(boolean skipOC) {

        int rTier = 3;
        FluidStack dnaFluid = Loader.isModLoaded("gendustry") ? FluidRegistry.getFluidStack("liquiddna", 1000) : Materials.Biomass.getFluid(1000L);

        if (this.getSpecialSlot() != null && this.getSpecialSlot().getItem() instanceof LabModule) {
            int damage = getSpecialSlot().getItemDamage();
            switch (damage) {
                case 0:
                    if (GT_Utility.isStackValid(this.mInventory[4]) && this.mInventory[4].getItem() instanceof LabParts && this.mInventory[4].getItemDamage() == 0 && this.mInventory[4].getTagCompound() != null &&  //checks if it is a Culture
                            GT_Utility.isStackValid(this.mInventory[5]) && this.mInventory[5].getItem() instanceof LabParts && this.mInventory[5].getItemDamage() == 1 && this.mInventory[5].getTagCompound() == null &&
                            GT_Utility.isStackValid(this.mInventory[6]) && this.mInventory[6].getItem() instanceof LabParts && this.mInventory[6].getItemDamage() == 3 &&
                            GT_Utility.areStacksEqual(this.mInventory[7], Materials.Ethanol.getCells(1)) &&
                            this.mFluid.isFluidEqual(FluidRegistry.getFluidStack("ic2distilledwater", 1000)) && this.mFluid.amount >= 1000
                    ) {

                        NBTTagCompound DNABioDataTag = this.mInventory[4].getTagCompound().getCompoundTag("DNA");
                        if (DNABioDataTag == null)
                            return super.checkRecipe(skipOC);
                        BioData cultureDNABioData = BioData.getBioDataFromName(this.mInventory[4].getTagCompound().getCompoundTag("DNA").getString("Name"));
                        if (cultureDNABioData == null)
                            return super.checkRecipe(skipOC);

                        if (this.mTier < rTier + cultureDNABioData.getTier())
                            return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;

                        for (int i = 4; i < 8; i++) {
                            if (this.mInventory[i] != null)
                                this.mInventory[i].stackSize--;
                        }

                        this.mFluid.amount -= 1000;

                        if (cultureDNABioData.getChance() > new XSTR().nextInt(10000)) {
                            this.mOutputItems[0] = BioItemList.getDNASampleFlask(BioDNA.convertDataToDNA(cultureDNABioData));
                        }
                        this.mOutputItems[1] = GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1L);
                        this.calculateOverclockedNess(BW_Util.getMachineVoltageFromTier(rTier + cultureDNABioData.getTier()), 500);

                        return FOUND_AND_SUCCESSFULLY_USED_RECIPE;
                    }
                    break;
                case 1: {
                    if (GT_Utility.isStackValid(this.mInventory[4]) && this.mInventory[4].getItem() instanceof LabParts && this.mInventory[4].getItemDamage() == 1 && this.mInventory[4].getTagCompound() != null &&  //checks if it is a Culture
                            GT_Utility.isStackValid(this.mInventory[7]) && GT_Utility.areStacksEqual(this.mInventory[7], ItemList.Tool_DataOrb.get(1L)) &&
                            GT_Utility.isStackValid(this.mInventory[5]) && GT_Utility.areStacksEqual(this.mInventory[5], FluidLoader.BioLabFluidCells[0]) &&
                            GT_Utility.isStackValid(this.mInventory[6]) && GT_Utility.areStacksEqual(this.mInventory[6], FluidLoader.BioLabFluidCells[3]) &&
                            this.mFluid.isFluidEqual(dnaFluid) && this.mFluid.amount >= 1000) {
                        NBTTagCompound DNABioDataTag = this.mInventory[4].getTagCompound();
                        if (DNABioDataTag == null)
                            return super.checkRecipe(skipOC);
                        BioData cultureDNABioData = BioData.getBioDataFromName(DNABioDataTag.getString("Name"));
                        if (cultureDNABioData == null)
                            return super.checkRecipe(skipOC);

                        if (this.mTier < 1 + rTier + cultureDNABioData.getTier())
                            return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;

                        for (int i = 4; i < 8; i++) {
                            if (this.mInventory[i] != null)
                                this.mInventory[i].stackSize--;
                        }

                        this.mFluid.amount -= 1000;

                        ItemStack Outp = ItemList.Tool_DataOrb.get(1L);
                        Behaviour_DataOrb.setDataTitle(Outp, "DNA Sample");
                        Behaviour_DataOrb.setDataName(Outp, cultureDNABioData.getName());

                        if (cultureDNABioData.getChance() > new XSTR().nextInt(10000)) {
                            this.mOutputItems[0] = Outp;
                        } else
                            this.mOutputItems[0] = ItemList.Tool_DataOrb.get(1L);
                        this.mOutputItems[1] = ItemList.Cell_Universal_Fluid.get(2L);

                        this.calculateOverclockedNess(BW_Util.getMachineVoltageFromTier(1 + rTier + cultureDNABioData.getTier()), 500);

                        return FOUND_AND_SUCCESSFULLY_USED_RECIPE;

                    }
                }
                break;
                case 2: {
                    ItemStack inp2 = ItemList.Tool_DataOrb.get(1L);
                    Behaviour_DataOrb.setDataTitle(inp2, "DNA Sample");
                    Behaviour_DataOrb.setDataName(inp2, BioCultureLoader.BIO_DATA_BETA_LACMATASE.getName());
                    if (
                            GT_Utility.isStackValid(this.mInventory[4]) && GT_Utility.areStacksEqual(FluidLoader.BioLabFluidCells[1], this.mInventory[4]) &&  //checks if it is a Culture
                                    GT_Utility.isStackValid(this.mInventory[5]) && GT_Utility.areStacksEqual(this.mInventory[5], BioItemList.getPlasmidCell(null)) &&
                                    GT_Utility.isStackValid(this.mInventory[6]) && GT_Utility.areStacksEqual(this.mInventory[6], ItemList.Tool_DataOrb.get(1L), true) &&
                                    Behaviour_DataOrb.getDataTitle(this.mInventory[6]).equals("DNA Sample") && (!(Behaviour_DataOrb.getDataName(this.mInventory[6]).isEmpty())) &&
                                    GT_Utility.isStackValid(this.mInventory[7]) && GT_Utility.areStacksEqual(this.mInventory[7], inp2) &&
                                    this.mFluid.isFluidEqual(dnaFluid) && (this.mFluid.amount >= 1000)) {
                        BioData cultureDNABioData = BioData.getBioDataFromName(Behaviour_DataOrb.getDataName(this.mInventory[6]));
                        if (cultureDNABioData == null)
                            return super.checkRecipe(skipOC);
                        if (this.mTier < 1 + rTier + cultureDNABioData.getTier())
                            return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
                        for (int i = 4; i < 6; i++) {
                            if (this.mInventory[i] != null)
                                this.mInventory[i].stackSize--;
                        }
                        this.mFluid.amount -= 1000;
                        if (cultureDNABioData.getChance() > new XSTR().nextInt(10000)) {
                            this.mOutputItems[0] = BioItemList.getPlasmidCell(BioPlasmid.convertDataToPlasmid(cultureDNABioData));
                        }
                        this.mOutputItems[1] = ItemList.Cell_Universal_Fluid.get(1L);
                        this.calculateOverclockedNess(BW_Util.getMachineVoltageFromTier(1 + rTier + cultureDNABioData.getTier()), 500);
                        return FOUND_AND_SUCCESSFULLY_USED_RECIPE;
                    }

                }
                break;

                case 3: {
                    if (
                            GT_Utility.isStackValid(this.mInventory[4]) && GT_Utility.areStacksEqual(this.mInventory[4], BioItemList.getPetriDish(null), true) && this.mInventory[4].getTagCompound() != null &&
                                    GT_Utility.isStackValid(this.mInventory[5]) && GT_Utility.areStacksEqual(this.mInventory[5], BioItemList.getPlasmidCell(null), true) && this.mInventory[5].getTagCompound() != null &&
                                    GT_Utility.isStackValid(this.mInventory[6]) && GT_Utility.areStacksEqual(this.mInventory[6], FluidLoader.BioLabFluidCells[2]) &&
                                    this.mFluid.isFluidEqual(FluidRegistry.getFluidStack("ic2distilledwater", 1000)) && this.mFluid.amount >= 1000) {
                        BioData cultureDNABioData = BioData.getBioDataFromNBTTag(this.mInventory[5].getTagCompound());
                        BioCulture bioCulture = BioCulture.getBioCultureFromNBTTag(this.mInventory[4].getTagCompound());
                        if (cultureDNABioData == null || bioCulture == null)
                            return super.checkRecipe(skipOC);
                        if (this.mTier < 3 + rTier + cultureDNABioData.getTier())
                            return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
                        for (int i = 4; i < 7; i++) {
                            if (this.mInventory[i] != null)
                                this.mInventory[i].stackSize--;
                        }
                        this.mFluid.amount -= 1000;
                        bioCulture.setPlasmid(BioPlasmid.convertDataToPlasmid(cultureDNABioData));
                        if (cultureDNABioData.getChance() > new XSTR().nextInt(10000)) {
                            this.mOutputItems[0] = BioItemList.getPetriDish(checkForExisting(bioCulture));
                        }
                        this.mOutputItems[1] = ItemList.Cell_Universal_Fluid.get(1L);
                        this.calculateOverclockedNess(BW_Util.getMachineVoltageFromTier(3 + rTier + cultureDNABioData.getTier()), 500);
                        return FOUND_AND_SUCCESSFULLY_USED_RECIPE;
                    }
                }
                break;
                case 4: {

                    ItemStack Outp = ItemList.Tool_DataOrb.get(1L);
                    Behaviour_DataOrb.setDataTitle(Outp, "DNA Sample");

                    if (
                            GT_Utility.isStackValid(this.mInventory[4]) && GT_Utility.areStacksEqual(this.mInventory[4], BioItemList.getPetriDish(null)) &&
                                    GT_Utility.isStackValid(this.mInventory[5]) && GT_Utility.areStacksEqual(this.mInventory[5], BioItemList.getOther(4)) &&
                                    GT_Utility.isStackValid(this.mInventory[6]) && GT_Utility.areStacksEqual(this.mInventory[6], ItemList.Circuit_Chip_Stemcell.get(2L)) &&
                                    GT_Utility.isStackValid(this.mInventory[7]) && GT_Utility.areStacksEqual(this.mInventory[7], ItemList.Tool_DataOrb.get(1L), true) &&
                                    Behaviour_DataOrb.getDataTitle(this.mInventory[7]).equals("DNA Sample") && this.mFluid.isFluidEqual(dnaFluid) && (this.mFluid.amount >= 8000)) {

                        BioData cultureDNABioData = BioData.getBioDataFromName(Behaviour_DataOrb.getDataName(this.mInventory[7]));
                        if (cultureDNABioData == null)
                            return super.checkRecipe(skipOC);
                        if (this.mTier < 3 + rTier + cultureDNABioData.getTier())
                            return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
                        for (int i = 4; i < 7; i++) {
                            if (this.mInventory[i] != null)
                                this.mInventory[i].stackSize--;
                        }
                        this.mFluid.amount -= 8000;
                        if (cultureDNABioData.getChance() > new XSTR().nextInt(10000)) {
                            BioCulture out = BioCulture.getBioCulture(BioDNA.convertDataToDNA(cultureDNABioData));
                            out.setPlasmid(BioPlasmid.convertDataToPlasmid(cultureDNABioData));
                            this.mOutputItems[0] = BioItemList.getPetriDish(out);
                        }
                        this.calculateOverclockedNess(BW_Util.getMachineVoltageFromTier(3 + rTier + cultureDNABioData.getTier()), 500);
                        return FOUND_AND_SUCCESSFULLY_USED_RECIPE;
                    }
                }
                break;
                default:
                    return super.checkRecipe(skipOC);
            }
        }
        return super.checkRecipe(skipOC);
    }

    private BioCulture checkForExisting(BioCulture culture) {
        if (culture == null)
            return null;
        for (BioCulture bc : BioCulture.BIO_CULTURE_ARRAY_LIST)
            if (culture.getdDNA().equals(bc.getdDNA()) && culture.getPlasmid().equals(bc.getPlasmid()))
                return bc;
        return culture;
    }

    @Override
    @SuppressWarnings("deprecation")
    public String[] getDescription() {
        return new String[]{StatCollector.translateToLocal("tooltip.tile.biolab.0.name"), StatCollector.translateToLocal("tooltip.bw.1.name") + ChatColorHelper.DARKGREEN + " BartWorks"};
    }
}

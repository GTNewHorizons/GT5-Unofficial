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

package com.github.bartimaeusnek.bartworks.common.tileentities.tiered;

import static gregtech.api.enums.Mods.Gendustry;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.github.bartimaeusnek.bartworks.API.recipe.BartWorksRecipeMaps;
import com.github.bartimaeusnek.bartworks.common.items.LabModule;
import com.github.bartimaeusnek.bartworks.common.items.LabParts;
import com.github.bartimaeusnek.bartworks.common.loaders.BioCultureLoader;
import com.github.bartimaeusnek.bartworks.common.loaders.BioItemList;
import com.github.bartimaeusnek.bartworks.common.loaders.FluidLoader;
import com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference;
import com.github.bartimaeusnek.bartworks.util.BW_Util;
import com.github.bartimaeusnek.bartworks.util.BioCulture;
import com.github.bartimaeusnek.bartworks.util.BioDNA;
import com.github.bartimaeusnek.bartworks.util.BioData;
import com.github.bartimaeusnek.bartworks.util.BioPlasmid;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.objects.XSTR;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gregtech.common.items.behaviors.Behaviour_DataOrb;

public class GT_MetaTileEntity_BioLab extends GT_MetaTileEntity_BasicMachine {

    private static final int DNA_EXTRACTION_MODULE = 0;
    private static final int PCR_THERMOCYCLE_MODULE = 1;
    private static final int PLASMID_SYNTHESIS_MODULE = 2;
    private static final int TRANSFORMATION_MODULE = 3;
    private static final int CLONAL_CELLULAR_SYNTHESIS_MODULE = 4;
    private static final int INCUBATION_MODULE = 5;

    public GT_MetaTileEntity_BioLab(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            1,
            (String) null,
            6,
            2,
            TextureFactory.of(
                TextureFactory
                    .of(new Textures.BlockIcons.CustomIcon("basicmachines/fluid_extractor/OVERLAY_SIDE_ACTIVE")),
                TextureFactory.builder()
                    .addIcon(
                        new Textures.BlockIcons.CustomIcon("basicmachines/fluid_extractor/OVERLAY_SIDE_ACTIVE_GLOW"))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/fluid_extractor/OVERLAY_SIDE")),
                TextureFactory.builder()
                    .addIcon(new Textures.BlockIcons.CustomIcon("basicmachines/fluid_extractor/OVERLAY_SIDE_GLOW"))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/microwave/OVERLAY_FRONT_ACTIVE")),
                TextureFactory.builder()
                    .addIcon(new Textures.BlockIcons.CustomIcon("basicmachines/microwave/OVERLAY_FRONT_ACTIVE_GLOW"))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/microwave/OVERLAY_FRONT")),
                TextureFactory.builder()
                    .addIcon(new Textures.BlockIcons.CustomIcon("basicmachines/microwave/OVERLAY_FRONT_GLOW"))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory
                    .of(new Textures.BlockIcons.CustomIcon("basicmachines/chemical_reactor/OVERLAY_FRONT_ACTIVE")),
                TextureFactory.builder()
                    .addIcon(
                        new Textures.BlockIcons.CustomIcon("basicmachines/chemical_reactor/OVERLAY_FRONT_ACTIVE_GLOW"))
                    .glow()
                    .build() /* this is topactive */),
            TextureFactory.of(
                TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/chemical_reactor/OVERLAY_FRONT")),
                TextureFactory.builder()
                    .addIcon(new Textures.BlockIcons.CustomIcon("basicmachines/chemical_reactor/OVERLAY_FRONT_GLOW"))
                    .glow()
                    .build() /* this is top */),
            TextureFactory.of(
                TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/polarizer/OVERLAY_BOTTOM_ACTIVE")),
                TextureFactory.builder()
                    .addIcon(new Textures.BlockIcons.CustomIcon("basicmachines/polarizer/OVERLAY_BOTTOM_ACTIVE_GLOW"))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/polarizer/OVERLAY_BOTTOM")),
                TextureFactory.builder()
                    .addIcon(new Textures.BlockIcons.CustomIcon("basicmachines/polarizer/OVERLAY_BOTTOM_GLOW"))
                    .glow()
                    .build()));
    }

    public GT_MetaTileEntity_BioLab(String aName, int aTier, int aAmperage, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aAmperage, aDescription, aTextures, 6, 2);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new GT_MetaTileEntity_BioLab(
            this.mName,
            this.mTier,
            this.mAmperage,
            this.mDescriptionArray,
            this.mTextures);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return BartWorksRecipeMaps.bioLabRecipes;
    }

    @Override
    public int getCapacity() {
        return this.mTier * 1000;
    }

    @Override
    public int checkRecipe(boolean skipOC) {

        int rTier = 3;
        FluidStack dnaFluid = Gendustry.isModLoaded() ? FluidRegistry.getFluidStack("liquiddna", 1000)
            : Materials.Biomass.getFluid(1000L);

        if (this.getSpecialSlot() != null && this.getSpecialSlot()
            .getItem() instanceof LabModule) {
            int damage = this.getSpecialSlot()
                .getItemDamage();
            switch (damage) {
                case DNA_EXTRACTION_MODULE:
                    if (GT_Utility.isStackValid(this.mInventory[this.getInputSlot()])
                        && this.mInventory[this.getInputSlot()].getItem() instanceof LabParts
                        && this.mInventory[this.getInputSlot()].getItemDamage() == 0
                        && this.mInventory[this.getInputSlot()].getTagCompound() != null
                        && // checks if it is a Culture
                        GT_Utility.isStackValid(this.mInventory[this.getInputSlot() + 1])
                        && this.mInventory[this.getInputSlot() + 1].getItem() instanceof LabParts
                        && this.mInventory[this.getInputSlot() + 1].getItemDamage() == 1
                        && this.mInventory[this.getInputSlot() + 1].getTagCompound() == null
                        && GT_Utility.isStackValid(this.mInventory[this.getInputSlot() + 2])
                        && this.mInventory[this.getInputSlot() + 2].getItem() instanceof LabParts
                        && this.mInventory[this.getInputSlot() + 2].getItemDamage() == 3
                        && GT_Utility
                            .areStacksEqual(this.mInventory[this.getInputSlot() + 3], Materials.Ethanol.getCells(1))
                        && this.mFluid != null
                        && this.mFluid.isFluidEqual(FluidRegistry.getFluidStack("ic2distilledwater", 1000))
                        && this.mFluid.amount >= 1000) {

                        NBTTagCompound DNABioDataTag = this.mInventory[this.getInputSlot()].getTagCompound()
                            .getCompoundTag("DNA");
                        if (DNABioDataTag == null) return super.checkRecipe(skipOC);
                        BioData cultureDNABioData = BioData.getBioDataFromName(
                            this.mInventory[this.getInputSlot()].getTagCompound()
                                .getCompoundTag("DNA")
                                .getString("Name"));
                        if (cultureDNABioData == null) return super.checkRecipe(skipOC);

                        if (this.mTier < rTier + cultureDNABioData.getTier())
                            return GT_MetaTileEntity_BasicMachine.FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;

                        for (int i = 0; i < 4; i++) {
                            if (this.mInventory[this.getInputSlot() + i] != null)
                                this.mInventory[this.getInputSlot() + i].stackSize--;
                        }

                        this.mFluid.amount -= 1000;

                        if (cultureDNABioData.getChance() > new XSTR().nextInt(10000)) {
                            this.mOutputItems[0] = BioItemList
                                .getDNASampleFlask(BioDNA.convertDataToDNA(cultureDNABioData));
                        }
                        this.mOutputItems[1] = GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1L);
                        this.calculateOverclockedNess(
                            BW_Util.getMachineVoltageFromTier(rTier + cultureDNABioData.getTier()),
                            500);

                        return GT_MetaTileEntity_BasicMachine.FOUND_AND_SUCCESSFULLY_USED_RECIPE;
                    }
                    break;
                case PCR_THERMOCYCLE_MODULE: {
                    if (GT_Utility.isStackValid(this.mInventory[this.getInputSlot()])
                        && this.mInventory[this.getInputSlot()].getItem() instanceof LabParts
                        && this.mInventory[this.getInputSlot()].getItemDamage() == 1
                        && this.mInventory[this.getInputSlot()].getTagCompound() != null
                        && // checks if it is a Culture
                        GT_Utility.isStackValid(this.mInventory[this.getInputSlot() + 3])
                        && GT_Utility
                            .areStacksEqual(this.mInventory[this.getInputSlot() + 3], ItemList.Tool_DataOrb.get(1L))
                        && GT_Utility.isStackValid(this.mInventory[this.getInputSlot() + 1])
                        && GT_Utility
                            .areStacksEqual(this.mInventory[this.getInputSlot() + 1], FluidLoader.BioLabFluidCells[0])
                        && GT_Utility.isStackValid(this.mInventory[this.getInputSlot() + 2])
                        && GT_Utility
                            .areStacksEqual(this.mInventory[this.getInputSlot() + 2], FluidLoader.BioLabFluidCells[3])
                        && this.mFluid != null
                        && this.mFluid.isFluidEqual(dnaFluid)
                        && this.mFluid.amount >= 1000) {
                        NBTTagCompound DNABioDataTag = this.mInventory[this.getInputSlot()].getTagCompound();
                        if (DNABioDataTag == null) return super.checkRecipe(skipOC);
                        BioData cultureDNABioData = BioData.getBioDataFromName(DNABioDataTag.getString("Name"));
                        if (cultureDNABioData == null) return super.checkRecipe(skipOC);

                        if (this.mTier < 1 + rTier + cultureDNABioData.getTier())
                            return GT_MetaTileEntity_BasicMachine.FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;

                        for (int i = 0; i < 4; i++) {
                            if (this.mInventory[this.getInputSlot() + i] != null)
                                this.mInventory[this.getInputSlot() + i].stackSize--;
                        }

                        this.mFluid.amount -= 1000;

                        ItemStack Outp = ItemList.Tool_DataOrb.get(1L);
                        Behaviour_DataOrb.setDataTitle(Outp, "DNA Sample");
                        Behaviour_DataOrb.setDataName(Outp, cultureDNABioData.getName());

                        if (cultureDNABioData.getChance() > new XSTR().nextInt(10000)) {
                            this.mOutputItems[0] = Outp;
                        } else this.mOutputItems[0] = ItemList.Tool_DataOrb.get(1L);
                        this.mOutputItems[1] = ItemList.Cell_Empty.get(2L);

                        this.calculateOverclockedNess(
                            BW_Util.getMachineVoltageFromTier(1 + rTier + cultureDNABioData.getTier()),
                            500);

                        return GT_MetaTileEntity_BasicMachine.FOUND_AND_SUCCESSFULLY_USED_RECIPE;
                    }
                }
                    break;
                case PLASMID_SYNTHESIS_MODULE: {
                    ItemStack inp2 = ItemList.Tool_DataOrb.get(1L);
                    Behaviour_DataOrb.setDataTitle(inp2, "DNA Sample");
                    Behaviour_DataOrb.setDataName(inp2, BioCultureLoader.BIO_DATA_BETA_LACMATASE.getName());
                    if (GT_Utility.isStackValid(this.mInventory[this.getInputSlot()])
                        && GT_Utility
                            .areStacksEqual(FluidLoader.BioLabFluidCells[1], this.mInventory[this.getInputSlot()])
                        && // checks
                           // if
                           // it
                           // is
                           // a
                           // Culture
                        GT_Utility.isStackValid(this.mInventory[this.getInputSlot() + 1])
                        && GT_Utility
                            .areStacksEqual(this.mInventory[this.getInputSlot() + 1], BioItemList.getPlasmidCell(null))
                        && GT_Utility.isStackValid(this.mInventory[this.getInputSlot() + 2])
                        && GT_Utility.areStacksEqual(
                            this.mInventory[this.getInputSlot() + 2],
                            ItemList.Tool_DataOrb.get(1L),
                            true)
                        && "DNA Sample".equals(Behaviour_DataOrb.getDataTitle(this.mInventory[this.getInputSlot() + 2]))
                        && !Behaviour_DataOrb.getDataName(this.mInventory[this.getInputSlot() + 2])
                            .isEmpty()
                        && GT_Utility.isStackValid(this.mInventory[this.getInputSlot() + 3])
                        && GT_Utility.areStacksEqual(this.mInventory[this.getInputSlot() + 3], inp2)
                        && this.mFluid != null
                        && this.mFluid.isFluidEqual(dnaFluid)
                        && this.mFluid.amount >= 1000) {
                        BioData cultureDNABioData = BioData.getBioDataFromName(
                            Behaviour_DataOrb.getDataName(this.mInventory[this.getInputSlot() + 2]));
                        if (cultureDNABioData == null) return super.checkRecipe(skipOC);
                        if (this.mTier < 1 + rTier + cultureDNABioData.getTier())
                            return GT_MetaTileEntity_BasicMachine.FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
                        for (int i = 0; i < 2; i++) {
                            if (this.mInventory[this.getInputSlot() + i] != null)
                                this.mInventory[this.getInputSlot() + i].stackSize--;
                        }
                        this.mFluid.amount -= 1000;
                        if (cultureDNABioData.getChance() > new XSTR().nextInt(10000)) {
                            this.mOutputItems[0] = BioItemList
                                .getPlasmidCell(BioPlasmid.convertDataToPlasmid(cultureDNABioData));
                        }
                        this.mOutputItems[1] = ItemList.Cell_Empty.get(1L);
                        this.calculateOverclockedNess(
                            BW_Util.getMachineVoltageFromTier(1 + rTier + cultureDNABioData.getTier()),
                            500);
                        return GT_MetaTileEntity_BasicMachine.FOUND_AND_SUCCESSFULLY_USED_RECIPE;
                    }
                }
                    break;
                case TRANSFORMATION_MODULE: {
                    if (GT_Utility.isStackValid(this.mInventory[this.getInputSlot()])
                        && GT_Utility
                            .areStacksEqual(this.mInventory[this.getInputSlot()], BioItemList.getPetriDish(null), true)
                        && this.mInventory[this.getInputSlot()].getTagCompound() != null
                        && GT_Utility.isStackValid(this.mInventory[this.getInputSlot() + 1])
                        && GT_Utility.areStacksEqual(
                            this.mInventory[this.getInputSlot() + 1],
                            BioItemList.getPlasmidCell(null),
                            true)
                        && this.mInventory[this.getInputSlot() + 1].getTagCompound() != null
                        && GT_Utility.isStackValid(this.mInventory[this.getInputSlot() + 2])
                        && GT_Utility
                            .areStacksEqual(this.mInventory[this.getInputSlot() + 2], FluidLoader.BioLabFluidCells[2])
                        && this.mFluid != null
                        && this.mFluid.isFluidEqual(FluidRegistry.getFluidStack("ic2distilledwater", 1000))
                        && this.mFluid.amount >= 1000) {
                        BioData cultureDNABioData = BioData
                            .getBioDataFromNBTTag(this.mInventory[this.getInputSlot() + 1].getTagCompound());
                        BioCulture bioCulture = BioCulture
                            .getBioCultureFromNBTTag(this.mInventory[this.getInputSlot()].getTagCompound());
                        if (cultureDNABioData == null || bioCulture == null) return super.checkRecipe(skipOC);
                        if (this.mTier < 3 + rTier + cultureDNABioData.getTier())
                            return GT_MetaTileEntity_BasicMachine.FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
                        for (int i = 0; i < 3; i++) {
                            if (this.mInventory[this.getInputSlot() + i] != null)
                                this.mInventory[this.getInputSlot() + i].stackSize--;
                        }
                        this.mFluid.amount -= 1000;
                        bioCulture = bioCulture.setPlasmid(BioPlasmid.convertDataToPlasmid(cultureDNABioData));
                        if (cultureDNABioData.getChance() > new XSTR().nextInt(10000)) {
                            this.mOutputItems[0] = BioItemList.getPetriDish(bioCulture);
                        }
                        this.mOutputItems[1] = ItemList.Cell_Empty.get(1L);
                        this.calculateOverclockedNess(
                            BW_Util.getMachineVoltageFromTier(3 + rTier + cultureDNABioData.getTier()),
                            500);
                        return GT_MetaTileEntity_BasicMachine.FOUND_AND_SUCCESSFULLY_USED_RECIPE;
                    }
                }
                    break;
                case CLONAL_CELLULAR_SYNTHESIS_MODULE: {
                    ItemStack Outp = ItemList.Tool_DataOrb.get(1L);
                    Behaviour_DataOrb.setDataTitle(Outp, "DNA Sample");

                    if (GT_Utility.isStackValid(this.mInventory[this.getInputSlot()])
                        && GT_Utility
                            .areStacksEqual(this.mInventory[this.getInputSlot()], BioItemList.getPetriDish(null))
                        && GT_Utility.isStackValid(this.mInventory[this.getInputSlot() + 1])
                        && GT_Utility.areStacksEqual(this.mInventory[this.getInputSlot() + 1], BioItemList.getOther(4))
                        && GT_Utility.isStackValid(this.mInventory[this.getInputSlot() + 2])
                        && GT_Utility.areStacksEqual(
                            this.mInventory[this.getInputSlot() + 2],
                            ItemList.Circuit_Chip_Stemcell.get(2L))
                        && GT_Utility.isStackValid(this.mInventory[this.getInputSlot() + 3])
                        && GT_Utility.areStacksEqual(
                            this.mInventory[this.getInputSlot() + 3],
                            ItemList.Tool_DataOrb.get(1L),
                            true)
                        && "DNA Sample".equals(Behaviour_DataOrb.getDataTitle(this.mInventory[this.getInputSlot() + 3]))
                        && this.mFluid.isFluidEqual(dnaFluid)
                        && this.mFluid.amount >= 8000) {

                        BioData cultureDNABioData = BioData.getBioDataFromName(
                            Behaviour_DataOrb.getDataName(this.mInventory[this.getInputSlot() + 3]));
                        if (cultureDNABioData == null) return super.checkRecipe(skipOC);
                        if (this.mTier < 3 + rTier + cultureDNABioData.getTier())
                            return GT_MetaTileEntity_BasicMachine.FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
                        for (int i = 0; i < 3; i++) {
                            if (this.mInventory[this.getInputSlot() + i] != null)
                                this.mInventory[this.getInputSlot() + i].stackSize--;
                        }
                        this.mFluid.amount -= 8000;
                        if (cultureDNABioData.getChance() > new XSTR().nextInt(10000)) {
                            BioCulture out = BioCulture.getBioCulture(BioDNA.convertDataToDNA(cultureDNABioData));
                            if (out == null) return GT_MetaTileEntity_BasicMachine.DID_NOT_FIND_RECIPE;
                            out = out.setPlasmid(BioPlasmid.convertDataToPlasmid(cultureDNABioData));
                            this.mOutputItems[0] = BioItemList.getPetriDish(out);
                        }
                        this.calculateOverclockedNess(
                            BW_Util.getMachineVoltageFromTier(3 + rTier + cultureDNABioData.getTier()),
                            500);
                        return GT_MetaTileEntity_BasicMachine.FOUND_AND_SUCCESSFULLY_USED_RECIPE;
                    }
                }
                    break;
                case INCUBATION_MODULE:
                default:
                    break;
            }
        }
        return super.checkRecipe(skipOC);
    }

    @Override
    public String[] getDescription() {
        return new String[] { StatCollector.translateToLocal("tooltip.tile.biolab.0.name"),
            BW_Tooltip_Reference.ADDED_BY_BARTIMAEUSNEK_VIA_BARTWORKS.get() };
    }
}

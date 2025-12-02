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

package bartworks.common.tileentities.tiered;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

import bartworks.API.recipe.BartWorksRecipeMaps;
import bartworks.common.items.ItemLabModule;
import bartworks.common.items.ItemLabParts;
import bartworks.common.loaders.BioCultureLoader;
import bartworks.common.loaders.BioItemList;
import bartworks.common.loaders.FluidLoader;
import bartworks.util.BWUtil;
import bartworks.util.BioCulture;
import bartworks.util.BioDNA;
import bartworks.util.BioData;
import bartworks.util.BioPlasmid;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicMachine;
import gregtech.api.objects.XSTR;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.common.items.behaviors.BehaviourDataOrb;

public class MTEBioLab extends MTEBasicMachine {

    private static final int DNA_EXTRACTION_MODULE = 0;
    private static final int PCR_THERMOCYCLE_MODULE = 1;
    private static final int PLASMID_SYNTHESIS_MODULE = 2;
    private static final int TRANSFORMATION_MODULE = 3;
    private static final int CLONAL_CELLULAR_SYNTHESIS_MODULE = 4;
    private static final int INCUBATION_MODULE = 5;

    public MTEBioLab(int aID, String aName, String aNameRegional, int aTier) {
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

    public MTEBioLab(String aName, int aTier, int aAmperage, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aAmperage, aDescription, aTextures, 6, 2);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MTEBioLab(this.mName, this.mTier, this.mAmperage, this.mDescriptionArray, this.mTextures);
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

        if (this.getSpecialSlot() != null && this.getSpecialSlot()
            .getItem() instanceof ItemLabModule) {
            int damage = this.getSpecialSlot()
                .getItemDamage();
            switch (damage) {
                case DNA_EXTRACTION_MODULE:
                    if (GTUtility.isStackValid(this.mInventory[this.getInputSlot()])
                        && this.mInventory[this.getInputSlot()].getItem() instanceof ItemLabParts
                        && this.mInventory[this.getInputSlot()].getItemDamage() == 0
                        && this.mInventory[this.getInputSlot()].getTagCompound() != null
                        && // checks if it is a Culture
                        GTUtility.isStackValid(this.mInventory[this.getInputSlot() + 1])
                        && this.mInventory[this.getInputSlot() + 1].getItem() instanceof ItemLabParts
                        && this.mInventory[this.getInputSlot() + 1].getItemDamage() == 1
                        && this.mInventory[this.getInputSlot() + 1].getTagCompound() == null
                        && GTUtility.isStackValid(this.mInventory[this.getInputSlot() + 2])
                        && this.mInventory[this.getInputSlot() + 2].getItem() instanceof ItemLabParts
                        && this.mInventory[this.getInputSlot() + 2].getItemDamage() == 3
                        && GTUtility
                            .areStacksEqual(this.mInventory[this.getInputSlot() + 3], Materials.Ethanol.getCells(1))
                        && this.mFluid != null
                        && this.mFluid.isFluidEqual(GTModHandler.getDistilledWater(1_000))
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
                            return MTEBasicMachine.FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;

                        for (int i = 0; i < 4; i++) {
                            if (this.mInventory[this.getInputSlot() + i] != null)
                                this.mInventory[this.getInputSlot() + i].stackSize--;
                        }

                        this.mFluid.amount -= 1000;

                        if (cultureDNABioData.getChance() > new XSTR().nextInt(10000)) {
                            this.mOutputItems[0] = BioItemList
                                .getDNASampleFlask(BioDNA.convertDataToDNA(cultureDNABioData));
                        }
                        this.mOutputItems[1] = GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1L);
                        this.calculateOverclockedNess(
                            BWUtil.getMachineVoltageFromTier(rTier + cultureDNABioData.getTier()),
                            500);

                        return MTEBasicMachine.FOUND_AND_SUCCESSFULLY_USED_RECIPE;
                    }
                    break;
                case PCR_THERMOCYCLE_MODULE: {
                    if (GTUtility.isStackValid(this.mInventory[this.getInputSlot()])
                        && this.mInventory[this.getInputSlot()].getItem() instanceof ItemLabParts
                        && this.mInventory[this.getInputSlot()].getItemDamage() == 1
                        && this.mInventory[this.getInputSlot()].getTagCompound() != null
                        && // checks if it is a Culture
                        GTUtility.isStackValid(this.mInventory[this.getInputSlot() + 3])
                        && GTUtility
                            .areStacksEqual(this.mInventory[this.getInputSlot() + 3], ItemList.Tool_DataOrb.get(1L))
                        && GTUtility.isStackValid(this.mInventory[this.getInputSlot() + 1])
                        && GTUtility
                            .areStacksEqual(this.mInventory[this.getInputSlot() + 1], FluidLoader.BioLabFluidCells[0])
                        && GTUtility.isStackValid(this.mInventory[this.getInputSlot() + 2])
                        && GTUtility
                            .areStacksEqual(this.mInventory[this.getInputSlot() + 2], FluidLoader.BioLabFluidCells[3])
                        && this.mFluid != null
                        && this.mFluid.isFluidEqual(GTModHandler.getLiquidDNA(1_000))
                        && this.mFluid.amount >= 1000) {
                        NBTTagCompound DNABioDataTag = this.mInventory[this.getInputSlot()].getTagCompound();
                        if (DNABioDataTag == null) return super.checkRecipe(skipOC);
                        BioData cultureDNABioData = BioData.getBioDataFromName(DNABioDataTag.getString("Name"));
                        if (cultureDNABioData == null) return super.checkRecipe(skipOC);

                        if (this.mTier < 1 + rTier + cultureDNABioData.getTier())
                            return MTEBasicMachine.FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;

                        for (int i = 0; i < 4; i++) {
                            if (this.mInventory[this.getInputSlot() + i] != null)
                                this.mInventory[this.getInputSlot() + i].stackSize--;
                        }

                        this.mFluid.amount -= 1000;

                        ItemStack Outp = ItemList.Tool_DataOrb.get(1L);
                        BehaviourDataOrb.setDataTitle(Outp, "DNA Sample");
                        BehaviourDataOrb.setDataName(Outp, cultureDNABioData.getName());

                        if (cultureDNABioData.getChance() > new XSTR().nextInt(10000)) {
                            this.mOutputItems[0] = Outp;
                        } else this.mOutputItems[0] = ItemList.Tool_DataOrb.get(1L);
                        this.mOutputItems[1] = ItemList.Cell_Empty.get(2L);

                        this.calculateOverclockedNess(
                            BWUtil.getMachineVoltageFromTier(1 + rTier + cultureDNABioData.getTier()),
                            500);

                        return MTEBasicMachine.FOUND_AND_SUCCESSFULLY_USED_RECIPE;
                    }
                }
                    break;
                case PLASMID_SYNTHESIS_MODULE: {
                    ItemStack inp2 = ItemList.Tool_DataOrb.get(1L);
                    BehaviourDataOrb.setDataTitle(inp2, "DNA Sample");
                    BehaviourDataOrb.setDataName(inp2, BioCultureLoader.BIO_DATA_BETA_LACMATASE.getName());
                    if (GTUtility.isStackValid(this.mInventory[this.getInputSlot()])
                        && GTUtility
                            .areStacksEqual(FluidLoader.BioLabFluidCells[1], this.mInventory[this.getInputSlot()])
                        && // checks
                           // if
                           // it
                           // is
                           // a
                           // Culture
                        GTUtility.isStackValid(this.mInventory[this.getInputSlot() + 1])
                        && GTUtility
                            .areStacksEqual(this.mInventory[this.getInputSlot() + 1], BioItemList.getPlasmidCell(null))
                        && GTUtility.isStackValid(this.mInventory[this.getInputSlot() + 2])
                        && GTUtility.areStacksEqual(
                            this.mInventory[this.getInputSlot() + 2],
                            ItemList.Tool_DataOrb.get(1L),
                            true)
                        && "DNA Sample".equals(BehaviourDataOrb.getDataTitle(this.mInventory[this.getInputSlot() + 2]))
                        && !BehaviourDataOrb.getDataName(this.mInventory[this.getInputSlot() + 2])
                            .isEmpty()
                        && GTUtility.isStackValid(this.mInventory[this.getInputSlot() + 3])
                        && GTUtility.areStacksEqual(this.mInventory[this.getInputSlot() + 3], inp2)
                        && this.mFluid != null
                        && this.mFluid.isFluidEqual(GTModHandler.getLiquidDNA(1_000))
                        && this.mFluid.amount >= 1000) {
                        BioData cultureDNABioData = BioData
                            .getBioDataFromName(BehaviourDataOrb.getDataName(this.mInventory[this.getInputSlot() + 2]));
                        if (cultureDNABioData == null) return super.checkRecipe(skipOC);
                        if (this.mTier < 1 + rTier + cultureDNABioData.getTier())
                            return MTEBasicMachine.FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
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
                            BWUtil.getMachineVoltageFromTier(1 + rTier + cultureDNABioData.getTier()),
                            500);
                        return MTEBasicMachine.FOUND_AND_SUCCESSFULLY_USED_RECIPE;
                    }
                }
                    break;
                case TRANSFORMATION_MODULE: {
                    if (GTUtility.isStackValid(this.mInventory[this.getInputSlot()])
                        && GTUtility
                            .areStacksEqual(this.mInventory[this.getInputSlot()], BioItemList.getPetriDish(null), true)
                        && this.mInventory[this.getInputSlot()].getTagCompound() != null
                        && GTUtility.isStackValid(this.mInventory[this.getInputSlot() + 1])
                        && GTUtility.areStacksEqual(
                            this.mInventory[this.getInputSlot() + 1],
                            BioItemList.getPlasmidCell(null),
                            true)
                        && this.mInventory[this.getInputSlot() + 1].getTagCompound() != null
                        && GTUtility.isStackValid(this.mInventory[this.getInputSlot() + 2])
                        && GTUtility
                            .areStacksEqual(this.mInventory[this.getInputSlot() + 2], FluidLoader.BioLabFluidCells[2])
                        && this.mFluid != null
                        && this.mFluid.isFluidEqual(GTModHandler.getDistilledWater(1_000))
                        && this.mFluid.amount >= 1000) {
                        BioData cultureDNABioData = BioData
                            .getBioDataFromNBTTag(this.mInventory[this.getInputSlot() + 1].getTagCompound());
                        BioCulture bioCulture = BioCulture
                            .getBioCultureFromNBTTag(this.mInventory[this.getInputSlot()].getTagCompound());
                        if (cultureDNABioData == null || bioCulture == null) return super.checkRecipe(skipOC);
                        if (this.mTier < 1 + rTier + cultureDNABioData.getTier())
                            return MTEBasicMachine.FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
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
                            BWUtil.getMachineVoltageFromTier(1 + rTier + cultureDNABioData.getTier()),
                            500);
                        return MTEBasicMachine.FOUND_AND_SUCCESSFULLY_USED_RECIPE;
                    }
                }
                    break;
                case CLONAL_CELLULAR_SYNTHESIS_MODULE: {
                    ItemStack Outp = ItemList.Tool_DataOrb.get(1L);
                    BehaviourDataOrb.setDataTitle(Outp, "DNA Sample");

                    if (GTUtility.isStackValid(this.mInventory[this.getInputSlot()])
                        && GTUtility
                            .areStacksEqual(this.mInventory[this.getInputSlot()], BioItemList.getPetriDish(null))
                        && GTUtility.isStackValid(this.mInventory[this.getInputSlot() + 1])
                        && GTUtility.areStacksEqual(this.mInventory[this.getInputSlot() + 1], BioItemList.getOther(4))
                        && GTUtility.isStackValid(this.mInventory[this.getInputSlot() + 2])
                        && GTUtility.areStacksEqual(
                            this.mInventory[this.getInputSlot() + 2],
                            ItemList.Circuit_Chip_Stemcell.get(2L))
                        && GTUtility.isStackValid(this.mInventory[this.getInputSlot() + 3])
                        && GTUtility.areStacksEqual(
                            this.mInventory[this.getInputSlot() + 3],
                            ItemList.Tool_DataOrb.get(1L),
                            true)
                        && "DNA Sample".equals(BehaviourDataOrb.getDataTitle(this.mInventory[this.getInputSlot() + 3]))
                        && this.mFluid.isFluidEqual(GTModHandler.getLiquidDNA(1_000))
                        && this.mFluid.amount >= 8000) {

                        BioData cultureDNABioData = BioData
                            .getBioDataFromName(BehaviourDataOrb.getDataName(this.mInventory[this.getInputSlot() + 3]));
                        if (cultureDNABioData == null) return super.checkRecipe(skipOC);
                        if (this.mTier < 3 + rTier + cultureDNABioData.getTier())
                            return MTEBasicMachine.FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
                        for (int i = 0; i < 3; i++) {
                            if (this.mInventory[this.getInputSlot() + i] != null)
                                this.mInventory[this.getInputSlot() + i].stackSize--;
                        }
                        this.mFluid.amount -= 8000;
                        if (cultureDNABioData.getChance() > new XSTR().nextInt(10000)) {
                            BioCulture out = BioCulture.getBioCulture(BioDNA.convertDataToDNA(cultureDNABioData));
                            if (out == null) return MTEBasicMachine.DID_NOT_FIND_RECIPE;
                            out = out.setPlasmid(BioPlasmid.convertDataToPlasmid(cultureDNABioData));
                            this.mOutputItems[0] = BioItemList.getPetriDish(out);
                        }
                        this.calculateOverclockedNess(
                            BWUtil.getMachineVoltageFromTier(3 + rTier + cultureDNABioData.getTier()),
                            500);
                        return MTEBasicMachine.FOUND_AND_SUCCESSFULLY_USED_RECIPE;
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
            StatCollector.translateToLocal("tooltip.bw.author_bart_via_bw.name") };
    }
}

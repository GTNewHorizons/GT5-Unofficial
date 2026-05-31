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

import java.util.Arrays;
import java.util.function.Predicate;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import bartworks.API.recipe.BartWorksRecipeMaps;
import bartworks.common.items.ItemLabModule;
import bartworks.common.items.ItemLabParts;
import bartworks.common.loaders.BioCultureLoader;
import bartworks.common.loaders.BioItemList;
import bartworks.common.loaders.FluidLoader;
import bartworks.util.BWTooltipReference;
import bartworks.util.BioCulture;
import bartworks.util.BioDNA;
import bartworks.util.BioData;
import bartworks.util.BioPlasmid;
import gregtech.api.enums.GTValues;
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

    private final int DISH_ITEM_DAMAGE = 0;
    private final int FLASK_ITEM_DAMAGE = 1;
    private final int DETERGENT_ITEM_DAMAGE = 3;
    private final int PLASMACELL_MEMBRAME = 4;
    private final int FLUORESCENT_DNA = 0;
    private final int ENZYME_SOLUTION = 1;
    private final int PENICILLIN = 2;
    private final int POLYMERASE = 3;

    private final int R_TIER = 3;

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
                    .of(Textures.BlockIcons.customOptional("basicmachines/fluid_extractor/OVERLAY_SIDE_ACTIVE")),
                TextureFactory.builder()
                    .addIcon(
                        Textures.BlockIcons.customOptional("basicmachines/fluid_extractor/OVERLAY_SIDE_ACTIVE_GLOW"))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(Textures.BlockIcons.customOptional("basicmachines/fluid_extractor/OVERLAY_SIDE")),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.customOptional("basicmachines/fluid_extractor/OVERLAY_SIDE_GLOW"))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(Textures.BlockIcons.customOptional("basicmachines/microwave/OVERLAY_FRONT_ACTIVE")),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.customOptional("basicmachines/microwave/OVERLAY_FRONT_ACTIVE_GLOW"))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(Textures.BlockIcons.customOptional("basicmachines/microwave/OVERLAY_FRONT")),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.customOptional("basicmachines/microwave/OVERLAY_FRONT_GLOW"))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory
                    .of(Textures.BlockIcons.customOptional("basicmachines/chemical_reactor/OVERLAY_FRONT_ACTIVE")),
                TextureFactory.builder()
                    .addIcon(
                        Textures.BlockIcons.customOptional("basicmachines/chemical_reactor/OVERLAY_FRONT_ACTIVE_GLOW"))
                    .glow()
                    .build() /* this is topactive */),
            TextureFactory.of(
                TextureFactory.of(Textures.BlockIcons.customOptional("basicmachines/chemical_reactor/OVERLAY_FRONT")),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.customOptional("basicmachines/chemical_reactor/OVERLAY_FRONT_GLOW"))
                    .glow()
                    .build() /* this is top */),
            TextureFactory.of(
                TextureFactory.of(Textures.BlockIcons.customOptional("basicmachines/polarizer/OVERLAY_BOTTOM_ACTIVE")),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.customOptional("basicmachines/polarizer/OVERLAY_BOTTOM_ACTIVE_GLOW"))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(Textures.BlockIcons.customOptional("basicmachines/polarizer/OVERLAY_BOTTOM")),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.customOptional("basicmachines/polarizer/OVERLAY_BOTTOM_GLOW"))
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
        if (hasModuleInstalled()) {
            int moduleType = this.getSpecialSlot()
                .getItemDamage();
            return switch (moduleType) {
                case DNA_EXTRACTION_MODULE -> processDNAModuleLogic(skipOC);
                case PCR_THERMOCYCLE_MODULE -> processPCRModuleLogic(skipOC);
                case PLASMID_SYNTHESIS_MODULE -> processSynthesisModuleLogic(skipOC);
                case TRANSFORMATION_MODULE -> processTransformationModule(skipOC);
                case CLONAL_CELLULAR_SYNTHESIS_MODULE -> processClonalCellularModule(skipOC);
                default -> super.checkRecipe(skipOC);
            };
        }
        return super.checkRecipe(skipOC);
    }

    private boolean hasModuleInstalled() {
        return this.getSpecialSlot() != null && this.getSpecialSlot()
            .getItem() instanceof ItemLabModule;
    }

    private boolean hasFluid(FluidStack fluid, int atLeast) {
        return this.mFluid != null && this.mFluid.isFluidEqual(fluid) && this.mFluid.amount >= atLeast;
    }

    private boolean isValidLabPart(ItemStack stack) {
        return GTUtility.isStackValid(stack) && stack.getItem() instanceof ItemLabParts;
    }

    @Override
    public String[] getDescription() {
        return new String[] { StatCollector.translateToLocal("tooltip.tile.biolab.0.name"),
            BWTooltipReference.ADDED_BY_BARTIMAEUSNEK_VIA_BARTWORKS.get() };
    }

    /**
     * Checks if all predicates are matched and the index of the first matching input slot will be saved per predicate.
     * Items must not be in the same order as the predicates, but they must exist within the input slots of the
     * inventory of the machine.
     * <p>
     * Only non-NCs (Non Consumables) get consumed by the recipe processing.
     */
    private int processGenericModuleLogic(boolean skipOC, FluidStack fluid, int recipeFluidAmount,
        Predicate<ItemStack>[] predicates, boolean[] isNC) {
        int inputSlot = this.getInputSlot();
        int[] inputSlotIndices = new int[predicates.length];
        Arrays.fill(inputSlotIndices, -1);
        for (int i = 0; i < this.mInputSlotCount; i++) {
            if (this.mInventory[inputSlot + i] == null || !GTUtility.isStackValid(this.mInventory[i])) {
                continue;
            }

            for (int ip = 0; ip < predicates.length; ip++) {
                if (inputSlotIndices[ip] == -1 && predicates[ip].test(this.mInventory[inputSlot + i])) {
                    inputSlotIndices[ip] = inputSlot + i;
                    break;
                }
            }
        }
        boolean hasItems = Arrays.stream(inputSlotIndices)
            .allMatch(i -> i != -1);

        if (hasItems && hasFluid(fluid, recipeFluidAmount)) {
            // TODO: think about a good api interface here

            int effectiveRecipeTier = R_TIER;

            if (this.mTier < effectiveRecipeTier) return MTEBasicMachine.FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;

            for (int i = 0; i < inputSlotIndices.length; i++) {
                if (!isNC[i]) {
                    this.mInventory[inputSlotIndices[i]].stackSize--;
                }
            }
            this.mFluid.amount -= recipeFluidAmount;
            this.calculateOverclockedNess(GTUtility.safeInt(GTValues.V[effectiveRecipeTier]), 500);

            return MTEBasicMachine.FOUND_AND_SUCCESSFULLY_USED_RECIPE;
        }

        return MTEBasicMachine.DID_NOT_FIND_RECIPE;
    }

    private int processDNAModuleLogic(boolean skipOC) {
        int inputSlot = this.getInputSlot();
        int dishSlot = -1;
        int flaskSlot = -1;
        int detergentSlot = -1;
        int cellSlot = -1;
        int recipeFluidAmount = 1_000;
        for (int i = 0; i < this.mInputSlotCount; i++) {
            if (this.mInventory[inputSlot + i] == null) {
                continue;
            }
            if (isValidLabPart(this.mInventory[inputSlot + i])) {
                switch (this.mInventory[inputSlot + i].getItemDamage()) {
                    case DISH_ITEM_DAMAGE -> {
                        if (this.mInventory[inputSlot + i].getTagCompound() != null) dishSlot = inputSlot + i;
                    }
                    case FLASK_ITEM_DAMAGE -> {
                        if (this.mInventory[inputSlot + i].getTagCompound() == null) flaskSlot = inputSlot + i;
                    }
                    case DETERGENT_ITEM_DAMAGE -> detergentSlot = inputSlot + i;
                    default -> {}
                }
                continue;
            }
            if (GTUtility.areStacksEqual(this.mInventory[inputSlot + i], Materials.Ethanol.getCells(1))) {
                cellSlot = inputSlot + i;
            }
        }

        boolean hasItems = dishSlot != -1 && flaskSlot != -1 && detergentSlot != -1 && cellSlot != -1;

        if (hasItems && hasFluid(GTModHandler.getDistilledWater(1_000), recipeFluidAmount)) {
            NBTTagCompound DNABioDataTag = this.mInventory[dishSlot].getTagCompound()
                .getCompoundTag("DNA");
            if (DNABioDataTag == null) return super.checkRecipe(skipOC);
            BioData cultureDNABioData = BioData.getBioDataFromName(
                this.mInventory[dishSlot].getTagCompound()
                    .getCompoundTag("DNA")
                    .getString("Name"));
            if (cultureDNABioData == null) return super.checkRecipe(skipOC);

            int effectiveRecipeTier = R_TIER + cultureDNABioData.getTier();

            if (this.mTier < effectiveRecipeTier) return MTEBasicMachine.FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;

            for (int slot : new int[] { dishSlot, flaskSlot, detergentSlot, cellSlot }) {
                this.mInventory[slot].stackSize--;
            }

            this.mFluid.amount -= recipeFluidAmount;

            if (cultureDNABioData.getChance() > new XSTR().nextInt(10000)) {
                this.mOutputItems[0] = BioItemList.getDNASampleFlask(BioDNA.convertDataToDNA(cultureDNABioData));
            }
            this.mOutputItems[1] = GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1L);
            this.calculateOverclockedNess(GTUtility.safeInt(GTValues.V[effectiveRecipeTier]), 500);

            return MTEBasicMachine.FOUND_AND_SUCCESSFULLY_USED_RECIPE;
        }
        return MTEBasicMachine.DID_NOT_FIND_RECIPE;
    }

    private int processPCRModuleLogic(boolean skipOC) {
        int inputSlot = this.getInputSlot();
        int orbSlot1 = -1;
        int flaskSlot = -1;
        int dnaCellSlot = -1;
        int polymeraseSlot = -1;
        int recipeFluidAmount = 1_000;
        for (int i = 0; i < this.mInputSlotCount; i++) {
            if (this.mInventory[inputSlot + i] == null) {
                continue;
            }
            if (isValidLabPart(this.mInventory[inputSlot + i])) {
                if (this.mInventory[inputSlot + i].getItemDamage() == FLASK_ITEM_DAMAGE) {
                    if (this.mInventory[inputSlot + i].getTagCompound() != null) flaskSlot = inputSlot + i;
                } else {
                    continue;
                }
            }
            if (GTUtility.areStacksEqual(this.mInventory[inputSlot + i], ItemList.Tool_DataOrb.get(1L))) {
                orbSlot1 = inputSlot + i;
                continue;
            }
            if (GTUtility
                .areStacksEqual(this.mInventory[inputSlot + i], FluidLoader.BioLabFluidCells[FLUORESCENT_DNA])) {
                dnaCellSlot = inputSlot + i;
                continue;
            }
            if (GTUtility.areStacksEqual(this.mInventory[inputSlot + i], FluidLoader.BioLabFluidCells[POLYMERASE])) {
                polymeraseSlot = inputSlot + i;
            }
        }

        boolean hasItems = orbSlot1 != -1 && flaskSlot != -1 && dnaCellSlot != -1 && polymeraseSlot != -1;

        if (hasItems && hasFluid(GTModHandler.getLiquidDNA(1_000), recipeFluidAmount)) {
            NBTTagCompound DNABioDataTag = this.mInventory[flaskSlot].getTagCompound();
            if (DNABioDataTag == null) return super.checkRecipe(skipOC);
            BioData cultureDNABioData = BioData.getBioDataFromName(DNABioDataTag.getString("Name"));
            if (cultureDNABioData == null) return super.checkRecipe(skipOC);

            int effectiveRecipeTier = 1 + R_TIER + cultureDNABioData.getTier();

            if (this.mTier < effectiveRecipeTier) return MTEBasicMachine.FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;

            for (int slot : new int[] { orbSlot1, flaskSlot, dnaCellSlot, polymeraseSlot }) {
                this.mInventory[slot].stackSize--;
            }

            this.mFluid.amount -= recipeFluidAmount;

            ItemStack Outp = ItemList.Tool_DataOrb.get(1L);
            BehaviourDataOrb.setDataTitle(Outp, "DNA Sample");
            BehaviourDataOrb.setDataName(Outp, cultureDNABioData.getName());

            if (cultureDNABioData.getChance() > new XSTR().nextInt(10000)) {
                this.mOutputItems[0] = Outp;
            } else this.mOutputItems[0] = ItemList.Tool_DataOrb.get(1L);
            this.mOutputItems[1] = ItemList.Cell_Empty.get(2L);

            this.calculateOverclockedNess(GTUtility.safeInt(GTValues.V[effectiveRecipeTier]), 500);

            return MTEBasicMachine.FOUND_AND_SUCCESSFULLY_USED_RECIPE;
        }
        return MTEBasicMachine.DID_NOT_FIND_RECIPE;
    }

    private int processSynthesisModuleLogic(boolean skipOC) {
        ItemStack inp2 = ItemList.Tool_DataOrb.get(1L);
        BehaviourDataOrb.setDataTitle(inp2, "DNA Sample");
        BehaviourDataOrb.setDataName(inp2, BioCultureLoader.BIO_DATA_BETA_LACMATASE.getName());

        int inputSlot = this.getInputSlot();
        int orbSlot1 = -1;
        int orbSlot2 = -1;
        int plasmidSlot = -1;
        int enzymeSlot = -1;
        int recipeFluidAmount = 1_000;
        for (int i = 0; i < this.mInputSlotCount; i++) {
            if (this.mInventory[inputSlot + i] == null || !GTUtility.isStackValid(this.mInventory[i])) {
                continue;
            }

            if (GTUtility.areStacksEqual(this.mInventory[i], FluidLoader.BioLabFluidCells[ENZYME_SOLUTION])) {
                enzymeSlot = inputSlot + i;
                continue;
            }
            if (GTUtility.areStacksEqual(this.mInventory[i], BioItemList.getPlasmidCell(null))) {
                plasmidSlot = inputSlot + i;
                continue;
            }
            if (GTUtility.areStacksEqual(this.mInventory[i], ItemList.Tool_DataOrb.get(1L), true)
                && "DNA Sample".equals(BehaviourDataOrb.getDataTitle(this.mInventory[i]))
                && !BehaviourDataOrb.getDataName(this.mInventory[i])
                    .isEmpty()) {
                orbSlot1 = inputSlot + i;
                continue;
            }
            if (GTUtility.areStacksEqual(this.mInventory[i], inp2)) {
                orbSlot2 = inputSlot + i;
            }
        }

        boolean hasItems = orbSlot1 != -1 && plasmidSlot != -1 && orbSlot2 != -1 && enzymeSlot != -1;

        if (hasItems && hasFluid(GTModHandler.getLiquidDNA(1_000), recipeFluidAmount)) {
            BioData cultureDNABioData = BioData
                .getBioDataFromName(BehaviourDataOrb.getDataName(this.mInventory[this.getInputSlot() + 2]));
            if (cultureDNABioData == null) return super.checkRecipe(skipOC);

            int effectiveRecipeTier = 1 + R_TIER + cultureDNABioData.getTier();

            if (this.mTier < effectiveRecipeTier) return MTEBasicMachine.FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;

            for (int slot : new int[] { plasmidSlot, enzymeSlot }) {
                this.mInventory[slot].stackSize--;
            }

            this.mFluid.amount -= recipeFluidAmount;

            if (cultureDNABioData.getChance() > new XSTR().nextInt(10000)) {
                this.mOutputItems[0] = BioItemList.getPlasmidCell(BioPlasmid.convertDataToPlasmid(cultureDNABioData));
            }

            this.mOutputItems[1] = ItemList.Cell_Empty.get(1L);
            this.calculateOverclockedNess(GTUtility.safeInt(GTValues.V[effectiveRecipeTier]), 500);
            return MTEBasicMachine.FOUND_AND_SUCCESSFULLY_USED_RECIPE;
        }
        return MTEBasicMachine.DID_NOT_FIND_RECIPE;
    }

    private int processTransformationModule(boolean skipOC) {
        int inputSlot = this.getInputSlot();
        int cellSlot = -1;
        int plasmidSlot = -1;
        int dishSlot = -1;
        int recipeFluidAmount = 1_000;
        for (int i = 0; i < this.mInputSlotCount; i++) {
            if (this.mInventory[inputSlot + i] == null || !GTUtility.isStackValid(this.mInventory[i])) {
                continue;
            }

            if (GTUtility.areStacksEqual(this.mInventory[i], BioItemList.getPetriDish(null), true)
                && this.mInventory[i].getTagCompound() != null) {
                dishSlot = inputSlot + i;
                continue;
            }
            if (GTUtility.areStacksEqual(this.mInventory[i], BioItemList.getPlasmidCell(null), true)
                && this.mInventory[this.getInputSlot() + 1].getTagCompound() != null) {
                plasmidSlot = inputSlot + i;
                continue;
            }
            if (GTUtility.areStacksEqual(this.mInventory[i], FluidLoader.BioLabFluidCells[PENICILLIN])) {
                cellSlot = inputSlot + i;
            }
        }

        boolean hasItems = cellSlot != -1 && plasmidSlot != -1 && dishSlot != -1;

        if (hasItems && hasFluid(GTModHandler.getDistilledWater(1_000), recipeFluidAmount)) {
            BioData cultureDNABioData = BioData.getBioDataFromNBTTag(this.mInventory[plasmidSlot].getTagCompound());
            BioCulture bioCulture = BioCulture.getBioCultureFromNBTTag(this.mInventory[dishSlot].getTagCompound());
            if (cultureDNABioData == null || bioCulture == null) return super.checkRecipe(skipOC);

            int effectiveRecipeTier = 1 + R_TIER + cultureDNABioData.getTier();

            if (this.mTier < effectiveRecipeTier) return MTEBasicMachine.FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;

            for (int slot : new int[] { cellSlot, plasmidSlot, dishSlot }) {
                this.mInventory[slot].stackSize--;
            }

            this.mFluid.amount -= recipeFluidAmount;
            bioCulture = bioCulture.setPlasmid(BioPlasmid.convertDataToPlasmid(cultureDNABioData));
            if (cultureDNABioData.getChance() > new XSTR().nextInt(10000)) {
                this.mOutputItems[0] = BioItemList.getPetriDish(bioCulture);
            }
            this.mOutputItems[1] = ItemList.Cell_Empty.get(1L);
            this.calculateOverclockedNess(GTUtility.safeInt(GTValues.V[effectiveRecipeTier]), 500);
            return MTEBasicMachine.FOUND_AND_SUCCESSFULLY_USED_RECIPE;
        }
        return MTEBasicMachine.DID_NOT_FIND_RECIPE;
    }

    private int processClonalCellularModule(boolean skipOC) {
        int inputSlot = this.getInputSlot();
        int dishSlot = -1;
        int stemcellSlot = -1;
        int membraneSlot = -1;
        int orbSlot = -1;
        int recipeFluidAmount = 8_000;
        for (int i = 0; i < this.mInputSlotCount; i++) {
            if (this.mInventory[inputSlot + i] == null || !GTUtility.isStackValid(this.mInventory[i])) {
                continue;
            }

            if (GTUtility.areStacksEqual(this.mInventory[i], BioItemList.getPetriDish(null))) {
                dishSlot = inputSlot + i;
                continue;
            }
            if (GTUtility.areStacksEqual(this.mInventory[i], BioItemList.getOther(4))) {
                membraneSlot = inputSlot + i;
                continue;
            }
            if (GTUtility.areStacksEqual(this.mInventory[i], ItemList.Circuit_Chip_Stemcell.get(2L))) {
                stemcellSlot = inputSlot + i;
                continue;
            }
            if (GTUtility.areStacksEqual(this.mInventory[i], ItemList.Tool_DataOrb.get(1L), true)
                && "DNA Sample".equals(BehaviourDataOrb.getDataTitle(this.mInventory[i]))) {
                orbSlot = inputSlot + i;
            }
        }

        boolean hasItems = stemcellSlot != -1 && membraneSlot != -1 && dishSlot != -1 && orbSlot != -1;

        if (hasItems && hasFluid(GTModHandler.getLiquidDNA(1_000), recipeFluidAmount)) {

            BioData cultureDNABioData = BioData
                .getBioDataFromName(BehaviourDataOrb.getDataName(this.mInventory[orbSlot]));
            if (cultureDNABioData == null) return super.checkRecipe(skipOC);

            int effectiveRecipeTier = 3 + R_TIER + cultureDNABioData.getTier();

            if (this.mTier < effectiveRecipeTier) return MTEBasicMachine.FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;

            for (int slot : new int[] { stemcellSlot, membraneSlot, dishSlot }) {
                this.mInventory[slot].stackSize--;
            }

            this.mFluid.amount -= recipeFluidAmount;

            if (cultureDNABioData.getChance() > new XSTR().nextInt(10000)) {
                BioCulture out = BioCulture.getBioCulture(BioDNA.convertDataToDNA(cultureDNABioData));
                if (out == null) return MTEBasicMachine.DID_NOT_FIND_RECIPE;
                out = out.setPlasmid(BioPlasmid.convertDataToPlasmid(cultureDNABioData));
                this.mOutputItems[0] = BioItemList.getPetriDish(out);
            }
            this.calculateOverclockedNess(GTUtility.safeInt(GTValues.V[effectiveRecipeTier]), 500);
            return MTEBasicMachine.FOUND_AND_SUCCESSFULLY_USED_RECIPE;
        }
        return MTEBasicMachine.DID_NOT_FIND_RECIPE;
    }
}

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

import static gregtech.api.objects.XSTR.XSTR_INSTANCE;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import bartworks.API.enums.BioCultureEnum;
import bartworks.API.enums.BioDataEnum;
import bartworks.API.recipe.BartWorksRecipeMaps;
import bartworks.common.items.ItemLabModule;
import bartworks.util.BWTooltipReference;
import bartworks.util.BioCulture;
import bartworks.util.BioData;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicMachine;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gregtech.common.items.MetaGeneratedItem98;
import gregtech.common.items.behaviors.BehaviourDataOrb;

public class MTEBioLab extends MTEBasicMachine {

    private static final int DNA_EXTRACTION_MODULE = 0;
    private static final int PCR_THERMOCYCLE_MODULE = 1;
    private static final int PLASMID_SYNTHESIS_MODULE = 2;
    private static final int TRANSFORMATION_MODULE = 3;
    private static final int CLONAL_CELLULAR_SYNTHESIS_MODULE = 4;
    private static final int INCUBATION_MODULE = 5;

    private static final boolean[] IS_NC_DNA = new boolean[] { false, false, false, false };
    private static final boolean[] IS_NC_PCR = new boolean[] { false, false, false, false };
    private static final boolean[] IS_NC_SYNTHESIS = new boolean[] { true, true, false, false };
    private static final boolean[] IS_NC_CLONAL = new boolean[] { true, false, false, false };

    private List<Predicate<ItemStack>> predicatesDNA;
    private List<Predicate<ItemStack>> predicatesPCR;
    private List<Predicate<ItemStack>> predicatesSynthesis;
    private List<Predicate<ItemStack>> predicatesClonal;
    BioLabRecipeOutputSupplier outputSupplierDNA;
    BioLabRecipeOutputSupplier outputSupplierPCR;
    BioLabRecipeOutputSupplier outputSupplierSynthesis;
    BioLabRecipeOutputSupplier outputSupplierClonal;

    public MTEBioLab(int aID, String aName, String aNameRegional, int aTier) {
        // spotless:off
        ITexture[] overlays = new ITexture[]{
            TextureFactory.of(
                TextureFactory.of(Textures.BlockIcons.customOptional("basicmachines/fluid_extractor/OVERLAY_SIDE_ACTIVE")),
                TextureFactory.builder().addIcon(Textures.BlockIcons.customOptional("basicmachines/fluid_extractor/OVERLAY_SIDE_ACTIVE_GLOW")).glow().build()
            ),
            TextureFactory.of(
                TextureFactory.of(Textures.BlockIcons.customOptional("basicmachines/fluid_extractor/OVERLAY_SIDE")),
                TextureFactory.builder().addIcon(Textures.BlockIcons.customOptional("basicmachines/fluid_extractor/OVERLAY_SIDE_GLOW")).glow().build()
            ),
            TextureFactory.of(
                TextureFactory.of(Textures.BlockIcons.customOptional("basicmachines/microwave/OVERLAY_FRONT_ACTIVE")),
                TextureFactory.builder().addIcon(Textures.BlockIcons.customOptional("basicmachines/microwave/OVERLAY_FRONT_ACTIVE_GLOW")).glow().build()
            ),
            TextureFactory.of(
                TextureFactory.of(Textures.BlockIcons.customOptional("basicmachines/microwave/OVERLAY_FRONT")),
                TextureFactory.builder().addIcon(Textures.BlockIcons.customOptional("basicmachines/microwave/OVERLAY_FRONT_GLOW")).glow().build()
            ),
            TextureFactory.of(
                TextureFactory.of(Textures.BlockIcons.customOptional("basicmachines/chemical_reactor/OVERLAY_FRONT_ACTIVE")),
                TextureFactory.builder().addIcon(Textures.BlockIcons.customOptional("basicmachines/chemical_reactor/OVERLAY_FRONT_ACTIVE_GLOW")).glow().build() /* this is topactive */
            ),
            TextureFactory.of(
                TextureFactory.of(Textures.BlockIcons.customOptional("basicmachines/chemical_reactor/OVERLAY_FRONT")),
                TextureFactory.builder().addIcon(Textures.BlockIcons.customOptional("basicmachines/chemical_reactor/OVERLAY_FRONT_GLOW")).glow().build() /* this is top */
            ),
            TextureFactory.of(
                TextureFactory.of(Textures.BlockIcons.customOptional("basicmachines/polarizer/OVERLAY_BOTTOM_ACTIVE")),
                TextureFactory.builder().addIcon(Textures.BlockIcons.customOptional("basicmachines/polarizer/OVERLAY_BOTTOM_ACTIVE_GLOW")).glow().build()
            ),
            TextureFactory.of(
                TextureFactory.of(Textures.BlockIcons.customOptional("basicmachines/polarizer/OVERLAY_BOTTOM")),
                TextureFactory.builder().addIcon(Textures.BlockIcons.customOptional("basicmachines/polarizer/OVERLAY_BOTTOM_GLOW")).glow().build()
            )
        };
        super(aID, aName, aNameRegional, aTier, 1, (String) null, 6, 2, overlays);
        // spotless:on
        initPredicatesAndOutputSuppliers();
    }

    public MTEBioLab(String aName, int aTier, int aAmperage, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aAmperage, aDescription, aTextures, 6, 2);
        initPredicatesAndOutputSuppliers();
    }

    private void initPredicatesAndOutputSuppliers() {
        predicatesDNA = List.of(
            this::isValidCulture,
            (stack) -> isDNAFlask(stack, false),
            this::isDetergentPowder,
            this::isEthanolCell);

        outputSupplierDNA = new BioLabRecipeOutputSupplier(
            (BioDataEnum::getDNASampleFlask),
            (() -> ItemList.Cell_Empty.get(1)));

        predicatesPCR = List.of(
            (stack -> isDNAFlask(stack, true)),
            this::isEmptyDataOrb,
            this::isFluorescentDNACell,
            this::isPolymeraseCell);

        outputSupplierPCR = new BioLabRecipeOutputSupplier((bioData -> {
            ItemStack DNAOrb = ItemList.Tool_DataOrb.get(1);
            BehaviourDataOrb.setDataTitle(DNAOrb, "DNA Sample");
            BehaviourDataOrb.setDataName(DNAOrb, bioData.getName());
            return DNAOrb;
        }), (() -> ItemList.Cell_Empty.get(2)));

        // todo: check if this is called too early, and postpone the registration in case it is
        final ItemStack inp2 = ItemList.Tool_DataOrb.get(1);
        BehaviourDataOrb.setDataTitle(inp2, "DNA Sample");
        BehaviourDataOrb.setDataName(inp2, BioDataEnum.BetaLactamase.name);

        predicatesSynthesis = List.of(
            this::isValidDNASampleOrb,
            (stack -> GTUtility.areStacksEqual(stack, inp2)),
            this::isEnzymeSolutionCell,
            (stack -> isPlasmidFlask(stack, false)));

        outputSupplierSynthesis = new BioLabRecipeOutputSupplier(
            BioDataEnum::getPlasmidCell,
            (() -> ItemList.Cell_Empty.get(1)));

        predicatesClonal = List
            .of(this::isValidDNASampleOrb, this::isEmptyPetriDish, this::isPlasmaMembrane, this::areTwoStemCells);

        outputSupplierClonal = new BioLabRecipeOutputSupplier((bioData -> {
            BioCulture out = BioCulture.getBioCulture(bioData);
            if (out == null) return null;
            return BioCultureEnum.getPetriDish(out.setPlasmid(bioData));
        }), BioLabRecipeOutputSupplier.EMPTY);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MTEBioLab(this.mName, this.mTier, this.mAmperage, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public final RecipeMap<?> getRecipeMap() {
        return BartWorksRecipeMaps.bioLabRecipes;
    }

    @Override
    public final int getCapacity() {
        return this.mTier * 1000;
    }

    private boolean isValidCulture(ItemStack stack) {
        // hyp: if it has a NBT then the Name field exist and is correct
        return GTUtility.areStacksEqual(stack, BioCultureEnum.NullBioCulture.culture.get(1), true)
            && stack.getTagCompound() != null;
    }

    private boolean isDNAFlask(ItemStack stack, boolean isFilled) {
        return GTUtility.areStacksEqual(stack, ItemList.EmptyDNAFlask.get(1), isFilled)
            && isFilled == (stack.getTagCompound() != null);
    }

    private boolean isPlasmidFlask(ItemStack stack, boolean isFilled) {
        return GTUtility.areStacksEqual(stack, ItemList.EmptyPlasmid.get(1), isFilled)
            && isFilled == (stack.getTagCompound() != null);
    }

    private boolean isEmptyDataOrb(ItemStack stack) {
        return GTUtility.areStacksEqual(stack, ItemList.Tool_DataOrb.get(1));
    }

    private boolean isValidDNASampleOrb(ItemStack stack) {
        return GTUtility.areStacksEqual(stack, ItemList.Tool_DataOrb.get(1L), true)
            && "DNA Sample".equals(BehaviourDataOrb.getDataTitle(stack))
            && !BehaviourDataOrb.getDataName(stack)
                .isEmpty();
    }

    private boolean isDetergentPowder(ItemStack stack) {
        return GTUtility.areStacksEqual(stack, ItemList.DetergentPowder.get(1), false);
    }

    private boolean isEmptyPetriDish(ItemStack stack) {
        return GTUtility.areStacksEqual(stack, ItemList.EmptyPetriDish.get(1));
    }

    private boolean isEthanolCell(ItemStack stack) {
        return GTUtility.areStacksEqual(stack, Materials.Ethanol.getCells(1));
    }

    private boolean isFluorescentDNACell(ItemStack stack) {
        return GTUtility.areStacksEqual(stack, MetaGeneratedItem98.FluidCell.FLUORESCENT_DNA.get());
    }

    private boolean isEnzymeSolutionCell(ItemStack stack) {
        return GTUtility.areStacksEqual(stack, MetaGeneratedItem98.FluidCell.ENZYME_SOLUTION.get());
    }

    private boolean isPenicillinCell(ItemStack stack) {
        return GTUtility.areStacksEqual(stack, MetaGeneratedItem98.FluidCell.PENICILLIN.get());
    }

    private boolean isPolymeraseCell(ItemStack stack) {
        return GTUtility.areStacksEqual(stack, MetaGeneratedItem98.FluidCell.POLYMERASE.get());
    }

    private boolean isPlasmaMembrane(ItemStack stack) {
        return GTUtility.areStacksEqual(stack, ItemList.PlasmaMembrane.get(1));
    }

    private boolean areTwoStemCells(ItemStack stack) {
        return GTUtility.areStacksEqual(stack, ItemList.Circuit_Chip_Stemcell.get(2L));
    }

    @Override
    public int checkRecipe(boolean skipOC) {
        if (hasModuleInstalled()) {
            int moduleType = this.getSpecialSlot()
                .getItemDamage();
            return switch (moduleType) {
                case DNA_EXTRACTION_MODULE -> processDNAModuleLogic();
                case PCR_THERMOCYCLE_MODULE -> processPCRModuleLogic();
                case PLASMID_SYNTHESIS_MODULE -> processSynthesisModuleLogic();
                case TRANSFORMATION_MODULE -> processTransformationModule(skipOC);
                case CLONAL_CELLULAR_SYNTHESIS_MODULE -> processClonalCellularModule();
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

    @Override
    public String[] getDescription() {
        return new String[] { StatCollector.translateToLocal("tooltip.tile.biolab.0.name"),
            BWTooltipReference.ADDED_BY_BARTIMAEUSNEK_VIA_BARTWORKS.get() };
    }

    /**
     * Safety checks like {@link GTUtility#isStackValid(ItemStack)} will be performed and may not be part of the
     * predicates.
     * Checks if all predicates are matched and the index of the first matching input slot will be saved per predicate.
     * Items must not be in the same order as the predicates, but they must exist within the input slots of the
     * inventory of the machine.
     * <p>
     * Only non-NCs (Non Consumables) get consumed by the recipe processing.
     *
     * @param itemWithBioData from 0 up to {@code predicate#size()}, {@link #getInputSlot()} offset will be applied.
     */
    private int processGenericModuleLogic(final FluidStack fluid, final int recipeFluidAmount,
        final List<Predicate<ItemStack>> predicates, final boolean[] isNC, final int recipeTierOffset,
        final int itemWithBioData, final Function<ItemStack, BioData> bioDataGetter,
        final BioLabRecipeOutputSupplier blOutputSupplier) {
        final int inputSlot = this.getInputSlot();
        int[] inputSlotIndices = new int[predicates.size()];
        Arrays.fill(inputSlotIndices, -1);

        if (itemWithBioData < 0 || itemWithBioData >= predicates.size()) return MTEBasicMachine.DID_NOT_FIND_RECIPE;

        for (int i = 0; i < this.mInputSlotCount; i++) {
            if (this.mInventory[inputSlot + i] == null || !GTUtility.isStackValid(this.mInventory[inputSlot + i])) {
                continue;
            }

            for (int ip = 0; ip < predicates.size(); ip++) {
                if (inputSlotIndices[ip] == -1 && predicates.get(ip)
                    .test(this.mInventory[inputSlot + i])) {
                    inputSlotIndices[ip] = inputSlot + i;
                    break;
                }
            }
        }
        boolean hasItems = Arrays.stream(inputSlotIndices)
            .allMatch(i -> i != -1);

        if (hasItems && hasFluid(fluid, recipeFluidAmount)) {
            BioData cultureDNABioData = bioDataGetter.apply(this.mInventory[inputSlotIndices[itemWithBioData]]);
            int effectiveRecipeTier = recipeTierOffset + cultureDNABioData.getTier();

            if (this.mTier < effectiveRecipeTier) return MTEBasicMachine.FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;

            var temp = blOutputSupplier.chanced()
                .apply(cultureDNABioData);
            if (temp == null) return MTEBasicMachine.DID_NOT_FIND_RECIPE;
            // 100_00 because it's % and not 10k
            if (cultureDNABioData.getChance() > XSTR_INSTANCE.nextInt(100_00)) {
                this.mOutputItems[0] = temp;
            }
            this.mOutputItems[1] = blOutputSupplier.nonChanced()
                .get();

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

    private int processDNAModuleLogic() {
        return processGenericModuleLogic(
            GTModHandler.getDistilledWater(1_000),
            1_000,
            predicatesDNA,
            IS_NC_DNA,
            0,
            0,
            (stack -> BioCulture.getBioCulture(
                stack.getTagCompound()
                    .getString("Name"))
                .getdDNA()),
            outputSupplierDNA);
    }

    private int processPCRModuleLogic() {
        return processGenericModuleLogic(
            GTModHandler.getLiquidDNA(1_000),
            1_000,
            predicatesPCR,
            IS_NC_PCR,
            1,
            0,
            (stack -> BioDataEnum.LOOKUPS_BY_NAME.get(
                stack.getTagCompound()
                    .getString("Name"))
                .getBioData()),
            outputSupplierPCR);
    }

    private int processSynthesisModuleLogic() {
        return processGenericModuleLogic(
            GTModHandler.getLiquidDNA(1_000),
            1_000,
            predicatesSynthesis,
            IS_NC_SYNTHESIS,
            1,
            0,
            (stack -> BioDataEnum.LOOKUPS_BY_NAME.get(BehaviourDataOrb.getDataName(stack))
                .getBioData()),
            outputSupplierSynthesis);
    }

    private int processTransformationModule(boolean skipOC) {
        int inputSlot = this.getInputSlot();
        int cellSlot = -1;
        int plasmidSlot = -1;
        int cultureSlot = -1;
        int recipeFluidAmount = 1_000;
        for (int i = 0; i < this.mInputSlotCount; i++) {
            if (this.mInventory[inputSlot + i] == null || !GTUtility.isStackValid(this.mInventory[inputSlot + i])) {
                continue;
            }
            if (cultureSlot == -1 && isValidCulture(this.mInventory[inputSlot + i])) {
                cultureSlot = inputSlot + i;
                continue;
            }
            if (plasmidSlot == -1 && isPlasmidFlask(this.mInventory[inputSlot + i], true)) {
                plasmidSlot = inputSlot + i;
                continue;
            }
            if (cellSlot == -1 && isPenicillinCell(this.mInventory[inputSlot + i])) {
                cellSlot = inputSlot + i;
            }
        }

        boolean hasItems = cellSlot != -1 && plasmidSlot != -1 && cultureSlot != -1;

        if (hasItems && hasFluid(GTModHandler.getDistilledWater(1_000), recipeFluidAmount)) {
            BioData cultureDNABioData = BioData.getBioDataFromNBTTag(this.mInventory[plasmidSlot].getTagCompound());
            BioCulture bioCulture = BioCulture.getBioCultureFromNBTTag(this.mInventory[cultureSlot].getTagCompound());
            if (cultureDNABioData == null || bioCulture == null) return super.checkRecipe(skipOC);

            int effectiveRecipeTier = 1 + cultureDNABioData.getTier();

            if (this.mTier < effectiveRecipeTier) return MTEBasicMachine.FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;

            for (int slot : new int[] { cellSlot, plasmidSlot, cultureSlot }) {
                this.mInventory[slot].stackSize--;
            }

            this.mFluid.amount -= recipeFluidAmount;
            bioCulture = bioCulture.setPlasmid(cultureDNABioData);
            if (cultureDNABioData.getChance() > XSTR_INSTANCE.nextInt(100_00)) {
                this.mOutputItems[0] = BioCultureEnum.getPetriDish(bioCulture);
            }
            this.mOutputItems[1] = ItemList.Cell_Empty.get(1);
            this.calculateOverclockedNess(GTUtility.safeInt(GTValues.V[effectiveRecipeTier]), 500);
            return MTEBasicMachine.FOUND_AND_SUCCESSFULLY_USED_RECIPE;
        }
        return MTEBasicMachine.DID_NOT_FIND_RECIPE;
    }

    private int processClonalCellularModule() {
        return processGenericModuleLogic(
            GTModHandler.getLiquidDNA(1_000),
            8_000,
            predicatesClonal,
            IS_NC_CLONAL,
            3,
            0,
            (stack -> BioDataEnum.LOOKUPS_BY_NAME.get(BehaviourDataOrb.getDataName(stack))
                .getBioData()),
            outputSupplierClonal);
    }

    private record BioLabRecipeOutputSupplier(Function<BioData, ItemStack> chanced, Supplier<ItemStack> nonChanced) {

        private static final Supplier<ItemStack> EMPTY = () -> null;
    }
}

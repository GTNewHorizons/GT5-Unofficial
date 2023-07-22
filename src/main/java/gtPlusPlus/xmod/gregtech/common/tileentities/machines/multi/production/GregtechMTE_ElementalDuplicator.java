package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.Muffler;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Element;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TAE;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IHasWorldObjectAndCoords;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.recipe.check.FindRecipeResult;
import gregtech.api.util.GTPP_Recipe.GTPP_Recipe_Map;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.GT_Utility;
import gregtech.common.items.behaviors.Behaviour_DataOrb;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_ElementalDataOrbHolder;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GregtechMTE_ElementalDuplicator extends GregtechMeta_MultiBlockBase<GregtechMTE_ElementalDuplicator> {

    private final ArrayList<GT_MetaTileEntity_Hatch_ElementalDataOrbHolder> mReplicatorDataOrbHatches = new ArrayList<GT_MetaTileEntity_Hatch_ElementalDataOrbHolder>();
    private static final int CASING_TEXTURE_ID = TAE.getIndexFromPage(0, 3);
    private int mCasing = 0;

    public GregtechMTE_ElementalDuplicator(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GregtechMTE_ElementalDuplicator(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new GregtechMTE_ElementalDuplicator(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Replicator";
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {

        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(getMachineType()).addInfo("Produces Elemental Material from UU Matter")
                .addInfo("Speed: +100% | EU Usage: 100% | Parallel: 8 * Tier").addInfo("Maximum 1x of each bus/hatch.")
                .addInfo("Requires circuit 1-16 in your Data Orb Repository")
                .addInfo("depending on what Data Orb you want to prioritize")
                .addPollutionAmount(getPollutionPerSecond(null)).addSeparator().beginStructureBlock(9, 6, 9, true)
                .addController("Top Center").addCasingInfoMin("Elemental Confinement Shell", 138, false)
                .addCasingInfoMin("Matter Fabricator Casing", 24, false)
                .addCasingInfoMin("Particle Containment Casing", 24, false)
                .addCasingInfoMin("Matter Generation Coil", 24, false)
                .addCasingInfoMin("High Voltage Current Capacitor", 20, false)
                .addCasingInfoMin("Resonance Chamber III", 24, false).addCasingInfoMin("Modulator III", 16, false)
                .addOtherStructurePart("Data Orb Repository", "1x", 1).addInputHatch("Any 1 dot hint", 1)
                .addOutputBus("Any 1 dot hint", 1).addOutputHatch("Any 1 dot hint", 1)
                .addEnergyHatch("Any 1 dot hint", 1).addMaintenanceHatch("Any 1 dot hint", 1)
                .addMufflerHatch("Any 1 dot hint", 1).toolTipFinisher(CORE.GT_Tooltip_Builder.get());
        return tt;
    }

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static IStructureDefinition<GregtechMTE_ElementalDuplicator> STRUCTURE_DEFINITION = null;

    @Override
    public IStructureDefinition<GregtechMTE_ElementalDuplicator> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GregtechMTE_ElementalDuplicator>builder()

                    // h = Hatch
                    // c = Casing

                    // a = MF Casing 1
                    // b = Matter Gen Coil

                    // d = Current Capacitor
                    // e = Particle

                    // f = Resonance III
                    // g = Modulator III

                    .addShape(
                            STRUCTURE_PIECE_MAIN,
                            (new String[][] {
                                    { "   ccc   ", "  ccccc  ", " ccccccc ", "ccchhhccc", "ccch~hccc", "ccchhhccc",
                                            " ccccccc ", "  ccccc  ", "   ccc   " },
                                    { "   cac   ", "  abfba  ", " abfgfba ", "cbfgdgfbc", "afgdddgfa", "cbfgdgfbc",
                                            " abfgfba ", "  abfba  ", "   cac   " },
                                    { "   cec   ", "  e   e  ", " e     e ", "c   d   c", "e  ddd  e", "c   d   c",
                                            " e     e ", "  e   e  ", "   cec   " },
                                    { "   cec   ", "  e   e  ", " e     e ", "c   d   c", "e  ddd  e", "c   d   c",
                                            " e     e ", "  e   e  ", "   cec   " },
                                    { "   cac   ", "  abfba  ", " abfgfba ", "cbfgdgfbc", "afgdddgfa", "cbfgdgfbc",
                                            " abfgfba ", "  abfba  ", "   cac   " },
                                    { "   ccc   ", "  ccccc  ", " ccccccc ", "ccchhhccc", "ccchhhccc", "ccchhhccc",
                                            " ccccccc ", "  ccccc  ", "   ccc   " }, }))
                    .addElement('a', ofBlock(getCasingBlock4(), getCasingMeta6()))
                    .addElement('b', ofBlock(getCasingBlock4(), getCasingMeta7()))
                    .addElement('d', ofBlock(getCasingBlock2(), getCasingMeta2()))
                    .addElement('e', ofBlock(getCasingBlock2(), getCasingMeta3()))
                    .addElement('f', ofBlock(getCasingBlock3(), getCasingMeta4()))
                    .addElement('g', ofBlock(getCasingBlock3(), getCasingMeta5()))
                    .addElement(
                            'c',
                            lazy(t -> onElementPass(x -> ++x.mCasing, ofBlock(getCasingBlock(), getCasingMeta()))))
                    .addElement(
                            'h',
                            lazy(
                                    t -> ofChain(
                                            buildHatchAdder(GregtechMTE_ElementalDuplicator.class).atLeast(
                                                    InputHatch,
                                                    OutputBus,
                                                    OutputHatch,
                                                    Maintenance,
                                                    Muffler,
                                                    Energy).casingIndex(getCasingTextureIndex()).dot(1).build(),
                                            buildHatchAdder(GregtechMTE_ElementalDuplicator.class)
                                                    .hatchClass(GT_MetaTileEntity_Hatch_ElementalDataOrbHolder.class)
                                                    .adder(GregtechMTE_ElementalDuplicator::addDataOrbHatch)
                                                    .casingIndex(getCasingTextureIndex()).dot(1).build(),
                                            onElementPass(
                                                    x -> ++x.mCasing,
                                                    ofBlock(getCasingBlock(), getCasingMeta())))))
                    .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 4, 4, 0);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        boolean aDidBuild = checkPiece(STRUCTURE_PIECE_MAIN, 4, 4, 0);
        if (this.mInputHatches.size() != 1 || (this.mOutputBusses.size() != 1 && this.mOutputHatches.size() != 0)
                || this.mEnergyHatches.size() != 1
                || this.mReplicatorDataOrbHatches.size() != 1) {
            return false;
        }
        log("Casings: " + mCasing);
        return aDidBuild && mCasing >= 138 && checkHatch();
    }

    protected static int getCasingTextureIndex() {
        return CASING_TEXTURE_ID;
    }

    protected static Block getCasingBlock() {
        return ModBlocks.blockCasings5Misc;
    }

    protected static Block getCasingBlock2() {
        return ModBlocks.blockSpecialMultiCasings;
    }

    protected static Block getCasingBlock3() {
        return ModBlocks.blockSpecialMultiCasings2;
    }

    protected static Block getCasingBlock4() {
        return ModBlocks.blockCasingsMisc;
    }

    protected static int getCasingMeta() {
        return 3;
    }

    protected static int getCasingMeta2() {
        return 12;
    }

    protected static int getCasingMeta3() {
        return 13;
    }

    protected static int getCasingMeta4() {
        return 2;
    }

    protected static int getCasingMeta5() {
        return 6;
    }

    protected static int getCasingMeta6() {
        return 9;
    }

    protected static int getCasingMeta7() {
        return 8;
    }

    private boolean addDataOrbHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity == null) {
                return false;
            }
            if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_ElementalDataOrbHolder) {
                try {
                    ((GT_MetaTileEntity_Hatch_ElementalDataOrbHolder) aMetaTileEntity).mRecipeMap = getRecipeMap();
                    return addToMachineListInternal(mReplicatorDataOrbHatches, aMetaTileEntity, aBaseCasingIndex);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }
        return false;
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.Overlay_Machine_Controller_Advanced;
    }

    @Override
    protected int getCasingTextureId() {
        return CASING_TEXTURE_ID;
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return GTPP_Recipe_Map.sElementalDuplicatorRecipes;
    }

    @Override
    public boolean isCorrectMachinePart(final ItemStack aStack) {
        return true;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected FindRecipeResult findRecipe(@Nullable GT_Recipe_Map map) {
                if (map == null) {
                    return FindRecipeResult.NOT_FOUND;
                }
                try {
                    ItemStack aDataOrbStack = null;
                    recipe: for (GT_Recipe nRecipe : map.mRecipeList) {
                        ItemStack aTempStack = getSpecialSlotStack(nRecipe);
                        if (aTempStack != null) {
                            for (ItemStack aItem : inputItems) {
                                if (nRecipe.mSpecialItems != null) {
                                    if (GT_Utility.areStacksEqual(aTempStack, aItem, false)) {
                                        aDataOrbStack = aTempStack;
                                        break recipe;
                                    }
                                }
                            }
                        }
                    }
                    if (aDataOrbStack != null) {
                        GT_Recipe recipe = GregtechMTE_ElementalDuplicator.this.findRecipe(
                                getBaseMetaTileEntity(),
                                mLastRecipe,
                                false,
                                false,
                                availableVoltage,
                                inputFluids,
                                aDataOrbStack,
                                inputItems);
                        if (recipe != null) {
                            return FindRecipeResult.ofSuccess(recipe);
                        }
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                }
                return FindRecipeResult.NOT_FOUND;
            }
        }.setSpeedBonus(1F / 2F).enablePerfectOverclock().setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (8 * GT_Utility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    public int getMaxEfficiency(final ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return CORE.ConfigSwitches.pollutionPerSecondMultiMolecularTransformer;
    }

    @Override
    public int getDamageToComponent(final ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(final ItemStack aStack) {
        return false;
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);
        // Fix GT bug
        if (this.getBaseMetaTileEntity().getFrontFacing() != ForgeDirection.UP) {
            this.getBaseMetaTileEntity().setFrontFacing(ForgeDirection.UP);
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (this.mUpdate == 1 || this.mStartUpCheck == 1) {
                this.mReplicatorDataOrbHatches.clear();
            }
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public ArrayList<ItemStack> getStoredInputs() {
        ArrayList<ItemStack> tItems = super.getStoredInputs();
        for (GT_MetaTileEntity_Hatch_ElementalDataOrbHolder tHatch : mReplicatorDataOrbHatches) {
            tHatch.mRecipeMap = getRecipeMap();
            if (isValidMetaTileEntity(tHatch)) {
                tItems.add(tHatch.getOrbByCircuit());
            }
        }
        tItems.removeAll(Collections.singleton(null));
        return tItems;
    }

    /**
     * finds a Recipe matching the aFluid and ItemStack Inputs.
     *
     * @param aTileEntity          an Object representing the current coordinates of the executing
     *                             Block/Entity/Whatever. This may be null, especially during Startup.
     * @param aRecipe              in case this is != null it will try to use this Recipe first when looking things up.
     * @param aNotUnificated       if this is T the Recipe searcher will unificate the ItemStack Inputs
     * @param aDontCheckStackSizes if set to false will only return recipes that can be executed at least once with the
     *                             provided input
     * @param aVoltage             Voltage of the Machine or Long.MAX_VALUE if it has no Voltage
     * @param aFluids              the Fluid Inputs
     * @param aSpecialSlot         the content of the Special Slot, the regular Manager doesn't do anything with this,
     *                             but some custom ones do.
     * @param aInputs              the Item Inputs
     * @return the Recipe it has found or null for no matching Recipe
     */
    @Override
    public GT_Recipe findRecipe(IHasWorldObjectAndCoords aTileEntity, GT_Recipe aRecipe, boolean aNotUnificated,
            boolean aDontCheckStackSizes, long aVoltage, FluidStack[] aFluids, ItemStack aSpecialSlot,
            ItemStack... aInputs) {

        GT_Recipe_Map mRecipeMap = this.getRecipeMap();
        // No Recipes? Well, nothing to be found then.
        if (mRecipeMap.mRecipeList.isEmpty()) {
            return null;
        }

        // Some Recipe Classes require a certain amount of Inputs of certain kinds. Like "at least 1 Fluid + 1 Stack" or
        // "at least 2 Stacks" before they start searching for Recipes.
        // This improves Performance massively, especially if people leave things like Circuits, Molds or Shapes in
        // their Machines to select Sub Recipes.
        if (GregTech_API.sPostloadFinished) {
            if (mRecipeMap.mMinimalInputFluids > 0) {
                if (aFluids == null) return null;
                int tAmount = 0;
                for (FluidStack aFluid : aFluids) if (aFluid != null) tAmount++;
                if (tAmount < mRecipeMap.mMinimalInputFluids) return null;
            }
            if (mRecipeMap.mMinimalInputItems > 0) {
                if (aInputs == null) return null;
                int tAmount = 0;
                for (ItemStack aInput : aInputs) if (aInput != null) tAmount++;
                if (tAmount < mRecipeMap.mMinimalInputItems) return null;
            }
        }

        // Unification happens here in case the Input isn't already unificated.
        if (aNotUnificated) {
            aInputs = GT_OreDictUnificator.getStackArray(true, (Object[]) aInputs);
        }

        // Check the Recipe which has been used last time in order to not have to search for it again, if possible.
        if (aRecipe != null) {
            ItemStack aRecipeSpecial = getSpecialSlotStack(aRecipe);
            if (!aRecipe.mFakeRecipe && aRecipe.mCanBeBuffered
                    && aRecipe.isRecipeInputEqual(false, aDontCheckStackSizes, aFluids, aInputs)
                    && GT_Utility.areStacksEqual(aRecipeSpecial, aSpecialSlot, false)
                    && areDataOrbsEqual(aRecipeSpecial, aSpecialSlot)) {
                return aRecipe.mEnabled && aVoltage * mRecipeMap.mAmperage >= aRecipe.mEUt ? aRecipe : null;
            }
        }

        // Now look for the Recipes inside the Item HashMaps, but only when the Recipes usually have Items.
        if (mRecipeMap.mUsualInputCount > 0 && aInputs != null) for (ItemStack tStack : aInputs) if (tStack != null) {
            Collection<GT_Recipe> tRecipes = mRecipeMap.mRecipeItemMap.get(new GT_ItemStack(tStack));
            if (tRecipes != null) {
                for (GT_Recipe tRecipe : tRecipes) {
                    if (!tRecipe.mFakeRecipe
                            && tRecipe.isRecipeInputEqual(false, aDontCheckStackSizes, aFluids, aInputs)) {
                        ItemStack aRecipeSpecial = getSpecialSlotStack(tRecipe);
                        if (GT_Utility.areStacksEqual(aRecipeSpecial, aSpecialSlot, false)
                                && areDataOrbsEqual(aRecipeSpecial, aSpecialSlot)) {
                            return tRecipe.mEnabled && aVoltage * mRecipeMap.mAmperage >= tRecipe.mEUt ? tRecipe : null;
                        }
                    }
                    tRecipes = mRecipeMap.mRecipeItemMap.get(new GT_ItemStack(tStack, true));
                }
            }
            if (tRecipes != null) {
                for (GT_Recipe tRecipe : tRecipes) {
                    if (!tRecipe.mFakeRecipe
                            && tRecipe.isRecipeInputEqual(false, aDontCheckStackSizes, aFluids, aInputs)) {
                        ItemStack aRecipeSpecial = getSpecialSlotStack(tRecipe);
                        if (GT_Utility.areStacksEqual(aRecipeSpecial, aSpecialSlot, false)
                                && areDataOrbsEqual(aRecipeSpecial, aSpecialSlot)) {
                            return tRecipe.mEnabled && aVoltage * mRecipeMap.mAmperage >= tRecipe.mEUt ? tRecipe : null;
                        }
                    }
                }
            }
        }

        // If the minimal Amount of Items for the Recipe is 0, then it could be a Fluid-Only Recipe, so check that Map
        // too.
        if (mRecipeMap.mMinimalInputItems == 0 && aFluids != null)
            for (FluidStack aFluid : aFluids) if (aFluid != null) {
                Collection<GT_Recipe> tRecipes = mRecipeMap.mRecipeFluidMap.get(aFluid.getFluid());
                if (tRecipes != null) for (GT_Recipe tRecipe : tRecipes) {
                    if (!tRecipe.mFakeRecipe
                            && tRecipe.isRecipeInputEqual(false, aDontCheckStackSizes, aFluids, aInputs)) {
                        ItemStack aRecipeSpecial = getSpecialSlotStack(tRecipe);
                        if (GT_Utility.areStacksEqual(aRecipeSpecial, aSpecialSlot, false)
                                && areDataOrbsEqual(aRecipeSpecial, aSpecialSlot)) {
                            return tRecipe.mEnabled && aVoltage * mRecipeMap.mAmperage >= tRecipe.mEUt ? tRecipe : null;
                        }
                    }
                }
            }

        // And nothing has been found.
        return null;
    }

    public static ItemStack getSpecialSlotStack(GT_Recipe aRecipe) {
        ItemStack aStack = null;
        if (aRecipe.mSpecialItems != null) {
            if (aRecipe.mSpecialItems instanceof ItemStack[]) {
                ItemStack[] aTempStackArray = (ItemStack[]) aRecipe.mSpecialItems;
                aStack = aTempStackArray[0];
            }
        }
        return aStack;
    }

    private static boolean areDataOrbsEqual(ItemStack aOrb1, ItemStack aOrb2) {
        if (aOrb1 != null && aOrb2 != null) {
            Materials tMaterial1 = Element.get(Behaviour_DataOrb.getDataName(aOrb1)).mLinkedMaterials.get(0);
            Materials tMaterial2 = Element.get(Behaviour_DataOrb.getDataName(aOrb2)).mLinkedMaterials.get(0);
            if (tMaterial1.equals(tMaterial2)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean doesBindPlayerInventory() {
        return false;
    }
}

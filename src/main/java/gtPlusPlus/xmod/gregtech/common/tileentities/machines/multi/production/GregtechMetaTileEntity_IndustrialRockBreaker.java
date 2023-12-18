package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.Muffler;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import java.util.ArrayList;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.TAE;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_ParallelHelper;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GregtechMetaTileEntity_IndustrialRockBreaker extends
        GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_IndustrialRockBreaker> implements ISurvivalConstructable {

    private int mCasing;
    private static IStructureDefinition<GregtechMetaTileEntity_IndustrialRockBreaker> STRUCTURE_DEFINITION = null;

    public GregtechMetaTileEntity_IndustrialRockBreaker(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GregtechMetaTileEntity_IndustrialRockBreaker(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntity_IndustrialRockBreaker(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Rock Breaker";
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(getMachineType()).addInfo("Controller Block for the Industrial Rock Breaker")
                .addInfo("Speed: +200% | EU Usage: 75% | Parallel: Tier x 8").addInfo("Circuit goes in the GUI slot")
                .addInfo("1 = cobble, 2 = stone, 3 = obsidian").addInfo("Supply Water/Lava")
                .addPollutionAmount(getPollutionPerSecond(null)).addSeparator().beginStructureBlock(3, 4, 3, true)
                .addController("Bottom Center").addCasingInfoMin("Thermal Processing Casing", 9, false)
                .addCasingInfoMin("Thermal Containment Casing", 9, false)
                .addInputBus("Any Thermal Containment Casing", 1).addInputHatch("Any Thermal Containment Casing", 1)
                .addOutputBus("Any Thermal Containment Casing", 1).addEnergyHatch("Any Thermal Containment Casing", 1)
                .addMaintenanceHatch("Any Thermal Containment Casing", 1)
                .addMufflerHatch("Any Thermal Containment Casing", 1).toolTipFinisher(CORE.GT_Tooltip_Builder.get());
        return tt;
    }

    @Override
    public IStructureDefinition<GregtechMetaTileEntity_IndustrialRockBreaker> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_IndustrialRockBreaker>builder()
                    .addShape(
                            mName,
                            transpose(
                                    new String[][] { { "CCC", "CCC", "CCC" }, { "HHH", "H-H", "HHH" },
                                            { "HHH", "H-H", "HHH" }, { "C~C", "CCC", "CCC" }, }))
                    .addElement(
                            'C',
                            buildHatchAdder(GregtechMetaTileEntity_IndustrialRockBreaker.class)
                                    .atLeast(InputBus, InputHatch, OutputBus, Maintenance, Energy, Muffler)
                                    .casingIndex(TAE.GTPP_INDEX(16)).dot(1).buildAndChain(
                                            onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasings2Misc, 0))))
                    .addElement('H', ofBlock(ModBlocks.blockCasings2Misc, 11)).build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 1, 3, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(mName, stackSize, 1, 3, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        boolean aCheckPiece = checkPiece(mName, 1, 3, 0);
        boolean aCasingCount = mCasing >= 9;
        boolean aCheckHatch = checkHatch();
        log("" + aCheckPiece + ", " + aCasingCount + ", " + aCheckHatch);
        return aCheckPiece && aCasingCount && aCheckHatch;
    }

    @Override
    protected SoundResource getProcessStartSound() {
        return SoundResource.IC2_MACHINES_INDUCTION_LOOP;
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
        return TAE.GTPP_INDEX(16);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.rockBreakerFakeRecipes;
    }

    @Override
    protected boolean filtersFluid() {
        return false;
    }

    @Override
    public boolean isCorrectMachinePart(final ItemStack aStack) {
        return true;
    }

    private static GT_Recipe sRecipe_Cobblestone;
    private static GT_Recipe sRecipe_SmoothStone;
    private static GT_Recipe sRecipe_Redstone;

    private static void generateRecipes() {
        sRecipe_Cobblestone = new GT_Recipe(
                false,
                new ItemStack[] { CI.getNumberedCircuit(1) },
                new ItemStack[] { ItemUtils.getSimpleStack(Blocks.cobblestone) },
                null,
                new int[] { 10000 },
                null,
                null,
                16,
                32,
                0);
        sRecipe_SmoothStone = new GT_Recipe(
                false,
                new ItemStack[] { CI.getNumberedCircuit(2) },
                new ItemStack[] { ItemUtils.getSimpleStack(Blocks.stone) },
                null,
                new int[] { 10000 },
                null,
                null,
                16,
                32,
                0);
        sRecipe_Redstone = new GT_Recipe(
                false,
                new ItemStack[] { CI.getNumberedCircuit(3),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L) },
                new ItemStack[] { ItemUtils.getSimpleStack(Blocks.obsidian) },
                null,
                new int[] { 10000 },
                null,
                null,
                128,
                32,
                0);
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        ArrayList<FluidStack> aFluids = this.getStoredFluids();
        if (aFluids.isEmpty()) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        boolean aHasWater = false;
        boolean aHasLava = false;
        for (FluidStack aFluid : aFluids) {
            if (aFluid.getFluid() == FluidRegistry.WATER) {
                aHasWater = true;
            } else if (aFluid.getFluid() == FluidRegistry.LAVA) {
                aHasLava = true;
            }
        }
        ArrayList<ItemStack> aItems = this.getStoredInputs();
        boolean aHasRedstone = false;
        if (!aItems.isEmpty()) {
            for (ItemStack aItem : aItems) {
                if (GT_Utility
                        .areStacksEqual(aItem, GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L))) {
                    aHasRedstone = true;
                    break;
                }
            }
        }

        if (!aHasWater) {
            return SimpleCheckRecipeResult.ofFailure("no_water");
        }
        if (!aHasLava) {
            return SimpleCheckRecipeResult.ofFailure("no_lava");
        }
        ItemStack aGuiCircuit = this.getControllerSlot();
        if (!ItemUtils.isControlCircuit(aGuiCircuit)) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        if (sRecipe_Cobblestone == null || sRecipe_SmoothStone == null || sRecipe_Redstone == null) {
            generateRecipes();
        }

        int aCircuit = aGuiCircuit.getItemDamage();

        GT_Recipe tRecipe = null;
        switch (aCircuit) {
            case 1 -> tRecipe = sRecipe_Cobblestone;
            case 2 -> tRecipe = sRecipe_SmoothStone;
            case 3 -> {
                if (aHasRedstone) {
                    tRecipe = sRecipe_Redstone;
                }
            }
        }

        if (tRecipe == null) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        ItemStack[] aItemInputs = aItems.toArray(new ItemStack[0]);
        FluidStack[] aFluidInputs = new FluidStack[] {};

        long tEnergy = getMaxInputEnergy();
        // Remember last recipe - an optimization for findRecipe()
        this.mLastRecipe = tRecipe;

        GT_ParallelHelper helper = new GT_ParallelHelper().setRecipe(tRecipe).setItemInputs(aItemInputs)
                .setFluidInputs(aFluidInputs).setAvailableEUt(tEnergy).setMaxParallel(getMaxParallelRecipes())
                .setConsumption(true).setOutputCalculation(true).setEUtModifier(0.75F).setMachine(this);

        if (batchMode) {
            helper.enableBatchMode(128);
        }

        helper.build();

        if (helper.getCurrentParallel() == 0) {
            return CheckRecipeResultRegistry.ITEM_OUTPUT_FULL;
        }

        this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
        this.mEfficiencyIncrease = 10000;

        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(tRecipe.mEUt).setEUt(tEnergy)
                .setDuration(tRecipe.mDuration).setEUtDiscount(0.75F).setSpeedBoost(1F / 3F)
                .setParallel((int) Math.floor(helper.getCurrentParallel() / helper.getDurationMultiplierDouble()))
                .calculate();
        lEUt = -calculator.getConsumption();
        mMaxProgresstime = (int) Math.ceil(calculator.getDuration() * helper.getDurationMultiplierDouble());

        mOutputItems = helper.getItemOutputs();
        mOutputFluids = helper.getFluidOutputs();
        updateSlots();
        return CheckRecipeResultRegistry.SUCCESSFUL;

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
        return CORE.ConfigSwitches.pollutionPerSecondMultiIndustrialRockBreaker;
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
    public ArrayList<ItemStack> getStoredInputs() {
        ArrayList<ItemStack> aInputs = super.getStoredInputs();
        if (this.getControllerSlot() != null) {
            aInputs.add(this.getControllerSlot());
        }
        return aInputs;
    }
}

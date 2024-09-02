package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

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
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.ParallelHelper;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTEIndustrialRockBreaker extends GTPPMultiBlockBase<MTEIndustrialRockBreaker>
    implements ISurvivalConstructable {

    private int mCasing;
    private static IStructureDefinition<MTEIndustrialRockBreaker> STRUCTURE_DEFINITION = null;

    public MTEIndustrialRockBreaker(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEIndustrialRockBreaker(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEIndustrialRockBreaker(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Rock Breaker";
    }

    private static final String casingBaseName = GTLanguageManager.getTranslation("gtplusplus.blockcasings.2.0.name");
    private static final String casingMiddleName = GTLanguageManager
        .getTranslation("gtplusplus.blockcasings.2.11.name");
    private static final String anyBaseCasing = "Any " + casingBaseName;

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addInfo("Controller Block for the Industrial Rock Breaker")
            .addInfo("Speed: +200% | EU Usage: 75% | Parallel: Tier x 8")
            .addInfo("Circuit goes in the GUI slot")
            .addInfo("1 = cobble, 2 = stone, 3 = obsidian")
            .addInfo("Needs Water and Lava in input hatch")
            .addPollutionAmount(getPollutionPerSecond(null))
            .addSeparator()
            .beginStructureBlock(3, 4, 3, true)
            .addController("Bottom Center")
            .addCasingInfoMin(casingBaseName, 9, false)
            .addCasingInfoExactly(casingMiddleName, 16, false)
            .addInputBus(anyBaseCasing, 1)
            .addInputHatch(anyBaseCasing, 1)
            .addOutputBus(anyBaseCasing, 1)
            .addEnergyHatch(anyBaseCasing, 1)
            .addMaintenanceHatch(anyBaseCasing, 1)
            .addMufflerHatch(anyBaseCasing, 1)
            .toolTipFinisher(GTPPCore.GT_Tooltip_Builder.get());
        return tt;
    }

    @Override
    public IStructureDefinition<MTEIndustrialRockBreaker> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEIndustrialRockBreaker>builder()
                .addShape(
                    mName,
                    transpose(
                        new String[][] { { "CCC", "CCC", "CCC" }, { "HHH", "H-H", "HHH" }, { "HHH", "H-H", "HHH" },
                            { "C~C", "CCC", "CCC" }, }))
                .addElement(
                    'C',
                    buildHatchAdder(MTEIndustrialRockBreaker.class)
                        .atLeast(InputBus, InputHatch, OutputBus, Maintenance, Energy, Muffler)
                        .casingIndex(TAE.GTPP_INDEX(16))
                        .dot(1)
                        .buildAndChain(onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasings2Misc, 0))))
                .addElement('H', ofBlock(ModBlocks.blockCasings2Misc, 11))
                .build();
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
        return TexturesGtBlock.oMCAIndustrialRockBreakerActive;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.oMCAIndustrialRockBreaker;
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

    private static GTRecipe sRecipe_Cobblestone;
    private static GTRecipe sRecipe_SmoothStone;
    private static GTRecipe sRecipe_Redstone;

    private static void generateRecipes() {
        sRecipe_Cobblestone = new GTRecipe(
            false,
            new ItemStack[] { GTUtility.getIntegratedCircuit(1) },
            new ItemStack[] { ItemUtils.getSimpleStack(Blocks.cobblestone) },
            null,
            new int[] { 10000 },
            null,
            null,
            16,
            32,
            0);
        sRecipe_SmoothStone = new GTRecipe(
            false,
            new ItemStack[] { GTUtility.getIntegratedCircuit(2) },
            new ItemStack[] { ItemUtils.getSimpleStack(Blocks.stone) },
            null,
            new int[] { 10000 },
            null,
            null,
            16,
            32,
            0);
        sRecipe_Redstone = new GTRecipe(
            false,
            new ItemStack[] { GTUtility.getIntegratedCircuit(3),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L) },
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
                if (GTUtility
                    .areStacksEqual(aItem, GTOreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L))) {
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

        GTRecipe tRecipe = null;
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

        ParallelHelper helper = new ParallelHelper().setRecipe(tRecipe)
            .setItemInputs(aItemInputs)
            .setFluidInputs(aFluidInputs)
            .setAvailableEUt(tEnergy)
            .setMaxParallel(getMaxParallelRecipes())
            .setConsumption(true)
            .setOutputCalculation(true)
            .setEUtModifier(0.75F)
            .setMachine(this);

        if (batchMode) {
            helper.enableBatchMode(128);
        }

        helper.build();

        if (helper.getCurrentParallel() == 0) {
            return CheckRecipeResultRegistry.ITEM_OUTPUT_FULL;
        }

        this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
        this.mEfficiencyIncrease = 10000;

        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(tRecipe.mEUt)
            .setEUt(tEnergy)
            .setDuration(tRecipe.mDuration)
            .setEUtDiscount(0.75F)
            .setSpeedBoost(1F / 3F)
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
        return (8 * GTUtility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    public int getMaxEfficiency(final ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return GTPPCore.ConfigSwitches.pollutionPerSecondMultiIndustrialRockBreaker;
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

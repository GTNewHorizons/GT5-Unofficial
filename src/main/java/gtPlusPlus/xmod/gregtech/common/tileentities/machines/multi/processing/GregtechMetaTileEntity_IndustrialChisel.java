package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.Muffler;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import team.chisel.carving.Carving;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTPP_Recipe;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_ParallelHelper;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GregtechMetaTileEntity_IndustrialChisel
        extends GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_IndustrialChisel> implements ISurvivalConstructable {

    private int mCasing;
    private static IStructureDefinition<GregtechMetaTileEntity_IndustrialChisel> STRUCTURE_DEFINITION = null;
    private ItemStack mInputCache;
    private ItemStack mOutputCache;
    private GTPP_Recipe mCachedRecipe;

    public GregtechMetaTileEntity_IndustrialChisel(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GregtechMetaTileEntity_IndustrialChisel(String aName) {
        super(aName);
    }

    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntity_IndustrialChisel(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Chisel";
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(getMachineType()).addInfo("Factory Grade Auto Chisel")
                .addInfo("Target block goes in GUI slot").addInfo("If no target provided, first chisel result is used")
                .addInfo("Speed: +200% | EU Usage: 75% | Parallel: Tier x 16")
                .addPollutionAmount(getPollutionPerSecond(null)).addSeparator().beginStructureBlock(3, 3, 3, true)
                .addController("Front center").addCasingInfo("Sturdy Printer Casing", 10).addInputBus("Any casing", 1)
                .addOutputBus("Any casing", 1).addEnergyHatch("Any casing", 1).addMaintenanceHatch("Any casing", 1)
                .addMufflerHatch("Any casing", 1).toolTipFinisher(CORE.GT_Tooltip_Builder.get());
        return tt;
    }

    @Override
    public IStructureDefinition<GregtechMetaTileEntity_IndustrialChisel> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_IndustrialChisel>builder()
                    .addShape(
                            mName,
                            transpose(
                                    new String[][] { { "CCC", "CCC", "CCC" }, { "C~C", "C-C", "CCC" },
                                            { "CCC", "CCC", "CCC" }, }))
                    .addElement(
                            'C',
                            buildHatchAdder(GregtechMetaTileEntity_IndustrialChisel.class)
                                    .atLeast(InputBus, OutputBus, Maintenance, Energy, Muffler).casingIndex(90).dot(1)
                                    .buildAndChain(
                                            onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasings5Misc, 5))))
                    .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 1, 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(mName, stackSize, 1, 1, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        return checkPiece(mName, 1, 1, 0) && mCasing >= 10 && checkHatch();
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
        return 90;
    }

    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return null;
    }

    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    private boolean hasValidCache(ItemStack aStack, ItemStack aSpecialSlot, boolean aClearOnFailure) {
        if (mInputCache != null && mOutputCache != null && mCachedRecipe != null) {
            if (GT_Utility.areStacksEqual(aStack, mInputCache)
                    && GT_Utility.areStacksEqual(aSpecialSlot, mOutputCache)) {
                return true;
            }
        }
        // clear cache if it was invalid
        if (aClearOnFailure) {
            mInputCache = null;
            mOutputCache = null;
            mCachedRecipe = null;
        }
        return false;
    }

    private void cacheItem(ItemStack aInputItem, ItemStack aOutputItem, GTPP_Recipe aRecipe) {
        mInputCache = aInputItem.copy();
        mOutputCache = aOutputItem.copy();
        mCachedRecipe = aRecipe;
    }

    // lets make sure the user isn't trying to make something from a block that doesn't have this as a valid target
    private static boolean canBeMadeFrom(ItemStack from, ItemStack to) {
        List<ItemStack> results = getItemsForChiseling(from);
        for (ItemStack s : results) {
            if (s.getItem() == to.getItem() && s.getItemDamage() == to.getItemDamage()) {
                return true;
            }
        }
        return false;
    }

    // lets make sure the user isn't trying to make something from a block that doesn't have this as a valid target
    private static boolean hasChiselResults(ItemStack from) {
        List<ItemStack> results = getItemsForChiseling(from);
        return results.size() > 0;
    }

    private static List<ItemStack> getItemsForChiseling(ItemStack aStack) {
        return Carving.chisel.getItemsForChiseling(aStack);
    }

    private static ItemStack getChiselOutput(ItemStack aInput, ItemStack aTarget) {
        ItemStack tOutput = null;
        if (aTarget != null && canBeMadeFrom(aInput, aTarget)) {
            tOutput = aTarget;
        } else if (aTarget != null && !canBeMadeFrom(aInput, aTarget)) {
            tOutput = null;
        } else {
            tOutput = getItemsForChiseling(aInput).get(0);
        }
        return tOutput;
    }

    private GTPP_Recipe generateChiselRecipe(ItemStack aInput, ItemStack aTarget) {
        boolean tIsCached = hasValidCache(aInput, aTarget, true);
        if (tIsCached || aInput != null && hasChiselResults(aInput)) {
            ItemStack tOutput = tIsCached ? mOutputCache.copy() : getChiselOutput(aInput, aTarget);
            if (tOutput != null) {
                if (mCachedRecipe != null && GT_Utility.areStacksEqual(aInput, mInputCache)
                        && GT_Utility.areStacksEqual(tOutput, mOutputCache)) {
                    return mCachedRecipe;
                }
                // We can chisel this
                GTPP_Recipe aRecipe = new GTPP_Recipe(
                        false,
                        new ItemStack[] { ItemUtils.getSimpleStack(aInput, 1) },
                        new ItemStack[] { ItemUtils.getSimpleStack(tOutput, 1) },
                        null,
                        new int[] { 10000 },
                        new FluidStack[] {},
                        new FluidStack[] {},
                        20,
                        16,
                        0);

                // Cache it
                cacheItem(aInput, tOutput, aRecipe);
                return aRecipe;
            }
        }
        return null;
    }

    public boolean checkRecipe(final ItemStack aStack) {
        ArrayList<ItemStack> aItems = this.getStoredInputs();
        if (!aItems.isEmpty()) {

            GT_Recipe tRecipe = generateChiselRecipe(aItems.get(0), this.getGUIItemStack());

            if (tRecipe == null) {
                return false;
            }

            // Based on the Processing Array. A bit overkill, but very flexible.
            ItemStack[] aItemInputs = aItems.toArray(new ItemStack[aItems.size()]);
            FluidStack[] aFluidInputs = new FluidStack[] {};

            // Reset outputs and progress stats
            this.lEUt = 0;
            this.mMaxProgresstime = 0;
            this.mOutputItems = new ItemStack[] {};
            this.mOutputFluids = new FluidStack[] {};

            long tVoltage = getMaxInputVoltage();
            long tEnergy = getMaxInputEnergy();
            // Remember last recipe - an optimization for findRecipe()
            this.mLastRecipe = tRecipe;

            int aMaxParallelRecipes = getMaxParallelRecipes();
            int aEUPercent = getEuDiscountForParallelism();
            int aSpeedBonusPercent = 200;

            GT_ParallelHelper helper = new GT_ParallelHelper().setRecipe(tRecipe).setItemInputs(aItemInputs)
                    .setFluidInputs(aFluidInputs).setAvailableEUt(tEnergy).setMaxParallel(aMaxParallelRecipes)
                    .enableConsumption().enableOutputCalculation().setEUtModifier(aEUPercent / 100.0f);
            if (!voidExcess) {
                helper.enableVoidProtection(this);
            }

            if (batchMode) {
                helper.enableBatchMode(128);
            }

            helper.build();

            if (helper.getCurrentParallel() == 0) {
                return false;
            }

            this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
            this.mEfficiencyIncrease = 10000;

            GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(tRecipe.mEUt).setEUt(tEnergy)
                    .setDuration(tRecipe.mDuration).setEUtDiscount(aEUPercent / 100.0f)
                    .setSpeedBoost(100.0f / (100.0f + aSpeedBonusPercent))
                    .setParallel((int) Math.floor(helper.getCurrentParallel() / helper.getDurationMultiplier()))
                    .calculate();
            lEUt = -calculator.getConsumption();
            mMaxProgresstime = (int) Math.ceil(calculator.getDuration() * helper.getDurationMultiplier());

            mOutputItems = helper.getItemOutputs();
            mOutputFluids = helper.getFluidOutputs();
            updateSlots();

            // Play sounds (GT++ addition - GT multiblocks play no sounds)
            startProcess();
            return true;
        }

        return false;
    }

    @Override
    public int getMaxParallelRecipes() {
        return (16 * GT_Utility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    public int getEuDiscountForParallelism() {
        return 75;
    }

    private static String sChiselSound = null;

    private static final String getChiselSound() {
        if (sChiselSound == null) {
            sChiselSound = Carving.chisel.getVariationSound(Blocks.stone, 0);
        }
        return sChiselSound;
    }

    @Override
    public String getSound() {
        return getChiselSound();
    }

    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    public int getPollutionPerSecond(ItemStack aStack) {
        return CORE.ConfigSwitches.pollutionPerSecondMultiIndustrialChisel;
    }

    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }
}

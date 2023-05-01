package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.algae;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.TAE;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_ParallelHelper;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.item.chemistry.AgriculturalChem;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.loaders.recipe.RecipeLoader_AlgaeFarm;
import ic2.core.init.BlocksItems;
import ic2.core.init.InternalName;

public class GregtechMTE_AlgaePondBase extends GregtechMeta_MultiBlockBase<GregtechMTE_AlgaePondBase>
        implements ISurvivalConstructable {

    private int mLevel = -1;
    private int mCasing;
    private static IStructureDefinition<GregtechMTE_AlgaePondBase> STRUCTURE_DEFINITION = null;
    private int checkMeta;
    private int minTierOfHatch;
    private static final Class<?> cofhWater;

    static {
        cofhWater = ReflectionUtils.getClass("cofh.asmhooks.block.BlockWater");
    }

    public GregtechMTE_AlgaePondBase(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GregtechMTE_AlgaePondBase(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new GregtechMTE_AlgaePondBase(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Algae Pond";
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(getMachineType()).addInfo("Grows Algae!").addInfo("Controller Block for the Algae Farm")
                .addInfo("Provide compost to boost production by one tier")
                .addInfo("Does not require power or maintenance")
                .addInfo("All Machine Casings must be the same tier, this dictates machine speed.")
                .addInfo("All Buses/Hatches must, at least, match the tier of the Casings")
                .addInfo("Fill Input Hatch with Water to fill the inside of the multiblock.")
                .addPollutionAmount(getPollutionPerSecond(null)).addSeparator().beginStructureBlock(9, 3, 9, true)
                .addController("Front Center").addCasingInfo("Machine Casings", 64)
                .addCasingInfo("Sterile Farm Casings", 64).addInputBus("Any Casing", 1).addOutputBus("Any Casing", 1)
                .addInputHatch("Any Casing", 1).toolTipFinisher(CORE.GT_Tooltip_Builder.get());
        return tt;
    }

    public void setMeta(int meta) {
        checkMeta = meta;
    }

    public int getMeta() {
        return checkMeta;
    }

    @Override
    public IStructureDefinition<GregtechMTE_AlgaePondBase> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GregtechMTE_AlgaePondBase>builder()
                    .addShape(
                            mName,
                            transpose(
                                    new String[][] {
                                            { "XXXXXXXXX", "X       X", "X       X", "X       X", "X       X",
                                                    "X       X", "X       X", "X       X", "XXXXXXXXX" },
                                            { "XXXXXXXXX", "X       X", "X       X", "X       X", "X       X",
                                                    "X       X", "X       X", "X       X", "XXXXXXXXX" },
                                            { "CCCC~CCCC", "CCCCCCCCC", "CCCCCCCCC", "CCCCCCCCC", "CCCCCCCCC",
                                                    "CCCCCCCCC", "CCCCCCCCC", "CCCCCCCCC", "CCCCCCCCC" }, }))
                    .addElement(
                            'C',
                            ofChain(
                                    buildHatchAdder(GregtechMTE_AlgaePondBase.class)
                                            .atLeast(InputHatch, InputBus, OutputBus)
                                            .casingIndex(TAE.getIndexFromPage(1, 15)).dot(1).build(),
                                    onElementPass(
                                            x -> ++x.mCasing,
                                            addTieredBlock(
                                                    GregTech_API.sBlockCasings1,
                                                    GregtechMTE_AlgaePondBase::setMeta,
                                                    GregtechMTE_AlgaePondBase::getMeta,
                                                    10))))
                    .addElement('X', ofBlock(ModBlocks.blockCasings2Misc, 15)).build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 4, 2, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(mName, stackSize, 4, 2, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        mLevel = 0;
        checkMeta = 0;
        minTierOfHatch = 100;
        if (checkPiece(mName, 4, 2, 0) && mCasing >= 64 && checkMeta > 0) {
            mLevel = checkMeta - 1;
            return mLevel <= minTierOfHatch;
        }
        return false;
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> d.offsetY == 0 && r.isNotRotated() && !f.isVerticallyFliped();
    }

    @Override
    public String getSound() {
        return GregTech_API.sSoundList.get(Integer.valueOf(207));
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return TexturesGtBlock.Overlay_Machine_Controller_Default_Active;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.Overlay_Machine_Controller_Default;
    }

    @Override
    protected int getCasingTextureId() {
        int aID = TAE.getIndexFromPage(1, 15);
        if (mLevel > -1) {
            aID = mLevel;
        }
        return aID;
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return null;
    }

    @Override
    public int getMaxParallelRecipes() {
        return 2;
    }

    @Override
    public int getEuDiscountForParallelism() {
        return 0;
    }

    public boolean checkForWater() {

        // Get Facing direction
        IGregTechTileEntity aBaseMetaTileEntity = this.getBaseMetaTileEntity();
        int mDirectionX = aBaseMetaTileEntity.getBackFacing().offsetX;
        int mCurrentDirectionX;
        int mCurrentDirectionZ;
        int mOffsetX_Lower = 0;
        int mOffsetX_Upper = 0;
        int mOffsetZ_Lower = 0;
        int mOffsetZ_Upper = 0;

        mCurrentDirectionX = 4;
        mCurrentDirectionZ = 4;

        mOffsetX_Lower = -4;
        mOffsetX_Upper = 4;
        mOffsetZ_Lower = -4;
        mOffsetZ_Upper = 4;

        // if (aBaseMetaTileEntity.fac)

        final int xDir = aBaseMetaTileEntity.getBackFacing().offsetX * mCurrentDirectionX;
        final int zDir = aBaseMetaTileEntity.getBackFacing().offsetZ * mCurrentDirectionZ;

        int tAmount = 0;
        for (int i = mOffsetX_Lower + 1; i <= mOffsetX_Upper - 1; ++i) {
            for (int j = mOffsetZ_Lower + 1; j <= mOffsetZ_Upper - 1; ++j) {
                for (int h = 0; h < 2; h++) {
                    Block tBlock = aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j);
                    byte tMeta = aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j);
                    if (isNotStaticWater(tBlock, tMeta)) {
                        if (this.getStoredFluids() != null) {
                            for (FluidStack stored : this.getStoredFluids()) {
                                if (stored.isFluidEqual(FluidUtils.getFluidStack("water", 1))) {
                                    if (stored.amount >= 1000) {
                                        // Utils.LOG_WARNING("Going to try swap an air block for water from inut bus.");
                                        stored.amount -= 1000;
                                        Block fluidUsed = Blocks.water;
                                        aBaseMetaTileEntity.getWorld().setBlock(
                                                aBaseMetaTileEntity.getXCoord() + xDir + i,
                                                aBaseMetaTileEntity.getYCoord() + h,
                                                aBaseMetaTileEntity.getZCoord() + zDir + j,
                                                fluidUsed);
                                    }
                                }
                            }
                        }
                    }
                    tBlock = aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j);
                    if (tBlock == Blocks.water || tBlock == Blocks.flowing_water) {
                        ++tAmount;
                        // Logger.INFO("Found Water");
                    }
                }
            }
        }

        boolean isValidWater = tAmount >= 49;

        if (isValidWater) {
            Logger.INFO("Filled structure.");
            return true;
        } else {
            return false;
        }
    }

    private boolean isNotStaticWater(Block block, byte meta) {
        return block == Blocks.air || block == Blocks.flowing_water
                || block == BlocksItems.getFluidBlock(InternalName.fluidDistilledWater)
                || (cofhWater != null && cofhWater.isAssignableFrom(block.getClass()) && meta != 0);
    }

    @Override
    public int getMaxEfficiency(final ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return CORE.ConfigSwitches.pollutionPerSecondMultiAlgaePond;
    }

    @Override
    public int getAmountOfOutputs() {
        return 1;
    }

    @Override
    public boolean explodesOnComponentBreak(final ItemStack aStack) {
        return false;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);
        this.fixAllMaintenanceIssue();
        // Silly Client Syncing
        if (aBaseMetaTileEntity.isClientSide()) {
            this.mLevel = getCasingTier();
        }
    }

    @Override
    public boolean checkRecipe(final ItemStack aStack) {
        return checkRecipeGeneric(getMaxParallelRecipes(), getEuDiscountForParallelism(), 0);
    }

    @Override
    public boolean checkRecipeGeneric(ItemStack[] aItemInputs, FluidStack[] aFluidInputs, int aMaxParallelRecipes,
            long aEUPercent, int aSpeedBonusPercent, int aOutputChanceRoll, GT_Recipe aRecipe) {

        if (this.mLevel < 0) {
            return false;
        }

        if (!checkForWater()) {
            return false;
        }

        // Reset outputs and progress stats
        this.lEUt = 0;
        this.mMaxProgresstime = 0;
        this.mOutputItems = new ItemStack[] {};
        this.mOutputFluids = new FluidStack[] {};

        GT_Recipe tRecipe = RecipeLoader_AlgaeFarm.getTieredRecipeFromCache(this.mLevel, isUsingCompost(aItemInputs));

        this.mLastRecipe = tRecipe;

        if (tRecipe == null) {
            return false;
        }

        GT_ParallelHelper helper = new GT_ParallelHelper().setRecipe(tRecipe).setItemInputs(aItemInputs)
                .setFluidInputs(aFluidInputs).setAvailableEUt(120).setMaxParallel(aMaxParallelRecipes)
                .enableConsumption().enableOutputCalculation();
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
        mMaxProgresstime = (int) Math.ceil(tRecipe.mDuration * helper.getDurationMultiplier());

        mOutputItems = helper.getItemOutputs();
        mOutputFluids = helper.getFluidOutputs();
        updateSlots();

        // Play sounds (GT++ addition - GT multiblocks play no sounds)
        startProcess();
        return true;
    }

    private boolean isUsingCompost(ItemStack[] aItemInputs) {
        ItemStack aCompost = ItemUtils.getSimpleStack(AgriculturalChem.mCompost, 1);
        for (ItemStack i : aItemInputs) {
            if (GT_Utility.areStacksEqual(aCompost, i)) {
                if (i.stackSize >= 8) {
                    return true;
                }
            }
        }
        return false;
    }

    private int getCasingTier() {
        if (this.getBaseMetaTileEntity().getWorld() == null) {
            return 0;
        }
        try {
            Block aInitStructureCheck;
            int aInitStructureCheckMeta;
            IGregTechTileEntity aBaseMetaTileEntity = this.getBaseMetaTileEntity();
            int xDir = aBaseMetaTileEntity.getBackFacing().offsetX;
            int zDir = aBaseMetaTileEntity.getBackFacing().offsetZ;
            aInitStructureCheck = aBaseMetaTileEntity.getBlockOffset(xDir, -1, zDir);
            aInitStructureCheckMeta = aBaseMetaTileEntity.getMetaIDOffset(xDir, -1, zDir);
            if (aInitStructureCheck == GregTech_API.sBlockCasings1) {
                return aInitStructureCheckMeta;
            }
            return 0;
        } catch (Throwable t) {
            t.printStackTrace();
            return 0;
        }
    }
}

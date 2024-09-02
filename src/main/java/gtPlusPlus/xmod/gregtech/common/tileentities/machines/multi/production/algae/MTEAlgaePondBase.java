package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.algae;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.TAE;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTStreamUtil;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.item.chemistry.AgriculturalChem;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.loaders.recipe.RecipeLoaderAlgaeFarm;
import ic2.core.init.BlocksItems;
import ic2.core.init.InternalName;
import tectech.thing.casing.TTCasingsContainer;

public class MTEAlgaePondBase extends GTPPMultiBlockBase<MTEAlgaePondBase> implements ISurvivalConstructable {

    private int mLevel = -1;
    private int mCasing;
    private static IStructureDefinition<MTEAlgaePondBase> STRUCTURE_DEFINITION = null;
    private int checkMeta;
    private static final Class<?> cofhWater;

    static {
        cofhWater = ReflectionUtils.getClass("cofh.asmhooks.block.BlockWater");
    }

    public MTEAlgaePondBase(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEAlgaePondBase(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEAlgaePondBase(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Algae Pond";
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addInfo("Grows Algae!")
            .addInfo("Controller Block for the Algae Farm")
            .addInfo("Provide compost to boost production by one tier")
            .addInfo("Does not require power or maintenance")
            .addInfo("All Machine Casings must be the same tier, this dictates machine speed.")
            .addInfo("Requires one Input Hatch that matches the tier of the Casings")
            .addInfo("Fill Input Hatch with Water to fill the inside of the multiblock.")
            .addPollutionAmount(getPollutionPerSecond(null))
            .addSeparator()
            .beginStructureBlock(9, 3, 9, true)
            .addController("Front Center")
            .addCasingInfoMin("Machine Casings", 64, true)
            .addCasingInfoMin("Sterile Farm Casings", 64, false)
            .addInputBus("Any Casing", 1)
            .addOutputBus("Any Casing", 1)
            .addInputHatch("Any Casing", 1)
            .toolTipFinisher(GTPPCore.GT_Tooltip_Builder.get());
        return tt;
    }

    public void setMeta(int meta) {
        checkMeta = meta;
    }

    public int getMeta() {
        return checkMeta;
    }

    @Override
    public IStructureDefinition<MTEAlgaePondBase> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEAlgaePondBase>builder()
                .addShape(
                    mName,
                    transpose(
                        new String[][] {
                            { "XXXXXXXXX", "X       X", "X       X", "X       X", "X       X", "X       X", "X       X",
                                "X       X", "XXXXXXXXX" },
                            { "XXXXXXXXX", "X       X", "X       X", "X       X", "X       X", "X       X", "X       X",
                                "X       X", "XXXXXXXXX" },
                            { "CCCC~CCCC", "CCCCCCCCC", "CCCCCCCCC", "CCCCCCCCC", "CCCCCCCCC", "CCCCCCCCC", "CCCCCCCCC",
                                "CCCCCCCCC", "CCCCCCCCC" }, }))
                .addElement(
                    'C',
                    ofChain(
                        buildHatchAdder(MTEAlgaePondBase.class).atLeast(InputHatch, InputBus, OutputBus)
                            .casingIndex(TAE.getIndexFromPage(1, 15))
                            .dot(1)
                            .build(),
                        onElementPass(
                            x -> ++x.mCasing,
                            addTieredBlock(
                                GregTechAPI.sBlockCasings1,
                                MTEAlgaePondBase::setMeta,
                                MTEAlgaePondBase::getMeta,
                                10)),
                        onElementPass(
                            x -> ++x.mCasing,
                            addTieredBlock(
                                TTCasingsContainer.sBlockCasingsNH,
                                MTEAlgaePondBase::setMeta,
                                MTEAlgaePondBase::getMeta,
                                10,
                                15))))
                .addElement('X', ofBlock(ModBlocks.blockCasings2Misc, 15))
                .build();
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

        if (checkPiece(mName, 4, 2, 0) && mCasing >= 64
            && checkMeta > 0
            && mInputHatches.size() >= 1
            && mOutputBusses.size() >= 1) {
            mLevel = checkMeta - 1;
            for (MTEHatchInput inputHatch : mInputHatches) {
                if (inputHatch.mTier < mLevel) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> d.offsetY == 0 && r.isNotRotated() && !f.isVerticallyFliped();
    }

    @Override
    protected SoundResource getProcessStartSound() {
        return SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP;
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return TexturesGtBlock.oMCDAlgaePondBaseActive;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.oMCDAlgaePondBase;
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
    public int getMaxParallelRecipes() {
        return 2;
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
                                        aBaseMetaTileEntity.getWorld()
                                            .setBlock(
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
        return GTPPCore.ConfigSwitches.pollutionPerSecondMultiAlgaePond;
    }

    @Override
    public boolean explodesOnComponentBreak(final ItemStack aStack) {
        return false;
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);
        // Silly Client Syncing
        if (aBaseMetaTileEntity.isClientSide()) {
            this.mLevel = getCasingTier();
        }
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Nonnull
            @Override
            protected Stream<GTRecipe> findRecipeMatches(@Nullable RecipeMap<?> map) {
                return GTStreamUtil
                    .ofNullable(RecipeLoaderAlgaeFarm.getTieredRecipeFromCache(mLevel, isUsingCompost(inputItems)));
            }

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                if (!checkForWater()) {
                    return SimpleCheckRecipeResult.ofFailure("no_water");
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }
        }.setEuModifier(0F)
            .setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    private boolean isUsingCompost(ItemStack[] aItemInputs) {
        ItemStack aCompost = ItemUtils.getSimpleStack(AgriculturalChem.mCompost, 1);
        for (ItemStack i : aItemInputs) {
            if (GTUtility.areStacksEqual(aCompost, i)) {
                if (i.stackSize >= RecipeLoaderAlgaeFarm.compostForTier(mLevel)) {
                    return true;
                }
            }
        }
        return false;
    }

    private int getCasingTier() {
        if (this.getBaseMetaTileEntity()
            .getWorld() == null) {
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
            if (aInitStructureCheck == GregTechAPI.sBlockCasings1
                || aInitStructureCheck == TTCasingsContainer.sBlockCasingsNH) {
                return aInitStructureCheckMeta;
            }
            return 0;
        } catch (Throwable t) {
            t.printStackTrace();
            return 0;
        }
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }
}

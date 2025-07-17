package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.algae;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import java.util.ArrayList;
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

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
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
import gregtech.api.util.ReflectionUtil;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import ic2.core.init.BlocksItems;
import ic2.core.init.InternalName;

public class MTEAlgaePondBase extends GTPPMultiBlockBase<MTEAlgaePondBase> implements ISurvivalConstructable {

    // TODO add a NEI handler for this machine

    private int mLevel = -1;
    private int mCasing;
    private static IStructureDefinition<MTEAlgaePondBase> STRUCTURE_DEFINITION = null;
    private int checkMeta;
    private static final Class<?> cofhWater;

    static {
        cofhWater = ReflectionUtil.getClass("cofh.asmhooks.block.BlockWater");
    }

    public MTEAlgaePondBase(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEAlgaePondBase(final String aName) {
        super(aName);
    }

    @Override
    public boolean supportsPowerPanel() {
        return false;
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
            .addInfo("Provide compost to boost production by one tier")
            .addInfo("Does not require power or maintenance")
            .addInfo("All Machine Casings must be the same tier, this dictates machine speed.")
            .addInfo("Requires one Input Hatch that matches the tier of the Casings")
            .addInfo("Fill Input Hatch with Water to fill the inside of the multiblock.")
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(9, 3, 9, true)
            .addController("Front Center")
            .addCasingInfoMin("Machine Casings", 64, true)
            .addCasingInfoMin("Sterile Farm Casings", 64, false)
            .addInputBus("Any Casing", 1)
            .addOutputBus("Any Casing", 1)
            .addInputHatch("Any Casing", 1)
            .toolTipFinisher();
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
                                GregTechAPI.sBlockCasingsNH,
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
        return survivalBuildPiece(mName, stackSize, 4, 2, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        mLevel = 0;
        checkMeta = 0;

        if (checkPiece(mName, 4, 2, 0) && mCasing >= 64
            && checkMeta > 0
            && !mInputHatches.isEmpty()
            && !mOutputBusses.isEmpty()) {
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
    protected IIconContainer getActiveGlowOverlay() {
        return TexturesGtBlock.oMCDAlgaePondBaseActiveGlow;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.oMCDAlgaePondBase;
    }

    @Override
    protected IIconContainer getInactiveGlowOverlay() {
        return TexturesGtBlock.oMCDAlgaePondBaseGlow;
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
                    int tMeta = aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j);
                    if (isNotStaticWater(tBlock, tMeta)) {
                        if (this.getStoredFluids() != null) {
                            for (FluidStack stored : this.getStoredFluids()) {
                                if (stored.isFluidEqual(Materials.Water.getFluid(1))) {
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
                    if (tBlock == Blocks.water || tBlock == Blocks.flowing_water
                        || tBlock == BlocksItems.getFluidBlock(InternalName.fluidDistilledWater)) {
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

    private boolean isNotStaticWater(Block block, int meta) {
        return block == Blocks.air || block == Blocks.flowing_water
            || (cofhWater != null && cofhWater.isAssignableFrom(block.getClass()) && meta != 0);
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiAlgaePond;
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
                return GTStreamUtil.ofNullable(getTieredRecipe(mLevel, inputItems));
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
            .setMaxParallelSupplier(this::getTrueParallel);
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
                || aInitStructureCheck == GregTechAPI.sBlockCasingsNH) {
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

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_ALGAE_LOOP;
    }

    private static GTRecipe getTieredRecipe(int aTier, ItemStack[] aItemInputs) {
        return generateBaseRecipe(aTier, isUsingCompost(aItemInputs, aTier));
    }

    private static boolean isUsingCompost(ItemStack[] aItemInputs, int aTier) {
        ItemStack aCompost = GregtechItemList.Compost.get(1);
        final int compostForTier = compostForTier(aTier);
        int compostFound = 0;
        for (ItemStack i : aItemInputs) {
            if (GTUtility.areStacksEqual(aCompost, i)) {
                compostFound += i.stackSize;
                if (compostFound >= compostForTier) {
                    return true;
                }
            }
        }
        return false;
    }

    private static int compostForTier(int aTier) {
        return aTier > 1 ? (int) Math.min(64, GTUtility.powInt(2, aTier - 1)) : 1;
    }

    private static GTRecipe generateBaseRecipe(int aTier, boolean isUsingCompost) {

        if (aTier < 0) return null; // Type Safety

        final ItemStack[] aInputs;
        if (isUsingCompost) {
            // Make it use 4 compost per tier if we have some available
            // Compost consumption maxes out at 1 stack per cycle
            ItemStack aCompost = GregtechItemList.Compost.get(compostForTier(aTier));
            aInputs = new ItemStack[] { aCompost };
            // Boost Tier by one if using compost, so it gets a speed boost
            aTier++;
        } else {
            aInputs = GTValues.emptyItemStackArray;
        }

        ItemStack[] aOutputs = getOutputsForTier(aTier);
        GTRecipe tRecipe = new GTRecipe(
            false,
            aInputs,
            aOutputs,
            null,
            GTValues.emptyIntArray,
            new FluidStack[] { GTValues.NF },
            new FluidStack[] { GTValues.NF },
            getRecipeDuration(aTier),
            0,
            0);
        tRecipe.mSpecialValue = tRecipe.hashCode();
        return tRecipe;
    }

    private static final int[] aDurations = new int[] { 2000, 1800, 1600, 1400, 1200, 1000, 512, 256, 128, 64, 32, 16,
        8, 4, 2, 1 };

    private static int getRecipeDuration(int aTier) {
        final float randFloat = GTPPCore.RANDOM.nextFloat();
        float randMult;
        if (randFloat < 0.96237624) randMult = 1f;
        else if (randFloat < 0.9912871) randMult = 2f;
        else randMult = 3f;
        return (int) (aDurations[aTier] * randMult / 2);
    }

    private static ItemStack[] getOutputsForTier(int aTier) {
        ArrayList<ItemStack> outputList = new ArrayList<>();

        if (aTier >= 0) {
            outputList.add(GregtechItemList.AlgaeBiomass.get(2));
            outputList.add(GregtechItemList.AlgaeBiomass.get(4));
            if (MathUtils.randInt(0, 10) > 9) {
                outputList.add(GregtechItemList.GreenAlgaeBiomass.get(2));
            }
        }
        if (aTier >= 1) {
            outputList.add(GregtechItemList.AlgaeBiomass.get(4));
            outputList.add(GregtechItemList.GreenAlgaeBiomass.get(2));
            if (MathUtils.randInt(0, 10) > 9) {
                outputList.add(GregtechItemList.GreenAlgaeBiomass.get(4));
            }
        }
        if (aTier >= 2) {
            outputList.add(GregtechItemList.GreenAlgaeBiomass.get(2));
            outputList.add(GregtechItemList.GreenAlgaeBiomass.get(3));
            if (MathUtils.randInt(0, 10) > 9) {
                outputList.add(GregtechItemList.GreenAlgaeBiomass.get(8));
            }
        }
        if (aTier >= 3) {
            outputList.add(GregtechItemList.GreenAlgaeBiomass.get(4));
            outputList.add(GregtechItemList.BrownAlgaeBiomass.get(1));
            if (MathUtils.randInt(0, 10) > 9) {
                outputList.add(GregtechItemList.BrownAlgaeBiomass.get(4));
            }
        }
        if (aTier >= 4) {
            outputList.add(GregtechItemList.BrownAlgaeBiomass.get(2));
            outputList.add(GregtechItemList.BrownAlgaeBiomass.get(3));
            if (MathUtils.randInt(0, 10) > 9) {
                outputList.add(GregtechItemList.GoldenBrownAlgaeBiomass.get(4));
            }
        }
        if (aTier >= 5) {
            outputList.add(GregtechItemList.BrownAlgaeBiomass.get(4));
            outputList.add(GregtechItemList.GoldenBrownAlgaeBiomass.get(2));
            if (MathUtils.randInt(0, 10) > 9) {
                outputList.add(GregtechItemList.RedAlgaeBiomass.get(4));
            }
        }
        if (aTier >= 6) {
            outputList.add(GregtechItemList.GoldenBrownAlgaeBiomass.get(4));
            outputList.add(GregtechItemList.RedAlgaeBiomass.get(2));
            if (MathUtils.randInt(0, 10) > 9) {
                outputList.add(GregtechItemList.RedAlgaeBiomass.get(8));
            }
            // Iterate a special loop at higher tiers to provide more Red/Gold Algae.
            for (int i = 0; i < 20; i++) {
                if (aTier >= (6 + i)) {
                    int aMulti = i + 1;
                    outputList.add(GregtechItemList.GreenAlgaeBiomass.get(aMulti * 4));
                    outputList.add(GregtechItemList.BrownAlgaeBiomass.get(aMulti * 3));
                    outputList.add(GregtechItemList.GoldenBrownAlgaeBiomass.get(aMulti * 2));
                    outputList.add(GregtechItemList.RedAlgaeBiomass.get(aMulti));
                } else {
                    break;
                }
            }
        }

        return outputList.toArray(new ItemStack[0]);
    }
}

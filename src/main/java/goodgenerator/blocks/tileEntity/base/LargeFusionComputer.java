package goodgenerator.blocks.tileEntity.base;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GT_StructureUtility.ofFrame;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyMulti;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.enums.GT_HatchElement;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.objects.GT_ChunkManager;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_ParallelHelper;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.power.FusionPower;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_Output_ME;

public abstract class LargeFusionComputer extends GT_MetaTileEntity_TooltipMultiBlockBase_EM
        implements IConstructable, ISurvivalConstructable {

    public static final String MAIN_NAME = "largeFusion";
    private boolean isLoadedChunk;
    public GT_Recipe mLastRecipe;
    public int para;
    protected FusionPower power;
    private static final ClassValue<IStructureDefinition<LargeFusionComputer>> STRUCTURE_DEFINITION = new ClassValue<IStructureDefinition<LargeFusionComputer>>() {

        @Override
        protected IStructureDefinition<LargeFusionComputer> computeValue(Class<?> type) {
            return StructureDefinition.<LargeFusionComputer>builder()
                    .addShape(MAIN_NAME, transpose(new String[][] { L0, L1, L2, L3, L2, L1, L0 }))
                    .addElement('H', lazy(x -> ofBlock(x.getCoilBlock(), x.getCoilMeta())))
                    .addElement('C', lazy(x -> ofBlock(x.getCasingBlock(), x.getCasingMeta())))
                    .addElement('B', lazy(x -> ofBlock(x.getGlassBlock(), x.getGlassMeta())))
                    .addElement(
                            'I',
                            lazy(
                                    x -> GT_HatchElementBuilder.<LargeFusionComputer>builder()
                                            .atLeast(GT_HatchElement.InputHatch, GT_HatchElement.OutputHatch)
                                            .adder(LargeFusionComputer::addFluidIO).casingIndex(x.textureIndex()).dot(1)
                                            .buildAndChain(x.getGlassBlock(), x.getGlassMeta())))
                    .addElement(
                            'E',
                            lazy(
                                    x -> GT_HatchElementBuilder.<LargeFusionComputer>builder()
                                            .atLeast(HatchElement.EnergyMulti.or(GT_HatchElement.Energy))
                                            .adder(LargeFusionComputer::addEnergyInjector).casingIndex(x.textureIndex())
                                            .dot(2).buildAndChain(x.getCasingBlock(), x.getCasingMeta())))
                    .addElement('F', lazy(x -> ofFrame(x.getFrameBox()))).build();
        }
    };

    static {
        Textures.BlockIcons.setCasingTextureForId(
                52,
                TextureFactory.of(
                        TextureFactory.builder().addIcon(MACHINE_CASING_FUSION_GLASS_YELLOW).extFacing().build(),
                        TextureFactory.builder().addIcon(MACHINE_CASING_FUSION_GLASS_YELLOW_GLOW).extFacing().glow()
                                .build()));
    }

    public LargeFusionComputer(String name) {
        super(name);
        useLongPower = true;
    }

    public LargeFusionComputer(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
        useLongPower = true;
    }

    @Override
    public FusionPower getPower() {
        return power;
    }

    @Override
    public abstract long maxEUStore();

    public abstract Block getCasingBlock();

    public abstract int getCasingMeta();

    public abstract Block getCoilBlock();

    public abstract int getCoilMeta();

    public abstract Block getGlassBlock();

    public abstract int getGlassMeta();

    public abstract int hatchTier();

    public abstract Materials getFrameBox();

    public abstract int getMaxPara();

    public abstract int extraPara(int startEnergy);

    public int textureIndex() {
        return 53;
    }

    public abstract ITexture getTextureOverlay();

    @Override
    public boolean allowCoverOnSide(ForgeDirection side, GT_ItemStack aStack) {
        return side != getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        // Migration code
        if (lEUt > 0) {
            lEUt = -lEUt;
        }
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        this.eEnergyMulti.clear();
        if (structureCheck_EM(MAIN_NAME, 23, 3, 40) && !mInputHatches.isEmpty()
                && !mOutputHatches.isEmpty()
                && (mEnergyHatches.size() + eEnergyMulti.size()) != 0) {
            fixAllIssue();
            return true;
        }
        return false;
    }

    public void fixAllIssue() {
        mWrench = true;
        mScrewdriver = true;
        mSoftHammer = true;
        mHardHammer = true;
        mSolderingTool = true;
        mCrowbar = true;
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        structureBuild_EM(MAIN_NAME, 23, 3, 40, itemStack, b);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (mMachine) {
            return -1;
        } else {
            return survivialBuildPiece(
                    MAIN_NAME,
                    stackSize,
                    23,
                    3,
                    40,
                    elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5),
                    source,
                    actor,
                    false,
                    true);
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide() && !aBaseMetaTileEntity.isAllowedToWork()) {
            // if machine has stopped, stop chunkloading
            GT_ChunkManager.releaseTicket((TileEntity) aBaseMetaTileEntity);
            this.isLoadedChunk = false;
        } else if (aBaseMetaTileEntity.isServerSide() && aBaseMetaTileEntity.isAllowedToWork() && !this.isLoadedChunk) {
            // load a 3x3 area when machine is running
            GT_ChunkManager.releaseTicket((TileEntity) aBaseMetaTileEntity);
            int offX = aBaseMetaTileEntity.getFrontFacing().offsetX;
            int offZ = aBaseMetaTileEntity.getFrontFacing().offsetZ;
            GT_ChunkManager.requestChunkLoad(
                    (TileEntity) aBaseMetaTileEntity,
                    new ChunkCoordIntPair(getChunkX() + offX, getChunkZ() + offZ));
            GT_ChunkManager.requestChunkLoad(
                    (TileEntity) aBaseMetaTileEntity,
                    new ChunkCoordIntPair(getChunkX() + 1 + offX, getChunkZ() + 1 + offZ));
            GT_ChunkManager.requestChunkLoad(
                    (TileEntity) aBaseMetaTileEntity,
                    new ChunkCoordIntPair(getChunkX() + 1 + offX, getChunkZ() + offZ));
            GT_ChunkManager.requestChunkLoad(
                    (TileEntity) aBaseMetaTileEntity,
                    new ChunkCoordIntPair(getChunkX() + 1 + offX, getChunkZ() - 1 + offZ));
            GT_ChunkManager.requestChunkLoad(
                    (TileEntity) aBaseMetaTileEntity,
                    new ChunkCoordIntPair(getChunkX() - 1 + offX, getChunkZ() + 1 + offZ));
            GT_ChunkManager.requestChunkLoad(
                    (TileEntity) aBaseMetaTileEntity,
                    new ChunkCoordIntPair(getChunkX() - 1 + offX, getChunkZ() + offZ));
            GT_ChunkManager.requestChunkLoad(
                    (TileEntity) aBaseMetaTileEntity,
                    new ChunkCoordIntPair(getChunkX() - 1 + offX, getChunkZ() - 1 + offZ));
            GT_ChunkManager.requestChunkLoad(
                    (TileEntity) aBaseMetaTileEntity,
                    new ChunkCoordIntPair(getChunkX() + offX, getChunkZ() + 1 + offZ));
            GT_ChunkManager.requestChunkLoad(
                    (TileEntity) aBaseMetaTileEntity,
                    new ChunkCoordIntPair(getChunkX() + offX, getChunkZ() - 1 + offZ));
            this.isLoadedChunk = true;
        }

        if (aBaseMetaTileEntity.isServerSide()) {
            if (aTick % 400 == 0) fixAllIssue();
            if (mEfficiency < 0) mEfficiency = 0;
            if (mRunningOnLoad && checkMachine(aBaseMetaTileEntity, mInventory[1])) {
                checkRecipe();
            }
            if (mUpdated) {
                mUpdate = 50;
                mUpdated = false;
            }
            if (--mUpdate == 0 || --mStartUpCheck == 0
                    || cyclicUpdate_EM()
                    || aBaseMetaTileEntity.hasWorkJustBeenEnabled()) {
                if (mUpdate <= -1000) {
                    mUpdate = 5000;
                }
                checkStructure(true, aBaseMetaTileEntity);
            }
            if (mStartUpCheck < 0) {
                if (mMachine) {
                    if (this.mEnergyHatches != null) {
                        for (GT_MetaTileEntity_Hatch_Energy tHatch : mEnergyHatches)
                            if (isValidMetaTileEntity(tHatch)) {
                                if (aBaseMetaTileEntity.getStoredEU()
                                        + (2048L * tierOverclock() * getMaxPara() * extraPara(100)) < maxEUStore()
                                        && tHatch.getBaseMetaTileEntity().decreaseStoredEnergyUnits(
                                                2048L * tierOverclock() * getMaxPara() * extraPara(100),
                                                false)) {
                                    aBaseMetaTileEntity.increaseStoredEnergyUnits(
                                            2048L * tierOverclock() * getMaxPara() * extraPara(100),
                                            true);
                                } else if (aBaseMetaTileEntity.getStoredEU() + (2048L * tierOverclock()) < maxEUStore()
                                        && tHatch.getBaseMetaTileEntity()
                                                .decreaseStoredEnergyUnits(2048L * tierOverclock(), false)) {
                                                    aBaseMetaTileEntity
                                                            .increaseStoredEnergyUnits(2048L * tierOverclock(), true);
                                                }
                            }
                    }
                    if (this.eEnergyMulti != null) {
                        for (GT_MetaTileEntity_Hatch_EnergyMulti tHatch : eEnergyMulti)
                            if (isValidMetaTileEntity(tHatch)) {
                                if (aBaseMetaTileEntity.getStoredEU()
                                        + (2048L * tierOverclock() * getMaxPara() * extraPara(100)) < maxEUStore()
                                        && tHatch.getBaseMetaTileEntity().decreaseStoredEnergyUnits(
                                                2048L * tierOverclock() * getMaxPara() * extraPara(100),
                                                false)) {
                                    aBaseMetaTileEntity.increaseStoredEnergyUnits(
                                            2048L * tierOverclock() * getMaxPara() * extraPara(100),
                                            true);
                                } else if (aBaseMetaTileEntity.getStoredEU() + (2048L * tierOverclock()) < maxEUStore()
                                        && tHatch.getBaseMetaTileEntity()
                                                .decreaseStoredEnergyUnits(2048L * tierOverclock(), false)) {
                                                    aBaseMetaTileEntity
                                                            .increaseStoredEnergyUnits(2048L * tierOverclock(), true);
                                                }
                            }
                    }
                    if (aBaseMetaTileEntity.getStoredEU() <= 0 && mMaxProgresstime > 0) {
                        stopMachine();
                    }
                    if (mMaxProgresstime > 0) {
                        this.getBaseMetaTileEntity().decreaseStoredEnergyUnits(-lEUt, true);
                        if (mMaxProgresstime > 0 && ++mProgresstime >= mMaxProgresstime) {
                            if (mOutputItems != null)
                                for (ItemStack tStack : mOutputItems) if (tStack != null) addOutput(tStack);
                            if (mOutputFluids != null)
                                for (FluidStack tStack : mOutputFluids) if (tStack != null) addOutput(tStack);
                            mEfficiency = Math.max(
                                    0,
                                    Math.min(mEfficiency + mEfficiencyIncrease, getMaxEfficiency(mInventory[1])));
                            mOutputItems = null;
                            mProgresstime = 0;
                            mMaxProgresstime = 0;
                            mEfficiencyIncrease = 0;
                            if (aBaseMetaTileEntity.isAllowedToWork()) checkRecipe();
                        }
                    } else {
                        if (aTick % 100 == 0 || aBaseMetaTileEntity.hasWorkJustBeenEnabled()
                                || aBaseMetaTileEntity.hasInventoryBeenModified()) {
                            turnCasingActive(mMaxProgresstime > 0);
                            if (aBaseMetaTileEntity.isAllowedToWork()) {
                                if (checkRecipe()) {
                                    if (aBaseMetaTileEntity.getStoredEU()
                                            < this.mLastRecipe.mSpecialValue + this.lEUt) {
                                        mMaxProgresstime = 0;
                                        turnCasingActive(false);
                                    }
                                    getBaseMetaTileEntity().decreaseStoredEnergyUnits(
                                            this.mLastRecipe.mSpecialValue + this.lEUt,
                                            false);
                                }
                            }
                            if (mMaxProgresstime <= 0) mEfficiency = Math.max(0, mEfficiency - 1000);
                        }
                    }
                } else {
                    turnCasingActive(false);
                    this.mLastRecipe = null;
                    stopMachine();
                }
            }
            aBaseMetaTileEntity
                    .setErrorDisplayID((aBaseMetaTileEntity.getErrorDisplayID() & ~127) | (mMachine ? 0 : 64));
            aBaseMetaTileEntity.setActive(mMaxProgresstime > 0);
        } else {
            soundMagic(getActivitySound());
        }
    }

    public boolean turnCasingActive(boolean status) {
        if (this.mEnergyHatches != null) {
            for (GT_MetaTileEntity_Hatch_Energy hatch : this.mEnergyHatches) {
                hatch.updateTexture(status ? 52 : 53);
            }
        }
        if (this.eEnergyMulti != null) {
            for (GT_MetaTileEntity_Hatch_EnergyMulti hatch : this.eEnergyMulti) {
                hatch.updateTexture(status ? 52 : 53);
            }
        }
        if (this.mOutputHatches != null) {
            for (GT_MetaTileEntity_Hatch_Output hatch : this.mOutputHatches) {
                hatch.updateTexture(status ? 52 : 53);
            }
        }
        if (this.mInputHatches != null) {
            for (GT_MetaTileEntity_Hatch_Input hatch : this.mInputHatches) {
                hatch.updateTexture(status ? 52 : 53);
            }
        }
        return true;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
            int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing)
            return new ITexture[] { TextureFactory.builder().addIcon(MACHINE_CASING_FUSION_GLASS).extFacing().build(),
                    getTextureOverlay() };
        if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(52) };
        return new ITexture[] { TextureFactory.builder().addIcon(MACHINE_CASING_FUSION_GLASS).extFacing().build() };
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public void onMachineBlockUpdate() {
        mUpdate = 100;
    }

    public abstract int tierOverclock();

    public int overclock(int mStartEnergy) {
        if (tierOverclock() == 1) {
            return 0;
        }
        if (tierOverclock() == 2) {
            return mStartEnergy <= 160000000 ? 1 : 0;
        }
        if (tierOverclock() == 4) {
            return (mStartEnergy <= 160000000 ? 2 : (mStartEnergy <= 320000000 ? 1 : 0));
        }
        if (tierOverclock() == 8) {
            return (mStartEnergy <= 160000000) ? 3
                    : ((mStartEnergy <= 320000000) ? 2 : (mStartEnergy <= 640000000) ? 1 : 0);
        }
        return (mStartEnergy <= 160000000) ? 4
                : ((mStartEnergy <= 320000000) ? 3
                        : ((mStartEnergy <= 640000000) ? 2 : (mStartEnergy <= 1280000000) ? 1 : 0));
    }

    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return GT_Recipe.GT_Recipe_Map.sFusionRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected GT_ParallelHelper createParallelHelper(@NotNull GT_Recipe recipe) {
                // When the fusion first loads and is still processing, it does the recipe check without consuming.
                if (mRunningOnLoad) {
                    return new GT_ParallelHelper().setRecipe(recipe).setItemInputs(inputItems)
                            .setFluidInputs(inputFluids).setAvailableEUt(availableVoltage * availableAmperage)
                            .setMachine(machine, protectItems, protectFluids)
                            .setRecipeLocked(recipeLockableMachine, isRecipeLocked).setMaxParallel(maxParallel)
                            .enableBatchMode(batchSize).enableOutputCalculation();
                }
                return super.createParallelHelper(recipe);
            }

            @NotNull
            @Override
            protected GT_OverclockCalculator createOverclockCalculator(@NotNull GT_Recipe recipe,
                    @NotNull GT_ParallelHelper helper) {
                return super.createOverclockCalculator(recipe, helper)
                        .limitOverclockCount(overclock(recipe.mSpecialValue));
            }

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GT_Recipe recipe) {
                if (!mRunningOnLoad && recipe.mSpecialValue > maxEUStore()) {
                    return CheckRecipeResultRegistry.insufficientStartupPower(recipe.mSpecialValue);
                }
                maxParallel = getMaxPara() * extraPara(recipe.mSpecialValue);
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @NotNull
            @Override
            public CheckRecipeResult process() {
                CheckRecipeResult result = super.process();
                if (mRunningOnLoad) mRunningOnLoad = false;
                turnCasingActive(result.wasSuccessful());
                if (result.wasSuccessful()) {
                    mLastRecipe = lastRecipe;
                } else {
                    mLastRecipe = null;
                }
                return result;
            }
        }.setOverclock(1, 1);
    }

    @Override
    public void onRemoval() {
        if (this.isLoadedChunk) GT_ChunkManager.releaseTicket((TileEntity) getBaseMetaTileEntity());
        super.onRemoval();
    }

    public int getChunkX() {
        return getBaseMetaTileEntity().getXCoord() >> 4;
    }

    public int getChunkZ() {
        return getBaseMetaTileEntity().getZCoord() >> 4;
    }

    private boolean addEnergyInjector(IGregTechTileEntity aBaseMetaTileEntity, int aBaseCasingIndex) {
        IMetaTileEntity aMetaTileEntity = aBaseMetaTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy) {
            GT_MetaTileEntity_Hatch_Energy tHatch = (GT_MetaTileEntity_Hatch_Energy) aMetaTileEntity;
            if (tHatch.mTier < hatchTier()) return false;
            tHatch.updateTexture(aBaseCasingIndex);
            return mEnergyHatches.add(tHatch);
        } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_EnergyMulti) {
            GT_MetaTileEntity_Hatch_EnergyMulti tHatch = (GT_MetaTileEntity_Hatch_EnergyMulti) aMetaTileEntity;
            if (tHatch.mTier < hatchTier()) return false;
            tHatch.updateTexture(aBaseCasingIndex);
            return eEnergyMulti.add(tHatch);
        }
        return false;
    }

    private boolean addFluidIO(IGregTechTileEntity aBaseMetaTileEntity, int aBaseCasingIndex) {
        IMetaTileEntity aMetaTileEntity = aBaseMetaTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch) {
            GT_MetaTileEntity_Hatch tHatch = (GT_MetaTileEntity_Hatch) aMetaTileEntity;
            if (tHatch.mTier < hatchTier() && !(tHatch instanceof GT_MetaTileEntity_Hatch_Output_ME)) return false;
        } else {
            return false;
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
            GT_MetaTileEntity_Hatch_Input tInput = (GT_MetaTileEntity_Hatch_Input) aMetaTileEntity;
            tInput.updateTexture(aBaseCasingIndex);
            tInput.mRecipeMap = getRecipeMap();
            return mInputHatches.add(tInput);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output) {
            GT_MetaTileEntity_Hatch_Output tOutput = (GT_MetaTileEntity_Hatch_Output) aMetaTileEntity;
            tOutput.updateTexture(aBaseCasingIndex);
            return mOutputHatches.add(tOutput);
        }
        return false;
    }

    @Override
    public IStructureDefinition<LargeFusionComputer> getStructure_EM() {
        return STRUCTURE_DEFINITION.get(getClass());
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    @Override
    protected ResourceLocation getActivitySound() {
        return SoundResource.GT_MACHINES_FUSION_LOOP.resourceLocation;
    }

    @Override
    public String[] getInfoData() {
        IGregTechTileEntity baseMetaTileEntity = getBaseMetaTileEntity();
        String tier = switch (hatchTier()) {
            case 6 -> EnumChatFormatting.RED + "I" + EnumChatFormatting.RESET;
            case 7 -> EnumChatFormatting.RED + "II" + EnumChatFormatting.RESET;
            case 8 -> EnumChatFormatting.RED + "III" + EnumChatFormatting.RESET;
            case 9 -> EnumChatFormatting.RED + "IV" + EnumChatFormatting.RESET;
            default -> EnumChatFormatting.GOLD + "V" + EnumChatFormatting.RESET;
        };
        float plasmaOut = 0;
        int powerRequired = 0;
        if (this.mLastRecipe != null) {
            powerRequired = this.mLastRecipe.mEUt * this.para * overclock(this.mLastRecipe.mSpecialValue);
            if (this.mLastRecipe.getFluidOutput(0) != null) {
                plasmaOut = (float) this.mLastRecipe.getFluidOutput(0).amount / (float) this.mLastRecipe.mDuration
                        * this.para;
            }
        }

        return new String[] { EnumChatFormatting.BLUE + "Fusion Reactor MK " + EnumChatFormatting.RESET + tier,
                StatCollector.translateToLocal("scanner.info.UX.0") + ": "
                        + EnumChatFormatting.LIGHT_PURPLE
                        + GT_Utility.formatNumbers(this.para)
                        + EnumChatFormatting.RESET,
                StatCollector.translateToLocal("GT5U.fusion.req") + ": "
                        + EnumChatFormatting.RED
                        + GT_Utility.formatNumbers(powerRequired)
                        + EnumChatFormatting.RESET
                        + "EU/t",
                StatCollector.translateToLocal("GT5U.multiblock.energy") + ": "
                        + EnumChatFormatting.GREEN
                        + GT_Utility.formatNumbers(baseMetaTileEntity != null ? baseMetaTileEntity.getStoredEU() : 0)
                        + EnumChatFormatting.RESET
                        + " EU / "
                        + EnumChatFormatting.YELLOW
                        + GT_Utility.formatNumbers(maxEUStore())
                        + EnumChatFormatting.RESET
                        + " EU",
                StatCollector.translateToLocal("GT5U.fusion.plasma") + ": "
                        + EnumChatFormatting.YELLOW
                        + GT_Utility.formatNumbers(plasmaOut)
                        + EnumChatFormatting.RESET
                        + "L/t" };
    }

    protected long energyStorageCache;

    @Override
    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        super.drawTexts(screenElements, inventorySlot);

        screenElements
                .widget(
                        TextWidget
                                .dynamicString(
                                        () -> StatCollector.translateToLocal("gui.LargeFusion.0") + " "
                                                + GT_Utility.formatNumbers(energyStorageCache)
                                                + " EU")
                                .setSynced(false).setDefaultColor(COLOR_TEXT_WHITE.get())
                                .setEnabled(widget -> getBaseMetaTileEntity().getErrorDisplayID() == 0))
                .widget(new FakeSyncWidget.LongSyncer(this::maxEUStore, val -> energyStorageCache = val))
                .widget(
                        TextWidget
                                .dynamicString(
                                        () -> StatCollector.translateToLocal("gui.LargeFusion.1") + " "
                                                + GT_Utility.formatNumbers(getEUVar())
                                                + " EU")
                                .setSynced(false).setDefaultColor(COLOR_TEXT_WHITE.get())
                                .setEnabled(widget -> getBaseMetaTileEntity().getErrorDisplayID() == 0))
                .widget(new FakeSyncWidget.LongSyncer(this::getEUVar, this::setEUVar));
    }

    public static final String[] L0 = { "                                               ",
            "                                               ", "                    FCCCCCF                    ",
            "                    FCIBICF                    ", "                    FCCCCCF                    ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "  FFF                                     FFF  ",
            "  CCC                                     CCC  ", "  CIC                                     CIC  ",
            "  CBC                                     CBC  ", "  CIC                                     CIC  ",
            "  CCC                                     CCC  ", "  FFF                                     FFF  ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                                               ",
            "                                               ", "                    FCCCCCF                    ",
            "                    FCIBICF                    ", "                    FCCCCCF                    ",
            "                                               ", "                                               ", };

    public static final String[] L1 = { "                                               ",
            "                    FCBBBCF                    ", "                   CC     CC                   ",
            "                CCCCC     CCCCC                ", "              CCCCCCC     CCCCCCC              ",
            "            CCCCCCC FCBBBCF CCCCCCC            ", "           CCCCC               CCCCC           ",
            "          CCCC                   CCCC          ", "         CCC                       CCC         ",
            "        CCC                         CCC        ", "       CCC                           CCC       ",
            "      CCC                             CCC      ", "     CCC                               CCC     ",
            "     CCC                               CCC     ", "    CCC                                 CCC    ",
            "    CCC                                 CCC    ", "   CCC                                   CCC   ",
            "   CCC                                   CCC   ", "   CCC                                   CCC   ",
            "  CCC                                     CCC  ", " FCCCF                                   FCCCF ",
            " C   C                                   C   C ", " B   B                                   B   B ",
            " B   B                                   B   B ", " B   B                                   B   B ",
            " C   C                                   C   C ", " FCCCF                                   FCCCF ",
            "  CCC                                     CCC  ", "   CCC                                   CCC   ",
            "   CCC                                   CCC   ", "   CCC                                   CCC   ",
            "    CCC                                 CCC    ", "    CCC                                 CCC    ",
            "     CCC                               CCC     ", "     CCC                               CCC     ",
            "      CCC                             CCC      ", "       CCC                           CCC       ",
            "        CCC                         CCC        ", "         CCC                       CCC         ",
            "          CCCC                   CCCC          ", "           CCCCC               CCCCC           ",
            "            CCCCCCC FCBBBCF CCCCCCC            ", "              CCCCCCC     CCCCCCC              ",
            "                CCCCC     CCCCC                ", "                   CC     CC                   ",
            "                    FCBBBCF                    ", "                                               ", };

    public static final String[] L2 = { "                    FCCCCCF                    ",
            "                   CC     CC                   ", "                CCCCC     CCCCC                ",
            "              CCCCCHHHHHHHHHCCCCC              ", "            CCCCHHHCC     CCHHHCCCC            ",
            "           CCCHHCCCCC     CCCCCHHCCC           ", "          ECHHCCCCC FCCCCCF CCCCCHHCE          ",
            "         CCHCCCC               CCCCHCC         ", "        CCHCCC                   CCCHCC        ",
            "       CCHCE                       ECHCC       ", "      ECHCC                         CCHCE      ",
            "     CCHCE                           ECHCC     ", "    CCHCC                             CCHCC    ",
            "    CCHCC                             CCHCC    ", "   CCHCC                               CCHCC   ",
            "   CCHCC                               CCHCC   ", "  CCHCC                                 CCHCC  ",
            "  CCHCC                                 CCHCC  ", "  CCHCC                                 CCHCC  ",
            " CCHCC                                   CCHCC ", "FCCHCCF                                 FCCHCCF",
            "C  H  C                                 C  H  C", "C  H  C                                 C  H  C",
            "C  H  C                                 C  H  C", "C  H  C                                 C  H  C",
            "C  H  C                                 C  H  C", "FCCHCCF                                 FCCHCCF",
            " CCHCC                                   CCHCC ", "  CCHCC                                 CCHCC  ",
            "  CCHCC                                 CCHCC  ", "  CCHCC                                 CCHCC  ",
            "   CCHCC                               CCHCC   ", "   CCHCC                               CCHCC   ",
            "    CCHCC                             CCHCC    ", "    CCHCC                             CCHCC    ",
            "     CCHCE                           ECHCC     ", "      ECHCC                         CCHCE      ",
            "       CCHCE                       ECHCC       ", "        CCHCCC                   CCCHCC        ",
            "         CCHCCCC               CCCCHCC         ", "          ECHHCCCCC FCCCCCF CCCCCHHCE          ",
            "           CCCHHCCCCC     CCCCCHHCCC           ", "            CCCCHHHCC     CCHHHCCCC            ",
            "              CCCCCHHHHHHHHHCCCCC              ", "                CCCCC     CCCCC                ",
            "                   CC     CC                   ", "                    FCCCCCF                    ", };

    public static final String[] L3 = { "                    FCIBICF                    ",
            "                   CC     CC                   ", "                CCCHHHHHHHHHCCC                ",
            "              CCHHHHHHHHHHHHHHHCC              ", "            CCHHHHHHHHHHHHHHHHHHHCC            ",
            "           CHHHHHHHCC     CCHHHHHHHC           ", "          CHHHHHCCC FCIBICF CCCHHHHHC          ",
            "         CHHHHCC               CCHHHHC         ", "        CHHHCC                   CCHHHC        ",
            "       CHHHC                       CHHHC       ", "      CHHHC                         CHHHC      ",
            "     CHHHC                           CHHHC     ", "    CHHHC                             CHHHC    ",
            "    CHHHC                             CHHHC    ", "   CHHHC                               CHHHC   ",
            "   CHHHC                               CHHHC   ", "  CHHHC                                 CHHHC  ",
            "  CHHHC                                 CHHHC  ", "  CHHHC                                 CHHHC  ",
            " CHHHC                                   CHHHC ", "FCHHHCF                                 FCHHHCF",
            "C HHH C                                 C HHH C", "I HHH I                                 I HHH I",
            "B HHH B                                 B HHH B", "I HHH I                                 I HHH I",
            "C HHH C                                 C HHH C", "FCHHHCF                                 FCHHHCF",
            " CHHHC                                   CHHHC ", "  CHHHC                                 CHHHC  ",
            "  CHHHC                                 CHHHC  ", "  CHHHC                                 CHHHC  ",
            "   CHHHC                               CHHHC   ", "   CHHHC                               CHHHC   ",
            "    CHHHC                             CHHHC    ", "    CHHHC                             CHHHC    ",
            "     CHHHC                           CHHHC     ", "      CHHHC                         CHHHC      ",
            "       CHHHC                       CHHHC       ", "        CHHHCC                   CCHHHC        ",
            "         CHHHHCC               CCHHHHC         ", "          CHHHHHCCC FCI~ICF CCCHHHHHC          ",
            "           CHHHHHHHCC     CCHHHHHHHC           ", "            CCHHHHHHHHHHHHHHHHHHHCC            ",
            "              CCHHHHHHHHHHHHHHHCC              ", "                CCCHHHHHHHHHCCC                ",
            "                   CC     CC                   ", "                    FCIBICF                    ", };
}

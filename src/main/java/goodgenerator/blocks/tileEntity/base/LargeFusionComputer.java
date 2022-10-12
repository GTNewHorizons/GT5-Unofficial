package goodgenerator.blocks.tileEntity.base;

import static com.github.bartimaeusnek.bartworks.util.RecipeFinderForParallel.getMultiOutput;
import static com.github.bartimaeusnek.bartworks.util.RecipeFinderForParallel.handleParallelRecipe;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GT_StructureUtility.ofFrame;

import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyMulti;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import goodgenerator.client.GUI.LargeFusionComputerGUIClient;
import gregtech.api.enums.GT_HatchElement;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.objects.GT_ChunkManager;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public abstract class LargeFusionComputer extends GT_MetaTileEntity_TooltipMultiBlockBase_EM
        implements IConstructable, ISurvivalConstructable {

    public static final String MAIN_NAME = "largeFusion";
    private boolean isLoadedChunk;
    public GT_Recipe mLastRecipe;
    public int para;
    public int mEUStore;
    private static final ClassValue<IStructureDefinition<LargeFusionComputer>> STRUCTURE_DEFINITION =
            new ClassValue<IStructureDefinition<LargeFusionComputer>>() {
                @Override
                protected IStructureDefinition<LargeFusionComputer> computeValue(Class<?> type) {
                    return StructureDefinition.<LargeFusionComputer>builder()
                            .addShape(MAIN_NAME, transpose(new String[][] {L0, L1, L2, L3, L2, L1, L0}))
                            .addElement('H', lazy(x -> ofBlock(x.getCoilBlock(), x.getCoilMeta())))
                            .addElement('C', lazy(x -> ofBlock(x.getCasingBlock(), x.getCasingMeta())))
                            .addElement('B', lazy(x -> ofBlock(x.getGlassBlock(), x.getGlassMeta())))
                            .addElement('I', lazy(x -> GT_HatchElementBuilder.<LargeFusionComputer>builder()
                                    .atLeast(GT_HatchElement.InputHatch)
                                    .adder(LargeFusionComputer::addInjector)
                                    .casingIndex(x.textureIndex())
                                    .dot(1)
                                    .buildAndChain(x.getGlassBlock(), x.getGlassMeta())))
                            .addElement('O', lazy(x -> GT_HatchElementBuilder.<LargeFusionComputer>builder()
                                    .atLeast(GT_HatchElement.OutputHatch)
                                    .adder(LargeFusionComputer::addExtractor)
                                    .casingIndex(x.textureIndex())
                                    .dot(2)
                                    .buildAndChain(x.getGlassBlock(), x.getGlassMeta())))
                            .addElement('E', lazy(x -> GT_HatchElementBuilder.<LargeFusionComputer>builder()
                                    .atLeast(HatchElement.EnergyMulti.or(GT_HatchElement.Energy))
                                    .adder(LargeFusionComputer::addEnergyInjector)
                                    .casingIndex(x.textureIndex())
                                    .dot(3)
                                    .buildAndChain(x.getCasingBlock(), x.getCasingMeta())))
                            .addElement('F', lazy(x -> ofFrame(x.getFrameBox())))
                            .build();
                }
            };

    static {
        Textures.BlockIcons.setCasingTextureForId(
                52,
                TextureFactory.of(
                        TextureFactory.builder()
                                .addIcon(MACHINE_CASING_FUSION_GLASS_YELLOW)
                                .extFacing()
                                .build(),
                        TextureFactory.builder()
                                .addIcon(MACHINE_CASING_FUSION_GLASS_YELLOW_GLOW)
                                .extFacing()
                                .glow()
                                .build()));
    }

    public LargeFusionComputer(String name) {
        super(name);
    }

    public LargeFusionComputer(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new LargeFusionComputerGUIClient(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "EMDisplay.png");
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
    public boolean allowCoverOnSide(byte aSide, GT_ItemStack aStack) {
        return aSide != getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        this.eEnergyMulti.clear();
        if (structureCheck_EM(MAIN_NAME, 23, 3, 40)
                && mInputHatches.size() > 1
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
            int offX = ForgeDirection.getOrientation(aBaseMetaTileEntity.getFrontFacing()).offsetX;
            int offZ = ForgeDirection.getOrientation(aBaseMetaTileEntity.getFrontFacing()).offsetZ;
            GT_ChunkManager.requestChunkLoad(
                    (TileEntity) aBaseMetaTileEntity, new ChunkCoordIntPair(getChunkX() + offX, getChunkZ() + offZ));
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
                this.mEUStore = (int) aBaseMetaTileEntity.getStoredEU();
                checkRecipe(mInventory[1]);
            }
            if (mUpdated) {
                mUpdate = 50;
                mUpdated = false;
            }
            if (--mUpdate == 0
                    || --mStartUpCheck == 0
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
                                                        + (2048L * tierOverclock() * getMaxPara() * extraPara(100))
                                                < maxEUStore()
                                        && tHatch.getBaseMetaTileEntity()
                                                .decreaseStoredEnergyUnits(
                                                        2048L * tierOverclock() * getMaxPara() * extraPara(100),
                                                        false)) {
                                    aBaseMetaTileEntity.increaseStoredEnergyUnits(
                                            2048L * tierOverclock() * getMaxPara() * extraPara(100), true);
                                } else if (aBaseMetaTileEntity.getStoredEU() + (2048L * tierOverclock()) < maxEUStore()
                                        && tHatch.getBaseMetaTileEntity()
                                                .decreaseStoredEnergyUnits(2048L * tierOverclock(), false)) {
                                    aBaseMetaTileEntity.increaseStoredEnergyUnits(2048L * tierOverclock(), true);
                                }
                            }
                    }
                    if (this.eEnergyMulti != null) {
                        for (GT_MetaTileEntity_Hatch_EnergyMulti tHatch : eEnergyMulti)
                            if (isValidMetaTileEntity(tHatch)) {
                                if (aBaseMetaTileEntity.getStoredEU()
                                                        + (2048L * tierOverclock() * getMaxPara() * extraPara(100))
                                                < maxEUStore()
                                        && tHatch.getBaseMetaTileEntity()
                                                .decreaseStoredEnergyUnits(
                                                        2048L * tierOverclock() * getMaxPara() * extraPara(100),
                                                        false)) {
                                    aBaseMetaTileEntity.increaseStoredEnergyUnits(
                                            2048L * tierOverclock() * getMaxPara() * extraPara(100), true);
                                } else if (aBaseMetaTileEntity.getStoredEU() + (2048L * tierOverclock()) < maxEUStore()
                                        && tHatch.getBaseMetaTileEntity()
                                                .decreaseStoredEnergyUnits(2048L * tierOverclock(), false)) {
                                    aBaseMetaTileEntity.increaseStoredEnergyUnits(2048L * tierOverclock(), true);
                                }
                            }
                    }
                    if (this.mEUStore <= 0 && mMaxProgresstime > 0) {
                        stopMachine();
                    }
                    if (mMaxProgresstime > 0) {
                        this.getBaseMetaTileEntity().decreaseStoredEnergyUnits(mEUt, true);
                        if (mMaxProgresstime > 0 && ++mProgresstime >= mMaxProgresstime) {
                            if (mOutputItems != null)
                                for (ItemStack tStack : mOutputItems) if (tStack != null) addOutput(tStack);
                            if (mOutputFluids != null)
                                for (FluidStack tStack : mOutputFluids) if (tStack != null) addOutput(tStack);
                            mEfficiency = Math.max(
                                    0, Math.min(mEfficiency + mEfficiencyIncrease, getMaxEfficiency(mInventory[1])));
                            mOutputItems = null;
                            mProgresstime = 0;
                            mMaxProgresstime = 0;
                            mEfficiencyIncrease = 0;
                            this.mEUStore = (int) getBaseMetaTileEntity().getStoredEU();
                            if (aBaseMetaTileEntity.isAllowedToWork()) checkRecipe(mInventory[1]);
                        }
                    } else {
                        if (aTick % 100 == 0
                                || aBaseMetaTileEntity.hasWorkJustBeenEnabled()
                                || aBaseMetaTileEntity.hasInventoryBeenModified()) {
                            turnCasingActive(mMaxProgresstime > 0);
                            if (aBaseMetaTileEntity.isAllowedToWork()) {
                                this.mEUStore = (int) getBaseMetaTileEntity().getStoredEU();
                                if (checkRecipe(mInventory[1])) {
                                    if (this.mEUStore < this.mLastRecipe.mSpecialValue - this.mEUt) {
                                        mMaxProgresstime = 0;
                                        turnCasingActive(false);
                                    }
                                    getBaseMetaTileEntity()
                                            .decreaseStoredEnergyUnits(
                                                    this.mLastRecipe.mSpecialValue - this.mEUt, false);
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
            aBaseMetaTileEntity.setErrorDisplayID(
                    (aBaseMetaTileEntity.getErrorDisplayID() & ~127) | (mMachine ? 0 : 64));
            aBaseMetaTileEntity.setActive(mMaxProgresstime > 0);
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
    public ITexture[] getTexture(
            IGregTechTileEntity aBaseMetaTileEntity,
            byte aSide,
            byte aFacing,
            byte aColorIndex,
            boolean aActive,
            boolean aRedstone) {
        if (aSide == aFacing)
            return new ITexture[] {
                TextureFactory.builder()
                        .addIcon(MACHINE_CASING_FUSION_GLASS)
                        .extFacing()
                        .build(),
                getTextureOverlay()
            };
        if (aActive) return new ITexture[] {Textures.BlockIcons.getCasingTextureForId(52)};
        return new ITexture[] {
            TextureFactory.builder()
                    .addIcon(MACHINE_CASING_FUSION_GLASS)
                    .extFacing()
                    .build()
        };
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public void onMachineBlockUpdate() {
        mUpdate = 100;
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (mEUt < 0) {
            if (!drainEnergyInput(((long) -mEUt * 10000) / Math.max(1000, mEfficiency))) {
                this.mLastRecipe = null;
                this.para = 0;
                stopMachine();
                return false;
            }
        }
        if (this.mEUStore <= 0) {
            this.mLastRecipe = null;
            this.para = 0;
            stopMachine();
            return false;
        }
        return true;
    }

    public abstract int tierOverclock();

    public int overclock(int mStartEnergy) {
        if (tierOverclock() == 1) {
            return 1;
        }
        if (tierOverclock() == 2) {
            return mStartEnergy < 160000000 ? 2 : 1;
        }
        if (tierOverclock() == 4) {
            return (mStartEnergy < 160000000 ? 4 : (mStartEnergy < 320000000 ? 2 : 1));
        }
        if (tierOverclock() == 8) {
            return (mStartEnergy < 160000000)
                    ? 8
                    : ((mStartEnergy < 320000000) ? 4 : (mStartEnergy < 640000000) ? 2 : 1);
        }
        return (mStartEnergy < 160000000)
                ? 16
                : ((mStartEnergy < 320000000)
                        ? 8
                        : ((mStartEnergy < 640000000) ? 4 : (mStartEnergy < 1280000000) ? 2 : 1));
    }

    @Override
    public boolean checkRecipe_EM(ItemStack aStack) {
        ArrayList<FluidStack> tFluidList = getStoredFluids();

        if (tFluidList.size() > 1) {
            FluidStack[] tFluids = tFluidList.toArray(new FluidStack[0]);
            GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sFusionRecipes.findRecipe(
                    this.getBaseMetaTileEntity(), this.mLastRecipe, false, Integer.MAX_VALUE, tFluids);
            if ((tRecipe == null && !mRunningOnLoad) || (maxEUStore() < tRecipe.mSpecialValue)) {
                turnCasingActive(false);
                this.mLastRecipe = null;
                return false;
            }
            int pall = handleParallelRecipe(
                    tRecipe, tFluids, null, Math.min(getMaxPara() * extraPara(tRecipe.mSpecialValue), (int)
                            (getMaxEUInput() / tRecipe.mEUt / overclock(tRecipe.mSpecialValue))));
            this.para = pall;
            if (mRunningOnLoad || pall > 0) {
                this.mLastRecipe = tRecipe;
                this.mEUt = (this.mLastRecipe.mEUt * overclock(this.mLastRecipe.mSpecialValue) * pall);
                this.mMaxProgresstime =
                        Math.max(this.mLastRecipe.mDuration / overclock(this.mLastRecipe.mSpecialValue), 1);
                this.mEfficiencyIncrease = 10000;
                this.mOutputFluids = getMultiOutput(mLastRecipe, pall).getKey().toArray(new FluidStack[0]);
                turnCasingActive(true);
                mRunningOnLoad = false;
                return true;
            }
        }
        return false;
    }

    public long getMaxEUInput() {
        long sum = 0;
        for (GT_MetaTileEntity_Hatch_Energy hatch : mEnergyHatches) {
            if (isValidMetaTileEntity(hatch)) {
                sum += Math.min(
                        2048L * tierOverclock() * getMaxPara() * extraPara(100),
                        hatch.maxEUInput() * hatch.maxAmperesIn());
            }
        }
        for (GT_MetaTileEntity_Hatch_EnergyMulti hatch : eEnergyMulti) {
            if (isValidMetaTileEntity(hatch)) {
                sum += Math.min(
                        2048L * tierOverclock() * getMaxPara() * extraPara(100),
                        hatch.maxEUInput() * hatch.maxAmperesIn());
            }
        }
        return sum;
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

    private boolean addInjector(IGregTechTileEntity aBaseMetaTileEntity, int aBaseCasingIndex) {
        IMetaTileEntity aMetaTileEntity = aBaseMetaTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (!(aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input)) return false;
        GT_MetaTileEntity_Hatch_Input tHatch = (GT_MetaTileEntity_Hatch_Input) aMetaTileEntity;
        if (tHatch.mTier < hatchTier()) return false;
        tHatch.updateTexture(aBaseCasingIndex);
        tHatch.mRecipeMap = getRecipeMap();
        return mInputHatches.add(tHatch);
    }

    private boolean addExtractor(IGregTechTileEntity aBaseMetaTileEntity, int aBaseCasingIndex) {
        if (aBaseMetaTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aBaseMetaTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (!(aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output)) return false;
        GT_MetaTileEntity_Hatch_Output tHatch = (GT_MetaTileEntity_Hatch_Output) aMetaTileEntity;
        if (tHatch.mTier < hatchTier()) return false;
        tHatch.updateTexture(aBaseCasingIndex);
        return mOutputHatches.add(tHatch);
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
    public String[] getInfoData() {
        String tier = hatchTier() == 6
                ? EnumChatFormatting.RED + "I" + EnumChatFormatting.RESET
                : hatchTier() == 7
                        ? EnumChatFormatting.YELLOW + "II" + EnumChatFormatting.RESET
                        : hatchTier() == 8 ? EnumChatFormatting.GRAY + "III" + EnumChatFormatting.RESET : "IV";
        float plasmaOut = 0;
        int powerRequired = 0;
        if (this.mLastRecipe != null) {
            powerRequired = this.mLastRecipe.mEUt * this.para;
            if (this.mLastRecipe.getFluidOutput(0) != null) {
                plasmaOut = (float) this.mLastRecipe.getFluidOutput(0).amount
                        / (float) this.mLastRecipe.mDuration
                        * this.para;
            }
        }

        return new String[] {
            EnumChatFormatting.BLUE + "Fusion Reactor MK " + EnumChatFormatting.RESET + tier,
            StatCollector.translateToLocal("scanner.info.UX.0") + ": " + EnumChatFormatting.LIGHT_PURPLE
                    + GT_Utility.formatNumbers(this.para) + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("GT5U.fusion.req") + ": " + EnumChatFormatting.RED
                    + GT_Utility.formatNumbers(powerRequired) + EnumChatFormatting.RESET + "EU/t",
            StatCollector.translateToLocal("GT5U.multiblock.energy") + ": " + EnumChatFormatting.GREEN
                    + GT_Utility.formatNumbers(mEUStore) + EnumChatFormatting.RESET + " EU / "
                    + EnumChatFormatting.YELLOW
                    + GT_Utility.formatNumbers(maxEUStore()) + EnumChatFormatting.RESET + " EU",
            StatCollector.translateToLocal("GT5U.fusion.plasma") + ": " + EnumChatFormatting.YELLOW
                    + GT_Utility.formatNumbers(plasmaOut) + EnumChatFormatting.RESET + "L/t"
        };
    }

    public static final String[] L0 = {
        "                                               ",
        "                                               ",
        "                    FCCCCCF                    ",
        "                    FCIBICF                    ",
        "                    FCCCCCF                    ",
        "                                               ",
        "                                               ",
        "                                               ",
        "                                               ",
        "                                               ",
        "                                               ",
        "                                               ",
        "                                               ",
        "                                               ",
        "                                               ",
        "                                               ",
        "                                               ",
        "                                               ",
        "                                               ",
        "                                               ",
        "  FFF                                     FFF  ",
        "  CCC                                     CCC  ",
        "  CIC                                     CIC  ",
        "  CBC                                     CBC  ",
        "  CIC                                     CIC  ",
        "  CCC                                     CCC  ",
        "  FFF                                     FFF  ",
        "                                               ",
        "                                               ",
        "                                               ",
        "                                               ",
        "                                               ",
        "                                               ",
        "                                               ",
        "                                               ",
        "                                               ",
        "                                               ",
        "                                               ",
        "                                               ",
        "                                               ",
        "                                               ",
        "                                               ",
        "                    FCCCCCF                    ",
        "                    FCIBICF                    ",
        "                    FCCCCCF                    ",
        "                                               ",
        "                                               ",
    };

    public static final String[] L1 = {
        "                                               ",
        "                    FCBBBCF                    ",
        "                   CC     CC                   ",
        "                CCCCC     CCCCC                ",
        "              CCCCCCC     CCCCCCC              ",
        "            CCCCCCC FCBBBCF CCCCCCC            ",
        "           CCCCC               CCCCC           ",
        "          CCCC                   CCCC          ",
        "         CCC                       CCC         ",
        "        CCC                         CCC        ",
        "       CCC                           CCC       ",
        "      CCC                             CCC      ",
        "     CCC                               CCC     ",
        "     CCC                               CCC     ",
        "    CCC                                 CCC    ",
        "    CCC                                 CCC    ",
        "   CCC                                   CCC   ",
        "   CCC                                   CCC   ",
        "   CCC                                   CCC   ",
        "  CCC                                     CCC  ",
        " FCCCF                                   FCCCF ",
        " C   C                                   C   C ",
        " B   B                                   B   B ",
        " B   B                                   B   B ",
        " B   B                                   B   B ",
        " C   C                                   C   C ",
        " FCCCF                                   FCCCF ",
        "  CCC                                     CCC  ",
        "   CCC                                   CCC   ",
        "   CCC                                   CCC   ",
        "   CCC                                   CCC   ",
        "    CCC                                 CCC    ",
        "    CCC                                 CCC    ",
        "     CCC                               CCC     ",
        "     CCC                               CCC     ",
        "      CCC                             CCC      ",
        "       CCC                           CCC       ",
        "        CCC                         CCC        ",
        "         CCC                       CCC         ",
        "          CCCC                   CCCC          ",
        "           CCCCC               CCCCC           ",
        "            CCCCCCC FCBBBCF CCCCCCC            ",
        "              CCCCCCC     CCCCCCC              ",
        "                CCCCC     CCCCC                ",
        "                   CC     CC                   ",
        "                    FCBBBCF                    ",
        "                                               ",
    };

    public static final String[] L2 = {
        "                    FCCCCCF                    ",
        "                   CC     CC                   ",
        "                CCCCC     CCCCC                ",
        "              CCCCCHHHHHHHHHCCCCC              ",
        "            CCCCHHHCC     CCHHHCCCC            ",
        "           CCCHHCCCCC     CCCCCHHCCC           ",
        "          ECHHCCCCC FCCCCCF CCCCCHHCE          ",
        "         CCHCCCC               CCCCHCC         ",
        "        CCHCCC                   CCCHCC        ",
        "       CCHCE                       ECHCC       ",
        "      ECHCC                         CCHCE      ",
        "     CCHCE                           ECHCC     ",
        "    CCHCC                             CCHCC    ",
        "    CCHCC                             CCHCC    ",
        "   CCHCC                               CCHCC   ",
        "   CCHCC                               CCHCC   ",
        "  CCHCC                                 CCHCC  ",
        "  CCHCC                                 CCHCC  ",
        "  CCHCC                                 CCHCC  ",
        " CCHCC                                   CCHCC ",
        "FCCHCCF                                 FCCHCCF",
        "C  H  C                                 C  H  C",
        "C  H  C                                 C  H  C",
        "C  H  C                                 C  H  C",
        "C  H  C                                 C  H  C",
        "C  H  C                                 C  H  C",
        "FCCHCCF                                 FCCHCCF",
        " CCHCC                                   CCHCC ",
        "  CCHCC                                 CCHCC  ",
        "  CCHCC                                 CCHCC  ",
        "  CCHCC                                 CCHCC  ",
        "   CCHCC                               CCHCC   ",
        "   CCHCC                               CCHCC   ",
        "    CCHCC                             CCHCC    ",
        "    CCHCC                             CCHCC    ",
        "     CCHCE                           ECHCC     ",
        "      ECHCC                         CCHCE      ",
        "       CCHCE                       ECHCC       ",
        "        CCHCCC                   CCCHCC        ",
        "         CCHCCCC               CCCCHCC         ",
        "          ECHHCCCCC FCCCCCF CCCCCHHCE          ",
        "           CCCHHCCCCC     CCCCCHHCCC           ",
        "            CCCCHHHCC     CCHHHCCCC            ",
        "              CCCCCHHHHHHHHHCCCCC              ",
        "                CCCCC     CCCCC                ",
        "                   CC     CC                   ",
        "                    FCCCCCF                    ",
    };

    public static final String[] L3 = {
        "                    FCOBOCF                    ",
        "                   CC     CC                   ",
        "                CCCHHHHHHHHHCCC                ",
        "              CCHHHHHHHHHHHHHHHCC              ",
        "            CCHHHHHHHHHHHHHHHHHHHCC            ",
        "           CHHHHHHHCC     CCHHHHHHHC           ",
        "          CHHHHHCCC FCOBOCF CCCHHHHHC          ",
        "         CHHHHCC               CCHHHHC         ",
        "        CHHHCC                   CCHHHC        ",
        "       CHHHC                       CHHHC       ",
        "      CHHHC                         CHHHC      ",
        "     CHHHC                           CHHHC     ",
        "    CHHHC                             CHHHC    ",
        "    CHHHC                             CHHHC    ",
        "   CHHHC                               CHHHC   ",
        "   CHHHC                               CHHHC   ",
        "  CHHHC                                 CHHHC  ",
        "  CHHHC                                 CHHHC  ",
        "  CHHHC                                 CHHHC  ",
        " CHHHC                                   CHHHC ",
        "FCHHHCF                                 FCHHHCF",
        "C HHH C                                 C HHH C",
        "O HHH O                                 O HHH O",
        "B HHH B                                 B HHH B",
        "O HHH O                                 O HHH O",
        "C HHH C                                 C HHH C",
        "FCHHHCF                                 FCHHHCF",
        " CHHHC                                   CHHHC ",
        "  CHHHC                                 CHHHC  ",
        "  CHHHC                                 CHHHC  ",
        "  CHHHC                                 CHHHC  ",
        "   CHHHC                               CHHHC   ",
        "   CHHHC                               CHHHC   ",
        "    CHHHC                             CHHHC    ",
        "    CHHHC                             CHHHC    ",
        "     CHHHC                           CHHHC     ",
        "      CHHHC                         CHHHC      ",
        "       CHHHC                       CHHHC       ",
        "        CHHHCC                   CCHHHC        ",
        "         CHHHHCC               CCHHHHC         ",
        "          CHHHHHCCC FCO~OCF CCCHHHHHC          ",
        "           CHHHHHHHCC     CCHHHHHHHC           ",
        "            CCHHHHHHHHHHHHHHHHHHHCC            ",
        "              CCHHHHHHHHHHHHHHHCC              ",
        "                CCCHHHHHHHHHCCC                ",
        "                   CC     CC                   ",
        "                    FCOBOCF                    ",
    };
}

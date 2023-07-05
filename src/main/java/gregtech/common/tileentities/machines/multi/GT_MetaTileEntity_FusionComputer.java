package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASING_FUSION_GLASS;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASING_FUSION_GLASS_YELLOW;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASING_FUSION_GLASS_YELLOW_GLOW;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_StructureUtility.filterByMTETier;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.google.common.collect.ImmutableMap;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.GT_Mod;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.power.FusionPower;
import gregtech.common.power.Power;

public abstract class GT_MetaTileEntity_FusionComputer extends
    GT_MetaTileEntity_EnhancedMultiBlockBase<GT_MetaTileEntity_FusionComputer> implements ISurvivalConstructable {

    protected FusionPower power;

    public static final String STRUCTURE_PIECE_MAIN = "main";
    private static final ClassValue<IStructureDefinition<GT_MetaTileEntity_FusionComputer>> STRUCTURE_DEFINITION = new ClassValue<>() {

        @Override
        protected IStructureDefinition<GT_MetaTileEntity_FusionComputer> computeValue(Class<?> type) {
            return StructureDefinition.<GT_MetaTileEntity_FusionComputer>builder()
                .addShape(
                    STRUCTURE_PIECE_MAIN,
                    transpose(
                        new String[][] {
                            { "               ", "      ihi      ", "    hh   hh    ", "   h       h   ",
                                "  h         h  ", "  h         h  ", " i           i ", " h           h ",
                                " i           i ", "  h         h  ", "  h         h  ", "   h       h   ",
                                "    hh   hh    ", "      ihi      ", "               ", },
                            { "      xhx      ", "    hhccchh    ", "   eccxhxcce   ", "  eceh   hece  ",
                                " hce       ech ", " hch       hch ", "xcx         xcx", "hch         hch",
                                "xcx         xcx", " hch       hch ", " hce       ech ", "  eceh   hece  ",
                                "   eccx~xcce   ", "    hhccchh    ", "      xhx      ", },
                            { "               ", "      ihi      ", "    hh   hh    ", "   h       h   ",
                                "  h         h  ", "  h         h  ", " i           i ", " h           h ",
                                " i           i ", "  h         h  ", "  h         h  ", "   h       h   ",
                                "    hh   hh    ", "      ihi      ", "               ", } }))
                .addElement('c', lazy(t -> ofBlock(t.getFusionCoil(), t.getFusionCoilMeta())))
                .addElement('h', lazy(t -> ofBlock(t.getCasing(), t.getCasingMeta())))
                .addElement(
                    'i',
                    lazy(
                        t -> buildHatchAdder(GT_MetaTileEntity_FusionComputer.class)
                            .atLeast(
                                ImmutableMap.of(InputHatch.withAdder(GT_MetaTileEntity_FusionComputer::addInjector), 2))
                            .hatchItemFilterAnd(t2 -> filterByMTETier(t2.tier(), Integer.MAX_VALUE))
                            .casingIndex(53)
                            .dot(1)
                            .buildAndChain(t.getCasing(), t.getCasingMeta())))
                .addElement(
                    'e',
                    lazy(
                        t -> buildHatchAdder(GT_MetaTileEntity_FusionComputer.class).atLeast(
                            ImmutableMap.of(Energy.withAdder(GT_MetaTileEntity_FusionComputer::addEnergyInjector), 16))
                            .hatchItemFilterAnd(t2 -> filterByMTETier(t2.tier(), Integer.MAX_VALUE))
                            .casingIndex(53)
                            .dot(2)
                            .buildAndChain(t.getCasing(), t.getCasingMeta())))
                .addElement(
                    'x',
                    lazy(
                        t -> buildHatchAdder(GT_MetaTileEntity_FusionComputer.class)
                            .atLeast(OutputHatch.withAdder(GT_MetaTileEntity_FusionComputer::addExtractor))
                            .hatchItemFilterAnd(t2 -> filterByMTETier(t2.tier(), Integer.MAX_VALUE))
                            .casingIndex(53)
                            .dot(3)
                            .buildAndChain(t.getCasing(), t.getCasingMeta())))
                .build();
        }
    };
    public GT_Recipe mLastRecipe;
    public long mEUStore;

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

    public GT_MetaTileEntity_FusionComputer(int aID, String aName, String aNameRegional, int tier) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_FusionComputer(String aName) {
        super(aName);
    }

    @Override
    public Power getPower() {
        return power;
    }

    public abstract int tier();

    @Override
    public abstract long maxEUStore();

    @Override
    public abstract MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity);

    @Override
    public boolean allowCoverOnSide(ForgeDirection side, GT_ItemStack aStack) {

        return side != getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_FusionComputer> getStructureDefinition() {
        return STRUCTURE_DEFINITION.get(getClass());
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addController("Fusion Reactor")
            .addInfo("Some kind of fusion reactor, maybe")
            .addSeparator()
            .addInfo("Some kind of fusion reactor, maybe")
            .addStructureInfo("Should probably be built similar to other fusions")
            .addStructureInfo("See controller tooltip for details")
            .toolTipFinisher("Gregtech");
        return tt;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        if (checkPiece(STRUCTURE_PIECE_MAIN, 7, 1, 12) && mInputHatches.size() > 1
            && !mOutputHatches.isEmpty()
            && !mEnergyHatches.isEmpty()) {
            mWrench = true;
            mScrewdriver = true;
            mSoftHammer = true;
            mHardHammer = true;
            mSolderingTool = true;
            mCrowbar = true;
            return true;
        }
        return false;
    }

    private boolean addEnergyInjector(IGregTechTileEntity aBaseMetaTileEntity, int aBaseCasingIndex) {
        IMetaTileEntity aMetaTileEntity = aBaseMetaTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (!(aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy tHatch)) return false;
        if (tHatch.mTier < tier()) return false;
        tHatch.updateTexture(aBaseCasingIndex);
        return mEnergyHatches.add(tHatch);
    }

    private boolean addInjector(IGregTechTileEntity aBaseMetaTileEntity, int aBaseCasingIndex) {
        IMetaTileEntity aMetaTileEntity = aBaseMetaTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (!(aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input tHatch)) return false;
        if (tHatch.mTier < tier()) return false;
        tHatch.updateTexture(aBaseCasingIndex);
        tHatch.mRecipeMap = getRecipeMap();
        return mInputHatches.add(tHatch);
    }

    private boolean addExtractor(IGregTechTileEntity aBaseMetaTileEntity, int aBaseCasingIndex) {
        if (aBaseMetaTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aBaseMetaTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (!(aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output tHatch)) return false;
        if (tHatch.mTier < tier()) return false;
        tHatch.updateTexture(aBaseCasingIndex);
        return mOutputHatches.add(tHatch);
    }

    public abstract Block getCasing();

    public abstract int getCasingMeta();

    public abstract Block getFusionCoil();

    public abstract int getFusionCoilMeta();

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) return new ITexture[] { TextureFactory.builder()
            .addIcon(MACHINE_CASING_FUSION_GLASS)
            .extFacing()
            .build(), getTextureOverlay() };
        if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(52) };
        return new ITexture[] { TextureFactory.builder()
            .addIcon(MACHINE_CASING_FUSION_GLASS)
            .extFacing()
            .build() };
    }

    /**
     * @return The list of textures overlay
     */
    public abstract ITexture getTextureOverlay();

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    public int overclock(int mStartEnergy) {
        if (tierOverclock() == 1) {
            return 1;
        }
        if (tierOverclock() == 2) {
            return mStartEnergy < 160000000 ? 2 : 1;
        }
        if (this.tierOverclock() == 4) {
            return (mStartEnergy < 160000000 ? 4 : (mStartEnergy < 320000000 ? 2 : 1));
        }
        return (mStartEnergy < 160000000) ? 8 : ((mStartEnergy < 320000000) ? 4 : (mStartEnergy < 640000000) ? 2 : 1);
    }

    @Override
    public boolean checkRecipe(ItemStack aStack) {
        ArrayList<FluidStack> tFluidList = getStoredFluids();
        int tFluidList_sS = tFluidList.size();
        for (int i = 0; i < tFluidList_sS - 1; i++) {
            for (int j = i + 1; j < tFluidList_sS; j++) {
                if (GT_Utility.areFluidsEqual(tFluidList.get(i), tFluidList.get(j))) {
                    if (tFluidList.get(i).amount >= tFluidList.get(j).amount) {
                        tFluidList.remove(j--);
                        tFluidList_sS = tFluidList.size();
                    } else {
                        tFluidList.remove(i--);
                        tFluidList_sS = tFluidList.size();
                        break;
                    }
                }
            }
        }
        if (tFluidList.size() > 1) {
            FluidStack[] tFluids = tFluidList.toArray(new FluidStack[0]);
            GT_Recipe tRecipe;

            tRecipe = GT_Recipe.GT_Recipe_Map.sFusionRecipes
                .findRecipe(this.getBaseMetaTileEntity(), this.mLastRecipe, false, GT_Values.V[tier()], tFluids);
            if (tRecipe == null) {
                tRecipe = GT_Recipe.GT_Recipe_Map.sComplexFusionRecipes
                    .findRecipe(this.getBaseMetaTileEntity(), this.mLastRecipe, false, GT_Values.V[tier()], tFluids);
            }

            if ((tRecipe == null && !mRunningOnLoad) || (maxEUStore() < tRecipe.mSpecialValue)) {
                turnCasingActive(false);
                this.mLastRecipe = null;
                return false;
            }
            if (!canOutputAll(tRecipe)) return false;
            if (mRunningOnLoad || tRecipe.isRecipeInputEqual(true, tFluids)) {
                this.mLastRecipe = tRecipe;
                this.mEUt = (this.mLastRecipe.mEUt * overclock(this.mLastRecipe.mSpecialValue));
                this.mMaxProgresstime = this.mLastRecipe.mDuration / overclock(this.mLastRecipe.mSpecialValue);
                this.mEfficiencyIncrease = 10000;
                this.mOutputFluids = this.mLastRecipe.mFluidOutputs;
                turnCasingActive(true);
                mRunningOnLoad = false;
                return true;
            }
        }
        return false;
    }

    public abstract int tierOverclock();

    public boolean turnCasingActive(boolean status) {
        if (this.mEnergyHatches != null) {
            for (GT_MetaTileEntity_Hatch_Energy hatch : this.mEnergyHatches) {
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
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (mEfficiency < 0) mEfficiency = 0;
            if (mRunningOnLoad && checkMachine(aBaseMetaTileEntity, mInventory[1])) {
                this.mEUStore = aBaseMetaTileEntity.getStoredEU();
                checkRecipe();
            }
            if (mUpdated) {
                mUpdate = 50;
                mUpdated = false;
            }
            if (--mUpdate == 0 || --mStartUpCheck == 0) {
                checkStructure(true, aBaseMetaTileEntity);
            }
            if (mStartUpCheck < 0) {
                if (mMachine) {
                    this.mEUStore = aBaseMetaTileEntity.getStoredEU();
                    if (this.mEnergyHatches != null) {
                        for (GT_MetaTileEntity_Hatch_Energy tHatch : mEnergyHatches)
                            if (isValidMetaTileEntity(tHatch)) {
                                long energyToMove = GT_Values.V[tier()] / 16;
                                if (aBaseMetaTileEntity.getStoredEU() + energyToMove < maxEUStore()
                                    && tHatch.getBaseMetaTileEntity()
                                        .decreaseStoredEnergyUnits(energyToMove, false)) {
                                    aBaseMetaTileEntity.increaseStoredEnergyUnits(energyToMove, true);
                                }
                            }
                    }
                    if (this.mEUStore <= 0 && mMaxProgresstime > 0) {
                        criticalStopMachine();
                    }
                    if (mMaxProgresstime > 0) {
                        this.getBaseMetaTileEntity()
                            .decreaseStoredEnergyUnits(mEUt, true);
                        if (mMaxProgresstime > 0 && ++mProgresstime >= mMaxProgresstime) {
                            if (mOutputItems != null)
                                for (ItemStack tStack : mOutputItems) if (tStack != null) addOutput(tStack);
                            if (mOutputFluids != null)
                                for (FluidStack tStack : mOutputFluids) if (tStack != null) addOutput(tStack);
                            mEfficiency = Math
                                .max(0, Math.min(mEfficiency + mEfficiencyIncrease, getMaxEfficiency(mInventory[1])));
                            mOutputItems = null;
                            mProgresstime = 0;
                            mMaxProgresstime = 0;
                            mEfficiencyIncrease = 0;
                            if (mOutputFluids != null && mOutputFluids.length > 0) {
                                try {
                                    GT_Mod.achievements.issueAchivementHatchFluid(
                                        aBaseMetaTileEntity.getWorld()
                                            .getPlayerEntityByName(aBaseMetaTileEntity.getOwnerName()),
                                        mOutputFluids[0]);
                                } catch (Exception ignored) {}
                            }
                            this.mEUStore = aBaseMetaTileEntity.getStoredEU();
                            if (aBaseMetaTileEntity.isAllowedToWork()) checkRecipe();
                        }
                    } else {
                        if (aTick % 100 == 0 || aBaseMetaTileEntity.hasWorkJustBeenEnabled()
                            || aBaseMetaTileEntity.hasInventoryBeenModified()) {
                            turnCasingActive(mMaxProgresstime > 0);
                            if (aBaseMetaTileEntity.isAllowedToWork()) {
                                this.mEUStore = aBaseMetaTileEntity.getStoredEU();
                                if (checkRecipe()) {
                                    if (this.mEUStore < this.mLastRecipe.mSpecialValue - this.mEUt) {
                                        criticalStopMachine();
                                    }
                                    aBaseMetaTileEntity
                                        .decreaseStoredEnergyUnits(this.mLastRecipe.mSpecialValue - this.mEUt, true);
                                }
                            }
                            if (mMaxProgresstime <= 0) mEfficiency = Math.max(0, mEfficiency - 1000);
                        }
                    }
                } else {
                    this.mLastRecipe = null;
                    stopMachine();
                }
            }
            aBaseMetaTileEntity
                .setErrorDisplayID((aBaseMetaTileEntity.getErrorDisplayID() & ~127) | (mMachine ? 0 : 64));
            aBaseMetaTileEntity.setActive(mMaxProgresstime > 0);
        }
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (mEUt < 0) {
            if (!drainEnergyInput(((long) -mEUt * 10000) / Math.max(1000, mEfficiency))) {
                this.mLastRecipe = null;
                criticalStopMachine();
                return false;
            }
        }
        if (this.mEUStore <= 0) {
            this.mLastRecipe = null;
            criticalStopMachine();
            return false;
        }
        return true;
    }

    @Override
    public boolean drainEnergyInput(long aEU) {
        return false;
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
    public void stopMachine() {
        super.stopMachine();
        turnCasingActive(false);
    }

    @Override
    public String[] getInfoData() {
        String tier = tier() == 6 ? EnumChatFormatting.RED + "I" + EnumChatFormatting.RESET
            : tier() == 7 ? EnumChatFormatting.YELLOW + "II" + EnumChatFormatting.RESET
                : tier() == 8 ? EnumChatFormatting.GRAY + "III" + EnumChatFormatting.RESET : "IV";
        float plasmaOut = 0;
        int powerRequired = 0;
        if (this.mLastRecipe != null) {
            powerRequired = this.mLastRecipe.mEUt;
            if (this.mLastRecipe.getFluidOutput(0) != null) {
                plasmaOut = (float) this.mLastRecipe.getFluidOutput(0).amount / (float) this.mLastRecipe.mDuration;
            }
        }

        return new String[] { EnumChatFormatting.BLUE + "Fusion Reactor MK " + EnumChatFormatting.RESET + tier,
            StatCollector.translateToLocal("GT5U.fusion.req") + ": "
                + EnumChatFormatting.RED
                + GT_Utility.formatNumbers(powerRequired)
                + EnumChatFormatting.RESET
                + "EU/t",
            StatCollector.translateToLocal("GT5U.multiblock.energy") + ": "
                + EnumChatFormatting.GREEN
                + GT_Utility.formatNumbers(mEUStore)
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

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 7, 1, 12);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 7, 1, 12, elementBudget, env, false, true);
    }

    @Override
    protected SoundResource getProcessStartSound() {
        return SoundResource.GT_MACHINES_FUSION_LOOP;
    }

    @Override
    public boolean doesBindPlayerInventory() {
        return false;
    }

    // @Override
    // public void addGregTechLogo(ModularWindow.Builder builder) {
    // builder.widget(
    // new DrawableWidget().setDrawable(getGUITextureSet().getGregTechLogo())
    // .setSize(17, 17)
    // .setPos(155, 145));
    // }

    // @Override
    // public GUITextureSet getGUITextureSet() {
    // return new GUITextureSet().setMainBackground(GT_UITextures.BACKGROUND_FUSION_COMPUTER);
    // }

    @Override
    public int getGUIWidth() {
        return 176;
    }

    @Override
    public int getGUIHeight() {
        return 166;
    }

    // @Override
    // public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
    // builder
    // .widget(
    // new TextWidget(GT_Utility.trans("138", "Incomplete Structure.")).setDefaultColor(COLOR_TEXT_WHITE.get())
    // .setEnabled(widget -> !mMachine)
    // .setPos(10, 8))
    // .widget(new FakeSyncWidget.BooleanSyncer(() -> mMachine, val -> mMachine = val))
    // .widget(
    // new TextWidget("Hit with Soft Mallet to (re-)start the Machine if it doesn't start.")
    // .setDefaultColor(COLOR_TEXT_WHITE.get())
    // .setTextAlignment(Alignment.Center)
    // .setEnabled(
    // widget -> getBaseMetaTileEntity().getErrorDisplayID() == 0
    // && !getBaseMetaTileEntity().isActive())
    // .setPos(-getGUIWidth() / 2, 170)
    // .setSize(getGUIWidth() * 2, 9))
    // .widget(
    // new FakeSyncWidget.IntegerSyncer(
    // () -> getBaseMetaTileEntity().getErrorDisplayID(),
    // val -> getBaseMetaTileEntity().setErrorDisplayID(val)))
    // .widget(
    // new FakeSyncWidget.BooleanSyncer(
    // () -> getBaseMetaTileEntity().isActive(),
    // val -> getBaseMetaTileEntity().setActive(val)))
    // .widget(
    // new TextWidget("Running perfectly.").setDefaultColor(COLOR_TEXT_WHITE.get())
    // .setTextAlignment(Alignment.Center)
    // .setEnabled(
    // widget -> getBaseMetaTileEntity().getErrorDisplayID() == 0
    // && getBaseMetaTileEntity().isActive())
    // .setPos(0, 170)
    // .setSize(getGUIWidth(), 9))
    // .widget(
    // new FakeSyncWidget.IntegerSyncer(
    // () -> getBaseMetaTileEntity().getErrorDisplayID(),
    // val -> getBaseMetaTileEntity().setErrorDisplayID(val)))
    // .widget(
    // new ProgressBar()
    // .setProgress(
    // () -> (float) getBaseMetaTileEntity().getStoredEU() / getBaseMetaTileEntity().getEUCapacity())
    // .setDirection(ProgressBar.Direction.RIGHT)
    // .setTexture(GT_UITextures.PROGRESSBAR_STORED_EU, 147)
    // .setPos(5, 156)
    // .setSize(147, 5))
    // .widget(TextWidget.dynamicString(() -> {
    // long energy = getBaseMetaTileEntity().getStoredEU();
    // if (energy > 160_000_000L && energy < 160_010_000L) {
    // energy = 160_000_000L;
    // }
    // if (energy > 320_000_000L && energy < 320_010_000L) {
    // energy = 320_000_000L;
    // }
    // if (energy > 640_000_000L && energy < 640_010_000L) {
    // energy = 640_000_000L;
    // }
    // if (energy > 5_120_000_000L && energy < 5_120_080_000L) {
    // energy = 5_120_000_000L;
    // }
    // return GT_Utility.formatNumbers(energy) + " EU";
    // })
    // .setDefaultColor(COLOR_TEXT_RED.get())
    // .setPos(50, 155))
    // .widget(
    // new ButtonWidget().setNEITransferRect(GT_Recipe.GT_Recipe_Map.sFusionRecipes.mNEIName)
    // .setBackground(GT_UITextures.BUTTON_STANDARD, GT_UITextures.OVERLAY_BUTTON_NEI)
    // .setPos(154, 4)
    // .setSize(18, 18));
    // }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }
}

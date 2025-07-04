package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASING_FUSION_GLASS;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASING_FUSION_GLASS_YELLOW;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASING_FUSION_GLASS_YELLOW_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.filterByMTETier;
import static gregtech.api.util.GTUtility.validMTEList;

import java.util.Set;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableMap;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.NumberFormatMUI;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GTMod;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.enums.VoidingMode;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.gui.modularui.GUITextureSet;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IOverclockDescriptionProvider;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.objects.overclockdescriber.FusionOverclockDescriber;
import gregtech.api.objects.overclockdescriber.OverclockDescriber;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.ParallelHelper;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.tileentities.machines.multi.drone.MTEHatchDroneDownLink;

public abstract class MTEFusionComputer extends MTEEnhancedMultiBlockBase<MTEFusionComputer>
    implements ISurvivalConstructable, IAddUIWidgets, IOverclockDescriptionProvider {

    private final OverclockDescriber overclockDescriber;

    public static final String STRUCTURE_PIECE_MAIN = "main";
    private static final ClassValue<IStructureDefinition<MTEFusionComputer>> STRUCTURE_DEFINITION = new ClassValue<>() {

        @Override
        protected IStructureDefinition<MTEFusionComputer> computeValue(Class<?> type) {
            return StructureDefinition.<MTEFusionComputer>builder()
                .addShape(
                    STRUCTURE_PIECE_MAIN,
                    transpose(
                        new String[][] {
                            { "               ", "      ihi      ", "    hh   hh    ", "   h       h   ",
                                "  h         h  ", "  h         h  ", " i           i ", " h           h ",
                                " i           i ", "  h         h  ", "  h         h  ", "   h       h   ",
                                "    hh   hh    ", "      ihi      ", "               ", },
                            { "      xhx      ", "    hhccchh    ", "   eccxdxcce   ", "  eceh   hece  ",
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
                        t -> buildHatchAdder(MTEFusionComputer.class)
                            .atLeast(ImmutableMap.of(InputHatch.withAdder(MTEFusionComputer::addInjector), 1))
                            .hatchItemFilterAnd(t2 -> filterByMTETier(t2.tier(), Integer.MAX_VALUE))
                            .casingIndex(53)
                            .dot(1)
                            .buildAndChain(t.getCasing(), t.getCasingMeta())))
                .addElement(
                    'e',
                    lazy(
                        t -> buildHatchAdder(MTEFusionComputer.class)
                            .atLeast(ImmutableMap.of(Energy.withAdder(MTEFusionComputer::addEnergyInjector), 16))
                            .hatchItemFilterAnd(t2 -> filterByMTETier(t2.tier(), Integer.MAX_VALUE))
                            .casingIndex(53)
                            .dot(2)
                            .buildAndChain(t.getCasing(), t.getCasingMeta())))
                .addElement(
                    'x',
                    lazy(
                        t -> buildHatchAdder(MTEFusionComputer.class)
                            .atLeast(OutputHatch.withAdder(MTEFusionComputer::addExtractor))
                            .hatchItemFilterAnd(t2 -> filterByMTETier(t2.tier(), Integer.MAX_VALUE))
                            .casingIndex(53)
                            .dot(3)
                            .buildAndChain(t.getCasing(), t.getCasingMeta())))
                .addElement(
                    'd',
                    lazy(
                        t -> buildHatchAdder(MTEFusionComputer.class).adder(MTEFusionComputer::addDroneHatch)
                            .hatchId(9401)
                            .casingIndex(53)
                            .dot(4)
                            .buildAndChain(t.getCasing(), t.getCasingMeta())))
                .build();
        }
    };
    public GTRecipe mLastRecipe;
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

    public MTEFusionComputer(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        this.overclockDescriber = createOverclockDescriber();
    }

    public MTEFusionComputer(String aName) {
        super(aName);
        this.overclockDescriber = createOverclockDescriber();
    }

    protected OverclockDescriber createOverclockDescriber() {
        return new FusionOverclockDescriber((byte) tier(), capableStartupCanonical());
    }

    @Nonnull
    @Override
    public OverclockDescriber getOverclockDescriber() {
        return overclockDescriber;
    }

    public abstract int tier();

    @Override
    public abstract long maxEUStore();

    /**
     * Unlike {@link #maxEUStore()}, this provides theoretical limit of startup EU, without considering the amount of
     * hatches nor the room for extra energy. Intended for simulation.
     */
    public abstract long capableStartupCanonical();

    @Override
    public abstract MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity);

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (mEUt > 0) {
            mEUt = -mEUt;
        }
    }

    @Override
    public IStructureDefinition<MTEFusionComputer> getStructureDefinition() {
        return STRUCTURE_DEFINITION.get(getClass());
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addController("Fusion Reactor")
            .addInfo("Some kind of fusion reactor, maybe")
            .addStructureInfo("Should probably be built similar to other fusions")
            .addStructureInfo("See controller tooltip for details")
            .toolTipFinisher();
        return tt;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return checkPiece(STRUCTURE_PIECE_MAIN, 7, 1, 12) && !mOutputHatches.isEmpty() && !mEnergyHatches.isEmpty();
    }

    private boolean addEnergyInjector(IGregTechTileEntity aBaseMetaTileEntity, int aBaseCasingIndex) {
        IMetaTileEntity aMetaTileEntity = aBaseMetaTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (!(aMetaTileEntity instanceof MTEHatchEnergy tHatch)) return false;
        if (tHatch.mTier < tier()) return false;
        tHatch.updateTexture(aBaseCasingIndex);
        return mEnergyHatches.add(tHatch);
    }

    private boolean addInjector(IGregTechTileEntity aBaseMetaTileEntity, int aBaseCasingIndex) {
        IMetaTileEntity aMetaTileEntity = aBaseMetaTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (!(aMetaTileEntity instanceof MTEHatchInput tHatch)) return false;
        if (tHatch.getTierForStructure() < tier()) return false;
        tHatch.updateTexture(aBaseCasingIndex);
        tHatch.mRecipeMap = getRecipeMap();
        return mInputHatches.add(tHatch);
    }

    private boolean addExtractor(IGregTechTileEntity aBaseMetaTileEntity, int aBaseCasingIndex) {
        if (aBaseMetaTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aBaseMetaTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (!(aMetaTileEntity instanceof MTEHatchOutput tHatch)) return false;
        if (tHatch.getTierForStructure() < tier()) return false;
        tHatch.updateTexture(aBaseCasingIndex);
        return mOutputHatches.add(tHatch);
    }

    private boolean addDroneHatch(IGregTechTileEntity aBaseMetaTileEntity, int aBaseCasingIndex) {
        if (aBaseMetaTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aBaseMetaTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (!(aMetaTileEntity instanceof MTEHatchDroneDownLink tHatch)) return false;
        tHatch.updateTexture(aBaseCasingIndex);
        return addToMachineList(aBaseMetaTileEntity, aBaseCasingIndex);
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
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.fusionRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected ParallelHelper createParallelHelper(@NotNull GTRecipe recipe) {
                // When the fusion first loads and is still processing, it does the recipe check without consuming.
                return super.createParallelHelper(recipe).setConsumption(!mRunningOnLoad);
            }

            @NotNull
            @Override
            protected OverclockCalculator createOverclockCalculator(@NotNull GTRecipe recipe) {
                return overclockDescriber.createCalculator(super.createOverclockCalculator(recipe), recipe);
            }

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                if (!mRunningOnLoad
                    && recipe.getMetadataOrDefault(GTRecipeConstants.FUSION_THRESHOLD, 0L) > maxEUStore()) {
                    return CheckRecipeResultRegistry.insufficientStartupPower(recipe.mSpecialValue);
                }
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
        };
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(GTValues.V[tier()]);
        logic.setAvailableAmperage(1);
        logic.setAmperageOC(false);
        logic.setUnlimitedTierSkips();
    }

    public boolean turnCasingActive(boolean status) {
        if (this.mEnergyHatches != null) {
            for (MTEHatchEnergy hatch : this.mEnergyHatches) {
                hatch.updateTexture(status ? 52 : 53);
            }
        }
        if (this.mOutputHatches != null) {
            for (MTEHatchOutput hatch : this.mOutputHatches) {
                hatch.updateTexture(status ? 52 : 53);
            }
        }
        if (this.mInputHatches != null) {
            for (MTEHatchInput hatch : this.mInputHatches) {
                hatch.updateTexture(status ? 52 : 53);
            }
        }
        return true;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            mTotalRunTime++;
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
                        for (MTEHatchEnergy tHatch : validMTEList(mEnergyHatches)) {
                            long energyToMove = GTValues.V[tier()] / 16;
                            if (aBaseMetaTileEntity.getStoredEU() + energyToMove < maxEUStore()
                                && tHatch.getBaseMetaTileEntity()
                                    .decreaseStoredEnergyUnits(energyToMove, false)) {
                                aBaseMetaTileEntity.increaseStoredEnergyUnits(energyToMove, true);
                            }
                        }
                    }
                    if (this.mEUStore <= 0 && mMaxProgresstime > 0) {
                        stopMachine(ShutDownReasonRegistry.POWER_LOSS);
                    }
                    if (mMaxProgresstime > 0) {
                        this.getBaseMetaTileEntity()
                            .decreaseStoredEnergyUnits(-mEUt, true);
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
                            mLastWorkingTick = mTotalRunTime;
                            if (mOutputFluids != null && mOutputFluids.length > 0) {
                                try {
                                    GTMod.achievements.issueAchievementHatchFluid(
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
                                    if (this.mEUStore < this.mLastRecipe.mSpecialValue + this.mEUt) {
                                        stopMachine(ShutDownReasonRegistry.POWER_LOSS);
                                    }
                                    aBaseMetaTileEntity
                                        .decreaseStoredEnergyUnits(this.mLastRecipe.mSpecialValue + this.mEUt, true);
                                }
                            }
                            if (mMaxProgresstime <= 0) mEfficiency = Math.max(0, mEfficiency - 1000);
                        }
                    }
                } else if (aBaseMetaTileEntity.isAllowedToWork()) {
                    this.mLastRecipe = null;
                    stopMachine(ShutDownReasonRegistry.STRUCTURE_INCOMPLETE);
                }
            }
            setErrorDisplayID((getErrorDisplayID() & ~127) | (mMachine ? 0 : 64));
            aBaseMetaTileEntity.setActive(mMaxProgresstime > 0);
        } else {
            doActivitySound(getActivitySoundLoop());
        }
    }

    @Override
    public boolean drainEnergyInput(long aEU) {
        return false;
    }

    @Override
    public void stopMachine(@NotNull ShutDownReason reason) {
        super.stopMachine(reason);
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

        return new String[] {
            EnumChatFormatting.BLUE + StatCollector.translateToLocal("GT5U.infodata.fusion_reactor_mk")
                + " "
                + EnumChatFormatting.RESET
                + tier,
            StatCollector.translateToLocal("GT5U.fusion.req") + ": "
                + EnumChatFormatting.RED
                + GTUtility.formatNumbers(powerRequired)
                + EnumChatFormatting.RESET
                + "EU/t",
            StatCollector.translateToLocal("GT5U.multiblock.energy") + ": "
                + EnumChatFormatting.GREEN
                + GTUtility.formatNumbers(mEUStore)
                + EnumChatFormatting.RESET
                + " EU / "
                + EnumChatFormatting.YELLOW
                + GTUtility.formatNumbers(maxEUStore())
                + EnumChatFormatting.RESET
                + " EU",
            StatCollector.translateToLocal("GT5U.fusion.plasma") + ": "
                + EnumChatFormatting.YELLOW
                + GTUtility.formatNumbers(plasmaOut)
                + EnumChatFormatting.RESET
                + "L/t" };
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 7, 1, 12);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 7, 1, 12, elementBudget, env, false, true);
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_FUSION_LOOP;
    }

    @Override
    public boolean doesBindPlayerInventory() {
        return false;
    }

    @Override
    public void addGregTechLogo(ModularWindow.Builder builder) {
        builder.widget(
            new DrawableWidget().setDrawable(getGUITextureSet().getGregTechLogo())
                .setSize(17, 17)
                .setPos(155, 145));
    }

    @Override
    public GUITextureSet getGUITextureSet() {
        return new GUITextureSet().setMainBackground(GTUITextures.BACKGROUND_FUSION_COMPUTER);
    }

    @Override
    public int getGUIWidth() {
        return 176;
    }

    @Override
    public int getGUIHeight() {
        return 166;
    }

    protected static final NumberFormatMUI numberFormat = new NumberFormatMUI();
    protected long clientEU;

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder
            .widget(
                new TextWidget(GTUtility.trans("138", "Incomplete Structure.")).setTextAlignment(Alignment.CenterLeft)
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> !mMachine)
                    .setPos(10, 8))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> mMachine, val -> mMachine = val))
            .widget(
                new TextWidget(StatCollector.translateToLocal("GT5U.gui.text.fusion_computer.hit_to_start"))
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setTextAlignment(Alignment.Center)
                    .setEnabled(widget -> getErrorDisplayID() == 0 && !getBaseMetaTileEntity().isActive())
                    .setPos(-getGUIWidth() / 2, 170)
                    .setSize(getGUIWidth() * 2, 9))
            .widget(new FakeSyncWidget.IntegerSyncer(() -> getErrorDisplayID(), val -> setErrorDisplayID(val)))
            .widget(
                new FakeSyncWidget.BooleanSyncer(
                    () -> getBaseMetaTileEntity().isActive(),
                    val -> getBaseMetaTileEntity().setActive(val)))
            .widget(
                new TextWidget(StatCollector.translateToLocal("GT5U.gui.text.fusion_computer.running_perfectly"))
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setTextAlignment(Alignment.Center)
                    .setEnabled(widget -> getErrorDisplayID() == 0 && getBaseMetaTileEntity().isActive())
                    .setPos(0, 170)
                    .setSize(getGUIWidth(), 9))
            .widget(new FakeSyncWidget.IntegerSyncer(this::getErrorDisplayID, this::setErrorDisplayID))
            .widget(
                new ProgressBar()
                    .setProgress(
                        () -> (float) getBaseMetaTileEntity().getStoredEU() / getBaseMetaTileEntity().getEUCapacity())
                    .setDirection(ProgressBar.Direction.RIGHT)
                    .setTexture(GTUITextures.PROGRESSBAR_STORED_EU, 147)
                    .setPos(5, 156)
                    .setSize(147, 5))
            .widget(new TextWidget().setStringSupplier(() -> {
                if (clientEU > 160_000_000L && clientEU < 160_010_000L) {
                    clientEU = 160_000_000L;
                }
                if (clientEU > 320_000_000L && clientEU < 320_010_000L) {
                    clientEU = 320_000_000L;
                }
                if (clientEU > 640_000_000L && clientEU < 640_010_000L) {
                    clientEU = 640_000_000L;
                }
                if (clientEU > 5_120_000_000L && clientEU < 5_120_080_000L) {
                    clientEU = 5_120_000_000L;
                }
                return numberFormat.format(clientEU) + " EU";
            })
                .setDefaultColor(COLOR_TEXT_RED.get())
                .setTextAlignment(Alignment.Center)
                .setScale(0.5f)
                .setPos(5, 157)
                .setSize(147, 5))
            .widget(new FakeSyncWidget.LongSyncer(() -> getBaseMetaTileEntity().getStoredEU(), val -> clientEU = val))
            .widget(
                new ButtonWidget().setNEITransferRect(
                    RecipeMaps.fusionRecipes.getFrontend()
                        .getUIProperties().neiTransferRectId)
                    .setBackground(GTUITextures.BUTTON_STANDARD, GTUITextures.OVERLAY_BUTTON_NEI)
                    .setPos(154, 4)
                    .setSize(18, 18));
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public Set<VoidingMode> getAllowedVoidingModes() {
        return VoidingMode.FLUID_ONLY_MODES;
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }
}

package goodgenerator.blocks.tileEntity.base;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GTRecipeConstants.FUSION_THRESHOLD;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.filterByMTETier;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.api.util.GTUtility.validMTEList;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.math.BigInteger;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.NumberFormatMUI;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IOverclockDescriptionProvider;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEHatch;
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
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.ParallelHelper;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.tileentities.machines.IDualInputHatch;
import gregtech.common.tileentities.machines.multi.drone.MTEHatchDroneDownLink;
import tectech.thing.metaTileEntity.hatch.MTEHatchEnergyMulti;
import tectech.thing.metaTileEntity.multi.base.INameFunction;
import tectech.thing.metaTileEntity.multi.base.IStatusFunction;
import tectech.thing.metaTileEntity.multi.base.LedStatus;
import tectech.thing.metaTileEntity.multi.base.Parameters;

public abstract class MTELargeFusionComputer extends MTETooltipMultiBlockBaseEM
    implements IConstructable, ISurvivalConstructable, IOverclockDescriptionProvider {

    public Parameters.Group.ParameterIn batchSetting;

    /** Name of the batch setting */
    public static final INameFunction<MTELargeFusionComputer> BATCH_SETTING_NAME = (base,
        p) -> translateToLocal("batch_mode.cfgi.0"); // Batch size
    /** Status of the batch setting */
    public static final IStatusFunction<MTELargeFusionComputer> BATCH_STATUS = (base, p) -> LedStatus
        .fromLimitsInclusiveOuterBoundary(p.get(), 1, 0, 32, 128);

    public static final String MAIN_NAME = "largeFusion";
    public static final int M = 1_000_000;
    public GTRecipe lastRecipe;
    public int para;
    protected OverclockDescriber overclockDescriber;
    private static final ClassValue<IStructureDefinition<MTELargeFusionComputer>> STRUCTURE_DEFINITION = new ClassValue<>() {

        @Override
        protected IStructureDefinition<MTELargeFusionComputer> computeValue(Class<?> type) {
            return StructureDefinition.<MTELargeFusionComputer>builder()
                .addShape(MAIN_NAME, transpose(new String[][] { L0, L1, L2, L3, L2, L1, L0 }))
                .addElement('H', lazy(x -> ofBlock(x.getCoilBlock(), x.getCoilMeta())))
                .addElement('C', lazy(x -> ofBlock(x.getCasingBlock(), x.getCasingMeta())))
                .addElement('B', lazy(x -> ofBlock(x.getGlassBlock(), x.getGlassMeta())))
                .addElement(
                    'I',
                    lazy(
                        x -> HatchElementBuilder.<MTELargeFusionComputer>builder()
                            .atLeast(
                                gregtech.api.enums.HatchElement.InputHatch
                                    // Input Bus for crib support
                                    .or(gregtech.api.enums.HatchElement.InputBus),
                                gregtech.api.enums.HatchElement.OutputHatch)
                            .casingIndex(x.textureIndex())
                            .hint(1)
                            .buildAndChain(x.getGlassBlock(), x.getGlassMeta())))
                .addElement(
                    'E',
                    lazy(
                        x -> HatchElementBuilder.<MTELargeFusionComputer>builder()
                            .anyOf(
                                tectech.thing.metaTileEntity.multi.base.TTMultiblockBase.HatchElement.EnergyMulti
                                    .or(gregtech.api.enums.HatchElement.Energy))
                            .adder(MTELargeFusionComputer::addEnergyInjector)
                            .casingIndex(x.textureIndex())
                            .hatchItemFilterAnd(x2 -> filterByMTETier(x2.energyHatchTier(), Integer.MAX_VALUE))
                            .hint(2)
                            .buildAndChain(x.getCasingBlock(), x.getCasingMeta())))
                .addElement('F', lazy(x -> ofFrame(x.getFrameBox())))
                .addElement(
                    'D',
                    lazy(
                        x -> buildHatchAdder(MTELargeFusionComputer.class).adder(MTELargeFusionComputer::addDroneHatch)
                            .hatchId(9401)
                            .casingIndex(x.textureIndex())
                            .hint(3)
                            .buildAndChain(x.getCasingBlock(), x.getCasingMeta())))
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

    public MTELargeFusionComputer(String name) {
        super(name);
        useLongPower = true;
        this.overclockDescriber = createOverclockDescriber();
    }

    public MTELargeFusionComputer(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
        useLongPower = true;
        this.overclockDescriber = createOverclockDescriber();
    }

    protected OverclockDescriber createOverclockDescriber() {
        return new FusionOverclockDescriber((byte) tier(), capableStartupCanonical());
    }

    @Nullable
    @Override
    public OverclockDescriber getOverclockDescriber() {
        return overclockDescriber;
    }

    public abstract int tier();

    @Override
    public long maxEUStore() {
        return capableStartupCanonical() * (Math.min(32, this.mEnergyHatches.size() + this.eEnergyMulti.size())) / 32L;
    }

    /**
     * Unlike {@link #maxEUStore()}, this provides theoretical limit of startup EU, without considering the amount of
     * hatches nor the room for extra energy. Intended for simulation.
     */
    public abstract long capableStartupCanonical();

    public abstract Block getCasingBlock();

    public abstract int getCasingMeta();

    public abstract Block getCoilBlock();

    public abstract int getCoilMeta();

    public abstract Block getGlassBlock();

    public abstract int getGlassMeta();

    public abstract int energyHatchTier();

    public abstract Materials getFrameBox();

    public abstract int getMaxPara();

    public abstract int extraPara(long startEnergy);

    public int textureIndex() {
        return 53;
    }

    public abstract ITexture getTextureOverlay();

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
        return structureCheck_EM(MAIN_NAME, 23, 3, 40) && mInputHatches.size() + mDualInputHatches.size() != 0
            && !mOutputHatches.isEmpty()
            && (mEnergyHatches.size() + eEnergyMulti.size()) != 0;
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        if (getMaxBatchSize() == 1) {
            parametrization.trySetParameters(batchSetting.hatchId(), batchSetting.parameterId(), 128);
            GTUtility.sendChatTrans(aPlayer, "misc.BatchModeTextOn");
        } else {
            parametrization.trySetParameters(batchSetting.hatchId(), batchSetting.parameterId(), 1);
            GTUtility.sendChatTrans(aPlayer, "misc.BatchModeTextOff");
        }
        return true;
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        structureBuild_EM(MAIN_NAME, 23, 3, 40, itemStack, b);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);
        return survivalBuildPiece(MAIN_NAME, stackSize, 23, 3, 40, realBudget, env, false, true);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            mTotalRunTime++;
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
                    if (aBaseMetaTileEntity.getStoredEU() <= 0 && mMaxProgresstime > 0) {
                        stopMachine(ShutDownReasonRegistry.POWER_LOSS);
                    }

                    long energyLimit = getSingleHatchPower();
                    List<MTEHatch> hatches = getExoticAndNormalEnergyHatchList();
                    for (MTEHatch hatch : validMTEList(hatches)) {
                        long consumableEnergy = Math.min(hatch.getEUVar(), energyLimit);
                        long receivedEnergy = Math
                            .min(consumableEnergy, maxEUStore() - aBaseMetaTileEntity.getStoredEU());
                        if (receivedEnergy > 0) {
                            hatch.getBaseMetaTileEntity()
                                .decreaseStoredEnergyUnits(receivedEnergy, false);
                            aBaseMetaTileEntity.increaseStoredEnergyUnits(receivedEnergy, true);
                        }
                    }

                    if (mMaxProgresstime > 0) {
                        this.getBaseMetaTileEntity()
                            .decreaseStoredEnergyUnits(-lEUt, true);
                        if (mMaxProgresstime > 0 && ++mProgresstime >= mMaxProgresstime) {
                            if (mOutputItems != null) addItemOutputs(mOutputItems);
                            if (mOutputFluids != null)
                                for (FluidStack tStack : mOutputFluids) if (tStack != null) addOutput(tStack);
                            mEfficiency = Math
                                .max(0, Math.min(mEfficiency + mEfficiencyIncrease, getMaxEfficiency(mInventory[1])));
                            mOutputItems = null;
                            mOutputFluids = null;
                            mProgresstime = 0;
                            mMaxProgresstime = 0;
                            mEfficiencyIncrease = 0;
                            recipesDone += Math.max(processingLogic.getCurrentParallels(), lastParallel);
                            mLastWorkingTick = mTotalRunTime;
                            para = 0;
                            if (aBaseMetaTileEntity.isAllowedToWork()) checkRecipe();
                        }
                    } else {
                        if (aTick % 100 == 0 || aBaseMetaTileEntity.hasWorkJustBeenEnabled()
                            || aBaseMetaTileEntity.hasInventoryBeenModified()) {
                            turnCasingActive(mMaxProgresstime > 0);
                            if (aBaseMetaTileEntity.isAllowedToWork()) {
                                if (checkRecipe()) {
                                    if (aBaseMetaTileEntity.getStoredEU()
                                        < this.lastRecipe.getMetadataOrDefault(FUSION_THRESHOLD, 0L) + this.lEUt) {
                                        mMaxProgresstime = 0;
                                        turnCasingActive(false);
                                        stopMachine(ShutDownReasonRegistry.POWER_LOSS);
                                    }
                                    getBaseMetaTileEntity().decreaseStoredEnergyUnits(
                                        this.lastRecipe.getMetadataOrDefault(FUSION_THRESHOLD, 0L) + this.lEUt,
                                        false);
                                }
                            }
                            if (mMaxProgresstime <= 0) mEfficiency = Math.max(0, mEfficiency - 1000);
                        }
                    }
                } else if (aBaseMetaTileEntity.isAllowedToWork()) {
                    turnCasingActive(false);
                    this.lastRecipe = null;
                    stopMachine(ShutDownReasonRegistry.STRUCTURE_INCOMPLETE);
                }
            }
            setErrorDisplayID((getErrorDisplayID() & ~127) | (mMachine ? 0 : 64));
            aBaseMetaTileEntity.setActive(mMaxProgresstime > 0);
        } else {
            doActivitySound(getActivitySoundLoop());
        }
    }

    /**
     * @return The power one hatch can deliver to the reactor
     */
    protected long getSingleHatchPower() {
        return GTValues.V[tier()] * getMaxPara() * extraPara(100) / 32;
    }

    public boolean turnCasingActive(boolean status) {
        if (this.mEnergyHatches != null) {
            for (MTEHatchEnergy hatch : this.mEnergyHatches) {
                hatch.updateTexture(status ? 52 : 53);
            }
        }
        if (this.eEnergyMulti != null) {
            for (MTEHatchEnergyMulti hatch : this.eEnergyMulti) {
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
        if (this.mDualInputHatches != null) {
            for (IDualInputHatch hatch : this.mDualInputHatches) {
                hatch.updateTexture(status ? 52 : 53);
            }
        }
        return true;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) return new ITexture[] { TextureFactory.builder()
            .addIcon(MACHINE_CASING_FUSION_GLASS)
            .extFacing()
            .build(), getTextureOverlay() };
        if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(52) };
        return new ITexture[] { TextureFactory.builder()
            .addIcon(MACHINE_CASING_FUSION_GLASS)
            .extFacing()
            .build() };
    }

    @Override
    public void onMachineBlockUpdate() {
        mUpdate = 100;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.fusionRecipes;
    }

    @Override
    public int getRecipeCatalystPriority() {
        return -2;
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
                long powerToStart = recipe.getMetadataOrDefault(GTRecipeConstants.FUSION_THRESHOLD, 0L);
                if (!mRunningOnLoad) {
                    if (powerToStart > maxEUStore()) {
                        return CheckRecipeResultRegistry.insufficientStartupPower(BigInteger.valueOf(powerToStart));
                    }
                    if (recipe.mEUt > GTValues.V[tier()]) {
                        return CheckRecipeResultRegistry.insufficientPower(recipe.mEUt);
                    }
                }
                maxParallel = getMaxPara() * extraPara(powerToStart);
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @NotNull
            @Override
            public CheckRecipeResult process() {
                CheckRecipeResult result = super.process();
                if (mRunningOnLoad) mRunningOnLoad = false;
                turnCasingActive(result.wasSuccessful());
                if (result.wasSuccessful()) {
                    MTELargeFusionComputer.this.lastRecipe = lastRecipe;
                } else {
                    MTELargeFusionComputer.this.lastRecipe = null;
                }
                para = getCurrentParallels();
                return result;
            }
        };
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(GTValues.V[tier()]);
        logic.setAvailableAmperage(getSingleHatchPower() * 32 / GTValues.V[tier()]);
        logic.setUnlimitedTierSkips();
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
        if (aMetaTileEntity instanceof MTEHatchEnergy tHatch) {
            if (tHatch.getTierForStructure() < energyHatchTier()) return false;
            tHatch.updateTexture(aBaseCasingIndex);
            return mEnergyHatches.add(tHatch);
        } else if (aMetaTileEntity instanceof MTEHatchEnergyMulti tHatch) {
            if (tHatch.getTierForStructure() < energyHatchTier()) return false;
            tHatch.updateTexture(aBaseCasingIndex);
            return eEnergyMulti.add(tHatch);
        }
        return false;
    }

    private boolean addDroneHatch(IGregTechTileEntity aBaseMetaTileEntity, int aBaseCasingIndex) {
        if (aBaseMetaTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aBaseMetaTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (!(aMetaTileEntity instanceof MTEHatchDroneDownLink tHatch)) return false;
        tHatch.updateTexture(aBaseCasingIndex);
        return addToMachineList(aBaseMetaTileEntity, aBaseCasingIndex);
    }

    @Override
    public IStructureDefinition<MTELargeFusionComputer> getStructure_EM() {
        return STRUCTURE_DEFINITION.get(getClass());
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_FUSION_LOOP;
    }


    protected long energyStorageCache;
    protected static final NumberFormatMUI numberFormat = new NumberFormatMUI();

    @Override
    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        super.drawTexts(screenElements, inventorySlot);

        screenElements
            .widget(
                new TextWidget()
                    .setStringSupplier(
                        () -> StatCollector.translateToLocal("gui.LargeFusion.0") + " "
                            + numberFormat.format(energyStorageCache)
                            + " EU")
                    .setTextAlignment(Alignment.CenterLeft)
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> getErrorDisplayID() == 0))
            .widget(new FakeSyncWidget.LongSyncer(this::maxEUStore, val -> energyStorageCache = val))
            .widget(
                new TextWidget()
                    .setStringSupplier(
                        () -> StatCollector.translateToLocal("gui.LargeFusion.1") + " "
                            + numberFormat.format(getEUVar())
                            + " EU")
                    .setTextAlignment(Alignment.CenterLeft)
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> getErrorDisplayID() == 0))
            .widget(new FakeSyncWidget.LongSyncer(this::getEUVar, this::setEUVar));
    }

    @Override
    protected void parametersInstantiation_EM() {
        batchSetting = parametrization.getGroup(9, false)
            .makeInParameter(1, 1, BATCH_SETTING_NAME, BATCH_STATUS);
    }

    @Override
    protected int getMaxBatchSize() {
        // Batch size 1~128
        return (int) Math.min(Math.max(batchSetting.get(), 1.0D), 128.0D);
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public boolean getDefaultBatchMode() {
        return true;
    }

    protected String createParallelText() {
        return "Has " + EnumChatFormatting.WHITE
            + "(1 + "
            + EnumChatFormatting.LIGHT_PURPLE
            + "Machine Tier"
            + EnumChatFormatting.WHITE
            + " - "
            + EnumChatFormatting.GREEN
            + "Recipe Tier"
            + EnumChatFormatting.WHITE
            + ") * 64"
            + EnumChatFormatting.GOLD
            + " Parallels";
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
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
        "         CCHCCCC               CCCCHCC         ", "          ECHHCCCCC FCCDCCF CCCCCHHCE          ",
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

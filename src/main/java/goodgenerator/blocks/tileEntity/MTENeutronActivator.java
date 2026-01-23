package goodgenerator.blocks.tileEntity;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

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

import goodgenerator.api.recipe.GoodGeneratorRecipeMaps;
import goodgenerator.blocks.tileEntity.GTMetaTileEntity.MTENeutronAccelerator;
import goodgenerator.blocks.tileEntity.GTMetaTileEntity.MTENeutronSensor;
import goodgenerator.blocks.tileEntity.base.MTETooltipMultiBlockBaseEM;
import goodgenerator.loader.Loaders;
import goodgenerator.util.DescTextLocalization;
import goodgenerator.util.ItemRefer;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.objects.XSTR;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import tectech.thing.metaTileEntity.multi.base.INameFunction;
import tectech.thing.metaTileEntity.multi.base.IStatusFunction;
import tectech.thing.metaTileEntity.multi.base.LedStatus;
import tectech.thing.metaTileEntity.multi.base.Parameters;

public class MTENeutronActivator extends MTETooltipMultiBlockBaseEM implements IConstructable, ISurvivalConstructable {

    public Parameters.Group.ParameterIn batchSetting;

    /** Name of the batch setting */
    public static final INameFunction<MTENeutronActivator> BATCH_SETTING_NAME = (base,
        p) -> translateToLocal("batch_mode.cfgi.0"); // Batch size
    /** Status of the batch setting */
    public static final IStatusFunction<MTENeutronActivator> BATCH_STATUS = (base, p) -> LedStatus
        .fromLimitsInclusiveOuterBoundary(p.get(), 1, 0, 32, 128);
    protected static IStructureDefinition<MTENeutronActivator> multiDefinition = null;
    protected final ArrayList<MTENeutronAccelerator> mNeutronAccelerator = new ArrayList<>();
    protected final ArrayList<MTENeutronSensor> mNeutronSensor = new ArrayList<>();
    protected int casingAmount = 0;
    protected int height = 0;
    protected int eV = 0, mCeil = 0, mFloor = 0;
    protected static final NumberFormatMUI numberFormat;
    static {
        numberFormat = new NumberFormatMUI();
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setMaximumFractionDigits(2);
    }
    final XSTR R = new XSTR();

    private static final IIconContainer textureFontOn = new Textures.BlockIcons.CustomIcon("icons/NeutronActivator_On");
    private static final IIconContainer textureFontOn_Glow = new Textures.BlockIcons.CustomIcon(
        "icons/NeutronActivator_On_GLOW");
    private static final IIconContainer textureFontOff = new Textures.BlockIcons.CustomIcon(
        "icons/NeutronActivator_Off");
    private static final IIconContainer textureFontOff_Glow = new Textures.BlockIcons.CustomIcon(
        "icons/NeutronActivator_Off_GLOW");

    protected final String NA_BOTTOM = mName + "buttom";
    protected final String NA_MID = mName + "mid";
    protected final String NA_TOP = mName + "top";

    public MTENeutronActivator(String name) {
        super(name);
    }

    public MTENeutronActivator(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected OverclockCalculator createOverclockCalculator(@NotNull GTRecipe recipe) {
                return OverclockCalculator.ofNoOverclock(recipe)
                    .setDuration((int) Math.ceil(recipe.mDuration * GTUtility.powInt(0.9f, height - 4)))
                    .setDurationUnderOneTickSupplier(() -> recipe.mDuration * GTUtility.powInt(0.9f, height - 4));
            }

            @NotNull
            @Override
            public CheckRecipeResult process() {
                CheckRecipeResult result = super.process();
                if (!result.wasSuccessful()) {
                    return result;
                }
                mFloor = (lastRecipe.mSpecialValue % 10000) * 1000000;
                mCeil = (lastRecipe.mSpecialValue / 10000) * 1000000;
                if (eV > mCeil || eV < mFloor) {
                    overwriteOutputItems(ItemRefer.Radioactive_Waste.get(4));
                }
                // NA does not consume power, its hatches do. Set it to 0 to be sure
                calculatedEut = 0;
                return result;
            }
        };
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        // NA does not use power, to prevent GT_ParallelHelper from failing we trick it into thinking
        // we have infinite power
        logic.setAvailableVoltage(Long.MAX_VALUE);
        logic.setAvailableAmperage(1);
        logic.setUnlimitedTierSkips();
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
    public void loadNBTData(NBTTagCompound aNBT) {
        eV = aNBT.getInteger("mKeV");
        mCeil = aNBT.getInteger("mCeil");
        mFloor = aNBT.getInteger("mFloor");
        height = aNBT.getInteger("height");
        super.loadNBTData(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("mKeV", eV);
        aNBT.setInteger("mCeil", mCeil);
        aNBT.setInteger("mFloor", mFloor);
        aNBT.setInteger("height", height);
        super.saveNBTData(aNBT);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GoodGeneratorRecipeMaps.neutronActivatorRecipes;
    }

    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Neutron Activator, NA")
            .addInfo("Superluminal-velocity Motion.")
            .addInfo("The minimum height of the Speeding Pipe Casing is 4")
            .addInfo("Per extra Speeding Pipe Casing will give time discount")
            .addInfo("But it will reduce the Neutron Accelerator efficiency")
            .addInfo("You need to input energy to the Neutron Accelerator to get it running")
            .addInfo("It will output correct products with Specific Neutron Kinetic Energy")
            .addInfo("Otherwise it will output trash")
            .addInfo("The Neutron Kinetic Energy will decrease 72KeV/s when no Neutron Accelerator is running")
            .addInfo(
                "It will explode when the Neutron Kinetic Energy is over" + EnumChatFormatting.RED
                    + " 1200MeV"
                    + EnumChatFormatting.GRAY
                    + ".")
            .addInfo("Inputting Graphite/Beryllium dust can reduce 10MeV per dust immediately.")
            .addController("Front bottom")
            .addCasingInfoRange("Clean Stainless Steel Machine Casing", 7, 31, false)
            .addCasingInfoExactly("Processor Machine Casing", 18, false)
            .addCasingInfoMin("Steel Frame Box", 16, false)
            .addCasingInfoMin("Speeding Pipe Casing", 4, false)
            .addCasingInfoMin("Any Tiered Glass", 32, false)
            .addInputHatch("Hint Block Number 1")
            .addInputBus("Hint Block Number 1")
            .addOutputHatch("Hint Block Number 2")
            .addOutputBus("Hint Block Number 2")
            .addMaintenanceHatch("Hint Block Number 2")
            .addOtherStructurePart("Neutron Accelerator", "Hint Block Number 2")
            .addOtherStructurePart("Neutron Sensor", "Hint Block Number 2")
            .toolTipFinisher();
        return tt;
    }

    @Override
    public IStructureDefinition<MTENeutronActivator> getStructure_EM() {
        if (multiDefinition == null) {
            multiDefinition = StructureDefinition.<MTENeutronActivator>builder()
                .addShape(NA_TOP, transpose(new String[][] { { "CCCCC", "CDDDC", "CDDDC", "CDDDC", "CCCCC" } }))
                .addShape(NA_MID, transpose(new String[][] { { "F   F", " GGG ", " GPG ", " GGG ", "F   F" } }))
                .addShape(NA_BOTTOM, transpose(new String[][] { { "XX~XX", "XDDDX", "XDDDX", "XDDDX", "XXXXX" } }))
                .addElement(
                    'C',
                    ofChain(
                        buildHatchAdder(MTENeutronActivator.class)
                            .atLeast(
                                gregtech.api.enums.HatchElement.InputHatch,
                                gregtech.api.enums.HatchElement.InputBus,
                                gregtech.api.enums.HatchElement.Maintenance)
                            .casingIndex(49)
                            .hint(1)
                            .build(),
                        onElementPass(MTENeutronActivator::onCasingFound, ofBlock(GregTechAPI.sBlockCasings4, 1))))
                .addElement('D', ofBlock(GregTechAPI.sBlockCasings2, 6))
                .addElement('F', ofFrame(Materials.Steel))
                .addElement('G', chainAllGlasses())
                .addElement('P', ofBlock(Loaders.speedingPipe, 0))
                .addElement(
                    'X',
                    ofChain(
                        buildHatchAdder(MTENeutronActivator.class)
                            .atLeast(
                                gregtech.api.enums.HatchElement.OutputHatch,
                                gregtech.api.enums.HatchElement.OutputBus,
                                gregtech.api.enums.HatchElement.Maintenance,
                                NeutronHatchElement.NeutronAccelerator,
                                NeutronHatchElement.NeutronSensor)
                            .casingIndex(49)
                            .hint(2)
                            .build(),
                        onElementPass(MTENeutronActivator::onCasingFound, ofBlock(GregTechAPI.sBlockCasings4, 1))))
                .build();
        }
        return multiDefinition;
    }

    @Override
    protected void clearHatches_EM() {
        super.clearHatches_EM();
        this.mNeutronAccelerator.clear();
        this.mNeutronSensor.clear();
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        this.casingAmount = 0;
        if (!structureCheck_EM(NA_BOTTOM, 2, 0, 0)) return false;
        height = 0;
        while (structureCheck_EM(NA_MID, 2, height + 1, 0)) {
            height++;
        }
        if (height < 4) return false;
        return structureCheck_EM(NA_TOP, 2, height + 1, 0) && casingAmount >= 7;
    }

    public final boolean addAcceleratorAndSensor(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof MTENeutronAccelerator) {
                ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                return this.mNeutronAccelerator.add((MTENeutronAccelerator) aMetaTileEntity);
            } else if (aMetaTileEntity instanceof MTENeutronSensor) {
                ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                return this.mNeutronSensor.add((MTENeutronSensor) aMetaTileEntity);
            }
        }
        return false;
    }

    public int maxNeutronKineticEnergy() {
        return 1200000000;
    }

    public int getCurrentNeutronKineticEnergy() {
        return eV;
    }

    @Override
    protected void parametersInstantiation_EM() {
        batchSetting = parametrization.getGroup(9, true)
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

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTENeutronActivator(this.mName);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        boolean anyWorking = false;
        if (aBaseMetaTileEntity.isServerSide()) {
            if (this.eV > 0 && (aTick % 20 == 0 || eV > mCeil)) {
                tryUseModerator();
            }

            for (MTENeutronAccelerator tHatch : mNeutronAccelerator) {
                if (tHatch.getBaseMetaTileEntity()
                    .isActive() && this.getRepairStatus() == this.getIdealStatus()) {
                    anyWorking = true;
                    this.eV += Math.max(
                        (R.nextInt(tHatch.getMaxEUConsume() + 1) + tHatch.getMaxEUConsume()) * 10
                            * GTUtility.powInt(0.95, height - 4),
                        10);
                }
            }
            if (!anyWorking) {
                if (this.eV >= 72000 && aTick % 20 == 0) {
                    this.eV -= 72000;
                } else if (this.eV > 0 && aTick % 20 == 0) {
                    this.eV = 0;
                }
            }
            if (this.eV < 0) this.eV = 0;
            if (this.eV > maxNeutronKineticEnergy()) doExplosion(4 * 32);

            for (MTENeutronSensor tHatch : mNeutronSensor) {
                tHatch.updateRedstoneOutput(this.eV);
            }

            if (mProgresstime < mMaxProgresstime && (eV > mCeil || eV < mFloor)) {
                this.mOutputFluids = null;
                this.mOutputItems = new ItemStack[] { ItemRefer.Radioactive_Waste.get(4) };
            }
        }
    }

    private void tryUseModerator() {
        startRecipeProcessing();
        for (ItemStack input : getStoredInputs()) {
            if (input.isItemEqual(Materials.Graphite.getDust(1)) || input.isItemEqual(Materials.Beryllium.getDust(1))) {
                int consume = Math.min(this.eV / 10000000, input.stackSize);
                depleteInput(GTUtility.copyAmount(consume, input));
                this.eV -= 10000000 * consume;
            }
        }
        endRecipeProcessing();
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM(NA_BOTTOM, 2, 0, 0, stackSize, hintsOnly);
        int heights = stackSize.stackSize + 3;
        structureBuild_EM(NA_TOP, 2, heights + 1, 0, stackSize, hintsOnly);
        while (heights > 0) {
            structureBuild_EM(NA_MID, 2, heights, 0, stackSize, hintsOnly);
            heights--;
        }
    }

    @Override
    public String[] getStructureDescription(ItemStack itemStack) {
        return DescTextLocalization.addText("NeutronActivator.hint", 7);
    }

    @Override
    public String[] getInfoData() {
        int currentNKEInput = 0;
        boolean anyWorking = false;
        for (MTENeutronAccelerator tHatch : mNeutronAccelerator) {
            if (tHatch.getBaseMetaTileEntity()
                .isActive()) {
                currentNKEInput += (R.nextInt(tHatch.getMaxEUConsume() + 1) + tHatch.getMaxEUConsume()) * 10
                    * GTUtility.powInt(0.95, height - 4);
                anyWorking = true;
            }
        }
        if (!anyWorking) currentNKEInput = -72000;
        return new String[] {
            StatCollector.translateToLocalFormatted(
                "gg.scanner.info.neutron_activator.progress",
                EnumChatFormatting.GREEN + Integer.toString(this.mProgresstime / 20) + EnumChatFormatting.RESET,
                EnumChatFormatting.YELLOW + Integer.toString(this.mMaxProgresstime / 20) + EnumChatFormatting.RESET),
            StatCollector.translateToLocalFormatted(
                "gg.scanner.info.neutron_activator.input",
                EnumChatFormatting.GREEN + formatNumber(currentNKEInput) + EnumChatFormatting.RESET),
            StatCollector.translateToLocal("scanner.info.NA") + " "
                + EnumChatFormatting.LIGHT_PURPLE
                + formatNumber(getCurrentNeutronKineticEnergy())
                + EnumChatFormatting.RESET
                + "eV",
            StatCollector.translateToLocal("GT5U.multiblock.recipesDone") + ": "
                + EnumChatFormatting.GREEN
                + formatNumber(recipesDone)
                + EnumChatFormatting.RESET };
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(49), TextureFactory.builder()
                .addIcon(textureFontOn)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(textureFontOn_Glow)
                    .extFacing()
                    .glow()
                    .build() };
            else return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(49), TextureFactory.builder()
                .addIcon(textureFontOff)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(textureFontOff_Glow)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(49) };
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;

        int built = survivalBuildPiece(NA_BOTTOM, stackSize, 2, 0, 0, elementBudget, env, false, true);
        if (built >= 0) return built;
        int heights = stackSize.stackSize + 3;
        for (int i = 1; i <= heights; i++) {
            built = survivalBuildPiece(NA_MID, stackSize, 2, i, 0, elementBudget, env, false, true);
            if (built >= 0) return built;
        }
        return survivalBuildPiece(NA_TOP, stackSize, 2, heights + 1, 0, elementBudget, env, false, true);
    }

    protected void onCasingFound() {
        casingAmount++;
    }

    @Override
    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        super.drawTexts(screenElements, inventorySlot);

        screenElements
            .widget(
                new TextWidget(StatCollector.translateToLocal("gui.NeutronActivator.0"))
                    .setTextAlignment(Alignment.CenterLeft)
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> getErrorDisplayID() == 0))
            .widget(
                new TextWidget().setStringSupplier(() -> numberFormat.format(eV / 1_000_000d) + " MeV")
                    .setTextAlignment(Alignment.CenterLeft)
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> getErrorDisplayID() == 0))
            .widget(new FakeSyncWidget.IntegerSyncer(() -> eV, val -> eV = val));
    }

    private enum NeutronHatchElement implements IHatchElement<MTENeutronActivator> {

        NeutronSensor(MTENeutronActivator::addAcceleratorAndSensor, MTENeutronSensor.class) {

            @Override
            public long count(MTENeutronActivator t) {
                return t.mNeutronSensor.size();
            }
        },
        NeutronAccelerator(MTENeutronActivator::addAcceleratorAndSensor, MTENeutronAccelerator.class) {

            @Override
            public long count(MTENeutronActivator t) {
                return t.mNeutronAccelerator.size();
            }
        };

        private final List<Class<? extends IMetaTileEntity>> mteClasses;
        private final IGTHatchAdder<MTENeutronActivator> adder;

        @SafeVarargs
        NeutronHatchElement(IGTHatchAdder<MTENeutronActivator> adder, Class<? extends IMetaTileEntity>... mteClasses) {
            this.mteClasses = Collections.unmodifiableList(Arrays.asList(mteClasses));
            this.adder = adder;
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return mteClasses;
        }

        public IGTHatchAdder<? super MTENeutronActivator> adder() {
            return adder;
        }
    }
}

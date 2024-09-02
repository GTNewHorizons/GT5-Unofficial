package tectech.thing.metaTileEntity.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.HatchElement.Dynamo;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.api.util.GTUtility.filterValidMTEs;
import static java.lang.Math.min;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.util.Vec3Impl;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchDynamo;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchMaintenance;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReason;
import tectech.TecTech;
import tectech.loader.NetworkDispatcher;
import tectech.mechanics.spark.RendererMessage;
import tectech.mechanics.spark.ThaumSpark;
import tectech.mechanics.tesla.ITeslaConnectable;
import tectech.mechanics.tesla.ITeslaConnectableSimple;
import tectech.thing.casing.BlockGTCasingsTT;
import tectech.thing.casing.TTCasingsContainer;
import tectech.thing.metaTileEntity.hatch.MTEHatchCapacitor;
import tectech.thing.metaTileEntity.hatch.MTEHatchDynamoMulti;
import tectech.thing.metaTileEntity.hatch.MTEHatchEnergyMulti;
import tectech.thing.metaTileEntity.hatch.MTEHatchParam;
import tectech.thing.metaTileEntity.multi.base.INameFunction;
import tectech.thing.metaTileEntity.multi.base.IStatusFunction;
import tectech.thing.metaTileEntity.multi.base.LedStatus;
import tectech.thing.metaTileEntity.multi.base.Parameters;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;
import tectech.thing.metaTileEntity.multi.base.render.TTRenderedExtendedFacingTexture;
import tectech.util.CommonValues;

public class MTETeslaTower extends TTMultiblockBase implements ISurvivalConstructable, ITeslaConnectable {

    // Interface fields
    private final Multimap<Integer, ITeslaConnectableSimple> teslaNodeMap = MultimapBuilder.treeKeys()
        .linkedListValues()
        .build();
    private final HashSet<ThaumSpark> sparkList = new HashSet<>();
    private int sparkCount = 10;

    // region variables
    private static final int transferRadiusTowerFromConfig = TecTech.configTecTech.TESLA_MULTI_RANGE_TOWER; // Default
                                                                                                            // is 32
    private static final int transferRadiusTransceiverFromConfig = TecTech.configTecTech.TESLA_MULTI_RANGE_TRANSCEIVER; // Default
                                                                                                                        // is
                                                                                                                        // 16
    private static final int transferRadiusCoverUltimateFromConfig = TecTech.configTecTech.TESLA_MULTI_RANGE_COVER; // Default
                                                                                                                    // is
                                                                                                                    // 16
    private static final int plasmaRangeMultiT1 = TecTech.configTecTech.TESLA_MULTI_RANGE_COEFFICIENT_PLASMA_T1; // Default
                                                                                                                 // is 2
    private static final int plasmaRangeMultiT2 = TecTech.configTecTech.TESLA_MULTI_RANGE_COEFFICIENT_PLASMA_T2; // Default
                                                                                                                 // is 4
    private static final int heliumUse = TecTech.configTecTech.TESLA_MULTI_PLASMA_PER_SECOND_T1_HELIUM; // Default is
                                                                                                        // 100
    private static final int nitrogenUse = TecTech.configTecTech.TESLA_MULTI_PLASMA_PER_SECOND_T1_NITROGEN; // Default
                                                                                                            // is 50
    private static final int radonUse = TecTech.configTecTech.TESLA_MULTI_PLASMA_PER_SECOND_T2_RADON; // Default is 50
    private static final boolean visualEffect = TecTech.configTecTech.TESLA_VISUAL_EFFECT; // Default is true
    // Default is {1, 1, 1}
    private static final int[] plasmaTierLoss = new int[] { TecTech.configTecTech.TESLA_MULTI_LOSS_PER_BLOCK_T0,
        TecTech.configTecTech.TESLA_MULTI_LOSS_PER_BLOCK_T1, TecTech.configTecTech.TESLA_MULTI_LOSS_PER_BLOCK_T2 };
    private static final float overDriveLoss = TecTech.configTecTech.TESLA_MULTI_LOSS_FACTOR_OVERDRIVE; // Default is
                                                                                                        // 0.25F;
    private static final boolean doFluidOutput = TecTech.configTecTech.TESLA_MULTI_GAS_OUTPUT; // Default is false

    // Face icons
    private static Textures.BlockIcons.CustomIcon ScreenOFF;
    private static Textures.BlockIcons.CustomIcon ScreenON;

    private int mTier = 0; // Determines max voltage (LV to ZPM)
    private int plasmaTier = 0; // 0 is None, 1 is Helium or Nitrogen, 2 is Radon (Does not match actual plasma tiers)

    private FluidStack[] mOutputFluidsQueue; // Used to buffer the fluid outputs, so the tesla takes a second to 'cool'
                                             // any plasma it
    // would output as a gas

    private final ArrayList<MTEHatchCapacitor> eCapacitorHatches = new ArrayList<>(); // Capacitor
                                                                                      // hatches which
                                                                                      // determine the
                                                                                      // max voltage
                                                                                      // tier and count
                                                                                      // of amps

    private long energyCapacity = 0; // Total energy storage limited by capacitors
    private long outputVoltageMax = 0; // Tesla voltage output limited by capacitors
    private int vTier = -1; // Tesla voltage tier limited by capacitors
    private long outputCurrentMax = 0; // Tesla current output limited by capacitors

    // outputVoltage and current after settings
    private long outputVoltage;
    private long outputCurrent;

    // Prevents unnecessary offset calculation, saving on lag
    private byte oldRotation = -1;
    private ForgeDirection oldOrientation = ForgeDirection.UNKNOWN;
    // Location of the center of the sphere on top of the tower, used as the Thaumcraft lightning and origin
    public Vec3Impl posTop = Vec3Impl.NULL_VECTOR;
    // endregion

    // region structure
    private static final String[] description = new String[] {
        EnumChatFormatting.AQUA + translateToLocal("tt.keyphrase.Hint_Details") + ":",
        translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.hint.0"), // 1 - Classic Hatches, Capacitor
                                                                               // Hatches or Tesla
        // Base Casing
        translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.hint.1"), // 2 - ""Titanium frames""
    };

    private static final IStructureDefinition<MTETeslaTower> STRUCTURE_DEFINITION = IStructureDefinition
        .<MTETeslaTower>builder()
        .addShape(
            "main",
            transpose(
                new String[][] { { "       ", "       ", "  BBB  ", "  BBB  ", "  BBB  ", "       ", "       " },
                    { "       ", "  BBB  ", " BBBBB ", " BBBBB ", " BBBBB ", "  BBB  ", "       " },
                    { "       ", "  BBB  ", " BBBBB ", " BBBBB ", " BBBBB ", "  BBB  ", "       " },
                    { "       ", "  BBB  ", " BBBBB ", " BBBBB ", " BBBBB ", "  BBB  ", "       " },
                    { "       ", "       ", "  BBB  ", "  BCB  ", "  BBB  ", "       ", "       " },
                    { "       ", "       ", "       ", "   C   ", "       ", "       ", "       " },
                    { "       ", "  BBB  ", " B F B ", " BFCFB ", " B F B ", "  BBB  ", "       " },
                    { "       ", "       ", "       ", "   C   ", "       ", "       ", "       " },
                    { "       ", "  BBB  ", " B F B ", " BFCFB ", " B F B ", "  BBB  ", "       " },
                    { "       ", "       ", "       ", "   C   ", "       ", "       ", "       " },
                    { "       ", "  BBB  ", " B F B ", " BFCFB ", " B F B ", "  BBB  ", "       " },
                    { "       ", "       ", "       ", "   C   ", "       ", "       ", "       " },
                    { "       ", "  BBB  ", " B F B ", " BFCFB ", " B F B ", "  BBB  ", "       " },
                    { "       ", "       ", "       ", "   C   ", "       ", "       ", "       " },
                    { "       ", "       ", "       ", "   C   ", "       ", "       ", "       " },
                    { "       ", "  DDD  ", " D   D ", " D C D ", " D   D ", "  DDD  ", "       " },
                    { " EE~EE ", "EAAAAAE", "EADDDAE", "EADADAE", "EADDDAE", "EAAAAAE", " EEEEE " } }))
        .addElement('A', ofBlock(TTCasingsContainer.sBlockCasingsBA0, 6))
        .addElement('B', ofBlock(TTCasingsContainer.sBlockCasingsBA0, 7))
        .addElement('C', ofBlock(TTCasingsContainer.sBlockCasingsBA0, 8))
        .addElement(
            'D',
            ofBlocksTiered(
                (block, meta) -> block != TTCasingsContainer.sBlockCasingsBA0 ? null
                    : meta <= 5 ? Integer.valueOf(meta) : meta == 9 ? 6 : null,
                IntStream.range(0, 7)
                    .map(tier -> tier == 6 ? 9 : tier)
                    .mapToObj(meta -> Pair.of(TTCasingsContainer.sBlockCasingsBA0, meta))
                    .collect(Collectors.toList()),
                -1,
                (t, v) -> t.mTier = v,
                t -> t.mTier))
        .addElement(
            'E',
            buildHatchAdder(MTETeslaTower.class)
                .atLeast(
                    CapacitorHatchElement.INSTANCE,
                    HatchElement.EnergyMulti,
                    Energy,
                    HatchElement.DynamoMulti,
                    Dynamo,
                    InputHatch,
                    OutputHatch,
                    HatchElement.Param,
                    Maintenance)
                .dot(1)
                .casingIndex(BlockGTCasingsTT.textureOffset + 16 + 6)
                .buildAndChain(TTCasingsContainer.sBlockCasingsBA0, 6))
        .addElement('F', ofFrame(Materials.Titanium))
        .build();
    // endregion

    // region parameters
    protected Parameters.Group.ParameterIn popogaSetting, histLowSetting, histHighSetting, transferRadiusTowerSetting,
        transferRadiusTransceiverSetting, transferRadiusCoverUltimateSetting, outputVoltageSetting,
        outputCurrentSetting, sortTimeMinSetting, overDriveSetting;
    protected Parameters.Group.ParameterOut popogaDisplay, transferRadiusTowerDisplay, transferRadiusTransceiverDisplay,
        transferRadiusCoverUltimateDisplay, outputVoltageDisplay, outputCurrentDisplay, outputMaxDisplay,
        energyCapacityDisplay, energyStoredDisplay, energyFractionDisplay, sortTimeDisplay;

    private static final INameFunction<MTETeslaTower> HYSTERESIS_LOW_SETTING_NAME = (base,
        p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgi.0"); // Hysteresis low setting
    private static final INameFunction<MTETeslaTower> HYSTERESIS_HIGH_SETTING_NAME = (base,
        p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgi.1"); // Hysteresis high setting
    private static final INameFunction<MTETeslaTower> TRANSFER_RADIUS_TOWER_SETTING_NAME = (base,
        p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgi.2"); // Tesla Towers transfer radius
    // setting
    private static final INameFunction<MTETeslaTower> TRANSFER_RADIUS_TRANSCEIVER_SETTING_NAME = (base,
        p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgi.3"); // Tesla Transceiver transfer
    // radius setting
    private static final INameFunction<MTETeslaTower> TRANSFER_RADIUS_COVER_ULTIMATE_SETTING_NAME = (base,
        p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgi.4"); // Tesla Ultimate Cover
                                                                                     // transfer radius
    // setting
    private static final INameFunction<MTETeslaTower> OUTPUT_VOLTAGE_SETTING_NAME = (base,
        p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgi.5"); // Output voltage setting
    private static final INameFunction<MTETeslaTower> OUTPUT_CURRENT_SETTING_NAME = (base,
        p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgi.6"); // Output current setting
    private static final INameFunction<MTETeslaTower> SCAN_TIME_MIN_SETTING_NAME = (base,
        p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgi.7"); // Scan time Min setting
    private static final INameFunction<MTETeslaTower> OVERDRIVE_SETTING_NAME = (base,
        p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgi.8"); // Overdrive setting
    private static final INameFunction<MTETeslaTower> POPOGA_NAME = (base,
        p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgi.9"); // Unused

    private static final INameFunction<MTETeslaTower> TRANSFER_RADIUS_TOWER_DISPLAY_NAME = (base,
        p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgo.0"); // Tesla Towers transfer radius
    // display
    private static final INameFunction<MTETeslaTower> TRANSFER_RADIUS_TRANSCEIVER_DISPLAY_NAME = (base,
        p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgo.1"); // Tesla Transceiver transfer
    // radius display
    private static final INameFunction<MTETeslaTower> TRANSFER_RADIUS_COVER_ULTIMATE_DISPLAY_NAME = (base,
        p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgo.2"); // Tesla Ultimate Cover
                                                                                     // transfer radius
    // display
    private static final INameFunction<MTETeslaTower> OUTPUT_VOLTAGE_DISPLAY_NAME = (base,
        p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgo.3"); // Output voltage display
    private static final INameFunction<MTETeslaTower> OUTPUT_MAX_DISPLAY_NAME = (base,
        p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgo.9"); // Output max display
    private static final INameFunction<MTETeslaTower> OUTPUT_CURRENT_DISPLAY_NAME = (base,
        p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgo.4"); // Output current display
    private static final INameFunction<MTETeslaTower> ENERGY_CAPACITY_DISPLAY_NAME = (base,
        p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgo.5"); // Energy Capacity display
    private static final INameFunction<MTETeslaTower> ENERGY_STORED_DISPLAY_NAME = (base,
        p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgo.6"); // Energy Stored display
    private static final INameFunction<MTETeslaTower> ENERGY_FRACTION_DISPLAY_NAME = (base,
        p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgo.7"); // Energy Fraction display
    private static final INameFunction<MTETeslaTower> SCAN_TIME_DISPLAY_NAME = (base,
        p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgo.8"); // Scan time display

    private static final IStatusFunction<MTETeslaTower> HYSTERESIS_LOW_STATUS = (base, p) -> {
        double value = p.get();
        if (Double.isNaN(value)) {
            return LedStatus.STATUS_WRONG;
        }
        if (value <= 0.05) return LedStatus.STATUS_TOO_LOW;
        if (value > base.histHighSetting.get()) return LedStatus.STATUS_TOO_HIGH;
        return LedStatus.STATUS_OK;
    };
    private static final IStatusFunction<MTETeslaTower> HYSTERESIS_HIGH_STATUS = (base, p) -> {
        double value = p.get();
        if (Double.isNaN(value)) return LedStatus.STATUS_WRONG;
        if (value <= base.histLowSetting.get()) return LedStatus.STATUS_TOO_LOW;
        if (value > 0.95) return LedStatus.STATUS_TOO_HIGH;
        return LedStatus.STATUS_OK;
    };
    private static final IStatusFunction<MTETeslaTower> TRANSFER_RADIUS_TOWER_STATUS = (base, p) -> {
        double value = p.get();
        if (Double.isNaN(value)) return LedStatus.STATUS_WRONG;
        value = (int) value;
        if (value < 0) return LedStatus.STATUS_TOO_LOW;
        if (value > transferRadiusTowerFromConfig) return LedStatus.STATUS_HIGH;
        if (value < transferRadiusTowerFromConfig) return LedStatus.STATUS_LOW;
        return LedStatus.STATUS_OK;
    };
    private static final IStatusFunction<MTETeslaTower> TRANSFER_RADIUS_TRANSCEIVER_STATUS = (base, p) -> {
        double value = p.get();
        if (Double.isNaN(value)) return LedStatus.STATUS_WRONG;
        value = (int) value;
        if (value < 0) return LedStatus.STATUS_TOO_LOW;
        if (value > transferRadiusTransceiverFromConfig) return LedStatus.STATUS_HIGH;
        if (value < transferRadiusTransceiverFromConfig) return LedStatus.STATUS_LOW;
        return LedStatus.STATUS_OK;
    };
    private static final IStatusFunction<MTETeslaTower> TRANSFER_RADIUS_COVER_ULTIMATE_STATUS = (base, p) -> {
        double value = p.get();
        if (Double.isNaN(value)) return LedStatus.STATUS_WRONG;
        value = (int) value;
        if (value < 0) return LedStatus.STATUS_TOO_LOW;
        if (value > transferRadiusCoverUltimateFromConfig) return LedStatus.STATUS_HIGH;
        if (value < transferRadiusCoverUltimateFromConfig) return LedStatus.STATUS_LOW;
        return LedStatus.STATUS_OK;
    };
    private static final IStatusFunction<MTETeslaTower> OUTPUT_VOLTAGE_OR_CURRENT_STATUS = (base, p) -> {
        double value = p.get();
        if (Double.isNaN(value)) return LedStatus.STATUS_WRONG;
        value = (long) value;
        if (value == -1) return LedStatus.STATUS_OK;
        if (value <= 0) return LedStatus.STATUS_TOO_LOW;
        return LedStatus.STATUS_OK;
    };
    private static final IStatusFunction<MTETeslaTower> SCAN_TIME_MIN_STATUS = (base, p) -> {
        double value = p.get();
        if (Double.isNaN(value)) return LedStatus.STATUS_WRONG;
        value = (int) value;
        if (value < 100) return LedStatus.STATUS_TOO_LOW;
        if (value == 100) return LedStatus.STATUS_OK;
        return LedStatus.STATUS_HIGH;
    };
    private static final IStatusFunction<MTETeslaTower> OVERDRIVE_STATUS = (base, p) -> {
        double value = p.get();
        if (Double.isNaN(value)) return LedStatus.STATUS_WRONG;
        value = (int) value;
        if (value < 0) return LedStatus.STATUS_TOO_LOW;
        if (value == 0) return LedStatus.STATUS_LOW;
        return LedStatus.STATUS_HIGH;
    };
    private static final IStatusFunction<MTETeslaTower> POPOGA_STATUS = (base, p) -> {
        if (base.getBaseMetaTileEntity()
            .getWorld()
            .isThundering()) {
            return LedStatus.STATUS_WTF;
        }
        return LedStatus.STATUS_NEUTRAL;
    };
    private static final IStatusFunction<MTETeslaTower> SCAN_TIME_STATUS = (base, p) -> {
        double value = p.get();
        if (Double.isNaN(value)) return LedStatus.STATUS_WRONG;
        value = (int) value;
        if (value == 0) return LedStatus.STATUS_HIGH;
        return LedStatus.STATUS_LOW;
    };
    private static final IStatusFunction<MTETeslaTower> POWER_STATUS = (base, p) -> {
        double value = p.get();
        if (Double.isNaN(value)) return LedStatus.STATUS_WRONG;
        value = (long) value;
        if (value > 0) {
            return LedStatus.STATUS_OK;
        } else {
            return LedStatus.STATUS_LOW;
        }
    };
    private static final IStatusFunction<MTETeslaTower> ENERGY_STATUS = (base, p) -> {
        double value = p.get();
        if (Double.isNaN(value)) return LedStatus.STATUS_WRONG;
        if (base.energyFractionDisplay.get() > base.histHighSetting.get()) {
            return LedStatus.STATUS_HIGH;
        } else if (base.energyFractionDisplay.get() < base.histLowSetting.get()) {
            return LedStatus.STATUS_LOW;
        } else {
            return LedStatus.STATUS_OK;
        }
    };
    // endregion

    public MTETeslaTower(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTETeslaTower(String aName) {
        super(aName);
    }

    private float getRangeMulti(int mTier, int vTier) {
        // By Default:
        // Helium and Nitrogen Plasmas will double the range
        // Radon will quadruple the range
        int plasmaBoost;
        switch (plasmaTier) {
            case 2:
                plasmaBoost = plasmaRangeMultiT2;
                break;
            case 1:
                plasmaBoost = plasmaRangeMultiT1;
                break;
            default:
                plasmaBoost = 1;
        }

        // Over-tiered coils will add +25% range
        if (vTier > mTier) {
            return 1.25F * plasmaBoost;
        }
        return 1F * plasmaBoost;
    }

    private void checkPlasmaBoost() {
        // If there's fluid in the queue, try to output it
        // That way it takes at least a second to 'process' the plasma
        if (mOutputFluidsQueue != null) {
            mOutputFluids = mOutputFluidsQueue;
            mOutputFluidsQueue = null;
        }

        for (MTEHatchInput fluidHatch : mInputHatches) {
            if (fluidHatch.mFluid != null) {
                if (fluidHatch.mFluid.isFluidEqual(Materials.Helium.getPlasma(1))
                    && fluidHatch.mFluid.amount >= heliumUse) {
                    fluidHatch.mFluid.amount = fluidHatch.mFluid.amount - heliumUse;
                    if (doFluidOutput) {
                        mOutputFluidsQueue = new FluidStack[] { Materials.Helium.getGas(heliumUse) };
                    }
                    plasmaTier = 1;
                    return;
                } else if (fluidHatch.mFluid.isFluidEqual(Materials.Nitrogen.getPlasma(1))
                    && fluidHatch.mFluid.amount >= nitrogenUse) {
                        fluidHatch.mFluid.amount = fluidHatch.mFluid.amount - nitrogenUse;
                        if (doFluidOutput) {
                            mOutputFluidsQueue = new FluidStack[] { Materials.Nitrogen.getGas(nitrogenUse) };
                        }
                        plasmaTier = 1;
                        return;
                    } else if (fluidHatch.mFluid.isFluidEqual(Materials.Radon.getPlasma(1))
                        && fluidHatch.mFluid.amount >= radonUse) {
                            fluidHatch.mFluid.amount = fluidHatch.mFluid.amount - radonUse;
                            if (doFluidOutput) {
                                mOutputFluidsQueue = new FluidStack[] { Materials.Radon.getGas(radonUse) };
                            }
                            plasmaTier = 2;
                            return;
                        }
            }
        }
        plasmaTier = 0;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTETeslaTower(mName);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        for (MTEHatchCapacitor cap : filterValidMTEs(eCapacitorHatches)) {
            cap.getBaseMetaTileEntity()
                .setActive(false);
        }
        eCapacitorHatches.clear();

        mTier = -1;

        if (structureCheck_EM("main", 3, 16, 0)) {
            for (MTEHatchCapacitor cap : filterValidMTEs(eCapacitorHatches)) {
                cap.getBaseMetaTileEntity()
                    .setActive(iGregTechTileEntity.isActive());
            }

            // Only recalculate offsets on orientation or rotation change
            if (oldRotation != getExtendedFacing().ordinal()
                || oldOrientation != iGregTechTileEntity.getFrontFacing()) {
                oldRotation = (byte) getExtendedFacing().ordinal();
                oldOrientation = iGregTechTileEntity.getFrontFacing();

                Vec3Impl posBMTE = new Vec3Impl(
                    getBaseMetaTileEntity().getXCoord(),
                    getBaseMetaTileEntity().getYCoord(),
                    getBaseMetaTileEntity().getZCoord());

                // Calculate coordinates of the middle bottom
                posTop = getExtendedFacing().getWorldOffset(new Vec3Impl(0, 0, 2))
                    .add(posBMTE);

                // Calculate coordinates of the top sphere
                posTop = getExtendedFacing().getWorldOffset(new Vec3Impl(0, -14, 2))
                    .add(posBMTE);
            }
            // Generate node map
            if (!getBaseMetaTileEntity().isClientSide()) {
                TeslaUtil.teslaSimpleNodeSetAdd(this);
                TeslaUtil.generateTeslaNodeMap(this);
            }
            return true;
        }
        return false;
    }

    @Override
    @NotNull
    protected CheckRecipeResult checkProcessing_EM() {
        checkPlasmaBoost();

        if (!histHighSetting.getStatus(false).isOk || !histLowSetting.getStatus(false).isOk)
            return SimpleCheckRecipeResult.ofFailure("invalid_hysteresis");
        if (!transferRadiusTowerSetting.getStatus(false).isOk || !transferRadiusTransceiverSetting.getStatus(false).isOk
            || !transferRadiusCoverUltimateSetting.getStatus(false).isOk)
            return SimpleCheckRecipeResult.ofFailure("invalid_transfer_radius");
        if (!outputVoltageSetting.getStatus(false).isOk)
            return SimpleCheckRecipeResult.ofFailure("invalid_voltage_setting");
        if (!outputCurrentSetting.getStatus(false).isOk)
            return SimpleCheckRecipeResult.ofFailure("invalid_current_setting");
        if (!sortTimeMinSetting.getStatus(false).isOk) return SimpleCheckRecipeResult.ofFailure("invalid_time_setting");
        if (!overDriveSetting.getStatus(false).isOk)
            return SimpleCheckRecipeResult.ofFailure("invalid_overdrive_setting");

        mEfficiencyIncrease = 10000;
        mMaxProgresstime = 20;
        vTier = -1;
        long[] capacitorData;
        for (MTEHatchCapacitor cap : filterValidMTEs(eCapacitorHatches)) {
            if (cap.getCapacitors()[0] > vTier) {
                vTier = (int) cap.getCapacitors()[0];
            }
        }

        energyCapacity = 0;
        outputCurrentMax = 0;

        if (vTier < 0) {
            // Returning true to allow for 'passive running'
            outputVoltageMax = 0;
            return SimpleCheckRecipeResult.ofSuccess("routing");
        } else if (vTier > mTier && getEUVar() > 0) {
            explodeMultiblock();
        }

        outputVoltageMax = V[vTier + 1];
        for (MTEHatchCapacitor cap : filterValidMTEs(eCapacitorHatches)) {
            cap.getBaseMetaTileEntity()
                .setActive(true);
            capacitorData = cap.getCapacitors();
            if (capacitorData[0] < vTier) {
                if (getEUVar() > 0 && capacitorData[0] != 0) {
                    cap.getBaseMetaTileEntity()
                        .setToFire();
                }
                eCapacitorHatches.remove(cap);
            } else {
                outputCurrentMax += capacitorData[1];
                energyCapacity += capacitorData[2];
            }
        }
        return SimpleCheckRecipeResult.ofSuccess("routing");
    }

    @Override
    public MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.name")) // Machine Type: Tesla
                                                                                               // Tower
            .addInfo(translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.desc.0")) // Controller block of
                                                                                            // the Tesla Tower
            .addInfo(translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.desc.1")) // Used to transmit
                                                                                            // power to Tesla
            // Coil Covers and Tesla
            // Transceivers
            .addInfo(translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.desc.2")) // Can be fed with
            // Helium/Nitrogen/Radon Plasma to
            // increase the range
            .addInfo(translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.desc.3")) // Transmitted voltage
                                                                                            // depends on
            // the used Tesla Capacitor tier
            .addInfo(translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.desc.4")) // Primary Tesla
                                                                                            // Windings need to
            // be at least the same tier as
            // the Tesla Capacitor
            .addInfo(translateToLocal("tt.keyword.Structure.StructureTooComplex")) // The structure is too complex!
            .addSeparator()
            .beginStructureBlock(7, 17, 7, false)
            .addOtherStructurePart(
                translateToLocal("gt.blockmachines.hatch.capacitor.tier.03.name"),
                translateToLocal("tt.keyword.Structure.AnyTeslaBaseCasingOuter"),
                1) // Capacitor Hatch: Any outer Tesla Base Casing
            .addEnergyHatch(translateToLocal("tt.keyword.Structure.AnyTeslaBaseCasingOuter"), 1) // Energy Hatch:
                                                                                                 // Any outer Tesla
                                                                                                 // Base Casing
            .addMaintenanceHatch(translateToLocal("tt.keyword.Structure.AnyTeslaBaseCasingOuter"), 1) // Maintenance
                                                                                                      // Hatch: Any
                                                                                                      // outer Tesla
                                                                                                      // Base Casing
            .toolTipFinisher(CommonValues.THETA_MOVEMENT);
        return tt;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        super.registerIcons(aBlockIconRegister);
        ScreenOFF = new Textures.BlockIcons.CustomIcon("iconsets/TM_TESLA_TOWER");
        ScreenON = new Textures.BlockIcons.CustomIcon("iconsets/TM_TESLA_TOWER_ACTIVE");
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            return new ITexture[] { Textures.BlockIcons.casingTexturePages[BlockGTCasingsTT.texturePage][16 + 6],
                new TTRenderedExtendedFacingTexture(aActive ? ScreenON : ScreenOFF) };
        }
        return new ITexture[] { Textures.BlockIcons.casingTexturePages[BlockGTCasingsTT.texturePage][16 + 6] };
    }

    @Override
    public void onRemoval() {
        super.onRemoval();
        if (!getBaseMetaTileEntity().isClientSide()) {
            TeslaUtil.teslaSimpleNodeSetRemove(this);
            for (MTEHatchCapacitor cap : filterValidMTEs(eCapacitorHatches)) {
                cap.getBaseMetaTileEntity()
                    .setActive(false);
            }
        }
    }

    @Override
    public void onUnload() {
        if (!getBaseMetaTileEntity().isClientSide()) {
            TeslaUtil.teslaSimpleNodeSetRemove(this);
        }
    }

    @Override
    protected void parametersInstantiation_EM() {
        Parameters.Group hatch_0 = parametrization.getGroup(0, true);
        Parameters.Group hatch_1 = parametrization.getGroup(1, true);
        Parameters.Group hatch_2 = parametrization.getGroup(2, true);
        Parameters.Group hatch_3 = parametrization.getGroup(3, true);
        Parameters.Group hatch_4 = parametrization.getGroup(4, true);
        Parameters.Group hatch_5 = parametrization.getGroup(5, true);
        Parameters.Group hatch_6 = parametrization.getGroup(6, true);
        Parameters.Group hatch_7 = parametrization.getGroup(7, true);
        Parameters.Group hatch_8 = parametrization.getGroup(8, true);
        Parameters.Group hatch_9 = parametrization.getGroup(9, true);

        histLowSetting = hatch_0.makeInParameter(0, 0.25, HYSTERESIS_LOW_SETTING_NAME, HYSTERESIS_LOW_STATUS);
        popogaSetting = hatch_0.makeInParameter(1, 0, POPOGA_NAME, POPOGA_STATUS);
        histHighSetting = hatch_1.makeInParameter(0, 0.75, HYSTERESIS_HIGH_SETTING_NAME, HYSTERESIS_HIGH_STATUS);
        popogaSetting = hatch_1.makeInParameter(1, 0, POPOGA_NAME, POPOGA_STATUS);
        transferRadiusTowerSetting = hatch_2.makeInParameter(
            0,
            transferRadiusTowerFromConfig,
            TRANSFER_RADIUS_TOWER_SETTING_NAME,
            TRANSFER_RADIUS_TOWER_STATUS);
        popogaSetting = hatch_2.makeInParameter(1, 0, POPOGA_NAME, POPOGA_STATUS);
        transferRadiusTransceiverSetting = hatch_3.makeInParameter(
            0,
            transferRadiusTransceiverFromConfig,
            TRANSFER_RADIUS_TRANSCEIVER_SETTING_NAME,
            TRANSFER_RADIUS_TRANSCEIVER_STATUS);
        transferRadiusCoverUltimateSetting = hatch_3.makeInParameter(
            1,
            transferRadiusCoverUltimateFromConfig,
            TRANSFER_RADIUS_COVER_ULTIMATE_SETTING_NAME,
            TRANSFER_RADIUS_COVER_ULTIMATE_STATUS);
        outputVoltageSetting = hatch_4
            .makeInParameter(0, -1, OUTPUT_VOLTAGE_SETTING_NAME, OUTPUT_VOLTAGE_OR_CURRENT_STATUS);
        popogaSetting = hatch_4.makeInParameter(1, 0, POPOGA_NAME, POPOGA_STATUS);
        outputCurrentSetting = hatch_5
            .makeInParameter(0, -1, OUTPUT_CURRENT_SETTING_NAME, OUTPUT_VOLTAGE_OR_CURRENT_STATUS);
        popogaSetting = hatch_5.makeInParameter(1, 0, POPOGA_NAME, POPOGA_STATUS);
        popogaSetting = hatch_6.makeInParameter(0, 0, POPOGA_NAME, POPOGA_STATUS);
        popogaSetting = hatch_6.makeInParameter(1, 0, POPOGA_NAME, POPOGA_STATUS);
        sortTimeMinSetting = hatch_7.makeInParameter(0, 100, SCAN_TIME_MIN_SETTING_NAME, SCAN_TIME_MIN_STATUS);
        popogaSetting = hatch_7.makeInParameter(1, 0, POPOGA_NAME, POPOGA_STATUS);
        overDriveSetting = hatch_8.makeInParameter(0, 0, OVERDRIVE_SETTING_NAME, OVERDRIVE_STATUS);
        popogaSetting = hatch_8.makeInParameter(1, 0, POPOGA_NAME, POPOGA_STATUS);
        popogaSetting = hatch_9.makeInParameter(0, 0, POPOGA_NAME, POPOGA_STATUS);
        popogaSetting = hatch_9.makeInParameter(1, 0, POPOGA_NAME, POPOGA_STATUS);

        popogaDisplay = hatch_0.makeOutParameter(0, 0, POPOGA_NAME, POPOGA_STATUS);
        popogaDisplay = hatch_0.makeOutParameter(1, 0, POPOGA_NAME, POPOGA_STATUS);
        popogaDisplay = hatch_1.makeOutParameter(0, 0, POPOGA_NAME, POPOGA_STATUS);
        popogaDisplay = hatch_1.makeOutParameter(1, 0, POPOGA_NAME, POPOGA_STATUS);
        transferRadiusTowerDisplay = hatch_2
            .makeOutParameter(0, 0, TRANSFER_RADIUS_TOWER_DISPLAY_NAME, TRANSFER_RADIUS_TOWER_STATUS);
        popogaDisplay = hatch_2.makeOutParameter(1, 0, POPOGA_NAME, POPOGA_STATUS);
        transferRadiusTransceiverDisplay = hatch_3
            .makeOutParameter(0, 0, TRANSFER_RADIUS_TRANSCEIVER_DISPLAY_NAME, TRANSFER_RADIUS_TRANSCEIVER_STATUS);
        transferRadiusCoverUltimateDisplay = hatch_3
            .makeOutParameter(1, 0, TRANSFER_RADIUS_COVER_ULTIMATE_DISPLAY_NAME, TRANSFER_RADIUS_COVER_ULTIMATE_STATUS);
        outputVoltageDisplay = hatch_4.makeOutParameter(0, 0, OUTPUT_VOLTAGE_DISPLAY_NAME, POWER_STATUS);
        outputMaxDisplay = hatch_4.makeOutParameter(1, 0, OUTPUT_MAX_DISPLAY_NAME, POWER_STATUS);
        outputCurrentDisplay = hatch_5.makeOutParameter(0, 0, OUTPUT_CURRENT_DISPLAY_NAME, POWER_STATUS);
        energyCapacityDisplay = hatch_5.makeOutParameter(1, 0, ENERGY_CAPACITY_DISPLAY_NAME, ENERGY_STATUS);
        energyStoredDisplay = hatch_6.makeOutParameter(0, 0, ENERGY_STORED_DISPLAY_NAME, ENERGY_STATUS);
        energyFractionDisplay = hatch_6.makeOutParameter(1, 0, ENERGY_FRACTION_DISPLAY_NAME, ENERGY_STATUS);
        sortTimeDisplay = hatch_7.makeOutParameter(0, 0, SCAN_TIME_DISPLAY_NAME, SCAN_TIME_STATUS);
        popogaDisplay = hatch_7.makeOutParameter(1, 0, POPOGA_NAME, POPOGA_STATUS);
        popogaDisplay = hatch_8.makeOutParameter(0, 0, POPOGA_NAME, POPOGA_STATUS);
        popogaDisplay = hatch_8.makeOutParameter(1, 0, POPOGA_NAME, POPOGA_STATUS);
        popogaDisplay = hatch_9.makeOutParameter(0, 0, POPOGA_NAME, POPOGA_STATUS);
        popogaDisplay = hatch_9.makeOutParameter(1, 0, POPOGA_NAME, POPOGA_STATUS);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setLong("eEnergyCapacity", energyCapacity);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        energyCapacity = aNBT.getLong("eEnergyCapacity");
        TeslaUtil.teslaSimpleNodeSetAdd(this);
    }

    @Override
    public void stopMachine(@Nonnull ShutDownReason reason) {
        super.stopMachine(reason);
        for (MTEHatchCapacitor cap : filterValidMTEs(eCapacitorHatches)) {
            cap.getBaseMetaTileEntity()
                .setActive(false);
        }

        ePowerPass = false;
        setEUVar(0);
        energyStoredDisplay.set(0);
        energyFractionDisplay.set(0);
        outputMaxDisplay.set(0);
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        // Hysteresis based ePowerPass setting
        float energyFrac = (float) getEUVar() / energyCapacity;

        energyCapacityDisplay.set(energyCapacity);
        energyStoredDisplay.set(getEUVar());
        energyFractionDisplay.set(energyFrac);

        if (!ePowerPass && energyFrac > histHighSetting.get()) {
            ePowerPass = true;
        } else if (ePowerPass && energyFrac < histLowSetting.get()) {
            ePowerPass = false;
        }

        // Power Limit Settings
        if (outputVoltageSetting.get() > 0) {
            outputVoltage = min(outputVoltageMax, (long) outputVoltageSetting.get());
        } else {
            outputVoltage = outputVoltageMax;
        }
        outputVoltageDisplay.set(outputVoltage);

        if (outputCurrentSetting.get() > 0) {
            outputCurrent = min(outputCurrentMax, (long) outputCurrentSetting.get());
        } else {
            outputCurrent = outputCurrentMax;
        }

        // Range calculation and display
        int transferRadiusTower = getTeslaTransmissionRange();
        transferRadiusTowerDisplay.set(transferRadiusTower);
        transferRadiusTransceiverDisplay.set(transferRadiusTower * 2);
        transferRadiusCoverUltimateDisplay.set(transferRadiusTower);

        // Power transfer
        outputCurrentDisplay.set(TeslaUtil.powerTeslaNodeMap(this));
        outputMaxDisplay.set(Math.max(outputCurrentDisplay.get(), outputMaxDisplay.get()));
        // TODO Encapsulate the spark sender
        sparkCount--;
        if (sparkCount == 0 && visualEffect) {
            IGregTechTileEntity mte = getBaseMetaTileEntity();
            sparkCount = 10;
            if (!sparkList.isEmpty()) {
                NetworkDispatcher.INSTANCE.sendToAllAround(
                    new RendererMessage.RendererData(sparkList),
                    mte.getWorld().provider.dimensionId,
                    mte.getXCoord(),
                    mte.getYCoord(),
                    mte.getZCoord(),
                    256);
                sparkList.clear();
            }
        }
        return true;
    }

    @Override
    public long maxEUStore() {
        // Setting the power here so that the tower looses all it's charge once disabled
        // This also stops it from exploding
        return getBaseMetaTileEntity().isActive() ? energyCapacity * 2 : 0;
    }

    @Override
    public long getEUVar() {
        // Same reason as maxEUStore, set to 1 instead of zero so it doesn't drain constantly
        return getBaseMetaTileEntity().isActive() ? super.getEUVar() : 1;
    }

    private boolean addCapacitorToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof MTEHatchCapacitor) {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return eCapacitorHatches.add((MTEHatchCapacitor) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof MTEHatchMaintenance) {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return mMaintenanceHatches.add((MTEHatchMaintenance) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof MTEHatchEnergy) {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return mEnergyHatches.add((MTEHatchEnergy) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof MTEHatchEnergyMulti) {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return eEnergyMulti.add((MTEHatchEnergyMulti) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof MTEHatchDynamo) {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return mDynamoHatches.add((MTEHatchDynamo) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof MTEHatchDynamoMulti) {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return eDynamoMulti.add((MTEHatchDynamoMulti) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof MTEHatchParam) {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return eParamHatches.add((MTEHatchParam) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof MTEHatchInput) {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return mInputHatches.add((MTEHatchInput) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof MTEHatchOutput) {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return mOutputHatches.add((MTEHatchOutput) aMetaTileEntity);
        }
        return false;
    }

    @Override
    public IStructureDefinition<MTETeslaTower> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM("main", 3, 16, 0, stackSize, hintsOnly);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (mMachine) return -1;
        return survivialBuildPiece("main", stackSize, 3, 16, 0, elementBudget, source, actor, false, true);
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return description;
    }

    @Override
    public byte getTeslaReceptionCapability() {
        return 0;
    }

    @Override
    public float getTeslaReceptionCoefficient() {
        return 0;
    }

    @Override
    public Multimap<Integer, ITeslaConnectableSimple> getTeslaNodeMap() {
        return teslaNodeMap;
    }

    @Override
    public HashSet<ThaumSpark> getSparkList() {
        return sparkList;
    }

    @Override
    public byte getTeslaTransmissionCapability() {
        return 1;
    }

    @Override
    public int getTeslaTransmissionRange() {
        return (int) (transferRadiusTowerSetting.get() * getRangeMulti(mTier, vTier));
    }

    @Override
    public boolean isOverdriveEnabled() {
        return overDriveSetting.get() > 0;
    }

    @Override
    public int getTeslaEnergyLossPerBlock() {
        return plasmaTierLoss[plasmaTier];
    }

    @Override
    public float getTeslaOverdriveLossCoefficient() {
        return overDriveLoss;
    }

    @Override
    public long getTeslaOutputVoltage() {
        return outputVoltage;
    }

    @Override
    public long getTeslaOutputCurrent() {
        return outputCurrent;
    }

    @Override
    public boolean teslaDrainEnergy(long teslaVoltageDrained) {
        if (getEUVar() < teslaVoltageDrained) {
            return false;
        }

        setEUVar(getEUVar() - teslaVoltageDrained);
        return true;
    }

    @Override
    public boolean isTeslaReadyToReceive() {
        return !this.ePowerPass;
    }

    @Override
    public long getTeslaStoredEnergy() {
        return getEUVar();
    }

    @Override
    public Vec3Impl getTeslaPosition() {
        return posTop;
    }

    @Override
    public Integer getTeslaDimension() {
        return this.getBaseMetaTileEntity()
            .getWorld().provider.dimensionId;
    }

    @Override
    public boolean teslaInjectEnergy(long teslaVoltageInjected) {
        if (this.getEUVar() + teslaVoltageInjected <= (this.maxEUStore() / 2)) {
            this.getBaseMetaTileEntity()
                .increaseStoredEnergyUnits(teslaVoltageInjected, true);
            return true;
        }
        return false;
    }

    private enum CapacitorHatchElement implements IHatchElement<MTETeslaTower> {

        INSTANCE;

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return Collections.singletonList(MTEHatchCapacitor.class);
        }

        @Override
        public IGTHatchAdder<? super MTETeslaTower> adder() {
            return MTETeslaTower::addCapacitorToMachineList;
        }

        @Override
        public long count(MTETeslaTower MTETeslaTower) {
            return MTETeslaTower.eCapacitorHatches.size();
        }
    }
}

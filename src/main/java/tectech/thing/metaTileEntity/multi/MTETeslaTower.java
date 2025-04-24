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
import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.api.util.GTUtility.validMTEList;
import static java.lang.Math.min;
import static net.minecraft.util.StatCollector.translateToLocal;
import static tectech.Reference.MODID;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.cleanroommc.modularui.utils.serialization.IByteBufAdapter;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;
import com.cleanroommc.modularui.value.sync.GenericSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.InteractionSyncHandler;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.value.sync.SyncHandlers;
import com.cleanroommc.modularui.widget.SingleChildWidget;
import com.cleanroommc.modularui.widget.sizer.Area;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ItemSlot;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.util.Vec3Impl;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GTMod;
import gregtech.api.enums.Materials;
import gregtech.api.enums.StructureError;
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
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.GTUtility;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import tectech.TecTech;
import tectech.loader.ConfigHandler;
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
import tectech.thing.metaTileEntity.multi.base.INameFunction;
import tectech.thing.metaTileEntity.multi.base.IStatusFunction;
import tectech.thing.metaTileEntity.multi.base.LedStatus;
import tectech.thing.metaTileEntity.multi.base.Parameter;
import tectech.thing.metaTileEntity.multi.base.Parameters;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;
import tectech.thing.metaTileEntity.multi.base.render.TTRenderedExtendedFacingTexture;

public class MTETeslaTower extends TTMultiblockBase
    implements ISurvivalConstructable, ITeslaConnectable, IGuiHolder<PosGuiData> {

    // Interface fields
    private final Multimap<Integer, ITeslaConnectableSimple> teslaNodeMap = MultimapBuilder.treeKeys()
        .linkedListValues()
        .build();
    private final HashSet<ThaumSpark> sparkList = new HashSet<>();
    private int sparkCount = 20;

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
        if (value > ConfigHandler.TeslaTweaks.TESLA_MULTI_RANGE_TOWER) return LedStatus.STATUS_HIGH;
        if (value < ConfigHandler.TeslaTweaks.TESLA_MULTI_RANGE_TOWER) return LedStatus.STATUS_LOW;
        return LedStatus.STATUS_OK;
    };
    private static final IStatusFunction<MTETeslaTower> TRANSFER_RADIUS_TRANSCEIVER_STATUS = (base, p) -> {
        double value = p.get();
        if (Double.isNaN(value)) return LedStatus.STATUS_WRONG;
        value = (int) value;
        if (value < 0) return LedStatus.STATUS_TOO_LOW;
        if (value > ConfigHandler.TeslaTweaks.TESLA_MULTI_RANGE_TRANSCEIVER) return LedStatus.STATUS_HIGH;
        if (value < ConfigHandler.TeslaTweaks.TESLA_MULTI_RANGE_TRANSCEIVER) return LedStatus.STATUS_LOW;
        return LedStatus.STATUS_OK;
    };
    private static final IStatusFunction<MTETeslaTower> TRANSFER_RADIUS_COVER_ULTIMATE_STATUS = (base, p) -> {
        double value = p.get();
        if (Double.isNaN(value)) return LedStatus.STATUS_WRONG;
        value = (int) value;
        if (value < 0) return LedStatus.STATUS_TOO_LOW;
        if (value > ConfigHandler.TeslaTweaks.TESLA_MULTI_RANGE_COVER) return LedStatus.STATUS_HIGH;
        if (value < ConfigHandler.TeslaTweaks.TESLA_MULTI_RANGE_COVER) return LedStatus.STATUS_LOW;
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
    private Map<String, Parameter<?>> parameterMap = new LinkedHashMap<>();

    public MTETeslaTower(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        initParameters();
    }

    public MTETeslaTower(String aName) {
        super(aName);
        initParameters();
    }

    private void initParameters() {
        parameterMap.put(
            "hysteresisLow",
            new Parameter.DoubleParameter(
                0.25,
                0.05,
                0.75,
                "gt.blockmachines.multimachine.tm.teslaCoil.hysteresisLow"));
        parameterMap.put(
            "hysteresisHigh",
            new Parameter.DoubleParameter(
                0.75,
                0.25,
                0.95,
                "gt.blockmachines.multimachine.tm.teslaCoil.hysteresisHigh"));
        parameterMap.put(
            "transferRadius",
            new Parameter.IntegerParameter(
                ConfigHandler.TeslaTweaks.TESLA_MULTI_RANGE_TOWER,
                0,
                ConfigHandler.TeslaTweaks.TESLA_MULTI_RANGE_TOWER,
                "gt.blockmachines.multimachine.tm.teslaCoil.transferRadius"));
        parameterMap.put(
            "transceiverRadius",
            new Parameter.IntegerParameter(
                ConfigHandler.TeslaTweaks.TESLA_MULTI_RANGE_TRANSCEIVER,
                0,
                ConfigHandler.TeslaTweaks.TESLA_MULTI_RANGE_TRANSCEIVER,
                "gt.blockmachines.multimachine.tm.teslaCoil.transceiverRadius"));
        parameterMap.put(
            "ultimateCoverTransferRadius",
            new Parameter.IntegerParameter(
                ConfigHandler.TeslaTweaks.TESLA_MULTI_RANGE_COVER,
                0,
                ConfigHandler.TeslaTweaks.TESLA_MULTI_RANGE_COVER,
                "gt.blockmachines.multimachine.tm.teslaCoil.ultimateCoverTransferRadius"));
        parameterMap.put(
            "outputVoltage",
            new Parameter.IntegerParameter(
                -1,
                -1,
                Integer.MAX_VALUE,
                "gt.blockmachines.multimachine.tm.teslaCoil.outputVoltage"));
        parameterMap.put(
            "outputCurrent",
            new Parameter.IntegerParameter(
                -1,
                -1,
                Integer.MAX_VALUE,
                "gt.blockmachines.multimachine.tm.teslaCoil.outputCurrent"));
        parameterMap.put(
            "scanTime",
            new Parameter.IntegerParameter(
                100,
                100,
                Integer.MAX_VALUE,
                "gt.blockmachines.multimachine.tm.teslaCoil.scanTime"));
        parameterMap.put(
            "overdrive",
            new Parameter.BooleanParameter(false, "gt.blockmachines.multimachine.tm.teslaCoil.overdrive"));
    }

    private float getRangeMulti(int mTier, int vTier) {
        // By Default:
        // Helium and Nitrogen Plasmas will double the range
        // Radon will quadruple the range
        int plasmaBoost;
        switch (plasmaTier) {
            case 2:
                plasmaBoost = ConfigHandler.TeslaTweaks.TESLA_MULTI_RANGE_COEFFICIENT_PLASMA_T2;
                break;
            case 1:
                plasmaBoost = ConfigHandler.TeslaTweaks.TESLA_MULTI_RANGE_COEFFICIENT_PLASMA_T1;
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
                    && fluidHatch.mFluid.amount >= ConfigHandler.TeslaTweaks.TESLA_MULTI_PLASMA_PER_SECOND_T1_HELIUM) {
                    fluidHatch.mFluid.amount = fluidHatch.mFluid.amount
                        - ConfigHandler.TeslaTweaks.TESLA_MULTI_PLASMA_PER_SECOND_T1_HELIUM;

                    plasmaTier = 1;
                    return;
                } else if (fluidHatch.mFluid.isFluidEqual(Materials.Nitrogen.getPlasma(1)) && fluidHatch.mFluid.amount
                    >= ConfigHandler.TeslaTweaks.TESLA_MULTI_PLASMA_PER_SECOND_T1_NITROGEN) {
                        fluidHatch.mFluid.amount = fluidHatch.mFluid.amount
                            - ConfigHandler.TeslaTweaks.TESLA_MULTI_PLASMA_PER_SECOND_T1_NITROGEN;

                        plasmaTier = 1;
                        return;
                    } else if (fluidHatch.mFluid.isFluidEqual(Materials.Radon.getPlasma(1)) && fluidHatch.mFluid.amount
                        >= ConfigHandler.TeslaTweaks.TESLA_MULTI_PLASMA_PER_SECOND_T2_RADON) {
                            fluidHatch.mFluid.amount = fluidHatch.mFluid.amount
                                - ConfigHandler.TeslaTweaks.TESLA_MULTI_PLASMA_PER_SECOND_T2_RADON;

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
        for (MTEHatchCapacitor cap : validMTEList(eCapacitorHatches)) {
            cap.getBaseMetaTileEntity()
                .setActive(false);
        }
        eCapacitorHatches.clear();

        mTier = -1;

        if (structureCheck_EM("main", 3, 16, 0)) {
            for (MTEHatchCapacitor cap : validMTEList(eCapacitorHatches)) {
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
        for (MTEHatchCapacitor cap : validMTEList(eCapacitorHatches)) {
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
            return SimpleCheckRecipeResult.ofFailure("invalid_primary_winding");
        }

        outputVoltageMax = V[vTier + 1];
        for (MTEHatchCapacitor cap : validMTEList(eCapacitorHatches)) {
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
            .addTecTechHatchInfo()
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
            .toolTipFinisher();
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
            for (MTEHatchCapacitor cap : validMTEList(eCapacitorHatches)) {
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
            ConfigHandler.TeslaTweaks.TESLA_MULTI_RANGE_TOWER,
            TRANSFER_RADIUS_TOWER_SETTING_NAME,
            TRANSFER_RADIUS_TOWER_STATUS);
        popogaSetting = hatch_2.makeInParameter(1, 0, POPOGA_NAME, POPOGA_STATUS);
        transferRadiusTransceiverSetting = hatch_3.makeInParameter(
            0,
            ConfigHandler.TeslaTweaks.TESLA_MULTI_RANGE_TRANSCEIVER,
            TRANSFER_RADIUS_TRANSCEIVER_SETTING_NAME,
            TRANSFER_RADIUS_TRANSCEIVER_STATUS);
        transferRadiusCoverUltimateSetting = hatch_3.makeInParameter(
            1,
            ConfigHandler.TeslaTweaks.TESLA_MULTI_RANGE_COVER,
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
        for (MTEHatchCapacitor cap : validMTEList(eCapacitorHatches)) {
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
        if (sparkCount == 0 && ConfigHandler.teslaTweaks.TESLA_VISUAL_EFFECT) {
            IGregTechTileEntity mte = getBaseMetaTileEntity();
            sparkCount = 20;
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
        return survivalBuildPiece("main", stackSize, 3, 16, 0, elementBudget, source, actor, false, true);
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

        return switch (plasmaTier) {
            case 0 -> ConfigHandler.TeslaTweaks.TESLA_MULTI_LOSS_PER_BLOCK_T0;
            case 1 -> ConfigHandler.TeslaTweaks.TESLA_MULTI_LOSS_PER_BLOCK_T1;
            case 2 -> ConfigHandler.TeslaTweaks.TESLA_MULTI_LOSS_PER_BLOCK_T2;
            default -> throw new IllegalStateException();
        };
    }

    @Override
    public float getTeslaOverdriveLossCoefficient() {
        return ConfigHandler.TeslaTweaks.TESLA_MULTI_LOSS_FACTOR_OVERDRIVE;
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

    @Override
    public boolean forceUseMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData guiData, PanelSyncManager syncManager) {
        UITexture bg = UITexture.builder()
            .location(MODID, "gui/background/screen_blue")
            .adaptable(2)
            .imageSize(90, 72)
            .canApplyTheme(true)
            .build();
        UITexture bgNoInv = UITexture.builder()
            .location(MODID, "gui/background/screen_blue_no_inventory")
            .canApplyTheme(true)
            .build();
        UITexture mesh = UITexture.builder()
            .location(MODID, "gui/overlay_slot/mesh")
            .canApplyTheme(true)
            .build();
        UITexture heatSinkSmall = UITexture.builder()
            .location(MODID, "gui/picture/heat_sink_small")
            .canApplyTheme(true)
            .build();
        ModularPanel panel = new ModularPanel("tesla_tower");
        panel.size(198, 191);

        syncManager.syncValue(
            "errors",
            new GenericSyncValue<EnumSet<StructureError>>(
                () -> structureErrors,
                val -> { structureErrors = val; },
                new StructureErrorAdapter()));
        syncManager.syncValue("errorID", new IntSyncValue(this::getErrorDisplayID, this::setErrorDisplayID));
        syncManager.syncValue(
            "machineActive",
            new BooleanSyncValue(
                () -> getBaseMetaTileEntity().isActive(),
                val -> getBaseMetaTileEntity().setActive(val)));

        syncManager.syncValue("wrench", new BooleanSyncValue(() -> mWrench, val -> mWrench = val));
        syncManager.syncValue("screwdriver", new BooleanSyncValue(() -> mScrewdriver, val -> mScrewdriver = val));
        syncManager.syncValue("softHammer", new BooleanSyncValue(() -> mSoftHammer, val -> mSoftHammer = val));
        syncManager.syncValue("hardHammer", new BooleanSyncValue(() -> mHardHammer, val -> mHardHammer = val));
        syncManager.syncValue("solderingTool", new BooleanSyncValue(() -> mSolderingTool, val -> mSolderingTool = val));
        syncManager.syncValue("crowbar", new BooleanSyncValue(() -> mCrowbar, val -> mCrowbar = val));
        syncManager.syncValue("machine", new BooleanSyncValue(() -> mMachine, val -> mMachine = val));

        syncManager.syncValue("totalRunTime", new LongSyncValue(() -> mTotalRunTime, time -> mTotalRunTime = time));
        syncManager
            .syncValue("lastWorkingTick", new LongSyncValue(() -> mLastWorkingTick, time -> mLastWorkingTick = time));
        syncManager.syncValue(
            "wasShutdown",
            new BooleanSyncValue(
                () -> getBaseMetaTileEntity().wasShutdown(),
                val -> getBaseMetaTileEntity().setShutdownStatus(val)));
        syncManager.syncValue(
            "shutdownReason",
            new GenericSyncValue<ShutDownReason>(
                () -> getBaseMetaTileEntity().getLastShutDownReason(),
                reason -> { getBaseMetaTileEntity().setShutDownReason(reason); },
                new ShutdownReasonAdapter()));
        syncManager.syncValue(
            "checkRecipeResult",
            new GenericSyncValue<CheckRecipeResult>(
                () -> checkRecipeResult,
                result -> { checkRecipeResult = result; },
                new CheckRecipeResultAdapter()));
        syncManager.syncValue(
            "fluidOutput",
            new GenericListSyncHandler<FluidStack>(
                () -> mOutputFluids != null ? Arrays.stream(mOutputFluids)
                    .map(fluidStack -> {
                        if (fluidStack == null) return null;
                        return new FluidStack(fluidStack, fluidStack.amount) {

                            @Override
                            public boolean isFluidEqual(FluidStack other) {
                                return super.isFluidEqual(other) && amount == other.amount;
                            }
                        };
                    })
                    .collect(Collectors.toList()) : Collections.emptyList(),
                val -> mOutputFluids = val.toArray(new FluidStack[0]),
                NetworkUtils::readFluidStack,
                NetworkUtils::writeFluidStack));
        syncManager.syncValue(
            "itemOutput",
            new GenericListSyncHandler<ItemStack>(
                () -> mOutputItems != null ? Arrays.asList(mOutputItems) : Collections.emptyList(),
                val -> mOutputItems = val.toArray(new ItemStack[0]),
                NetworkUtils::readItemStack,
                NetworkUtils::writeItemStack));
        syncManager.syncValue("progressTime", new IntSyncValue(() -> mProgresstime, val -> mProgresstime = val));
        syncManager
            .syncValue("maxProgressTime", new IntSyncValue(() -> mMaxProgresstime, val -> mMaxProgresstime = val));

        ListWidget<IWidget, ?> machineInfo = new ListWidget<>().size(178, 85)
            .pos(6, 3);

        if (doesBindPlayerInventory()) {
            panel.child(
                new SingleChildWidget<>().pos(4, 4)
                    .size(190, 91)
                    .overlay(bg)
                    .child(machineInfo));
        } else {
            panel.child(
                new SingleChildWidget<>().pos(4, 4)
                    .size(190, 171)
                    .overlay(bgNoInv));
        }
        final ItemStackHandler invSlot = new ItemStackHandler(1);
        if (doesBindPlayerInventory()) {
            panel.child(
                SlotGroupWidget.playerInventory()
                    .pos(7, 95 + 12 + 2));
            panel.child(
                new ItemSlot().slot(
                    SyncHandlers.itemSlot(invSlot, 0)
                        .singletonSlotGroup())
                    .pos(173, 167)
                    .overlay(mesh));
            panel.child(
                new SingleChildWidget<>().pos(173, 185)
                    .size(18, 6)
                    .overlay(heatSinkSmall));
        }

        insertTexts(machineInfo, invSlot);
        addTitleTextStyle(panel, "Tesla Tower");

        UITexture powerPassOn = UITexture.fullImage(MODID, "gui/overlay_button/power_pass_on");
        UITexture powerPassOff = UITexture.fullImage(MODID, "gui/overlay_button/power_pass_off");
        UITexture powerPassDisabled = UITexture.fullImage(MODID, "gui/overlay_button/power_pass_disabled");
        ButtonWidget powerPassButton = new ButtonWidget();

        powerPassButton.overlay(
            !isPowerPassButtonEnabled() && !ePowerPassCover ? powerPassDisabled
                : ePowerPass ? powerPassOn : powerPassOff);
        powerPassButton.tooltip(new RichTooltip(powerPassButton).add("Safe Void"));
        powerPassButton.syncHandler(new InteractionSyncHandler().setOnMousePressed(mouseData -> {
            TecTech.proxy.playSound(getBaseMetaTileEntity(), "fx_click");
            ePowerPass = !ePowerPass;
            if (!isAllowedToWorkButtonEnabled()) { // TRANSFORMER HACK
                if (ePowerPass) {
                    getBaseMetaTileEntity().enableWorking();
                } else {
                    getBaseMetaTileEntity().disableWorking();
                }
            }
            powerPassButton.overlay(
                !isPowerPassButtonEnabled() && !ePowerPassCover ? powerPassDisabled
                    : ePowerPass ? powerPassOn : powerPassOff);
        }));
        powerPassButton.pos(173, doesBindPlayerInventory() ? 109 : 133)
            .size(18, 18);
        panel.child(powerPassButton);

        IPanelHandler infoPanel = syncManager
            .panel("info_panel", (p_syncManager, syncHandler) -> getInfoPopup(panel), true);
        ButtonWidget editParametersButton = new ButtonWidget();
        editParametersButton.overlay(UITexture.fullImage(MODID, "gui/overlay_button/edit_parameters"));
        editParametersButton.tooltip(new RichTooltip(editParametersButton).add("Edit Parameters"));
        editParametersButton.pos(173, doesBindPlayerInventory() ? 109 + 18 : 133 + 18)
            .size(18, 18);
        editParametersButton
            .syncHandler(new InteractionSyncHandler().setOnMousePressed(mouseData -> { infoPanel.openPanel(); }));
        panel.child(editParametersButton);

        UITexture powerSwitchOn = UITexture.fullImage(MODID, "gui/overlay_button/power_switch_on");
        UITexture powerSwitchOff = UITexture.fullImage(MODID, "gui/overlay_button/power_switch_off");
        UITexture powerSwitchDisabled = UITexture.fullImage(MODID, "gui/overlay_button/power_switch_disabled");
        ButtonWidget powerSwitchButton = new ButtonWidget();

        powerSwitchButton.overlay(
            !isAllowedToWorkButtonEnabled() ? powerSwitchDisabled
                : getBaseMetaTileEntity().isAllowedToWork() ? powerSwitchOn : powerSwitchOff);
        powerSwitchButton.tooltip(new RichTooltip(powerSwitchButton).add("Power Switch"));
        powerSwitchButton.syncHandler(new InteractionSyncHandler().setOnMousePressed(mouseData -> {
            if (isAllowedToWorkButtonEnabled()) {
                TecTech.proxy.playSound(getBaseMetaTileEntity(), "fx_click");
                if (getBaseMetaTileEntity().isAllowedToWork()) {
                    getBaseMetaTileEntity().disableWorking();
                } else {
                    getBaseMetaTileEntity().enableWorking();
                }
            }
            powerSwitchButton.overlay(
                !isAllowedToWorkButtonEnabled() ? powerSwitchDisabled
                    : getBaseMetaTileEntity().isAllowedToWork() ? powerSwitchOn : powerSwitchOff);
        }));
        powerSwitchButton.pos(173, doesBindPlayerInventory() ? 109 + 18 * 2 : 133 + 18 * 2)
            .size(18, 18);
        panel.child(powerSwitchButton);
        return panel;
    }

    private ModularPanel getInfoPopup(ModularPanel parent) {
        Area parentArea = parent.getArea();
        ModularPanel panel = new ModularPanel("parameters").size(125, 191)
            .pos(parentArea.x + parentArea.width, parentArea.y);
        ListWidget parameterListWidget = new ListWidget();
        parameterListWidget.sizeRel(1)
            .margin(2);
        for (Map.Entry<String, Parameter<?>> entry : parameterMap.entrySet()) {
            Parameter<?> parameter = entry.getValue();
            TextFieldWidget parameterField = new TextFieldWidget();

            if (parameter instanceof Parameter.IntegerParameter intParameter) {
                parameterField.value(new IntSyncValue(intParameter::getValue, intParameter::setValue))
                    .setNumbers(intParameter::getMinValue, intParameter::getMaxValue);
            } else if (parameter instanceof Parameter.DoubleParameter doubleParameter) {
                parameterField.value(new DoubleSyncValue(doubleParameter::getValue, doubleParameter::setValue))
                    .setNumbersDouble(
                        val -> Math.max(doubleParameter.getMinValue(), Math.min(doubleParameter.getMaxValue(), val)));
            } else if (parameter instanceof Parameter.StringParameter stringParameter) {
                parameterField.value(new StringSyncValue(stringParameter::getValue, stringParameter::setValue));
            }
            parameterField.setText(parameter.getValueString());
            parameterField.sizeRel(0.9f, 0.5f)
                .align(Alignment.Center);

            ButtonWidget parameterButton = new ButtonWidget<>();
            if (parameter instanceof Parameter.BooleanParameter booleanParameter) {
                parameterButton.syncHandler(new InteractionSyncHandler().setOnMousePressed(mouseData -> {
                    booleanParameter.invert();
                    parameterButton.overlay(booleanParameter.getValue() ? GuiTextures.CHECK_BOX : GuiTextures.CROSS);
                }))
                    .overlay(booleanParameter.getValue() ? GuiTextures.CHECK_BOX : GuiTextures.CROSS)
                    .align(Alignment.Center)
                    .size(18, 18);
            }

            parameterListWidget.child(
                new Column().heightRel(0.2f)
                    .child(
                        IKey.str(parameter.getLocalizedName())
                            .asWidget()
                            .alignment(Alignment.Center)
                            .sizeRel(1, 0.5f))
                    .child(
                        new SingleChildWidget<>().sizeRel(1, 0.5f)
                            .child(parameter instanceof Parameter.BooleanParameter ? parameterButton : parameterField))
                    .marginBottom(2));
        }
        panel.child(parameterListWidget);
        return panel;
    }

    private void insertTexts(ListWidget<IWidget, ?> machineInfo, ItemStackHandler invSlot) {
        machineInfo.child(
            new TextWidget(GTUtility.trans("132", "Pipe is loose. (Wrench)")).color(COLOR_TEXT_WHITE.get())
                .setEnabledIf(widget -> !mWrench)
                .marginBottom(2)

        );

        machineInfo.child(
            new TextWidget(GTUtility.trans("133", "Screws are loose. (Screwdriver)")).color(COLOR_TEXT_WHITE.get())
                .setEnabledIf(widget -> !mScrewdriver)
                .marginBottom(2)

        );

        machineInfo.child(

            new TextWidget(GTUtility.trans("134", "Something is stuck. (Soft Mallet)")).color(COLOR_TEXT_WHITE.get())
                .setEnabledIf(widget -> !mSoftHammer)
                .marginBottom(2)

        );
        machineInfo.child(

            new TextWidget(GTUtility.trans("135", "Platings are dented. (Hammer)")).color(COLOR_TEXT_WHITE.get())
                .setEnabledIf(widget -> !mHardHammer)
                .marginBottom(2)

        );

        machineInfo.child(

            new TextWidget(GTUtility.trans("136", "Circuitry burned out. (Soldering)")).color(COLOR_TEXT_WHITE.get())
                .setEnabledIf(widget -> !mSolderingTool)
                .marginBottom(2)

        );

        machineInfo.child(

            new TextWidget(GTUtility.trans("137", "That doesn't belong there. (Crowbar)")).color(COLOR_TEXT_WHITE.get())
                .setEnabledIf(widget -> !mCrowbar)
                .marginBottom(2)

        );

        machineInfo.child(
            new TextWidget(GTUtility.trans("138", "Incomplete Structure.")).color(COLOR_TEXT_WHITE.get())
                .setEnabledIf(widget -> !mMachine)
                .marginBottom(2)

        );

        machineInfo.child(
            new TextWidget(StatCollector.translateToLocal("GT5U.gui.text.too_uncertain")).color(COLOR_TEXT_WHITE.get())
                .setEnabledIf(widget -> (getErrorDisplayID() & 128) != 0)
                .marginBottom(2)

        );

        machineInfo.child(
            new TextWidget(StatCollector.translateToLocal("GT5U.gui.text.invalid_parameters"))
                .color(COLOR_TEXT_WHITE.get())
                .setEnabledIf(widget -> (getErrorDisplayID() & 256) != 0)
                .marginBottom(2)

        );

        machineInfo.child(
            new TextWidget(
                GTUtility.trans("139", "Hit with Soft Mallet") + "\n"
                    + GTUtility.trans("140", "to (re-)start the Machine")
                    + "\n"
                    + GTUtility.trans("141", "if it doesn't start.")).color(COLOR_TEXT_WHITE.get())
                        .setEnabledIf(widget -> getErrorDisplayID() == 0 && !getBaseMetaTileEntity().isActive())
                        .marginBottom(2)

        );

        machineInfo.child(
            new TextWidget(GTUtility.trans("142", "Running perfectly.")).color(COLOR_TEXT_WHITE.get())
                .setEnabledIf(widget -> getErrorDisplayID() == 0 && getBaseMetaTileEntity().isActive())
                .marginBottom(2)

        );

        TextWidget shutdownDuration = IKey.dynamic(() -> {
            Duration time = Duration.ofSeconds((mTotalRunTime - mLastWorkingTick) / 20);
            return StatCollector.translateToLocalFormatted(
                "GT5U.gui.text.shutdown_duration",
                time.toHours(),
                time.toMinutes() % 60,
                time.getSeconds() % 60);
        })
            .asWidget();

        machineInfo.child(
            shutdownDuration.marginBottom(2)
                .setEnabledIf(
                    widget -> shouldDisplayShutDownReason() && !getBaseMetaTileEntity().isActive()
                        && getBaseMetaTileEntity().wasShutdown()));

        TextWidget shutdownReason = IKey.dynamic(
            () -> getBaseMetaTileEntity().getLastShutDownReason()
                .getDisplayString())
            .asWidget();

        machineInfo.child(
            shutdownReason.setEnabledIf(
                widget -> shouldDisplayShutDownReason() && !getBaseMetaTileEntity().isActive()
                    && GTUtility.isStringValid(
                        getBaseMetaTileEntity().getLastShutDownReason()
                            .getDisplayString())
                    && getBaseMetaTileEntity().wasShutdown()

            ));

        TextWidget checkRecipeResultWidget = IKey.dynamic(() -> this.checkRecipeResult.getDisplayString())
            .asWidget();
        machineInfo.child(
            checkRecipeResultWidget.marginBottom(2)
                .setEnabledIf(
                    widget -> shouldDisplayCheckRecipeResult()
                        && GTUtility.isStringValid(checkRecipeResult.getDisplayString())
                        && (isAllowedToWork() || getBaseMetaTileEntity().isActive()
                            || checkRecipeResult.persistsOnShutdown())));

        if (showRecipeTextInGUI()) {
            // Display current recipe
            TextWidget recipeInfoWidget = IKey.dynamic(this::generateCurrentRecipeInfoString)
                .asWidget();
            machineInfo.child(
                recipeInfoWidget.marginBottom(2)
                    .setEnabledIf(
                        widget -> (mOutputFluids != null && mOutputFluids.length > 0)
                            || (mOutputItems != null && mOutputItems.length > 0)));
        }

        machineInfo.child(
            new TextWidget(GTUtility.trans("144", "Missing Turbine Rotor")).color(COLOR_TEXT_WHITE.get())
                .setEnabledIf(widget -> {
                    if (getBaseMetaTileEntity().isAllowedToWork()) return false;
                    // if (getErrorDisplayID() == 0 && this instanceof MTELargeTurbine) {
                    // final ItemStack tItem = inventorySlot.getMcSlot()
                    // .getStack();
                    // return tItem == null
                    // || !(tItem.getItem() == MetaGeneratedTool01.INSTANCE && tItem.getItemDamage() >= 170
                    // && tItem.getItemDamage() <= 177);
                    // }
                    return false;
                })
                .marginBottom(2));
    }

    protected void addTitleTextStyle(ModularPanel panel, String title) {
        final int TAB_PADDING = 3;
        final int TITLE_PADDING = 2;
        int titleWidth = 0, titleHeight = 0;
        if (NetworkUtils.isClient()) {
            final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
            final List<String> titleLines = fontRenderer
                .listFormattedStringToWidth(title, getGUIWidth() - (TAB_PADDING + TITLE_PADDING) * 2);
            titleWidth = titleLines.size() > 1 ? getGUIWidth() - (TAB_PADDING + TITLE_PADDING) * 2
                : fontRenderer.getStringWidth(title);
            // noinspection PointlessArithmeticExpression
            titleHeight = titleLines.size() * fontRenderer.FONT_HEIGHT + (titleLines.size() - 1) * 1;
        }

        final SingleChildWidget<?> tab = new SingleChildWidget<>();
        final TextWidget text = new TextWidget(title).color(0x404040)
            .alignment(Alignment.CenterLeft)
            .width(titleWidth);

        UITexture angular = UITexture.builder()
            .location(GregTech.ID, "gui/tab/title_angular_%s")
            .adaptable(4)
            .imageSize(18, 18)
            .canApplyTheme(true)
            .build();
        UITexture dark = UITexture.builder()
            .location(GregTech.ID, "gui/tab/title_dark")
            .adaptable(4)
            .imageSize(28, 28)
            .canApplyTheme(true)
            .build();
        if (GTMod.gregtechproxy.mTitleTabStyle == 1) {
            panel.child(
                angular.asWidget()
                    .pos(0, -(titleHeight + TAB_PADDING) + 1)
                    .size(getGUIWidth(), titleHeight + TAB_PADDING * 2));
            text.pos(TAB_PADDING + TITLE_PADDING, -titleHeight + TAB_PADDING);
        } else {
            panel.child(
                dark.asWidget()
                    .pos(0, -(titleHeight + TAB_PADDING * 2) + 1)
                    .size(titleWidth + (TAB_PADDING + TITLE_PADDING) * 2, titleHeight + TAB_PADDING * 2 - 1));
            text.pos(TAB_PADDING + TITLE_PADDING, -titleHeight);
        }
        panel.child(text);
    }

    private class StructureErrorAdapter implements IByteBufAdapter<EnumSet<StructureError>> {

        @Override
        public EnumSet<StructureError> deserialize(PacketBuffer buffer) {
            byte[] data = new byte[buffer.readVarIntFromBuffer()];
            buffer.readBytes(data);

            BitSet bits = BitSet.valueOf(data);

            EnumSet<StructureError> out = EnumSet.noneOf(StructureError.class);

            for (StructureError error : StructureError.values()) {
                if (bits.get(error.ordinal())) {
                    out.add(error);
                }
            }

            return out;
        }

        @Override
        public void serialize(PacketBuffer buffer, EnumSet<StructureError> errors) {
            BitSet bits = new BitSet();

            for (StructureError error : errors) {
                bits.set(error.ordinal());
            }

            byte[] data = bits.toByteArray();

            buffer.writeVarIntToBuffer(data.length);
            buffer.writeBytes(data);
        }

        @Override
        public boolean areEqual(@NotNull EnumSet<StructureError> t1, @NotNull EnumSet<StructureError> t2) {
            return false;
        }
    }

    private class ShutdownReasonAdapter implements IByteBufAdapter<ShutDownReason> {

        @Override
        public ShutDownReason deserialize(PacketBuffer buffer) throws IOException {
            String id = NetworkUtils.readStringSafe(buffer);
            ShutDownReason result = ShutDownReasonRegistry.getSampleFromRegistry(id)
                .newInstance();
            result.decode(buffer);
            return result;
        }

        @Override
        public void serialize(PacketBuffer buffer, ShutDownReason result) throws IOException {
            NetworkUtils.writeStringSafe(buffer, result.getID());
            result.encode(buffer);
        }

        @Override
        public boolean areEqual(@NotNull ShutDownReason t1, @NotNull ShutDownReason t2) {
            return false;
        }
    }

    private class CheckRecipeResultAdapter implements IByteBufAdapter<CheckRecipeResult> {

        @Override
        public CheckRecipeResult deserialize(PacketBuffer buffer) throws IOException {
            String id = NetworkUtils.readStringSafe(buffer);
            CheckRecipeResult result = CheckRecipeResultRegistry.getSampleFromRegistry(id)
                .newInstance();
            result.decode(buffer);
            return result;
        }

        @Override
        public void serialize(PacketBuffer buffer, CheckRecipeResult result) throws IOException {
            NetworkUtils.writeStringSafe(buffer, result.getID());
            result.encode(buffer);
        }

        @Override
        public boolean areEqual(@NotNull CheckRecipeResult t1, @NotNull CheckRecipeResult t2) {
            return false;
        }
    }

}

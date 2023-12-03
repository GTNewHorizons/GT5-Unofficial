package com.github.technus.tectech.thing.metaTileEntity.multi;

import static com.github.technus.tectech.mechanics.tesla.ITeslaConnectable.TeslaUtil.generateTeslaNodeMap;
import static com.github.technus.tectech.mechanics.tesla.ITeslaConnectable.TeslaUtil.powerTeslaNodeMap;
import static com.github.technus.tectech.mechanics.tesla.ITeslaConnectable.TeslaUtil.teslaSimpleNodeSetAdd;
import static com.github.technus.tectech.mechanics.tesla.ITeslaConnectable.TeslaUtil.teslaSimpleNodeSetRemove;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.texturePage;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsBA0;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM.HatchElement.DynamoMulti;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM.HatchElement.EnergyMulti;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM.HatchElement.Param;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.STATUS_HIGH;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.STATUS_LOW;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.STATUS_NEUTRAL;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.STATUS_OK;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.STATUS_TOO_HIGH;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.STATUS_TOO_LOW;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.STATUS_WRONG;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.STATUS_WTF;
import static com.github.technus.tectech.util.CommonValues.V;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Dynamo;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_Utility.filterValidMTEs;
import static java.lang.Math.min;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.loader.NetworkDispatcher;
import com.github.technus.tectech.mechanics.spark.RendererMessage;
import com.github.technus.tectech.mechanics.spark.ThaumSpark;
import com.github.technus.tectech.mechanics.tesla.ITeslaConnectable;
import com.github.technus.tectech.mechanics.tesla.ITeslaConnectableSimple;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_Capacitor;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_DynamoMulti;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyMulti;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_Param;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.INameFunction;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.IStatusFunction;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.Parameters;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.render.TT_RenderedExtendedFacingTexture;
import com.github.technus.tectech.util.CommonValues;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.StructureUtility;
import com.gtnewhorizon.structurelib.util.ItemStackPredicate;
import com.gtnewhorizon.structurelib.util.Vec3Impl;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Frame;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Dynamo;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Maintenance;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.IGT_HatchAdder;
import gregtech.common.blocks.GT_Item_Machines;

public class GT_MetaTileEntity_TM_teslaCoil extends GT_MetaTileEntity_MultiblockBase_EM
        implements ISurvivalConstructable, ITeslaConnectable {

    // Interface fields
    private final Multimap<Integer, ITeslaConnectableSimple> teslaNodeMap = MultimapBuilder.treeKeys()
            .linkedListValues().build();
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

    private final ArrayList<GT_MetaTileEntity_Hatch_Capacitor> eCapacitorHatches = new ArrayList<>(); // Capacitor
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

    private static final IStructureDefinition<GT_MetaTileEntity_TM_teslaCoil> STRUCTURE_DEFINITION = IStructureDefinition
            .<GT_MetaTileEntity_TM_teslaCoil>builder()
            .addShape(
                    "main",
                    transpose(
                            new String[][] {
                                    { "       ", "       ", "  BBB  ", "  BBB  ", "  BBB  ", "       ", "       " },
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
            .addElement('A', ofBlock(sBlockCasingsBA0, 6)).addElement('B', ofBlock(sBlockCasingsBA0, 7))
            .addElement('C', ofBlock(sBlockCasingsBA0, 8))
            .addElement(
                    'D',
                    ofBlocksTiered(
                            (block, meta) -> block != sBlockCasingsBA0 ? null
                                    : meta <= 5 ? Integer.valueOf(meta) : meta == 9 ? 6 : null,
                            IntStream.range(0, 7).map(tier -> tier == 6 ? 9 : tier)
                                    .mapToObj(meta -> Pair.of(sBlockCasingsBA0, meta)).collect(Collectors.toList()),
                            -1,
                            (t, v) -> t.mTier = v,
                            t -> t.mTier))
            .addElement(
                    'E',
                    buildHatchAdder(GT_MetaTileEntity_TM_teslaCoil.class)
                            .atLeast(
                                    CapacitorHatchElement.INSTANCE,
                                    EnergyMulti,
                                    Energy,
                                    DynamoMulti,
                                    Dynamo,
                                    InputHatch,
                                    OutputHatch,
                                    Param,
                                    Maintenance)
                            .dot(1).casingIndex(textureOffset + 16 + 6).buildAndChain(sBlockCasingsBA0, 6))
            .addElement('F', new IStructureElement<GT_MetaTileEntity_TM_teslaCoil>() {

                private IIcon[] mIcons;

                @Override
                public boolean check(GT_MetaTileEntity_TM_teslaCoil t, World world, int x, int y, int z) {
                    TileEntity tBase = world.getTileEntity(x, y, z);
                    if (tBase instanceof BaseMetaPipeEntity tPipeBase) {
                        if (tPipeBase.isInvalidTileEntity()) return false;
                        return tPipeBase.getMetaTileEntity() instanceof GT_MetaPipeEntity_Frame;
                    }
                    return false;
                }

                @Override
                public boolean spawnHint(GT_MetaTileEntity_TM_teslaCoil t, World world, int x, int y, int z,
                        ItemStack trigger) {
                    if (mIcons == null) {
                        mIcons = new IIcon[6];
                        Arrays.fill(
                                mIcons,
                                Materials._NULL.mIconSet.mTextures[OrePrefixes.frameGt.mTextureIndex].getIcon());
                    }
                    StructureLibAPI.hintParticleTinted(world, x, y, z, mIcons, Materials._NULL.mRGBa);
                    return true;
                }

                @Override
                public boolean placeBlock(GT_MetaTileEntity_TM_teslaCoil t, World world, int x, int y, int z,
                        ItemStack trigger) {
                    ItemStack tFrameStack = GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Titanium, 1);
                    if (!GT_Utility.isStackValid(tFrameStack)
                            || !(tFrameStack.getItem() instanceof ItemBlock tFrameStackItem))
                        return false;
                    return tFrameStackItem.placeBlockAt(
                            tFrameStack,
                            null,
                            world,
                            x,
                            y,
                            z,
                            6,
                            0,
                            0,
                            0,
                            Items.feather.getDamage(tFrameStack));
                }

                @Override
                public PlaceResult survivalPlaceBlock(GT_MetaTileEntity_TM_teslaCoil t, World world, int x, int y,
                        int z, ItemStack trigger, IItemSource source, EntityPlayerMP actor,
                        Consumer<IChatComponent> chatter) {
                    if (check(t, world, x, y, z)) return PlaceResult.SKIP;
                    ItemStack tFrameStack = source.takeOne(
                            s -> GT_Item_Machines.getMetaTileEntity(s) instanceof GT_MetaPipeEntity_Frame,
                            true);
                    if (tFrameStack == null) return PlaceResult.REJECT;
                    return StructureUtility.survivalPlaceBlock(
                            tFrameStack,
                            ItemStackPredicate.NBTMode.IGNORE_KNOWN_INSIGNIFICANT_TAGS,
                            null,
                            false,
                            world,
                            x,
                            y,
                            z,
                            source,
                            actor,
                            chatter);
                }
            }).build();
    // endregion

    // region parameters
    protected Parameters.Group.ParameterIn popogaSetting, histLowSetting, histHighSetting, transferRadiusTowerSetting,
            transferRadiusTransceiverSetting, transferRadiusCoverUltimateSetting, outputVoltageSetting,
            outputCurrentSetting, sortTimeMinSetting, overDriveSetting;
    protected Parameters.Group.ParameterOut popogaDisplay, transferRadiusTowerDisplay, transferRadiusTransceiverDisplay,
            transferRadiusCoverUltimateDisplay, outputVoltageDisplay, outputCurrentDisplay, energyCapacityDisplay,
            energyStoredDisplay, energyFractionDisplay, sortTimeDisplay;

    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> HYSTERESIS_LOW_SETTING_NAME = (base,
            p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgi.0"); // Hysteresis low setting
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> HYSTERESIS_HIGH_SETTING_NAME = (base,
            p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgi.1"); // Hysteresis high setting
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> TRANSFER_RADIUS_TOWER_SETTING_NAME = (base,
            p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgi.2"); // Tesla Towers transfer radius
                                                                                         // setting
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> TRANSFER_RADIUS_TRANSCEIVER_SETTING_NAME = (base,
            p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgi.3"); // Tesla Transceiver transfer
                                                                                         // radius setting
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> TRANSFER_RADIUS_COVER_ULTIMATE_SETTING_NAME = (
            base, p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgi.4"); // Tesla Ultimate Cover
                                                                                               // transfer radius
    // setting
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> OUTPUT_VOLTAGE_SETTING_NAME = (base,
            p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgi.5"); // Output voltage setting
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> OUTPUT_CURRENT_SETTING_NAME = (base,
            p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgi.6"); // Output current setting
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> SCAN_TIME_MIN_SETTING_NAME = (base,
            p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgi.7"); // Scan time Min setting
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> OVERDRIVE_SETTING_NAME = (base,
            p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgi.8"); // Overdrive setting
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> POPOGA_NAME = (base,
            p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgi.9"); // Unused

    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> TRANSFER_RADIUS_TOWER_DISPLAY_NAME = (base,
            p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgo.0"); // Tesla Towers transfer radius
                                                                                         // display
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> TRANSFER_RADIUS_TRANSCEIVER_DISPLAY_NAME = (base,
            p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgo.1"); // Tesla Transceiver transfer
                                                                                         // radius display
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> TRANSFER_RADIUS_COVER_ULTIMATE_DISPLAY_NAME = (
            base, p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgo.2"); // Tesla Ultimate Cover
                                                                                               // transfer radius
    // display
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> OUTPUT_VOLTAGE_DISPLAY_NAME = (base,
            p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgo.3"); // Output voltage display
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> OUTPUT_CURRENT_DISPLAY_NAME = (base,
            p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgo.4"); // Output current display
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> ENERGY_CAPACITY_DISPLAY_NAME = (base,
            p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgo.5"); // Energy Capacity display
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> ENERGY_STORED_DISPLAY_NAME = (base,
            p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgo.6"); // Energy Stored display
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> ENERGY_FRACTION_DISPLAY_NAME = (base,
            p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgo.7"); // Energy Fraction display
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> SCAN_TIME_DISPLAY_NAME = (base,
            p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgo.8"); // Scan time display

    private static final IStatusFunction<GT_MetaTileEntity_TM_teslaCoil> HYSTERESIS_LOW_STATUS = (base, p) -> {
        double value = p.get();
        if (Double.isNaN(value)) {
            return STATUS_WRONG;
        }
        if (value <= 0.05) return STATUS_TOO_LOW;
        if (value > base.histHighSetting.get()) return STATUS_TOO_HIGH;
        return STATUS_OK;
    };
    private static final IStatusFunction<GT_MetaTileEntity_TM_teslaCoil> HYSTERESIS_HIGH_STATUS = (base, p) -> {
        double value = p.get();
        if (Double.isNaN(value)) return STATUS_WRONG;
        if (value <= base.histLowSetting.get()) return STATUS_TOO_LOW;
        if (value > 0.95) return STATUS_TOO_HIGH;
        return STATUS_OK;
    };
    private static final IStatusFunction<GT_MetaTileEntity_TM_teslaCoil> TRANSFER_RADIUS_TOWER_STATUS = (base, p) -> {
        double value = p.get();
        if (Double.isNaN(value)) return STATUS_WRONG;
        value = (int) value;
        if (value < 0) return STATUS_TOO_LOW;
        if (value > transferRadiusTowerFromConfig) return STATUS_HIGH;
        if (value < transferRadiusTowerFromConfig) return STATUS_LOW;
        return STATUS_OK;
    };
    private static final IStatusFunction<GT_MetaTileEntity_TM_teslaCoil> TRANSFER_RADIUS_TRANSCEIVER_STATUS = (base,
            p) -> {
        double value = p.get();
        if (Double.isNaN(value)) return STATUS_WRONG;
        value = (int) value;
        if (value < 0) return STATUS_TOO_LOW;
        if (value > transferRadiusTransceiverFromConfig) return STATUS_HIGH;
        if (value < transferRadiusTransceiverFromConfig) return STATUS_LOW;
        return STATUS_OK;
    };
    private static final IStatusFunction<GT_MetaTileEntity_TM_teslaCoil> TRANSFER_RADIUS_COVER_ULTIMATE_STATUS = (base,
            p) -> {
        double value = p.get();
        if (Double.isNaN(value)) return STATUS_WRONG;
        value = (int) value;
        if (value < 0) return STATUS_TOO_LOW;
        if (value > transferRadiusCoverUltimateFromConfig) return STATUS_HIGH;
        if (value < transferRadiusCoverUltimateFromConfig) return STATUS_LOW;
        return STATUS_OK;
    };
    private static final IStatusFunction<GT_MetaTileEntity_TM_teslaCoil> OUTPUT_VOLTAGE_OR_CURRENT_STATUS = (base,
            p) -> {
        double value = p.get();
        if (Double.isNaN(value)) return STATUS_WRONG;
        value = (long) value;
        if (value == -1) return STATUS_OK;
        if (value <= 0) return STATUS_TOO_LOW;
        return STATUS_OK;
    };
    private static final IStatusFunction<GT_MetaTileEntity_TM_teslaCoil> SCAN_TIME_MIN_STATUS = (base, p) -> {
        double value = p.get();
        if (Double.isNaN(value)) return STATUS_WRONG;
        value = (int) value;
        if (value < 100) return STATUS_TOO_LOW;
        if (value == 100) return STATUS_OK;
        return STATUS_HIGH;
    };
    private static final IStatusFunction<GT_MetaTileEntity_TM_teslaCoil> OVERDRIVE_STATUS = (base, p) -> {
        double value = p.get();
        if (Double.isNaN(value)) return STATUS_WRONG;
        value = (int) value;
        if (value < 0) return STATUS_TOO_LOW;
        if (value == 0) return STATUS_LOW;
        return STATUS_HIGH;
    };
    private static final IStatusFunction<GT_MetaTileEntity_TM_teslaCoil> POPOGA_STATUS = (base, p) -> {
        if (base.getBaseMetaTileEntity().getWorld().isThundering()) {
            return STATUS_WTF;
        }
        return STATUS_NEUTRAL;
    };
    private static final IStatusFunction<GT_MetaTileEntity_TM_teslaCoil> SCAN_TIME_STATUS = (base, p) -> {
        double value = p.get();
        if (Double.isNaN(value)) return STATUS_WRONG;
        value = (int) value;
        if (value == 0) return STATUS_HIGH;
        return STATUS_LOW;
    };
    private static final IStatusFunction<GT_MetaTileEntity_TM_teslaCoil> POWER_STATUS = (base, p) -> {
        double value = p.get();
        if (Double.isNaN(value)) return STATUS_WRONG;
        value = (long) value;
        if (value > 0) {
            return STATUS_OK;
        } else {
            return STATUS_LOW;
        }
    };
    private static final IStatusFunction<GT_MetaTileEntity_TM_teslaCoil> ENERGY_STATUS = (base, p) -> {
        double value = p.get();
        if (Double.isNaN(value)) return STATUS_WRONG;
        if (base.energyFractionDisplay.get() > base.histHighSetting.get()) {
            return STATUS_HIGH;
        } else if (base.energyFractionDisplay.get() < base.histLowSetting.get()) {
            return STATUS_LOW;
        } else {
            return STATUS_OK;
        }
    };
    // endregion

    public GT_MetaTileEntity_TM_teslaCoil(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_TM_teslaCoil(String aName) {
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

        for (GT_MetaTileEntity_Hatch_Input fluidHatch : mInputHatches) {
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
                        } else
                    if (fluidHatch.mFluid.isFluidEqual(Materials.Radon.getPlasma(1))
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
        return new GT_MetaTileEntity_TM_teslaCoil(mName);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        for (GT_MetaTileEntity_Hatch_Capacitor cap : filterValidMTEs(eCapacitorHatches)) {
            cap.getBaseMetaTileEntity().setActive(false);
        }
        eCapacitorHatches.clear();

        mTier = -1;

        if (structureCheck_EM("main", 3, 16, 0)) {
            for (GT_MetaTileEntity_Hatch_Capacitor cap : filterValidMTEs(eCapacitorHatches)) {
                cap.getBaseMetaTileEntity().setActive(iGregTechTileEntity.isActive());
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
                posTop = getExtendedFacing().getWorldOffset(new Vec3Impl(0, 0, 2)).add(posBMTE);

                // Calculate coordinates of the top sphere
                posTop = getExtendedFacing().getWorldOffset(new Vec3Impl(0, -14, 2)).add(posBMTE);
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
        for (GT_MetaTileEntity_Hatch_Capacitor cap : filterValidMTEs(eCapacitorHatches)) {
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
        for (GT_MetaTileEntity_Hatch_Capacitor cap : filterValidMTEs(eCapacitorHatches)) {
            cap.getBaseMetaTileEntity().setActive(true);
            capacitorData = cap.getCapacitors();
            if (capacitorData[0] < vTier) {
                if (getEUVar() > 0 && capacitorData[0] != 0) {
                    cap.getBaseMetaTileEntity().setToFire();
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
    public GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
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
                .addSeparator().beginStructureBlock(7, 17, 7, false)
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
            return new ITexture[] { Textures.BlockIcons.casingTexturePages[texturePage][16 + 6],
                    new TT_RenderedExtendedFacingTexture(aActive ? ScreenON : ScreenOFF) };
        }
        return new ITexture[] { Textures.BlockIcons.casingTexturePages[texturePage][16 + 6] };
    }

    @Override
    public void onRemoval() {
        super.onRemoval();
        if (!getBaseMetaTileEntity().isClientSide()) {
            teslaSimpleNodeSetRemove(this);
            for (GT_MetaTileEntity_Hatch_Capacitor cap : filterValidMTEs(eCapacitorHatches)) {
                cap.getBaseMetaTileEntity().setActive(false);
            }
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
        transferRadiusCoverUltimateDisplay = hatch_3.makeOutParameter(
                1,
                0,
                TRANSFER_RADIUS_COVER_ULTIMATE_DISPLAY_NAME,
                TRANSFER_RADIUS_COVER_ULTIMATE_STATUS);
        outputVoltageDisplay = hatch_4.makeOutParameter(0, 0, OUTPUT_VOLTAGE_DISPLAY_NAME, POWER_STATUS);
        popogaDisplay = hatch_4.makeOutParameter(1, 0, POPOGA_NAME, POPOGA_STATUS);
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
        teslaSimpleNodeSetAdd(this);
    }

    @Override
    public void stopMachine() {
        super.stopMachine();
        for (GT_MetaTileEntity_Hatch_Capacitor cap : eCapacitorHatches) {
            cap.getBaseMetaTileEntity().setActive(false);
        }

        ePowerPass = false;
        setEUVar(0);
        energyStoredDisplay.set(0);
        energyFractionDisplay.set(0);
    }

    @Override
    public void onFirstTick_EM(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick_EM(aBaseMetaTileEntity);
        if (!aBaseMetaTileEntity.isClientSide()) {
            teslaSimpleNodeSetAdd(this);
            generateTeslaNodeMap(this);
        }
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
        outputCurrentDisplay.set(powerTeslaNodeMap(this));

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
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Capacitor) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return eCapacitorHatches.add((GT_MetaTileEntity_Hatch_Capacitor) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return mMaintenanceHatches.add((GT_MetaTileEntity_Hatch_Maintenance) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return mEnergyHatches.add((GT_MetaTileEntity_Hatch_Energy) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_EnergyMulti) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return eEnergyMulti.add((GT_MetaTileEntity_Hatch_EnergyMulti) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Dynamo) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return mDynamoHatches.add((GT_MetaTileEntity_Hatch_Dynamo) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_DynamoMulti) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return eDynamoMulti.add((GT_MetaTileEntity_Hatch_DynamoMulti) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Param) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return eParamHatches.add((GT_MetaTileEntity_Hatch_Param) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return mInputHatches.add((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return mOutputHatches.add((GT_MetaTileEntity_Hatch_Output) aMetaTileEntity);
        }
        return false;
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_TM_teslaCoil> getStructure_EM() {
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
        return this.getBaseMetaTileEntity().getWorld().provider.dimensionId;
    }

    @Override
    public boolean teslaInjectEnergy(long teslaVoltageInjected) {
        if (this.getEUVar() + teslaVoltageInjected <= (this.maxEUStore() / 2)) {
            this.getBaseMetaTileEntity().increaseStoredEnergyUnits(teslaVoltageInjected, true);
            return true;
        }
        return false;
    }

    private enum CapacitorHatchElement implements IHatchElement<GT_MetaTileEntity_TM_teslaCoil> {

        INSTANCE;

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return Collections.singletonList(GT_MetaTileEntity_Hatch_Capacitor.class);
        }

        @Override
        public IGT_HatchAdder<? super GT_MetaTileEntity_TM_teslaCoil> adder() {
            return GT_MetaTileEntity_TM_teslaCoil::addCapacitorToMachineList;
        }

        @Override
        public long count(GT_MetaTileEntity_TM_teslaCoil gt_metaTileEntity_tm_teslaCoil) {
            return gt_metaTileEntity_tm_teslaCoil.eCapacitorHatches.size();
        }
    }
}

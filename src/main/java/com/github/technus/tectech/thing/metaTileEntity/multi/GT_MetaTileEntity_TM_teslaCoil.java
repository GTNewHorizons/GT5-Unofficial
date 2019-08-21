package com.github.technus.tectech.thing.metaTileEntity.multi;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.Util;
import com.github.technus.tectech.loader.NetworkDispatcher;
import com.github.technus.tectech.mechanics.data.RendererMessage;
import com.github.technus.tectech.thing.cover.GT_Cover_TM_TeslaCoil;
import com.github.technus.tectech.thing.cover.GT_Cover_TM_TeslaCoil_Ultimate;
import com.github.technus.tectech.thing.metaTileEntity.IConstructable;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_Capacitor;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_DynamoMulti;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyMulti;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_Param;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.*;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.render.TT_RenderedTexture;
import com.github.technus.tectech.thing.metaTileEntity.single.GT_MetaTileEntity_TeslaCoil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.*;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static com.github.technus.tectech.CommonValues.V;
import static com.github.technus.tectech.Util.*;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.texturePage;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsBA0;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.*;
import static gregtech.api.enums.GT_Values.E;

public class GT_MetaTileEntity_TM_teslaCoil extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {
    private final static HashSet<Util.thaumSpark> sparkList = new HashSet<Util.thaumSpark>();

    private static Textures.BlockIcons.CustomIcon ScreenOFF;
    private static Textures.BlockIcons.CustomIcon ScreenON;

    private int mTier = 0; //Determines max voltage and efficiency (MV to LuV)
    private int maxTier = 6; //Max tier for efficiency calcuation

    private float energyEfficiency = 1;
    private float overdriveEfficiency = 1;
    private float minEfficiency = TecTech.configTecTech.TESLA_MULTI_MIN_EFFICIENCY;//Default is 0.955F
    private float maxEfficiency = TecTech.configTecTech.TESLA_MULTI_MAX_EFFICIENCY;//Default is 0.98F;
    private float overdriveEfficiencyExtra = TecTech.configTecTech.TESLA_MULTI_OVERDRIVE_LOSS;//Default is 0.005F

    private Map<IGregTechTileEntity, Integer> eTeslaMap = new HashMap<>(); //Used to store targets for power transmission
    private final ArrayList<GT_MetaTileEntity_Hatch_Capacitor> eCapacitorHatches = new ArrayList<>(); //Used to determine count and tier of capacitors present

    private int scanTime = 0; //Scan timer used for tesla search intervals

    private long energyCapacity = 0; //Total energy storage limited by capacitors
    private long outputVoltageMax = 0; //Tesla voltage output limited by capacitors
    private int vTier = -1; //Tesla voltage tier limited by capacitors
    private long outputCurrentMax = 0; //Tesla current output limited by capacitors

    //Prevents unnecessary offset calculation
    private byte oldRotation = -1;
    private byte oldOrientation = -1;

    //Coordinate Arrays
    private int[][] scanPosOffsets = new int[10][3];
    private int[] posZap = new int[3];//Power Transfer Origin
    public int[] posTop = new int[3];//Lightning Origin

    //region structure
    private static final String[][] shape = new String[][]{//3 16 0
            {"\u000F", "A  .  ",},
            {E, "B000", "B000", "B000", "\u0001", "B000", E, "B000", E, "B000", E, "B000", "\u0001", "B111", " 22222 ",},
            {"B000", "A00000", "A00000", "A00000", "B000", E, "A0A!A0", E, "A0A!A0", E, "A0A!A0", E, "A0A!A0", "\u0001", "A1C1", " 21112 ",},
            {"B000", "A00000", "A00000", "A00000", "B030", "C3", "A0!3!0", "C3", "A0!3!0", "C3", "A0!3!0", "C3", "A0!3!0", "C3", "C3", "A1A3A1", " 21212 ",},
            {"B000", "A00000", "A00000", "A00000", "B000", E, "A0A!A0", E, "A0A!A0", E, "A0A!A0", E, "A0A!A0", "\u0001", "A1C1", " 21112 ",},
            {E, "B000", "B000", "B000", "\u0001", "B000", E, "B000", E, "B000", E, "B000", "\u0001", "B111", " 22222 ",},
            {"\u000F", "A     ",},
    };
    private static final Block[] blockType = new Block[]{sBlockCasingsBA0, sBlockCasingsBA0, sBlockCasingsBA0, sBlockCasingsBA0};
    private static final byte[] blockMetaT0 = new byte[]{7, 0, 6, 8};
    private static final byte[] blockMetaT1 = new byte[]{7, 1, 6, 8};
    private static final byte[] blockMetaT2 = new byte[]{7, 2, 6, 8};
    private static final byte[] blockMetaT3 = new byte[]{7, 3, 6, 8};
    private static final byte[] blockMetaT4 = new byte[]{7, 4, 6, 8};
    private static final byte[] blockMetaT5 = new byte[]{7, 5, 6, 8};
    private static final byte[][] blockMetas = new byte[][]{blockMetaT0, blockMetaT1, blockMetaT2, blockMetaT3, blockMetaT4, blockMetaT5};
    private final IHatchAdder[] addingMethods = new IHatchAdder[]{this::addCapacitorToMachineList, this::addFrameToMachineList};
    private static final short[] casingTextures = new short[]{(texturePage << 7) + 16 + 6, 0};
    private static final Block[] blockTypeFallback = new Block[]{sBlockCasingsBA0, null};
    private static final byte[] blockMetaFallback = new byte[]{6, 0};
    private static final String[] description = new String[]{
            EnumChatFormatting.AQUA + "Hint Details:",
            "1 - Classic Hatches, Capacitor Hatches or Tesla Base Casing",
            "2 - Titanium Frames",
    };
    //endregion

    //region parameters
    protected Parameters.Group.ParameterIn popogaSetting, histLowSetting, histHighSetting, transferRadiusTowerSetting, transferRadiusTransceiverSetting, transferRadiusCoverUltimateSetting, outputVoltageSetting, outputCurrentSetting, scanTimeMinSetting, overDriveSetting;
    protected Parameters.Group.ParameterOut popogaDisplay, transferRadiusTowerDisplay, transferRadiusTransceiverDisplay, transferRadiusCoverUltimateDisplay, outputVoltageDisplay, outputCurrentDisplay, energyCapacityDisplay, energyStoredDisplay, energyFractionDisplay, scanTimeDisplay;

    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> HYSTERESIS_LOW_SETTING_NAME = (base, p) -> "Hysteresis low setting";
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> HYSTERESIS_HIGH_SETTING_NAME = (base, p) -> "Hysteresis high setting";
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> TRANSFER_RADIUS_TOWER_SETTING_NAME = (base, p) -> "Tesla Towers transfer radius setting";
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> TRANSFER_RADIUS_TRANSCEIVER_SETTING_NAME = (base, p) -> "Tesla Transceiver transfer radius setting";
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> TRANSFER_RADIUS_COVER_ULTIMATE_SETTING_NAME = (base, p) -> "Tesla Ultimate Cover transfer radius setting";
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> OUTPUT_VOLTAGE_SETTING_NAME = (base, p) -> "Output voltage setting";
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> OUTPUT_CURRENT_SETTING_NAME = (base, p) -> "Output current setting";
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> SCAN_TIME_MIN_SETTING_NAME = (base, p) -> "Scan time Min setting";
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> OVERDRIVE_SETTING_NAME = (base, p) -> "Overdrive setting";
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> POPOGA_NAME = (base, p) -> "Unused";

    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> TRANSFER_RADIUS_TOWER_DISPLAY_NAME = (base, p) -> "Tesla Towers transfer radius display";
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> TRANSFER_RADIUS_TRANSCEIVER_DISPLAY_NAME = (base, p) -> "Tesla Transceiver transfer radius display";
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> TRANSFER_RADIUS_COVER_ULTIMATE_DISPLAY_NAME = (base, p) -> "Tesla Ultimate Cover transfer radius display";
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> OUTPUT_VOLTAGE_DISPLAY_NAME = (base, p) -> "Output voltage display";
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> OUTPUT_CURRENT_DISPLAY_NAME = (base, p) -> "Output current display";
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> ENERGY_CAPACITY_DISPLAY_NAME = (base, p) -> "Energy Capacity display";
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> ENERGY_STORED_DISPLAY_NAME = (base, p) -> "Energy Stored display";
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> ENERGY_FRACTION_DISPLAY_NAME = (base, p) -> "Energy Fraction display";
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> SCAN_TIME_DISPLAY_NAME = (base, p) -> "Scan time display";

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
        if (value > 40) return STATUS_TOO_HIGH;
        if (value < 32) return STATUS_LOW;
        return STATUS_OK;
    };
    private static final IStatusFunction<GT_MetaTileEntity_TM_teslaCoil> TRANSFER_RADIUS_TRANSCEIVER_OR_COVER_ULTIMATE_STATUS = (base, p) -> {
        double value = p.get();
        if (Double.isNaN(value)) return STATUS_WRONG;
        value = (int) value;
        if (value < 0) return STATUS_TOO_LOW;
        if (value > 20) return STATUS_TOO_HIGH;
        if (value < 16) return STATUS_LOW;
        return STATUS_OK;
    };
    private static final IStatusFunction<GT_MetaTileEntity_TM_teslaCoil> OUTPUT_VOLTAGE_OR_CURRENT_STATUS = (base, p) -> {
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
        if (value == 0 || value == 20 || value == 40 || value == 60 || value == 80) return STATUS_HIGH;
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
    //endregion

    public GT_MetaTileEntity_TM_teslaCoil(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_TM_teslaCoil(String aName) {
        super(aName);
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
        transferRadiusTowerSetting = hatch_2.makeInParameter(0, 32, TRANSFER_RADIUS_TOWER_SETTING_NAME, TRANSFER_RADIUS_TOWER_STATUS);
        popogaSetting = hatch_2.makeInParameter(1, 0, POPOGA_NAME, POPOGA_STATUS);
        transferRadiusTransceiverSetting = hatch_3.makeInParameter(0, 16, TRANSFER_RADIUS_TRANSCEIVER_SETTING_NAME, TRANSFER_RADIUS_TRANSCEIVER_OR_COVER_ULTIMATE_STATUS);
        transferRadiusCoverUltimateSetting = hatch_3.makeInParameter(1, 16, TRANSFER_RADIUS_COVER_ULTIMATE_SETTING_NAME, TRANSFER_RADIUS_TRANSCEIVER_OR_COVER_ULTIMATE_STATUS);
        outputVoltageSetting = hatch_4.makeInParameter(0, -1, OUTPUT_VOLTAGE_SETTING_NAME, OUTPUT_VOLTAGE_OR_CURRENT_STATUS);
        popogaSetting = hatch_4.makeInParameter(1, 0, POPOGA_NAME, POPOGA_STATUS);
        outputCurrentSetting = hatch_5.makeInParameter(0, -1, OUTPUT_CURRENT_SETTING_NAME, OUTPUT_VOLTAGE_OR_CURRENT_STATUS);
        popogaSetting = hatch_5.makeInParameter(1, 0, POPOGA_NAME, POPOGA_STATUS);
        popogaSetting = hatch_6.makeInParameter(0, 0, POPOGA_NAME, POPOGA_STATUS);
        popogaSetting = hatch_6.makeInParameter(1, 0, POPOGA_NAME, POPOGA_STATUS);
        scanTimeMinSetting = hatch_7.makeInParameter(0, 100, SCAN_TIME_MIN_SETTING_NAME, SCAN_TIME_MIN_STATUS);
        popogaSetting = hatch_7.makeInParameter(1, 0, POPOGA_NAME, POPOGA_STATUS);
        overDriveSetting = hatch_8.makeInParameter(0, 0, OVERDRIVE_SETTING_NAME, OVERDRIVE_STATUS);
        popogaSetting = hatch_8.makeInParameter(1, 0, POPOGA_NAME, POPOGA_STATUS);
        popogaSetting = hatch_9.makeInParameter(0, 0, POPOGA_NAME, POPOGA_STATUS);
        popogaSetting = hatch_9.makeInParameter(1, 0, POPOGA_NAME, POPOGA_STATUS);

        popogaDisplay = hatch_0.makeOutParameter(0, 0, POPOGA_NAME, POPOGA_STATUS);
        popogaDisplay = hatch_0.makeOutParameter(1, 0, POPOGA_NAME, POPOGA_STATUS);
        popogaDisplay = hatch_1.makeOutParameter(0, 0, POPOGA_NAME, POPOGA_STATUS);
        popogaDisplay = hatch_1.makeOutParameter(1, 0, POPOGA_NAME, POPOGA_STATUS);
        transferRadiusTowerDisplay = hatch_2.makeOutParameter(0, 0, TRANSFER_RADIUS_TOWER_DISPLAY_NAME, TRANSFER_RADIUS_TOWER_STATUS);
        popogaDisplay = hatch_2.makeOutParameter(1, 0, POPOGA_NAME, POPOGA_STATUS);
        transferRadiusTransceiverDisplay = hatch_3.makeOutParameter(0, 0, TRANSFER_RADIUS_TRANSCEIVER_DISPLAY_NAME, TRANSFER_RADIUS_TRANSCEIVER_OR_COVER_ULTIMATE_STATUS);
        transferRadiusCoverUltimateDisplay = hatch_3.makeOutParameter(1, 0, TRANSFER_RADIUS_COVER_ULTIMATE_DISPLAY_NAME, TRANSFER_RADIUS_TRANSCEIVER_OR_COVER_ULTIMATE_STATUS);
        outputVoltageDisplay = hatch_4.makeOutParameter(0, 0, OUTPUT_VOLTAGE_DISPLAY_NAME, POWER_STATUS);
        popogaDisplay = hatch_4.makeOutParameter(1, 0, POPOGA_NAME, POPOGA_STATUS);
        outputCurrentDisplay = hatch_5.makeOutParameter(0, 0, OUTPUT_CURRENT_DISPLAY_NAME, POWER_STATUS);
        energyCapacityDisplay = hatch_5.makeOutParameter(1, 0, ENERGY_CAPACITY_DISPLAY_NAME, ENERGY_STATUS);
        energyStoredDisplay = hatch_6.makeOutParameter(0, 0, ENERGY_STORED_DISPLAY_NAME, ENERGY_STATUS);
        energyFractionDisplay = hatch_6.makeOutParameter(1, 0, ENERGY_FRACTION_DISPLAY_NAME, ENERGY_STATUS);
        scanTimeDisplay = hatch_7.makeOutParameter(0, 0, SCAN_TIME_DISPLAY_NAME, SCAN_TIME_STATUS);
        popogaDisplay = hatch_7.makeOutParameter(1, 0, POPOGA_NAME, POPOGA_STATUS);
        popogaDisplay = hatch_8.makeOutParameter(0, 0, POPOGA_NAME, POPOGA_STATUS);
        popogaDisplay = hatch_8.makeOutParameter(1, 0, POPOGA_NAME, POPOGA_STATUS);
        popogaDisplay = hatch_9.makeOutParameter(0, 0, POPOGA_NAME, POPOGA_STATUS);
        popogaDisplay = hatch_9.makeOutParameter(1, 0, POPOGA_NAME, POPOGA_STATUS);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_TM_teslaCoil(mName);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        ScreenOFF = new Textures.BlockIcons.CustomIcon("iconsets/TM_TESLA_TOWER");
        ScreenON = new Textures.BlockIcons.CustomIcon("iconsets/TM_TESLA_TOWER_ACTIVE");
        super.registerIcons(aBlockIconRegister);
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
    }

    private long getEnergyEfficiency(long voltage, int distance, boolean overDriveToggle) {
        if (overDriveToggle) {
            return (long) ((voltage * 2) - (voltage * Math.pow(overdriveEfficiency, distance)));
        } else {
            return (long) (voltage * Math.pow(energyEfficiency, distance));
        }
    }

    private float getRangeMulti(int mTier, int vTier) {
        //Over-tiered coils will add +25% range
        if (vTier > mTier) {
            return 1.25F;
        }
        return 1F;
    }

    @Override
    public long maxEUStore() {
        return energyCapacity * 2;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.casingTexturePages[texturePage][16 + 6], new TT_RenderedTexture(aActive ? ScreenON : ScreenOFF)};
        }
        return new ITexture[]{Textures.BlockIcons.casingTexturePages[texturePage][16 + 6]};
    }

    @Override
    public void onRemoval() {
        super.onRemoval();
        for (GT_MetaTileEntity_Hatch_Capacitor cap : eCapacitorHatches) {
            cap.getBaseMetaTileEntity().setActive(false);
        }
    }

    @Override
    public void stopMachine() {
        super.stopMachine();
        for (GT_MetaTileEntity_Hatch_Capacitor cap : eCapacitorHatches) {
            cap.getBaseMetaTileEntity().setActive(false);
        }

        setEUVar(0);
        energyStoredDisplay.set(0);
        energyFractionDisplay.set(0);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        for (GT_MetaTileEntity_Hatch_Capacitor cap : eCapacitorHatches) {
            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(cap)) {
                cap.getBaseMetaTileEntity().setActive(false);
            }
        }
        eCapacitorHatches.clear();

        int[] xyzOffsets;
        xyzOffsets = getTranslatedOffsets(0, -1, 1);
        mTier = iGregTechTileEntity.getMetaIDOffset(xyzOffsets[0], xyzOffsets[1], xyzOffsets[2]);

        if (structureCheck_EM(shape, blockType, blockMetas[mTier], addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 3, 16, 0) && eCapacitorHatches.size() > 0) {
            for (GT_MetaTileEntity_Hatch_Capacitor cap : eCapacitorHatches) {
                if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(cap)) {
                    cap.getBaseMetaTileEntity().setActive(iGregTechTileEntity.isActive());
                }
            }

            //Only recalculate offsets on orientation or rotation change
            if (oldRotation != getFrontRotation() || oldOrientation != iGregTechTileEntity.getFrontFacing()) {
                oldRotation = getFrontRotation();
                oldOrientation = iGregTechTileEntity.getFrontFacing();

                //Calculate coordinates of the middle bottom
                xyzOffsets = getTranslatedOffsets(0, 0, 2);
                posZap[0] = iGregTechTileEntity.getXCoord() + xyzOffsets[0];
                posZap[1] = iGregTechTileEntity.getYCoord() + xyzOffsets[1];
                posZap[2] = iGregTechTileEntity.getZCoord() + xyzOffsets[2];

                //Calculate coordinates of the top sphere
                xyzOffsets = getTranslatedOffsets(0, -14, 2);
                posTop[0] = iGregTechTileEntity.getXCoord() + xyzOffsets[0];
                posTop[1] = iGregTechTileEntity.getYCoord() + xyzOffsets[1];
                posTop[2] = iGregTechTileEntity.getZCoord() + xyzOffsets[2];

                //Calculate offsets for scanning
                scanPosOffsets[0] = getTranslatedOffsets(40, 0, 43);
                scanPosOffsets[1] = getTranslatedOffsets(-40, -4, -37);

                scanPosOffsets[2] = getTranslatedOffsets(40, -5, 43);
                scanPosOffsets[3] = getTranslatedOffsets(-40, -8, -37);

                scanPosOffsets[4] = getTranslatedOffsets(40, -9, 43);
                scanPosOffsets[5] = getTranslatedOffsets(-40, -12, -37);

                scanPosOffsets[6] = getTranslatedOffsets(40, -13, 43);
                scanPosOffsets[7] = getTranslatedOffsets(-40, -16, -37);

                scanPosOffsets[8] = getTranslatedOffsets(40, -17, 43);
                scanPosOffsets[9] = getTranslatedOffsets(-40, -20, -37);
            }
            return true;
        }
        return false;
    }

    @Override
    public void construct(int stackSize, boolean hintsOnly) {
        StructureBuilderExtreme(shape, blockType, blockMetas[(stackSize - 1) % 6], 3, 16, 0, getBaseMetaTileEntity(), this, hintsOnly);
    }

    @Override
    public String[] getStructureDescription(int stackSize) {
        return description;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.BASS_MARK,
                "Tower of Wireless Power",
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "Fewer pesky cables!",
                EnumChatFormatting.BLUE + "Survival chances might be affected",
        };
    }

    @Override
    public boolean checkRecipe_EM(ItemStack itemStack) {
        if (!histHighSetting.getStatus(false).isOk ||
                !histLowSetting.getStatus(false).isOk ||
                !transferRadiusTowerSetting.getStatus(false).isOk ||
                !transferRadiusTransceiverSetting.getStatus(false).isOk ||
                !transferRadiusCoverUltimateSetting.getStatus(false).isOk ||
                !outputVoltageSetting.getStatus(false).isOk ||
                !outputCurrentSetting.getStatus(false).isOk ||
                !scanTimeMinSetting.getStatus(false).isOk ||
                !overDriveSetting.getStatus(false).isOk
        ) return false;

        mEfficiencyIncrease = 10000;
        mMaxProgresstime = 20;
        vTier = -1;
        long[] capacitorData;
        for (GT_MetaTileEntity_Hatch_Capacitor cap : eCapacitorHatches) {
            if (!GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(cap)) {
                continue;
            }
            if (cap.getCapacitors()[0] > vTier) {
                vTier = (int) cap.getCapacitors()[0];
            }
        }

        //Calculate Efficiency values
        energyEfficiency = map(mTier + 1, 1, maxTier, minEfficiency, maxEfficiency);
        overdriveEfficiency = energyEfficiency - overdriveEfficiencyExtra;

        energyCapacity = 0;
        outputCurrentMax = 0;

        if (vTier < 0) {
            //Returning true to allow for 'passive running'
            outputVoltageMax = 0;
            return true;
        } else if (vTier > mTier && getEUVar() > 0) {
            explodeMultiblock();
        }

        outputVoltageMax = V[vTier + 1];
        for (GT_MetaTileEntity_Hatch_Capacitor cap : eCapacitorHatches) {
            if (!GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(cap)) {
                continue;
            }
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
        return true;
    }

    private void scanForTransmissionTargets(int[] coordsMin, int[] coordsMax) {
        //This makes sure the minimums are actually smaller than the maximums
        int xMin = coordsMin[0] < coordsMax[0] ? coordsMin[0] : coordsMax[0];
        int yMin = coordsMin[1] < coordsMax[1] ? coordsMin[1] : coordsMax[1];
        int zMin = coordsMin[2] < coordsMax[2] ? coordsMin[2] : coordsMax[2];
        //And vice versa
        int xMax = coordsMin[0] > coordsMax[0] ? coordsMin[0] : coordsMax[0];
        int yMax = coordsMin[1] > coordsMax[1] ? coordsMin[1] : coordsMax[1];
        int zMax = coordsMin[2] > coordsMax[2] ? coordsMin[2] : coordsMax[2];

        for (int xPos = xMin; xPos <= xMax; xPos++) {
            for (int yPos = yMin; yPos <= yMax; yPos++) {
                for (int zPos = zMin; zPos <= zMax; zPos++) {
                    if (xPos == 0 && yPos == 0 && zPos == 0) {
                        continue;
                    }
                    IGregTechTileEntity node = getBaseMetaTileEntity().getIGregTechTileEntityOffset(xPos, yPos, zPos);
                    if (node == null) {
                        continue;
                    }
                    IMetaTileEntity nodeInside = node.getMetaTileEntity();
                    if (nodeInside instanceof GT_MetaTileEntity_TeslaCoil || nodeInside instanceof GT_MetaTileEntity_TM_teslaCoil && node.isActive() || (node.getCoverBehaviorAtSide((byte) 1) instanceof GT_Cover_TM_TeslaCoil)) {
                        eTeslaMap.put(node, (int) Math.ceil(Math.sqrt(Math.pow(xPos, 2) + Math.pow(yPos, 2) + Math.pow(zPos, 2))));
                    }
                }
            }
        }

    }

    private void thaumLightning(IGregTechTileEntity mte, IGregTechTileEntity node) {
        byte xR = (byte) (node.getXCoord() - posTop[0]);
        byte yR = (byte) (node.getYCoord() - posTop[1]);
        byte zR = (byte) (node.getZCoord() - posTop[2]);

        int wID = mte.getWorld().provider.dimensionId;

        sparkList.add(new Util.thaumSpark(posTop[0], posTop[1], posTop[2], xR, yR, zR, wID));
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        IGregTechTileEntity mte = getBaseMetaTileEntity();

        //Hysteresis based ePowerPass setting
        long energyMax = maxEUStore() / 2;
        long energyStored = getEUVar();

        float energyFrac = (float) energyStored / energyMax;

        energyCapacityDisplay.set(energyMax);
        energyStoredDisplay.set(energyStored);
        energyFractionDisplay.set(energyFrac);

        if (!ePowerPass && energyFrac > histHighSetting.get()) {
            ePowerPass = true;
        } else if (ePowerPass && energyFrac < histLowSetting.get()) {
            ePowerPass = false;
        }

        //Clean the eTeslaMap
        for (Map.Entry<IGregTechTileEntity, Integer> Rx : eTeslaMap.entrySet()) {
            IGregTechTileEntity node = Rx.getKey();
            if (node != null) {
                IMetaTileEntity nodeInside = node.getMetaTileEntity();
                try {
                    if (nodeInside instanceof GT_MetaTileEntity_TM_teslaCoil && node.isActive()) {
                        GT_MetaTileEntity_TM_teslaCoil teslaTower = (GT_MetaTileEntity_TM_teslaCoil) nodeInside;
                        if (teslaTower.maxEUStore() > 0) {
                            continue;
                        }
                    } else if (nodeInside instanceof GT_MetaTileEntity_TeslaCoil) {
                        GT_MetaTileEntity_TeslaCoil teslaTransceiver = (GT_MetaTileEntity_TeslaCoil) nodeInside;
                        if (teslaTransceiver.mBatteryCount > 0) {
                            continue;
                        }
                    } else if ((node.getCoverBehaviorAtSide((byte) 1) instanceof GT_Cover_TM_TeslaCoil) && node.getEUCapacity() > 0) {
                        continue;
                    }
                } catch (Exception e) {
                }
            }
            eTeslaMap.remove(Rx.getKey());
        }

        //Scan for transmission targets
        switch (scanTime) {
            case 0:
                scanTimeDisplay.updateStatus();
                scanForTransmissionTargets(scanPosOffsets[0], scanPosOffsets[1]);
                break;
            case 20:
                scanTimeDisplay.updateStatus();
                scanForTransmissionTargets(scanPosOffsets[2], scanPosOffsets[3]);
                break;
            case 40:
                scanTimeDisplay.updateStatus();
                scanForTransmissionTargets(scanPosOffsets[4], scanPosOffsets[5]);
                break;
            case 60:
                scanTimeDisplay.updateStatus();
                scanForTransmissionTargets(scanPosOffsets[6], scanPosOffsets[7]);
                break;
            case 80:
                scanTimeDisplay.updateStatus();
                scanForTransmissionTargets(scanPosOffsets[8], scanPosOffsets[9]);
                break;
            default:
                if (scanTime == (int) scanTimeMinSetting.get() - 1) {
                    scanTime = -1;

                    for (Map.Entry<IGregTechTileEntity, Integer> Rx : eTeslaMap.entrySet()) {
                        IGregTechTileEntity node = Rx.getKey();
                        if (node != null) {
                            IMetaTileEntity nodeInside = node.getMetaTileEntity();
                            try {
                                if (nodeInside instanceof GT_MetaTileEntity_TeslaCoil) {
                                    GT_MetaTileEntity_TeslaCoil teslaCoil = (GT_MetaTileEntity_TeslaCoil) nodeInside;

                                    int tX = node.getXCoord();
                                    int tY = node.getYCoord();
                                    int tZ = node.getZCoord();

                                    int tXN = posZap[0];
                                    int tYN = posZap[1];
                                    int tZN = posZap[2];

                                    int tOffset = (int) Math.ceil(Math.sqrt(Math.pow(tX - tXN, 2) + Math.pow(tY - tYN, 2) + Math.pow(tZ - tZN, 2)));
                                    teslaCoil.eTeslaMap.put(mte, tOffset);

                                    for (Map.Entry<IGregTechTileEntity, Integer> RRx : eTeslaMap.entrySet()) {
                                        IGregTechTileEntity nodeN = RRx.getKey();
                                        if (nodeN == node) {
                                            continue;
                                        }
                                        tXN = nodeN.getXCoord();
                                        tYN = nodeN.getYCoord();
                                        tZN = nodeN.getZCoord();
                                        tOffset = (int) Math.ceil(Math.sqrt(Math.pow(tX - tXN, 2) + Math.pow(tY - tYN, 2) + Math.pow(tZ - tZN, 2)));
                                        if (tOffset > 20) {
                                            continue;
                                        }
                                        teslaCoil.eTeslaMap.put(nodeN, tOffset);
                                    }
                                }
                            } catch (Exception e) {
                                eTeslaMap.remove(Rx.getKey());
                            }
                        }
                    }
                }
                break;
        }

        scanTime++;
        scanTimeDisplay.set(scanTime);

        //Power Limit Settings
        long outputVoltage;
        if (outputVoltageSetting.get() > 0) {
            outputVoltage = Math.min(outputVoltageMax, (long) outputVoltageSetting.get());
        } else {
            outputVoltage = outputVoltageMax;
        }
        outputVoltageDisplay.set(outputVoltage);

        long outputCurrent;
        if (outputCurrentSetting.get() > 0) {
            outputCurrent = Math.min(outputCurrentMax, (long) outputCurrentSetting.get());
        } else {
            outputCurrent = outputCurrentMax;
        }
        outputCurrentDisplay.set(0);

        //Stuff to do if ePowerPass
        if (ePowerPass) {
            //Range calculation and display
            float rangeFrac = (float) ((-0.5 * Math.pow(energyFrac, 2)) + (1.5 * energyFrac));
            int transferRadiusTower = (int) (transferRadiusTowerSetting.get() * getRangeMulti(mTier, vTier) * rangeFrac);
            transferRadiusTowerDisplay.set(transferRadiusTower);
            int transferRadiusTransceiver = (int) (transferRadiusTransceiverSetting.get() * getRangeMulti(mTier, vTier) * rangeFrac);
            transferRadiusTransceiverDisplay.set(transferRadiusTransceiver);
            int transferRadiusCoverUltimate = (int) (transferRadiusCoverUltimateSetting.get() * getRangeMulti(mTier, vTier) * rangeFrac);
            transferRadiusCoverUltimateDisplay.set(transferRadiusCoverUltimate);

            //Clean the eTeslaMap
            for (Map.Entry<IGregTechTileEntity, Integer> Rx : eTeslaMap.entrySet()) {
                IGregTechTileEntity node = Rx.getKey();
                if (node != null) {
                    IMetaTileEntity nodeInside = node.getMetaTileEntity();
                    try {
                        if (nodeInside instanceof GT_MetaTileEntity_TM_teslaCoil && node.isActive()) {
                            GT_MetaTileEntity_TM_teslaCoil teslaTower = (GT_MetaTileEntity_TM_teslaCoil) nodeInside;
                            if (teslaTower.maxEUStore() > 0) {
                                continue;
                            }
                        } else if (nodeInside instanceof GT_MetaTileEntity_TeslaCoil) {
                            GT_MetaTileEntity_TeslaCoil teslaCoil = (GT_MetaTileEntity_TeslaCoil) nodeInside;
                            if (teslaCoil.getStoredEnergy()[1] > 0) {
                                continue;
                            }
                        } else if ((node.getCoverBehaviorAtSide((byte) 1) instanceof GT_Cover_TM_TeslaCoil) && node.getEUCapacity() > 0) {
                            continue;
                        }
                    } catch (Exception e) {
                    }
                }
                eTeslaMap.remove(Rx.getKey());
            }

            //Power transfer
            long sparks = outputCurrent;
            while (sparks > 0) {
                boolean overdriveToggle = overDriveSetting.get() > 0;
                boolean idle = true;
                for (Map.Entry<IGregTechTileEntity, Integer> Rx : entriesSortedByValues(eTeslaMap)) {
                    if (energyStored >= (overdriveToggle ? outputVoltage * 2 : outputVoltage)) {
                        IGregTechTileEntity node = Rx.getKey();
                        IMetaTileEntity nodeInside = node.getMetaTileEntity();

                        long outputVoltageInjectable;
                        long outputVoltageConsumption;
                        if (overdriveToggle) {
                            outputVoltageInjectable = outputVoltage;
                            outputVoltageConsumption = getEnergyEfficiency(outputVoltage, Rx.getValue(), true);
                        } else {
                            outputVoltageInjectable = getEnergyEfficiency(outputVoltage, Rx.getValue(), false);
                            outputVoltageConsumption = outputVoltage;
                        }

                        if (nodeInside instanceof GT_MetaTileEntity_TM_teslaCoil && Rx.getValue() <= transferRadiusTower) {
                            GT_MetaTileEntity_TM_teslaCoil nodeTesla = (GT_MetaTileEntity_TM_teslaCoil) nodeInside;
                            if (!nodeTesla.ePowerPass) {
                                if (nodeTesla.getEUVar() + outputVoltageInjectable <= (nodeTesla.maxEUStore() / 2)) {
                                    setEUVar(getEUVar() - outputVoltageConsumption);
                                    node.increaseStoredEnergyUnits(outputVoltageConsumption, true);
                                    thaumLightning(mte, node);
                                    sparks--;
                                    idle = false;
                                }
                            }
                        } else if (nodeInside instanceof GT_MetaTileEntity_TeslaCoil && Rx.getValue() <= transferRadiusTransceiver) {
                            GT_MetaTileEntity_TeslaCoil nodeTesla = (GT_MetaTileEntity_TeslaCoil) nodeInside;
                            if (!nodeTesla.powerPassToggle) {
                                if (node.injectEnergyUnits((byte) 6, outputVoltageInjectable, 1L) > 0L) {
                                    setEUVar(getEUVar() - outputVoltageConsumption);
                                    thaumLightning(mte, node);
                                    sparks--;
                                    idle = false;
                                }
                            }
                        } else if ((node.getCoverBehaviorAtSide((byte) 1) instanceof GT_Cover_TM_TeslaCoil_Ultimate) && Rx.getValue() <= transferRadiusCoverUltimate) {
                            if (node.injectEnergyUnits((byte) 1, outputVoltageInjectable, 1L) > 0L) {
                                setEUVar(getEUVar() - outputVoltageConsumption);
                                thaumLightning(mte, node);
                                sparks--;
                                idle = false;
                            }
                        }
                        if (sparks == 0) {
                            break;
                        }
                    } else {
                        idle = true;
                        break;
                    }
                }
                if (idle) {
                    break;
                }
            }
            outputCurrentDisplay.set(outputCurrent - sparks);
            if (scanTime % 60 == 0 && !sparkList.isEmpty()) {
                NetworkDispatcher.INSTANCE.sendToAllAround(new RendererMessage.RendererData(sparkList),
                        mte.getWorld().provider.dimensionId,
                        mte.getXCoord(),
                        mte.getYCoord(),
                        mte.getZCoord(),
                        256);
            }
            sparkList.clear();
        } else {
            outputCurrentDisplay.set(0);
        }
        return true;
    }

    private boolean addFrameToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        return aTileEntity != null && aTileEntity.getMetaTileEntity() instanceof GT_MetaPipeEntity_Frame;
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
        return false;
    }
}
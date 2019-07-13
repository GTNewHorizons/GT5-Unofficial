package com.github.technus.tectech.thing.metaTileEntity.multi;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.thing.cover.GT_Cover_TM_TeslaCoil_Ultimate;
import com.github.technus.tectech.thing.metaTileEntity.IConstructable;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_Capacitor;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_DynamoMulti;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyMulti;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_Param;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.*;
import com.github.technus.tectech.thing.metaTileEntity.single.GT_MetaTileEntity_TeslaCoil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.objects.GT_RenderedTexture;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.github.technus.tectech.CommonValues.V;
import static com.github.technus.tectech.Util.StructureBuilder;
import static com.github.technus.tectech.Util.entriesSortedByValues;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.texturePage;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsBA0;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.*;
import static gregtech.api.enums.GT_Values.E;

public class GT_MetaTileEntity_TM_teslaCoil extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {
    private static Textures.BlockIcons.CustomIcon ScreenOFF;
    private static Textures.BlockIcons.CustomIcon ScreenON;

    private int tier = 0;
    private int mTier = 1;//Tier offset by +1
    private int orientation = 0;
    private int maxTier = 6;
    private int minTier = 1;

    private int scanTime = 0; //Sets scan time to Z E R O :epic:
    //private int scanTimeMin = 100; //Min scan time in ticks
    //private int scanTimeTill = scanTimeMin; //Set default scan time

    private Map<IGregTechTileEntity, Integer> eTeslaMap = new HashMap<>(); //Tesla Map to map them tesla bois!
    private final ArrayList<GT_MetaTileEntity_Hatch_Capacitor> eCaps = new ArrayList<>(); //Capacitor List

    //private float histLow = 0.25F; //Power pass is disabled if power is under this fraction
    //private float histHigh = 0.75F; //Power pass is enabled if power is over this fraction

    //private float histLowLimit = 0.05F; //How low can you configure it?
    //private float histHighLimit = 0.95F; //How high can you configure it?

    private int scanRadius = 32; //Tesla scan radius

    private int transferRadiusTower; //Radius for tower to tower transfers
    private int transferRadiusTransceiver; //Radius for tower to transceiver transfers
    private int transferRadiusCoverUltimate; //Radius for tower to ultimate cover transfers

    private long outputVoltage = 0; //Tesla Voltage Output
    private long outputCurrent = 0; //Tesla Current Output
    private long energyCapacity = 0; //Total energy the tower can store

    //public boolean powerPassToggle = false; //Power Pass for public viewing

    public boolean tPowerPass(){
        return ePowerPass;
    }

    public int vTier = -1;

    private long lossPerBlock = 1; //EU lost per block traveled
    private float energyEfficiencyMax = 0.95F; //Max efficiency
    private float energyEfficiencyMin = 0.75F; //Min efficiency
    private boolean overDriveToggle(){return (overDriveSetting.get() > 0);}
    private float overdriveEfficiency = 0.95F; //Overdrive efficiency
    private long outputVoltageInjectable = 0; //How much EU will be received post distance losses
    private long outputVoltageConsumption = 0; //How much EU will be drained

    private long getEnergyEfficiency(long voltage, int mTier){
        if (overDriveToggle()){
            return (long)(voltage * energyEfficiencyMin + (energyEfficiencyMax - energyEfficiencyMin) / (maxTier - minTier + 1) * (mTier - 1)); //Efficiency Formula
        } else {
            return (long)(voltage * (2-energyEfficiencyMin + (energyEfficiencyMax - energyEfficiencyMin) / (maxTier - minTier + 1) * (mTier - 1))*(2- overdriveEfficiency)); //Sum overdrive efficiency formula
        }
    }

    public int pTier = 0;
    public int sTier = 0;

    //region structure
    private static final String[][] shape0 = new String[][]{//3 16 0
            {"\u000F", "A  .  ",},
            {E, "B000", "B000", "B000", "\u0001", "B000", E, "B000", E, "B000", E, "B000", "\u0001", "B111", " 22222 ",},
            {"B000", "A00000", "A00000", "A00000", "B000", E, "A0A!A0", E, "A0A!A0", E, "A0A!A0", E, "A0A!A0", "\u0001", "A1C1", " 21112 ",},
            {"B000", "A00000", "A00000", "A00000", "B030", "C3", "A0!3!0", "C3", "A0!3!0", "C3", "A0!3!0", "C3", "A0!3!0", "C3", "C3", "A1A3A1", " 21212 ",},
            {"B000", "A00000", "A00000", "A00000", "B000", E, "A0A!A0", E, "A0A!A0", E, "A0A!A0", E, "A0A!A0", "\u0001", "A1C1", " 21112 ",},
            {E, "B000", "B000", "B000", "\u0001", "B000", E, "B000", E, "B000", E, "B000", "\u0001", "B111", " 22222 ",},
            {"\u000F", "A     ",},
    };
    private static final String[][] shape1 = new String[][]{//3 0 0
            {"A  .  ",},
            {" 22222 ","A11111","\u0001","B000",E,"B000",E,"B000",E,"B000","\u0001","B000","B000","B000",},
            {" 21112 ","A1C1","\u0001","A0A!A0",E,"A0A!A0",E,"A0A!A0",E,"A0A!A0",E,"B000","A00000","A00000","A00000","B000",},
            {" 21212 ","A1A3A1","C3","C3","A0!3!0","C3","A0!3!0","C3","A0!3!0","C3","A0!3!0","C3","B030","A00000","A00000","A00000","B000",},
            {" 21112 ","A1C1","\u0001","A0A!A0",E,"A0A!A0",E,"A0A!A0",E,"A0A!A0",E,"B000","A00000","A00000","A00000","B000",},
            {" 22222 ","A11111","\u0001","B000",E,"B000",E,"B000",E,"B000","\u0001","B000","B000","B000",},
            {"A     ",},
    };
    private static final String[][] shape2 = new String[][]{//16 3 0
            {E,"P ","P ","P.","P ","P ",},
            {"P ","O12","A000B0A0A0A0B12","A000B0A0A0A0B12","A000B0A0A0A0B12","O12","P ",},
            {"P ","A000B0A0A0A0B12","00000K1","00000A!A!A!A!C1","00000K1","A000B0A0A0A0B12","P ",},
            {"P ","A000B0A0A0A0B12","00000A!A!A!A!C1","00003333333333332","00000A!A!A!A!C1","A000B0A0A0A0B12","P ",},
            {"P ","A000B0A0A0A0B12","00000K1","00000A!A!A!A!C1","00000K1","A000B0A0A0A0B12","P ",},
            {"P ","O12","A000B0A0A0A0B12","A000B0A0A0A0B12","A000B0A0A0A0B12","F0H12","P ",},
            {E,"P ","P ","P ","P ","P ",},
    };
    private static final String[][] shape3 = new String[][]{//0 3 0
            {E," "," ","."," "," ",},
            {" ","21","21B0A0A0A0B000","21B0A0A0A0B000","21B0A0A0A0B000","21H0"," ",},
            {" ","21B0A0A0A0B000","1K00000","1C!A!A!A!A00000","1K00000","21B0A0A0A0B000"," ",},
            {" ","21B0A0A0A0B000","1C!A!A!A!A00000","23333333333330000","1C!A!A!A!A00000","21B0A0A0A0B000"," ",},
            {" ","21B0A0A0A0B000","1K00000","1C!A!A!A!A00000","1K00000","21B0A0A0A0B000"," ",},
            {" ","21","21B0A0A0A0B000","21B0A0A0A0B000","21B0A0A0A0B000","21"," ",},
            {E," "," "," "," "," ",},
    };

    private static final String[][][] shapes = new String[][][]{shape0,shape1,shape2,shape3};
    private static final Block[] blockType = new Block[]{sBlockCasingsBA0,sBlockCasingsBA0,sBlockCasingsBA0,sBlockCasingsBA0};
    private static final byte[] blockMetaT0 = new byte[]{7, 0, 6, 8};
    private static final byte[] blockMetaT1 = new byte[]{7, 1, 6, 8};
    private static final byte[] blockMetaT2 = new byte[]{7, 2, 6, 8};
    private static final byte[] blockMetaT3 = new byte[]{7, 3, 6, 8};
    private static final byte[] blockMetaT4 = new byte[]{7, 4, 6, 8};
    private static final byte[] blockMetaT5 = new byte[]{7, 5, 6, 8};
    private static final byte[][] blockMetas = new byte[][]{blockMetaT0,blockMetaT1,blockMetaT2,blockMetaT3,blockMetaT4,blockMetaT5};
    private final HatchAdder[] addingMethods = new HatchAdder[]{this::addCapacitorToMachineList, this::addFrameToMachineList};
    private static final short[] casingTextures = new short[]{(texturePage << 7)+16+6, 0};
    private static final Block[] blockTypeFallback = new Block[]{sBlockCasingsBA0, null};
    private static final byte[] blockMetaFallback = new byte[]{6, 0};
    private static final String[] description = new String[]{
            EnumChatFormatting.AQUA + "Hint Details:",
            "1 - Classic Hatches or Steel Pipe Casing",
            "2 - Titanium Frames",
    };
    //endregion

    //region parameters
    protected Parameters.Group.ParameterIn histLowSetting,histHighSetting,transferRadiusTowerSetting,transferRadiusTransceiverSetting,transferRadiusCoverUltimateSetting,outputVoltageSetting,outputCurrentSetting,scanTimeMinSetting,overDriveSetting;
    //protected Parameters.Group.ParameterOut timerValue,remainingTime;
    private static final NameFunction<GT_MetaTileEntity_TM_teslaCoil> HYSTERESIS_LOW_SETTING_NAME = (base, p)-> "Hysteresis low setting";
    private static final NameFunction<GT_MetaTileEntity_TM_teslaCoil> HYSTERESIS_HIGH_SETTING_NAME = (base, p)-> "Hysteresis high setting";
    private static final NameFunction<GT_MetaTileEntity_TM_teslaCoil> TRANSFER_RADIUS_TOWER_SETTING_NAME = (base, p)-> "Tesla Towers transfer radius";
    private static final NameFunction<GT_MetaTileEntity_TM_teslaCoil> TRANSFER_RADIUS_TRANSCEIVER_SETTING_NAME = (base, p)-> "Tesla Transceiver transfer radius";
    private static final NameFunction<GT_MetaTileEntity_TM_teslaCoil> TRANSFER_RADIUS_COVER_ULTIMATE_SETTING_NAME = (base, p)-> "Tesla Ultimate Cover transfer radius";
    private static final NameFunction<GT_MetaTileEntity_TM_teslaCoil> OUTPUT_VOLTAGE_SETTING_NAME = (base, p)-> "Output voltage setting";
    private static final NameFunction<GT_MetaTileEntity_TM_teslaCoil> OUTPUT_CURRENT_SETTING_NAME = (base, p)-> "Output current setting";
    private static final NameFunction<GT_MetaTileEntity_TM_teslaCoil> SCAN_TIME_MIN_SETTING_NAME = (base, p)-> "Scan time Min setting";
    private static final NameFunction<GT_MetaTileEntity_TM_teslaCoil> OVERDRIVE_SETTING_NAME = (base, p)-> "Overdrive setting";

    private static final StatusFunction<GT_MetaTileEntity_TM_teslaCoil> HYSTERESIS_LOW_STATUS=(base, p)->{
        double value=p.get();
        if(Double.isNaN(value)){return STATUS_WRONG;}
        if(value<=0.05) return STATUS_TOO_LOW;
        if(value>base.histHighSetting.get()) return STATUS_TOO_HIGH;
        return STATUS_OK;
    };
    private static final StatusFunction<GT_MetaTileEntity_TM_teslaCoil> HYSTERESIS_HIGH_STATUS=(base, p)->{
        double value=p.get();
        if(Double.isNaN(value)) return STATUS_WRONG;
        if(value<=base.histLowSetting.get()) return STATUS_TOO_LOW;
        if(value>0.95) return STATUS_TOO_HIGH;
        return STATUS_OK;
    };
    private static final StatusFunction<GT_MetaTileEntity_TM_teslaCoil> TRANSFER_RADIUS_TOWER_STATUS=(base, p)->{
        double value=p.get();
        if(Double.isNaN(value)) return STATUS_WRONG;
        value=(int)value;
        if(value<0) return STATUS_TOO_LOW;
        if(value>32) return STATUS_TOO_HIGH;
        if(value<32) return STATUS_LOW;
        return STATUS_OK;
    };
    private static final StatusFunction<GT_MetaTileEntity_TM_teslaCoil> TRANSFER_RADIUS_TRANSCEIVER_STATUS=(base, p)->{
        double value=p.get();
        if(Double.isNaN(value)) return STATUS_WRONG;
        value=(int)value;
        if(value<0) return STATUS_TOO_LOW;
        if(value>16) return STATUS_TOO_HIGH;
        if(value<16) return STATUS_LOW;
        return STATUS_OK;
    };
    private static final StatusFunction<GT_MetaTileEntity_TM_teslaCoil> TRANSFER_RADIUS_COVER_ULTIMATE_STATUS=(base, p)->{
        double value=p.get();
        if(Double.isNaN(value)) return STATUS_WRONG;
        value=(int)value;
        if(value<0) return STATUS_TOO_LOW;
        if(value>16) return STATUS_TOO_HIGH;
        if(value<16) return STATUS_LOW;
        return STATUS_OK;
    };
    private static final StatusFunction<GT_MetaTileEntity_TM_teslaCoil> OUTPUT_VOLTAGE_STATUS=(base, p)->{
        double value=p.get();
        if(Double.isNaN(value)) return STATUS_WRONG;
        value=(long)value;
        if(value==-1) return STATUS_OK;
        if(value<=0) return STATUS_TOO_LOW;
        return STATUS_OK;
    };
    private static final StatusFunction<GT_MetaTileEntity_TM_teslaCoil> OUTPUT_CURRENT_STATUS=(base, p)->{
        double value=p.get();
        if(Double.isNaN(value)) return STATUS_WRONG;
        value=(long)value;
        if(value==-1) return STATUS_OK;
        if(value<=0) return STATUS_TOO_LOW;
        return STATUS_LOW;
    };
    private static final StatusFunction<GT_MetaTileEntity_TM_teslaCoil> SCAN_TIME_MIN_STATUS=(base, p)->{
        double value=p.get();
        if(Double.isNaN(value)) return STATUS_WRONG;
        value=(int)value;
        if(value<100) return STATUS_TOO_LOW;
        if(value==100) return STATUS_OK;
        return STATUS_HIGH;
    };
    private static final StatusFunction<GT_MetaTileEntity_TM_teslaCoil> OVERDRIVE_STATUS=(base, p)->{
        double value=p.get();
        if(Double.isNaN(value)) return STATUS_WRONG;
        value=(int)value;
        if(value<0) return STATUS_TOO_LOW;
        if(value==0) return STATUS_LOW;
        return STATUS_HIGH;
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
        Parameters.Group hatch_0=parametrization.getGroup(0, true);
        Parameters.Group hatch_1=parametrization.getGroup(1, true);
        Parameters.Group hatch_2=parametrization.getGroup(2, true);
        Parameters.Group hatch_3=parametrization.getGroup(3, true);
        Parameters.Group hatch_4=parametrization.getGroup(4, true);
        Parameters.Group hatch_5=parametrization.getGroup(5, true);
        Parameters.Group hatch_6=parametrization.getGroup(6, true);
        Parameters.Group hatch_7=parametrization.getGroup(7, true);

        histLowSetting=hatch_0.makeInParameter(0,0.25, HYSTERESIS_LOW_SETTING_NAME,HYSTERESIS_LOW_STATUS);//TODO Fix Grouping
        histHighSetting=hatch_1.makeInParameter(0,0.75, HYSTERESIS_HIGH_SETTING_NAME,HYSTERESIS_HIGH_STATUS);
        transferRadiusTowerSetting=hatch_2.makeInParameter(0,32, TRANSFER_RADIUS_TOWER_SETTING_NAME,TRANSFER_RADIUS_TOWER_STATUS);
        transferRadiusTransceiverSetting=hatch_3.makeInParameter(0,16, TRANSFER_RADIUS_TRANSCEIVER_SETTING_NAME,TRANSFER_RADIUS_TRANSCEIVER_STATUS);
        transferRadiusCoverUltimateSetting=hatch_3.makeInParameter(1,16, TRANSFER_RADIUS_COVER_ULTIMATE_SETTING_NAME,TRANSFER_RADIUS_COVER_ULTIMATE_STATUS);
        outputVoltageSetting=hatch_4.makeInParameter(0,-1, OUTPUT_VOLTAGE_SETTING_NAME,OUTPUT_VOLTAGE_STATUS);
        outputCurrentSetting=hatch_5.makeInParameter(0,-1, OUTPUT_CURRENT_SETTING_NAME,OUTPUT_CURRENT_STATUS);
        scanTimeMinSetting=hatch_6.makeInParameter(0,100, SCAN_TIME_MIN_SETTING_NAME,SCAN_TIME_MIN_STATUS);
        overDriveSetting=hatch_7.makeInParameter(0,0, OVERDRIVE_SETTING_NAME,OVERDRIVE_STATUS);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_TM_teslaCoil(mName);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        ScreenOFF = new Textures.BlockIcons.CustomIcon("iconsets/EM_WH");
        ScreenON = new Textures.BlockIcons.CustomIcon("iconsets/EM_WH_ACTIVE");
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public long maxEUStore() {
        return energyCapacity * 2;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.casingTexturePages[texturePage][16+6], new GT_RenderedTexture(aActive ? ScreenON : ScreenOFF)};
        }
        return new ITexture[]{Textures.BlockIcons.casingTexturePages[texturePage][16+6]};
    }

    @Override
    public void onRemoval() {
        super.onRemoval();
        for (GT_MetaTileEntity_Hatch_Capacitor cap : eCaps) {
            cap.getBaseMetaTileEntity().setActive(false);
        }
    }

    @Override
    public void stopMachine() {
        super.stopMachine();
        for (GT_MetaTileEntity_Hatch_Capacitor cap : eCaps) {
            cap.getBaseMetaTileEntity().setActive(false);
        }
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        for (GT_MetaTileEntity_Hatch_Capacitor cap : eCaps) {
            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(cap)) {
                cap.getBaseMetaTileEntity().setActive(false);
            }
        }
        eCaps.clear();

        int coilX0 = 0;
        int coilX1 = 0;
        int coilX2 = 0;

        int coilY0 = 1;
        int coilY1 = 0;
        int coilY2 = 0;

        int coilZ0 = 0;
        int coilZ1 = 0;
        int coilZ2 = 0;

        switch (iGregTechTileEntity.getFrontFacing()) {
            case 2:
                coilZ0 = 1;
                coilZ1 = 1;
                coilZ2 = 1;
                coilX1 = 1;
                coilX2 = -1;
                break;
            case 3:
                coilZ0 = -1;
                coilZ1 = -1;
                coilZ2 = -1;
                coilX1 = -1;
                coilX2 = 1;
                break;
            case 4:
                coilX0 = 1;
                coilX1 = 1;
                coilX2 = 1;
                coilZ1 = -1;
                coilZ2 = 1;
                break;
            case 5:
                coilX0 = -1;
                coilX1 = -1;
                coilX2 = -1;
                coilZ1 = 1;
                coilZ2 = -1;
                break;
            default:
                return false;
        }

        Block coil0 = iGregTechTileEntity.getBlockOffset(coilX0, coilY0, coilZ0);
        Block coil1 = iGregTechTileEntity.getBlockOffset(coilX0, -coilY0, coilZ0);
        Block coil2 = iGregTechTileEntity.getBlockOffset(coilX1, coilY1, coilZ1);
        Block coil3 = iGregTechTileEntity.getBlockOffset(coilX2, coilY2, coilZ2);

        int xOffset;
        int yOffset;
        int zOffset;

        if (coil0 == sBlockCasingsBA0) {
            xOffset = 3;
            yOffset = 16;
            zOffset = 0;
            orientation = 0;
            tier = iGregTechTileEntity.getMetaIDOffset(coilX0, coilY0, coilZ0);
        } else if (coil1 == sBlockCasingsBA0) {
            xOffset = 3;
            yOffset = 0;
            zOffset = 0;
            orientation = 1;
            tier = iGregTechTileEntity.getMetaIDOffset(coilX0, -coilY0, coilZ0);
        } else if (coil2 == sBlockCasingsBA0) {
            xOffset = 16;
            yOffset = 3;
            zOffset = 0;
            orientation = 2;
            tier = iGregTechTileEntity.getMetaIDOffset(coilX1, coilY1, coilZ1);
        } else if (coil3 == sBlockCasingsBA0) {
            xOffset = 0;
            yOffset = 3;
            zOffset = 0;
            orientation = 3;
            tier = iGregTechTileEntity.getMetaIDOffset(coilX2, coilY2, coilZ2);
        } else {
            return false;
        }

        mTier = tier + 1;

        if (structureCheck_EM(shapes[orientation], blockType, blockMetas[tier], addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, xOffset, yOffset, zOffset) && eCaps.size() > 0) {
            for (GT_MetaTileEntity_Hatch_Capacitor cap : eCaps) {
                if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(cap)) {
                    cap.getBaseMetaTileEntity().setActive(iGregTechTileEntity.isActive());
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void construct(int stackSize, boolean hintsOnly) {
        StructureBuilder(shapes[0], blockType, blockMetas[(stackSize-1)%6], 3, 16, 0, getBaseMetaTileEntity(), hintsOnly);
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
        if (!histHighSetting.getStatus(false).isOk||
                !histLowSetting.getStatus(false).isOk||
                !transferRadiusTowerSetting.getStatus(false).isOk||
                !transferRadiusTransceiverSetting.getStatus(false).isOk||
                !transferRadiusCoverUltimateSetting.getStatus(false).isOk||
                !outputVoltageSetting.getStatus(false).isOk||
                !outputCurrentSetting.getStatus(false).isOk||
                !scanTimeMinSetting.getStatus(false).isOk||
                !overDriveSetting.getStatus(false).isOk
        ) return false;

        mEfficiencyIncrease = 10000;
        mMaxProgresstime = 20;
        vTier = -1;

        energyCapacity = 0;
        outputCurrent = 0;
        long[] capacitorData;
        for (GT_MetaTileEntity_Hatch_Capacitor cap : eCaps) {
            if (!GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(cap)) {
                continue;
            }
            if (cap.getCapacitors()[0] > vTier) {
                vTier = (int) cap.getCapacitors()[0];
            }
        }
        if(vTier < 0){return false;}
        outputVoltage = V[vTier];
        for (GT_MetaTileEntity_Hatch_Capacitor cap : eCaps) {
            if (!GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(cap)) {
                continue;
            }
            cap.getBaseMetaTileEntity().setActive(true);
            capacitorData = cap.getCapacitors();
            if (capacitorData[0] < vTier) {
                if(getEUVar() > 0 && capacitorData[0] != 0){
                    cap.getBaseMetaTileEntity().setToFire();
                }
                eCaps.remove(cap);
            } else {
                outputCurrent += capacitorData[1];
                energyCapacity += capacitorData[2];
            }
        }
        return true;
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (getBaseMetaTileEntity().isClientSide()) {
            return true;
        }
        ////Hysteresis based ePowerPass Config
        long energyMax = maxEUStore() / 2;
        long energyStored = getEUVar();

        float energyFrac = (float)energyStored/energyMax;

        for (GT_MetaTileEntity_Hatch_Capacitor cap : eCaps) {
            if (!GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(cap)) {
                continue;
            }
            cap.energyStoredFrac = energyFrac;
        }

        //ePowerPass hist toggle
        if (!ePowerPass && energyFrac > histHighSetting.get()) {
            ePowerPass = true;
        } else if (ePowerPass && energyFrac < histLowSetting.get()) {
            ePowerPass = false;
        }
        ////Scanning for active teslas

        scanTime++;
        if (scanTime >= 100) {
            scanTime = 0;
            eTeslaMap.clear();

            for (int xPosOffset = -scanRadius; xPosOffset <= scanRadius; xPosOffset++) {
                for (int yPosOffset = -scanRadius; yPosOffset <= scanRadius; yPosOffset++) {
                    for (int zPosOffset = -scanRadius; zPosOffset <= scanRadius; zPosOffset++) {
                        if (xPosOffset == 0 && yPosOffset == 0 && zPosOffset == 0) {
                            continue;
                        }
                        IGregTechTileEntity node = getBaseMetaTileEntity().getIGregTechTileEntityOffset(xPosOffset, yPosOffset, zPosOffset);
                        if (node == null) {
                            continue;
                        }
                        IMetaTileEntity nodeInside = node.getMetaTileEntity();
                        if (nodeInside instanceof GT_MetaTileEntity_TeslaCoil || nodeInside instanceof GT_MetaTileEntity_TM_teslaCoil && node.isActive() || (node.getCoverBehaviorAtSide((byte) 1) instanceof GT_Cover_TM_TeslaCoil_Ultimate)) {
                            eTeslaMap.put(node, (int) Math.ceil(Math.sqrt(xPosOffset * xPosOffset + yPosOffset * yPosOffset + zPosOffset * zPosOffset)));
                        }
                    }
                }
            }
        }

        //Stuff to do if ePowerPass
        if (ePowerPass) {

            transferRadiusTower = (int) transferRadiusTowerSetting.get(); //TODO generate based on power stored
            transferRadiusTransceiver = (int) transferRadiusTransceiverSetting.get(); //TODO generate based on power stored
            transferRadiusCoverUltimate = (int) transferRadiusCoverUltimateSetting.get(); //TODO generate based on power stored

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
                        } else if ((node.getCoverBehaviorAtSide((byte) 1) instanceof GT_Cover_TM_TeslaCoil_Ultimate) && node.getEUCapacity() > 0) {
                            continue;
                        }
                    } catch (Exception e) {
                    }
                }
                eTeslaMap.remove(Rx.getKey());
            }

            //Power transfer
            long sparks = outputCurrent;
            System.out.println("Output Current at: " + outputCurrent + "A" );
            long sparkz = 0;
            while (sparks > 0) {
                boolean idle = true;
                for (Map.Entry<IGregTechTileEntity, Integer> Rx : entriesSortedByValues(eTeslaMap)) {
                    if(energyStored >= (overDriveToggle() ? outputVoltage*2 : outputVoltage)) {
                        IGregTechTileEntity node = Rx.getKey();
                        IMetaTileEntity nodeInside = node.getMetaTileEntity();

                        if (overDriveToggle()){
                            outputVoltageInjectable = outputVoltage;
                            outputVoltageConsumption = getEnergyEfficiency(outputVoltage, mTier) + (lossPerBlock * Rx.getValue());
                        } else {
                            outputVoltageInjectable = getEnergyEfficiency(outputVoltage, mTier) - (lossPerBlock * Rx.getValue());
                            outputVoltageConsumption = outputVoltage;
                        }

                        if (nodeInside instanceof GT_MetaTileEntity_TM_teslaCoil && Rx.getValue() <= transferRadiusTower) {
                            GT_MetaTileEntity_TM_teslaCoil nodeTesla = (GT_MetaTileEntity_TM_teslaCoil) nodeInside;
                            if (!nodeTesla.tPowerPass()) {
                                if (nodeTesla.getEUVar() + outputVoltageInjectable <= (nodeTesla.maxEUStore() / 2)) {
                                    setEUVar(getEUVar() - outputVoltageConsumption);
                                    node.increaseStoredEnergyUnits(outputVoltageConsumption, true);
                                    sparks--;
                                    sparkz++;
                                    idle = false;
                                }
                            }
                        } else if (nodeInside instanceof GT_MetaTileEntity_TeslaCoil && Rx.getValue() <= transferRadiusTransceiver) {
                            GT_MetaTileEntity_TeslaCoil nodeTesla = (GT_MetaTileEntity_TeslaCoil) nodeInside;
                            if (!nodeTesla.powerPassToggle) {
                                if (node.injectEnergyUnits((byte) 6, outputVoltageInjectable, 1L) > 0L) {
                                    setEUVar(getEUVar() - outputVoltageConsumption);
                                    sparks--;
                                    sparkz++;
                                    idle = false;
                                }
                            }
                        } else if ((node.getCoverBehaviorAtSide((byte) 1) instanceof GT_Cover_TM_TeslaCoil_Ultimate) && Rx.getValue() <= transferRadiusCoverUltimate) {
                            if (node.injectEnergyUnits((byte) 1, outputVoltageInjectable, 1L) > 0L) {
                                setEUVar(getEUVar() - outputVoltageConsumption);
                                sparks--;
                                sparkz++;
                                idle = false;
                            }
                        }
                        if (sparks == 0) {
                            System.out.println("Fresh out of sparks!");
                            break;
                        }
                    } else {
                        idle = true;
                        System.out.println("ENERGY LOW!");
                        break;
                    }
                }
                if (idle) {
                    System.out.println("IDLE DROP!");
                    break;
                }
            }
            System.out.println("I sent a total of :" + sparkz + " This tick!");
        }
        return true;
    }

    public final boolean addFrameToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        return aTileEntity != null && aTileEntity.getMetaTileEntity() instanceof GT_MetaPipeEntity_Frame;
    }

    public final boolean addCapacitorToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Capacitor) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return eCaps.add((GT_MetaTileEntity_Hatch_Capacitor) aMetaTileEntity);
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

package com.github.technus.tectech.thing.metaTileEntity.multi;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.thing.cover.GT_Cover_TM_TeslaCoil_Ultimate;
import com.github.technus.tectech.thing.metaTileEntity.IConstructable;
import com.github.technus.tectech.thing.metaTileEntity.hatch.*;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
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

import java.util.*;

import static com.github.technus.tectech.CommonValues.V;
import static com.github.technus.tectech.Util.StructureBuilder;
import static com.github.technus.tectech.Util.entriesSortedByValues;
import static com.github.technus.tectech.loader.TecTechConfig.DEBUG_MODE;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.texturePage;
import static gregtech.api.GregTech_API.*;
import static gregtech.api.enums.GT_Values.E;

public class GT_MetaTileEntity_TM_teslaCoil extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {//TODO Add capacitors
    private static Textures.BlockIcons.CustomIcon ScreenOFF;
    private static Textures.BlockIcons.CustomIcon ScreenON;

    private int tier = 0;
    private int orientation = 0;

    private int scanTime = 0; //Sets scan time to Z E R O :epic:
    private int scanTimeMin = 100; //Min scan time in ticks
    private int scanTimeTill = scanTimeMin; //Set default scan time

    private Map<IGregTechTileEntity, Integer> eTeslaMap = new HashMap<IGregTechTileEntity, Integer>(); //Tesla Map to map them tesla bois!
    private final ArrayList<GT_MetaTileEntity_Hatch_Capacitor> eCaps = new ArrayList<>(); //Capacitor List

    private float histLow = 0.25F; //Power pass is disabled if power is under this fraction
    private float histHigh = 0.75F; //Power pass is enabled if power is over this fraction

    private float histLowLimit = 0.05F; //How low can you configure it?
    private float histHighLimit = 0.95F; //How high can you configure it?

    private int scanRadius = 32; //Tesla scan radius

    private int transferRadiusTower = 32; //Radius for tower to tower transfers
    private int transferRadiusTransceiver = 16; //Radius for tower to transceiver transfers
    private int transferRadiusCoverUltimate = 16; //Radius for tower to ultimate cover transfers

    private long outputVoltage = 0; //Tesla Voltage Output
    private long outputCurrent = 0; //Tesla Current Output
    private long energyCapacity = 0; //Total energy the tower can store

    public boolean powerPassToggle = false; //Power Pass for public viewing

    private boolean parametrized = false; //Assumes no parametrizer on initialisation
    //Default parametrized variables
    private long histLowParam = 0;
    private long histHighParam = 0;
    private long histScaleParam = 0;
    private int transferRadiusTowerParam = 0;
    private int transferRadiusTransceiverParam = 0;
    private int transferRadiusCoverUltimateParam = 0;
    private long outputVoltageParam = 0;
    private long outputCurrentParam = 0;
    private int scanTimeMinParam = 0;

    public int vTier = -1;
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
    private static final Block[] blockType = new Block[]{sBlockCasings1, sBlockCasings5, sBlockCasings2, sBlockCasings5};//TODO Give it it's own casing type, add a primary coil type, add a secondary coil type and add toroid casing type
    private static final byte[] blockMetaT0 = new byte[]{15, 0, 13, 0};
    private static final byte[] blockMetaT1 = new byte[]{15, 1, 13, 0};
    private static final byte[] blockMetaT2 = new byte[]{15, 2, 13, 0};
    private static final byte[][] blockMetas = new byte[][]{blockMetaT0,blockMetaT1,blockMetaT2};
    private static final String[] addingMethods = new String[]{"addCapacitorToMachineList", "addFrameToMachineList"};
    private static final short[] casingTextures = new short[]{29, 0};
    private static final Block[] blockTypeFallback = new Block[]{sBlockCasings2, null};
    private static final byte[] blockMetaFallback = new byte[]{13, 0};
    private static final String[] description = new String[]{
            EnumChatFormatting.AQUA + "Hint Details:",
            "1 - Classic Hatches or Steel Pipe Casing",
            "2 - Titanium Frames",
    };
    //endregion

    public GT_MetaTileEntity_TM_teslaCoil(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_TM_teslaCoil(String aName) {
        super(aName);
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
            return new ITexture[]{Textures.BlockIcons.casingTexturePages[texturePage][4], new GT_RenderedTexture(aActive ? ScreenON : ScreenOFF)};
        }
        return new ITexture[]{Textures.BlockIcons.casingTexturePages[texturePage][4]};
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

        if (coil0 == sBlockCasings5) {
            xOffset = 3;
            yOffset = 16;
            zOffset = 0;
            orientation = 0;
            tier = iGregTechTileEntity.getMetaIDOffset(coilX0, coilY0, coilZ0);
        } else if (coil1 == sBlockCasings5) {
            xOffset = 3;
            yOffset = 0;
            zOffset = 0;
            orientation = 1;
            tier = iGregTechTileEntity.getMetaIDOffset(coilX0, -coilY0, coilZ0);
        } else if (coil2 == sBlockCasings5) {
            xOffset = 16;
            yOffset = 3;
            zOffset = 0;
            orientation = 2;
            tier = iGregTechTileEntity.getMetaIDOffset(coilX1, coilY1, coilZ1);
        } else if (coil3 == sBlockCasings5) {
            xOffset = 0;
            yOffset = 3;
            zOffset = 0;
            orientation = 3;
            tier = iGregTechTileEntity.getMetaIDOffset(coilX2, coilY2, coilZ2);
        } else {
            return false;
        }

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
        StructureBuilder(shapes[0], blockType, blockMetas[(stackSize-1)%3], 3, 16, 0, getBaseMetaTileEntity(), hintsOnly);
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
        mEfficiencyIncrease = 10000;
        mMaxProgresstime = 20;
        vTier = -1;
        long[] capacitorData;
        for (GT_MetaTileEntity_Hatch_Capacitor cap : eCaps) {
            if (!GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(cap)) {
                continue;
            }
            if (cap.getCapacitors()[0] > vTier) {
                vTier = (int) cap.getCapacitors()[0];
            }
        }
        if(vTier < 0){ return false; }
        outputVoltage = V[vTier];
        for (GT_MetaTileEntity_Hatch_Capacitor cap : eCaps) {
            if (!GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(cap)) {
                continue;
            }
            capacitorData = cap.getCapacitors();
            if (capacitorData[0] < vTier) {
                if(getEUVar() > 0){
                    cap.getBaseMetaTileEntity().setToFire();
                }
                eCaps.remove(cap);
            } else {
                cap.getBaseMetaTileEntity().setActive(true);
                outputCurrent =+ capacitorData[1];
                energyCapacity =+ capacitorData[2];
            }
        }
        return true;
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (getBaseMetaTileEntity().isClientSide()) {
            return true;
        }
        //Parametrizer hatch loader TODO Add parametrizer detection
        if (false) {
            parametrized = true;
            histLowParam = 0;
            histHighParam = 0;
            histScaleParam = 0;
            transferRadiusTowerParam = 0;
            transferRadiusTransceiverParam = 0;
            transferRadiusCoverUltimateParam = 0;
            outputVoltageParam = 0;
            outputCurrentParam = 0;
            scanTimeMin = 0;
        } else {
            parametrized = false;
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

        //Hysteresis Parameters sanity check
        if (parametrized && histScaleParam > 0 && histLowParam > 0 && histScaleParam <= histHighParam && histLowParam < histHighParam) {
            float histLowt = (float)histLowParam/histScaleParam;
            if (histLowt >= histLowLimit){
                histLow = histLowt;
            } else {
                histLow = histLowLimit;
            }

            float histHight = (float)histHighParam/histScaleParam;
            if (histHight <= histHighLimit){
                histHigh = histHight;
            } else {
                histHigh = histHighLimit;
            }
        }

        //ePowerPass hist toggle
        if (!ePowerPass && energyFrac > histHigh) {
            ePowerPass = true;
        } else if (ePowerPass && energyFrac < histLow) {
            ePowerPass = false;
        }
        powerPassToggle = ePowerPass;

        ////Scanning for active teslas

        if (parametrized && scanTimeMinParam > scanTimeMin) {
            scanTimeTill = scanTimeMinParam;
        }

        scanTime++;
        if (scanTime >= scanTimeTill) {
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
        if (powerPassToggle) {
            if (parametrized && outputVoltageParam > 0 && outputVoltage > outputVoltageParam){
            outputVoltage = outputVoltageParam;
            }

            if (parametrized && outputCurrentParam > 0 && outputCurrent > outputCurrentParam){
            outputCurrent = outputCurrentParam;
            }

            transferRadiusTower = 32; //TODO generate based on power stored
            transferRadiusTransceiver = 16; //TODO generate based on power stored
            transferRadiusCoverUltimate = 16; //TODO generate based on power stored

            if(parametrized && transferRadiusTowerParam > 0 && transferRadiusTowerParam < transferRadiusTower){
                transferRadiusTower = transferRadiusTowerParam;
            }

            if(parametrized && transferRadiusTransceiverParam > 0 && transferRadiusTransceiverParam < transferRadiusTransceiver){
                transferRadiusTransceiver = transferRadiusTransceiverParam;
            }

            if(parametrized && transferRadiusCoverUltimateParam > 0 && transferRadiusCoverUltimateParam < transferRadiusCoverUltimate){
                transferRadiusCoverUltimate = transferRadiusCoverUltimateParam;
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
            for (Map.Entry<IGregTechTileEntity, Integer> Rx : entriesSortedByValues(eTeslaMap)) {
                IGregTechTileEntity node = Rx.getKey();
                IMetaTileEntity nodeInside = node.getMetaTileEntity();
                long euTran = outputVoltage;

                if (nodeInside instanceof GT_MetaTileEntity_TM_teslaCoil && Rx.getValue() <= transferRadiusTower) {
                    GT_MetaTileEntity_TM_teslaCoil nodeTesla = (GT_MetaTileEntity_TM_teslaCoil) nodeInside;
                    if (!nodeTesla.powerPassToggle) {
                        if (nodeTesla.getEUVar() + euTran <= (nodeTesla.maxEUStore() / 2)) {
                            setEUVar(getEUVar() - euTran);
                            node.increaseStoredEnergyUnits(euTran, true);
                        }
                    }
                } else if (nodeInside instanceof GT_MetaTileEntity_TeslaCoil && Rx.getValue() <= transferRadiusTransceiver) {
                    GT_MetaTileEntity_TeslaCoil nodeTesla = (GT_MetaTileEntity_TeslaCoil) nodeInside;
                    if (!nodeTesla.powerPassToggle) {
                        if (node.injectEnergyUnits((byte)6, euTran, 1L) > 0L) {
                            setEUVar(getEUVar() - euTran);
                        }
                    }
                } else if ((node.getCoverBehaviorAtSide((byte)1) instanceof GT_Cover_TM_TeslaCoil_Ultimate) && Rx.getValue() <= transferRadiusCoverUltimate){
                    if (node.injectEnergyUnits((byte)1, euTran, 1L) > 0L) {
                        setEUVar(getEUVar() - euTran);
                    }
                }
            }
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

    public static void run() {
        try {
            adderMethodMap.put("addFrameToMachineList", GT_MetaTileEntity_TM_teslaCoil.class.getMethod("addFrameToMachineList", IGregTechTileEntity.class, int.class));
            adderMethodMap.put("addCapacitorToMachineList", GT_MetaTileEntity_TM_teslaCoil.class.getMethod("addCapacitorToMachineList", IGregTechTileEntity.class, int.class));
        } catch (NoSuchMethodException e) {
            if (DEBUG_MODE) {
                e.printStackTrace();
            }
        }
    }
}

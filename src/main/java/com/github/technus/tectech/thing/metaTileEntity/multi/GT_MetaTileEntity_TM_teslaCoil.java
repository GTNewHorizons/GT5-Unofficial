package com.github.technus.tectech.thing.metaTileEntity.multi;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.loader.NetworkDispatcher;
import com.github.technus.tectech.mechanics.constructable.IConstructable;
import com.github.technus.tectech.mechanics.spark.RendererMessage;
import com.github.technus.tectech.mechanics.spark.ThaumSpark;
import com.github.technus.tectech.mechanics.structure.Structure;
import com.github.technus.tectech.mechanics.structure.adders.IHatchAdder;
import com.github.technus.tectech.thing.cover.GT_Cover_TM_TeslaCoil;
import com.github.technus.tectech.thing.cover.GT_Cover_TM_TeslaCoil_Ultimate;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_Capacitor;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_DynamoMulti;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyMulti;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_Param;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.INameFunction;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.IStatusFunction;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.Parameters;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.render.TT_RenderedExtendedFacingTexture;
import com.github.technus.tectech.thing.metaTileEntity.single.GT_MetaTileEntity_TeslaCoil;
import com.github.technus.tectech.util.CommonValues;
import com.github.technus.tectech.util.Vec3Impl;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Materials;
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
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static com.github.technus.tectech.mechanics.structure.Structure.adders;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.texturePage;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsBA0;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.*;
import static com.github.technus.tectech.util.CommonValues.V;
import static com.github.technus.tectech.util.Util.entriesSortedByValues;
import static gregtech.api.enums.GT_Values.E;
import static java.lang.Math.*;
import static net.minecraft.util.StatCollector.translateToLocal;

public class GT_MetaTileEntity_TM_teslaCoil extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {
    //region variables
    private static final int heliumUse = TecTech.configTecTech.TESLA_MULTI_HELIUM_PLASMA_PER_SECOND;//Default is 100
    private static final int nitrogenUse = TecTech.configTecTech.TESLA_MULTI_NITROGEN_PLASMA_PER_SECOND;//Default is 50
    private static final int radonUse = TecTech.configTecTech.TESLA_MULTI_RADON_PLASMA_PER_SECOND;//Default is 50
    //Default is {1, 1, 1}
    private static final int[] plasmaTierLoss = new int[]{TecTech.configTecTech.TESLA_MULTI_LOSS_PER_BLOCK_T0,
            TecTech.configTecTech.TESLA_MULTI_LOSS_PER_BLOCK_T1, TecTech.configTecTech.TESLA_MULTI_LOSS_PER_BLOCK_T2};
    private static final float overDriveLoss = TecTech.configTecTech.TESLA_MULTI_OVERDRIVE_LOSS_FACTOR;//Default is 0.25F;
    private static final boolean doFluidOutput = TecTech.configTecTech.TESLA_MULTI_GAS_OUTPUT; //Default is false

    //Face icons
    private static Textures.BlockIcons.CustomIcon ScreenOFF;
    private static Textures.BlockIcons.CustomIcon ScreenON;

    private int mTier = 0; //Determines max voltage (LV to ZPM)
    private int plasmaTier = 0; //0 is None, 1 is Helium or Nitrogen, 2 is Radon (Does not match actual plasma tiers)

    private FluidStack[] mOutputFluidsQueue; //Used to buffer the fluid outputs, so the tesla takes a second to 'cool' any plasma it would output as a gas

    public static final HashSet<IGregTechTileEntity> teslaNodeSet = new HashSet<>(); //Targets for power transmission
    private final HashSet<ThaumSpark> sparkList = new HashSet<>(); //Thaumcraft lighting coordinate pairs, so we can send them in bursts and save on lag
    private Map<IGregTechTileEntity, Integer> teslaNodeMap = new HashMap<>(); //Targets for power transmission
    private final ArrayList<GT_MetaTileEntity_Hatch_Capacitor> eCapacitorHatches = new ArrayList<>(); //Capacitor hatches which determine the max voltage tier and count of amps

    private int sortTime = 0; //Scan timer used for tesla search intervals

    private long energyCapacity = 0; //Total energy storage limited by capacitors
    private long outputVoltageMax = 0; //Tesla voltage output limited by capacitors
    private int vTier = -1; //Tesla voltage tier limited by capacitors
    private long outputCurrentMax = 0; //Tesla current output limited by capacitors

    //Prevents unnecessary offset calculation, saving on lag
    private byte oldRotation = -1;
    private byte oldOrientation = -1;
    //Location of the center of the sphere on top of the tower, used as the Thaumcraft lightning and origin
    public Vec3Impl posTop = Vec3Impl.NULL_VECTOR;
    //endregion

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
    private static final byte[] blockMetaT6 = new byte[]{7, 9, 6, 8};
    private static final byte[][] blockMetas = new byte[][]{blockMetaT0, blockMetaT1, blockMetaT2, blockMetaT3, blockMetaT4, blockMetaT5, blockMetaT6};
    private static final IHatchAdder<GT_MetaTileEntity_TM_teslaCoil>[] addingMethods = adders(
            GT_MetaTileEntity_TM_teslaCoil::addCapacitorToMachineList,
            GT_MetaTileEntity_TM_teslaCoil::addFrameToMachineList);
    private static final short[] casingTextures = new short[]{(texturePage << 7) + 16 + 6, 0};
    private static final Block[] blockTypeFallback = new Block[]{sBlockCasingsBA0, null};
    private static final byte[] blockMetaFallback = new byte[]{6, 0};
    private static final String[] description = new String[]{
            EnumChatFormatting.AQUA + translateToLocal("tt.keyphrase.Hint_Details") + ":",
            translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.hint.0"),//1 - Classic Hatches, Capacitor Hatches or Tesla Base Casing
            translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.hint.1"),//2 - Titanium Frames
    };
    //endregion

    //region parameters
    protected Parameters.Group.ParameterIn popogaSetting, histLowSetting, histHighSetting, transferRadiusTowerSetting, transferRadiusTransceiverSetting, transferRadiusCoverUltimateSetting, outputVoltageSetting, outputCurrentSetting, sortTimeMinSetting, overDriveSetting;
    protected Parameters.Group.ParameterOut popogaDisplay, transferRadiusTowerDisplay, transferRadiusTransceiverDisplay, transferRadiusCoverUltimateDisplay, outputVoltageDisplay, outputCurrentDisplay, energyCapacityDisplay, energyStoredDisplay, energyFractionDisplay, sortTimeDisplay;

    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> HYSTERESIS_LOW_SETTING_NAME = (base, p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgi.0");//Hysteresis low setting
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> HYSTERESIS_HIGH_SETTING_NAME = (base, p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgi.1");//Hysteresis high setting
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> TRANSFER_RADIUS_TOWER_SETTING_NAME = (base, p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgi.2");//Tesla Towers transfer radius setting
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> TRANSFER_RADIUS_TRANSCEIVER_SETTING_NAME = (base, p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgi.3");//Tesla Transceiver transfer radius setting
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> TRANSFER_RADIUS_COVER_ULTIMATE_SETTING_NAME = (base, p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgi.4");//Tesla Ultimate Cover transfer radius setting
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> OUTPUT_VOLTAGE_SETTING_NAME = (base, p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgi.5");//Output voltage setting
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> OUTPUT_CURRENT_SETTING_NAME = (base, p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgi.6");//Output current setting
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> SCAN_TIME_MIN_SETTING_NAME = (base, p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgi.7");//Scan time Min setting
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> OVERDRIVE_SETTING_NAME = (base, p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgi.8");//Overdrive setting
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> POPOGA_NAME = (base, p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgi.9");//Unused

    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> TRANSFER_RADIUS_TOWER_DISPLAY_NAME = (base, p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgo.0");//Tesla Towers transfer radius display
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> TRANSFER_RADIUS_TRANSCEIVER_DISPLAY_NAME = (base, p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgo.1");//Tesla Transceiver transfer radius display
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> TRANSFER_RADIUS_COVER_ULTIMATE_DISPLAY_NAME = (base, p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgo.2");//Tesla Ultimate Cover transfer radius display
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> OUTPUT_VOLTAGE_DISPLAY_NAME = (base, p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgo.3");//Output voltage display
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> OUTPUT_CURRENT_DISPLAY_NAME = (base, p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgo.4");//Output current display
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> ENERGY_CAPACITY_DISPLAY_NAME = (base, p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgo.5");//Energy Capacity display
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> ENERGY_STORED_DISPLAY_NAME = (base, p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgo.6");//Energy Stored display
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> ENERGY_FRACTION_DISPLAY_NAME = (base, p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgo.7");//Energy Fraction display
    private static final INameFunction<GT_MetaTileEntity_TM_teslaCoil> SCAN_TIME_DISPLAY_NAME = (base, p) -> translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgo.8");//Scan time display

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

    private int getPerBlockLoss(){
        return plasmaTierLoss[plasmaTier];
    }

    private long[] getOutputVoltage(long outputVoltage, int distance, boolean overDriveToggle) {
        long outputVoltageInjectable;
        long outputVoltageConsumption;

        if (overDriveToggle) {
            outputVoltageInjectable = outputVoltage;
            outputVoltageConsumption = outputVoltage + (distance * getPerBlockLoss()) + (long) Math.round(overDriveLoss * outputVoltage);
        } else {
            outputVoltageInjectable = outputVoltage - (distance * getPerBlockLoss());
            outputVoltageConsumption = outputVoltage;
        }
        return new long[]{outputVoltageInjectable, outputVoltageConsumption};
    }

    private float getRangeMulti(int mTier, int vTier) {
        //Helium and Nitrogen Plasmas will double the range
        //Radon will quadruple the range
        int plasmaBoost;
        switch (plasmaTier) {
            case 2:
                plasmaBoost = 4;
                break;
            case 1:
                plasmaBoost = 2;
                break;
            default:
                plasmaBoost = 1;
        }

        //Over-tiered coils will add +25% range
        if (vTier > mTier) {
            return 1.25F * plasmaBoost;
        }
        return 1F * plasmaBoost;
    }

    private void thaumLightning(IGregTechTileEntity mte, IGregTechTileEntity node) {
        byte xR = (byte) (node.getXCoord() - posTop.get0());
        byte yR = (byte) (node.getYCoord() - posTop.get1());
        byte zR = (byte) (node.getZCoord() - posTop.get2());

        int wID = mte.getWorld().provider.dimensionId;

        sparkList.add(new ThaumSpark(posTop.get0(), posTop.get1(), posTop.get2(), xR, yR, zR, wID));
    }

    private void checkPlasmaBoost() {
        //If there's fluid in the queue, try to output it
        //That way it takes at least a second to 'process' the plasma
        if (mOutputFluidsQueue != null) {
            mOutputFluids = mOutputFluidsQueue;
            mOutputFluidsQueue = null;
        }

        for (GT_MetaTileEntity_Hatch_Input fluidHatch : mInputHatches) {
            if (fluidHatch.mFluid != null) {
                if (fluidHatch.mFluid.isFluidEqual(Materials.Helium.getPlasma(1)) && fluidHatch.mFluid.amount >= heliumUse) {
                    fluidHatch.mFluid.amount = fluidHatch.mFluid.amount - heliumUse;
                    if (doFluidOutput) {
                        mOutputFluidsQueue = new FluidStack[]{Materials.Helium.getGas(heliumUse)};
                    }
                    plasmaTier = 1;
                    return;
                } else if (fluidHatch.mFluid.isFluidEqual(Materials.Nitrogen.getPlasma(1)) && fluidHatch.mFluid.amount >= nitrogenUse) {
                    fluidHatch.mFluid.amount = fluidHatch.mFluid.amount - nitrogenUse;
                    if (doFluidOutput) {
                        mOutputFluidsQueue = new FluidStack[]{Materials.Nitrogen.getGas(nitrogenUse)};
                    }
                    plasmaTier = 1;
                    return;
                } else if (fluidHatch.mFluid.isFluidEqual(Materials.Radon.getPlasma(1)) && fluidHatch.mFluid.amount >= radonUse) {
                    fluidHatch.mFluid.amount = fluidHatch.mFluid.amount - radonUse;
                    if (doFluidOutput) {
                        mOutputFluidsQueue = new FluidStack[]{Materials.Radon.getGas(radonUse)};
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
        for (GT_MetaTileEntity_Hatch_Capacitor cap : eCapacitorHatches) {
            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(cap)) {
                cap.getBaseMetaTileEntity().setActive(false);
            }
        }
        eCapacitorHatches.clear();

        Vec3Impl xyzOffsets;

        xyzOffsets = getExtendedFacing().getWorldOffset(new Vec3Impl(0, -1, 1));
        mTier = iGregTechTileEntity.getMetaIDOffset(xyzOffsets.get0(), xyzOffsets.get1(), xyzOffsets.get2());
        if (mTier == 9){mTier = 6;}//Hacky remap because the ZPM coils were added later

        if (structureCheck_EM(shape, blockType, blockMetas[mTier], addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 3, 16, 0) && eCapacitorHatches.size() > 0) {
            for (GT_MetaTileEntity_Hatch_Capacitor cap : eCapacitorHatches) {
                if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(cap)) {
                    cap.getBaseMetaTileEntity().setActive(iGregTechTileEntity.isActive());
                }
            }

            //Only recalculate offsets on orientation or rotation change
            if (oldRotation != getExtendedFacing().ordinal() || oldOrientation != iGregTechTileEntity.getFrontFacing()) {
                oldRotation = (byte) getExtendedFacing().ordinal();
                oldOrientation = iGregTechTileEntity.getFrontFacing();

                //Calculate coordinates of the middle bottom
                posTop = getExtendedFacing().getWorldOffset(new Vec3Impl(0, 0, 2)).add(getBaseMetaTileEntity());

                //Calculate coordinates of the top sphere
                posTop = getExtendedFacing().getWorldOffset(new Vec3Impl(0, -14, 2)).add(getBaseMetaTileEntity());
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean checkRecipe_EM(ItemStack itemStack) {
        checkPlasmaBoost();

        if (!histHighSetting.getStatus(false).isOk ||
                !histLowSetting.getStatus(false).isOk ||
                !transferRadiusTowerSetting.getStatus(false).isOk ||
                !transferRadiusTransceiverSetting.getStatus(false).isOk ||
                !transferRadiusCoverUltimateSetting.getStatus(false).isOk ||
                !outputVoltageSetting.getStatus(false).isOk ||
                !outputCurrentSetting.getStatus(false).isOk ||
                !sortTimeMinSetting.getStatus(false).isOk ||
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

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.BASS_MARK,
                translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.desc.0"),//Tower of Wireless Power
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.desc.1"),//Fewer pesky cables!
                EnumChatFormatting.BLUE + translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.desc.2"),//Survival chances might be affected
        };
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        ScreenOFF = new Textures.BlockIcons.CustomIcon("iconsets/TM_TESLA_TOWER");
        ScreenON = new Textures.BlockIcons.CustomIcon("iconsets/TM_TESLA_TOWER_ACTIVE");
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.casingTexturePages[texturePage][16 + 6], new TT_RenderedExtendedFacingTexture(aActive ? ScreenON : ScreenOFF)};
        }
        return new ITexture[]{Textures.BlockIcons.casingTexturePages[texturePage][16 + 6]};
    }

    @Override
    public void onRemoval() {
        super.onRemoval();
        IGregTechTileEntity aBaseMetaTileEntity = this.getBaseMetaTileEntity();
        if (aBaseMetaTileEntity.isClientSide()) {
            return;
        }
        teslaNodeSet.remove(aBaseMetaTileEntity);
        for (GT_MetaTileEntity_Hatch_Capacitor cap : eCapacitorHatches) {
            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(cap)) {
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
        teslaNodeSet.add(this.getBaseMetaTileEntity());
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
            teslaNodeSet.add(aBaseMetaTileEntity);
        }
    }


    public static Map<IGregTechTileEntity, Integer> generateTeslaNodeMap(IGregTechTileEntity orgin){
        Map<IGregTechTileEntity, Integer> generatedNodeMap = new HashMap<>();
        IMetaTileEntity orginInside = orgin.getMetaTileEntity();
        int orginX;
        int orginY;
        int orginZ;
        if (orginInside instanceof GT_MetaTileEntity_TM_teslaCoil) {
            GT_MetaTileEntity_TM_teslaCoil teslaTower = (GT_MetaTileEntity_TM_teslaCoil) orginInside;
            orginX = teslaTower.posTop.get0();
            orginY = teslaTower.posTop.get1();
            orginZ = teslaTower.posTop.get2();
        } else {
            orginX = orgin.getXCoord();
            orginY = orgin.getYCoord();
            orginZ = orgin.getZCoord();
        }

        for (IGregTechTileEntity node : teslaNodeSet) {
            if (node == null) {
                //Technically, the Tesla Covers do not remove themselves from the list and this is the code that does
                teslaNodeSet.remove(null);
                continue;
            } else if (node == orgin || orgin.getWorld().provider.dimensionId != node.getWorld().provider.dimensionId) {
                //Skip if looking at myself and skip if not in the same dimention
                //TODO, INTERDIM?
                continue;
            }

            //Makes the target coordinates the center of the sphere of the tower if it has one
            IMetaTileEntity nodeInside = node.getMetaTileEntity();
            int targetX;
            int targetY;
            int targetZ;
            if (nodeInside instanceof GT_MetaTileEntity_TM_teslaCoil) {
                GT_MetaTileEntity_TM_teslaCoil teslaTower = (GT_MetaTileEntity_TM_teslaCoil) nodeInside;
                targetX = teslaTower.posTop.get0();
                targetY = teslaTower.posTop.get1();
                targetZ = teslaTower.posTop.get2();
            } else {
                targetX = node.getXCoord();
                targetY = node.getYCoord();
                targetZ = node.getZCoord();
            }

            //Find the xyz offsets and calculate the distance between us and the target
            int xPosOffset = targetX - orginX;
            int yPosOffset = targetY - orginY;
            int zPosOffset = targetZ - orginZ;
            int distance = (int) ceil(sqrt(pow(xPosOffset, 2) + pow(yPosOffset, 2) + pow(zPosOffset, 2)));
            //Thought we need abs here, we don't. An Integer to the power of two is going to be either 0 or positive.
            //We also can just put stuff here without further checks, as we always check the next section
            generatedNodeMap.put(node, distance);
        }
        return generatedNodeMap;
    }

    public static Map<IGregTechTileEntity, Integer> cleanTeslaNodeMap(Map<IGregTechTileEntity, Integer> nodeMap, IGregTechTileEntity orgin) {
        IMetaTileEntity orginInside = orgin.getMetaTileEntity();
        //Assumes that if the orgin is not a Tesla Tower, it mus be a single block.
        boolean isMulti = orginInside instanceof GT_MetaTileEntity_TM_teslaCoil;

        for (Map.Entry<IGregTechTileEntity, Integer> Rx : nodeMap.entrySet()) {
            //All the checks need to pass or the target gets removed from the transmission list
            IGregTechTileEntity node = Rx.getKey();
            //Null check
            if (node != null) {
                IMetaTileEntity nodeInside = node.getMetaTileEntity();
                //We need this in a try catch, just as a precaution that a chunk had unloaded or a machine was removed
                try {
                    //Is it an Active Tesla Tower with at least some energy capacity?
                    //Singles and Multis can send power here, so we don't check
                    if (nodeInside instanceof GT_MetaTileEntity_TM_teslaCoil && node.isActive()) {
                        GT_MetaTileEntity_TM_teslaCoil teslaTower = (GT_MetaTileEntity_TM_teslaCoil) nodeInside;
                        if (teslaTower.maxEUStore() > 0) {
                            continue;
                        }
                        //Is it a Tesla Transceiver with at least one battery?
                        //Only multis can send power to singles
                    } else if (isMulti && nodeInside instanceof GT_MetaTileEntity_TeslaCoil) {
                        GT_MetaTileEntity_TeslaCoil teslaTransceiver = (GT_MetaTileEntity_TeslaCoil) nodeInside;
                        if (teslaTransceiver.mBatteryCount > 0) {
                            continue;
                        }
                        //Is it a tile entity with a Tesla Coil Cover?
                        //Only single can send power to non-Rich edition covers
                        //Since Rich edition inherits from regular, we need the final check
                    } else if (!isMulti && node.getCoverBehaviorAtSide((byte) 1) instanceof GT_Cover_TM_TeslaCoil &&
                            node.getEUCapacity() > 0 && !(node.getCoverBehaviorAtSide((byte) 1) instanceof GT_Cover_TM_TeslaCoil_Ultimate)) {
                        continue;
                        //Is it a tile entity with a Tesla Coil Cover Rich edition?
                        //Only multis can send power to Rich edition covers
                    } else if (isMulti && node.getCoverBehaviorAtSide((byte) 1) instanceof GT_Cover_TM_TeslaCoil_Ultimate && node.getEUCapacity() > 0) {
                        continue;
                    }
                } catch (Exception ignored) {
                }
            }
            nodeMap.remove(Rx.getKey());
        }
        return nodeMap;
    }


    @Override
    public boolean onRunningTick(ItemStack aStack) {
        IGregTechTileEntity mte = getBaseMetaTileEntity();

        //Hysteresis based ePowerPass setting
        float energyFrac = (float) getEUVar() / energyCapacity;

        energyCapacityDisplay.set(energyCapacity);
        energyStoredDisplay.set(getEUVar());
        energyFractionDisplay.set(energyFrac);

        if (!ePowerPass && energyFrac > histHighSetting.get()) {
            ePowerPass = true;
        } else if (ePowerPass && energyFrac < histLowSetting.get()) {
            ePowerPass = false;
        }

        //Create the teslaNodeMap
        if (sortTime == sortTimeMinSetting.get()) {
            sortTime = 0;
            sortTimeDisplay.updateStatus();
            teslaNodeMap = generateTeslaNodeMap(mte);
        }
        sortTime++;
        sortTimeDisplay.set(sortTime);

        //Power Limit Settings
        long outputVoltage;
        if (outputVoltageSetting.get() > 0) {
            outputVoltage = min(outputVoltageMax, (long) outputVoltageSetting.get());
        } else {
            outputVoltage = outputVoltageMax;
        }
        outputVoltageDisplay.set(outputVoltage);

        long outputCurrent;
        if (outputCurrentSetting.get() > 0) {
            outputCurrent = min(outputCurrentMax, (long) outputCurrentSetting.get());
        } else {
            outputCurrent = outputCurrentMax;
        }
        outputCurrentDisplay.set(0);

        //Stuff to do if ePowerPass
        if (ePowerPass) {
            //Range calculation and display
            int transferRadiusTower = (int) (transferRadiusTowerSetting.get() * getRangeMulti(mTier, vTier));
            transferRadiusTowerDisplay.set(transferRadiusTower);
            int transferRadiusTransceiver = (int) (transferRadiusTransceiverSetting.get() * getRangeMulti(mTier, vTier));
            transferRadiusTransceiverDisplay.set(transferRadiusTransceiver);
            int transferRadiusCoverUltimate = (int) (transferRadiusCoverUltimateSetting.get() * getRangeMulti(mTier, vTier));
            transferRadiusCoverUltimateDisplay.set(transferRadiusCoverUltimate);

            //Clean the teslaNodeMap
            teslaNodeMap = cleanTeslaNodeMap(teslaNodeMap, mte);

            //Power transfer
            long sparks = outputCurrent;
            while (sparks > 0) {
                boolean overdriveToggle = overDriveSetting.get() > 0;
                boolean idle = true;
                for (Map.Entry<IGregTechTileEntity, Integer> Rx : entriesSortedByValues(teslaNodeMap)) {
                    if (getEUVar() >= (overdriveToggle ? outputVoltage * 2 : outputVoltage)) {
                        IGregTechTileEntity node = Rx.getKey();
                        IMetaTileEntity nodeInside = node.getMetaTileEntity();

                        long[] outputVoltageNow = getOutputVoltage(outputVoltage, Rx.getValue(), overdriveToggle);
                        long outputVoltageInjectable = outputVoltageNow[0];
                        long outputVoltageConsumption = outputVoltageNow[1];

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
            if (sortTime % 60 == 0 && !sparkList.isEmpty()) {
                NetworkDispatcher.INSTANCE.sendToAllAround(new RendererMessage.RendererData(sparkList),
                        mte.getWorld().provider.dimensionId,
                        posTop.get0(),
                        posTop.get1(),
                        posTop.get2(),
                        256);
            }
            sparkList.clear();
        } else {
            outputCurrentDisplay.set(0);
        }
        return true;
    }

    @Override
    public long maxEUStore() {
        return energyCapacity * 2;
    }

    @Override
    public long getEUVar() {
        return getBaseMetaTileEntity().isActive() ? super.getEUVar() : 0;
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

    private boolean addFrameToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        return aTileEntity != null && aTileEntity.getMetaTileEntity() instanceof GT_MetaPipeEntity_Frame;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        Structure.builder(shape, blockType, blockMetas[(stackSize.stackSize - 1) % 7], 3, 16, 0, getBaseMetaTileEntity(), getExtendedFacing(), hintsOnly);
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return description;
    }
}

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
import static gregtech.api.util.GTUtility.validMTEList;
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

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ListWidget;
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
import tectech.thing.metaTileEntity.multi.base.Parameter;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;
import tectech.thing.metaTileEntity.multi.base.render.TTRenderedExtendedFacingTexture;

public class MTETeslaTower extends TTMultiblockBase implements ISurvivalConstructable, ITeslaConnectable {

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
    private long usedAmps;

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

    public MTETeslaTower(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        initParameters();
    }

    public MTETeslaTower(String aName) {
        super(aName);
        initParameters();
    }

    @Override
    public void initParameters() {
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
                Integer.MAX_VALUE,
                "gt.blockmachines.multimachine.tm.teslaCoil.transferRadius"));
        parameterMap.put(
            "transceiverRadius",
            new Parameter.IntegerParameter(
                ConfigHandler.TeslaTweaks.TESLA_MULTI_RANGE_TRANSCEIVER,
                0,
                Integer.MAX_VALUE,
                "gt.blockmachines.multimachine.tm.teslaCoil.transceiverRadius"));
        parameterMap.put(
            "ultimateCoverTransferRadius",
            new Parameter.IntegerParameter(
                ConfigHandler.TeslaTweaks.TESLA_MULTI_RANGE_COVER,
                0,
                Integer.MAX_VALUE,
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
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        // Hysteresis based ePowerPass setting
        float energyFrac = (float) getEUVar() / energyCapacity;

        if (!ePowerPass && energyFrac > ((Parameter.DoubleParameter) parameterMap.get("hysteresisHigh")).getValue()) {
            ePowerPass = true;
        } else
            if (ePowerPass && energyFrac < ((Parameter.DoubleParameter) parameterMap.get("hysteresisLow")).getValue()) {
                ePowerPass = false;
            }

        // Power Limit Settings
        int outputVoltageParameter = ((Parameter.IntegerParameter) parameterMap.get("outputVoltage")).getValue();
        if (outputVoltageParameter > 0) {
            outputVoltage = min(outputVoltageMax, outputVoltageParameter);
        } else {
            outputVoltage = outputVoltageMax;
        }

        int outputCurrentParameter = ((Parameter.IntegerParameter) parameterMap.get("outputCurrent")).getValue();
        if (outputCurrentParameter > 0) {
            outputCurrent = min(outputCurrentMax, outputCurrentParameter);
        } else {
            outputCurrent = outputCurrentMax;
        }

        // Power transfer
        usedAmps = TeslaUtil.powerTeslaNodeMap(this);

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
        return ((Parameter.IntegerParameter) parameterMap.get("transferRadius")).getValue();
    }

    @Override
    public boolean isOverdriveEnabled() {
        return ((Parameter.BooleanParameter) parameterMap.get("overdrive")).getValue();
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
    public void insertTexts(ListWidget<IWidget, ?> machineInfo, ItemStackHandler invSlot,
        PanelSyncManager syncManager) {
        IntSyncValue outputVoltageSyncer = new IntSyncValue(() -> 0, () -> (int) outputVoltage);
        IntSyncValue outputCurrentSyncer = new IntSyncValue(() -> 0, () -> (int) outputCurrent);
        IntSyncValue usedAmpsSyncer = new IntSyncValue(() -> 0, () -> (int) usedAmps);
        syncManager.syncValue("outputVoltage", outputVoltageSyncer);
        syncManager.syncValue("outputCurrent", outputCurrentSyncer);
        syncManager.syncValue("usedAmps", usedAmpsSyncer);
        super.insertTexts(machineInfo, invSlot, syncManager);
        machineInfo.child(
            IKey.dynamic(
                () -> EnumChatFormatting.WHITE + "Output Voltage: "
                    + EnumChatFormatting.BLUE
                    + outputVoltageSyncer.getValue())
                .asWidget()
                .setEnabledIf(w -> getBaseMetaTileEntity().isActive())
                .color(COLOR_TEXT_WHITE.get())
                .widthRel(1)
                .marginBottom(2));
        machineInfo.child(
            IKey.dynamic(
                () -> EnumChatFormatting.WHITE + "Used Amperage: "
                    + EnumChatFormatting.GREEN
                    + usedAmpsSyncer.getValue()
                    + "/"
                    + outputCurrentSyncer.getValue())
                .asWidget()
                .setEnabledIf(w -> getBaseMetaTileEntity().isActive())
                .color(COLOR_TEXT_WHITE.get())
                .widthRel(1)
                .marginBottom(2));
    }
}

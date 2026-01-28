package kekztech.common.tileentities;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onlyIf;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.filterByMTEClass;
import static java.lang.Math.min;
import static kekztech.util.Util.toPercentageFrom;
import static kekztech.util.Util.toStandardForm;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.AutoPlaceEnvironment;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureUtility;
import com.gtnewhorizon.structurelib.util.ItemStackPredicate.NBTMode;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchDynamo;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.metatileentity.implementations.MTEHatchMaintenance;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.LongData;
import gregtech.api.util.LongRunningAverage;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.gui.modularui.multiblock.MTELapotronicSuperCapacitorgui;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.misc.WirelessNetworkManager;
import gregtech.common.misc.spaceprojects.SpaceProjectManager;
import gregtech.common.tileentities.machines.multi.drone.MTEHatchDroneDownLink;
import kekztech.common.Blocks;
import kekztech.common.itemBlocks.ItemBlockLapotronicEnergyUnit;
import tectech.thing.metaTileEntity.hatch.MTEHatchDynamoMulti;
import tectech.thing.metaTileEntity.hatch.MTEHatchDynamoTunnel;
import tectech.thing.metaTileEntity.hatch.MTEHatchEnergyMulti;
import tectech.thing.metaTileEntity.hatch.MTEHatchEnergyTunnel;

public class MTELapotronicSuperCapacitor extends MTEEnhancedMultiBlockBase<MTELapotronicSuperCapacitor>
    implements ISurvivalConstructable {

    private enum TopState {
        MayBeTop,
        Top,
        NotTop
    }

    private boolean canUseWireless = false;
    private boolean wireless_mode = false;
    private int counter = 1;
    private boolean balanced = false;

    private final LongRunningAverage energyInputValues1h = new LongRunningAverage(3600 * 20);
    private final LongRunningAverage energyOutputValues1h = new LongRunningAverage(3600 * 20);

    private final LongData energyInputValues = energyInputValues1h.view(DURATION_AVERAGE_TICKS);
    private final LongData energyOutputValues = energyOutputValues1h.view(DURATION_AVERAGE_TICKS);

    private final LongData energyInputValues5m = energyInputValues1h.view(5 * 60 * 20);
    private final LongData energyOutputValues5m = energyOutputValues1h.view(5 * 60 * 20);

    private final long max_passive_drain_eu_per_tick_per_uhv_cap = 1_000_000;
    private final long max_passive_drain_eu_per_tick_per_uev_cap = 100_000_000;
    private final long max_passive_drain_eu_per_tick_per_uiv_cap = (long) GTUtility.powInt(10, 10);
    private final long max_passive_drain_eu_per_tick_per_umv_cap = (long) GTUtility.powInt(10, 12);

    private final BigInteger guiCapacityStoredReformatLimit = BigInteger.valueOf(1_000_000_000_000L);

    public enum Capacitor {

        IV(2, BigInteger.valueOf(ItemBlockLapotronicEnergyUnit.IV_cap_storage)),
        LuV(3, BigInteger.valueOf(ItemBlockLapotronicEnergyUnit.LuV_cap_storage)),
        ZPM(4, BigInteger.valueOf(ItemBlockLapotronicEnergyUnit.ZPM_cap_storage)),
        UV(5, BigInteger.valueOf(ItemBlockLapotronicEnergyUnit.UV_cap_storage)),
        UHV(6, BigInteger.valueOf(ItemBlockLapotronicEnergyUnit.UHV_cap_storage)),
        None(0, BigInteger.ZERO),
        EV(1, BigInteger.valueOf(ItemBlockLapotronicEnergyUnit.EV_cap_storage)),
        UEV(7, BigInteger.valueOf(ItemBlockLapotronicEnergyUnit.UEV_cap_storage)),
        UIV(8, BigInteger.valueOf(ItemBlockLapotronicEnergyUnit.UIV_cap_storage)),
        UMV(9, ItemBlockLapotronicEnergyUnit.UMV_cap_storage);

        private final int minimalGlassTier;
        private final BigInteger providedCapacity;
        public static final Capacitor[] VALUES = values();
        public static final Capacitor[] VALUES_BY_TIER = Arrays.stream(values())
            .sorted(Comparator.comparingInt(Capacitor::getMinimalGlassTier))
            .toArray(Capacitor[]::new);

        Capacitor(int minimalGlassTier, BigInteger providedCapacity) {
            this.minimalGlassTier = minimalGlassTier;
            this.providedCapacity = providedCapacity;
        }

        public int getMinimalGlassTier() {
            return minimalGlassTier;
        }

        public BigInteger getProvidedCapacity() {
            return providedCapacity;
        }

        public static int getIndexFromGlassTier(int glassTier) {
            for (int index = 0; index < VALUES.length; index++) {
                if (VALUES[index].getMinimalGlassTier() == glassTier) {
                    return index;
                }
            }
            return -1;
        }
    }

    private static final String STRUCTURE_PIECE_BASE = "base";
    private static final String STRUCTURE_PIECE_LAYER = "slice";
    private static final String STRUCTURE_PIECE_TOP = "top";
    private static final String STRUCTURE_PIECE_MID = "mid";
    private static final int GLASS_TIER_UNSET = -1;

    private static final Block LSC_PART = Blocks.lscLapotronicEnergyUnit;
    private static final Item LSC_PART_ITEM = Item.getItemFromBlock(LSC_PART);
    private static final int CASING_META = 0;
    private static final int CASING_TEXTURE_ID = (42 << 7) | 127;

    private static final int DURATION_AVERAGE_TICKS = 100;
    private static final int DEBUG_POWER_WINDOW_ID = 10;
    private static final int WIRELESS_WARNING_WINDOW_ID = 11;

    private static final BigInteger LONG_MAX = BigInteger.valueOf(Long.MAX_VALUE);

    // height channel for height.
    // glass channel for glass
    // capacitor channel for capacitor, but it really just pick whatever capacitor it can find in survival
    private static final IStructureDefinition<MTELapotronicSuperCapacitor> STRUCTURE_DEFINITION = IStructureDefinition
        .<MTELapotronicSuperCapacitor>builder()
        .addShape(
            STRUCTURE_PIECE_BASE,
            transpose(
                new String[][] { { "bbbbb", "bbbbb", "bbbbb", "bbbbb", "bbbbb", },
                    { "bb~bb", "bbbbb", "bbbbb", "bbbbb", "bbbbb", }, }))
        .addShape(
            STRUCTURE_PIECE_LAYER,
            transpose(new String[][] { { "ggggg", "gcccg", "gcccg", "gcccg", "ggggg", }, }))
        .addShape(STRUCTURE_PIECE_TOP, transpose(new String[][] { { "ggggg", "ggggg", "ggggg", "ggggg", "ggggg", }, }))
        .addShape(STRUCTURE_PIECE_MID, transpose(new String[][] { { "ggggg", "gCCCg", "gCCCg", "gCCCg", "ggggg", }, }))
        .addElement(
            'b',
            buildHatchAdder(
                MTELapotronicSuperCapacitor.class).atLeast(LSCHatchElement.Energy, LSCHatchElement.Dynamo, Maintenance)
                    .hatchItemFilterAnd(
                        (t, h) -> GTStructureChannels.BOROGLASS.getValue(h) < 6
                            ? filterByMTEClass(ImmutableList.of(MTEHatchEnergyTunnel.class, MTEHatchDynamoTunnel.class))
                                .negate()
                            : s -> true)
                    .casingIndex(CASING_TEXTURE_ID)
                    .hint(1)
                    .buildAndChain(onElementPass(te -> te.casingAmount++, ofBlock(LSC_PART, CASING_META))))
        .addElement('g', chainAllGlasses(GLASS_TIER_UNSET, (te, t) -> te.glassTier = t, te -> te.glassTier))
        .addElement(
            'c',
            ofChain(
                onlyIf(
                    te -> te.topState != TopState.NotTop,
                    onElementPass(
                        te -> te.topState = TopState.Top,
                        chainAllGlasses(-1, (te, t) -> te.glassTier = t, te -> te.glassTier))),
                onlyIf(
                    te -> te.topState != TopState.Top,
                    onElementPass(te -> te.topState = TopState.NotTop, CellElement.INSTANCE))))
        .addElement('C', GTStructureChannels.LSC_CAPACITOR.use(CellElement.INSTANCE))
        .build();

    private static final BigInteger MAX_LONG = BigInteger.valueOf(Long.MAX_VALUE);

    private final Set<MTEHatchEnergyMulti> mEnergyHatchesTT = new HashSet<>();
    private final Set<MTEHatchDynamoMulti> mDynamoHatchesTT = new HashSet<>();
    private final Set<MTEHatchEnergyTunnel> mEnergyTunnelsTT = new HashSet<>();
    private final Set<MTEHatchDynamoTunnel> mDynamoTunnelsTT = new HashSet<>();
    /**
     * Count the amount of capacitors of each tier in each slot. Index = meta - 1
     */
    private final int[] capacitors = new int[10];
    private BigInteger capacity = BigInteger.ZERO;
    private BigInteger stored = BigInteger.ZERO;
    private long passiveDischargeAmount = 0;
    private long inputLastTick = 0;
    private long outputLastTick = 0;
    private int repairStatusCache = 0;

    private int glassTier = -1;
    private int casingAmount = 0;
    private TopState topState = TopState.MayBeTop;

    private long mMaxEUIn = 0;
    private long mMaxEUOut = 0;

    public MTELapotronicSuperCapacitor(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTELapotronicSuperCapacitor(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity var1) {
        return new MTELapotronicSuperCapacitor(super.mName);
    }

    @Override
    public IStructureDefinition<MTELapotronicSuperCapacitor> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    Boolean showWarning = true;
    Boolean hasRebalanced = false;
    Boolean canRebalance = false;

    public Boolean getShowWarning() {
        return showWarning;
    }

    public void setShowWarning(Boolean showWarning) {
        this.showWarning = showWarning;
    }

    public Boolean getCanRebalance() {
        return canRebalance;
    }

    public void setCanRebalance(Boolean canRebalance) {
        this.canRebalance = canRebalance;
    }

    public BigInteger getStored() {
        return stored;
    }

    public Boolean getHasRebalanced() {
        return hasRebalanced;
    }

    public void setHasRebalanced(Boolean hasRebalanced) {
        this.hasRebalanced = hasRebalanced;
    }

    public LongData getEnergyInputValues() {
        return energyInputValues;
    }

    public LongData getEnergyOutputValues() {
        return energyOutputValues;
    }

    public void setStored(BigInteger stored) {
        this.stored = stored;
    }

    public long getPassiveDischargeAmount() {
        return passiveDischargeAmount;
    }

    public BigInteger getEnergyCapacity() {
        return capacity;
    }

    public void setCapacity(BigInteger capacity) {
        this.capacity = capacity;
    }

    private void processInputHatch(MTEHatch aHatch, int aBaseCasingIndex) {
        mMaxEUIn += aHatch.maxEUInput() * aHatch.maxAmperesIn();
        aHatch.updateTexture(aBaseCasingIndex);
    }

    private void processOutputHatch(MTEHatch aHatch, int aBaseCasingIndex) {
        mMaxEUOut += aHatch.maxEUOutput() * aHatch.maxAmperesOut();
        aHatch.updateTexture(aBaseCasingIndex);
    }

    private boolean addBottomHatches(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null || aTileEntity.isDead()) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (!(aMetaTileEntity instanceof MTEHatch)) return false;
        if (aMetaTileEntity instanceof MTEHatchMaintenance hatch) {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            if (hatch instanceof MTEHatchDroneDownLink droneDownLink) {
                droneDownLink.registerMachineController(this);
            }
            return MTELapotronicSuperCapacitor.this.mMaintenanceHatches.add(hatch);
        } else if (aMetaTileEntity instanceof MTEHatchEnergy) {
            // Add GT hatches
            final MTEHatchEnergy tHatch = ((MTEHatchEnergy) aMetaTileEntity);
            processInputHatch(tHatch, aBaseCasingIndex);
            return mEnergyHatches.add(tHatch);
        } else if (aMetaTileEntity instanceof MTEHatchEnergyTunnel) {
            // Add TT Laser hatches
            final MTEHatchEnergyTunnel tHatch = ((MTEHatchEnergyTunnel) aMetaTileEntity);
            processInputHatch(tHatch, aBaseCasingIndex);
            return mEnergyTunnelsTT.add(tHatch);
        } else if (aMetaTileEntity instanceof MTEHatchEnergyMulti) {
            // Add TT hatches
            final MTEHatchEnergyMulti tHatch = (MTEHatchEnergyMulti) aMetaTileEntity;
            processInputHatch(tHatch, aBaseCasingIndex);
            return mEnergyHatchesTT.add(tHatch);
        } else if (aMetaTileEntity instanceof MTEHatchDynamo) {
            // Add GT hatches
            final MTEHatchDynamo tDynamo = (MTEHatchDynamo) aMetaTileEntity;
            processOutputHatch(tDynamo, aBaseCasingIndex);
            return mDynamoHatches.add(tDynamo);
        } else if (aMetaTileEntity instanceof MTEHatchDynamoTunnel) {
            // Add TT Laser hatches
            final MTEHatchDynamoTunnel tDynamo = (MTEHatchDynamoTunnel) aMetaTileEntity;
            processOutputHatch(tDynamo, aBaseCasingIndex);
            return mDynamoTunnelsTT.add(tDynamo);
        } else if (aMetaTileEntity instanceof MTEHatchDynamoMulti) {
            // Add TT hatches
            final MTEHatchDynamoMulti tDynamo = (MTEHatchDynamoMulti) aMetaTileEntity;
            processOutputHatch(tDynamo, aBaseCasingIndex);
            return mDynamoHatchesTT.add(tDynamo);
        }
        return false;
    }

    private int getUHVCapacitorCount() {
        return capacitors[4];
    }

    private int getUEVCapacitorCount() {
        return capacitors[7];
    }

    private int getUIVCapacitorCount() {
        return capacitors[8];
    }

    private int getUMVCapacitorCount() {
        return capacitors[9];
    }

    private int wirelessCapableCapacitors() {
        return capacitors[4] + capacitors[7] + capacitors[8] + capacitors[9];
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Energy Storage, LSC")
            .addInfo("Loses energy equal to 1% of the total capacity every 24 hours")
            .addInfo(
                "Capped at " + EnumChatFormatting.RED
                    + formatNumber(max_passive_drain_eu_per_tick_per_uhv_cap)
                    + EnumChatFormatting.GRAY
                    + " EU/t passive loss per "
                    + GTValues.TIER_COLORS[9]
                    + GTValues.VN[9]
                    + EnumChatFormatting.GRAY
                    + " capacitor")
            .addInfo(
                "The passive loss increases " + EnumChatFormatting.DARK_RED
                    + "100"
                    + EnumChatFormatting.GRAY
                    + "-fold"
                    + " for every capacitor tier above")
            .addInfo("Passive loss is multiplied by the number of maintenance issues present")
            .addSeparator()
            .addInfo("Glass shell has to be Tier - 3 of the highest capacitor tier")
            .addTecTechHatchInfo()
            .addMinGlassForLaser(VoltageIndex.UV)
            .addInfo("Add more or better capacitors to increase capacity")
            .addSeparator()
            .addInfo("Wireless mode can be enabled by right clicking with a screwdriver")
            .addInfo(
                "This mode can only be enabled if you have a " + GTValues.TIER_COLORS[9]
                    + GTValues.VN[9]
                    + EnumChatFormatting.GRAY
                    + "+ capacitor in the multiblock.")
            .addInfo(
                "When enabled every " + EnumChatFormatting.BLUE
                    + formatNumber(ItemBlockLapotronicEnergyUnit.LSC_time_between_wireless_rebalance_in_ticks)
                    + EnumChatFormatting.GRAY
                    + " ticks the LSC will attempt to re-balance against your")
            .addInfo("wireless EU network.")
            .addInfo(
                "If there is less than " + EnumChatFormatting.RED
                    + formatNumber(ItemBlockLapotronicEnergyUnit.LSC_wireless_eu_cap)
                    + EnumChatFormatting.GRAY
                    + "("
                    + GTValues.TIER_COLORS[9]
                    + GTValues.VN[9]
                    + EnumChatFormatting.GRAY
                    + ") EU in the LSC")
            .addInfo("it will withdraw from the network and add to the LSC.")
            .addInfo(
                "If there is more it will add " + EnumChatFormatting.DARK_RED
                    + EnumChatFormatting.BOLD
                    + EnumChatFormatting.UNDERLINE
                    + "all excess"
                    + EnumChatFormatting.RESET
                    + EnumChatFormatting.GRAY
                    + " EU to the network, removing it from the LSC")
            .addInfo("This can potentially brick your base, be careful")
            .addInfo(
                "The threshold increases " + EnumChatFormatting.DARK_RED
                    + "100"
                    + EnumChatFormatting.GRAY
                    + "-fold"
                    + " for every capacitor tier above")
            .beginVariableStructureBlock(5, 5, 4, 50, 5, 5, false)
            .addStructureInfo("Modular height of 4-50 blocks.")
            .addController("Front center bottom")
            .addOtherStructurePart("Lapotronic Super Capacitor Casing", "5x2x5 base (at least 17x)")
            .addOtherStructurePart(
                "Lapotronic Capacitor (" + GTValues.TIER_COLORS[4]
                    + GTValues.VN[4]
                    + EnumChatFormatting.GRAY
                    + "-"
                    + GTValues.TIER_COLORS[8]
                    + GTValues.VN[8]
                    + EnumChatFormatting.GRAY
                    + "), Ultimate Capacitor ("
                    + GTValues.TIER_COLORS[9]
                    + GTValues.VN[9]
                    + EnumChatFormatting.GRAY
                    + "-"
                    + GTValues.TIER_COLORS[12]
                    + GTValues.VN[12]
                    + EnumChatFormatting.GRAY
                    + ")",
                "Center 3x(1-47)x3 above base (9-423 blocks)")
            .addStructureInfo(
                "You can also use the Empty Capacitor to save materials if you use it for less than half the blocks")
            .addCasingInfoRange("Any Tiered Glass", 41, 777, true)
            .addEnergyHatch("Any casing")
            .addDynamoHatch("Any casing")
            .addOtherStructurePart(
                "Laser Target/Source Hatches",
                "Any casing, must be using " + GTValues.TIER_COLORS[8]
                    + GTValues.VN[8]
                    + EnumChatFormatting.GRAY
                    + "-tier glass")
            .addStructureInfo("You can have several I/O Hatches")
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            .addSubChannelUsage(
                GTStructureChannels.LSC_CAPACITOR,
                "Capacitor Tier if specified. Otherwise pick any acceptable capacitor.")
            .addSubChannelUsage(GTStructureChannels.STRUCTURE_HEIGHT)
            .addMaintenanceHatch("Any casing")
            .toolTipFinisher();
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side,
        ForgeDirection forgeDirectionacing, int colorIndex, boolean aActive, boolean aRedstone) {
        ITexture[] sTexture = new ITexture[] {
            TextureFactory.of(BlockIcons.MACHINE_CASING_FUSION_GLASS, Dyes.getModulation(-1)) };
        if (side == forgeDirectionacing && aActive) {
            sTexture = new ITexture[] {
                TextureFactory.of(BlockIcons.MACHINE_CASING_FUSION_GLASS_YELLOW, Dyes.getModulation(-1)) };
        }
        return sTexture;
    }

    @Override
    public boolean supportsPowerPanel() {
        return false;
    }

    private UUID global_energy_user_uuid;

    @Override
    public void onFirstTick(IGregTechTileEntity tileEntity) {
        super.onFirstTick(tileEntity);

        if (!tileEntity.isServerSide()) return;

        global_energy_user_uuid = tileEntity.getOwnerUuid();
        SpaceProjectManager.checkOrCreateTeam(global_energy_user_uuid);
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        this.mProgresstime = 1;
        this.mMaxProgresstime = 1;
        this.mEUt = 0;
        this.mEfficiencyIncrease = 10000;
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity thisController, ItemStack guiSlotItem) {
        // Reset capacitor counts
        Arrays.fill(capacitors, 0);
        // Clear TT hatches
        mEnergyHatchesTT.clear();
        mDynamoHatchesTT.clear();
        mEnergyTunnelsTT.clear();
        mDynamoTunnelsTT.clear();

        mMaxEUIn = 0;
        mMaxEUOut = 0;

        glassTier = GLASS_TIER_UNSET;
        casingAmount = 0;

        if (!checkPiece(STRUCTURE_PIECE_BASE, 2, 1, 0)) return false;

        if (casingAmount < 17) return false;

        topState = TopState.NotTop; // need at least one layer of capacitor to form, obviously
        int layer = 2;
        while (true) {
            if (!checkPiece(STRUCTURE_PIECE_LAYER, 2, layer, 0)) return false;
            layer++;
            if (topState == TopState.Top) break; // top found, break out
            topState = TopState.MayBeTop;
            if (layer > 50) return false; // too many layers
        }

        // Make sure glass tier is T-2 of the highest tier capacitor in the structure
        // Count down from the highest tier until an entry is found
        // Borosilicate glass after 5 are just recolours of 0
        for (int highestGlassTier = capacitors.length - 1; highestGlassTier >= 0; highestGlassTier--) {
            int highestCapacitor = Capacitor.getIndexFromGlassTier(highestGlassTier);
            if (capacitors[highestCapacitor] > 0) {
                if (Capacitor.VALUES[highestCapacitor].getMinimalGlassTier() > glassTier) return false;
                break;
            }
        }

        // Glass has to be at least UV-tier to allow TT Laser hatches
        if (glassTier < 8) {
            if (!mEnergyTunnelsTT.isEmpty() || !mDynamoTunnelsTT.isEmpty()) return false;
        }

        // Check if enough (more than 50%) non-empty caps
        if (capacitors[5] > capacitors[0] + capacitors[1]
            + capacitors[2]
            + capacitors[3]
            + getUHVCapacitorCount()
            + capacitors[6]
            + getUEVCapacitorCount()
            + getUIVCapacitorCount()
            + getUMVCapacitorCount()) return false;

        // Calculate total capacity
        capacity = BigInteger.ZERO;
        for (int i = 0; i < capacitors.length; i++) {
            int count = capacitors[i];
            capacity = capacity.add(
                Capacitor.VALUES[i].getProvidedCapacity()
                    .multiply(BigInteger.valueOf(count)));
        }
        // Calculate how much energy to void each tick
        passiveDischargeAmount = recalculateLossWithMaintenance(getRepairStatus());
        return mMaintenanceHatches.size() == 1;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        int layer = GTStructureChannels.STRUCTURE_HEIGHT.getValueClamped(stackSize, 4, 50);
        buildPiece(STRUCTURE_PIECE_BASE, stackSize, hintsOnly, 2, 1, 0);
        for (int i = 2; i < layer - 1; i++) buildPiece(STRUCTURE_PIECE_MID, stackSize, hintsOnly, 2, i, 0);
        buildPiece(STRUCTURE_PIECE_TOP, stackSize, hintsOnly, 2, layer - 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int layer = GTStructureChannels.STRUCTURE_HEIGHT.getValueClamped(stackSize, 4, 50);
        int built;
        built = survivalBuildPiece(STRUCTURE_PIECE_BASE, stackSize, 2, 1, 0, elementBudget, env, false, true);
        if (built >= 0) return built;
        for (int i = 2; i < layer - 1; i++)
            built = survivalBuildPiece(STRUCTURE_PIECE_MID, stackSize, 2, i, 0, elementBudget, env, false, true);
        if (built >= 0) return built;
        return survivalBuildPiece(STRUCTURE_PIECE_TOP, stackSize, 2, layer - 1, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean onRunningTick(ItemStack stack) {
        // Reset I/O cache
        inputLastTick = 0;
        outputLastTick = 0;

        long temp_stored = 0L;

        // Draw energy from GT hatches
        for (MTEHatchEnergy eHatch : super.mEnergyHatches) {
            if (eHatch == null || !eHatch.isValid()) {
                continue;
            }
            final long power = getPowerToDraw(eHatch.maxEUInput() * eHatch.maxAmperesIn());
            if (eHatch.getEUVar() >= power) {
                eHatch.setEUVar(eHatch.getEUVar() - power);
                temp_stored += power;
                inputLastTick += power;
            }
        }

        // Output energy to GT hatches
        for (MTEHatchDynamo eDynamo : super.mDynamoHatches) {
            if (eDynamo == null || !eDynamo.isValid()) {
                continue;
            }
            final long power = getPowerToPush(eDynamo.maxEUOutput() * eDynamo.maxAmperesOut());
            if (power <= eDynamo.maxEUStore() - eDynamo.getEUVar()) {
                eDynamo.setEUVar(eDynamo.getEUVar() + power);
                temp_stored -= power;
                outputLastTick += power;
            }
        }

        // Draw energy from TT hatches
        for (MTEHatchEnergyMulti eHatch : mEnergyHatchesTT) {
            if (eHatch == null || !eHatch.isValid()) {
                continue;
            }
            final long power = getPowerToDraw(eHatch.maxEUInput() * eHatch.maxAmperesIn());
            if (eHatch.getEUVar() >= power) {
                eHatch.setEUVar(eHatch.getEUVar() - power);
                temp_stored += power;
                inputLastTick += power;
            }
        }

        // Output energy to TT hatches
        for (MTEHatchDynamoMulti eDynamo : mDynamoHatchesTT) {
            if (eDynamo == null || !eDynamo.isValid()) {
                continue;
            }
            final long power = getPowerToPush(eDynamo.maxEUOutput() * eDynamo.maxAmperesOut());
            if (power <= eDynamo.maxEUStore() - eDynamo.getEUVar()) {
                eDynamo.setEUVar(eDynamo.getEUVar() + power);
                temp_stored -= power;
                outputLastTick += power;
            }
        }

        // Draw energy from TT Laser hatches
        for (MTEHatchEnergyTunnel eHatch : mEnergyTunnelsTT) {
            if (eHatch == null || !eHatch.isValid()) {
                continue;
            }
            final long ttLaserWattage = eHatch.maxEUInput() * eHatch.Amperes - (eHatch.Amperes / 20);
            final long power = getPowerToDraw(ttLaserWattage);
            if (eHatch.getEUVar() >= power) {
                eHatch.setEUVar(eHatch.getEUVar() - power);
                temp_stored += power;
                inputLastTick += power;
            }
        }

        // Output energy to TT Laser hatches
        for (MTEHatchDynamoTunnel eDynamo : mDynamoTunnelsTT) {
            if (eDynamo == null || !eDynamo.isValid()) {
                continue;
            }
            final long ttLaserWattage = eDynamo.maxEUOutput() * eDynamo.Amperes - (eDynamo.Amperes / 20);
            final long power = getPowerToPush(ttLaserWattage);
            if (power <= eDynamo.maxEUStore() - eDynamo.getEUVar()) {
                eDynamo.setEUVar(eDynamo.getEUVar() + power);
                temp_stored -= power;
                outputLastTick += power;
            }
        }

        if (wirelessCapableCapacitors() <= 0) {
            wireless_mode = false;
        }

        // Every LSC_time_between_wireless_rebalance_in_ticks check against wireless network for re-balancing.
        counter++;
        if (wireless_mode && (counter >= ItemBlockLapotronicEnergyUnit.LSC_time_between_wireless_rebalance_in_ticks)) {

            // Reset tick counter.
            counter = rebalance();
        }

        // Lose some energy.
        // Re-calculate if the repair status changed.
        if (super.getRepairStatus() != repairStatusCache) {
            passiveDischargeAmount = recalculateLossWithMaintenance(super.getRepairStatus());
        }

        // This will break if you transfer more than 2^63 EU/t, so don't do that. Thanks <3
        temp_stored -= passiveDischargeAmount;
        stored = stored.add(BigInteger.valueOf(temp_stored));

        // Check that the machine has positive EU stored.
        stored = (stored.compareTo(BigInteger.ZERO) <= 0) ? BigInteger.ZERO : stored;

        IGregTechTileEntity tBMTE = this.getBaseMetaTileEntity();

        tBMTE.injectEnergyUnits(ForgeDirection.UNKNOWN, inputLastTick, 1L);
        tBMTE.drainEnergyUnits(ForgeDirection.UNKNOWN, outputLastTick, 1L);

        // collect stats
        energyInputValues1h.update(inputLastTick);
        energyOutputValues1h.update(outputLastTick);
        return true;
    }

    public int rebalance() {

        balanced = true;

        // Find difference.
        BigInteger transferred_eu = stored.subtract(
            (ItemBlockLapotronicEnergyUnit.LSC_wireless_eu_cap.multiply(BigInteger.valueOf(getUHVCapacitorCount())))
                .add(
                    ItemBlockLapotronicEnergyUnit.UEV_wireless_eu_cap
                        .multiply(BigInteger.valueOf(getUEVCapacitorCount())))
                .add(
                    ItemBlockLapotronicEnergyUnit.UIV_wireless_eu_cap
                        .multiply(BigInteger.valueOf(getUIVCapacitorCount())))
                .add(
                    ItemBlockLapotronicEnergyUnit.UMV_wireless_eu_cap
                        .multiply(BigInteger.valueOf(getUMVCapacitorCount()))));

        if (transferred_eu.signum() == -1) {
            inputLastTick += Math.abs(transferred_eu.longValue());
        } else {
            outputLastTick += transferred_eu.longValue();
        }

        // If that difference can be added then do so.
        if (WirelessNetworkManager.addEUToGlobalEnergyMap(global_energy_user_uuid, transferred_eu)) {
            // If it succeeds there was sufficient energy so set the internal capacity as such.
            stored = ItemBlockLapotronicEnergyUnit.LSC_wireless_eu_cap
                .multiply(BigInteger.valueOf(getUHVCapacitorCount()))
                .add(
                    ItemBlockLapotronicEnergyUnit.UEV_wireless_eu_cap
                        .multiply(BigInteger.valueOf(getUEVCapacitorCount()))
                        .add(
                            ItemBlockLapotronicEnergyUnit.UIV_wireless_eu_cap
                                .multiply(BigInteger.valueOf(getUIVCapacitorCount())))
                        .add(
                            ItemBlockLapotronicEnergyUnit.UMV_wireless_eu_cap
                                .multiply(BigInteger.valueOf(getUMVCapacitorCount()))));
        }

        return 1;
    }

    /**
     * To be called whenever the maintenance status changes or the capacity was recalculated
     *
     * @param repairStatus This machine's repair status
     * @return new BigInteger instance for passiveDischargeAmount
     */
    private long recalculateLossWithMaintenance(int repairStatus) {
        repairStatusCache = repairStatus;

        long temp_capacity_divided = 0;

        if (wirelessCapableCapacitors() == 0) {
            temp_capacity_divided = capacity.divide(BigInteger.valueOf(100L * 86400L * 20L))
                .longValue();
        }

        // Passive loss is multiplied by number of UHV+ caps. Minimum of 1 otherwise loss is 0 for non-UHV+ caps
        // calculations.
        if (wirelessCapableCapacitors() != 0) {
            temp_capacity_divided = getUHVCapacitorCount() * max_passive_drain_eu_per_tick_per_uhv_cap
                + getUEVCapacitorCount() * max_passive_drain_eu_per_tick_per_uev_cap
                + getUIVCapacitorCount() * max_passive_drain_eu_per_tick_per_uiv_cap
                + getUMVCapacitorCount() * max_passive_drain_eu_per_tick_per_umv_cap;
        }

        // Passive loss is multiplied by number of maintenance issues.
        // Maximum of 100,000 EU/t drained per UHV cell. The logic is 1% of EU capacity should be drained every 86400
        // seconds (1 day).
        return temp_capacity_divided * (getIdealStatus() - repairStatus + 1);
    }

    /**
     * Calculate how much EU to draw from an Energy Hatch
     *
     * @param hatchWatts Hatch amperage * voltage
     * @return EU amount
     */
    private long getPowerToDraw(long hatchWatts) {
        if (stored.compareTo(capacity) >= 0) return 0;
        final BigInteger remcapActual = capacity.subtract(stored);
        final BigInteger recampLimited = (MAX_LONG.compareTo(remcapActual) > 0) ? remcapActual : MAX_LONG;
        return min(hatchWatts, recampLimited.longValue());
    }

    /**
     * Calculate how much EU to push into a Dynamo Hatch
     *
     * @param hatchWatts Hatch amperage * voltage
     * @return EU amount
     */
    private long getPowerToPush(long hatchWatts) {
        final BigInteger remStoredLimited = (MAX_LONG.compareTo(stored) > 0) ? stored : MAX_LONG;
        return min(hatchWatts, remStoredLimited.longValue());
    }

    public String getTimeTo() {
        double avgIn = energyInputValues.avgLong();
        double avgOut = energyOutputValues.avgLong();
        double passLoss = passiveDischargeAmount;
        double cap = capacity.doubleValue();
        double sto = stored.doubleValue();
        if (avgIn >= avgOut + passLoss) {
            // Calculate time to full if charging
            if (avgIn - passLoss > 0) {
                double timeToFull = (cap - sto) / (avgIn - (passLoss + avgOut)) / 20;
                return translateToLocalFormatted(
                    "kekztech.infodata.lapotronic_super_capacitor.time_to.full",
                    formatTime(timeToFull, true));
            }
            return translateToLocal("kekztech.infodata.lapotronic_super_capacitor.time_to.sth");
        } else {
            // Calculate time to empty if discharging
            double timeToEmpty = sto / ((avgOut + passLoss) - avgIn) / 20;
            return translateToLocalFormatted(
                "kekztech.infodata.lapotronic_super_capacitor.time_to.empty",
                formatTime(timeToEmpty, false));
        }
    }

    public BigInteger getWirelessValue() {
        return WirelessNetworkManager.getUserEU(global_energy_user_uuid);
    }

    public void setWirelessValue(BigInteger value) {
        WirelessNetworkManager.setUserEU(global_energy_user_uuid, value);
    }

    public boolean isWireless_mode() {
        return wireless_mode;
    }

    public void setWireless_mode(boolean wireless_mode) {
        this.wireless_mode = wireless_mode;
    }

    @Override
    public void getExtraInfoData(ArrayList<String> info) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        int secInterval = DURATION_AVERAGE_TICKS / 20;

        info.add(
            EnumChatFormatting.YELLOW + translateToLocal("kekztech.infodata.operational_data")
                + EnumChatFormatting.RESET);
        info.add(
            translateToLocalFormatted("kekztech.infodata.lapotronic_super_capacitor.eu_stored", nf.format(stored)));
        info.add(
            translateToLocalFormatted(
                "kekztech.infodata.lapotronic_super_capacitor.eu_stored",
                toStandardForm(stored)));
        info.add(
            translateToLocalFormatted(
                "kekztech.infodata.lapotronic_super_capacitor.used_capacity",
                toPercentageFrom(stored, capacity)));
        info.add(
            translateToLocalFormatted(
                "kekztech.infodata.lapotronic_super_capacitor.total_capacity",
                nf.format(capacity)));
        info.add(
            translateToLocalFormatted(
                "kekztech.infodata.lapotronic_super_capacitor.total_capacity",
                toStandardForm(capacity)));
        info.add(
            translateToLocalFormatted(
                "kekztech.infodata.lapotronic_super_capacitor.passive_loss",
                nf.format(passiveDischargeAmount)));
        info.add(
            translateToLocalFormatted(
                "kekztech.infodata.lapotronic_super_capacitor.eu_in",
                formatNumber(inputLastTick)));
        info.add(
            translateToLocalFormatted(
                "kekztech.infodata.lapotronic_super_capacitor.eu_out",
                formatNumber(outputLastTick)));
        info.add(
            translateToLocalFormatted(
                "kekztech.infodata.lapotronic_super_capacitor.avg_eu_in.sec",
                nf.format(energyInputValues.avgLong()),
                secInterval));
        info.add(
            translateToLocalFormatted(
                "kekztech.infodata.lapotronic_super_capacitor.avg_eu_out.sec",
                nf.format(energyOutputValues.avgLong()),
                secInterval));
        info.add(
            translateToLocalFormatted(
                "kekztech.infodata.lapotronic_super_capacitor.avg_eu_in.min5",
                nf.format(energyInputValues5m.avgLong())));
        info.add(
            translateToLocalFormatted(
                "kekztech.infodata.lapotronic_super_capacitor.avg_eu_out.min5",
                nf.format(energyOutputValues5m.avgLong())));
        info.add(
            translateToLocalFormatted(
                "kekztech.infodata.lapotronic_super_capacitor.avg_eu_in.hour1",
                nf.format(energyInputValues1h.avgLong())));
        info.add(
            translateToLocalFormatted(
                "kekztech.infodata.lapotronic_super_capacitor.avg_eu_out.hour1",
                nf.format(energyOutputValues1h.avgLong())));

        info.add(getTimeTo());

        info.add(
            translateToLocalFormatted(
                "kekztech.infodata.multi.maintenance_status",
                ((super.getRepairStatus() == super.getIdealStatus())
                    ? EnumChatFormatting.GREEN + translateToLocal("kekztech.infodata.multi.maintenance_status.ok")
                        + EnumChatFormatting.RESET
                    : EnumChatFormatting.RED + translateToLocal("kekztech.infodata.multi.maintenance_status.bad")
                        + EnumChatFormatting.RESET)));
        info.add(
            translateToLocalFormatted(
                "kekztech.infodata.lapotronic_super_capacitor.wireless_mode",
                (wireless_mode
                    ? EnumChatFormatting.GREEN
                        + translateToLocal("kekztech.infodata.lapotronic_super_capacitor.wireless_mode.enabled")
                        + EnumChatFormatting.RESET
                    : EnumChatFormatting.RED
                        + translateToLocal("kekztech.infodata.lapotronic_super_capacitor.wireless_mode.disabled")
                        + EnumChatFormatting.RESET)));
        info.add(
            translateToLocalFormatted(
                "kekztech.infodata.lapotronic_super_capacitor.capacitors",
                GTValues.TIER_COLORS[9] + GTValues.VN[9] + EnumChatFormatting.RESET,
                getUHVCapacitorCount()));
        info.add(
            translateToLocalFormatted(
                "kekztech.infodata.lapotronic_super_capacitor.capacitors",
                GTValues.TIER_COLORS[10] + GTValues.VN[10] + EnumChatFormatting.RESET,
                getUEVCapacitorCount()));
        info.add(
            translateToLocalFormatted(
                "kekztech.infodata.lapotronic_super_capacitor.capacitors",
                GTValues.TIER_COLORS[11] + GTValues.VN[11] + EnumChatFormatting.RESET,
                getUIVCapacitorCount()));
        info.add(
            translateToLocalFormatted(
                "kekztech.infodata.lapotronic_super_capacitor.capacitors",
                GTValues.TIER_COLORS[12] + GTValues.VN[12] + EnumChatFormatting.RESET,
                getUMVCapacitorCount()));
        info.add(
            translateToLocalFormatted(
                "kekztech.infodata.lapotronic_super_capacitor.wireless_eu",
                EnumChatFormatting.RED + nf.format(WirelessNetworkManager.getUserEU(global_energy_user_uuid))));
        info.add(
            translateToLocalFormatted(
                "kekztech.infodata.lapotronic_super_capacitor.wireless_eu",
                EnumChatFormatting.RED + toStandardForm(WirelessNetworkManager.getUserEU(global_energy_user_uuid))));
    }

    // Method to format time in seconds, minutes, days, and years
    private String formatTime(double time, boolean fill) {
        if (time < 1) {
            return "Completely " + (fill ? "full" : "empty");
        } else if (time < 60) {
            return String.format("%.2f seconds", time);
        } else if (time < 3600) {
            return String.format("%.2f minutes", time / 60);
        } else if (time < 86400) {
            return String.format("%.2f hours", time / 3600);
        } else if (time < 31536000) {
            return String.format("%.2f days", time / 86400);
        } else {
            double y = time / 31536000;
            return y < 9_000 ? String.format("%.2f years", y) : "Over9000 years";
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound nbt) {
        nbt = (nbt == null) ? new NBTTagCompound() : nbt;

        nbt.setByteArray("capacity", capacity.toByteArray());
        nbt.setByteArray("stored", stored.toByteArray());
        nbt.setBoolean("wireless_mode", wireless_mode);
        nbt.setInteger("wireless_mode_cooldown", counter);
        nbt.setBoolean("warningReceived", showWarning);
        nbt.setBoolean("hasRebalanced", hasRebalanced);
        nbt.setBoolean("canRebalance", canRebalance);
        super.saveNBTData(nbt);
    }

    @Override
    public void loadNBTData(NBTTagCompound nbt) {
        nbt = (nbt == null) ? new NBTTagCompound() : nbt;

        capacity = new BigInteger(nbt.getByteArray("capacity"));
        stored = new BigInteger(nbt.getByteArray("stored"));
        wireless_mode = nbt.getBoolean("wireless_mode");
        counter = nbt.getInteger("wireless_mode_cooldown");
        showWarning = nbt.getBoolean("warningReceived");
        nbt.getBoolean("hasRebalanced");
        nbt.getBoolean("canRebalance");

        super.loadNBTData(nbt);
    }

    // called by the getEUCapacity() function in BaseMetaTileEntity
    @Override
    public long maxEUStore() {
        return capacity.compareTo(LONG_MAX) > 0 ? Long.MAX_VALUE : capacity.longValue();
    }

    // called by the getEUStored() function in BaseMetaTileEntity
    @Override
    public long getEUVar() {
        return stored.longValue();
    }

    /*
     * all of these are needed for the injectEnergyUnits() and drainEnergyUnits() in IGregTechTileEntity
     */
    @Override
    public long maxEUInput() {
        if (wireless_mode) {
            return Long.MAX_VALUE;
        } else {
            return mMaxEUIn;
        }
    }

    @Override
    public long maxAmperesIn() {
        return 1L;
    }

    @Override
    public long maxEUOutput() {
        if (wireless_mode) {
            return Long.MAX_VALUE;
        } else {
            return mMaxEUOut;
        }
    }

    @Override
    public long maxAmperesOut() {
        return 1L;
    }

    @Override
    public boolean isEnetInput() {
        return true;
    }

    @Override
    public boolean isEnetOutput() {
        return true;
    }

    protected boolean canUseWireless() {
        return wirelessCapableCapacitors() != 0;
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        if (canUseWireless()) {
            wireless_mode = !wireless_mode;
            GTUtility.sendChatToPlayer(aPlayer, "Wireless network mode " + (wireless_mode ? "enabled." : "disabled."));
        } else {
            GTUtility.sendChatToPlayer(
                aPlayer,
                "Wireless mode cannot be enabled without at least 1 " + GTValues.TIER_COLORS[9]
                    + GTValues.VN[9]
                    + EnumChatFormatting.RESET
                    + "+ capacitor.");
            wireless_mode = false;
        }
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public boolean hasRunningText() {
        return false;
    }

    @Override
    public boolean shouldDisplayCheckRecipeResult() {
        return false;
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new MTELapotronicSuperCapacitorgui(this);
    }

    @Override
    public boolean showMachineStatusInGUI() {
        return false;
    }

    @Override
    public boolean showRecipeTextInGUI() {
        return false;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    private enum LSCHatchElement implements IHatchElement<MTELapotronicSuperCapacitor> {

        Energy(MTEHatchEnergyMulti.class, MTEHatchEnergy.class) {

            @Override
            public long count(MTELapotronicSuperCapacitor t) {
                return t.mEnergyHatches.size() + t.mEnergyHatchesTT.size() + t.mEnergyTunnelsTT.size();
            }
        },
        Dynamo(MTEHatchDynamoMulti.class, MTEHatchDynamo.class) {

            @Override
            public long count(MTELapotronicSuperCapacitor t) {
                return t.mDynamoHatches.size() + t.mDynamoHatchesTT.size() + t.mDynamoTunnelsTT.size();
            }
        },;

        private final List<? extends Class<? extends IMetaTileEntity>> mteClasses;

        @SafeVarargs
        LSCHatchElement(Class<? extends IMetaTileEntity>... mteClasses) {
            this.mteClasses = Arrays.asList(mteClasses);
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return mteClasses;
        }

        @Override
        public IGTHatchAdder<? super MTELapotronicSuperCapacitor> adder() {
            return MTELapotronicSuperCapacitor::addBottomHatches;
        }
    }

    private enum CellElement implements IStructureElement<MTELapotronicSuperCapacitor> {

        INSTANCE;

        @Override
        public boolean check(MTELapotronicSuperCapacitor t, World world, int x, int y, int z) {
            Block worldBlock = world.getBlock(x, y, z);
            int meta = worldBlock.getDamageValue(world, x, y, z);
            if (LSC_PART != worldBlock || meta == 0) return false;
            t.capacitors[meta - 1]++;
            return true;
        }

        @Override
        public boolean couldBeValid(MTELapotronicSuperCapacitor mteLapotronicSuperCapacitor, World world, int x, int y,
            int z, ItemStack trigger) {
            Block worldBlock = world.getBlock(x, y, z);
            int meta = worldBlock.getDamageValue(world, x, y, z);
            return LSC_PART == worldBlock && meta != 0;
        }

        private int getHint(ItemStack stack) {
            return switch (Capacitor.VALUES_BY_TIER[GTStructureChannels.LSC_CAPACITOR
                .getValueClamped(stack, 1, Capacitor.VALUES_BY_TIER.length) - 1].getMinimalGlassTier() + 1) {
                // This is necessary for mapping from channel number to the correct capacitor tier
                case 2 -> 7;
                case 3 -> 1;
                case 4 -> 2;
                case 5 -> 3;
                case 6 -> 4;
                case 7 -> 5;
                case 8 -> 8;
                case 9 -> 9;
                case 10 -> 10;
                default -> 6;
            };
        }

        @Override
        public boolean spawnHint(MTELapotronicSuperCapacitor t, World world, int x, int y, int z, ItemStack trigger) {
            StructureLibAPI.hintParticle(world, x, y, z, LSC_PART, getHint(trigger));
            return true;
        }

        @Override
        public BlocksToPlace getBlocksToPlace(MTELapotronicSuperCapacitor mteLapotronicSuperCapacitor, World world,
            int x, int y, int z, ItemStack trigger, AutoPlaceEnvironment env) {
            return BlocksToPlace.create(
                new ItemStack(
                    LSC_PART_ITEM,
                    1,
                    GTStructureChannels.LSC_CAPACITOR.getValueClamped(trigger, 1, Capacitor.VALUES_BY_TIER.length)));
        }

        @Override
        public boolean placeBlock(MTELapotronicSuperCapacitor t, World world, int x, int y, int z, ItemStack trigger) {
            world.setBlock(x, y, z, LSC_PART, getHint(trigger), 3);
            return true;
        }

        @Override
        public PlaceResult survivalPlaceBlock(MTELapotronicSuperCapacitor t, World world, int x, int y, int z,
            ItemStack trigger, AutoPlaceEnvironment env) {
            if (check(t, world, x, y, z)) return PlaceResult.SKIP;
            // glass for LSC can be paired with capacitors up to 3 tiers higher
            int glassTier = GTStructureChannels.BOROGLASS.getValue(trigger) + 2;
            ItemStack targetStack;
            // if user specified a capacitor tier, use it.
            // otherwise scan for any capacitor that can be used
            if (GTStructureChannels.LSC_CAPACITOR.hasValue(trigger)) {
                int capacitorTier = GTStructureChannels.LSC_CAPACITOR
                    .getValueClamped(trigger, 1, Capacitor.VALUES_BY_TIER.length);
                if (Capacitor.VALUES_BY_TIER[capacitorTier - 1].getMinimalGlassTier() > glassTier) {
                    env.getChatter()
                        .accept(new ChatComponentTranslation("kekztech.structure.glass_incompatible"));
                    return PlaceResult.REJECT;
                }
                targetStack = new ItemStack(
                    LSC_PART_ITEM,
                    1,
                    Capacitor.VALUES_BY_TIER[capacitorTier - 1].ordinal() + 1);
                if (!env.getSource()
                    .takeOne(targetStack, true)) return PlaceResult.REJECT;
            } else {
                targetStack = env.getSource()
                    .takeOne(
                        s -> s != null && s.stackSize >= 0
                            && s.getItem() == LSC_PART_ITEM
                            && s.getItemDamage() != 0 // LSC casing, not a capacitor
                            && glassTier >= Capacitor.VALUES[min(s.getItemDamage(), Capacitor.VALUES.length) - 1]
                                .getMinimalGlassTier(),
                        true);
            }
            if (targetStack == null) return PlaceResult.REJECT;
            return StructureUtility.survivalPlaceBlock(
                targetStack,
                NBTMode.EXACT,
                targetStack.stackTagCompound,
                true,
                world,
                x,
                y,
                z,
                env.getSource(),
                env.getActor(),
                env.getChatter());
        }
    }
}

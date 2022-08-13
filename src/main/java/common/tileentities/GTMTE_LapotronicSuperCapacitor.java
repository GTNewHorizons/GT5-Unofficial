package common.tileentities;

import com.github.bartimaeusnek.bartworks.API.BorosilicateGlass;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_DynamoMulti;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_DynamoTunnel;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyMulti;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyTunnel;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import common.Blocks;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.IGlobalWirelessEnergy;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.*;

import static com.google.common.math.LongMath.pow;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static common.itemBlocks.IB_LapotronicEnergyUnit.*;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class GTMTE_LapotronicSuperCapacitor extends GT_MetaTileEntity_EnhancedMultiBlockBase<GTMTE_LapotronicSuperCapacitor> implements IGlobalWirelessEnergy {
    private enum TopState {
		MayBeTop,
		Top,
		NotTop
	}

    private boolean wireless_mode = false;
    private boolean not_processed_lsc = true;
    private long uhv_cap_count = 0;
    private int counter = 1;

    private long max_passive_drain_eu_per_tick_per_uhv_cap = 1_000_000;

    private enum Capacitor {
        IV(2, BigInteger.valueOf(IV_cap_storage)),
        LuV(3, BigInteger.valueOf(LuV_cap_storage)),
        ZPM(4, BigInteger.valueOf(ZPM_cap_storage)),
        UV(5, BigInteger.valueOf(UV_cap_storage)),
        UHV(6, MAX_LONG),
        None(0, BigInteger.ZERO),
        EV(1, BigInteger.valueOf(EV_cap_storage));

        private final int minimalGlassTier;
		private final BigInteger providedCapacity;
		static final Capacitor[] VALUES = values();

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
            for (int index = 0; index < values().length; index++) {
                if (values()[index].getMinimalGlassTier() == glassTier) {
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

	private static final Block LSC_PART = Blocks.lscLapotronicEnergyUnit;
	private static final int CASING_META = 0;
	private static final int CASING_TEXTURE_ID = (42 << 7) | 127;

	private static final IStructureDefinition<GTMTE_LapotronicSuperCapacitor> STRUCTURE_DEFINITION = IStructureDefinition.<GTMTE_LapotronicSuperCapacitor>builder()
			.addShape(STRUCTURE_PIECE_BASE, transpose(new String[][]{
					{"bbbbb", "bbbbb", "bbbbb", "bbbbb", "bbbbb",},
					{"bb~bb", "bbbbb", "bbbbb", "bbbbb", "bbbbb",},
			}))
			.addShape(STRUCTURE_PIECE_LAYER, transpose(new String[][]{
					{"ggggg", "gcccg", "gcccg", "gcccg", "ggggg",},
			}))
			.addShape(STRUCTURE_PIECE_TOP, transpose(new String[][]{
					{"ggggg", "ggggg", "ggggg", "ggggg", "ggggg",},
			}))
			.addShape(STRUCTURE_PIECE_MID, transpose(new String[][]{
					{"ggggg", "gCCCg", "gCCCg", "gCCCg", "ggggg",},
			}))
			.addElement('b', ofChain(
					ofHatchAdder(GTMTE_LapotronicSuperCapacitor::addBottomHatches, CASING_TEXTURE_ID, 1),
					onElementPass(te -> te.casingAmount++, ofBlock(LSC_PART, CASING_META))
			))
			.addElement('g', BorosilicateGlass.ofBoroGlass((byte) -1, (te, t) -> te.glassTier = t, te -> te.glassTier))
			.addElement('c', ofChain(
					onlyIf(te -> te.topState != TopState.NotTop, onElementPass(te -> te.topState = TopState.Top, BorosilicateGlass.ofBoroGlass((byte) -1, (te, t) -> te.glassTier = t, te -> te.glassTier))),
					onlyIf(te -> te.topState != TopState.Top, onElementPass(te -> te.topState = TopState.NotTop,
							ofBlockAdder(GTMTE_LapotronicSuperCapacitor::addStorageCell, LSC_PART, 1)
					))
			))
			.addElement('C', ofBlock(LSC_PART, 1))
			.build();

	private static final BigInteger MAX_LONG = BigInteger.valueOf(Long.MAX_VALUE);

	private final Set<GT_MetaTileEntity_Hatch_EnergyMulti> mEnergyHatchesTT = new HashSet<>();
	private final Set<GT_MetaTileEntity_Hatch_DynamoMulti> mDynamoHatchesTT = new HashSet<>();
	private final Set<GT_MetaTileEntity_Hatch_EnergyTunnel> mEnergyTunnelsTT = new HashSet<>();
	private final Set<GT_MetaTileEntity_Hatch_DynamoTunnel> mDynamoTunnelsTT = new HashSet<>();
    /**
     * Count the amount of capacitors of each tier in each slot.
     * Index = meta - 1
     */
	private final int[] capacitors = new int[7];
	private BigInteger capacity = BigInteger.ZERO;
	private BigInteger stored = BigInteger.ZERO;
	private long passiveDischargeAmount = 0;
	private BigInteger inputLastTick = BigInteger.ZERO;
	private BigInteger outputLastTick = BigInteger.ZERO;
	private int repairStatusCache = 0;

	private byte glassTier = -1;
	private int casingAmount = 0;
	private TopState topState = TopState.MayBeTop;

	private long mMaxEUIn = 0;
	private long mMaxEUOut = 0;

	public GTMTE_LapotronicSuperCapacitor(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GTMTE_LapotronicSuperCapacitor(String aName) {
		super(aName);
	}

	@Override
	public IMetaTileEntity newMetaEntity(IGregTechTileEntity var1) {
		return new GTMTE_LapotronicSuperCapacitor(super.mName);
	}

	@Override
	public IStructureDefinition<GTMTE_LapotronicSuperCapacitor> getStructureDefinition() {
		return STRUCTURE_DEFINITION;
	}

	@Override
	protected IAlignmentLimits getInitialAlignmentLimits() {
		return (d, r, f) -> d.offsetY == 0 && r.isNotRotated() && !f.isVerticallyFliped();
	}

	private void processInputHatch(GT_MetaTileEntity_Hatch aHatch, int aBaseCasingIndex) {
		mMaxEUIn += aHatch.maxEUInput() * aHatch.maxAmperesIn();
		aHatch.updateTexture(aBaseCasingIndex);
	}

	private void processOutputHatch(GT_MetaTileEntity_Hatch aHatch, int aBaseCasingIndex) {
		mMaxEUOut += aHatch.maxEUOutput() * aHatch.maxAmperesOut();
		aHatch.updateTexture(aBaseCasingIndex);
	}

	private boolean addBottomHatches(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
		if (aTileEntity == null || aTileEntity.isDead()) return false;
		IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
		if (!(aMetaTileEntity instanceof GT_MetaTileEntity_Hatch)) return false;
		if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance) {
			((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
			return GTMTE_LapotronicSuperCapacitor.this.mMaintenanceHatches.add((GT_MetaTileEntity_Hatch_Maintenance) aMetaTileEntity);
		} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy) {
			// Add GT hatches
			final GT_MetaTileEntity_Hatch_Energy tHatch = ((GT_MetaTileEntity_Hatch_Energy) aMetaTileEntity);
			processInputHatch(tHatch, aBaseCasingIndex);
			return mEnergyHatches.add(tHatch);
		} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_EnergyTunnel) {
			// Add TT Laser hatches
			final GT_MetaTileEntity_Hatch_EnergyTunnel tHatch = ((GT_MetaTileEntity_Hatch_EnergyTunnel) aMetaTileEntity);
			processInputHatch(tHatch, aBaseCasingIndex);
			return mEnergyTunnelsTT.add(tHatch);
		} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_EnergyMulti) {
			// Add TT hatches
			final GT_MetaTileEntity_Hatch_EnergyMulti tHatch = (GT_MetaTileEntity_Hatch_EnergyMulti) aMetaTileEntity;
			processInputHatch(tHatch, aBaseCasingIndex);
			return mEnergyHatchesTT.add(tHatch);
		} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Dynamo) {
			// Add GT hatches
			final GT_MetaTileEntity_Hatch_Dynamo tDynamo = (GT_MetaTileEntity_Hatch_Dynamo) aMetaTileEntity;
			processOutputHatch(tDynamo, aBaseCasingIndex);
			return mDynamoHatches.add(tDynamo);
		} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_DynamoTunnel) {
			// Add TT Laser hatches
			final GT_MetaTileEntity_Hatch_DynamoTunnel tDynamo = (GT_MetaTileEntity_Hatch_DynamoTunnel) aMetaTileEntity;
			processOutputHatch(tDynamo, aBaseCasingIndex);
			return mDynamoTunnelsTT.add(tDynamo);
		} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_DynamoMulti) {
			// Add TT hatches
			final GT_MetaTileEntity_Hatch_DynamoMulti tDynamo = (GT_MetaTileEntity_Hatch_DynamoMulti) aMetaTileEntity;
			processOutputHatch(tDynamo, aBaseCasingIndex);
			return mDynamoHatchesTT.add(tDynamo);
		}
		return false;
	}

	private boolean addStorageCell(Block block, int meta) {
		if (block != LSC_PART || meta == 0) return false;
		capacitors[meta - 1]++;

        if (meta == 5) {
            uhv_cap_count++;
        }
        return true;
	}

	@Override
	protected GT_Multiblock_Tooltip_Builder createTooltip() {
		final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
		tt.addMachineType("Energy Storage")
            .addInfo("Loses energy equal to 1% of the total capacity every 24 hours. Capped")
            .addInfo("at " + EnumChatFormatting.RED + GT_Utility.formatNumbers(max_passive_drain_eu_per_tick_per_uhv_cap) + EnumChatFormatting.GRAY + "EU/t passive loss per " + GT_Values.TIER_COLORS[9] + GT_Values.VN[9] + EnumChatFormatting.GRAY + " capacitor.")
            .addSeparator()
            .addInfo("Glass shell has to be Tier - 3 of the highest capacitor tier.")
            .addInfo(GT_Values.TIER_COLORS[8] + GT_Values.VN[8] + EnumChatFormatting.GRAY + "-tier glass required for " + EnumChatFormatting.BLUE + "Tec" + EnumChatFormatting.DARK_BLUE + "Tech" + EnumChatFormatting.GRAY + " Laser Hatches.")
            .addInfo("Add more or better capacitors to increase capacity.")
            .addSeparator()
            .addInfo("Wireless mode can be enabled by right clicking with a screwdriver.")
            .addInfo("This mode can only be enabled if you have a "+ GT_Values.TIER_COLORS[9] + GT_Values.VN[9] + EnumChatFormatting.GRAY + " capacitor in the multiblock.")
            .addInfo("When enabled every " + EnumChatFormatting.BLUE + GT_Utility.formatNumbers(LSC_time_between_wireless_rebalance_in_ticks) + EnumChatFormatting.GRAY + " ticks the LSC will attempt to re-balance against your")
            .addInfo("wireless EU network. If there is less than " + EnumChatFormatting.RED + GT_Utility.formatNumbers(LSC_wireless_eu_cap) + EnumChatFormatting.GRAY + "EU in the LSC")
            .addInfo("it will withdraw from the network and add to the LSC. If there is more it will add")
            .addInfo("the EU to the network and remove it from the LSC.")
            .addSeparator()
            .beginVariableStructureBlock(5, 5, 4, 18, 5, 5, false)
		.addStructureInfo("Modular height of 4-18 blocks.")
		.addController("Front center bottom")
		.addOtherStructurePart("Lapotronic Super Capacitor Casing", "5x2x5 base (at least 17x)")
		.addOtherStructurePart("Lapotronic Capacitor (" + GT_Values.TIER_COLORS[4] + GT_Values.VN[4] + EnumChatFormatting.GRAY + "-" + GT_Values.TIER_COLORS[8] + GT_Values.VN[8] + EnumChatFormatting.GRAY + "), Ultimate Capacitor (" + GT_Values.TIER_COLORS[9] + GT_Values.VN[9] + EnumChatFormatting.GRAY + ")", "Center 3x(1-15)x3 above base (9-135 blocks)")
		.addStructureInfo("You can also use the Empty Capacitor to save materials if you use it for less than half the blocks")
		.addOtherStructurePart("Borosilicate Glass (any)", "41-265x, Encase capacitor pillar")
		.addEnergyHatch("Any casing")
		.addDynamoHatch("Any casing")
		.addOtherStructurePart("Laser Target/Source Hatches", "Any casing, must be using " + GT_Values.TIER_COLORS[8] + GT_Values.VN[8] + EnumChatFormatting.GRAY + "-tier glass")
		.addStructureInfo("You can have several I/O Hatches")
		.addMaintenanceHatch("Any casing")
		.toolTipFinisher("KekzTech");
		return tt;
	}

	@Override
	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex,
			boolean aActive, boolean aRedstone) {
		ITexture[] sTexture = new ITexture[]{TextureFactory.of(BlockIcons.MACHINE_CASING_FUSION_GLASS,
				Dyes.getModulation(-1, Dyes._NULL.mRGBa))};
		if (aSide == aFacing && aActive) {
			sTexture = new ITexture[]{TextureFactory.of(BlockIcons.MACHINE_CASING_FUSION_GLASS_YELLOW,
					Dyes.getModulation(-1, Dyes._NULL.mRGBa))};
		}
		return sTexture;
	}

	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(),
				"MultiblockDisplay.png");
	}

    private String global_energy_user_uuid;

    @Override
    public void onPreTick(IGregTechTileEntity tileEntity, long aTick) {
        super.onPreTick(tileEntity, aTick);

        // On first tick (aTick restarts from 0 upon world reload).
        if (not_processed_lsc && tileEntity.isServerSide()) {
            // Add user to wireless network.
            loadGlobalEnergyInfo(tileEntity.getWorld());
            strongCheckOrAddUser(tileEntity.getOwnerUuid(), tileEntity.getOwnerName());

            // Get team UUID.
            global_energy_user_uuid = getUUIDFromUsername(tileEntity.getOwnerName());

            not_processed_lsc = false;
        }
    }

	@Override
	public boolean isCorrectMachinePart(ItemStack stack) {
		return true;
	}

	@Override
	public boolean checkRecipe(ItemStack stack) {
		this.mProgresstime = 1;
		this.mMaxProgresstime = 1;
		this.mEUt = 0;
		this.mEfficiencyIncrease = 10000;
		return true;
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity thisController, ItemStack guiSlotItem) {
        uhv_cap_count = 0;

        strongCheckOrAddUser(thisController.getOwnerUuid(), thisController.getOwnerName());

		// Reset capacitor counts
		Arrays.fill(capacitors, 0);
		// Clear TT hatches
		mEnergyHatchesTT.clear();
		mDynamoHatchesTT.clear();
		mEnergyTunnelsTT.clear();
		mDynamoTunnelsTT.clear();

		mMaxEUIn = 0;
		mMaxEUOut = 0;

		if (!checkPiece(STRUCTURE_PIECE_BASE, 2, 1, 0))
			return false;

		topState = TopState.NotTop; // need at least one layer of capacitor to form, obviously
		int layer = 2;
		while (true) {
			if (!checkPiece(STRUCTURE_PIECE_LAYER, 2, layer, 0))
				return false;
			layer ++;
			if (topState == TopState.Top)
				break; // top found, break out
			topState = TopState.MayBeTop;
			if (layer > 18)
				return false; // too many layers
		}

		// Make sure glass tier is T-2 of the highest tier capacitor in the structure
		// Count down from the highest tier until an entry is found
		// Borosilicate glass after 5 are just recolours of 0
		for (int highestGlassTier = capacitors.length - 1; highestGlassTier >= 0; highestGlassTier--) {
            int highestCapacitor = Capacitor.getIndexFromGlassTier(highestGlassTier);
			if (capacitors[highestCapacitor] > 0) {
				if (Capacitor.VALUES[highestCapacitor].getMinimalGlassTier() > glassTier)
					return false;
				break;
			}
		}

		// Glass has to be at least UV-tier to allow TT Laser hatches
		if (glassTier < 8) {
			if(mEnergyTunnelsTT.size() > 0 || mDynamoTunnelsTT.size() > 0)
				return false;
		}

		//Check if enough (more than 50%) non-empty caps
		if (capacitors[5] > capacitors[0] + capacitors[1] + capacitors[2] + capacitors[3] + capacitors[4] + capacitors[6])
			return false;

		// Calculate total capacity
		capacity = BigInteger.ZERO;
		for(int i = 0; i < capacitors.length; i++) {
			int count = capacitors[i];
			capacity = capacity.add(Capacitor.VALUES[i].getProvidedCapacity().multiply(BigInteger.valueOf(count)));
		}
		// Calculate how much energy to void each tick
		passiveDischargeAmount = recalculateLossWithMaintenance(getRepairStatus());
		return mMaintenanceHatches.size() == 1;
	}

	@Override
	public void construct(ItemStack stackSize, boolean hintsOnly) {
		int layer = min(stackSize.stackSize + 3, 18);
		buildPiece(STRUCTURE_PIECE_BASE, stackSize, hintsOnly, 2, 1, 0);
		for (int i = 2; i < layer - 1; i++)
			buildPiece(STRUCTURE_PIECE_MID, stackSize, hintsOnly, 2, i, 0);
		buildPiece(STRUCTURE_PIECE_TOP, stackSize, hintsOnly, 2, layer - 1, 0);
	}

	@Override
	public boolean onRunningTick(ItemStack stack){
		// Reset I/O cache
		inputLastTick = BigInteger.ZERO;
		outputLastTick = BigInteger.ZERO;

        long temp_stored = 0L;
        long temp_inputLastTick = 0L;
        long temp_outputLastTick = 0L;

		// Draw energy from GT hatches
		for(GT_MetaTileEntity_Hatch_Energy eHatch : super.mEnergyHatches) {
			if(eHatch == null || eHatch.getBaseMetaTileEntity().isInvalidTileEntity()) {
				continue;
			}
			final long power = getPowerToDraw(eHatch.maxEUInput() * eHatch.maxAmperesIn());
			if(eHatch.getEUVar() >= power) {
				eHatch.setEUVar(eHatch.getEUVar() - power);
                temp_stored += power;
                temp_inputLastTick += power;
			}
		}

		// Output energy to GT hatches
		for(GT_MetaTileEntity_Hatch_Dynamo eDynamo : super.mDynamoHatches){
			if(eDynamo == null || eDynamo.getBaseMetaTileEntity().isInvalidTileEntity()){
				continue;
			}
			final long power = getPowerToPush(eDynamo.maxEUOutput() * eDynamo.maxAmperesOut());
			if(power <= eDynamo.maxEUStore() - eDynamo.getEUVar()) {
				eDynamo.setEUVar(eDynamo.getEUVar() + power);
                temp_stored -= power;
                temp_outputLastTick += power;
			}
		}

		// Draw energy from TT hatches
		for(GT_MetaTileEntity_Hatch_EnergyMulti eHatch : mEnergyHatchesTT) {
			if(eHatch == null || eHatch.getBaseMetaTileEntity().isInvalidTileEntity()) {
				continue;
			}
			final long power = getPowerToDraw(eHatch.maxEUInput() * eHatch.maxAmperesIn());
			if(eHatch.getEUVar() >= power) {
				eHatch.setEUVar(eHatch.getEUVar() - power);
                temp_stored += power;
                temp_inputLastTick += power;
			}
		}

		// Output energy to TT hatches
		for(GT_MetaTileEntity_Hatch_DynamoMulti eDynamo : mDynamoHatchesTT){
			if(eDynamo == null || eDynamo.getBaseMetaTileEntity().isInvalidTileEntity()){
				continue;
			}
			final long power = getPowerToPush(eDynamo.maxEUOutput() * eDynamo.maxAmperesOut());
			if(power <= eDynamo.maxEUStore() - eDynamo.getEUVar()) {
				eDynamo.setEUVar(eDynamo.getEUVar() + power);
                temp_stored -= power;
                temp_outputLastTick += power;
			}
		}

		// Draw energy from TT Laser hatches
		for(GT_MetaTileEntity_Hatch_EnergyTunnel eHatch : mEnergyTunnelsTT) {
			if(eHatch == null || eHatch.getBaseMetaTileEntity().isInvalidTileEntity()) {
				continue;
			}
			final long ttLaserWattage = eHatch.maxEUInput() * eHatch.Amperes - (eHatch.Amperes / 20);
			final long power = getPowerToDraw(ttLaserWattage);
			if(eHatch.getEUVar() >= power) {
				eHatch.setEUVar(eHatch.getEUVar() - power);
                temp_stored += power;
                temp_inputLastTick += power;
			}
		}

		// Output energy to TT Laser hatches
		for(GT_MetaTileEntity_Hatch_DynamoTunnel eDynamo : mDynamoTunnelsTT){
			if(eDynamo == null || eDynamo.getBaseMetaTileEntity().isInvalidTileEntity()){
				continue;
			}
			final long ttLaserWattage = eDynamo.maxEUOutput() * eDynamo.Amperes - (eDynamo.Amperes / 20);
			final long power = getPowerToPush(ttLaserWattage);
			if(power <= eDynamo.maxEUStore() - eDynamo.getEUVar()) {
				eDynamo.setEUVar(eDynamo.getEUVar() + power);
                temp_stored -= power;
                temp_outputLastTick += power;
			}
		}

		// Lose some energy.
		// Re-calculate if the repair status changed.
		if(super.getRepairStatus() != repairStatusCache) {
			passiveDischargeAmount = recalculateLossWithMaintenance(super.getRepairStatus());
		}

        // This will break if you transfer more than 2^63 EU/t, so don't do that. Thanks <3
		temp_stored -= passiveDischargeAmount;
        stored = stored.add(BigInteger.valueOf(temp_stored));

        // Check that the machine has positive EU stored.
		stored = (stored.compareTo(BigInteger.ZERO) <= 0) ? BigInteger.ZERO : stored;

		IGregTechTileEntity tBMTE = this.getBaseMetaTileEntity();

		tBMTE.injectEnergyUnits((byte)ForgeDirection.UNKNOWN.ordinal(), inputLastTick.longValue(), 1L);
		tBMTE.drainEnergyUnits((byte)ForgeDirection.UNKNOWN.ordinal(), outputLastTick.longValue(), 1L);

        if (uhv_cap_count <= 0) {
            wireless_mode = false;
        }

        // Every LSC_time_between_wireless_rebalance_in_ticks check against wireless network for re-balancing.
        counter++;
        if (wireless_mode && (counter == LSC_time_between_wireless_rebalance_in_ticks)) {

            // Reset tick counter.
            counter = 1;

            // Find difference.
            BigInteger transferred_eu = stored.subtract(LSC_wireless_eu_cap.multiply(BigInteger.valueOf(uhv_cap_count)));

            if (transferred_eu.signum() == 1) {
                temp_inputLastTick += transferred_eu.longValue();
            } else {
                temp_outputLastTick += transferred_eu.longValue();
            }

            // If that difference can be added then do so.
            if (addEUToGlobalEnergyMap(global_energy_user_uuid, transferred_eu)) {
                // If it succeeds there was sufficient energy so set the internal capacity as such.
                stored = LSC_wireless_eu_cap.multiply(BigInteger.valueOf(uhv_cap_count));
            }
        }

        inputLastTick = BigInteger.valueOf(temp_inputLastTick);
        outputLastTick = BigInteger.valueOf(temp_outputLastTick);

		return true;
	}

	/**
	 * To be called whenever the maintenance status changes or the capacity was recalculated
	 * @param repairStatus
	 * 		This machine's repair status
	 * @return new BigInteger instance for passiveDischargeAmount
	 */
	private long recalculateLossWithMaintenance(int repairStatus) {
		repairStatusCache = repairStatus;

        // This cannot overflow because there is a 135 capacitor maximum per LSC.
        long temp_capacity_divided = capacity.divide(BigInteger.valueOf(100L * 86400L * 20L)).longValue();

        // Passive loss is multiplied by number of UHV caps. Minimum of 1 otherwise loss is 0 for non-UHV caps calculations.
        long uhv_cap_multiplier = min(temp_capacity_divided, max_passive_drain_eu_per_tick_per_uhv_cap * max(1, uhv_cap_count));

        // Passive loss is multiplied by number of maintenance issues.
        long total_passive_loss = uhv_cap_multiplier * (getIdealStatus() - repairStatus + 1);


        // Maximum of 100,000 EU/t drained per UHV cell. The logic is 1% of EU capacity should be drained every 86400 seconds (1 day).
        return total_passive_loss;
	}

	/**
	 * Calculate how much EU to draw from an Energy Hatch
	 * @param hatchWatts
	 * 		Hatch amperage * voltage
	 * @return EU amount
	 */
	private long getPowerToDraw(long hatchWatts){
		final BigInteger remcapActual = capacity.subtract(stored);
		final BigInteger recampLimited = (MAX_LONG.compareTo(remcapActual) > 0) ? remcapActual : MAX_LONG;
		return min(hatchWatts, recampLimited.longValue());
	}

	/**
	 * Calculate how much EU to push into a Dynamo Hatch
	 * @param hatchWatts
	 * 		Hatch amperage * voltage
	 * @return EU amount
	 */
	private long getPowerToPush(long hatchWatts){
		final BigInteger remStoredLimited = (MAX_LONG.compareTo(stored) > 0) ? stored : MAX_LONG;
		return min(hatchWatts, remStoredLimited.longValue());
	}

	@Override
	public String[] getInfoData() {
		final IGregTechTileEntity tGTTE = getBaseMetaTileEntity();

		final ArrayList<String> ll = new ArrayList<>();
		ll.add(EnumChatFormatting.YELLOW + "Operational Data:" + EnumChatFormatting.RESET);
		ll.add("Used Capacity: " + NumberFormat.getNumberInstance().format(stored) + "EU");
		ll.add("Total Capacity: " + NumberFormat.getNumberInstance().format(capacity) + "EU");
		ll.add("Passive Loss: " + NumberFormat.getNumberInstance().format(passiveDischargeAmount) + "EU/t");
		ll.add("EU IN: " + NumberFormat.getNumberInstance().format(inputLastTick) + "EU/t");
		ll.add("EU OUT: " + NumberFormat.getNumberInstance().format(outputLastTick) + "EU/t");
		ll.add("Avg EU IN: " + NumberFormat.getNumberInstance().format(tGTTE.getAverageElectricInput()));
		ll.add("Avg EU OUT: " + NumberFormat.getNumberInstance().format(tGTTE.getAverageElectricOutput()));
		ll.add("Maintenance Status: " + ((super.getRepairStatus() == super.getIdealStatus())
				? EnumChatFormatting.GREEN + "Working perfectly" + EnumChatFormatting.RESET
				: EnumChatFormatting.RED + "Has Problems" + EnumChatFormatting.RESET));
        ll.add("Wireless mode: " + (wireless_mode
            ? EnumChatFormatting.GREEN + "enabled" + EnumChatFormatting.RESET
            : EnumChatFormatting.RED + "disabled" + EnumChatFormatting.RESET));
        ll.add(GT_Values.TIER_COLORS[9] + GT_Values.VN[9] + EnumChatFormatting.RESET + " Capacitors detected: " + uhv_cap_count);
        ll.add("---------------------------------------------");

		final String[] a = new String[ll.size()];
		return ll.toArray(a);
	}

	@Override
	public void saveNBTData(NBTTagCompound nbt) {
		nbt = (nbt == null) ? new NBTTagCompound() : nbt;

		nbt.setByteArray("capacity", capacity.toByteArray());
		nbt.setByteArray("stored", stored.toByteArray());
        nbt.setBoolean("wireless_mode", wireless_mode);

        super.saveNBTData(nbt);
	}

	@Override
	public void loadNBTData(NBTTagCompound nbt) {
		nbt = (nbt == null) ? new NBTTagCompound() : nbt;

		capacity = new BigInteger(nbt.getByteArray("capacity"));
		stored = new BigInteger(nbt.getByteArray("stored"));
        wireless_mode = nbt.getBoolean("wireless_mode");

        super.loadNBTData(nbt);
	}

	@Override
	public boolean isGivingInformation() {
		return true;
	}

	@Override
	public int getMaxEfficiency(ItemStack stack) { return 10000; }

	@Override
	public int getPollutionPerTick(ItemStack stack) { return 0; }

	@Override
	public int getDamageToComponent(ItemStack stack) { return 0; }

	@Override
	public boolean explodesOnComponentBreak(ItemStack stack) { return false; }

	//called by the getEUCapacity() function in BaseMetaTileEntity
	@Override
	public long maxEUStore()
	{
		return capacity.longValue();
	}

	//called by the getEUStored() function in BaseMetaTileEntity
	@Override
	public long getEUVar()
	{
		return stored.longValue();
	}

	/*  all of these are needed for the injectEnergyUnits() and drainEnergyUnits()
		in IGregTechTileEntity
	 */
	@Override
	public long maxEUInput()
	{
		return mMaxEUIn;
	}

	@Override
	public long maxAmperesIn()
	{
		return 1L;
	}

	@Override
	public long maxEUOutput()
	{
		return mMaxEUOut;
	}

	@Override
	public long maxAmperesOut()
	{
		return 1L;
	}

	@Override
	public boolean isEnetInput()
	{
		return true;
	}

	@Override
	public boolean isEnetOutput()
	{
		return true;
	}

    @Override
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (uhv_cap_count != 0) {
            wireless_mode = !wireless_mode;
            GT_Utility.sendChatToPlayer(aPlayer, "Wireless network mode " + (wireless_mode ? "enabled." : "disabled."));
        } else {
            GT_Utility.sendChatToPlayer(aPlayer, "Wireless mode cannot be enabled without at least 1 " + GT_Values.TIER_COLORS[9] + GT_Values.VN[9] + EnumChatFormatting.RESET + " capacitor.");
            wireless_mode = false;
        }
    }
}

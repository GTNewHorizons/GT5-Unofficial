package common.tileentities;

import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_DynamoMulti;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_DynamoTunnel;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyMulti;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyTunnel;
import common.Blocks;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Dynamo;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.input.Keyboard;
import util.Vector3i;
import util.Vector3ic;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class GTMTE_LapotronicSuperCapacitor extends GT_MetaTileEntity_MultiBlockBase {
	
	private final static String glassNameBorosilicate = "BW_GlasBlocks";
	private static final Block LSC_PART = Blocks.lscLapotronicEnergyUnit;
	private static final int CASING_META = 0;
	private static final int CASING_TEXTURE_ID = 62;

	private static final BigInteger MAX_LONG = BigInteger.valueOf(Long.MAX_VALUE);
	private static final BigDecimal PASSIVE_DISCHARGE_FACTOR_PER_TICK =
			BigDecimal.valueOf(0.01D / 1728000.0D); // The magic number is ticks per 24 hours

	private final Set<GT_MetaTileEntity_Hatch_EnergyMulti> mEnergyHatchesTT = new HashSet<>();
	private final Set<GT_MetaTileEntity_Hatch_DynamoMulti> mDynamoHatchesTT = new HashSet<>();
	private final Set<GT_MetaTileEntity_Hatch_EnergyTunnel> mEnergyTunnelsTT = new HashSet<>();
	private final Set<GT_MetaTileEntity_Hatch_DynamoTunnel> mDynamoTunnelsTT = new HashSet<>();
	// Count the amount of capacitors of each tier in each slot (translate with meta - 1)
	private final int[] capacitors = new int[7];
	private BigInteger capacity = BigInteger.ZERO;
	private BigInteger stored = BigInteger.ZERO;
	private BigInteger passiveDischargeAmount = BigInteger.ZERO;
	private BigInteger inputLastTick = BigInteger.ZERO;
	private BigInteger outputLastTick = BigInteger.ZERO;
	private int repairStatusCache = 0;

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
	public String[] getDescription() {
		final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
		tt.addMachineType("Battery Buffer")
		.addInfo("Power storage structure. Does not charge batteries or tools, however.")
		.addInfo("Loses energy equal to 1% of the total capacity every 24 hours.")
		.addInfo("Exception: Ultimate Capacitors only count as Lapotronic Capacitors (UV) for the")
		.addInfo("purposes of passive loss calculation. The full capacity is counted towards the actual power capacity.")
		.addSeparator()
		.addInfo("Glass shell has to be Tier - 2 of the highest capacitor tier")
		.addInfo("UV-tier glass required for TecTech Laser Hatches")
		.addInfo("Add more or better capacitors to increase capacity")
		.addSeparator()
		.beginStructureBlock(5, 4, 5, false)
		.addStructureInfo("Modular height of 4-18 blocks.")
		.addController("Front center bottom")
		.addOtherStructurePart("Lapotronic Super Capacitor Casing", "5x2x5 base (at least 17x)")
		.addOtherStructurePart("Lapotronic Capacitor (EV-UV), Ultimate Capacitor (UHV)", "Center 3x(1-15)x3 above base (9-135 blocks)")
		.addStructureInfo("You can also use the Empty Capacitor to save materials if you use it for less than half the blocks")
		.addOtherStructurePart("Borosilicate Glass (any)", "41-265x, Encase capacitor pillar")
		.addEnergyHatch("Any casing")
		.addDynamoHatch("Any casing")
		.addOtherStructurePart("Laser Target/Source Hatches", "Any casing, must be using UV-tier glass")
		.addStructureInfo("You can have several I/O Hatches")
		.addMaintenanceHatch("Any casing")
		.toolTipFinisher("KekzTech");
		if(!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			return tt.getInformation();
		} else {
			return tt.getStructureInformation();
		}
	}
	
	@Override
	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex,
			boolean aActive, boolean aRedstone) {
		ITexture[] sTexture = new ITexture[]{new GT_RenderedTexture(BlockIcons.MACHINE_CASING_FUSION_GLASS,
				Dyes.getModulation(-1, Dyes._NULL.mRGBa))};
		if (aSide == aFacing && aActive) {
			sTexture = new ITexture[]{new GT_RenderedTexture(BlockIcons.MACHINE_CASING_FUSION_GLASS_YELLOW,
					Dyes.getModulation(-1, Dyes._NULL.mRGBa))};
		}
		return sTexture;
	}
	
	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(),
				"MultiblockDisplay.png");
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

	public Vector3ic rotateOffsetVector(Vector3ic forgeDirection, int x, int y, int z) {
		final Vector3i offset = new Vector3i();

		// either direction on z-axis
		if (forgeDirection.x() == 0 && forgeDirection.z() == -1) {
			offset.x = x;
			offset.y = y;
			offset.z = z;
		}
		if (forgeDirection.x() == 0 && forgeDirection.z() == 1) {
			offset.x = -x;
			offset.y = y;
			offset.z = -z;
		}
		// either direction on x-axis
		if (forgeDirection.x() == -1 && forgeDirection.z() == 0) {
			offset.x = z;
			offset.y = y;
			offset.z = -x;
		}
		if (forgeDirection.x() == 1 && forgeDirection.z() == 0) {
			offset.x = -z;
			offset.y = y;
			offset.z = x;
		}
		// either direction on y-axis
		if (forgeDirection.y() == -1) {
			offset.x = x;
			offset.y = z;
			offset.z = y;
		}

		return offset;
	}
	
	@Override
	public boolean checkMachine(IGregTechTileEntity thisController, ItemStack guiSlotItem) {
		// Figure out the vector for the direction the back face of the controller is facing
		final Vector3ic forgeDirection = new Vector3i(
				ForgeDirection.getOrientation(thisController.getBackFacing()).offsetX,
				ForgeDirection.getOrientation(thisController.getBackFacing()).offsetY,
				ForgeDirection.getOrientation(thisController.getBackFacing()).offsetZ
				);
		boolean formationChecklist = true;
		int minCasingAmount = 16;
		int firstGlassMeta = -1;
		// Reset capacitor counts
		Arrays.fill(capacitors, 0);
		// Clear TT hatches
		mEnergyHatchesTT.clear();
		mDynamoHatchesTT.clear();
		mEnergyTunnelsTT.clear();
		mDynamoTunnelsTT.clear();

		mMaxEUIn = 0;
		mMaxEUOut = 0;
		// Temp var for loss calculation
		BigInteger tempCapacity = BigInteger.ZERO;

		// Capacitor base
		for(int Y = 0; Y <= 1; Y++) {
			for(int X = -2; X <= 2; X++) {
				for(int Z = 0; Z >= -4; Z--) {
					if(X == 0 && Y == 0 && Z == 0) {
						continue; // Skip controller
					}
					
					final Vector3ic offset = rotateOffsetVector(forgeDirection, X, Y, Z);
					final IGregTechTileEntity currentTE = thisController.getIGregTechTileEntityOffset(offset.x(), offset.y(), offset.z());
					
					// Tries to add TE as either of those kinds of hatches.
					// The number is the texture index number for the texture that needs to be painted over the hatch texture
					if (   !super.addMaintenanceToMachineList(currentTE, CASING_TEXTURE_ID)
						&& !this.addEnergyInputToMachineList(currentTE, CASING_TEXTURE_ID)
						&& !this.addDynamoToMachineList(currentTE, CASING_TEXTURE_ID)) {
						
						// If it's not a hatch, is it the right casing for this machine? Check block and block meta.
						if ((thisController.getBlockOffset(offset.x(), offset.y(), offset.z()) == LSC_PART) 
								&& (thisController.getMetaIDOffset(offset.x(), offset.y(), offset.z()) == CASING_META)) {
							// Seems to be valid casing. Decrement counter.
							minCasingAmount--;
						} else {
							formationChecklist = false;
						}
					}
				}
			}
		}
		// Capacitor units
		int firstGlassHeight = 3; // Initialize at basic height (-1 because it's an offset)
		for(int X = -1; X <= 1; X++) {
			for(int Z = -1; Z >= -3; Z--) {
				// Y has to be the innermost loop to properly deal with the dynamic height.
				// This way each "pillar" of capacitors is checked from bottom to top until it hits glass.
				for(int Y = 2; Y <= 17; Y++) {
					final Vector3ic offset = rotateOffsetVector(forgeDirection, X, Y, Z);

					final int meta = thisController.getMetaIDOffset(offset.x(), offset.y(), offset.z());
					if(thisController.getBlockOffset(offset.x(), offset.y(), offset.z()) == LSC_PART && (meta > 0)) {
						// Add capacity
						tempCapacity = calculateTempCapacity(tempCapacity, meta);
						capacitors[meta - 1]++;
					} else if(thisController.getBlockOffset(offset.x(), offset.y(), offset.z()).getUnlocalizedName().equals(glassNameBorosilicate)){
						firstGlassHeight = Y;
						break;
					} else {
						formationChecklist = false;
					}
				}
			}
		}
		// Glass shell
		// Make Y the outermost loop, so each layer is checked completely before moving up
		for(int Y = 2; Y <= firstGlassHeight; Y++) {
			for(int X = -2; X <= 2; X++) {
				for(int Z = 0; Z >= -4; Z--) {
					final Vector3ic offset = rotateOffsetVector(forgeDirection, X, Y, Z);
					final String blockNameAt = thisController.getBlockOffset(offset.x(), offset.y(), offset.z()).getUnlocalizedName();
					final int meta = thisController.getMetaIDOffset(offset.x(), offset.y(), offset.z());
					// Check only outer ring, except when on roof height
					if((Y < firstGlassHeight)){
						if(X == -2 || X == 2 || Z == 0 || Z == 4){
							if(glassNameBorosilicate.equals(blockNameAt)) {
								if(firstGlassMeta == -1) {
									firstGlassMeta = meta;
								} else if(meta != firstGlassMeta) {
									formationChecklist = false;
								}
							} else {
								formationChecklist = false;
							}
						}
					} else {
						if (glassNameBorosilicate.equals(blockNameAt)) {
							if(meta != firstGlassMeta) {
								formationChecklist = false;
							}
						} else {
							formationChecklist = false;
						}
					}
				}
			}
		}

		if(minCasingAmount > 0) {
			formationChecklist = false;
		}

		// Make sure glass tier is T-2 of the highest tier capacitor in the structure
		// Count down from the highest tier until an entry is found
		// Borosilicate glass after 5 are just recolours of 0
		final int colourCorrectedMeta = firstGlassMeta > 5 ? 0 : firstGlassMeta;
		for(int highestCapacitor = capacitors.length - 1; highestCapacitor >= 0; highestCapacitor--){
			if(capacitors[highestCapacitor] > 0 && formationChecklist == true) {
				formationChecklist = checkGlassTier(colourCorrectedMeta, highestCapacitor);
			}
		}

		// Glass has to be at least UV-tier to allow TT Laser hatches
		if(colourCorrectedMeta < 5) {
			if(mEnergyTunnelsTT.size() > 0 || mDynamoTunnelsTT.size() > 0) {
				formationChecklist = false;
			}
			mEnergyTunnelsTT.clear();
			mDynamoTunnelsTT.clear();
		}

		//Check if enough (more than 50%) non-empty caps
		double emptyCheck = ((double) capacitors[5]) / (double) (capacitors[0] + capacitors[1] + capacitors[2] + capacitors[3] + capacitors[4] + capacitors[6]);
		if (emptyCheck > 0.5)
			formationChecklist = false;
		
		// Calculate total capacity
		calculateCapacity();
		
		// Calculate how much energy to void each tick
		passiveDischargeAmount = new BigDecimal(tempCapacity).multiply(PASSIVE_DISCHARGE_FACTOR_PER_TICK).toBigInteger();
		passiveDischargeAmount = recalculateLossWithMaintenance(super.getRepairStatus());
		return formationChecklist;
	}

	public BigInteger calculateTempCapacity(BigInteger tempCapacity, int meta) {
		switch(meta) {
		case 1: tempCapacity = tempCapacity.add(BigInteger.valueOf(100000000L)); capacity = capacity.add(BigInteger.valueOf(100000000L)); break;
		case 2: tempCapacity = tempCapacity.add(BigInteger.valueOf(1000000000L)); capacity = capacity.add(BigInteger.valueOf(1000000000L)); break;
		case 3: tempCapacity = tempCapacity.add(BigInteger.valueOf(10000000000L)); capacity = capacity.add(BigInteger.valueOf(10000000000L)); break;
		case 4: tempCapacity = tempCapacity.add(BigInteger.valueOf(100000000000L)); capacity = capacity.add(BigInteger.valueOf(100000000000L)); break;
		case 5: tempCapacity = tempCapacity.add(BigInteger.valueOf(100000000000L));	capacity = capacity.add(MAX_LONG); break;
		case 6: break;
		case 7: tempCapacity = tempCapacity.add(BigInteger.valueOf(10000000L)); capacity = capacity.add(BigInteger.valueOf(10000000L)); break;
		default: break; 
		}
		return tempCapacity;
	}
	
	public boolean checkGlassTier(int colourCorrectedMeta, int highestCapacitor) {
		Boolean check = true;
		switch (highestCapacitor) {
		case 0://For the empty/EV/IV caps, any BS glass works. The case is meta - 1
			break; 
		case 1:
			if(colourCorrectedMeta < highestCapacitor) {
				check = false;
			}
			break;
		case 2:
			if(colourCorrectedMeta < highestCapacitor) {
				check = false;
			}
			break;
		case 3:
			if(colourCorrectedMeta < highestCapacitor) {
				check = false;
			}
			break;
		case 4:
			if(colourCorrectedMeta < highestCapacitor) {
				check = false;
			}
			break;
		case 5:
			break;
		case 6:
			break;
		default:
			check = false;
		}
		return check; //Return false if it fails the check, otherwise true
	}
		
	public void calculateCapacity() {
		capacity = BigInteger.ZERO;
		for(int i = 0; i < capacitors.length; i++) {	
			switch(i) {
			case 0: capacity = capacity.add(BigInteger.valueOf(100000000L).multiply(BigInteger.valueOf(capacitors[i]))); break;
			case 1: capacity = capacity.add(BigInteger.valueOf(1000000000L).multiply(BigInteger.valueOf(capacitors[i]))); break;
			case 2: capacity = capacity.add(BigInteger.valueOf(10000000000L).multiply(BigInteger.valueOf(capacitors[i]))); break;
			case 3: capacity = capacity.add(BigInteger.valueOf(100000000000L).multiply(BigInteger.valueOf(capacitors[i]))); break;
			case 4: capacity = capacity.add(MAX_LONG.multiply(BigInteger.valueOf(capacitors[i]))); break;
			case 5: break;
			case 6: capacity = capacity.add(BigInteger.valueOf(10000000L).multiply(BigInteger.valueOf(capacitors[i]))); break;
			default: break; 
			}
		}
	}
		
	@Override
	public boolean addEnergyInputToMachineList(IGregTechTileEntity te, int aBaseCasingIndex) {
		if (te == null) {
			return false;
		} else {
			final IMetaTileEntity mte = te.getMetaTileEntity();

			if (mte instanceof MetaTileEntity) {
				mMaxEUIn += ((MetaTileEntity) mte).maxEUInput() * ((MetaTileEntity) mte).maxAmperesIn();
			}

			if (mte instanceof GT_MetaTileEntity_Hatch_Energy) {
				// Add GT hatches
				final GT_MetaTileEntity_Hatch_Energy tHatch = ((GT_MetaTileEntity_Hatch_Energy) mte);
				tHatch.updateTexture(aBaseCasingIndex);

				return super.mEnergyHatches.add(tHatch);
			} else if(mte instanceof  GT_MetaTileEntity_Hatch_EnergyTunnel) {
				// Add TT Laser hatches
				final GT_MetaTileEntity_Hatch_EnergyTunnel tHatch = ((GT_MetaTileEntity_Hatch_EnergyTunnel) mte);

				return mEnergyTunnelsTT.add((GT_MetaTileEntity_Hatch_EnergyTunnel) mte);
			} else if(mte instanceof GT_MetaTileEntity_Hatch_EnergyMulti) {
				// Add TT hatches
				final GT_MetaTileEntity_Hatch_EnergyMulti tHatch = (GT_MetaTileEntity_Hatch_EnergyMulti) mte;
				tHatch.updateTexture(aBaseCasingIndex);

				return mEnergyHatchesTT.add(tHatch);
			} else {
				return false;
			}
		}
	}

	@Override
	public boolean addDynamoToMachineList(IGregTechTileEntity te, int aBaseCasingIndex) {
		if (te == null) {
			return false;
		} else {
			final IMetaTileEntity mte = te.getMetaTileEntity();

			if (mte instanceof MetaTileEntity) {
				mMaxEUOut += ((MetaTileEntity) mte).maxEUOutput() * ((MetaTileEntity) mte).maxAmperesOut();
			}

			if (mte instanceof GT_MetaTileEntity_Hatch_Dynamo) {
				// Add GT hatches
				final GT_MetaTileEntity_Hatch_Dynamo tDynamo = (GT_MetaTileEntity_Hatch_Dynamo) mte;
				tDynamo.updateTexture(aBaseCasingIndex);

				return super.mDynamoHatches.add(tDynamo);
			} else if(mte instanceof  GT_MetaTileEntity_Hatch_DynamoTunnel) {
				// Add TT Laser hatches
				final GT_MetaTileEntity_Hatch_DynamoTunnel tDynamo = (GT_MetaTileEntity_Hatch_DynamoTunnel) mte;

				return mDynamoTunnelsTT.add(tDynamo);
			} else if(mte instanceof GT_MetaTileEntity_Hatch_DynamoMulti) {
				// Add TT hatches
				final GT_MetaTileEntity_Hatch_DynamoMulti tDynamo = (GT_MetaTileEntity_Hatch_DynamoMulti) mte;
				tDynamo.updateTexture(aBaseCasingIndex);

				return mDynamoHatchesTT.add(tDynamo);
			} else {
				return false;
			}
		}
	}

	@Override
	public boolean onRunningTick(ItemStack stack){
		// Reset I/O cache
		inputLastTick = BigInteger.ZERO;
		outputLastTick = BigInteger.ZERO;

		//System.out.println(getBaseMetaTileEntity().)

		// Draw energy from GT hatches
		for(GT_MetaTileEntity_Hatch_Energy eHatch : super.mEnergyHatches) {
			if(eHatch == null || eHatch.getBaseMetaTileEntity().isInvalidTileEntity()) {
				continue;
			}
			final long power = getPowerToDraw(eHatch.maxEUInput() * eHatch.maxAmperesIn());
			if(eHatch.getEUVar() >= power) {
				eHatch.setEUVar(eHatch.getEUVar() - power);
				stored = stored.add(BigInteger.valueOf(power));
				inputLastTick = inputLastTick.add(BigInteger.valueOf(power));
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
				stored = stored.subtract(BigInteger.valueOf(power));
				outputLastTick = outputLastTick.add(BigInteger.valueOf(power));
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
				stored = stored.add(BigInteger.valueOf(power));
				inputLastTick = inputLastTick.add(BigInteger.valueOf(power));
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
				stored = stored.subtract(BigInteger.valueOf(power));
				outputLastTick = outputLastTick.add(BigInteger.valueOf(power));
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
				stored = stored.add(BigInteger.valueOf(power));
				inputLastTick = inputLastTick.add(BigInteger.valueOf(power));
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
				stored = stored.subtract(BigInteger.valueOf(power));
				outputLastTick = outputLastTick.add(BigInteger.valueOf(power));
			}
		}
		// Loose some energy
		// Recalculate if the repair status changed
		if(super.getRepairStatus() != repairStatusCache) {
			passiveDischargeAmount = recalculateLossWithMaintenance(super.getRepairStatus());
		}
		stored = stored.subtract(passiveDischargeAmount);
		stored = (stored.compareTo(BigInteger.ZERO) <= 0) ? BigInteger.ZERO : stored;

		IGregTechTileEntity tBMTE = this.getBaseMetaTileEntity();

		tBMTE.injectEnergyUnits((byte)ForgeDirection.UNKNOWN.ordinal(), inputLastTick.longValue(), 1L);
		tBMTE.drainEnergyUnits((byte)ForgeDirection.UNKNOWN.ordinal(), outputLastTick.longValue(), 1L);

		return true;
	}

	/**
	 * To be called whenever the maintenance status changes or the capacity was recalculated
	 * @param repairStatus
	 * 		This machine's repair status
	 * @return new BigInteger instance for passiveDischargeAmount
	 */
	private BigInteger recalculateLossWithMaintenance(int repairStatus) {
		repairStatusCache = repairStatus;
		return new BigDecimal(passiveDischargeAmount)
				.multiply(BigDecimal.valueOf(1.0D + 0.2D * repairStatus)).toBigInteger();
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
		return Math.min(hatchWatts, recampLimited.longValue());
	}

	/**
	 * Calculate how much EU to push into a Dynamo Hatch
	 * @param hatchWatts
	 * 		Hatch amperage * voltage
	 * @return EU amount
	 */
	private long getPowerToPush(long hatchWatts){
		final BigInteger remStoredLimited = (MAX_LONG.compareTo(stored) > 0) ? stored : MAX_LONG;
		return Math.min(hatchWatts, remStoredLimited.longValue());
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
		ll.add("---------------------------------------------");

		final String[] a = new String[ll.size()];
		return ll.toArray(a);
	}

	@Override
	public void saveNBTData(NBTTagCompound nbt) {
		nbt = (nbt == null) ? new NBTTagCompound() : nbt;

		nbt.setByteArray("capacity", capacity.toByteArray());
		nbt.setByteArray("stored", stored.toByteArray());
		nbt.setByteArray("passiveDischargeAmount", passiveDischargeAmount.toByteArray());

		super.saveNBTData(nbt);
	}

	@Override
	public void loadNBTData(NBTTagCompound nbt) {
		nbt = (nbt == null) ? new NBTTagCompound() : nbt;

		capacity = new BigInteger(nbt.getByteArray("capacity"));
		stored = new BigInteger(nbt.getByteArray("stored"));
		passiveDischargeAmount = new BigInteger(nbt.getByteArray("passiveDischargeAmount"));

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
}

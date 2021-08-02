package common.tileentities;

import common.Blocks;
import common.blocks.*;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import kekztech.MultiFluidHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.input.Keyboard;
import util.Vector3i;
import util.Vector3ic;

import java.util.ArrayList;
import java.util.HashSet;

public class GTMTE_FluidMultiStorage extends GT_MetaTileEntity_MultiBlockBase {
	
	private final static String glassNameIC2Reinforced = "blockAlloyGlass";
	private final static Block CASING = Blocks.tfftCasing;
	private final static Block_TFFTStorageFieldBlockT1 STORAGE_FIELD1 = (Block_TFFTStorageFieldBlockT1) Blocks.tfftStorageField1;
	private final static Block_TFFTStorageFieldBlockT2 STORAGE_FIELD2 = (Block_TFFTStorageFieldBlockT2) Blocks.tfftStorageField2;
	private final static Block_TFFTStorageFieldBlockT3 STORAGE_FIELD3 = (Block_TFFTStorageFieldBlockT3) Blocks.tfftStorageField3;
	private final static Block_TFFTStorageFieldBlockT4 STORAGE_FIELD4 = (Block_TFFTStorageFieldBlockT4) Blocks.tfftStorageField4;
	private final static Block_TFFTStorageFieldBlockT5 STORAGE_FIELD5 = (Block_TFFTStorageFieldBlockT5) Blocks.tfftStorageField5;
	private final static int CASING_TEXTURE_ID = 176;

	private MultiFluidHandler mfh;
	private final HashSet<GTMTE_TFFTMultiHatch> sMultiHatches = new HashSet<>();

	private int runningCost = 0;
	private boolean doVoidExcess = false;
	private byte fluidSelector = 0;

	public GTMTE_FluidMultiStorage(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GTMTE_FluidMultiStorage(String aName) {
		super(aName);
	}

	@Override
	public IMetaTileEntity newMetaEntity(IGregTechTileEntity var1) {
		return new GTMTE_FluidMultiStorage(super.mName);
	}

	@Override
	public String[] getDescription() {
		final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
		tt.addMachineType("Fluid Tank")
		.addInfo("High-Tech fluid tank that can hold up to 25 different fluids!")
		.addInfo("Has 1/25th of the total capacity as capacity for each fluid.")
		.addInfo("Right clicking the controller with a screwdriver will turn on excess voiding.")
		.addInfo("Fluid storage amount and running cost depends on the storage field blocks used.")
		.addSeparator()
		.addInfo("Note on hatch locking:")
		.addInfo("Use an Integrated Circuit in the GUI slot to limit which fluid is output.")
		.addInfo("The index of a stored fluid can be obtained through the Tricorder.")
		.addSeparator()
		.beginStructureBlock(5, 9, 5, false)
		.addController("Top Center")
		.addCasingInfo("T.F.F.T. Casing", 20)
		.addOtherStructurePart("Storage Field Blocks (Tier I-V)", "Inner 3x7x3 solid pillar")
		.addOtherStructurePart("IC2 Reinforced Glass", "Outer 5x7x5 glass shell")
		.addMaintenanceHatch("Any top or bottom casing")
		.addEnergyHatch("Any top or bottom casing")
		.addInputHatch("Instead of any casing or glass, has to touch storage field block")
		.addOutputHatch("Instead of any casing or glass, has to touch storage field block")
		.addStructureInfo("You can have a bunch of hatches")
		.addOtherStructurePart("Multi I/O Hatches", "Instead of any casing or glass, has to touch storage field block")
		.addStructureInfo("Use MIOH with conduits or fluid storage busses to see all fluids at once. If it's fixed.")
		.addStructureInfo("Ask someone else why there's 4 versions, with 2 uncraftable ones")
		.toolTipFinisher("KekzTech");
		if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			return tt.getInformation();
		} else {
			return tt.getStructureInformation();
		}
	}

	@Override
	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex,
								 boolean aActive, boolean aRedstone) {
		return aSide == aFacing
				? new ITexture[]{BlockIcons.casingTexturePages[1][48],
				new GT_RenderedTexture(aActive
						? BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE
						: BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR)}
				: new ITexture[]{BlockIcons.casingTexturePages[1][48]};
	}

	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(),
				"MultiblockDisplay.png");
	}

	@Override
	public boolean isCorrectMachinePart(ItemStack var1) {
		return true;
	}

	@Override
	public boolean checkRecipe(ItemStack guiSlotItem) {

		super.mEfficiency = 10000 - (super.getIdealStatus() - super.getRepairStatus()) * 1000;
		super.mEfficiencyIncrease = 10000;
		super.mEUt = runningCost;
		super.mMaxProgresstime = 10;
		
		if(guiSlotItem != null && guiSlotItem.getUnlocalizedName().equals("gt.integrated_circuit")) {
			this.fluidSelector = (byte) guiSlotItem.getItemDamage();
		}
		
		// If there are no basic I/O hatches, let multi hatches handle it and skip a lot of code!
		if (sMultiHatches.size() > 0 && super.mInputHatches.size() == 0 && super.mOutputHatches.size() == 0) {
			return true;
		}

		// Suck in fluids
		final ArrayList<FluidStack> inputHatchFluids = super.getStoredFluids();
		if (inputHatchFluids.size() > 0) {

			for (FluidStack fluidStack : inputHatchFluids) {

				final int pushed = mfh.pushFluid(fluidStack, true);
				final FluidStack toDeplete = fluidStack.copy();
				toDeplete.amount = pushed;
				super.depleteInput(toDeplete);
			}
		}

		// Push out fluids
		if (guiSlotItem != null && guiSlotItem.getUnlocalizedName().equals("gt.integrated_circuit")) {
			final FluidStack storedFluid = mfh.getFluidCopy(fluidSelector);
			// Sum available output capacity
			int possibleOutput = 0;
			for (GT_MetaTileEntity_Hatch_Output outputHatch : super.mOutputHatches) {
				if (outputHatch.isFluidLocked() && outputHatch.getLockedFluidName().equals(storedFluid.getUnlocalizedName())) {
					possibleOutput += outputHatch.getCapacity() - outputHatch.getFluidAmount();
				} else if (outputHatch.getFluid() != null && outputHatch.getFluid().getUnlocalizedName().equals(storedFluid.getUnlocalizedName())) {
					possibleOutput += outputHatch.getCapacity() - outputHatch.getFluidAmount();
				} else if (outputHatch.getFluid() == null) {
					possibleOutput += outputHatch.getCapacity() - outputHatch.getFluidAmount();
				}
			}
			// Output as much as possible
			final FluidStack tempStack = storedFluid.copy();
			tempStack.amount = possibleOutput;
			tempStack.amount = mfh.pullFluid(tempStack, fluidSelector, true);
			super.addOutput(tempStack);

		} else {
			int tDistinct = mfh.getDistinctFluids();
			int tDistinctCount = 0;
			int tMaxDistinct = mfh.getMaxDistinctFluids();
			for(int i = 0; i < tMaxDistinct && tDistinctCount< tDistinct;i++) {
				final FluidStack storedFluidCopy = mfh.getFluidCopy(i);
				if (storedFluidCopy == null)
					continue;
				tDistinctCount++;
				storedFluidCopy.amount = 0;
				// Calculate how much capacity all available Output Hatches offer
				for (GT_MetaTileEntity_Hatch_Output outputHatch : super.mOutputHatches) {
					if (outputHatch.isFluidLocked() && outputHatch.getLockedFluidName().equals(storedFluidCopy.getUnlocalizedName())) {
						storedFluidCopy.amount += outputHatch.getCapacity() - outputHatch.getFluidAmount();
						addFluidToHatch(storedFluidCopy,outputHatch);
					} else if (outputHatch.getFluid() != null && outputHatch.getFluid().isFluidEqual(storedFluidCopy)) {
						storedFluidCopy.amount += outputHatch.getCapacity() - outputHatch.getFluidAmount();
						addFluidToHatch(storedFluidCopy,outputHatch);
					} else if (!outputHatch.isFluidLocked() && outputHatch.getFluid() == null) {
						storedFluidCopy.amount += outputHatch.getCapacity() - outputHatch.getFluidAmount();
						addFluidToHatch(storedFluidCopy,outputHatch);
					}
				}
			}
		}

		return true;
	}

	public void addFluidToHatch(FluidStack aFluid, GT_MetaTileEntity_Hatch_Output aHatch) {
		aFluid.amount = mfh.pullFluid(aFluid, true);
		aHatch.fill(aFluid,true);
	}

	@Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		super.onPostTick(aBaseMetaTileEntity, aTick);

		if (mfh != null) {
			mfh.setLock(!super.getBaseMetaTileEntity().isActive());
			mfh.setFluidSelector(fluidSelector);
			mfh.setDoVoidExcess(doVoidExcess);
		}
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
		int minCasingAmount = 20;
		boolean formationChecklist = true; // If this is still true at the end, machine is good to go :)
		float runningCostAcc = 0;
		double fluidCapacityAcc = 0;

		sMultiHatches.clear();

		// Front segment
		for (int X = -2; X <= 2; X++) {
			for (int Y = -2; Y <= 2; Y++) {
				if (X == 0 && Y == 0) {
					continue; // Skip controller
				}

				// Get next TE
				final Vector3ic offset = rotateOffsetVector(forgeDirection, X, Y, 0);
				final IGregTechTileEntity currentTE =
						thisController.getIGregTechTileEntityOffset(offset.x(), offset.y(), offset.z());

				// Fluid hatches should touch the storage field. 
				// Maintenance/Energy hatch can go anywhere
				if (X > -2 && X < 2 && Y > -2 && Y < 2) {
					if (!super.addMaintenanceToMachineList(currentTE, CASING_TEXTURE_ID)
							&& !super.addInputToMachineList(currentTE, CASING_TEXTURE_ID)
							&& !super.addOutputToMachineList(currentTE, CASING_TEXTURE_ID)
							&& !super.addEnergyInputToMachineList(currentTE, CASING_TEXTURE_ID)
							&& !addMultiHatchToMachineList(currentTE, CASING_TEXTURE_ID)) {

						// If it's not a hatch, is it the right casing for this machine? Check block and block meta.
						// Also check for multi hatch
						if (thisController.getBlockOffset(offset.x(), offset.y(), offset.z()) == CASING) {
							// Seems to be valid casing. Decrement counter.
							minCasingAmount--;
						} else {
							formationChecklist = false;
						}
					}
				} else {
					if (!super.addMaintenanceToMachineList(currentTE, CASING_TEXTURE_ID)
							&& !super.addEnergyInputToMachineList(currentTE, CASING_TEXTURE_ID)) {

						// If it's not a hatch, is it the right casing for this machine? Check block and block meta.
						if (thisController.getBlockOffset(offset.x(), offset.y(), offset.z()) == CASING) {
							// Seems to be valid casing. Decrement counter.
							minCasingAmount--;
						} else {
							formationChecklist = false;
						}
					}
				}
			}
		}

		// Middle seven long segment
		for (int X = -2; X <= 2; X++) {
			for (int Y = -2; Y <= 2; Y++) {
				for (int Z = -1; Z >= -7; Z--) {
					final Vector3ic offset = rotateOffsetVector(forgeDirection, X, Y, Z);
					if (X > -2 && X < 2 && Y > -2 && Y < 2) {
						if (thisController.getBlockOffset(offset.x(), offset.y(), offset.z()).getUnlocalizedName()
								.equals(STORAGE_FIELD1.getUnlocalizedName())) {
							runningCostAcc += 0.5f;
							fluidCapacityAcc += (float) Block_TFFTStorageFieldBlockT1.getCapacity();
						} else if (thisController.getBlockOffset(offset.x(), offset.y(), offset.z()).getUnlocalizedName()
								.equals(STORAGE_FIELD2.getUnlocalizedName())) {
							runningCostAcc += 1.0f;
							fluidCapacityAcc += (float) Block_TFFTStorageFieldBlockT2.getCapacity();
						} else if (thisController.getBlockOffset(offset.x(), offset.y(), offset.z()).getUnlocalizedName()
								.equals(STORAGE_FIELD3.getUnlocalizedName())) {
							runningCostAcc += 2.0f;
							fluidCapacityAcc += (float) Block_TFFTStorageFieldBlockT3.getCapacity();
						} else if (thisController.getBlockOffset(offset.x(), offset.y(), offset.z()).getUnlocalizedName()
								.equals(STORAGE_FIELD4.getUnlocalizedName())) {
							runningCostAcc += 4.0f;
							fluidCapacityAcc += (float) Block_TFFTStorageFieldBlockT4.getCapacity();
						} else if (thisController.getBlockOffset(offset.x(), offset.y(), offset.z()).getUnlocalizedName()
								.equals(STORAGE_FIELD5.getUnlocalizedName())) {
							runningCostAcc += 8.0f;
							fluidCapacityAcc += (float) Block_TFFTStorageFieldBlockT5.getCapacity();
						} else {
							formationChecklist = false;
						}
						continue;
					}

					// Get next TE
					final IGregTechTileEntity currentTE =
							thisController.getIGregTechTileEntityOffset(offset.x(), offset.y(), offset.z());

					// Corner allows only glass
					if (X == -2 && Y == -2 || X == 2 && Y == 2 || X == -2 && Y == 2 || X == 2 && Y == -2) {
						if (!(thisController.getBlockOffset(offset.x(), offset.y(), offset.z()).getUnlocalizedName().equals(glassNameIC2Reinforced))) {
							formationChecklist = false;
						}
					} else {
						// Tries to add TE as either of those kinds of hatches.
						// The number is the texture index number for the texture that needs to be painted over the hatch texture (TAE for GT++)
						if (!super.addInputToMachineList(currentTE, CASING_TEXTURE_ID)
								&& !super.addOutputToMachineList(currentTE, CASING_TEXTURE_ID)
								&& !addMultiHatchToMachineList(currentTE, CASING_TEXTURE_ID)) {

							// If it's not a hatch, is it the right casing for this machine? Check block and block meta.
							// Also check for multi hatch
							if (thisController.getBlockOffset(offset.x(), offset.y(), offset.z()) == CASING) {
								// Seems to be valid casing. Decrement counter.
								minCasingAmount--;
							} else if (!thisController.getBlockOffset(offset.x(), offset.y(), offset.z()).getUnlocalizedName().equals(glassNameIC2Reinforced)) {
								formationChecklist = false;
							}
						}
					}
				}
			}
		}

		// Back segment
		for (int X = -2; X <= 2; X++) {
			for (int Y = -2; Y <= 2; Y++) {
				// Get next TE
				final Vector3ic offset = rotateOffsetVector(forgeDirection, X, Y, -8);
				final IGregTechTileEntity currentTE =
						thisController.getIGregTechTileEntityOffset(offset.x(), offset.y(), offset.z());

				// Fluid hatches should touch the storage field. 
				// Maintenance/Energy hatch can go anywhere
				if (X > -2 && X < 2 && Y > -2 && Y < 2) {
					if (!super.addMaintenanceToMachineList(currentTE, CASING_TEXTURE_ID)
							&& !super.addInputToMachineList(currentTE, CASING_TEXTURE_ID)
							&& !super.addOutputToMachineList(currentTE, CASING_TEXTURE_ID)
							&& !super.addEnergyInputToMachineList(currentTE, CASING_TEXTURE_ID)
							&& !addMultiHatchToMachineList(currentTE, CASING_TEXTURE_ID)) {

						// If it's not a hatch, is it the right casing for this machine? Check block and block meta.
						if (thisController.getBlockOffset(offset.x(), offset.y(), offset.z()) == CASING) {
							// Seems to be valid casing. Decrement counter.
							minCasingAmount--;
						} else {
							formationChecklist = false;
						}
					}
				} else {
					if (!super.addMaintenanceToMachineList(currentTE, CASING_TEXTURE_ID)
							&& !super.addEnergyInputToMachineList(currentTE, CASING_TEXTURE_ID)) {

						// If it's not a hatch, is it the right casing for this machine? Check block and block meta.
						if (thisController.getBlockOffset(offset.x(), offset.y(), offset.z()) == CASING) {
							// Seems to be valid casing. Decrement counter.
							minCasingAmount--;
						} else {
							formationChecklist = false;
						}
					}
				}
			}
		}

		if (this.mEnergyHatches.size() < 1) {
			formationChecklist = false;
		}

		if (this.mMaintenanceHatches.size() != 1) {
			formationChecklist = false;
		}

		if (minCasingAmount > 0) {
			formationChecklist = false;
		}

		if (formationChecklist) {
			runningCost = Math.round(-runningCostAcc);
			// Update MultiFluidHandler in case storage cells have been changed
			final int capacityPerFluid = (int) Math.round(fluidCapacityAcc / 25.0f);
			if (mfh == null) {
				mfh = MultiFluidHandler.newInstance(25, capacityPerFluid);
			} else {
				if (mfh.getCapacity() != capacityPerFluid) {
					mfh = MultiFluidHandler.newAdjustedInstance(mfh, capacityPerFluid);
				}
			}
			for (GTMTE_TFFTMultiHatch mh : sMultiHatches) {
				mh.setMultiFluidHandler(mfh);
			}
		}

		return formationChecklist;
	}

	public boolean addMultiHatchToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		} else {
			final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
			if (aMetaTileEntity == null) {
				return false;
			} else if (aMetaTileEntity instanceof GTMTE_TFFTMultiHatch) {
				((GTMTE_TFFTMultiHatch)aMetaTileEntity).updateTexture(aBaseCasingIndex);
				return this.sMultiHatches.add((GTMTE_TFFTMultiHatch)aMetaTileEntity);
			} else {
				return false;
			}
		}
	}

	@Override
	public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
		doVoidExcess = !doVoidExcess;
		GT_Utility.sendChatToPlayer(aPlayer, doVoidExcess ? "Auto-voiding enabled" : "Auto-voiding disabled");
	}

	@Override
	public String[] getInfoData() {
		final ArrayList<String> ll = mfh.getInfoData();

		ll.add(EnumChatFormatting.YELLOW + "Operational Data:" + EnumChatFormatting.RESET);
		ll.add("Auto-voiding: " + doVoidExcess);
		ll.add("Per-Fluid Capacity: " + mfh.getCapacity() + "L");
		ll.add("Running Cost: "
				// mEUt does not naturally reflect efficiency status. Do that here.
				+ ((-super.mEUt) * 10000 / Math.max(1000, super.mEfficiency)) + "EU/t");
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

		nbt.setInteger("runningCost", runningCost);
		nbt.setBoolean("doVoidExcess", doVoidExcess);

		nbt.setInteger("capacityPerFluid", mfh.getCapacity());
		nbt.setTag("fluids", mfh.saveNBTData(new NBTTagCompound()));

		super.saveNBTData(nbt);
	}

	@Override
	public void loadNBTData(NBTTagCompound nbt) {
		nbt = (nbt == null) ? new NBTTagCompound() : nbt;

		runningCost = nbt.getInteger("runningCost");
		doVoidExcess = nbt.getBoolean("doVoidExcess");

		mfh = MultiFluidHandler.loadNBTData(nbt);
		for (GTMTE_TFFTMultiHatch mh : sMultiHatches) {
			mh.setMultiFluidHandler(mfh);
		}
		super.loadNBTData(nbt);
	}

	@Override
	public boolean isGivingInformation() {
		return true;
	}

	@Override
	public int getMaxEfficiency(ItemStack var1) {
		return 10000;
	}

	@Override
	public int getPollutionPerTick(ItemStack var1) {
		return 0;
	}

	@Override
	public int getDamageToComponent(ItemStack var1) {
		return 0;
	}

	@Override
	public boolean explodesOnComponentBreak(ItemStack var1) {
		return false;
	}
}
package tileentities;

import java.util.ArrayList;

import blocks.Block_TFFTCasing;
import blocks.Block_TFFTStorageFieldBlockT1;
import blocks.Block_TFFTStorageFieldBlockT2;
import blocks.Block_TFFTStorageFieldBlockT3;
import blocks.Block_TFFTStorageFieldBlockT4;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_RenderedTexture;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import util.Vector3i;
import util.Vector3ic;

public class GTMTE_FluidMultiStorage extends GT_MetaTileEntity_MultiBlockBase {
	
	private final String glassNameAE2 = "tile.appliedenergistics2.BlockQuartzGlass";
	private final String glassNameStained = "tile.stainedGlass";
	private final Block CASING = Block_TFFTCasing.getInstance();
	private final Block STORAGE_FIELD1 = Block_TFFTStorageFieldBlockT1.getInstance();
	private final Block STORAGE_FIELD2 = Block_TFFTStorageFieldBlockT2.getInstance();
	private final Block STORAGE_FIELD3 = Block_TFFTStorageFieldBlockT3.getInstance();
	private final Block STORAGE_FIELD4 = Block_TFFTStorageFieldBlockT4.getInstance();
	private final int CASING_TEXTURE_ID = 176;
	
	private final ArrayList<FluidStack> fluidList = new ArrayList<>();
	private long totalFluidCapacity = 0;
	
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
		return new String[] {
				"High-Tech fluid tank that can hold up to 25 different fluids",
				"Fluid storage amount and running cost depends on the storage field blocks used",
				"Different tiers can be combined freely",
				"Tier I:   500,000L per block, 0.33EU/t cost",
				"Tier II:  4,000,000L per block, 1EU/t cost",
				"Tier III: 16,000,000L per block, 3EU/t",
				"Tier IV:  64,000,000L per block, 9EU/t",
				"------------------------------------------",
				"Note on hatch locking:",
				"Inserting an Integrated Circuit into to GUI slot",
				"forces the T.F.F.T to only output the fluid with that number on all hatches.",
				"It is thereby recommended to add Output Hatches one by one while cycling through the IC configurations.",
				"The number of a stored fluid can be obtained through the Tricorder.",
				"------------------------------------------",
				"Dimensions: 5x9x5 (WxHxL)",
				"Structure:",
				"   Controller: Top center",
				"   Energy Hatch: Any top or bottom casing",
				"   Inner 3x7x3 tube are Storage Field Blocks",
				"   Outer 5x7x5 glass shell is AE2 Quartz Glass or Vanilla Stained Glass",
				"   Maintenance Hatch: Any top or bottom casing",
				"   I/O Hatches: Instead of any casing or glass, have to touch storage field"
		};
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
		// TODO Auto-generated method stub
		if(guiSlotItem.getUnlocalizedName().equals("gt.integrated_circuit")) {
			
		}
		
		
		super.mEUt = 0;
		super.mEfficiency = 0;
		return false;
	}
	
	public Vector3ic rotateOffsetVector(Vector3ic forgeDirection, int x, int y, int z) {
		final Vector3i offset = new Vector3i();
		
		// either direction on z-axis
		if(forgeDirection.x() == 0 && forgeDirection.z() == -1) {
			offset.x = x;
			offset.y = y;
			offset.z = z;
		}
		if(forgeDirection.x() == 0 && forgeDirection.z() == 1) {
			offset.x = -x;
			offset.y = y;
			offset.z = -z;
		}
		// either direction on x-axis
		if(forgeDirection.x() == -1 && forgeDirection.z() == 0) {
			offset.x = z;
			offset.y = y;
			offset.z = -x;
		}
		if(forgeDirection.x() == 1 && forgeDirection.z() == 0) {
			offset.x = -z;
			offset.y = y;
			offset.z = x;
		}
		// either direction on y-axis
		if(forgeDirection.y() == -1) {
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
			boolean formationChecklist = true; // if this is still true at the end, machine is good to go :)
			float runningCost = 0;
			
		// Front slice
		for(int X = -2; X <= 2; X++) {
			for(int Y = -2; Y <= 2; Y++) {
				if(X == 0 && Y == 0) {
					continue; // is controller
				}
				
				// Get next TE
				final Vector3ic offset = rotateOffsetVector(forgeDirection, X, Y, 0);
				IGregTechTileEntity currentTE = 
						thisController.getIGregTechTileEntityOffset(offset.x(), offset.y(), offset.z());
				
				// Fluid hatches should touch the storage field. 
				// Maintenance/Energy hatch can go anywhere
				if(X > -2 && X < 2 && Y > -2 && Y < 2) {
					if (!super.addMaintenanceToMachineList(currentTE, CASING_TEXTURE_ID)
							&& !super.addInputToMachineList(currentTE, CASING_TEXTURE_ID)
							&& !super.addOutputToMachineList(currentTE, CASING_TEXTURE_ID)
							&& !super.addEnergyInputToMachineList(currentTE, CASING_TEXTURE_ID)) {
						
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
		
		// Middle three slices
		for(int X = -2; X <= 2; X++) {
			for(int Y = -2; Y <= 2; Y++) {
				for(int Z = -1; Z >= -7; Z--) {
					final Vector3ic offset = rotateOffsetVector(forgeDirection, X, Y, Z);
					if(X > -2 && X < 2 && Y > -2 && Y < 2) {
						if(thisController.getBlockOffset(offset.x(), offset.y(), offset.z()).getUnlocalizedName()
								.equals(STORAGE_FIELD1.getUnlocalizedName())) {
							runningCost += 0.33f;
							totalFluidCapacity += 500000;
						} else if(thisController.getBlockOffset(offset.x(), offset.y(), offset.z()).getUnlocalizedName()
								.equals(STORAGE_FIELD2.getUnlocalizedName())) {
							runningCost += 1.0f;
							totalFluidCapacity += 4000000;
						} else if(thisController.getBlockOffset(offset.x(), offset.y(), offset.z()).getUnlocalizedName()
								.equals(STORAGE_FIELD3.getUnlocalizedName())) {
							runningCost += 3.0f;
							totalFluidCapacity += 16000000;
						} else if(thisController.getBlockOffset(offset.x(), offset.y(), offset.z()).getUnlocalizedName()
								.equals(STORAGE_FIELD4.getUnlocalizedName())) {
							runningCost += 9.0f;
							totalFluidCapacity += 64000000;
						} else {
							formationChecklist = false;
						}
						continue;
					}
					
					// Get next TE
					IGregTechTileEntity currentTE = 
							thisController.getIGregTechTileEntityOffset(offset.x(), offset.y(), offset.z());// x, y ,z
					
					// Corner allows only glass or casings
					if(X == -2 && Y == -2 || X == 2 && Y == 2 || X == -2 && Y == 2 || X == 2 && Y == -2) {
						if(!(thisController.getBlockOffset(offset.x(), offset.y(), offset.z()).getUnlocalizedName().equals(glassNameAE2)
								|| thisController.getBlockOffset(offset.x(), offset.y(), offset.z()).getUnlocalizedName().equals(glassNameStained)
								|| thisController.getBlockOffset(offset.x(), offset.y(), offset.z()) == CASING)) {							
							formationChecklist = false; // do nothing yet
						}
					} else {
						// Tries to add TE as either of those kinds of hatches.
						// The number is the texture index number for the texture that needs to be painted over the hatch texture (TAE for GT++)
						if (   !super.addInputToMachineList(currentTE, CASING_TEXTURE_ID)
							&& !super.addOutputToMachineList(currentTE, CASING_TEXTURE_ID)) {
							
							// If it's not a hatch, is it the right casing for this machine? Check block and block meta.
							if (thisController.getBlockOffset(offset.x(), offset.y(), offset.z()) == CASING) {
								// Seems to be valid casing. Decrement counter.
								minCasingAmount--;
							} else if (thisController.getBlockOffset(offset.x(), offset.y(), offset.z()).getUnlocalizedName().equals(glassNameAE2)
								|| thisController.getBlockOffset(offset.x(), offset.y(), offset.z()).getUnlocalizedName().equals(glassNameStained)) {
								// do nothing lol
							} else {
								formationChecklist = false;
							}
						}
					}
				}
			}
		}
		
		// Front slice
		for(int X = -2; X <= 2; X++) {
			for(int Y = -2; Y <= 2; Y++) {
				// Get next TE
				final Vector3ic offset = rotateOffsetVector(forgeDirection, X, Y, -8);
				IGregTechTileEntity currentTE = 
						thisController.getIGregTechTileEntityOffset(offset.x(), offset.y(), offset.z());
				
				// Fluid hatches should touch the storage field. 
				// Maintenance/Energy hatch can go anywhere
				if(X > -2 && X < 2 && Y > -2 && Y < 2) {
					if (!super.addMaintenanceToMachineList(currentTE, CASING_TEXTURE_ID)
							&& !super.addInputToMachineList(currentTE, CASING_TEXTURE_ID)
							&& !super.addOutputToMachineList(currentTE, CASING_TEXTURE_ID)
							&& !super.addEnergyInputToMachineList(currentTE, CASING_TEXTURE_ID)) {
						
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
		
		if(this.mEnergyHatches.size() < 1) {
			System.out.println("At least one energy hatch is required!");
			formationChecklist = false;
		}
		
		if(this.mMaintenanceHatches.size() < 1) {
			System.out.println("You need a maintenance hatch to do maintenance.");
		}
		
		if(minCasingAmount > 0) {
			formationChecklist = false;
		}
		
		if(formationChecklist) {
			super.mEUt = (int) Math.round(-runningCost);
			super.mEfficiency = 10000;
		} else {
			super.mEUt = 0;
			super.mEfficiency = 0;
		}
		
		return formationChecklist;
	}
	
	public String[] getInfoData() {
		final String[] lines = new String[fluidList.size() + 5];
		lines[0] = "Stored Fluids:";
		for(int i = 1; i < lines.length - 5; i++) {
			lines[i] = (i - 1) + " - " + fluidList.get(i - 1).getLocalizedName() + ": " + fluidList.get(i - 1).amount;
		}
		lines[fluidList.size() + 1] = "Operation Data:";
		lines[fluidList.size() + 2] = "Used Capacity: " + 0;
		lines[fluidList.size() + 3] = "Total Capacity: " + totalFluidCapacity;
		lines[fluidList.size() + 4] = "Running Cost: " + super.mEUt;
		lines[fluidList.size() + 5] = "Maintenance Status: " + ((super.getRepairStatus() == 0) ? "Working perfectly" : "Has Problems");
		return lines;
	}
	
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

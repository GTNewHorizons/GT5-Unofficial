package tileentities;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import blocks.Block_ItemServerDrive;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_RenderedTexture;
import kekztech.MultiFluidHandler;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import util.MultiBlockTooltipBuilder;
import util.Vector3i;
import util.Vector3ic;

public class GTMTE_ItemServer extends GT_MetaTileEntity_MultiBlockBase {
	
	private static final int BASE_SLICE_ENERGY_COST = 1;
	private static final int BASE_PER_ITEM_CAPACITY = 1024;
	private static final int BASE_ITEM_TYPES_PER_SLICE = 128;
	
	private final Block_ItemServerDrive DRIVE = Block_ItemServerDrive.getInstance();
	private final String ALU_FRAME_BOX_NAME = "gt.blockmachines.gt_frame_aluminium";
	private final int CASING_TEXTURE_ID = 176;
	
	private int sliceCount = 0;
	
	public GTMTE_ItemServer(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GTMTE_ItemServer(String aName) {
		super(aName);
	}
	
	@Override
	public IMetaTileEntity newMetaEntity(IGregTechTileEntity var1) {
		return new GTMTE_ItemServer(super.mName);
	}
	
	@Override
	public String[] getDescription() {
		final MultiBlockTooltipBuilder b = new MultiBlockTooltipBuilder();
		b.addInfo("High-Tech item storage!")
				.addInfo("Variable length: Slices 2-4 can be repeated as long as the total length does not exceed 16 blocks.")
				.addInfo("Each slices offers storage for 128 item types")
				.addInfo("Storage capacity per item depends on the controller configuration.")
				.addInfo("Insert an Integrated Circuit into the controller with your desired configuration.")
				.addInfo("The base configuration (0) is 1024 items per type. For each higher level, the capacity quadruples.")
				.addInfo("Each slice also adds 1EU/t of power consumption and doubles with rising configuration values.")
				.addInfo("Valid config values are from zero to eight.")
				.addSeparator()
				.beginStructureBlock(3, 5, 4)
				.addController("Front Bottom Center")
				.addEnergyHatch("Any casing")
				.addOtherStructurePart("Front slice", "3x5x1 Item Server Rack Casing")
				.addOtherStructurePart("2nd and 3rd slice, center", "1x4x1 Tungstensteel Frame Box")
				.addOtherStructurePart("2nd and 3rd slice, top", "3x1x1 Item Server Rack Casing")
				.addOtherStructurePart("2nd and 3rd slice, sides", "2x 1x4x1 Item Server Drive")
				.addOtherStructurePart("Back slice", "3x5x1 Item Server Rack Casing")
				.addInputBus("Instead of any casing")
				.signAndFinalize("Kekzdealer");
		if(!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			return b.getInformation();
		} else {
			return b.getStructureInformation();
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
		final int config = (guiSlotItem != null && guiSlotItem.getUnlocalizedName().equals("gt.integrated_circuit")) 
				? Math.min(8, guiSlotItem.getItemDamage()) : 0;
				
		this.mEfficiency = 10000 - (this.getIdealStatus() - this.getRepairStatus()) * 1000;
		this.mEfficiencyIncrease = 10000;
		this.mEUt = (int) (BASE_SLICE_ENERGY_COST * sliceCount * Math.pow(2, config));
		super.mMaxProgresstime = 10;
		
		return true;
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
		
		// Front slice
		for(int X = -1; X <= 1; X++) {
			for(int Y = 0; Y < 5; Y++) {
				if(X == 0 && Y == 0) {
					continue; // is controller
				}
				
				// Get next TE
				final Vector3ic offset = rotateOffsetVector(forgeDirection, X, Y, 0);
				IGregTechTileEntity currentTE = 
						thisController.getIGregTechTileEntityOffset(offset.x(), offset.y(), offset.z());
				
				if(!super.addMaintenanceToMachineList(currentTE, CASING_TEXTURE_ID)
						&& !super.addInputToMachineList(currentTE, CASING_TEXTURE_ID)
						&& !super.addEnergyInputToMachineList(currentTE, CASING_TEXTURE_ID)) {
					
					// If it's not a hatch, is it the right casing for this machine?
					// TODO: Also check IO port
					if(thisController.getBlockOffset(offset.x(), offset.y(), offset.z()) == CASING) {
						// yay
					} else if (thisController.getBlockOffset(offset.x(), offset.y(), offset.z()) == IO_PORT) {
						// TODO: register IO port
					} else {
						formationChecklist = false;
					}
				}
			}
		}
		
		// Check slices
		int slicesFound = 0;
		int zOffset = 1;
		for(int s = 0; s < slicesFound; s++) {
			final Vector3ic probe = rotateOffsetVector(forgeDirection, 1, 0, zOffset);
			// Probe if another slice might exist
			if(thisController.getBlockOffset(probe.x(), probe.y(), probe.z()) == DRIVE) {
				formationChecklist = checkSlice(thisController, zOffset);
				if(!formationChecklist) {
					break;
				} else {
					slicesFound++;
					zOffset += 2;
				}
			}
		}
		
		if(this.mEnergyHatches.size() < 1) {
			System.out.println("At least one energy hatch is required!");
			formationChecklist = false;
		}
		
		if(this.mMaintenanceHatches.size() < 1) {
			System.out.println("You need a maintenance hatch to do maintenance.");
			formationChecklist = false;
		}
		
		if(formationChecklist) {
			slicesFound = sliceCount;
		}
		
		return formationChecklist;
	}
	
	public boolean checkSlice(IGregTechTileEntity thisController, int zOffset) {
		// Figure out the vector for the direction the back face of the controller is facing
		final Vector3ic forgeDirection = new Vector3i(
				ForgeDirection.getOrientation(thisController.getBackFacing()).offsetX,
				ForgeDirection.getOrientation(thisController.getBackFacing()).offsetY,
				ForgeDirection.getOrientation(thisController.getBackFacing()).offsetZ
				); 
		boolean formationChecklist = true;
		
		for(int Z = 0; Z <= 2; Z++) {
			if(Z != 2) {
				for(int X = -1; X <= 1; X++) {
					for(int Y = 0; Y < 5; Y++) {
						final Vector3ic offset = rotateOffsetVector(forgeDirection, X, Y, zOffset + Z);
						
						// Server rack roof is casings
						if(Y == 4) {
							// Get next TE
							
							IGregTechTileEntity currentTE = 
									thisController.getIGregTechTileEntityOffset(offset.x(), offset.y(), offset.z());
							
							if(!super.addMaintenanceToMachineList(currentTE, CASING_TEXTURE_ID)
									&& !super.addInputToMachineList(currentTE, CASING_TEXTURE_ID)
									&& !super.addEnergyInputToMachineList(currentTE, CASING_TEXTURE_ID)) {
								
								// If it's not a hatch, is it the right casing for this machine?
								// TODO: Also check IO port
								if(thisController.getBlockOffset(offset.x(), offset.y(), offset.z()) == CASING) {
									// yay
								} else if (thisController.getBlockOffset(offset.x(), offset.y(), offset.z()) == IO_PORT) {
									// TODO register port
								} else {
									formationChecklist = false;
								}
							}
						}
						
						// Middle wall is aluminium frame boxes
						else if(Y < 4 && X == 0) {
							if(!(thisController.getBlockOffset(offset.x(), offset.y(), offset.z()).getUnlocalizedName().equals(ALU_FRAME_BOX_NAME))) {
								formationChecklist = false;
							}
						}
						
						// Side walls are item server drives
						else if(Y < 4 && X != 0) {
							if(!(thisController.getBlockOffset(offset.x(), offset.y(), offset.z()) == DRIVE)) {
								formationChecklist = false;
							}
						}
					}
				}
			} else {
				// Back slice
				for(int X = -1; X <= 1; X++) {
					for(int Y = 0; Y < 5; Y++) {
						
						// Get next TE
						final Vector3ic offset = rotateOffsetVector(forgeDirection, X, Y, zOffset + Z);
						IGregTechTileEntity currentTE = 
								thisController.getIGregTechTileEntityOffset(offset.x(), offset.y(), offset.z());
						
						// Disallow nonsensical hatches in the middle of the structure
						if(Y < 4 && Y > 0 && X == 0) {
							if(!(thisController.getBlockOffset(offset.x(), offset.y(), offset.z()) == CASING)) {
								formationChecklist = false;
							}
						} else {
							if(!super.addMaintenanceToMachineList(currentTE, CASING_TEXTURE_ID)
									&& !super.addInputToMachineList(currentTE, CASING_TEXTURE_ID)
									&& !super.addEnergyInputToMachineList(currentTE, CASING_TEXTURE_ID)) {
								
								// If it's not a hatch, is it the right casing for this machine?
								// TODO: Also check IO port
								if(thisController.getBlockOffset(offset.x(), offset.y(), offset.z()) == CASING) {
									// yay
								} else if (thisController.getBlockOffset(offset.x(), offset.y(), offset.z()) == IO_PORT) {
									// TODO: register IO port
								} else {
									formationChecklist = false;
								}
							}
						}
					}
				}
			}
		}
		
		
		return formationChecklist;
	}
	
	@Override
	public String[] getInfoData() {
		final ArrayList<String> ll = new ArrayList<>();//mfh.getInfoData();
		
		ll.add(EnumChatFormatting.YELLOW + "Operational Data:" + EnumChatFormatting.RESET);
		ll.add("Per-Fluid Capacity: " + BASE_PER_ITEM_CAPACITY);
		ll.add("Running Cost: " + (-super.mEUt) + "EU/t");
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
		
		super.saveNBTData(nbt);
	}
	
	@Override
	public void loadNBTData(NBTTagCompound nbt) {
		nbt = (nbt == null) ? new NBTTagCompound() : nbt;
		
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

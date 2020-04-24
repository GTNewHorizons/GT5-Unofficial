package common.tileentities;

import java.util.ArrayList;
import java.util.HashSet;

import org.lwjgl.input.Keyboard;

import common.blocks.Block_ItemServerDrive;
import common.blocks.Block_ItemServerIOPort;
import common.blocks.Block_ItemServerRackCasing;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_RenderedTexture;
import kekztech.MultiItemHandler;
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
	private final Block_ItemServerRackCasing CASING = Block_ItemServerRackCasing.getInstance();
	private final Block_ItemServerIOPort IO_PORT = Block_ItemServerIOPort.getInstance();
	private final String ALU_FRAME_BOX_NAME = "gt.blockmachines";
	private final int ALU_FRAME_BOX_META = 6;//4115;
	private final int CASING_TEXTURE_ID = 176;
	
	private MultiItemHandler mih;
	private HashSet<TE_ItemServerIOPort> ioPorts = new HashSet<>(); 
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
		b.addInfo("[W.I.P - Probably doesn't work]")
				.addInfo("High-Tech item storage!")
				.addInfo("Variable length: Slices 2-4 can be repeated as long as the total length does not exceed 16 blocks.")
				.addInfo("Each slice offers storage for 128 item types")
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
				.addOtherStructurePart("2nd and 3rd slice, center", "1x4x1 Aluminium Frame Box")
				.addOtherStructurePart("2nd and 3rd slice, top", "3x1x1 Item Server Rack Casing")
				.addOtherStructurePart("2nd and 3rd slice, sides", "2x 1x4x1 Item Server Drive")
				.addOtherStructurePart("Back slice", "3x5x1 Item Server Rack Casing")
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
		this.mEUt = (int) -(BASE_SLICE_ENERGY_COST * sliceCount * Math.pow(2, config));
		super.mMaxProgresstime = 20;
		
		mih.setPerTypeCapacity((int) (BASE_PER_ITEM_CAPACITY * Math.pow(4, config)));
		
		return true;
	}
	
	@Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		super.onPostTick(aBaseMetaTileEntity, aTick);
		
		if(mih != null) {
			mih.setLock(!super.getBaseMetaTileEntity().isActive());
		}
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
			for(int Y = 0; Y <= 4; Y++) {
				if(X == 0 && Y == 0) {
					continue; // is controller
				}
				
				// Get next TE
				final Vector3ic offset = rotateOffsetVector(forgeDirection, X, Y, 0);
				IGregTechTileEntity currentTE = 
						thisController.getIGregTechTileEntityOffset(offset.x(), offset.y(), offset.z());
				
				if(!super.addMaintenanceToMachineList(currentTE, CASING_TEXTURE_ID)
						&& !super.addEnergyInputToMachineList(currentTE, CASING_TEXTURE_ID)) {
					
					// Is casing or IO port?
					if(thisController.getBlockOffset(offset.x(), offset.y(), offset.z()) == CASING) {
						// Is casing, but there's no casing requirements
					} else if (thisController.getBlockOffset(offset.x(), offset.y(), offset.z()) == IO_PORT) {
						final TE_ItemServerIOPort port = 
								(TE_ItemServerIOPort) thisController.getWorld().getTileEntity(
										thisController.getXCoord() + offset.x(), 
										thisController.getYCoord() + offset.y(),
										thisController.getZCoord() + offset.z());
						ioPorts.add(port);
					} else {
						formationChecklist = false;
					}
				}
			}
		}
		
		if(formationChecklist) {
			System.out.println("Item Server front slice approved");
		}
		
		// Check slices
		int segmentsFound = 0;
		int zOffset = -1; // -1 is the first slice after the front one. It goes in negative direction.
		
		while(segmentsFound < 5) {
			if(checkSegment(thisController, forgeDirection, zOffset)) {
				segmentsFound++;
				zOffset -= 3; // Each segment is 3 blocks long, so progress Z by -3
				
				System.out.println("Item Server segment approved: " + segmentsFound);
			} else {
				System.out.println("Item Server segment rejected: " + (segmentsFound + 1));
				break;
			}
		}
		
		if(segmentsFound < 1) {
			System.out.println("At least one slice required for storage");
			formationChecklist = false;
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
			sliceCount = segmentsFound;
			
			if(mih == null) {
				mih = new MultiItemHandler();
				mih.setItemTypeCapacity(segmentsFound * BASE_ITEM_TYPES_PER_SLICE);
			}
			System.out.println("Configuring " + ioPorts.size() + " ports");
			for(TE_ItemServerIOPort port : ioPorts) {
				port.setMultiItemHandler(mih);
			}
		}
		
		return formationChecklist;
	}
	
	public boolean checkSegment(IGregTechTileEntity thisController, Vector3ic forgeDirection, int zOffset) {
		boolean formationChecklist = true;
		// Slice by slice
		for(int Z = 0; Z >= -2; Z--) {
			// Is not back slice
			if(Z != -2) {
				// Left to right
				for(int X = -1; X <= 1; X++) {
					// Bottom to top
					for(int Y = 0; Y <= 4; Y++) {
						final Vector3ic offset = rotateOffsetVector(forgeDirection, X, Y, zOffset + Z);
						
						// Server rack roof
						if(Y == 4) {
							final IGregTechTileEntity currentTE = 
									thisController.getIGregTechTileEntityOffset(offset.x(), offset.y(), offset.z());
							
							if(!super.addMaintenanceToMachineList(currentTE, CASING_TEXTURE_ID)
									&& !super.addEnergyInputToMachineList(currentTE, CASING_TEXTURE_ID)) {
								// Is casing or IO port?
								if(thisController.getBlockOffset(offset.x(), offset.y(), offset.z()) == CASING) {
									// Is casing, but there's no casing requirements
								} else if (thisController.getBlockOffset(offset.x(), offset.y(), offset.z()) == IO_PORT) {
									final TE_ItemServerIOPort port = 
											(TE_ItemServerIOPort) thisController.getWorld().getTileEntity(
													thisController.getXCoord() + offset.x(), 
													thisController.getYCoord() + offset.y(),
													thisController.getZCoord() + offset.z());
									ioPorts.add(port);
								} else {
									formationChecklist = false;
								}
							}
						}
						
						// Middle wall is aluminium frame boxes
						else if(Y <= 3 && X == 0) {
							if(!(thisController.getBlockOffset(offset.x(), offset.y(), offset.z()).getUnlocalizedName().equals(ALU_FRAME_BOX_NAME))
									|| !(thisController.getMetaIDOffset(offset.x(), offset.y(), offset.z()) == ALU_FRAME_BOX_META)) {
								System.out.println("Rejected Frame box: " 
										+ thisController.getBlockOffset(offset.x(), offset.y(), offset.z()).getUnlocalizedName()
										+ ":"
										+ thisController.getMetaIDOffset(offset.x(), offset.y(), offset.z()));
								formationChecklist = false;
							}
						}
						
						// Side walls are item server drives
						else if(Y <= 3 && X != 0) {
							if(!(thisController.getBlockOffset(offset.x(), offset.y(), offset.z()) == DRIVE)) {
								formationChecklist = false;
							}
						}
					}
				}
			} else {
				// Back slice
				for(int X = -1; X <= 1; X++) {
					for(int Y = 0; Y <= 4; Y++) {
						
						final Vector3ic offset = rotateOffsetVector(forgeDirection, X, Y, zOffset + Z);
						IGregTechTileEntity currentTE = 
								thisController.getIGregTechTileEntityOffset(offset.x(), offset.y(), offset.z());
						
						if(!super.addMaintenanceToMachineList(currentTE, CASING_TEXTURE_ID)
								&& !super.addEnergyInputToMachineList(currentTE, CASING_TEXTURE_ID)) {
							// Is casing or IO port?
							if(thisController.getBlockOffset(offset.x(), offset.y(), offset.z()) == CASING) {
								// Is casing, but there's no casing requirements
							} else if (thisController.getBlockOffset(offset.x(), offset.y(), offset.z()) == IO_PORT) {
								final TE_ItemServerIOPort port = 
										(TE_ItemServerIOPort) thisController.getWorld().getTileEntity(
												thisController.getXCoord() + offset.x(), 
												thisController.getYCoord() + offset.y(),
												thisController.getZCoord() + offset.z());
								ioPorts.add(port);
							} else {
								formationChecklist = false;
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
		ll.add("Per-Item Capacity: " + mih.getPerTypeCapacity());
		ll.add("Item-Type Capacity: " + BASE_ITEM_TYPES_PER_SLICE * sliceCount);
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

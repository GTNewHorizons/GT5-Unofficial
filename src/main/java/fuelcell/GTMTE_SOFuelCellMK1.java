package fuelcell;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
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
import reactor.GUIContainer_ModularNuclearReactor;

public class GTMTE_SOFuelCellMK1  extends GT_MetaTileEntity_MultiBlockBase {
	
	final Block CASING = GregTech_API.sBlockCasings4;
	final int CASING_META = 1;
	final int CASING_TEXTURE_ID = 49;
	
	public GTMTE_SOFuelCellMK1(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
		
	}

	public GTMTE_SOFuelCellMK1(String aName) {
		super(aName);
		
	}
	
	@Override
	public IMetaTileEntity newMetaEntity(IGregTechTileEntity var1) {
		return new GTMTE_SOFuelCellMK1(super.mName);
	}

	@Override
	public String[] getDescription() {
		return new String[] { 
				"Oxidizes gas fuels to generate electricity without polluting the environment",
				"29,480EU worth of fuel are consumed each second",
				"Outputs 1024EU/t and 18,000L/s Steam",
				"Additionally requires 360L/s Oxygen gas",
				"------------------------------------------",
				"Dimensions: 3x3x5 (WxHxL)",
				"Structure:",
				"   3x YSZ Ceramic Electrolyte Unit (center 1x1x3)",
				"   12x Clean Stainless Steel Machine Casing (at least)",
				"   Controller front center",
				"   Dynamo Hatch back center",
				"   Maintenance Hatch, Input Hatches, Output Hatches"
				};	
	}
	
	//TODO
	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing,
			final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		return aSide == aFacing
				? new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[CASING_TEXTURE_ID],
						new GT_RenderedTexture(aActive ? 
								Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER_ACTIVE 
								: Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER)}
				: new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[CASING_TEXTURE_ID]};
	}
	
	//TODO
	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(),
				"LargeTurbine.png");
	}
	
	@Override
	public boolean isCorrectMachinePart(ItemStack stack) {
		return true;
	}

	@Override
	public boolean checkRecipe(ItemStack stack) {
		return false;
	}
	
	//TODO
	@Override
	public boolean checkMachine(IGregTechTileEntity thisController, ItemStack guiSlotItem) {		
		
		final int XDIR_BACKFACE = ForgeDirection.getOrientation(thisController.getBackFacing()).offsetX;
		final int ZDIR_BACKFACE = ForgeDirection.getOrientation(thisController.getBackFacing()).offsetZ;

		int minCasingAmount = 12; 
		boolean checklist = true; // if this is still true at the end, machine is good to go :)
		
		// Front slice
		for(int X = -1; X <= 1; X++) {
			for(int Y = -1; Y <= 1; Y++) {
				if(X == 0 && Y == 0) {
					continue; // is controller
				}
				// Get next TE
				final int THIS_X = XDIR_BACKFACE + X;
				final int THIS_Z = ZDIR_BACKFACE + -1;
				IGregTechTileEntity currentTE = 
						thisController.getIGregTechTileEntityOffset(THIS_X, Y, THIS_Z);// x, y ,z
				
				// Tries to add TE as either of those kinds of hatches.
				// The number is the texture index number for the texture that needs to be painted over the hatch texture (TAE for GT++)
				if (   !super.addMaintenanceToMachineList(currentTE, CASING_TEXTURE_ID) 
					&& !super.addInputToMachineList(currentTE, CASING_TEXTURE_ID)
					&& !super.addOutputToMachineList(currentTE, CASING_TEXTURE_ID)) {
					
					// If it's not a hatch, is it the right casing for this machine? Check block and block meta.
					if ((thisController.getBlockOffset(THIS_X, Y, THIS_Z) == CASING) && (thisController.getMetaIDOffset(THIS_X, Y, THIS_Z) == CASING_META)) {
						// Seems to be valid casing. Decrement counter.
						minCasingAmount--;
					} else {
						checklist = false;
					}
				}
			}
		}
		System.out.println("Front slice status: " +checklist +" / casings left of 12: " +minCasingAmount);
		
		// Middle three slices
		for(int X = -1; X <= 1; X++) {
			for(int Y = -1; Y <= 1; Y++) {
				for(int Z = 0; Z < 3; Z++) {
					final int THIS_X = XDIR_BACKFACE + X;
					final int THIS_Z = ZDIR_BACKFACE + Z;
					if(X == 0 && Y == 0) {
						if(!thisController.getBlockOffset(THIS_X, 0, THIS_Z).getUnlocalizedName()
								.equals("kekztech_yszceramicelectrolyteunit_block")) {
							checklist = false;
							System.out.println("Expected YSZ Ceramic");
						}
						continue;
					}
					if(Y == 0 && (X == -1 || X == 1)) {
						if(!thisController.getBlockOffset(XDIR_BACKFACE, 0, ZDIR_BACKFACE * Z).getUnlocalizedName()
								.equals("blockAlloyGlass")) {
							checklist = false;
							System.out.println("Expected Reinforced Glass");
						}
						continue;
					}
					// Get next TE
					IGregTechTileEntity currentTE = 
							thisController.getIGregTechTileEntityOffset(THIS_X, Y, THIS_Z);// x, y ,z
					
					// Tries to add TE as either of those kinds of hatches.
					// The number is the texture index number for the texture that needs to be painted over the hatch texture (TAE for GT++)
					if (   !super.addMaintenanceToMachineList(currentTE, CASING_TEXTURE_ID) 
						&& !super.addInputToMachineList(currentTE, CASING_TEXTURE_ID)
						&& !super.addOutputToMachineList(currentTE, CASING_TEXTURE_ID)) {
						
						// If it's not a hatch, is it the right casing for this machine? Check block and block meta.
						if ((thisController.getBlockOffset(THIS_X, Y, THIS_Z) == CASING) && (thisController.getMetaIDOffset(THIS_X, Y, THIS_Z) == CASING_META)) {
							// Seems to be valid casing. Decrement counter.
							minCasingAmount--;
						} else {
							checklist = false;
						}
					}
				}
			}
		}
		System.out.println("Middle slices status: " +checklist);
		
		// Back slice
		for(int X = -1; X <= 1; X++) {
			for(int Y = -1; Y <= 1; Y++) {
				// Get next TE
				final int THIS_X = XDIR_BACKFACE + X;
				final int THIS_Z = ZDIR_BACKFACE + 3;
				IGregTechTileEntity currentTE = 
						thisController.getIGregTechTileEntityOffset(THIS_X, Y, THIS_Z);// x, y ,z
				
				// Tries to add TE as either of those kinds of hatches.
				// The number is the texture index number for the texture that needs to be painted over the hatch texture (TAE for GT++)
				if (   !super.addMaintenanceToMachineList(currentTE, CASING_TEXTURE_ID) 
					&& !super.addInputToMachineList(currentTE, CASING_TEXTURE_ID)
					&& !super.addOutputToMachineList(currentTE, CASING_TEXTURE_ID)
					&& !super.addDynamoToMachineList(currentTE, CASING_TEXTURE_ID)) {
					
					// If it's not a hatch, is it the right casing for this machine? Check block and block meta.
					if ((thisController.getBlockOffset(THIS_X, Y, THIS_Z) == CASING) && (thisController.getMetaIDOffset(THIS_X, Y, THIS_Z) == CASING_META)) {
						// Seems to be valid casing. Decrement counter.
						minCasingAmount--;
					} else {
						checklist = false;
					}
				}
			}
		}
		System.out.println("Back slice status: " +checklist);
		
		if(minCasingAmount > 0) {
			checklist = false;
		}
		
		if(this.mDynamoHatches.size() < 1) {
			System.out.println("At least one dynamo hatch is required!");
			checklist = false;
		}
		if(this.mInputHatches.size() < 2) {
			System.out.println("At least two input hatches are required!");
			checklist = false;
		}
		
		return checklist;
	}
	
	@Override
	public int getMaxEfficiency(ItemStack stack) {
		return 10000;
	}

	@Override
	public int getPollutionPerTick(ItemStack stack) {
		return 0;
	}

	@Override
	public int getDamageToComponent(ItemStack stack) {
		return 0;
	}

	@Override
	public boolean explodesOnComponentBreak(ItemStack stack) {
		return false;
	}
	
}

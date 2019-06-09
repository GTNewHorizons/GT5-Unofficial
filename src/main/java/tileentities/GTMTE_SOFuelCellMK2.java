package tileentities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import blocks.Block_GDCUnit;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class GTMTE_SOFuelCellMK2  extends GT_MetaTileEntity_MultiBlockBase {
	
	final Block CASING = GregTech_API.sBlockCasings4;
	final int CASING_META = 0;
	final int CASING_TEXTURE_ID = 48;
	
	private final int OXYGEN_PER_TICK = 100;
	private final int EU_PER_TICK = 24576; // 100% Efficiency, 3A IV
	private final int STEAM_PER_TICK = 4800; // SH Steam (10,800EU/t @ 150% Efficiency) 
	
	public GTMTE_SOFuelCellMK2(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
		
	}

	public GTMTE_SOFuelCellMK2(String aName) {
		super(aName);
		
	}
	
	@Override
	public IMetaTileEntity newMetaEntity(IGregTechTileEntity var1) {
		return new GTMTE_SOFuelCellMK2(super.mName);
	}

	@Override
	public String[] getDescription() {
		return new String[] { 
				"Oxidizes gas fuels to generate electricity without polluting the environment",
				"Consumes 442,200EU worth of fuel with up to 160% efficiency each second",
				"Steam production requires the SOFC to heat up completely first",
				"Outputs " + EU_PER_TICK + "EU/t and " + STEAM_PER_TICK + "L/t Superheated Steam",
				"Additionally requires " + OXYGEN_PER_TICK + "L/t Oxygen gas",
				"------------------------------------------",
				"Dimensions: 3x3x5 (WxHxL)",
				"Structure:",
				"   3x GDC Ceramic Electrolyte Unit (center 1x1x3)",
				"   12x Robust Tungstensteel Machine Casing (at least)",
				"   Controller front center",
				"   Dynamo Hatch back center",
				"   Maintenance Hatch, Input Hatches, Output Hatches"
				};	
	}
	
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
		final ArrayList<FluidStack> storedFluids = super.getStoredFluids();
		Collection<GT_Recipe> recipeList = GT_Recipe_Map.sTurbineFuels.mRecipeList;
		
		if((storedFluids.size() > 0 && recipeList != null)) {
						
			final Iterator<FluidStack> fluidsIterator = storedFluids.iterator();
			while(fluidsIterator.hasNext()) {
				
				final FluidStack hatchFluid = fluidsIterator.next();
				final Iterator<GT_Recipe> recipeIterator = recipeList.iterator();
				while(recipeIterator.hasNext()) {
					
					final GT_Recipe aFuel = recipeIterator.next();
					FluidStack liquid;
					if((liquid = GT_Utility.getFluidForFilledItem(aFuel.getRepresentativeInput(0), true)) != null
							&& hatchFluid.isFluidEqual(liquid)) {
						
						liquid.amount = EU_PER_TICK / aFuel.mSpecialValue;
						
						if(super.depleteInput(liquid)) {
							
							if(!super.depleteInput(Materials.Oxygen.getGas(OXYGEN_PER_TICK))) {
								super.mEUt = 0;
								super.mEfficiency = 0;
								return false;
							}
							
							super.mEUt = EU_PER_TICK;
							super.mProgresstime = 1;
							super.mMaxProgresstime = 1;
							super.mEfficiencyIncrease = 20;
							if(super.mEfficiency == getMaxEfficiency(null)) {
								super.addOutput(FluidRegistry.getFluidStack("ic2superheatedsteam", STEAM_PER_TICK));
							}
							return true;
						}
					}
				}
			}			
		}
		
		super.mEUt = 0;
		super.mEfficiency = 0;
		return false;
	}
	
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
		
		// Middle three slices
		for(int X = -1; X <= 1; X++) {
			for(int Y = -1; Y <= 1; Y++) {
				for(int Z = 0; Z < 3; Z++) {
					final int THIS_X = XDIR_BACKFACE + X;
					final int THIS_Z = ZDIR_BACKFACE + Z;
					if(X == 0 && Y == 0) {
						if(!thisController.getBlockOffset(THIS_X, 0, THIS_Z).getUnlocalizedName()
								.equals(Block_GDCUnit.getInstance().getUnlocalizedName())) {
							checklist = false;
						}
						continue;
					}
					if(Y == 0 && (X == -1 || X == 1)) {
						if(!thisController.getBlockOffset(THIS_X, 0, THIS_Z).getUnlocalizedName()
								.equals("blockAlloyGlass")) {
							checklist = false;
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

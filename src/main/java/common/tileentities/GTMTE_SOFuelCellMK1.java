package common.tileentities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.lwjgl.input.Keyboard;

import common.Blocks;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import util.Vector3i;
import util.Vector3ic;

public class GTMTE_SOFuelCellMK1  extends GT_MetaTileEntity_MultiBlockBase {
	
	private final Block CASING = GregTech_API.sBlockCasings4;
	private final int CASING_META = 1;
	private final int CASING_TEXTURE_ID = 49;
	
	private final int OXYGEN_PER_SEC = 400;
	private final int EU_PER_TICK = 1024;
	private final int STEAM_PER_SEC = 18000;
	
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
		final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
		tt.addMachineType("Gas Turbine")
		.addInfo("Oxidizes gas fuels to generate electricity without polluting the environment")
		.addInfo("Consumes 29,480EU worth of fuel with up to 97% efficiency each second")
		.addInfo("Steam production requires the SOFC to heat up completely first")
		.addInfo("Outputs " + EU_PER_TICK + "EU/t and " + STEAM_PER_SEC + "L/s Steam")
		.addInfo("Additionally, requires " + OXYGEN_PER_SEC + "L/s Oxygen gas")
		.addSeparator()
		.beginStructureBlock(3, 3, 5, false)
		.addController("Front center")
		.addCasingInfo("Clean Stainless Steel Casing", 12)
		.addOtherStructurePart("YSZ Ceramic Electrolyte Unit", "3x, Center 1x1x3")
		.addOtherStructurePart("Reinforced Glass", "6x, touching the electrolyte units on the horizontal sides")
		.addDynamoHatch("Back center")
		.addMaintenanceHatch("Any casing")
		.addInputHatch("Fuel, any casing")
		.addInputHatch("Oxygen, any casing")
		.addOutputHatch("Steam, any casing")
		.toolTipFinisher("KekzTech");
		if(!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			return tt.getInformation();
		} else {
			return tt.getStructureInformation();
		}
	}
	
	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing,
			final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		return aSide == aFacing
				? new ITexture[]{Textures.BlockIcons.getCasingTextureForId(CASING_TEXTURE_ID),
						new GT_RenderedTexture(aActive ? 
								Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER_ACTIVE 
								: Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER)}
				: new ITexture[]{Textures.BlockIcons.getCasingTextureForId(CASING_TEXTURE_ID)};
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
						
						liquid.amount = Math.round((EU_PER_TICK * 20) / aFuel.mSpecialValue);
						
						if(super.depleteInput(liquid)) {
							
							if(!super.depleteInput(Materials.Oxygen.getGas(OXYGEN_PER_SEC))) {
								super.mEUt = 0;
								super.mEfficiency = 0;
								return false;
							}
							
							super.mEUt = EU_PER_TICK;
							super.mMaxProgresstime = 20;
							super.mEfficiencyIncrease = 40;
							if(super.mEfficiency == getMaxEfficiency(null)) {
								super.addOutput(GT_ModHandler.getSteam(STEAM_PER_SEC));
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
		int minCasingAmount = 12; 
		boolean formationChecklist = true; // if this is still true at the end, machine is good to go :)
		
		// Front slice			
		for(int X = -1; X <= 1; X++) {
			for(int Y = -1; Y <= 1; Y++) {
				if(X == 0 && Y == 0) {
					continue; // is controller
				}
				
				// Get next TE
				final Vector3ic offset = rotateOffsetVector(forgeDirection, X, Y, 0);
				IGregTechTileEntity currentTE = 
						thisController.getIGregTechTileEntityOffset(offset.x(), offset.y(), offset.z());
				
				// Tries to add TE as either of those kinds of hatches.
				// The number is the texture index number for the texture that needs to be painted over the hatch texture (TAE for GT++)
				if (   !super.addMaintenanceToMachineList(currentTE, CASING_TEXTURE_ID) 
					&& !super.addInputToMachineList(currentTE, CASING_TEXTURE_ID)
					&& !super.addOutputToMachineList(currentTE, CASING_TEXTURE_ID)) {
					
					// If it's not a hatch, is it the right casing for this machine? Check block and block meta.
					if ((thisController.getBlockOffset(offset.x(), offset.y(), offset.z()) == CASING) 
							&& (thisController.getMetaIDOffset(offset.x(), offset.y(), offset.z()) == CASING_META)) {
						// Seems to be valid casing. Decrement counter.
						minCasingAmount--;
					} else {
						formationChecklist = false;
					}
				}
			}
		}
		
		// Middle three slices
		for(int X = -1; X <= 1; X++) {
			for(int Y = -1; Y <= 1; Y++) {
				for(int Z = -1; Z >= -3; Z--) {
					final Vector3ic offset = rotateOffsetVector(forgeDirection, X, Y, Z);
					if(X == 0 && Y == 0) {
						if(!thisController.getBlockOffset(offset.x(), offset.y(), offset.z()).getUnlocalizedName()
								.equals(Blocks.yszUnit.getUnlocalizedName())) {
							formationChecklist = false;
						}
						continue;
					}
					if(Y == 0 && (X == -1 || X == 1)) {
						if(!thisController.getBlockOffset(offset.x(), offset.y(), offset.z()).getUnlocalizedName()
								.equals("blockAlloyGlass")) {
							formationChecklist = false;
						}
						continue;
					}
					// Get next TE
					IGregTechTileEntity currentTE = 
							thisController.getIGregTechTileEntityOffset(offset.x(), offset.y(), offset.z());// x, y ,z
					
					// Tries to add TE as either of those kinds of hatches.
					// The number is the texture index number for the texture that needs to be painted over the hatch texture (TAE for GT++)
					if (   !super.addMaintenanceToMachineList(currentTE, CASING_TEXTURE_ID) 
						&& !super.addInputToMachineList(currentTE, CASING_TEXTURE_ID)
						&& !super.addOutputToMachineList(currentTE, CASING_TEXTURE_ID)) {
						
						// If it's not a hatch, is it the right casing for this machine? Check block and block meta.
						if ((thisController.getBlockOffset(offset.x(), offset.y(), offset.z()) == CASING) 
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
		
		// Back slice
		for(int X = -1; X <= 1; X++) {
			for(int Y = -1; Y <= 1; Y++) {
				// Get next TE
				final Vector3ic offset = rotateOffsetVector(forgeDirection, X, Y, -4);
				IGregTechTileEntity currentTE = 
						thisController.getIGregTechTileEntityOffset(offset.x(), offset.y(), offset.z());// x, y ,z
				
				// Tries to add TE as either of those kinds of hatches.
				// The number is the texture index number for the texture that needs to be painted over the hatch texture (TAE for GT++)
				if (   !super.addMaintenanceToMachineList(currentTE, CASING_TEXTURE_ID) 
					&& !super.addInputToMachineList(currentTE, CASING_TEXTURE_ID)
					&& !super.addOutputToMachineList(currentTE, CASING_TEXTURE_ID)
					&& !super.addDynamoToMachineList(currentTE, CASING_TEXTURE_ID)) {
					
					// If it's not a hatch, is it the right casing for this machine? Check block and block meta.
					if ((thisController.getBlockOffset(offset.x(), offset.y(), offset.z()) == CASING) 
							&& (thisController.getMetaIDOffset(offset.x(), offset.y(), offset.z()) == CASING_META)) {
						// Seems to be valid casing. Decrement counter.
						minCasingAmount--;
					} else {
						formationChecklist = false;
					}
				}
			}
		}		
		
		if(minCasingAmount > 0) {
			formationChecklist = false;
		}
		
		if(this.mDynamoHatches.size() != 1) {
			System.out.println("Exactly one dynamo hatch is required!");
			formationChecklist = false;
		}
		if(this.mInputHatches.size() < 2) {
			System.out.println("At least two input hatches are required!");
			formationChecklist = false;
		}
		
		if(this.mMaintenanceHatches.size() < 1) {
			System.out.println("You need a maintenance hatch to do maintenance.");
		}
		
		return formationChecklist;
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

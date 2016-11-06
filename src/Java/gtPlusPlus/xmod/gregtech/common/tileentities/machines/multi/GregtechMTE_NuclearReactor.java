package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Config;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_MultiMachine;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

public class GregtechMTE_NuclearReactor extends GT_MetaTileEntity_MultiBlockBase {

	public static int sUUAperUUM = 1;
	public static int sUUASpeedBonus = 4;
	public static int sDurationMultiplier = 3215;
	public static boolean sRequiresUUA = false;
	private int recipeCounter = 0;
	private static Block IC2Glass = Block.getBlockFromItem(ItemUtils.getItem("IC2:blockAlloyGlass"));
	//public FluidStack mFluidOut = Materials.UUMatter.getFluid(1L);

	public GregtechMTE_NuclearReactor(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMTE_NuclearReactor(String aName) {
		super(aName);
	}

	@Override
	public String[] getDescription() {
		return new String[]{
				"Controller Block for the Nuclear Reactor",
				"Produces heat from Radioactive beta decay.",
				"Size(WxHxD): 7x4x7, Controller (Bottom, Center)",				
				CORE.GT_Tooltip};
	}

	@Override
	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[70],
					new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_REPLICATOR_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_REPLICATOR)};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[70]};
	}

	@Override
	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GUI_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "MatterFabricator.png");
	}

	@Override
	public void onConfigLoad(GT_Config aConfig) {
	}

	@Override
	public boolean checkRecipe(ItemStack aStack) {		
		return false;
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {

		// Life Lessons from Greg.
		/**
		[23:41:15] <GregoriusTechneticies> xdir and zdir are x2 and not x3
		[23:41:26] <GregoriusTechneticies> thats you issue
		[23:44:33] <Alkalus> mmm?
		[23:44:49] <Alkalus> Should they be x3?
		[23:44:50] <GregoriusTechneticies> you just do a x2, what is for a 5x5 multiblock
		[23:45:01] <GregoriusTechneticies> x3 is for a 7x7 one
		[23:45:06] <Alkalus> I have no idea what that value does, tbh..
		[23:45:15] <GregoriusTechneticies> its the offset
		[23:45:23] <Alkalus> Debugging checkMachine has been a pain and I usually trash designs that don't work straight up..
		[23:45:28] <GregoriusTechneticies> it determines the horizontal middle of the multiblock
		[23:45:47] <GregoriusTechneticies> which is in your case THREE blocks away from the controller
		[23:45:51] <Alkalus> Ahh
		[23:45:57] <GregoriusTechneticies> and not 2
		[23:46:06] <Alkalus> Noted, thanks :D
		 */

		int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * 3; 
		int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * 3;

		for (int i = -3; i <= 3; i++) {
			for (int j = -3; j <= 3; j++) {
				for (int h = 0; h < 4; h++) {
					IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, h, zDir + j);

					// Reactor Floor/Roof inner 5x5
					if ((i != -3 && i != 3) && (j != -3 && j != 3)) {
						
						// Reactor Floor & Roof (Inner 5x5) + Mufflers, Dynamos and Fluid outputs.
						if (h == 0 || h == 3) {
							
							//If not a hatch, continue, else add hatch and continue.
							if ((!addMufflerToMachineList(tTileEntity, 70)) && (!addOutputToMachineList(tTileEntity, 70)) && (!addDynamoToMachineList(tTileEntity, 70))) {
								if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
									Utils.LOG_INFO("Matter Fabricator Casings Missing from one of the top layers inner 3x3.");
									Utils.LOG_INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
									aBaseMetaTileEntity.getWorld().setBlock(
											(aBaseMetaTileEntity.getXCoord()+(xDir+i)),
											(aBaseMetaTileEntity.getYCoord()+(h)),
											(aBaseMetaTileEntity.getZCoord()+(zDir+j)),
											Blocks.melon_block);
									return false;
								}
								if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 12) {
									Utils.LOG_INFO("Matter Fabricator Casings Missing from one of the top layers inner 3x3. Wrong Meta for Casing.");
									return false;
								}
							}	
						} 
						
						// Inside 2 layers, mostly air
						else {		
							
							// Reactor Inner 5x5
							//if ((i != -1 && i != 1) && (j != -1 && j != 1)) {
							if (!aBaseMetaTileEntity.getAirOffset(xDir + i, h, zDir + j)) {
								Utils.LOG_INFO("Make sure the inner 3x3 of the Multiblock is Air.");
								aBaseMetaTileEntity.getWorld().setBlock(
										(aBaseMetaTileEntity.getXCoord()+(xDir+i)),
										(aBaseMetaTileEntity.getYCoord()+(h)),
										(aBaseMetaTileEntity.getZCoord()+(zDir+j)),
										Blocks.melon_block);
								return false;
							}
							
						}
						
							//TODO - Add Caron Moderation Rods
							/*
							else { //carbon moderation rods are at 1,1 & -1,-1 & 1,-1 & -1,1
								if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
									Utils.LOG_INFO("Matter Fabricator Casings Missing from one of the top layers inner 3x3.");
									Utils.LOG_INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
									return false;
								}
								if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 12) {
									Utils.LOG_INFO("Matter Fabricator Casings Missing from one of the top layers inner 3x3.");
									return false;
								}
							}*/						
						
					}

					//Dealt with inner 5x5, now deal with the exterior.
					else {
						
						//Deal with all 4 sides (Reactor walls)
						if (h == 1 || h == 2) {														
							if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
								Utils.LOG_INFO("Glass Casings Missing from somewhere in the second layer.");
								Utils.LOG_INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
								return false;
							}
							if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 13) {
								Utils.LOG_INFO("Glass Casings Missing from somewhere in the second layer.");
								return false;
							}							
						}
						
						//Deal with top and Bottom edges (Inner 5x5)
						else if (h == 0 || h == 3) {
							if ((!addMaintenanceToMachineList(tTileEntity, 70)) && (!addInputToMachineList(tTileEntity, 70)) && (!addOutputToMachineList(tTileEntity, 70)) && (!addDynamoToMachineList(tTileEntity, 70))) {
								if ((xDir + i != 0) || (zDir + j != 0)) {//no controller
									if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
										Utils.LOG_INFO("Matter Fabricator Casings Missing from one of the edges on the top layer.");
										Utils.LOG_INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
										return false;
									}
									if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 12) {
										Utils.LOG_INFO("Matter Fabricator Casings Missing from one of the edges on the top layer.");
										return false;
									}
								}
							}
						}
					}
				}
			}
		}
		
		if (this.mMufflerHatches.size() != 4){
			Utils.LOG_INFO("You require EXACTLY 4 muffler hatches on top. FOUR.");
			return false;
		}
		
		Utils.LOG_INFO("Multiblock Formed.");
		return true;
	}

	@Override
	public boolean isCorrectMachinePart(ItemStack aStack) {
		return true;
	}

	@Override
	public int getMaxEfficiency(ItemStack aStack) {
		return 10000;
	}

	@Override
	public int getPollutionPerTick(ItemStack aStack) {
		return 0;
	}

	@Override
	public int getDamageToComponent(ItemStack aStack) {
		return 0;
	}

	@Override
	public int getAmountOfOutputs() {
		return 1;
	}

	@Override
	public boolean explodesOnComponentBreak(ItemStack aStack) {
		return false;
	}

	@Override
	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GregtechMTE_NuclearReactor(this.mName);
	}

}
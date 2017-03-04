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
import gtPlusPlus.xmod.gregtech.api.gui.GUI_MultiMachine;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

public class GregtechMetaTileEntity_PowerSubStationController extends GT_MetaTileEntity_MultiBlockBase {

	private final int recipeCounter = 0;

	public GregtechMetaTileEntity_PowerSubStationController(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntity_PowerSubStationController(final String aName) {
		super(aName);
	}

	@Override
	public String[] getDescription() {
		return new String[]{
				"Controller Block for the Power Sub-Station",
				"Stores quite a lot of power.",
				"Size(WxHxD): 1x5x1, Controller (One above the Bottom)",
				CORE.GT_Tooltip};
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[67],
					new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_LARGE_BOILER_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_LARGE_BOILER)};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[67]};
	}

	@Override
	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GUI_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(), "MatterFabricator.png");
	}

	@Override
	public void onConfigLoad(final GT_Config aConfig) {
		super.onConfigLoad(aConfig);
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {
		return false;
	}

	@Override
	public boolean checkMachine(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {
		final int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
		final int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
		for (int i = -2; i < 3; i++) {
			for (int j = -2; j < 3; j++) {
				for (int h = 0; h < 4; h++) {

					//Utils.LOG_INFO("Logging Variables - xDir:"+xDir+" zDir:"+zDir+" h:"+h+" i:"+i+" j:"+j);

					final IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, h, zDir + j);
					/*if (tTileEntity != Block.getBlockFromItem(UtilsItems.getItem("IC2:blockAlloyGlass"))) {
							Utils.LOG_INFO("h:"+h+" i:"+i+" j:"+j);
							double tX = tTileEntity.getXCoord();
							double tY = tTileEntity.getYCoord();
							double tZ = tTileEntity.getZCoord();
							Utils.LOG_INFO("Found Glass at X:"+tX+" Y:"+tY+" Z:"+tZ);
							//return false;
						}*/
					if (((i != -2) && (i != 2)) && ((j != -2) && (j != 2))) {// innerer 3x3 ohne h�he
						if ((h == 0) && (i != 0) && (j != 0)) {// innen boden (kantal coils)
							if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
								Utils.LOG_INFO("Matter Generation Coils missings from the bottom layer, inner 3x3. - Wrong Block");
								Utils.LOG_INFO("Found: "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getUnlocalizedName()+" at x:"+(xDir + i)+" y:" +h+" z:"+(zDir + j));
								//tTileEntity.getWorld().setBlock(xDir+i, h, zDir+j, Blocks.diamond_block);
								//Utils.LOG_INFO("Changed into : "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getUnlocalizedName());
								return false;
							}
							if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 4) {
								Utils.LOG_INFO("Matter Generation Coils missings from the bottom layer, inner 3x3. - Wrong Meta");
								return false;
							}
						}
						else if ((h == 3) && (i != 0) && (j != 0)) {// innen decke (ulv casings + input + muffler)
							//if(j == 0 && i == 0) {

							if ((!this.addMufflerToMachineList(tTileEntity, 66))) {
								if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
									Utils.LOG_INFO("Found: "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getUnlocalizedName()+" at x:"+(xDir + i)+" y:" +h+" z:"+(zDir + j));
									Utils.LOG_INFO("Matter Fabricator Casings Missing from one of the top layers inner 3x3.");
									return false;
								}
								if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 4) {
									Utils.LOG_INFO("Found: "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getUnlocalizedName()+" at x:"+(xDir + i)+" y:" +h+" z:"+(zDir + j));
									Utils.LOG_INFO("Matter Fabricator Casings Missing from one of the top layers inner 3x3.");
									return false;
								}
							}
							//}

						} /*else {// top air
							if ((i != -2 && i != 2) && (j != -2 && j != 2) && h == 3) {
							if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
								Utils.LOG_INFO("Matter Generation Coils missings from the top layer, inner 3x3. - Wrong Block");
								Utils.LOG_INFO("Found: "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getUnlocalizedName()+" at x:"+(xDir + i)+" y:" +h+" z:"+(zDir + j));
								//tTileEntity.getWorld().setBlock(xDir+i, h, zDir+j, Blocks.diamond_block);
								//Utils.LOG_INFO("Changed into : "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getUnlocalizedName());
								return false;
							}
							if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 4) {
								Utils.LOG_INFO("Matter Generation Coils missings from the top layer, inner 3x3. - Wrong Meta");
								return false;
							}
							}
						}*/
					} else {// Outer 5x5
						if (h == 0) {// au�en boden (controller, output, energy, maintainance, rest ulv casings)
							if ((!this.addEnergyInputToMachineList(tTileEntity, 66) && (!this.addDynamoToMachineList(tTileEntity, 66)))) {
								if (((xDir + i) != 0) || ((zDir + j) != 0)) {//no controller
									if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
										Utils.LOG_INFO("Found: "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getUnlocalizedName()+" at x:"+(xDir + i)+" y:" +h+" z:"+(zDir + j));
										Utils.LOG_INFO("Matter Fabricator Casings Missing from one of the edges of the bottom layer. "+(aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName()));
										return false;
									}
									if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 4) {
										Utils.LOG_INFO("Matter Fabricator Casings Missing from one of the edges of the bottom layer. 2");
										return false;
									}
								}
							}
						} else {

						}
					}
				}
			}
		}
		Utils.LOG_INFO("Multiblock Formed.");
		return true;
	}

	@Override
	public boolean isCorrectMachinePart(final ItemStack aStack) {
		return true;
	}

	@Override
	public int getMaxEfficiency(final ItemStack aStack) {
		return 10000;
	}

	@Override
	public int getPollutionPerTick(final ItemStack aStack) {
		return 0;
	}

	@Override
	public int getDamageToComponent(final ItemStack aStack) {
		return 0;
	}

	@Override
	public int getAmountOfOutputs() {
		return 1;
	}

	@Override
	public boolean explodesOnComponentBreak(final ItemStack aStack) {
		return false;
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_PowerSubStationController(this.mName);
	}

}
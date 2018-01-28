package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi;

import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Maintenance;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_MultiMachine;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock.CustomIcon;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

public class GregtechMetaTileEntity_IndustrialCentrifuge
extends GregtechMeta_MultiBlockBase {
	private static ITexture frontFace;
	private static ITexture frontFaceActive;
	private static CustomIcon GT9_5_Active = new CustomIcon("iconsets/LARGECENTRIFUGE_ACTIVE5");
	private static CustomIcon GT9_5 = new CustomIcon("iconsets/LARGECENTRIFUGE5");
	//public static double recipesComplete = 0;

	public GregtechMetaTileEntity_IndustrialCentrifuge(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
		frontFaceActive = new GT_RenderedTexture(GT9_5_Active);
		frontFace = new GT_RenderedTexture(GT9_5);
	}

	public GregtechMetaTileEntity_IndustrialCentrifuge(final String aName) {
		super(aName);
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_IndustrialCentrifuge(this.mName);
	}

	@Override
	public String[] getDescription() {
		return new String[]{
				"Controller Block for the Industrial Centrifuge",
				"40% faster than using single block machines of the same voltage",
				"Size: 3x3x3 (Hollow)",
				"Controller (Front Center) [Orange]",
				"1x Maintenance Hatch (Rear Center) [Green]",
				"The rest can be placed anywhere except the Front [Red]",
				"1x Input Hatch",
				"1x Output Hatch",
				"1x Input Bus",
				"1x Output Bus",
				"1x [EV] Energy Hatch (Can be higher Tier) [Blue]",
				"Centrifuge Casings for the rest (16 at least)",
				CORE.GT_Tooltip};
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[1][aColorIndex + 1], aFacing == aSide ? aActive ? frontFaceActive : frontFace : Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(0)]};
	}

	@Override
	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GUI_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(), "IndustrialCentrifuge.png");
	}

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes;
	}

	@Override
	public boolean isFacingValid(final byte aFacing) {
		return aFacing > 1;
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {
		return checkRecipeGeneric(4, 100, 40);
	}

	@SuppressWarnings("static-method")
	public Block getCasingBlock() {
		return ModBlocks.blockCasingsMisc;
	}

	@SuppressWarnings("static-method")
	public byte getCasingMeta() {
		return 0;
	}

	@SuppressWarnings("static-method")
	public byte getCasingTextureIndex() {
		return 0;
	}

	@Override
	public boolean checkMachine(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {
		/*if (ConfigSwitches.disableCentrifugeFormation){
			EntityPlayerMP player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(this.getBaseMetaTileEntity().getOwnerName());
			if (!player.getEntityWorld().isRemote && isDisabled == false)
				PlayerUtils.messagePlayer(player, "This Multiblock is disabled via the config. [Only re-enable if you're bugtesting.]");
			isDisabled = true;
			return false;
		}*/
		final int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
		final int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
		//Utils.LOG_WARNING("X:"+xDir+" Z:"+zDir);
		if (!aBaseMetaTileEntity.getAirOffset(xDir, 0, zDir)) {
			return false;
		}
		int tAmount = 0;
		for (int i = -1; i < 2; i++) { //X-Dir
			for (int j = -1; j < 2; j++) { //Z-Dir
				for (int h = -1; h < 2; h++) { //Y-Dir
					if ((h != 0) || ((((xDir + i) != 0) || ((zDir + j) != 0)) && ((i != 0) || (j != 0)))) {

						final IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, h, zDir + j);
						//Utils.LOG_WARNING("X:"+tTileEntity.getXCoord()+" Y:"+tTileEntity.getYCoord()+" Z:"+tTileEntity.getZCoord());
						if ((!this.addMaintenanceToMachineList(tTileEntity, 57)) && (!this.addInputToMachineList(tTileEntity, 57)) && (!this.addOutputToMachineList(tTileEntity, 57)) && (!this.addEnergyInputToMachineList(tTileEntity, 57))) {

							//Maintenance Hatch
							if ((tTileEntity != null) && (tTileEntity.getMetaTileEntity() != null)) {
								if ((tTileEntity.getXCoord() == aBaseMetaTileEntity.getXCoord()) && (tTileEntity.getYCoord() == aBaseMetaTileEntity.getYCoord()) && (tTileEntity.getZCoord() == (aBaseMetaTileEntity.getZCoord()+2))) {
									if ((tTileEntity.getMetaTileEntity() instanceof GT_MetaTileEntity_Hatch_Maintenance)) {
										Logger.WARNING("MAINT HATCH IN CORRECT PLACE");
										this.mMaintenanceHatches.add((GT_MetaTileEntity_Hatch_Maintenance) tTileEntity.getMetaTileEntity());
										((GT_MetaTileEntity_Hatch) tTileEntity.getMetaTileEntity()).mMachineBlock = this.getCasingTextureIndex();
									} else {
										return false;
									}
								}
								else {
									Logger.WARNING("MAINT HATCH IN WRONG PLACE");
								}
							}

							if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
								return false;
							}
							if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 0) {
								return false;
							}
							tAmount++;

						}
					}
				}
			}
		}
		return tAmount >= 16;
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
	public int getAmountOfOutputs() {
		return 1;
	}

	@Override
	public boolean explodesOnComponentBreak(final ItemStack aStack) {
		return false;
	}

}
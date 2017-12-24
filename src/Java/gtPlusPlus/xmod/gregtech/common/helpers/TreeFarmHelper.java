package gtPlusPlus.xmod.gregtech.common.helpers;

import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableTreeFarmerParticles;

import cpw.mods.fml.common.eventhandler.Event.Result;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.common.items.GT_MetaGenerated_Item_02;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.slots.SlotBuzzSaw.SAWTOOL;
import gtPlusPlus.core.util.fluid.FluidUtils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.particles.BlockBreakParticles;
import gtPlusPlus.xmod.forestry.trees.TreefarmManager;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.fluids.FluidStack;

public class TreeFarmHelper {

	public static final FluidStack fertT1 = FluidUtils.getFluidStack("fluid.fertiliser", 3);
	public static final FluidStack fertT2 = FluidUtils.getFluidStack("fluid.un18fertiliser", 2);
	public static final FluidStack fertT3 = FluidUtils.getFluidStack("fluid.un32fertiliser", 1);

	public static ITexture[][][] getTextureSet() {
		final ITexture[][][] rTextures = new ITexture[10][17][];
		for (byte i = -1; i < 16; i++) {
			rTextures[0][i + 1] = TreeFarmHelper.getFront(i);
			rTextures[1][i + 1] = TreeFarmHelper.getBack(i);
			rTextures[2][i + 1] = TreeFarmHelper.getBottom(i);
			rTextures[3][i + 1] = TreeFarmHelper.getTop(i);
			rTextures[4][i + 1] = TreeFarmHelper.getSides(i);
			rTextures[5][i + 1] = TreeFarmHelper.getFrontActive(i);
			rTextures[6][i + 1] = TreeFarmHelper.getBackActive(i);
			rTextures[7][i + 1] = TreeFarmHelper.getBottomActive(i);
			rTextures[8][i + 1] = TreeFarmHelper.getTopActive(i);
			rTextures[9][i + 1] = TreeFarmHelper.getSidesActive(i);
		}
		return rTextures;
	}

	public static ITexture[] getFront(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[2][aColor + 1]};
	}

	public static ITexture[] getBack(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[2][aColor + 1]};
	}

	public static ITexture[] getBottom(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[2][aColor + 1]};
	}

	public static ITexture[] getTop(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[2][aColor + 1]};
	}

	public static ITexture[] getSides(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[2][aColor + 1]};
	}

	public static ITexture[] getFrontActive(final byte aColor) {
		return getFront(aColor);
	}

	public static ITexture[] getBackActive(final byte aColor) {
		return getBack(aColor);
	}

	public static ITexture[] getBottomActive(final byte aColor) {
		return getBottom(aColor);
	}

	public static ITexture[] getTopActive(final byte aColor) {
		return getTop(aColor);
	}

	public static ITexture[] getSidesActive(final byte aColor) {
		return getSides(aColor);
	}

	public static boolean applyBonemeal(final EntityPlayer player, final World world, final int intX, final int intY, final int intZ, final short multiplier){
		final Block block = world.getBlock(intX, intY, intZ);


		int roll;
		int rollNeeded;

		if (multiplier==1){
			roll = MathUtils.randInt(1, 15);
			rollNeeded = 15;
		}
		else if (multiplier==2){
			roll = MathUtils.randInt(1, 10);
			rollNeeded = 10;
		}
		else {
			roll = MathUtils.randInt(1, 5);
			rollNeeded = 5;
		}

		if (roll != rollNeeded){
			return false;
		}

		//EntityPlayer player = FakePlayerFactory.getMinecraft((WorldServer)world);
		if (!world.isRemote){
			if (enableTreeFarmerParticles){
				world.playAuxSFX(2005, intX, intY, intZ, 0);
			}
		}
		final BonemealEvent event = new BonemealEvent(player, world, block, intX, intY, intZ);
		if (MinecraftForge.EVENT_BUS.post(event)){
			Logger.MACHINE_INFO("Not sure why this returned false");
			return false;
		}
		if (event.getResult() == Result.ALLOW){
			if (!world.isRemote){
				world.playAuxSFX(2005, intX, intY, intZ, 0);
			}
			return true;
		}
		if (block instanceof IGrowable){
			final IGrowable igrowable = (IGrowable)block;
			if (igrowable.func_149851_a(world, intX, intY, intZ, world.isRemote)){
				if (!world.isRemote){
					if (igrowable.func_149852_a(world, CORE.RANDOM, intX, intY, intZ)){
						igrowable.func_149853_b(world, CORE.RANDOM, intX, intY, intZ);
					}
				}
				return true;
			}
		}
		return false;
	}

	public static boolean cleanUp(final IGregTechTileEntity aBaseMetaTileEntity){
		Logger.MACHINE_INFO("called cleanUp()");
		int cleanedUp = 0;
		final int xDir = net.minecraftforge.common.util.ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * 11;
		final int zDir = net.minecraftforge.common.util.ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * 11;

		for (int h=1;h<175;h++){
			for (int i = -11; i <= 11; i++) {
				for (int j = -11; j <= 11; j++) {

					final Block testBlock = aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j);


					if
					((
							((i == -8) || (i == 8)) ||
							((i == -9) || (i == 9)) ||
							((i == -10) || (i == 10)) ||
							((i == -11) || (i == 11))
							)
							&&
							(
									((j == -8) || (j == 8)) ||
									((j == -9) || (j == 9)) ||
									((j == -10) || (j == 10)) ||
									((j == -11) || (j == 11))
									)){

						if (!testBlock.getUnlocalizedName().toLowerCase().contains("air") || !testBlock.getUnlocalizedName().toLowerCase().contains("pumpkin")) {
							Logger.WARNING("5:"+testBlock.getUnlocalizedName());
						} else {
							aBaseMetaTileEntity.getWorld().setBlock(aBaseMetaTileEntity.getXCoord()+xDir+i, aBaseMetaTileEntity.getYCoord()+h, aBaseMetaTileEntity.getZCoord()+zDir+j, Blocks.bookshelf);
						}
					}


					//If not in the middle - don't know how else to check this one without lots of !=
					if (
							(i != 7) && (i != -7) && (j != 7) && (j != -7) &&
							(i != 6) && (i != -6) && (j != 6) && (j != -6) &&
							(i != 5) && (i != -5) && (j != 5) && (j != -5) &&
							(i != 4) && (i != -4) && (j != 4) && (j != -4) &&
							(i != 3) && (i != -3) && (j != 3) && (j != -3) &&
							(i != 2) && (i != -2) && (j != 2) && (j != -2) &&
							(i != 1) && (i != -1) && (j != 1) && (j != -1) &&
							(i != 0) && (j != 0)
							){

						if (!testBlock.getUnlocalizedName().toLowerCase().contains("air") || !testBlock.getUnlocalizedName().toLowerCase().contains("pumpkin")) {
							Logger.WARNING("0:"+testBlock.getUnlocalizedName());
						} else {
							aBaseMetaTileEntity.getWorld().setBlock(aBaseMetaTileEntity.getXCoord()+xDir+i, aBaseMetaTileEntity.getYCoord()+h, aBaseMetaTileEntity.getZCoord()+zDir+j, Blocks.melon_block);
						}


						if (TreefarmManager.isLeaves(testBlock) || TreefarmManager.isWoodLog(testBlock)){
							Logger.WARNING("1:"+testBlock.getUnlocalizedName());
							int posiX, posiY, posiZ;
							posiX = aBaseMetaTileEntity.getXCoord()+xDir+i;
							posiY = aBaseMetaTileEntity.getYCoord()+h;
							posiZ = aBaseMetaTileEntity.getZCoord()+zDir+j;
							//Utils.LOG_MACHINE_INFO("Cleaning Up some leftovers.");
							cleanedUp++;
							aBaseMetaTileEntity.getWorld().setBlockToAir(posiX, posiY, posiZ);
							new BlockBreakParticles(aBaseMetaTileEntity.getWorld(), posiX, posiY, posiZ, Blocks.dirt);
						}
						else {
							//Utils.LOG_WARNING("2:"+testBlock.getUnlocalizedName());
						}
					}
					else {
						//Utils.LOG_WARNING("1");
					}


				}

			}
		}
		Logger.MACHINE_INFO("cleaning up | "+cleanedUp );
		return true;
	}

	public static SAWTOOL isCorrectMachinePart(final ItemStack aStack) {
		if (aStack != null){
			//Utils.LOG_WARNING("Found "+aStack.getDisplayName()+" in the GUI slot.");
			if ((aStack.getItem() instanceof GT_MetaGenerated_Item_02) || (aStack.getItem() instanceof GT_MetaGenerated_Tool)){
				if (OrePrefixes.craftingTool.contains(aStack)){
					if (aStack.getDisplayName().toLowerCase().contains("saw") || aStack.getDisplayName().toLowerCase().contains("gt.metatool.01.10")){
						if (aStack.getItemDamage() == 10){
							return SAWTOOL.SAW;
						}
						else if (aStack.getItemDamage() == 140  || aStack.getDisplayName().toLowerCase().contains("gt.metatool.01.140")){
							return SAWTOOL.BUZZSAW;
						}
						else if (aStack.getItemDamage() == 110  || aStack.getDisplayName().toLowerCase().contains("gt.metatool.01.110")){
							return SAWTOOL.CHAINSAW;
						}
						else if (aStack.getItemDamage() == 112  || aStack.getDisplayName().toLowerCase().contains("gt.metatool.01.112")){
							return SAWTOOL.CHAINSAW;
						}
						else if (aStack.getItemDamage() == 114  || aStack.getDisplayName().toLowerCase().contains("gt.metatool.01.114")){
							return SAWTOOL.CHAINSAW;
						}
						else {
							return SAWTOOL.NONE;
						}
					}
				}
			}
		}
		return SAWTOOL.NONE;
	}

}

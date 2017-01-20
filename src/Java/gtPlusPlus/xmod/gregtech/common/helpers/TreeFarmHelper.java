package gtPlusPlus.xmod.gregtech.common.helpers;

import static gtPlusPlus.core.lib.CORE.configSwitches.enableTreeFarmerParticles;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.common.items.GT_MetaGenerated_Item_02;
import gtPlusPlus.core.slots.SlotBuzzSaw.SAWTOOL;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.forestry.trees.TreefarmManager;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.entity.player.BonemealEvent;
import cpw.mods.fml.common.eventhandler.Event.Result;

public class TreeFarmHelper {

	public static ITexture[][][] getTextureSet() {
		ITexture[][][] rTextures = new ITexture[10][17][];
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

	public static boolean applyBonemeal(World world, int intX, int intY, int intZ){
		Block block = world.getBlock(intX, intY, intZ);
		int chance = MathUtils.randInt(1, 10); //TODO
	
		//Random Growth
		if (chance < 8){
			return false;
		}
	
		EntityPlayer player = FakePlayerFactory.getMinecraft((WorldServer)world);
		if (!world.isRemote){
			if (enableTreeFarmerParticles){
				world.playAuxSFX(2005, intX, intY, intZ, 0);
			}
		}
		BonemealEvent event = new BonemealEvent(player, world, block, intX, intY, intZ);
		if (MinecraftForge.EVENT_BUS.post(event)){
			Utils.LOG_MACHINE_INFO("Not sure why this returned false");
			return false;
		}
		if (event.getResult() == Result.ALLOW){
			if (!world.isRemote){
				world.playAuxSFX(2005, intX, intY, intZ, 0);
			}
			return true;
		}
		if (block instanceof IGrowable){
			IGrowable igrowable = (IGrowable)block;
			if (igrowable.func_149851_a(world, intX, intY, intZ, world.isRemote)){
				if (!world.isRemote){
					if (igrowable.func_149852_a(world, world.rand, intX, intY, intZ)){
						igrowable.func_149853_b(world, world.rand, intX, intY, intZ);
					}
				}				
				return true;
			}
		}
		return false;
	}

	public static boolean cleanUp(final IGregTechTileEntity aBaseMetaTileEntity){
		Utils.LOG_MACHINE_INFO("called cleanUp()");
		int cleanedUp = 0;
		final int xDir = net.minecraftforge.common.util.ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * 7; 
		final int zDir = net.minecraftforge.common.util.ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * 7;
		for (int i = -10; i <= 10; i++) {
			for (int j = -10; j <= 10; j++) {
				for (int h=1;h<175;h++){
					if (TreefarmManager.isLeaves(aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j)) || TreefarmManager.isWoodLog(aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j))){
						int posiX, posiY, posiZ;
						posiX = aBaseMetaTileEntity.getXCoord()+xDir+i;
						posiY = aBaseMetaTileEntity.getYCoord()+h;
						posiZ = aBaseMetaTileEntity.getZCoord()+zDir+j;
						//Utils.LOG_MACHINE_INFO("Cleaning Up some leftovers.");
						cleanedUp++;
						aBaseMetaTileEntity.getWorld().setBlockToAir(posiX, posiY, posiZ);
					}
	
				}
	
			}
		}
		Utils.LOG_MACHINE_INFO("cleaning up | "+cleanedUp );
		return true;		
	}

	public static SAWTOOL isCorrectMachinePart(final ItemStack aStack) {
		if (aStack != null){
			if (aStack.getItem() instanceof GT_MetaGenerated_Item_02 || aStack.getItem() instanceof GT_MetaGenerated_Tool){
				if (OrePrefixes.craftingTool.contains(aStack)){
					if (aStack.getDisplayName().toLowerCase().contains("saw")){
						if (aStack.getItemDamage() == 10){
							return SAWTOOL.NONE;
						}
						else if (aStack.getItemDamage() == 140){
							return SAWTOOL.NONE;
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

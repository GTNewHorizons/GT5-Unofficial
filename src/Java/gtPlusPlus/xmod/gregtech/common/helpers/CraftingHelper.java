package gtPlusPlus.xmod.gregtech.common.helpers;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.common.helpers.autocrafter.AC_Helper_Container;
import gtPlusPlus.xmod.gregtech.common.helpers.autocrafter.AC_Helper_Utils;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.GT4Entity_AutoCrafter;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayerFactory;

public class CraftingHelper{

	public final String mInventoryName;
	public final int mPosX;
	public final int mPosY;
	public final int mPosZ;
	public final GT4Entity_AutoCrafter crafter;
	public final World world;
	public final EntityPlayerMP player;
	public final AC_Helper_Container inventory;

	public CraftingHelper(GT4Entity_AutoCrafter AC){
		Logger.INFO("[A-C] Created a crafting helper.");
		crafter = AC;
		AC_Helper_Utils.addCrafter(AC);
		//Get some variables.
		world = AC.getBaseMetaTileEntity().getWorld();
		mPosX = AC.getBaseMetaTileEntity().getXCoord();
		mPosY = AC.getBaseMetaTileEntity().getYCoord();
		mPosZ = AC.getBaseMetaTileEntity().getZCoord();
		//Create Fake player to handle crating.
		player = FakePlayerFactory.get((WorldServer) world, CORE.gameProfile);
		//Set storage container
		inventory = new AC_Helper_Container(player.inventory, world, mPosX, mPosY, mPosZ);
		mInventoryName = inventory.getMatrix().getInventoryName();

	}
}
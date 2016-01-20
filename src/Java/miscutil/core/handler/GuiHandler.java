package miscutil.core.handler;

import miscutil.core.gui.GUI_Bat_Buf;
import miscutil.core.gui.GUI_Battery_Buffer;
import miscutil.core.util.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	private static final int GUI1 = 0;      //Nothing Yet
	private static final int GUI2 = 1;      //Energy Buffer



	@Override //ContainerModTileEntity
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == GUI1)
			return new GUI_Battery_Buffer();

		return null;
	}

	@Override //GuiModTileEntity
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		Utils.LOG_WARNING("getClientGuiElement Called by: "+player+", in world: "+player.dimension+" at x:"+x+", y:"+y+", z:"+z+".");
		if (ID == GUI1){
			Utils.LOG_WARNING("Opening Gui with Id: "+ID);
			return new GUI_Battery_Buffer();
		}
		else  if (ID == GUI2){
			Utils.LOG_WARNING("Opening Gui with Id: "+ID+" Energy Buffer");
			return new GUI_Bat_Buf();
		}
		return null;
	}

}
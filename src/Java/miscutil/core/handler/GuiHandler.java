package miscutil.core.handler;

import miscutil.MiscUtils;
import miscutil.core.container.Container_Charger;
import miscutil.core.container.Container_NHG;
import miscutil.core.gui.beta.Gui_ID_Registry;
import miscutil.core.gui.beta.MU_GuiId;
import miscutil.core.gui.machine.GUI_Charger;
import miscutil.core.gui.machine.GUI_NHG;
import miscutil.core.interfaces.IGuiManager;
import miscutil.core.tileentities.machines.TileEntityCharger;
import miscutil.core.tileentities.machines.TileEntityNHG;
import miscutil.core.util.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	private static final int GUI1 = 0;      //Nuclear Helium Gen.
	private static final int GUI2 = 1;      //Energy Charger

	@Override //ContainerModTileEntity
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity te = world.getTileEntity(x, y, z);

		if (te != null){
			if (ID == GUI1){
				return new Container_NHG((TileEntityNHG)te, player);
			}
			else if (ID == GUI2){
				return new Container_Charger((TileEntityCharger)te, player);
			}



		}
		return null;
	}

	@Override //GuiModTileEntity
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		Utils.LOG_WARNING("getClientGuiElement Called by: "+player+", in world: "+player.dimension+" at x:"+x+", y:"+y+", z:"+z+".");
		TileEntity te = world.getTileEntity(x, y, z);
		if (te != null){
			if (ID == GUI1){
				Utils.LOG_WARNING("Opening Gui with Id: "+ID+" NHG");
				return new GUI_NHG((TileEntityNHG) te, player);
			}
			else  if (ID == GUI2){
				Utils.LOG_WARNING("Opening Gui with Id: "+ID+" Charger");
				return new GUI_Charger((TileEntityCharger) te, player);
			}
		}
		return null;
	}
	
	
	
	//New Methods
	public static void openGui(EntityPlayer entityplayer, IGuiManager guiHandler)
	{
		openGui(entityplayer, guiHandler, (short)0);
	}

	public static void openGui(EntityPlayer entityplayer, IGuiManager guiHandler, short data)
	{
		int guiData = encodeGuiData(guiHandler, data);
		ChunkCoordinates coordinates = guiHandler.getCoordinates();
		entityplayer.openGui(MiscUtils.instance, guiData, entityplayer.worldObj, coordinates.posX, coordinates.posY, coordinates.posZ);
	}

	private static int encodeGuiData(IGuiManager guiHandler, short data)
	{
		MU_GuiId guiId = Gui_ID_Registry.getGuiIdForGuiHandler(guiHandler);
		return data << 16 | guiId.getId();
	}

	private static MU_GuiId decodeGuiID(int guiData)
	{
		int guiId = guiData & 0xFF;
		return Gui_ID_Registry.getGuiId(guiId);
	}
	
	private static short decodeGuiData(int guiId)
	  {
	    return (short)(guiId >> 16);
	  }

}
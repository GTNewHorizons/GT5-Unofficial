package miscutil.core.handler;

import miscutil.MiscUtils;
import miscutil.core.gui.beta.Gui_ID_Registry;
import miscutil.core.gui.beta.MU_GuiId;
import miscutil.core.interfaces.IGuiManager;
import miscutil.core.lib.CORE;
import miscutil.core.util.Utils;
import miscutil.xmod.forestry.bees.alveary.TileAlvearyFrameHousing;
import miscutil.xmod.forestry.bees.alveary.gui.CONTAINER_FrameHousing;
import miscutil.xmod.forestry.bees.alveary.gui.GUI_FrameHousing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;

public class GuiHandler implements IGuiHandler {

	private static final int GUI1 = 0;      //Frame Alveary
	private static final int GUI2 = 1;      //RTG
	


	public static void init(){ 	

		Utils.LOG_INFO("Registering GUIs."); 	
		NetworkRegistry.INSTANCE.registerGuiHandler(MiscUtils.instance, new GuiHandler()); 	
		//Register GuiHandler 	
		//NetworkRegistry.INSTANCE.registerGuiHandler(MiscUtils.instance, new GuiHandler()); 	
	}


	@Override //ContainerModTileEntity
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity te = world.getTileEntity(x, y, z);

		if (te != null){
			if (ID == GUI1){
				if (CORE.configSwitches.enableCustomAlvearyBlocks){
					return new CONTAINER_FrameHousing((TileAlvearyFrameHousing)te, player);
				}
			}
			else if (ID == GUI2){
				//return new CONTAINER_RTG(player, (TileEntityRTG)te);
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
				if (CORE.configSwitches.enableCustomAlvearyBlocks){
					Utils.LOG_WARNING("Opening Gui with Id: "+ID+" Alveary Frame Housing");
					return new GUI_FrameHousing((TileAlvearyFrameHousing) te, player);
				}
			}
			else  if (ID == GUI2){
				Utils.LOG_WARNING("Opening Gui with Id: "+ID+" RTG");
				//return new GUI_RTG((TileEntityRTG) te.);
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
package gtPlusPlus.core.handler;

import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.core.container.Container_BackpackBase;
import gtPlusPlus.core.container.Container_FishTrap;
import gtPlusPlus.core.container.Container_Grindle;
import gtPlusPlus.core.container.Container_ModularityTable;
import gtPlusPlus.core.container.Container_ProjectTable;
import gtPlusPlus.core.container.Container_TradeTable;
import gtPlusPlus.core.container.Container_Workbench;
import gtPlusPlus.core.container.Container_WorkbenchAdvanced;
import gtPlusPlus.core.gui.beta.Gui_ID_Registry;
import gtPlusPlus.core.gui.beta.MU_GuiId;
import gtPlusPlus.core.gui.item.GuiBaseBackpack;
import gtPlusPlus.core.gui.item.GuiBaseGrindle;
import gtPlusPlus.core.gui.machine.GUI_FishTrap;
import gtPlusPlus.core.gui.machine.GUI_ModularityTable;
import gtPlusPlus.core.gui.machine.GUI_ProjectTable;
import gtPlusPlus.core.gui.machine.GUI_TradeTable;
import gtPlusPlus.core.gui.machine.GUI_Workbench;
import gtPlusPlus.core.gui.machine.GUI_WorkbenchAdvanced;
import gtPlusPlus.core.interfaces.IGuiManager;
import gtPlusPlus.core.inventories.BaseInventoryBackpack;
import gtPlusPlus.core.inventories.BaseInventoryGrindle;
import gtPlusPlus.core.tileentities.base.TileEntityBase;
import gtPlusPlus.core.tileentities.general.TileEntityFishTrap;
import gtPlusPlus.core.tileentities.machines.TileEntityModularityTable;
import gtPlusPlus.core.tileentities.machines.TileEntityProjectTable;
import gtPlusPlus.core.tileentities.machines.TileEntityTradeTable;
import gtPlusPlus.core.tileentities.machines.TileEntityWorkbench;
import gtPlusPlus.core.tileentities.machines.TileEntityWorkbenchAdvanced;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.thaumcraft.common.tile.TileFastAlchemyFurnace;
import gtPlusPlus.xmod.thaumcraft.gui.ContainerFastAlchemyFurnace;
import gtPlusPlus.xmod.thaumcraft.gui.GuiFastAlchemyFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {

	public static final int GUI1 = 0;      //Project Table
	public static final int GUI2 = 1;      //Helium Generator
	public static final int GUI3 = 2;      //BackpackHandler
	public static final int GUI4 = 3;      //Workbench
	public static final int GUI5 = 4;      //Workbench Adv
	public static final int GUI6 = 5;      //Fish trap
	public static final int GUI7 = 6;      //Trade table
	public static final int GUI8 = 7;      //Alchemical Furnace
	public static final int GUI9 = 8;      //Grindle



	public static void init(){

		Utils.LOG_INFO("Registering GUIs.");
		NetworkRegistry.INSTANCE.registerGuiHandler(GTplusplus.instance, new GuiHandler());
		//Register GuiHandler
		//NetworkRegistry.INSTANCE.registerGuiHandler(GTplusplus.instance, new GuiHandler());
	}


	@Override //ContainerModTileEntity
	public Object getServerGuiElement(final int ID, final EntityPlayer player, final World world, final int x, final int y, final int z) {
		final TileEntity te = world.getTileEntity(x, y, z);

		if (te != null){
			if (ID == GUI1){
				return new Container_ProjectTable(player.inventory, (TileEntityProjectTable)te);
			}
			else if (ID == GUI2){
				return new Container_ModularityTable(player.inventory, (TileEntityModularityTable) te);
			}
		}

		if (ID == GUI3)
		{
			// Use the player's held item to create the inventory
			return new Container_BackpackBase(player, player.inventory, new BaseInventoryBackpack(player.getHeldItem()));
		}

		if (te != null){
			if (ID == GUI4){
				return new Container_Workbench(player.inventory, (TileEntityWorkbench)te);
			}
			else if (ID == GUI5){
				Utils.LOG_INFO("sad");
				return new Container_WorkbenchAdvanced(player.inventory, (TileEntityWorkbenchAdvanced)te);

			}
			else if (ID == GUI6){
				return new Container_FishTrap(player.inventory, (TileEntityFishTrap)te);
			}
			else if (ID == GUI7){
				return new Container_TradeTable(player.inventory, (TileEntityTradeTable)te);
			}
			else if (ID == GUI8){
				return new ContainerFastAlchemyFurnace(player.inventory, (TileFastAlchemyFurnace)te);
			}
		}

		if (ID == GUI9){
			return new Container_Grindle(player, player.inventory, new BaseInventoryGrindle(player.getHeldItem()));
		}







		return null;
	}

	@Override //GuiModTileEntity
	public Object getClientGuiElement(final int ID, final EntityPlayer player, final World world, final int x, final int y, final int z) {
		Utils.LOG_WARNING("getClientGuiElement Called by: "+player+", in world: "+player.dimension+" at x:"+x+", y:"+y+", z:"+z+".");
		final TileEntity te = world.getTileEntity(x, y, z);
		if (te != null){
			if (ID == GUI1){
				return new GUI_ProjectTable(player.inventory, (TileEntityProjectTable)te);
			}
			else  if (ID == GUI2){
				return new GUI_ModularityTable(player.inventory, (TileEntityModularityTable) te);
			}
		}

		if (ID == GUI3)
		{
			// We have to cast the new container as our custom class
			// and pass in currently held item for the inventory
			return new GuiBaseBackpack(new Container_BackpackBase(player, player.inventory, new BaseInventoryBackpack(player.getHeldItem())));
		}

		if (te != null){
			if (ID == GUI4){
				return new GUI_Workbench(player.inventory, (TileEntityWorkbench)te);
			}
			else if (ID == GUI5){
				Utils.LOG_INFO("sad");
				return new GUI_WorkbenchAdvanced(player.inventory, (TileEntityWorkbenchAdvanced)te);
			}
			else if (ID == GUI6){
				return new GUI_FishTrap(player.inventory, (TileEntityFishTrap)te);
			}
			else if (ID == GUI7){
				return new GUI_TradeTable(player.inventory, (TileEntityTradeTable)te, ((TileEntityBase) te).getOwner());
			}
			else if (ID == GUI8){
				return new GuiFastAlchemyFurnace(player.inventory, (TileFastAlchemyFurnace)te);
			}
		}

		if (ID == GUI9){
			return new GuiBaseGrindle(new Container_Grindle(player, player.inventory, new BaseInventoryGrindle(player.getHeldItem())));
		}

		return null;
	}



	//New Methods
	public static void openGui(final EntityPlayer entityplayer, final IGuiManager guiHandler)
	{
		openGui(entityplayer, guiHandler, (short)0);
	}

	public static void openGui(final EntityPlayer entityplayer, final IGuiManager guiHandler, final short data)
	{
		final int guiData = encodeGuiData(guiHandler, data);
		final ChunkCoordinates coordinates = guiHandler.getCoordinates();
		entityplayer.openGui(GTplusplus.instance, guiData, entityplayer.worldObj, coordinates.posX, coordinates.posY, coordinates.posZ);
	}

	private static int encodeGuiData(final IGuiManager guiHandler, final short data)
	{
		final MU_GuiId guiId = Gui_ID_Registry.getGuiIdForGuiHandler(guiHandler);
		return (data << 16) | guiId.getId();
	}

	private static MU_GuiId decodeGuiID(final int guiData)
	{
		final int guiId = guiData & 0xFF;
		return Gui_ID_Registry.getGuiId(guiId);
	}

	private static short decodeGuiData(final int guiId)
	{
		return (short)(guiId >> 16);
	}

}
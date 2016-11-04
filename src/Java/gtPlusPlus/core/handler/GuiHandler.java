package gtPlusPlus.core.handler;

import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.core.container.*;
import gtPlusPlus.core.gui.beta.Gui_ID_Registry;
import gtPlusPlus.core.gui.beta.MU_GuiId;
import gtPlusPlus.core.gui.item.GuiBaseBackpack;
import gtPlusPlus.core.gui.machine.GUI_Workbench;
import gtPlusPlus.core.gui.machine.GUI_WorkbenchAdvanced;
import gtPlusPlus.core.interfaces.IGuiManager;
import gtPlusPlus.core.inventories.BaseInventoryBackpack;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.tileentities.machines.TileEntityWorkbench;
import gtPlusPlus.core.tileentities.machines.TileEntityWorkbenchAdvanced;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.forestry.bees.alveary.TileAlvearyFrameHousing;
import gtPlusPlus.xmod.forestry.bees.alveary.gui.CONTAINER_FrameHousing;
import gtPlusPlus.xmod.forestry.bees.alveary.gui.GUI_FrameHousing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {

	public static final int	GUI1	= 0;	// Frame Alveary
	public static final int	GUI2	= 1;	// RTG
	public static final int	GUI3	= 2;	// BackpackHandler
	public static final int	GUI4	= 3;	// Workbench
	public static final int	GUI5	= 4;	// Workbench Adv
	public static final int	GUI6	= 5;	//
	public static final int	GUI7	= 6;	//
	public static final int	GUI8	= 7;	//

	private static short decodeGuiData(final int guiId) {
		return (short) (guiId >> 16);
	}

	private static MU_GuiId decodeGuiID(final int guiData) {
		final int guiId = guiData & 0xFF;
		return Gui_ID_Registry.getGuiId(guiId);
	}

	private static int encodeGuiData(final IGuiManager guiHandler, final short data) {
		final MU_GuiId guiId = Gui_ID_Registry.getGuiIdForGuiHandler(guiHandler);
		return data << 16 | guiId.getId();
	}

	public static void init() {

		Utils.LOG_INFO("Registering GUIs.");
		NetworkRegistry.INSTANCE.registerGuiHandler(GTplusplus.instance, new GuiHandler());
		// Register GuiHandler
		// NetworkRegistry.INSTANCE.registerGuiHandler(GTplusplus.instance, new
		// GuiHandler());
	}

	// New Methods
	public static void openGui(final EntityPlayer entityplayer, final IGuiManager guiHandler) {
		GuiHandler.openGui(entityplayer, guiHandler, (short) 0);
	}

	public static void openGui(final EntityPlayer entityplayer, final IGuiManager guiHandler, final short data) {
		final int guiData = GuiHandler.encodeGuiData(guiHandler, data);
		final ChunkCoordinates coordinates = guiHandler.getCoordinates();
		entityplayer.openGui(GTplusplus.instance, guiData, entityplayer.worldObj, coordinates.posX, coordinates.posY,
				coordinates.posZ);
	}

	@Override // GuiModTileEntity
	public Object getClientGuiElement(final int ID, final EntityPlayer player, final World world, final int x,
			final int y, final int z) {
		Utils.LOG_WARNING("getClientGuiElement Called by: " + player + ", in world: " + player.dimension + " at x:" + x
				+ ", y:" + y + ", z:" + z + ".");
		final TileEntity te = world.getTileEntity(x, y, z);
		if (te != null) {
			if (ID == GuiHandler.GUI1) {
				if (CORE.configSwitches.enableCustomAlvearyBlocks) {
					Utils.LOG_WARNING("Opening Gui with Id: " + ID + " Alveary Frame Housing");
					return new GUI_FrameHousing((TileAlvearyFrameHousing) te, player);
				}
			}
			else if (ID == GuiHandler.GUI2) {
				Utils.LOG_WARNING("Opening Gui with Id: " + ID + " RTG");
				// return new GUI_RTG((TileEntityRTG) te.);
			}
		}

		if (ID == GuiHandler.GUI3) {
			// We have to cast the new container as our custom class
			// and pass in currently held item for the inventory
			return new GuiBaseBackpack(new Container_BackpackBase(player, player.inventory,
					new BaseInventoryBackpack(player.getHeldItem())));
		}

		if (te != null) {
			if (ID == GuiHandler.GUI4) {
				return new GUI_Workbench(player.inventory, (TileEntityWorkbench) te);
			}
			if (ID == GuiHandler.GUI5) {
				Utils.LOG_INFO("sad");
				return new GUI_WorkbenchAdvanced(player.inventory, (TileEntityWorkbenchAdvanced) te);
			}
		}

		return null;
	}

	@Override // ContainerModTileEntity
	public Object getServerGuiElement(final int ID, final EntityPlayer player, final World world, final int x,
			final int y, final int z) {
		final TileEntity te = world.getTileEntity(x, y, z);

		if (te != null) {
			if (ID == GuiHandler.GUI1) {
				if (CORE.configSwitches.enableCustomAlvearyBlocks) {
					return new CONTAINER_FrameHousing((TileAlvearyFrameHousing) te, player);
				}
			}
			else if (ID == GuiHandler.GUI2) {
				// return new CONTAINER_RTG(player, (TileEntityRTG)te);
			}

		}

		if (ID == GuiHandler.GUI3) {
			// Use the player's held item to create the inventory
			return new Container_BackpackBase(player, player.inventory,
					new BaseInventoryBackpack(player.getHeldItem()));
		}

		if (te != null) {
			if (ID == GuiHandler.GUI4) {

				return new Container_Workbench(player.inventory, (TileEntityWorkbench) te);

			}
			if (ID == GuiHandler.GUI5) {
				Utils.LOG_INFO("sad");
				return new Container_WorkbenchAdvanced(player.inventory, (TileEntityWorkbenchAdvanced) te);

			}
		}

		return null;
	}

}
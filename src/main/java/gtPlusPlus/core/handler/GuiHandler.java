package gtPlusPlus.core.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.machine.BlockSuperJukebox.TileEntitySuperJukebox;
import gtPlusPlus.core.container.ContainerCircuitProgrammer;
import gtPlusPlus.core.container.ContainerFishTrap;
import gtPlusPlus.core.container.ContainerPestKiller;
import gtPlusPlus.core.container.ContainerProjectTable;
import gtPlusPlus.core.container.ContainerSuperJukebox;
import gtPlusPlus.core.container.ContainerVolumetricFlaskSetter;
import gtPlusPlus.core.gui.beta.GUIIDRegistry;
import gtPlusPlus.core.gui.beta.MUGuild;
import gtPlusPlus.core.gui.machine.GUICircuitProgrammer;
import gtPlusPlus.core.gui.machine.GUIFishTrap;
import gtPlusPlus.core.gui.machine.GUIPestKiller;
import gtPlusPlus.core.gui.machine.GUIProjectTable;
import gtPlusPlus.core.gui.machine.GUISuperJukebox;
import gtPlusPlus.core.gui.machine.GUIVolumetricFlaskSetter;
import gtPlusPlus.core.interfaces.IGuiManager;
import gtPlusPlus.core.tileentities.general.TileEntityCircuitProgrammer;
import gtPlusPlus.core.tileentities.general.TileEntityFishTrap;
import gtPlusPlus.core.tileentities.general.TileEntityVolumetricFlaskSetter;
import gtPlusPlus.core.tileentities.machines.TileEntityPestKiller;
import gtPlusPlus.core.tileentities.machines.TileEntityProjectTable;

public class GuiHandler implements IGuiHandler {

    public static final int GUI1 = 0; // Project Table
    public static final int GUI2 = 1; // None
    public static final int GUI3 = 2; // None
    public static final int GUI4 = 3; // Workbench
    public static final int GUI5 = 4; // Workbench Adv
    public static final int GUI6 = 5; // Fish trap
    public static final int GUI7 = 6; // None
    public static final int GUI8 = 7; // Circuit Programmer
    public static final int GUI9 = 8; // None
    public static final int GUI10 = 9; // None
    public static final int GUI11 = 10; // None
    public static final int GUI12 = 11; // None
    public static final int GUI14 = 13; // Super Jukebox
    public static final int GUI15 = 14; // Pest Killer
    public static final int GUI16 = 15; // None
    public static final int GUI17 = 16; // None
    public static final int GUI18 = 17; // Volumetric Flask Setter

    public static void init() {
        Logger.INFO("Registering GUIs.");
        NetworkRegistry.INSTANCE.registerGuiHandler(GTplusplus.instance, new GuiHandler());
    }

    @Override // ContainerModTileEntity
    public Object getServerGuiElement(final int ID, final EntityPlayer player, final World world, final int x,
        final int y, final int z) {
        final TileEntity te = world.getTileEntity(x, y, z);

        if (te != null) {
            if (ID == GUI1) {
                return new ContainerProjectTable(player.inventory, (TileEntityProjectTable) te);
            } else if (ID == GUI2) {}
        }

        if (te != null) {
            if (ID == GUI5) {
                Logger.INFO("sad");
                // return new Container_WorkbenchAdvanced(player.inventory, (TileEntityWorkbenchAdvanced) te);
            } else if (ID == GUI6) {
                return new ContainerFishTrap(player.inventory, (TileEntityFishTrap) te);
            } else if (ID == GUI8) {
                return new ContainerCircuitProgrammer(player.inventory, (TileEntityCircuitProgrammer) te);
            } else if (ID == GUI14) {
                return new ContainerSuperJukebox(player.inventory, (TileEntitySuperJukebox) te);
            } else if (ID == GUI15) {
                return new ContainerPestKiller(player.inventory, (TileEntityPestKiller) te);
            } else if (ID == GUI18) {
                return new ContainerVolumetricFlaskSetter(player.inventory, (TileEntityVolumetricFlaskSetter) te);
            }
        }

        return null;
    }

    @Override // GuiModTileEntity
    public Object getClientGuiElement(final int ID, final EntityPlayer player, final World world, final int x,
        final int y, final int z) {
        Logger.WARNING(
            "getClientGuiElement Called by: " + player
                + ", in world: "
                + player.dimension
                + " at x:"
                + x
                + ", y:"
                + y
                + ", z:"
                + z
                + ".");
        final TileEntity te = world.getTileEntity(x, y, z);
        if (te != null) {
            if (ID == GUI1) {
                return new GUIProjectTable(player.inventory, (TileEntityProjectTable) te);
            }
        }

        if (te != null) {
            if (ID == GUI6) {
                return new GUIFishTrap(player.inventory, (TileEntityFishTrap) te);
            } else if (ID == GUI8) {
                return new GUICircuitProgrammer(player.inventory, (TileEntityCircuitProgrammer) te);
            } else if (ID == GUI14) {
                return new GUISuperJukebox(player.inventory, (TileEntitySuperJukebox) te);
            } else if (ID == GUI15) {
                return new GUIPestKiller(player.inventory, (TileEntityPestKiller) te);
            } else if (ID == GUI18) {
                return new GUIVolumetricFlaskSetter(
                    new ContainerVolumetricFlaskSetter(player.inventory, (TileEntityVolumetricFlaskSetter) te));
            }
        }

        return null;
    }

    // New Methods
    public static void openGui(final EntityPlayer entityplayer, final IGuiManager guiHandler) {
        openGui(entityplayer, guiHandler, (short) 0);
    }

    public static void openGui(final EntityPlayer entityplayer, final IGuiManager guiHandler, final short data) {
        final int guiData = encodeGuiData(guiHandler, data);
        final ChunkCoordinates coordinates = guiHandler.getCoordinates();
        entityplayer.openGui(
            GTplusplus.instance,
            guiData,
            entityplayer.worldObj,
            coordinates.posX,
            coordinates.posY,
            coordinates.posZ);
    }

    private static int encodeGuiData(final IGuiManager guiHandler, final short data) {
        final MUGuild guiId = GUIIDRegistry.getGuiIdForGuiHandler(guiHandler);
        return (data << 16) | guiId.getId();
    }

}

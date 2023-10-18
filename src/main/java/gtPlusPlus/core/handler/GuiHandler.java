package gtPlusPlus.core.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.machine.Machine_SuperJukebox.TileEntitySuperJukebox;
import gtPlusPlus.core.container.Container_CircuitProgrammer;
import gtPlusPlus.core.container.Container_DecayablesChest;
import gtPlusPlus.core.container.Container_FishTrap;
import gtPlusPlus.core.container.Container_PestKiller;
import gtPlusPlus.core.container.Container_ProjectTable;
import gtPlusPlus.core.container.Container_SuperJukebox;
import gtPlusPlus.core.container.Container_VolumetricFlaskSetter;
import gtPlusPlus.core.gui.beta.Gui_ID_Registry;
import gtPlusPlus.core.gui.beta.MU_GuiId;
import gtPlusPlus.core.gui.machine.GUI_CircuitProgrammer;
import gtPlusPlus.core.gui.machine.GUI_DecayablesChest;
import gtPlusPlus.core.gui.machine.GUI_FishTrap;
import gtPlusPlus.core.gui.machine.GUI_PestKiller;
import gtPlusPlus.core.gui.machine.GUI_ProjectTable;
import gtPlusPlus.core.gui.machine.GUI_SuperJukebox;
import gtPlusPlus.core.gui.machine.GUI_VolumetricFlaskSetter;
import gtPlusPlus.core.interfaces.IGuiManager;
import gtPlusPlus.core.tileentities.general.TileEntityCircuitProgrammer;
import gtPlusPlus.core.tileentities.general.TileEntityDecayablesChest;
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
    public static final int GUI13 = 12; // Decayables Chest
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
                return new Container_ProjectTable(player.inventory, (TileEntityProjectTable) te);
            } else if (ID == GUI2) {}
        }

        if (te != null) {
            if (ID == GUI5) {
                Logger.INFO("sad");
                // return new Container_WorkbenchAdvanced(player.inventory, (TileEntityWorkbenchAdvanced) te);
            } else if (ID == GUI6) {
                return new Container_FishTrap(player.inventory, (TileEntityFishTrap) te);
            } else if (ID == GUI8) {
                return new Container_CircuitProgrammer(player.inventory, (TileEntityCircuitProgrammer) te);
            } else if (ID == GUI13) {
                return new Container_DecayablesChest(player.inventory, (TileEntityDecayablesChest) te);
            } else if (ID == GUI14) {
                return new Container_SuperJukebox(player.inventory, (TileEntitySuperJukebox) te);
            } else if (ID == GUI15) {
                return new Container_PestKiller(player.inventory, (TileEntityPestKiller) te);
            } else if (ID == GUI18) {
                return new Container_VolumetricFlaskSetter(player.inventory, (TileEntityVolumetricFlaskSetter) te);
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
                return new GUI_ProjectTable(player.inventory, (TileEntityProjectTable) te);
            }
        }

        if (te != null) {
            if (ID == GUI6) {
                return new GUI_FishTrap(player.inventory, (TileEntityFishTrap) te);
            } else if (ID == GUI8) {
                return new GUI_CircuitProgrammer(player.inventory, (TileEntityCircuitProgrammer) te);
            } else if (ID == GUI13) {
                return new GUI_DecayablesChest(player.inventory, (TileEntityDecayablesChest) te);
            } else if (ID == GUI14) {
                return new GUI_SuperJukebox(player.inventory, (TileEntitySuperJukebox) te);
            } else if (ID == GUI15) {
                return new GUI_PestKiller(player.inventory, (TileEntityPestKiller) te);
            } else if (ID == GUI18) {
                return new GUI_VolumetricFlaskSetter(
                        new Container_VolumetricFlaskSetter(player.inventory, (TileEntityVolumetricFlaskSetter) te));
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
        final MU_GuiId guiId = Gui_ID_Registry.getGuiIdForGuiHandler(guiHandler);
        return (data << 16) | guiId.getId();
    }

}

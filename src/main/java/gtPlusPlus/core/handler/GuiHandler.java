package gtPlusPlus.core.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.core.container.ContainerCircuitProgrammer;
import gtPlusPlus.core.container.ContainerFishTrap;
import gtPlusPlus.core.container.ContainerVolumetricFlaskSetter;
import gtPlusPlus.core.gui.machine.GUICircuitProgrammer;
import gtPlusPlus.core.gui.machine.GUIFishTrap;
import gtPlusPlus.core.gui.machine.GUIVolumetricFlaskSetter;
import gtPlusPlus.core.tileentities.general.TileEntityCircuitProgrammer;
import gtPlusPlus.core.tileentities.general.TileEntityFishTrap;
import gtPlusPlus.core.tileentities.general.TileEntityVolumetricFlaskSetter;

public class GuiHandler implements IGuiHandler {

    public static final int GUI1 = 0; // None
    public static final int GUI2 = 1; // None
    public static final int GUI3 = 2; // None
    public static final int GUI4 = 3; // None
    public static final int GUI5 = 4; // Workbench Adv
    public static final int GUI6 = 5; // Fish trap
    public static final int GUI7 = 6; // None
    public static final int GUI8 = 7; // Circuit Programmer
    public static final int GUI9 = 8; // None
    public static final int GUI10 = 9; // None
    public static final int GUI11 = 10; // None
    public static final int GUI12 = 11; // None
    public static final int GUI14 = 13; // None
    public static final int GUI15 = 14; // None
    public static final int GUI16 = 15; // None
    public static final int GUI17 = 16; // None
    public static final int GUI18 = 17; // Volumetric Flask Setter

    public static void init() {
        NetworkRegistry.INSTANCE.registerGuiHandler(GTplusplus.instance, new GuiHandler());
    }

    @Override
    public Object getServerGuiElement(final int ID, final EntityPlayer player, final World world, final int x,
        final int y, final int z) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te == null) return null;

        return switch (ID) {
            case GUI5 -> {
                yield null;
            }
            case GUI6 -> new ContainerFishTrap(player.inventory, (TileEntityFishTrap) te);
            case GUI8 -> new ContainerCircuitProgrammer(player.inventory, (TileEntityCircuitProgrammer) te);
            case GUI18 -> new ContainerVolumetricFlaskSetter(player.inventory, (TileEntityVolumetricFlaskSetter) te);
            default -> null;
        };
    }

    @Override
    public Object getClientGuiElement(final int ID, final EntityPlayer player, final World world, final int x,
        final int y, final int z) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te == null) return null;

        return switch (ID) {
            case GUI6 -> new GUIFishTrap(player.inventory, (TileEntityFishTrap) te);
            case GUI8 -> new GUICircuitProgrammer(player.inventory, (TileEntityCircuitProgrammer) te);
            case GUI18 -> new GUIVolumetricFlaskSetter(
                new ContainerVolumetricFlaskSetter(player.inventory, (TileEntityVolumetricFlaskSetter) te));
            default -> null;
        };
    }

}

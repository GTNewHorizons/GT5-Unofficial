package miscutil.core.gui;

import miscutil.MiscUtils;
import miscutil.core.block.heliumgen.container.ContainerHeliumGenerator;
import miscutil.core.block.heliumgen.gui.GUIHeliumGenerator;
import miscutil.core.block.heliumgen.tileentity.TileEntityHeliumGenerator;
import miscutil.core.util.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;

public class ModGUI {

	
	public static void init(){
		
		Utils.LOG_INFO("Registering GUIs.");
		NetworkRegistry.INSTANCE.registerGuiHandler(MiscUtils.instance, new GUI_HANDLER());
		//Register GuiHandler
		//NetworkRegistry.INSTANCE.registerGuiHandler(MiscUtils.instance, new GuiHandler());
	}
}

class GUI_HANDLER implements IGuiHandler {

    @Override
    public Object getClientGuiElement (int ID, EntityPlayer player, World world, int x, int y, int z){
        if(ID == 0)
            return false;
        else if(ID == 1)
        	   return false;
        else if(ID == 2)
            return new GUIHeliumGenerator(player.inventory, (TileEntityHeliumGenerator)world.getTileEntity(x, y, z));
        else if(ID == 3)
        	   return false;
        return null;
    }

    @Override
    public Object getServerGuiElement (int ID, EntityPlayer player, World world, int x, int y, int z){
        if(ID == 0)
        	   return false;
        else if(ID == 1)
        	   return false;
        else if(ID == 2)
            return new ContainerHeliumGenerator(player.inventory, (TileEntityHeliumGenerator)world.getTileEntity(x, y, z));
        else if(ID == 3)
        	   return false;
         return null;
    }
}

package com.github.bartimaeusnek.bartworks;

import com.github.bartimaeusnek.bartworks.client.gui.GT_GUIContainer_Destructopack;
import com.github.bartimaeusnek.bartworks.server.container.GT_Container_Item_Destructopack;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID){
            case 0: return new GT_Container_Item_Destructopack(player.inventory);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID){
            case 0: return new GT_GUIContainer_Destructopack(player.inventory);
        }
        return null;
    }
}

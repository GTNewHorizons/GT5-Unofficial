package gregtech.common.misc.techtree.gui;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.net.PacketOpenTechTree;
import gregtech.client.GT_KeyBindings;
import gtPlusPlus.core.handler.PacketHandler;

public class GuiOpenEventHandler {

    public static final GuiOpenEventHandler INSTANCE = new GuiOpenEventHandler();

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onKey(InputEvent.KeyInputEvent event) {
        if (GT_KeyBindings.openTechTree.isPressed()) {
            // Yeah, this is the GT++ packet handler
            PacketHandler.sendToServer(new PacketOpenTechTree());
        }
    }
}

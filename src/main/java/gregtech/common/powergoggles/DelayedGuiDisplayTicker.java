package gregtech.common.powergoggles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

// Thanks, InGame-Info :)
public class DelayedGuiDisplayTicker {

    private final GuiScreen guiScreen;
    private int ticks;

    private DelayedGuiDisplayTicker(GuiScreen guiScreen, int delay) {
        this.guiScreen = guiScreen;
        this.ticks = delay;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        this.ticks--;

        if (this.ticks < 0) {
            Minecraft.getMinecraft()
                .displayGuiScreen(this.guiScreen);
            FMLCommonHandler.instance()
                .bus()
                .unregister(this);
        }
    }

    public static void create(GuiScreen guiScreen, int delay) {
        FMLCommonHandler.instance()
            .bus()
            .register(new DelayedGuiDisplayTicker(guiScreen, delay));
    }
}

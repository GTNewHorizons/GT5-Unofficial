package gregtech.client;

import com.gtnewhorizon.gtnhlib.eventbus.EventBusSubscriber;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.relauncher.Side;

@EventBusSubscriber(side = Side.CLIENT)
public class TextureAtlasSpriteSyncher {

    private static int frameCounter;

    public static int getFrameCounter() {
        return frameCounter;
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent event) {
        if (event.phase == Phase.START) {
            frameCounter++;
        }
    }
}

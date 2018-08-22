package gregtech.loaders.postload;

import appeng.items.parts.PartType;
import gregtech.GT_Mod;
import gregtech.api.util.GT_Log;

import java.lang.reflect.Field;

public class GT_AE2EnergyTunnelLoader implements Runnable {
    @Override
    public void run() {
        if (GT_Mod.gregtechproxy.mAE2Integration && GT_Mod.gregtechproxy.mAE2Tunnel) {
            try {
                load();
            } catch (Throwable e) {
                GT_Log.out.println("Failed to load P2P tunnel for GT electricity");
                e.printStackTrace(GT_Log.out);
            }
        }
    }

    public void load() throws Throwable {
        Field f = PartType.class.getDeclaredField("myPart");
        f.setAccessible(true);
        f.set(PartType.P2PTunnelEU, PartP2PGTPower.class);
    }
}


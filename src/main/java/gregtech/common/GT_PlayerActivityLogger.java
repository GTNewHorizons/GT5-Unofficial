package gregtech.common;

import java.util.ArrayList;

import gregtech.GT_Mod;
import gregtech.api.util.GT_Log;

public class GT_PlayerActivityLogger implements Runnable {

    @Override
    public void run() {
        try {
            ArrayList<String> buffer = new ArrayList<>();
            for (;;) {
                if (GT_Log.pal == null) {
                    return;
                }
                String tLastOutput = "";
                // Block on first element for efficiency
                buffer.add(GT_Mod.gregtechproxy.mBufferedPlayerActivity.take());
                GT_Mod.gregtechproxy.mBufferedPlayerActivity.drainTo(buffer);
                for (String output : buffer) {
                    if (!output.equals(tLastOutput)) {
                        GT_Log.pal.println(output);
                        tLastOutput = output;
                    }
                }
                buffer.clear();
                Thread.sleep(10000L);
            }
        } catch (Throwable ignored) {}
    }
}

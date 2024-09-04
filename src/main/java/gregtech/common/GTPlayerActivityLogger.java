package gregtech.common;

import java.util.ArrayList;

import gregtech.GTMod;
import gregtech.api.util.GTLog;

public class GTPlayerActivityLogger implements Runnable {

    @Override
    public void run() {
        try {
            ArrayList<String> buffer = new ArrayList<>();
            for (;;) {
                if (GTLog.pal == null) {
                    return;
                }
                String tLastOutput = "";
                // Block on first element for efficiency
                buffer.add(GTMod.gregtechproxy.mBufferedPlayerActivity.take());
                GTMod.gregtechproxy.mBufferedPlayerActivity.drainTo(buffer);
                for (String output : buffer) {
                    if (!output.equals(tLastOutput)) {
                        GTLog.pal.println(output);
                        tLastOutput = output;
                    }
                }
                buffer.clear();
                // TODO: swap from sleep to event bus
                Thread.sleep(10000L);
            }
        } catch (Throwable ignored) {}
    }
}

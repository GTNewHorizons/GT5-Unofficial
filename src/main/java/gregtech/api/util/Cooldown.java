package gregtech.api.util;

import net.minecraft.server.MinecraftServer;

public class Cooldown {

    public Cooldown(int aLengthInTicks) {
        this.lengthInTicks = aLengthInTicks;
        this.lastTimeStarted = -aLengthInTicks;
    }

    public void set() {
        lastTimeStarted = getServerTimeInTicks();
    }

    public boolean hasPassed() {
        final int currServTime = getServerTimeInTicks();
        return currServTime - lastTimeStarted >= lengthInTicks;
    }

    private int getServerTimeInTicks() {
        return MinecraftServer.getServer()
            .getTickCounter();
    }

    private int lastTimeStarted;
    private int lengthInTicks;
}

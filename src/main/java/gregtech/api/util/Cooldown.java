package gregtech.api.util;

import java.security.InvalidParameterException;

import net.minecraft.server.MinecraftServer;

public class Cooldown {

    public Cooldown(int aLengthInTicks) {

        if (aLengthInTicks <= 0) throw new InvalidParameterException("length should be a positive non-zero number");

        this.lengthInTicks = aLengthInTicks;
        this.lastTimeStarted = -aLengthInTicks;
    }

    public void set() {
        lastTimeStarted = getServerTimeInTicks();
        // GT_Log.out.println(String.format("cooldown set on %d", lastTimeStarted));
    }

    public boolean hasPassed() {
        final int currServTime = getServerTimeInTicks();
        // GT_Log.out.println(
        // String.format(
        // "cooldown has passed = %b on %d",
        // currServTime - lastTimeStarted >= lengthInTicks,
        // lastTimeStarted));
        return currServTime - lastTimeStarted >= lengthInTicks;
    }

    private int getServerTimeInTicks() {
        return MinecraftServer.getServer()
            .getTickCounter();
    }

    private int lastTimeStarted;
    protected int lengthInTicks;
}

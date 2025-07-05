package gregtech.common.data.maglev;

public final class Tether {

    private final int sourceX;
    private final int sourceY;
    private final int sourceZ;
    private int playersConnected;

    public Tether(int sourceX, int sourceY, int sourceZ) {
        this.sourceX = sourceX;
        this.sourceY = sourceY;
        this.sourceZ = sourceZ;
    }

    public int sourceX() {
        return sourceX;
    }

    public int sourceY() {
        return sourceY;
    }

    public int sourceZ() {
        return sourceZ;
    }

    public boolean hasPlayerConnected() {
        return playersConnected > 0;
    }

    public void connectPlayer() {
        playersConnected++;
    }

    public void disconnectPlayer() {
        playersConnected--;
        playersConnected = Math.max(0, playersConnected);
    }
}

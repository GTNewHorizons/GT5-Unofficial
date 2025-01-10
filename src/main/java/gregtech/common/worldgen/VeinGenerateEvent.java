package gregtech.common.worldgen;

import net.minecraft.world.World;

import cpw.mods.fml.common.eventhandler.Event;
import gregtech.common.WorldgenGTOreLayer;

public class VeinGenerateEvent extends Event {

    public final World world;
    public final int chunkX, chunkZ, oreSeedX, oreSeedZ;
    public final WorldgenGTOreLayer layer;

    private int result = -1;

    public VeinGenerateEvent(World world, int chunkX, int chunkZ, int oreSeedX, int oreSeedZ,
        WorldgenGTOreLayer layer) {
        this.world = world;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.oreSeedX = oreSeedX;
        this.oreSeedZ = oreSeedZ;
        this.layer = layer;
    }

    @Override
    public boolean isCancelable() {
        return true;
    }

    public void setOreGenResult(int result) {
        this.result = result;
    }

    public int getOreGenResult() {
        return result;
    }
}

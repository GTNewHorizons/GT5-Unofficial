package gregtech.api.events;

import net.minecraft.world.World;

import cpw.mods.fml.common.eventhandler.Event;
import gregtech.common.WorldgenGTOreLayer;

public class VeinGenerateEvent extends Event {

    public final World world;
    public final int chunkX, chunkZ, oreSeedX, oreSeedZ;
    public final WorldgenGTOreLayer layer;
    public final int result;

    public VeinGenerateEvent(World world, int chunkX, int chunkZ, int oreSeedX, int oreSeedZ, WorldgenGTOreLayer layer,
        int result) {
        this.world = world;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.oreSeedX = oreSeedX;
        this.oreSeedZ = oreSeedZ;
        this.layer = layer;
        this.result = result;
    }
}

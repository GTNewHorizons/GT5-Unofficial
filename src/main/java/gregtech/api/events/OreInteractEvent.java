package gregtech.api.events;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import cpw.mods.fml.common.eventhandler.Event;

public class OreInteractEvent extends Event {

    public final World world;
    public final int x, y, z;
    public final Block block;
    public final int meta;
    public final EntityPlayer player;

    public OreInteractEvent(World world, int x, int y, int z, Block block, int meta, EntityPlayer player) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.block = block;
        this.meta = meta;
        this.player = player;
    }
}

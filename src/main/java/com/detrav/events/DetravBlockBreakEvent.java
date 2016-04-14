package com.detrav.events;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;

/**
 * Created by wital_000 on 14.04.2016.
 */
public class DetravBlockBreakEvent extends BlockEvent.BreakEvent {
    public DetravBlockBreakEvent(int x, int y, int z, World world, Block block, int blockMetadata, EntityPlayer player) {
        super(x, y, z, world, block, blockMetadata, player);
    }
}

package gtPlusPlus.core.util.minecraft.particles;

import net.minecraft.block.Block;
import net.minecraft.world.World;

import gtPlusPlus.xmod.forestry.HANDLER_FR;

public class BlockBreakParticles {

	public BlockBreakParticles(final World world, final int x, final int y, final int z, final Block block){
		try {
			HANDLER_FR.createBlockBreakParticles(world, x, y, z, block);
		} catch (final Throwable T){

		}
	}

}

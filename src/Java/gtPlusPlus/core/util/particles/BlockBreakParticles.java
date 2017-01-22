package gtPlusPlus.core.util.particles;

import gtPlusPlus.xmod.forestry.HANDLER_FR;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public class BlockBreakParticles {

	public BlockBreakParticles(World world, int x, int y, int z, Block block){
		try {
			HANDLER_FR.createBlockBreakParticles(world, x, y, z, block);
		} catch (Throwable T){
			
		}
	}
	
}

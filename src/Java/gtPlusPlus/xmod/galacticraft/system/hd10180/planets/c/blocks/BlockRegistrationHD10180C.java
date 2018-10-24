package gtPlusPlus.xmod.galacticraft.system.hd10180.planets.c.blocks;

import java.util.LinkedHashMap;
import java.util.Map;

import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.xmod.galacticraft.system.objects.IPlanetBlockRegister;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class BlockRegistrationHD10180C implements IPlanetBlockRegister {

	private Map<Integer, Block> mBlocks = new LinkedHashMap<Integer, Block>();
	private static AutoMap<Block> mBlocksToRegister = new AutoMap<Block>();

	public static void initialize() {
		//mBlocksToRegister.put(new TCetiEBlocks());
		//mBlocksToRegister.put(new TCetiEBlockDandelions());
	}	
	
	@Override
	public Map<Integer, Block> getBlocks() {
		return mBlocks;
	}

	@Override
	public Block getTopLayer() {
		return mBlocks.get(0);
	}

	@Override
	public Block getSoil() {
		return mBlocks.get(1);
	}

	@Override
	public Block getSoil2() {
		return mBlocks.get(2);
	}

	@Override
	public Block getStone() {
		return mBlocks.get(3);
	}

	@Override
	public Block getWaterBlock() {
		return mBlocks.get(4);
	}

	@Override
	public void register() {		
		//Register Blocks, Add to List
		mBlocks.put(0, Blocks.grass);
		mBlocks.put(1, Blocks.dirt);
		mBlocks.put(2, Blocks.gravel);
		mBlocks.put(3, Blocks.stone);
		mBlocks.put(4, Blocks.lava);
		
	}

}

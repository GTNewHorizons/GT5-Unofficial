package com.detrav.utils;

import gtPlusPlus.core.block.base.BlockBaseOre;
import gtPlusPlus.core.material.Material;
import net.minecraft.block.Block;

import java.util.HashMap;

/**
 * Created by bartimaeusnek on 19.04.2018.
 */
public class GTppHelper {
	public static final HashMap<Short,Material> decodeoresGTpp = new HashMap<>();
	public static final HashMap<Material,Short> encodeoresGTpp = new HashMap<>();
	
	public static void generate_OreIDs() {
		for (short n=0 ; n < gtPlusPlus.core.material.ORES.class.getFields().length ; ++n) {
			try {
				Short i = (short) (n+1);
				Material m = ((Material)gtPlusPlus.core.material.ORES.class.getFields()[n].get(gtPlusPlus.core.material.ORES.class.getFields()[n]));
				decodeoresGTpp.put(i,m);
				encodeoresGTpp.put(m,i);
			} catch (Exception ignored) {}
			
		}
	}


	public static boolean isGTppBlock(Block tBlock){
		return tBlock instanceof BlockBaseOre;
	}


	public static short getGTppMeta(Block tBlock){
			return (short) (GTppHelper.encodeoresGTpp.get(((BlockBaseOre) tBlock).getMaterialEx()) +7000);
	}

	public static String getGTppVeinName(Block tBlock){
			return tBlock.getLocalizedName();
	}

}

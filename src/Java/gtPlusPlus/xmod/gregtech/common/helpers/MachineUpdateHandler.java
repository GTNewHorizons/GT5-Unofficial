package gtPlusPlus.xmod.gregtech.common.helpers;

import java.util.HashMap;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.GregTech_API;
import net.minecraft.block.Block;
import net.minecraftforge.event.world.BlockEvent;

public class MachineUpdateHandler {

	private static final HashMap<String, Block> mBlockCache = new HashMap<String, Block>();
	
	public static void registerBlockToCauseMachineUpdate(String aUnlocalName, Block aBlock) {
		mBlockCache.put(aUnlocalName, aBlock);
	}
	
    @SubscribeEvent
    public void onBlockEvent(BlockEvent event) {
    	Block aBlock = event.block;
    	String aUnlocalName = aBlock != null ? aBlock.getUnlocalizedName() : "NULL";
    	boolean aDoUpdate = false;
    	if (aBlock != null && aUnlocalName != null && !aUnlocalName.equals("NULL")) {
    		for (String aCachedName : mBlockCache.keySet()) {
    			if (aCachedName.equals(aUnlocalName)) {
					aDoUpdate = true;
					break;    				
    			}
    			else {
    				if (aBlock == mBlockCache.get(aCachedName)) {
    					aDoUpdate = true;
    					break;
    				}
    			}
    		}
    		if (aDoUpdate) {
        		GregTech_API.causeMachineUpdate(event.world, event.x, event.y, event.z);    			
    		}
    	}
    }
	
}

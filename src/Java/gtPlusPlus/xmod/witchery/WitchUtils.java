package gtPlusPlus.xmod.witchery;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.entity.ai.EntityAIDigBlocks;
import com.mojang.authlib.GameProfile;

import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.oredict.OreDictionary;

public class WitchUtils {

	private static final Field KOBOLDITE_MINER_PROFILE;
	
	static {
		if (LoadedMods.Witchery) {
			KOBOLDITE_MINER_PROFILE = getField("com.emoniph.witchery.entity.ai.EntityAIDigBlocks", "KOBOLDITE_MINER_PROFILE");			
		}
		else {
			KOBOLDITE_MINER_PROFILE = null;
		}
		
	}
	
	//com.emoniph.witchery.entity.ai.EntityAIDigBlocks.onHarvestDrops(EntityPlayer, HarvestDropsEvent)
    public static void onHarvestDrops(final EntityPlayer harvester, final BlockEvent.HarvestDropsEvent event) {
        if (harvester != null && !harvester.worldObj.isRemote && !event.isCanceled() && (isEqual(harvester.getGameProfile(), EntityAIDigBlocks.KOBOLDITE_MINER_PROFILE) || isEqual(harvester.getGameProfile(), EntityAIDigBlocks.NORMAL_MINER_PROFILE))) {
            final boolean hasKobolditePick = isEqual(harvester.getGameProfile(), EntityAIDigBlocks.KOBOLDITE_MINER_PROFILE);
            final ArrayList<ItemStack> newDrops = new ArrayList<ItemStack>();
            double kobolditeChance = hasKobolditePick ? 0.02 : 0.01;
            for (final ItemStack drop : event.drops) {
                final int[] oreIDs = OreDictionary.getOreIDs(drop);
                boolean addOriginal = true;
                if (oreIDs.length > 0) {
                    final String oreName = OreDictionary.getOreName(oreIDs[0]);
                    if (oreName != null && oreName.startsWith("ore")) {
                        final ItemStack smeltedDrop = FurnaceRecipes.smelting().getSmeltingResult(drop);
                        if (smeltedDrop != null && hasKobolditePick && harvester.worldObj.rand.nextDouble() < 0.5) {
                            addOriginal = false;
                            newDrops.add(smeltedDrop.copy());
                            newDrops.add(smeltedDrop.copy());
                            if (harvester.worldObj.rand.nextDouble() < 0.25) {
                                newDrops.add(smeltedDrop.copy());
                            }
                        }
                        kobolditeChance = (hasKobolditePick ? 0.08 : 0.05);
                    }
                }
                if (addOriginal) {
                    newDrops.add(drop);
                }
            }
            event.drops.clear();
            for (final ItemStack newDrop : newDrops) {
                event.drops.add(newDrop);
            }
            if (kobolditeChance > 0.0 && harvester.worldObj.rand.nextDouble() < kobolditeChance) {
                event.drops.add(Witchery.Items.GENERIC.itemKobolditeDust.createStack());
            }
        }
    }
    
    public static Field getField(String aClassName, String aFieldName) {    	
    	Class c;
		try {
			c = Class.forName(aClassName);
	    	if (c != null) {
	    		Field f = ReflectionUtils.getField(c, aFieldName);
	    		if (f != null) {
	    			return f;
	    		}
	    	}
		} catch (ClassNotFoundException | NoSuchFieldException e) {
		}
		return null;
    }
    
    public static boolean isEqual(final GameProfile a, final GameProfile b) {
        return a != null && b != null && a.getId() != null && b.getId() != null && a.getId().equals(b.getId());
    }
	
}

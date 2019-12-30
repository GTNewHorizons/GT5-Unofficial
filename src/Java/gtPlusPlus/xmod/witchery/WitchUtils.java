package gtPlusPlus.xmod.witchery;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.mojang.authlib.GameProfile;

import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.material.ALLOY;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.oredict.OreDictionary;

public class WitchUtils {
	
	private static final GameProfile NORMAL_MINER_PROFILE;
	private static final GameProfile KOBOLDITE_MINER_PROFILE;

	static {
		Field a1 = null, a2 = null;
		GameProfile b1 = null, b2 = null;
		if (LoadedMods.Witchery) {
			try {
				a1 = getField("com.emoniph.witchery.entity.ai.EntityAIDigBlocks", "NORMAL_MINER_PROFILE");
				a2 = getField("com.emoniph.witchery.entity.ai.EntityAIDigBlocks", "KOBOLDITE_MINER_PROFILE");
				b1 = (GameProfile) a1.get(null);
				b2 = (GameProfile) a2.get(null);
			}
			catch (Throwable t) {				
			}
		}		
		NORMAL_MINER_PROFILE = b1;
		KOBOLDITE_MINER_PROFILE = b2;
	}

	//com.emoniph.witchery.entity.ai.EntityAIDigBlocks.onHarvestDrops(EntityPlayer, HarvestDropsEvent)
	public static void onHarvestDrops(final EntityPlayer harvester, final BlockEvent.HarvestDropsEvent event) {


		if (LoadedMods.Witchery) {			

			if (harvester != null && !harvester.worldObj.isRemote && !event.isCanceled() && (isEqual(harvester.getGameProfile(), KOBOLDITE_MINER_PROFILE) || isEqual(harvester.getGameProfile(), NORMAL_MINER_PROFILE))) {
				final boolean hasKobolditePick = isEqual(harvester.getGameProfile(), KOBOLDITE_MINER_PROFILE);
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
					event.drops.add(ALLOY.KOBOLDITE.getDust(1));
				}
			}
		}   	

	}

	public static Field getField(String aClassName, String aFieldName) {
		Class c;
		c = ReflectionUtils.getClass(aClassName);
		if (c != null) {
			Field f = ReflectionUtils.getField(c, aFieldName);
			if (f != null) {
				return f;
			}
		}
		return null;
	}

	public static boolean isEqual(final GameProfile a, final GameProfile b) {
		return a != null && b != null && a.getId() != null && b.getId() != null && a.getId().equals(b.getId());
	}

}

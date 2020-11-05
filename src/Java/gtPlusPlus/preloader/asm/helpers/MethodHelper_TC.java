package gtPlusPlus.preloader.asm.helpers;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;

public class MethodHelper_TC {

	private static Class mThaumcraftCraftingManager;

	public static AspectList generateTags(Item item, int meta) {
		AspectList temp = generateTags(item, meta, new ArrayList());
		return temp;
	}

	public static AspectList generateTags(final Item item, final int meta, final ArrayList<List> history) {
		int tmeta = meta;
		if (item == null) {
			return null;
		}
		//Preloader_Logger.INFO("Generating aspect tags for "+item.getUnlocalizedName()+":"+meta);
		try {
			tmeta = ((new ItemStack(item, 1, meta).getItem().isDamageable() || !new ItemStack(item, 1, meta).getItem().getHasSubtypes()) ? 32767 : meta);
		}
		catch (Exception ex) {}
		//Preloader_Logger.INFO("Set Meta to "+tmeta);
		if (ThaumcraftApi.exists(item, tmeta)) {
			return ThaumcraftCraftingManager.getObjectTags(new ItemStack(item, 1, tmeta));
		}
		if (history.contains(Arrays.asList(item, tmeta))) {
			return null;
		}
		history.add(Arrays.asList(item, tmeta));
		if (history.size() < 100) {
			AspectList ret = generateTagsFromRecipes(item, (tmeta == 32767) ? 0 : meta, history);
			ret = capAspects(ret, 64);
			ThaumcraftApi.registerObjectTag(new ItemStack(item, 1, tmeta), ret);
			return ret;
		}
		return null;
	}

	private static AspectList capAspects(final AspectList sourcetags, final int amount) {
		if (sourcetags == null) {
			return sourcetags;
		}
		final AspectList out = new AspectList();
		for (final Aspect aspect : sourcetags.getAspects()) {
			out.merge(aspect, Math.min(amount, sourcetags.getAmount(aspect)));
		}
		return out;
	}

	private static AspectList generateTagsFromRecipes(final Item item, final int meta, final ArrayList<List> history) {
		AspectList ret = null;
		ret = generateTagsFromCrucibleRecipes(item, meta, history);
		if (ret != null) {
			return ret;
		}
		ret = generateTagsFromArcaneRecipes(item, meta, history);
		if (ret != null) {
			return ret;
		}
		ret = generateTagsFromInfusionRecipes(item, meta, history);
		if (ret != null) {
			return ret;
		}
		ret = generateTagsFromCraftingRecipes(item, meta, history);
		return ret;
	}

	private static boolean isClassSet() {
		if (mThaumcraftCraftingManager == null) {
			mThaumcraftCraftingManager = ReflectionUtils.getClass("thaumcraft.common.lib.crafting.ThaumcraftCraftingManager");
		}    	
		return true;
	}

	private static Method mGetTagsFromCraftingRecipes;
	private static Method mGetTagsFromInfusionRecipes;
	private static Method mGetTagsFromArcaneRecipes;
	private static Method mGetTagsFromCrucibleRecipes;

	private static AspectList generateTagsFromCraftingRecipes(Item item, int meta, ArrayList<List> history) {
		isClassSet();
		if (mGetTagsFromCraftingRecipes == null) {
			mGetTagsFromCraftingRecipes = ReflectionUtils.getMethod(mThaumcraftCraftingManager, "generateTagsFromCraftingRecipes", new Class[] {Item.class, int.class, ArrayList.class});
		}
		return (AspectList) ReflectionUtils.invokeNonBool(null, mGetTagsFromCraftingRecipes, new Object[] {item, meta, history});
	}

	private static AspectList generateTagsFromInfusionRecipes(Item item, int meta, ArrayList<List> history) {
		isClassSet();
		if (mGetTagsFromInfusionRecipes == null) {
			mGetTagsFromInfusionRecipes = ReflectionUtils.getMethod(mThaumcraftCraftingManager, "generateTagsFromInfusionRecipes", new Class[] {Item.class, int.class, ArrayList.class});
		}
		return (AspectList) ReflectionUtils.invokeNonBool(null, mGetTagsFromInfusionRecipes, new Object[] {item, meta, history});
	}

	private static AspectList generateTagsFromArcaneRecipes(Item item, int meta, ArrayList<List> history) {
		isClassSet();
		if (mGetTagsFromArcaneRecipes == null) {
			mGetTagsFromArcaneRecipes = ReflectionUtils.getMethod(mThaumcraftCraftingManager, "generateTagsFromArcaneRecipes", new Class[] {Item.class, int.class, ArrayList.class});
		}
		return (AspectList) ReflectionUtils.invokeNonBool(null, mGetTagsFromArcaneRecipes, new Object[] {item, meta, history});
	}

	private static AspectList generateTagsFromCrucibleRecipes(Item item, int meta, ArrayList<List> history) {
		isClassSet();
		if (mGetTagsFromCrucibleRecipes == null) {
			mGetTagsFromCrucibleRecipes = ReflectionUtils.getMethod(mThaumcraftCraftingManager, "generateTagsFromCrucibleRecipes", new Class[] {Item.class, int.class, ArrayList.class});
		}
		return (AspectList) ReflectionUtils.invokeNonBool(null, mGetTagsFromCrucibleRecipes, new Object[] {item, meta, history});
	}



	/*
	 * Let's improve the TC lookup for aspects, cause the default implementation is shit. 
	 */


	public static AspectList getObjectTags(ItemStack itemstack) {
		Item item;
		int meta;
		try {
			item = itemstack.getItem();
			meta = itemstack.getItemDamage();
		} catch (Exception var8) {
			return null;
		}

		AspectList tmp = (AspectList)ThaumcraftApi.objectTags.get(Arrays.asList(new Object[]{item, Integer.valueOf(meta)}));
		if(tmp == null) {
			for(List l : ThaumcraftApi.objectTags.keySet()) {
				if((Item)l.get(0) == item && l.get(1) instanceof int[]) {
					int[] range = (int[])((int[])l.get(1));
					Arrays.sort(range);
					if(Arrays.binarySearch(range, meta) >= 0) {
						tmp = (AspectList)ThaumcraftApi.objectTags.get(Arrays.asList(new Object[]{item, range}));
						return tmp;
					}
				}
			}

			tmp = (AspectList)ThaumcraftApi.objectTags.get(Arrays.asList(new Object[]{item, Integer.valueOf(32767)}));
			if(tmp == null && tmp == null) {
				if(meta == 32767 && tmp == null) {
					int index = 0;

					while(true) {
						tmp = (AspectList)ThaumcraftApi.objectTags.get(Arrays.asList(new Object[]{item, Integer.valueOf(index)}));
						++index;
						if(index >= 16 || tmp != null) {
							break;
						}
					}
				}

				if(tmp == null) {
					tmp = generateTags(item, meta);
				}
			}
		}

		if(itemstack.getItem() instanceof ItemWandCasting) {
			ItemWandCasting wand = (ItemWandCasting)itemstack.getItem();
			if(tmp == null) {
				tmp = new AspectList();
			}

			tmp.merge(Aspect.MAGIC, (wand.getRod(itemstack).getCraftCost() + wand.getCap(itemstack).getCraftCost()) / 2);
			tmp.merge(Aspect.TOOL, (wand.getRod(itemstack).getCraftCost() + wand.getCap(itemstack).getCraftCost()) / 3);
		}

		if(item != null && item == Items.potionitem) {
			if(tmp == null) {
				tmp = new AspectList();
			}

			tmp.merge(Aspect.WATER, 1);
			ItemPotion ip = (ItemPotion)item;
			List<PotionEffect> effects = ip.getEffects(itemstack.getItemDamage());
			if(effects != null) {
				if(ItemPotion.isSplash(itemstack.getItemDamage())) {
					tmp.merge(Aspect.ENTROPY, 2);
				}

				for(PotionEffect var6 : effects) {
					tmp.merge(Aspect.MAGIC, (var6.getAmplifier() + 1) * 2);
					if(var6.getPotionID() == Potion.blindness.id) {
						tmp.merge(Aspect.DARKNESS, (var6.getAmplifier() + 1) * 3);
					} else if(var6.getPotionID() == Potion.confusion.id) {
						tmp.merge(Aspect.ELDRITCH, (var6.getAmplifier() + 1) * 3);
					} else if(var6.getPotionID() == Potion.damageBoost.id) {
						tmp.merge(Aspect.WEAPON, (var6.getAmplifier() + 1) * 3);
					} else if(var6.getPotionID() == Potion.digSlowdown.id) {
						tmp.merge(Aspect.TRAP, (var6.getAmplifier() + 1) * 3);
					} else if(var6.getPotionID() == Potion.digSpeed.id) {
						tmp.merge(Aspect.TOOL, (var6.getAmplifier() + 1) * 3);
					} else if(var6.getPotionID() == Potion.fireResistance.id) {
						tmp.merge(Aspect.ARMOR, var6.getAmplifier() + 1);
						tmp.merge(Aspect.FIRE, (var6.getAmplifier() + 1) * 2);
					} else if(var6.getPotionID() == Potion.harm.id) {
						tmp.merge(Aspect.DEATH, (var6.getAmplifier() + 1) * 3);
					} else if(var6.getPotionID() == Potion.heal.id) {
						tmp.merge(Aspect.HEAL, (var6.getAmplifier() + 1) * 3);
					} else if(var6.getPotionID() == Potion.hunger.id) {
						tmp.merge(Aspect.DEATH, (var6.getAmplifier() + 1) * 3);
					} else if(var6.getPotionID() == Potion.invisibility.id) {
						tmp.merge(Aspect.SENSES, (var6.getAmplifier() + 1) * 3);
					} else if(var6.getPotionID() == Potion.jump.id) {
						tmp.merge(Aspect.FLIGHT, (var6.getAmplifier() + 1) * 3);
					} else if(var6.getPotionID() == Potion.moveSlowdown.id) {
						tmp.merge(Aspect.TRAP, (var6.getAmplifier() + 1) * 3);
					} else if(var6.getPotionID() == Potion.moveSpeed.id) {
						tmp.merge(Aspect.MOTION, (var6.getAmplifier() + 1) * 3);
					} else if(var6.getPotionID() == Potion.nightVision.id) {
						tmp.merge(Aspect.SENSES, (var6.getAmplifier() + 1) * 3);
					} else if(var6.getPotionID() == Potion.poison.id) {
						tmp.merge(Aspect.POISON, (var6.getAmplifier() + 1) * 3);
					} else if(var6.getPotionID() == Potion.regeneration.id) {
						tmp.merge(Aspect.HEAL, (var6.getAmplifier() + 1) * 3);
					} else if(var6.getPotionID() == Potion.resistance.id) {
						tmp.merge(Aspect.ARMOR, (var6.getAmplifier() + 1) * 3);
					} else if(var6.getPotionID() == Potion.waterBreathing.id) {
						tmp.merge(Aspect.AIR, (var6.getAmplifier() + 1) * 3);
					} else if(var6.getPotionID() == Potion.weakness.id) {
						tmp.merge(Aspect.DEATH, (var6.getAmplifier() + 1) * 3);
					}
				}
			}
		}

		return capAspects(tmp, 64);
	}

}

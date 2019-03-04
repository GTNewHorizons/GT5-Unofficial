package gtPlusPlus.xmod.thaumcraft.aspect;

import gregtech.common.GT_ThaumcraftCompat;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.thaumcraft.aspect.GTPP_Aspects.TC_AspectStack_Ex;
import gtPlusPlus.xmod.thaumcraft.util.ThaumcraftUtils;

import gregtech.api.enums.TC_Aspects;
import gregtech.api.enums.TC_Aspects.TC_AspectStack;
import gregtech.api.interfaces.internal.IThaumcraftCompat;
import gregtech.api.util.GT_LanguageManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public class GTPP_AspectCompat implements IThaumcraftCompat {
	
	public static volatile Method m = null;
	
	private static HashMap<String, TC_Aspect> mAspectCache = new LinkedHashMap<String, TC_Aspect>();
	
	public GTPP_AspectCompat() {
		
		
		//Generate all existing Aspects as TC_Aspects
		LinkedHashMap<String, Object> h = TC_Aspect.getVanillaAspectList();
		for (String g : h.keySet()) {
			Object aBaseAspect = h.get(g);
			if (aBaseAspect != null && TC_Aspect.isObjectAnAspect(aBaseAspect)) {
				TC_Aspect aS = TC_Aspect.getAspect(g);
				if (aS != null) {
					mAspectCache.put(g, aS);
					continue;
				}
			}
		}
		
		
		
		
		// Custom Aspects
		GTPP_Aspects.CUSTOM_1.mAspect = new Aspect("custom1", 15647411, new Aspect[]{Aspect.COLD, Aspect.FIRE},
				new ResourceLocation("gregtech:textures/aspects/" + TC_Aspects.RADIO.name() + ".png"), 1);
		GTPP_Aspects.CUSTOM_2.mAspect = new Aspect("custom2", 15658622, new Aspect[]{Aspect.MAGIC, Aspect.SLIME},
				new ResourceLocation("gregtech:textures/aspects/" + TC_Aspects.RADIO.name() + ".png"), 1);
		GTPP_Aspects.CUSTOM_3.mAspect = new Aspect("custom3", 12644078, new Aspect[]{Aspect.ENERGY, Aspect.ARMOR},
				new ResourceLocation("gregtech:textures/aspects/" + TC_Aspects.RADIO.name() + ".png"), 1);
		GTPP_Aspects.CUSTOM_4.mAspect = new Aspect("custom4", 12632256, new Aspect[]{Aspect.METAL, Aspect.POISON},
				new ResourceLocation("gregtech:textures/aspects/" + TC_Aspects.RADIO.name() + ".png"), 1);
		GTPP_Aspects.CUSTOM_5.mAspect = new Aspect("custom4", 12648384, new Aspect[]{Aspect.LIGHT, Aspect.SOUL},
				new ResourceLocation("gregtech:textures/aspects/" + TC_Aspects.RADIO.name() + ".png"), 1);
		GT_LanguageManager.addStringLocalization("tc.aspect.custom1", "Balance");
		GT_LanguageManager.addStringLocalization("tc.aspect.custom2", "Lust");
		GT_LanguageManager.addStringLocalization("tc.aspect.custom3", "Starbound");
		GT_LanguageManager.addStringLocalization("tc.aspect.custom4", "Toxic");
		GT_LanguageManager.addStringLocalization("tc.aspect.custom5", "Heaven");
	}

	public static synchronized final AspectList getAspectList(final List<TC_AspectStack_Ex> aAspects) {
		AspectList o = null;
		try {
			if (m == null || (m != null && !m.isAccessible())) {
				m = GT_ThaumcraftCompat.class.getDeclaredMethod("getAspectList", List.class);
				m.setAccessible(true);				
			}
			if (m != null) {
				o = (AspectList) m.invoke(null, aAspects);				
			}
		}
		catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}		
		if (o == null) {
			Logger.REFLECTION("[Aspect] Did not retrieve valid aspect list from reflective invocation.");
		}
		return o;
	}
	
	public static synchronized final AspectList getAspectList_Ex(final List<TC_AspectStack_Ex> aAspects) {
		final AspectList rAspects = new AspectList();
		for (final TC_AspectStack_Ex tAspect : aAspects) {
			rAspects.add((Aspect) tAspect.mAspect.mAspect, (int) tAspect.mAmount);
		}
		return rAspects;
	}

	@Override
	public boolean registerPortholeBlacklistedBlock(Block p0) {
		return ThaumcraftUtils.registerPortholeBlacklistedBlock(p0);
	}

	@Override
	public boolean registerThaumcraftAspectsToItem(ItemStack p0, List<TC_AspectStack> p1, boolean p2) {
		return ThaumcraftUtils.registerThaumcraftAspectsToItem(p0, ThaumcraftUtils.convertAspectStack(p1), p2);
	}

	@Override
	public boolean registerThaumcraftAspectsToItem(ItemStack p0, List<TC_AspectStack> p1, String p2) {
		return ThaumcraftUtils.registerThaumcraftAspectsToItem(p0, ThaumcraftUtils.convertAspectStack(p1), p2);
	}

	@Override
	public Object addCrucibleRecipe(String p0, Object p1, ItemStack p2, List<TC_AspectStack> p3) {
		return ThaumcraftUtils.addCrucibleRecipe(p0, p1, p2, ThaumcraftUtils.convertAspectStack(p3));
	}

	@Override
	public Object addInfusionRecipe(String p0, ItemStack p1, ItemStack[] p2, ItemStack p3, int p4, List<TC_AspectStack> p5) {
		return ThaumcraftUtils.addInfusionRecipe(p0, p1, p2, p3, p4, ThaumcraftUtils.convertAspectStack(p5));
	}

	@Override
	public Object addResearch(String p0, String p1, String p2, String[] p3, String p4, ItemStack p5, int p6, int p7,
			int p8, int p9, List<TC_AspectStack> p10, ItemStack[] p11, Object[] p12) {
		return ThaumcraftUtils.addResearch(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, ThaumcraftUtils.convertAspectStack(p10), p11, p12);
	}
	
}
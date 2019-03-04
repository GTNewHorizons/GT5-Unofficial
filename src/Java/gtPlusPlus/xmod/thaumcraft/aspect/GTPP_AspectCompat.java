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
	

	public static TC_Aspect ASPECT_BALANCE;
	public static TC_Aspect ASPECT_LUST;
	public static TC_Aspect ASPECT_STARBOUND;
	public static TC_Aspect ASPECT_TOXIC;
	public static TC_Aspect ASPECT_HEAVEN;
	
	
/*	TC_Aspects.AER.mAspect = Aspect.AIR;
	TC_Aspects.ALIENIS.mAspect = Aspect.ELDRITCH;
	TC_Aspects.AQUA.mAspect = Aspect.WATER;
	TC_Aspects.ARBOR.mAspect = Aspect.TREE;
	TC_Aspects.AURAM.mAspect = Aspect.AURA;
	TC_Aspects.BESTIA.mAspect = Aspect.BEAST;
	TC_Aspects.COGNITIO.mAspect = Aspect.MIND;
	TC_Aspects.CORPUS.mAspect = Aspect.FLESH;
	TC_Aspects.EXANIMIS.mAspect = Aspect.UNDEAD;
	TC_Aspects.FABRICO.mAspect = Aspect.CRAFT;
	TC_Aspects.FAMES.mAspect = Aspect.HUNGER;
	TC_Aspects.GELUM.mAspect = Aspect.COLD;
	TC_Aspects.GRANUM.mAspect = Aspect.PLANT;
	TC_Aspects.HERBA.mAspect = Aspect.PLANT;
	TC_Aspects.HUMANUS.mAspect = Aspect.MAN;
	TC_Aspects.IGNIS.mAspect = Aspect.FIRE;
	TC_Aspects.INSTRUMENTUM.mAspect = Aspect.TOOL;
	TC_Aspects.ITER.mAspect = Aspect.TRAVEL;
	TC_Aspects.LIMUS.mAspect = Aspect.SLIME;
	TC_Aspects.LUCRUM.mAspect = Aspect.GREED;
	TC_Aspects.LUX.mAspect = Aspect.LIGHT;
	TC_Aspects.MACHINA.mAspect = Aspect.MECHANISM;
	TC_Aspects.MESSIS.mAspect = Aspect.CROP;
	TC_Aspects.METALLUM.mAspect = Aspect.METAL;
	TC_Aspects.METO.mAspect = Aspect.HARVEST;
	TC_Aspects.MORTUUS.mAspect = Aspect.DEATH;
	TC_Aspects.MOTUS.mAspect = Aspect.MOTION;
	TC_Aspects.ORDO.mAspect = Aspect.ORDER;
	TC_Aspects.PANNUS.mAspect = Aspect.CLOTH;
	TC_Aspects.PERDITIO.mAspect = Aspect.ENTROPY;
	TC_Aspects.PERFODIO.mAspect = Aspect.MINE;
	TC_Aspects.PERMUTATIO.mAspect = Aspect.EXCHANGE;
	TC_Aspects.POTENTIA.mAspect = Aspect.ENERGY;
	TC_Aspects.PRAECANTATIO.mAspect = Aspect.MAGIC;
	TC_Aspects.SANO.mAspect = Aspect.HEAL;
	TC_Aspects.SENSUS.mAspect = Aspect.SENSES;
	TC_Aspects.SPIRITUS.mAspect = Aspect.SOUL;
	TC_Aspects.TELUM.mAspect = Aspect.WEAPON;
	TC_Aspects.TERRA.mAspect = Aspect.EARTH;
	TC_Aspects.TEMPESTAS.mAspect = Aspect.WEATHER;
	TC_Aspects.TENEBRAE.mAspect = Aspect.DARKNESS;
	TC_Aspects.TUTAMEN.mAspect = Aspect.ARMOR;
	TC_Aspects.VACUOS.mAspect = Aspect.VOID;
	TC_Aspects.VENENUM.mAspect = Aspect.POISON;
	TC_Aspects.VICTUS.mAspect = Aspect.LIFE;
	TC_Aspects.VINCULUM.mAspect = Aspect.TRAP;
	TC_Aspects.VITIUM.mAspect = Aspect.TAINT;
	TC_Aspects.VITREUS.mAspect = Aspect.CRYSTAL;
	TC_Aspects.VOLATUS.mAspect = Aspect.FLIGHT;*/	
	
	
	public GTPP_AspectCompat() {
		
		
		// Generate all existing Aspects as TC_Aspects
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
		ASPECT_BALANCE = 
				new TC_Aspect(
						"Purity",
						15647411,
						new TC_Aspect[]{
								get(TC_Aspects.GELUM),
								get(TC_Aspects.IGNIS)
								},
				new ResourceLocation("gregtech:textures/aspects/" + TC_Aspects.RADIO.name() + ".png"),
				false,
				1);
		ASPECT_LUST = 
				new TC_Aspect(
						"Vengeance",
						15647411,
						new TC_Aspect[]{
								get(TC_Aspects.CORPUS),
								get(TC_Aspects.ORDO)
								},
				new ResourceLocation("gregtech:textures/aspects/" + TC_Aspects.NEBRISUM.name() + ".png"),
				false,
				1);
		
		
		
		
		/*GTPP_Aspects.CUSTOM_2.mAspect = new Aspect("custom2", 15658622, new Aspect[]{Aspect.MAGIC, Aspect.SLIME},
				new ResourceLocation("gregtech:textures/aspects/" + TC_Aspects.RADIO.name() + ".png"), 1);
		GTPP_Aspects.CUSTOM_3.mAspect = new Aspect("custom3", 12644078, new Aspect[]{Aspect.ENERGY, Aspect.ARMOR},
				new ResourceLocation("gregtech:textures/aspects/" + TC_Aspects.RADIO.name() + ".png"), 1);
		GTPP_Aspects.CUSTOM_4.mAspect = new Aspect("custom4", 12632256, new Aspect[]{Aspect.METAL, Aspect.POISON},
				new ResourceLocation("gregtech:textures/aspects/" + TC_Aspects.RADIO.name() + ".png"), 1);
		GTPP_Aspects.CUSTOM_5.mAspect = new Aspect("custom4", 12648384, new Aspect[]{Aspect.LIGHT, Aspect.SOUL},
				new ResourceLocation("gregtech:textures/aspects/" + TC_Aspects.RADIO.name() + ".png"), 1);*/
		GT_LanguageManager.addStringLocalization("tc.aspect.Purity", "Balance");
		GT_LanguageManager.addStringLocalization("tc.aspect.Vengeance", "Lust");
		GT_LanguageManager.addStringLocalization("tc.aspect.custom3", "Starbound");
		GT_LanguageManager.addStringLocalization("tc.aspect.custom4", "Toxic");
		GT_LanguageManager.addStringLocalization("tc.aspect.custom5", "Heaven");
	}
	
	private TC_Aspect get(TC_Aspects aGtObjects) {
		try {
			return TC_Aspect.generate(aGtObjects.mAspect);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
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
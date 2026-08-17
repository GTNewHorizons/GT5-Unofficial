package gregtech.common;

import static gregtech.GTLoggers.GT_FML_LOGGER;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.TCAspects;
import gregtech.api.interfaces.internal.IThaumcraftCompat;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTUtility;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.crafting.InfusionEnchantmentRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchCategoryList;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;

public class GTThaumcraftCompat implements IThaumcraftCompat {

    private static final boolean PROFILE_ASPECT_REGISTRATION = Boolean.getBoolean("gt.debug.thaumcraft_aspects");
    private static long profileEmptyCalls;
    private static long profileAdditiveCalls;
    private static long profileNonAdditiveCalls;
    private static long profilePreRegistered;
    private static long profilePreMissing;
    private static long profileMissingGeneratedPositive;
    private static long profileExplicitRegistrations;
    private static long profileExistsNanos;
    private static long profileLookupNanos;
    private static long profileHitLookupNanos;
    private static long profileMissLookupNanos;

    public GTThaumcraftCompat() {
        TCAspects.AER.mAspect = Aspect.AIR;
        TCAspects.ALIENIS.mAspect = Aspect.ELDRITCH;
        TCAspects.AQUA.mAspect = Aspect.WATER;
        TCAspects.ARBOR.mAspect = Aspect.TREE;
        TCAspects.AURAM.mAspect = Aspect.AURA;
        TCAspects.BESTIA.mAspect = Aspect.BEAST;
        TCAspects.COGNITIO.mAspect = Aspect.MIND;
        TCAspects.CORPUS.mAspect = Aspect.FLESH;
        TCAspects.EXANIMIS.mAspect = Aspect.UNDEAD;
        TCAspects.FABRICO.mAspect = Aspect.CRAFT;
        TCAspects.FAMES.mAspect = Aspect.HUNGER;
        TCAspects.GELUM.mAspect = Aspect.COLD;
        TCAspects.GRANUM.mAspect = Aspect.PLANT;
        TCAspects.HERBA.mAspect = Aspect.PLANT;
        TCAspects.HUMANUS.mAspect = Aspect.MAN;
        TCAspects.IGNIS.mAspect = Aspect.FIRE;
        TCAspects.INSTRUMENTUM.mAspect = Aspect.TOOL;
        TCAspects.ITER.mAspect = Aspect.TRAVEL;
        TCAspects.LIMUS.mAspect = Aspect.SLIME;
        TCAspects.LUCRUM.mAspect = Aspect.GREED;
        TCAspects.LUX.mAspect = Aspect.LIGHT;
        TCAspects.MACHINA.mAspect = Aspect.MECHANISM;
        TCAspects.MESSIS.mAspect = Aspect.CROP;
        TCAspects.METALLUM.mAspect = Aspect.METAL;
        TCAspects.METO.mAspect = Aspect.HARVEST;
        TCAspects.MORTUUS.mAspect = Aspect.DEATH;
        TCAspects.MOTUS.mAspect = Aspect.MOTION;
        TCAspects.ORDO.mAspect = Aspect.ORDER;
        TCAspects.PANNUS.mAspect = Aspect.CLOTH;
        TCAspects.PERDITIO.mAspect = Aspect.ENTROPY;
        TCAspects.PERFODIO.mAspect = Aspect.MINE;
        TCAspects.PERMUTATIO.mAspect = Aspect.EXCHANGE;
        TCAspects.POTENTIA.mAspect = Aspect.ENERGY;
        TCAspects.PRAECANTATIO.mAspect = Aspect.MAGIC;
        TCAspects.SANO.mAspect = Aspect.HEAL;
        TCAspects.SENSUS.mAspect = Aspect.SENSES;
        TCAspects.SPIRITUS.mAspect = Aspect.SOUL;
        TCAspects.TELUM.mAspect = Aspect.WEAPON;
        TCAspects.TERRA.mAspect = Aspect.EARTH;
        TCAspects.TEMPESTAS.mAspect = Aspect.WEATHER;
        TCAspects.TENEBRAE.mAspect = Aspect.DARKNESS;
        TCAspects.TUTAMEN.mAspect = Aspect.ARMOR;
        TCAspects.VACUOS.mAspect = Aspect.VOID;
        TCAspects.VENENUM.mAspect = Aspect.POISON;
        TCAspects.VICTUS.mAspect = Aspect.LIFE;
        TCAspects.VINCULUM.mAspect = Aspect.TRAP;
        TCAspects.VITIUM.mAspect = Aspect.TAINT;
        TCAspects.VITREUS.mAspect = Aspect.CRYSTAL;
        TCAspects.VOLATUS.mAspect = Aspect.FLIGHT;

        TCAspects.STRONTIO.mAspect = new Aspect(
            "strontio",
            0xEEC2B3,
            new Aspect[] { Aspect.MIND, Aspect.ENTROPY },
            new ResourceLocation("gregtech:textures/aspects/" + TCAspects.STRONTIO.name() + ".png"),
            1);
        TCAspects.NEBRISUM.mAspect = new Aspect(
            "nebrisum",
            0xEEEE7E,
            new Aspect[] { Aspect.MINE, Aspect.GREED },
            new ResourceLocation("gregtech:textures/aspects/" + TCAspects.NEBRISUM.name() + ".png"),
            1);
        TCAspects.ELECTRUM.mAspect = new Aspect(
            "electrum",
            0xC0EEEE,
            new Aspect[] { Aspect.ENERGY, Aspect.MECHANISM },
            new ResourceLocation("gregtech:textures/aspects/" + TCAspects.ELECTRUM.name() + ".png"),
            1);
        TCAspects.MAGNETO.mAspect = new Aspect(
            "magneto",
            0xC0C0C0,
            new Aspect[] { Aspect.METAL, Aspect.TRAVEL },
            new ResourceLocation("gregtech:textures/aspects/" + TCAspects.MAGNETO.name() + ".png"),
            1);
        TCAspects.RADIO.mAspect = new Aspect(
            "radio",
            0xC0FFC0,
            new Aspect[] { Aspect.LIGHT, Aspect.ENERGY },
            new ResourceLocation("gregtech:textures/aspects/" + TCAspects.RADIO.name() + ".png"),
            1);
        TCAspects.AEQUALITAS.mAspect = new LocalizedCustomAspectName(
            "custom1",
            0xEEF0EA,
            new Aspect[] { Aspect.MIND, Aspect.ORDER },
            new ResourceLocation("gregtech:textures/aspects/" + TCAspects.AEQUALITAS.name() + ".png"),
            1);
        TCAspects.VESANIA.mAspect = new LocalizedCustomAspectName(
            "custom2",
            0x1B122C,
            new Aspect[] { Aspect.MIND, Aspect.TAINT },
            new ResourceLocation("gregtech:textures/aspects/" + TCAspects.VESANIA.name() + ".png"),
            1);
        TCAspects.PRIMORDIUM.mAspect = new LocalizedCustomAspectName(
            "custom3",
            0xF7F7DB,
            new Aspect[] { Aspect.VOID, Aspect.MOTION },
            new ResourceLocation("gregtech:textures/aspects/" + TCAspects.PRIMORDIUM.name() + ".png"),
            1);
        TCAspects.ASTRUM.mAspect = new LocalizedCustomAspectName(
            "custom4",
            0x2D2C2B,
            new Aspect[] { Aspect.LIGHT, Aspect.getAspect("custom3") },
            new ResourceLocation("gregtech:textures/aspects/" + TCAspects.ASTRUM.name() + ".png"),
            1);
        TCAspects.GLORIA.mAspect = new LocalizedCustomAspectName(
            "custom5",
            0xFFE980,
            new Aspect[] { Aspect.MAN, Aspect.TRAVEL },
            new ResourceLocation("gregtech:textures/aspects/" + TCAspects.GLORIA.name() + ".png"),
            1);

        if (PROFILE_ASPECT_REGISTRATION) GregTechAPI.sGTCompleteLoad.add(GTThaumcraftCompat::logAspectProfile);
    }

    public static class LocalizedCustomAspectName extends Aspect {

        public LocalizedCustomAspectName(String tag, int color, Aspect[] components, ResourceLocation image,
            int blend) {
            super(tag, color, components, image, blend);
        }

        @Override
        public String getName() {
            return StatCollector.translateToLocal("tc.aspect." + this.getTag() + ".name");
        }
    }

    private static AspectList getAspectList(List<TCAspects.TC_AspectStack> aAspects) {
        AspectList rAspects = new AspectList();
        TCAspects.TC_AspectStack tAspect;
        for (Iterator<TCAspects.TC_AspectStack> i$ = aAspects.iterator(); i$.hasNext(); rAspects
            .add((Aspect) tAspect.mAspect.mAspect, (int) tAspect.mAmount)) {
            tAspect = i$.next();
        }
        return rAspects;
    }

    private static void logAspectProfile() {
        GT_FML_LOGGER.info(
            "GT Thaumcraft aspect profile: emptyCalls={}, additiveCalls={}, nonAdditiveCalls={}, preRegistered={}, preMissing={}, missingGeneratedPositive={}, explicitRegistrations={}, existsMs={}, lookupMs={} (hitsMs={}, missesMs={}), objectTags={}",
            profileEmptyCalls,
            profileAdditiveCalls,
            profileNonAdditiveCalls,
            profilePreRegistered,
            profilePreMissing,
            profileMissingGeneratedPositive,
            profileExplicitRegistrations,
            profileExistsNanos / 1_000_000.0,
            profileLookupNanos / 1_000_000.0,
            profileHitLookupNanos / 1_000_000.0,
            profileMissLookupNanos / 1_000_000.0,
            ThaumcraftApi.objectTags.size());
    }

    @Override
    public Object addResearch(String aResearch, String aName, String aText, String[] aParentResearches,
        String aCategory, ItemStack aIcon, int aComplexity, int aType, int aX, int aY,
        List<TCAspects.TC_AspectStack> aAspects, ItemStack[] aResearchTriggers, Object[] aPages) {
        ResearchCategoryList tCategory = ResearchCategories.getResearchList(aCategory);
        if (tCategory == null) {
            return null;
        }
        for (ResearchItem tResearch : tCategory.research.values()) {
            if ((tResearch.displayColumn == aX) && (tResearch.displayRow == aY)) {
                aX += (aX > 0 ? 5 : -5);
                aY += (aY > 0 ? 5 : -5);
            }
        }
        ResearchItem rResearch = new ResearchItem(
            aResearch,
            aCategory,
            getAspectList(aAspects),
            aX,
            aY,
            aComplexity,
            aIcon);
        ArrayList<ResearchPage> tPages = new ArrayList<>(aPages.length);
        GTLanguageManager.addStringLocalization("tc.research_name." + aResearch, aName);
        GTLanguageManager.addStringLocalization("tc.research_text." + aResearch, "[GT] " + aText);
        for (Object tPage : aPages) {
            if ((tPage instanceof String)) {
                tPages.add(new ResearchPage((String) tPage));
            } else if ((tPage instanceof IRecipe)) {
                tPages.add(new ResearchPage((IRecipe) tPage));
            } else if ((tPage instanceof IArcaneRecipe)) {
                tPages.add(new ResearchPage((IArcaneRecipe) tPage));
            } else if ((tPage instanceof CrucibleRecipe)) {
                tPages.add(new ResearchPage((CrucibleRecipe) tPage));
            } else if ((tPage instanceof InfusionRecipe)) {
                tPages.add(new ResearchPage((InfusionRecipe) tPage));
            } else if ((tPage instanceof InfusionEnchantmentRecipe)) {
                tPages.add(new ResearchPage((InfusionEnchantmentRecipe) tPage));
            }
        }
        if ((aType & 0x40) != 0) {
            rResearch.setAutoUnlock();
        }
        if ((aType & 0x1) != 0) {
            rResearch.setSecondary();
        }
        if ((aType & 0x20) != 0) {
            rResearch.setSpecial();
        }
        if ((aType & 0x8) != 0) {
            rResearch.setVirtual();
        }
        if ((aType & 0x4) != 0) {
            rResearch.setHidden();
        }
        if ((aType & 0x10) != 0) {
            rResearch.setRound();
        }
        if ((aType & 0x2) != 0) {
            rResearch.setStub();
        }
        if (aParentResearches != null) {
            ArrayList<String> tParentResearches = new ArrayList<>();
            Collections.addAll(tParentResearches, aParentResearches);
            if (!tParentResearches.isEmpty()) {
                rResearch.setParents(tParentResearches.toArray(new String[0]));
                rResearch.setConcealed();
            }
        }
        if (aResearchTriggers != null) {
            rResearch.setItemTriggers(aResearchTriggers);
            rResearch.setHidden();
        }
        rResearch.setPages(tPages.toArray(new ResearchPage[0]));
        return rResearch.registerResearchItem();
    }

    @Override
    public Object addCrucibleRecipe(String aResearch, Object aInput, ItemStack aOutput,
        List<TCAspects.TC_AspectStack> aAspects) {
        if ((GTUtility.isStringInvalid(aResearch)) || (aInput == null)
            || (aOutput == null)
            || (aAspects == null)
            || (aAspects.isEmpty())) {
            return null;
        }
        return ThaumcraftApi.addCrucibleRecipe(
            aResearch,
            GTUtility.copyOrNull(aOutput),
            ((aInput instanceof ItemStack)) || ((aInput instanceof ArrayList)) ? aInput : aInput.toString(),
            getAspectList(aAspects));
    }

    @Override
    public Object addInfusionRecipe(String aResearch, ItemStack aMainInput, ItemStack[] aSideInputs, ItemStack aOutput,
        int aInstability, List<TCAspects.TC_AspectStack> aAspects) {
        if ((GTUtility.isStringInvalid(aResearch)) || (aMainInput == null)
            || (aSideInputs == null)
            || (aOutput == null)
            || (aAspects == null)
            || (aAspects.isEmpty())) {
            return null;
        }
        return ThaumcraftApi.addInfusionCraftingRecipe(
            aResearch,
            GTUtility.copyOrNull(aOutput),
            aInstability,
            getAspectList(aAspects),
            aMainInput,
            aSideInputs);
    }

    @Override
    public Object addInfusionEnchantmentRecipe(String aResearch, Enchantment aEnchantment, int aInstability,
        List<TCAspects.TC_AspectStack> aAspects, ItemStack[] aSideInputs) {
        if ((GTUtility.isStringInvalid(aResearch)) || (aSideInputs == null)
            || (aAspects == null)
            || (aEnchantment == null)
            || (aAspects.isEmpty())) {
            return null;
        }
        return ThaumcraftApi
            .addInfusionEnchantmentRecipe(aResearch, aEnchantment, aInstability, getAspectList(aAspects), aSideInputs);
    }

    @Override
    public boolean registerThaumcraftAspectsToItem(ItemStack aExampleStack, List<TCAspects.TC_AspectStack> aAspects,
        String aOreDict) {
        if (aAspects.isEmpty()) return false;
        ThaumcraftApi.registerObjectTag(aOreDict, getAspectList(aAspects));
        return true;
    }

    @Override
    public boolean registerThaumcraftAspectsToItem(ItemStack aStack, List<TCAspects.TC_AspectStack> aAspects,
        boolean aAdditive) {
        if (aAspects.isEmpty()) {
            if (PROFILE_ASPECT_REGISTRATION) profileEmptyCalls++;
            return false;
        }
        if (aAdditive) {
            if (PROFILE_ASPECT_REGISTRATION) profileAdditiveCalls++;
            ThaumcraftApi.registerComplexObjectTag(aStack, getAspectList(aAspects));
            return true;
        }
        if (!PROFILE_ASPECT_REGISTRATION) {
            AspectList tAlreadyRegisteredAspects = ThaumcraftApiHelper.getObjectAspects(aStack);
            if (tAlreadyRegisteredAspects == null || tAlreadyRegisteredAspects.size() <= 0) {
                ThaumcraftApi.registerObjectTag(aStack, getAspectList(aAspects));
            }
            return true;
        }

        profileNonAdditiveCalls++;
        long started = System.nanoTime();
        boolean wasRegistered = ThaumcraftApi.exists(aStack.getItem(), aStack.getItemDamage());
        profileExistsNanos += System.nanoTime() - started;

        started = System.nanoTime();
        AspectList tAlreadyRegisteredAspects = ThaumcraftApiHelper.getObjectAspects(aStack);
        long lookupNanos = System.nanoTime() - started;
        profileLookupNanos += lookupNanos;

        boolean hasPositiveAspects = tAlreadyRegisteredAspects != null && tAlreadyRegisteredAspects.size() > 0;
        if (wasRegistered) {
            profilePreRegistered++;
            profileHitLookupNanos += lookupNanos;
        } else {
            profilePreMissing++;
            profileMissLookupNanos += lookupNanos;
            if (hasPositiveAspects) profileMissingGeneratedPositive++;
        }

        if (!hasPositiveAspects) {
            profileExplicitRegistrations++;
            ThaumcraftApi.registerObjectTag(aStack, getAspectList(aAspects));
        }
        return true;
    }

    @Override
    public boolean registerPortholeBlacklistedBlock(Block aBlock) {
        ThaumcraftApi.portableHoleBlackList.add(aBlock);
        return true;
    }

}

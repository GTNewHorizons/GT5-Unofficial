package gregtech.api.interfaces.internal;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.TC_Aspects;
import gregtech.api.enums.TC_Aspects.TC_AspectStack;

public interface IThaumcraftCompat {

    int RESEARCH_TYPE_NORMAL = 0, RESEARCH_TYPE_SECONDARY = 1, RESEARCH_TYPE_FREE = 2, RESEARCH_TYPE_HIDDEN = 4,
        RESEARCH_TYPE_VIRTUAL = 8, RESEARCH_TYPE_ROUND = 16, RESEARCH_TYPE_SPECIAL = 32, RESEARCH_TYPE_AUTOUNLOCK = 64;

    /**
     * The Research Keys of GT
     */
    String IRON_TO_STEEL = "GT_IRON_TO_STEEL", FILL_WATER_BUCKET = "GT_FILL_WATER_BUCKET",
        WOOD_TO_CHARCOAL = "GT_WOOD_TO_CHARCOAL", TRANSZINC = "GT_TRANSZINC", TRANSNICKEL = "GT_TRANSNICKEL",
        TRANSCOBALT = "GT_TRANSCOBALT", TRANSBISMUTH = "GT_TRANSBISMUTH", TRANSANTIMONY = "GT_TRANSANTIMONY",
        TRANSCUPRONICKEL = "GT_TRANSCUPRONICKEL", TRANSBATTERYALLOY = "GT_TRANSBATTERYALLOY",
        TRANSSOLDERINGALLOY = "GT_TRANSSOLDERINGALLOY", TRANSBRASS = "GT_TRANSBRASS", TRANSBRONZE = "GT_TRANSBRONZE",
        TRANSINVAR = "GT_TRANSINVAR", TRANSELECTRUM = "GT_TRANSELECTRUM", TRANSALUMINIUM = "GT_TRANSALUMINIUM",
        CRYSTALLISATION = "GT_CRYSTALLISATION", ADVANCEDENTROPICPROCESSING = "GT_ADVANCEDENTROPICPROCESSING", // unused
        ADVANCEDMETALLURGY = "GT_ADVANCEDMETALLURGY";

    boolean registerPortholeBlacklistedBlock(Block aBlock);

    boolean registerThaumcraftAspectsToItem(ItemStack aStack, List<TC_AspectStack> aAspects, boolean aAdditive);

    boolean registerThaumcraftAspectsToItem(ItemStack aStack, List<TC_AspectStack> aAspects, String aOreDict);

    Object addCrucibleRecipe(String aResearch, Object aInput, ItemStack aOutput, List<TC_AspectStack> aAspects);

    Object addInfusionRecipe(String aResearch, ItemStack aMainInput, ItemStack[] aSideInputs, ItemStack aOutput,
        int aInstability, List<TC_Aspects.TC_AspectStack> aAspects);

    Object addInfusionEnchantmentRecipe(String aResearch, Enchantment aEnchantment, int aInstability,
        List<TC_Aspects.TC_AspectStack> aAspects, ItemStack[] aSideInputs);

    Object addResearch(String aResearch, String aName, String aText, String[] aParentResearches, String aCategory,
        ItemStack aIcon, int aComplexity, int aType, int aX, int aY, List<TC_AspectStack> aAspects,
        ItemStack[] aResearchTriggers, Object[] aPages);
}

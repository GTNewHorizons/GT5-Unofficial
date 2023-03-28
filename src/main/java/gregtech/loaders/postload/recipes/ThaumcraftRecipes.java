package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.GT_Values.MOD_ID_TC;
import static gregtech.api.enums.ModIDs.Thaumcraft;
import static gregtech.api.util.GT_ModHandler.getModItem;

import java.util.Arrays;
import java.util.Collections;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import gregtech.api.GregTech_API;
import gregtech.api.enchants.Enchantment_Hazmat;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TC_Aspects;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.loaders.postload.GT_MachineRecipeLoader;

public class ThaumcraftRecipes implements Runnable {

    @Override
    public void run() {
        if (Thaumcraft.isModLoaded()) {
            // Add Recipe for TC Crucible: Salis Mundus to Balanced Shards
            String tKey = "GT_BALANCE_SHARD_RECIPE";
            GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                    "TB.SM",
                    getModItem(MOD_ID_TC, "ItemResource", 1L, 14),
                    getModItem(MOD_ID_TC, "ItemShard", 1L, 6),
                    Arrays.asList(
                            new TC_Aspects.TC_AspectStack(TC_Aspects.PRAECANTATIO, 2L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 1L)));

            if (GregTech_API.sThaumcraftCompat != null) {

                tKey = "GT_WOOD_TO_CHARCOAL";
                GT_LanguageManager.addStringLocalization(
                        GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        "You have discovered a way of making charcoal magically instead of using regular ovens for this purpose.<BR><BR>To create charcoal from wood you first need an air-free environment, some vacuus essentia is needed for that, then you need to incinerate the wood using ignis essentia and wait until all the water inside the wood is burned away.<BR><BR>This method however doesn't create creosote oil as byproduct.");

                GregTech_API.sThaumcraftCompat.addResearch(
                        tKey,
                        "Charcoal Transmutation",
                        "Turning wood into charcoal",
                        new String[] { "ALUMENTUM" },
                        "ALCHEMY",
                        GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 1L),
                        2,
                        0,
                        13,
                        5,
                        Arrays.asList(
                                new TC_Aspects.TC_AspectStack(TC_Aspects.ARBOR, 10L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 8L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 8L)),
                        null,
                        new Object[] { GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                                GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                        tKey,
                                        OrePrefixes.log.get(Materials.Wood),
                                        GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 1L),
                                        Arrays.asList(
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 2L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 1L))) });

                tKey = "GT_FILL_WATER_BUCKET";
                GT_LanguageManager.addStringLocalization(
                        GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        "You have discovered a way of filling a bucket with aqua essentia in order to simply get water.");
                GregTech_API.sThaumcraftCompat.addResearch(
                        tKey,
                        "Water Transmutation",
                        "Filling buckets with water",
                        null,
                        "ALCHEMY",
                        GT_OreDictUnificator.get(OrePrefixes.bucket, Materials.Water, 1L),
                        2,
                        0,
                        16,
                        5,
                        Arrays.asList(
                                new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 4L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 4L)),
                        null,
                        new Object[] { GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                                GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                        tKey,
                                        GT_OreDictUnificator.get(OrePrefixes.bucket, Materials.Empty, 1L),
                                        GT_OreDictUnificator.get(OrePrefixes.bucket, Materials.Water, 1L),
                                        Collections.singletonList(new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 4L))),
                                GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                        tKey,
                                        GT_OreDictUnificator.get(OrePrefixes.bucketClay, Materials.Empty, 1L),
                                        GT_OreDictUnificator.get(OrePrefixes.bucketClay, Materials.Water, 1L),
                                        Collections.singletonList(new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 4L))),
                                GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                        tKey,
                                        GT_OreDictUnificator.get(OrePrefixes.capsule, Materials.Empty, 1L),
                                        GT_OreDictUnificator.get(OrePrefixes.capsule, Materials.Water, 1L),
                                        Collections.singletonList(new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 4L))),
                                GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                        tKey,
                                        GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1L),
                                        GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1L),
                                        Collections
                                                .singletonList(new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 4L))) });

                tKey = "GT_TRANSZINC";
                GT_LanguageManager.addStringLocalization(
                        GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        "You have discovered a way to multiply zinc by steeping zinc nuggets in metallum harvested from other metals.");
                GregTech_API.sThaumcraftCompat.addResearch(
                        tKey,
                        "Zinc Transmutation",
                        "Transformation of metals into zinc",
                        new String[] { "TRANSTIN" },
                        "ALCHEMY",
                        GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Zinc, 1L),
                        2,
                        1,
                        9,
                        13,
                        Arrays.asList(
                                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.SANO, 3L)),
                        null,
                        new Object[] { GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                                GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                        tKey,
                                        OrePrefixes.nugget.get(Materials.Zinc),
                                        GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Zinc, 3L),
                                        Arrays.asList(
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.SANO, 1L))) });

                tKey = "GT_TRANSANTIMONY";
                GT_LanguageManager.addStringLocalization(
                        GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        "You have discovered a way to multiply antimony by steeping antimony nuggets in metallum harvested from other metals.");
                GregTech_API.sThaumcraftCompat.addResearch(
                        tKey,
                        "Antimony Transmutation",
                        "Transformation of metals into antimony",
                        new String[] { "GT_TRANSZINC", "TRANSLEAD" },
                        "ALCHEMY",
                        GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Antimony, 1L),
                        2,
                        1,
                        9,
                        14,
                        Arrays.asList(
                                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 3L)),
                        null,
                        new Object[] { GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                                GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                        tKey,
                                        OrePrefixes.nugget.get(Materials.Antimony),
                                        GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Antimony, 3L),
                                        Arrays.asList(
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 1L))) });

                tKey = "GT_TRANSNICKEL";
                GT_LanguageManager.addStringLocalization(
                        GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        "You have discovered a way to multiply nickel by steeping nickel nuggets in metallum harvested from other metals.");
                GregTech_API.sThaumcraftCompat.addResearch(
                        tKey,
                        "Nickel Transmutation",
                        "Transformation of metals into nickel",
                        new String[] { "TRANSLEAD" },
                        "ALCHEMY",
                        GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Nickel, 1L),
                        2,
                        1,
                        9,
                        15,
                        Arrays.asList(
                                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 3L)),
                        null,
                        new Object[] { GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                                GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                        tKey,
                                        OrePrefixes.nugget.get(Materials.Nickel),
                                        GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Nickel, 3L),
                                        Arrays.asList(
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 1L))) });

                tKey = "GT_TRANSCOBALT";
                GT_LanguageManager.addStringLocalization(
                        GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        "You have discovered a way to multiply cobalt by steeping cobalt nuggets in metallum harvested from other metals.");
                GregTech_API.sThaumcraftCompat.addResearch(
                        tKey,
                        "Cobalt Transmutation",
                        "Transformation of metals into cobalt",
                        new String[] { "GT_TRANSNICKEL" },
                        "ALCHEMY",
                        GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Cobalt, 1L),
                        2,
                        1,
                        9,
                        16,
                        Arrays.asList(
                                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 3L)),
                        null,
                        new Object[] { GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                                GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                        tKey,
                                        OrePrefixes.nugget.get(Materials.Cobalt),
                                        GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Cobalt, 3L),
                                        Arrays.asList(
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 1L))) });

                tKey = "GT_TRANSBISMUTH";
                GT_LanguageManager.addStringLocalization(
                        GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        "You have discovered a way to multiply bismuth by steeping bismuth nuggets in metallum harvested from other metals.");
                GregTech_API.sThaumcraftCompat.addResearch(
                        tKey,
                        "Bismuth Transmutation",
                        "Transformation of metals into bismuth",
                        new String[] { "GT_TRANSCOBALT" },
                        "ALCHEMY",
                        GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Bismuth, 1L),
                        2,
                        1,
                        11,
                        17,
                        Arrays.asList(
                                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 3L)),
                        null,
                        new Object[] { GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                                GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                        tKey,
                                        OrePrefixes.nugget.get(Materials.Bismuth),
                                        GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Bismuth, 3L),
                                        Arrays.asList(
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 1L))) });

                tKey = "GT_IRON_TO_STEEL";
                GT_LanguageManager.addStringLocalization(
                        GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        "You have discovered a way of making Iron harder by just re-ordering its components.<BR><BR>This Method can be used to create a Material called Steel, which is used in many non-Thaumaturgic applications.");
                GregTech_API.sThaumcraftCompat.addResearch(
                        tKey,
                        "Steel Transmutation",
                        "Transforming iron to steel",
                        new String[] { "TRANSIRON", "GT_WOOD_TO_CHARCOAL" },
                        "ALCHEMY",
                        GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Steel, 1L),
                        3,
                        0,
                        13,
                        8,
                        Arrays.asList(
                                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 3L)),
                        null,
                        new Object[] { GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                                GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                        tKey,
                                        OrePrefixes.nugget.get(Materials.Iron),
                                        GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Steel, 1L),
                                        Collections
                                                .singletonList(new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 1L))) });

                tKey = "GT_TRANSBRONZE";
                GT_LanguageManager.addStringLocalization(
                        GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        "You have discovered a way of creating Alloys using the already known transmutations of Copper and Tin.<BR><BR>This Method can be used to create a Bronze directly without having to go through an alloying process.");
                GregTech_API.sThaumcraftCompat.addResearch(
                        tKey,
                        "Bronze Transmutation",
                        "Transformation of metals into bronze",
                        new String[] { "TRANSTIN", "TRANSCOPPER" },
                        "ALCHEMY",
                        GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Bronze, 1L),
                        2,
                        0,
                        13,
                        11,
                        Arrays.asList(
                                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 3L)),
                        null,
                        new Object[] { GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                                GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                        tKey,
                                        OrePrefixes.nugget.get(Materials.Bronze),
                                        GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Bronze, 3L),
                                        Arrays.asList(
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 1L))) });

                tKey = "GT_TRANSELECTRUM";
                GT_LanguageManager.addStringLocalization(
                        GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        "Your discovery of Bronze Transmutation has lead you to the conclusion it works with other Alloys such as Electrum as well.");
                GregTech_API.sThaumcraftCompat.addResearch(
                        tKey,
                        "Electrum Transmutation",
                        "Transformation of metals into electrum",
                        new String[] { "GT_TRANSBRONZE", "TRANSGOLD", "TRANSSILVER" },
                        "ALCHEMY",
                        GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Electrum, 1L),
                        2,
                        1,
                        11,
                        11,
                        Arrays.asList(
                                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.LUCRUM, 3L)),
                        null,
                        new Object[] { GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                                GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                        tKey,
                                        OrePrefixes.nugget.get(Materials.Electrum),
                                        GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Electrum, 3L),
                                        Arrays.asList(
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.LUCRUM, 1L))) });

                tKey = "GT_TRANSBRASS";
                GT_LanguageManager.addStringLocalization(
                        GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        "Your discovery of Bronze Transmutation has lead you to the conclusion it works with other Alloys such as Brass as well.");
                GregTech_API.sThaumcraftCompat.addResearch(
                        tKey,
                        "Brass Transmutation",
                        "Transformation of metals into brass",
                        new String[] { "GT_TRANSBRONZE", "GT_TRANSZINC" },
                        "ALCHEMY",
                        GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Brass, 1L),
                        2,
                        1,
                        11,
                        12,
                        Arrays.asList(
                                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 3L)),
                        null,
                        new Object[] { GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                                GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                        tKey,
                                        OrePrefixes.nugget.get(Materials.Brass),
                                        GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Brass, 3L),
                                        Arrays.asList(
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 1L))) });

                tKey = "GT_TRANSINVAR";
                GT_LanguageManager.addStringLocalization(
                        GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        "Your discovery of Bronze Transmutation has lead you to the conclusion it works with other Alloys such as Invar as well.");
                GregTech_API.sThaumcraftCompat.addResearch(
                        tKey,
                        "Invar Transmutation",
                        "Transformation of metals into invar",
                        new String[] { "GT_TRANSBRONZE", "GT_TRANSNICKEL" },
                        "ALCHEMY",
                        GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Invar, 1L),
                        2,
                        1,
                        11,
                        15,
                        Arrays.asList(
                                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.GELUM, 3L)),
                        null,
                        new Object[] { GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                                GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                        tKey,
                                        OrePrefixes.nugget.get(Materials.Invar),
                                        GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Invar, 3L),
                                        Arrays.asList(
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.GELUM, 1L))) });

                tKey = "GT_TRANSCUPRONICKEL";
                GT_LanguageManager.addStringLocalization(
                        GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        "Your discovery of Bronze Transmutation has lead you to the conclusion it works with other Alloys such as Cupronickel as well.");
                GregTech_API.sThaumcraftCompat.addResearch(
                        tKey,
                        "Cupronickel Transmutation",
                        "Transformation of metals into cupronickel",
                        new String[] { "GT_TRANSBRONZE", "GT_TRANSNICKEL" },
                        "ALCHEMY",
                        GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Cupronickel, 1L),
                        2,
                        1,
                        11,
                        16,
                        Arrays.asList(
                                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 3L)),
                        null,
                        new Object[] { GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                                GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                        tKey,
                                        OrePrefixes.nugget.get(Materials.Cupronickel),
                                        GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Cupronickel, 3L),
                                        Arrays.asList(
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 1L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 1L))) });

                tKey = "GT_TRANSBATTERYALLOY";
                GT_LanguageManager.addStringLocalization(
                        GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        "Your discovery of Bronze Transmutation has lead you to the conclusion it works with other Alloys such as Battery Alloy as well.");
                GregTech_API.sThaumcraftCompat.addResearch(
                        tKey,
                        "Battery Alloy Transmutation",
                        "Transformation of metals into battery alloy",
                        new String[] { "GT_TRANSBRONZE", "GT_TRANSANTIMONY" },
                        "ALCHEMY",
                        GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.BatteryAlloy, 1L),
                        2,
                        1,
                        11,
                        13,
                        Arrays.asList(
                                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 3L)),
                        null,
                        new Object[] { GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                                GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                        tKey,
                                        OrePrefixes.nugget.get(Materials.BatteryAlloy),
                                        GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.BatteryAlloy, 3L),
                                        Arrays.asList(
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 1L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 1L))) });

                tKey = "GT_TRANSSOLDERINGALLOY";
                GT_LanguageManager.addStringLocalization(
                        GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        "Your discovery of Bronze Transmutation has lead you to the conclusion it works with other Alloys such as Soldering Alloy as well.");
                GregTech_API.sThaumcraftCompat.addResearch(
                        tKey,
                        "Soldering Alloy Transmutation",
                        "Transformation of metals into soldering alloy",
                        new String[] { "GT_TRANSBRONZE", "GT_TRANSANTIMONY" },
                        "ALCHEMY",
                        GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.SolderingAlloy, 1L),
                        2,
                        1,
                        11,
                        14,
                        Arrays.asList(
                                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 3L)),
                        null,
                        new Object[] { GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                                GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                        tKey,
                                        OrePrefixes.nugget.get(Materials.SolderingAlloy),
                                        GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.SolderingAlloy, 3L),
                                        Arrays.asList(
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 1L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 1L))) });

                tKey = "GT_ADVANCEDMETALLURGY";
                GT_LanguageManager.addStringLocalization(
                        GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        "Now that you have discovered all the basic metals, you can finally move on to the next Level of magic metallurgy and create more advanced metals");
                GregTech_API.sThaumcraftCompat.addResearch(
                        tKey,
                        "Advanced Metallurgic Transmutation",
                        "Mastering the basic metals",
                        new String[] { "GT_TRANSBISMUTH", "GT_IRON_TO_STEEL", "GT_TRANSSOLDERINGALLOY",
                                "GT_TRANSBATTERYALLOY", "GT_TRANSBRASS", "GT_TRANSELECTRUM", "GT_TRANSCUPRONICKEL",
                                "GT_TRANSINVAR" },
                        "ALCHEMY",
                        GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Iron, 1L),
                        3,
                        0,
                        16,
                        14,
                        Arrays.asList(
                                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 50L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 20L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 20L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.PRAECANTATIO, 20L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.NEBRISUM, 20L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.MAGNETO, 20L)),
                        null,
                        new Object[] { GT_MachineRecipeLoader.aTextTCGTPage + tKey });

                tKey = "GT_TRANSALUMINIUM";
                GT_LanguageManager.addStringLocalization(
                        GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        "You have discovered a way to multiply aluminium by steeping aluminium nuggets in metallum harvested from other metals.<BR><BR>This transmutation is slightly harder to achieve, because aluminium has special properties, which require more order to achieve the desired result.");
                GregTech_API.sThaumcraftCompat.addResearch(
                        tKey,
                        "Aluminium Transmutation",
                        "Transformation of metals into aluminium",
                        new String[] { "GT_ADVANCEDMETALLURGY" },
                        "ALCHEMY",
                        GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Aluminium, 1L),
                        4,
                        0,
                        19,
                        14,
                        Arrays.asList(
                                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.VOLATUS, 3L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 3L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 3L)),
                        null,
                        new Object[] { GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                                GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                        tKey,
                                        OrePrefixes.nugget.get(Materials.Aluminium),
                                        GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Aluminium, 3L),
                                        Arrays.asList(
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.VOLATUS, 1L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 1L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 1L))) });

                tKey = "GT_TRANSSKYSTONE";
                GT_LanguageManager.addStringLocalization(
                        GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        "You have discovered a way to convert obsidian to skystone.<BR><BR>Not sure why you'd want to do this, unless skystone is somehow unavailable in your world.");
                GregTech_API.sThaumcraftCompat.addResearch(
                        tKey,
                        "Skystone Transmutation",
                        "Transformation of obsidian into skystone",
                        new String[] { "GT_ADVANCEDMETALLURGY" },
                        "ALCHEMY",
                        getModItem("appliedenergistics2", "tile.BlockSkyStone", 1),
                        4,
                        0,
                        19,
                        15,
                        Arrays.asList(
                                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.VOLATUS, 3L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.ALIENIS, 3L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.TERRA, 3L)),
                        null,
                        new Object[] { GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                                GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                        tKey,
                                        new ItemStack(Blocks.obsidian),
                                        getModItem("appliedenergistics2", "tile.BlockSkyStone", 1),
                                        Arrays.asList(
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 2L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.VOLATUS, 1L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.TERRA, 1L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.ALIENIS, 2L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.TENEBRAE, 1L))) });

                tKey = "GT_TRANSMINERAL";
                GT_LanguageManager.addStringLocalization(
                        GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        "You have discovered a way to convert basaltic mineral sand to granitic mineral sand and vice versa.<BR><BR>Handy for people living in the sky who can't access it normally, or if you really want one or the other.");
                GregTech_API.sThaumcraftCompat.addResearch(
                        tKey,
                        "Basaltic Mineral Transmutation",
                        "Transformation of mineral sands",
                        new String[] { "GT_ADVANCEDMETALLURGY" },
                        "ALCHEMY",
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.GraniticMineralSand, 1L),
                        4,
                        0,
                        19,
                        16,
                        Arrays.asList(
                                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.VOLATUS, 3L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.MAGNETO, 3L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.TERRA, 3L)),
                        null,
                        new Object[] { GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                                GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                        tKey,
                                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.BasalticMineralSand, 1L),
                                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.GraniticMineralSand, 1L),
                                        Arrays.asList(
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 1L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 1L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.MAGNETO, 1L))),
                                GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                        tKey,
                                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.GraniticMineralSand, 1L),
                                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.BasalticMineralSand, 1L),
                                        Arrays.asList(
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 1L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 1L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.MAGNETO, 1L))) });

                tKey = "GT_CRYSTALLISATION";
                GT_LanguageManager.addStringLocalization(
                        GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        "Sometimes when processing your Crystal Shards they become a pile of Dust instead of the mostly required Shard.<BR><BR>You have finally found a way to reverse this Process by using Vitreus Essentia for recrystallising the Shards.");
                GregTech_API.sThaumcraftCompat.addResearch(
                        tKey,
                        "Shard Recrystallisation",
                        "Fixing your precious crystals",
                        new String[] { "ALCHEMICALMANUFACTURE" },
                        "ALCHEMY",
                        GT_OreDictUnificator.get(OrePrefixes.gem, Materials.InfusedOrder, 1L),
                        3,
                        0,
                        -11,
                        -3,
                        Arrays.asList(
                                new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 5L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 3L)),
                        null,
                        new Object[] { GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                                GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                        tKey,
                                        OrePrefixes.dust.get(Materials.Amber),
                                        GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Amber, 1L),
                                        Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 4L))),
                                GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                        tKey,
                                        OrePrefixes.dust.get(Materials.InfusedOrder),
                                        GT_OreDictUnificator.get(OrePrefixes.gem, Materials.InfusedOrder, 1L),
                                        Collections
                                                .singletonList(new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 4L))),
                                GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                        tKey,
                                        OrePrefixes.dust.get(Materials.InfusedEntropy),
                                        GT_OreDictUnificator.get(OrePrefixes.gem, Materials.InfusedEntropy, 1L),
                                        Collections
                                                .singletonList(new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 4L))),
                                GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                        tKey,
                                        OrePrefixes.dust.get(Materials.InfusedAir),
                                        GT_OreDictUnificator.get(OrePrefixes.gem, Materials.InfusedAir, 1L),
                                        Collections
                                                .singletonList(new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 4L))),
                                GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                        tKey,
                                        OrePrefixes.dust.get(Materials.InfusedEarth),
                                        GT_OreDictUnificator.get(OrePrefixes.gem, Materials.InfusedEarth, 1L),
                                        Collections
                                                .singletonList(new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 4L))),
                                GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                        tKey,
                                        OrePrefixes.dust.get(Materials.InfusedFire),
                                        GT_OreDictUnificator.get(OrePrefixes.gem, Materials.InfusedFire, 1L),
                                        Collections
                                                .singletonList(new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 4L))),
                                GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                        tKey,
                                        OrePrefixes.dust.get(Materials.InfusedWater),
                                        GT_OreDictUnificator.get(OrePrefixes.gem, Materials.InfusedWater, 1L),
                                        Collections.singletonList(
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 4L))) });

                tKey = "GT_MAGICENERGY";
                GT_LanguageManager.addStringLocalization(
                        GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        "While trying to find new ways to integrate magic into your industrial factories, you have discovered a way to convert magical energy into electrical power.");
                GregTech_API.sThaumcraftCompat.addResearch(
                        tKey,
                        "Magic Energy Conversion",
                        "Magic to Power",
                        new String[] { "ARCANEBORE" },
                        "ARTIFICE",
                        ItemList.MagicEnergyConverter_LV.get(1L),
                        3,
                        0,
                        -3,
                        10,
                        Arrays.asList(
                                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 10L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 10L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 20L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 10L)),
                        null,
                        new Object[] { GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                                GregTech_API.sThaumcraftCompat.addInfusionRecipe(
                                        tKey,
                                        ItemList.Hull_LV.get(1L),
                                        new ItemStack[] { new ItemStack(Blocks.beacon),
                                                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Good, 1L),
                                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Aluminium, 1L),
                                                ItemList.Sensor_MV.get(2L),
                                                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Good, 1L),
                                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Thaumium, 1L),
                                                ItemList.Sensor_MV.get(2L) },
                                        ItemList.MagicEnergyConverter_LV.get(1L),
                                        5,
                                        Arrays.asList(
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 32L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 16L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 32L))) });

                tKey = "GT_MAGICENERGY2";
                GT_LanguageManager.addStringLocalization(
                        GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        "Attempts to increase the output of your Magic Energy generators have resulted in significant improvements.");
                GregTech_API.sThaumcraftCompat.addResearch(
                        tKey,
                        "Adept Magic Energy Conversion",
                        "Magic to Power",
                        new String[] { "GT_MAGICENERGY" },
                        "ARTIFICE",
                        ItemList.MagicEnergyConverter_MV.get(1L),
                        1,
                        1,
                        -4,
                        12,
                        Arrays.asList(
                                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 10L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 10L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 20L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 10L)),
                        null,
                        new Object[] { GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                                GregTech_API.sThaumcraftCompat.addInfusionRecipe(
                                        tKey,
                                        ItemList.Hull_MV.get(1L),
                                        new ItemStack[] { new ItemStack(Blocks.beacon),
                                                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Advanced, 1L),
                                                GT_OreDictUnificator
                                                        .get(OrePrefixes.plateDouble, Materials.Thaumium, 1L),
                                                ItemList.Sensor_HV.get(2L),
                                                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Advanced, 1L),
                                                GT_OreDictUnificator
                                                        .get(OrePrefixes.plateDouble, Materials.StainlessSteel, 1L),
                                                ItemList.Sensor_HV.get(2L) },
                                        ItemList.MagicEnergyConverter_MV.get(1L),
                                        6,
                                        Arrays.asList(
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 64L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 32L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 64L))) });

                tKey = "GT_MAGICENERGY3";
                GT_LanguageManager.addStringLocalization(
                        GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        "Attempts to further increase the output of your Magic Energy generators have resulted in great improvements.");
                GregTech_API.sThaumcraftCompat.addResearch(
                        tKey,
                        "Master Magic Energy Conversion",
                        "Magic to Power",
                        new String[] { "GT_MAGICENERGY2" },
                        "ARTIFICE",
                        ItemList.MagicEnergyConverter_HV.get(1L),
                        1,
                        1,
                        -4,
                        14,
                        Arrays.asList(
                                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 20L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 20L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 40L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 20L)),
                        null,
                        new Object[] { GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                                GregTech_API.sThaumcraftCompat.addInfusionRecipe(
                                        tKey,
                                        ItemList.Hull_HV.get(1L),
                                        new ItemStack[] { new ItemStack(Blocks.beacon),
                                                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Data, 1L),
                                                GT_OreDictUnificator
                                                        .get(OrePrefixes.plateDense, Materials.Thaumium, 1L),
                                                ItemList.Field_Generator_MV.get(1L),
                                                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Data, 1L),
                                                GT_OreDictUnificator
                                                        .get(OrePrefixes.plateDense, Materials.Titanium, 1L),
                                                ItemList.Field_Generator_MV.get(1L) },
                                        ItemList.MagicEnergyConverter_HV.get(1L),
                                        8,
                                        Arrays.asList(
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 128L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 64L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 128L))) });

                tKey = "GT_MAGICABSORB";
                GT_LanguageManager.addStringLocalization(
                        GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        "Research into magical energy conversion methods has identified a way to convert surrounding energies into electrical power.");
                GregTech_API.sThaumcraftCompat.addResearch(
                        tKey,
                        "Magic Energy Absorption",
                        "Harvesting Magic",
                        new String[] { "GT_MAGICENERGY" },
                        "ARTIFICE",
                        ItemList.MagicEnergyAbsorber_LV.get(1L),
                        3,
                        0,
                        -2,
                        12,
                        Arrays.asList(
                                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 10L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 10L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 20L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 10L)),
                        null,
                        new Object[] { GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                                GregTech_API.sThaumcraftCompat.addInfusionRecipe(
                                        tKey,
                                        ItemList.Hull_LV.get(1L),
                                        new ItemStack[] { ItemList.MagicEnergyConverter_LV.get(1L),
                                                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Good, 1L),
                                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Thaumium, 1L),
                                                ItemList.Sensor_MV.get(2L) },
                                        ItemList.MagicEnergyAbsorber_LV.get(1L),
                                        6,
                                        Arrays.asList(
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 32L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 16L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 32L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 16L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 32L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.STRONTIO, 4L))) });

                tKey = "GT_MAGICABSORB2";
                GT_LanguageManager.addStringLocalization(
                        GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        "Moar output! Drain all the Magic!");
                GregTech_API.sThaumcraftCompat.addResearch(
                        tKey,
                        "Improved Magic Energy Absorption",
                        "Harvesting Magic",
                        new String[] { "GT_MAGICABSORB" },
                        "ARTIFICE",
                        ItemList.MagicEnergyAbsorber_EV.get(1L),
                        3,
                        1,
                        -2,
                        14,
                        Arrays.asList(
                                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 10L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 10L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 20L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 10L)),
                        null,
                        new Object[] { GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                                GregTech_API.sThaumcraftCompat.addInfusionRecipe(
                                        tKey,
                                        ItemList.Hull_MV.get(1L),
                                        new ItemStack[] { ItemList.MagicEnergyConverter_MV.get(1L),
                                                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Advanced, 1L),
                                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Thaumium, 1L),
                                                ItemList.Sensor_HV.get(2L),
                                                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Advanced, 1L),
                                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Thaumium, 1L) },
                                        ItemList.MagicEnergyAbsorber_MV.get(1L),
                                        6,
                                        Arrays.asList(
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 64L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 32L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 64L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 32L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 64L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.STRONTIO, 8L))),
                                GregTech_API.sThaumcraftCompat.addInfusionRecipe(
                                        tKey,
                                        ItemList.Hull_HV.get(1L),
                                        new ItemStack[] { ItemList.MagicEnergyConverter_MV.get(1L),
                                                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Data, 1L),
                                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Void, 1),
                                                ItemList.Field_Generator_MV.get(1L),
                                                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Data, 1L),
                                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Void, 1), },
                                        ItemList.MagicEnergyAbsorber_HV.get(1L),
                                        8,
                                        Arrays.asList(
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 128L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 64L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 128L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 64L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 128L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.STRONTIO, 16L))),
                                GregTech_API.sThaumcraftCompat.addInfusionRecipe(
                                        tKey,
                                        ItemList.Hull_EV.get(1L),
                                        new ItemStack[] { ItemList.MagicEnergyConverter_HV.get(1L),
                                                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Elite, 1L),
                                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Void, 1),
                                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 1),
                                                ItemList.Field_Generator_HV.get(1L),
                                                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Elite, 1L),
                                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Void, 1),
                                                GT_OreDictUnificator
                                                        .get(OrePrefixes.plate, Materials.TungstenSteel, 1), },
                                        ItemList.MagicEnergyAbsorber_EV.get(1L),
                                        10,
                                        Arrays.asList(
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 256L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 128L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 256L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 128L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 256L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.STRONTIO, 64L))) });

                tKey = "GT_HAZMATENCH";
                GT_LanguageManager.addStringLocalization(
                        GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        "You have discovered a way to magically enchant a mundane piece of armor with the protective properties of a Hazmat suite.");
                GregTech_API.sThaumcraftCompat.addResearch(
                        tKey,
                        "Hazmat Protection",
                        "Magical protection from physical hazards",
                        new String[] { "INFUSIONENCHANTMENT" },
                        "ARTIFICE",
                        GT_ModHandler.getIC2Item("hazmatChestplate", 1),
                        4,
                        0,
                        -7,
                        13,
                        Arrays.asList(
                                new TC_Aspects.TC_AspectStack(TC_Aspects.VITIUM, 1L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.GELUM, 1L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.RADIO, 1L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.TUTAMEN, 1L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.VENENUM, 1L)),
                        null,
                        new Object[] { GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                                GregTech_API.sThaumcraftCompat.addInfusionEnchantmentRecipe(
                                        tKey,
                                        Enchantment_Hazmat.INSTANCE,
                                        5,
                                        Arrays.asList(
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.VITIUM, 8L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.GELUM, 16L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.RADIO, 16L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.TUTAMEN, 32L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.VENENUM, 16L)),
                                        new ItemStack[] { getModItem(MOD_ID_TC, "ItemResource", 1L, 14),
                                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 1),
                                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Rubber, 1),
                                                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Lead, 1),
                                                getModItem(MOD_ID_TC, "ItemResource", 1L, 14),
                                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 1),
                                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Rubber, 1),
                                                GT_OreDictUnificator
                                                        .get(OrePrefixes.plateDense, Materials.Lead, 1) }) });
            }
        }
    }
}

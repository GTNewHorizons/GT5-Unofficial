package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.AppliedEnergistics2;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.util.GTModHandler.getModItem;

import java.util.Arrays;
import java.util.Collections;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import gregtech.api.GregTechAPI;
import gregtech.api.enchants.EnchantmentHazmat;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TCAspects;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.loaders.postload.MachineRecipeLoader;

public class ThaumcraftRecipes implements Runnable {

    @Override
    public void run() {
        if ((!Thaumcraft.isModLoaded()) || GregTechAPI.sThaumcraftCompat == null) {
            return;
        }

        // Add Recipe for TC Crucible: Salis Mundus to Balanced Shards
        String tKey = "GT_BALANCE_SHARD_RECIPE";
        GregTechAPI.sThaumcraftCompat.addCrucibleRecipe(
            "TB.SM",
            getModItem(Thaumcraft.ID, "ItemResource", 1L, 14),
            getModItem(Thaumcraft.ID, "ItemShard", 1L, 6),
            Arrays.asList(
                new TCAspects.TC_AspectStack(TCAspects.PRAECANTATIO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 1L)));

        tKey = "GT_WOOD_TO_CHARCOAL";
        GTLanguageManager.addStringLocalization(
            MachineRecipeLoader.aTextTCGTPage + tKey,
            "You have discovered a way of making charcoal magically instead of using regular ovens for this purpose.<BR><BR>To create charcoal from wood you first need an air-free environment, some vacuus essentia is needed for that, then you need to incinerate the wood using ignis essentia and wait until all the water inside the wood is burned away.<BR><BR>This method however doesn't create creosote oil as byproduct.");

        GregTechAPI.sThaumcraftCompat.addResearch(
            tKey,
            "Charcoal Transmutation",
            "Turning wood into charcoal",
            new String[] { "ALUMENTUM" },
            "ALCHEMY",
            GTOreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 1L),
            2,
            0,
            13,
            5,
            Arrays.asList(
                new TCAspects.TC_AspectStack(TCAspects.ARBOR, 10L),
                new TCAspects.TC_AspectStack(TCAspects.VACUOS, 8L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 8L)),
            null,
            new Object[] { MachineRecipeLoader.aTextTCGTPage + tKey,
                GregTechAPI.sThaumcraftCompat.addCrucibleRecipe(
                    tKey,
                    OrePrefixes.log.get(Materials.Wood),
                    GTOreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 1L),
                    Arrays.asList(
                        new TCAspects.TC_AspectStack(TCAspects.VACUOS, 2L),
                        new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L))) });

        tKey = "GT_FILL_WATER_BUCKET";
        GTLanguageManager.addStringLocalization(
            MachineRecipeLoader.aTextTCGTPage + tKey,
            "You have discovered a way of filling a bucket with aqua essentia in order to simply get water.");
        GregTechAPI.sThaumcraftCompat.addResearch(
            tKey,
            "Water Transmutation",
            "Filling buckets with water",
            null,
            "ALCHEMY",
            GTOreDictUnificator.get(OrePrefixes.bucket, Materials.Water, 1L),
            2,
            0,
            16,
            5,
            Arrays.asList(
                new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 4L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 4L)),
            null,
            new Object[] { MachineRecipeLoader.aTextTCGTPage + tKey,
                GregTechAPI.sThaumcraftCompat.addCrucibleRecipe(
                    tKey,
                    GTOreDictUnificator.get(OrePrefixes.bucket, Materials.Empty, 1L),
                    GTOreDictUnificator.get(OrePrefixes.bucket, Materials.Water, 1L),
                    Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.AQUA, 4L))),
                GregTechAPI.sThaumcraftCompat.addCrucibleRecipe(
                    tKey,
                    GTOreDictUnificator.get(OrePrefixes.bucketClay, Materials.Empty, 1L),
                    GTOreDictUnificator.get(OrePrefixes.bucketClay, Materials.Water, 1L),
                    Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.AQUA, 4L))),
                GregTechAPI.sThaumcraftCompat.addCrucibleRecipe(
                    tKey,
                    GTOreDictUnificator.get(OrePrefixes.capsule, Materials.Empty, 1L),
                    GTOreDictUnificator.get(OrePrefixes.capsule, Materials.Water, 1L),
                    Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.AQUA, 4L))),
                GregTechAPI.sThaumcraftCompat.addCrucibleRecipe(
                    tKey,
                    GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1L),
                    GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1L),
                    Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.AQUA, 4L))) });

        tKey = "GT_TRANSZINC";
        GTLanguageManager.addStringLocalization(
            MachineRecipeLoader.aTextTCGTPage + tKey,
            "You have discovered a way to multiply zinc by steeping zinc nuggets in metallum harvested from other metals.");
        GregTechAPI.sThaumcraftCompat.addResearch(
            tKey,
            "Zinc Transmutation",
            "Transformation of metals into zinc",
            new String[] { "TRANSTIN" },
            "ALCHEMY",
            GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Zinc, 1L),
            2,
            1,
            9,
            13,
            Arrays.asList(
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 5L),
                new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 3L),
                new TCAspects.TC_AspectStack(TCAspects.SANO, 3L)),
            null,
            new Object[] { MachineRecipeLoader.aTextTCGTPage + tKey,
                GregTechAPI.sThaumcraftCompat.addCrucibleRecipe(
                    tKey,
                    OrePrefixes.nugget.get(Materials.Zinc),
                    GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Zinc, 3L),
                    Arrays.asList(
                        new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2L),
                        new TCAspects.TC_AspectStack(TCAspects.SANO, 1L))) });

        tKey = "GT_TRANSANTIMONY";
        GTLanguageManager.addStringLocalization(
            MachineRecipeLoader.aTextTCGTPage + tKey,
            "You have discovered a way to multiply antimony by steeping antimony nuggets in metallum harvested from other metals.");
        GregTechAPI.sThaumcraftCompat.addResearch(
            tKey,
            "Antimony Transmutation",
            "Transformation of metals into antimony",
            new String[] { "GT_TRANSZINC", "TRANSLEAD" },
            "ALCHEMY",
            GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Antimony, 1L),
            2,
            1,
            9,
            14,
            Arrays.asList(
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 5L),
                new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 3L),
                new TCAspects.TC_AspectStack(TCAspects.AQUA, 3L)),
            null,
            new Object[] { MachineRecipeLoader.aTextTCGTPage + tKey,
                GregTechAPI.sThaumcraftCompat.addCrucibleRecipe(
                    tKey,
                    OrePrefixes.nugget.get(Materials.Antimony),
                    GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Antimony, 3L),
                    Arrays.asList(
                        new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2L),
                        new TCAspects.TC_AspectStack(TCAspects.AQUA, 1L))) });

        tKey = "GT_TRANSNICKEL";
        GTLanguageManager.addStringLocalization(
            MachineRecipeLoader.aTextTCGTPage + tKey,
            "You have discovered a way to multiply nickel by steeping nickel nuggets in metallum harvested from other metals.");
        GregTechAPI.sThaumcraftCompat.addResearch(
            tKey,
            "Nickel Transmutation",
            "Transformation of metals into nickel",
            new String[] { "TRANSLEAD" },
            "ALCHEMY",
            GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Nickel, 1L),
            2,
            1,
            9,
            15,
            Arrays.asList(
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 5L),
                new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 3L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 3L)),
            null,
            new Object[] { MachineRecipeLoader.aTextTCGTPage + tKey,
                GregTechAPI.sThaumcraftCompat.addCrucibleRecipe(
                    tKey,
                    OrePrefixes.nugget.get(Materials.Nickel),
                    GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Nickel, 3L),
                    Arrays.asList(
                        new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2L),
                        new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L))) });

        tKey = "GT_TRANSCOBALT";
        GTLanguageManager.addStringLocalization(
            MachineRecipeLoader.aTextTCGTPage + tKey,
            "You have discovered a way to multiply cobalt by steeping cobalt nuggets in metallum harvested from other metals.");
        GregTechAPI.sThaumcraftCompat.addResearch(
            tKey,
            "Cobalt Transmutation",
            "Transformation of metals into cobalt",
            new String[] { "GT_TRANSNICKEL" },
            "ALCHEMY",
            GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Cobalt, 1L),
            2,
            1,
            9,
            16,
            Arrays.asList(
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 5L),
                new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 3L),
                new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 3L)),
            null,
            new Object[] { MachineRecipeLoader.aTextTCGTPage + tKey,
                GregTechAPI.sThaumcraftCompat.addCrucibleRecipe(
                    tKey,
                    OrePrefixes.nugget.get(Materials.Cobalt),
                    GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Cobalt, 3L),
                    Arrays.asList(
                        new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2L),
                        new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 1L))) });

        tKey = "GT_TRANSBISMUTH";
        GTLanguageManager.addStringLocalization(
            MachineRecipeLoader.aTextTCGTPage + tKey,
            "You have discovered a way to multiply bismuth by steeping bismuth nuggets in metallum harvested from other metals.");
        GregTechAPI.sThaumcraftCompat.addResearch(
            tKey,
            "Bismuth Transmutation",
            "Transformation of metals into bismuth",
            new String[] { "GT_TRANSCOBALT" },
            "ALCHEMY",
            GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Bismuth, 1L),
            2,
            1,
            11,
            17,
            Arrays.asList(
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 5L),
                new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 3L),
                new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 3L)),
            null,
            new Object[] { MachineRecipeLoader.aTextTCGTPage + tKey,
                GregTechAPI.sThaumcraftCompat.addCrucibleRecipe(
                    tKey,
                    OrePrefixes.nugget.get(Materials.Bismuth),
                    GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Bismuth, 3L),
                    Arrays.asList(
                        new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2L),
                        new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 1L))) });

        tKey = "GT_IRON_TO_STEEL";
        GTLanguageManager.addStringLocalization(
            MachineRecipeLoader.aTextTCGTPage + tKey,
            "You have discovered a way of making Iron harder by just re-ordering its components.<BR><BR>This Method can be used to create a Material called Steel, which is used in many non-Thaumaturgic applications.");
        GregTechAPI.sThaumcraftCompat.addResearch(
            tKey,
            "Steel Transmutation",
            "Transforming iron to steel",
            new String[] { "TRANSIRON", "GT_WOOD_TO_CHARCOAL" },
            "ALCHEMY",
            GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Steel, 1L),
            3,
            0,
            13,
            8,
            Arrays.asList(
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 5L),
                new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 3L),
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 3L)),
            null,
            new Object[] { MachineRecipeLoader.aTextTCGTPage + tKey,
                GregTechAPI.sThaumcraftCompat.addCrucibleRecipe(
                    tKey,
                    OrePrefixes.nugget.get(Materials.Iron),
                    GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Steel, 1L),
                    Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.ORDO, 1L))) });

        tKey = "GT_TRANSBRONZE";
        GTLanguageManager.addStringLocalization(
            MachineRecipeLoader.aTextTCGTPage + tKey,
            "You have discovered a way of creating Alloys using the already known transmutations of Copper and Tin.<BR><BR>This Method can be used to create a Bronze directly without having to go through an alloying process.");
        GregTechAPI.sThaumcraftCompat.addResearch(
            tKey,
            "Bronze Transmutation",
            "Transformation of metals into bronze",
            new String[] { "TRANSTIN", "TRANSCOPPER" },
            "ALCHEMY",
            GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Bronze, 1L),
            2,
            0,
            13,
            11,
            Arrays.asList(
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 5L),
                new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 3L),
                new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 3L)),
            null,
            new Object[] { MachineRecipeLoader.aTextTCGTPage + tKey,
                GregTechAPI.sThaumcraftCompat.addCrucibleRecipe(
                    tKey,
                    OrePrefixes.nugget.get(Materials.Bronze),
                    GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Bronze, 3L),
                    Arrays.asList(
                        new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2L),
                        new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 1L))) });

        tKey = "GT_TRANSELECTRUM";
        GTLanguageManager.addStringLocalization(
            MachineRecipeLoader.aTextTCGTPage + tKey,
            "Your discovery of Bronze Transmutation has lead you to the conclusion it works with other Alloys such as Electrum as well.");
        GregTechAPI.sThaumcraftCompat.addResearch(
            tKey,
            "Electrum Transmutation",
            "Transformation of metals into electrum",
            new String[] { "GT_TRANSBRONZE", "TRANSGOLD", "TRANSSILVER" },
            "ALCHEMY",
            GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Electrum, 1L),
            2,
            1,
            11,
            11,
            Arrays.asList(
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 5L),
                new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 3L),
                new TCAspects.TC_AspectStack(TCAspects.LUCRUM, 3L)),
            null,
            new Object[] { MachineRecipeLoader.aTextTCGTPage + tKey,
                GregTechAPI.sThaumcraftCompat.addCrucibleRecipe(
                    tKey,
                    OrePrefixes.nugget.get(Materials.Electrum),
                    GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Electrum, 3L),
                    Arrays.asList(
                        new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2L),
                        new TCAspects.TC_AspectStack(TCAspects.LUCRUM, 1L))) });

        tKey = "GT_TRANSBRASS";
        GTLanguageManager.addStringLocalization(
            MachineRecipeLoader.aTextTCGTPage + tKey,
            "Your discovery of Bronze Transmutation has lead you to the conclusion it works with other Alloys such as Brass as well.");
        GregTechAPI.sThaumcraftCompat.addResearch(
            tKey,
            "Brass Transmutation",
            "Transformation of metals into brass",
            new String[] { "GT_TRANSBRONZE", "GT_TRANSZINC" },
            "ALCHEMY",
            GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Brass, 1L),
            2,
            1,
            11,
            12,
            Arrays.asList(
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 5L),
                new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 3L),
                new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 3L)),
            null,
            new Object[] { MachineRecipeLoader.aTextTCGTPage + tKey,
                GregTechAPI.sThaumcraftCompat.addCrucibleRecipe(
                    tKey,
                    OrePrefixes.nugget.get(Materials.Brass),
                    GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Brass, 3L),
                    Arrays.asList(
                        new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2L),
                        new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 1L))) });

        tKey = "GT_TRANSINVAR";
        GTLanguageManager.addStringLocalization(
            MachineRecipeLoader.aTextTCGTPage + tKey,
            "Your discovery of Bronze Transmutation has lead you to the conclusion it works with other Alloys such as Invar as well.");
        GregTechAPI.sThaumcraftCompat.addResearch(
            tKey,
            "Invar Transmutation",
            "Transformation of metals into invar",
            new String[] { "GT_TRANSBRONZE", "GT_TRANSNICKEL" },
            "ALCHEMY",
            GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Invar, 1L),
            2,
            1,
            11,
            15,
            Arrays.asList(
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 5L),
                new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 3L),
                new TCAspects.TC_AspectStack(TCAspects.GELUM, 3L)),
            null,
            new Object[] { MachineRecipeLoader.aTextTCGTPage + tKey,
                GregTechAPI.sThaumcraftCompat.addCrucibleRecipe(
                    tKey,
                    OrePrefixes.nugget.get(Materials.Invar),
                    GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Invar, 3L),
                    Arrays.asList(
                        new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2L),
                        new TCAspects.TC_AspectStack(TCAspects.GELUM, 1L))) });

        tKey = "GT_TRANSCUPRONICKEL";
        GTLanguageManager.addStringLocalization(
            MachineRecipeLoader.aTextTCGTPage + tKey,
            "Your discovery of Bronze Transmutation has lead you to the conclusion it works with other Alloys such as Cupronickel as well.");
        GregTechAPI.sThaumcraftCompat.addResearch(
            tKey,
            "Cupronickel Transmutation",
            "Transformation of metals into cupronickel",
            new String[] { "GT_TRANSBRONZE", "GT_TRANSNICKEL" },
            "ALCHEMY",
            GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Cupronickel, 1L),
            2,
            1,
            11,
            16,
            Arrays.asList(
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 5L),
                new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 3L),
                new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 3L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 3L)),
            null,
            new Object[] { MachineRecipeLoader.aTextTCGTPage + tKey,
                GregTechAPI.sThaumcraftCompat.addCrucibleRecipe(
                    tKey,
                    OrePrefixes.nugget.get(Materials.Cupronickel),
                    GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Cupronickel, 3L),
                    Arrays.asList(
                        new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2L),
                        new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 1L),
                        new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L))) });

        tKey = "GT_TRANSBATTERYALLOY";
        GTLanguageManager.addStringLocalization(
            MachineRecipeLoader.aTextTCGTPage + tKey,
            "Your discovery of Bronze Transmutation has lead you to the conclusion it works with other Alloys such as Battery Alloy as well.");
        GregTechAPI.sThaumcraftCompat.addResearch(
            tKey,
            "Battery Alloy Transmutation",
            "Transformation of metals into battery alloy",
            new String[] { "GT_TRANSBRONZE", "GT_TRANSANTIMONY" },
            "ALCHEMY",
            GTOreDictUnificator.get(OrePrefixes.nugget, Materials.BatteryAlloy, 1L),
            2,
            1,
            11,
            13,
            Arrays.asList(
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 5L),
                new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 3L),
                new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 3L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 3L)),
            null,
            new Object[] { MachineRecipeLoader.aTextTCGTPage + tKey,
                GregTechAPI.sThaumcraftCompat.addCrucibleRecipe(
                    tKey,
                    OrePrefixes.nugget.get(Materials.BatteryAlloy),
                    GTOreDictUnificator.get(OrePrefixes.nugget, Materials.BatteryAlloy, 3L),
                    Arrays.asList(
                        new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2L),
                        new TCAspects.TC_AspectStack(TCAspects.AQUA, 1L),
                        new TCAspects.TC_AspectStack(TCAspects.ORDO, 1L))) });

        tKey = "GT_TRANSSOLDERINGALLOY";
        GTLanguageManager.addStringLocalization(
            MachineRecipeLoader.aTextTCGTPage + tKey,
            "Your discovery of Bronze Transmutation has lead you to the conclusion it works with other Alloys such as Soldering Alloy as well.");
        GregTechAPI.sThaumcraftCompat.addResearch(
            tKey,
            "Soldering Alloy Transmutation",
            "Transformation of metals into soldering alloy",
            new String[] { "GT_TRANSBRONZE", "GT_TRANSANTIMONY" },
            "ALCHEMY",
            GTOreDictUnificator.get(OrePrefixes.nugget, Materials.SolderingAlloy, 1L),
            2,
            1,
            11,
            14,
            Arrays.asList(
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 5L),
                new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 3L),
                new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 3L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 3L)),
            null,
            new Object[] { MachineRecipeLoader.aTextTCGTPage + tKey,
                GregTechAPI.sThaumcraftCompat.addCrucibleRecipe(
                    tKey,
                    OrePrefixes.nugget.get(Materials.SolderingAlloy),
                    GTOreDictUnificator.get(OrePrefixes.nugget, Materials.SolderingAlloy, 3L),
                    Arrays.asList(
                        new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2L),
                        new TCAspects.TC_AspectStack(TCAspects.AQUA, 1L),
                        new TCAspects.TC_AspectStack(TCAspects.VITREUS, 1L))) });

        tKey = "GT_ADVANCEDMETALLURGY";
        GTLanguageManager.addStringLocalization(
            MachineRecipeLoader.aTextTCGTPage + tKey,
            "Now that you have discovered all the basic metals, you can finally move on to the next Level of magic metallurgy and create more advanced metals");
        GregTechAPI.sThaumcraftCompat.addResearch(
            tKey,
            "Advanced Metallurgic Transmutation",
            "Mastering the basic metals",
            new String[] { "GT_TRANSBISMUTH", "GT_IRON_TO_STEEL", "GT_TRANSSOLDERINGALLOY", "GT_TRANSBATTERYALLOY",
                "GT_TRANSBRASS", "GT_TRANSELECTRUM", "GT_TRANSCUPRONICKEL", "GT_TRANSINVAR" },
            "ALCHEMY",
            GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Iron, 1L),
            3,
            0,
            16,
            14,
            Arrays.asList(
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 50L),
                new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 20L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 20L),
                new TCAspects.TC_AspectStack(TCAspects.PRAECANTATIO, 20L),
                new TCAspects.TC_AspectStack(TCAspects.NEBRISUM, 20L),
                new TCAspects.TC_AspectStack(TCAspects.MAGNETO, 20L)),
            null,
            new Object[] { MachineRecipeLoader.aTextTCGTPage + tKey });

        tKey = "GT_TRANSALUMINIUM";
        GTLanguageManager.addStringLocalization(
            MachineRecipeLoader.aTextTCGTPage + tKey,
            "You have discovered a way to multiply aluminium by steeping aluminium nuggets in metallum harvested from other metals.<BR><BR>This transmutation is slightly harder to achieve, because aluminium has special properties, which require more order to achieve the desired result.");
        GregTechAPI.sThaumcraftCompat.addResearch(
            tKey,
            "Aluminium Transmutation",
            "Transformation of metals into aluminium",
            new String[] { "GT_ADVANCEDMETALLURGY" },
            "ALCHEMY",
            GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Aluminium, 1L),
            4,
            0,
            19,
            14,
            Arrays.asList(
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 5L),
                new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 3L),
                new TCAspects.TC_AspectStack(TCAspects.VOLATUS, 3L),
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 3L),
                new TCAspects.TC_AspectStack(TCAspects.IGNIS, 3L)),
            null,
            new Object[] { MachineRecipeLoader.aTextTCGTPage + tKey,
                GregTechAPI.sThaumcraftCompat.addCrucibleRecipe(
                    tKey,
                    OrePrefixes.nugget.get(Materials.Aluminium),
                    GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Aluminium, 3L),
                    Arrays.asList(
                        new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2L),
                        new TCAspects.TC_AspectStack(TCAspects.VOLATUS, 1L),
                        new TCAspects.TC_AspectStack(TCAspects.ORDO, 1L),
                        new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1L))) });

        tKey = "GT_TRANSSKYSTONE";
        GTLanguageManager.addStringLocalization(
            MachineRecipeLoader.aTextTCGTPage + tKey,
            "You have discovered a way to convert obsidian to skystone.<BR><BR>Not sure why you'd want to do this, unless skystone is somehow unavailable in your world.");
        GregTechAPI.sThaumcraftCompat.addResearch(
            tKey,
            "Skystone Transmutation",
            "Transformation of obsidian into skystone",
            new String[] { "GT_ADVANCEDMETALLURGY" },
            "ALCHEMY",
            getModItem(AppliedEnergistics2.ID, "tile.BlockSkyStone", 1),
            4,
            0,
            19,
            15,
            Arrays.asList(
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 5L),
                new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 3L),
                new TCAspects.TC_AspectStack(TCAspects.VOLATUS, 3L),
                new TCAspects.TC_AspectStack(TCAspects.ALIENIS, 3L),
                new TCAspects.TC_AspectStack(TCAspects.TERRA, 3L)),
            null,
            new Object[] { MachineRecipeLoader.aTextTCGTPage + tKey,
                GregTechAPI.sThaumcraftCompat.addCrucibleRecipe(
                    tKey,
                    new ItemStack(Blocks.obsidian),
                    getModItem(AppliedEnergistics2.ID, "tile.BlockSkyStone", 1),
                    Arrays.asList(
                        new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 2L),
                        new TCAspects.TC_AspectStack(TCAspects.VOLATUS, 1L),
                        new TCAspects.TC_AspectStack(TCAspects.TERRA, 1L),
                        new TCAspects.TC_AspectStack(TCAspects.ALIENIS, 2L),
                        new TCAspects.TC_AspectStack(TCAspects.TENEBRAE, 1L))) });

        tKey = "GT_TRANSMINERAL";
        GTLanguageManager.addStringLocalization(
            MachineRecipeLoader.aTextTCGTPage + tKey,
            "You have discovered a way to convert basaltic mineral sand to granitic mineral sand and vice versa.<BR><BR>Handy for people living in the sky who can't access it normally, or if you really want one or the other.");
        GregTechAPI.sThaumcraftCompat.addResearch(
            tKey,
            "Basaltic Mineral Transmutation",
            "Transformation of mineral sands",
            new String[] { "GT_ADVANCEDMETALLURGY" },
            "ALCHEMY",
            GTOreDictUnificator.get(OrePrefixes.dust, Materials.GraniticMineralSand, 1L),
            4,
            0,
            19,
            16,
            Arrays.asList(
                new TCAspects.TC_AspectStack(TCAspects.METALLUM, 5L),
                new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 3L),
                new TCAspects.TC_AspectStack(TCAspects.VOLATUS, 3L),
                new TCAspects.TC_AspectStack(TCAspects.MAGNETO, 3L),
                new TCAspects.TC_AspectStack(TCAspects.TERRA, 3L)),
            null,
            new Object[] { MachineRecipeLoader.aTextTCGTPage + tKey,
                GregTechAPI.sThaumcraftCompat.addCrucibleRecipe(
                    tKey,
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.BasalticMineralSand, 1L),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.GraniticMineralSand, 1L),
                    Arrays.asList(
                        new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2L),
                        new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 1L),
                        new TCAspects.TC_AspectStack(TCAspects.ORDO, 1L),
                        new TCAspects.TC_AspectStack(TCAspects.MAGNETO, 1L))),
                GregTechAPI.sThaumcraftCompat.addCrucibleRecipe(
                    tKey,
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.GraniticMineralSand, 1L),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.BasalticMineralSand, 1L),
                    Arrays.asList(
                        new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2L),
                        new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 1L),
                        new TCAspects.TC_AspectStack(TCAspects.ORDO, 1L),
                        new TCAspects.TC_AspectStack(TCAspects.MAGNETO, 1L))) });

        tKey = "GT_CRYSTALLISATION";
        GTLanguageManager.addStringLocalization(
            MachineRecipeLoader.aTextTCGTPage + tKey,
            "Sometimes when processing your Crystal Shards they become a pile of Dust instead of the mostly required Shard.<BR><BR>You have finally found a way to reverse this Process by using Vitreus Essentia for recrystallising the Shards.");
        GregTechAPI.sThaumcraftCompat.addResearch(
            tKey,
            "Shard Recrystallisation",
            "Fixing your precious crystals",
            new String[] { "ALCHEMICALMANUFACTURE" },
            "ALCHEMY",
            GTOreDictUnificator.get(OrePrefixes.gem, Materials.InfusedOrder, 1L),
            3,
            0,
            -11,
            -3,
            Arrays.asList(
                new TCAspects.TC_AspectStack(TCAspects.VITREUS, 5L),
                new TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 3L),
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 3L)),
            null,
            new Object[] { MachineRecipeLoader.aTextTCGTPage + tKey,
                GregTechAPI.sThaumcraftCompat.addCrucibleRecipe(
                    tKey,
                    OrePrefixes.dust.get(Materials.Amber),
                    GTOreDictUnificator.get(OrePrefixes.gem, Materials.Amber, 1L),
                    Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.VITREUS, 4L))),
                GregTechAPI.sThaumcraftCompat.addCrucibleRecipe(
                    tKey,
                    OrePrefixes.dust.get(Materials.InfusedOrder),
                    GTOreDictUnificator.get(OrePrefixes.gem, Materials.InfusedOrder, 1L),
                    Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.VITREUS, 4L))),
                GregTechAPI.sThaumcraftCompat.addCrucibleRecipe(
                    tKey,
                    OrePrefixes.dust.get(Materials.InfusedEntropy),
                    GTOreDictUnificator.get(OrePrefixes.gem, Materials.InfusedEntropy, 1L),
                    Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.VITREUS, 4L))),
                GregTechAPI.sThaumcraftCompat.addCrucibleRecipe(
                    tKey,
                    OrePrefixes.dust.get(Materials.InfusedAir),
                    GTOreDictUnificator.get(OrePrefixes.gem, Materials.InfusedAir, 1L),
                    Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.VITREUS, 4L))),
                GregTechAPI.sThaumcraftCompat.addCrucibleRecipe(
                    tKey,
                    OrePrefixes.dust.get(Materials.InfusedEarth),
                    GTOreDictUnificator.get(OrePrefixes.gem, Materials.InfusedEarth, 1L),
                    Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.VITREUS, 4L))),
                GregTechAPI.sThaumcraftCompat.addCrucibleRecipe(
                    tKey,
                    OrePrefixes.dust.get(Materials.InfusedFire),
                    GTOreDictUnificator.get(OrePrefixes.gem, Materials.InfusedFire, 1L),
                    Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.VITREUS, 4L))),
                GregTechAPI.sThaumcraftCompat.addCrucibleRecipe(
                    tKey,
                    OrePrefixes.dust.get(Materials.InfusedWater),
                    GTOreDictUnificator.get(OrePrefixes.gem, Materials.InfusedWater, 1L),
                    Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.VITREUS, 4L))) });

        tKey = "GT_MAGICENERGY";
        GTLanguageManager.addStringLocalization(
            MachineRecipeLoader.aTextTCGTPage + tKey,
            "While trying to find new ways to integrate magic into your industrial factories, you have discovered a way to convert magical energy into electrical power.");
        GregTechAPI.sThaumcraftCompat.addResearch(
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
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 10L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 10L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 20L),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 10L)),
            null,
            new Object[] { MachineRecipeLoader.aTextTCGTPage + tKey, GregTechAPI.sThaumcraftCompat.addInfusionRecipe(
                tKey,
                ItemList.Hull_LV.get(1L),
                new ItemStack[] { new ItemStack(Blocks.beacon),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.MV, 1L),
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.Aluminium, 1L), ItemList.Sensor_MV.get(1L),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.MV, 1L),
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.Thaumium, 1L), ItemList.Sensor_MV.get(1L) },
                ItemList.MagicEnergyConverter_LV.get(1L),
                5,
                Arrays.asList(
                    new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 32L),
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 32L))) });

        tKey = "GT_MAGICENERGY2";
        GTLanguageManager.addStringLocalization(
            MachineRecipeLoader.aTextTCGTPage + tKey,
            "Attempts to increase the output of your Magic Energy generators have resulted in significant improvements.");
        GregTechAPI.sThaumcraftCompat.addResearch(
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
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 10L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 10L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 20L),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 10L)),
            null,
            new Object[] { MachineRecipeLoader.aTextTCGTPage + tKey,
                GregTechAPI.sThaumcraftCompat.addInfusionRecipe(
                    tKey,
                    ItemList.Hull_MV.get(1L),
                    new ItemStack[] { new ItemStack(Blocks.beacon),
                        GTOreDictUnificator.get(OrePrefixes.circuit, Materials.HV, 1L),
                        GTOreDictUnificator.get(OrePrefixes.plateDouble, Materials.Thaumium, 1L),
                        ItemList.Sensor_HV.get(1L), GTOreDictUnificator.get(OrePrefixes.circuit, Materials.HV, 1L),
                        GTOreDictUnificator.get(OrePrefixes.plateDouble, Materials.StainlessSteel, 1L),
                        ItemList.Sensor_HV.get(1L) },
                    ItemList.MagicEnergyConverter_MV.get(1L),
                    6,
                    Arrays.asList(
                        new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 64L),
                        new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 32L),
                        new TCAspects.TC_AspectStack(TCAspects.MACHINA, 64L))) });

        tKey = "GT_MAGICENERGY3";
        GTLanguageManager.addStringLocalization(
            MachineRecipeLoader.aTextTCGTPage + tKey,
            "Attempts to further increase the output of your Magic Energy generators have resulted in great improvements.");
        GregTechAPI.sThaumcraftCompat.addResearch(
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
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 20L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 20L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 40L),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 20L)),
            null,
            new Object[] { MachineRecipeLoader.aTextTCGTPage + tKey, GregTechAPI.sThaumcraftCompat.addInfusionRecipe(
                tKey,
                ItemList.Hull_HV.get(1L),
                new ItemStack[] { new ItemStack(Blocks.beacon),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.EV, 1L),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Thaumium, 1L),
                    ItemList.Field_Generator_MV.get(1L), GTOreDictUnificator.get(OrePrefixes.circuit, Materials.EV, 1L),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Titanium, 1L),
                    ItemList.Field_Generator_MV.get(1L) },
                ItemList.MagicEnergyConverter_HV.get(1L),
                8,
                Arrays.asList(
                    new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 128L),
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 64L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 128L))) });

        tKey = "GT_MAGICABSORB";
        GTLanguageManager.addStringLocalization(
            MachineRecipeLoader.aTextTCGTPage + tKey,
            "Research into magical energy conversion methods has identified a way to convert surrounding energies into electrical power.");
        GregTechAPI.sThaumcraftCompat.addResearch(
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
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 10L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 10L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 20L),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 10L)),
            null,
            new Object[] { MachineRecipeLoader.aTextTCGTPage + tKey, GregTechAPI.sThaumcraftCompat.addInfusionRecipe(
                tKey,
                ItemList.Hull_LV.get(1L),
                new ItemStack[] { ItemList.MagicEnergyConverter_LV.get(1L),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.MV, 1L),
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.Thaumium, 1L), ItemList.Sensor_MV.get(1L) },
                ItemList.MagicEnergyAbsorber_LV.get(1L),
                6,
                Arrays.asList(
                    new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 32L),
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 16L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 32L),
                    new TCAspects.TC_AspectStack(TCAspects.VACUOS, 16L),
                    new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 32L),
                    new TCAspects.TC_AspectStack(TCAspects.STRONTIO, 4L))) });

        tKey = "GT_MAGICABSORB2";
        GTLanguageManager
            .addStringLocalization(MachineRecipeLoader.aTextTCGTPage + tKey, "Moar output! Drain all the Magic!");
        GregTechAPI.sThaumcraftCompat.addResearch(
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
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 10L),
                new TCAspects.TC_AspectStack(TCAspects.COGNITIO, 10L),
                new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 20L),
                new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 10L)),
            null,
            new Object[] { MachineRecipeLoader.aTextTCGTPage + tKey,
                GregTechAPI.sThaumcraftCompat.addInfusionRecipe(
                    tKey,
                    ItemList.Hull_MV.get(1L),
                    new ItemStack[] { ItemList.MagicEnergyConverter_MV.get(1L),
                        GTOreDictUnificator.get(OrePrefixes.circuit, Materials.HV, 1L),
                        GTOreDictUnificator.get(OrePrefixes.plate, Materials.Thaumium, 1L), ItemList.Sensor_HV.get(1L),
                        GTOreDictUnificator.get(OrePrefixes.circuit, Materials.HV, 1L),
                        GTOreDictUnificator.get(OrePrefixes.plate, Materials.Thaumium, 1L) },
                    ItemList.MagicEnergyAbsorber_MV.get(1L),
                    6,
                    Arrays.asList(
                        new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 64L),
                        new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 32L),
                        new TCAspects.TC_AspectStack(TCAspects.MACHINA, 64L),
                        new TCAspects.TC_AspectStack(TCAspects.VACUOS, 32L),
                        new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 64L),
                        new TCAspects.TC_AspectStack(TCAspects.STRONTIO, 8L))),
                GregTechAPI.sThaumcraftCompat.addInfusionRecipe(
                    tKey,
                    ItemList.Hull_HV.get(1L),
                    new ItemStack[] { ItemList.MagicEnergyConverter_MV.get(1L),
                        GTOreDictUnificator.get(OrePrefixes.circuit, Materials.EV, 1L),
                        GTOreDictUnificator.get(OrePrefixes.plate, Materials.Void, 1),
                        ItemList.Field_Generator_MV.get(1L),
                        GTOreDictUnificator.get(OrePrefixes.circuit, Materials.EV, 1L),
                        GTOreDictUnificator.get(OrePrefixes.plate, Materials.Void, 1), },
                    ItemList.MagicEnergyAbsorber_HV.get(1L),
                    8,
                    Arrays.asList(
                        new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 128L),
                        new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 64L),
                        new TCAspects.TC_AspectStack(TCAspects.MACHINA, 128L),
                        new TCAspects.TC_AspectStack(TCAspects.VACUOS, 64L),
                        new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 128L),
                        new TCAspects.TC_AspectStack(TCAspects.STRONTIO, 16L))),
                GregTechAPI.sThaumcraftCompat.addInfusionRecipe(
                    tKey,
                    ItemList.Hull_EV.get(1L),
                    new ItemStack[] { ItemList.MagicEnergyConverter_HV.get(1L),
                        GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 1L),
                        GTOreDictUnificator.get(OrePrefixes.plate, Materials.Void, 1),
                        GTOreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 1),
                        ItemList.Field_Generator_HV.get(1L),
                        GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 1L),
                        GTOreDictUnificator.get(OrePrefixes.plate, Materials.Void, 1),
                        GTOreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 1), },
                    ItemList.MagicEnergyAbsorber_EV.get(1L),
                    10,
                    Arrays.asList(
                        new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 256L),
                        new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 128L),
                        new TCAspects.TC_AspectStack(TCAspects.MACHINA, 256L),
                        new TCAspects.TC_AspectStack(TCAspects.VACUOS, 128L),
                        new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 256L),
                        new TCAspects.TC_AspectStack(TCAspects.STRONTIO, 64L))) });

        tKey = "GT_HAZMATENCH";
        GTLanguageManager.addStringLocalization(
            MachineRecipeLoader.aTextTCGTPage + tKey,
            "You have discovered a way to magically enchant a mundane piece of armor with the protective properties of a Hazmat suite.");
        GregTechAPI.sThaumcraftCompat.addResearch(
            tKey,
            "Hazmat Protection",
            "Magical protection from physical hazards",
            new String[] { "INFUSIONENCHANTMENT" },
            "ARTIFICE",
            GTModHandler.getIC2Item("hazmatChestplate", 1),
            4,
            0,
            -7,
            13,
            Arrays.asList(
                new TCAspects.TC_AspectStack(TCAspects.VITIUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.GELUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.RADIO, 1L),
                new TCAspects.TC_AspectStack(TCAspects.TUTAMEN, 1L),
                new TCAspects.TC_AspectStack(TCAspects.VENENUM, 1L)),
            null,
            new Object[] { MachineRecipeLoader.aTextTCGTPage + tKey,
                GregTechAPI.sThaumcraftCompat.addInfusionEnchantmentRecipe(
                    tKey,
                    EnchantmentHazmat.INSTANCE,
                    5,
                    Arrays.asList(
                        new TCAspects.TC_AspectStack(TCAspects.VITIUM, 8L),
                        new TCAspects.TC_AspectStack(TCAspects.GELUM, 16L),
                        new TCAspects.TC_AspectStack(TCAspects.RADIO, 16L),
                        new TCAspects.TC_AspectStack(TCAspects.TUTAMEN, 32L),
                        new TCAspects.TC_AspectStack(TCAspects.VENENUM, 16L)),
                    new ItemStack[] { getModItem(Thaumcraft.ID, "ItemResource", 1L, 14),
                        GTOreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 1),
                        GTOreDictUnificator.get(OrePrefixes.plate, Materials.Rubber, 1),
                        GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Lead, 1),
                        getModItem(Thaumcraft.ID, "ItemResource", 1L, 14),
                        GTOreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 1),
                        GTOreDictUnificator.get(OrePrefixes.plate, Materials.Rubber, 1),
                        GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Lead, 1) }) });

    }
}

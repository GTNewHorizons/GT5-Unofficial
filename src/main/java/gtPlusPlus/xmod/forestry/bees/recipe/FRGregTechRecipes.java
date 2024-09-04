package gtPlusPlus.xmod.forestry.bees.recipe;

import static gregtech.api.enums.Mods.MagicBees;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.RecipeUtils;
import gtPlusPlus.xmod.forestry.bees.items.FRItemRegistry;

public class FRGregTechRecipes {

    private static String rod_Electrum = "stickElectrum";
    private static String rod_LongElectrum = "stickLongElectrum";
    private static String foil_Electrum = "foilElectrum";
    private static String rod_Uranium = "stickUranium";
    private static String rod_LongUranium = "stickLongUranium";
    private static String foil_Uranium235 = "foilUranium235";
    private static ItemStack hiveFrameAccelerated = ItemUtils.getSimpleStack(FRItemRegistry.hiveFrameAccelerated);
    private static ItemStack hiveFrameMutagenic = ItemUtils.getSimpleStack(FRItemRegistry.hiveFrameMutagenic);
    private static ItemStack hiveFrameVoid = ItemUtils.getSimpleStack(FRItemRegistry.hiveFrameVoid);
    private static ItemStack hiveFrameBusy = ItemUtils.getSimpleStack(FRItemRegistry.hiveFrameBusy);

    private static ItemStack hiveFrameCocoa = ItemUtils.getSimpleStack(FRItemRegistry.hiveFrameCocoa);
    private static ItemStack hiveFrameCaged = ItemUtils.getSimpleStack(FRItemRegistry.hiveFrameCaged);
    private static ItemStack hiveFrameSoul = ItemUtils.getSimpleStack(FRItemRegistry.hiveFrameSoul);
    private static ItemStack hiveFrameClay = ItemUtils.getSimpleStack(FRItemRegistry.hiveFrameClay);
    private static ItemStack hiveFrameNova = ItemUtils.getSimpleStack(FRItemRegistry.hiveFrameNova);

    private static ItemStack hiveFrameImpregnated = ItemUtils.getItemStackFromFQRN("Forestry:frameImpregnated", 1);
    private static ItemStack blockSoulSand = new ItemStack(Blocks.soul_sand, 1);
    private static ItemStack blockIronBars = new ItemStack(Blocks.iron_bars, 1);
    private static ItemStack itemClayDust = new ItemStack(Items.clay_ball, 1);
    private static ItemStack itemCocoaBeans = new ItemStack(Items.dye, 1, 3);

    private static ItemStack hiveFrameDecay = ItemUtils.getSimpleStack(FRItemRegistry.hiveFrameDecay);
    private static ItemStack hiveFrameSlow = ItemUtils.getSimpleStack(FRItemRegistry.hiveFrameSlow);
    private static ItemStack hiveFrameStalilize = ItemUtils.getSimpleStack(FRItemRegistry.hiveFrameStalilize);
    private static ItemStack hiveFrameArborist = ItemUtils.getSimpleStack(FRItemRegistry.hiveFrameArborist);

    public static void registerItems() {
        // Magic Bee Like Frames
        RecipeUtils.addShapedGregtechRecipe(
            rod_LongElectrum,
            rod_Electrum,
            rod_LongElectrum,
            rod_LongElectrum,
            foil_Electrum,
            rod_LongElectrum,
            rod_Electrum,
            rod_Electrum,
            rod_Electrum,
            hiveFrameAccelerated);

        RecipeUtils.addShapedGregtechRecipe(
            rod_LongUranium,
            rod_Uranium,
            rod_LongUranium,
            rod_LongUranium,
            foil_Uranium235,
            rod_LongUranium,
            rod_Uranium,
            rod_Uranium,
            rod_Uranium,
            hiveFrameMutagenic);
        if (MagicBees.isModLoaded()) {
            RecipeUtils.addShapelessGregtechRecipe(
                new ItemStack[] { hiveFrameVoid },
                ItemUtils.getCorrectStacktype("MagicBees:frameOblivion", 1));
        }
        RecipeUtils.addShapedGregtechRecipe(
            "stickLongBlueSteel",
            "stickBlueSteel",
            "stickLongBlueSteel",
            "stickLongBlueSteel",
            ItemUtils.getSimpleStack(Items.nether_star),
            "stickLongBlueSteel",
            "stickBlueSteel",
            "stickBlueSteel",
            "stickBlueSteel",
            hiveFrameBusy);

        // Frame Items added by bartimaeusnek
        RecipeUtils.addShapedGregtechRecipe(
            ItemUtils.getItemStackOfAmountFromOreDict("stickLongTumbaga", 1),
            ItemUtils.getItemStackOfAmountFromOreDict("stickTumbaga", 1),
            ItemUtils.getItemStackOfAmountFromOreDict("stickLongTumbaga", 1),
            ItemUtils.getItemStackOfAmountFromOreDict("stickLongTumbaga", 1),
            foil_Electrum,
            ItemUtils.getItemStackOfAmountFromOreDict("stickLongTumbaga", 1),
            ItemUtils.getItemStackOfAmountFromOreDict("stickTumbaga", 1),
            ItemUtils.getItemStackOfAmountFromOreDict("stickTumbaga", 1),
            ItemUtils.getItemStackOfAmountFromOreDict("stickTumbaga", 1),
            hiveFrameSlow);

        RecipeUtils.addShapedGregtechRecipe(
            "stickLongWroughtIron",
            "stickWroughtIron",
            "stickLongWroughtIron",
            "stickLongWroughtIron",
            "foilZinc",
            "stickLongWroughtIron",
            "stickWroughtIron",
            "stickWroughtIron",
            "stickWroughtIron",
            hiveFrameDecay);

        RecipeUtils.addShapedGregtechRecipe(
            "stickLongOsmiridium",
            "stickOsmiridium",
            "stickLongOsmiridium",
            "stickLongOsmiridium",
            "foilOsmiridium",
            "stickLongOsmiridium",
            "stickOsmiridium",
            "stickOsmiridium",
            "stickOsmiridium",
            hiveFrameStalilize);

        RecipeUtils.addShapedGregtechRecipe(
            "stickLongWoodSealed",
            "stickWoodSealed",
            "stickLongWoodSealed",
            "stickLongWoodSealed",
            Items.paper,
            "stickLongWoodSealed",
            "stickWoodSealed",
            "stickWoodSealed",
            "stickWoodSealed",
            hiveFrameArborist);
    }
}

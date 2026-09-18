package gregtech.common.items;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TCAspects;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.common.items.tools.GTToolItems;
import gregtech.common.tools.ItemNetworkAnalyzer;
import gregtech.common.tools.ToolVajra;

public class MetaGeneratedTool01 extends MetaGeneratedTool {

    public static MetaGeneratedTool01 INSTANCE;

    public MetaGeneratedTool01() {
        super("metatool.01");
        INSTANCE = this;

        ItemList.Tool_Vajra.set(new ToolVajra("Tool_Vajra", "Vajra", "", 0, 20, true));
        ItemList.NetworkAnalyzer.set(new ItemNetworkAnalyzer("Network Analyzer", "", 0, 0, true));

        initCraftingShapedRecipes();
        initCraftingShapelessRecipes();
    }

    private void initCraftingShapelessRecipes() {
        GTModHandler.addShapelessCraftingRecipe(
            GTOreDictUnificator.get(OrePrefixes.dust, Materials.Coal, 1L),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ToolDictNames.craftingToolMortar, new ItemStack(Items.coal, 1) });
        GTModHandler.addShapelessCraftingRecipe(
            GTOreDictUnificator.get(OrePrefixes.dust, Materials.Clay, 1L),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ToolDictNames.craftingToolMortar, new ItemStack(Blocks.clay, 1) });
        GTModHandler.addShapelessCraftingRecipe(
            GTOreDictUnificator.get(OrePrefixes.dust, Materials.Wheat, 1L),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ToolDictNames.craftingToolMortar, new ItemStack(Items.wheat, 1) });
        GTModHandler.addShapelessCraftingRecipe(
            new ItemStack(Items.flint, 1),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ToolDictNames.craftingToolMortar, new ItemStack(Blocks.gravel, 1) });
        GTModHandler.addShapelessCraftingRecipe(
            new ItemStack(Items.blaze_powder, 2),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ToolDictNames.craftingToolMortar, new ItemStack(Items.blaze_rod, 1) });
    }

    private void initCraftingShapedRecipes() {
        GTModHandler.addCraftingRecipe(
            mortar(Materials.Flint),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " I ", "SIS", "SSS", 'I', new ItemStack(Items.flint, 1), 'S', OrePrefixes.stone });
        GTModHandler.addCraftingRecipe(
            mortar(Materials.Bronze),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.Bronze), 'S', OrePrefixes.stone });
        GTModHandler.addCraftingRecipe(
            mortar(Materials.Iron),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.Iron), 'S', OrePrefixes.stone });
        GTModHandler.addCraftingRecipe(
            mortar(Materials.Steel),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.Steel), 'S', OrePrefixes.stone });
        GTModHandler.addCraftingRecipe(
            mortar(Materials.CastIron),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.CastIron), 'S',
                OrePrefixes.stone });
        GTModHandler.addCraftingRecipe(
            mortar(Materials.RedSteel),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.RedSteel), 'S',
                OrePrefixes.stone });
        GTModHandler.addCraftingRecipe(
            mortar(Materials.BlueSteel),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.BlueSteel), 'S',
                OrePrefixes.stone });
        GTModHandler.addCraftingRecipe(
            mortar(Materials.BlackSteel),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.BlackSteel), 'S',
                OrePrefixes.stone });
        GTModHandler.addCraftingRecipe(
            mortar(Materials.DamascusSteel),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.DamascusSteel), 'S',
                OrePrefixes.stone });
        GTModHandler.addCraftingRecipe(
            mortar(Materials.Thaumium),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.Thaumium), 'S',
                OrePrefixes.stone });

        GTModHandler.addCraftingRecipe(
            rollingPin(Materials.Wood),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "  S", " I ", "S f", 'I', OrePrefixes.plank.get(Materials.Wood), 'S',
                OrePrefixes.stick.get(Materials.Wood) });
        GTModHandler.addCraftingRecipe(
            rollingPin(Materials.Polyethylene),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "  S", " I ", "S f", 'I', OrePrefixes.ingot.get(Materials.Polyethylene), 'S',
                OrePrefixes.stick.get(Materials.Polyethylene) });
        GTModHandler.addCraftingRecipe(
            rollingPin(Materials.Aluminium),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "  S", " I ", "S f", 'I', OrePrefixes.ingot.get(Materials.Aluminium), 'S',
                OrePrefixes.stick.get(Materials.Aluminium) });
        GTModHandler.addCraftingRecipe(
            rollingPin(Materials.StainlessSteel),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "  S", " I ", "S f", 'I', OrePrefixes.ingot.get(Materials.StainlessSteel), 'S',
                OrePrefixes.stick.get(Materials.StainlessSteel) });
        GTModHandler.addCraftingRecipe(
            rollingPin(Materials.IronWood),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "  S", " I ", "S f", 'I', OrePrefixes.ingot.get(Materials.IronWood), 'S',
                OrePrefixes.stick.get(Materials.IronWood) });

        GTModHandler.addCraftingRecipe(
            GTToolItems.KNIFE.registerMaterial(
                Materials.Flint,
                new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.TELUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.CORPUS, 2L)),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "F", "S", 'S', OrePrefixes.stick.get(Materials.Wood), 'F', new ItemStack(Items.flint, 1) });
    }

    /**
     * Declares the mortar for this material and returns a stack of it, for the fixed-material recipes below. The
     * mortar is its own item now, so the material is its metadata rather than NBT.
     */
    private static ItemStack mortar(Materials material) {
        return GTToolItems.MORTAR.registerMaterial(
            material,
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.FABRICO, 2L),
            new TCAspects.TC_AspectStack(TCAspects.PERDITIO, 2L));
    }

    /**
     * Declares the rolling pin for this material and returns a stack of it, for the fixed-material recipes above.
     */
    private static ItemStack rollingPin(Materials material) {
        return GTToolItems.ROLLING_PIN.registerMaterial(
            material,
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.LIMUS, 4L));
    }
}

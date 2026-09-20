package gregtech.common.items;

import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TCAspects;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.common.items.tools.GTToolItems;
import gregtech.common.tools.ItemNetworkAnalyzer;
import gregtech.common.tools.ToolVajra;

/**
 * The old tool meta-item, which no longer carries a single tool: every type has its own item under
 * {@link GTToolItems} now, with the material in the metadata. This class survives for three reasons -- it keeps
 * {@code gregtech:gt.metatool.01} in the registry so that {@code PosteaTransformers} can rewrite saved stacks
 * against it, it is where the Vajra and the Network Analyzer happen to be constructed, and it holds the
 * fixed-material mortar, rolling pin and flint knife recipes.
 * <p/>
 * Nothing should ever hold a stack of this item. A stack that survives the world converter is one it could not
 * place -- a tool whose material no longer resolves, or a metadata that never had a tool behind it -- so the
 * display overrides below make such a stack unmistakable rather than letting it pass as a working tool: it draws
 * as the missing texture and its tooltip asks for a bug report.
 */
public class MetaGeneratedTool01 extends MetaGeneratedTool {

    public static MetaGeneratedTool01 INSTANCE;

    private static final String DEPRECATED_NAME_KEY = "gt.metatool.01.deprecated.name";
    private static final String DEPRECATED_TOOLTIP_KEY = "gt.metatool.01.deprecated.tooltip";
    private static final String DEPRECATED_MIGRATED_KEY = "gt.metatool.01.deprecated.migrated";
    private static final String DEPRECATED_REPORT_KEY = "gt.metatool.01.deprecated.report";

    public MetaGeneratedTool01() {
        super("metatool.01");
        INSTANCE = this;

        ItemList.Tool_Vajra.set(new ToolVajra("Tool_Vajra", "Vajra", "", 0, 20, true));
        ItemList.NetworkAnalyzer.set(new ItemNetworkAnalyzer("Network Analyzer", "", 0, 0, true));

        initCraftingShapedRecipes();
        initCraftingShapelessRecipes();
        hideResidualMetasFromNEI();
    }

    /**
     * This item registers no tools any more, so {@link #getSubItems} (final in {@link MetaGeneratedTool}) never adds
     * anything for it: {@code mToolStats} is empty for every metadata value. NEI doesn't take an empty result as
     * "no subtypes" though -- for an item that reports {@code hasSubtypes() == true} (every {@code MetaBaseItem}
     * does) but hands back nothing, it falls back to guessing metadata 0 through 15 itself, the same guess it'd make
     * for a block's four-bit metadata range. That guess is wrong here: this item's real stacks, when the world
     * converter leaves one behind, carry whatever old tool-type metadata the save had, not necessarily inside
     * 0-15. Hiding that guessed range is enough to stop the false entries from showing in NEI's item list; it isn't
     * meant to hide every metadata this item could theoretically hold.
     */
    private void hideResidualMetasFromNEI() {
        if (!Mods.NotEnoughItems.isModLoaded()) return;
        for (int i = 0; i < 16; i++) {
            codechicken.nei.api.API.hideItem(new ItemStack(this, 1, i));
        }
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

    /**
     * The missing texture, deliberately. Asking the atlas for a sprite it does not hold gives back its
     * missing-texture sprite, which gets the magenta-and-black cube without logging a resource error on every
     * launch for an item that should normally have no stacks at all.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aIconRegister) {
        mIcon = aIconRegister instanceof TextureMap atlas ? atlas.getAtlasSprite("missingno") : null;
    }

    @Override
    public IIcon getIconFromDamage(int aMetaData) {
        return mIcon;
    }

    /**
     * Named for what it is rather than for the tool it used to be, and carrying the metadata, so that a screenshot
     * of one is enough to say which stack the converter left behind.
     */
    @Override
    public String getItemStackDisplayName(ItemStack aStack) {
        return translateToLocalFormatted(DEPRECATED_NAME_KEY, String.valueOf(aStack.getItemDamage()));
    }

    @Override
    public void addAdditionalToolTips(List<String> aList, ItemStack aStack, EntityPlayer aPlayer) {
        aList.add(EnumChatFormatting.RED + translateToLocal(DEPRECATED_TOOLTIP_KEY));
        aList.add(EnumChatFormatting.GRAY + translateToLocal(DEPRECATED_MIGRATED_KEY));
        aList.add(
            EnumChatFormatting.YELLOW
                + translateToLocalFormatted(DEPRECATED_REPORT_KEY, String.valueOf(aStack.getItemDamage())));
    }
}

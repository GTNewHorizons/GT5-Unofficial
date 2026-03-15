package gregtech.common.items;

import static gregtech.api.enums.GTValues.NF;
import static gregtech.api.enums.GTValues.NI;
import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.Mods.AE2FluidCraft;
import static gregtech.api.enums.Mods.Botania;
import static gregtech.api.enums.Mods.ExtraBees;
import static gregtech.api.enums.Mods.ExtraUtilities;
import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.GalaxySpace;
import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.enums.Mods.MagicBees;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.enums.Mods.ThaumicBases;
import static gregtech.api.recipe.RecipeMaps.autoclaveRecipes;
import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidExtractionRecipes;
import static gregtech.api.recipe.RecipeMaps.multiblockChemicalReactorRecipes;
import static gregtech.api.recipe.RecipeMaps.sifterRecipes;
import static gregtech.api.util.GTRecipeBuilder.EIGHTH_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.CLEANROOM;
import static gregtech.api.util.GTRecipeConstants.UniversalChemical;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalDehydratorRecipes;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.core.Tabs;
import forestry.api.recipes.RecipeManagers;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IGT_ItemWithMaterialRenderer;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTUtility;
import gregtech.common.render.items.GeneratedMaterialRenderer;
import gregtech.loaders.misc.GTBees;
import mods.railcraft.common.items.firestone.IItemFirestoneBurning;
import vazkii.botania.common.item.ModItems;

@Optional.Interface(
    iface = "mods.railcraft.common.items.firestone.IItemFirestoneBurning",
    modid = Mods.ModIDs.RAILCRAFT)
public class ItemComb extends Item implements IGT_ItemWithMaterialRenderer, IItemFirestoneBurning {

    @SideOnly(Side.CLIENT)
    private IIcon secondIcon;

    public ItemComb() {
        super();
        this.setCreativeTab(Tabs.tabApiculture);
        this.setHasSubtypes(true);
        this.setUnlocalizedName("gt.comb");
        GameRegistry.registerItem(this, "gt.comb", GregTech.ID);
    }

    public ItemStack getStackForType(CombType type) {
        return new ItemStack(this, 1, type.getId());
    }

    public ItemStack getStackForType(CombType type, int count) {
        return new ItemStack(this, count, type.getId());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tabs, List<ItemStack> list) {
        for (CombType type : CombType.values()) {
            if (type.showInList) {
                list.add(this.getStackForType(type));
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    public int getRenderPasses(int meta) {
        return 2;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon("forestry:beeCombs.0");
        this.secondIcon = iconRegister.registerIcon("forestry:beeCombs.1");
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        return (pass == 0) ? itemIcon : secondIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int pass) {
        CombType type = CombType.valueOf(stack.getItemDamage());
        return type.getColours()[GTUtility.clamp(pass, 0, 1)];
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return CombType.valueOf(stack.getItemDamage())
            .getName();
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean debugInfo) {
        tooltip.add(EnumChatFormatting.DARK_RED + StatCollector.translateToLocal("GT5U.tooltip.comb.ban_forestry"));
    }

    @Override
    public boolean shouldUseCustomRenderer(int aMetaData) {
        return CombType.valueOf(aMetaData).material.renderer != null;
    }

    @Override
    public GeneratedMaterialRenderer getMaterialRenderer(int aMetaData) {
        return CombType.valueOf(aMetaData).material.renderer;
    }

    @Override
    public boolean allowMaterialRenderer(int aMetaData) {
        return true;
    }

    @Override
    public IIcon getIcon(int aMetaData, int pass) {
        return (pass == 0) ? itemIcon : secondIcon;
    }

    @Override
    public IIcon getOverlayIcon(int aMetaData, int pass) {
        return null;
    }

    @Override
    public short[] getRGBa(ItemStack aStack) {
        return CombType.valueOf(aStack.getItemDamage()).material.mRGBa;
    }

    public void initCombsRecipes() {

        // Organic
        addProcessGT(CombType.LIGNIE, new Materials[] { Materials.Lignite }, Voltage.LV);
        addProcessGT(CombType.COAL, new Materials[] { Materials.Coal }, Voltage.LV);
        addCentrifugeToItemStack(
            CombType.STICKY,
            new ItemStack[] { ItemList.IC2_Resin.get(1), ItemList.IC2_Plantball.get(1), ItemList.FR_Wax.get(1) },
            new int[] { 50 * 100, 15 * 100, 50 * 100 },
            Voltage.ULV);
        addProcessGT(CombType.OIL, new Materials[] { Materials.Oilsands }, Voltage.LV);
        addProcessGT(CombType.APATITE, new Materials[] { Materials.Apatite }, Voltage.LV);
        addCentrifugeToMaterial(
            CombType.ASH,
            new Materials[] { Materials.AshDark, Materials.Ash },
            new int[] { 50 * 100, 50 * 100 },
            GTValues.emptyIntArray,
            Voltage.ULV,
            NI,
            50 * 100);
        addCentrifugeToItemStack(
            CombType.PHOSPHORUS,
            new ItemStack[] { Materials.Phosphorus.getDust(1), Materials.TricalciumPhosphate.getDust(2),
                ItemList.FR_Wax.get(1) },
            new int[] { 100 * 100, 100 * 100, 100 * 100 },
            Voltage.HV);
        addCentrifugeToItemStack(
            CombType.MICA,
            new ItemStack[] { Materials.Mica.getDust(2), ItemList.FR_Wax.get(1) },
            new int[] { 100 * 100, 75 * 100 },
            Voltage.HV);

        addCentrifugeToItemStack(
            CombType.LIGNIE,
            new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.gem, Materials.Lignite, 1), ItemList.FR_Wax.get(1) },
            new int[] { 90 * 100, 50 * 100 },
            Voltage.ULV);
        addCentrifugeToItemStack(
            CombType.COAL,
            new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 1), ItemList.FR_Wax.get(1) },
            new int[] { 5 * 100, 50 * 100 },
            Voltage.ULV);

        // ic2
        addCentrifugeToItemStack(
            CombType.COOLANT,
            new ItemStack[] { GTBees.drop.getStackForType(DropType.COOLANT), ItemList.FR_Wax.get(1) },
            new int[] { 100 * 100, 100 * 100 },
            Voltage.HV,
            196);
        addCentrifugeToItemStack(
            CombType.ENERGY,
            new ItemStack[] { GTBees.drop.getStackForType(DropType.HOT_COOLANT), ItemList.IC2_Energium_Dust.get(1L),
                ItemList.FR_RefractoryWax.get(1) },
            new int[] { 20 * 100, 20 * 100, 50 * 100 },
            Voltage.HV,
            196);
        addCentrifugeToItemStack(
            CombType.LAPOTRON,
            new ItemStack[] { GTBees.drop.getStackForType(DropType.LAPIS),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "LapotronDust", 1, 0),
                GTModHandler.getModItem(MagicBees.ID, "wax", 1, 2) },
            new int[] { 20 * 100, 100 * 100, 40 * 100 },
            Voltage.HV,
            240);
        addCentrifugeToMaterial(
            CombType.PYROTHEUM,
            new Materials[] { Materials.Blaze, Materials.Pyrotheum },
            new int[] { 25 * 100, 20 * 100 },
            GTValues.emptyIntArray,
            Voltage.HV,
            NI,
            30 * 100);
        addCentrifugeToItemStack(
            CombType.CRYOTHEUM,
            new ItemStack[] { ItemList.FR_RefractoryWax.get(1), Materials.Cryotheum.getDust(1) },
            new int[] { 50 * 100, 100 * 100 },
            Voltage.MV);
        addCentrifugeToItemStack(
            CombType.BLIZZ,
            new ItemStack[] { ItemList.FR_RefractoryWax.get(1), Materials.Blizz.getDust(1) },
            new int[] { 50 * 100, 100 * 100 },
            Voltage.MV);
        // Alloy
        addProcessGT(CombType.REDALLOY, new Materials[] { Materials.RedAlloy }, Voltage.LV);
        addProcessGT(CombType.REDSTONEALLOY, new Materials[] { Materials.RedstoneAlloy }, Voltage.LV);
        addProcessGT(CombType.CONDUCTIVEIRON, new Materials[] { Materials.ConductiveIron }, Voltage.MV);
        addProcessGT(CombType.VIBRANTALLOY, new Materials[] { Materials.VibrantAlloy }, Voltage.HV);
        addProcessGT(CombType.ENERGETICALLOY, new Materials[] { Materials.EnergeticAlloy }, Voltage.HV);
        addProcessGT(CombType.ELECTRICALSTEEL, new Materials[] { Materials.ElectricalSteel }, Voltage.LV);
        addProcessGT(CombType.DARKSTEEL, new Materials[] { Materials.DarkSteel }, Voltage.MV);
        addProcessGT(CombType.PULSATINGIRON, new Materials[] { Materials.PulsatingIron }, Voltage.HV);
        addProcessGT(CombType.STAINLESSSTEEL, new Materials[] { Materials.StainlessSteel }, Voltage.HV);
        addProcessGT(CombType.BEDROCKIUM, new Materials[] { Materials.Bedrockium }, Voltage.EV);
        addCentrifugeToItemStack(
            CombType.ENDERIUM,
            new ItemStack[] { ItemList.FR_RefractoryWax.get(1),
                GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.EnderiumBase, 1),
                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Enderium, 1) },
            new int[] { 50 * 100, 30 * 100, 50 * 100 },
            Voltage.HV);

        addCentrifugeToMaterial(
            CombType.REDALLOY,
            new Materials[] { Materials.RedAlloy },
            new int[] { 100 * 100 },
            GTValues.emptyIntArray,
            Voltage.ULV,
            ItemList.FR_RefractoryWax.get(1),
            50 * 100);
        addCentrifugeToMaterial(
            CombType.REDSTONEALLOY,
            new Materials[] { Materials.RedstoneAlloy },
            new int[] { 100 * 100 },
            GTValues.emptyIntArray,
            Voltage.ULV,
            ItemList.FR_RefractoryWax.get(1),
            50 * 100);
        addCentrifugeToMaterial(
            CombType.CONDUCTIVEIRON,
            new Materials[] { Materials.ConductiveIron },
            new int[] { 90 * 100 },
            GTValues.emptyIntArray,
            Voltage.MV,
            ItemList.FR_RefractoryWax.get(1),
            50 * 100);
        addCentrifugeToMaterial(
            CombType.VIBRANTALLOY,
            new Materials[] { Materials.VibrantAlloy },
            new int[] { 70 * 100 },
            GTValues.emptyIntArray,
            Voltage.HV,
            ItemList.FR_RefractoryWax.get(1),
            50 * 100);
        addCentrifugeToMaterial(
            CombType.ENERGETICALLOY,
            new Materials[] { Materials.EnergeticAlloy },
            new int[] { 80 * 100 },
            GTValues.emptyIntArray,
            Voltage.HV,
            ItemList.FR_RefractoryWax.get(1),
            50 * 100);
        addCentrifugeToMaterial(
            CombType.ELECTRICALSTEEL,
            new Materials[] { Materials.ElectricalSteel },
            new int[] { 100 * 100 },
            GTValues.emptyIntArray,
            Voltage.ULV,
            ItemList.FR_RefractoryWax.get(1),
            50 * 100);
        addCentrifugeToMaterial(
            CombType.DARKSTEEL,
            new Materials[] { Materials.DarkSteel },
            new int[] { 100 * 100 },
            GTValues.emptyIntArray,
            Voltage.MV,
            ItemList.FR_RefractoryWax.get(1),
            50 * 100);
        addCentrifugeToMaterial(
            CombType.PULSATINGIRON,
            new Materials[] { Materials.PulsatingIron },
            new int[] { 80 * 100 },
            GTValues.emptyIntArray,
            Voltage.HV,
            ItemList.FR_RefractoryWax.get(1),
            50 * 100);
        addCentrifugeToMaterial(
            CombType.STAINLESSSTEEL,
            new Materials[] { Materials.StainlessSteel },
            new int[] { 50 * 100 },
            GTValues.emptyIntArray,
            Voltage.HV,
            ItemList.FR_RefractoryWax.get(1),
            50 * 100);
        addCentrifugeToMaterial(
            CombType.BEDROCKIUM,
            new Materials[] { Materials.Bedrockium },
            new int[] { 50 * 100 },
            GTValues.emptyIntArray,
            Voltage.EV,
            ItemList.FR_RefractoryWax.get(1),
            50 * 100);

        // Thaumic
        addProcessGT(CombType.THAUMIUMDUST, new Materials[] { Materials.Thaumium }, Voltage.MV);
        addCentrifugeToItemStack(
            CombType.THAUMIUMSHARD,
            new ItemStack[] { GTModHandler.getModItem(MagicBees.ID, "propolis", 1, 1),
                GTModHandler.getModItem(MagicBees.ID, "propolis", 1, 2),
                GTModHandler.getModItem(MagicBees.ID, "propolis", 1, 3),
                GTModHandler.getModItem(MagicBees.ID, "propolis", 1, 4),
                GTModHandler.getModItem(MagicBees.ID, "propolis", 1, 5),
                GTModHandler.getModItem(MagicBees.ID, "propolis", 1, 6),
                GTModHandler.getModItem(MagicBees.ID, "wax", 1, 0) },
            new int[] { 20 * 100, 20 * 100, 20 * 100, 20 * 100, 20 * 100, 20 * 100, 50 * 100 },
            Voltage.ULV);
        addProcessGT(CombType.AMBER, new Materials[] { Materials.Amber }, Voltage.LV);
        addProcessGT(CombType.QUICKSILVER, new Materials[] { Materials.Cinnabar }, Voltage.LV);
        addCentrifugeToItemStack(
            CombType.SALISMUNDUS,
            new ItemStack[] { GTModHandler.getModItem(Thaumcraft.ID, "ItemResource", 1, 14),
                GTModHandler.getModItem(MagicBees.ID, "wax", 1, 0) },
            new int[] { 100 * 100, 50 * 100 },
            Voltage.MV);
        addCentrifugeToItemStack(
            CombType.TAINTED,
            new ItemStack[] { GTModHandler.getModItem(Thaumcraft.ID, "ItemResource", 1, 11),
                GTModHandler.getModItem(Thaumcraft.ID, "ItemResource", 1, 12),
                GTModHandler.getModItem(Thaumcraft.ID, "blockTaintFibres", 1, 0),
                GTModHandler.getModItem(Thaumcraft.ID, "blockTaintFibres", 1, 1),
                GTModHandler.getModItem(Thaumcraft.ID, "blockTaintFibres", 1, 2),
                GTModHandler.getModItem(MagicBees.ID, "wax", 1, 0) },
            new int[] { 15 * 100, 15 * 100, 15 * 100, 15 * 100, 15 * 100, 50 * 100 },
            Voltage.ULV);
        addProcessGT(CombType.MITHRIL, new Materials[] { Materials.Mithril }, Voltage.HV);
        addProcessGT(CombType.ASTRALSILVER, new Materials[] { Materials.AstralSilver }, Voltage.HV);
        addCentrifugeToMaterial(
            CombType.ASTRALSILVER,
            new Materials[] { Materials.AstralSilver, Materials.Silver },
            new int[] { 20 * 100, 10 * 100 },
            GTValues.emptyIntArray,
            Voltage.HV,
            GTModHandler.getModItem(MagicBees.ID, "wax", 1, 0),
            50 * 100);
        addCentrifugeToItemStack(
            CombType.THAUMINITE,
            new ItemStack[] { GTModHandler.getModItem(ThaumicBases.ID, "resource", 1, 0),
                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Thaumium, 1),
                GTModHandler.getModItem(MagicBees.ID, "wax", 1, 0) },
            new int[] { 20 * 100, 10 * 100, 50 * 100 },
            Voltage.HV);
        addProcessGT(CombType.SHADOWMETAL, new Materials[] { Materials.Shadow }, Voltage.HV);
        addCentrifugeToMaterial(
            CombType.SHADOWMETAL,
            new Materials[] { Materials.Shadow, Materials.ShadowSteel },
            new int[] { 20 * 100, 10 * 100 },
            GTValues.emptyIntArray,
            Voltage.HV,
            GTModHandler.getModItem(MagicBees.ID, "wax", 1, 0),
            50 * 100);
        addProcessGT(CombType.DIVIDED, new Materials[] { Materials.Diamond }, Voltage.HV);
        addCentrifugeToItemStack(
            CombType.DIVIDED,
            new ItemStack[] { GTModHandler.getModItem(MagicBees.ID, "wax", 1, 0),
                GTModHandler.getModItem(ExtraUtilities.ID, "unstableingot", 1, 1),
                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Iron, 1),
                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Diamond, 1) },
            new int[] { 50 * 100, 20 * 100, 10 * 100, 5 * 100 },
            Voltage.HV);
        addProcessGT(CombType.SPARKLING, new Materials[] { Materials.NetherStar }, Voltage.EV);
        addCentrifugeToItemStack(
            CombType.SPARKLING,
            new ItemStack[] { GTModHandler.getModItem(MagicBees.ID, "wax", 1, 0),
                GTModHandler.getModItem(MagicBees.ID, "miscResources", 2, 5),
                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.NetherStar, 1) },
            new int[] { 50 * 100, 10 * 100, 10 * 100 },
            Voltage.EV);

        addCentrifugeToMaterial(
            CombType.THAUMIUMDUST,
            new Materials[] { Materials.Thaumium },
            new int[] { 100 * 100 },
            GTValues.emptyIntArray,
            Voltage.MV,
            GTModHandler.getModItem(MagicBees.ID, "wax", 1, 0),
            50 * 100);
        addCentrifugeToItemStack(
            CombType.QUICKSILVER,
            new ItemStack[] { GTModHandler.getModItem(MagicBees.ID, "wax", 1, 0),
                GTModHandler.getModItem(Thaumcraft.ID, "ItemNugget", 1, 5) },
            new int[] { 50 * 100, 100 * 100 },
            Voltage.ULV);

        // Gem Line
        addProcessGT(CombType.STONE, new Materials[] { Materials.Soapstone }, Voltage.LV);
        addProcessGT(CombType.CERTUS, new Materials[] { Materials.CertusQuartz }, Voltage.LV);
        addProcessGT(CombType.FLUIX, new Materials[] { Materials.Fluix }, Voltage.LV);
        addProcessGT(CombType.REDSTONE, new Materials[] { Materials.Redstone }, Voltage.LV);
        addCentrifugeToMaterial(
            CombType.RAREEARTH,
            new Materials[] { Materials.RareEarth },
            new int[] { 100 * 100 },
            new int[] { 1 },
            Voltage.ULV,
            NI,
            30 * 100);
        addProcessGT(CombType.LAPIS, new Materials[] { Materials.Lapis }, Voltage.LV);
        addProcessGT(CombType.RUBY, new Materials[] { Materials.Ruby }, Voltage.LV);
        addProcessGT(CombType.REDGARNET, new Materials[] { Materials.GarnetRed }, Voltage.LV);
        addProcessGT(CombType.YELLOWGARNET, new Materials[] { Materials.GarnetYellow }, Voltage.LV);
        addProcessGT(CombType.SAPPHIRE, new Materials[] { Materials.Sapphire }, Voltage.LV);
        addProcessGT(CombType.DIAMOND, new Materials[] { Materials.Diamond }, Voltage.LV);
        addProcessGT(CombType.OLIVINE, new Materials[] { Materials.Olivine }, Voltage.LV);
        addProcessGT(CombType.EMERALD, new Materials[] { Materials.Emerald }, Voltage.LV);
        addProcessGT(CombType.FIRESTONE, new Materials[] { Materials.Firestone }, Voltage.LV);
        addProcessGT(CombType.PYROPE, new Materials[] { Materials.Pyrope }, Voltage.LV);
        addProcessGT(CombType.GROSSULAR, new Materials[] { Materials.Grossular }, Voltage.LV);

        addCentrifugeToMaterial(
            CombType.STONE,
            new Materials[] { Materials.Stone, Materials.GraniteBlack, Materials.GraniteRed, Materials.Basalt,
                Materials.Marble, Materials.Redrock },
            new int[] { 70 * 100, 50 * 100, 50 * 100, 50 * 100, 50 * 100, 50 * 100 },
            new int[] { 9, 9, 9, 9, 9, 9 },
            Voltage.ULV,
            NI,
            50 * 100);
        addCentrifugeToMaterial(
            CombType.FLUIX,
            new Materials[] { Materials.Fluix },
            new int[] { 25 * 100 },
            new int[] { 9 },
            Voltage.ULV,
            NI,
            30 * 100);

        // Metals Line
        addProcessGT(CombType.SLAG, new Materials[] { Materials.Limestone }, Voltage.LV);
        addProcessGT(CombType.COPPER, new Materials[] { Materials.Copper }, Voltage.LV);
        addProcessGT(CombType.TIN, new Materials[] { Materials.Tin }, Voltage.LV);
        addProcessGT(CombType.LEAD, new Materials[] { Materials.Lead }, Voltage.LV);
        addProcessGT(CombType.NICKEL, new Materials[] { Materials.Nickel }, Voltage.LV);
        addProcessGT(CombType.ZINC, new Materials[] { Materials.Zinc }, Voltage.LV);
        addProcessGT(CombType.SILVER, new Materials[] { Materials.Silver }, Voltage.LV);
        addProcessGT(CombType.CRYOLITE, new Materials[] { Materials.Cryolite }, Voltage.LV);
        addProcessGT(CombType.GOLD, new Materials[] { Materials.Gold }, Voltage.LV);
        addProcessGT(CombType.SULFUR, new Materials[] { Materials.Sulfur }, Voltage.LV);
        addProcessGT(CombType.GALLIUM, new Materials[] { Materials.Gallium }, Voltage.LV);
        addProcessGT(CombType.ARSENIC, new Materials[] { Materials.Arsenic }, Voltage.LV);
        addProcessGT(CombType.IRON, new Materials[] { Materials.Iron }, Voltage.LV);
        addProcessGT(CombType.STEEL, new Materials[] { Materials.Steel }, Voltage.LV);
        addProcessGT(CombType.PYRITE, new Materials[] { Materials.Pyrite }, Voltage.LV);

        addCentrifugeToMaterial(
            CombType.SLAG,
            new Materials[] { Materials.Stone, Materials.GraniteBlack, Materials.GraniteRed },
            new int[] { 50 * 100, 20 * 100, 20 * 100 },
            new int[] { 9, 9, 9 },
            Voltage.ULV,
            NI,
            30 * 100);
        addCentrifugeToMaterial(
            CombType.COPPER,
            new Materials[] { Materials.Copper },
            new int[] { 70 * 100 },
            GTValues.emptyIntArray,
            Voltage.ULV,
            NI,
            30 * 100);
        addCentrifugeToMaterial(
            CombType.TIN,
            new Materials[] { Materials.Tin },
            new int[] { 60 * 100 },
            GTValues.emptyIntArray,
            Voltage.ULV,
            NI,
            30 * 100);
        addCentrifugeToMaterial(
            CombType.LEAD,
            new Materials[] { Materials.Lead },
            new int[] { 45 * 100 },
            GTValues.emptyIntArray,
            Voltage.ULV,
            NI,
            30 * 100);
        addCentrifugeToMaterial(
            CombType.IRON,
            new Materials[] { Materials.Iron },
            new int[] { 30 * 100 },
            GTValues.emptyIntArray,
            Voltage.ULV,
            NI,
            30 * 100);
        addCentrifugeToMaterial(
            CombType.STEEL,
            new Materials[] { Materials.Steel },
            new int[] { 40 * 100 },
            GTValues.emptyIntArray,
            Voltage.ULV,
            NI,
            30 * 100);
        addCentrifugeToMaterial(
            CombType.SILVER,
            new Materials[] { Materials.Silver },
            new int[] { 80 * 100 },
            GTValues.emptyIntArray,
            Voltage.ULV,
            NI,
            30 * 100);
        addCentrifugeToMaterial(
            CombType.CRYOLITE,
            new Materials[] { Materials.Cryolite },
            new int[] { 80 * 100 },
            GTValues.emptyIntArray,
            Voltage.ULV,
            NI,
            30 * 100);
        addCentrifugeToMaterial(
            CombType.SULFUR,
            new Materials[] { Materials.Sulfur },
            new int[] { 100 * 100 },
            GTValues.emptyIntArray,
            Voltage.ULV,
            NI,
            30 * 100);

        // Rare Metals Line
        addProcessGT(CombType.BAUXITE, new Materials[] { Materials.Bauxite }, Voltage.LV);
        addProcessGT(CombType.ALUMINIUM, new Materials[] { Materials.Aluminium }, Voltage.LV);
        addProcessGT(CombType.MANGANESE, new Materials[] { Materials.Manganese }, Voltage.LV);
        addProcessGT(CombType.TITANIUM, new Materials[] { Materials.Titanium }, Voltage.EV);
        addProcessGT(CombType.MAGNESIUM, new Materials[] { Materials.Magnesium }, Voltage.LV);
        addProcessGT(CombType.CHROME, new Materials[] { Materials.Chrome }, Voltage.HV);
        addProcessGT(CombType.TUNGSTEN, new Materials[] { Materials.Tungsten }, Voltage.IV);
        addProcessGT(CombType.PLATINUM, new Materials[] { Materials.Platinum }, Voltage.HV);
        addProcessGT(CombType.MOLYBDENUM, new Materials[] { Materials.Molybdenum }, Voltage.LV);
        addProcessGT(CombType.IRIDIUM, new Materials[] { Materials.Iridium }, Voltage.IV);
        addProcessGT(CombType.PALLADIUM, new Materials[] { Materials.Palladium }, Voltage.IV);
        addProcessGT(CombType.OSMIUM, new Materials[] { Materials.Osmium }, Voltage.IV);
        addProcessGT(CombType.NEODYMIUM, new Materials[] { Materials.Neodymium }, Voltage.MV);
        addProcessGT(CombType.LITHIUM, new Materials[] { Materials.Lithium }, Voltage.MV);
        addProcessGT(CombType.ELECTROTINE, new Materials[] { Materials.Electrotine }, Voltage.MV);
        addProcessGT(CombType.DRACONIC, new Materials[] { Materials.Draconium }, Voltage.IV);
        addProcessGT(CombType.AWAKENEDDRACONIUM, new Materials[] { Materials.DraconiumAwakened }, Voltage.ZPM);

        addCentrifugeToItemStack(
            CombType.SALT,
            new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.dust, Materials.Salt, 6),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.RockSalt, 6), ItemList.FR_Wax.get(1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Saltpeter, 6) },
            new int[] { 100 * 100, 100 * 100, 50 * 100, 25 * 100 },
            Voltage.MV,
            160);

        // Radioactive Line
        addProcessGT(CombType.ALMANDINE, new Materials[] { Materials.Almandine }, Voltage.LV);
        addProcessGT(CombType.URANIUM, new Materials[] { Materials.Uranium }, Voltage.EV);
        addProcessGT(CombType.PLUTONIUM, new Materials[] { Materials.Plutonium }, Voltage.EV);
        addProcessGT(CombType.NAQUADAH, new Materials[] { Materials.Naquadah }, Voltage.IV);
        addProcessGT(CombType.NAQUADRIA, new Materials[] { Materials.Naquadria }, Voltage.LuV);
        addProcessGT(CombType.THORIUM, new Materials[] { Materials.Thorium }, Voltage.MV);
        addProcessGT(CombType.LUTETIUM, new Materials[] { Materials.Lutetium }, Voltage.IV);
        addProcessGT(CombType.NEUTRONIUM, new Materials[] { Materials.Neutronium }, Voltage.UHV);

        // Twilight
        addCentrifugeToItemStack(
            CombType.NAGA,
            new ItemStack[] { GTModHandler.getModItem(MagicBees.ID, "propolis", 1L, 4),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "NagaScaleChip", 1L, 0),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "NagaScaleFragment", 1L, 0), ItemList.FR_Wax.get(1) },
            new int[] { 5 * 100, 33 * 100, 8 * 100, 30 * 100 },
            Voltage.MV);
        addCentrifugeToItemStack(
            CombType.LICH,
            new ItemStack[] { GTModHandler.getModItem(MagicBees.ID, "propolis", 1L, 5),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "LichBoneChip", 1L, 0),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "LichBoneFragment", 1L, 0), ItemList.FR_Wax.get(1) },
            new int[] { 5 * 100, 33 * 100, 8 * 100, 30 * 100 },
            Voltage.HV);
        addCentrifugeToItemStack(
            CombType.HYDRA,
            new ItemStack[] { GTModHandler.getModItem(MagicBees.ID, "propolis", 1L, 1),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "FieryBloodDrop", 1L, 0),
                GTBees.drop.getStackForType(DropType.HYDRA), ItemList.FR_Wax.get(1) },
            new int[] { 5 * 100, 33 * 100, 8 * 100, 30 * 100 },
            Voltage.HV);
        addCentrifugeToItemStack(
            CombType.URGHAST,
            new ItemStack[] { GTModHandler.getModItem(MagicBees.ID, "propolis", 1L, 2),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "CarminiteChip", 1L, 0),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "CarminiteFragment", 1L, 0), ItemList.FR_Wax.get(1) },
            new int[] { 5 * 100, 33 * 100, 8 * 100, 30 * 100 },
            Voltage.EV);
        addCentrifugeToItemStack(
            CombType.SNOWQUEEN,
            new ItemStack[] { GTModHandler.getModItem(MagicBees.ID, "propolis", 1L, 3),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "SnowQueenBloodDrop", 1L, 0),
                GTBees.drop.getStackForType(DropType.SNOW_QUEEN), ItemList.FR_Wax.get(1) },
            new int[] { 5 * 100, 33 * 100, 8 * 100, 30 * 100 },
            Voltage.EV);

        // HEE
        addCentrifugeToItemStack(
            CombType.ENDDUST,
            new ItemStack[] { GTModHandler.getModItem(MagicBees.ID, "wax", 1L, 0),
                GTBees.propolis.getStackForType(PropolisType.End), GTBees.drop.getStackForType(DropType.ENDERGOO),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Endstone, 1L) },
            new int[] { 20 * 100, 15 * 100, 10 * 100, 100 * 100 },
            Voltage.HV);
        addCentrifugeToItemStack(
            CombType.STARDUST,
            new ItemStack[] { GTModHandler.getModItem(MagicBees.ID, "wax", 1L, 0),
                GTBees.propolis.getStackForType(PropolisType.Stardust),
                GTBees.drop.getStackForType(DropType.ENDERGOO) },
            new int[] { 20 * 100, 15 * 100, 10 * 100 },
            Voltage.HV);
        addCentrifugeToItemStack(
            CombType.ECTOPLASMA,
            new ItemStack[] { GTModHandler.getModItem(MagicBees.ID, "wax", 1L, 0),
                GTBees.propolis.getStackForType(PropolisType.Ectoplasma),
                GTBees.drop.getStackForType(DropType.ENDERGOO) },
            new int[] { 25 * 100, 10 * 100, 15 * 100 },
            Voltage.EV);
        addCentrifugeToItemStack(
            CombType.ARCANESHARD,
            new ItemStack[] { GTModHandler.getModItem(MagicBees.ID, "wax", 1L, 0),
                GTBees.propolis.getStackForType(PropolisType.Arcaneshard),
                GTBees.drop.getStackForType(DropType.ENDERGOO) },
            new int[] { 25 * 100, 10 * 100, 15 * 100 },
            Voltage.EV);
        addCentrifugeToItemStack(
            CombType.DRAGONESSENCE,
            new ItemStack[] { GTModHandler.getModItem(MagicBees.ID, "wax", 1L, 0),
                GTBees.propolis.getStackForType(PropolisType.Dragonessence),
                GTBees.drop.getStackForType(DropType.ENDERGOO) },
            new int[] { 30 * 100, (int) (7.5 * 100), 20 * 100 },
            Voltage.IV);
        addCentrifugeToItemStack(
            CombType.ENDERMAN,
            new ItemStack[] { GTModHandler.getModItem(MagicBees.ID, "wax", 1L, 0),
                GTBees.propolis.getStackForType(PropolisType.Enderman),
                GTBees.drop.getStackForType(DropType.ENDERGOO) },
            new int[] { 3000, 750, 2000 },
            Voltage.IV);
        addCentrifugeToItemStack(
            CombType.SILVERFISH,
            new ItemStack[] { GTModHandler.getModItem(MagicBees.ID, "wax", 1L, 0),
                GTBees.propolis.getStackForType(PropolisType.Silverfish),
                GTBees.drop.getStackForType(DropType.ENDERGOO), new ItemStack(Items.spawn_egg, 1, 60) },
            new int[] { 25 * 100, 10 * 100, 20 * 100, 15 * 100 },
            Voltage.EV);
        addProcessGT(CombType.ENDIUM, new Materials[] { Materials.Endium }, Voltage.HV);

        addCentrifugeToItemStack(
            CombType.RUNEI,
            new ItemStack[] { GTModHandler.getModItem(MagicBees.ID, "wax", 1L, 0),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "RuneOfPowerFragment", 1L, 0),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "RuneOfAgilityFragment", 1L, 0),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "RuneOfVigorFragment", 1L, 0),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "RuneOfDefenseFragment", 1L, 0),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "RuneOfMagicFragment", 1L, 0) },
            new int[] { 25 * 100, 5 * 100, 5 * 100, 5 * 100, 5 * 100, 5 * 100 },
            Voltage.IV);
        addCentrifugeToItemStack(
            CombType.RUNEII,
            new ItemStack[] { GTModHandler.getModItem(MagicBees.ID, "wax", 1L, 0),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "RuneOfVoidFragment", 1L, 0) },
            new int[] { 50 * 100, (int) (2.5 * 100) },
            Voltage.IV);
        addCentrifugeToItemStack(
            CombType.FIREESSENSE,
            new ItemStack[] { GTModHandler.getModItem(MagicBees.ID, "wax", 1L, 0),
                GTBees.propolis.getStackForType(PropolisType.Fireessence),
                GTBees.drop.getStackForType(DropType.ENDERGOO) },
            new int[] { 30 * 100, (int) (7.5 * 100), 20 * 100 },
            Voltage.IV);
        // Walrus Recipe
        if (AE2FluidCraft.isModLoaded()) {
            addCentrifugeToItemStack(
                CombType.WALRUS,
                new ItemStack[] { GTModHandler.getModItem(AE2FluidCraft.ID, "walrus", 1L, 0) },
                new int[] { 100 * 100 },
                Voltage.LV);
        }
        addCentrifugeToItemStack(
            CombType.MACHINIST,
            new ItemStack[] { GTModHandler.getModItem(Forestry.ID, "royalJelly", 1L, 0),
                GTModHandler.getModItem(Forestry.ID, "pollen", 1L, 0),
                GTModHandler.getModItem(Forestry.ID, "honeyDrop", 1L, 0),
                GTModHandler.getModItem(ExtraBees.ID, "honeyDrop", 1L, 6),
                GTModHandler.getModItem(Forestry.ID, "beeswax", 1L, 0), ItemList.ElectronicsLump.get(1) },
            new int[] { 10 * 100, 10 * 100, 20 * 100, 10 * 100, 20 * 100, 5 * 100 },
            Voltage.ULV);
        // Space Line
        addCentrifugeToItemStack(
            CombType.SPACE,
            new ItemStack[] { ItemList.FR_Wax.get(1L), ItemList.FR_RefractoryWax.get(1L),
                GTBees.drop.getStackForType(DropType.OXYGEN),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "CoinSpace", 1L, 0) },
            new int[] { 50 * 100, 30 * 100, 15 * 100, 5 * 100 },
            Voltage.HV);
        addProcessGT(CombType.METEORICIRON, new Materials[] { Materials.MeteoricIron }, Voltage.HV);
        addProcessGT(CombType.DESH, new Materials[] { Materials.Desh }, Voltage.EV);
        addProcessGT(CombType.LEDOX, new Materials[] { Materials.Ledox }, Voltage.EV);
        addProcessGT(CombType.CALLISTOICE, new Materials[] { Materials.CallistoIce }, Voltage.IV);
        addProcessGT(CombType.MYTRYL, new Materials[] { Materials.Mytryl }, Voltage.IV);
        addProcessGT(CombType.QUANTIUM, new Materials[] { Materials.Quantium }, Voltage.IV);
        addProcessGT(CombType.ORIHARUKON, new Materials[] { Materials.Oriharukon }, Voltage.IV);
        addProcessGT(CombType.INFUSEDGOLD, new Materials[] { Materials.InfusedGold }, Voltage.IV);
        addCentrifugeToMaterial(
            CombType.INFUSEDGOLD,
            new Materials[] { Materials.InfusedGold, Materials.Gold },
            new int[] { 20 * 100, 10 * 100 },
            GTValues.emptyIntArray,
            Voltage.IV,
            200,
            NI,
            10 * 100);
        addProcessGT(CombType.MYSTERIOUSCRYSTAL, new Materials[] { Materials.MysteriousCrystal }, Voltage.LuV);
        addCentrifugeToMaterial(
            CombType.MYSTERIOUSCRYSTAL,
            new Materials[] { Materials.MysteriousCrystal },
            new int[] { 10 * 100, 15 * 100 },
            GTValues.emptyIntArray,
            Voltage.LuV,
            512,
            NI,
            50 * 100);
        addProcessGT(CombType.BLACKPLUTONIUM, new Materials[] { Materials.BlackPlutonium }, Voltage.LuV);
        addProcessGT(CombType.TRINIUM, new Materials[] { Materials.Trinium }, Voltage.ZPM);

        // Planet Line
        addCentrifugeToItemStack(
            CombType.MOON,
            new ItemStack[] { ItemList.FR_Wax.get(1L),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "MoonStoneDust", 1L, 0) },
            new int[] { 50 * 100, 30 * 100 },
            Voltage.MV,
            300);
        addCentrifugeToItemStack(
            CombType.MARS,
            new ItemStack[] { ItemList.FR_Wax.get(1L),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "MarsStoneDust", 1L, 0) },
            new int[] { 50 * 100, 30 * 100 },
            Voltage.HV,
            300);
        addCentrifugeToItemStack(
            CombType.JUPITER,
            new ItemStack[] { GTModHandler.getModItem(NewHorizonsCoreMod.ID, "IoStoneDust", 1L, 0),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "EuropaIceDust", 1L, 0),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "EuropaStoneDust", 1L, 0),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "GanymedeStoneDust", 1L, 0),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "CallistoStoneDust", 1L, 0),
                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.CallistoIce, 1L), ItemList.FR_Wax.get(1L) },
            new int[] { 30 * 100, 30 * 100, 30 * 100, 30 * 100, 30 * 100, 5 * 100, 50 * 100 },
            Voltage.HV,
            300);
        addCentrifugeToItemStack(
            CombType.MERCURY,
            new ItemStack[] { ItemList.FR_Wax.get(1L),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "MercuryCoreDust", 1L, 0),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "MercuryStoneDust", 1L, 0) },
            new int[] { 50 * 100, 30 * 100, 30 * 100 },
            Voltage.EV,
            300);
        addCentrifugeToItemStack(
            CombType.VENUS,
            new ItemStack[] { ItemList.FR_Wax.get(1L),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "VenusStoneDust", 1L, 0) },
            new int[] { 50 * 100, 30 * 100 },
            Voltage.EV,
            300);
        addCentrifugeToItemStack(
            CombType.SATURN,
            new ItemStack[] { ItemList.FR_Wax.get(1L),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "EnceladusStoneDust", 1L, 0),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "TitanStoneDust", 1L, 0) },
            new int[] { 50 * 100, 30 * 100, 30 * 100 },
            Voltage.IV,
            300);
        addCentrifugeToItemStack(
            CombType.URANUS,
            new ItemStack[] { ItemList.FR_Wax.get(1L),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "MirandaStoneDust", 1L, 0),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "OberonStoneDust", 1L, 0) },
            new int[] { 50 * 100, 30 * 100, 30 * 100 },
            Voltage.IV,
            300);
        addCentrifugeToItemStack(
            CombType.NEPTUNE,
            new ItemStack[] { ItemList.FR_Wax.get(1L),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "ProteusStoneDust", 1L, 0),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "TritonStoneDust", 1L, 0) },
            new int[] { 50 * 100, 30 * 100, 30 * 100 },
            Voltage.IV,
            300);
        addCentrifugeToItemStack(
            CombType.PLUTO,
            new ItemStack[] { ItemList.FR_Wax.get(1L),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "PlutoStoneDust", 1L, 0),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "PlutoIceDust", 1L, 0) },
            new int[] { 50 * 100, 30 * 100, 30 * 100 },
            Voltage.LuV,
            300);
        addCentrifugeToItemStack(
            CombType.HAUMEA,
            new ItemStack[] { ItemList.FR_Wax.get(1L),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "HaumeaStoneDust", 1L, 0) },
            new int[] { 50 * 100, 30 * 100 },
            Voltage.LuV,
            300);
        addCentrifugeToItemStack(
            CombType.MAKEMAKE,
            new ItemStack[] { ItemList.FR_Wax.get(1L),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "MakeMakeStoneDust", 1L, 0) },
            new int[] { 50 * 100, 30 * 100 },
            Voltage.LuV,
            300);
        addCentrifugeToItemStack(
            CombType.CENTAURI,
            new ItemStack[] { ItemList.FR_Wax.get(1L),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "CentauriASurfaceDust", 1L, 0),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "CentauriAStoneDust", 1L, 0) },
            new int[] { 50 * 100, 30 * 100, 30 * 100 },
            Voltage.ZPM,
            300);
        addCentrifugeToItemStack(
            CombType.TCETI,
            new ItemStack[] { ItemList.FR_Wax.get(1L),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "TCetiEStoneDust", 1L, 0) },
            new int[] { 50 * 100, 30 * 100 },
            Voltage.ZPM,
            300);
        addCentrifugeToItemStack(
            CombType.BARNARDA,
            new ItemStack[] { ItemList.FR_Wax.get(1L),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "BarnardaEStoneDust", 1L, 0),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "BarnardaFStoneDust", 1L, 0) },
            new int[] { 50 * 100, 30 * 100, 30 * 100 },
            Voltage.ZPM,
            300);
        addCentrifugeToItemStack(
            CombType.VEGA,
            new ItemStack[] { ItemList.FR_Wax.get(1L),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "VegaBStoneDust", 1L, 0) },
            new int[] { 50 * 100, 30 * 100 },
            Voltage.ZPM,
            300);
        if (GalaxySpace.isModLoaded()) {
            addCentrifugeToItemStack(
                CombType.SEAWEED,
                new ItemStack[] { ItemList.FR_Wax.get(1L),
                    GTModHandler.getModItem(GalaxySpace.ID, "tcetiedandelions", 1L, 0) },
                new int[] { 50 * 100, 100 * 100 },
                Voltage.UV,
                100);
        }
        // Infinity Line
        addCentrifugeToMaterial(
            CombType.INFINITYCATALYST,
            new Materials[] { Materials.InfinityCatalyst, Materials.Neutronium },
            new int[] { 25 * 100, 20 * 100 },
            GTValues.emptyIntArray,
            Voltage.ZPM,
            100,
            NI,
            50 * 100);
        // (Noble)gas Line
        addFluidExtractorProcess(CombType.HELIUM, Materials.Helium.getGas(250), Voltage.HV);
        addFluidExtractorProcess(CombType.ARGON, Materials.Argon.getGas(250), Voltage.MV);
        addFluidExtractorProcess(CombType.NITROGEN, Materials.Nitrogen.getGas(500), Voltage.MV);
        addFluidExtractorProcess(CombType.HYDROGEN, Materials.Hydrogen.getGas(500), Voltage.MV);
        addFluidExtractorProcess(CombType.FLUORINE, Materials.Fluorine.getGas(250), Voltage.MV);
        addFluidExtractorProcess(CombType.OXYGEN, Materials.Oxygen.getGas(500), Voltage.MV);
        // Organic part 2, unknown liquid
        // yes, unknowwater. It's not a typo, it's how it is spelled. Stupid game.
        addFluidExtractorProcess(CombType.UNKNOWNLIQUID, FluidRegistry.getFluidStack("unknowwater", 250), Voltage.ZPM);
        /*
         * TODO: update this comment The Centrifuge Recipes for Infused Shards and Nether/End-Shard from the Infused
         * Shard Line are below the NobleGas Lines for Xenon and co. in GT_MachineRecipeLoader.java In Lines 1525
         */
        if (Botania.isModLoaded()) {
            registerBotaniaItems();
        }
    }

    /**
     * Currently only used for CombType.MOLYBDENUM
     *
     * @param circuitNumber should not conflict with addProcessGT
     *
     **/
    public void addAutoclaveProcess(CombType comb, Materials aMaterial, Voltage volt, int circuitNumber) {
        if (GTOreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial, 4) == NI) {
            return;
        }
        GTRecipeBuilder recipeBuilder = GTValues.RA.stdBuilder();
        recipeBuilder.itemInputs(GTUtility.copyAmount(9, getStackForType(comb)))
            .circuit(circuitNumber)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial, 4))
            .fluidInputs(Materials.UUMatter.getFluid(Math.max(1, ((aMaterial.getMass() + volt.getUUAmplifier()) / 10))))
            .duration(((int) (aMaterial.getMass() * 128)) * TICKS)
            .eut(volt.getAutoClaveEnergy());
        if (volt.compareTo(Voltage.HV) > 0) {
            recipeBuilder.requiresCleanRoom();
        }
        recipeBuilder.addTo(autoclaveRecipes);
    }

    public void addFluidExtractorProcess(CombType comb, FluidStack fluid, Voltage volt) {
        if (fluid == null) {
            return;
        }

        int duration = (fluid.getFluid()
            .getDensity() * 128 > 0
                ? (fluid.getFluid()
                    .getDensity() * 100)
                : 128);
        int eut = volt.getSimpleEnergy() / 2;

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, getStackForType(comb)))
            .fluidOutputs(fluid)
            .duration(duration)
            .eut(eut)
            .addTo(fluidExtractionRecipes);
    }

    /**
     * this only adds Chemical and AutoClave process. If you need Centrifuge recipe. use addCentrifugeToMaterial or
     * addCentrifugeToItemStack
     *
     * @param volt      This determine the required Tier of process for this recipes. This decide the required aEU/t,
     *                  progress time, required additional UU-Matter, requirement of cleanRoom, needed fluid stack for
     *                  Chemical.
     * @param aMaterial result of Material that should be generated by this process.
     **/
    public void addProcessGT(CombType comb, Materials[] aMaterial, Voltage volt) {
        ItemStack tComb = getStackForType(comb);
        for (Materials materials : aMaterial) {
            if (GTOreDictUnificator.get(OrePrefixes.crushedPurified, materials, 4) != null) {
                ItemStack combInput;
                ItemStack combOutput;
                FluidStack fluidInput;
                FluidStack fluidOutput;
                int durationTicks;
                int eut;
                boolean requiresCleanroom;
                switch (comb) {
                    case NEUTRONIUM -> {
                        combInput = GTUtility.copyAmount(4, tComb);
                        combOutput = Materials.Neutronium.getNuggets(1);
                        fluidInput = volt.getFluidAccordingToCombTier();
                        fluidOutput = Materials.Neutronium.getMolten(4 * INGOTS);
                        durationTicks = volt.getComplexTime() * 17;
                        eut = volt.getChemicalEnergy();
                        requiresCleanroom = volt.compareTo(Voltage.IV) > 0;
                    }
                    case OSMIUM -> {
                        combInput = GTUtility.copyAmount(4, tComb);
                        combOutput = Materials.Osmium.getNuggets(1);
                        fluidInput = volt.getFluidAccordingToCombTier();
                        fluidOutput = Materials.Osmium.getMolten(2 * INGOTS);
                        durationTicks = volt.getComplexTime() * 17;
                        eut = volt.getChemicalEnergy();
                        requiresCleanroom = volt.compareTo(Voltage.IV) > 0;
                    }
                    case PLATINUM -> {
                        combInput = GTUtility.copyAmount(4, tComb);
                        combOutput = Materials.Platinum.getNuggets(1);
                        fluidInput = volt.getFluidAccordingToCombTier();
                        fluidOutput = Materials.Platinum.getMolten(2 * INGOTS);
                        durationTicks = volt.getComplexTime() * 10;
                        eut = volt.getChemicalEnergy();
                        requiresCleanroom = volt.compareTo(Voltage.HV) > 0;
                    }
                    case IRIDIUM -> {
                        combInput = GTUtility.copyAmount(4, tComb);
                        combOutput = Materials.Iridium.getNuggets(1);
                        fluidInput = volt.getFluidAccordingToCombTier();
                        fluidOutput = Materials.Iridium.getMolten(2 * INGOTS);
                        durationTicks = volt.getComplexTime() * 14;
                        eut = volt.getChemicalEnergy();
                        requiresCleanroom = volt.compareTo(Voltage.EV) > 0;
                    }
                    default -> {
                        combInput = GTUtility.copyAmount(4, tComb);
                        combOutput = GTOreDictUnificator.get(OrePrefixes.crushedPurified, materials, 4);
                        fluidInput = volt.getFluidAccordingToCombTier();
                        fluidOutput = null;
                        durationTicks = volt.getComplexTime();
                        eut = volt.getChemicalEnergy();
                        requiresCleanroom = volt.compareTo(Voltage.IV) > 0;
                    }
                }
                GTRecipeBuilder recipeBuilder = GTValues.RA.stdBuilder();
                recipeBuilder.itemInputs(combInput)
                    .itemOutputs(combOutput)
                    .fluidInputs(fluidInput);
                if (fluidOutput != null) {
                    recipeBuilder.fluidOutputs(fluidOutput);
                }
                recipeBuilder.duration(durationTicks)
                    .eut(eut)
                    .metadata(CLEANROOM, requiresCleanroom)
                    .addTo(UniversalChemical);
            }
        }
    }

    /**
     * this method only adds Centrifuge based on Material. If volt is lower than MV than it will also adds forestry
     * centrifuge recipe.
     *
     * @param comb      BeeComb
     * @param aMaterial resulting Material of processing. can be more than 6. but over 6 will be ignored in Gregtech
     *                  Centrifuge.
     * @param chance    chance to get result, 10000 == 100%
     * @param volt      required Voltage Tier for this recipe, this also affect the duration, amount of UU-Matter, and
     *                  needed liquid type and amount for chemical reactor
     * @param stackSize This parameter can be null, in that case stack size will be just 1. This handle the stackSize of
     *                  the resulting Item, and Also the Type of Item. if this value is multiple of 9, than related
     *                  Material output will be dust, if this value is multiple of 4 than output will be Small dust,
     *                  else the output will be Tiny dust
     * @param beeWax    if this is null, then the comb will product default Bee wax. But if aMaterial is more than 5,
     *                  beeWax will be ignored in Gregtech Centrifuge.
     * @param waxChance have same format like "chance"
     **/
    public void addCentrifugeToMaterial(CombType comb, Materials[] aMaterial, int[] chance, int[] stackSize,
        Voltage volt, ItemStack beeWax, int waxChance) {
        addCentrifugeToMaterial(comb, aMaterial, chance, stackSize, volt, volt.getSimpleTime(), beeWax, waxChance);
    }

    public void addCentrifugeToMaterial(CombType comb, Materials[] aMaterial, int[] chance, int[] stackSize,
        Voltage volt, int duration, ItemStack beeWax, int waxChance) {
        ItemStack[] aOutPut = new ItemStack[aMaterial.length + 1];
        stackSize = Arrays.copyOf(stackSize, aMaterial.length);
        chance = Arrays.copyOf(chance, aOutPut.length);
        chance[chance.length - 1] = waxChance;
        for (int i = 0; i < (aMaterial.length); i++) {
            if (chance[i] == 0) {
                continue;
            }
            if (Math.max(1, stackSize[i]) % 9 == 0) {
                aOutPut[i] = GTOreDictUnificator.get(OrePrefixes.dust, aMaterial[i], (Math.max(1, stackSize[i]) / 9));
            } else if (Math.max(1, stackSize[i]) % 4 == 0) {
                aOutPut[i] = GTOreDictUnificator.get(OrePrefixes.dust, aMaterial[i], (Math.max(1, stackSize[i]) / 4));
            } else {
                aOutPut[i] = GTOreDictUnificator.get(OrePrefixes.dust, aMaterial[i], Math.max(1, stackSize[i]));
            }
        }
        if (beeWax != NI) {
            aOutPut[aOutPut.length - 1] = beeWax;
        } else {
            aOutPut[aOutPut.length - 1] = ItemList.FR_Wax.get(1);
        }

        addCentrifugeToItemStack(comb, aOutPut, chance, volt, duration);
    }

    /**
     * @param volt  required Tier of system. If it's lower than MV, it will also add forestry centrifuge.
     * @param aItem can be more than 6. but Over 6 will be ignored in Gregtech Centrifuge.
     **/
    public void addCentrifugeToItemStack(CombType comb, ItemStack[] aItem, int[] chance, Voltage volt) {
        addCentrifugeToItemStack(comb, aItem, chance, volt, volt.getSimpleTime());
    }

    public void addCentrifugeToItemStack(CombType comb, ItemStack[] aItem, int[] chance, Voltage volt, int duration) {
        ItemStack tComb = getStackForType(comb);
        Builder<ItemStack, Float> Product = new ImmutableMap.Builder<>();
        for (int i = 0; i < aItem.length; i++) {
            if (aItem[i] == NI) {
                continue;
            }
            Product.put(aItem[i], chance[i] / 10000.0f);
        }

        if (volt.compareTo(Voltage.MV) < 0) {
            RecipeManagers.centrifugeManager.addRecipe(40, tComb, Product.build());
        }

        GTValues.RA.stdBuilder()
            .itemInputs(tComb)
            .itemOutputs(aItem)
            .outputChances(chance)
            .duration(duration)
            .eut(volt.getSimpleEnergy())
            .addTo(centrifugeRecipes);
    }

    public void registerOreDict() {
        for (CombType comb : CombType.values()) {
            ItemStack tComb = getStackForType(comb);
            GTOreDictUnificator.registerOre(OrePrefixes.beeComb.getName(), tComb);
            OrePrefixes.beeComb.add(tComb);
            if (comb.voltage != null) GTOreDictUnificator.registerOre("comb" + comb.voltage.name(), tComb);
        }
    }

    public enum Voltage {

        ULV,
        LV,
        MV,
        HV,
        EV,
        IV,
        LuV,
        ZPM,
        UV,
        UHV,
        UEV,
        UIV,
        UMV,
        UXV,
        MAX;

        public int getVoltage() {
            return (int) V[this.ordinal()];
        }

        /** @return aEU/t needed for chemical and autoclave process related to the Tier **/
        public int getVoltageFromEU() {
            return Math.max(GTUtility.log4(2 * this.getVoltage()) - 1, 0);
        }

        /** @return Voltage tier according to EU provided. 0 = ULV, 1 = LV, 2 = MV ... **/
        public int getChemicalEnergy() {
            return this.getVoltage() * 3 / 4;
        }

        public int getAutoClaveEnergy() {
            return (int) ((this.getVoltage() * 3 / 4) * (Math.max(1, GTUtility.powInt(2, 5 - this.ordinal()))));
        }

        /** @return FluidStack needed for chemical process related to the Tier **/
        public FluidStack getComplexChemical() {
            if (this.compareTo(Voltage.MV) < 0) {
                return Materials.HydrofluoricAcid.getFluid((this.compareTo(Voltage.ULV) > 0) ? 1000 : 500);
            } else if (this.compareTo(Voltage.HV) < 0) {
                return GTModHandler.getDistilledWater(1_000);
            } else if (this.compareTo(Voltage.LuV) < 0) {
                return Materials.HydrofluoricAcid
                    .getFluid((long) (GTUtility.powInt(2, this.compareTo(Voltage.HV)) * INGOTS));
            } else if (this.compareTo(Voltage.UHV) < 0) {
                return FluidRegistry
                    .getFluidStack("mutagen", (int) (GTUtility.powInt(2, this.compareTo(Voltage.LuV)) * INGOTS));
            } else {
                return NF;
            }
        }

        /** @return FluidStack needed for chemical process related to the Tier **/
        public FluidStack getFluidAccordingToCombTier() {
            // checking what Voltage tier the Comb is
            // cascading from IV to UMV since all recipes use HydrofluiricAcid
            // for later tiers, just add the corresponding tier to a case
            int fluidAmount = this.getFluidAmount();
            return switch (this.getVoltageFromEU()) {
                case 0 ->
                    /* ULV */
                    Materials.Water.getFluid(fluidAmount);
                case 1 ->
                    /* LV */
                    Materials.SulfuricAcid.getFluid(fluidAmount);
                case 2 ->
                    /* MV */
                    Materials.HydrochloricAcid.getFluid(fluidAmount);
                case 3 ->
                    /* HV */
                    Materials.PhosphoricAcid.getFluid(fluidAmount);
                case 4 ->
                    /* EV */
                    Materials.HydrofluoricAcid.getFluid(this.getFluidAmount());
                default -> Materials.PhthalicAcid.getFluid(fluidAmount);
            };
        }

        /** @return additional required UU-Matter amount for Autoclave process related to the Tier **/
        public int getUUAmplifier() {
            return 9 * ((this.compareTo(Voltage.MV) < 0) ? 1 : this.compareTo(Voltage.MV));
        }

        /** @return duration needed for Chemical process related to the Tier **/
        public int getComplexTime() {
            return 64 + this.ordinal() * 32;
        }

        /** @return Fluid amount needed for Chemical process related to the Tier **/
        public int getFluidAmount() {
            return 9 * this.getSimpleTime() / 3;
        }

        /** @return duration needed for Centrifuge process related to the Tier **/
        public int getSimpleTime() {

            // ULV, LV needs 128ticks, MV need 256 ticks, HV need 384 ticks, EV need 512 ticks, IV need 640 ticks
            return 128 * (Math.max(1, this.ordinal()));

        }

        /** @return aEU/t needed for Centrifuge process related to the Tier **/
        public int getSimpleEnergy() {
            if (this == Voltage.ULV) {
                return 5;
            } else {
                return (this.getVoltage() / 16) * 15;
            }
        }
    }

    public void registerBotaniaItems() {
        // Manasteel -> Manasteel LCR w/Steel
        GTValues.RA.stdBuilder()
            .itemInputs(GTBees.combs.getStackForType(CombType.MANASTEEL, 4))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Manasteel, 1))
            .fluidInputs(Materials.Steel.getMolten(1 * INGOTS))
            .duration(33 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(GTRecipeConstants.UniversalChemical);

        // Manasteel -> Manasteel LCR w/Thaumium
        GTValues.RA.stdBuilder()
            .itemInputs(GTBees.combs.getStackForType(CombType.MANASTEEL, 4))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Manasteel, 4))
            .fluidInputs(Materials.Thaumium.getMolten(2 * INGOTS))
            .duration(33 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(GTRecipeConstants.UniversalChemical);

        // Elven -> Dragonstone Autoclave
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTBees.combs.getStackForType(CombType.ELVEN, 6),
                GTOreDictUnificator.get(OrePrefixes.gemFlawless, Materials.ManaDiamond, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Dragonstone, 1))
            .fluidInputs(Materials.Void.getMolten(1 * EIGHTH_INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(autoclaveRecipes);

        // Elven -> Pixie Dust Dehydrator
        GTValues.RA.stdBuilder()
            .itemInputs(GTBees.combs.getStackForType(CombType.ELVEN, 1))
            .itemOutputs(new ItemStack(ModItems.manaResource, 1, 8))
            .outputChances(50 * 100)
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalDehydratorRecipes);

        // Elven -> Eleven Elementium LCR w/Manasteel
        GTValues.RA.stdBuilder()
            .itemInputs(GTBees.combs.getStackForType(CombType.ELVEN, 4))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.ElvenElementium, 1))
            .fluidInputs(Materials.Manasteel.getMolten(2 * INGOTS))
            .duration(33 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(GTRecipeConstants.UniversalChemical);

        // Elven -> Eleven Elementium LCR w/Shadowmetal
        GTValues.RA.stdBuilder()
            .itemInputs(GTBees.combs.getStackForType(CombType.ELVEN, 4))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.ElvenElementium, 4))
            .fluidInputs(Materials.Shadow.getMolten(2 * INGOTS))
            .duration(33 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(GTRecipeConstants.UniversalChemical);

        // Terrasteel -> Terrasteel LCR
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTBees.combs.getStackForType(CombType.TERRASTEEL, 4),
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Terrasteel, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Terrasteel, 1))
            .fluidInputs(Materials.ElvenElementium.getMolten(4 * INGOTS))
            .outputChances(50 * 100)
            .fluidOutputs(Materials.Terrasteel.getMolten(1 * INGOTS))
            .duration(33 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(GTRecipeConstants.UniversalChemical);

        // Gaia -> Gaia Spirit LCR w/Elementium
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTBees.combs.getStackForType(CombType.GAIASPIRIT, 4),
                new ItemStack(ModItems.manaResource, 4, 8),
                new ItemStack(ModItems.dice, 1))
            .itemOutputs(new ItemStack(ModItems.manaResource, 4, 5))
            .fluidInputs(Materials.ElvenElementium.getMolten(4 * INGOTS))
            .duration(33 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(multiblockChemicalReactorRecipes);

        // Gaia -> Gaia Spirit LCR w/Terrasteel
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTBees.combs.getStackForType(CombType.GAIASPIRIT, 4),
                new ItemStack(ModItems.manaResource, 4, 8),
                new ItemStack(ModItems.dice, 1))
            .itemOutputs(new ItemStack(ModItems.manaResource, 6, 5))
            .fluidInputs(Materials.Terrasteel.getMolten(2 * INGOTS))
            .duration(33 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(multiblockChemicalReactorRecipes);

        // MMM -> Mana Fly
        GTValues.RA.stdBuilder()
            .itemInputs(GTBees.combs.getStackForType(CombType.MMM, 1))
            .itemOutputs(ItemList.ManaFly.get(1))
            .outputChances(80 * 100)
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(centrifugeRecipes);

        // Sifting Mana Bunches
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.ManaFly.get(1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Manasteel, 1),
                new ItemStack(ModItems.manaResource, 1, 16),
                new ItemStack(ModItems.manaResource, 1, 23),
                new ItemStack(ModItems.quartz, 1, 1),
                new ItemStack(ModItems.manaResource, 1, 22),
                new ItemStack(ModItems.manaResource, 1, 1),
                GTOreDictUnificator.get(OrePrefixes.gem, Materials.ManaDiamond, 1),
                new ItemStack(ModItems.manaCookie, 1))
            .outputChances(35 * 100, 15 * 100, 15 * 100, 15 * 100, 15 * 100, 5 * 100, 2 * 100, 1 * 10)
            .duration(20 * SECONDS)
            .fluidInputs(Materials.HydrofluoricAcid.getFluid(250L))
            .eut(TierEU.RECIPE_EV)
            .addTo(sifterRecipes);
    }

    @Override
    @Optional.Method(modid = Mods.ModIDs.RAILCRAFT)
    public boolean shouldBurn(ItemStack itemStack) {
        return itemStack.isItemEqual(getStackForType(CombType.FIRESTONE));
    }
}

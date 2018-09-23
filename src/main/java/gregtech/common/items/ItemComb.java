package gregtech.common.items;

import com.google.common.collect.ImmutableMap;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.core.Tabs;
import forestry.api.recipes.RecipeManagers;
import gregtech.GT_Mod;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gregtech.loaders.materialprocessing.ProcessingModSupport;
import gregtech.loaders.misc.GT_Bees;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.List;

import static gregtech.api.enums.GT_Values.MOD_ID;
import static gregtech.api.enums.GT_Values.MOD_ID_FR;

public class ItemComb extends Item {
	@SideOnly(Side.CLIENT)
	private IIcon secondIcon;

	public ItemComb() {
		super();
		this.setCreativeTab(Tabs.tabApiculture);
		this.setHasSubtypes(true);
		this.setUnlocalizedName("gt.comb");
		GameRegistry.registerItem(this, "gt.comb", MOD_ID);
	}

	public ItemStack getStackForType(CombType type) {
		return new ItemStack(this, 1, type.ordinal());
	}

	public ItemStack getStackForType(CombType type, int count) {
		return new ItemStack(this, count, type.ordinal());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tabs, List list) {
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

	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
		this.itemIcon = par1IconRegister.registerIcon("forestry:beeCombs.0");
		this.secondIcon = par1IconRegister.registerIcon("forestry:beeCombs.1");
	}

	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		return (pass == 0) ? itemIcon : secondIcon;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int pass) {
		int meta = Math.max(0, Math.min(CombType.values().length - 1, stack.getItemDamage()));
		int colour = CombType.values()[meta].getColours()[0];

		if (pass >= 1) {
			colour = CombType.values()[meta].getColours()[1];
		}

		return colour;
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return CombType.values()[stack.getItemDamage()].getName();
	}
	public void initCombsRecipes() {
		ItemStack tComb;

	    //Organic
		tComb = getStackForType(CombType.LIGNIE);
		addSpecialCentLV(tComb,GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Lignite, 1), 90);
		addProcessLV(tComb, Materials.Lignite, 100);
		tComb = getStackForType(CombType.COAL);
		addSpecialCentLV(tComb,GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 1), 5, GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Coal, 1), 100);
		addProcessLV(tComb, Materials.Coal, 100);
		tComb = getStackForType(CombType.STICKY);
		addSpecialCentLV(tComb, ItemList.IC2_Resin.get(1, new Object[0]), 50, ItemList.IC2_Plantball.get(1, new Object[0]), 15);
		tComb = getStackForType(CombType.OIL);
		addSpecialCentLV(tComb, ItemList.Crop_Drop_OilBerry.get(1, new Object[0]), 70, GT_Bees.drop.getStackForType(DropType.OIL), 100);
		addProcessLV(tComb, Materials.Oilsands, 100);

		//ic2
		tComb = getStackForType(CombType.COOLANT);
		addSpecialCentHV(tComb, GT_Bees.drop.getStackForType(DropType.COOLANT), 100, ItemList.FR_Wax.get(1, new Object[0]), 100);
		tComb = getStackForType(CombType.ENERGY);
		addSpecialCentHV(tComb, GT_Bees.drop.getStackForType(DropType.HOT_COOLANT), 20, ItemList.IC2_Energium_Dust.get(1L), 20, ItemList.FR_RefractoryWax.get(1, new Object[0]), 50);
		tComb = getStackForType(CombType.LAPOTRON);
		addSpecialCentHV(tComb, GT_Bees.drop.getStackForType(DropType.LAPIS), 20,  GT_OreDictUnificator.get(OrePrefixes.gemExquisite, Materials.Sapphire, 1), 15, ItemList.FR_Wax.get(1, new Object[0]), 40);

		//Alloy
		tComb = getStackForType(CombType.REDALLOY);
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, ItemList.FR_RefractoryWax.get(1, new Object[0]),GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.RedAlloy, 1), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[] {5000, 10000}, 128, 5);
		addProcessLV(tComb, Materials.Redstone, 75);
		addProcessLV(tComb, Materials.Copper, 90);
		/*tComb = getStackForType(CombType.REDSTONEALLOY);
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, ItemList.FR_RefractoryWax.get(1, new Object[0]),GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.RedstoneAlloy, 1), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[] {5000, 10000}, 128, 5);
		addProcessLV(tComb, Materials.Redstone, 90);
		addProcessLV(tComb, Materials.Silicon, 75);
		addProcessLV(tComb, Materials.Coal, 75);
		tComb = getStackForType(CombType.CONDUCTIVEIRON);
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, ItemList.FR_RefractoryWax.get(1, new Object[0]),GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.ConductiveIron, 1), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[] {5000, 9000}, 256, 120);
		addProcessMV(tComb, Materials.Silver, 55);
		addProcessMV(tComb, Materials.Iron, 65);
		tComb = getStackForType(CombType.VIBRANTALLOY);
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, ItemList.FR_RefractoryWax.get(1, new Object[0]),GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.VibrantAlloy, 1), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[] {5000, 7000}, 384, 480);
		addProcessHV(tComb, Materials.Chrome, 50);
		tComb = getStackForType(CombType.ENERGETICALLOY);
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, ItemList.FR_RefractoryWax.get(1, new Object[0]),GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.VibrantAlloy, 1), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[] {5000, 8000}, 384, 480);
		addProcessHV(tComb, Materials.Gold, 60);
		tComb = getStackForType(CombType.ELECTRICALSTEEL);
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, ItemList.FR_RefractoryWax.get(1, new Object[0]),GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.ElectricalSteel, 1), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[] {5000, 10000}, 128, 5);
		addProcessLV(tComb, Materials.Silicon, 75);
		addProcessLV(tComb, Materials.Coal, 75);
		tComb = getStackForType(CombType.DARKSTEEL);
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, ItemList.FR_RefractoryWax.get(1, new Object[0]),GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkSteel, 1), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[] {5000, 10000}, 256, 120);
		addProcessMV(tComb, Materials.Coal, 75);
		tComb = getStackForType(CombType.PULSATINGIRON);
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, ItemList.FR_RefractoryWax.get(1, new Object[0]),GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.PulsatingIron, 1), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[] {5000, 8000}, 384, 480);
		addProcessHV(tComb, Materials.Iron, 75);*/
		tComb = getStackForType(CombType.STAINLESSSTEEL);
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, ItemList.FR_RefractoryWax.get(1, new Object[0]),GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.StainlessSteel, 1), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[] {5000, 5000}, 384, 480);
		addProcessHV(tComb, Materials.Iron, 75);
		addProcessHV(tComb, Materials.Chrome, 55);
		addProcessHV(tComb, Materials.Manganese, 75);
		addProcessHV(tComb, Materials.Nickel, 75);
		tComb = getStackForType(CombType.ENDERIUM);
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, ItemList.FR_RefractoryWax.get(1, new Object[0]), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.EnderEye, 1), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Enderium, 1), GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[] {5000, 3000, 5000}, 384, 480);


		/*//Thaumic
		tComb = getStackForType(CombType.THAUMIUMDUST);
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_ModHandler.getModItem("MagicBees", "wax", 1, 0), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Thaumium, 1), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[] {5000, 10000}, 256, 120);
		addProcessMV(tComb, Materials.Iron, 75);
		tComb = getStackForType(CombType.THAUMIUMSHARD);
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_ModHandler.getModItem("MagicBees", "propolis", 1, 1), GT_ModHandler.getModItem("MagicBees", "propolis", 1, 2), GT_ModHandler.getModItem("MagicBees", "propolis", 1, 3), GT_ModHandler.getModItem("MagicBees", "propolis", 1, 4), GT_ModHandler.getModItem("MagicBees", "propolis", 1, 5), GT_ModHandler.getModItem("MagicBees", "propolis", 1, 6), new int[] {2000, 2000, 2000, 2000, 2000, 2000 }, 128, 5);
		tComb = getStackForType(CombType.AMBER);
		addProcessLV(tComb, Materials.Amber, 100);
		tComb = getStackForType(CombType.QUICKSILVER);
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_ModHandler.getModItem("MagicBees", "wax", 1, 0), GT_ModHandler.getModItem("Thaumcraft", "ItemNugget", 1, 5), GT_Values.NI,  GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[] {5000, 1000}, 128, 5);
		addProcessLV(tComb, Materials.Cinnabar, 85);
		tComb = getStackForType(CombType.SALISMUNDUS);
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_ModHandler.getModItem("MagicBees", "wax", 1, 0), GT_ModHandler.getModItem("Thaumcraft", "ItemResource", 1, 14), GT_Values.NI,  GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[] {5000, 1000}, 256, 120);
		tComb = getStackForType(CombType.TAINTED);
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_ModHandler.getModItem("Thaumcraft", "ItemResource", 1, 11), GT_ModHandler.getModItem("Thaumcraft", "ItemResource", 1, 12), GT_ModHandler.getModItem("Thaumcraft", "blockTaintFibres", 1, 0),  GT_ModHandler.getModItem("Thaumcraft", "blockTaintFibres", 1, 1), GT_ModHandler.getModItem("Thaumcraft", "blockTaintFibres", 1, 2),  GT_ModHandler.getModItem("MagicBees", "wax", 1, 0), new int[] {1500, 1500, 1500, 1500, 1500, 5000}, 128, 5);
		tComb = getStackForType(CombType.MITHRIL);
		addProcessHV(tComb, Materials.Mithril, 75);
		addProcessHV(tComb, Materials.Platinum, 55);
		tComb = getStackForType(CombType.ASTRALSILVER);
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_ModHandler.getModItem("MagicBees", "wax", 1, 0), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.AstralSilver, 1), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Silver, 1), GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[] {5000, 2000, 1000}, 384, 480);
		addProcessHV(tComb, Materials.Silver, 75);
		tComb = getStackForType(CombType.THAUMINITE);
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_ModHandler.getModItem("MagicBees", "wax", 1, 0), GT_ModHandler.getModItem("thaumicbases", "resource", 1, 0), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Thaumium, 1), GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[] {5000, 2000, 1000}, 384, 480);
		tComb = getStackForType(CombType.SHADOWMETAL);
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_ModHandler.getModItem("MagicBees", "wax", 1, 0), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Shadow, 1), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.ShadowSteel, 1), GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[] {5000, 2000, 1000}, 384, 480);
		addProcessHV(tComb, Materials.ShadowSteel, 75);
		tComb = getStackForType(CombType.DIVIDED);
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_ModHandler.getModItem("MagicBees", "wax", 1, 0), GT_ModHandler.getModItem("ExtraUtilities", "unstableingot", 1, 1), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Iron, 1), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Diamond, 1), GT_Values.NI, GT_Values.NI, new int[] {5000, 2000, 1000, 500}, 384, 480);
		addProcessHV(tComb, Materials.Iron, 75);
		addProcessHV(tComb, Materials.Diamond, 55);
		tComb = getStackForType(CombType.SPARKELING);
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_ModHandler.getModItem("MagicBees", "wax", 1, 0), GT_ModHandler.getModItem("MagicBees", "miscResources", 1, 5), GT_ModHandler.getModItem("MagicBees", "miscResources", 1, 5), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.NetherStar, 1), GT_Values.NI, GT_Values.NI, new int[] {5000, 1000, 500, 1000}, 512, 1920);
		addProcessEV(tComb, Materials.NetherStar, 50);*/

		//Gem Line
		tComb = getStackForType(CombType.STONE);
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1), GT_OreDictUnificator.get(OrePrefixes.dust,Materials.GraniteBlack,1), GT_OreDictUnificator.get(OrePrefixes.dust,Materials.GraniteRed,1),  GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Basalt,1),  GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Marble,1),  GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Redrock,1), new int[] {7000, 5000, 5000, 5000, 5000, 5000}, 128, 5);
		addProcessLV(tComb, Materials.Soapstone, 95);
		addProcessLV(tComb, Materials.Talc, 90);
		addProcessLV(tComb, Materials.Apatite, 80);
		addProcessLV(tComb, Materials.Phosphate, 75);
		addProcessLV(tComb, Materials.Phosphorus, 75);
		tComb = getStackForType(CombType.CERTUS);
		addProcessLV(tComb, Materials.CertusQuartz, 100);
		addProcessLV(tComb, Materials.Quartzite, 80);
		addProcessLV(tComb, Materials.Barite, 75);
		tComb = getStackForType(CombType.FLUIX);
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF,ItemList.FR_Wax.get(1, new Object[0]), GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Fluix,1), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[] {3000, 2500}, 128, 5);
		addProcessLV(tComb, Materials.Redstone, 90);
		addProcessLV(tComb, Materials.CertusQuartz, 90);
		addProcessLV(tComb, Materials.NetherQuartz, 90);
		tComb = getStackForType(CombType.REDSTONE);
		addProcessLV(tComb, Materials.Redstone, 100);
		addProcessLV(tComb, Materials.Cinnabar, 80);
		tComb = getStackForType(CombType.RAREEARTH);
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF,ItemList.FR_Wax.get(1, new Object[0]), GT_OreDictUnificator.get(OrePrefixes.dustTiny,Materials.RareEarth,1), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[] {3000, 10000}, 128, 5);
		tComb = getStackForType(CombType.LAPIS);
		addProcessLV(tComb, Materials.Lapis, 100);
		addProcessLV(tComb, Materials.Sodalite, 90);
		addProcessLV(tComb, Materials.Lazurite, 90);
		addProcessLV(tComb, Materials.Calcite, 85);
		tComb = getStackForType(CombType.RUBY);
		addProcessLV(tComb, Materials.Ruby, 100);
		addProcessLV(tComb, Materials.Redstone, 90);
		tComb = getStackForType(CombType.REDGARNET);
		addProcessLV(tComb, Materials.GarnetRed, 100);
		addProcessLV(tComb, Materials.GarnetYellow, 75);
		tComb = getStackForType(CombType.YELLOWGARNET);
		addProcessLV(tComb, Materials.GarnetYellow, 100);
		addProcessLV(tComb, Materials.GarnetRed, 75);
		tComb = getStackForType(CombType.SAPPHIRE);
		addProcessLV(tComb, Materials.Sapphire, 100);
		addProcessLV(tComb, Materials.GreenSapphire, 90);
		addProcessLV(tComb, Materials.Almandine, 75);
		addProcessLV(tComb, Materials.Pyrope, 75);
		tComb = getStackForType(CombType.DIAMOND);
		addProcessLV(tComb, Materials.Diamond, 100);
		addProcessLV(tComb, Materials.Graphite, 75);
		tComb = getStackForType(CombType.OLIVINE);
		addProcessLV(tComb, Materials.Olivine, 100);
		addProcessLV(tComb, Materials.Bentonite, 90);
		addProcessLV(tComb, Materials.Magnesite, 80);
		addProcessLV(tComb, Materials.Glauconite, 75);
		tComb = getStackForType(CombType.EMERALD);
		addProcessLV(tComb, Materials.Emerald, 100);
		addProcessLV(tComb, Materials.Beryllium, 85);
		addProcessLV(tComb, Materials.Thorium, 75);
		//tComb = getStackForType(CombType.FIRESTONE);
		//addProcessLV(tComb, Materials.Firestone, 100);
		tComb = getStackForType(CombType.PYROPE);
		addProcessLV(tComb, Materials.Pyrope, 100);
		addProcessLV(tComb, Materials.Aluminium, 75);
		addProcessLV(tComb, Materials.Magnesium, 80);
		addProcessLV(tComb, Materials.Silicon, 75);
		tComb = getStackForType(CombType.GROSSULAR);
		addProcessLV(tComb, Materials.Grossular, 100);
		addProcessLV(tComb, Materials.Calcium, 80);
		addProcessLV(tComb, Materials.Aluminium, 75);
		addProcessLV(tComb, Materials.Silicon, 75);
//	    // Metals Line
		tComb = getStackForType(CombType.SLAG);
		addSpecialCentLV(tComb, GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1), 50,GT_OreDictUnificator.get(OrePrefixes.dust, Materials.GraniteBlack, 1), 20,GT_OreDictUnificator.get(OrePrefixes.dust, Materials.GraniteRed, 1), 20);
		addProcessLV(tComb, Materials.Salt, 100);
		addProcessLV(tComb, Materials.RockSalt, 100);
		addProcessLV(tComb, Materials.Lepidolite, 100);
		addProcessLV(tComb, Materials.Spodumene, 100);
		addProcessLV(tComb, Materials.Monazite, 100);
		tComb = getStackForType(CombType.COPPER);
		addSpecialCentLV(tComb, GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Copper, 1), 70);
		addProcessLV(tComb, Materials.Copper, 100);
		addProcessLV(tComb, Materials.Tetrahedrite, 85);
		addProcessLV(tComb, Materials.Chalcopyrite, 95);
		addProcessLV(tComb, Materials.Malachite, 80);
		addProcessLV(tComb, Materials.Pyrite, 75);
		addProcessLV(tComb, Materials.Stibnite, 65);
		tComb = getStackForType(CombType.TIN);
		addSpecialCentLV(tComb, GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Tin, 1), 60);
		addProcessLV(tComb, Materials.Tin, 100);
		addProcessLV(tComb, Materials.Cassiterite, 85);
		tComb = getStackForType(CombType.LEAD);
		addSpecialCentLV(tComb, GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Lead, 1), 45);
		addProcessLV(tComb, Materials.Lead, 100);
		tComb = getStackForType(CombType.IRON);
        addSpecialCentLV(tComb, GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Iron, 1), 30);
		addProcessLV(tComb, Materials.Iron, 100);
		addProcessLV(tComb, Materials.Magnetite, 90);
		addProcessLV(tComb, Materials.BrownLimonite, 85);
		addProcessLV(tComb, Materials.YellowLimonite, 85);
		addProcessLV(tComb, Materials.VanadiumMagnetite, 80);
		addProcessLV(tComb, Materials.BandedIron, 85);
		addProcessLV(tComb, Materials.Pyrite, 80);
		if (ProcessingModSupport.aEnableGCMarsMats)
			addProcessLV(tComb, Materials.MeteoricIron, 75);
		tComb = getStackForType(CombType.STEEL);
		addProcessLV(tComb, Materials.Iron, Materials.Iron, 100);
		addProcessLV(tComb, Materials.Magnetite, Materials.Magnetite, 90);
		addProcessLV(tComb, Materials.BrownLimonite, Materials.BrownLimonite, 85);
		addProcessLV(tComb, Materials.YellowLimonite, Materials.YellowLimonite, 85);
		addProcessLV(tComb, Materials.VanadiumMagnetite, Materials.VanadiumMagnetite, 80);
		addProcessLV(tComb, Materials.BandedIron, Materials.BandedIron, 85);
		addProcessLV(tComb, Materials.Pyrite, Materials.Pyrite, 80);
		if (ProcessingModSupport.aEnableGCMarsMats)
			addProcessLV(tComb, Materials.MeteoricIron, Materials.MeteoricIron, 75);
		addProcessLV(tComb, Materials.Molybdenite, 65);
		addProcessLV(tComb, Materials.Molybdenum, 65);
		tComb = getStackForType(CombType.NICKEL);
		addProcessLV(tComb, Materials.Nickel, 100);
		addProcessLV(tComb, Materials.Garnierite, 85);
		addProcessLV(tComb, Materials.Pentlandite, 85);
		addProcessLV(tComb, Materials.Cobaltite, 80);
		addProcessLV(tComb, Materials.Wulfenite, 75);
		addProcessLV(tComb, Materials.Powellite, 75);
		tComb = getStackForType(CombType.ZINC);
		addProcessLV(tComb, Materials.Zinc, 100);
		addProcessLV(tComb, Materials.Sphalerite, 80);
		addProcessLV(tComb, Materials.Sulfur, 75);
		tComb = getStackForType(CombType.SILVER);
		addSpecialCentLV(tComb, GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Silver, 1), 80);
		addProcessLV(tComb, Materials.Silver, 100);
		addProcessLV(tComb, Materials.Galena, 80);
		tComb = getStackForType(CombType.GOLD);
		addProcessLV(tComb, Materials.Gold, 100);
		addProcessLV(tComb, Materials.Magnetite, Materials.Gold, 80);
		tComb = getStackForType(CombType.SULFUR);
		addProcessLV(tComb, Materials.Sulfur, 100);
        addProcessLV(tComb, Materials.Pyrite, 90);
        addProcessLV(tComb, Materials.Sphalerite, 80);
		tComb = getStackForType(CombType.GALLIUM);
		addProcessLV(tComb, Materials.Gallium, 80);
        addProcessLV(tComb, Materials.Niobium, 75);
		tComb = getStackForType(CombType.ARSENIC);
		addProcessLV(tComb, Materials.Arsenic, 80);
        addProcessLV(tComb, Materials.Bismuth, 70);
        addProcessLV(tComb, Materials.Antimony, 70);

	    // Rare Metals Line
		tComb = getStackForType(CombType.BAUXITE);
		addProcessLV(tComb, Materials.Bauxite, 75);
		addProcessLV(tComb,Materials.Aluminium,55);
		tComb = getStackForType(CombType.ALUMINIUM);
		addProcessLV(tComb,Materials.Aluminium,60);
		addProcessLV(tComb,Materials.Bauxite,80);
		tComb = getStackForType(CombType.MANGANESE);
		addProcessLV(tComb,Materials.Manganese,30);
		addProcessLV(tComb,Materials.Grossular,100);
		addProcessLV(tComb,Materials.Spessartine,100);
		addProcessLV(tComb,Materials.Pyrolusite,100);
		addProcessLV(tComb,Materials.Tantalite,100);
		tComb = getStackForType(CombType.TITANIUM);
		addProcessEV(tComb,Materials.Titanium,90);
        addProcessEV(tComb,Materials.Ilmenite,80);
        addProcessEV(tComb,Materials.Bauxite,75);
		tComb = getStackForType(CombType.CHROME);
		addProcessHV(tComb,Materials.Chrome,50);
        addProcessHV(tComb,Materials.Ruby,100);
        addProcessHV(tComb,Materials.Chromite,50);
        addProcessHV(tComb,Materials.Redstone,100);
        addProcessHV(tComb, Materials.Neodymium, 80);
        addProcessHV(tComb, Materials.Bastnasite, 80);
		tComb = getStackForType(CombType.TUNGSTEN);
		addProcessIV(tComb,Materials.Tungstate,80);
        addProcessIV(tComb,Materials.Scheelite,75);
        addProcessIV(tComb,Materials.Lithium,75);
		tComb = getStackForType(CombType.PLATINUM);
		addProcessHV(tComb,Materials.Platinum,40);
        addProcessHV(tComb,Materials.Cooperite,40);
        addProcessHV(tComb,Materials.Palladium,40);
        tComb = getStackForType(CombType.MOLYBDENUM);
        addProcessLV(tComb,Materials.Molybdenum,100);
        addProcessLV(tComb,Materials.Molybdenite,90);
        addProcessLV(tComb,Materials.Powellite,80);
        addProcessLV(tComb,Materials.Wulfenite,75);
        addProcessIV(tComb,Materials.Osmium,15);
		tComb = getStackForType(CombType.IRIDIUM);
        addProcessIV(tComb,Materials.Iridium,20);
        addProcessIV(tComb,Materials.Osmium,15);
        tComb = getStackForType(CombType.OSMIUM);
        addProcessIV(tComb,Materials.Osmium,25);
        addProcessIV(tComb,Materials.Iridium,15);
        tComb = getStackForType(CombType.LITHIUM);
        addProcessMV(tComb,Materials.Lithium,85);
        addProcessMV(tComb,Materials.Aluminium,75);
        tComb = getStackForType(CombType.SALT);
        addProcessMV(tComb,Materials.Salt,100);
        addProcessMV(tComb,Materials.Sodium,75);
        addProcessMV(tComb,Materials.RockSalt,75);
        addProcessMV(tComb,Materials.Saltpeter,65);
        addSpecialCentMV(tComb, GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Salt, 1), 100);
        /*tComb = getStackForType(CombType.ELECTROTINE);
        addProcessHV(tComb,Materials.Electrotine,80);
        addProcessHV(tComb,Materials.Electrum,75);
        addProcessHV(tComb,Materials.Redstone,65);*/

	    // Radioactive Line
        tComb = getStackForType(CombType.ALMANDINE);
        addProcessLV(tComb,Materials.Almandine,90);
        addProcessLV(tComb,Materials.Pyrope,80);
        addProcessLV(tComb,Materials.Sapphire,75);
        addProcessLV(tComb,Materials.GreenSapphire,75);
		tComb = getStackForType(CombType.URANIUM);
		addProcessEV(tComb,Materials.Uranium,50);
		addProcessEV(tComb,Materials.Pitchblende,65);
		addProcessEV(tComb,Materials.Uraninite,75);
		addProcessEV(tComb,Materials.Uranium235,50);
		tComb = getStackForType(CombType.PLUTONIUM);
		addProcessEV(tComb,Materials.Plutonium,10);
		addProcessEV(tComb,Materials.Uranium235, Materials.Plutonium,5);
		tComb = getStackForType(CombType.NAQUADAH);
		addProcessIV(tComb,Materials.Naquadah,10);
		addProcessIV(tComb,Materials.NaquadahEnriched,5);
		addProcessIV(tComb,Materials.Naquadria,5);
		tComb = getStackForType(CombType.NAQUADRIA);
		addProcessIV(tComb,Materials.Naquadah,15);
		addProcessLUV(tComb,Materials.NaquadahEnriched,10);
		addProcessLUV(tComb,Materials.Naquadria,10);
		tComb = getStackForType(CombType.THORIUM);
		addProcessEV(tComb,Materials.Thorium,75);
		addProcessEV(tComb,Materials.Uranium,75);
		addProcessEV(tComb,Materials.Coal,95);
		tComb = getStackForType(CombType.LUTETIUM);
		addProcessIV(tComb,Materials.Lutetium,35);
		addProcessIV(tComb,Materials.Thorium,55);
		tComb = getStackForType(CombType.AMERICUM);
		addProcessLUV(tComb,Materials.Americium,25);
		addProcessLUV(tComb,Materials.Lutetium,45);
		tComb = getStackForType(CombType.NEUTRONIUM);
		addProcessZPM(tComb,Materials.Neutronium,15);
		addProcessZPM(tComb,Materials.Americium,35);

		/*// Twilight
		tComb = getStackForType(CombType.NAGA);
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, ItemList.FR_Wax.get(1, new Object[0]), GT_ModHandler.getModItem("MagicBees", "propolis", 1L, 4), GT_ModHandler.getModItem("dreamcraft", "item.NagaScaleChip", 1L, 0),  GT_ModHandler.getModItem("dreamcraft", "item.NagaScaleFragment", 1L, 0), GT_Values.NI, GT_Values.NI, new int[]{3000, 500, 3300, 800, 0, 0}, 256, 120);
		tComb = getStackForType(CombType.LICH);
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, ItemList.FR_Wax.get(1, new Object[0]), GT_ModHandler.getModItem("MagicBees", "propolis", 1L, 5), GT_ModHandler.getModItem("dreamcraft", "item.LichBoneChip", 1L, 0), GT_ModHandler.getModItem("dreamcraft", "item.LichBoneFragment", 1L, 0), GT_Values.NI, GT_Values.NI, new int[]{3000, 500, 3300, 800, 0, 0}, 384, 480);
		tComb = getStackForType(CombType.HYDRA);
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, ItemList.FR_Wax.get(1, new Object[0]), GT_ModHandler.getModItem("MagicBees", "propolis", 1L, 1), GT_ModHandler.getModItem("dreamcraft", "item.FieryBloodDrop", 1L, 0), GT_Bees.drop.getStackForType(DropType.HYDRA), GT_Values.NI, GT_Values.NI, new int[]{3000, 500, 3300, 1000, 0, 0}, 384, 480);
		tComb = getStackForType(CombType.URGHAST);
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, ItemList.FR_Wax.get(1, new Object[0]), GT_ModHandler.getModItem("MagicBees", "propolis", 1L, 2), GT_ModHandler.getModItem("dreamcraft", "item..CarminiteChip", 1L, 0), GT_ModHandler.getModItem("dreamcraft", "item.CarminiteFragment",1L, 0), GT_Values.NI, GT_Values.NI, new int[]{3000, 500, 3300, 800, 0, 0}, 512, 1920);
		tComb = getStackForType(CombType.SNOWQUEEN);
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, ItemList.FR_Wax.get(1, new Object[0]), GT_ModHandler.getModItem("MagicBees", "propolis", 1L, 3), GT_ModHandler.getModItem("dreamcraft", "item.SnowQueenBloodDrop", 1L, 0), GT_Bees.drop.getStackForType(DropType.SNOW_QUEEN), GT_Values.NI, GT_Values.NI, new int[]{3000, 500, 3300, 1000, 0, 0}, 512, 1920);*/

		//Space Line
		tComb = getStackForType(CombType.SPACE);
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, ItemList.FR_Wax.get(1L), ItemList.FR_RefractoryWax.get(1L), GT_Bees.drop.getStackForType(DropType.OXYGEN), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Steel, 1), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Titanium, 1), GT_Values.NI, new int[]{5000, 3000, 1500, 1000, 500, 0}, 384, 480);
		tComb = getStackForType(CombType.METEORICIRON);
		addProcessHV(tComb,Materials.MeteoricIron,85);
		addProcessHV(tComb,Materials.Iron,100);
		/*tComb = getStackForType(CombType.DESH);
		addProcessEV(tComb,Materials.Desh,75);
		addProcessEV(tComb,Materials.Titanium,50);
		tComb = getStackForType(CombType.LEDOX);
		addProcessEV(tComb,Materials.Ledox,65);
		addProcessEV(tComb,Materials.CallistoIce,55);
		addProcessEV(tComb,Materials.Lead,85);
		tComb = getStackForType(CombType.CALLISTOICE);
		addProcessIV(tComb,Materials.CallistoIce,65);
		addProcessIV(tComb,Materials.Ledox,75);
		addProcessIV(tComb,Materials.Lead,100);
		tComb = getStackForType(CombType.MYTRYL);
		addProcessIV(tComb,Materials.Mytryl,55);
		addProcessIV(tComb,Materials.Mithril,50);
		tComb = getStackForType(CombType.QUANTIUM);
		addProcessIV(tComb,Materials.Quantium,50);
		addProcessIV(tComb,Materials.Osmium,60);
		tComb = getStackForType(CombType.ORIHARUKON);
		addProcessIV(tComb,Materials.Oriharukon,50);
		addProcessIV(tComb,Materials.Lead,75);
		tComb = getStackForType(CombType.MYSTERIOUSCRYSTAL);
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, ItemList.FR_Wax.get(1L), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.MysteriousCrystal, 1L), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Emerald, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{5000, 1000, 1500, 0, 0, 0}, 512, 30720);
		addProcessLUV(tComb,Materials.Emerald,50);
		tComb = getStackForType(CombType.BLACKPLUTONIUM);
		addProcessLUV(tComb,Materials.BlackPlutonium,25);
		addProcessLUV(tComb,Materials.Plutonium,50);
		tComb = getStackForType(CombType.TRINIUM);
		addProcessZPM(tComb,Materials.Trinium,35);
		addProcessZPM(tComb,Materials.Iridium,45);

		//Planet Line
		tComb = getStackForType(CombType.MOON);
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, ItemList.FR_Wax.get(1L), GT_ModHandler.getModItem("dreamcraft", "item.MoonStoneDust", 1L, 0), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{5000, 3000, 0, 0, 0, 0}, 300, 120);
		tComb = getStackForType(CombType.MARS);
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, ItemList.FR_Wax.get(1L), GT_ModHandler.getModItem("dreamcraft", "item.MarsStoneDust", 1L, 0), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{5000, 3000, 0, 0, 0, 0}, 300, 480);
		tComb = getStackForType(CombType.JUPITER);
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_ModHandler.getModItem("dreamcraft", "item.IoStoneDust", 1L, 0), GT_ModHandler.getModItem("dreamcraft", "item.EuropaIceDust", 1L, 0), GT_ModHandler.getModItem("dreamcraft", "item.EuropaStoneDust", 1L, 0), GT_ModHandler.getModItem("dreamcraft", "item.GanymedStoneDust", 1L, 0), GT_ModHandler.getModItem("dreamcraft", "item.CallistoStoneDust", 1L, 0), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.CallistoIce, 1L), new int[]{3000, 3000, 3000, 3000, 3000, 500}, 300, 480);
		tComb = getStackForType(CombType.MERCURY);
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, ItemList.FR_Wax.get(1L), GT_ModHandler.getModItem("dreamcraft", "item.MercuryCoreDust", 1L, 0), GT_ModHandler.getModItem("dreamcraft", "item.MercuryStoneDust", 1L, 0),  GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{5000, 3000, 3000, 0, 0, 0}, 300, 1920);
		tComb = getStackForType(CombType.VENUS);
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, ItemList.FR_Wax.get(1L), GT_ModHandler.getModItem("dreamcraft", "item.VenusStoneDust", 1L, 0), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{5000, 3000, 0, 0, 0, 0}, 300, 1920);
		tComb = getStackForType(CombType.SATURN);
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, ItemList.FR_Wax.get(1L), GT_ModHandler.getModItem("dreamcraft", "item.EnceladusStoneDust", 1L, 0), GT_ModHandler.getModItem("dreamcraft", "item.TitanStoneDust", 1L, 0), GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{5000, 3000, 3000, 0, 0, 0}, 300, 7680);
		tComb = getStackForType(CombType.URANUS);
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, ItemList.FR_Wax.get(1L), GT_ModHandler.getModItem("dreamcraft", "item.MirandaStoneDust", 1L, 0), GT_ModHandler.getModItem("dreamcraft", "item.OberonStoneDust", 1L, 0), GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{5000, 3000, 3000, 0, 0, 0}, 300, 7680);
		tComb = getStackForType(CombType.NEPTUN);
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, ItemList.FR_Wax.get(1L), GT_ModHandler.getModItem("dreamcraft", "item.ProteusStoneDust", 1L, 0), GT_ModHandler.getModItem("dreamcraft", "item.TritonStoneDust", 1L, 0), GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{5000, 3000, 3000, 0, 0, 0}, 300, 7680);
		tComb = getStackForType(CombType.PLUTO);
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, ItemList.FR_Wax.get(1L), GT_ModHandler.getModItem("dreamcraft", "item.PlutoStoneDust", 1L, 0), GT_ModHandler.getModItem("dreamcraft", "item.PlutoIceDust", 1L, 0), GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{5000, 3000, 3000, 0, 0, 0}, 300, 30720);
		tComb = getStackForType(CombType.HAUMEA);
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, ItemList.FR_Wax.get(1L), GT_ModHandler.getModItem("dreamcraft", "item.HaumeaStoneDust", 1L, 0), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{5000, 3000, 0, 0, 0, 0}, 300, 30720);
		tComb = getStackForType(CombType.MAKEMAKE);
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, ItemList.FR_Wax.get(1L), GT_ModHandler.getModItem("dreamcraft", "item.MakeMakeStoneDust", 1L, 0), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{5000, 3000, 0, 0, 0, 0}, 300, 30720);
		tComb = getStackForType(CombType.CENTAURI);
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, ItemList.FR_Wax.get(1L), GT_ModHandler.getModItem("dreamcraft", "item.CentauriASurfaceDust", 1L, 0), GT_ModHandler.getModItem("dreamcraft", "item.CentauriAStoneDust", 1L, 0), GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{5000, 3000, 3000, 0, 0, 0}, 300, 122880);
		tComb = getStackForType(CombType.TCETI);
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, ItemList.FR_Wax.get(1L), GT_ModHandler.getModItem("dreamcraft", "item.TCetiEStoneDust", 1L, 0), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{5000, 3000, 0, 0, 0, 0}, 300, 122880);
		tComb = getStackForType(CombType.BARNARDA);
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, ItemList.FR_Wax.get(1L), GT_ModHandler.getModItem("dreamcraft", "item.BarnardaEStoneDust", 1L, 0), GT_ModHandler.getModItem("dreamcraft", "item.BarnardaFStoneDust", 1L, 0), GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{5000, 3000, 3000, 0, 0, 0}, 300, 122880);
		tComb = getStackForType(CombType.VEGA);
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, ItemList.FR_Wax.get(1L), GT_ModHandler.getModItem("dreamcraft", "item.VegaBStoneDust", 1L, 0), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{5000, 3000, 0, 0, 0, 0}, 300, 122880);
		//Infinity Line
		tComb = getStackForType(CombType.COSMICNEUTRONIUM);
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, ItemList.FR_Wax.get(1L), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.CosmicNeutronium, 1L), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Neutronium, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{5000, 50, 100, 0, 0, 0}, 12000, 500000);
		tComb = getStackForType(CombType.INFINITYCATALYST);
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, ItemList.FR_Wax.get(1L), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.InfinityCatalyst, 1L), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Neutronium, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{5000, 5, 100, 0, 0, 0}, 24000, 2000000);
		tComb = getStackForType(CombType.INFINITY);
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, ItemList.FR_Wax.get(1L), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Infinity, 1L), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.InfinityCatalyst, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{5000, 1, 5, 0, 0, 0}, 48000, 8000000);*/

	}
	public void addSpecialCentLV(ItemStack tComb, ItemStack aOutput, int chance){
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, aOutput,	ItemList.FR_Wax.get(1, new Object[0]), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[] { chance * 100, 5000 }, 128, 5);
		RecipeManagers.centrifugeManager.addRecipe(40, tComb, ImmutableMap.of(aOutput, chance * 0.01f, ItemList.FR_Wax.get(1, new Object[0]), 0.3f));
	}
	
	public void addSpecialCentLV(ItemStack tComb, ItemStack aOutput, int chance, ItemStack aOutput2, int chance2){
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, aOutput,	ItemList.FR_Wax.get(1, new Object[0]), aOutput2, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[] { chance * 100, 5000, chance2 * 100 }, 128, 5);
		RecipeManagers.centrifugeManager.addRecipe(40, tComb, ImmutableMap.of(aOutput, chance * 0.01f, ItemList.FR_Wax.get(1, new Object[0]), 0.3f,aOutput2,chance2 * 0.01f));
	}
	
	public void addSpecialCentLV(ItemStack tComb, ItemStack aOutput, int chance, ItemStack aOutput2, int chance2, ItemStack aOutput3, int chance3){
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, aOutput,	ItemList.FR_Wax.get(1, new Object[0]), aOutput2, aOutput3, GT_Values.NI, GT_Values.NI, new int[] { chance * 100, 5000, chance2 * 100, chance3*100 }, 128, 5);
		RecipeManagers.centrifugeManager.addRecipe(40, tComb, ImmutableMap.of(aOutput, chance * 0.01f, ItemList.FR_Wax.get(1, new Object[0]), 0.3f,aOutput2,chance2 * 0.01f,aOutput3,chance3*0.01f));
	}
    public void addSpecialCentMV(ItemStack tComb, ItemStack aOutput, int chance){
        GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, aOutput,GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[] { chance * 100, 7000 }, 160, 120);
    }

    public void addSpecialCentMV(ItemStack tComb, ItemStack aOutput, int chance, ItemStack aOutput2, int chance2){
        GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, aOutput,	aOutput2, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[] { chance * 100, chance2 * 100}, 160, 120);
    }

	public void addSpecialCentHV(ItemStack tComb, ItemStack aOutput, int chance){
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, aOutput,GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[] { chance * 100, 7000 }, 196, 480);
		}

	public void addSpecialCentHV(ItemStack tComb, ItemStack aOutput, int chance, ItemStack aOutput2, int chance2){
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, aOutput,	aOutput2, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[] { chance * 100, chance2 * 100}, 196, 480);
	}

	public void addSpecialCentHV(ItemStack tComb, ItemStack aOutput, int chance, ItemStack aOutput2, int chance2, ItemStack aOutput3, int chance3){
		GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, aOutput,	aOutput2, aOutput3, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[] { chance * 100, chance2 * 100, chance3 * 100 }, 196, 480);
	}

	public void addProcessLV(ItemStack tComb, Materials aMaterial, int chance){
		if(GT_Mod.gregtechproxy.mNerfedCombs){
			GT_Values.RA.addChemicalRecipe(GT_Utility.copyAmount(9, tComb), GT_OreDictUnificator.get(OrePrefixes.crushed, aMaterial, 1), Materials.Water.getFluid(1000), aMaterial.mOreByProducts.isEmpty() ? null : aMaterial.mOreByProducts.get(0).getMolten(144), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial, 4), 96, 24);
			GT_Values.RA.addAutoclaveRecipe(GT_Utility.copyAmount(16, tComb), Materials.UUMatter.getFluid(Math.max(1, ((aMaterial.getMass()+9)/10))), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial, 1), 10000, (int) (aMaterial.getMass() * 128), 384);
		}else{
			GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dustTiny, aMaterial, 1),	ItemList.FR_Wax.get(1, new Object[0]), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[] { chance * 100, 3000 }, 128, 5);
			RecipeManagers.centrifugeManager.addRecipe(40, tComb, ImmutableMap.of(GT_OreDictUnificator.get(OrePrefixes.dustTiny, aMaterial, 1), chance * 0.01f, ItemList.FR_Wax.get(1, new Object[0]), 0.3f));
		}
	}
	public void addProcessMV(ItemStack tComb, Materials aMaterial, int chance){
		if(GT_Mod.gregtechproxy.mNerfedCombs){
			GT_Values.RA.addChemicalRecipe(GT_Utility.copyAmount(9, tComb), GT_OreDictUnificator.get(OrePrefixes.crushed, aMaterial, 1), GT_ModHandler.getDistilledWater(1000L), aMaterial.mOreByProducts.isEmpty() ? null : aMaterial.mOreByProducts.get(0).getMolten(144), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial, 4), 128, 96);
			GT_Values.RA.addAutoclaveRecipe(GT_Utility.copyAmount(16, tComb), Materials.UUMatter.getFluid(Math.max(1, ((aMaterial.getMass()+9)/10))), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial, 1), 10000, (int) (aMaterial.getMass() * 128), 768);
		}else{
			GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dustTiny, aMaterial, 1),	ItemList.FR_Wax.get(1, new Object[0]), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[] { chance * 100, 3000 }, 160, 120);
			}
	}
	public void addProcessHV(ItemStack tComb, Materials aMaterial, int chance){
		if(GT_Mod.gregtechproxy.mNerfedCombs){
			GT_Values.RA.addChemicalRecipe(GT_Utility.copyAmount(9, tComb), GT_OreDictUnificator.get(OrePrefixes.crushed, aMaterial, 1), Materials.Mercury.getFluid(144L), aMaterial.mOreByProducts.isEmpty() ? null : aMaterial.mOreByProducts.get(0).getMolten(144), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial, 4), 160, 384);
			GT_Values.RA.addAutoclaveRecipe(GT_Utility.copyAmount(24, tComb), Materials.GrowthMediumRaw.getFluid(Math.max(1, ((aMaterial.getMass()+90)/10))), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial, 1), 10000, (int) (aMaterial.getMass() * 128), 1536);
			GT_Values.RA.addAutoclaveRecipe(GT_Utility.copyAmount(16, tComb), Materials.UUMatter.getFluid(Math.max(1, ((aMaterial.getMass()+9)/10))), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial, 1), 10000, (int) (aMaterial.getMass() * 128), 1536);
		}else{
			GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dustTiny, aMaterial, 1),	ItemList.FR_Wax.get(1, new Object[0]), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[] { chance * 100, 3000 }, 192, 480);
		}
	}
	public void addProcessEV(ItemStack tComb, Materials aMaterial, int chance){
		if(GT_Mod.gregtechproxy.mNerfedCombs){
			GT_Values.RA.addChemicalRecipe(GT_Utility.copyAmount(9, tComb), GT_OreDictUnificator.get(OrePrefixes.crushed, aMaterial, 1), Materials.GrowthMediumRaw.getFluid(144L), aMaterial.mOreByProducts.isEmpty() ? null : aMaterial.mOreByProducts.get(0).getMolten(144), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial, 4), GT_Values.NI,192, 1536, true);
			GT_Values.RA.addAutoclaveRecipe(GT_Utility.copyAmount(24, tComb), Materials.GrowthMediumRaw.getFluid(Math.max(1, ((aMaterial.getMass()+180)/10))), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial, 1), 10000, (int) (aMaterial.getMass() * 128), 3072, true);
			GT_Values.RA.addAutoclaveRecipe(GT_Utility.copyAmount(16, tComb), Materials.UUMatter.getFluid(Math.max(1, ((aMaterial.getMass()+18)/10))), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial, 1), 10000, (int) (aMaterial.getMass() * 128), 3072, true);
		}else{
			GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dustTiny, aMaterial, 1),	ItemList.FR_Wax.get(1, new Object[0]), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[] { chance * 100, 3000 }, 224, 1920);
		}
	}
	public void addProcessIV(ItemStack tComb, Materials aMaterial, int chance){
		if(GT_Mod.gregtechproxy.mNerfedCombs){
			GT_Values.RA.addChemicalRecipe(GT_Utility.copyAmount(9, tComb), GT_OreDictUnificator.get(OrePrefixes.crushed, aMaterial, 1), Materials.GrowthMediumRaw.getFluid(288L), aMaterial.mOreByProducts.isEmpty() ? null : aMaterial.mOreByProducts.get(0).getMolten(144), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial, 4), GT_Values.NI,224, 3072, true);
			GT_Values.RA.addAutoclaveRecipe(GT_Utility.copyAmount(24, tComb), Materials.GrowthMediumRaw.getFluid(Math.max(1, ((aMaterial.getMass()+270)/10))), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial, 1), 10000, (int) (aMaterial.getMass() * 128), 6144, true);
			GT_Values.RA.addAutoclaveRecipe(GT_Utility.copyAmount(16, tComb), Materials.UUMatter.getFluid(Math.max(1, ((aMaterial.getMass()+27)/10))), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial, 1), 10000, (int) (aMaterial.getMass() * 128), 6144, true);
		}else{
			GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dustTiny, aMaterial, 1),	ItemList.FR_Wax.get(1, new Object[0]), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[] { chance * 100, 3000 }, 256, 7680);
		}
	}
	public void addProcessLUV(ItemStack tComb, Materials aMaterial, int chance){
		if(GT_Mod.gregtechproxy.mNerfedCombs){
			GT_Values.RA.addChemicalRecipe(GT_Utility.copyAmount(9, tComb), GT_OreDictUnificator.get(OrePrefixes.crushed, aMaterial, 1), FluidRegistry.getFluidStack("mutagen", 144), aMaterial.mOreByProducts.isEmpty() ? null : aMaterial.mOreByProducts.get(0).getMolten(144), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial, 4), GT_Values.NI,256, 12288, true);
			GT_Values.RA.addAutoclaveRecipe(GT_Utility.copyAmount(24, tComb), Materials.GrowthMediumRaw.getFluid(Math.max(1, ((aMaterial.getMass()+360)/10))), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial, 1), 10000, (int) (aMaterial.getMass() * 128), 24576, true);
			GT_Values.RA.addAutoclaveRecipe(GT_Utility.copyAmount(16, tComb), Materials.UUMatter.getFluid(Math.max(1, ((aMaterial.getMass()+36)/10))), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial, 1), 10000, (int) (aMaterial.getMass() * 128), 24576, true);
		}else{
			GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dustTiny, aMaterial, 1),	ItemList.FR_Wax.get(1, new Object[0]), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[] { chance * 100, 3000 }, 288, 30720);
		}
	}
	public void addProcessZPM(ItemStack tComb, Materials aMaterial, int chance){
		if(GT_Mod.gregtechproxy.mNerfedCombs){
			GT_Values.RA.addChemicalRecipe(GT_Utility.copyAmount(9, tComb), GT_OreDictUnificator.get(OrePrefixes.crushed, aMaterial, 1), FluidRegistry.getFluidStack("mutagen", 288), aMaterial.mOreByProducts.isEmpty() ? null : aMaterial.mOreByProducts.get(0).getMolten(144), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial, 4), GT_Values.NI,288, 500000, true);
			GT_Values.RA.addAutoclaveRecipe(GT_Utility.copyAmount(24, tComb), Materials.GrowthMediumRaw.getFluid(Math.max(1, ((aMaterial.getMass()+450)/10))), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial, 1), 10000, (int) (aMaterial.getMass() * 128), 100000, true);
			GT_Values.RA.addAutoclaveRecipe(GT_Utility.copyAmount(16, tComb), Materials.UUMatter.getFluid(Math.max(1, ((aMaterial.getMass()+45)/10))), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial, 1), 10000, (int) (aMaterial.getMass() * 128), 100000, true);
		}else{
			GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dustTiny, aMaterial, 1),	ItemList.FR_Wax.get(1, new Object[0]), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[] { chance * 100, 3000 }, 320, 122880);
		}
	}

	public void addProcessLV(ItemStack tComb, Materials aInMaterial, Materials aOutMaterial, int chance) {
		if (GT_Mod.gregtechproxy.mNerfedCombs) {
			GT_Values.RA.addChemicalRecipe(GT_Utility.copyAmount(9, tComb), GT_OreDictUnificator.get(OrePrefixes.crushed, aInMaterial, 1), Materials.Water.getFluid(1000L), aInMaterial.mOreByProducts.isEmpty() ? null : aInMaterial.mOreByProducts.get(0).getMolten(144), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aOutMaterial, 4), 96, 24);
			GT_Values.RA.addAutoclaveRecipe(GT_Utility.copyAmount(16, tComb), Materials.UUMatter.getFluid(Math.max(1, ((aOutMaterial.getMass() +9)/10))), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aOutMaterial, 1), 10000, (int) (aOutMaterial.getMass() * 128), 384);
		} else {
			GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dustTiny, aOutMaterial, 1), ItemList.FR_Wax.get(1, new Object[0]), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{chance * 100, 3000}, 128, 5);
			RecipeManagers.centrifugeManager.addRecipe(40, tComb, ImmutableMap.of(GT_OreDictUnificator.get(OrePrefixes.dustTiny, aOutMaterial, 1), chance * 0.01f, ItemList.FR_Wax.get(1, new Object[0]), 0.3f));
		}
	}
		public void addProcessMV(ItemStack tComb, Materials aInMaterial, Materials aOutMaterial, int chance){
			if(GT_Mod.gregtechproxy.mNerfedCombs){
				GT_Values.RA.addChemicalRecipe(GT_Utility.copyAmount(9, tComb), GT_OreDictUnificator.get(OrePrefixes.crushed, aInMaterial, 1), GT_ModHandler.getDistilledWater(1000L), aInMaterial.mOreByProducts.isEmpty() ? null : aInMaterial.mOreByProducts.get(0).getMolten(144), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aOutMaterial, 4), 128, 96);
				GT_Values.RA.addAutoclaveRecipe(GT_Utility.copyAmount(16, tComb), Materials.UUMatter.getFluid(Math.max(1, ((aOutMaterial.getMass()+9)/10))), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aOutMaterial, 1), 10000, (int) (aOutMaterial.getMass() * 128), 768);
			}else{
				GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dustTiny, aOutMaterial, 1),	ItemList.FR_Wax.get(1, new Object[0]), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[] { chance * 100, 3000 }, 160, 120);
			}

	}
	public void addProcessHV(ItemStack tComb, Materials aInMaterial, Materials aOutMaterial, int chance){
		if(GT_Mod.gregtechproxy.mNerfedCombs){
			GT_Values.RA.addChemicalRecipe(GT_Utility.copyAmount(9, tComb), GT_OreDictUnificator.get(OrePrefixes.crushed, aInMaterial, 1), Materials.Mercury.getFluid(144L), aInMaterial.mOreByProducts.isEmpty() ? null : aInMaterial.mOreByProducts.get(0).getMolten(144), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aOutMaterial, 4), 160, 384);
			GT_Values.RA.addAutoclaveRecipe(GT_Utility.copyAmount(24, tComb), Materials.GrowthMediumRaw.getFluid(Math.max(1, ((aOutMaterial.getMass()+90)/10))), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aOutMaterial, 1), 10000, (int) (aOutMaterial.getMass() * 128), 1536);
			GT_Values.RA.addAutoclaveRecipe(GT_Utility.copyAmount(16, tComb), Materials.UUMatter.getFluid(Math.max(1, ((aOutMaterial.getMass()+9)/10))), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aOutMaterial, 1), 10000, (int) (aOutMaterial.getMass() * 128), 1536);
		}else{
			GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dustTiny, aOutMaterial, 1),	ItemList.FR_Wax.get(1, new Object[0]), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[] { chance * 100, 3000 }, 192, 480);
		}

	}
	public void addProcessEV(ItemStack tComb, Materials aInMaterial, Materials aOutMaterial, int chance){
		if(GT_Mod.gregtechproxy.mNerfedCombs){
			GT_Values.RA.addChemicalRecipe(GT_Utility.copyAmount(9, tComb), GT_OreDictUnificator.get(OrePrefixes.crushed, aInMaterial, 1), Materials.GrowthMediumRaw.getFluid(144L), aInMaterial.mOreByProducts.isEmpty() ? null : aInMaterial.mOreByProducts.get(0).getMolten(144), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aOutMaterial, 4), 192, 1536);
			GT_Values.RA.addAutoclaveRecipe(GT_Utility.copyAmount(24, tComb), Materials.GrowthMediumRaw.getFluid(Math.max(1, ((aOutMaterial.getMass()+180)/10))), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aOutMaterial, 1), 10000, (int) (aOutMaterial.getMass() * 128), 3072);
			GT_Values.RA.addAutoclaveRecipe(GT_Utility.copyAmount(16, tComb), Materials.UUMatter.getFluid(Math.max(1, ((aOutMaterial.getMass()+18)/10))), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aOutMaterial, 1), 10000, (int) (aOutMaterial.getMass() * 128), 3072);
		}else{
			GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dustTiny, aOutMaterial, 1),	ItemList.FR_Wax.get(1, new Object[0]), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[] { chance * 100, 3000 }, 224, 1920, true);
		}

	}
	public void addProcessIV(ItemStack tComb, Materials aInMaterial, Materials aOutMaterial, int chance){
		if(GT_Mod.gregtechproxy.mNerfedCombs){
			GT_Values.RA.addChemicalRecipe(GT_Utility.copyAmount(9, tComb), GT_OreDictUnificator.get(OrePrefixes.crushed, aInMaterial, 1), Materials.GrowthMediumRaw.getFluid(288L), aInMaterial.mOreByProducts.isEmpty() ? null : aInMaterial.mOreByProducts.get(0).getMolten(144), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aOutMaterial, 4), 224, 3072);
			GT_Values.RA.addAutoclaveRecipe(GT_Utility.copyAmount(24, tComb), Materials.GrowthMediumRaw.getFluid(Math.max(1, ((aOutMaterial.getMass()+270)/10))), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aOutMaterial, 1), 10000, (int) (aOutMaterial.getMass() * 128), 6144);
			GT_Values.RA.addAutoclaveRecipe(GT_Utility.copyAmount(16, tComb), Materials.UUMatter.getFluid(Math.max(1, ((aOutMaterial.getMass()+27)/10))), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aOutMaterial, 1), 10000, (int) (aOutMaterial.getMass() * 128), 6144);
		}else{
			GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dustTiny, aOutMaterial, 1),	ItemList.FR_Wax.get(1, new Object[0]), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[] { chance * 100, 3000 }, 256, 7680, true);
		}

	}
	public void addProcessLUV(ItemStack tComb, Materials aInMaterial, Materials aOutMaterial, int chance){
		if(GT_Mod.gregtechproxy.mNerfedCombs){
			GT_Values.RA.addChemicalRecipe(GT_Utility.copyAmount(9, tComb), GT_OreDictUnificator.get(OrePrefixes.crushed, aInMaterial, 1), FluidRegistry.getFluidStack("mutagen", 144), aInMaterial.mOreByProducts.isEmpty() ? null : aInMaterial.mOreByProducts.get(0).getMolten(144), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aOutMaterial, 4), GT_Values.NI, 256, 12288, true);
			GT_Values.RA.addAutoclaveRecipe(GT_Utility.copyAmount(24, tComb), Materials.GrowthMediumRaw.getFluid(Math.max(1, ((aOutMaterial.getMass()+360)/10))), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aOutMaterial, 1), 10000, (int) (aOutMaterial.getMass() * 128), 24576, true);
			GT_Values.RA.addAutoclaveRecipe(GT_Utility.copyAmount(16, tComb), Materials.UUMatter.getFluid(Math.max(1, ((aOutMaterial.getMass()+36)/10))), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aOutMaterial, 1), 10000, (int) (aOutMaterial.getMass() * 128), 24576, true);
		}else{
			GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dustTiny, aOutMaterial, 1),	ItemList.FR_Wax.get(1, new Object[0]), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[] { chance * 100, 3000 }, 288, 30720, true);
		}

	}
	public void addProcessZPM(ItemStack tComb, Materials aInMaterial, Materials aOutMaterial, int chance){
		if(GT_Mod.gregtechproxy.mNerfedCombs){
			GT_Values.RA.addChemicalRecipe(GT_Utility.copyAmount(9, tComb), GT_OreDictUnificator.get(OrePrefixes.crushed, aInMaterial, 1), FluidRegistry.getFluidStack("mutagen", 288), aInMaterial.mOreByProducts.isEmpty() ? null : aInMaterial.mOreByProducts.get(0).getMolten(144), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aOutMaterial, 4), GT_Values.NI, 288, 500000, true);
			GT_Values.RA.addAutoclaveRecipe(GT_Utility.copyAmount(24, tComb), Materials.GrowthMediumRaw.getFluid(Math.max(1, ((aOutMaterial.getMass()+450)/10))), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aOutMaterial, 1), 10000, (int) (aOutMaterial.getMass() * 128), 100000, true);
			GT_Values.RA.addAutoclaveRecipe(GT_Utility.copyAmount(16, tComb), Materials.UUMatter.getFluid(Math.max(1, ((aOutMaterial.getMass()+45)/10))), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aOutMaterial, 1), 10000, (int) (aOutMaterial.getMass() * 128), 100000, true);
		}else{
			GT_Values.RA.addCentrifugeRecipe(tComb, GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dustTiny, aOutMaterial, 1),	ItemList.FR_Wax.get(1, new Object[0]), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[] { chance * 100, 3000 }, 320, 122880, true);
		}

	}
}
package gregtech.common.items;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.core.Tabs;
import forestry.api.recipes.RecipeManagers;
import gregtech.GT_Mod;
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
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.Arrays;

import static gregtech.api.enums.GT_Values.MOD_ID;
import static gregtech.api.enums.GT_Values.L;
import static gregtech.api.enums.GT_Values.NF;
import static gregtech.api.enums.GT_Values.NI;
import static gregtech.api.enums.GT_Values.RA;
import static gregtech.api.enums.GT_Values.V;

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

		//Organic
		addProcessGT(CombType.LIGNIE, new Materials[] {Materials.Lignite}, Voltage.LV);
		addProcessGT(CombType.COAL, new Materials[] {Materials.Coal}, Voltage.LV);
		addCentrifugeToItemStack(CombType.STICKY, new ItemStack[] { ItemList.IC2_Resin.get(1), ItemList.IC2_Plantball.get(1), ItemList.FR_Wax.get(1) }, new int[] {50 * 100, 15 * 100, 50 * 100}, Voltage.ULV);
		addProcessGT(CombType.OIL, new Materials[] {Materials.Oilsands}, Voltage.LV);
		addProcessGT(CombType.APATITE, new Materials[] {Materials.Apatite, Materials.Phosphate}, Voltage.LV);
		addCentrifugeToMaterial(CombType.ASH, new Materials[] {Materials.DarkAsh, Materials.Ash}, new int[] { 50*100, 50*100}, new int[] {}, Voltage.ULV, NI, 50 * 100);
		if(GT_Mod.gregtechproxy.mNerfedCombs) {
			addCentrifugeToItemStack(CombType.LIGNIE, new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Lignite, 1), ItemList.FR_Wax.get(1) }, new int[] {90 * 100, 50 * 100}, Voltage.ULV);
			addCentrifugeToItemStack(CombType.COAL, new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 1), ItemList.FR_Wax.get(1) }, new int[] {5 * 100, 50 * 100}, Voltage.ULV);
			addCentrifugeToItemStack(CombType.OIL, new ItemStack[] { ItemList.Crop_Drop_OilBerry.get(1), GT_Bees.drop.getStackForType(DropType.OIL), ItemList.FR_Wax.get(1) }, new int[] {70 * 100, 100 * 100, 50 * 100}, Voltage.ULV);
		}else {
			addCentrifugeToItemStack(CombType.LIGNIE, new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Lignite, 1), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Lignite, 1), ItemList.FR_Wax.get(1) }, new int[] {90 * 100, 100 * 100, 50 * 100}, Voltage.ULV);
			addCentrifugeToItemStack(CombType.COAL, new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 1), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Coal, 1), ItemList.FR_Wax.get(1) }, new int[] {5 * 100, 100 * 100, 50 * 100}, Voltage.ULV);
			addCentrifugeToItemStack(CombType.OIL, new ItemStack[] { ItemList.Crop_Drop_OilBerry.get(1), GT_Bees.drop.getStackForType(DropType.OIL), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Oilsands, 1), ItemList.FR_Wax.get(1) }, new int[] {70 * 100, 100 * 100, 100 * 100, 50 * 100}, Voltage.ULV);
			addCentrifugeToMaterial(CombType.APATITE, new Materials[] { Materials.Apatite, Materials.Phosphate }, new int[] {100 * 100, 80 * 100 }, new int[] {}, Voltage.ULV, NI, 30 * 100);
		}
		
		//ic2
		addCentrifugeToItemStack(CombType.COOLANT, new ItemStack[] { GT_Bees.drop.getStackForType(DropType.COOLANT), ItemList.FR_Wax.get(1) }, new int[] {100 * 100, 100 * 100}, Voltage.HV, 196);
		addCentrifugeToItemStack(CombType.ENERGY, new ItemStack[] {GT_Bees.drop.getStackForType(DropType.HOT_COOLANT), ItemList.IC2_Energium_Dust.get(1L), ItemList.FR_RefractoryWax.get(1)}, new int[] {20 * 100, 20 * 100, 50 * 100}, Voltage.HV, 196);
		addCentrifugeToItemStack(CombType.LAPOTRON, new ItemStack[] {GT_Bees.drop.getStackForType(DropType.LAPIS), GT_ModHandler.getModItem("dreamcraft", "item.LapotronDust", 1, 0), GT_ModHandler.getModItem("MagicBees", "wax", 1, 2) }, new int[] {20 * 100, 15 * 100, 40 * 100}, Voltage.HV, 196);
		addCentrifugeToMaterial(CombType.PYROTHEUM, new Materials[] {Materials.Blaze, Materials.Pyrotheum}, new int[] { 25 * 100, 20 * 100}, new int[] {}, Voltage.HV, NI, 30 * 100);
		addCentrifugeToMaterial(CombType.CRYOTHEUM, new Materials[] {Materials.Blizz, Materials.Cryotheum}, new int[] { 25 * 100, 20 * 100}, new int[] {}, Voltage.HV, NI, 30 * 100);

		//Alloy
		addProcessGT(CombType.REDALLOY, new Materials[] {Materials.RedAlloy, Materials.Redstone, Materials.Copper}, Voltage.LV);
		addProcessGT(CombType.REDSTONEALLOY, new Materials[] {Materials.RedstoneAlloy, Materials.Redstone, Materials.Silicon, Materials.Coal}, Voltage.LV);
		addProcessGT(CombType.CONDUCTIVEIRON, new Materials[] {Materials.ConductiveIron, Materials.Silver, Materials.Iron }, Voltage.MV);
		addProcessGT(CombType.VIBRANTALLOY, new Materials[] {Materials.VibrantAlloy, Materials.Chrome }, Voltage.HV);
		addProcessGT(CombType.ENERGETICALLOY, new Materials[] {Materials.EnergeticAlloy, Materials.Gold }, Voltage.HV);
		addProcessGT(CombType.ELECTRICALSTEEL, new Materials[] {Materials.ElectricalSteel, Materials.Silicon, Materials.Coal }, Voltage.LV);
		addProcessGT(CombType.DARKSTEEL, new Materials[] {Materials.DarkSteel, Materials.Coal }, Voltage.MV);
		addProcessGT(CombType.PULSATINGIRON, new Materials[] {Materials.PulsatingIron, Materials.Iron }, Voltage.HV);
		addProcessGT(CombType.STAINLESSSTEEL, new Materials[] {Materials.StainlessSteel, Materials.Iron, Materials.Chrome, Materials.Manganese, Materials.Nickel }, Voltage.HV);
		addCentrifugeToItemStack(CombType.ENDERIUM, new ItemStack[] {ItemList.FR_RefractoryWax.get(1), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.EnderiumBase, 1), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Enderium, 1) }, new int[] {50 * 100, 30 * 100, 50 * 100 }, Voltage.HV);
		if(GT_Mod.gregtechproxy.mNerfedCombs) {
			addCentrifugeToMaterial(CombType.REDALLOY, new Materials[] {Materials.RedAlloy}, new int[] {100 * 100 }, new int[] {}, Voltage.ULV, ItemList.FR_RefractoryWax.get(1), 50 * 100);
			addCentrifugeToMaterial(CombType.REDSTONEALLOY, new Materials[] {Materials.RedstoneAlloy}, new int[] {100 * 100 }, new int[] {}, Voltage.ULV, ItemList.FR_RefractoryWax.get(1), 50 * 100);
			addCentrifugeToMaterial(CombType.CONDUCTIVEIRON, new Materials[] {Materials.ConductiveIron }, new int[] {90 * 100}, new int[] {}, Voltage.MV, ItemList.FR_RefractoryWax.get(1), 50 * 100);
			addCentrifugeToMaterial(CombType.VIBRANTALLOY, new Materials[] {Materials.VibrantAlloy }, new int[] {70 * 100}, new int[] {}, Voltage.HV, ItemList.FR_RefractoryWax.get(1), 50 * 100);
			addCentrifugeToMaterial(CombType.ENERGETICALLOY, new Materials[] {Materials.EnergeticAlloy }, new int[] {80 * 100}, new int[] {}, Voltage.HV, ItemList.FR_RefractoryWax.get(1), 50 * 100);
			addCentrifugeToMaterial(CombType.ELECTRICALSTEEL, new Materials[] {Materials.ElectricalSteel }, new int[] {100 * 100}, new int[] {}, Voltage.ULV, ItemList.FR_RefractoryWax.get(1), 50 * 100);
			addCentrifugeToMaterial(CombType.DARKSTEEL, new Materials[] {Materials.DarkSteel }, new int[] {100 * 100}, new int[] {}, Voltage.MV, ItemList.FR_RefractoryWax.get(1), 50 * 100);
			addCentrifugeToMaterial(CombType.PULSATINGIRON, new Materials[] {Materials.PulsatingIron }, new int[] {80 * 100}, new int[] {}, Voltage.HV, ItemList.FR_RefractoryWax.get(1), 50 * 100);
			addCentrifugeToMaterial(CombType.STAINLESSSTEEL, new Materials[] {Materials.StainlessSteel }, new int[] {50 * 100}, new int[] {}, Voltage.HV, ItemList.FR_RefractoryWax.get(1), 50 * 100);
		}else {
			addCentrifugeToMaterial(CombType.REDALLOY, new Materials[] {Materials.RedAlloy, Materials.Redstone, Materials.Copper}, new int[] {100 * 100, 75 * 100, 90 * 100 }, new int[] {}, Voltage.ULV, ItemList.FR_RefractoryWax.get(1), 50 * 100);
			addCentrifugeToMaterial(CombType.REDSTONEALLOY, new Materials[] {Materials.RedstoneAlloy, Materials.Redstone, Materials.Silicon, Materials.Coal}, new int[] {100 * 100, 90 * 100, 75 * 100, 75 * 100 }, new int[] {}, Voltage.ULV, ItemList.FR_RefractoryWax.get(1), 50 * 100);
			addCentrifugeToMaterial(CombType.CONDUCTIVEIRON, new Materials[] {Materials.ConductiveIron, Materials.Silver, Materials.Iron }, new int[] {90 * 100, 55 * 100, 65 * 100}, new int[] {}, Voltage.MV, ItemList.FR_RefractoryWax.get(1), 50 * 100);
			addCentrifugeToMaterial(CombType.VIBRANTALLOY, new Materials[] {Materials.VibrantAlloy, Materials.Chrome }, new int[] {70 * 100, 50 * 100}, new int[] {}, Voltage.HV, ItemList.FR_RefractoryWax.get(1), 50 * 100);
			addCentrifugeToMaterial(CombType.ENERGETICALLOY, new Materials[] {Materials.EnergeticAlloy, Materials.Gold }, new int[] {80 * 100, 60 * 100}, new int[] {}, Voltage.HV, ItemList.FR_RefractoryWax.get(1), 50 * 100);
			addCentrifugeToMaterial(CombType.ELECTRICALSTEEL, new Materials[] {Materials.ElectricalSteel, Materials.Silicon, Materials.Coal }, new int[] {100 * 100, 75 * 100, 75 * 100}, new int[] {}, Voltage.ULV, ItemList.FR_RefractoryWax.get(1), 50 * 100);
			addCentrifugeToMaterial(CombType.DARKSTEEL, new Materials[] {Materials.DarkSteel, Materials.Coal }, new int[] {100 * 100, 75 * 100 }, new int[] {}, Voltage.MV, ItemList.FR_RefractoryWax.get(1), 50 * 100);
			addCentrifugeToMaterial(CombType.PULSATINGIRON, new Materials[] {Materials.PulsatingIron, Materials.Iron }, new int[] {80 * 100, 75 * 100 }, new int[] {}, Voltage.HV, ItemList.FR_RefractoryWax.get(1), 50 * 100);
			addCentrifugeToMaterial(CombType.STAINLESSSTEEL, new Materials[] {Materials.StainlessSteel, Materials.Iron, Materials.Chrome, Materials.Manganese, Materials.Nickel }, new int[] {50 * 100, 75 * 100, 55 * 100, 75 * 100, 75 * 100 }, new int[] {}, Voltage.HV, ItemList.FR_RefractoryWax.get(1), 50 * 100);
		}
		
		//Thaumic
		addProcessGT(CombType.THAUMIUMDUST, new Materials[] {Materials.Thaumium, Materials.Iron }, Voltage.MV);
		addCentrifugeToItemStack(CombType.THAUMIUMSHARD, new ItemStack[] {GT_ModHandler.getModItem("MagicBees", "propolis", 1, 1), GT_ModHandler.getModItem("MagicBees", "propolis", 1, 2), GT_ModHandler.getModItem("MagicBees", "propolis", 1, 3), GT_ModHandler.getModItem("MagicBees", "propolis", 1, 4), GT_ModHandler.getModItem("MagicBees", "propolis", 1, 5), GT_ModHandler.getModItem("MagicBees", "propolis", 1, 6), GT_ModHandler.getModItem("MagicBees", "wax", 1, 0) }, new int[] {20 * 100, 20 * 100, 20 * 100, 20 * 100, 20 * 100, 20 * 100, 50 * 100 }, Voltage.ULV);
		addProcessGT(CombType.AMBER, new Materials[] {Materials.Amber}, Voltage.LV);
		addProcessGT(CombType.QUICKSILVER, new Materials[] {Materials.Cinnabar }, Voltage.LV);
		addCentrifugeToItemStack(CombType.SALISMUNDUS, new ItemStack[] {GT_ModHandler.getModItem("Thaumcraft", "ItemResource", 1, 14), GT_ModHandler.getModItem("MagicBees", "wax", 1, 0)}, new int[] {100 * 100, 50 * 100}, Voltage.MV);
		addCentrifugeToItemStack(CombType.TAINTED, new ItemStack[] {GT_ModHandler.getModItem("Thaumcraft", "ItemResource", 1, 11), GT_ModHandler.getModItem("Thaumcraft", "ItemResource", 1, 12), GT_ModHandler.getModItem("Thaumcraft", "blockTaintFibres", 1, 0),  GT_ModHandler.getModItem("Thaumcraft", "blockTaintFibres", 1, 1), GT_ModHandler.getModItem("Thaumcraft", "blockTaintFibres", 1, 2), GT_ModHandler.getModItem("MagicBees", "wax", 1, 0)}, new int[] {15 * 100, 15 * 100, 15 * 100, 15 * 100, 15 * 100, 50 * 100}, Voltage.ULV);
		addProcessGT(CombType.MITHRIL, new Materials[] {Materials.Mithril, Materials.Platinum }, Voltage.HV);
		addProcessGT(CombType.ASTRALSILVER, new Materials[] {Materials.AstralSilver, Materials.Silver }, Voltage.HV);
		addCentrifugeToMaterial(CombType.ASTRALSILVER, new Materials[] {Materials.AstralSilver, Materials.Silver}, new int[] {20 * 100, (GT_Mod.gregtechproxy.mNerfedCombs ? 10:75) * 100}, new int[] {}, Voltage.HV, GT_ModHandler.getModItem("MagicBees", "wax", 1, 0), 50 * 100);
		addCentrifugeToItemStack(CombType.THAUMINITE, new ItemStack[] {GT_ModHandler.getModItem("thaumicbases", "resource", 1, 0), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Thaumium, 1), GT_ModHandler.getModItem("MagicBees", "wax", 1, 0)}, new int[] {20 * 100, 10 * 100, 50 *100}, Voltage.HV);
		addProcessGT(CombType.SHADOWMETAL, new Materials[] {Materials.Shadow, Materials.ShadowSteel }, Voltage.HV);
		addCentrifugeToMaterial(CombType.SHADOWMETAL, new Materials[] {Materials.Shadow, Materials.ShadowSteel}, new int[] {(GT_Mod.gregtechproxy.mNerfedCombs ? 20:75) * 100, 10 * 100}, new int[] {}, Voltage.HV, GT_ModHandler.getModItem("MagicBees", "wax", 1, 0) , 50 * 100);
		addProcessGT(CombType.DIVIDED, new Materials[] {Materials.Iron, Materials.Diamond }, Voltage.HV);
		addCentrifugeToItemStack(CombType.DIVIDED, new ItemStack[] { GT_ModHandler.getModItem("MagicBees", "wax", 1, 0), GT_ModHandler.getModItem("ExtraUtilities", "unstableingot", 1, 1), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Iron, 1), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Diamond, 1)}, new int[] {50 * 100, 20 * 100, (GT_Mod.gregtechproxy.mNerfedCombs ? 10:75) * 100, (GT_Mod.gregtechproxy.mNerfedCombs ? 5:55) * 100}, Voltage.HV);
		addProcessGT(CombType.SPARKELING, new Materials[] {Materials.NetherStar}, Voltage.EV);
		addCentrifugeToItemStack(CombType.SPARKELING, new ItemStack[] { GT_ModHandler.getModItem("MagicBees", "wax", 1, 0), GT_ModHandler.getModItem("MagicBees", "miscResources", 2, 5), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.NetherStar, 1)}, new int[] {50 * 100, 10 * 100, (GT_Mod.gregtechproxy.mNerfedCombs ? 10:50) * 100}, Voltage.EV);
		if(GT_Mod.gregtechproxy.mNerfedCombs) {
			addCentrifugeToMaterial(CombType.THAUMIUMDUST, new Materials[] {Materials.Thaumium }, new int[] {100 * 100}, new int[] {}, Voltage.MV, GT_ModHandler.getModItem("MagicBees", "wax", 1, 0), 50 * 100);
			addCentrifugeToItemStack(CombType.QUICKSILVER, new ItemStack[] {GT_ModHandler.getModItem("MagicBees", "wax", 1, 0), GT_ModHandler.getModItem("Thaumcraft", "ItemNugget", 1, 5) }, new int[] {50 * 100, 100 * 100 }, Voltage.ULV);
		}else {
			addCentrifugeToMaterial(CombType.THAUMIUMDUST, new Materials[] {Materials.Thaumium, Materials.Iron }, new int[] {100 * 100, 75 * 100 }, new int[] {}, Voltage.MV, GT_ModHandler.getModItem("MagicBees", "wax", 1, 0), 50 * 100);
			addCentrifugeToMaterial(CombType.AMBER, new Materials[] {Materials.Amber }, new int[] {100 * 100 }, new int[] {}, Voltage.ULV, GT_ModHandler.getModItem("MagicBees", "wax", 1, 0), 50 * 100);
			addCentrifugeToItemStack(CombType.QUICKSILVER, new ItemStack[] {GT_ModHandler.getModItem("MagicBees", "wax", 1, 0), GT_ModHandler.getModItem("Thaumcraft", "ItemNugget", 1, 5), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Cinnabar, 1) }, new int[] {50 * 100, 100 * 100, 85 * 100 }, Voltage.ULV);
			addCentrifugeToMaterial(CombType.MITHRIL, new Materials[] {Materials.Mithril, Materials.Platinum}, new int[] {75 * 100, 55 * 100}, new int[] {}, Voltage.HV, GT_ModHandler.getModItem("MagicBees", "wax", 1, 0), 50 * 100);
		}
		
		//Gem Line
		addProcessGT(CombType.STONE, new Materials[] {Materials.Soapstone, Materials.Talc, Materials.Apatite, Materials.Phosphate, Materials.TricalciumPhosphate}, Voltage.LV);
		addProcessGT(CombType.CERTUS, new Materials[] {Materials.CertusQuartz, Materials.Quartzite, Materials.Barite}, Voltage.LV);
		addProcessGT(CombType.FLUIX, new Materials[] {Materials.Redstone, Materials.CertusQuartz, Materials.NetherQuartz}, Voltage.LV);
		addProcessGT(CombType.REDSTONE, new Materials[] {Materials.Redstone, Materials.Cinnabar}, Voltage.LV);
		addCentrifugeToMaterial(CombType.RAREEARTH, new Materials[] {Materials.RareEarth}, new int[] {100 * 100}, new int[] { 1}, Voltage.ULV, NI, 30 * 100);
		addProcessGT(CombType.LAPIS, new Materials[] {Materials.Lapis, Materials.Sodalite, Materials.Lazurite, Materials.Calcite }, Voltage.LV);
		addProcessGT(CombType.RUBY, new Materials[] {Materials.Ruby, Materials.Redstone }, Voltage.LV);
		addProcessGT(CombType.REDGARNET, new Materials[] {Materials.GarnetRed, Materials.GarnetYellow }, Voltage.LV);
		addProcessGT(CombType.YELLOWGARNET, new Materials[] {Materials.GarnetYellow, Materials.GarnetRed }, Voltage.LV);
		addProcessGT(CombType.SAPPHIRE, new Materials[] {Materials.Sapphire, Materials.GreenSapphire, Materials.Almandine, Materials.Pyrope}, Voltage.LV);
		addProcessGT(CombType.DIAMOND, new Materials[] {Materials.Diamond, Materials.Graphite}, Voltage.LV);
		addProcessGT(CombType.OLIVINE, new Materials[] {Materials.Olivine, Materials.Bentonite, Materials.Magnesite, Materials.Glauconite}, Voltage.LV);
		addProcessGT(CombType.EMERALD, new Materials[] {Materials.Emerald, Materials.Beryllium, Materials.Thorium}, Voltage.LV);
		addProcessGT(CombType.FIRESTONE, new Materials[] {Materials.Firestone}, Voltage.LV);
		addProcessGT(CombType.PYROPE, new Materials[] {Materials.Pyrope, Materials.Aluminium, Materials.Magnesium, Materials.Silicon}, Voltage.LV);
		addProcessGT(CombType.GROSSULAR, new Materials[] {Materials.Grossular, Materials.Aluminium, Materials.Silicon}, Voltage.LV);
		if(GT_Mod.gregtechproxy.mNerfedCombs) {
			addCentrifugeToMaterial(CombType.STONE, new Materials[] {Materials.Stone, Materials.GraniteBlack, Materials.GraniteRed, Materials.Basalt, Materials.Marble, Materials.Redrock}, new int[] {70 * 100, 50 * 100, 50 * 100, 50 * 100, 50 * 100, 50 * 100}, new int[] { 9, 9, 9, 9, 9, 9}, Voltage.ULV, NI, 50 * 100);
			addCentrifugeToMaterial(CombType.FLUIX, new Materials[] {Materials.Fluix}, new int[] {25 * 100}, new int[] { 9}, Voltage.ULV, NI, 30 * 100);
		}else {
			addCentrifugeToMaterial(CombType.STONE, new Materials[] {Materials.Soapstone, Materials.Talc, Materials.Apatite, Materials.Phosphate, Materials.TricalciumPhosphate}, new int[] {95 * 100, 90 * 100, 80 * 100, 75 * 100, 75 * 100}, new int[] {}, Voltage.ULV, NI, 50 * 100);
			addCentrifugeToMaterial(CombType.CERTUS, new Materials[] {Materials.CertusQuartz, Materials.Quartzite, Materials.Barite}, new int[] {100 * 100, 80 * 100, 75 * 100}, new int[] {}, Voltage.ULV, NI, 50 * 100);
			addCentrifugeToMaterial(CombType.FLUIX, new Materials[] {Materials.Fluix, Materials.Redstone, Materials.CertusQuartz, Materials.NetherQuartz}, new int[] {25 * 100, 90 * 100, 90 * 100, 90 * 100}, new int[] { 9, 1, 1, 1}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(CombType.REDSTONE, new Materials[] {Materials.Redstone, Materials.Cinnabar }, new int[] {100 * 100, 80 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(CombType.LAPIS, new Materials[] {Materials.Lapis, Materials.Sodalite, Materials.Lazurite, Materials.Calcite }, new int[] {100 * 100, 90 * 100, 90 * 100, 85 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(CombType.RUBY, new Materials[] {Materials.Ruby, Materials.Redstone }, new int[] {100 * 100, 90 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(CombType.REDGARNET, new Materials[] {Materials.GarnetRed, Materials.GarnetYellow }, new int[] {100 * 100, 75 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(CombType.YELLOWGARNET, new Materials[] {Materials.GarnetYellow, Materials.GarnetRed }, new int[] {100 * 100, 75 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(CombType.SAPPHIRE, new Materials[] {Materials.Sapphire, Materials.GreenSapphire, Materials.Almandine, Materials.Pyrope}, new int[] {100 * 100, 90 * 100, 90 * 100, 75 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(CombType.DIAMOND, new Materials[] {Materials.Diamond, Materials.Graphite}, new int[] {100 * 100, 75 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(CombType.OLIVINE, new Materials[] {Materials.Olivine, Materials.Bentonite, Materials.Magnesite, Materials.Glauconite}, new int[] {100 * 100, 90 * 100, 80 * 100, 75 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(CombType.EMERALD, new Materials[] {Materials.Emerald, Materials.Beryllium, Materials.Thorium}, new int[] {100 * 100, 85 * 100, 75 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(CombType.FIRESTONE, new Materials[] {Materials.Firestone}, new int[] {100 * 100}, new int[] {}, Voltage.ULV, ItemList.FR_RefractoryWax.get(1), 30 * 100);
			addCentrifugeToMaterial(CombType.PYROPE, new Materials[] {Materials.Pyrope, Materials.Aluminium, Materials.Magnesium, Materials.Silicon}, new int[] {100 * 100, 75 * 100, 80 * 100, 75 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(CombType.GROSSULAR, new Materials[] {Materials.Grossular, Materials.Aluminium, Materials.Silicon}, new int[] {100 * 100, 75 * 100, 75 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
		}
		
		// Metals Line
		addProcessGT(CombType.SLAG, new Materials[] {Materials.Salt, Materials.RockSalt, Materials.Lepidolite, Materials.Spodumene, Materials.Monazite}, Voltage.LV);
		addProcessGT(CombType.COPPER, new Materials[] {Materials.Copper, Materials.Tetrahedrite, Materials.Chalcopyrite, Materials.Malachite, Materials.Pyrite, Materials.Stibnite}, Voltage.LV);
		addProcessGT(CombType.TIN, new Materials[] {Materials.Tin, Materials.Cassiterite, Materials.CassiteriteSand}, Voltage.LV);
		addProcessGT(CombType.LEAD, new Materials[] {Materials.Lead, Materials.Galena}, Voltage.LV);
		addProcessGT(CombType.NICKEL, new Materials[] {Materials.Nickel, Materials.Garnierite, Materials.Pentlandite, Materials.Cobaltite, Materials.Wulfenite, Materials.Powellite}, Voltage.LV);
		addProcessGT(CombType.ZINC, new Materials[] {Materials.Zinc, Materials.Sulfur}, Voltage.LV);
		addProcessGT(CombType.SILVER, new Materials[] {Materials.Silver, Materials.Galena}, Voltage.LV);
		addProcessGT(CombType.GOLD, new Materials[] {Materials.Gold, Materials.Magnetite}, Voltage.LV);
		addChemicalProcess(CombType.GOLD, Materials.Magnetite, Materials.Gold, Voltage.LV);
		addProcessGT(CombType.SULFUR, new Materials[] {Materials.Sulfur, Materials.Pyrite, Materials.Sphalerite}, Voltage.LV);
		addProcessGT(CombType.GALLIUM, new Materials[] {Materials.Gallium, Materials.Niobium}, Voltage.LV);
		addProcessGT(CombType.ARSENIC, new Materials[] {Materials.Arsenic, Materials.Bismuth, Materials.Antimony}, Voltage.LV);
		if (ProcessingModSupport.aEnableGCMarsMats) {
			addProcessGT(CombType.IRON, new Materials[] {Materials.Iron, Materials.Magnetite, Materials.BrownLimonite, Materials.YellowLimonite, Materials.VanadiumMagnetite, Materials.BandedIron, Materials.Pyrite, Materials.MeteoricIron}, Voltage.LV);
			addProcessGT(CombType.STEEL, new Materials[] {Materials.Iron, Materials.Magnetite, Materials.YellowLimonite, Materials.BrownLimonite, Materials.VanadiumMagnetite, Materials.BandedIron, Materials.Pyrite, Materials.MeteoricIron, Materials.Molybdenite, Materials.Molybdenum}, Voltage.LV);
		}else {
			addProcessGT(CombType.IRON, new Materials[] {Materials.Iron, Materials.Magnetite, Materials.BrownLimonite, Materials.YellowLimonite, Materials.VanadiumMagnetite, Materials.BandedIron, Materials.Pyrite}, Voltage.LV);
			addProcessGT(CombType.STEEL, new Materials[] {Materials.Iron, Materials.Magnetite, Materials.YellowLimonite, Materials.BrownLimonite, Materials.VanadiumMagnetite, Materials.BandedIron, Materials.Pyrite, Materials.Molybdenite, Materials.Molybdenum}, Voltage.LV);
		}
		addChemicalProcess(CombType.STEEL, Materials.BrownLimonite, Materials.YellowLimonite, Voltage.LV);
		addChemicalProcess(CombType.STEEL, Materials.YellowLimonite, Materials.BrownLimonite, Voltage.LV);
		if(GT_Mod.gregtechproxy.mNerfedCombs) {
			addCentrifugeToMaterial(CombType.SLAG, new Materials[] {Materials.Stone, Materials.GraniteBlack, Materials.GraniteRed}, new int[] {50 * 100, 20 * 100, 20 * 100}, new int[] { 9, 9, 9}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(CombType.COPPER, new Materials[] {Materials.Copper}, new int[] {70 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(CombType.TIN, new Materials[] {Materials.Tin}, new int[] {60 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(CombType.LEAD, new Materials[] {Materials.Lead}, new int[] {45 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(CombType.IRON, new Materials[] {Materials.Iron}, new int[] {30 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(CombType.STEEL, new Materials[] {Materials.Steel}, new int[] {40 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(CombType.SILVER, new Materials[] {Materials.Silver}, new int[] {80 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
		}else {
			addCentrifugeToMaterial(CombType.SLAG, new Materials[] {Materials.Salt, Materials.RockSalt, Materials.Lepidolite, Materials.Spodumene, Materials.Monazite}, new int[] {100 * 100, 100 * 100, 100 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(CombType.COPPER, new Materials[] {Materials.Copper, Materials.Tetrahedrite, Materials.Chalcopyrite, Materials.Malachite, Materials.Pyrite, Materials.Stibnite}, new int[] {100 * 100, 85 * 100, 95 * 100, 80 * 100, 75 * 100, 65 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(CombType.TIN, new Materials[] {Materials.Tin, Materials.Cassiterite, Materials.CassiteriteSand}, new int[] {100 * 100, 85 * 100, 65 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(CombType.LEAD, new Materials[] {Materials.Lead, Materials.Galena}, new int[] {100 * 100, 75 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			if (ProcessingModSupport.aEnableGCMarsMats) {
				addCentrifugeToMaterial(CombType.IRON, new Materials[] {Materials.Iron, Materials.Magnetite, Materials.BrownLimonite, Materials.YellowLimonite, Materials.VanadiumMagnetite, Materials.MeteoricIron}, new int[] {100 * 100, 90 * 100, 85 * 100, 85 * 100, 80 * 100, 75 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
				addCentrifugeToMaterial(CombType.LEAD, new Materials[] {Materials.Steel, Materials.Magnetite, Materials.VanadiumMagnetite, Materials.Molybdenite, Materials.Molybdenum, Materials.MeteoricIron }, new int[] {100 * 100, 90 * 100, 80 * 100, 65 * 100, 65 * 100, 75 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			}else {
				addCentrifugeToMaterial(CombType.IRON, new Materials[] {Materials.Iron, Materials.Magnetite, Materials.BrownLimonite, Materials.YellowLimonite, Materials.VanadiumMagnetite, Materials.BandedIron}, new int[] {100 * 100, 90 * 100, 85 * 100, 85 * 100, 80 * 100, 85 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
				addCentrifugeToMaterial(CombType.STEEL, new Materials[] {Materials.Steel, Materials.Magnetite, Materials.VanadiumMagnetite, Materials.BandedIron, Materials.Molybdenite, Materials.Molybdenum }, new int[] {100 * 100, 90 * 100, 80 * 100, 85 * 100, 65 * 100, 65 * 100 }, new int[] {}, Voltage.ULV, NI, 30 * 100);
			}
			addCentrifugeToMaterial(CombType.NICKEL, new Materials[] {Materials.Nickel, Materials.Garnierite, Materials.Pentlandite, Materials.Cobaltite, Materials.Wulfenite, Materials.Powellite}, new int[] {100 * 100, 85 * 100, 85 * 100, 80 * 100, 75 * 100, 75 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(CombType.ZINC, new Materials[] {Materials.Zinc, Materials.Sphalerite, Materials.Sulfur}, new int[] {100 * 100, 80 * 100, 75 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(CombType.SILVER, new Materials[] {Materials.Silver, Materials.Galena}, new int[] {100 * 100, 80 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(CombType.GOLD, new Materials[] {Materials.Gold}, new int[] {100 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(CombType.SULFUR, new Materials[] {Materials.Sulfur, Materials.Pyrite, Materials.Sphalerite}, new int[] {100 * 100, 90 * 100, 80 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(CombType.GALLIUM, new Materials[] {Materials.Gallium, Materials.Niobium}, new int[] { 80 * 100, 75 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(CombType.ARSENIC, new Materials[] {Materials.Arsenic, Materials.Bismuth, Materials.Antimony}, new int[] {80 * 100, 70 * 100, 70 * 100}, new int[] {}, Voltage.ULV , NI, 30 * 100);
		}

		// Rare Metals Line
		addProcessGT(CombType.BAUXITE, new Materials[] {Materials.Bauxite, Materials.Aluminium}, Voltage.LV);
		addProcessGT(CombType.ALUMINIUM, new Materials[] {Materials.Aluminium, Materials.Bauxite}, Voltage.LV);
		addProcessGT(CombType.MANGANESE, new Materials[] {Materials.Manganese, Materials.Grossular, Materials.Spessartine, Materials.Pyrolusite, Materials.Tantalite}, Voltage.LV);
		addProcessGT(CombType.TITANIUM, new Materials[] {Materials.Titanium, Materials.Ilmenite, Materials.Bauxite, Materials.Rutile}, Voltage.EV);
		addProcessGT(CombType.MAGNESIUM, new Materials[] {Materials.Magnesium, Materials.Magnesite}, Voltage.LV);
		addProcessGT(CombType.CHROME, new Materials[] {Materials.Chrome, Materials.Ruby, Materials.Chromite, Materials.Redstone, Materials.Neodymium, Materials.Bastnasite}, Voltage.HV);
		addProcessGT(CombType.TUNGSTEN, new Materials[] {Materials.Tungsten, Materials.Tungstate, Materials.Scheelite, Materials.Lithium}, Voltage.IV);
		addProcessGT(CombType.PLATINUM, new Materials[] {Materials.Platinum, Materials.Cooperite, Materials.Palladium}, Voltage.HV);
		addProcessGT(CombType.MOLYBDENUM, new Materials[] {Materials.Molybdenum, Materials.Molybdenite, Materials.Powellite, Materials.Wulfenite}, Voltage.LV);
		addChemicalProcess(CombType.MOLYBDENUM, Materials.Osmium, Materials.Osmium, Voltage.IV);
		addAutoclaveProcess(CombType.MOLYBDENUM, Materials.Osmium, Voltage.IV, 5);
		addProcessGT(CombType.IRIDIUM, new Materials[] {Materials.Iridium, Materials.Osmium}, Voltage.IV);
		addProcessGT(CombType.OSMIUM, new Materials[] {Materials.Osmium, Materials.Iridium}, Voltage.IV);
		addProcessGT(CombType.LITHIUM, new Materials[] {Materials.Lithium, Materials.Aluminium}, Voltage.MV);
		addProcessGT(CombType.SALT, new Materials[] {Materials.Salt, Materials.RockSalt, Materials.Saltpeter}, Voltage.MV);
		addProcessGT(CombType.ELECTROTINE, new Materials[] {Materials.Electrotine, Materials.Electrum, Materials.Redstone}, Voltage.MV);
		if(GT_Mod.gregtechproxy.mNerfedCombs) {
			addCentrifugeToItemStack(CombType.SALT, new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Salt, 1), GT_ModHandler.getModItem("dreamcraft", "item.EdibleSalt", 1L, 0), ItemList.FR_Wax.get(1) }, new int[] {100 * 100, 50 * 100, 50 * 100}, Voltage.MV, 160);
		}else {
			addCentrifugeToMaterial(CombType.BAUXITE, new Materials[] {Materials.Bauxite, Materials.Aluminium}, new int[] { 75 * 100, 55 * 100}, new int[] {}, Voltage.ULV , NI, 30 * 100);
			addCentrifugeToMaterial(CombType.ALUMINIUM, new Materials[] {Materials.Aluminium, Materials.Bauxite}, new int[] { 60 * 100, 80 * 100}, new int[] {}, Voltage.ULV , NI, 30 * 100);
			addCentrifugeToMaterial(CombType.MANGANESE, new Materials[] {Materials.Manganese, Materials.Grossular, Materials.Spessartine, Materials.Pyrolusite, Materials.Tantalite}, new int[] { 30 * 100, 100 * 100, 100 * 100, 100 * 100, 100 * 100}, new int[] {}, Voltage.ULV , NI, 30 * 100);
			addCentrifugeToMaterial(CombType.TITANIUM, new Materials[] {Materials.Titanium, Materials.Ilmenite, Materials.Bauxite, Materials.Rutile}, new int[] { 90 * 100, 80 * 100, 75 * 100, 75 * 100}, new int[] {}, Voltage.EV, NI, 30 * 100);
			addCentrifugeToMaterial(CombType.MAGNESIUM, new Materials[] {Materials.Magnesium, Materials.Magnesite}, new int[] { 100 * 100, 80 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(CombType.CHROME, new Materials[] {Materials.Chrome, Materials.Ruby, Materials.Chromite, Materials.Redstone, Materials.Neodymium, Materials.Bastnasite}, new int[] { 50 * 100, 100 * 100, 50 * 100, 100 * 100, 80 * 100, 80 * 100}, new int[] {}, Voltage.HV, NI, 30 * 100);
			addCentrifugeToMaterial(CombType.TUNGSTEN, new Materials[] {Materials.Tungsten, Materials.Tungstate, Materials.Scheelite, Materials.Lithium}, new int[] { 50 * 100, 80 * 100, 75 * 100, 75 * 100}, new int[] {}, Voltage.IV, NI, 30 * 100);
			addCentrifugeToMaterial(CombType.PLATINUM, new Materials[] {Materials.Platinum, Materials.Cooperite, Materials.Palladium}, new int[] { 40 * 100, 40 * 100, 40 * 100}, new int[] {}, Voltage.HV, NI, 30 * 100);
			addCentrifugeToMaterial(CombType.MOLYBDENUM, new Materials[] {Materials.Molybdenum, Materials.Molybdenite, Materials.Powellite, Materials.Wulfenite}, new int[] { 100 * 100, 80 * 100, 75 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(CombType.IRIDIUM, new Materials[] {Materials.Iridium, Materials.Osmium}, new int[] { 20 * 100, 15 * 100}, new int[] {}, Voltage.IV, NI, 30 * 100);
			addCentrifugeToMaterial(CombType.OSMIUM, new Materials[] {Materials.Osmium, Materials.Iridium}, new int[] { 25 * 100, 15 * 100}, new int[] {}, Voltage.IV, NI, 30 * 100);
			addCentrifugeToMaterial(CombType.LITHIUM, new Materials[] {Materials.Lithium, Materials.Aluminium}, new int[] { 85 * 100, 75 * 100}, new int[] {}, Voltage.MV, NI, 30 * 100);
			addCentrifugeToItemStack(CombType.SALT, new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Salt, 1), GT_ModHandler.getModItem("dreamcraft", "item.EdibleSalt", 1L, 0),GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.RockSalt, 1), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Saltpeter, 1), ItemList.FR_Wax.get(1) }, new int[] {100 * 100, 50 * 100, 75 * 100, 65 * 100, 50 * 100}, Voltage.MV, 160);
			addCentrifugeToMaterial(CombType.ELECTROTINE, new Materials[] {Materials.Electrotine, Materials.Electrum, Materials.Redstone}, new int[] { 80, 75, 65}, new int[] {}, Voltage.MV, NI, 30 * 100);
		}
		
		//Radioactive Line
		addProcessGT(CombType.ALMANDINE, new Materials[] {Materials.Almandine, Materials.Pyrope, Materials.Sapphire, Materials.GreenSapphire}, Voltage.LV);
		addProcessGT(CombType.URANIUM, new Materials[] {Materials.Uranium, Materials.Pitchblende, Materials.Uraninite, Materials.Uranium235}, Voltage.EV);
		addProcessGT(CombType.PLUTONIUM,new Materials[] {Materials.Plutonium, Materials.Uranium235}, Voltage.EV);
		addChemicalProcess(CombType.PLUTONIUM, Materials.Uranium235, Materials.Plutonium, Voltage.EV);
		addProcessGT(CombType.NAQUADAH,new Materials[] {Materials.Naquadah, Materials.NaquadahEnriched, Materials.Naquadria}, Voltage.IV);
		addProcessGT(CombType.NAQUADRIA,new Materials[] {Materials.Naquadria, Materials.NaquadahEnriched, Materials.Naquadah}, Voltage.LUV);
		addProcessGT(CombType.THORIUM,new Materials[] {Materials.Thorium, Materials.Uranium, Materials.Coal}, Voltage.EV);
		addProcessGT(CombType.LUTETIUM,new Materials[] {Materials.Lutetium, Materials.Thorium}, Voltage.IV);
		addProcessGT(CombType.AMERICUM,new Materials[] {Materials.Americium, Materials.Lutetium}, Voltage.LUV);
		addProcessGT(CombType.NEUTRONIUM,new Materials[] {Materials.Neutronium, Materials.Americium}, Voltage.UV);
		if(!GT_Mod.gregtechproxy.mNerfedCombs) {
			addCentrifugeToMaterial(CombType.ALMANDINE, new Materials[] {Materials.Almandine, Materials.Pyrope, Materials.Sapphire, Materials.GreenSapphire}, new int[] { 90 * 100, 80 * 100, 75 * 100, 75 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(CombType.URANIUM, new Materials[] {Materials.Uranium, Materials.Pitchblende, Materials.Uraninite, Materials.Uranium235}, new int[] { 50 * 100, 65 * 100, 75 * 100, 50 * 100}, new int[] {}, Voltage.EV, NI, 30 * 100);
			addCentrifugeToMaterial(CombType.PLUTONIUM,new Materials[] {Materials.Plutonium, Materials.Uranium235}, new int[] {10, 5}, new int[] {}, Voltage.EV, NI, 30 * 100);
			addCentrifugeToMaterial(CombType.NAQUADAH,new Materials[] {Materials.Naquadah, Materials.NaquadahEnriched, Materials.Naquadria}, new int[] {10 * 100, 5 * 100, 5 * 100}, new int[] {}, Voltage.IV, NI, 30 * 100);
			addCentrifugeToMaterial(CombType.NAQUADRIA,new Materials[] {Materials.Naquadria, Materials.NaquadahEnriched, Materials.Naquadah}, new int[] {10 * 100, 10 * 100, 15 * 100}, new int[] {}, Voltage.LUV, NI, 30 * 100);
			addCentrifugeToMaterial(CombType.THORIUM,new Materials[] {Materials.Thorium, Materials.Uranium, Materials.Coal}, new int[] {75 * 100, 75 * 100 * 100, 95 * 100}, new int[] {}, Voltage.EV, NI, 30 * 100);
			addCentrifugeToMaterial(CombType.LUTETIUM,new Materials[] {Materials.Lutetium, Materials.Thorium}, new int[] {35 * 100, 55 * 100}, new int[] {}, Voltage.IV, NI, 30 * 100);
			addCentrifugeToMaterial(CombType.AMERICUM,new Materials[] {Materials.Americium, Materials.Lutetium}, new int[] {25 * 100, 45 * 100}, new int[] {}, Voltage.LUV, NI, 30 * 100);
			addCentrifugeToMaterial(CombType.NEUTRONIUM,new Materials[] {Materials.Neutronium, Materials.Americium}, new int[] {15 * 100, 35 * 100}, new int[] {}, Voltage.UV, NI, 30 * 100);
		}
		
		// Twilight
		addCentrifugeToItemStack(CombType.NAGA, new ItemStack[] { GT_ModHandler.getModItem("MagicBees", "propolis", 1L, 4), GT_ModHandler.getModItem("dreamcraft", "item.NagaScaleChip", 1L, 0),  GT_ModHandler.getModItem("dreamcraft", "item.NagaScaleFragment", 1L, 0), ItemList.FR_Wax.get(1)}, new int[]{5 * 100, 33 * 100, 8 * 100, 30 * 100}, Voltage.MV);
		addCentrifugeToItemStack(CombType.LICH, new ItemStack[] { GT_ModHandler.getModItem("MagicBees", "propolis", 1L, 5), GT_ModHandler.getModItem("dreamcraft", "item.LichBoneChip", 1L, 0),  GT_ModHandler.getModItem("dreamcraft", "item.LichBoneFragment", 1L, 0), ItemList.FR_Wax.get(1)}, new int[]{5 * 100, 33 * 100, 8 * 100, 30 * 100}, Voltage.HV);
		addCentrifugeToItemStack(CombType.HYDRA, new ItemStack[] { GT_ModHandler.getModItem("MagicBees", "propolis", 1L, 1), GT_ModHandler.getModItem("dreamcraft", "item.FieryBloodDrop", 1L, 0),  GT_Bees.drop.getStackForType(DropType.HYDRA), ItemList.FR_Wax.get(1)}, new int[]{5 * 100, 33 * 100, 8 * 100, 30 * 100}, Voltage.HV);
		addCentrifugeToItemStack(CombType.URGHAST, new ItemStack[] { GT_ModHandler.getModItem("MagicBees", "propolis", 1L, 2), GT_ModHandler.getModItem("dreamcraft", "item.CarminiteChip", 1L, 0),  GT_ModHandler.getModItem("dreamcraft", "item.CarminiteFragment", 1L, 0), ItemList.FR_Wax.get(1)}, new int[]{5 * 100, 33 * 100, 8 * 100, 30 * 100}, Voltage.EV);
		addCentrifugeToItemStack(CombType.SNOWQUEEN, new ItemStack[] { GT_ModHandler.getModItem("MagicBees", "propolis", 1L, 3), GT_ModHandler.getModItem("dreamcraft", "item.SnowQueenBloodDrop", 1L, 0),   GT_Bees.drop.getStackForType(DropType.SNOW_QUEEN), ItemList.FR_Wax.get(1)}, new int[]{5 * 100, 33 * 100, 8 * 100, 30 * 100}, Voltage.EV);

		// HEE
		addCentrifugeToItemStack(CombType.ENDDUST, new ItemStack[] { GT_ModHandler.getModItem("MagicBees", "wax", 1L, 0), GT_Bees.propolis.getStackForType(PropolisType.End), GT_Bees.drop.getStackForType(DropType.ENDERGOO),  }, new int[]{20 * 100, 15 * 100, 10 * 100}, Voltage.HV);
		addCentrifugeToItemStack(CombType.STARDUST, new ItemStack[] { GT_ModHandler.getModItem("MagicBees", "wax", 1L, 0), GT_Bees.propolis.getStackForType(PropolisType.Stardust), GT_Bees.drop.getStackForType(DropType.ENDERGOO) }, new int[]{20 * 100, 15 * 100, 10 * 100}, Voltage.HV);
		addCentrifugeToItemStack(CombType.ECTOPLASMA, new ItemStack[] { GT_ModHandler.getModItem("MagicBees", "wax", 1L, 0), GT_Bees.propolis.getStackForType(PropolisType.Ectoplasma), GT_Bees.drop.getStackForType(DropType.ENDERGOO) }, new int[]{25 * 100, 10 * 100, 15 * 100}, Voltage.EV);
		addCentrifugeToItemStack(CombType.ARCANESHARD, new ItemStack[] { GT_ModHandler.getModItem("MagicBees", "wax", 1L, 0), GT_Bees.propolis.getStackForType(PropolisType.Arcaneshard), GT_Bees.drop.getStackForType(DropType.ENDERGOO) }, new int[]{25 * 100, 10 * 100, 15 * 100}, Voltage.EV);
		addCentrifugeToItemStack(CombType.DRAGONESSENCE, new ItemStack[] { GT_ModHandler.getModItem("MagicBees", "wax", 1L, 0), GT_Bees.propolis.getStackForType(PropolisType.Dragonessence), GT_Bees.drop.getStackForType(DropType.ENDERGOO) }, new int[]{30 * 100, (int) (7.5 * 100), 20 * 100}, Voltage.IV);
		addCentrifugeToItemStack(CombType.ENDERMAN, new ItemStack[] { GT_ModHandler.getModItem("MagicBees", "wax", 1L, 0), GT_Bees.propolis.getStackForType(PropolisType.Enderman), GT_Bees.drop.getStackForType(DropType.ENDERGOO) }, new int[]{3000, 750, 2000}, Voltage.IV);
		addCentrifugeToItemStack(CombType.SILVERFISH, new ItemStack[] { GT_ModHandler.getModItem("MagicBees", "wax", 1L, 0), GT_Bees.propolis.getStackForType(PropolisType.Silverfish), GT_Bees.drop.getStackForType(DropType.ENDERGOO), new ItemStack(Items.spawn_egg, 1,60) }, new int[]{25 * 100, 10 * 100, 20 * 100, 15 * 100}, Voltage.EV);
		addProcessGT(CombType.ENDIUM,new Materials[] {Materials.HeeEndium}, Voltage.HV);
		if(!GT_Mod.gregtechproxy.mNerfedCombs) {
			addCentrifugeToMaterial(CombType.ENDIUM,new Materials[] {Materials.HeeEndium}, new int[] {50 * 100}, new int[] {}, Voltage.HV, GT_ModHandler.getModItem("MagicBees", "wax", 1L, 0), 20 * 100);
		}
		addCentrifugeToItemStack(CombType.RUNEI, new ItemStack[] { GT_ModHandler.getModItem("MagicBees", "wax", 1L, 0), GT_ModHandler.getModItem("dreamcraft", "item.RuneOfPowerFragment", 1L, 0),   GT_ModHandler.getModItem("dreamcraft", "item.RuneOfAgilityFragment", 1L, 0),  GT_ModHandler.getModItem("dreamcraft", "item.RuneOfVigorFragment", 1L, 0),  GT_ModHandler.getModItem("dreamcraft", "item.RuneOfDefenseFragment", 1L, 0), GT_ModHandler.getModItem("dreamcraft", "item.RuneOfMagicFragment", 1L, 0) }, new int[]{25 * 100, 5 * 100, 5 * 100, 5 * 100, 5 * 100, 5 * 100}, Voltage.IV);
		addCentrifugeToItemStack(CombType.RUNEII, new ItemStack[] { GT_ModHandler.getModItem("MagicBees", "wax", 1L, 0), GT_ModHandler.getModItem("dreamcraft", "item.RuneOfVoidFragment", 1L, 0) }, new int[]{50 * 100, (int) (2.5 * 100)}, Voltage.IV);
		addCentrifugeToItemStack(CombType.FIREESSENSE, new ItemStack[] { GT_ModHandler.getModItem("MagicBees", "wax", 1L, 0), GT_Bees.propolis.getStackForType(PropolisType.Fireessence), GT_Bees.drop.getStackForType(DropType.ENDERGOO) }, new int[]{30 * 100, (int) (7.5 * 100), 20 * 100}, Voltage.IV);

		//Space Line
		addCentrifugeToItemStack(CombType.SPACE, new ItemStack[] { ItemList.FR_Wax.get(1L), ItemList.FR_RefractoryWax.get(1L), GT_Bees.drop.getStackForType(DropType.OXYGEN), GT_ModHandler.getModItem("dreamcraft", "item.CoinSpace", 1L, 0)}, new int[]{50 * 100, 30 * 100, 15 * 100, 5 * 100}, Voltage.HV);
		addProcessGT(CombType.METEORICIRON, new Materials[] {Materials.MeteoricIron, Materials.Iron}, Voltage.HV);
		addProcessGT(CombType.DESH, new Materials[] {Materials.Desh, Materials.Titanium}, Voltage.EV);
		addProcessGT(CombType.LEDOX, new Materials[] {Materials.Ledox, Materials.CallistoIce, Materials.Lead}, Voltage.EV);
		addProcessGT(CombType.CALLISTOICE, new Materials[] {Materials.CallistoIce, Materials.Ledox, Materials.Lead}, Voltage.IV);
		addProcessGT(CombType.MYTRYL, new Materials[] {Materials.Mytryl, Materials.Mithril}, Voltage.IV);
		addProcessGT(CombType.QUANTIUM, new Materials[] {Materials.Quantium, Materials.Osmium}, Voltage.IV);
		addProcessGT(CombType.ORIHARUKON, new Materials[] {Materials.Oriharukon, Materials.Lead}, Voltage.IV);
		addProcessGT(CombType.MYSTERIOUSCRYSTAL, new Materials[] {Materials.MysteriousCrystal, Materials.Emerald}, Voltage.LUV);
		addCentrifugeToMaterial(CombType.MYSTERIOUSCRYSTAL, new Materials[] {Materials.MysteriousCrystal, Materials.Emerald}, new int[] {(GT_Mod.gregtechproxy.mNerfedCombs ? 10 : 40) * 100, (GT_Mod.gregtechproxy.mNerfedCombs ? 15 : 50) * 100}, new int[] {}, Voltage.LUV, 512, NI, 50 * 100);
		addProcessGT(CombType.BLACKPLUTONIUM, new Materials[] {Materials.BlackPlutonium, Materials.Plutonium}, Voltage.LUV);
		addProcessGT(CombType.TRINIUM, new Materials[] {Materials.Trinium, Materials.Iridium}, Voltage.ZPM);
		if(!GT_Mod.gregtechproxy.mNerfedCombs) {
			addCentrifugeToMaterial(CombType.METEORICIRON, new Materials[] {Materials.MeteoricIron, Materials.Iron}, new int[] {85 * 100, 100 * 100}, new int[] {}, Voltage.HV, NI, 30 * 100);
			addCentrifugeToMaterial(CombType.DESH, new Materials[] {Materials.Desh, Materials.Titanium}, new int[] {75 * 100, 50 * 100}, new int[] {}, Voltage.EV, NI, 30 * 100);
			addCentrifugeToMaterial(CombType.LEDOX, new Materials[] {Materials.Ledox, Materials.CallistoIce, Materials.Lead}, new int[] {65 * 100, 55 * 100, 85 *100}, new int[] {}, Voltage.EV, NI, 30 * 100);
			addCentrifugeToMaterial(CombType.CALLISTOICE, new Materials[] {Materials.CallistoIce, Materials.Ledox, Materials.Lead}, new int[] {65 * 100, 75 * 100, 100 *100}, new int[] {}, Voltage.IV, NI, 30 * 100);
			addCentrifugeToMaterial(CombType.MYTRYL, new Materials[] {Materials.Mytryl, Materials.Mithril}, new int[] {55 * 100, 50 * 100}, new int[] {}, Voltage.IV, NI, 30 * 100);
			addCentrifugeToMaterial(CombType.QUANTIUM, new Materials[] {Materials.Quantium, Materials.Osmium}, new int[] {50 * 100, 60 * 100}, new int[] {}, Voltage.IV, NI, 30 * 100);
			addCentrifugeToMaterial(CombType.ORIHARUKON, new Materials[] {Materials.Oriharukon, Materials.Lead}, new int[] {50 * 100, 75 * 100}, new int[] {}, Voltage.IV, NI, 30 * 100);
			addCentrifugeToMaterial(CombType.BLACKPLUTONIUM, new Materials[] {Materials.BlackPlutonium, Materials.Plutonium}, new int[] {25 * 100, 50 * 100}, new int[] {}, Voltage.LUV, NI, 30 * 100);
			addCentrifugeToMaterial(CombType.TRINIUM, new Materials[] {Materials.Trinium, Materials.Iridium}, new int[] {35 * 100, 45 * 100}, new int[] {}, Voltage.ZPM, NI, 30 * 100);
		}

		//Planet Line
		addCentrifugeToItemStack(CombType.MOON, new ItemStack[] {ItemList.FR_Wax.get(1L), GT_ModHandler.getModItem("dreamcraft", "item.MoonStoneDust", 1L, 0)}, new int[]{50 * 100, 30 * 100 }, Voltage.MV, 300);
		addCentrifugeToItemStack(CombType.MARS, new ItemStack[] {ItemList.FR_Wax.get(1L), GT_ModHandler.getModItem("dreamcraft", "item.MarsStoneDust", 1L, 0)}, new int[]{50 * 100, 30 * 100 }, Voltage.HV, 300);
		addCentrifugeToItemStack(CombType.JUPITER, new ItemStack[] { GT_ModHandler.getModItem("dreamcraft", "item.IoStoneDust", 1L, 0), GT_ModHandler.getModItem("dreamcraft", "item.EuropaIceDust", 1L, 0), GT_ModHandler.getModItem("dreamcraft", "item.EuropaStoneDust", 1L, 0), GT_ModHandler.getModItem("dreamcraft", "item.GanymedeStoneDust", 1L, 0), GT_ModHandler.getModItem("dreamcraft", "item.CallistoStoneDust", 1L, 0), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.CallistoIce, 1L), ItemList.FR_Wax.get(1L)}, new int[]{30 * 100, 30 * 100, 30 * 100, 30 * 100, 30 * 100, 5 * 100, 50 * 100 }, Voltage.HV, 300);
		addCentrifugeToItemStack(CombType.MERCURY, new ItemStack[] {ItemList.FR_Wax.get(1L), GT_ModHandler.getModItem("dreamcraft", "item.MercuryCoreDust", 1L, 0), GT_ModHandler.getModItem("dreamcraft", "item.MercuryStoneDust", 1L, 0)}, new int[]{50 * 100, 30 * 100, 30 * 100 }, Voltage.EV, 300);
		addCentrifugeToItemStack(CombType.VENUS, new ItemStack[] {ItemList.FR_Wax.get(1L), GT_ModHandler.getModItem("dreamcraft", "item.VenusStoneDust", 1L, 0)}, new int[]{50 * 100, 30 * 100 }, Voltage.EV, 300);
		addCentrifugeToItemStack(CombType.SATURN, new ItemStack[] {ItemList.FR_Wax.get(1L), GT_ModHandler.getModItem("dreamcraft", "item.EnceladusStoneDust", 1L, 0), GT_ModHandler.getModItem("dreamcraft", "item.TitanStoneDust", 1L, 0)}, new int[]{50 * 100, 30 * 100, 30 * 100 }, Voltage.IV, 300);
		addCentrifugeToItemStack(CombType.URANUS, new ItemStack[] {ItemList.FR_Wax.get(1L), GT_ModHandler.getModItem("dreamcraft", "item.MirandaStoneDust", 1L, 0), GT_ModHandler.getModItem("dreamcraft", "item.OberonStoneDust", 1L, 0)}, new int[]{50 * 100, 30 * 100, 30 * 100 }, Voltage.IV, 300);
		addCentrifugeToItemStack(CombType.NEPTUN, new ItemStack[] {ItemList.FR_Wax.get(1L), GT_ModHandler.getModItem("dreamcraft", "item.ProteusStoneDust", 1L, 0), GT_ModHandler.getModItem("dreamcraft", "item.TritonStoneDust", 1L, 0)}, new int[]{50 * 100, 30 * 100, 30 * 100 }, Voltage.IV, 300);
		addCentrifugeToItemStack(CombType.PLUTO, new ItemStack[] {ItemList.FR_Wax.get(1L), GT_ModHandler.getModItem("dreamcraft", "item.PlutoStoneDust", 1L, 0), GT_ModHandler.getModItem("dreamcraft", "item.PlutoIceDust", 1L, 0)}, new int[]{50 * 100, 30 * 100, 30 * 100 }, Voltage.LUV, 300);
		addCentrifugeToItemStack(CombType.HAUMEA, new ItemStack[] {ItemList.FR_Wax.get(1L), GT_ModHandler.getModItem("dreamcraft", "item.HaumeaStoneDust", 1L, 0)}, new int[]{50 * 100, 30 * 100 }, Voltage.LUV, 300);
		addCentrifugeToItemStack(CombType.MAKEMAKE, new ItemStack[] {ItemList.FR_Wax.get(1L), GT_ModHandler.getModItem("dreamcraft", "item.MakeMakeStoneDust", 1L, 0)}, new int[]{50 * 100, 30 * 100 }, Voltage.LUV, 300);
		addCentrifugeToItemStack(CombType.CENTAURI, new ItemStack[] {ItemList.FR_Wax.get(1L), GT_ModHandler.getModItem("dreamcraft", "item.CentauriASurfaceDust", 1L, 0), GT_ModHandler.getModItem("dreamcraft", "item.CentauriAStoneDust", 1L, 0)}, new int[]{50 * 100, 30 * 100, 30 * 100 }, Voltage.ZPM, 300);
		addCentrifugeToItemStack(CombType.TCETI, new ItemStack[] {ItemList.FR_Wax.get(1L), GT_ModHandler.getModItem("dreamcraft", "item.TCetiEStoneDust", 1L, 0)}, new int[]{50 * 100, 30 * 100 }, Voltage.ZPM, 300);
		addCentrifugeToItemStack(CombType.BARNARDA, new ItemStack[] {ItemList.FR_Wax.get(1L), GT_ModHandler.getModItem("dreamcraft", "item.BarnardaEStoneDust", 1L, 0), GT_ModHandler.getModItem("dreamcraft", "item.BarnardaFStoneDust", 1L, 0)}, new int[]{50 * 100, 30 * 100, 30 * 100 }, Voltage.ZPM, 300);
		addCentrifugeToItemStack(CombType.VEGA, new ItemStack[] {ItemList.FR_Wax.get(1L), GT_ModHandler.getModItem("dreamcraft", "item.VegaBStoneDust", 1L, 0)}, new int[]{50 * 100, 30 * 100 }, Voltage.ZPM, 300);

		//Infinity Line
		addCentrifugeToMaterial(CombType.COSMICNEUTRONIUM, new Materials[] {Materials.CosmicNeutronium, Materials.Neutronium}, new int[] {(int) (0.5 * 100), 1 * 100}, new int[] {}, Voltage.UHV, 12000, NI, 50 * 100);
		addCentrifugeToMaterial(CombType.INFINITYCATALYST, new Materials[] {Materials.InfinityCatalyst, Materials.Neutronium}, new int[] {(int) (0.05 * 100), 1 * 100}, new int[] {}, Voltage.UEV, 48000, NI, 50 * 100);
		addCentrifugeToMaterial(CombType.INFINITY, new Materials[] {Materials.Infinity, Materials.InfinityCatalyst}, new int[] {(int) (0.01 * 100), (int) (0.05 * 100)}, new int[] {}, Voltage.UIV, 96000, NI, 50 * 100);
	}
	
	/**
	 * Currently use for STEEL, GOLD, MOLYBDENUM, PLUTONIUM
	 * **/
	public void addChemicalProcess(CombType comb, Materials aInMaterial, Materials aOutMaterial, Voltage volt){
		if(GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aOutMaterial, 4) == NI) return;
		RA.addChemicalRecipe(GT_Utility.copyAmount(9, getStackForType(comb)), GT_OreDictUnificator.get(OrePrefixes.crushed, aInMaterial, 1), volt.getComplexChemical(), aInMaterial.mOreByProducts.isEmpty() ? null : aInMaterial.mOreByProducts.get(0).getMolten(144), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aOutMaterial, 4), NI, volt.getComplexTime(), volt.getChemicalEnergy(), volt.compareTo(Voltage.IV) > 0);
	}
	
	/**
	 * Currently only used for CombType.MOLYBDENUM
	 * @param circuitNumber should not conflict with addProcessGT
	 *
	 * **/
	public void addAutoclaveProcess(CombType comb, Materials aMaterial, Voltage volt, int circuitNumber){
		if(GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial, 4) == NI) return;
		RA.addAutoclaveRecipe(GT_Utility.copyAmount(9, getStackForType(comb)), GT_Utility.getIntegratedCircuit(circuitNumber), Materials.UUMatter.getFluid(Math.max(1, ((aMaterial.getMass()+volt.getUUAmplifier())/10))), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial, 4), 10000, (int) (aMaterial.getMass() * 128), volt.getAutoClaveEnergy(), volt.compareTo(Voltage.HV) > 0);
	}
	
	/**
	 * this only adds Chemical and AutoClave process.
	 * If you need Centrifuge recipe. use  addCentrifugeToMaterial or addCentrifugeToItemStack
	 * @param volt This determine the required Tier of process for this recipes. This decide the required aEU/t, progress time, required additional UU-Matter, requirement of cleanRoom, needed fluid stack for Chemical.
	 * @param aMaterial result of Material that should be generated by this process.
	 * **/
	public void addProcessGT(CombType comb, Materials[] aMaterial, Voltage volt) {
		ItemStack tComb = getStackForType(comb);
		for(int i=0; i < aMaterial.length; i++) {
			if(GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial[i], 4)!= NI) {
				RA.addChemicalRecipe(GT_Utility.copyAmount(9, tComb), GT_OreDictUnificator.get(OrePrefixes.crushed, aMaterial[i], 1), volt.getComplexChemical(), aMaterial[i].mOreByProducts.isEmpty() ? null : aMaterial[i].mOreByProducts.get(0).getMolten(144), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial[i], 4), NI, volt.getComplexTime(), volt.getChemicalEnergy(), volt.compareTo(Voltage.IV) > 0);
				RA.addAutoclaveRecipe(GT_Utility.copyAmount(9, tComb), GT_Utility.getIntegratedCircuit(i+1), Materials.UUMatter.getFluid(Math.max(1, ((aMaterial[i].getMass()+volt.getUUAmplifier())/10))), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial[i], 4), 10000, (int) (aMaterial[i].getMass() * 128), volt.getAutoClaveEnergy(), volt.compareTo(Voltage.HV) > 0);
			}
		}
	}
	
	/**
	 * this method only adds Centrifuge based on Material. If volt is lower than MV than it will also adds forestry centrifuge recipe.
	 * @param comb		BeeComb
	 * @param aMaterial resulting Material of processing. can be more than 6. but over 6 will be ignored in Gregtech Centrifuge.
	 * @param chance chance to get result, 10000 == 100%
	 * @param volt required Voltage Tier for this recipe, this also affect the duration, amount of UU-Matter, and needed liquid type and amount for chemical reactor
	 * @param stackSize This parameter can be null, in that case stack size will be just 1. This handle the stackSize of the resulting Item, and Also the Type of Item. if this value is multiple of 9, than related Material output will be dust, if this value is multiple of 4 than output will be Small dust, else the output will be Tiny dust
	 * @param beeWax if this is null, than the comb will product default Bee wax. But if aMaterial is more than 5, beeWax will be ignored in Gregtech Centrifuge.
	 * @param waxChance have same format like "chance"
	**/
	public void addCentrifugeToMaterial(CombType comb, Materials[] aMaterial, int[] chance, int[] stackSize, Voltage volt, ItemStack beeWax, int waxChance) {
		addCentrifugeToMaterial(comb, aMaterial, chance, stackSize, volt, volt.getSimpleTime(), beeWax, waxChance);
	}
	public void addCentrifugeToMaterial(CombType comb, Materials[] aMaterial, int[] chance, int[] stackSize, Voltage volt, int duration, ItemStack beeWax, int waxChance) {
		ItemStack[] aOutPut = new ItemStack[aMaterial.length+1];
		stackSize = Arrays.copyOf(stackSize, aMaterial.length);
		chance = Arrays.copyOf(chance, aOutPut.length);
		chance[chance.length - 1] = waxChance;
		for(int i = 0; i < (aMaterial.length); i++) {
			if(chance[i] == 0) {
				continue;
			}
			if(Math.max(1, stackSize[i]) % 9 == 0) {
				aOutPut[i] = GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial[i], (Math.max(1, stackSize[i])/9) );
			}else if(Math.max(1, stackSize[i]) % 4 == 0) {
				aOutPut[i] = GT_OreDictUnificator.get(OrePrefixes.dustSmall, aMaterial[i], (Math.max(1, stackSize[i])/4) );
			}else {
				aOutPut[i] = GT_OreDictUnificator.get(OrePrefixes.dustTiny, aMaterial[i], Math.max(1, stackSize[i]));
			}
		}
		if(beeWax != NI) {
			aOutPut[aOutPut.length - 1] = beeWax;
		}else {
			aOutPut[aOutPut.length - 1] = ItemList.FR_Wax.get(1);
		}

		addCentrifugeToItemStack(comb, aOutPut, chance, volt, duration);
	}
	
	/**
	 * @param volt	required Tier of system. If it's lower than MV, it will also add forestry centrifuge.
	 * @param aItem can be more than 6. but Over 6 will be ignored in Gregtech Centrifuge.
	 **/
	public void addCentrifugeToItemStack(CombType comb, ItemStack[] aItem, int[] chance, Voltage volt) {
		addCentrifugeToItemStack(comb, aItem, chance, volt, volt.getSimpleTime());
	}
	public void addCentrifugeToItemStack(CombType comb, ItemStack[] aItem, int[] chance, Voltage volt, int duration) {
		ItemStack tComb = getStackForType(comb);
		Builder<ItemStack,Float> Product = new ImmutableMap.Builder<ItemStack, Float>();
		for(int i=0; i < aItem.length; i++) {
			if(aItem[i] == NI) { continue; }
				Product.put(aItem[i],chance[i]/10000.0f);
		}

		if(volt.compareTo(Voltage.MV) < 0 || !GT_Mod.gregtechproxy.mNerfedCombs) {
			RecipeManagers.centrifugeManager.addRecipe(40, tComb, Product.build());
		}
		
		aItem = Arrays.copyOf(aItem, 6);
		if(aItem.length > 6) {
			chance = Arrays.copyOf(chance, 6);
		}

		RA.addCentrifugeRecipe(tComb, NI, NF, NF, aItem[0], aItem[1], aItem[2], aItem[3], aItem[4], aItem[5], chance, duration, volt.getSimpleEnergy());
	}
	
	enum Voltage {
		ULV, LV, MV,
        HV, EV, IV,
        LUV, ZPM, UV,
        UHV, UEV, UIV,
        UMV, UXV, OpV,
        MAX;
		public int getVoltage() {
			return (int) V[this.ordinal()];
		}
		/**@return aEU/t needed for chemical and autoclave process related to the Tier**/
		public int getChemicalEnergy() {
			return this.getVoltage()*3/4;
		}
		public int getAutoClaveEnergy() {
			return (int) ((this.getVoltage()*3/4) * (Math.max(1, Math.pow(2, 5 - this.ordinal()))));
		}
		/**@return FluidStack needed for chemical process related to the Tier**/
		public FluidStack getComplexChemical() {
			if(this.compareTo(Voltage.MV) < 0) {
				return Materials.Water.getFluid((this.compareTo(Voltage.ULV) > 0) ? 1000 : 500);
			}else if(this.compareTo(Voltage.HV) < 0) {
				return GT_ModHandler.getDistilledWater(1000L);
			}else if(this.compareTo(Voltage.LUV) < 0) {
				return Materials.Mercury.getFluid((long) (Math.pow(2, this.compareTo(Voltage.HV)) * L));
			}else if(this.compareTo(Voltage.UHV) < 0) {
				return FluidRegistry.getFluidStack("mutagen", (int) (Math.pow(2, this.compareTo(Voltage.LUV)) * L));
			}else {
				return NF;
			}
		}
		/**@return additional required UU-Matter amount for Autoclave process related to the Tier**/
		public int getUUAmplifier() {
			return 9 * ( (this.compareTo(Voltage.MV) < 0) ? 1 : this.compareTo(Voltage.MV));
		}
		/**@return duration needed for Chemical process related to the Tier**/
		public int getComplexTime() {
			return 64 + this.ordinal() * 32;
		}
		/**@return duration needed for Centrifuge process related to the Tier**/
		public int getSimpleTime() {
			if(!GT_Mod.gregtechproxy.mNerfedCombs) {
				return 96 + this.ordinal() * 32;
			} else {
				//ULV, LV needs 128ticks, MV need 256 ticks, HV need 384 ticks, EV need 512 ticks, IV need 640 ticks
				return 128 * (Math.max(1, this.ordinal()));
			}
		}
		/**@return aEU/t needed for Centrifuge process related to the Tier**/
		public int getSimpleEnergy() {
			if(this == Voltage.ULV) {
				return 5;
			}else {
				return (int) (this.getVoltage() / 16) * 15;
			}
		}
	}
}

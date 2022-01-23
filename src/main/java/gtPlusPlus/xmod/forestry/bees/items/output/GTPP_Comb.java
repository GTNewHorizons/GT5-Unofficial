package gtPlusPlus.xmod.forestry.bees.items.output;

import static gregtech.api.enums.GT_Values.*;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.core.Tabs;
import forestry.api.recipes.RecipeManagers;
import gregtech.GT_Mod;
import gregtech.api.enums.*;
import gregtech.api.util.*;
import gregtech.loaders.materialprocessing.ProcessingModSupport;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.forestry.bees.handler.*;
import gtPlusPlus.xmod.forestry.bees.registry.GTPP_Bees;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class GTPP_Comb extends Item {
	
	@SideOnly(Side.CLIENT)
	private IIcon secondIcon;

	public GTPP_Comb() {
		super();
		this.setCreativeTab(Tabs.tabApiculture);
		this.setHasSubtypes(true);
		this.setUnlocalizedName("gtpp.comb");
		GameRegistry.registerItem(this, "gtpp.comb", CORE.MODID);
	}

	public ItemStack getStackForType(GTPP_CombType type) {
		return new ItemStack(this, 1, type.ordinal());
	}

	public ItemStack getStackForType(GTPP_CombType type, int count) {
		return new ItemStack(this, count, type.ordinal());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tabs, List list) {
		for (GTPP_CombType type : GTPP_CombType.values()) {
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
		int meta = Math.max(0, Math.min(GTPP_CombType.values().length - 1, stack.getItemDamage()));
		int colour = GTPP_CombType.values()[meta].getColours()[0];

		if (pass >= 1) {
			colour = GTPP_CombType.values()[meta].getColours()[1];
		}

		return colour;
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return GTPP_CombType.values()[stack.getItemDamage()].getName();
	}
	public void initCombsRecipes() {

		//Organic
		addProcessGT(GTPP_CombType.LIGNIE, new Materials[] {Materials.Lignite}, Voltage.LV);
		addProcessGT(GTPP_CombType.COAL, new Materials[] {Materials.Coal}, Voltage.LV);
		addCentrifugeToItemStack(GTPP_CombType.STICKY, new ItemStack[] { ItemList.IC2_Resin.get(1), ItemList.IC2_Plantball.get(1), ItemList.FR_Wax.get(1) }, new int[] {50 * 100, 15 * 100, 50 * 100}, Voltage.ULV);
		addProcessGT(GTPP_CombType.OIL, new Materials[] {Materials.Oilsands}, Voltage.LV);
		addProcessGT(GTPP_CombType.APATITE, new Materials[] {Materials.Apatite, Materials.Phosphate}, Voltage.LV);
		addCentrifugeToMaterial(GTPP_CombType.ASH, new Materials[] {Materials.DarkAsh, Materials.Ash}, new int[] { 50*100, 50*100}, new int[] {}, Voltage.ULV, NI, 50 * 100);
		if(GT_Mod.gregtechproxy.mNerfedCombs) {
			addCentrifugeToItemStack(GTPP_CombType.LIGNIE, new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Lignite, 1), ItemList.FR_Wax.get(1) }, new int[] {90 * 100, 50 * 100}, Voltage.ULV);
			addCentrifugeToItemStack(GTPP_CombType.COAL, new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 1), ItemList.FR_Wax.get(1) }, new int[] {5 * 100, 50 * 100}, Voltage.ULV);
			addCentrifugeToItemStack(GTPP_CombType.OIL, new ItemStack[] { ItemList.Crop_Drop_OilBerry.get(1), GTPP_Bees.drop.getStackForType(GTPP_DropType.OIL), ItemList.FR_Wax.get(1) }, new int[] {70 * 100, 100 * 100, 50 * 100}, Voltage.ULV);
		}else {
			addCentrifugeToItemStack(GTPP_CombType.LIGNIE, new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Lignite, 1), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Lignite, 1), ItemList.FR_Wax.get(1) }, new int[] {90 * 100, 100 * 100, 50 * 100}, Voltage.ULV);
			addCentrifugeToItemStack(GTPP_CombType.COAL, new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 1), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Coal, 1), ItemList.FR_Wax.get(1) }, new int[] {5 * 100, 100 * 100, 50 * 100}, Voltage.ULV);
			addCentrifugeToItemStack(GTPP_CombType.OIL, new ItemStack[] { ItemList.Crop_Drop_OilBerry.get(1), GTPP_Bees.drop.getStackForType(GTPP_DropType.OIL), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Oilsands, 1), ItemList.FR_Wax.get(1) }, new int[] {70 * 100, 100 * 100, 100 * 100, 50 * 100}, Voltage.ULV);
			addCentrifugeToMaterial(GTPP_CombType.APATITE, new Materials[] { Materials.Apatite, Materials.Phosphate }, new int[] {100 * 100, 80 * 100 }, new int[] {}, Voltage.ULV, NI, 30 * 100);
		}
		
		//ic2
		addCentrifugeToItemStack(GTPP_CombType.COOLANT, new ItemStack[] { GTPP_Bees.drop.getStackForType(GTPP_DropType.COOLANT), ItemList.FR_Wax.get(1) }, new int[] {100 * 100, 100 * 100}, Voltage.HV, 196);
		addCentrifugeToItemStack(GTPP_CombType.ENERGY, new ItemStack[] {GTPP_Bees.drop.getStackForType(GTPP_DropType.HOT_COOLANT), ItemList.IC2_Energium_Dust.get(1L), ItemList.FR_RefractoryWax.get(1)}, new int[] {20 * 100, 20 * 100, 50 * 100}, Voltage.HV, 196);
		addCentrifugeToItemStack(GTPP_CombType.LAPOTRON, new ItemStack[] {GTPP_Bees.drop.getStackForType(GTPP_DropType.LAPIS), GT_ModHandler.getModItem(MOD_ID_DC, "item.LapotronDust", 1, 0), GT_ModHandler.getModItem("MagicBees", "wax", 1, 2) }, new int[] {20 * 100, 15 * 100, 40 * 100}, Voltage.HV, 196);
		addCentrifugeToMaterial(GTPP_CombType.PYROTHEUM, new Materials[] {Materials.Blaze, Materials.Pyrotheum}, new int[] { 25 * 100, 20 * 100}, new int[] {}, Voltage.HV, NI, 30 * 100);
		addCentrifugeToMaterial(GTPP_CombType.CRYOTHEUM, new Materials[] {Materials.Blizz, Materials.Cryotheum}, new int[] { 25 * 100, 20 * 100}, new int[] {}, Voltage.HV, NI, 30 * 100);

		//Alloy
		addProcessGT(GTPP_CombType.REDALLOY, new Materials[] {Materials.RedAlloy, Materials.Redstone, Materials.Copper}, Voltage.LV);
		addProcessGT(GTPP_CombType.REDSTONEALLOY, new Materials[] {Materials.RedstoneAlloy, Materials.Redstone, Materials.Silicon, Materials.Coal}, Voltage.LV);
		addProcessGT(GTPP_CombType.CONDUCTIVEIRON, new Materials[] {Materials.ConductiveIron, Materials.Silver, Materials.Iron }, Voltage.MV);
		addProcessGT(GTPP_CombType.VIBRANTALLOY, new Materials[] {Materials.VibrantAlloy, Materials.Chrome }, Voltage.HV);
		addProcessGT(GTPP_CombType.ENERGETICALLOY, new Materials[] {Materials.EnergeticAlloy, Materials.Gold }, Voltage.HV);
		addProcessGT(GTPP_CombType.ELECTRICALSTEEL, new Materials[] {Materials.ElectricalSteel, Materials.Silicon, Materials.Coal }, Voltage.LV);
		addProcessGT(GTPP_CombType.DARKSTEEL, new Materials[] {Materials.DarkSteel, Materials.Coal }, Voltage.MV);
		addProcessGT(GTPP_CombType.PULSATINGIRON, new Materials[] {Materials.PulsatingIron, Materials.Iron }, Voltage.HV);
		addProcessGT(GTPP_CombType.STAINLESSSTEEL, new Materials[] {Materials.StainlessSteel, Materials.Iron, Materials.Chrome, Materials.Manganese, Materials.Nickel }, Voltage.HV);
		addCentrifugeToItemStack(GTPP_CombType.ENDERIUM, new ItemStack[] {ItemList.FR_RefractoryWax.get(1), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.EnderiumBase, 1), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Enderium, 1) }, new int[] {50 * 100, 30 * 100, 50 * 100 }, Voltage.HV);
		if(GT_Mod.gregtechproxy.mNerfedCombs) {
			addCentrifugeToMaterial(GTPP_CombType.REDALLOY, new Materials[] {Materials.RedAlloy}, new int[] {100 * 100 }, new int[] {}, Voltage.ULV, ItemList.FR_RefractoryWax.get(1), 50 * 100);
			addCentrifugeToMaterial(GTPP_CombType.REDSTONEALLOY, new Materials[] {Materials.RedstoneAlloy}, new int[] {100 * 100 }, new int[] {}, Voltage.ULV, ItemList.FR_RefractoryWax.get(1), 50 * 100);
			addCentrifugeToMaterial(GTPP_CombType.CONDUCTIVEIRON, new Materials[] {Materials.ConductiveIron }, new int[] {90 * 100}, new int[] {}, Voltage.MV, ItemList.FR_RefractoryWax.get(1), 50 * 100);
			addCentrifugeToMaterial(GTPP_CombType.VIBRANTALLOY, new Materials[] {Materials.VibrantAlloy }, new int[] {70 * 100}, new int[] {}, Voltage.HV, ItemList.FR_RefractoryWax.get(1), 50 * 100);
			addCentrifugeToMaterial(GTPP_CombType.ENERGETICALLOY, new Materials[] {Materials.EnergeticAlloy }, new int[] {80 * 100}, new int[] {}, Voltage.HV, ItemList.FR_RefractoryWax.get(1), 50 * 100);
			addCentrifugeToMaterial(GTPP_CombType.ELECTRICALSTEEL, new Materials[] {Materials.ElectricalSteel }, new int[] {100 * 100}, new int[] {}, Voltage.ULV, ItemList.FR_RefractoryWax.get(1), 50 * 100);
			addCentrifugeToMaterial(GTPP_CombType.DARKSTEEL, new Materials[] {Materials.DarkSteel }, new int[] {100 * 100}, new int[] {}, Voltage.MV, ItemList.FR_RefractoryWax.get(1), 50 * 100);
			addCentrifugeToMaterial(GTPP_CombType.PULSATINGIRON, new Materials[] {Materials.PulsatingIron }, new int[] {80 * 100}, new int[] {}, Voltage.HV, ItemList.FR_RefractoryWax.get(1), 50 * 100);
			addCentrifugeToMaterial(GTPP_CombType.STAINLESSSTEEL, new Materials[] {Materials.StainlessSteel }, new int[] {50 * 100}, new int[] {}, Voltage.HV, ItemList.FR_RefractoryWax.get(1), 50 * 100);
		}else {
			addCentrifugeToMaterial(GTPP_CombType.REDALLOY, new Materials[] {Materials.RedAlloy, Materials.Redstone, Materials.Copper}, new int[] {100 * 100, 75 * 100, 90 * 100 }, new int[] {}, Voltage.ULV, ItemList.FR_RefractoryWax.get(1), 50 * 100);
			addCentrifugeToMaterial(GTPP_CombType.REDSTONEALLOY, new Materials[] {Materials.RedstoneAlloy, Materials.Redstone, Materials.Silicon, Materials.Coal}, new int[] {100 * 100, 90 * 100, 75 * 100, 75 * 100 }, new int[] {}, Voltage.ULV, ItemList.FR_RefractoryWax.get(1), 50 * 100);
			addCentrifugeToMaterial(GTPP_CombType.CONDUCTIVEIRON, new Materials[] {Materials.ConductiveIron, Materials.Silver, Materials.Iron }, new int[] {90 * 100, 55 * 100, 65 * 100}, new int[] {}, Voltage.MV, ItemList.FR_RefractoryWax.get(1), 50 * 100);
			addCentrifugeToMaterial(GTPP_CombType.VIBRANTALLOY, new Materials[] {Materials.VibrantAlloy, Materials.Chrome }, new int[] {70 * 100, 50 * 100}, new int[] {}, Voltage.HV, ItemList.FR_RefractoryWax.get(1), 50 * 100);
			addCentrifugeToMaterial(GTPP_CombType.ENERGETICALLOY, new Materials[] {Materials.EnergeticAlloy, Materials.Gold }, new int[] {80 * 100, 60 * 100}, new int[] {}, Voltage.HV, ItemList.FR_RefractoryWax.get(1), 50 * 100);
			addCentrifugeToMaterial(GTPP_CombType.ELECTRICALSTEEL, new Materials[] {Materials.ElectricalSteel, Materials.Silicon, Materials.Coal }, new int[] {100 * 100, 75 * 100, 75 * 100}, new int[] {}, Voltage.ULV, ItemList.FR_RefractoryWax.get(1), 50 * 100);
			addCentrifugeToMaterial(GTPP_CombType.DARKSTEEL, new Materials[] {Materials.DarkSteel, Materials.Coal }, new int[] {100 * 100, 75 * 100 }, new int[] {}, Voltage.MV, ItemList.FR_RefractoryWax.get(1), 50 * 100);
			addCentrifugeToMaterial(GTPP_CombType.PULSATINGIRON, new Materials[] {Materials.PulsatingIron, Materials.Iron }, new int[] {80 * 100, 75 * 100 }, new int[] {}, Voltage.HV, ItemList.FR_RefractoryWax.get(1), 50 * 100);
			addCentrifugeToMaterial(GTPP_CombType.STAINLESSSTEEL, new Materials[] {Materials.StainlessSteel, Materials.Iron, Materials.Chrome, Materials.Manganese, Materials.Nickel }, new int[] {50 * 100, 75 * 100, 55 * 100, 75 * 100, 75 * 100 }, new int[] {}, Voltage.HV, ItemList.FR_RefractoryWax.get(1), 50 * 100);
		}
		
		//Thaumic
		addProcessGT(GTPP_CombType.THAUMIUMDUST, new Materials[] {Materials.Thaumium, Materials.Iron }, Voltage.MV);
		addCentrifugeToItemStack(GTPP_CombType.THAUMIUMSHARD, new ItemStack[] {GT_ModHandler.getModItem("MagicBees", "propolis", 1, 1), GT_ModHandler.getModItem("MagicBees", "propolis", 1, 2), GT_ModHandler.getModItem("MagicBees", "propolis", 1, 3), GT_ModHandler.getModItem("MagicBees", "propolis", 1, 4), GT_ModHandler.getModItem("MagicBees", "propolis", 1, 5), GT_ModHandler.getModItem("MagicBees", "propolis", 1, 6), GT_ModHandler.getModItem("MagicBees", "wax", 1, 0) }, new int[] {20 * 100, 20 * 100, 20 * 100, 20 * 100, 20 * 100, 20 * 100, 50 * 100 }, Voltage.ULV);
		addProcessGT(GTPP_CombType.AMBER, new Materials[] {Materials.Amber}, Voltage.LV);
		addProcessGT(GTPP_CombType.QUICKSILVER, new Materials[] {Materials.Cinnabar }, Voltage.LV);
		addCentrifugeToItemStack(GTPP_CombType.SALISMUNDUS, new ItemStack[] {GT_ModHandler.getModItem("Thaumcraft", "ItemResource", 1, 14), GT_ModHandler.getModItem("MagicBees", "wax", 1, 0)}, new int[] {100 * 100, 50 * 100}, Voltage.MV);
		addCentrifugeToItemStack(GTPP_CombType.TAINTED, new ItemStack[] {GT_ModHandler.getModItem("Thaumcraft", "ItemResource", 1, 11), GT_ModHandler.getModItem("Thaumcraft", "ItemResource", 1, 12), GT_ModHandler.getModItem("Thaumcraft", "blockTaintFibres", 1, 0),  GT_ModHandler.getModItem("Thaumcraft", "blockTaintFibres", 1, 1), GT_ModHandler.getModItem("Thaumcraft", "blockTaintFibres", 1, 2), GT_ModHandler.getModItem("MagicBees", "wax", 1, 0)}, new int[] {15 * 100, 15 * 100, 15 * 100, 15 * 100, 15 * 100, 50 * 100}, Voltage.ULV);
		addProcessGT(GTPP_CombType.MITHRIL, new Materials[] {Materials.Mithril, Materials.Platinum }, Voltage.HV);
		addProcessGT(GTPP_CombType.ASTRALSILVER, new Materials[] {Materials.AstralSilver, Materials.Silver }, Voltage.HV);
		addCentrifugeToMaterial(GTPP_CombType.ASTRALSILVER, new Materials[] {Materials.AstralSilver, Materials.Silver}, new int[] {20 * 100, (GT_Mod.gregtechproxy.mNerfedCombs ? 10:75) * 100}, new int[] {}, Voltage.HV, GT_ModHandler.getModItem("MagicBees", "wax", 1, 0), 50 * 100);
		addCentrifugeToItemStack(GTPP_CombType.THAUMINITE, new ItemStack[] {GT_ModHandler.getModItem("thaumicbases", "resource", 1, 0), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Thaumium, 1), GT_ModHandler.getModItem("MagicBees", "wax", 1, 0)}, new int[] {20 * 100, 10 * 100, 50 *100}, Voltage.HV);
		addProcessGT(GTPP_CombType.SHADOWMETAL, new Materials[] {Materials.Shadow, Materials.ShadowSteel }, Voltage.HV);
		addCentrifugeToMaterial(GTPP_CombType.SHADOWMETAL, new Materials[] {Materials.Shadow, Materials.ShadowSteel}, new int[] {(GT_Mod.gregtechproxy.mNerfedCombs ? 20:75) * 100, 10 * 100}, new int[] {}, Voltage.HV, GT_ModHandler.getModItem("MagicBees", "wax", 1, 0) , 50 * 100);
		addProcessGT(GTPP_CombType.DIVIDED, new Materials[] {Materials.Iron, Materials.Diamond }, Voltage.HV);
		addCentrifugeToItemStack(GTPP_CombType.DIVIDED, new ItemStack[] { GT_ModHandler.getModItem("MagicBees", "wax", 1, 0), GT_ModHandler.getModItem("ExtraUtilities", "unstableingot", 1, 1), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Iron, 1), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Diamond, 1)}, new int[] {50 * 100, 20 * 100, (GT_Mod.gregtechproxy.mNerfedCombs ? 10:75) * 100, (GT_Mod.gregtechproxy.mNerfedCombs ? 5:55) * 100}, Voltage.HV);
		addProcessGT(GTPP_CombType.SPARKELING, new Materials[] {Materials.NetherStar}, Voltage.EV);
		addCentrifugeToItemStack(GTPP_CombType.SPARKELING, new ItemStack[] { GT_ModHandler.getModItem("MagicBees", "wax", 1, 0), GT_ModHandler.getModItem("MagicBees", "miscResources", 2, 5), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.NetherStar, 1)}, new int[] {50 * 100, 10 * 100, (GT_Mod.gregtechproxy.mNerfedCombs ? 10:50) * 100}, Voltage.EV);
		if(GT_Mod.gregtechproxy.mNerfedCombs) {
			addCentrifugeToMaterial(GTPP_CombType.THAUMIUMDUST, new Materials[] {Materials.Thaumium }, new int[] {100 * 100}, new int[] {}, Voltage.MV, GT_ModHandler.getModItem("MagicBees", "wax", 1, 0), 50 * 100);
			addCentrifugeToItemStack(GTPP_CombType.QUICKSILVER, new ItemStack[] {GT_ModHandler.getModItem("MagicBees", "wax", 1, 0), GT_ModHandler.getModItem("Thaumcraft", "ItemNugget", 1, 5) }, new int[] {50 * 100, 100 * 100 }, Voltage.ULV);
		}else {
			addCentrifugeToMaterial(GTPP_CombType.THAUMIUMDUST, new Materials[] {Materials.Thaumium, Materials.Iron }, new int[] {100 * 100, 75 * 100 }, new int[] {}, Voltage.MV, GT_ModHandler.getModItem("MagicBees", "wax", 1, 0), 50 * 100);
			addCentrifugeToMaterial(GTPP_CombType.AMBER, new Materials[] {Materials.Amber }, new int[] {100 * 100 }, new int[] {}, Voltage.ULV, GT_ModHandler.getModItem("MagicBees", "wax", 1, 0), 50 * 100);
			addCentrifugeToItemStack(GTPP_CombType.QUICKSILVER, new ItemStack[] {GT_ModHandler.getModItem("MagicBees", "wax", 1, 0), GT_ModHandler.getModItem("Thaumcraft", "ItemNugget", 1, 5), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Cinnabar, 1) }, new int[] {50 * 100, 100 * 100, 85 * 100 }, Voltage.ULV);
			addCentrifugeToMaterial(GTPP_CombType.MITHRIL, new Materials[] {Materials.Mithril, Materials.Platinum}, new int[] {75 * 100, 55 * 100}, new int[] {}, Voltage.HV, GT_ModHandler.getModItem("MagicBees", "wax", 1, 0), 50 * 100);
		}
		
		//Gem Line
		addProcessGT(GTPP_CombType.STONE, new Materials[] {Materials.Soapstone, Materials.Talc, Materials.Apatite, Materials.Phosphate, Materials.TricalciumPhosphate}, Voltage.LV);
		addProcessGT(GTPP_CombType.CERTUS, new Materials[] {Materials.CertusQuartz, Materials.Quartzite, Materials.Barite}, Voltage.LV);
		addProcessGT(GTPP_CombType.FLUIX, new Materials[] {Materials.Redstone, Materials.CertusQuartz, Materials.NetherQuartz}, Voltage.LV);
		addProcessGT(GTPP_CombType.REDSTONE, new Materials[] {Materials.Redstone, Materials.Cinnabar}, Voltage.LV);
		addCentrifugeToMaterial(GTPP_CombType.RAREEARTH, new Materials[] {Materials.RareEarth}, new int[] {100 * 100}, new int[] { 1}, Voltage.ULV, NI, 30 * 100);
		addProcessGT(GTPP_CombType.LAPIS, new Materials[] {Materials.Lapis, Materials.Sodalite, Materials.Lazurite, Materials.Calcite }, Voltage.LV);
		addProcessGT(GTPP_CombType.RUBY, new Materials[] {Materials.Ruby, Materials.Redstone }, Voltage.LV);
		addProcessGT(GTPP_CombType.REDGARNET, new Materials[] {Materials.GarnetRed, Materials.GarnetYellow }, Voltage.LV);
		addProcessGT(GTPP_CombType.YELLOWGARNET, new Materials[] {Materials.GarnetYellow, Materials.GarnetRed }, Voltage.LV);
		addProcessGT(GTPP_CombType.SAPPHIRE, new Materials[] {Materials.Sapphire, Materials.GreenSapphire, Materials.Almandine, Materials.Pyrope}, Voltage.LV);
		addProcessGT(GTPP_CombType.DIAMOND, new Materials[] {Materials.Diamond, Materials.Graphite}, Voltage.LV);
		addProcessGT(GTPP_CombType.OLIVINE, new Materials[] {Materials.Olivine, Materials.Bentonite, Materials.Magnesite, Materials.Glauconite}, Voltage.LV);
		addProcessGT(GTPP_CombType.EMERALD, new Materials[] {Materials.Emerald, Materials.Beryllium, Materials.Thorium}, Voltage.LV);
		addProcessGT(GTPP_CombType.FIRESTONE, new Materials[] {Materials.Firestone}, Voltage.LV);
		addProcessGT(GTPP_CombType.PYROPE, new Materials[] {Materials.Pyrope, Materials.Aluminium, Materials.Magnesium, Materials.Silicon}, Voltage.LV);
		addProcessGT(GTPP_CombType.GROSSULAR, new Materials[] {Materials.Grossular, Materials.Aluminium, Materials.Silicon}, Voltage.LV);
		if(GT_Mod.gregtechproxy.mNerfedCombs) {
			addCentrifugeToMaterial(GTPP_CombType.STONE, new Materials[] {Materials.Stone, Materials.GraniteBlack, Materials.GraniteRed, Materials.Basalt, Materials.Marble, Materials.Redrock}, new int[] {70 * 100, 50 * 100, 50 * 100, 50 * 100, 50 * 100, 50 * 100}, new int[] { 9, 9, 9, 9, 9, 9}, Voltage.ULV, NI, 50 * 100);
			addCentrifugeToMaterial(GTPP_CombType.FLUIX, new Materials[] {Materials.Fluix}, new int[] {25 * 100}, new int[] { 9}, Voltage.ULV, NI, 30 * 100);
		}else {
			addCentrifugeToMaterial(GTPP_CombType.STONE, new Materials[] {Materials.Soapstone, Materials.Talc, Materials.Apatite, Materials.Phosphate, Materials.TricalciumPhosphate}, new int[] {95 * 100, 90 * 100, 80 * 100, 75 * 100, 75 * 100}, new int[] {}, Voltage.ULV, NI, 50 * 100);
			addCentrifugeToMaterial(GTPP_CombType.CERTUS, new Materials[] {Materials.CertusQuartz, Materials.Quartzite, Materials.Barite}, new int[] {100 * 100, 80 * 100, 75 * 100}, new int[] {}, Voltage.ULV, NI, 50 * 100);
			addCentrifugeToMaterial(GTPP_CombType.FLUIX, new Materials[] {Materials.Fluix, Materials.Redstone, Materials.CertusQuartz, Materials.NetherQuartz}, new int[] {25 * 100, 90 * 100, 90 * 100, 90 * 100}, new int[] { 9, 1, 1, 1}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.REDSTONE, new Materials[] {Materials.Redstone, Materials.Cinnabar }, new int[] {100 * 100, 80 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.LAPIS, new Materials[] {Materials.Lapis, Materials.Sodalite, Materials.Lazurite, Materials.Calcite }, new int[] {100 * 100, 90 * 100, 90 * 100, 85 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.RUBY, new Materials[] {Materials.Ruby, Materials.Redstone }, new int[] {100 * 100, 90 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.REDGARNET, new Materials[] {Materials.GarnetRed, Materials.GarnetYellow }, new int[] {100 * 100, 75 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.YELLOWGARNET, new Materials[] {Materials.GarnetYellow, Materials.GarnetRed }, new int[] {100 * 100, 75 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.SAPPHIRE, new Materials[] {Materials.Sapphire, Materials.GreenSapphire, Materials.Almandine, Materials.Pyrope}, new int[] {100 * 100, 90 * 100, 90 * 100, 75 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.DIAMOND, new Materials[] {Materials.Diamond, Materials.Graphite}, new int[] {100 * 100, 75 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.OLIVINE, new Materials[] {Materials.Olivine, Materials.Bentonite, Materials.Magnesite, Materials.Glauconite}, new int[] {100 * 100, 90 * 100, 80 * 100, 75 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.EMERALD, new Materials[] {Materials.Emerald, Materials.Beryllium, Materials.Thorium}, new int[] {100 * 100, 85 * 100, 75 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.FIRESTONE, new Materials[] {Materials.Firestone}, new int[] {100 * 100}, new int[] {}, Voltage.ULV, ItemList.FR_RefractoryWax.get(1), 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.PYROPE, new Materials[] {Materials.Pyrope, Materials.Aluminium, Materials.Magnesium, Materials.Silicon}, new int[] {100 * 100, 75 * 100, 80 * 100, 75 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.GROSSULAR, new Materials[] {Materials.Grossular, Materials.Aluminium, Materials.Silicon}, new int[] {100 * 100, 75 * 100, 75 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
		}
		
		// Metals Line
		addProcessGT(GTPP_CombType.SLAG, new Materials[] {Materials.Salt, Materials.RockSalt, Materials.Lepidolite, Materials.Spodumene, Materials.Monazite}, Voltage.LV);
		addProcessGT(GTPP_CombType.COPPER, new Materials[] {Materials.Copper, Materials.Tetrahedrite, Materials.Chalcopyrite, Materials.Malachite, Materials.Pyrite, Materials.Stibnite}, Voltage.LV);
		addProcessGT(GTPP_CombType.TIN, new Materials[] {Materials.Tin, Materials.Cassiterite, Materials.CassiteriteSand}, Voltage.LV);
		addProcessGT(GTPP_CombType.LEAD, new Materials[] {Materials.Lead, Materials.Galena}, Voltage.LV);
		addProcessGT(GTPP_CombType.NICKEL, new Materials[] {Materials.Nickel, Materials.Garnierite, Materials.Pentlandite, Materials.Cobaltite, Materials.Wulfenite, Materials.Powellite}, Voltage.LV);
		addProcessGT(GTPP_CombType.ZINC, new Materials[] {Materials.Zinc, Materials.Sulfur}, Voltage.LV);
		addProcessGT(GTPP_CombType.SILVER, new Materials[] {Materials.Silver, Materials.Galena}, Voltage.LV);
		addProcessGT(GTPP_CombType.CRYOLITE, new Materials[] {Materials.Cryolite, Materials.Silver}, Voltage.LV);
		addProcessGT(GTPP_CombType.GOLD, new Materials[] {Materials.Gold, Materials.Magnetite}, Voltage.LV);
		addChemicalProcess(GTPP_CombType.GOLD, Materials.Magnetite, Materials.Gold, Voltage.LV);
		addProcessGT(GTPP_CombType.SULFUR, new Materials[] {Materials.Sulfur, Materials.Pyrite, Materials.Sphalerite}, Voltage.LV);
		addProcessGT(GTPP_CombType.GALLIUM, new Materials[] {Materials.Gallium, Materials.Niobium}, Voltage.LV);
		addProcessGT(GTPP_CombType.ARSENIC, new Materials[] {Materials.Arsenic, Materials.Bismuth, Materials.Antimony}, Voltage.LV);
		if (ProcessingModSupport.aEnableGCMarsMats) {
			addProcessGT(GTPP_CombType.IRON, new Materials[] {Materials.Iron, Materials.Magnetite, Materials.BrownLimonite, Materials.YellowLimonite, Materials.VanadiumMagnetite, Materials.BandedIron, Materials.Pyrite, Materials.MeteoricIron}, Voltage.LV);
			addProcessGT(GTPP_CombType.STEEL, new Materials[] {Materials.Iron, Materials.Magnetite, Materials.YellowLimonite, Materials.BrownLimonite, Materials.VanadiumMagnetite, Materials.BandedIron, Materials.Pyrite, Materials.MeteoricIron, Materials.Molybdenite, Materials.Molybdenum}, Voltage.LV);
		}else {
			addProcessGT(GTPP_CombType.IRON, new Materials[] {Materials.Iron, Materials.Magnetite, Materials.BrownLimonite, Materials.YellowLimonite, Materials.VanadiumMagnetite, Materials.BandedIron, Materials.Pyrite}, Voltage.LV);
			addProcessGT(GTPP_CombType.STEEL, new Materials[] {Materials.Iron, Materials.Magnetite, Materials.YellowLimonite, Materials.BrownLimonite, Materials.VanadiumMagnetite, Materials.BandedIron, Materials.Pyrite, Materials.Molybdenite, Materials.Molybdenum}, Voltage.LV);
		}
		addChemicalProcess(GTPP_CombType.STEEL, Materials.BrownLimonite, Materials.YellowLimonite, Voltage.LV);
		addChemicalProcess(GTPP_CombType.STEEL, Materials.YellowLimonite, Materials.BrownLimonite, Voltage.LV);
		if(GT_Mod.gregtechproxy.mNerfedCombs) {
			addCentrifugeToMaterial(GTPP_CombType.SLAG, new Materials[] {Materials.Stone, Materials.GraniteBlack, Materials.GraniteRed}, new int[] {50 * 100, 20 * 100, 20 * 100}, new int[] { 9, 9, 9}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.COPPER, new Materials[] {Materials.Copper}, new int[] {70 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.TIN, new Materials[] {Materials.Tin}, new int[] {60 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.LEAD, new Materials[] {Materials.Lead}, new int[] {45 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.IRON, new Materials[] {Materials.Iron}, new int[] {30 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.STEEL, new Materials[] {Materials.Steel}, new int[] {40 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.SILVER, new Materials[] {Materials.Silver}, new int[] {80 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.CRYOLITE, new Materials[] {Materials.Cryolite}, new int[] {80 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
		}else {
			addCentrifugeToMaterial(GTPP_CombType.SLAG, new Materials[] {Materials.Salt, Materials.RockSalt, Materials.Lepidolite, Materials.Spodumene, Materials.Monazite}, new int[] {100 * 100, 100 * 100, 100 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.COPPER, new Materials[] {Materials.Copper, Materials.Tetrahedrite, Materials.Chalcopyrite, Materials.Malachite, Materials.Pyrite, Materials.Stibnite}, new int[] {100 * 100, 85 * 100, 95 * 100, 80 * 100, 75 * 100, 65 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.TIN, new Materials[] {Materials.Tin, Materials.Cassiterite, Materials.CassiteriteSand}, new int[] {100 * 100, 85 * 100, 65 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.LEAD, new Materials[] {Materials.Lead, Materials.Galena}, new int[] {100 * 100, 75 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			if (ProcessingModSupport.aEnableGCMarsMats) {
				addCentrifugeToMaterial(GTPP_CombType.IRON, new Materials[] {Materials.Iron, Materials.Magnetite, Materials.BrownLimonite, Materials.YellowLimonite, Materials.VanadiumMagnetite, Materials.MeteoricIron}, new int[] {100 * 100, 90 * 100, 85 * 100, 85 * 100, 80 * 100, 75 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
				addCentrifugeToMaterial(GTPP_CombType.LEAD, new Materials[] {Materials.Steel, Materials.Magnetite, Materials.VanadiumMagnetite, Materials.Molybdenite, Materials.Molybdenum, Materials.MeteoricIron }, new int[] {100 * 100, 90 * 100, 80 * 100, 65 * 100, 65 * 100, 75 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			}else {
				addCentrifugeToMaterial(GTPP_CombType.IRON, new Materials[] {Materials.Iron, Materials.Magnetite, Materials.BrownLimonite, Materials.YellowLimonite, Materials.VanadiumMagnetite, Materials.BandedIron}, new int[] {100 * 100, 90 * 100, 85 * 100, 85 * 100, 80 * 100, 85 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
				addCentrifugeToMaterial(GTPP_CombType.STEEL, new Materials[] {Materials.Steel, Materials.Magnetite, Materials.VanadiumMagnetite, Materials.BandedIron, Materials.Molybdenite, Materials.Molybdenum }, new int[] {100 * 100, 90 * 100, 80 * 100, 85 * 100, 65 * 100, 65 * 100 }, new int[] {}, Voltage.ULV, NI, 30 * 100);
			}
			addCentrifugeToMaterial(GTPP_CombType.NICKEL, new Materials[] {Materials.Nickel, Materials.Garnierite, Materials.Pentlandite, Materials.Cobaltite, Materials.Wulfenite, Materials.Powellite}, new int[] {100 * 100, 85 * 100, 85 * 100, 80 * 100, 75 * 100, 75 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.ZINC, new Materials[] {Materials.Zinc, Materials.Sphalerite, Materials.Sulfur}, new int[] {100 * 100, 80 * 100, 75 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.SILVER, new Materials[] {Materials.Silver, Materials.Galena}, new int[] {100 * 100, 80 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.CRYOLITE, new Materials[] {Materials.Cryolite, Materials.Silver}, new int[] {100 * 100, 80 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.GOLD, new Materials[] {Materials.Gold}, new int[] {100 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.SULFUR, new Materials[] {Materials.Sulfur, Materials.Pyrite, Materials.Sphalerite}, new int[] {100 * 100, 90 * 100, 80 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.GALLIUM, new Materials[] {Materials.Gallium, Materials.Niobium}, new int[] { 80 * 100, 75 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.ARSENIC, new Materials[] {Materials.Arsenic, Materials.Bismuth, Materials.Antimony}, new int[] {80 * 100, 70 * 100, 70 * 100}, new int[] {}, Voltage.ULV , NI, 30 * 100);
		}

		// Rare Metals Line
		addProcessGT(GTPP_CombType.BAUXITE, new Materials[] {Materials.Bauxite, Materials.Aluminium}, Voltage.LV);
		addProcessGT(GTPP_CombType.ALUMINIUM, new Materials[] {Materials.Aluminium, Materials.Bauxite}, Voltage.LV);
		addProcessGT(GTPP_CombType.MANGANESE, new Materials[] {Materials.Manganese, Materials.Grossular, Materials.Spessartine, Materials.Pyrolusite, Materials.Tantalite}, Voltage.LV);
		addProcessGT(GTPP_CombType.TITANIUM, new Materials[] {Materials.Titanium, Materials.Ilmenite, Materials.Bauxite, Materials.Rutile}, Voltage.EV);
		addProcessGT(GTPP_CombType.MAGNESIUM, new Materials[] {Materials.Magnesium, Materials.Magnesite}, Voltage.LV);
		addProcessGT(GTPP_CombType.CHROME, new Materials[] {Materials.Chrome, Materials.Ruby, Materials.Chromite, Materials.Redstone, Materials.Neodymium, Materials.Bastnasite}, Voltage.HV);
		addProcessGT(GTPP_CombType.TUNGSTEN, new Materials[] {Materials.Tungsten, Materials.Tungstate, Materials.Scheelite, Materials.Lithium}, Voltage.IV);
		addProcessGT(GTPP_CombType.PLATINUM, new Materials[] {Materials.Platinum, Materials.Cooperite, Materials.Palladium}, Voltage.HV);
		addProcessGT(GTPP_CombType.MOLYBDENUM, new Materials[] {Materials.Molybdenum, Materials.Molybdenite, Materials.Powellite, Materials.Wulfenite}, Voltage.LV);
		addChemicalProcess(GTPP_CombType.MOLYBDENUM, Materials.Osmium, Materials.Osmium, Voltage.IV);
		addAutoclaveProcess(GTPP_CombType.MOLYBDENUM, Materials.Osmium, Voltage.IV, 5);
		addProcessGT(GTPP_CombType.IRIDIUM, new Materials[] {Materials.Iridium, Materials.Osmium}, Voltage.IV);
		addProcessGT(GTPP_CombType.OSMIUM, new Materials[] {Materials.Osmium, Materials.Iridium}, Voltage.IV);
		addProcessGT(GTPP_CombType.LITHIUM, new Materials[] {Materials.Lithium, Materials.Aluminium}, Voltage.MV);
		addProcessGT(GTPP_CombType.SALT, new Materials[] {Materials.Salt, Materials.RockSalt, Materials.Saltpeter}, Voltage.MV);
		addProcessGT(GTPP_CombType.ELECTROTINE, new Materials[] {Materials.Electrotine, Materials.Electrum, Materials.Redstone}, Voltage.MV);
		if(GT_Mod.gregtechproxy.mNerfedCombs) {
			addCentrifugeToItemStack(GTPP_CombType.SALT, new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Salt, 1), GT_ModHandler.getModItem(MOD_ID_DC, "item.EdibleSalt", 1L, 0), ItemList.FR_Wax.get(1) }, new int[] {100 * 100, 50 * 100, 50 * 100}, Voltage.MV, 160);
		}else {
			addCentrifugeToMaterial(GTPP_CombType.BAUXITE, new Materials[] {Materials.Bauxite, Materials.Aluminium}, new int[] { 75 * 100, 55 * 100}, new int[] {}, Voltage.ULV , NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.ALUMINIUM, new Materials[] {Materials.Aluminium, Materials.Bauxite}, new int[] { 60 * 100, 80 * 100}, new int[] {}, Voltage.ULV , NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.MANGANESE, new Materials[] {Materials.Manganese, Materials.Grossular, Materials.Spessartine, Materials.Pyrolusite, Materials.Tantalite}, new int[] { 30 * 100, 100 * 100, 100 * 100, 100 * 100, 100 * 100}, new int[] {}, Voltage.ULV , NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.TITANIUM, new Materials[] {Materials.Titanium, Materials.Ilmenite, Materials.Bauxite, Materials.Rutile}, new int[] { 90 * 100, 80 * 100, 75 * 100, 75 * 100}, new int[] {}, Voltage.EV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.MAGNESIUM, new Materials[] {Materials.Magnesium, Materials.Magnesite}, new int[] { 100 * 100, 80 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.CHROME, new Materials[] {Materials.Chrome, Materials.Ruby, Materials.Chromite, Materials.Redstone, Materials.Neodymium, Materials.Bastnasite}, new int[] { 50 * 100, 100 * 100, 50 * 100, 100 * 100, 80 * 100, 80 * 100}, new int[] {}, Voltage.HV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.TUNGSTEN, new Materials[] {Materials.Tungsten, Materials.Tungstate, Materials.Scheelite, Materials.Lithium}, new int[] { 50 * 100, 80 * 100, 75 * 100, 75 * 100}, new int[] {}, Voltage.IV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.PLATINUM, new Materials[] {Materials.Platinum, Materials.Cooperite, Materials.Palladium}, new int[] { 40 * 100, 40 * 100, 40 * 100}, new int[] {}, Voltage.HV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.MOLYBDENUM, new Materials[] {Materials.Molybdenum, Materials.Molybdenite, Materials.Powellite, Materials.Wulfenite}, new int[] { 100 * 100, 80 * 100, 75 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.IRIDIUM, new Materials[] {Materials.Iridium, Materials.Osmium}, new int[] { 20 * 100, 15 * 100}, new int[] {}, Voltage.IV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.OSMIUM, new Materials[] {Materials.Osmium, Materials.Iridium}, new int[] { 25 * 100, 15 * 100}, new int[] {}, Voltage.IV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.LITHIUM, new Materials[] {Materials.Lithium, Materials.Aluminium}, new int[] { 85 * 100, 75 * 100}, new int[] {}, Voltage.MV, NI, 30 * 100);
			addCentrifugeToItemStack(GTPP_CombType.SALT, new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Salt, 1), GT_ModHandler.getModItem(MOD_ID_DC, "item.EdibleSalt", 1L, 0),GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.RockSalt, 1), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Saltpeter, 1), ItemList.FR_Wax.get(1) }, new int[] {100 * 100, 50 * 100, 75 * 100, 65 * 100, 50 * 100}, Voltage.MV, 160);
			addCentrifugeToMaterial(GTPP_CombType.ELECTROTINE, new Materials[] {Materials.Electrotine, Materials.Electrum, Materials.Redstone}, new int[] { 80, 75, 65}, new int[] {}, Voltage.MV, NI, 30 * 100);
		}
		
		//Radioactive Line
		addProcessGT(GTPP_CombType.ALMANDINE, new Materials[] {Materials.Almandine, Materials.Pyrope, Materials.Sapphire, Materials.GreenSapphire}, Voltage.LV);
		addProcessGT(GTPP_CombType.URANIUM, new Materials[] {Materials.Uranium, Materials.Pitchblende, Materials.Uraninite, Materials.Uranium235}, Voltage.EV);
		addProcessGT(GTPP_CombType.PLUTONIUM,new Materials[] {Materials.Plutonium, Materials.Uranium235}, Voltage.EV);
		addChemicalProcess(GTPP_CombType.PLUTONIUM, Materials.Uranium235, Materials.Plutonium, Voltage.EV);
		addProcessGT(GTPP_CombType.NAQUADAH,new Materials[] {Materials.Naquadah, Materials.NaquadahEnriched, Materials.Naquadria}, Voltage.IV);
		addProcessGT(GTPP_CombType.NAQUADRIA,new Materials[] {Materials.Naquadria, Materials.NaquadahEnriched, Materials.Naquadah}, Voltage.LUV);
		addProcessGT(GTPP_CombType.THORIUM,new Materials[] {Materials.Thorium, Materials.Uranium, Materials.Coal}, Voltage.EV);
		addProcessGT(GTPP_CombType.LUTETIUM,new Materials[] {Materials.Lutetium, Materials.Thorium}, Voltage.IV);
		addProcessGT(GTPP_CombType.AMERICIUM,new Materials[] {Materials.Americium, Materials.Lutetium}, Voltage.LUV);
		addProcessGT(GTPP_CombType.NEUTRONIUM,new Materials[] {Materials.Neutronium, Materials.Americium}, Voltage.UV);
		if(!GT_Mod.gregtechproxy.mNerfedCombs) {
			addCentrifugeToMaterial(GTPP_CombType.ALMANDINE, new Materials[] {Materials.Almandine, Materials.Pyrope, Materials.Sapphire, Materials.GreenSapphire}, new int[] { 90 * 100, 80 * 100, 75 * 100, 75 * 100}, new int[] {}, Voltage.ULV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.URANIUM, new Materials[] {Materials.Uranium, Materials.Pitchblende, Materials.Uraninite, Materials.Uranium235}, new int[] { 50 * 100, 65 * 100, 75 * 100, 50 * 100}, new int[] {}, Voltage.EV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.PLUTONIUM,new Materials[] {Materials.Plutonium, Materials.Uranium235}, new int[] {10, 5}, new int[] {}, Voltage.EV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.NAQUADAH,new Materials[] {Materials.Naquadah, Materials.NaquadahEnriched, Materials.Naquadria}, new int[] {10 * 100, 5 * 100, 5 * 100}, new int[] {}, Voltage.IV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.NAQUADRIA,new Materials[] {Materials.Naquadria, Materials.NaquadahEnriched, Materials.Naquadah}, new int[] {10 * 100, 10 * 100, 15 * 100}, new int[] {}, Voltage.LUV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.THORIUM,new Materials[] {Materials.Thorium, Materials.Uranium, Materials.Coal}, new int[] {75 * 100, 75 * 100 * 100, 95 * 100}, new int[] {}, Voltage.EV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.LUTETIUM,new Materials[] {Materials.Lutetium, Materials.Thorium}, new int[] {35 * 100, 55 * 100}, new int[] {}, Voltage.IV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.AMERICIUM,new Materials[] {Materials.Americium, Materials.Lutetium}, new int[] {25 * 100, 45 * 100}, new int[] {}, Voltage.LUV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.NEUTRONIUM,new Materials[] {Materials.Neutronium, Materials.Americium}, new int[] {15 * 100, 35 * 100}, new int[] {}, Voltage.UV, NI, 30 * 100);
		}
		
		// Twilight
		addCentrifugeToItemStack(GTPP_CombType.NAGA, new ItemStack[] { GT_ModHandler.getModItem("MagicBees", "propolis", 1L, 4), GT_ModHandler.getModItem(MOD_ID_DC, "item.NagaScaleChip", 1L, 0),  GT_ModHandler.getModItem(MOD_ID_DC, "item.NagaScaleFragment", 1L, 0), ItemList.FR_Wax.get(1)}, new int[]{5 * 100, 33 * 100, 8 * 100, 30 * 100}, Voltage.MV);
		addCentrifugeToItemStack(GTPP_CombType.LICH, new ItemStack[] { GT_ModHandler.getModItem("MagicBees", "propolis", 1L, 5), GT_ModHandler.getModItem(MOD_ID_DC, "item.LichBoneChip", 1L, 0),  GT_ModHandler.getModItem(MOD_ID_DC, "item.LichBoneFragment", 1L, 0), ItemList.FR_Wax.get(1)}, new int[]{5 * 100, 33 * 100, 8 * 100, 30 * 100}, Voltage.HV);
		addCentrifugeToItemStack(GTPP_CombType.HYDRA, new ItemStack[] { GT_ModHandler.getModItem("MagicBees", "propolis", 1L, 1), GT_ModHandler.getModItem(MOD_ID_DC, "item.FieryBloodDrop", 1L, 0),  GTPP_Bees.drop.getStackForType(GTPP_DropType.HYDRA), ItemList.FR_Wax.get(1)}, new int[]{5 * 100, 33 * 100, 8 * 100, 30 * 100}, Voltage.HV);
		addCentrifugeToItemStack(GTPP_CombType.URGHAST, new ItemStack[] { GT_ModHandler.getModItem("MagicBees", "propolis", 1L, 2), GT_ModHandler.getModItem(MOD_ID_DC, "item.CarminiteChip", 1L, 0),  GT_ModHandler.getModItem(MOD_ID_DC, "item.CarminiteFragment", 1L, 0), ItemList.FR_Wax.get(1)}, new int[]{5 * 100, 33 * 100, 8 * 100, 30 * 100}, Voltage.EV);
		addCentrifugeToItemStack(GTPP_CombType.SNOWQUEEN, new ItemStack[] { GT_ModHandler.getModItem("MagicBees", "propolis", 1L, 3), GT_ModHandler.getModItem(MOD_ID_DC, "item.SnowQueenBloodDrop", 1L, 0),   GTPP_Bees.drop.getStackForType(GTPP_DropType.SNOW_QUEEN), ItemList.FR_Wax.get(1)}, new int[]{5 * 100, 33 * 100, 8 * 100, 30 * 100}, Voltage.EV);

		// HEE
		addCentrifugeToItemStack(GTPP_CombType.ENDDUST, new ItemStack[] { GT_ModHandler.getModItem("MagicBees", "wax", 1L, 0), GTPP_Bees.propolis.getStackForType(GTPP_PropolisType.End), GTPP_Bees.drop.getStackForType(GTPP_DropType.ENDERGOO),  }, new int[]{20 * 100, 15 * 100, 10 * 100}, Voltage.HV);
		addCentrifugeToItemStack(GTPP_CombType.STARDUST, new ItemStack[] { GT_ModHandler.getModItem("MagicBees", "wax", 1L, 0), GTPP_Bees.propolis.getStackForType(GTPP_PropolisType.Stardust), GTPP_Bees.drop.getStackForType(GTPP_DropType.ENDERGOO) }, new int[]{20 * 100, 15 * 100, 10 * 100}, Voltage.HV);
		addCentrifugeToItemStack(GTPP_CombType.ECTOPLASMA, new ItemStack[] { GT_ModHandler.getModItem("MagicBees", "wax", 1L, 0), GTPP_Bees.propolis.getStackForType(GTPP_PropolisType.Ectoplasma), GTPP_Bees.drop.getStackForType(GTPP_DropType.ENDERGOO) }, new int[]{25 * 100, 10 * 100, 15 * 100}, Voltage.EV);
		addCentrifugeToItemStack(GTPP_CombType.ARCANESHARD, new ItemStack[] { GT_ModHandler.getModItem("MagicBees", "wax", 1L, 0), GTPP_Bees.propolis.getStackForType(GTPP_PropolisType.Arcaneshard), GTPP_Bees.drop.getStackForType(GTPP_DropType.ENDERGOO) }, new int[]{25 * 100, 10 * 100, 15 * 100}, Voltage.EV);
		addCentrifugeToItemStack(GTPP_CombType.DRAGONESSENCE, new ItemStack[] { GT_ModHandler.getModItem("MagicBees", "wax", 1L, 0), GTPP_Bees.propolis.getStackForType(GTPP_PropolisType.Dragonessence), GTPP_Bees.drop.getStackForType(GTPP_DropType.ENDERGOO) }, new int[]{30 * 100, (int) (7.5 * 100), 20 * 100}, Voltage.IV);
		addCentrifugeToItemStack(GTPP_CombType.ENDERMAN, new ItemStack[] { GT_ModHandler.getModItem("MagicBees", "wax", 1L, 0), GTPP_Bees.propolis.getStackForType(GTPP_PropolisType.Enderman), GTPP_Bees.drop.getStackForType(GTPP_DropType.ENDERGOO) }, new int[]{3000, 750, 2000}, Voltage.IV);
		addCentrifugeToItemStack(GTPP_CombType.SILVERFISH, new ItemStack[] { GT_ModHandler.getModItem("MagicBees", "wax", 1L, 0), GTPP_Bees.propolis.getStackForType(GTPP_PropolisType.Silverfish), GTPP_Bees.drop.getStackForType(GTPP_DropType.ENDERGOO), new ItemStack(Items.spawn_egg, 1,60) }, new int[]{25 * 100, 10 * 100, 20 * 100, 15 * 100}, Voltage.EV);
		addProcessGT(GTPP_CombType.ENDIUM,new Materials[] {Materials.HeeEndium}, Voltage.HV);
		if(!GT_Mod.gregtechproxy.mNerfedCombs) {
			addCentrifugeToMaterial(GTPP_CombType.ENDIUM,new Materials[] {Materials.HeeEndium}, new int[] {50 * 100}, new int[] {}, Voltage.HV, GT_ModHandler.getModItem("MagicBees", "wax", 1L, 0), 20 * 100);
		}
		addCentrifugeToItemStack(GTPP_CombType.RUNEI, new ItemStack[] { GT_ModHandler.getModItem("MagicBees", "wax", 1L, 0), GT_ModHandler.getModItem(MOD_ID_DC, "item.RuneOfPowerFragment", 1L, 0),   GT_ModHandler.getModItem(MOD_ID_DC, "item.RuneOfAgilityFragment", 1L, 0),  GT_ModHandler.getModItem(MOD_ID_DC, "item.RuneOfVigorFragment", 1L, 0),  GT_ModHandler.getModItem(MOD_ID_DC, "item.RuneOfDefenseFragment", 1L, 0), GT_ModHandler.getModItem(MOD_ID_DC, "item.RuneOfMagicFragment", 1L, 0) }, new int[]{25 * 100, 5 * 100, 5 * 100, 5 * 100, 5 * 100, 5 * 100}, Voltage.IV);
		addCentrifugeToItemStack(GTPP_CombType.RUNEII, new ItemStack[] { GT_ModHandler.getModItem("MagicBees", "wax", 1L, 0), GT_ModHandler.getModItem(MOD_ID_DC, "item.RuneOfVoidFragment", 1L, 0) }, new int[]{50 * 100, (int) (2.5 * 100)}, Voltage.IV);
		addCentrifugeToItemStack(GTPP_CombType.FIREESSENSE, new ItemStack[] { GT_ModHandler.getModItem("MagicBees", "wax", 1L, 0), GTPP_Bees.propolis.getStackForType(GTPP_PropolisType.Fireessence), GTPP_Bees.drop.getStackForType(GTPP_DropType.ENDERGOO) }, new int[]{30 * 100, (int) (7.5 * 100), 20 * 100}, Voltage.IV);

		//Space Line
		addCentrifugeToItemStack(GTPP_CombType.SPACE, new ItemStack[] { ItemList.FR_Wax.get(1L), ItemList.FR_RefractoryWax.get(1L), GTPP_Bees.drop.getStackForType(GTPP_DropType.OXYGEN), GT_ModHandler.getModItem(MOD_ID_DC, "item.CoinSpace", 1L, 0)}, new int[]{50 * 100, 30 * 100, 15 * 100, 5 * 100}, Voltage.HV);
		addProcessGT(GTPP_CombType.METEORICIRON, new Materials[] {Materials.MeteoricIron, Materials.Iron}, Voltage.HV);
		addProcessGT(GTPP_CombType.DESH, new Materials[] {Materials.Desh, Materials.Titanium}, Voltage.EV);
		addProcessGT(GTPP_CombType.LEDOX, new Materials[] {Materials.Ledox, Materials.CallistoIce, Materials.Lead}, Voltage.EV);
		addProcessGT(GTPP_CombType.CALLISTOICE, new Materials[] {Materials.CallistoIce, Materials.Ledox, Materials.Lead}, Voltage.IV);
		addProcessGT(GTPP_CombType.MYTRYL, new Materials[] {Materials.Mytryl, Materials.Mithril}, Voltage.IV);
		addProcessGT(GTPP_CombType.QUANTIUM, new Materials[] {Materials.Quantium, Materials.Osmium}, Voltage.IV);
		addProcessGT(GTPP_CombType.ORIHARUKON, new Materials[] {Materials.Oriharukon, Materials.Lead}, Voltage.IV);
		addProcessGT(GTPP_CombType.MYSTERIOUSCRYSTAL, new Materials[] {Materials.MysteriousCrystal, Materials.Emerald}, Voltage.LUV);
		addCentrifugeToMaterial(GTPP_CombType.MYSTERIOUSCRYSTAL, new Materials[] {Materials.MysteriousCrystal, Materials.Emerald}, new int[] {(GT_Mod.gregtechproxy.mNerfedCombs ? 10 : 40) * 100, (GT_Mod.gregtechproxy.mNerfedCombs ? 15 : 50) * 100}, new int[] {}, Voltage.LUV, 512, NI, 50 * 100);
		addProcessGT(GTPP_CombType.BLACKPLUTONIUM, new Materials[] {Materials.BlackPlutonium, Materials.Plutonium}, Voltage.LUV);
		addProcessGT(GTPP_CombType.TRINIUM, new Materials[] {Materials.Trinium, Materials.Iridium}, Voltage.ZPM);
		if(!GT_Mod.gregtechproxy.mNerfedCombs) {
			addCentrifugeToMaterial(GTPP_CombType.METEORICIRON, new Materials[] {Materials.MeteoricIron, Materials.Iron}, new int[] {85 * 100, 100 * 100}, new int[] {}, Voltage.HV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.DESH, new Materials[] {Materials.Desh, Materials.Titanium}, new int[] {75 * 100, 50 * 100}, new int[] {}, Voltage.EV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.LEDOX, new Materials[] {Materials.Ledox, Materials.CallistoIce, Materials.Lead}, new int[] {65 * 100, 55 * 100, 85 *100}, new int[] {}, Voltage.EV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.CALLISTOICE, new Materials[] {Materials.CallistoIce, Materials.Ledox, Materials.Lead}, new int[] {65 * 100, 75 * 100, 100 *100}, new int[] {}, Voltage.IV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.MYTRYL, new Materials[] {Materials.Mytryl, Materials.Mithril}, new int[] {55 * 100, 50 * 100}, new int[] {}, Voltage.IV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.QUANTIUM, new Materials[] {Materials.Quantium, Materials.Osmium}, new int[] {50 * 100, 60 * 100}, new int[] {}, Voltage.IV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.ORIHARUKON, new Materials[] {Materials.Oriharukon, Materials.Lead}, new int[] {50 * 100, 75 * 100}, new int[] {}, Voltage.IV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.BLACKPLUTONIUM, new Materials[] {Materials.BlackPlutonium, Materials.Plutonium}, new int[] {25 * 100, 50 * 100}, new int[] {}, Voltage.LUV, NI, 30 * 100);
			addCentrifugeToMaterial(GTPP_CombType.TRINIUM, new Materials[] {Materials.Trinium, Materials.Iridium}, new int[] {35 * 100, 45 * 100}, new int[] {}, Voltage.ZPM, NI, 30 * 100);
		}

		//Planet Line
		addCentrifugeToItemStack(GTPP_CombType.MOON, new ItemStack[] {ItemList.FR_Wax.get(1L), GT_ModHandler.getModItem(MOD_ID_DC, "item.MoonStoneDust", 1L, 0)}, new int[]{50 * 100, 30 * 100 }, Voltage.MV, 300);
		addCentrifugeToItemStack(GTPP_CombType.MARS, new ItemStack[] {ItemList.FR_Wax.get(1L), GT_ModHandler.getModItem(MOD_ID_DC, "item.MarsStoneDust", 1L, 0)}, new int[]{50 * 100, 30 * 100 }, Voltage.HV, 300);
		addCentrifugeToItemStack(GTPP_CombType.JUPITER, new ItemStack[] { GT_ModHandler.getModItem(MOD_ID_DC, "item.IoStoneDust", 1L, 0), GT_ModHandler.getModItem(MOD_ID_DC, "item.EuropaIceDust", 1L, 0), GT_ModHandler.getModItem(MOD_ID_DC, "item.EuropaStoneDust", 1L, 0), GT_ModHandler.getModItem(MOD_ID_DC, "item.GanymedeStoneDust", 1L, 0), GT_ModHandler.getModItem(MOD_ID_DC, "item.CallistoStoneDust", 1L, 0), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.CallistoIce, 1L), ItemList.FR_Wax.get(1L)}, new int[]{30 * 100, 30 * 100, 30 * 100, 30 * 100, 30 * 100, 5 * 100, 50 * 100 }, Voltage.HV, 300);
		addCentrifugeToItemStack(GTPP_CombType.MERCURY, new ItemStack[] {ItemList.FR_Wax.get(1L), GT_ModHandler.getModItem(MOD_ID_DC, "item.MercuryCoreDust", 1L, 0), GT_ModHandler.getModItem(MOD_ID_DC, "item.MercuryStoneDust", 1L, 0)}, new int[]{50 * 100, 30 * 100, 30 * 100 }, Voltage.EV, 300);
		addCentrifugeToItemStack(GTPP_CombType.VENUS, new ItemStack[] {ItemList.FR_Wax.get(1L), GT_ModHandler.getModItem(MOD_ID_DC, "item.VenusStoneDust", 1L, 0)}, new int[]{50 * 100, 30 * 100 }, Voltage.EV, 300);
		addCentrifugeToItemStack(GTPP_CombType.SATURN, new ItemStack[] {ItemList.FR_Wax.get(1L), GT_ModHandler.getModItem(MOD_ID_DC, "item.EnceladusStoneDust", 1L, 0), GT_ModHandler.getModItem(MOD_ID_DC, "item.TitanStoneDust", 1L, 0)}, new int[]{50 * 100, 30 * 100, 30 * 100 }, Voltage.IV, 300);
		addCentrifugeToItemStack(GTPP_CombType.URANUS, new ItemStack[] {ItemList.FR_Wax.get(1L), GT_ModHandler.getModItem(MOD_ID_DC, "item.MirandaStoneDust", 1L, 0), GT_ModHandler.getModItem(MOD_ID_DC, "item.OberonStoneDust", 1L, 0)}, new int[]{50 * 100, 30 * 100, 30 * 100 }, Voltage.IV, 300);
		addCentrifugeToItemStack(GTPP_CombType.NEPTUN, new ItemStack[] {ItemList.FR_Wax.get(1L), GT_ModHandler.getModItem(MOD_ID_DC, "item.ProteusStoneDust", 1L, 0), GT_ModHandler.getModItem(MOD_ID_DC, "item.TritonStoneDust", 1L, 0)}, new int[]{50 * 100, 30 * 100, 30 * 100 }, Voltage.IV, 300);
		addCentrifugeToItemStack(GTPP_CombType.PLUTO, new ItemStack[] {ItemList.FR_Wax.get(1L), GT_ModHandler.getModItem(MOD_ID_DC, "item.PlutoStoneDust", 1L, 0), GT_ModHandler.getModItem(MOD_ID_DC, "item.PlutoIceDust", 1L, 0)}, new int[]{50 * 100, 30 * 100, 30 * 100 }, Voltage.LUV, 300);
		addCentrifugeToItemStack(GTPP_CombType.HAUMEA, new ItemStack[] {ItemList.FR_Wax.get(1L), GT_ModHandler.getModItem(MOD_ID_DC, "item.HaumeaStoneDust", 1L, 0)}, new int[]{50 * 100, 30 * 100 }, Voltage.LUV, 300);
		addCentrifugeToItemStack(GTPP_CombType.MAKEMAKE, new ItemStack[] {ItemList.FR_Wax.get(1L), GT_ModHandler.getModItem(MOD_ID_DC, "item.MakeMakeStoneDust", 1L, 0)}, new int[]{50 * 100, 30 * 100 }, Voltage.LUV, 300);
		addCentrifugeToItemStack(GTPP_CombType.CENTAURI, new ItemStack[] {ItemList.FR_Wax.get(1L), GT_ModHandler.getModItem(MOD_ID_DC, "item.CentauriASurfaceDust", 1L, 0), GT_ModHandler.getModItem(MOD_ID_DC, "item.CentauriAStoneDust", 1L, 0)}, new int[]{50 * 100, 30 * 100, 30 * 100 }, Voltage.ZPM, 300);
		addCentrifugeToItemStack(GTPP_CombType.TCETI, new ItemStack[] {ItemList.FR_Wax.get(1L), GT_ModHandler.getModItem(MOD_ID_DC, "item.TCetiEStoneDust", 1L, 0)}, new int[]{50 * 100, 30 * 100 }, Voltage.ZPM, 300);
		addCentrifugeToItemStack(GTPP_CombType.BARNARDA, new ItemStack[] {ItemList.FR_Wax.get(1L), GT_ModHandler.getModItem(MOD_ID_DC, "item.BarnardaEStoneDust", 1L, 0), GT_ModHandler.getModItem(MOD_ID_DC, "item.BarnardaFStoneDust", 1L, 0)}, new int[]{50 * 100, 30 * 100, 30 * 100 }, Voltage.ZPM, 300);
		addCentrifugeToItemStack(GTPP_CombType.VEGA, new ItemStack[] {ItemList.FR_Wax.get(1L), GT_ModHandler.getModItem(MOD_ID_DC, "item.VegaBStoneDust", 1L, 0)}, new int[]{50 * 100, 30 * 100 }, Voltage.ZPM, 300);

		//Infinity Line
		addCentrifugeToMaterial(GTPP_CombType.COSMICNEUTRONIUM, new Materials[] {Materials.CosmicNeutronium, Materials.Neutronium}, new int[] {(int) (0.5 * 100), 1 * 100}, new int[] {}, Voltage.UHV, 12000, NI, 50 * 100);
		addCentrifugeToMaterial(GTPP_CombType.INFINITYCATALYST, new Materials[] {Materials.InfinityCatalyst, Materials.Neutronium}, new int[] {(int) (0.05 * 100), 1 * 100}, new int[] {}, Voltage.UEV, 48000, NI, 50 * 100);
		addCentrifugeToMaterial(GTPP_CombType.INFINITY, new Materials[] {Materials.Infinity, Materials.InfinityCatalyst}, new int[] {(int) (0.01 * 100), (int) (0.05 * 100)}, new int[] {}, Voltage.UIV, 96000, NI, 50 * 100);
	}
	
	/**
	 * Currently use for STEEL, GOLD, MOLYBDENUM, PLUTONIUM
	 * **/
	public void addChemicalProcess(GTPP_CombType comb, Materials aInMaterial, Materials aOutMaterial, Voltage volt){
		if(GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aOutMaterial, 4) == NI) return;
		RA.addChemicalRecipe(GT_Utility.copyAmount(9, getStackForType(comb)), GT_OreDictUnificator.get(OrePrefixes.crushed, aInMaterial, 1), volt.getComplexChemical(), aInMaterial.mOreByProducts.isEmpty() ? null : aInMaterial.mOreByProducts.get(0).getMolten(144), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aOutMaterial, 4), NI, volt.getComplexTime(), volt.getChemicalEnergy(), volt.compareTo(Voltage.IV) > 0);
	}
	
	/**
	 * Currently only used for GTPP_CombType.MOLYBDENUM
	 * @param circuitNumber should not conflict with addProcessGT
	 *
	 * **/
	public void addAutoclaveProcess(GTPP_CombType comb, Materials aMaterial, Voltage volt, int circuitNumber){
		if(GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial, 4) == NI) return;
		RA.addAutoclaveRecipe(GT_Utility.copyAmount(9, getStackForType(comb)), GT_Utility.getIntegratedCircuit(circuitNumber), Materials.UUMatter.getFluid(Math.max(1, ((aMaterial.getMass()+volt.getUUAmplifier())/10))), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial, 4), 10000, (int) (aMaterial.getMass() * 128), volt.getAutoClaveEnergy(), volt.compareTo(Voltage.HV) > 0);
	}
	
	/**
	 * this only adds Chemical and AutoClave process.
	 * If you need Centrifuge recipe. use  addCentrifugeToMaterial or addCentrifugeToItemStack
	 * @param volt This determine the required Tier of process for this recipes. This decide the required aEU/t, progress time, required additional UU-Matter, requirement of cleanRoom, needed fluid stack for Chemical.
	 * @param aMaterial result of Material that should be generated by this process.
	 * **/
	public void addProcessGT(GTPP_CombType comb, Materials[] aMaterial, Voltage volt) {
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
	public void addCentrifugeToMaterial(GTPP_CombType comb, Materials[] aMaterial, int[] chance, int[] stackSize, Voltage volt, ItemStack beeWax, int waxChance) {
		addCentrifugeToMaterial(comb, aMaterial, chance, stackSize, volt, volt.getSimpleTime(), beeWax, waxChance);
	}
	public void addCentrifugeToMaterial(GTPP_CombType comb, Materials[] aMaterial, int[] chance, int[] stackSize, Voltage volt, int duration, ItemStack beeWax, int waxChance) {
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
	public void addCentrifugeToItemStack(GTPP_CombType comb, ItemStack[] aItem, int[] chance, Voltage volt) {
		addCentrifugeToItemStack(comb, aItem, chance, volt, volt.getSimpleTime());
	}
	public void addCentrifugeToItemStack(GTPP_CombType comb, ItemStack[] aItem, int[] chance, Voltage volt, int duration) {
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

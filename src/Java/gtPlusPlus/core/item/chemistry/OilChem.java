package gtPlusPlus.core.item.chemistry;

import java.util.ArrayList;

import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.minecraft.ItemPackage;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.oredict.OreDictionary;

public class OilChem extends ItemPackage {

	/**
	 * Fluids
	 */

	// Poop Juice
	public static Fluid PoopJuice;
	// Manure Slurry
	public static Fluid ManureSlurry;
	// Fertile Manure Slurry
	public static Fluid FertileManureSlurry;

	/**
	 * Items
	 */

	// Manure Byproducts
	public static Item dustManureByproducts;
	// Organic Fertilizer
	public static Item dustOrganicFertilizer;
	// Dirt
	public static Item dustDirt;

	// Poop Juice
	// vv - Centrifuge
	// Manure Slurry && Manure Byproducts -> (Elements) Centrifuge to several tiny
	// piles
	// vv - Chem Reactor - Add Peat, Meat
	// Organic Fertilizer
	// vv - Dehydrate
	// Fertilizer

	// Poop Juice
	// vv - Mixer - Add Blood, Bone, Meat (1000L Poo, 200L Blood, x2 Bone, x3 Meat)
	// Fertile Manure Slurry
	// vv - Chem Reactor - Add Peat x1.5
	// Organic Fertilizer x3
	// vv - Dehydrate
	// Fertilizer


	@Override
	public void items() {
		// Nitrogen, Ammonium Nitrate, Phosphates, Calcium, Copper, Carbon
		dustManureByproducts = ItemUtils.generateSpecialUseDusts("ManureByproducts", "Manure Byproduct",
				"(N2H4O3)N2P2Ca3CuC8", Utils.rgbtoHexValue(110, 75, 25))[0];

		// Basically Guano
		dustOrganicFertilizer = ItemUtils.generateSpecialUseDusts("OrganicFertilizer", "Organic Fertilizer",
				"Ca5(PO4)3(OH)", Utils.rgbtoHexValue(240, 240, 240))[0];

		// Dirt Dust :)
		dustDirt = ItemUtils.generateSpecialUseDusts("Dirt", "Dried Earth", Utils.rgbtoHexValue(65, 50, 15))[0];		
	}

	@Override
	public void blocks() {
		// None yet
	}

	@Override
	public void fluids() {
		// Sewage
		PoopJuice = FluidUtils.generateFluidNonMolten("raw.waste", "Raw Animal Waste", 32 + 175,
				new short[] { 100, 70, 30, 100 }, null, null, 0, true);

		// Sewage
		ManureSlurry = FluidUtils.generateFluidNonMolten("manure.slurry", "Manure Slurry", 39 + 175,
				new short[] { 75, 45, 15, 100 }, null, null, 0, true);

		// Sewage
		FertileManureSlurry = FluidUtils.generateFluidNonMolten("fertile.manure.slurry", "Fertile Manure Slurry",
				45 + 175, new short[] { 65, 50, 15, 100 }, null, null, 0, true);		
	}

	private static AutoMap<ItemStack> mMeats = new AutoMap<ItemStack>();
	private static AutoMap<ItemStack> mFish = new AutoMap<ItemStack>();
	private static AutoMap<ItemStack> mFruits = new AutoMap<ItemStack>();
	private static AutoMap<ItemStack> mVege = new AutoMap<ItemStack>();
	private static AutoMap<ItemStack> mNuts = new AutoMap<ItemStack>();
	private static AutoMap<ItemStack> mSeeds = new AutoMap<ItemStack>();
	private static AutoMap<ItemStack> mPeat = new AutoMap<ItemStack>();
	private static AutoMap<ItemStack> mBones = new AutoMap<ItemStack>();
	private static AutoMap<ItemStack> mBoneMeal = new AutoMap<ItemStack>();

	private static AutoMap<ItemStack> mList_Master_Meats = new AutoMap<ItemStack>();
	private static AutoMap<ItemStack> mList_Master_FruitVege = new AutoMap<ItemStack>();
	private static AutoMap<ItemStack> mList_Master_Bones = new AutoMap<ItemStack>();
	private static AutoMap<ItemStack> mList_Master_Seeds = new AutoMap<ItemStack>();

	private static void processAllOreDict() {
		processOreDict("listAllmeatraw", mMeats);
		processOreDict("listAllfishraw", mFish);
		processOreDict("listAllfruit", mFruits);
		processOreDict("listAllVeggie", mVege);
		processOreDict("listAllnut", mNuts);
		processOreDict("listAllSeed", mSeeds);
		processOreDict("brickPeat", mPeat);
		processOreDict("bone", mBones);
		processOreDict("dustBone", mBoneMeal);
		// Just make a mega list, makes life easier.
		if (!mMeats.isEmpty()) {
			for (ItemStack g : mMeats) {
				mList_Master_Meats.put(g);
			}
		}
		if (!mFish.isEmpty()) {
			for (ItemStack g : mFish) {
				mList_Master_Meats.put(g);
			}
		}
		if (!mFruits.isEmpty()) {
			for (ItemStack g : mFruits) {
				mList_Master_FruitVege.put(g);
			}
		}
		if (!mVege.isEmpty()) {
			for (ItemStack g : mVege) {
				mList_Master_FruitVege.put(g);
			}
		}
		if (!mNuts.isEmpty()) {
			for (ItemStack g : mNuts) {
				mList_Master_FruitVege.put(g);
			}
		}
		if (!mSeeds.isEmpty()) {
			for (ItemStack g : mSeeds) {
				mList_Master_Seeds.put(g);
			}
		}
		if (!mBoneMeal.isEmpty()) {
			for (ItemStack g : mBoneMeal) {
				mList_Master_Bones.put(g);
			}
		}
		if (!mBones.isEmpty()) {
			for (ItemStack g : mBones) {
				mList_Master_Bones.put(g);
			}
		}
	}

	private static void processOreDict(String aOreName, AutoMap<ItemStack> aMap) {
		ArrayList<ItemStack> aTemp = OreDictionary.getOres(aOreName);
		if (!aTemp.isEmpty()) {
			for (ItemStack stack : aTemp) {
				aMap.put(stack);
			}
		}
	}

	private static void addBasicSlurryRecipes() {}

	private static void addAdvancedSlurryRecipes() {}

	private static void addBasicOrganiseFertRecipes() {}

	private static void addAdvancedOrganiseFertRecipes() {}

	private static void addMiscRecipes() {}

	@Override
	public String errorMessage() {
		// TODO Auto-generated method stub
		return "Failed to generate recipes for AgroChem.";
	}

	@Override
	public boolean generateRecipes() {		

		// Organise OreDict
		processAllOreDict();

		// Slurry Production
		addBasicSlurryRecipes();
		addAdvancedSlurryRecipes();

		// Organic Fert. Production
		addBasicOrganiseFertRecipes();
		addAdvancedOrganiseFertRecipes();

		addMiscRecipes();
		return true;
	}
}

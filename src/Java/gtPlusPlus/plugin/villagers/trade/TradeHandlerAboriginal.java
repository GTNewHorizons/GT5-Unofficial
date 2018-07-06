package gtPlusPlus.plugin.villagers.trade;

import static java.util.Collections.shuffle;

import java.util.Collections;
import java.util.Random;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public class TradeHandlerAboriginal extends TradeHandlerBase {

	private final static AutoMap<ItemStack> mInputs = new AutoMap<ItemStack>();
	private final static AutoMap<ItemStack> mOutputs = new AutoMap<ItemStack>();
	private static boolean initialised = false;

	public static void init() {
		mOutputs.put(ItemUtils.getSimpleStack(Blocks.anvil, 1));
		mOutputs.put(ItemUtils.getSimpleStack(Blocks.bookshelf, 0));
		mOutputs.put(ItemUtils.getSimpleStack(Blocks.cactus, 0));
		mOutputs.put(ItemUtils.getSimpleStack(Blocks.dirt, 0));
		mOutputs.put(ItemUtils.getSimpleStack(Blocks.cobblestone, 0));
		mOutputs.put(ItemUtils.getSimpleStack(Blocks.mossy_cobblestone, 0));
		mOutputs.put(ItemUtils.getSimpleStack(Blocks.pumpkin, 0));
		mOutputs.put(ItemUtils.getSimpleStack(Blocks.hardened_clay, 0));
		mOutputs.put(ItemUtils.getSimpleStack(Blocks.log, 0));
		mOutputs.put(ItemUtils.getSimpleStack(Blocks.obsidian, 8));
		mOutputs.put(ItemUtils.getSimpleStack(Items.wheat, 0));
		mOutputs.put(ItemUtils.getSimpleStack(Blocks.gravel, 0));
		mOutputs.put(ItemUtils.getSimpleStack(Blocks.sand, 0));
		mOutputs.put(ItemUtils.getSimpleStack(Items.apple, 0));
		mOutputs.put(ItemUtils.getSimpleStack(Items.emerald, 1));
		mOutputs.put(ItemUtils.getSimpleStack(Items.diamond, 1));
		mOutputs.put(ItemUtils.getSimpleStack(Items.baked_potato, 0));
		mOutputs.put(ItemUtils.getSimpleStack(Items.beef, 0));
		mOutputs.put(ItemUtils.getSimpleStack(Items.bone, 0));
		mOutputs.put(ItemUtils.getSimpleStack(Items.bread, 0));
		mOutputs.put(ItemUtils.getSimpleStack(Items.carrot, 0));
		mOutputs.put(ItemUtils.getSimpleStack(Items.potato, 0));
		mOutputs.put(ItemUtils.getSimpleStack(Items.poisonous_potato, 0));
		mOutputs.put(ItemUtils.getSimpleStack(Items.chicken, 0));
		mOutputs.put(ItemUtils.getSimpleStack(Items.porkchop, 0));
		mOutputs.put(ItemUtils.getSimpleStack(Items.cooked_beef, 0));
		mOutputs.put(ItemUtils.getSimpleStack(Items.cooked_chicken, 0));
		mOutputs.put(ItemUtils.getSimpleStack(Items.cooked_porkchop, 0));
		mOutputs.put(ItemUtils.getSimpleStack(Items.fish, 0));
		mOutputs.put(ItemUtils.getSimpleStack(Items.cooked_fished, 0));
		mOutputs.put(ItemUtils.getSimpleStack(Items.feather, 0));
		mOutputs.put(ItemUtils.getSimpleStack(Items.egg, 0));
		mOutputs.put(ItemUtils.getSimpleStack(Items.gold_nugget, 0));
		mOutputs.put(ItemUtils.getSimpleStack(Items.leather, 0));
		mOutputs.put(ItemUtils.getSimpleStack(Items.melon_seeds, 0));
		mOutputs.put(ItemUtils.getSimpleStack(Items.reeds, 0));
		mInputs.put(ItemUtils.getSimpleStack(Blocks.wooden_door));
		mInputs.put(ItemUtils.getSimpleStack(Blocks.log));
		mInputs.put(ItemUtils.getSimpleStack(Blocks.log2));
		mInputs.put(ItemUtils.getSimpleStack(Blocks.planks));
		mInputs.put(ItemUtils.getSimpleStack(Blocks.sapling));
		mInputs.put(ItemUtils.getSimpleStack(Blocks.sandstone));
		mInputs.put(ItemUtils.getSimpleStack(Blocks.nether_brick));
		mInputs.put(ItemUtils.getSimpleStack(Blocks.bookshelf));
		mInputs.put(ItemUtils.getSimpleStack(Blocks.crafting_table));
		mInputs.put(ItemUtils.getSimpleStack(Blocks.gravel));
		mInputs.put(ItemUtils.getSimpleStack(Blocks.hardened_clay));
		mInputs.put(ItemUtils.getSimpleStack(Blocks.cactus));
		mInputs.put(ItemUtils.getSimpleStack(Blocks.quartz_block));
		mInputs.put(ItemUtils.getSimpleStack(Blocks.stone));
		mInputs.put(ItemUtils.getSimpleStack(Blocks.mossy_cobblestone));
		mInputs.put(ItemUtils.getSimpleStack(Items.apple, 0));
		mInputs.put(ItemUtils.getSimpleStack(Items.emerald, 1));
		mInputs.put(ItemUtils.getSimpleStack(Items.diamond, 1));
		mInputs.put(ItemUtils.getSimpleStack(Items.baked_potato, 0));
		mInputs.put(ItemUtils.getSimpleStack(Items.beef, 0));
		mInputs.put(ItemUtils.getSimpleStack(Items.bone, 0));
		mInputs.put(ItemUtils.getSimpleStack(Items.bread, 0));
		mInputs.put(ItemUtils.getSimpleStack(Items.carrot, 0));
		mInputs.put(ItemUtils.getSimpleStack(Items.potato, 0));
		mInputs.put(ItemUtils.getSimpleStack(Items.poisonous_potato, 0));
		mInputs.put(ItemUtils.getSimpleStack(Items.chicken, 0));
		mInputs.put(ItemUtils.getSimpleStack(Items.porkchop, 0));
		mInputs.put(ItemUtils.getSimpleStack(Items.cooked_beef, 0));
		mInputs.put(ItemUtils.getSimpleStack(Items.cooked_chicken, 0));
		mInputs.put(ItemUtils.getSimpleStack(Items.cooked_porkchop, 0));
		mInputs.put(ItemUtils.getSimpleStack(Items.fish, 0));
		mInputs.put(ItemUtils.getSimpleStack(Items.cooked_fished, 0));
		mInputs.put(ItemUtils.getSimpleStack(Items.feather, 0));
		mInputs.put(ItemUtils.getSimpleStack(Items.egg, 0));
		mInputs.put(ItemUtils.getSimpleStack(Items.gold_nugget, 0));
		mInputs.put(ItemUtils.getSimpleStack(Items.leather, 0));
		mInputs.put(ItemUtils.getSimpleStack(Items.melon_seeds, 0));
		mInputs.put(ItemUtils.getSimpleStack(Items.reeds, 0));		
		initialised = true;
	}


	public TradeHandlerAboriginal() {
		Logger.INFO("Created Trade Manager for 'Trader' villager profession type.");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void manipulateTradesForVillager(EntityVillager villager, MerchantRecipeList recipeList, Random random) {		
		if (!initialised) {
			init();
		}	
		if (initialised) {
			recipeList.add(new MerchantRecipe(getInput(), getOutput()));
			recipeList.add(new MerchantRecipe(getInput(), getOutput()));
			recipeList.add(new MerchantRecipe(getInput(), getOutput()));
			recipeList.add(new MerchantRecipe(getInput(), getOutput()));
			recipeList.add(new MerchantRecipe(getInput(), getOutput()));
			recipeList.add(new MerchantRecipe(getInput(), getOutput()));
			recipeList.add(new MerchantRecipe(getInput(), getOutput()));
			recipeList.add(new MerchantRecipe(getInput(), getOutput()));
			recipeList.add(new MerchantRecipe(getInput(), getOutput()));
			recipeList.add(new MerchantRecipe(getInput(), getOutput()));
			recipeList.add(new MerchantRecipe(getInput(), getOutput()));
			recipeList.add(new MerchantRecipe(getInput(), getOutput()));
			recipeList.add(new MerchantRecipe(getInput(), getOutput()));
			recipeList.add(new MerchantRecipe(getInput(), getOutput()));
			recipeList.add(new MerchantRecipe(getInput(), getOutput()));
			recipeList.add(new MerchantRecipe(getInput(), getInput(), getOutput()));
			recipeList.add(new MerchantRecipe(getInput(), getInput(), getOutput()));
			recipeList.add(new MerchantRecipe(getInput(), getInput(), getOutput()));
			recipeList.add(new MerchantRecipe(getInput(), getInput(), getOutput()));
			recipeList.add(new MerchantRecipe(getInput(), getInput(), getOutput()));
			recipeList.add(new MerchantRecipe(getInput(), getInput(), getOutput()));
			recipeList.add(new MerchantRecipe(getInput(), getInput(), getOutput()));
			recipeList.add(new MerchantRecipe(getInput(), getInput(), getOutput()));
			recipeList.add(new MerchantRecipe(getInput(), getInput(), getOutput()));
			recipeList.add(new MerchantRecipe(getInput(), getInput(), getOutput()));
			recipeList.add(new MerchantRecipe(getInput(), getInput(), getOutput()));
			recipeList.add(new MerchantRecipe(getInput(), getInput(), getOutput()));
			recipeList.add(new MerchantRecipe(getInput(), getInput(), getOutput()));
			shuffle(recipeList);
		}
	}

	private ItemStack getInput() {
		ItemStack input = mInputs.get(MathUtils.randInt(0, mInputs.size()-1));
		int outputSize = (input.stackSize == 0 ? (Math.max(MathUtils.randInt(1, 64), MathUtils.randInt(1, 32))) : input.stackSize);
		return ItemUtils.getSimpleStack(input, outputSize);
	}
	
	final static int MID_BOUND = 24;	
	private ItemStack getOutput() {
		ItemStack output = mOutputs.get(MathUtils.randInt(0, mOutputs.size()-1));
		int outputSize = (output.stackSize == 0 ? (Math.min(MathUtils.randInt(MathUtils.randInt(1, MID_BOUND), MathUtils.randInt(MID_BOUND, 32)), MathUtils.randInt(MathUtils.randInt(12, MID_BOUND), MathUtils.randInt(MID_BOUND, 48)))) : output.stackSize);
		return ItemUtils.getSimpleStack(output, outputSize);
	}

}

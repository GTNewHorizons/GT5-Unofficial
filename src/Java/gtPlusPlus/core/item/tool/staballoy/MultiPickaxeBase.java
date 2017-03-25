package gtPlusPlus.core.item.tool.staballoy;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.array.Pair;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.recipe.RecipeUtils;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.world.World;

public class MultiPickaxeBase extends StaballoyPickaxe{

	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#getDurabilityForDisplay(net.minecraft.item.ItemStack)
	 */
	@Override
	public double getDurabilityForDisplay(final ItemStack stack) {
		if (super.getDurabilityForDisplay(stack) > 0){
			return super.getDurabilityForDisplay(stack);}
		return 0;
	}

	protected boolean canBreak = true;
	protected final int colour;
	protected final String materialName;
	protected final String displayName;
	public boolean isValid = true;
	private final Pair<?, ?> enchantment;

	public MultiPickaxeBase(final String unlocalizedName, final ToolMaterial material, final long materialDurability, final int colour, final Object enchant) {
		super(Utils.sanitizeString(unlocalizedName), material);
		this.setUnlocalizedName(Utils.sanitizeString(unlocalizedName));
		//this.setTextureName(CORE.MODID + ":" + "itemPickaxe");
		this.setTextureName("minecraft"+":"+"iron_pickaxe");
		this.FACING_HORIZONTAL=true;
		this.setMaxStackSize(1);
		if ((materialDurability*3) <= Integer.MAX_VALUE){
			this.setMaxDamage((int) (materialDurability*3));			
		}
		else {
			this.setMaxDamage(Integer.MAX_VALUE);
			this.canBreak = false;
		}
		this.colour = colour;
		this.materialName = material.name();
		this.displayName = unlocalizedName;
		this.setCreativeTab(AddToCreativeTab.tabTools);
		this.miningLevel = material.getHarvestLevel();



		if (enchant != null){
			if (enchant instanceof Pair){
				this.enchantment = (Pair<?, ?>) enchant;
			}
			else {
				this.enchantment = null;
			}
		}
		else {
			this.enchantment = null;
		}

		try {this.isValid = this.addRecipe();} catch (final Throwable e){}
		if ((colour != 0) && this.isValid && (materialDurability > 10000)){
			if (GameRegistry.findItem(CORE.MODID, Utils.sanitizeString(unlocalizedName)) == null){
				GameRegistry.registerItem(this, Utils.sanitizeString(unlocalizedName));
			}
		}

	}

	/*
	 *
	 *
	 *
	 *  Methods
	 *
	 *
	 *
	 */

	private boolean addRecipe(){

		final String cleanName = Utils.sanitizeString(this.materialName);

		final String plateDense = "plateDense"+cleanName;
		final String plateDouble = "plateDouble"+cleanName;
		final String rodLong = "stickLong"+cleanName;
		final String toolHammer = "craftingToolHardHammer";
		final String toolWrench = "craftingToolWrench";
		final String toolFile = "craftingToolFile";
		final String toolScrewDriver = "craftingToolScrewdriver";

		if (null == ItemUtils.getItemStackOfAmountFromOreDictNoBroken(rodLong, 1)){
			Utils.LOG_WARNING("stickLong of "+cleanName+" does not exist.");
			return false;
		}
		if (null == ItemUtils.getItemStackOfAmountFromOreDictNoBroken(plateDense, 1)){
			Utils.LOG_WARNING("plateDense of "+cleanName+" does not exist.");
			if (null != ItemUtils.getItemStackOfAmountFromOreDictNoBroken(plateDouble, 1)){
				Utils.LOG_WARNING("plateDouble of "+cleanName+" does exist. Using it instead.");
				RecipeUtils.recipeBuilder(
						plateDouble, plateDouble, plateDouble,
						toolFile, rodLong, toolHammer,
						toolWrench, rodLong, toolScrewDriver,
						ItemUtils.getSimpleStack(this));

				return true;
			}
			Utils.LOG_WARNING("plateDouble of "+cleanName+" does not exist.");
			return false;
		}

		RecipeUtils.recipeBuilder(
				plateDense, plateDense, plateDense,
				toolFile, rodLong, toolHammer,
				toolWrench, rodLong, toolScrewDriver,
				ItemUtils.getSimpleStack(this));

		return true;
	}

	public final String getMaterialName() {
		return this.materialName;
	}

	@Override
	public String getItemStackDisplayName(final ItemStack iStack) {
		return this.displayName;
	}

	@Override
	public int getColorFromItemStack(final ItemStack stack, final int HEX_OxFFFFFF) {
		if (this.colour == 0){
			return MathUtils.generateSingularRandomHexValue();
		}
		return this.colour;

	}

	@SuppressWarnings("static-method")
	private float calculateDurabilityLoss(final World world, final int X, final int Y, final int Z){
		float bDurabilityLoss = 0;
		Boolean correctTool = false;
		float bHardness = 0;
		if (!world.isRemote){
			try {
				final Block removalist = world.getBlock(X, Y, Z);
				//Utils.LOG_WARNING(removalist.toString());

				bHardness = removalist.getBlockHardness(world, X, Y, Z)*100;
				Utils.LOG_INFO("Hardness: "+bHardness);

				bDurabilityLoss = 100;
				//Utils.LOG_WARNING("Durability Loss: "+bDurabilityLoss);

				correctTool = this.canPickaxeBlock(removalist, world, new int[]{X,Y,Z});
				Utils.LOG_WARNING(""+correctTool);

				if (!correctTool){
					return 0;
				}

			} catch (final NullPointerException e){

			}
		}
		return bDurabilityLoss;
	}

	//Should clear up blocks quicker if I chain it.
	/*@Override
	public void removeBlockAndDropAsItem(World world, int X, int Y, int Z, ItemStack heldItem){
		localWorld = world;
		try {
			Block block = world.getBlock(X, Y, Z);
			float dur = calculateDurabilityLoss(world, X, Y, Z);
			Utils.LOG_WARNING(block.toString());
			String removalTool = "";
			removalTool = block.getHarvestTool(1);

			if (removalTool.equals("pickaxe") || UtilsMining.getBlockType(block, world, new int[]{X,Y,Z}, miningLevel)){
				if (canPickaxeBlock(block, world, new int[]{X,Y,Z})){
					if((block != Blocks.bedrock) && (block.getBlockHardness(world, X, Y, Z) != -1) && (block.getBlockHardness(world, X, Y, Z) <= 100) && (block != Blocks.water) && (block != Blocks.lava)){

						if (heldItem.getItemDamage() <= (heldItem.getMaxDamage()-dur)){

							block.dropBlockAsItem(world, X, Y, Z, world.getBlockMetadata(X, Y, Z), 0);
							world.setBlockToAir(X, Y, Z);

						}
						else {
							return;
						}

					}
				}
				else {
					Utils.LOG_WARNING("Incorrect Tool for mining this block.");
				}
			}
		} catch (NullPointerException e){

		}
	}*/

	@Override
	public void damageItem(final ItemStack item, final int damage, final EntityPlayer localPlayer){
		if (this.canBreak){
			item.damageItem(damage, localPlayer);
		}
	}

	@Override
	public void setItemDamage(final ItemStack item, final int damage){
		if (this.canBreak){
			item.setItemDamage(damage-1);			
		}
	}


	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(final ItemStack par1ItemStack){
		return EnumRarity.uncommon;
	}

	@Override
	public boolean hasEffect(final ItemStack par1ItemStack){
		return false;
	}

	@Override
	public void onCreated(final ItemStack mThisItem, final World mWorld, final EntityPlayer mPlayer) {
		Enchantment enchant = null;
		int enchantmentLevel = 0;
		final Pair<?, ?> Y = this.enchantment;
		if (Y != null){
			if (Y.getKey() != null){
				enchant = (Enchantment) this.enchantment.getKey();
			}
			if (Y.getValue() != null){
				enchantmentLevel = ((Integer) this.enchantment.getValue()).intValue();
			}
		}
		final ItemStack itemToEnchant = mThisItem;
		if ((enchant != null) && (enchantmentLevel != 0) && (enchantmentLevel >= 1)){
			itemToEnchant.addEnchantment(enchant, enchantmentLevel);
		}
		super.onCreated(itemToEnchant, mWorld, mPlayer);
	}

	@Override
	public void getSubItems(final Item mItem, final CreativeTabs mCreativeTab, final List mList) {
		Enchantment enchant = null;
		int enchantmentLevel = 0;
		final Pair<?, ?> Y = this.enchantment;
		if (Y != null){
			if (Y.getKey() != null){
				enchant = (Enchantment) this.enchantment.getKey();
			}
			if (Y.getValue() != null){
				enchantmentLevel = ((Integer) this.enchantment.getValue()).intValue();
			}
		}

		final Item thisItem = mItem;
		final ItemStack itemToEnchant = ItemUtils.getSimpleStack(thisItem);
		if ((enchant != null) && (enchantmentLevel != 0) && (enchantmentLevel >= 1)){
			itemToEnchant.addEnchantment(enchant, enchantmentLevel);
			mList.add(itemToEnchant);
		}
		else {
			mList.add(new ItemStack(thisItem, 1, 0));
		}

	}
	
	
	/**
	 * 
	 * Time to Override Minecraft stupid mechanics, allowing unbreakable stuff.
	 * 
	 */

    public boolean isDamageable(){
        if (this.getMaxDamage() > 0 && !this.hasSubtypes){
        	if (this.canBreak){
        		return true;
        	}
        }
        return false;
    }
	
}

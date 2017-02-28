package gtPlusPlus.core.item.tool.staballoy;

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

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MultiPickaxeBase extends StaballoyPickaxe{

	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#getDurabilityForDisplay(net.minecraft.item.ItemStack)
	 */
	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		if (super.getDurabilityForDisplay(stack) > 0){
			return super.getDurabilityForDisplay(stack);}
		return 0;
	}

	protected final int colour;
	protected final String materialName;
	protected final String displayName;
	public boolean isValid = true;
	private final Pair<?, ?> enchantment;

	public MultiPickaxeBase(String unlocalizedName, ToolMaterial material, int materialDurability, int colour, Object enchant) {
		super(Utils.sanitizeString(unlocalizedName), material);
		this.setUnlocalizedName(Utils.sanitizeString(unlocalizedName));
		//this.setTextureName(CORE.MODID + ":" + "itemPickaxe");
		this.setTextureName("minecraft"+":"+"iron_pickaxe");
		this.FACING_HORIZONTAL=true;
		this.setMaxStackSize(1);
		this.setMaxDamage(materialDurability*3);
		this.colour = colour;
		this.materialName = material.name();
		this.displayName = unlocalizedName;
		this.setCreativeTab(AddToCreativeTab.tabTools);
		miningLevel = material.getHarvestLevel();
		
		
		
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
		
		try {isValid = addRecipe();} catch (Throwable e){}
		if (colour != 0 && isValid && materialDurability > 10000){
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
		
		String cleanName = Utils.sanitizeString(materialName);
		
		String plateDense = "plateDense"+cleanName;
		String plateDouble = "plateDouble"+cleanName;
		String rodLong = "stickLong"+cleanName;
		String toolHammer = "craftingToolHardHammer";
		String toolWrench = "craftingToolWrench";
		String toolFile = "craftingToolFile";
		String toolScrewDriver = "craftingToolScrewdriver";
		
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
		return materialName;
	}

	@Override
	public String getItemStackDisplayName(ItemStack iStack) {
		return displayName;
		/*String name;
		if (getUnlocalizedName().toLowerCase().contains("wood")){
			name = "Wooden";
		}
		else if (getUnlocalizedName().toLowerCase().contains("cobblestone")){
			name = "Cobblestone";
		}
		else if (getUnlocalizedName().toLowerCase().contains("iron")){
			name = "Iron";
		}
		else if (getUnlocalizedName().toLowerCase().contains("gold")){
			name = "Gold";
		}
		else if (getUnlocalizedName().toLowerCase().contains("diamond")){
			name = "Diamond";
		}
		else {
		}
		return name+" Multipickaxe";*/
	}

	@Override
	public int getColorFromItemStack(ItemStack stack, int HEX_OxFFFFFF) {
		if (colour == 0){
			return MathUtils.generateSingularRandomHexValue();
		}
		return colour;

	}

	@SuppressWarnings("static-method")
	private float calculateDurabilityLoss(World world, int X, int Y, int Z){
		float bDurabilityLoss = 0;
		Boolean correctTool = false;
		float bHardness = 0;
		if (!world.isRemote){			
			try {
				Block removalist = world.getBlock(X, Y, Z);
				//Utils.LOG_WARNING(removalist.toString());

				bHardness = removalist.getBlockHardness(world, X, Y, Z)*100;
				Utils.LOG_INFO("Hardness: "+bHardness);

				bDurabilityLoss = 100;
				//Utils.LOG_WARNING("Durability Loss: "+bDurabilityLoss);

				correctTool = canPickaxeBlock(removalist, world, new int[]{X,Y,Z});
				Utils.LOG_WARNING(""+correctTool);

				if (!correctTool){
					return 0;
				}

			} catch (NullPointerException e){

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

	public void damageItem(ItemStack item, int damage, EntityPlayer localPlayer){
		item.damageItem(damage, localPlayer);
	}

	public void setItemDamage(ItemStack item, int damage){
		item.setItemDamage(damage-1);
	}


	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack par1ItemStack){
		return EnumRarity.uncommon;
	}

	@Override
	public boolean hasEffect(ItemStack par1ItemStack){
		return false;
	}

	@Override
	public void onCreated(ItemStack mThisItem, World mWorld, EntityPlayer mPlayer) {
		Enchantment enchant = null;
		int enchantmentLevel = 0;
		Pair<?, ?> Y = this.enchantment;
		if (Y != null){
			if (Y.getKey() != null){
				enchant = (Enchantment) ((Pair<?, ?>) this.enchantment).getKey();				
			}
			if (Y.getValue() != null){
				enchantmentLevel = (byte) ((Pair<?, ?>) this.enchantment).getValue();				
			}
		}
		ItemStack itemToEnchant = mThisItem;		
		if (enchant != null && enchantmentLevel != 0 && enchantmentLevel >= 1){
			itemToEnchant.addEnchantment(enchant, enchantmentLevel);
		}
		super.onCreated(itemToEnchant, mWorld, mPlayer);
	}

	@Override
	public void getSubItems(Item mItem, CreativeTabs mCreativeTab, List mList) {
		Enchantment enchant = null;
		int enchantmentLevel = 0;
		Pair<?, ?> Y = this.enchantment;
		if (Y != null){
			if (Y.getKey() != null){
				enchant = (Enchantment) ((Pair<?, ?>) this.enchantment).getKey();				
			}
			if (Y.getValue() != null){
				enchantmentLevel = (byte) ((Pair<?, ?>) this.enchantment).getValue();				
			}
		}
		
		Item thisItem = mItem;
		ItemStack itemToEnchant = ItemUtils.getSimpleStack(thisItem);		
		if (enchant != null && enchantmentLevel != 0 && enchantmentLevel >= 1){
			itemToEnchant.addEnchantment(enchant, enchantmentLevel);
			mList.add(itemToEnchant);
		}
		else {
			mList.add(new ItemStack(thisItem, 1, 0));			
		}
		
	}

}

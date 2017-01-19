package gtPlusPlus.core.item.tool.staballoy;

import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.recipe.RecipeUtils;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MultiSpadeBase extends StaballoySpade{

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

	public MultiSpadeBase(String unlocalizedName, ToolMaterial material, int materialDurability, int colour) {
		super(Utils.sanitizeString(unlocalizedName), material);
		this.setUnlocalizedName(Utils.sanitizeString(unlocalizedName));
		//this.setTextureName(CORE.MODID + ":" + "itemShovel");
		this.setTextureName("minecraft"+":"+"iron_shovel");
		this.FACING_HORIZONTAL=true;
		this.setMaxStackSize(1);
		this.setMaxDamage(materialDurability*3);
		this.colour = colour;
		this.materialName = material.name();
		this.displayName = unlocalizedName;
		this.setCreativeTab(AddToCreativeTab.tabTools);
		try {isValid = addRecipe();} catch (Throwable e){}
		if (colour != 0 && isValid){
			if (GameRegistry.findItem(CORE.MODID, Utils.sanitizeString(unlocalizedName)) == null){
				GameRegistry.registerItem(this, Utils.sanitizeString(unlocalizedName));			
			}
		}
	}

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
			return false;
		}
		if (null == ItemUtils.getItemStackOfAmountFromOreDictNoBroken(plateDense, 1)){
			if (null != ItemUtils.getItemStackOfAmountFromOreDictNoBroken(plateDouble, 1)){
				RecipeUtils.recipeBuilder(
						toolFile, plateDouble, toolHammer,
						null, rodLong, null,
						toolWrench, rodLong, toolScrewDriver,
						ItemUtils.getSimpleStack(this));

				return true;
			}
			return false;
		}		

		RecipeUtils.recipeBuilder(
				toolFile, plateDense, toolHammer,
				null, rodLong, null,
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
		else {
		}
		return "Big "+name+" Spade";*/
	}

	@Override
	public int getColorFromItemStack(ItemStack stack, int HEX_OxFFFFFF) {
		if (colour == 0){
			return MathUtils.generateSingularRandomHexValue();
		}
		return colour;
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

}

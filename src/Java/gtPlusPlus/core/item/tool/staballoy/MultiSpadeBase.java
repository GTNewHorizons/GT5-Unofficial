package gtPlusPlus.core.item.tool.staballoy;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.RecipeUtils;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

public class MultiSpadeBase extends StaballoySpade{

	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#getDurabilityForDisplay(net.minecraft.item.ItemStack)
	 */
	@Override
	public double getDurabilityForDisplay(final ItemStack stack) {
		if (super.getDurabilityForDisplay(stack) > 0){
			return super.getDurabilityForDisplay(stack);}
		return 0;
	}

	protected final int colour;
	protected final String materialName;
	protected final String displayName;
	public boolean isValid = true;

	public MultiSpadeBase(final String unlocalizedName, final ToolMaterial material, final int materialDurability, final int colour) {
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
		try {this.isValid = this.addRecipe();} catch (final Throwable e){}
		if ((colour != 0) && this.isValid){
			if (GameRegistry.findItem(CORE.MODID, Utils.sanitizeString(unlocalizedName)) == null){
				GameRegistry.registerItem(this, Utils.sanitizeString(unlocalizedName));
			}
		}
	}

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
		return this.materialName;
	}

	@Override
	public String getItemStackDisplayName(final ItemStack iStack) {
		return this.displayName;
		/*String name;
		if (getUnlocalizedName().toLowerCase().contains("wood")){
			name = "Wooden";
		}
		else {
		}
		return "Big "+name+" Spade";*/
	}

	@Override
	public int getColorFromItemStack(final ItemStack stack, final int HEX_OxFFFFFF) {
		if (this.colour == 0){
			return MathUtils.generateSingularRandomHexValue();
		}
		return this.colour;
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

}

package gtPlusPlus.core.item.tool.staballoy;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.recipe.RecipeUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

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

	protected Boolean FACING_HORIZONTAL = true;
	protected String FACING = "north";
	protected EntityPlayer localPlayer;
	protected String lookingDirection;
	protected World localWorld;
	protected ItemStack thisPickaxe = null;
	protected final int colour;
	protected final String materialName;
	public boolean isValid = true;

	public MultiSpadeBase(String unlocalizedName, ToolMaterial material, int materialDurability, int colour) {
		super(Utils.sanitizeString(unlocalizedName), material);
		this.setUnlocalizedName(Utils.sanitizeString(unlocalizedName));
		this.setTextureName(CORE.MODID + ":" + "itemShovel");
		this.FACING_HORIZONTAL=true;
		this.setMaxStackSize(1);
		this.setMaxDamage(materialDurability);
		this.colour = colour;
		this.materialName = material.name();
		this.setCreativeTab(AddToCreativeTab.tabTools);
		try {isValid = addRecipe();} catch (Throwable e){}
		if (colour != 0 && isValid){
			GameRegistry.registerItem(this, Utils.sanitizeString(unlocalizedName));			
		}
	}
	
	private boolean addRecipe(){
		String plateDense = "plateDense"+materialName;
		String rodLong = "stickLong"+materialName;
		String toolHammer = "craftingToolHardHammer";
		String toolWrench = "craftingToolWrench";
		String toolFile = "craftingToolFile";
		String toolScrewDriver = "craftingToolScrewdriver";
		
		if (null == ItemUtils.getItemStackOfAmountFromOreDictNoBroken(rodLong, 1)){
			return false;
		}
		if (null == ItemUtils.getItemStackOfAmountFromOreDictNoBroken(plateDense, 1)){
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

		String name;
		if (getUnlocalizedName().toLowerCase().contains("wood")){
			name = "Wooden";
		}
		else {
			name = materialName;
		}
		return "Big "+name+" Spade";
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

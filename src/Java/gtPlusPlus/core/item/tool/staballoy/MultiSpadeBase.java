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

public class MultiSpadeBase extends StaballoySpade {

	protected Boolean		FACING_HORIZONTAL	= true;

	protected String		FACING				= "north";
	protected EntityPlayer	localPlayer;
	protected String		lookingDirection;
	protected World			localWorld;
	protected ItemStack		thisPickaxe			= null;
	protected final int		colour;
	protected final String	materialName;
	public boolean			isValid				= true;
	public MultiSpadeBase(final String unlocalizedName, final ToolMaterial material, final int materialDurability,
			final int colour) {
		super(Utils.sanitizeString(unlocalizedName), material);
		this.setUnlocalizedName(Utils.sanitizeString(unlocalizedName));
		this.setTextureName(CORE.MODID + ":" + "itemShovel");
		this.FACING_HORIZONTAL = true;
		this.setMaxStackSize(1);
		this.setMaxDamage(materialDurability);
		this.colour = colour;
		this.materialName = material.name();
		this.setCreativeTab(AddToCreativeTab.tabTools);
		try {
			this.isValid = this.addRecipe();
		}
		catch (final Throwable e) {
		}
		if (colour != 0 && this.isValid) {
			GameRegistry.registerItem(this, Utils.sanitizeString(unlocalizedName));
		}
	}

	private boolean addRecipe() {
		final String plateDense = "plateDense" + this.materialName;
		final String rodLong = "stickLong" + this.materialName;
		final String toolHammer = "craftingToolHardHammer";
		final String toolWrench = "craftingToolWrench";
		final String toolFile = "craftingToolFile";
		final String toolScrewDriver = "craftingToolScrewdriver";

		if (null == ItemUtils.getItemStackOfAmountFromOreDictNoBroken(rodLong, 1)) {
			return false;
		}
		if (null == ItemUtils.getItemStackOfAmountFromOreDictNoBroken(plateDense, 1)) {
			return false;
		}

		RecipeUtils.recipeBuilder(toolFile, plateDense, toolHammer, null, rodLong, null, toolWrench, rodLong,
				toolScrewDriver, ItemUtils.getSimpleStack(this));

		return true;
	}

	@Override
	public int getColorFromItemStack(final ItemStack stack, final int HEX_OxFFFFFF) {
		if (this.colour == 0) {
			return MathUtils.generateSingularRandomHexValue();
		}
		return this.colour;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.minecraft.item.Item#getDurabilityForDisplay(net.minecraft.item.
	 * ItemStack)
	 */
	@Override
	public double getDurabilityForDisplay(final ItemStack stack) {
		if (super.getDurabilityForDisplay(stack) > 0) {
			return super.getDurabilityForDisplay(stack);
		}
		return 0;
	}

	@Override
	public String getItemStackDisplayName(final ItemStack iStack) {

		String name;
		if (this.getUnlocalizedName().toLowerCase().contains("wood")) {
			name = "Wooden";
		}
		else {
			name = this.materialName;
		}
		return "Big " + name + " Spade";
	}

	public final String getMaterialName() {
		return this.materialName;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(final ItemStack par1ItemStack) {
		return EnumRarity.uncommon;
	}

	@Override
	public boolean hasEffect(final ItemStack par1ItemStack) {
		return false;
	}

}

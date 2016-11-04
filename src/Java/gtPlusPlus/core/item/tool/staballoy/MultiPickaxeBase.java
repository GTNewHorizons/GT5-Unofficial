package gtPlusPlus.core.item.tool.staballoy;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.player.UtilsMining;
import gtPlusPlus.core.util.recipe.RecipeUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class MultiPickaxeBase extends StaballoyPickaxe {

	protected Boolean		FACING_HORIZONTAL	= true;

	protected String		FACING				= "north";
	protected EntityPlayer	localPlayer;
	protected String		lookingDirection;
	protected World			localWorld;
	protected ItemStack		thisPickaxe			= null;
	protected final int		colour;
	protected final String	materialName;
	public boolean			isValid				= true;
	public MultiPickaxeBase(final String unlocalizedName, final ToolMaterial material, final int materialDurability,
			final int colour) {
		super(Utils.sanitizeString(unlocalizedName), material);
		this.setUnlocalizedName(Utils.sanitizeString(unlocalizedName));
		this.setTextureName(CORE.MODID + ":" + "itemPickaxe");
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

		RecipeUtils.recipeBuilder(plateDense, plateDense, plateDense, toolFile, rodLong, toolHammer, toolWrench,
				rodLong, toolScrewDriver, ItemUtils.getSimpleStack(this));

		return true;
	}

	/*
	 *
	 *
	 *
	 * Methods
	 *
	 *
	 *
	 */

	@SuppressWarnings("static-method")
	private float calculateDurabilityLoss(final World world, final int X, final int Y, final int Z) {
		float bDurabilityLoss = 0;
		Boolean correctTool = false;
		float bHardness = 0;
		if (!world.isRemote) {
			try {
				final Block removalist = world.getBlock(X, Y, Z);
				// Utils.LOG_WARNING(removalist.toString());

				bHardness = removalist.getBlockHardness(world, X, Y, Z) * 100;
				Utils.LOG_WARNING("Hardness: " + bHardness);

				bDurabilityLoss = 100;
				// Utils.LOG_WARNING("Durability Loss: "+bDurabilityLoss);

				correctTool = this.canPickaxeBlock(removalist, world);
				Utils.LOG_WARNING("" + correctTool);

				if (!correctTool) {
					return 0;
				}

			}
			catch (final NullPointerException e) {

			}
		}
		return bDurabilityLoss;
	}

	@Override
	public void damageItem(final ItemStack item, final int damage, final EntityPlayer localPlayer) {
		item.damageItem(damage, localPlayer);
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
		else if (this.getUnlocalizedName().toLowerCase().contains("cobblestone")) {
			name = "Cobblestone";
		}
		else if (this.getUnlocalizedName().toLowerCase().contains("iron")) {
			name = "Iron";
		}
		else if (this.getUnlocalizedName().toLowerCase().contains("gold")) {
			name = "Gold";
		}
		else if (this.getUnlocalizedName().toLowerCase().contains("diamond")) {
			name = "Diamond";
		}
		else {
			name = this.materialName;
		}
		return name + " Multipickaxe";
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

	// Should clear up blocks quicker if I chain it.
	@Override
	public void removeBlockAndDropAsItem(final World world, final int X, final int Y, final int Z,
			final ItemStack heldItem) {
		this.localWorld = world;
		try {
			final Block block = world.getBlock(X, Y, Z);
			final float dur = this.calculateDurabilityLoss(world, X, Y, Z);
			Utils.LOG_WARNING(block.toString());
			String removalTool = "";
			removalTool = block.getHarvestTool(1);

			if (removalTool.equals("pickaxe") || UtilsMining.getBlockType(block)) {
				if (this.canPickaxeBlock(block, world)) {
					if (block != Blocks.bedrock && block.getBlockHardness(world, X, Y, Z) != -1
							&& block.getBlockHardness(world, X, Y, Z) <= 100 && block != Blocks.water
							&& block != Blocks.lava) {

						if (heldItem.getItemDamage() <= heldItem.getMaxDamage() - dur) {

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
		}
		catch (final NullPointerException e) {

		}
	}

	@Override
	public void setItemDamage(final ItemStack item, final int damage) {
		item.setItemDamage(damage - 1);
	}

}

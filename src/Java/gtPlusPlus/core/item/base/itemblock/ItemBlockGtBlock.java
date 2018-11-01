package gtPlusPlus.core.item.base.itemblock;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import gtPlusPlus.core.block.base.BlockBaseModular;
import gtPlusPlus.core.block.base.BlockBaseOre;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialStack;
import gtPlusPlus.core.util.minecraft.EntityUtils;
import gtPlusPlus.core.util.sys.KeyboardUtils;

public class ItemBlockGtBlock extends ItemBlock {

	protected final int blockColour;
	private int sRadiation;

	private Material mMaterial;

	private final Block thisBlock;
	private boolean isOre = false;
	private boolean isModular = false;

	public ItemBlockGtBlock(final Block block) {
		super(block);
		this.thisBlock = block;
		if (block instanceof BlockBaseOre) {
			this.isOre = true;
		} else if (block instanceof BlockBaseModular) {
			this.isModular = true;
		}
		final BlockBaseModular baseBlock = (BlockBaseModular) block;
		if (isModular) {
			this.blockColour = baseBlock.getRenderColor(0);
		} else if (isOre) {
			this.blockColour = block.getBlockColor();
		} else {
			this.blockColour = block.getBlockColor();
		}
		// GT_OreDictUnificator.registerOre("block"+block.getUnlocalizedName().replace("tile.block",
		// "").replace("tile.", "").replace("of", "").replace("Of", "").replace("Block",
		// "").replace("-", "").replace("_", "").replace(" ", ""),
		// ItemUtils.getSimpleStack(this));
	}

	public int getRenderColor(final int aMeta) {
		return this.blockColour;
	}

	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {

		if (this.mMaterial != null) {
			list.add(this.mMaterial.vChemicalFormula);
			if (this.mMaterial.vRadiationLevel > 0) {
				list.add(CORE.GT_Tooltip_Radioactive);
			}
		} else {
			list.add("Material is Null.");
		}

		if (KeyboardUtils.isCtrlKeyDown()) {
			Block b = Block.getBlockFromItem(stack.getItem());
			if (b != null) {

				String aTool = b.getHarvestTool(stack.getItemDamage());
				int aMiningLevel1 = b.getHarvestLevel(stack.getItemDamage());
				list.add("Mining Level: " + Math.min(Math.max(aMiningLevel1, 0), 5));

				if (this.mMaterial != null) {
					list.add("Ore contains:    ");
					if (mMaterial.getComposites().isEmpty()) {
						list.add("- " + mMaterial.getLocalizedName());
					} else {
						for (MaterialStack m : mMaterial.getComposites()) {
							list.add("- " + m.getStackMaterial().getLocalizedName() + " x" + m.getPartsPerOneHundred());
						}
					}
				}
			}
		} else {
			list.add(EnumChatFormatting.DARK_GRAY + "Hold Ctrl to show additional info.");
		}
		super.addInformation(stack, aPlayer, list, bool);
	}

	@Override
	public void onUpdate(final ItemStack iStack, final World world, final Entity entityHolding, final int p_77663_4_,
			final boolean p_77663_5_) {

		if (!isModular && !isOre) {
			mMaterial = null;
		} else {
			if (this.mMaterial == null) {
				Block b = Block.getBlockFromItem(iStack.getItem());
				if (isOre) {
					mMaterial = ((BlockBaseOre) b).getMaterialEx();
				} else {
					mMaterial = ((BlockBaseModular) b).getMaterialEx();
				}
				if (mMaterial != null) {
					this.sRadiation = mMaterial.vRadiationLevel;
				} else {
					this.sRadiation = 0;
				}
			}
			if (this.sRadiation > 0) {
				EntityUtils.applyRadiationDamageToEntity(iStack.stackSize, this.sRadiation, world, entityHolding);
			}
		}

	}

}

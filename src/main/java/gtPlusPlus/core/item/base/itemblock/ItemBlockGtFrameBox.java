package gtPlusPlus.core.item.base.itemblock;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.List;

import gtPlusPlus.core.block.base.BlockBaseModular;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialStack;
import gtPlusPlus.core.util.minecraft.EntityUtils;
import gtPlusPlus.core.util.sys.KeyboardUtils;

public class ItemBlockGtFrameBox extends ItemBlock{

	protected int blockColour;
	private Material mMaterial;
	private int sRadiation;

	public ItemBlockGtFrameBox(final Block block) {
		super(block);
		final BlockBaseModular baseBlock = (BlockBaseModular) block;
		this.blockColour = baseBlock.getRenderColor(1);
		


		if (block instanceof BlockBaseModular){
			BlockBaseModular g = (BlockBaseModular) block;
			this.mMaterial = g.getMaterialEx();
			sRadiation = mMaterial.vRadiationLevel;
		}
		else {
			this.mMaterial = null;
			sRadiation = 0;
		}
		
		//GT_OreDictUnificator.registerOre("frameGt"+block.getUnlocalizedName().replace("tile.", "").replace("tile.BlockGtFrame", "").replace("-", "").replace("_", "").replace(" ", "").replace("FrameBox", ""), ItemUtils.getSimpleStack(this));
	}

	public int getRenderColor(final int aMeta) {
		return this.blockColour;
	}

	@SuppressWarnings("unchecked")
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
					list.add("Contains:    ");
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

			if (this.sRadiation > 0) {
				EntityUtils.applyRadiationDamageToEntity(iStack.stackSize, this.sRadiation, world, entityHolding);
			}
		}
	

}

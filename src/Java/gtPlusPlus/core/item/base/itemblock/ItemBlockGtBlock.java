package gtPlusPlus.core.item.base.itemblock;

import java.util.List;

import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.block.base.BlockBaseModular;
import gtPlusPlus.core.block.base.BlockBaseOre;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.entity.EntityUtils;
import gtPlusPlus.core.util.item.ItemUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBlockGtBlock extends ItemBlock{

	protected final int blockColour;
	protected final int sRadiation;

	private final Block thisBlock;
	private boolean isOre = false;

	public ItemBlockGtBlock(final Block block) {
		super(block);
		this.thisBlock = block;
		if (block instanceof BlockBaseOre){
			this.isOre = true;
		}
		final BlockBaseModular baseBlock = (BlockBaseModular) block;
		this.blockColour = baseBlock.getRenderColor(0);
		if (block.getLocalizedName().toLowerCase().contains("uranium") || block.getLocalizedName().toLowerCase().contains("plutonium") || block.getLocalizedName().toLowerCase().contains("thorium")){
			this.sRadiation = 2;
		}
		else {
			this.sRadiation = 0;
		}
		GT_OreDictUnificator.registerOre("block"+block.getUnlocalizedName().replace("tile.block", "").replace("tile.", "").replace("of", "").replace("Of", "").replace("Block", "").replace("-", "").replace("_", "").replace(" ", ""), ItemUtils.getSimpleStack(this));
	}

	public int getRenderColor(final int aMeta) {
		return this.blockColour;
	}

	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
		if (this.sRadiation > 0){
			list.add(CORE.GT_Tooltip_Radioactive);
		}
		if (this.isOre){
			if (this.thisBlock != null){
				if (this.thisBlock.getLocalizedName().equalsIgnoreCase("fluorite ore")){
					list.add("Mined from Sandstone and Limestone.");
				}
			}
		}
		else {
		}
		super.addInformation(stack, aPlayer, list, bool);
	}

	@Override
	public void onUpdate(final ItemStack iStack, final World world, final Entity entityHolding, final int p_77663_4_, final boolean p_77663_5_) {
		EntityUtils.applyRadiationDamageToEntity(this.sRadiation, world, entityHolding);
	}

}

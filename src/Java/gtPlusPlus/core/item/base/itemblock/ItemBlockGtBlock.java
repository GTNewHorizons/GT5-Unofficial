package gtPlusPlus.core.item.base.itemblock;

import java.util.List;

import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.block.base.BlockBaseModular;
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
	
	public ItemBlockGtBlock(Block block) {
		super(block);
		BlockBaseModular baseBlock = (BlockBaseModular) block;
		this.blockColour = baseBlock.getRenderColor(0);
		if (block.getLocalizedName().toLowerCase().contains("uranium") || block.getLocalizedName().toLowerCase().contains("plutonium") || block.getLocalizedName().toLowerCase().contains("thorium")){
			sRadiation = 2;
		}
		else {
			sRadiation = 0;
		}
		GT_OreDictUnificator.registerOre("block"+block.getUnlocalizedName().replace("tile.block", "").replace("tile.", "").replace("of", "").replace("Of", "").replace("Block", "").replace("-", "").replace("_", "").replace(" ", ""), ItemUtils.getSimpleStack(this));
	}

    public int getRenderColor(int aMeta) {
        return blockColour;
    }
    
    @Override
	public void addInformation(ItemStack stack, EntityPlayer aPlayer, List list, boolean bool) {
			if (sRadiation > 0){
				list.add(CORE.GT_Tooltip_Radioactive);
			}
		super.addInformation(stack, aPlayer, list, bool);
	}
    
	 @Override
		public void onUpdate(ItemStack iStack, World world, Entity entityHolding, int p_77663_4_, boolean p_77663_5_) {
		 EntityUtils.applyRadiationDamageToEntity(sRadiation, world, entityHolding);
		}

}

package miscutil.core.item.base.itemblock;

import gregtech.api.util.GT_OreDictUnificator;
import ic2.core.IC2Potion;
import ic2.core.item.armor.ItemArmorHazmat;

import java.util.List;

import miscutil.core.util.item.UtilsItems;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class ItemBlockGtBlock extends ItemBlock{

	protected final int blockColour;
	protected final int sRadiation;
	
	public ItemBlockGtBlock(Block block) {
		super(block);
		this.blockColour = block.getBlockColor();
		if (block.getLocalizedName().toLowerCase().contains("uranium") || block.getLocalizedName().toLowerCase().contains("plutonium") || block.getLocalizedName().toLowerCase().contains("thorium")){
			sRadiation = 2;
		}
		else {
			sRadiation = 0;
		}
		GT_OreDictUnificator.registerOre("block"+block.getUnlocalizedName().replace("tile.block", "").replace("tile.", "").replace("of", "").replace("Of", "").replace("Block", "").replace("-", "").replace("_", "").replace(" ", ""), UtilsItems.getSimpleStack(this));
	}

    public int getRenderColor(int aMeta) {
        return blockColour;
    }
    
    @Override
	public void addInformation(ItemStack stack, EntityPlayer aPlayer, List list, boolean bool) {
			if (sRadiation > 0){
				list.add(EnumChatFormatting.GRAY+"Warning: "+EnumChatFormatting.GREEN+"Radioactive! "+EnumChatFormatting.GOLD+" Avoid direct handling without hazmat protection.");
			}
		super.addInformation(stack, aPlayer, list, bool);
	}
    
	 @Override
		public void onUpdate(ItemStack iStack, World world, Entity entityHolding, int p_77663_4_, boolean p_77663_5_) {
			if (!world.isRemote){				
				if (this.sRadiation > 0 && (entityHolding instanceof EntityLivingBase)) {
			         EntityLivingBase entityLiving = (EntityLivingBase) entityHolding;
			         if (!ItemArmorHazmat.hasCompleteHazmat(entityLiving)) {
			             IC2Potion.radiation.applyTo(entityLiving, sRadiation * 20, sRadiation * 10);
			         }
			     }
			}
		}

}

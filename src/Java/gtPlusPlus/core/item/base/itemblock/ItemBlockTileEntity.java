package gtPlusPlus.core.item.base.itemblock;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBlockTileEntity extends ItemBlock{

	String[] description;
	
	public ItemBlockTileEntity(Block block) {
		super(block);
	}
    
    @Override
	public void addInformation(ItemStack stack, EntityPlayer aPlayer, List list, boolean bool) {
				for (int i =0; i< this.description.length; i++){
					if (!this.description[i].equals("")){
						list.add(this.description[i]);
					}
				 }
				
			
		super.addInformation(stack, aPlayer, list, bool);
	}
    
	 @Override
		public void onUpdate(ItemStack iStack, World world, Entity entityHolding, int p_77663_4_, boolean p_77663_5_) {
		 
		}
	 
	 public void setDecription(String[] description){
		 for (int i =0; i< description.length; i++){
			 this.description[i] = description[i];
		 }
	 }

}

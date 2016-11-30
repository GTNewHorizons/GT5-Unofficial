package gtPlusPlus.core.item.general;

import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.base.CoreItem;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.player.PlayerUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBasicFirestarter extends CoreItem {

	public ItemBasicFirestarter() {
		super("itemSimpleFiremaker", AddToCreativeTab.tabTools, 1, 5, "Can probably make you a fire");		
		this.setTextureName("flint_and_steel");
	}

	@Override
	public boolean onItemUse(
			ItemStack thisItem, EntityPlayer thisPlayer, World thisWorld,
			int blockX, int blockY, int blockZ,
			int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_) {
		if (p_77648_7_ == 0) {
			--blockY;
		}
		if (p_77648_7_ == 1) {
			++blockY;
		}
		if (p_77648_7_ == 2) {
			--blockZ;
		}
		if (p_77648_7_ == 3) {
			++blockZ;
		}
		if (p_77648_7_ == 4) {
			--blockX;
		}
		if (p_77648_7_ == 5) {
			++blockX;
		}
		if (!thisPlayer.canPlayerEdit(blockX, blockY, blockZ, p_77648_7_, thisItem)) {
			return false;
		}
		if (thisWorld.isAirBlock(blockX, blockY, blockZ))
		{
			int random = MathUtils.randInt(0, 3);
			//Explode, lol.
			if (random == 0){
				PlayerUtils.messagePlayer(thisPlayer, "You somehow managed to set yourself on fire... ");
				thisWorld.playSoundEffect((double)thisPlayer.posX + 0.5D, (double)thisPlayer.posY + 0.5D, (double)thisPlayer.posZ + 0.5D, "fire.ignite", 1.0F, itemRand.nextFloat() * 0.4F + 0.8F);
				thisPlayer.setFire(4);	
				thisItem.damageItem(thisItem.getMaxDamage(), thisPlayer);
			}

			//Create a fire
			else if (random == 2){
				PlayerUtils.messagePlayer(thisPlayer, "You created a fire!");
				thisWorld.playSoundEffect((double)blockX + 0.5D, (double)blockY + 0.5D, (double)blockZ + 0.5D, "fire.ignite", 1.0F, itemRand.nextFloat() * 0.4F + 0.8F);
				thisWorld.setBlock(blockX, blockY, blockZ, Blocks.fire);	
			}

			//Do nothing
			else {
				PlayerUtils.messagePlayer(thisPlayer, "Your attemp does nothing.");
				thisItem.damageItem(1, thisPlayer);
				return false;
			}
		}
		thisItem.damageItem(1, thisPlayer);
		return true;
	}

	@Override
	public String getItemStackDisplayName(ItemStack thisItem) {
		return "Basic Firemaker";
	}
}
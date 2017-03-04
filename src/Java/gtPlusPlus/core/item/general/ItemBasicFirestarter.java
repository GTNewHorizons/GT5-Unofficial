package gtPlusPlus.core.item.general;

import gtPlusPlus.core.block.general.FirePit;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.base.CoreItem;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.player.PlayerUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBasicFirestarter extends CoreItem {

	public ItemBasicFirestarter() {
		super("itemSimpleFiremaker", AddToCreativeTab.tabTools, 1, 5, "Can probably make you a fire");
		this.setTextureName(CORE.MODID+":"+"itemFireStarter");
	}

	@Override
	public boolean onItemUse(
			final ItemStack thisItem, final EntityPlayer thisPlayer, final World thisWorld,
			int blockX, int blockY, int blockZ,
			final int p_77648_7_, final float p_77648_8_, final float p_77648_9_, final float p_77648_10_) {
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
		if (thisWorld.getBlock(blockX, blockY, blockZ) instanceof FirePit){
			thisWorld.setBlockMetadataWithNotify(blockX, blockY, blockZ, 2, 4);
			PlayerUtils.messagePlayer(thisPlayer, "You light the fire pit. ");
		}
		if (thisWorld.isAirBlock(blockX, blockY, blockZ))
		{
			final int random = MathUtils.randInt(0, 3);
			//Explode, lol.
			if (random == 0){
				PlayerUtils.messagePlayer(thisPlayer, "You somehow managed to set yourself on fire... ");
				thisWorld.playSoundEffect(thisPlayer.posX + 0.5D, thisPlayer.posY + 0.5D, thisPlayer.posZ + 0.5D, "fire.ignite", 1.0F, (itemRand.nextFloat() * 0.4F) + 0.8F);
				thisPlayer.setFire(4);
				thisItem.damageItem(thisItem.getMaxDamage(), thisPlayer);
			}

			//Create a fire
			else if (random == 2){
				PlayerUtils.messagePlayer(thisPlayer, "You created a fire!");
				thisWorld.playSoundEffect(blockX + 0.5D, blockY + 0.5D, blockZ + 0.5D, "fire.ignite", 1.0F, (itemRand.nextFloat() * 0.4F) + 0.8F);
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
	public String getItemStackDisplayName(final ItemStack thisItem) {
		return "Basic Firemaker";
	}
}
package gtPlusPlus.core.item.general;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import gtPlusPlus.core.item.base.BaseItemBurnable;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.NBTUtils;

public class ItemGiantEgg extends BaseItemBurnable {

	public ItemGiantEgg(String unlocalizedName, String displayName, CreativeTabs creativeTab, int stackSize, int maxDmg,
			String description, String oredictName, int burnTime, int meta) {
		super(unlocalizedName, displayName, creativeTab, stackSize, maxDmg, description, oredictName, burnTime, meta);
	}

	@Override
	public String getItemStackDisplayName(ItemStack aStack) {
		String localName = super.getItemStackDisplayName(aStack);
		nbtWork(aStack);
		int size = 1;
		if (NBTUtils.hasKey(aStack, "size")){
			size = NBTUtils.getInteger(aStack, "size");
			return ""+size+" "+localName;
		}
		return "?? "+localName;
	}

	@Override
	public void onUpdate(ItemStack aStack, World world, Entity entityHolding, int p_77663_4_, boolean p_77663_5_) {
		if (entityHolding != null && entityHolding instanceof EntityPlayer) {
			NBTUtils.setBoolean(aStack, "playerHeld", true);
		}
		else {
			NBTUtils.setBoolean(aStack, "playerHeld", false);
		}		
		nbtWork(aStack);
		super.onUpdate(aStack, world, entityHolding, p_77663_4_, p_77663_5_);
	}

	@Override
	public void onCreated(ItemStack p_77622_1_, World p_77622_2_, EntityPlayer p_77622_3_) {
		// TODO Auto-generated method stub
		super.onCreated(p_77622_1_, p_77622_2_, p_77622_3_);
	}
	
	public void nbtWork(ItemStack aStack) {
		if (NBTUtils.hasKey(aStack, "playerHeld")) {
			if (NBTUtils.getBoolean(aStack, "playerHeld") && !NBTUtils.hasKey(aStack, "size")) {
				NBTUtils.setInteger(aStack, "size", MathUtils.randInt(1, 8));
			}
		}		
	}

}

package gtPlusPlus.core.item.general;

import static gtPlusPlus.core.lib.CORE.RANDOM;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import gtPlusPlus.core.entity.item.ItemEntityGiantEgg;
import gtPlusPlus.core.item.base.BaseItemBurnable;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.NBTUtils;

public class ItemGiantEgg extends BaseItemBurnable {

	public ItemGiantEgg(String unlocalizedName, String displayName, CreativeTabs creativeTab, int stackSize, int maxDmg,
			String description, String oredictName, int burnTime, int meta) {
		super(unlocalizedName, displayName, creativeTab, stackSize, maxDmg, description, oredictName, burnTime, meta);
		this.setMaxStackSize(1);
	}

	@Override
	public String getItemStackDisplayName(ItemStack aStack) {
		String localName = super.getItemStackDisplayName(aStack);
		nbtWork(aStack);
		int size = 1;
		int age = 0;
		if (NBTUtils.hasKey(aStack, "size")){
			size = NBTUtils.getInteger(aStack, "size");
			if (NBTUtils.hasKey(aStack, "mTicksExisted")){
				age = NBTUtils.getInteger(aStack, "mTicksExisted");
				return ""+size+" "+localName+" ["+age+"]";
			}
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
		super.onCreated(p_77622_1_, p_77622_2_, p_77622_3_);
	}

	public void nbtWork(ItemStack aStack) {
		if (NBTUtils.hasKey(aStack, "playerHeld")) {
			if (NBTUtils.getBoolean(aStack, "playerHeld") && !NBTUtils.hasKey(aStack, "size")) {
				NBTUtils.setInteger(aStack, "size", MathUtils.randInt(1, 8));
			}
		}		
	}

	@Override
	public boolean hasCustomEntity(ItemStack stack) {
		return true;
	}

	@Override
	public Entity createEntity(World world, Entity location, ItemStack itemstack) {

		if (location instanceof EntityPlayer) {

			EntityPlayer player = (EntityPlayer) location;

			if (itemstack == null) {
				return null;
			}
			else if (itemstack.stackSize == 0) {
				return null;
			}
			else {
				ItemEntityGiantEgg entityitem = new ItemEntityGiantEgg(world, player.posX, player.posY - 0.30000001192092896D + (double)player.getEyeHeight(), player.posZ, itemstack);
				entityitem.delayBeforeCanPickup = 40;
				entityitem.func_145799_b(player.getCommandSenderName());
				float f = 0.1F;
				float f1;
				f = 0.3F;
				entityitem.motionX = (double)(-MathHelper.sin(player.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(player.rotationPitch / 180.0F * (float)Math.PI) * f);
				entityitem.motionZ = (double)(MathHelper.cos(player.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(player.rotationPitch / 180.0F * (float)Math.PI) * f);
				entityitem.motionY = (double)(-MathHelper.sin(player.rotationPitch / 180.0F * (float)Math.PI) * f + 0.1F);
				f = 0.02F;
				f1 = RANDOM.nextFloat() * (float)Math.PI * 2.0F;
				f *= RANDOM.nextFloat();
				entityitem.motionX += Math.cos((double)f1) * (double)f;
				entityitem.motionY += (double)((RANDOM.nextFloat() - RANDOM.nextFloat()) * 0.1F);
				entityitem.motionZ += Math.sin((double)f1) * (double)f;
				return entityitem;
			}
		}
		return null;
	}

}

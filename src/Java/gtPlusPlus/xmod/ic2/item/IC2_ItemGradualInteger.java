package gtPlusPlus.xmod.ic2.item;

import ic2.api.item.ICustomDamageItem;
import ic2.core.util.StackUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class IC2_ItemGradualInteger
extends IC2_ItemGradual
implements ICustomDamageItem
{
	private final int maxDmg;

	public IC2_ItemGradualInteger(final String internalName, final int maxdmg)
	{
		super(internalName);

		this.maxDmg = maxdmg;
	}

	@Override
	public int getCustomDamage(final ItemStack stack)
	{
		final NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
		return nbt.getInteger("advDmg");
	}

	@Override
	public int getMaxCustomDamage(final ItemStack stack)
	{
		return this.maxDmg;
	}

	@Override
	public void setCustomDamage(final ItemStack stack, final int damage)
	{
		final NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
		nbt.setInteger("advDmg", 0);

		final int maxStackDamage = stack.getMaxDamage();
		if (maxStackDamage > 2) {
			//stack.setItemDamage(1 + (int)Util.map(damage, this.maxDmg, maxStackDamage - 2));
		}
	}

	@Override
	public boolean applyCustomDamage(final ItemStack stack, final int damage, final EntityLivingBase src)
	{
		this.setCustomDamage(stack, this.getCustomDamage(stack) + damage);
		return true;
	}
}

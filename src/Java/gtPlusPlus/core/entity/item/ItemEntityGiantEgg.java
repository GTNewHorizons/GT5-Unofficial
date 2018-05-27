package gtPlusPlus.core.entity.item;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.entity.monster.EntityGiantChickenBase;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.NBTUtils;

public class ItemEntityGiantEgg extends EntityItem {

	/**
	 * The maximum age of this Chicken Egg.  The item will try hatch once this is reached.
	 */
	public int mEggAge = 10000;
	public int mEggSize = -1;

	public ItemEntityGiantEgg(World aWorld) {
		super(aWorld);
	}

	public ItemEntityGiantEgg(World aWorld, double aX, double aY, double aZ) {
		super(aWorld, aX, aY, aZ);
	}

	public ItemEntityGiantEgg(World aWorld, double aX, double aY, double aZ, ItemStack aStack) {
		super(aWorld, aX, aY, aZ, aStack);
	}


	//Large eggs don't despawn, because they will try hatch first.
	@Override
	public void onUpdate() {
		if (this.lifespan != Integer.MAX_VALUE) {
			this.lifespan = Integer.MAX_VALUE;
		}
		
		if (this.getEntityItem() != null) {
			ItemStack g = this.getEntityItem();
			NBTUtils.setInteger(g, "mTicksExisted", this.age);
			this.setEntityItemStack(g);
			Logger.INFO("Writing age to NBT of stored stack item.");
		}
		else {
			ItemStack g = ItemUtils.getSimpleStack(ModItems.itemBigEgg);
			NBTUtils.setInteger(g, "mTicksExisted", this.age);
			this.setEntityItemStack(g);
			Logger.INFO("Writing age to NBT of new stack item.");
			
		}
		
		if (this.age >= 1000) {
			//Cache the value for efficiency
			if (mEggSize == -1)
				mEggSize = (this.getEntityItem() != null ? (this.getEntityItem().hasTagCompound() ? (this.getEntityItem().getTagCompound().hasKey("size") ? this.getEntityItem().getTagCompound().getInteger("size") : 1) : 1) : 1);
			if (MathUtils.randInt(100*mEggSize, 1000) >= MathUtils.randInt(950, 1000)) {
				//Spawn Chicken
				spawnGiantChicken();
			}
		}
		super.onUpdate();
	}

	private void spawnGiantChicken() {
		try {
			EntityGiantChickenBase entitychicken = new EntityGiantChickenBase(this.worldObj);
			entitychicken.setGrowingAge(-24000);
			entitychicken.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
			this.worldObj.spawnEntityInWorld(entitychicken);
		}
		catch (Throwable t) {}
	}

	//These eggs also do not combine.
	@Override
	public boolean combineItems(EntityItem p_70289_1_) {
		return false;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound aNBT) {
		super.writeEntityToNBT(aNBT);
		aNBT.setInteger("mEggAge", mEggAge);		
		aNBT.setInteger("mTicksExisted", this.age);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound aNBT) {
		super.readEntityFromNBT(aNBT);
		mEggAge = aNBT.getInteger("mEggAge");
	}

	//They're fireproof
	@Override
	public void setFire(int p_70015_1_) {
	}

	@Override
	public boolean isBurning() {
		return false;
	}


}

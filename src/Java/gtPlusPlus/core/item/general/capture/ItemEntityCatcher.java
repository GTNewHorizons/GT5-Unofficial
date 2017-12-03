package gtPlusPlus.core.item.general.capture;

import java.util.UUID;

import gtPlusPlus.api.interfaces.IEntityCatcher;
import gtPlusPlus.core.util.nbt.NBTUtils;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemEntityCatcher extends Item implements IEntityCatcher {

	@Override
	public boolean hasEntity(ItemStack aStack) {
		if (NBTUtils.hasKey(aStack, "mHasEntity")){
			return NBTUtils.getBoolean(aStack, "mHasEntity");
		}
		return false;
	}

	@Override
	public Entity getStoredEntity(ItemStack aStack) {
		if (aStack == null || !NBTUtils.getBooleanTagCompound(aStack, "mEntity", "mHasEntity")){
			return null;
		}
		
		NBTTagCompound mEntityData;
		Class<? extends Entity> mEntityClass;
		String mClassName;
		int mEntityID, mEntityHashcode;
		UUID mUuidPersistent, mUuidUnique;

		mEntityData = NBTUtils.getTagCompound(aStack, "mEntity", "mEntityData");
		mEntityID = NBTUtils.getIntegerTagCompound(aStack, "mEntity", "mEntityID");
		mClassName = NBTUtils.getStringTagCompound(aStack, "mEntity", "mClassName");
		mUuidPersistent = UUID.fromString(NBTUtils.getStringTagCompound(aStack, "mEntity", "mUuidPersistent"));
		mUuidUnique = UUID.fromString(NBTUtils.getStringTagCompound(aStack, "mEntity", "mUuidUnique"));
		mEntityHashcode = NBTUtils.getIntegerTagCompound(aStack, "mEntity", "mEntityHashcode");
	}

	@Override
	public boolean setStoredEntity(ItemStack aStack, Entity aEntity) {
		if (aEntity == null){
			NBTUtils.setBoolean(aStack, "mHasEntity", true);
			return false;
		}
		
		NBTTagCompound mEntityData;
		Class<? extends Entity> mEntityClass;
		String mClassName;
		int mEntityID, mEntityHashcode;
		UUID mUuidPersistent, mUuidUnique;
		
		mEntityData = aEntity.getEntityData();
		mEntityClass = aEntity.getClass();
		mClassName = mEntityClass.getName();
		mEntityID = aEntity.getEntityId();
		mEntityHashcode = aEntity.hashCode();
		mUuidPersistent = aEntity.getPersistentID();
		mUuidUnique = aEntity.getUniqueID();

		NBTUtils.createTagCompound(aStack, "mEntity", "mEntityData", mEntityData);
		NBTUtils.createIntegerTagCompound(aStack, "mEntity", "mEntityID", mEntityID);
		NBTUtils.createStringTagCompound(aStack, "mEntity", "mClassName", mClassName);
		NBTUtils.createStringTagCompound(aStack, "mEntity", "mUuidPersistent", mUuidPersistent.toString());
		NBTUtils.createStringTagCompound(aStack, "mEntity", "mUuidUnique", mUuidUnique.toString());
		NBTUtils.createIntegerTagCompound(aStack, "mEntity", "mEntityHashcode", mEntityHashcode);
		NBTUtils.createBooleanTagCompound(aStack, "mEntity", "mHasEntity", true);
		return true;
	}

	@Override
	public Class getStoredEntityClass(ItemStack aStack) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}

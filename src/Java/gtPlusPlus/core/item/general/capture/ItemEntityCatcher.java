package gtPlusPlus.core.item.general.capture;

import java.util.List;
import java.util.UUID;

import gtPlusPlus.api.interfaces.IEntityCatcher;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.array.BlockPos;
import gtPlusPlus.core.util.nbt.NBTUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemEntityCatcher extends Item implements IEntityCatcher {
	
	//PlayerInteractEvent.EntityInteract;
	
	public ItemEntityCatcher(){
		Utils.registerEvent(this);
	}
	
	@Override
	public boolean hasEntity(ItemStack aStack) {
		if (NBTUtils.hasKey(aStack, "mHasEntity")){
			return NBTUtils.getBoolean(aStack, "mHasEntity");
		}
		return false;
	}

	@Override
	public Entity getStoredEntity(World aWorld, ItemStack aStack) {
		if (aStack == null || !NBTUtils.getBooleanTagCompound(aStack, "mEntity", "mHasEntity")){
			return null;
		}

		Entity mEntityToSpawn;
		int mEntityID;

		mEntityID = NBTUtils.getIntegerTagCompound(aStack, "mEntity", "mEntityID");		
		mEntityToSpawn = EntityList.createEntityByID(mEntityID, aWorld);
		if (mEntityToSpawn != null) {
			return mEntityToSpawn;
		}

		return null;		
	}

	@Override
	public boolean setStoredEntity(World aWorld, ItemStack aStack, Entity aEntity) {
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
		//mEntityID = aEntity.getEntityId();		
		mEntityID = EntityList.getEntityID(aEntity);
		mEntityHashcode = aEntity.hashCode();
		mUuidPersistent = aEntity.getPersistentID();
		mUuidUnique = aEntity.getUniqueID();

		NBTUtils.createTagCompound(aStack, "mEntityData", mEntityData);
		NBTUtils.createIntegerTagCompound(aStack, "mEntity", "mEntityID", mEntityID);
		NBTUtils.createStringTagCompound(aStack, "mEntity", "mClassName", mClassName);
		NBTUtils.createStringTagCompound(aStack, "mEntity", "mUuidPersistent", mUuidPersistent.toString());
		NBTUtils.createStringTagCompound(aStack, "mEntity", "mUuidUnique", mUuidUnique.toString());
		NBTUtils.createIntegerTagCompound(aStack, "mEntity", "mEntityHashcode", mEntityHashcode);
		NBTUtils.createBooleanTagCompound(aStack, "mEntity", "mHasEntity", true);
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends Entity> getStoredEntityClass(ItemStack aStack) {
		if (aStack == null || !NBTUtils.getBooleanTagCompound(aStack, "mEntity", "mHasEntity")){
			return null;
		}

		Class<? extends Entity> mEntityClass;
		String mClassName;

		mClassName = NBTUtils.getStringTagCompound(aStack, "mEntity", "mClassName");

		try {
			mEntityClass = (Class<? extends Entity>) Class.forName(mClassName);
			if (mEntityClass != null) {
				return mEntityClass;
			}
		}
		catch (ClassNotFoundException e) {}		
		return null;
	}

	@Override
	public boolean spawnStoredEntity(World aWorld, ItemStack aStack, BlockPos aPos) {
		if (aStack == null || !NBTUtils.getBooleanTagCompound(aStack, "mEntity", "mHasEntity")){
			return false;
		}

		NBTTagCompound mEntityData = NBTUtils.getTagCompound(aStack, "mEntityData");
		
		int mEntityID = NBTUtils.getIntegerTagCompound(aStack, "mEntity", "mEntityID");
		String mClassName = NBTUtils.getStringTagCompound(aStack, "mEntity", "mClassName");
		UUID mUuidPersistent = UUID.fromString(NBTUtils.getStringTagCompound(aStack, "mEntity", "mUuidPersistent"));
		UUID mUuidUnique = UUID.fromString(NBTUtils.getStringTagCompound(aStack, "mEntity", "mUuidUnique"));
		int mEntityHashcode = NBTUtils.getIntegerTagCompound(aStack, "mEntity", "mEntityHashcode");

		EntityLiving mEntityToSpawn = (EntityLiving) getStoredEntity(aWorld, aStack);
		Class<? extends Entity> mEntityClass = getStoredEntityClass(aStack);
		
		if (mEntityToSpawn != null && mEntityClass != null) {
			if (mEntityToSpawn.getEntityData() != mEntityData){
				NBTUtils.setEntityCustomData(mEntityToSpawn, mEntityData);
			}


			mEntityToSpawn.setLocationAndAngles(aPos.xPos, aPos.yPos, aPos.zPos, aWorld.rand.nextFloat() * 360.0F, 0.0F);
			if (mEntityToSpawn instanceof EntityLiving){
				((EntityLiving) mEntityToSpawn).onSpawnWithEgg(null);
				aWorld.spawnEntityInWorld(mEntityToSpawn);
			}
			if (mEntityToSpawn instanceof EntityLiving){
				((EntityLiving) mEntityToSpawn).playLivingSound();
			}
			NBTUtils.createBooleanTagCompound(aStack, "mEntity", "mHasEntity", false);


		}

		return false;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
		return super.onItemRightClick(aStack, aWorld, aPlayer);
	}

	@Override
	public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_) {
		super.addInformation(p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_);
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return false;
	}

	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int side,
			float xOffset, float yOffset, float zOffset) {		
		if (NBTUtils.hasKeyInTagCompound(itemstack, "mEntity", "mHasEntity") && NBTUtils.getBooleanTagCompound(itemstack, "mEntity", "mHasEntity")){
			return spawnStoredEntity(world, itemstack, new BlockPos(x, y, z));
		}
		else {
			return super.onItemUse(itemstack, player, world, x, y, z, side, xOffset, yOffset, zOffset);			
		}		
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack aStack, EntityPlayer aPlayer,	EntityLivingBase aEntity) {
		return super.itemInteractionForEntity(aStack, aPlayer, aEntity);
	}



}

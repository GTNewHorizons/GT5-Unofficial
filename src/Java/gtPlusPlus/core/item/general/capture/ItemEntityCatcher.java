package gtPlusPlus.core.item.general.capture;

import java.util.List;
import java.util.UUID;

import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraft.entity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import gtPlusPlus.api.interfaces.IEntityCatcher;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.minecraft.BlockPos;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.NBTUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;

public class ItemEntityCatcher extends Item implements IEntityCatcher {

	// PlayerInteractEvent.EntityInteract;


	public ItemEntityCatcher() {
		//Probably won't ever need this event handler.
		//Utils.registerEvent(this);
		this.setUnlocalizedName("itemDragonJar");
		this.setTextureName(CORE.MODID + ":" + getUnlocalizedName());
		this.setCreativeTab(AddToCreativeTab.tabMisc);
		this.setMaxStackSize(16);
		this.setMaxDamage(0);
		GameRegistry.registerItem(this, getUnlocalizedName());
	}

	@Override
	public boolean hasEntity(ItemStack aStack) {
		if (NBTUtils.hasKey(aStack, "mHasEntity")) {
			return NBTUtils.getBoolean(aStack, "mHasEntity");
		}
		return false;
	}

	@Override
	public Entity getStoredEntity(World aWorld, ItemStack aStack) {
		if (aStack == null || !hasEntity(aStack)) {
			Logger.INFO("Cannot get stored entity.");
			return null;
		}

		Entity mEntityToSpawn;
		int mEntityID;
		Logger.WARNING("getStoredEntity(1)");

		mEntityID = NBTUtils.getInteger(aStack, "mEntityID");
		mEntityToSpawn = EntityList.createEntityByID(mEntityID, aWorld);
		if (mEntityToSpawn != null) {
			Logger.WARNING("getStoredEntity(2)");
			return mEntityToSpawn;
		}

		Logger.INFO("Failed to get stored entity. - getStoredEntity()");
		return null;
	}

	@Override
	public boolean setStoredEntity(World aWorld, ItemStack aStack, Entity aEntity) {
		if (aEntity == null) {
			NBTUtils.setBoolean(aStack, "mHasEntity", false);
			Logger.INFO("Bad Entity being stored.");
			return false;
		}
		
		Logger.WARNING("setStoredEntity(1)");

		NBTTagCompound mEntityData;
		Class<? extends Entity> mEntityClass;
		String mClassName;
		String mEntityName;
		int mEntityID, mEntityHashcode;
		UUID mUuidPersistent, mUuidUnique;
		Logger.WARNING("setStoredEntity(2)");

		mEntityData = aEntity.getEntityData();
		mEntityClass = aEntity.getClass();
		mClassName = mEntityClass.getName();
		mEntityName = aEntity.getCommandSenderName();
		// mEntityID = aEntity.getEntityId();
		mEntityID = EntityList.getEntityID(aEntity);
		mEntityHashcode = aEntity.hashCode();
		mUuidPersistent = aEntity.getPersistentID();
		mUuidUnique = aEntity.getUniqueID();
		Logger.WARNING("setStoredEntity(3)");

		NBTUtils.createTagCompound(aStack, "mEntityData", mEntityData);
		NBTUtils.setString(aStack,"mEntityName", mEntityName);
		NBTUtils.setInteger(aStack,"mEntityID", mEntityID);
		NBTUtils.setString(aStack,"mClassName", mClassName);
		NBTUtils.setString(aStack,"mUuidPersistent", mUuidPersistent.toString());
		NBTUtils.setString(aStack,"mUuidUnique", mUuidUnique.toString());
		NBTUtils.setInteger(aStack,"mEntityHashcode", mEntityHashcode);
		NBTUtils.setBoolean(aStack,"mHasEntity", true);
		Logger.WARNING("setStoredEntity(4)");
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends Entity> getStoredEntityClass(ItemStack aStack) {
		if (aStack == null || !hasEntity(aStack)) {
			return null;
		}
		Class<? extends Entity> mEntityClass;
		String mClassName;
		mClassName = NBTUtils.getString(aStack,"mClassName");
		try {
			mEntityClass = (Class<? extends Entity>) Class.forName(mClassName);
			if (mEntityClass != null) {
				return mEntityClass;
			}
		}
		catch (ClassNotFoundException e) {
		}
		return null;
	}

	@Override
	public boolean spawnStoredEntity(World aWorld, ItemStack aStack, BlockPos aPos) {
		if (aStack == null || !hasEntity(aStack)) {
			Logger.INFO("Cannot release, either invalid Itemstack or no entity stored.");
			return false;
		}

		NBTTagCompound mEntityData = NBTUtils.getTagCompound(aStack, "mEntityData");

		int mEntityID = NBTUtils.getInteger(aStack,"mEntityID");
		String mClassName = NBTUtils.getString(aStack,"mClassName");
		UUID mUuidPersistent = UUID.fromString(NBTUtils.getString(aStack,"mUuidPersistent"));
		UUID mUuidUnique = UUID.fromString(NBTUtils.getString(aStack,"mUuidUnique"));
		int mEntityHashcode = NBTUtils.getInteger(aStack,"mEntityHashcode");

		EntityLiving mEntityToSpawn = (EntityLiving) getStoredEntity(aWorld, aStack);
		Class<? extends Entity> mEntityClass = getStoredEntityClass(aStack);
		
		Logger.WARNING("spawnStoredEntity(1)");

		if (mEntityToSpawn != null && mEntityClass != null) {
			Logger.WARNING("spawnStoredEntity(2)");
			if (mEntityToSpawn.getEntityData() != mEntityData) {
				Logger.WARNING("spawnStoredEntity(x)");
				NBTUtils.setEntityCustomData(mEntityToSpawn, mEntityData);
			}

			mEntityToSpawn.setLocationAndAngles(aPos.xPos, aPos.yPos, aPos.zPos, aWorld.rand.nextFloat() * 360.0F,
					0.0F);
			if (mEntityToSpawn != null) {
				mEntityToSpawn.onSpawnWithEgg(null);
				aWorld.spawnEntityInWorld(mEntityToSpawn);
				Logger.WARNING("spawnStoredEntity(3)");
			}
			if (mEntityToSpawn != null) {
				mEntityToSpawn.playLivingSound();
				Logger.WARNING("spawnStoredEntity(4)");
			}
			Logger.WARNING("spawnStoredEntity(5)");
			NBTUtils.setBoolean(aStack,"mHasEntity", false);
			return true;
		}
		Logger.INFO("Failed to spawn stored entity. - spawnStoredEntity()");
		return false;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
		return super.onItemRightClick(aStack, aWorld, aPlayer);
	}

	@Override
	public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_) {
		if (hasEntity(p_77624_1_)){
			String mName = NBTUtils.getString(p_77624_1_,"mEntityName");
			if (mName != null && !mName.equals("")){
				p_77624_3_.add(EnumChatFormatting.GRAY+"Contains a "+mName+".");				
			}
		}
		else {
			p_77624_3_.add(EnumChatFormatting.GRAY+"Does not contain anything.");
		}
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return false;
	}

	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int side,
			float xOffset, float yOffset, float zOffset) {
		if (Utils.isServer()) {
			Logger.WARNING("Trying to release (1)");
			if (NBTUtils.hasKey(itemstack,"mHasEntity")
					&& NBTUtils.getBoolean(itemstack,"mHasEntity")) {
				Logger.WARNING("Trying to release (2)");
				boolean mDidSpawn =  spawnStoredEntity(world, itemstack, new BlockPos(x, y+1, z, world));
				
				if (!mDidSpawn){
					PlayerUtils.messagePlayer(player, "You failed to release a "+NBTUtils.getString(itemstack,"mEntityName")+".");					
				}
				
				return mDidSpawn;
				
			}
		}
		return super.onItemUse(itemstack, player, world, x, y, z, side, xOffset, yOffset, zOffset);

	}

	@Override
	public boolean itemInteractionForEntity(ItemStack aStack, EntityPlayer aPlayer, EntityLivingBase aEntity) {
		if (Utils.isServer()) {
			Logger.WARNING("Trying to catch (1)");
			if (!hasEntity(aStack)) {
				Logger.WARNING("Trying to catch (2)");
				boolean mStored = setStoredEntity(aPlayer.worldObj, aStack, aEntity);
				if (mStored) {
					Logger.WARNING("Trying to catch (3)");
					aEntity.setDead();
					PlayerUtils.messagePlayer(aPlayer, "You have captured a "+NBTUtils.getString(aStack,"mEntityName")+" in the Jar.");
					//NBTUtils.tryIterateNBTData(aStack);
				}
			}
		}
		return super.itemInteractionForEntity(aStack, aPlayer, aEntity);
	}

	@Override
	public String getItemStackDisplayName(ItemStack aStack) {
		if (hasEntity(aStack)){
			return "Captured Dragon Jar";
		}
		return "Dragon Capture Jar";
	}

}

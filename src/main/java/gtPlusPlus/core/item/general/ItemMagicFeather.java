package gtPlusPlus.core.item.general;

import java.util.*;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.item.base.CoreItem;
import gtPlusPlus.core.lib.CORE;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class ItemMagicFeather extends CoreItem {

	public static final String NAME = "magicfeather";
	private static final WeakHashMap<UUID, MagicFeatherData> sPlayerData = new WeakHashMap<UUID, MagicFeatherData>();
	private static final WeakHashMap<UUID, HashSet<TileEntityBeacon>> sBeaconData = new WeakHashMap<UUID, HashSet<TileEntityBeacon>>();

	public ItemMagicFeather() {
		super("magicfeather", AddToCreativeTab.tabMisc, 1, 100, new String[]{"Lets you fly around Beacons"}, EnumRarity.uncommon, null, false, null);
		setMaxStackSize(1);
		setUnlocalizedName(CORE.MODID + ":" + NAME);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public int getEntityLifespan(ItemStack itemStack, World world) {
		return Integer.MAX_VALUE;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
		list.add("Does not need to be the item held in your hand to work");
		super.addInformation(stack, aPlayer, list, bool);
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		list.add("");
		if (player != null) {
			if (!isInBeaconRange(player)) {
				list.add("" + EnumChatFormatting.RED + "Needs to be within beacon range");
			}
			else {
				list.add("" + EnumChatFormatting.GOLD + "Fly like an eagle");
			}
			HashSet<TileEntityBeacon> aBeaconData = sBeaconData.get(player.getUniqueID());
			if (aBeaconData != null && !aBeaconData.isEmpty()) {
				list.add("Nearby Beacons:");
				for (TileEntityBeacon aBeacon : aBeaconData) {
					int level = aBeacon.getLevels();
					if (level > 0) {
						int radius = (level * 10 + 10);
						int x = aBeacon.xCoord;
						int z = aBeacon.zCoord;
						int y = aBeacon.yCoord;
						list.add("Level: "+level+", Radius: "+radius);
						list.add("Location: "+x+", "+y+", "+z);
					}					
				}
			}
		}
	}

	public boolean hasCustomEntity(ItemStack stack) {
		return true;
	}

	private static boolean isInBeaconRange(EntityPlayer player) {
		World world = player.getEntityWorld();
		if (world.isRemote) {
			HashSet<TileEntityBeacon> aBeaconData = sBeaconData.get(player.getUniqueID());
			if (aBeaconData != null) {
				return !aBeaconData.isEmpty();
			}
			return false;
		}
		List<TileEntity> tileEntities = world.loadedTileEntityList;
		HashSet<TileEntityBeacon> aBeaconData = sBeaconData.get(player.getUniqueID());
		if (aBeaconData == null) {
			aBeaconData = new HashSet<TileEntityBeacon>();
			sBeaconData.put(player.getUniqueID(), aBeaconData);
		}
		for (TileEntity t : tileEntities) {
			if (!(t instanceof TileEntityBeacon)) {
				continue;
			}
			TileEntityBeacon beacon = (TileEntityBeacon) t;
			int level = beacon.getLevels();
			if (level == 0) {
				continue;
			}
			int radius = (level * 10 + 10);
			int x = beacon.xCoord;
			int z = beacon.zCoord;
			if (player.posX < (x - radius) || player.posX > (x + radius)) {
				continue;
			}
			if (player.posZ < (z - radius) || player.posZ > (z + radius)) {
				continue;
			}
			if (!aBeaconData.contains(beacon)) {
				aBeaconData.add(beacon);
			}
		}
		if (aBeaconData.size() > 0) {
			return true;
		}

		return false;
	}

	private static void setMayFly(EntityPlayer player, boolean mayFly) {
		if (player.capabilities.allowFlying == mayFly) {
			return;
		}
		if (!mayFly) {
			// force the player on the ground then remove ability to fly
			// this prevent crashing the the ground and dying
			// when you accidentally get out of the beacon range
			player.capabilities.isFlying = false;
			if (player.onGround && player.fallDistance < 1F) {
				player.capabilities.allowFlying = false;
			}
		}
		else {
			player.capabilities.allowFlying = true;
		}
		player.sendPlayerAbilities();
	}

	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.side != Side.SERVER) {
			return;
		}
		EntityPlayer player = event.player;
		HashSet<TileEntityBeacon> aBeaconData = sBeaconData.get(player.getUniqueID());
		if (aBeaconData != null && !aBeaconData.isEmpty()) {
			HashSet<TileEntityBeacon> aRemovals = new HashSet<TileEntityBeacon>();
			for (TileEntityBeacon aBeacon : aBeaconData) {
				int level = aBeacon.getLevels();
				if (level == 0) {
					aRemovals.add(aBeacon);
					continue;
				}
				int radius = (level * 10 + 10);
				int x = aBeacon.xCoord;
				int z = aBeacon.zCoord;
				if (player.posX < (x - radius) || player.posX > (x + radius)) {
					aRemovals.add(aBeacon);
					continue;
				}
				if (player.posZ < (z - radius) || player.posZ > (z + radius)) {
					aRemovals.add(aBeacon);
					continue;
				}
			}
			aBeaconData.removeAll(aRemovals);
		}
		boolean hasItem = hasItem(player, ModItems.itemMagicFeather);
		if (!hasItem) {
			ItemMagicFeather.sPlayerData.remove(player.getUniqueID());			
		}		
		MagicFeatherData data = ItemMagicFeather.sPlayerData.get(player.getUniqueID());
		if (data == null) {
			data = new MagicFeatherData(player);
			ItemMagicFeather.sPlayerData.put(player.getUniqueID(), data);
		}
		data.onTick();
	}
	
	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void onPlayerDeath(LivingDeathEvent event) {		
		if (event.entityLiving != null) {
			EntityLivingBase aEntity = event.entityLiving;
			if (aEntity instanceof EntityPlayer && aEntity.worldObj != null) {
				if (aEntity.worldObj.isRemote) {
					return;
				}
				else {
					EntityPlayer aPlayer = (EntityPlayer) aEntity;
					ItemMagicFeather.sPlayerData.remove(aPlayer.getUniqueID());
					ItemMagicFeather.sBeaconData.remove(aPlayer.getUniqueID());
				}
			}			
		}		
	}

	private static boolean hasItem(EntityPlayer player, Item item) {
		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack stack = player.inventory.getStackInSlot(i);
			if (stack != null && stack.getItem() != null && stack.getItem() instanceof ItemMagicFeather) {
				return true;
			}
		}
		return false;
	}

	private static class MagicFeatherData {

		private final EntityPlayer player;
		private boolean hasItem = false;
		private int checkTick = 0;
		private boolean beaconInRangeCache;

		public MagicFeatherData(EntityPlayer player) {
			this.player = player;
			this.beaconInRangeCache = player.capabilities.allowFlying;
		}

		public void onTick() {
			try {
			boolean hasItem = hasItem(player, ModItems.itemMagicFeather);
			if (hasItem != this.hasItem) {
				if (hasItem) {
					this.onAdd();
				}
				if (!hasItem) {
					this.onRemove();
				}
				this.hasItem = hasItem;
				Logger.INFO("Ticking feather "+hasItem);
				return;
			}
			}
			catch (Throwable t) {
				t.printStackTrace();
			}

			boolean mayFly = player.capabilities.isCreativeMode || (hasItem && checkBeaconInRange(player));
			setMayFly(player, mayFly);
		}

		private void onAdd() {
			if (!ItemMagicFeather.isInBeaconRange(player)) {
				return;
			}
			setMayFly(player, true);
		}

		private void onRemove() {
			if (player.capabilities.isCreativeMode) {
				return;
			}
			setMayFly(player, false);
		}

		private boolean checkBeaconInRange(EntityPlayer player) {
			if (checkTick++ % 40 != 0) {
				return beaconInRangeCache;
			}
			beaconInRangeCache = ItemMagicFeather.isInBeaconRange(player);
			return beaconInRangeCache;
		}
	}

}

package gtPlusPlus.core.item.general;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.WeakHashMap;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.item.base.CoreItem;

public class ItemMagicFeather extends CoreItem {

    public static final String NAME = "magicfeather";
    private static final WeakHashMap<EntityPlayer, MagicFeatherData> sPlayerData = new WeakHashMap<>();
    private static final WeakHashMap<EntityPlayer, HashSet<TileEntityBeacon>> sBeaconData = new WeakHashMap<>();

    public ItemMagicFeather() {
        super(
                "magicfeather",
                AddToCreativeTab.tabMisc,
                1,
                100,
                new String[] { "Lets you fly around Beacons" },
                EnumRarity.uncommon,
                null,
                false,
                null);
        setMaxStackSize(1);
        setUnlocalizedName(GTPlusPlus.ID + ":" + NAME);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, World world) {
        return Integer.MAX_VALUE;
    }

    @SuppressWarnings("unchecked")
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
        list.add("Does not need to be the item held in your hand to work");
        super.addInformation(stack, aPlayer, list, bool);
        list.add("Needs to be within beacon range");
        list.add("Range is beacon level * 10 + 10");
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    private static boolean isInBeaconRange(EntityPlayer player) {
        World world = player.getEntityWorld();
        if (world.isRemote) {
            return false;
        }
        HashSet<TileEntityBeacon> aBeaconData = sBeaconData.computeIfAbsent(player, k -> new HashSet<>());
        int chunkXlo = (int) (player.posX - 50) >> 4, chunkXhi = (int) (player.posX + 50) >> 4,
                chunkZlo = (int) (player.posZ - 50) >> 4, chunkZhi = (int) (player.posZ + 50) >> 4;
        for (int chunkX = chunkXlo; chunkX < chunkXhi; chunkX++) {
            for (int chunkZ = chunkZlo; chunkZ < chunkZhi; chunkZ++) {
                if (!world.getChunkProvider().chunkExists(chunkX, chunkZ)) continue;
                findSuitableBeacon(
                        player,
                        world.getChunkFromChunkCoords(chunkX, chunkZ).chunkTileEntityMap.values(),
                        aBeaconData);
            }
        }
        return aBeaconData.size() > 0;
    }

    private static void findSuitableBeacon(EntityPlayer player, Collection<TileEntity> tileEntities,
            HashSet<TileEntityBeacon> aBeaconData) {
        for (TileEntity t : tileEntities) {
            if (!(t instanceof TileEntityBeacon beacon)) {
                continue;
            }
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
            aBeaconData.add(beacon);
        }
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
        } else {
            player.capabilities.allowFlying = true;
        }
        player.sendPlayerAbilities();
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side != Side.SERVER || event.phase != Phase.END) {
            return;
        }
        EntityPlayer player = event.player;
        HashSet<TileEntityBeacon> aBeaconData = sBeaconData.get(player);
        if (aBeaconData != null && !aBeaconData.isEmpty()) {
            for (Iterator<TileEntityBeacon> iterator = aBeaconData.iterator(); iterator.hasNext();) {
                TileEntityBeacon aBeacon = iterator.next();
                int level = aBeacon.getLevels();
                if (level == 0) {
                    iterator.remove();
                    continue;
                }
                int radius = (level * 10 + 10);
                int x = aBeacon.xCoord;
                int z = aBeacon.zCoord;
                if (player.posX < (x - radius) || player.posX > (x + radius)
                        || player.posZ < (z - radius)
                        || player.posZ > (z + radius)) {
                    iterator.remove();
                }
            }
        }
        boolean hasItem = hasItem(player, ModItems.itemMagicFeather);
        if (!hasItem) {
            ItemMagicFeather.sPlayerData.remove(player);
        }
        MagicFeatherData data = ItemMagicFeather.sPlayerData.get(player);
        if (data == null) {
            data = new MagicFeatherData(player);
            ItemMagicFeather.sPlayerData.put(player, data);
        }
        data.onTick();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onPlayerDeath(LivingDeathEvent event) {
        if (event.entityLiving == null) return;
        EntityLivingBase aEntity = event.entityLiving;
        if (!(aEntity instanceof EntityPlayer aPlayer) || aEntity.worldObj == null || aEntity.worldObj.isRemote) return;
        ItemMagicFeather.sPlayerData.remove(aPlayer);
        ItemMagicFeather.sBeaconData.remove(aPlayer);
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

        private final WeakReference<EntityPlayer> player;
        private boolean hasItem = false;
        private int checkTick = 0;
        private boolean beaconInRangeCache;

        public MagicFeatherData(EntityPlayer player) {
            this.player = new WeakReference<>(player);
            this.beaconInRangeCache = player.capabilities.allowFlying;
        }

        public void onTick() {
            EntityPlayer player = this.player.get();
            if (player == null) return;
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
                    Logger.INFO("Ticking feather " + hasItem);
                    return;
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }

            if (hasItem) {
                // only modify if hasItem. Override other flight methods since you are literally holding this item in
                // your own inventory. You have sent your consent.
                boolean mayFly = player.capabilities.isCreativeMode || checkBeaconInRange(player);
                setMayFly(player, mayFly);
            }
        }

        private void onAdd() {
            if (!ItemMagicFeather.isInBeaconRange(getPlayer())) {
                return;
            }
            setMayFly(getPlayer(), true);
        }

        private void onRemove() {
            if (getPlayer().capabilities.isCreativeMode) {
                return;
            }
            setMayFly(getPlayer(), false);
        }

        private boolean checkBeaconInRange(EntityPlayer player) {
            if (checkTick++ % 40 != 0) {
                return beaconInRangeCache;
            }
            beaconInRangeCache = ItemMagicFeather.isInBeaconRange(player);
            return beaconInRangeCache;
        }

        private EntityPlayer getPlayer() {
            return player.get();
        }
    }
}

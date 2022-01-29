package gtPlusPlus.core.item.general;

import java.util.List;
import java.util.WeakHashMap;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.item.base.CoreItem;
import gtPlusPlus.core.lib.CORE;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class ItemMagicFeather extends CoreItem {

    public static final String NAME = "magicfeather";
    private static final WeakHashMap<EntityPlayer, MagicFeatherData> playerData = new WeakHashMap<>();

    public ItemMagicFeather() {
    	super("magicfeather", AddToCreativeTab.tabMisc, 1, 100, new String[] {"Lets you fly around Beacons"}, EnumRarity.rare, EnumChatFormatting.BOLD, false, null);
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
        super.addInformation(stack, aPlayer, list, bool);
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (player != null) {
            if (!isInBeaconRange(player)) {
            	list.add(""+EnumChatFormatting.RED+"Needs to be within beacon range");
            }
        }
    }

    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    private static boolean isInBeaconRange(EntityPlayer player) {
        World world = player.getEntityWorld();

        List<TileEntity> tileEntities = world.loadedTileEntityList;
        for (TileEntity t : tileEntities) {
            if (!(t instanceof TileEntityBeacon)) {
                continue;
            }

            TileEntityBeacon beacon = (TileEntityBeacon) t;

            int level = beacon.getLevels();
            int radius = (level * 10 + 10);

            int x = beacon.xCoord;
            int z = beacon.zCoord;;

            if (player.posX < (x - radius) || player.posX > (x + radius)) {
                continue;
            }

            if (player.posZ < (z - radius) || player.posZ > (z + radius)) {
                continue;
            }

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
        } else {
            player.capabilities.allowFlying = true;
        }

        player.sendPlayerAbilities();
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side != Side.SERVER) {
            return;
        }

        EntityPlayer player = event.player;

        MagicFeatherData data = ItemMagicFeather.playerData.get(player);
        if (data == null) {
            data = new MagicFeatherData(player);
            ItemMagicFeather.playerData.put(player, data);
        }

        data.onTick();
    }

    private static boolean hasItem(EntityPlayer player, Item item) {
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (item == stack.getItem()) {
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

            boolean hasItem = hasItem(player, ModItems.itemMagicFeather);
            if (hasItem != this.hasItem) {
                if (hasItem) {
                    this.onAdd();
                }

                if (!hasItem) {
                    this.onRemove();
                }

                this.hasItem = hasItem;
                return;
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
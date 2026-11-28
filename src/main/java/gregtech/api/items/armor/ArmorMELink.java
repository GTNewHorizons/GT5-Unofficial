package gregtech.api.items.armor;

import java.util.Map;
import java.util.WeakHashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import appeng.api.AEApi;
import appeng.api.config.SecurityPermissions;
import appeng.api.config.Settings;
import appeng.api.config.SortDir;
import appeng.api.config.SortOrder;
import appeng.api.config.ViewItems;
import appeng.api.features.ILocatable;
import appeng.api.features.IWirelessTermHandler;
import appeng.api.implementations.tiles.IWirelessAccessPoint;
import appeng.api.networking.IGrid;
import appeng.api.networking.IGridHost;
import appeng.api.networking.IGridNode;
import appeng.api.networking.security.ISecurityGrid;
import appeng.api.networking.storage.IStorageGrid;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.util.DimensionalCoord;
import appeng.api.util.IConfigManager;
import appeng.tile.networking.TileWireless;
import appeng.util.ConfigManager;
import gregtech.api.items.armor.AugmentBuilder.AugmentCategory;
import gregtech.api.items.armor.MechArmorAugmentRegistries.Augments;
import gregtech.common.items.armor.MechArmorBase;

public class ArmorMELink implements IWirelessTermHandler {

    public static final String LINK_KEY = "MELinkKey";
    private static final double EU_PER_AE = 0.5;
    private static final ArmorMELink INSTANCE = new ArmorMELink();
    private static final Map<EntityPlayer, IWirelessAccessPoint> lastAccessPoint = new WeakHashMap<>();

    public static void register() {
        AEApi.instance()
            .registries()
            .wireless()
            .registerWirelessHandler(INSTANCE);
    }

    public static IMEMonitor<IAEItemStack> getNetworkInventory(EntityPlayer player, ItemStack armor) {
        IGrid grid = getLinkedGrid(armor);
        if (grid == null || !isInRange(grid, player)) return null;

        ISecurityGrid security = grid.getCache(ISecurityGrid.class);
        if (security != null && !security.hasPermission(player, SecurityPermissions.EXTRACT)) return null;

        IStorageGrid storage = grid.getCache(IStorageGrid.class);
        return storage == null ? null : storage.getItemInventory();
    }

    public static boolean isLinked(ItemStack armor) {
        return !INSTANCE.getEncryptionKey(armor)
            .isEmpty();
    }

    private static IGrid getLinkedGrid(ItemStack armor) {
        String key = INSTANCE.getEncryptionKey(armor);
        if (key.isEmpty()) return null;

        ILocatable linked;
        try {
            linked = AEApi.instance()
                .registries()
                .locatable()
                .getLocatableBy(Long.parseLong(key));
        } catch (NumberFormatException e) {
            return null;
        }

        if (!(linked instanceof IGridHost host)) return null;
        IGridNode node = host.getGridNode(ForgeDirection.UNKNOWN);
        return node == null ? null : node.getGrid();
    }

    private static boolean isInRange(IGrid grid, EntityPlayer player) {
        IWirelessAccessPoint cached = lastAccessPoint.get(player);
        if (cached != null && cached.getGrid() == grid && reaches(cached, player)) return true;

        for (IGridNode node : grid.getMachines(TileWireless.class)) {
            if (node.getMachine() instanceof IWirelessAccessPoint accessPoint && reaches(accessPoint, player)) {
                lastAccessPoint.put(player, accessPoint);
                return true;
            }
        }
        lastAccessPoint.remove(player);
        return false;
    }

    private static boolean reaches(IWirelessAccessPoint accessPoint, EntityPlayer player) {
        if (!accessPoint.isActive()) return false;

        DimensionalCoord location = accessPoint.getLocation();
        if (location.getWorld() != player.worldObj) return false;

        double dx = location.x - player.posX;
        double dy = location.y - player.posY;
        double dz = location.z - player.posZ;
        double range = accessPoint.getRange();
        return dx * dx + dy * dy + dz * dz < range * range;
    }

    @Override
    public boolean canHandle(ItemStack is) {
        if (is == null || !(is.getItem() instanceof MechArmorBase) || is.getTagCompound() == null) return false;

        NBTTagCompound utility = is.getTagCompound()
            .getCompoundTag("augments")
            .getCompoundTag(Integer.toString(AugmentCategory.Utility.ordinal()));
        String autoFeeder = Augments.AutoFeeder.getId();
        for (Object slot : utility.func_150296_c()) {
            if (autoFeeder.equals(utility.getString((String) slot))) return true;
        }
        return false;
    }

    @Override
    public String getEncryptionKey(ItemStack item) {
        NBTTagCompound tag = item.getTagCompound();
        return tag == null ? "" : tag.getString(LINK_KEY);
    }

    @Override
    public void setEncryptionKey(ItemStack item, String encKey, String name) {
        if (item.getTagCompound() == null) item.setTagCompound(new NBTTagCompound());
        item.getTagCompound()
            .setString(LINK_KEY, encKey);
    }

    @Override
    public boolean usePower(EntityPlayer player, double amount, ItemStack is) {
        ArmorContext context = MechArmorBase.load(player.worldObj, player, is);
        if (context.getArmorState() == null || !context.drainEnergy(amount * EU_PER_AE)) return false;
        context.save();
        return true;
    }

    @Override
    public boolean hasPower(EntityPlayer player, double amount, ItemStack is) {
        return is.getTagCompound() != null && ArmorState.load(is)
            .canDrainEnergy(amount * EU_PER_AE);
    }

    @Override
    public IConfigManager getConfigManager(ItemStack is) {
        ConfigManager manager = new ConfigManager((configManager, setting, value) -> {});
        manager.registerSetting(Settings.SORT_BY, SortOrder.NAME);
        manager.registerSetting(Settings.VIEW_MODE, ViewItems.ALL);
        manager.registerSetting(Settings.SORT_DIRECTION, SortDir.ASCENDING);
        return manager;
    }
}

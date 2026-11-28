package gregtech.api.items.armor.behaviors;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;

import appeng.api.config.Actionable;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.networking.security.PlayerSource;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.data.IAEItemStack;
import appeng.util.item.AEItemStack;
import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.IFoodStat;
import gregtech.api.items.MetaGeneratedItem;
import gregtech.api.items.armor.ArmorContext;
import gregtech.api.items.armor.ArmorHelper;
import gregtech.api.items.armor.ArmorMELink;
import gregtech.api.util.GTFoodStat;
import gregtech.api.util.GTUtility;
import gregtech.common.items.behaviors.BehaviourTinCan;

public class AutoFeederBehavior implements IArmorBehavior {

    public static final AutoFeederBehavior INSTANCE = new AutoFeederBehavior();

    private static final int HUNGER_THRESHOLD = 14;
    private static final int CHECK_INTERVAL = 20;
    private static final double EU_PER_CAN = 100;
    private static final double EU_PER_NETWORK_CAN = 200;
    private static final double EU_PER_MEAL = 1000;
    private static final double EU_PER_NETWORK_MEAL = 2000;
    private static final int NETWORK_RETRY_DELAY = 200;

    private static final Map<EntityPlayer, ItemStack> lastNetworkFood = new WeakHashMap<>();
    private static final Map<EntityPlayer, Long> nextNetworkScan = new WeakHashMap<>();
    private static final Map<EntityPlayer, Long> nextNetworkCanTry = new WeakHashMap<>();
    private static ItemStack tinCan;

    protected AutoFeederBehavior() {}

    @Override
    public BehaviorName getName() {
        return BehaviorName.AutoFeeder;
    }

    @Override
    public void addArmorInformation(@NotNull ArmorContext context, @NotNull List<String> tooltip) {
        if (!ArmorHelper.isShiftPressed()) return;

        boolean linked = ArmorMELink.isLinked(context.getArmorStack());
        tooltip.add(
            linked ? EnumChatFormatting.GREEN + StatCollector.translateToLocal("GT5U.armor.tooltip.me_linked")
                : EnumChatFormatting.GRAY + StatCollector.translateToLocal("GT5U.armor.tooltip.me_not_linked"));
    }

    @Override
    public void onArmorTick(@NotNull ArmorContext context) {
        if (context.isRemote()) return;

        World world = context.getWorld();
        if (world.getTotalWorldTime() % CHECK_INTERVAL != 0) return;

        EntityPlayer player = context.getPlayer();
        if (BehaviourTinCan.missingFood(player) <= 0 || !canAfford(context, EU_PER_CAN)) return;

        IMEMonitor<IAEItemStack> network = canAfford(context, EU_PER_NETWORK_CAN)
            ? ArmorMELink.getNetworkInventory(player, context.getArmorStack())
            : null;
        BaseActionSource source = network == null ? null : new PlayerSource(player, null);
        if (network != null && world.getTotalWorldTime() >= nextNetworkCanTry.getOrDefault(player, 0L)) {
            if (eatCansFromNetwork(context, player, network, source)) return;
            nextNetworkCanTry.put(player, world.getTotalWorldTime() + NETWORK_RETRY_DELAY);
        }

        ItemStack[] inventory = player.inventory.mainInventory;
        int canSlot = -1;
        int foodSlot = -1;
        for (int n = 0; n < 36 && canSlot == -1; n++) {
            int slot = (n + 9) % 36;
            ItemStack stack = inventory[slot];
            if (stack == null) continue;
            if (isTinCan(stack)) canSlot = slot;
            else if (foodSlot == -1 && isSafeFood(stack)) foodSlot = slot;
        }

        if (canSlot != -1) {
            eatCansFromInventory(context, player, canSlot, network, source);
            return;
        }

        if (player.getFoodStats()
            .getFoodLevel() >= HUNGER_THRESHOLD || !canAfford(context, EU_PER_MEAL)) return;
        if (network != null && !canAfford(context, EU_PER_NETWORK_MEAL)) network = null;

        if (network != null && eatFoodFromNetwork(context, player, world, network, source)) return;

        if (foodSlot != -1) eatFoodFromInventory(context, player, world, foodSlot);
    }

    private static boolean canAfford(ArmorContext context, double eu) {
        return context.getArmorState()
            .canDrainEnergy(eu);
    }

    private static int affordableCans(ArmorContext context, int wanted, double euPerCan) {
        int cans = wanted;
        while (cans > 0 && !canAfford(context, cans * euPerCan)) cans--;
        return cans;
    }

    private static void eatCansFromInventory(ArmorContext context, EntityPlayer player, int slot,
        IMEMonitor<IAEItemStack> network, BaseActionSource source) {
        ItemStack[] inventory = player.inventory.mainInventory;
        ItemStack cans = inventory[slot];
        int affordable = affordableCans(
            context,
            Math.min(cans.stackSize, BehaviourTinCan.missingFood(player)),
            EU_PER_CAN);
        int eaten = BehaviourTinCan.feed(player, affordable, false);
        context.drainEnergy(eaten * EU_PER_CAN);
        cans.stackSize -= eaten;
        if (cans.stackSize <= 0) inventory[slot] = null;
        if (eaten <= 0) return;

        ItemStack empties = ItemList.TinCan_Empty.get(eaten);
        if (network != null) returnToNetwork(player, network, source, empties);
        else GTUtility.addItemToPlayerInventory(player, empties);
    }

    private static boolean eatCansFromNetwork(ArmorContext context, EntityPlayer player,
        IMEMonitor<IAEItemStack> network, BaseActionSource source) {
        int wanted = affordableCans(context, BehaviourTinCan.missingFood(player), EU_PER_NETWORK_CAN);
        if (wanted <= 0) return false;

        IAEItemStack request = AEItemStack.create(ItemList.TinCan_Filled.get(wanted));
        IAEItemStack extracted = network.extractItems(request, Actionable.MODULATE, source);
        if (extracted == null || extracted.getStackSize() <= 0) return false;

        int eaten = BehaviourTinCan.feed(player, (int) extracted.getStackSize(), false);
        context.drainEnergy(eaten * EU_PER_NETWORK_CAN);
        returnToNetwork(player, network, source, ItemList.TinCan_Empty.get(eaten));
        return true;
    }

    private static void eatFoodFromInventory(ArmorContext context, EntityPlayer player, World world, int slot) {
        context.drainEnergy(EU_PER_MEAL);

        ItemStack[] inventory = player.inventory.mainInventory;
        ItemStack food = inventory[slot];
        ItemStack result = food.getItem()
            .onEaten(food, world, player);
        inventory[slot] = (result == null || result.stackSize <= 0) ? null : result;
    }

    private static boolean eatFoodFromNetwork(ArmorContext context, EntityPlayer player, World world,
        IMEMonitor<IAEItemStack> network, BaseActionSource source) {
        ItemStack remembered = lastNetworkFood.get(player);
        if (remembered != null && tryEatFromNetwork(context, player, world, network, source, remembered)) return true;

        long now = world.getTotalWorldTime();
        if (now < nextNetworkScan.getOrDefault(player, 0L)) return false;

        ItemStack found = findFoodInNetwork(network);
        if (found == null) {
            lastNetworkFood.remove(player);
            nextNetworkScan.put(player, now + NETWORK_RETRY_DELAY);
            return false;
        }

        lastNetworkFood.put(player, found);
        return tryEatFromNetwork(context, player, world, network, source, found);
    }

    private static ItemStack findFoodInNetwork(IMEMonitor<IAEItemStack> network) {
        for (IAEItemStack stored : network.getStorageList()) {
            Item item = stored.getItem();
            if (item instanceof MetaGeneratedItem metaItem) {
                if (!metaItem.mFoodStats.containsKey((short) stored.getItemDamage())) continue;
            } else if (!(item instanceof ItemFood)) continue;

            ItemStack food = stored.getItemStack();
            food.stackSize = 1;
            if (isSafeFood(food)) return food;
        }
        return null;
    }

    private static boolean tryEatFromNetwork(ArmorContext context, EntityPlayer player, World world,
        IMEMonitor<IAEItemStack> network, BaseActionSource source, ItemStack food) {
        IAEItemStack extracted = network.extractItems(AEItemStack.create(food), Actionable.MODULATE, source);
        if (extracted == null) return false;

        context.drainEnergy(EU_PER_NETWORK_MEAL);

        ItemStack meal = extracted.getItemStack();
        ItemStack leftover = meal.getItem()
            .onEaten(meal, world, player);
        returnToNetwork(player, network, source, leftover);
        return true;
    }

    private static void returnToNetwork(EntityPlayer player, IMEMonitor<IAEItemStack> network, BaseActionSource source,
        ItemStack stack) {
        if (stack == null || stack.stackSize <= 0) return;

        IAEItemStack rejected = network.injectItems(AEItemStack.create(stack), Actionable.MODULATE, source);
        if (rejected != null && rejected.getStackSize() > 0) {
            GTUtility.addItemToPlayerInventory(player, rejected.getItemStack());
        }
    }

    private static boolean isTinCan(ItemStack stack) {
        if (tinCan == null) tinCan = ItemList.TinCan_Filled.get(1);
        return stack.getItem() == tinCan.getItem() && stack.getItemDamage() == tinCan.getItemDamage();
    }

    private static boolean isSafeFood(ItemStack stack) {
        if (stack == null) return false;
        Item item = stack.getItem();
        if (item.getItemUseAction(stack) != EnumAction.eat) return false;
        if (item == Items.rotten_flesh || item == Items.spider_eye || item == Items.poisonous_potato) return false;
        if (item == Items.golden_apple) return false;
        if (item == Items.fish && stack.getItemDamage() == 3) return false;
        if (item instanceof MetaGeneratedItem metaItem) return isSafeMetaFood(metaItem, stack);
        return true;
    }

    private static boolean isSafeMetaFood(MetaGeneratedItem item, ItemStack stack) {
        IFoodStat stat = item.mFoodStats.get((short) stack.getItemDamage());
        if (stat == null || stat.isRotten(item, stack, null)) return false;
        return !(stat instanceof GTFoodStat gtStat && gtStat.isExplosive());
    }
}

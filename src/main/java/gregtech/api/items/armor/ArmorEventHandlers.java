package gregtech.api.items.armor;

import static gregtech.api.items.armor.ArmorHelper.SLOT_BOOTS;
import static gregtech.api.items.armor.ArmorHelper.SLOT_CHEST;

import java.util.ListIterator;
import java.util.WeakHashMap;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

import com.gtnewhorizon.gtnhlib.client.event.LivingEquipmentChangeEvent;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.items.armor.behaviors.BehaviorName;
import gregtech.api.util.GTUtility;
import gregtech.common.items.armor.MechArmorBase;

public class ArmorEventHandlers {

    @SubscribeEvent
    public void onLivingEquipmentChange(LivingEquipmentChangeEvent event) {
        int slot = event.getSlot();
        if (slot == 0) {
            return;
        }
        if (!(event.entity instanceof EntityPlayer player)) {
            return;
        }

        ItemStack from = event.getFrom();
        ItemStack to = event.getTo();

        if (GTUtility.areStacksEqual(from, to, true) && from.getItem() instanceof MechArmorBase) {
            ItemStack fromCopy = from.copy();
            ItemStack toCopy = to.copy();

            NBTTagCompound fromTag = fromCopy.getTagCompound();
            NBTTagCompound toTag = toCopy.getTagCompound();

            // If only the charge changes, don't treat this change as an 'equip'
            if (fromTag != null && toTag != null) {
                toTag.removeTag("charge");
                fromTag.removeTag("charge");

                if (GTUtility.areStacksEqual(fromCopy, toCopy)) return;
            }
        }

        if (from != null) {
            // maybe unnecessary sanity check to make sure this same item wasn't immediately re-equipped
            if (to != null && from.isItemEqual(to)) {
                return;
            }

            if (from.getItem() instanceof MechArmorBase armor) {
                armor.onArmorUnequip(player.getEntityWorld(), player, from);
            }
        }

        if (to != null) {
            if (to.getItem() instanceof MechArmorBase armor) {
                armor.onArmorEquip(player.getEntityWorld(), player, to);
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onClientPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START && !(event.player instanceof EntityOtherPlayerMP)
            && !(event.player instanceof FakePlayer)) {
            onPlayerTick(event);
        }
    }

    @SubscribeEvent
    @SideOnly(Side.SERVER)
    public void onServerPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START && !(event.player instanceof FakePlayer)) {
            onPlayerTick(event);
        }
    }

    private static final float MAGIC_STEP_HEIGHT = 1.0023f;
    private final WeakHashMap<EntityPlayer, Float> savedStepHeight = new WeakHashMap<>();

    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;

        if (event.phase != TickEvent.Phase.START || player == null || player.isDead) {
            return;
        }

        // Step Assist
        // for some reason, doing this in ticking and on unequip is not sufficient, this value is somewhat sticky
        if (!player.isSneaking()) {
            for (int i = 0; i < 4; i++) {
                ItemStack armorStack = player.getCurrentArmor(i);
                if (armorStack == null) continue;
                if (armorStack.getItem() instanceof MechArmorBase) {
                    ArmorContext context = MechArmorBase.load(player, armorStack);

                    if (context.hasBehavior(BehaviorName.StepAssist)) {
                        if (player.stepHeight < MAGIC_STEP_HEIGHT) {
                            savedStepHeight.putIfAbsent(player, player.stepHeight);
                            player.stepHeight = MAGIC_STEP_HEIGHT;
                            return;
                        }
                    }
                }
            }
        }
        if (savedStepHeight.containsKey(player)) {
            player.stepHeight = savedStepHeight.get(player);
            savedStepHeight.remove(player);
        }
    }

    @SubscribeEvent
    public void onPlayerJump(LivingEvent.LivingJumpEvent event) {
        if (event.entityLiving instanceof EntityPlayer player) {
            ItemStack boots = player.getCurrentArmor(SLOT_BOOTS);

            if (boots == null) return;

            if (!(boots.getItem() instanceof MechArmorBase)) return;

            ArmorContext context = MechArmorBase.load(player, boots);

            float jumpBoost = context.getArmorState().jumpBoost;
            if (jumpBoost > 0 && context.drainEnergy(50)) {
                player.motionY += jumpBoost;
                player.fallDistance = player.fallDistance - (jumpBoost * 10);
                context.save();
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onLivingFall(LivingFallEvent event) {
        if (event.entityLiving instanceof EntityPlayerMP player) {

            ItemStack chest = player.getCurrentArmor(SLOT_CHEST);
            if (chest != null && chest.getItem() instanceof MechArmorBase) {
                ArmorContext chestContext = MechArmorBase.load(player, chest);
                if (chestContext.hasBehavior(BehaviorName.CreativeFlight) && chestContext.getArmorState()
                    .canDrainEnergy(1)) {
                    event.setCanceled(true);
                    return;
                }
            }

            if (player.fallDistance < 3.2f) {
                return;
            }
            ItemStack boots = player.getCurrentArmor(SLOT_BOOTS);
            if (boots == null) return;

            if (!(boots.getItem() instanceof MechArmorBase)) return;

            ArmorContext context = MechArmorBase.load(player, boots);

            if (context.hasBehavior(BehaviorName.FallProtection)
                && context.drainEnergy(500 * (player.fallDistance - 1.2f))) {
                player.fallDistance = 0;
                event.setCanceled(true);
                context.save();
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDropsEvent event) {
        if (event.entityPlayer == null || event.isCanceled()) {
            return;
        }

        if (event.entityPlayer.worldObj.getGameRules()
            .getGameRuleBooleanValue("keepInventory")) {
            return;
        }

        EntityPlayer player = event.entityPlayer;
        ListIterator<EntityItem> iter = event.drops.listIterator();

        while (iter.hasNext()) {
            EntityItem drop = iter.next();
            ItemStack armorStack = drop.getEntityItem();

            if (armorStack != null && armorStack.getItem() instanceof MechArmorBase) {
                ArmorContext context = MechArmorBase.load(player, armorStack);

                if (context.hasBehavior(BehaviorName.Soulbound)) {
                    if (addToPlayerInventory(player, armorStack)) {
                        iter.remove();
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {
        if (!event.wasDeath || event.isCanceled()) {
            return;
        }
        if (event.original == null || event.entityPlayer == null) {
            return;
        }
        if (event.entityPlayer.worldObj.getGameRules()
            .getGameRuleBooleanValue("keepInventory")) {
            return;
        }

        EntityPlayer originalPlayer = event.original;
        EntityPlayer newPlayer = event.entityPlayer;

        for (int i = 0; i < originalPlayer.inventory.armorInventory.length; i++) {
            ItemStack armorStack = originalPlayer.inventory.armorInventory[i];
            if (armorStack != null && armorStack.getItem() instanceof MechArmorBase) {
                ArmorContext context = MechArmorBase.load(originalPlayer, armorStack);
                if (context.hasBehavior(BehaviorName.Soulbound)) {
                    if (addToPlayerInventory(newPlayer, armorStack)) {
                        originalPlayer.inventory.armorInventory[i] = null;
                    }
                }
            }
        }

        for (int i = 0; i < originalPlayer.inventory.mainInventory.length; i++) {
            ItemStack armorStack = originalPlayer.inventory.mainInventory[i];
            if (armorStack != null && armorStack.getItem() instanceof MechArmorBase) {
                ArmorContext context = MechArmorBase.load(originalPlayer, armorStack);
                if (context.hasBehavior(BehaviorName.Soulbound)) {
                    if (addToPlayerInventory(newPlayer, armorStack)) {
                        originalPlayer.inventory.mainInventory[i] = null;
                    }
                }
            }
        }
    }

    private boolean addToPlayerInventory(EntityPlayer player, ItemStack armorStack) {
        if (armorStack == null || player == null) {
            return false;
        }

        if (armorStack.getItem() instanceof MechArmorBase armorItem) {
            int armorSlot = 3 - armorItem.getArmorType()
                .ordinal();

            if (player.inventory.armorInventory[armorSlot] == null) {
                player.inventory.armorInventory[armorSlot] = armorStack;
                return true;
            }
        }

        for (int i = 0; i < player.inventory.mainInventory.length; i++) {
            if (player.inventory.mainInventory[i] == null) {
                player.inventory.mainInventory[i] = armorStack.copy();
                return true;
            }
        }

        return false;
    }
}

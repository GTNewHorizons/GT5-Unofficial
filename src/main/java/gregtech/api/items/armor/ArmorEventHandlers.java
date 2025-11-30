package gregtech.api.items.armor;

import static gregtech.api.items.armor.ArmorHelper.SLOT_BOOTS;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;

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

                if (GTUtility.areStacksEqual(from, toCopy)) return;
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

    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;

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
                            player.stepHeight = MAGIC_STEP_HEIGHT;
                            return;
                        }
                    }
                }
            }
        }
        if (player.stepHeight == MAGIC_STEP_HEIGHT) {
            player.stepHeight = 0.6f;
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
            if (jumpBoost > 0) {
                player.motionY += jumpBoost;
                player.fallDistance = player.fallDistance - (jumpBoost * 10);
                context.drainEnergy(50);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onLivingFall(LivingFallEvent event) {
        if (event.entityLiving instanceof EntityPlayerMP player) {

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
            }
        }
    }
}

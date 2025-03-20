package gregtech.api.items.armor;

import static gregtech.api.items.armor.ArmorHelper.JUMP_BOOST_KEY;
import static gregtech.api.items.armor.ArmorHelper.drainArmor;
import static gregtech.api.util.GTUtility.getOrCreateNbtCompound;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingEvent;

import com.gtnewhorizon.gtnhlib.client.event.LivingEquipmentChangeEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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

        if (from != null) {
            // maybe unnecessary sanity check to make sure this same item wasn't immediately re-equipped
            if (to != null && event.getFrom()
                .isItemEqual(event.getTo())) {
                return;
            }

            if (event.getFrom()
                .getItem() instanceof MechArmorBase armor) {
                armor.onArmorUnequip(player.getEntityWorld(), player, event.getFrom());
            }
        }
        if (to != null) {
            if (event.getTo()
                .getItem() instanceof MechArmorBase armor) {
                armor.onArmorEquip(player.getEntityWorld(), player, event.getTo());
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
                if (armorStack.getItem() instanceof MechArmorBase gtArmor) {
                    NBTTagCompound behaviorTag = getOrCreateNbtCompound(armorStack);

                    if (behaviorTag.getBoolean(ArmorHelper.STEP_ASSIST_KEY)) {
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
            ItemStack boots = player.getCurrentArmor(0);

            if (boots == null) return;
            NBTTagCompound tag = boots.getTagCompound();

            if (tag == null) return;

            float jumpboost = tag.getFloat(JUMP_BOOST_KEY);
            if (jumpboost != 0) {
                player.motionY += jumpboost;
                player.fallDistance = player.fallDistance - (jumpboost * 10);
                drainArmor(boots, 50);
            }
        }
    }
}

package gregtech.api.items.armor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import com.gtnewhorizon.gtnhlib.client.event.LivingEquipmentChangeEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
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

    /*
     * @SubscribeEvent(priority = EventPriority.LOW)
     * public static void onLivingFall(LivingFallEvent event) {
     * if (event.getEntity() instanceof EntityPlayerMP player) {
     * if (player.fallDistance < 3.2f) {
     * return;
     * }
     * for (EntityEquipmentSlot slot : ArmorHelper.getArmorSlots()) {
     * ItemStack armorStack = player.getItemStackFromSlot(slot);
     * if (armorStack.isEmpty() || !(armorStack.getItem() instanceof IGTArmor gtArmor)) {
     * continue;
     * }
     * // todo make sure jetpack sets this fall damage key when needed ("flyMode" nbt key previously?)
     * NBTTagCompound behaviorTag = ArmorHelper.getBehaviorsTag(armorStack);
     * if (behaviorTag.getBoolean(ArmorHelper.FALL_DAMAGE_KEY)) {
     * if (gtArmor.areBehaviorsActive(armorStack)) {
     * gtArmor.damageArmor(player, armorStack, DamageSource.FALL, (int) (player.fallDistance - 1.2f),
     * slot.getIndex());
     * player.fallDistance = 0;
     * event.setCanceled(true);
     * return;
     * }
     * }
     * }
     * }
     * }
     * private static final float MAGIC_STEP_HEIGHT = 1.0023f;
     * private static void onPlayerTick(TickEvent.PlayerTickEvent event) {
     * EntityPlayer player = event.player;
     * // Step Assist
     * // for some reason, doing this in ticking and on unequip is not sufficient, this value is somewhat sticky
     * if (!player.isSneaking()) {
     * for (EntityEquipmentSlot slot : ArmorHelper.getArmorSlots()) {
     * ItemStack armorStack = player.getItemStackFromSlot(slot);
     * if (armorStack.getItem() instanceof IGTArmor gtArmor) {
     * NBTTagCompound behaviorTag = ArmorHelper.getBehaviorsTag(armorStack);
     * if (behaviorTag.getBoolean(ArmorHelper.STEP_ASSIST_KEY) && gtArmor.areBehaviorsActive(armorStack)) {
     * if (player.stepHeight < MAGIC_STEP_HEIGHT) {
     * player.stepHeight = MAGIC_STEP_HEIGHT;
     * return;
     * }
     * }
     * }
     * }
     * }
     * if (player.stepHeight == MAGIC_STEP_HEIGHT) {
     * player.stepHeight = 0.6f;
     * }
     * }
     */
}

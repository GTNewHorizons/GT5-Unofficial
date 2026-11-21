package gregtech.api.items.armor;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import crazypants.enderio.api.teleport.TravelSource;
import crazypants.enderio.teleport.TravelController;
import gregtech.api.items.armor.behaviors.BehaviorName;
import gregtech.common.items.armor.MechArmorBase;

@SideOnly(Side.CLIENT)
public class MovementStaffClientState {

    public static boolean isPreviewActive = false;
    private static int keyHeldTicks = 0;
    private static boolean wasKeyDown = false;
    private static int cooldownTimer = 0;
    private final static int HOLD_TIME = 5;

    public static void updateClientTick(EntityPlayer player, ItemStack armorStack) {
        Minecraft mc = Minecraft.getMinecraft();
        if (player != mc.thePlayer) return;

        if (mc.currentScreen != null) {
            isPreviewActive = false;
            keyHeldTicks = 0;
            wasKeyDown = false;
            return;
        }

        if (cooldownTimer > 0) cooldownTimer--;

        boolean isKeyDown = ArmorActionManager.getKeybind("activate_movement_staff")
            .isKeyDown(player);

        if (isKeyDown) {
            keyHeldTicks++;
            if (keyHeldTicks >= HOLD_TIME) {
                isPreviewActive = true;
            }
        } else if (wasKeyDown) {

            if (cooldownTimer == 0) {
                if (keyHeldTicks < HOLD_TIME) {
                    isPreviewActive = true;
                    TravelController.instance.forceUpdateTarget(player);
                }

                ArmorContext context = MechArmorBase.load(player.worldObj, player, armorStack);
                boolean isAdvanced = context.hasBehavior(BehaviorName.TeleportationStaff);

                TravelSource source = isAdvanced ? TravelSource.TELEPORT_STAFF : TravelSource.STAFF;
                boolean success = false;

                if (TravelController.instance.hasTarget()) {
                    success = TravelController.instance
                        .activateTravelAccessable(armorStack, player.worldObj, player, source);
                } else {
                    if (isAdvanced) {
                        success = TravelController.instance.doTeleport(player);
                    } else {
                        success = TravelController.instance.doBlink(armorStack, player);
                    }
                }

                if (success) {
                    cooldownTimer = isAdvanced ? 0 : 10;
                }
            }

            isPreviewActive = false;
            keyHeldTicks = 0;
        } else {
            isPreviewActive = false;
            keyHeldTicks = 0;
        }
        wasKeyDown = isKeyDown;
    }
}

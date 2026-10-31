package gregtech.api.items.armor;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.FMLCommonHandler;

public class ArmorHelper {

    // This is a magic key synchronized with an identical one in GalaxySpace. When GS runs checks for spacesuits, it
    // will also check against this key.

    // TODO: If the hazmat api is finished, remove this hack and use the hazmat system!
    public static final String FORCE_SPACE_SUIT_NBT_KEY = "ForceValidSpaceSuit";

    public static final int REGISTER_HELMET = 0;
    public static final int REGISTER_CHEST = 1;
    public static final int REGISTER_LEGS = 2;
    public static final int REGISTER_BOOTS = 3;

    public static final int SLOT_HELMET = 3;
    public static final int SLOT_CHEST = 2;
    public static final int SLOT_LEGS = 1;
    public static final int SLOT_BOOTS = 0;

    public static boolean isShiftPressed() {
        if (FMLCommonHandler.instance()
            .getSide()
            .isClient()) {
            return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
        } else {
            return true;
        }
    }

    public static boolean isJumpInput(EntityPlayer player) {
        if (player.worldObj.isRemote) return player instanceof EntityPlayerSP sp && sp.movementInput.jump;
        return isSyncedKeyDown(player, "VANILLA_JUMP");
    }

    public static boolean isSneakInput(EntityPlayer player) {
        if (player.worldObj.isRemote) return player instanceof EntityPlayerSP sp && sp.movementInput.sneak;
        return isSyncedKeyDown(player, "VANILLA_SNEAK");
    }

    public static float getForwardInput(EntityPlayer player) {
        if (player.worldObj.isRemote) return player instanceof EntityPlayerSP sp ? sp.movementInput.moveForward : 0;
        return (isSyncedKeyDown(player, "VANILLA_FORWARD") ? 1 : 0)
            - (isSyncedKeyDown(player, "VANILLA_BACKWARD") ? 1 : 0);
    }

    public static float getStrafeInput(EntityPlayer player) {
        if (player.worldObj.isRemote) return player instanceof EntityPlayerSP sp ? sp.movementInput.moveStrafe : 0;
        return (isSyncedKeyDown(player, "VANILLA_LEFT") ? 1 : 0) - (isSyncedKeyDown(player, "VANILLA_RIGHT") ? 1 : 0);
    }

    private static boolean isSyncedKeyDown(EntityPlayer player, String id) {
        return ArmorActionManager.getKeybind(id)
            .isKeyDown(player);
    }
}

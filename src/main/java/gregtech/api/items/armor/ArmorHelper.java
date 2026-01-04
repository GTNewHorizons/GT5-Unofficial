package gregtech.api.items.armor;

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
}

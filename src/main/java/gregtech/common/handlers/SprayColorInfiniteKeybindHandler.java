package gregtech.common.handlers;

import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;

public final class SprayColorInfiniteKeybindHandler {

    public static final KeyBinding shakeLockKey = new KeyBinding(
        "GTPacketInfiniteSpraycan.Action.TOGGLE_SHAKE_LOCK",
        Keyboard.CHAR_NONE,
        "Gregtech");

    private SprayColorInfiniteKeybindHandler() {}

    public static void init() {
        ClientRegistry.registerKeyBinding(shakeLockKey);
    }
}

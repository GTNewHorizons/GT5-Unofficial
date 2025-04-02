package gregtech.common.handlers;

import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class PowerGogglesKeybindHandler {

    public static final KeyBinding openConfigGui;

    static {
        openConfigGui = new KeyBinding("key.openConfigGui", Keyboard.KEY_NUMPAD7, "gregtech");
    }

    private PowerGogglesKeybindHandler() {}

    public static void init() {
        ClientRegistry.registerKeyBinding(openConfigGui);
    }
}

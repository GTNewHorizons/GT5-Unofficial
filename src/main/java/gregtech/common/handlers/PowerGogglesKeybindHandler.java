package gregtech.common.handlers;

import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class PowerGogglesKeybindHandler {

    public static final KeyBinding openConfigGui;
    public static final KeyBinding toggleChart;

    static {
        openConfigGui = new KeyBinding("GT5U.power_goggles.open_config_gui", Keyboard.KEY_NUMPAD7, "Gregtech");
        toggleChart = new KeyBinding("GT5U.power_goggles.toggle_power_chart", Keyboard.KEY_NUMPAD8, "Gregtech");
    }

    private PowerGogglesKeybindHandler() {}

    public static void init() {
        ClientRegistry.registerKeyBinding(openConfigGui);
        ClientRegistry.registerKeyBinding(toggleChart);
    }
}

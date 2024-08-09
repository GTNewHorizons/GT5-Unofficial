package gregtech.client;

import static gregtech.api.enums.Mods.GregTech;

import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;

public class GT_KeyBindings {

    public static KeyBinding openTechTree;

    public static void registerBindings() {
        openTechTree = new KeyBinding("Open Tech Tree", Keyboard.KEY_GRAVE, GregTech.name());
        ClientRegistry.registerKeyBinding(openTechTree);
    }
}

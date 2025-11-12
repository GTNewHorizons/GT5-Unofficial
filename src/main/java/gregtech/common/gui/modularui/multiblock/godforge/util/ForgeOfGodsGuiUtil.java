package gregtech.common.gui.modularui.multiblock.godforge.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;

import com.cleanroommc.modularui.widgets.ButtonWidget;

import gregtech.api.enums.Mods;

public class ForgeOfGodsGuiUtil {

    private static final ResourceLocation PRESS_SOUND = new ResourceLocation(Mods.TecTech.ID, "fx_click");

    public static Runnable getButtonSound() {
        return () -> Minecraft.getMinecraft()
            .getSoundHandler()
            .playSound(PositionedSoundRecord.func_147673_a(PRESS_SOUND));
    }

    public static ButtonWidget<?> panelCloseButton() {
        return ButtonWidget.panelCloseButton()
            .clickSound(getButtonSound());
    }
}

package gregtech.common.gui.modularui.multiblock.godforge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.widgets.ButtonWidget;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Mods;
import gregtech.api.modularui2.GTGuiTextures;

public class ForgeOfGodsGuiUtil {

    private static final ResourceLocation PRESS_SOUND = new ResourceLocation(Mods.TecTech.ID, "fx_click");

    public static Runnable getButtonSound() {
        return ForgeOfGodsGuiUtil::playButtonSound;
    }

    private static void playButtonSound() {
        playButtonSoundClient();
    }

    @SideOnly(Side.CLIENT)
    private static void playButtonSoundClient() {
        Minecraft.getMinecraft()
            .getSoundHandler()
            .playSound(PositionedSoundRecord.func_147673_a(PRESS_SOUND));
    }

    public static ButtonWidget<?> panelCloseButton() {
        return ButtonWidget.panelCloseButton()
            .background(GTGuiTextures.CLOSE_BUTTON_HOLLOW)
            .overlay(IDrawable.EMPTY)
            .disableHoverBackground()
            .disableHoverOverlay()
            .clickSound(getButtonSound());
    }

    public static ButtonWidget<?> panelCloseButtonStandard() {
        return ButtonWidget.panelCloseButton()
            .clickSound(getButtonSound());
    }
}

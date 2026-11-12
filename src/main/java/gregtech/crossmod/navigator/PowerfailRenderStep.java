package gregtech.crossmod.navigator;

import java.text.DateFormat;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;

import com.gtnewhorizons.navigator.api.NavigatorApi;
import com.gtnewhorizons.navigator.api.model.steps.UniversalInteractableStep;
import com.gtnewhorizons.navigator.api.util.DrawUtils;

import gregtech.GTMod;
import gregtech.api.enums.GTValues;
import gregtech.api.net.GTPacketClearPowerfail;
import gregtech.api.util.GTDataUtils;
import gregtech.api.util.GTTextBuilder;
import gregtech.common.data.GTPowerfailTracker;

public class PowerfailRenderStep extends UniversalInteractableStep<PowerfailLocationWrapper> {

    private static final ResourceLocation CLICK_SOUND = new ResourceLocation("gui.button.press");
    private static final int FONT_HEIGHT = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;

    public PowerfailRenderStep(PowerfailLocationWrapper location) {
        super(location);
        setFontScale(1.2f);
        setMinScale(1);
    }

    @Override
    public void preRender(double topX, double topY, float drawScale, double zoom) {
        double iconSize = isXaero ? 10 * zoom : 32 * drawScale * getZoomScale(1, 2, 3, 5);

        double blockWidth = isXaero ? zoom : Math.pow(2, zoom);
        double padding = -(iconSize - blockWidth) / 2;

        setSize(iconSize);
        setOffset(padding);
    }

    @Override
    public void draw(double topX, double topY, float drawScale, double zoom) {
        if (!location.highlighted) return;

        double labelScale = isXaero ? getFontScale() : getFontScale() * getZoomScale(1, 3, 3, 5);
        if (isXaero || getZoomStep() >= 2) {
            DrawUtils.drawLabel(
                location.powerfail.toSummary()
                    .toString(),
                topX + width / 2,
                isXaero ? (topY + height + (2 * getScaling(zoom))) : ((topY - FONT_HEIGHT - 5) + (height * 1.5)),
                0xFFFFFF,
                0,
                true,
                labelScale);
        }

        DrawUtils
            .drawQuad(GTMod.clientProxy().powerfailRenderer.powerfailIcon, topX, topY, width, height, 0xFFFFFF, 255);
    }

    @Override
    public void getTooltip(List<String> list) {
        GTPowerfailTracker.Powerfail p = location.powerfail;

        String[] lines = new GTTextBuilder("GT5U.gui.chat.powerfail.waypoint.tooltip").setBase(EnumChatFormatting.GRAY)
            .addName(p.getMTEName())
            .addCoord(p.x, p.y, p.z)
            .addNumber(p.count)
            .addValue(
                DateFormat.getDateTimeInstance()
                    .format(p.lastOccurrence))
            .addName(Keyboard.getKeyName(NavigatorApi.ACTION_KEY.getKeyCode()))
            .toString()
            .split("\\\\n");

        list.addAll(GTDataUtils.mapToList(lines, s -> EnumChatFormatting.GRAY + s));
    }

    @Override
    public boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX >= getX() && mouseX <= getX() + width && mouseY >= getY() && mouseY <= getY() + height;
    }

    @Override
    public void onActionKeyPressed() {
        super.onActionKeyPressed();

        playClickSound();

        GTValues.NW.sendToServer(new GTPacketClearPowerfail(location.powerfail));
    }

    private static void playClickSound() {
        Minecraft.getMinecraft()
            .getSoundHandler()
            .playSound(PositionedSoundRecord.func_147674_a(CLICK_SOUND, 1.0F));
    }
}

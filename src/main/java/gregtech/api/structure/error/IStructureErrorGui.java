package gregtech.api.structure.error;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.client.StructureErrorHighlightRenderer;

public interface IStructureErrorGui {

    default IWidget createHighlightButton(int errX, int errY, int errZ) {
        return new ButtonWidget<>().size(12, 12)
            .marginRight(4)
            .disableHoverBackground()
            .background(IDrawable.EMPTY)
            .overlay(GTGuiTextures.OVERLAY_BUTTON_HIGHLIGHT_BLOCK)
            .onMousePressed(d -> {
                highlightAndFaceBlock(errX, errY, errZ);
                return true;
            })
            .tooltipBuilder(t -> t.addLine(IKey.lang("GT5U.gui.button.highlight_block")))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    @SideOnly(Side.CLIENT)
    static void highlightAndFaceBlock(int errX, int errY, int errZ) {
        StructureErrorHighlightRenderer.highlight(errX, errY, errZ);
        net.minecraft.client.entity.EntityPlayerSP player = net.minecraft.client.Minecraft.getMinecraft().thePlayer;
        if (player != null) {
            player.closeScreen();
            net.minecraft.util.Vec3 eyePos = player.getPosition(1.0F);
            double dx = errX + 0.5 - eyePos.xCoord;
            double dy = errY + 0.5 - eyePos.yCoord;
            double dz = errZ + 0.5 - eyePos.zCoord;
            double distXZ = Math.sqrt(dx * dx + dz * dz);
            player.rotationYaw = (float) Math.toDegrees(Math.atan2(-dx, dz));
            player.rotationPitch = (float) Math.toDegrees(Math.atan2(-dy, distXZ));
        }
    }
}

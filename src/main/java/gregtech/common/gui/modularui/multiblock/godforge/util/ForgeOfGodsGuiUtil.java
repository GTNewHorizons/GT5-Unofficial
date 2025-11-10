package gregtech.common.gui.modularui.multiblock.godforge.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.widgets.ButtonWidget;

import gregtech.api.enums.Mods;
import gregtech.common.items.ItemFluidDisplay;

public class ForgeOfGodsGuiUtil {

    private static final ResourceLocation PRESS_SOUND = new ResourceLocation(Mods.TecTech.ID, "fx_click");

    public static void addFluidNameInfo(RichTooltip tooltip, @NotNull FluidStack fluid) {
        tooltip.addLine(EnumChatFormatting.WHITE + fluid.getLocalizedName());
        String formula = ItemFluidDisplay.getChemicalFormula(fluid);
        if (!formula.isEmpty()) {
            tooltip.addLine(EnumChatFormatting.YELLOW + formula);
        }
    }

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

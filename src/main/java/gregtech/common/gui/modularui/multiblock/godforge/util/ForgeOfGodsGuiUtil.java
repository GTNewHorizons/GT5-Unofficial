package gregtech.common.gui.modularui.multiblock.godforge.util;

import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.screen.RichTooltip;

import gregtech.common.items.ItemFluidDisplay;

public class ForgeOfGodsGuiUtil {

    public static void addFluidNameInfo(RichTooltip tooltip, @NotNull FluidStack fluid) {
        tooltip.addLine(EnumChatFormatting.WHITE + fluid.getLocalizedName());
        String formula = ItemFluidDisplay.getChemicalFormula(fluid);
        if (!formula.isEmpty()) {
            tooltip.addLine(EnumChatFormatting.YELLOW + formula);
        }
    }
}

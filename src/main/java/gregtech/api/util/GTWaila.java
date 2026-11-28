package gregtech.api.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;

import appeng.util.ReadableNumberConverter;
import gregtech.common.config.Client;
import mcp.mobius.waila.overlay.tooltiprenderers.TTRenderBar;

public abstract class GTWaila {

    public static List<String> getLockedRecipeOutputLines(NBTTagCompound tag) {
        List<String> lines = new ArrayList<>();
        NBTTagList fluids = tag.getTagList("lockedRecipeFluids", Constants.NBT.TAG_COMPOUND);
        if (fluids.tagCount() > 0) {
            lines.add(StatCollector.translateToLocal("GT5U.waila.multiblock.locked_recipe.fluid_outputs"));
            for (int i = 0; i < fluids.tagCount(); i++) {
                FluidStack fluid = FluidStack.loadFluidStackFromNBT(fluids.getCompoundTagAt(i));
                if (fluid != null) lines.add("- " + fluid.getLocalizedName());
            }
        }

        NBTTagList items = tag.getTagList("lockedRecipeItems", Constants.NBT.TAG_COMPOUND);
        if (items.tagCount() > 0) {
            lines.add(StatCollector.translateToLocal("GT5U.waila.multiblock.locked_recipe.item_outputs"));
            for (int i = 0; i < items.tagCount(); i++) {
                ItemStack stack = ItemStack.loadItemStackFromNBT(items.getCompoundTagAt(i));
                if (stack != null) lines.add("- " + stack.getDisplayName());
            }
        }
        return lines;
    }

    /**
     * One line for a stack listed in a WAILA tooltip, e.g. {@code Tin Ingot: §61.5k§r}. Tiles that list their stored or
     * cached contents all render through this, so their tooltips read the same.
     */
    public static String getStackListLine(String name, long amount) {
        return name + ": "
            + EnumChatFormatting.GOLD
            + ReadableNumberConverter.INSTANCE.toWideReadableForm(amount)
            + EnumChatFormatting.RESET;
    }

    public static String getMachineProgressString(int maxProgressTime, int progressTime) {
        return getMachineProgressString(true, true, maxProgressTime, progressTime);
    }

    public static String getMachineProgressString(boolean isActive, int maxProgressTime, int progressTime) {
        return getMachineProgressString(isActive, true, maxProgressTime, (long) progressTime);
    }

    public static String getMachineProgressString(boolean isActive, boolean isAllowedToWork, int maxProgressTime,
        int progressTime) {
        return getMachineProgressString(isActive, isAllowedToWork, maxProgressTime, (long) progressTime);
    }

    public static String getMachineProgressString(boolean isActive, long maxProgressTime, long progressTime) {
        return getMachineProgressString(isActive, true, maxProgressTime, progressTime);
    }

    public static String getMachineProgressString(boolean isActive, boolean isAllowedToWork, long maxProgressTime,
        long progressTime) {

        if (Client.waila.showWorkingDisable) {
            if (!isAllowedToWork && !isActive) {
                return StatCollector.translateToLocal("GT5U.waila.machine.working_disabled");
            }
        }

        if (Client.waila.showIdle) {
            if (!isActive) {
                return StatCollector.translateToLocal("GT5U.waila.machine.idle");
            }
        }

        String progressText;

        if (maxProgressTime < 20) {
            progressText = StatCollector
                .translateToLocalFormatted("GT5U.waila.machine.progress_tick", progressTime, maxProgressTime);
        } else {
            String currentSeconds = String.format("%.2f", progressTime / 20.0);
            String maxSeconds = String.format("%.2f", maxProgressTime / 20.0);
            progressText = StatCollector
                .translateToLocalFormatted("GT5U.waila.machine.progress_second", currentSeconds, maxSeconds);
        }

        return TTRenderBar.create(
            progressText,
            ColorUtils.progressBarTop.getColor(),
            ColorUtils.progressBarBottom.getColor(),
            (double) progressTime / maxProgressTime);
    }
}

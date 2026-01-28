package gregtech.common.gui.modularui.multiblock.dronecentre;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.MCHelper;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ButtonWidget;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.tileentities.machines.multi.drone.DroneConnection;
import gregtech.common.tileentities.machines.multi.drone.MTEDroneCentre;

public class DroneCentreGuiUtil {

    public static final Map<Integer, String> TIME_OPTIONS = new LinkedHashMap<>();
    static {
        DroneCentreGuiUtil.TIME_OPTIONS.put(10, "10s");
        DroneCentreGuiUtil.TIME_OPTIONS.put(60, "1min");
        DroneCentreGuiUtil.TIME_OPTIONS.put(600, "10min");
        DroneCentreGuiUtil.TIME_OPTIONS.put(3600, "1h");
        DroneCentreGuiUtil.TIME_OPTIONS.put(86400, "24h");
        DroneCentreGuiUtil.TIME_OPTIONS.put(-1, "All");
    }

    public static IWidget createHighLightButton(DroneConnection conn, PanelSyncManager syncManager) {
        return new ButtonWidget<>().size(16)
            .background(GTGuiTextures.BUTTON_STANDARD)
            .overlay(GTGuiTextures.OVERLAY_BUTTON_REDSTONE_ON)
            .onMousePressed(mouseButton -> {
                EntityPlayer player = syncManager.getPlayer();
                MTEDroneCentre.highlightMachine(player, conn.getMachineCoord());
                player.closeScreen();
                return true;
            })
            .tooltipBuilder(
                t -> t.addLine(IKey.lang("GT5U.gui.button.drone_highlight"))
                    .addLine("x:" + conn.getMachineCoord().posX)
                    .addLine("y:" + conn.getMachineCoord().posY)
                    .addLine("z:" + conn.getMachineCoord().posZ)
                    .addLine(IKey.lang("GT5U.infodata.dimension", conn.getMachineWorld())));
    }

    public static void getTooltipFromItemSafely(RichTooltip tooltipBuilder, ItemStack itemStack) {
        List<String> lines = itemStack
            .getTooltip(MCHelper.getPlayer(), MCHelper.getMc().gameSettings.advancedItemTooltips);

        for (int i = 0; i < lines.size(); ++i) {
            if (i == 0) {
                lines.set(
                    i,
                    itemStack.getItem()
                        .getRarity(itemStack).rarityColor + lines.get(i));
            } else {
                lines.set(i, EnumChatFormatting.GRAY + lines.get(i));
            }
        }
        tooltipBuilder.add(lines.get(0))
            .newLine();
        if (lines.size() > 1) {
            tooltipBuilder.spaceLine();
            int i = 1;

            for (int n = lines.size(); i < n; ++i) {
                tooltipBuilder.add(lines.get(i))
                    .newLine();
            }
        }
    }

    public static String formatValueWithUnits(long value) {
        if (value < 1000) {
            return String.valueOf(value);
        }
        int exp = (int) (Math.log(value) / Math.log(1000));
        char unit = "KMGTP".charAt(exp - 1);
        double formattedValue = value / Math.pow(1000, exp);
        if (formattedValue == Math.floor(formattedValue)) {
            return String.format(Locale.ROOT, "%.0f%c", formattedValue, unit);
        } else {
            return String.format(Locale.ROOT, "%.1f%c", formattedValue, unit);
        }
    }

    public enum SortMode {
        DISTANCE,
        NAME,
        STATUS
    }
}

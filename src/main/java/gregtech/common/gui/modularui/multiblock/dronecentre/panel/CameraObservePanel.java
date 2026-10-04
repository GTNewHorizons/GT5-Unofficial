package gregtech.common.gui.modularui.multiblock.dronecentre.panel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.input.Mouse;

import com.cleanroommc.modularui.api.GuiAxis;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.text.TextRenderer;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.ModularScreen;
import com.cleanroommc.modularui.screen.viewport.ModularGuiContext;
import com.cleanroommc.modularui.theme.WidgetThemeEntry;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.gtnewhorizon.gtnhlib.util.CoordinatePacker;

import gregtech.GTMod;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.common.data.drone.CameraViewportClientManager;
import gregtech.common.data.drone.CameraViewportManager;
import gregtech.common.gui.modularui.multiblock.dronecentre.widget.CameraViewportWidget;

public class CameraObservePanel extends ModularPanel {

    private static final int HEADER_HEIGHT = 16;
    private static final int FOOTER_HEIGHT = 26;
    private static final int SIDEBAR_WIDTH = 100;
    private static final int RESCUE_BUTTON_HEIGHT = 18;
    private static final float SIDEBAR_TEXT_SCALE = 0.75F;
    private static final float HELP_TEXT_SCALE = 0.7F;

    private final List<String> rawRecipeInfo = new ArrayList<>();
    private final List<String> currentRecipeInfo = new ArrayList<>();

    private int lastUpdateWidth = -1;
    private NBTTagCompound lastObservedStatus = null;
    private long lastHoveredCoord = CoordinatePacker.pack(-2, -2, -2);

    public CameraObservePanel(PanelSyncManager syncManager, Runnable closeCallback) {
        super("cameraObservePanel");

        this.sizeRel(CameraViewportClientManager.PANEL_SCREEN_FRACTION)
            .center()
            .disableThemeBackground(true)
            .child(
                Flow.column()
                    .full()
                    .child(createHeader())
                    .child(
                        Flow.row()
                            .fullWidth()
                            .expanded()
                            .child(createMetricsSidebar())
                            .child(createViewportFrame())
                            .child(createRecipeSidebar()))
                    .child(createFooter()))
            .child(ButtonWidget.panelCloseButton())
            .onCloseAction(() -> {
                if (syncManager.isClient() && !GTMod.proxy.cameraViewportManager.isSwitchingToRemoteGui()) {
                    GTMod.proxy.cameraViewportManager.stopObserving();
                    if (closeCallback != null) {
                        closeCallback.run();
                    }
                }
            });
    }

    @Override
    public void onOpen(ModularScreen screen) {
        super.onOpen(screen);
        if (GTMod.proxy.cameraViewportManager != null) {
            GTMod.proxy.cameraViewportManager.setSwitchingToRemoteGui(false);
            if (GTMod.proxy.cameraViewportManager instanceof CameraViewportClientManager cvm) {
                cvm.hideScreenMainPanel(screen);
                Mouse.setGrabbed(false);
            }
        }
    }

    @Override
    public boolean isDraggable() {
        return false;
    }

    private static IWidget createHeader() {
        return IKey.lang("GT5U.gui.text.drone_observe_default")
            .asWidget()
            .fullWidth()
            .height(HEADER_HEIGHT)
            .textAlign(Alignment.CENTER)
            .background(GTGuiTextures.BACKGROUND_STANDARD);
    }

    private static IWidget createFooter() {
        return Flow.column()
            .fullWidth()
            .height(FOOTER_HEIGHT)
            .background(GTGuiTextures.BACKGROUND_STANDARD)
            .mainAxisAlignment(Alignment.MainAxis.CENTER)
            .childPadding(2)
            .child(createHelpLine("GT5U.gui.text.drone_observe_help_1"))
            .child(createHelpLine("GT5U.gui.text.drone_observe_help_2"));
    }

    private static IWidget createHelpLine(String langKey) {
        return IKey.lang(langKey)
            .asWidget()
            .fullWidth()
            .scale(HELP_TEXT_SCALE)
            .textAlign(Alignment.CENTER);
    }

    private static Flow createSidebar() {
        return Flow.column()
            .width(SIDEBAR_WIDTH)
            .fullHeight()
            .background(GTGuiTextures.BACKGROUND_STANDARD)
            .padding(4)
            .childPadding(4);
    }

    private IWidget createMetricsSidebar() {
        return createSidebar().child(createDroneMetricsWidget(SIDEBAR_TEXT_SCALE))
            .child(
                new ButtonWidget<>().fullWidth()
                    .height(RESCUE_BUTTON_HEIGHT)
                    .overlay(IKey.lang("GT5U.gui.button.drone_rescue"))
                    .onMousePressed(mouseButton -> {
                        if (mouseButton == 0) {
                            ((CameraViewportClientManager) GTMod.proxy.cameraViewportManager).resetToSpawn();
                        }
                        return true;
                    })
                    .tooltipBuilder(t -> {
                        if (!Mouse.isGrabbed()) {
                            t.add(IKey.lang("GT5U.gui.button.drone_rescue.tooltip"));
                        }
                    }));
    }

    private IWidget createRecipeSidebar() {
        return createSidebar().child(createRecipeWidget(SIDEBAR_TEXT_SCALE));
    }

    /** The camera view, wrapped in a 1px themed frame drawn just outside of it. */
    private static IWidget createViewportFrame() {
        return new ParentWidget<>().expanded()
            .fullHeight()
            .padding(1)
            .widgetTheme(GTWidgetThemes.DRONE_CAMERA_FRAME)
            .child(new CameraViewportWidget().full());
    }

    /** A dark info box used by both sidebars. */
    private static <T extends Flow> T styleInfoBox(T column) {
        column.widgetTheme(GTWidgetThemes.DRONE_CAMERA_SCREEN)
            .fullWidth()
            .expanded()
            .padding(4)
            .childPadding(2);
        return column;
    }

    private static IWidget createDivider() {
        return new Widget<>().widgetTheme(GTWidgetThemes.DRONE_CAMERA_DIVIDER)
            .fullWidth()
            .height(2)
            .marginBottom(4);
    }

    private Flow createDroneMetricsWidget(float textScale) {
        CameraViewportClientManager cvm = (CameraViewportClientManager) GTMod.proxy.cameraViewportManager;
        Flow col = styleInfoBox(Flow.column());

        int innerW = SIDEBAR_WIDTH - 16;

        // Title header
        col.child(
            IKey.lang("GT5U.gui.text.drone_metrics_header")
                .asWidget()
                .width(innerW)
                .scale(textScale)
                .height((int) (12 * textScale)));

        col.child(createDivider());

        col.child(
            IKey.lang("GT5U.gui.text.drone_cam_stream_on")
                .asWidget()
                .width(innerW)
                .scale(textScale)
                .height((int) (10 * textScale)));

        col.child(IKey.dynamic(() -> {
            int signal = cvm.getSignalStrength();
            if (cvm.isSignalLost()) {
                return StatCollector.translateToLocal("GT5U.gui.text.drone_signal_link") + "§c0%";
            }
            String sigColor = "§a";
            if (signal < 40) {
                sigColor = "§c";
            } else if (signal < 75) {
                sigColor = "§e";
            }
            return StatCollector.translateToLocal("GT5U.gui.text.drone_signal_link") + sigColor + signal + "%";
        })
            .asWidget()
            .width(innerW)
            .scale(textScale)
            .height((int) (10 * textScale)));

        col.child(
            IKey.lang("GT5U.gui.text.drone_recipe_on")
                .asWidget()
                .width(innerW)
                .scale(textScale)
                .height((int) (10 * textScale))
                .marginBottom(4));

        col.child(
            IKey.dynamic(
                () -> String.format(
                    StatCollector.translateToLocal("GT5U.gui.text.drone_cam_pos"),
                    'X',
                    (int) Math.floor(cvm.cameraX)))
                .asWidget()
                .width(innerW)
                .scale(textScale)
                .height((int) (10 * textScale)));

        col.child(
            IKey.dynamic(
                () -> String.format(
                    StatCollector.translateToLocal("GT5U.gui.text.drone_cam_pos"),
                    'Y',
                    (int) Math.floor(cvm.cameraY)))
                .asWidget()
                .width(innerW)
                .scale(textScale)
                .height((int) (10 * textScale)));

        col.child(
            IKey.dynamic(
                () -> String.format(
                    StatCollector.translateToLocal("GT5U.gui.text.drone_cam_pos"),
                    'Z',
                    (int) Math.floor(cvm.cameraZ)))
                .asWidget()
                .width(innerW)
                .scale(textScale)
                .height((int) (10 * textScale))
                .marginBottom(4));

        col.child(
            IKey.str("§7--------------")
                .asWidget()
                .width(innerW)
                .scale(textScale)
                .height((int) (10 * textScale)));

        col.child(
            IKey.lang("GT5U.gui.text.drone_level_excellent")
                .asWidget()
                .width(innerW)
                .scale(textScale)
                .height((int) (10 * textScale)));

        return col;
    }

    private RecipeFlow createRecipeWidget(float textScale) {
        RecipeFlow col = styleInfoBox(new RecipeFlow(this));

        int innerW = SIDEBAR_WIDTH - 16;

        // Title header
        col.child(
            IKey.lang("GT5U.gui.text.recipe_metrics_header")
                .asWidget()
                .width(innerW)
                .scale(textScale)
                .height((int) (12 * textScale)));

        col.child(createDivider());

        // recipe details
        final int maxWidth = (int) (innerW / textScale);
        for (int i = 0; i < 16; i++) {
            final int index = i;
            TextWidget<?> textWidget = IKey.dynamic(() -> {
                if (index < currentRecipeInfo.size()) {
                    return currentRecipeInfo.get(index);
                }
                return "";
            })
                .asWidget()
                .widgetTheme(GTWidgetThemes.DRONE_CAMERA_TEXT)
                .width(innerW)
                .scale(textScale)
                .height((int) (10 * textScale))
                .setEnabledIf(_ -> index < currentRecipeInfo.size());

            textWidget.tooltip()
                .setAutoUpdate(true);
            textWidget.tooltipBuilder(builder -> {
                if (Mouse.isGrabbed()) {
                    return;
                }
                if (index < rawRecipeInfo.size()) {
                    String rawLine = rawRecipeInfo.get(index);
                    String cleanRaw = cleanWailaLine(rawLine);
                    if (!cleanRaw.isEmpty() && TextRenderer.getFontRenderer()
                        .getStringWidth(cleanRaw) > maxWidth) {
                        builder.addLine(cleanRaw);
                    }
                }
            });

            col.child(textWidget);
        }

        return col;
    }

    private void updateExtraInfo(int width) {
        CameraViewportClientManager cvm = (CameraViewportClientManager) GTMod.proxy.cameraViewportManager;
        if (cvm.activeConnection == null) {
            rawRecipeInfo.clear();
            currentRecipeInfo.clear();
            lastUpdateWidth = -1;
            lastObservedStatus = null;
            lastHoveredCoord = CoordinatePacker.pack(-2, -2, -2);
            return;
        }

        NBTTagCompound tag = cvm.observedMachineStatus;
        long hCoord = cvm.hoveredMachineCoord;

        if (width == lastUpdateWidth && tag == lastObservedStatus && hCoord == lastHoveredCoord) {
            return;
        }

        lastUpdateWidth = width;
        lastObservedStatus = tag;
        lastHoveredCoord = hCoord;

        int maxWidth = (int) ((width - 8) / SIDEBAR_TEXT_SCALE);

        List<String> newInfo = new ArrayList<>();

        boolean hasHovered = (hCoord != CameraViewportManager.NULL_COORD);
        boolean hasMatchingTag = tag != null && hasHovered && tag.getLong("observePos") == hCoord;

        if (hasHovered && hasMatchingTag) {
            boolean isActive = tag.getBoolean("isActive");

            if (isActive) {
                newInfo.add(StatCollector.translateToLocal("GT5U.waila.producing"));

                int itemLength = tag.getInteger("outputItemLength");
                for (int i = 0; i < itemLength; i++) {
                    NBTTagCompound itemNBT = tag.getCompoundTag("outputItemStack" + i);
                    ItemStack outputStack = ItemStack.loadItemStackFromNBT(itemNBT);
                    if (outputStack != null) {
                        String name = outputStack.getDisplayName();
                        int count = tag.getInteger("outputItemCount" + i);
                        newInfo.add("§b" + name + " x" + count);
                    }
                }

                int fluidLength = tag.getInteger("outputFluidLength");
                for (int i = 0; i < fluidLength; i++) {
                    String internalName = tag.getString("outputFluidName" + i);
                    if (!internalName.isEmpty()) {
                        net.minecraftforge.fluids.Fluid fluid = FluidRegistry.getFluid(internalName);
                        String fluidName = fluid != null ? new FluidStack(fluid, 1).getLocalizedName() : internalName;
                        int count = tag.getInteger("outputFluidCount" + i);
                        newInfo.add("§3" + fluidName + " x" + count + "L");
                    }
                }

                if (itemLength == 0 && fluidLength == 0) {
                    newInfo.add("§7" + StatCollector.translateToLocal("GT5U.gui.text.drone_no_outputs"));
                }
            } else {
                newInfo.add(StatCollector.translateToLocal("GT5U.gui.text.recipe_none"));
            }

            boolean isLocked = tag.getBoolean("isLockedToRecipe");
            if (isLocked) {
                newInfo.add("§7--------------");
                newInfo.add(StatCollector.translateToLocal("GT5U.gui.text.drone_locked_recipe") + ":");
                String lockedName = tag.getString("lockedRecipeName");
                if (lockedName != null && !lockedName.isEmpty()) {
                    String[] lines = lockedName.split("\r?\n");
                    for (String line : lines) {
                        String trimmed = line.trim();
                        if (!trimmed.isEmpty()) {
                            if (trimmed.startsWith("-")) {
                                newInfo.add("§e" + trimmed);
                            } else {
                                newInfo.add("§6" + trimmed);
                            }
                        }
                    }
                } else {
                    newInfo.add("§aON");
                }
            }
        } else {
            if (hasHovered) {
                newInfo.add(StatCollector.translateToLocal("GT5U.gui.text.recipe_connecting_1"));
                newInfo.add(StatCollector.translateToLocal("GT5U.gui.text.recipe_connecting_2"));
                newInfo.add(StatCollector.translateToLocal("GT5U.gui.text.recipe_connecting_3"));
            } else {
                newInfo.add(StatCollector.translateToLocal("GT5U.gui.text.recipe_no_conn_1"));
                newInfo.add("§7--------------");
                newInfo.add(StatCollector.translateToLocal("GT5U.gui.text.recipe_no_conn_2"));
                newInfo.add(StatCollector.translateToLocal("GT5U.gui.text.recipe_no_conn_3"));
                newInfo.add(StatCollector.translateToLocal("GT5U.gui.text.recipe_no_conn_4"));
            }
        }

        this.rawRecipeInfo.clear();
        for (String line : newInfo) {
            if (line.startsWith("§b") || line.startsWith("§3")
                || line.startsWith("§a")
                || line.startsWith("§e")
                || line.startsWith("§6")
                || line.startsWith("§7")
                || line.startsWith("§8")) {
                this.rawRecipeInfo.add(line.substring(2));
            } else {
                this.rawRecipeInfo.add(line);
            }
        }

        List<String> formatted = new ArrayList<>();
        int maxLines = 15;
        for (int i = 0; i < newInfo.size(); i++) {
            String line = newInfo.get(i);
            String clean = cleanWailaLine(line);
            if (formatted.size() >= maxLines - 1 && i < newInfo.size() - 1) {
                int remaining = newInfo.size() - i;
                String moreTemplate = StatCollector.translateToLocal("GT5U.waila.producing.andmore")
                    .trim();
                formatted.add("§7" + String.format(moreTemplate, remaining));
                break;
            }
            if (!clean.isEmpty()) {
                String drawText = clean;
                if (TextRenderer.getFontRenderer()
                    .getStringWidth(clean) > maxWidth) {
                    int dotW = TextRenderer.getFontRenderer()
                        .getStringWidth("...");
                    drawText = TextRenderer.getFontRenderer()
                        .trimStringToWidth(clean, maxWidth - dotW) + "...";
                }
                formatted.add(drawText);
            }
        }

        this.currentRecipeInfo.clear();
        this.currentRecipeInfo.addAll(formatted);
    }

    public static String cleanWailaLine(String line) {
        if (line == null) return "";

        StringBuilder prefix = new StringBuilder();
        int idx = 0;
        while (idx < line.length()) {
            char c = line.charAt(idx);
            if (c == ' ' || c == '\u00a0') {
                prefix.append(c);
                idx++;
            } else if (c == '§' && idx + 1 < line.length()) {
                prefix.append(line, idx, idx + 2);
                idx += 2;
            } else {
                break;
            }
        }
        String contentPart = line.substring(idx);

        if (contentPart.startsWith("¤¦a{") && contentPart.endsWith("}")) {
            String content = contentPart.substring(4, contentPart.length() - 1);
            String[] parts = content.split("\u0082");
            if (parts.length > 0) {
                String key = parts[0];
                if ("waila.gt.progress".equals(key) && parts.length >= 3) {
                    try {
                        long progress = Long.parseLong(parts[1]);
                        long max = Long.parseLong(parts[2]);
                        String label = StatCollector.translateToLocal("GT5U.gui.text.progress");
                        if (max <= 40) {
                            if (max <= 1) {
                                return String.format(Locale.ROOT, "%s%s%d / %d t", prefix, label, progress, max);
                            } else {
                                double pct = (progress * 100.0) / max;
                                return String
                                    .format(Locale.ROOT, "%s%s%d / %d t (%.1f%%)", prefix, label, progress, max, pct);
                            }
                        } else {
                            double pSec = progress * 0.05;
                            double mSec = max * 0.05;
                            double pct = (progress * 100.0) / max;
                            return String
                                .format(Locale.ROOT, "%s%s%.1f / %.1f s (%.1f%%)", prefix, label, pSec, mSec, pct);
                        }
                    } catch (Exception ignored) {}
                } else if ("waila.stack".equals(key) && parts.length >= 5) {
                    try {
                        int type = Integer.parseInt(parts[1]);
                        String name = parts[2];
                        int amount = Integer.parseInt(parts[3]);
                        int meta = Integer.parseInt(parts[4]);

                        ItemStack stack = null;
                        if (type == 0) {
                            net.minecraft.block.Block block = (net.minecraft.block.Block) net.minecraft.block.Block.blockRegistry
                                .getObject(name);
                            if (block != null) {
                                stack = new ItemStack(block, amount, meta);
                            }
                        } else if (type == 1) {
                            Item item = (Item) Item.itemRegistry.getObject(name);
                            if (item != null) {
                                stack = new ItemStack(item, amount, meta);
                            }
                        }

                        if (stack != null) {
                            String displayName = stack.getDisplayName();
                            return prefix + displayName + " x" + amount;
                        }
                    } catch (Exception ignored) {}
                }
            }
        }

        // WAILA control characters
        contentPart = contentPart.replace("¤", "");
        contentPart = contentPart.replace("¥", "");
        contentPart = contentPart.replace("¦", "");
        contentPart = contentPart.replace("\u0082", "");
        contentPart = contentPart.replace("\u0001", "");
        contentPart = contentPart.replace("\u0002", "");
        contentPart = contentPart.replace("\u0003", "");
        contentPart = contentPart.replace("\u0004", "");
        if (contentPart.contains("{") && contentPart.contains("}")) {
            contentPart = contentPart.replaceAll("\\{[^}]*}", "");
        }
        return prefix + contentPart.trim();
    }

    public static class RecipeFlow extends Flow {

        private final CameraObservePanel panel;

        public RecipeFlow(CameraObservePanel panel) {
            super(GuiAxis.Y);
            this.panel = panel;
        }

        @Override
        public void draw(ModularGuiContext context, WidgetThemeEntry<?> widgetTheme) {
            panel.updateExtraInfo(getArea().width);
            super.draw(context, widgetTheme);
        }
    }
}

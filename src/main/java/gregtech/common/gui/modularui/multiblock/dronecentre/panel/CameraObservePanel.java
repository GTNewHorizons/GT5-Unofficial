package gregtech.common.gui.modularui.multiblock.dronecentre.panel;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.input.Mouse;

import com.cleanroommc.modularui.api.GuiAxis;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.GuiDraw;
import com.cleanroommc.modularui.drawable.text.TextRenderer;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.ModularScreen;
import com.cleanroommc.modularui.screen.viewport.ModularGuiContext;
import com.cleanroommc.modularui.theme.WidgetThemeEntry;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.gtnewhorizon.gtnhlib.util.CoordinatePacker;

import gregtech.GTMod;
import gregtech.common.data.drone.CameraViewportClientManager;
import gregtech.common.data.drone.CameraViewportManager;
import gregtech.common.gui.modularui.multiblock.dronecentre.widget.CameraViewportWidget;

public class CameraObservePanel extends ModularPanel {

    private final Runnable closeCallback;
    private final List<String> rawRecipeInfo = new ArrayList<>();
    private final List<String> currentRecipeInfo = new ArrayList<>();

    private int lastUpdateWidth = -1;
    private NBTTagCompound lastObservedStatus = null;
    private long lastHoveredCoord = CoordinatePacker.pack(-2, -2, -2);

    private static final IDrawable BACKGROUND = (_, x, y, width, height, _) -> {
        drawRect(x, y, x + width, y + height, 0x90101010);
        drawFrame(x, y, x + width, y + height, 0xFF00D2D3);
    };

    public CameraObservePanel(PanelSyncManager syncManager, Runnable closeCallback) {
        super("cameraObservePanel");
        this.closeCallback = closeCallback;

        this.background((_, x, y, w, h, _) -> {
            int sideW = Math.clamp((int) (w * 0.18), 60, 100);
            int margin = Math.max(8, (int) (w * 0.03));
            int gap = Math.max(6, (int) (w * 0.02));

            int vx = margin + sideW + gap;
            int vw = w - 2 * (margin + sideW + gap);
            int vy = Math.max(16, (int) (h * 0.1));
            int footerH = Math.max(20, (int) (h * 0.15));
            int vh = h - vy - footerH;

            int avx = x + vx;
            int avy = y + vy;

            // Background
            drawRect(x, y, x + w, avy, 0xFFC6C6C6);
            drawRect(x, avy + vh, x + w, y + h, 0xFFC6C6C6);
            drawRect(x, avy, avx, avy + vh, 0xFFC6C6C6);
            drawRect(avx + vw, avy, x + w, avy + vh, 0xFFC6C6C6);

            // Border
            int borderColor = 0xFF00D2D3;
            drawRect(avx - 1, avy - 1, avx + vw + 1, avy, borderColor);
            drawRect(avx - 1, avy - 1, avx, avy + vh + 1, borderColor);
            drawRect(avx - 1, avy + vh, avx + vw + 1, avy + vh + 1, borderColor);
            drawRect(avx + vw, avy - 1, avx + vw + 1, avy + vh + 1, borderColor);

            // Outer line
            drawRect(x, y, x + w, y + 1, 0xFF000000);
            drawRect(x, y, x + 1, y + h, 0xFF000000);
            drawRect(x, y + h - 1, x + w, y + h, 0xFF000000);
            drawRect(x + w - 1, y, x + w, y + h, 0xFF000000);

            // Inner line
            drawRect(x + 1, y + 1, x + w - 1, y + 2, 0xFFFFFFFF);
            drawRect(x + 1, y + 1, x + 2, y + h - 1, 0xFFFFFFFF);
            drawRect(x + 1, y + h - 2, x + w - 1, y + h - 1, 0xFF555555);
            drawRect(x + w - 2, y + 1, x + w - 1, y + h - 1, 0xFF555555);
        })
            .onCloseAction(() -> {
                if (syncManager.isClient()) {
                    if (!GTMod.proxy.cameraViewportManager.isSwitchingToRemoteGui()) {
                        GTMod.proxy.cameraViewportManager.stopObserving();
                        if (this.closeCallback != null) {
                            this.closeCallback.run();
                        }
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
    public void beforeResize(boolean onOpen) {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        int sw = sr.getScaledWidth();
        int sh = sr.getScaledHeight();

        int panelW = (int) (sw * 0.8);
        int panelH = (int) (sh * 0.8);

        this.width(panelW)
            .height(panelH);

        this.removeAll();
        buildChildren(panelW, panelH);

        super.beforeResize(onOpen);
    }

    private void buildChildren(int panelW, int panelH) {
        final int sideW = Math.clamp((int) (panelW * 0.18), 60, 100);
        final int margin = Math.max(8, (int) (panelW * 0.03));
        final int gap = Math.max(6, (int) (panelW * 0.02));

        final int vx = margin + sideW + gap;
        final int vw = panelW - 2 * (margin + sideW + gap);
        final int vy = Math.max(16, (int) (panelH * 0.1));
        final int footerH = Math.max(20, (int) (panelH * 0.15));
        final int vh = panelH - vy - footerH;

        this.child(
            new Widget<>().size(panelW, panelH)
                .left(0)
                .top(0));

        this.child(
            IKey.lang("GT5U.gui.text.drone_observe_default")
                .asWidget()
                .textAlign(Alignment.CENTER)
                .scale(Math.clamp(panelW / 426.6F, 0.7F, 1.1F))
                .left(vx)
                .top((vy - 14) / 2)
                .width(vw)
                .height(14));

        // Drone Metrics
        float textScale = Math.clamp(sideW / 113.3F, 0.5F, 0.8F);
        this.child(
            createDroneMetricsWidget(sideW, vh, textScale).left(margin)
                .top(vy));

        this.child(
            new ButtonWidget<>().left(margin + 4)
                .top(vy + vh - 22)
                .size(sideW - 8, 18)
                .onMousePressed(mouseButton -> {
                    if (mouseButton == 0) {
                        ((CameraViewportClientManager) GTMod.proxy.cameraViewportManager).resetToSpawn();
                    }
                    return true;
                })
                .overlay(
                    IKey.lang("GT5U.gui.button.drone_rescue")
                        .alignment(Alignment.CENTER)
                        .color(0xFFFFFFFF))
                .tooltipBuilder(t -> {
                    if (!Mouse.isGrabbed()) {
                        t.add(IKey.lang("GT5U.gui.button.drone_rescue.tooltip"));
                    }
                }));

        // Viewport
        this.child(
            new CameraViewportWidget().left(vx)
                .top(vy)
                .size(vw, vh));

        // Recipe
        this.child(
            createRecipeWidget(sideW, vh, textScale).left(panelW - margin - sideW)
                .top(vy));

        // Help text
        float helpScale = Math.clamp(panelW / 640F, 0.5F, 0.7F);
        int lineH = (int) (10 * helpScale);
        int helpColH = 2 * lineH + 4;

        this.child(
            Flow.column()
                .center()
                .childPadding(4)
                .child(
                    IKey.lang("GT5U.gui.text.drone_observe_help_1")
                        .asWidget()
                        .textAlign(Alignment.CENTER)
                        .scale(helpScale)
                        .width(vw)
                        .height(lineH))
                .child(
                    IKey.lang("GT5U.gui.text.drone_observe_help_2")
                        .asWidget()
                        .textAlign(Alignment.CENTER)
                        .scale(helpScale)
                        .width(vw)
                        .height(lineH))
                .left(vx)
                .top(vy + vh + (footerH - helpColH) / 2)
                .width(vw)
                .height(helpColH));

        this.child(ButtonWidget.panelCloseButton());
    }

    @Override
    public boolean isDraggable() {
        return false;
    }

    private Flow createDroneMetricsWidget(int width, int height, float textScale) {
        CameraViewportClientManager cvm = (CameraViewportClientManager) GTMod.proxy.cameraViewportManager;
        Flow col = Flow.column()
            .background(BACKGROUND)
            .size(width, height)
            .padding(4)
            .childPadding(2);

        int innerW = width - 8;

        // Title header
        col.child(
            IKey.lang("GT5U.gui.text.drone_metrics_header")
                .asWidget()
                .width(innerW)
                .scale(textScale)
                .height((int) (12 * textScale)));

        // Decorative line
        col.child(
            ((IDrawable) (_, x, y, width1, height1, _) -> drawRect(x, y, x + width1, y + height1, 0x5000D2D3))
                .asWidget()
                .width(innerW)
                .height(2)
                .marginBottom(4));

        col.child(
            IKey.lang("GT5U.gui.text.drone_cam_stream_on")
                .asWidget()
                .width(innerW)
                .scale(textScale)
                .height((int) (10 * textScale)));

        col.child(IKey.dynamic(() -> {
            int signal = cvm.getSignalStrength();
            if (signal <= 10) {
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
            IKey.str("§8------------------")
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

    private RecipeFlow createRecipeWidget(int width, int height, float textScale) {
        RecipeFlow col = new RecipeFlow(this);
        col.background(BACKGROUND)
            .size(width, height)
            .padding(4)
            .childPadding(2);

        int innerW = width - 8;

        // Title header
        col.child(
            IKey.lang("GT5U.gui.text.recipe_metrics_header")
                .asWidget()
                .width(innerW)
                .scale(textScale)
                .height((int) (12 * textScale)));

        // line
        col.child(
            ((IDrawable) (_, x, y, width1, height1, _) -> drawRect(x, y, x + width1, y + height1, 0x5000D2D3))
                .asWidget()
                .width(innerW)
                .height(2)
                .marginBottom(4));

        // recipe details
        final int maxWidth = (int) ((width - 8) / textScale);
        for (int i = 0; i < 16; i++) {
            final int index = i;
            TextWidget<?> textWidget = IKey.dynamic(() -> {
                if (index < currentRecipeInfo.size()) {
                    return currentRecipeInfo.get(index);
                }
                return "";
            })
                .color(0xFFCCCCCC)
                .asWidget()
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

        float textScale = Math.clamp(width / 113.3F, 0.5F, 0.8F);
        int maxWidth = (int) ((width - 8) / textScale);

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
        } else {
            if (hasHovered) {
                newInfo.add(StatCollector.translateToLocal("GT5U.gui.text.recipe_connecting_1"));
                newInfo.add(StatCollector.translateToLocal("GT5U.gui.text.recipe_connecting_2"));
                newInfo.add(StatCollector.translateToLocal("GT5U.gui.text.recipe_connecting_3"));
            } else {
                newInfo.add(StatCollector.translateToLocal("GT5U.gui.text.recipe_no_conn_1"));
                newInfo.add("§8------------------");
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
                        String progress = parts[1];
                        String max = parts[2];
                        return prefix + StatCollector.translateToLocal("GT5U.gui.text.progress")
                            + progress
                            + " / "
                            + max;
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

    private static void drawRect(int left, int top, int right, int bottom, int color) {
        GuiDraw.drawRect(left, top, right - left, bottom - top, color);
    }

    private static void drawFrame(int left, int top, int right, int bottom, int color) {
        GuiDraw.drawRect(left, top, right - left, 1, color);
        GuiDraw.drawRect(left, bottom - 1, right - left, 1, color);
        GuiDraw.drawRect(left, top, 1, bottom - top, color);
        GuiDraw.drawRect(right - 1, top, 1, bottom - top, color);
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

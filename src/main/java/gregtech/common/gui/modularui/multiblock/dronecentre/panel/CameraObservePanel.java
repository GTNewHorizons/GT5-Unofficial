package gregtech.common.gui.modularui.multiblock.dronecentre.panel;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleSupplier;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.input.Mouse;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.ModularScreen;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.GTMod;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.common.data.drone.CameraViewportClientManager;
import gregtech.common.data.drone.CameraViewportManager;
import gregtech.common.gui.modularui.multiblock.dronecentre.DroneCentreGuiUtil;
import gregtech.common.gui.modularui.multiblock.dronecentre.widget.CameraViewportWidget;

public class CameraObservePanel extends ModularPanel {

    // These must match the border sizes of GTGuiTextures.BACKGROUND_DRONE_CAMERA, whose transparent window is where
    // the camera view goes
    private static final int HEADER_HEIGHT = 16;
    private static final int FOOTER_HEIGHT = 26;
    private static final int SIDEBAR_WIDTH = 100;
    private static final int FRAME_BEVEL = 3;
    private static final int RESCUE_BUTTON_HEIGHT = 26;
    private static final float SIDEBAR_TEXT_SCALE = 0.75F;
    private static final float HELP_TEXT_SCALE = 0.7F;

    private final CameraViewportClientManager cameraManager = (CameraViewportClientManager) GTMod.proxy.cameraViewportManager;

    /** Recipe sidebar lines. The detail lines are shown below a divider, which is hidden when they are empty. */
    private final List<String> recipeLines = new ArrayList<>();
    private final List<String> recipeDetailLines = new ArrayList<>();
    private boolean recipeTextOutdated = true;
    private NBTTagCompound lastObservedStatus = null;
    private long lastHoveredCoord = CameraViewportManager.NULL_COORD;

    public CameraObservePanel(PanelSyncManager syncManager, Runnable closeCallback) {
        super("cameraObservePanel");

        this.sizeRel(CameraViewportClientManager.PANEL_SCREEN_FRACTION)
            .center()
            .widgetTheme(GTWidgetThemes.BACKGROUND_DRONE_CAMERA)
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
    public void onUpdate() {
        super.onUpdate();
        refreshRecipeText();
    }

    @Override
    public boolean isDraggable() {
        return false;
    }

    private static IWidget createHeader() {
        return IKey.lang("GT5U.gui.text.drone_observe_default")
            .asWidget()
            .fullWidth()
            .marginTop(FRAME_BEVEL)
            .height(HEADER_HEIGHT - FRAME_BEVEL)
            .textAlign(Alignment.CENTER);
    }

    private static IWidget createFooter() {
        return Flow.column()
            .fullWidth()
            .height(FOOTER_HEIGHT)
            .paddingBottom(FRAME_BEVEL)
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
            .padding(4)
            .childPadding(2);
    }

    private IWidget createMetricsSidebar() {
        ListWidget<IWidget, ?> metrics = createInfoBox("GT5U.gui.text.drone_metrics_header")
            .child(createInfoLine(IKey.lang("GT5U.gui.text.drone_cam_stream_on")))
            .child(createInfoLine(IKey.dynamic(this::formatSignalStrength)))
            .child(createInfoLine(IKey.lang("GT5U.gui.text.drone_recipe_on")).marginBottom(4))
            .child(createInfoLine(createCameraPositionKey('X', () -> cameraManager.cameraX)))
            .child(createInfoLine(createCameraPositionKey('Y', () -> cameraManager.cameraY)))
            .child(createInfoLine(createCameraPositionKey('Z', () -> cameraManager.cameraZ)))
            .child(createDivider())
            .child(createInfoLine(IKey.lang("GT5U.gui.text.drone_level_excellent")));

        return createSidebar().child(metrics)
            .child(
                new ButtonWidget<>().fullWidth()
                    .height(RESCUE_BUTTON_HEIGHT)
                    .overlay(IKey.lang("GT5U.gui.button.drone_rescue"))
                    .onMousePressed(mouseButton -> {
                        if (mouseButton == 0) {
                            cameraManager.resetToSpawn();
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
        return createSidebar().child(
            createInfoBox("GT5U.gui.text.recipe_metrics_header")
                .child(createInfoLine(IKey.dynamic(() -> String.join("\n", recipeLines))))
                .child(createDivider().setEnabledIf(_ -> !recipeDetailLines.isEmpty()))
                .child(
                    createInfoLine(IKey.dynamic(() -> String.join("\n", recipeDetailLines)))
                        .setEnabledIf(_ -> !recipeDetailLines.isEmpty())));
    }

    /** The camera view, wrapped in a 1px themed frame drawn just outside of it. */
    private static IWidget createViewportFrame() {
        return new ParentWidget<>().expanded()
            .fullHeight()
            .padding(1)
            .widgetTheme(GTWidgetThemes.DRONE_CAMERA_FRAME)
            .child(new CameraViewportWidget().full());
    }

    /** A dark, scrollable info box with a title and divider, used by both sidebars. */
    private static ListWidget<IWidget, ?> createInfoBox(String headerLangKey) {
        return new ListWidget<>().widgetTheme(GTWidgetThemes.DRONE_CAMERA_SCREEN)
            .fullWidth()
            .expanded()
            .padding(4)
            .collapseDisabledChild()
            .child(createInfoLine(IKey.lang(headerLangKey)).textAlign(Alignment.CENTER))
            .child(createDivider());
    }

    private static Widget<?> createDivider() {
        return new Widget<>().widgetTheme(GTWidgetThemes.DRONE_CAMERA_DIVIDER)
            .fullWidth()
            .height(2)
            .marginTop(2)
            .marginBottom(4);
    }

    private static TextWidget<?> createInfoLine(IKey text) {
        return text.asWidget()
            .widgetTheme(GTWidgetThemes.DRONE_CAMERA_TEXT)
            .fullWidth()
            .scale(SIDEBAR_TEXT_SCALE)
            .textAlign(Alignment.CenterLeft)
            .marginBottom(2);
    }

    private static IKey createCameraPositionKey(char axis, DoubleSupplier position) {
        return IKey
            .lang("GT5U.gui.text.drone_cam_pos", () -> new Object[] { axis, (int) Math.floor(position.getAsDouble()) });
    }

    private String formatSignalStrength() {
        int signal = cameraManager.getSignalStrength();
        EnumChatFormatting signalColor;
        if (cameraManager.isSignalLost()) {
            signal = 0;
            signalColor = EnumChatFormatting.RED;
        } else if (signal < 40) {
            signalColor = EnumChatFormatting.RED;
        } else if (signal < 75) {
            signalColor = EnumChatFormatting.YELLOW;
        } else {
            signalColor = EnumChatFormatting.GREEN;
        }
        return StatCollector.translateToLocalFormatted("GT5U.gui.text.drone_signal_link", signalColor, signal);
    }

    private void refreshRecipeText() {
        NBTTagCompound status = cameraManager.observedMachineStatus;
        long hoveredCoord = cameraManager.hoveredMachineCoord;
        if (!recipeTextOutdated && status == lastObservedStatus && hoveredCoord == lastHoveredCoord) {
            return;
        }
        recipeTextOutdated = false;
        lastObservedStatus = status;
        lastHoveredCoord = hoveredCoord;

        recipeLines.clear();
        recipeDetailLines.clear();
        buildRecipeLines(status, hoveredCoord);
        cleanLines(recipeLines);
        cleanLines(recipeDetailLines);
    }

    /** Strips WAILA formatting from every line and drops lines that end up empty. */
    private static void cleanLines(List<String> lines) {
        lines.replaceAll(DroneCentreGuiUtil::cleanWailaLine);
        lines.removeIf(String::isEmpty);
    }

    private void buildRecipeLines(NBTTagCompound status, long hoveredCoord) {
        if (cameraManager.activeConnection == null) {
            return;
        }

        if (hoveredCoord == CameraViewportManager.NULL_COORD) {
            recipeLines.add(StatCollector.translateToLocal("GT5U.gui.text.recipe_no_conn"));
            recipeDetailLines.add(StatCollector.translateToLocal("GT5U.gui.text.recipe_no_conn_hint"));
            return;
        }

        // The server has not answered for the newly hovered machine yet
        if (status == null || status.getLong("observePos") != hoveredCoord) {
            recipeLines.add(StatCollector.translateToLocal("GT5U.gui.text.recipe_connecting"));
            return;
        }

        if (status.getBoolean("isActive")) {
            addOutputLines(status, recipeLines);
        } else {
            recipeLines.add(StatCollector.translateToLocal("GT5U.gui.text.recipe_none"));
        }

        if (status.getBoolean("isLockedToRecipe")) {
            recipeDetailLines.add(StatCollector.translateToLocal("GT5U.gui.text.drone_locked_recipe"));
            String lockedRecipeName = status.getString("lockedRecipeName");
            if (lockedRecipeName.isEmpty()) {
                recipeDetailLines.add(
                    EnumChatFormatting.GREEN
                        + StatCollector.translateToLocal("GT5U.gui.text.drone_locked_recipe_unnamed"));
            } else {
                for (String line : lockedRecipeName.split("\r?\n")) {
                    String trimmed = line.trim();
                    if (trimmed.isEmpty()) continue;
                    EnumChatFormatting color = trimmed.startsWith("-") ? EnumChatFormatting.YELLOW
                        : EnumChatFormatting.GOLD;
                    recipeDetailLines.add(color + trimmed);
                }
            }
        }
    }

    private static void addOutputLines(NBTTagCompound status, List<String> lines) {
        lines.add(StatCollector.translateToLocal("GT5U.waila.producing"));

        int itemCount = status.getInteger("outputItemLength");
        for (int i = 0; i < itemCount; i++) {
            ItemStack outputStack = ItemStack.loadItemStackFromNBT(status.getCompoundTag("outputItemStack" + i));
            if (outputStack != null) {
                lines.add(
                    EnumChatFormatting.AQUA + StatCollector.translateToLocalFormatted(
                        "GT5U.gui.text.drone_output_item",
                        outputStack.getDisplayName(),
                        status.getInteger("outputItemCount" + i)));
            }
        }

        int fluidCount = status.getInteger("outputFluidLength");
        for (int i = 0; i < fluidCount; i++) {
            String fluidId = status.getString("outputFluidName" + i);
            if (fluidId.isEmpty()) continue;
            Fluid fluid = FluidRegistry.getFluid(fluidId);
            String fluidName = fluid != null ? new FluidStack(fluid, 1).getLocalizedName() : fluidId;
            lines.add(
                EnumChatFormatting.DARK_AQUA + StatCollector.translateToLocalFormatted(
                    "GT5U.gui.text.drone_output_fluid",
                    fluidName,
                    status.getInteger("outputFluidCount" + i)));
        }

        if (itemCount == 0 && fluidCount == 0) {
            lines.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("GT5U.gui.text.drone_no_outputs"));
        }
    }
}

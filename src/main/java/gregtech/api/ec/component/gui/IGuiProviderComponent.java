package gregtech.api.ec.component.gui;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.IComponent;
import gregtech.api.ec.ISectionKey;

public interface IGuiProviderComponent extends IComponent {

    ComponentKey.ListKey<IGuiProviderComponent> KEY = ComponentKey.list();

    enum TerminalSection implements ISectionKey {

        MACHINE_MODE(1000),
        START_UP(2000),
        RUNNING(3000),
        SHUTDOWN_DURATION(4000),
        SHUTDOWN_REASON(5000),
        STRUCTURE_ERRORS(6000),
        RECIPE_RESULT(7000),
        RECIPE_PROGRESS(8000),
        RECIPE_OUTPUTS(9000),
        PENDING_TITLE(10000),
        PENDING_OUTPUTS(11000),
        EFFICIENCY(12000);

        public final int priority;

        TerminalSection(int priority) {
            this.priority = priority;
        }

        @Override
        public @NotNull String getSectionId() {
            return name();
        }

        @Override
        public int getPriority() {
            return priority;
        }
    }

    enum LeftGapSection implements ISectionKey {

        VOIDING(1000),
        INPUT_SEPARATION(2000),
        BATCH_MODE(3000),
        RECIPE_LOCK(4000),
        MACHINE_MODE(5000);

        public final int priority;

        LeftGapSection(int priority) {
            this.priority = priority;
        }

        @Override
        public @NotNull String getSectionId() {
            return name();
        }

        @Override
        public int getPriority() {
            return priority;
        }
    }

    enum RightGapSection implements ISectionKey {

        POWER_PANEL(1000);

        public final int priority;

        RightGapSection(int priority) {
            this.priority = priority;
        }

        @Override
        public @NotNull String getSectionId() {
            return name();
        }

        @Override
        public int getPriority() {
            return priority;
        }
    }

    enum LeftCornerSection implements ISectionKey {
        ;

        public final int priority;

        LeftCornerSection(int priority) {
            this.priority = priority;
        }

        @Override
        public @NotNull String getSectionId() {
            return name();
        }

        @Override
        public int getPriority() {
            return priority;
        }
    }

    enum RightCornerSection implements ISectionKey {

        SHUTDOWN_REASON(1000),
        MAINTAIN(2000),
        LOGO(3000);

        public final int priority;

        RightCornerSection(int priority) {
            this.priority = priority;
        }

        @Override
        public @NotNull String getSectionId() {
            return name();
        }

        @Override
        public int getPriority() {
            return priority;
        }
    }

    enum PowerPanelSection implements ISectionKey {

        TITLE(1000),
        PARALLEL_LABEL(2000),
        PARALLEL_CONFIGURE(3000),
        POWERFAIL_EVENTS(4000);

        public final int priority;

        PowerPanelSection(int priority) {
            this.priority = priority;
        }

        @Override
        public @NotNull String getSectionId() {
            return name();
        }

        @Override
        public int getPriority() {
            return priority;
        }
    }

    @FunctionalInterface
    interface GuiWidgetFactory {

        @Nullable
        IWidget create(@NotNull ModularPanel panel, @NotNull PanelSyncManager syncManager);
    }

    void configureGui(@NotNull MultiblockGuiBuilder builder);
}

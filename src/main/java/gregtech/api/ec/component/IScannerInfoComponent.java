package gregtech.api.ec.component;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.IComponent;
import gregtech.api.ec.ISectionKey;
import gregtech.api.ec.OrderedSectionBuilder;

public interface IScannerInfoComponent extends IComponent {

    ComponentKey.ListKey<IScannerInfoComponent> KEY = ComponentKey.list();

    enum Section implements ISectionKey {

        OWNER(1000),
        MTE_ID(2000),
        PROGRESS(3000),
        ENERGY_INPUT(4000),
        ENERGY_CONSUME(5000),
        MAINTAIN(6000),
        EFFICIENCY(7000),
        POLLUTION(8000),
        RECIPES_DONE(9000),
        TOTAL_RUN_TIME(10000);
        ;

        public final int priority;

        Section(int priority) {
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

    void appendInfoData(@NotNull OrderedSectionBuilder<Consumer<List<String>>> builder);

    void appendInfoMap(@NotNull Map<String, String> map);
}

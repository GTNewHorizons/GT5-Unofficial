package gregtech.api.ec.component;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.IComponent;
import gregtech.api.ec.ISectionKey;
import gregtech.api.ec.OrderedSectionBuilder;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public interface IWailaBodyProviderComponent extends IComponent {

    ComponentKey.ListKey<IWailaBodyProviderComponent> KEY = ComponentKey.list();

    enum Section implements ISectionKey {

        STATUS(1000),
        ENERGY(2000),
        OUTPUTS(3000),
        PROGRESS(4000),
        CPU_LOAD(5000),
        PARALLEL(6000),
        MODE(7000),
        CUSTOM(8000);

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

    record WailaBodyContext(ItemStack itemStack, @NotNull NBTTagCompound tag, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {}

    @FunctionalInterface
    interface WailaBodyProvider {

        void append(@NotNull WailaBodyContext ctx, @NotNull List<String> currentTip);
    }

    void appendWailaBody(@NotNull OrderedSectionBuilder<WailaBodyProvider> builder);
}

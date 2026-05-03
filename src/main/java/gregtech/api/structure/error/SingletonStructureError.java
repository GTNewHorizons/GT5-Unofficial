package gregtech.api.structure.error;

import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.github.bsideup.jabel.Desugar;

import gregtech.api.enums.StructureErrorId;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;

@Desugar
public record SingletonStructureError(StructureErrorId id, String langKey, Supplier<IWidget> widgetSupplier)
    implements StructureError {

    SingletonStructureError(StructureErrorId id, String langKey) {
        this(
            id,
            langKey,
            () -> IKey.lang(langKey)
                .asWidget());
    }

    SingletonStructureError(StructureErrorId id, Supplier<IWidget> widgetSupplier) {
        this(id, null, widgetSupplier);
    }

    @Override
    public StructureErrorId getId() {
        return id;
    }

    @Override
    public void serialize(PacketBuffer buffer) {

    }

    @Override
    public StructureError deserialize(PacketBuffer buffer) {
        return this;
    }

    @Override
    public IWidget createWidget(MTEMultiBlockBaseGui<?> gui) {
        return widgetSupplier.get();
    }

    @Override
    public String getDisplayString() {
        if (langKey != null) {
            return net.minecraft.util.StatCollector.translateToLocal(langKey);
        }
        return id.name();
    }

    @Override
    public StructureError copy() {
        return this;
    }
}

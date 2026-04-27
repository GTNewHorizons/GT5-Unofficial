package gregtech.api.structure.error;

import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.github.bsideup.jabel.Desugar;

import gregtech.api.enums.StructureErrorId;

@Desugar
public record NullaryStructureError(StructureErrorId id, Supplier<IWidget> widgetSupplier) implements StructureError {

    NullaryStructureError(StructureErrorId id, String lang_key) {
        this(
            id,
            IKey.lang(lang_key)
                .asWidget());
    }

    NullaryStructureError(StructureErrorId id, IWidget widget) {
        this(id, () -> widget);
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
    public IWidget createWidget() {
        return widgetSupplier.get();
    }

    @Override
    public StructureError copy() {
        return this;
    }
}

package gregtech.api.structure.error;

import net.minecraft.network.PacketBuffer;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.github.bsideup.jabel.Desugar;

import gregtech.api.enums.StructureErrorId;

@Desugar
public record NullaryStructureError(StructureErrorId id, String lang_key, Object[] format) implements StructureError {

    NullaryStructureError(StructureErrorId id, String lang_key) {
        this(id, lang_key, new Object[0]);
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
        return IKey.lang(lang_key, () -> format)
            .asWidget();
    }

    @Override
    public StructureError copy() {
        return this;
    }
}

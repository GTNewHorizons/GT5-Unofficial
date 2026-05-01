package gregtech.api.structure.error;

import java.io.IOException;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.github.bsideup.jabel.Desugar;

import gregtech.api.enums.StructureErrorId;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;

@Desugar
public record TooManyHatch(int itemId, int itemMeta, int max) implements StructureError {

    public TooManyHatch(ItemStack stack, int max) {
        this(Item.getIdFromItem(stack.getItem()), stack.getItemDamage(), max);
    }

    @Override
    public StructureErrorId getId() {
        return StructureErrorId.TOO_MANY_HATCHES;
    }

    @Override
    public void serialize(PacketBuffer buffer) throws IOException {
        buffer.writeInt(itemId);
        buffer.writeInt(itemMeta);
        buffer.writeInt(max);
    }

    @Override
    public StructureError deserialize(PacketBuffer buffer) throws IOException {
        return new TooManyHatch(buffer.readInt(), buffer.readInt(), buffer.readInt());
    }

    @Override
    public IWidget createWidget(MTEMultiBlockBaseGui<?> gui) {
        return IKey
            .lang(
                "GT5U.gui.too_many_hatches",
                new ItemStack(Item.getItemById(itemId), 1, itemMeta).getDisplayName(),
                max)
            .asWidget();
    }

    @Override
    public String getDisplayString() {
        return net.minecraft.util.StatCollector.translateToLocalFormatted(
            "GT5U.gui.too_many_hatches",
            new ItemStack(Item.getItemById(itemId), 1, itemMeta).getDisplayName(),
            max);
    }

    @Override
    public StructureError copy() {
        return new TooManyHatch(itemId, itemMeta, max);
    }
}

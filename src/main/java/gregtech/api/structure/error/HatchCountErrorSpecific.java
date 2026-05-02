package gregtech.api.structure.error;

import static gregtech.api.structure.error.HatchCountError.createWidgetByName;

import java.io.IOException;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.widget.IWidget;
import com.github.bsideup.jabel.Desugar;

import gregtech.api.casing.Casings;
import gregtech.api.enums.StructureErrorId;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;

@Desugar
public record HatchCountErrorSpecific(ErrorType type, int itemId, int itemMeta, int current, int target)
    implements StructureError {

    public HatchCountErrorSpecific(ErrorType type, ItemStack stack, int current, int target) {
        this(type, Item.getIdFromItem(stack.getItem()), stack.getItemDamage(), current, target);
    }

    public HatchCountErrorSpecific(ErrorType type, Casings casings, int current, int target) {
        this(type, Item.getIdFromItem(casings.getItem()), casings.getBlockMeta(), current, target);
    }

    @Override
    public StructureErrorId getId() {
        return StructureErrorId.HATCH_COUNT_ERROR_SPECIFIC;
    }

    @Override
    public void serialize(PacketBuffer buffer) throws IOException {
        buffer.writeInt(type.ordinal());
        buffer.writeInt(itemId);
        buffer.writeInt(itemMeta);
        buffer.writeInt(current);
        buffer.writeInt(target);
    }

    @Override
    public StructureError deserialize(PacketBuffer buffer) throws IOException {
        return new HatchCountErrorSpecific(
            ErrorType.from(buffer.readInt()),
            buffer.readInt(),
            buffer.readInt(),
            buffer.readInt(),
            buffer.readInt());
    }

    @Override
    public IWidget createWidget(MTEMultiBlockBaseGui<?> gui) {
        ItemStack hatch = new ItemStack(Item.getItemById(itemId), 1, itemMeta);
        return createWidgetByName(type, target, hatch.getDisplayName(), current);
    }

    @Override
    public String getDisplayString() {
        ItemStack hatch = new ItemStack(Item.getItemById(itemId), 1, itemMeta);
        return switch (type) {
            case TOO_FEW -> {
                if (target == 1) {
                    yield StatCollector.translateToLocalFormatted("GT5U.gui.missing_hatch", hatch.getDisplayName());
                } else {
                    yield StatCollector.translateToLocalFormatted(
                        "GT5U.gui.text.too_few_hatch",
                        hatch.getDisplayName(),
                        target,
                        current);
                }
            }
            case NOT_MATCH -> StatCollector
                .translateToLocalFormatted("GT5U.gui.text.not_match_hatch", target, hatch.getDisplayName());
            case TOO_MANY -> StatCollector
                .translateToLocalFormatted("GT5U.gui.text.too_many_hatch", hatch.getDisplayName(), target, current);
        };
    }

    @Override
    public StructureError copy() {
        return new HatchCountErrorSpecific(type, itemId, itemMeta, current, target);
    }
}

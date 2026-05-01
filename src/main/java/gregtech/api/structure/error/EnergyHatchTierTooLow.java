package gregtech.api.structure.error;

import static gregtech.api.enums.GTValues.VN;

import java.io.IOException;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.github.bsideup.jabel.Desugar;

import gregtech.api.enums.StructureErrorId;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;

@Desugar
public record EnergyHatchTierTooLow(int current, int needed) implements StructureError {

    @Override
    public StructureErrorId getId() {
        return StructureErrorId.ENERGY_HATCH_TIER_TOO_LOW;
    }

    @Override
    public void serialize(PacketBuffer buffer) throws IOException {
        buffer.writeInt(current);
        buffer.writeInt(needed);
    }

    @Override
    public StructureError deserialize(PacketBuffer buffer) throws IOException {
        return new EnergyHatchTierTooLow(buffer.readInt(), buffer.readInt());
    }

    @Override
    public IWidget createWidget(MTEMultiBlockBaseGui<?> gui) {
        return IKey.lang("GT5U.gui.text.energy_hatch_tier_too_low", VN[needed])
            .asWidget();
    }

    @Override
    public String getDisplayString() {
        return StatCollector.translateToLocalFormatted("GT5U.gui.text.energy_hatch_tier_too_low", VN[needed]);
    }

    @Override
    public StructureError copy() {
        return new EnergyHatchTierTooLow(current, needed);
    }
}

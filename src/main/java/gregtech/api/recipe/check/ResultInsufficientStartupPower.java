package gregtech.api.recipe.check;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.StatCollector;

import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.util.GT_Utility;

public class ResultInsufficientStartupPower implements CheckRecipeResult {

    private int required;

    ResultInsufficientStartupPower(int required) {
        this.required = required;
    }

    @Override
    public String getID() {
        return "insufficient_startup_power";
    }

    @Override
    public boolean wasSuccessful() {
        return false;
    }

    @Override
    public String getDisplayString() {
        return StatCollector.translateToLocalFormatted(
            "GT5U.gui.text.insufficient_startup_power",
            GT_Utility.formatNumbers(required),
            HeatingCoilLevel.getDisplayNameFromHeat(required, true));
    }

    @Override
    public CheckRecipeResult newInstance() {
        return new ResultInsufficientStartupPower(0);
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeVarIntToBuffer(required);
    }

    @Override
    public void decode(PacketBuffer buffer) {
        required = buffer.readVarIntFromBuffer();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultInsufficientStartupPower that = (ResultInsufficientStartupPower) o;
        return required == that.required;
    }
}

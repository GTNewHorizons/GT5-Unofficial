package com.github.bartimaeusnek.bartworks.util;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.StatCollector;

import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.util.GT_Utility;

public class ResultWrongSievert implements CheckRecipeResult {

    public enum NeededSievertType {
        EXACTLY,
        MINIMUM
    }

    private NeededSievertType type;
    private int required;

    public ResultWrongSievert(int required, NeededSievertType type) {
        this.required = required;
        this.type = type;
    }

    public String getID() {
        return "wrong_sievert";
    }

    @Override
    public boolean wasSuccessful() {
        return false;
    }

    @Override
    public String getDisplayString() {
        return switch (this.type) {
            case EXACTLY -> StatCollector.translateToLocalFormatted(
                    "GT5U.gui.text.wrong_sievert_exactly",
                    GT_Utility.formatNumbers(this.required));
            case MINIMUM -> StatCollector.translateToLocalFormatted(
                    "GT5U.gui.text.wrong_sievert_min",
                    GT_Utility.formatNumbers(this.required));
        };
    }

    @Override
    public CheckRecipeResult newInstance() {
        return new ResultWrongSievert(0, NeededSievertType.EXACTLY);
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeVarIntToBuffer(this.required);
        buffer.writeVarIntToBuffer(this.type.ordinal());
    }

    @Override
    public void decode(PacketBuffer buffer) {
        this.required = buffer.readVarIntFromBuffer();
        this.type = NeededSievertType.values()[buffer.readVarIntFromBuffer()];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        ResultWrongSievert that = (ResultWrongSievert) o;
        return this.required == that.required;
    }

    /**
     * Cannot process recipe because the machine doesn't have the minimum amount of sievert
     */
    public static CheckRecipeResult insufficientSievert(int required) {
        return new ResultWrongSievert(required, NeededSievertType.MINIMUM);
    }

    /**
     * Cannot process recipe because the machine doesn't have the exact amount of sievert
     */
    public static CheckRecipeResult wrongSievert(int required) {
        return new ResultWrongSievert(required, NeededSievertType.EXACTLY);
    }
}

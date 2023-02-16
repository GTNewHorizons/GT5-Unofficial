package gregtech.common.power;

import gregtech.api.util.GT_Utility;

public class UnspecifiedEUPower extends EUPower {

    private final String VOLTAGE = GT_Utility.trans("271", "unspecified");
    private final String AMPERAGE = GT_Utility.trans("271", "unspecified");

    public UnspecifiedEUPower(byte tier, int amperage) {
        super(tier, amperage);
    }

    @Override
    public String getPowerUsageString() {
        return super.getVoltageString();
    }

    @Override
    public String getVoltageString() {
        return VOLTAGE;
    }

    @Override
    public String getAmperageString() {
        return AMPERAGE;
    }
}

package gregtech.api.multitileentity.enums;

import gregtech.api.enums.GT_Values;

public enum GT_MultiTileUpgradeCasing {

    ULV_Inventory(0),
    LV_Inventory(1),
    MV_Inventory(2),
    HV_Inventory(3),
    EV_Inventory(4),
    IV_Inventory(5),
    LuV_Inventory(6),
    ZPM_Inventory(7),
    UV_Inventory(8),
    UHV_Inventory(9),
    UEV_Inventory(10),
    UIV_Inventory(11),
    UXV_Inventory(12),
    UMV_Inventory(13),
    MAX_Inventory(14),
    ULV_Tank(15),
    LV_Tank(16),
    MV_Tank(17),
    HV_Tank(18),
    EV_Tank(19),
    IV_Tank(20),
    LuV_Tank(21),
    ZPM_Tank(22),
    UV_Tank(23),
    UHV_Tank(24),
    UEV_Tank(25),
    UIV_Tank(26),
    UXV_Tank(27),
    UMV_Tank(28),
    MAX_Tank(29),
    Cleanroom(30),
    NONE(GT_Values.W);

    private final int meta;

    GT_MultiTileUpgradeCasing(int meta) {
        this.meta = meta;
    }

    public int getId() {
        return meta;
    }
}

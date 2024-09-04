package gregtech.api.multitileentity.enums;

import gregtech.api.enums.GTValues;

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
    Amp_4(30),
    Amp_16(31),
    Amp_64(32),
    Amp_256(33),
    Amp_1_024(34),
    Amp_4_096(35),
    Amp_16_384(36),
    Amp_65_536(37),
    Amp_262_144(38),
    Amp_1_048_576(39),
    Laser(40),
    Wireless(41),
    Cleanroom(42),
    Heater_Prototype(100),
    Heater_IndustrialGrade(101),
    Heater_NextGen(102),
    Heater_Omnipotent(103),
    Heater_OmegaType(104),
    Insulator_Prototype(105),
    Insulator_IndustrialGrade(106),
    Insulator_NextGen(107),
    Insulator_Omnipotent(108),
    Insulator_OmegaType(109),
    NONE(GTValues.W);

    private final int meta;

    GT_MultiTileUpgradeCasing(int meta) {
        this.meta = meta;
    }

    public int getId() {
        return meta;
    }
}

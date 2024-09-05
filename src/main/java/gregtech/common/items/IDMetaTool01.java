package gregtech.common.items;

public enum IDMetaTool01 {

    // Please pretty please, add your entries while conserving the order
    SWORD(0),
    PICKAXE(2),
    SHOVEL(4),
    AXE(6),
    HOE(8),
    SAW(10),
    HARDHAMMER(12),
    SOFTMALLET(14),
    @Deprecated
    SOFTHAMMER(14),
    WRENCH(16),
    FILE(18),
    CROWBAR(20),
    SCREWDRIVER(22),
    MORTAR(24),
    WIRECUTTER(26),
    SCOOP(28),
    BRANCHCUTTER(30),
    UNIVERSALSPADE(32),
    KNIFE(34),
    BUTCHERYKNIFE(36),
    @Deprecated
    SICKLE(38),
    SENSE(40),
    PLOW(42),
    PLUNGER(44),
    ROLLING_PIN(46),
    DRILL_LV(100),
    DRILL_MV(102),
    DRILL_HV(104),
    CHAINSAW_LV(110),
    CHAINSAW_MV(112),
    CHAINSAW_HV(114),
    WRENCH_LV(120),
    WRENCH_MV(122),
    WRENCH_HV(124),
    JACKHAMMER(130),
    BUZZSAW_LV(140),
    @Deprecated
    BUZZSAW(140),
    BUZZSAW_MV(142),
    BUZZSAW_HV(144),
    SCREWDRIVER_LV(150),
    SCREWDRIVER_MV(152),
    SCREWDRIVER_HV(154),
    SOLDERING_IRON_LV(160),
    SOLDERING_IRON_MV(162),
    SOLDERING_IRON_HV(164),
    TURBINE_SMALL(170),
    TURBINE(172),
    TURBINE_LARGE(174),
    TURBINE_HUGE(176),
    @Deprecated
    TURBINE_BLADE(178),
    POCKET_MULTITOOL(180),
    POCKET_BRANCHCUTTER(182),
    POCKET_FILE(184),
    POCKET_KNIFE(186),
    POCKET_SAW(188),
    POCKET_SCREWDRIVER(190),
    POCKET_WIRECUTTER(192);

    public final int ID;

    IDMetaTool01(int ID) {
        this.ID = ID;
    }
}

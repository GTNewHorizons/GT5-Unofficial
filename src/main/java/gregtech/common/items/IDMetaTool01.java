package gregtech.common.items;

public enum IDMetaTool01 {

    // Please pretty please, add your entries while conserving the order
    // Reserved, see the note above.
    SAW(10),
    // Reserved, see SOFTMALLET below.
    HARDHAMMER(12),
    // Tools that moved to their own items (see gregtech.common.items.tools.GTToolItems) keep their ids reserved:
    // the Postea migration in PosteaTransformers matches old saved stacks by them, so they must never be reused.
    SOFTMALLET(14),
    WRENCH(16),
    // Reserved, see the note above.
    FILE(18),
    // Reserved, see SOFTMALLET above.
    CROWBAR(20),
    // Reserved, see SOFTMALLET above.
    SCREWDRIVER(22),
    // Reserved, see the note above.
    MORTAR(24),
    // Reserved, see SOFTMALLET above.
    WIRECUTTER(26),
    // Reserved, see the note above.
    SCOOP(28),
    // Reserved, see the note above.
    BRANCHCUTTER(30),
    UNIVERSALSPADE(32),
    // Reserved, see the note above.
    KNIFE(34),
    // Reserved, see the note above.
    BUTCHERYKNIFE(36),
    // Reserved, see the note above.
    PLUNGER(44),
    // Reserved, see the note above.
    ROLLING_PIN(46),
    DRILL_LV(100),
    DRILL_MV(102),
    DRILL_HV(104),
    CHAINSAW_LV(110),
    CHAINSAW_MV(112),
    CHAINSAW_HV(114),
    // Reserved, see WRENCH above.
    WRENCH_LV(120),
    WRENCH_MV(122),
    WRENCH_HV(124),
    JACKHAMMER_LV(130),
    JACKHAMMER_MV(132),
    JACKHAMMER_HV(134),
    BUZZSAW_LV(140),
    BUZZSAW_MV(142),
    BUZZSAW_HV(144),
    // Reserved, see SOFTMALLET above.
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
    POCKET_MULTITOOL(180),
    POCKET_BRANCHCUTTER(182),
    POCKET_FILE(184),
    POCKET_KNIFE(186),
    POCKET_SAW(188),
    POCKET_SCREWDRIVER(190),
    POCKET_WIRECUTTER(192),
    // Reserved, see the note above.
    TROWEL(194),
    // Reserved, see SOFTMALLET above.
    WIRECUTTER_LV(196),
    WIRECUTTER_MV(198),
    WIRECUTTER_HV(200),
    // Reserved, see the note above.
    FILE_LV(202),
    FILE_MV(204),
    FILE_HV(206);

    public final int ID;

    IDMetaTool01(int ID) {
        this.ID = ID;
    }
}

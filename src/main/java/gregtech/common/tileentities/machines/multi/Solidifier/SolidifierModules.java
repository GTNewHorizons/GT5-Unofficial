package gregtech.common.tileentities.machines.multi.Solidifier;

public enum SolidifierModules {

    UNSET("Unset", "Unset", ""),
    ACTIVE_TIME_DILATION_SYSTEM("Active Time Dilation System", "A.T.D.S", "atds"),
    EFFICIENT_OC("Efficient Overclocking System", "E.O.C", "eff_oc"),
    POWER_EFFICIENT_SUBSYSTEMS("Power Efficient Subsytems", "P.E.S", "power_efficient_subsystems"),
    TRANSCENDENT_REINFORCEMENT("Transcendent Reinforcement", "TrRe", "transcendent_reinforcement"),
    EXTRA_CASTING_BASINS("Extra Casting Basins", "E.C.B", "extra_casting_basins"),
    HYPERCOOLER("Hypercooler", "HC", "hypercooler"),
    STREAMLINED_CASTERS("Streamlined Casters", "S.L.C", "streamlined_casters");

    public final String displayName;
    public final String shorthand;
    public final String structureID;

    private SolidifierModules(String display, String shortname, String structid) {
        this.displayName = display;
        this.shorthand = shortname;
        this.structureID = structid;
    }

    public static SolidifierModules getModule(int ordinal)
    {
        return values()[ordinal];
    }

    public static int size()
    {
        return values().length;
    }
}

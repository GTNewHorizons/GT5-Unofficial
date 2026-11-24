package gregtech.api.enums;

public class MaintenanceIssues {

    public static final int WRENCH = 1 << 0;
    public static final int SCREWDRIVER = 1 << 1;
    public static final int SOFT_MALLET = 1 << 2;
    public static final int HARD_HAMMER = 1 << 3;
    public static final int SOLDERING_TOOL = 1 << 4;
    public static final int CROWBAR = 1 << 5;
    public static final int ALL_ISSUES = WRENCH | SCREWDRIVER | SOFT_MALLET | HARD_HAMMER | SOLDERING_TOOL | CROWBAR;
}

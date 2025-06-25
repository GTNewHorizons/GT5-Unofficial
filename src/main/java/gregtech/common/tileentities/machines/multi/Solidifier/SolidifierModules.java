package gregtech.common.tileentities.machines.multi.Solidifier;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.ItemList;

public enum SolidifierModules {

    UNSET("Unset", "Unset", "", ItemList.Display_ITS_FREE.get(1)),
    ACTIVE_TIME_DILATION_SYSTEM("Active Time Dilation System", "A.T.D.S", "atds",
        ItemList.Active_Time_Dilation_System_Solidifier_Modular.get(0)),
    EFFICIENT_OC("Efficient Overclocking System", "E.O.C", "eff_oc",
        ItemList.Efficient_Overclocking_Solidifier_Modular.get(1)),
    POWER_EFFICIENT_SUBSYSTEMS("Power Efficient Subsytems", "P.E.S", "power_efficient_subsystems",
        ItemList.Power_Efficient_Subsystems_Solidifier_Modular.get(1)),
    TRANSCENDENT_REINFORCEMENT("Transcendent Reinforcement", "T.R", "transcendent_reinforcement",
        ItemList.Transcendent_Reinforcement_Solidifier_Modular.get(1)),
    EXTRA_CASTING_BASINS("Extra Casting Basins", "E.C.B", "extra_casting_basins",
        ItemList.Extra_Casting_Basins_Solidifier_Modular.get(1)),
    HYPERCOOLER("Hypercooler", "H.C", "hypercooler", ItemList.Hypercooler_Solidifier_Modular.get(1)),
    STREAMLINED_CASTERS("Streamlined Casters", "S.L.C", "streamlined_casters",
        ItemList.Streamlined_Casters_Solidifier_Modular.get(1));

    public final String displayName;
    public final String shorthand;
    public final String structureID;
    private final ItemStack icon;

    // declaring it once here, instead of on every call
    private static final SolidifierModules[] lookupArray = values();

    private SolidifierModules(String display, String shortname, String structid, ItemStack icon) {
        this.displayName = display;
        this.shorthand = shortname;
        this.structureID = structid;
        this.icon = icon;
    }

    public static SolidifierModules getModule(int ordinal) {
        return lookupArray[ordinal];
    }

    public static int size() {
        return lookupArray.length;
    }

    public ItemStack getItemIcon() {
        return this.icon;
    }
}

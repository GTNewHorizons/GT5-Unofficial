package gregtech.api.enums;

import static gregtech.api.enums.GTValues.V;

public class TierEU {

    // Do NOT use these for crafting recipes as they are exactly 1A! Use RECIPE_ULV etc.
    public static final long ULV = V[VoltageIndex.ULV];
    public static final long LV = V[VoltageIndex.LV];
    public static final long MV = V[VoltageIndex.MV];
    public static final long HV = V[VoltageIndex.HV];
    public static final long EV = V[VoltageIndex.EV];
    public static final long IV = V[VoltageIndex.IV];
    public static final long LuV = V[VoltageIndex.LuV];
    public static final long ZPM = V[VoltageIndex.ZPM];
    public static final long UV = V[VoltageIndex.UV];
    public static final long UHV = V[VoltageIndex.UHV];
    public static final long UEV = V[VoltageIndex.UEV];
    public static final long UIV = V[VoltageIndex.UIV];
    public static final long UMV = V[VoltageIndex.UMV];
    public static final long UXV = V[VoltageIndex.UXV];
    public static final long MAX = V[VoltageIndex.MAX];

    // Use me for recipes.
    public static final long RECIPE_ULV = GTValues.VP[VoltageIndex.ULV];
    public static final long RECIPE_LV = GTValues.VP[VoltageIndex.LV];
    public static final long RECIPE_MV = GTValues.VP[VoltageIndex.MV];
    public static final long RECIPE_HV = GTValues.VP[VoltageIndex.HV];
    public static final long RECIPE_EV = GTValues.VP[VoltageIndex.EV];
    public static final long RECIPE_IV = GTValues.VP[VoltageIndex.IV];
    public static final long RECIPE_LuV = GTValues.VP[VoltageIndex.LuV];
    public static final long RECIPE_ZPM = GTValues.VP[VoltageIndex.ZPM];
    public static final long RECIPE_UV = GTValues.VP[VoltageIndex.UV];
    public static final long RECIPE_UHV = GTValues.VP[VoltageIndex.UHV];
    public static final long RECIPE_UEV = GTValues.VP[VoltageIndex.UEV];
    public static final long RECIPE_UIV = GTValues.VP[VoltageIndex.UIV];
    public static final long RECIPE_UMV = GTValues.VP[VoltageIndex.UMV];
    public static final long RECIPE_UXV = GTValues.VP[VoltageIndex.UXV];
    public static final long RECIPE_MAX = GTValues.VP[VoltageIndex.MAX];
}

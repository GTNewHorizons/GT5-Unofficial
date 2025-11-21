package gregtech.api.items.armor.behaviors;

import net.minecraft.util.StatCollector;

import gregtech.api.util.GTUtility;

public enum BehaviorName {

    NightVision,
    CreativeFlight,
    InertiaCanceling,
    OmniMovement,
    Jetpack,
    JetpackHover,
    JetpackPerfectHover,
    FireImmunity,
    StepAssist,
    GogglesOfRevealing,
    HazmatProtection,
    Apiarist,
    SwimSpeed,
    KnockbackResistance,
    SpeedBoost,
    JumpBoost,
    FallProtection,
    InfiniteEnergy,
    ForceField,
    WaterBreathing,
    VisDiscount,
    SpaceSuit,
    //
    ;

    public boolean hasDisplayName() {
        return StatCollector.canTranslate("GT5U.armor.behavior." + name());
    }

    public String getDisplayName() {
        return GTUtility.translate("GT5U.armor.behavior." + name());
    }
}

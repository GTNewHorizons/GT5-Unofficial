package gregtech.api.items.armor.behaviors;

import net.minecraft.item.EnumRarity;
import net.minecraft.util.StatCollector;

import gregtech.api.util.GTUtility;

/// Represents a category of [IArmorBehavior]. Primarily used to check behavior equality and presence. Behaviors have a
/// separate set of rarities from augments because they're completely decoupled from augments. Any part can provide a
/// behavior, so they need to have their own rarities defined.
public enum BehaviorName {

    NightVision(EnumRarity.common),
    CreativeFlight(EnumRarity.epic),
    InertiaCanceling(EnumRarity.rare),
    OmniMovement(EnumRarity.rare),
    Jetpack(EnumRarity.uncommon),
    JetpackHover(EnumRarity.uncommon),
    JetpackPerfectHover(EnumRarity.rare),
    FireImmunity(EnumRarity.common),
    StepAssist(EnumRarity.common),
    GogglesOfRevealing(EnumRarity.uncommon),
    HazmatProtection(EnumRarity.common),
    Apiarist(EnumRarity.uncommon),
    SwimSpeed(EnumRarity.common),
    KnockbackResistance(EnumRarity.uncommon),
    SpeedBoost(EnumRarity.common),
    JumpBoost(EnumRarity.common),
    FallProtection(EnumRarity.uncommon),
    InfiniteEnergy(EnumRarity.epic),
    ForceField(EnumRarity.epic),
    WaterBreathing(EnumRarity.uncommon),
    VisDiscount(EnumRarity.uncommon),
    SpaceSuit(EnumRarity.rare),
    //
    ;

    private final EnumRarity rarity;

    BehaviorName(EnumRarity rarity) {
        this.rarity = rarity;
    }

    public EnumRarity getRarity() {
        return rarity;
    }

    public boolean hasDisplayName() {
        return StatCollector.canTranslate("GT5U.armor.behavior." + name());
    }

    public String getDisplayName() {
        return "§s" + rarity.rarityColor + GTUtility.translate("GT5U.armor.behavior." + name()) + "§t";
    }
}

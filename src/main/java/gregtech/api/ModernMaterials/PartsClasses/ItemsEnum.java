package gregtech.api.ModernMaterials.PartsClasses;

import java.util.HashSet;

import net.minecraft.item.Item;

import gregtech.api.ModernMaterials.ModernMaterial;

public enum ItemsEnum implements IEnumPart {

    HotIngot("Hot % Ingot"),
    Ingot("% Ingot"),
    // DoubleIngot("Double % Ingot"),
    // TripleIngot("Triple % Ingot"),
    // QuadrupleIngot("Quadruple % Ingot"),
    // QuintupleIngot("Quintuple % Ingot"),
    Nugget("% Nugget"),

    // Gears
    Gear("% Gear"),
    SmallGear("Small % Gear"),

    // Plates
    DensePlate("Dense % Plate"),
    Foil("% Foil"),
    Plate("% Plate"),
    // DoublePlate("Double % Plate"),
    // TriplePlate("Triple % Plate"),
    // QuadruplePlate("Quadruple % Plate"),
    // QuintuplePlate("Quintuple % Plate"),

    // Gems.
    Lens("% Lens"),
    ExquisiteGem("Exquisite %"),
    FlawlessGem("Flawless %"),
    Gem("% Gem"),
    FlawedGem("Flawed %"),
    ChippedGem("Chipped %"),

    // Misc.
    Ring("% Ring"),
    Rotor("% Rotor"),
    Round("% Round"),

    LongRod("% Long Rod"),
    Rod("% Rod"),
    Bolt("% Bolt"),
    Screw("% Screw"),

    TurbineBlade("% Turbine Blade"),
    FineWire("Fine % Wire"),

    // Springs
    Spring("% Spring"),
    SmallSpring("Small % Spring"),

    // Dusts.
    ImpureDust("Impure % Dust"),
    PurifiedDust("Purified % Dust"),
    Dust("% Dust"),
    SmallDust("Small % Dust"),
    TinyDust("Tiny % Dust"),

    // Ore stuff.
    CrushedCentrifugedOre("Centrifuged % Ore"),
    PurifiedOre("Purified % Ore"),
    CrushedOre("Crushed % Ore"),
    Nanites("% Nanites"),

    // Tool Parts.
    // ArrowHead("% Arrow Head"),
    // AxeHead("% Axe Head"),
    BuzzSaw("% Buzz Saw"),
    ChainSaw("% Chainsaw"),
    DrillTip("% Drill Tip"),
    FileHead("% File Head"),
    HammerHead("% Hammer Head"),
    // HoeHead("% Hoe Head"),
    MalletHead("% Mallet Head"),
    // PickaxeHead("% Pickaxe Head"),
    // PlowHead("% Plow Head"),
    SawBlade("% Saw Blade"),
    ScrewdriverHead("% Screwdriver Head"),
    // SenseHead("% Sense Head"),
    // ShovelHead("% Shovel Head"),
    SolderingHead("% Soldering Head"),
    // SwordBlade("% Sword Blade"),
    ElectricWrenchHead("% Electric Wrench Head");

    public final String partName;

    private Item item;

    private final HashSet<ModernMaterial> associatedMaterials = new HashSet<>();

    ItemsEnum(final String partName) {
        this.partName = partName;
    }

    @Override
    public void setAssociatedItem(final Item item) {
        this.item = item;
    }

    @Override
    public Item getItem() {
        return item;
    }

    @Override
    public HashSet<ModernMaterial> getAssociatedMaterials() {
        return associatedMaterials;
    }

    @Override
    public void addAssociatedMaterial(final ModernMaterial modernMaterial) {
        associatedMaterials.add(modernMaterial);
    }
}

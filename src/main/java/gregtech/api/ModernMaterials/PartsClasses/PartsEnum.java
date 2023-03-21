package gregtech.api.ModernMaterials.PartsClasses;

import net.minecraft.item.Item;

public enum PartsEnum implements IGetItem{

    // Ingots
    HotIngot("Hot % Ingot"),
    Ingot("% Ingot"),
    // DoubleIngot("Double % Ingot"),
    // TripleIngot("Triple % Ingot"),
    // QuadrupleIngot("Quadruple % Ingot"),
    // QuintupleIngot("Quintuple % Ingot"),

    // Gears
    Gear("% Gear"),
    SmallGear("Small % Gear"),

    // Plates
    DensePlate("Dense % Plate"),
    Plate("% Plate"),
    // DoublePlate("Double % Plate"),
    // TriplePlate("Triple % Plate"),
    // QuadruplePlate("Quadruple % Plate"),
    // QuintuplePlate("Quintuple % Plate"),

    // Gems.
    Gem("% Gem"),
    ChippedGem("Chipped %"),
    FlawedGem("Flawed %"),
    FlawlessGem("Flawless %"),
    ExquisiteGem("Exquisite %"),

    // Misc.
    Foil("% Foil"),
    Lens("% Lens"),
    Nugget("% Nugget"),
    Ring("% Ring"),
    Rotor("% Rotor"),
    Round("% Round"),
    Screw("% Screw"),
    Bolt("% Bolt"),

    Rod("% Rod"),
    LongRod("% Long Rod"),
    TurbineBlade("% Turbine Blade"),
    FineWire("Fine % Wire"),

    // Springs
    Spring("% Spring"),
    SmallSpring("Small % Spring"),

    // Dusts.
    TinyDust("Tiny % Dust"),
    SmallDust("Small % Dust"),
    Dust("% Dust"),

    // Ore stuff.
    CrushedOre("Crushed % Ore"),
    CrushedCentrifugedOre("Centrifuged % Ore"),
    PurifiedOre("Purified % Ore"),
    ImpureDust("Impure % Dust"),
    PurifiedDust("Purified % Dust"),

    // Tool Parts.
    ArrowHead("% Arrow Head"),
    AxeHead("% Axe Head"),
    BuzzSaw("% Buzz Saw"),
    ChainSaw("% Chainsaw"),
    DrillTip("% Drill Tip"),
    FileHead("% File Head"),
    HammerHead("% Hammer Head"),
    HoeHead("% Hoe Head"),
    MalletHead("% Mallet Head"),
    PickaxeHead("% Pickaxe Head"),
    PlowHead("% Plow Head"),
    SawBlade("% Saw Blade"),
    ScrewdriverHead("% Screwdriver Head"),
    SenseHead("% Sense Head"),
    ShovelHead("% Shovel Head"),
    SolderingHead("% Soldering Head"),
    SwordBlade("% Sword Blade"),
    ElectricWrenchHead("% Electric Wrench Head");

    PartsEnum(final String partName) {
        this.partName = partName;
    }

    public final String partName;

    private Item item;

    @Override
    public void setAssociatedItem(Item item) {
        this.item = item;
    }

    @Override
    public Item getItem() {
        return item;
    }
}

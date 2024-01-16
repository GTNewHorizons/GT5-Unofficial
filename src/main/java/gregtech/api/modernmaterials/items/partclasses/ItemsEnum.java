package gregtech.api.modernmaterials.items.partclasses;

import java.util.HashSet;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

import gregtech.api.modernmaterials.ModernMaterial;

public enum ItemsEnum implements IEnumPart {

    HotIngot(1),
    Ingot(1),
    DoubleIngot(2.0),
    TripleIngot(3.0),
    QuadrupleIngot(4.0),
    QuintupleIngot(5.0),
    Nugget(1.0 / 9.0),

    // Gears
    Gear(4),
    SmallGear(1),

    // Plates
    DensePlate(9),
    Foil(1.0 / 4.0),

    Plate(1),
    DoublePlate(2),
    TriplePlate(3),
    QuadruplePlate(4),
    QuintuplePlate(5),

    // Gems.
    Lens(1),
    ExquisiteGem(9),
    FlawlessGem(3),
    Gem(1),
    FlawedGem(1.0 / 3.0),
    ChippedGem(1.0 / 9.0),

    // Misc.
    Ring(1.0 / 4.0),
    Rotor(612.0 / 144.0),
    Round(9),

    LongRod(1),
    Rod(1 / 2.0),
    Bolt(1 / 4.0),
    Screw(1 / 8.0),

    TurbineBlade(6),
    FineWire(1 / 8.0),
    ItemCasing(),

    // Springs
    Spring(1),
    SmallSpring(1 / 4.0),

    // Dusts.
    ImpureDust(1),
    PurifiedDust(1),
    Dust(1),
    SmallDust(1 / 4.0),
    TinyDust(1 / 9.0),

    // Ore stuff.
    CrushedCentrifugedOre(),
    PurifiedCrushedOre(),
    CrushedOre(),
    Nanites(),

    // Tool Parts.
    MalletHandle(),
    ArrowHead(),
    AxeHead(),
    BuzzSawHead(),
    ChainSawHead(),
    DrillHead(),
    FileHead(),
    HammerHead(),
    HoeHead(),
    MalletHead(),
    PickaxeHead(),
    PlowHead(),
    SawHead(),
    ScrewdriverHead(),
    SenseHead(),
    ShovelHead(),
    SolderingHead(),
    SwordHead(),
    ElectricWrenchHead();

    public double percentageOfIngot;

    private final HashSet<ModernMaterial> associatedMaterials = new HashSet<>();

    ItemsEnum(double percentageOfIngot) {
        this.percentageOfIngot = percentageOfIngot;
    }

    ItemsEnum() {}
    @Override
    public @NotNull ItemStack getPart(@NotNull ModernMaterial material, int stackSize) {
        return new ItemStack(getItem(), stackSize, material.getMaterialID());
    }

    private Item item;

    public Item getItem() {
        return item;
    }

    public void setItem(Item setItem) {
        if (this.item != null) throw new RuntimeException(this + " already has a set item.");
        this.item = setItem;
    }

    public String getUnlocalizedNameForItem() {
        return this.toString();
    }

    public String getLocalizedName(ModernMaterial material) {
        return StatCollector.translateToLocalFormatted("gt.part.item." + this, material.getLocalizedName());
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

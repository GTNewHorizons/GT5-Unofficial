package gregtech.api.modernmaterials.items.partclasses;

import java.util.HashSet;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

import gregtech.api.modernmaterials.ModernMaterial;

public enum ItemsEnum implements IEnumPart {

    HotIngot("ingotHot", 1),
    Ingot("ingot", 1),
    DoubleIngot("ingotDouble", 2.0),
    TripleIngot("ingotTriple", 3.0),
    QuadrupleIngot("ingotQuadruple", 4.0),
    QuintupleIngot("ingotQuintuple", 5.0),
    Nugget("nugget", 1.0 / 9.0),

    // Gears
    Gear("gearGt", 4),
    SmallGear("gearGtSmall", 1),

    // Plates
    DensePlate("plateDense", 9),
    Foil("foil", 1.0 / 4.0),

    Plate("plate", 1),
    DoublePlate("plateDouble", 2),
    TriplePlate("plateTriple", 3),
    QuadruplePlate("plateQuadruple", 4),
    QuintuplePlate("plateQuintuple", 5),

    // Gems.
    Lens("lens", 1),
    ExquisiteGem("gemExquisite", 9),
    FlawlessGem("gemFlawless", 3),
    Gem("gem", 1),
    FlawedGem("gemFlawed", 1.0 / 3.0),
    ChippedGem("gemChipped", 1.0 / 9.0),

    // Misc.
    Ring("ring", 1.0 / 4.0),
    Rotor("rotor", 612.0 / 144.0),
    Round("round", 9),

    LongRod("stickLong", 1),
    Rod("stick", 1 / 2.0),
    Bolt("bolt", 1 / 4.0),
    Screw("screw", 1 / 8.0),

    TurbineBlade("turbineBlade", 6),
    FineWire("wireFine", 1 / 8.0),
    ItemCasing("itemCasing"),

    // Springs
    Spring("spring", 1),
    SmallSpring("springSmall", 1 / 4.0),

    // Dusts.
    ImpureDust("dustImpure", 1),
    PurifiedDust("dustPure", 1),
    Dust("dust", 1),
    SmallDust("dustSmall", 1 / 4.0),
    TinyDust("dustTiny", 1 / 9.0),

    // Ore stuff.
    CrushedCentrifugedOre("crushedCentrifuged"),
    PurifiedOre("crushedPurified"),
    CrushedOre("crushed"),
    Nanites("nanite"),

    // Tool Parts.
    MalletHandle("handleMallet"),
    ArrowHead("toolHeadArrow"),
    AxeHead("toolHeadAxe"),
    BuzzSawHead("toolHeadBuzzSaw"),
    ChainSawHead("toolHeadChainsaw"),
    DrillHead("toolHeadDrill"),
    FileHead("toolHeadFile"),
    HammerHead("toolHeadHammer"),
    HoeHead("toolHeadHoe"),
    MalletHead("toolHeadMallet"),
    PickaxeHead("toolHeadPickaxe"),
    PlowHead("toolHeadPlow"),
    SawHead("toolHeadSaw"),
    ScrewdriverHead("toolHeadScrewdriver"),
    SenseHead("toolHeadSense"),
    ShovelHead("toolHeadShovel"),
    SolderingHead("toolHeadSoldering"),
    SwordHead("toolHeadSword"),
    ElectricWrenchHead("toolHeadWrench");

    public final String partName;

    public double percentageOfIngot;

    private final HashSet<ModernMaterial> associatedMaterials = new HashSet<>();

    ItemsEnum(final String partName, double percentageOfIngot) {
        this.partName = partName;
        this.percentageOfIngot = percentageOfIngot;
    }

    ItemsEnum(final String partName) {
        this.partName = partName;
    }

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
        return partName;
    }

    public String getLocalizedName(ModernMaterial material) {
        return StatCollector.translateToLocalFormatted("gt.part.item." + partName, material.getLocalizedName());
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

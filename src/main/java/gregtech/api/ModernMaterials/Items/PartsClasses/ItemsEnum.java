package gregtech.api.ModernMaterials.Items.PartsClasses;

import java.util.HashMap;
import java.util.HashSet;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ModernMaterials.ModernMaterial;

public enum ItemsEnum implements IEnumPart {

    HotIngot("Hot % Ingot", 1),
    Ingot("% Ingot", 1),
    // DoubleIngot("Double % Ingot"),
    // TripleIngot("Triple % Ingot"),
    // QuadrupleIngot("Quadruple % Ingot"),
    // QuintupleIngot("Quintuple % Ingot"),
    Nugget("% Nugget", 1 / 9.0),

    // Gears
    Gear("% Gear", 4),
    SmallGear("Small % Gear", 1),

    // Plates
    DensePlate("Dense % Plate", 9),
    Foil("% Foil", 4),
    Plate("% Plate", 1),
    // DoublePlate("Double % Plate"),
    // TriplePlate("Triple % Plate"),
    // QuadruplePlate("Quadruple % Plate"),
    // QuintuplePlate("Quintuple % Plate"),

    // Gems.
    Lens("% Lens", 1),
    ExquisiteGem("Exquisite %", 9),
    FlawlessGem("Flawless %", 3),
    Gem("% Gem", 1),
    FlawedGem("Flawed %", 1 / 3.0),
    ChippedGem("Chipped %", 1 / 9.0),

    // Misc.
    Ring("% Ring", 1 / 4.0),
    Rotor("% Rotor", 612 / 144.0),
    Round("% Round", 9),

    LongRod("% Long Rod", 1),
    Rod("% Rod", 1 / 2.0),
    Bolt("% Bolt", 1 / 4.0),
    Screw("% Screw", 1 / 8.0),

    TurbineBlade("% Turbine Blade", 6),
    FineWire("Fine % Wire", 1 / 8.0),

    // Springs
    Spring("% Spring", 1),
    SmallSpring("Small % Spring", 1 / 4.0),

    // Dusts.
    ImpureDust("Impure % Dust", 1),
    PurifiedDust("Purified % Dust", 1),
    Dust("% Dust", 1),
    SmallDust("Small % Dust", 1 / 4.0),
    TinyDust("Tiny % Dust", 1 / 9.0),

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
    ItemCasing("% Item Casing"),
    ScrewdriverHead("% Screwdriver Head"),
    // SenseHead("% Sense Head"),
    // ShovelHead("% Shovel Head"),
    SolderingHead("% Soldering Head"),
    // SwordBlade("% Sword Blade"),
    ElectricWrenchHead("% Electric Wrench Head");

    public final String partName;

    public double percentageOfIngot;

    private final HashSet<ModernMaterial> associatedMaterials = new HashSet<>();
    private final HashMap<ModernMaterial, ItemStack> itemHashMap = new HashMap<>();

    ItemsEnum(final String partName, double percentageOfIngot) {
        this.partName = partName;
        this.percentageOfIngot = percentageOfIngot;
    }

    ItemsEnum(final String partName) {
        this.partName = partName;
    }

    @Override
    public @NotNull ItemStack getPart(@NotNull ModernMaterial material, int stackSize) {

        final ItemStack itemStack = itemHashMap.get(material);

        return new ItemStack(itemStack.getItem(), stackSize, itemStack.getItemDamage());
    }

    @Override
    public void setItemStack(@NotNull ModernMaterial material, ItemStack itemStack) {
        itemHashMap.put(material, itemStack);
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

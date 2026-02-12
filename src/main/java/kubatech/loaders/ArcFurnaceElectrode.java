package kubatech.loaders;

import static gregtech.client.GTTooltipHandler.Tier.HV;
import static gregtech.client.GTTooltipHandler.Tier.IV;
import static gregtech.client.GTTooltipHandler.Tier.LV;
import static gregtech.client.GTTooltipHandler.Tier.LuV;
import static gregtech.client.GTTooltipHandler.Tier.MV;
import static gregtech.client.GTTooltipHandler.Tier.UEV;
import static gregtech.client.GTTooltipHandler.Tier.UHV;
import static gregtech.client.GTTooltipHandler.Tier.UIV;
import static gregtech.client.GTTooltipHandler.Tier.UXV;
import static gregtech.client.GTTooltipHandler.Tier.ZPM;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import net.minecraft.item.ItemStack;

import gregtech.api.util.GTUtility;
import gregtech.client.GTTooltipHandler;
import kubatech.api.arcfurnace.ArcFurnaceContext;

public enum ArcFurnaceElectrode {

    GraphiteElectrode(LV, 1, 4, 2d, 4d, 100, 0.03d, 1, 0, 0.5d),
    TantalumElectrode(MV, 1.2d, 2, 4d, 4d, 300, 0.01d, 1.2d, 3, 0.1d),
    MolybdenumElectrode(HV, 0.9d, 16, 3d, 4d, 200, 0.01d, 0.8d, 5, 0),
    TungstenElectrode(IV, 1, 128, 1d, 4d, 500, 0.04d, 1.1d, 3, 2),
    TungstenSteelElectrode(IV, 0.8d, 160, 1d, 4d, 700, 0.05d, 1.2d, 4, 1),
    GrapheneElectrode(IV, 2.5d, 4, 2d, 4d, 350, 0, 1, 0, 0.5d),
    YBCOElectrode(LuV, 2d, 8, 6d, 4d, 500, 0, 0.7d, 1.2d, 0.3d),
    NetheriteElectrode(LuV, 1.6d, 16, 1d, 4d, 600, 0, 1.3d, 2d, 0.2d),
    TritaniumElectrode(ZPM, 3d, 48, 2d, 4d, 600, 0, 1.7d, 5d, 0.1d),
    InfinityElectrode(UHV, 4.2d, 0, 3d, 4d, 800, 0.02d, 2.4d, 3d, 0d),
    HypogenElectrode(UEV, 6.5d, 256, 1d, 4d, 1000, 0.02d, 1.5d, 3.5d, 0.1d),

    // nanite eletrodes
    NeutroniumNaniteElectrode(UHV, 5d, 64, 2d, 4d, 1000, 0.03d, 4d, 5d, 0.5d),
    TranscendentNaniteElectrode(UIV, 7.5d, 512, 4d, 4d, 1000, 0.01d, 3.5d, 2d, 0.1d),
    UniversiumNaniteElectrode(UXV, 10d, 1024, 8d, 4d, 1000, 0d, 2d, 1d, 0d),

    ;

    private static final HashMap<Integer, ArcFurnaceElectrode> ID_MAP = new HashMap<>();

    public final int id;
    public final GTTooltipHandler.Tier tier;
    public final double speedModifier;
    public final int parallelLimit;
    public final double OCSpeedFactor;
    public final double OCPowerFactor;
    public final int durability;
    public final double failChance;
    public final double amperagePerParallel;
    public final double startupSurge;
    public final double inertGasDurabilityModifier;
    public final Function<ArcFurnaceContext, Boolean> specialEffect;

    private ItemStack electrodeItem;
    private ItemStack electrodeSetItem;

    ArcFurnaceElectrode(GTTooltipHandler.Tier tier, double speedModifier, int parallelLimit, double OCSpeedFactor,
        double OCPowerFactor, int durability, double failChance, double amperagePerParallel, double startupSurge,
        double inertGasDurabilityModifier) {
        this(
            tier,
            speedModifier,
            parallelLimit,
            OCSpeedFactor,
            OCPowerFactor,
            durability,
            failChance,
            amperagePerParallel,
            startupSurge,
            inertGasDurabilityModifier,
            null);
    }

    ArcFurnaceElectrode(GTTooltipHandler.Tier tier, double speedModifier, int parallelLimit, double OCSpeedFactor,
        double OCPowerFactor, int durability, double failChance, double amperagePerParallel, double startupSurge,
        double inertGasDurabilityModifier, Function<ArcFurnaceContext, Boolean> specialEffect) {
        this.id = this.ordinal();
        this.tier = tier;
        this.speedModifier = speedModifier;
        this.parallelLimit = parallelLimit;
        this.OCSpeedFactor = OCSpeedFactor;
        this.OCPowerFactor = OCPowerFactor;
        this.durability = durability;
        this.failChance = failChance;
        this.amperagePerParallel = amperagePerParallel;
        this.startupSurge = startupSurge;
        this.inertGasDurabilityModifier = inertGasDurabilityModifier;
        this.specialEffect = specialEffect;
    }

    public static void registerElectrodes() {
        for (ArcFurnaceElectrode electrode : values()) {
            ID_MAP.put(electrode.id, electrode);
            GTTooltipHandler.registerTieredTooltip(electrode.getElectrodeItem(1), electrode.tier);
            GTTooltipHandler.registerTieredTooltip(electrode.getElectrodeSetItem(1), electrode.tier);
            electrode.electrodeItem = ArcFurnaceLoader.ARC_FURNACE_ELECTRODE.getElectrodeStack(electrode);
            electrode.electrodeSetItem = ArcFurnaceLoader.ARC_FURNACE_ELECTRODE.getElectrodeSetStack(electrode);
        }
    }

    public ItemStack getElectrodeItem(int amount) {
        return GTUtility.copyAmount(amount, this.electrodeItem);
    }

    public ItemStack getElectrodeSetItem(int amount) {
        return GTUtility.copyAmount(amount, this.electrodeSetItem);
    }

    public static ArcFurnaceElectrode getById(int id) {
        return ID_MAP.get(id);
    }

    public String getUnlocalizedName() {
        return "item.arc_furnace_electrode." + this.name();
    }

    public String getSetUnlocalizedName() {
        return "item.arc_furnace_electrode_set." + this.name();
    }

    public void addInformation(List<String> tooltip) {
        tooltip.add("Speed Modifier: " + this.speedModifier + "x");
        tooltip.add("Parallel Limit: " + (this.parallelLimit == 0 ? "None" : this.parallelLimit));
        tooltip.add("Overclock Speed Factor: " + this.OCSpeedFactor + "x");
        tooltip.add("Overclock Power Factor: " + this.OCPowerFactor + "x");
        tooltip.add("Durability: " + this.durability);
        tooltip.add("Fail Chance: " + (this.failChance * 100) + "%");
        tooltip.add("Amperage per Parallel: " + this.amperagePerParallel + "A");
        tooltip.add("Startup Surge: " + (this.startupSurge * 100) + "%");
        tooltip.add("Inert Gas Durability Modifier: " + (this.inertGasDurabilityModifier * 100) + "%");
    }

}

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
import static kubatech.loaders.ArcFurnaceLoader.ARC_FURNACE_ELECTRODE;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import net.minecraft.item.ItemStack;

import gregtech.api.util.GTUtility;
import gregtech.client.GTTooltipHandler;
import kubatech.api.arcfurnace.ArcFurnaceContext;

public enum ArcFurnaceElectrode {

    GraphiteElectrode(LV, 1, 4, 2d, 4d, 100, 1, 0),
    TantalumElectrode(MV, 1.2d, 2, 4d, 4d, 300, 1.2d, 3),
    MolybdenumElectrode(HV, 0.9d, 16, 3d, 4d, 200, 0.8d, 5),
    TungstenElectrode(IV, 1, 128, 1d, 4d, 500, 1.1d, 3),
    TungstenSteelElectrode(IV, 0.8d, 160, 1d, 4d, 700, 1.2d, 4),
    GrapheneElectrode(IV, 2.5d, 4, 2d, 4d, 350, 1, 0),
    YBCOElectrode(LuV, 2d, 8, 6d, 4d, 500, 0.7d, 1.2d),
    NetheriteElectrode(LuV, 1.6d, 16, 1d, 4d, 600, 1.3d, 2d),
    TritaniumElectrode(ZPM, 3d, 48, 2d, 4d, 600, 1.7d, 5d),
    InfinityElectrode(UHV, 4.2d, 0, 3d, 4d, 800, 2.4d, 3d),
    HypogenElectrode(UEV, 6.5d, 256, 1d, 4d, 1000, 1.5d, 3.5d),

    // nanite eletrodes
    NeutroniumNaniteElectrode(UHV, 5d, 64, 2d, 4d, 1000, 4d, 5d),
    TranscendentNaniteElectrode(UIV, 7.5d, 512, 4d, 4d, 1000, 3.5d, 2d),
    UniversiumNaniteElectrode(UXV, 10d, 1024, 8d, 4d, 1000, 2d, 1d),

    ;

    private static final HashMap<Integer, ArcFurnaceElectrode> ID_MAP = new HashMap<>();

    public final int id;
    public final GTTooltipHandler.Tier tier;
    public final double speedModifier;
    public final int parallelLimit;
    public final double OCSpeedFactor;
    public final double OCPowerFactor;
    public final int durability;
    public final double amperagePerParallel;
    public final double startupSurge;
    public final Function<ArcFurnaceContext, Boolean> specialEffect;

    private ItemStack electrodeItem;

    ArcFurnaceElectrode(GTTooltipHandler.Tier tier, double speedModifier, int parallelLimit, double OCSpeedFactor,
        double OCPowerFactor, int durability, double amperagePerParallel, double startupSurge) {
        this(
            tier,
            speedModifier,
            parallelLimit,
            OCSpeedFactor,
            OCPowerFactor,
            durability,
            amperagePerParallel,
            startupSurge,
            null);
    }

    ArcFurnaceElectrode(GTTooltipHandler.Tier tier, double speedModifier, int parallelLimit, double OCSpeedFactor,
        double OCPowerFactor, int durability, double amperagePerParallel, double startupSurge,
        Function<ArcFurnaceContext, Boolean> specialEffect) {
        this.id = this.ordinal();
        this.tier = tier;
        this.speedModifier = speedModifier;
        this.parallelLimit = parallelLimit;
        this.OCSpeedFactor = OCSpeedFactor;
        this.OCPowerFactor = OCPowerFactor;
        this.durability = durability;
        this.amperagePerParallel = amperagePerParallel;
        this.startupSurge = startupSurge;
        this.specialEffect = specialEffect;
    }

    public static void registerElectrodes() {
        for (ArcFurnaceElectrode electrode : values()) {
            ID_MAP.put(electrode.id, electrode);
            GTTooltipHandler.registerTieredTooltip(electrode.getElectrodeItem(1), electrode.tier);
            electrode.electrodeItem = ARC_FURNACE_ELECTRODE.getElectrodeStack(electrode);
        }
    }

    public ItemStack getElectrodeItem(int amount) {
        return GTUtility.copyAmount(amount, this.electrodeItem);
    }

    public static ArcFurnaceElectrode getById(int id) {
        return ID_MAP.get(id);
    }

    public static ArcFurnaceElectrode getByStack(ItemStack stack) {
        return ARC_FURNACE_ELECTRODE.getElectrodeFromStack(stack);
    }

    public String getUnlocalizedName() {
        return "item.arc_furnace_electrode." + this.name();
    }

    public void addInformation(List<String> tooltip) {
        tooltip.add("Speed Modifier: " + this.speedModifier + "x");
        tooltip.add("Parallel Limit: " + (this.parallelLimit == 0 ? "None" : this.parallelLimit));
        tooltip.add("OC Speed Factor: " + this.OCSpeedFactor + "x");
        tooltip.add("OC Power Factor: " + this.OCPowerFactor + "x");
        tooltip.add("Durability: " + this.durability);
        tooltip.add("Amperage per Parallel: " + this.amperagePerParallel + "A");
        tooltip.add("Startup Surge: " + (this.startupSurge * 100) + "%");
    }

}

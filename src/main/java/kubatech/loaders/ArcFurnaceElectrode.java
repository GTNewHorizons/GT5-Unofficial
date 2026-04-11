package kubatech.loaders;

import static kubatech.loaders.ArcFurnaceLoader.ARC_FURNACE_ELECTRODE;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import gregtech.api.enums.Materials;
import gregtech.api.interfaces.IOreMaterial;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.material.MaterialsElements;
import kubatech.api.arcfurnace.ArcFurnaceProcessingEvent;

public enum ArcFurnaceElectrode {

    GraphiteElectrode(Materials.Graphite, 1, 4, 2d, 4d, 100, 1, 0, e -> {}),
    TantalumElectrode(Materials.Tantalum, 1.2d, 2, 4d, 4d, 300, 1.2d, 3),
    MolybdenumElectrode(Materials.Molybdenum, 0.9d, 16, 3d, 4d, 200, 0.8d, 5),
    TungstenElectrode(Materials.Tungsten, 1, 128, 1d, 4d, 500, 1.1d, 2),
    TungstenSteelElectrode(Materials.TungstenSteel, 0.8d, 160, 1d, 4d, 700, 1.2d, 3),
    GrapheneElectrode(Materials.Graphene, 2.5d, 4, 2d, 4d, 350, 1, 0, e -> {}),
    YBCOElectrode(Materials.YttriumBariumCuprate, 1.2d, 8, 6d, 4d, 400, 0.8d, 2d, event -> {
        if (event instanceof ArcFurnaceProcessingEvent.EventPostRecipeCheck postRecipe) {
            if (!postRecipe.result.wasSuccessful()) return;
            int performedOC = postRecipe.calculator.getPerformedOverclocks();
            postRecipe.arcFurnace.setDurabilityConsumptionThisRun(
                postRecipe.arcFurnace.getDurabilityConsumptionThisRun() * (1 + performedOC));
        }
    }),
    NetheriteElectrode(Materials.Netherite, 2.2d, 32, 1.5d, 4d, 600, 1.3d, 10d, event -> {
        if (event instanceof ArcFurnaceProcessingEvent.EventStartShutdown startShutdown) {
            startShutdown.duration = 1;
        }
        if (event instanceof ArcFurnaceProcessingEvent.EventStartIgnition startIgnition) {
            if (event.arcFurnace.getTotalRunTime() - event.arcFurnace.getLastWorkingTick() < 20 * 4) {
                startIgnition.duration = 1;
                startIgnition.eut = 1;
            }
        }
    }),
    TritaniumElectrode(Materials.Tritanium, 3d, 48, 2d, 4d, 600, 1.7d, 4d),
    InfinityElectrode(Materials.Infinity, 4.2d, 0, 1d, 4d, 800, 1d, 1d, event -> {
        if (event instanceof ArcFurnaceProcessingEvent.EventConfigureProcessing configure) {
            configure.processingLogic.setMaxParallel(getInfinityTargetParallel(configure.arcFurnace.getEffectState()));
            return;
        }
        if (event instanceof ArcFurnaceProcessingEvent.EventCreateParallelHelper parallelHelperEvent) {
            parallelHelperEvent.parallelHelper.setAvailableEUt(Long.MAX_VALUE);
            return;
        }
        if (event instanceof ArcFurnaceProcessingEvent.EventConfigureOverclock configureOC) {
            configureOC.overclockCalculator.setAmperage(1L);
            configureOC.overclockCalculator.setEUt(Long.MAX_VALUE);
            return;
        }
        if (event instanceof ArcFurnaceProcessingEvent.EventPostRecipeCheck afterRecipe) {
            if (!afterRecipe.processingPhase || !afterRecipe.result.wasSuccessful()) return;
            int actualParallel = Math.max(1, afterRecipe.helper.getCurrentParallel());
            int targetParallel = getInfinityTargetParallel(afterRecipe.arcFurnace.getEffectState());
            int chargedParallel = Math.max(actualParallel, targetParallel);
            if (actualParallel < targetParallel) afterRecipe.eut = (afterRecipe.eut / chargedParallel) * actualParallel;
            return;
        }
        if (event instanceof ArcFurnaceProcessingEvent.EventRunCompleted completed) {
            NBTTagCompound state = completed.arcFurnace.getEffectState();
            int next = getInfinityTargetParallel(state);
            next = next > Integer.MAX_VALUE / 2 ? Integer.MAX_VALUE : next * 2;
            state.setInteger("infinityTargetParallel", next);
            return;
        }
        if (event instanceof ArcFurnaceProcessingEvent.EventReset reset) {
            reset.arcFurnace.getEffectState()
                .setInteger("infinityTargetParallel", 1);
        }
    }),
    HypogenElectrode(MaterialsElements.STANDALONE.HYPOGEN, 6.5d, 256, 1d, 4d, 1000, 1.5d, 3.5d),

    // nanite eletrodes
    NeutroniumNaniteElectrode(Materials.Neutronium, 5d, 64, 2d, 4d, 275, 4d, 5d, event -> {
        if (event instanceof ArcFurnaceProcessingEvent.EventRunCompleted completed) {
            if (completed.arcFurnace.depleteInputAndUpdate(Materials.Neutronium.getDust(1))
                && completed.arcFurnace.getRandomNumber(100) < 70) {
                completed.arcFurnace.setDurabilityConsumptionThisRun(0);
            }
        }
    }),
    TranscendentNaniteElectrode(Materials.TranscendentMetal, 7.5d, 512, 4d, 4d, 500, 3.5d, 2d, event -> {
        if (event instanceof ArcFurnaceProcessingEvent.EventRunCompleted completed) {
            if (completed.arcFurnace.depleteInputAndUpdate(Materials.TranscendentMetal.getDust(1))
                && completed.arcFurnace.getRandomNumber(100) < 90) {
                completed.arcFurnace.setDurabilityConsumptionThisRun(0);
            }
        }
    }),
    UniversiumNaniteElectrode(Materials.Universium, 10d, 1024, 8d, 4d, 1000, 2d, 1d, event -> {
        if (event instanceof ArcFurnaceProcessingEvent.EventRunCompleted completed) {
            if (completed.arcFurnace.depleteInputAndUpdate(Materials.Universium.getDust(1))) {
                if (completed.arcFurnace.getRandomNumber(100) < 10) completed.arcFurnace
                    .setDurabilityConsumptionThisRun(completed.arcFurnace.getDurabilityConsumptionThisRun() * -1);
                else completed.arcFurnace.setDurabilityConsumptionThisRun(0);
            }
        }
    }),

    ;

    private static final HashMap<Integer, ArcFurnaceElectrode> ID_MAP = new HashMap<>();
    private static final String INFINITY_TARGET_PARALLEL_KEY = "infinityTargetParallel";

    public final int id;
    public final IOreMaterial associatedMaterial;
    public final double speedModifier;
    public final int parallelLimit;
    public final double OCSpeedFactor;
    public final double OCPowerFactor;
    public final int durability;
    public final double amperagePerParallel;
    public final double startupSurge;
    public final Consumer<ArcFurnaceProcessingEvent> specialEffect;

    private ItemStack electrodeItem;

    ArcFurnaceElectrode(IOreMaterial associatedMaterial, double speedModifier, int parallelLimit, double OCSpeedFactor,
        double OCPowerFactor, int durability, double amperagePerParallel, double startupSurge) {
        this(
            associatedMaterial,
            speedModifier,
            parallelLimit,
            OCSpeedFactor,
            OCPowerFactor,
            durability,
            amperagePerParallel,
            startupSurge,
            null);
    }

    ArcFurnaceElectrode(IOreMaterial associatedMaterial, double speedModifier, int parallelLimit, double OCSpeedFactor,
        double OCPowerFactor, int durability, double amperagePerParallel, double startupSurge,
        Consumer<ArcFurnaceProcessingEvent> specialEffect) {
        this.id = this.ordinal();
        this.associatedMaterial = associatedMaterial;
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

    public void addInformation(List<String> tooltip, int durability) {
        tooltip.add(
            StatCollector.translateToLocalFormatted(
                "item.arc_furnace_electrode.tip.durability",
                getModifierFormatted(durability, 100, 500, 700) + EnumChatFormatting.RESET + EnumChatFormatting.GRAY,
                getModifierFormatted(this.durability, 100, 500, 700)));
        tooltip.add(
            StatCollector.translateToLocalFormatted(
                "item.arc_furnace_electrode.tip.speed",
                getModifierFormatted(this.speedModifier)));
        tooltip.add(
            StatCollector.translateToLocalFormatted(
                "item.arc_furnace_electrode.tip.parallel_limit",
                this.parallelLimit == 0
                    ? StatCollector.translateToLocal("item.arc_furnace_electrode.tip.parallel_limit_none")
                    : getModifierFormatted(this.parallelLimit, 4, 64, 128)));
        tooltip.add(
            StatCollector.translateToLocalFormatted(
                "item.arc_furnace_electrode.tip.oc_speed_factor",
                getModifierFormatted(this.OCSpeedFactor, false, 1d, 2d, 4d, "x", "")));
        tooltip.add(
            StatCollector.translateToLocalFormatted(
                "item.arc_furnace_electrode.tip.oc_power_factor",
                getModifierFormatted(this.OCPowerFactor, true, 0.25d, 0.5d, 1d, "x", "")));
        tooltip.add(
            StatCollector.translateToLocalFormatted(
                "item.arc_furnace_electrode.tip.amperage_per_parallel",
                getModifierFormatted(this.amperagePerParallel, true, 0.6d, 1d, 1.25d, "", "A")));
        tooltip.add(
            StatCollector.translateToLocalFormatted(
                "item.arc_furnace_electrode.tip.startup_surge",
                getModifierFormatted(this.startupSurge * 100, false, 0, 100, 400, "", "%")));
        if (specialEffect != null) {
            tooltip.addAll(
                Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(
                    StatCollector.translateToLocal("item.arc_furnace_electrode.tip.special_effect." + this.name()),
                    300));
        }
    }

    private static int getInfinityTargetParallel(NBTTagCompound state) {
        int parallel = Math.max(1, state.getInteger(INFINITY_TARGET_PARALLEL_KEY));
        state.setInteger(INFINITY_TARGET_PARALLEL_KEY, parallel);
        return parallel;
    }

    private static String getModifierFormatted(int value, int lo_threshold, int mid_threshold, int hi_threshold) {
        EnumChatFormatting color;
        if (value <= lo_threshold) {
            color = EnumChatFormatting.DARK_RED; // "bad" stat
        } else if (value < mid_threshold) {
            color = EnumChatFormatting.RED; // "below average" stat
        } else if (value == mid_threshold) {
            color = EnumChatFormatting.GOLD; // "average" stat
        } else if (value <= hi_threshold) {
            color = EnumChatFormatting.GREEN; // "above average" stat
        } else {
            color = EnumChatFormatting.AQUA; // "great" stat
        }
        return color.toString() + value;
    }

    private static String getModifierFormatted(double value) {
        return getModifierFormatted(value, false, 0.5d, 1.0d, 2.0d, "x", "");
    }

    private static String getModifierFormatted(double value, boolean undesireable, double lo_threshold,
        double mid_threshold, double hi_threshold, String prefix, String suffix) {
        EnumChatFormatting color;
        double discriminant = value;
        if (undesireable) {
            discriminant = 1.0d / value;
        }
        if (discriminant <= lo_threshold) {
            color = EnumChatFormatting.DARK_RED; // "bad" stat
        } else if (discriminant < mid_threshold) {
            color = EnumChatFormatting.RED; // "below average" stat
        } else if (discriminant == mid_threshold) {
            color = EnumChatFormatting.GOLD; // "average" stat
        } else if (discriminant <= hi_threshold) {
            color = EnumChatFormatting.GREEN; // "above average" stat
        } else {
            color = EnumChatFormatting.AQUA; // "great" stat
        }
        String formatted = color.toString();

        formatted += prefix;

        // trim trailing zeroes if no decimal-precision is needed
        if (value == (int) value) {
            return formatted + (int) value + suffix;
        }
        return formatted + value + suffix;
    }

}

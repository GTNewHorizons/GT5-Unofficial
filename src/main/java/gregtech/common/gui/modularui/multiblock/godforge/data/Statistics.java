package gregtech.common.gui.modularui.multiblock.godforge.data;

import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import tectech.thing.metaTileEntity.multi.godforge.MTEBaseModule;
import tectech.thing.metaTileEntity.multi.godforge.util.ForgeOfGodsData;
import tectech.thing.metaTileEntity.multi.godforge.util.GodforgeMath;
import tectech.thing.metaTileEntity.multi.godforge.util.MilestoneFormatter;

public enum Statistics {

    HEAT("gt.blockmachines.multimachine.FOG.heat", "fog.text.tooltip.heat", 0),
    EFFECTIVE_HEAT("gt.blockmachines.multimachine.FOG.effectiveheat", "fog.text.tooltip.effectiveheat", 1),
    PARALLEL("gt.blockmachines.multimachine.FOG.parallel", "fog.text.tooltip.parallel", 2),
    SPEED_BONUS("gt.blockmachines.multimachine.FOG.speedbonus", "fog.text.tooltip.speedbonus", 3),
    ENERGY_DISCOUNT("gt.blockmachines.multimachine.FOG.energydiscount", "fog.text.tooltip.energydiscount", 4),
    OC_DIVISOR("gt.blockmachines.multimachine.FOG.ocdivisor", "fog.text.tooltip.ocdivisor", 5),
    PROCESSING_VOLTAGE("gt.blockmachines.multimachine.FOG.processingvoltage", "fog.text.tooltip.processingvoltage", 6);

    private final String key;
    private final String tooltipKey;
    public final int displayIndex;

    Statistics(String key, String tooltipKey, int displayIndex) {
        this.key = key;
        this.tooltipKey = tooltipKey;
        this.displayIndex = displayIndex;
    }

    @Override
    public String toString() {
        return EnumChatFormatting.GOLD + translateToLocal(key);
    }

    public String tooltip() {
        return StatCollector.translateToLocal(tooltipKey);
    }

    // todo: test if this works with upgrades when synced, it works fine with preview fuel
    public String calculate(MTEBaseModule module, int fuelFactor, ForgeOfGodsData data,
        Formatters format) {
        String relevantInfo = "report me :(";
        if (data == null) return relevantInfo;

        relevantInfo = switch (this) {
            case HEAT -> {
                GodforgeMath.calculateMaxHeatForModules(module, data, fuelFactor);
                yield String.valueOf(format.format(module.getHeat()));
            }
            case EFFECTIVE_HEAT -> {
                GodforgeMath.calculateMaxHeatForModules(module, data, fuelFactor);
                yield String.valueOf(format.format(module.getHeatForOC()));
            }
            case PARALLEL -> {
                GodforgeMath.calculateMaxParallelForModules(module, data, fuelFactor);
                yield String.valueOf(format.format(module.getMaxParallel()));
            }
            case SPEED_BONUS -> {
                GodforgeMath.calculateSpeedBonusForModules(module, data);
                yield String.valueOf(format.format(module.getSpeedBonus()));
            }
            case ENERGY_DISCOUNT -> {
                GodforgeMath.calculateEnergyDiscountForModules(module, data);
                yield String.valueOf(format.format(module.getEnergyDiscount()));
            }
            case OC_DIVISOR -> {
                GodforgeMath.setMiscModuleParameters(module, data);
                yield String.valueOf(format.format(module.getOverclockTimeFactor()));
            }
            case PROCESSING_VOLTAGE -> {
                GodforgeMath.calculateProcessingVoltageForModules(module, data, fuelFactor);
                yield String.valueOf(MilestoneFormatter.EXPONENT.format((module.getProcessingVoltage())));
            }
        };
        return relevantInfo;
    }

}

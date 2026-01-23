package gregtech.common.gui.modularui.multiblock.godforge.data;

import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import gregtech.api.util.GTUtility;
import tectech.thing.metaTileEntity.multi.godforge.MTEBaseModule;
import tectech.thing.metaTileEntity.multi.godforge.util.ForgeOfGodsData;
import tectech.thing.metaTileEntity.multi.godforge.util.GodforgeMath;

public enum Statistics {

    HEAT("gt.blockmachines.multimachine.FOG.heat", "fog.text.tooltip.heat"),
    EFFECTIVE_HEAT("gt.blockmachines.multimachine.FOG.effectiveheat", "fog.text.tooltip.effectiveheat"),
    PARALLEL("gt.blockmachines.multimachine.FOG.parallel", "fog.text.tooltip.parallel"),
    SPEED_BONUS("gt.blockmachines.multimachine.FOG.speedbonus", "fog.text.tooltip.speedbonus"),
    ENERGY_DISCOUNT("gt.blockmachines.multimachine.FOG.energydiscount", "fog.text.tooltip.energydiscount"),
    OC_DIVISOR("gt.blockmachines.multimachine.FOG.ocdivisor", "fog.text.tooltip.ocdivisor"),
    PROCESSING_VOLTAGE("gt.blockmachines.multimachine.FOG.processingvoltage", "fog.text.tooltip.processingvoltage");

    private final String key;
    private final String tooltipKey;

    Statistics(String key, String tooltipKey) {
        this.key = key;
        this.tooltipKey = tooltipKey;
    }

    @Override
    public String toString() {
        return EnumChatFormatting.GOLD + translateToLocal(key);
    }

    public String tooltip() {
        return StatCollector.translateToLocal(tooltipKey);
    }

    public String calculate(MTEBaseModule module, int fuelFactor, ForgeOfGodsData data, Formatters format) {
        return switch (this) {
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
                yield String.valueOf(format.format(module.getCalculatedMaxParallel()));
            }
            case SPEED_BONUS -> {
                GodforgeMath.calculateSpeedBonusForModules(module, data);
                yield String.valueOf(formatNumber(module.getSpeedBonus()));
            }
            case ENERGY_DISCOUNT -> {
                GodforgeMath.calculateEnergyDiscountForModules(module, data);
                yield String.valueOf(formatNumber(module.getEnergyDiscount()));
            }
            case OC_DIVISOR -> {
                GodforgeMath.setMiscModuleParameters(module, data);
                yield String.valueOf(formatNumber(module.getOverclockTimeFactor()));
            }
            case PROCESSING_VOLTAGE -> {
                GodforgeMath.calculateProcessingVoltageForModules(module, data, fuelFactor);
                yield String.valueOf(Formatters.EXPONENT.format((module.getProcessingVoltage())));
            }
        };
    }
}

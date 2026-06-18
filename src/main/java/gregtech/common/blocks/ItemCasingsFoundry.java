package gregtech.common.blocks;

import static gregtech.common.tileentities.machines.multi.foundry.FoundryTooltipValues.coolingStrOrder;
import static gregtech.common.tileentities.machines.multi.foundry.FoundryTooltipValues.createFoundryFlavorText;
import static gregtech.common.tileentities.machines.multi.foundry.FoundryTooltipValues.createModuleBaseText;
import static gregtech.common.tileentities.machines.multi.foundry.FoundryTooltipValues.createModuleLimitText;
import static gregtech.common.tileentities.machines.multi.foundry.FoundryTooltipValues.createTierLine;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import gregtech.api.util.tooltip.TooltipHelper;
import gregtech.api.util.tooltip.TooltipTier;

public class ItemCasingsFoundry extends ItemCasings {

    public ItemCasingsFoundry(Block block) {
        super(block);
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List<String> tooltip, boolean aF3_H) {
        switch (aStack.getItemDamage()) {
            case 1 -> {
                tooltip.add(createTierLine(10));
                tooltip.add(createFoundryFlavorText("gt.blockmachines.multimachine.foundry.chassis1.flavor"));
            }
            case 2 -> {
                tooltip.add(createTierLine(11));
                tooltip.add(createFoundryFlavorText("gt.blockmachines.multimachine.foundry.chassis2.flavor"));
            }
            case 3 -> {
                tooltip.add(createTierLine(13));
                tooltip.add(createFoundryFlavorText("gt.blockmachines.multimachine.foundry.chassis3.flavor"));
            }
            case 4 -> {
                tooltip.add(createModuleBaseText());
                tooltip.add(createModuleLimitText());
                tooltip.add(
                    StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.foundry.universalcollapser.tooltip1",
                        TooltipHelper.SPEED_COLOR));
                tooltip.add(
                    StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.foundry.universalcollapser.tooltip2"));
                tooltip.add(
                    StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.foundry.universalcollapser.tooltip3"));
                tooltip.add(
                    StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.foundry.universalcollapser.tooltip4",
                        TooltipHelper.SPEED_COLOR));
                tooltip.add(
                    StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.foundry.universalcollapser.tooltip5"));
                tooltip.add(createTierLine(13));
                tooltip.add(createFoundryFlavorText("gt.blockmachines.multimachine.foundry.universalcollapser.flavor"));
            }
            case 5 -> {
                tooltip.add(createModuleBaseText());
                tooltip.add(createModuleLimitText());
                tooltip.add(
                    StatCollector
                        .translateToLocalFormatted("gt.blockmachines.multimachine.foundry.efficientoc.tooltip1"));
                tooltip.add(
                    StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.foundry.efficientoc.tooltip2",
                        TooltipHelper.SPEED_COLOR));
                tooltip.add(
                    StatCollector
                        .translateToLocalFormatted("gt.blockmachines.multimachine.foundry.efficientoc.tooltip3"));
                tooltip.add(
                    StatCollector
                        .translateToLocalFormatted("gt.blockmachines.multimachine.foundry.efficientoc.tooltip4"));

                tooltip.add(createTierLine(12));
                tooltip.add(createFoundryFlavorText("gt.blockmachines.multimachine.foundry.efficientoc.flavor"));
            }
            case 6 -> {
                tooltip.add(createModuleBaseText());
                tooltip.add(
                    StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.foundry.powerefficient.tooltip1",
                        TooltipHelper.EFF_COLOR));
                tooltip.add(
                    StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.foundry.powerefficient.tooltip2",
                        TooltipHelper.EFF_COLOR));
                tooltip.add(
                    StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.foundry.powerefficient.tooltip3",
                        TooltipHelper.EFF_COLOR));
                tooltip.add(
                    StatCollector
                        .translateToLocalFormatted("gt.blockmachines.multimachine.foundry.powerefficient.tooltip4"));
                tooltip.add(createTierLine(10));
                tooltip.add(createFoundryFlavorText("gt.blockmachines.multimachine.foundry.powerefficient.flavor"));
            }
            case 7 -> {
                tooltip.add(createModuleBaseText());
                tooltip.add(
                    StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.foundry.heliocastreinforcement.tooltip1"));
                tooltip.add(
                    StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.foundry.heliocastreinforcement.tooltip2"));
                tooltip.add(
                    StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.foundry.heliocastreinforcement.tooltip3"));
                tooltip.add(
                    StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.foundry.heliocastreinforcement.tooltip4",
                        TooltipHelper.SPEED_COLOR));
                tooltip.add(
                    StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.foundry.heliocastreinforcement.tooltip5",
                        TooltipHelper.EFF_COLOR));
                tooltip.add(
                    StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.foundry.heliocastreinforcement.tooltip6"));
                tooltip.add(
                    StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.foundry.heliocastreinforcement.tooltip7",
                        TooltipHelper.PARALLEL_COLOR,
                        TooltipHelper.TIER_COLOR));
                tooltip.add(
                    StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.foundry.heliocastreinforcement.tooltip8"));
                tooltip.add(
                    StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.foundry.heliocastreinforcement.tooltip9"));
                tooltip.add(
                    StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.foundry.heliocastreinforcement.tooltip10"));
                tooltip.add(createTierLine(11));
                tooltip.add(
                    createFoundryFlavorText("gt.blockmachines.multimachine.foundry.heliocastreinforcement.flavor"));
            }
            case 8 -> {
                tooltip.add(createModuleBaseText());
                tooltip.add(
                    StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.foundry.parallel.tooltip1",
                        TooltipTier.VOLTAGE.getValue()));
                tooltip.add(StatCollector.translateToLocal("gt.blockmachines.multimachine.foundry.parallel.tooltip2"));
                tooltip.add(
                    StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.foundry.parallel.tooltip3",
                        TooltipTier.VOLTAGE.getValue()));
                tooltip.add(StatCollector.translateToLocal("gt.blockmachines.multimachine.foundry.parallel.tooltip4"));
                tooltip.add(createTierLine(10));
                tooltip.add(createFoundryFlavorText("gt.blockmachines.multimachine.foundry.parallel.flavor"));
            }
            case 9 -> {
                tooltip.add(createModuleBaseText());
                tooltip.add(createModuleLimitText());
                tooltip
                    .add(StatCollector.translateToLocal("gt.blockmachines.multimachine.foundry.hypercooler.tooltip1"));
                tooltip.add(
                    StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.foundry.hypercooler.tooltip2",
                        coolingStrOrder("100", "50"),
                        coolingStrOrder("Super Coolant", "Spacetime"),
                        coolingStrOrder("1", "2")));
                tooltip
                    .add(StatCollector.translateToLocal("gt.blockmachines.multimachine.foundry.hypercooler.tooltip3"));
                tooltip
                    .add(StatCollector.translateToLocal("gt.blockmachines.multimachine.foundry.hypercooler.tooltip4"));
                tooltip
                    .add(StatCollector.translateToLocal("gt.blockmachines.multimachine.foundry.hypercooler.tooltip5"));
                tooltip
                    .add(StatCollector.translateToLocal("gt.blockmachines.multimachine.foundry.hypercooler.tooltip6"));
                tooltip.add(createTierLine(11));
                tooltip.add(createFoundryFlavorText("gt.blockmachines.multimachine.foundry.hypercooler.flavor"));
            }
            case 10 -> {
                tooltip.add(createModuleBaseText());
                tooltip.add(
                    StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.foundry.streamlinedcasters.tooltip1",
                        TooltipHelper.SPEED_COLOR));
                tooltip.add(
                    StatCollector.translateToLocalFormatted(
                        "gt.blockmachines.multimachine.foundry.streamlinedcasters.tooltip2",
                        TooltipHelper.SPEED_COLOR));
                tooltip.add(
                    StatCollector
                        .translateToLocal("gt.blockmachines.multimachine.foundry.streamlinedcasters.tooltip3"));
                tooltip.add(createTierLine(10));
                tooltip.add(createFoundryFlavorText("gt.blockmachines.multimachine.foundry.streamlinedcasters.flavor"));
            }
        }
    }

    // copied methods so I can avoid a public static in MTEExoFoundry class

}

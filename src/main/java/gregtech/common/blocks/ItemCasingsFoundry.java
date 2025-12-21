package gregtech.common.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import gregtech.api.util.GTUtility;
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
                tooltip.add(createFoundryFlavorText("gt.blockmachines.multimachine.foundry.flavor.chassis1"));
            }
            case 2 -> {
                tooltip.add(createTierLine(11));
                tooltip.add(createFoundryFlavorText("gt.blockmachines.multimachine.foundry.flavor.chassis2"));
            }
            case 3 -> {
                tooltip.add(createTierLine(13));
                tooltip.add(createFoundryFlavorText("gt.blockmachines.multimachine.foundry.flavor.chassis3"));
            }
            case 4 -> {
                tooltip.add(createModuleBaseText());
                tooltip.add(createModuleLimitText());
                tooltip.add("- Multiplies Speed by " + TooltipHelper.SPEED_COLOR + "2x");
                tooltip.add("- Multiplies EU Consumption by " + EnumChatFormatting.RED + "4x");
                tooltip.add("- Reduces Hatch Space by " + EnumChatFormatting.GOLD + "20");
                tooltip.add(
                    "- Multiplies Speed by an additional " + TooltipHelper.SPEED_COLOR
                        + "2x "
                        + EnumChatFormatting.GRAY
                        + "and EU Consumption by an additional "
                        + EnumChatFormatting.RED
                        + "2x "
                        + EnumChatFormatting.GRAY
                        + "when");
                tooltip.add(
                    "   paired with the " + EnumChatFormatting.AQUA
                        + "Hypercooler "
                        + EnumChatFormatting.GRAY
                        + "module");
                tooltip.add(createTierLine(13));
                tooltip.add(createFoundryFlavorText("gt.blockmachines.multimachine.foundry.flavor.timedilation"));
            }
            case 5 -> {
                tooltip.add(createModuleBaseText());
                tooltip.add(createModuleLimitText());
                tooltip.add("- Increases Overclock Factor by " + EnumChatFormatting.LIGHT_PURPLE + "0.35");
                tooltip.add(
                    "- This means that each overclock provides an additional " + TooltipHelper.speedText("35%")
                        + " speed");
                tooltip.add(
                    "- Increases Overclock Factor by an additional " + EnumChatFormatting.LIGHT_PURPLE
                        + "0.1 "
                        + EnumChatFormatting.GRAY
                        + "when");
                tooltip.add(
                    "   paired with the " + EnumChatFormatting.GREEN
                        + "Power Efficient Subsystems "
                        + EnumChatFormatting.GRAY
                        + "module");
                tooltip.add(createTierLine(12));
                tooltip.add(createFoundryFlavorText("gt.blockmachines.multimachine.foundry.flavor.efficientoc"));
            }
            case 6 -> {
                tooltip.add(createModuleBaseText());
                tooltip.add("- Subtracts " + TooltipHelper.effText("10%") + " from Initial EU Cost");
                tooltip.add("- Multiplies EU cost by " + TooltipHelper.effText("0.8x"));
                tooltip.add("- Subtracts an additional " + TooltipHelper.effText("50%") + " from Initial EU Cost when");
                tooltip.add(
                    "   paired with the " + EnumChatFormatting.DARK_AQUA
                        + "Efficient Overclocking System "
                        + EnumChatFormatting.GRAY
                        + "module");
                tooltip.add(createTierLine(10));
                tooltip.add(createFoundryFlavorText("gt.blockmachines.multimachine.foundry.flavor.powerefficient"));
            }
            case 7 -> {
                tooltip.add(createModuleBaseText());
                tooltip.add(
                    "- Allows for " + EnumChatFormatting.LIGHT_PURPLE
                        + "UIV+ Recipes"
                        + EnumChatFormatting.GRAY
                        + " to be processed");
                tooltip.add(
                    "- Grants benefits when paired with other " + EnumChatFormatting.LIGHT_PURPLE
                        + "Harmonic Reinforcement "
                        + EnumChatFormatting.GRAY
                        + "modules");
                tooltip.add(EnumChatFormatting.WHITE + "- 2+ Modules:");
                tooltip.add("   - Increases Base Speed by " + TooltipHelper.speedText("75%") + " per module");
                tooltip.add("   - Subtracts " + TooltipHelper.effText("10%") + " from Initial EU Cost per module");
                tooltip.add(EnumChatFormatting.WHITE + "- 3+ Modules:");
                tooltip.add(
                    "   - Adds " + EnumChatFormatting.GOLD
                        + "6"
                        + EnumChatFormatting.GRAY
                        + " Parallels per "
                        + TooltipTier.VOLTAGE.getValue()
                        + EnumChatFormatting.GRAY
                        + " tier per module");
                tooltip.add("   - Increases Overclock Factor by " + EnumChatFormatting.LIGHT_PURPLE + "0.1");
                tooltip.add(EnumChatFormatting.WHITE + "- 4 Modules:");
                tooltip.add("   - Grants 2 extra " + EnumChatFormatting.LIGHT_PURPLE + "overclocks");
                tooltip.add(createTierLine(11));
                tooltip
                    .add(createFoundryFlavorText("gt.blockmachines.multimachine.foundry.flavor.harmonicreinforcement"));
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
                tooltip.add(createFoundryFlavorText("gt.blockmachines.multimachine.foundry.flavor.parallel"));
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
                tooltip.add(createFoundryFlavorText("gt.blockmachines.multimachine.foundry.flavor.hypercooler"));
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
                tooltip.add(createFoundryFlavorText("gt.blockmachines.multimachine.foundry.flavor.streamlinedcasters"));
            }
        }
    }

    private String createTierLine(int tier) {
        return StatCollector.translateToLocalFormatted(
            "gt.blockmachines.multimachine.foundry.tier",
            GTUtility.getColoredTierNameFromTier((byte) tier));
    }

    // copied methods so I can avoid a public static in MTEExoFoundry class
    private String coolingStrOrder(String val1, String val2) {
        return EnumChatFormatting.BLUE + val1
            + EnumChatFormatting.GRAY
            + "/"
            + EnumChatFormatting.LIGHT_PURPLE
            + val2
            + EnumChatFormatting.GRAY;
    }

    private String createModuleBaseText() {
        return EnumChatFormatting.GOLD
            + StatCollector.translateToLocal("gt.blockmachines.multimachine.foundry.basemodule");
    }

    private String createModuleLimitText() {
        return StatCollector.translateToLocal("gt.blockmachines.multimachine.foundry.modulelimit");
    }

    private String createFoundryFlavorText(String key) {
        return EnumChatFormatting.RED + StatCollector.translateToLocal(key);
    }
}

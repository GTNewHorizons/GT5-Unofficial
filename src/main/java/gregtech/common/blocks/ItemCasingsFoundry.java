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

    private final static String moduleBaseText = EnumChatFormatting.GOLD
        + StatCollector.translateToLocal("gt.blockmachines.multimachine.foundry.basemodule");
    private final static String moduleLimitText = "Limit of " + EnumChatFormatting.WHITE
        + "1"
        + EnumChatFormatting.GRAY
        + " Per "
        + EnumChatFormatting.GOLD
        + "Foundry";

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List<String> tooltip, boolean aF3_H) {

        switch (aStack.getItemDamage()) {
            case 1 -> {
                tooltip.add(createTierLine(10));
                tooltip.add(createFoundryFlavorText("gt.blockmachines.multimachine.foundry.chassis1"));
            }
            case 2 -> {
                tooltip.add(createTierLine(11));
                tooltip.add(createFoundryFlavorText("gt.blockmachines.multimachine.foundry.chassis2"));
            }
            case 3 -> {
                tooltip.add(createTierLine(13));
                tooltip.add(createFoundryFlavorText("gt.blockmachines.multimachine.foundry.chassis3"));
            }
            case 4 -> {
                tooltip.add(moduleBaseText);
                tooltip.add(moduleLimitText);
                tooltip.add("Multiplies Speed by " + TooltipHelper.SPEED_COLOR + "4x");
                tooltip.add("Multiplies EU Consumption by " + EnumChatFormatting.RED + "8x");
                tooltip.add("Reduces Hatch Space by " + EnumChatFormatting.GOLD + "20");
                tooltip.add(createTierLine(13));
                tooltip.add(createFoundryFlavorText("gt.blockmachines.multimachine.foundry.timedilation"));
            }
            case 5 -> {
                tooltip.add(moduleBaseText);
                tooltip.add(moduleLimitText);
                tooltip.add("Increases Overclock Factor by " + EnumChatFormatting.LIGHT_PURPLE + "0.25");
                tooltip.add(
                    "This means that each overclock provides an additional " + TooltipHelper.speedText("25%")
                        + " speed");
                tooltip.add(createTierLine(12));
                tooltip.add(createFoundryFlavorText("gt.blockmachines.multimachine.foundry.efficientoc"));
            }
            case 6 -> {
                tooltip.add(moduleBaseText);
                tooltip.add("Subtracts " + TooltipHelper.effText("10%") + " from Initial EU Cost");
                tooltip.add("Multiplies EU cost by " + TooltipHelper.effText("0.8x"));
                tooltip.add(createTierLine(10));
                tooltip.add(createFoundryFlavorText("gt.blockmachines.multimachine.foundry.powerefficient"));
            }
            case 7 -> {
                tooltip.add(moduleBaseText);
                tooltip.add(moduleLimitText);
                tooltip.add(
                    "Allows for " + EnumChatFormatting.LIGHT_PURPLE
                        + "UIV+ Recipes"
                        + EnumChatFormatting.GRAY
                        + " to be processed");
                tooltip.add(createTierLine(11));
                tooltip.add(createFoundryFlavorText("gt.blockmachines.multimachine.foundry.harmonicreinforcement"));
            }
            case 8 -> {
                tooltip.add(moduleBaseText);
                tooltip.add(
                    "Adds " + EnumChatFormatting.GOLD
                        + "12"
                        + EnumChatFormatting.GRAY
                        + " Parallels per "
                        + TooltipTier.VOLTAGE.getValue()
                        + EnumChatFormatting.GRAY
                        + " tier");
                tooltip.add("Increases Hatch Space by " + EnumChatFormatting.GOLD + "36");
                tooltip.add(createTierLine(10));

                tooltip.add(createFoundryFlavorText("gt.blockmachines.multimachine.foundry.parallel"));
            }
            case 9 -> {
                tooltip.add(moduleBaseText);
                tooltip.add(moduleLimitText);
                tooltip.add(
                    "Consumes " + EnumChatFormatting.AQUA
                        + "Cooling Fluid"
                        + EnumChatFormatting.GRAY
                        + " for "
                        + EnumChatFormatting.LIGHT_PURPLE
                        + "Extra Overclocks");
                tooltip.add(
                    "Drains " + coolingStrOrder("100", "50", "25")
                        + " L/s of "
                        + coolingStrOrder("Super Coolant", "Spacetime", "Eternity")
                        + " to gain "
                        + coolingStrOrder("1", "2", "3")
                        + " Maximum Overclocks");
                tooltip.add(
                    "Only drains " + EnumChatFormatting.AQUA
                        + "cooling fluid"
                        + EnumChatFormatting.GRAY
                        + " when the "
                        + EnumChatFormatting.GOLD
                        + "Foundry"
                        + EnumChatFormatting.GRAY
                        + " is active");
                tooltip.add(
                    EnumChatFormatting.DARK_AQUA + "Requires an input hatch on any Hypercooler Casing to drain from!");
                tooltip.add(createTierLine(11));
                tooltip.add(createFoundryFlavorText("gt.blockmachines.multimachine.foundry.hypercooler"));
            }
            case 10 -> {
                tooltip.add(moduleBaseText);
                tooltip.add("Increases Base Speed by " + TooltipHelper.speedText("150%"));
                tooltip.add(createTierLine(10));
                tooltip.add(createFoundryFlavorText("gt.blockmachines.multimachine.foundry.speed"));
            }
        }

    }

    private String createTierLine(int tier) {
        return "Tier: " + GTUtility.getColoredTierNameFromTier((byte) tier);
    }

    // copied methods so I can avoid a public static in MTEExoFoundry class
    private String coolingStrOrder(String val1, String val2, String val3) {
        return EnumChatFormatting.BLUE + val1
            + EnumChatFormatting.GRAY
            + "/"
            + EnumChatFormatting.LIGHT_PURPLE
            + val2
            + EnumChatFormatting.GRAY
            + "/"
            + EnumChatFormatting.GREEN
            + val3
            + EnumChatFormatting.GRAY;
    }

    private String createFoundryFlavorText(String key) {
        return EnumChatFormatting.RED + StatCollector.translateToLocal(key);
    }
}

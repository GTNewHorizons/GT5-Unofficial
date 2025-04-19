package gregtech.common.pollution;

import static gregtech.api.enums.Mods.IndustrialCraft2;
import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.enums.Mods.ThaumicBases;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.enums.Mods;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;

public class PollutionTooltip {

    private static final String PRODUCES_POLLUTION_FORMAT = "GT5U.tooltip.pollution.produces";
    private static final String MULTI_POLLUTION_FORMAT = "GT5U.tooltip.pollution.produces.multiblock";

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void getTooltip(ItemTooltipEvent event) {
        if (event.itemStack == null) return;

        if (PollutionConfig.furnacesPollute) {
            String furnacePollution = StatCollector
                .translateToLocalFormatted(PRODUCES_POLLUTION_FORMAT, PollutionConfig.furnacePollutionAmount);

            // Furnace and Iron Furnace
            if (GTUtility.areStacksEqual(event.itemStack, new ItemStack(Blocks.furnace)) || GTUtility
                .areStacksEqual(event.itemStack, GTModHandler.getModItem(IndustrialCraft2.ID, "blockMachine", 1, 1))) {
                event.toolTip.add(furnacePollution);
            }

            // Alchemical Furnace
            if (Thaumcraft.isModLoaded()) {
                if (GTUtility.areStacksEqual(
                    event.itemStack,
                    GTModHandler.getModItem(Thaumcraft.ID, "blockStoneDevice", 1, 0))) {
                    event.toolTip.add(furnacePollution);
                }
            }

            // Advanced Alchemical Furnace
            if (ThaumicBases.isModLoaded()) {
                if (GTUtility.areStacksEqual(
                    event.itemStack,
                    GTModHandler.getModItem(ThaumicBases.ID, "advAlchFurnace", 1, 0))) {
                    event.toolTip.add(furnacePollution);
                }
            }
        }

        if (Railcraft.isModLoaded() && PollutionConfig.railcraftPollutes) {

            // Solid and Liquid Boiler Firebox
            if (GTUtility.areStacksEqual(event.itemStack, GTModHandler.getModItem(Railcraft.ID, "machine.beta", 1, 5))
                || GTUtility
                    .areStacksEqual(event.itemStack, GTModHandler.getModItem(Railcraft.ID, "machine.beta", 1, 6))) {
                event.toolTip.add(
                    StatCollector.translateToLocalFormatted(
                        "GT5U.tooltip.pollution.produces.firebox",
                        PollutionConfig.fireboxPollutionAmount));
            }

            // Tunnel Bore
            if (GTUtility.areStacksEqual(event.itemStack, GTModHandler.getModItem(Railcraft.ID, "cart.bore", 1, 0))) {
                event.toolTip.add(
                    StatCollector.translateToLocalFormatted(
                        PRODUCES_POLLUTION_FORMAT,
                        PollutionConfig.tunnelBorePollutionAmount));
            }

            // Coke Oven Brick
            if (GTUtility
                .areStacksEqual(event.itemStack, GTModHandler.getModItem(Railcraft.ID, "machine.alpha", 1, 7))) {
                event.toolTip.add(
                    StatCollector
                        .translateToLocalFormatted(MULTI_POLLUTION_FORMAT, PollutionConfig.cokeOvenPollutionAmount));
            }

            // Advanced Coke Oven Brick
            if (GTUtility
                .areStacksEqual(event.itemStack, GTModHandler.getModItem(Railcraft.ID, "machine.alpha", 1, 12))) {
                event.toolTip.add(
                    StatCollector.translateToLocalFormatted(
                        MULTI_POLLUTION_FORMAT,
                        PollutionConfig.advancedCokeOvenPollutionAmount));
            }

            // Hobbyist's Steam Engine
            if (GTUtility
                .areStacksEqual(event.itemStack, GTModHandler.getModItem(Railcraft.ID, "machine.beta", 1, 7))) {
                event.toolTip.add(
                    StatCollector.translateToLocalFormatted(
                        PRODUCES_POLLUTION_FORMAT,
                        PollutionConfig.hobbyistEnginePollutionAmount));
            }
        }

        // Galacticraft (and Galaxy Space) rockets
        if (Mods.GalacticraftCore.isModLoaded() && PollutionConfig.rocketsPollute
            && event.itemStack.getItem() != null) {
            String simpleName = event.itemStack.getItem()
                .getClass()
                .getSimpleName();
            // TODO I'm sure there is a better way to check the tier of a rocket....
            if (simpleName.contains("Rocket")) {
                for (char d : simpleName.toCharArray()) {
                    if (Character.isDigit(d)) {
                        int tier = Character.getNumericValue(d);
                        event.toolTip.add(
                            StatCollector.translateToLocalFormatted(
                                "GT5U.tooltip.pollution.produces.rocket.ignited",
                                (PollutionConfig.rocketPollutionAmount * tier / 100)));
                        event.toolTip.add(
                            StatCollector.translateToLocalFormatted(
                                "GT5U.tooltip.pollution.produces.rocket.flying",
                                PollutionConfig.rocketPollutionAmount * tier));
                        break;
                    }
                }
            }
        }
    }
}

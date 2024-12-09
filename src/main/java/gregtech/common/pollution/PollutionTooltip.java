package gregtech.common.pollution;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.enums.Mods;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;

public class PollutionTooltip {

    private static final String PRODUCES_POLLUTION_FORMAT = "Produces %d Pollution/Second";
    private static final String MULTI_POLLUTION_FORMAT = "A complete Multiblock produces %d Pollution/Second";

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void getTooltip(ItemTooltipEvent event) {
        if (event.itemStack == null) return;

        if (PollutionConfig.furnacesPollute) {
            String furnacePollution = String.format(PRODUCES_POLLUTION_FORMAT, PollutionConfig.furnacePollutionAmount);

            // Furnace and Iron Furnace
            if (GTUtility.areStacksEqual(event.itemStack, new ItemStack(Blocks.furnace))
                || GTUtility.areStacksEqual(event.itemStack, GTModHandler.getModItem("IC2", "blockMachine", 1, 1))) {
                event.toolTip.add(furnacePollution);
            }

            // Alchemical Furnace
            if (Mods.Thaumcraft.isModLoaded()) {
                if (GTUtility
                    .areStacksEqual(event.itemStack, GTModHandler.getModItem("Thaumcraft", "blockStoneDevice", 1, 0))) {
                    event.toolTip.add(furnacePollution);
                }
            }

            // Advanced Alchemical Furnace
            if (Mods.ThaumicBases.isModLoaded()) {
                if (GTUtility
                    .areStacksEqual(event.itemStack, GTModHandler.getModItem("thaumicbases", "advAlchFurnace", 1, 0))) {
                    event.toolTip.add(furnacePollution);
                }
            }
        }

        if (Mods.Railcraft.isModLoaded() && PollutionConfig.railcraftPollutes) {

            // Solid and Liquid Boiler Firebox
            if (GTUtility.areStacksEqual(event.itemStack, GTModHandler.getModItem("Railcraft", "machine.beta", 1, 5))
                || GTUtility
                    .areStacksEqual(event.itemStack, GTModHandler.getModItem("Railcraft", "machine.beta", 1, 6))) {
                event.toolTip.add(
                    String.format("Produces %d Pollution/Second per firebox", PollutionConfig.fireboxPollutionAmount));
            }

            // Tunnel Bore
            if (GTUtility.areStacksEqual(event.itemStack, GTModHandler.getModItem("Railcraft", "cart.bore", 1, 0))) {
                event.toolTip.add(String.format(PRODUCES_POLLUTION_FORMAT, PollutionConfig.tunnelBorePollutionAmount));
            }

            // Coke Oven Brick
            if (GTUtility
                .areStacksEqual(event.itemStack, GTModHandler.getModItem("Railcraft", "machine.alpha", 1, 7))) {
                event.toolTip.add(String.format(MULTI_POLLUTION_FORMAT, PollutionConfig.cokeOvenPollutionAmount));
            }

            // Advanced Coke Oven Brick
            if (GTUtility
                .areStacksEqual(event.itemStack, GTModHandler.getModItem("Railcraft", "machine.alpha", 1, 12))) {
                event.toolTip
                    .add(String.format(MULTI_POLLUTION_FORMAT, PollutionConfig.advancedCokeOvenPollutionAmount));
            }

            // Hobbyist's Steam Engine
            if (GTUtility.areStacksEqual(event.itemStack, GTModHandler.getModItem("Railcraft", "machine.beta", 1, 7))) {
                event.toolTip
                    .add(String.format(PRODUCES_POLLUTION_FORMAT, PollutionConfig.hobbyistEnginePollutionAmount));
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
                            String.format(
                                "Produces %d Pollution/Second when ignited",
                                (PollutionConfig.rocketPollutionAmount * tier / 100)));
                        event.toolTip.add(
                            String.format(
                                "Produces %d Pollution/Second when flying",
                                PollutionConfig.rocketPollutionAmount * tier));
                        break;
                    }
                }
            }
        }
    }
}

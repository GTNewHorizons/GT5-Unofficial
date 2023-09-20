package gtPlusPlus.nei;

import static gregtech.api.enums.Mods.AdvancedSolarPanel;
import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.enums.Mods.NotEnoughItems;

import net.minecraft.nbt.NBTTagCompound;

import cpw.mods.fml.common.event.FMLInterModComms;

public class NEI_IMC_Sender {

    public static void IMCSender() {
        // NEI jar is using some outdated handler names
        sendHandler("gtpp.recipe.alloyblastsmelter", "gregtech:gt.blockmachines:810", 2);
        sendCatalyst("gtpp.recipe.alloyblastsmelter", "gregtech:gt.blockmachines:31150");
        sendHandler("gtpp.recipe.rocketenginefuel", "gregtech:gt.blockmachines:793");
        sendHandler("gtpp.recipe.cyclotron", "gregtech:gt.blockmachines:828");
        sendHandler("gtpp.recipe.chemicaldehydrator", "gregtech:gt.blockmachines:911");
        sendHandler("gtpp.recipe.slowfusionreactor", "gregtech:gt.blockmachines:31015");
        sendHandler("gtpp.recipe.RTGgenerators", "gregtech:gt.blockmachines:869");
        sendHandler("gtpp.recipe.cokeoven", "gregtech:gt.blockmachines:791");
        sendHandler("gtpp.recipe.semifluidgeneratorfuels", "gregtech:gt.blockmachines:837");
        sendHandler("gtpp.recipe.fishpond", "gregtech:gt.blockmachines:829", 1);
        sendHandler("gtpp.recipe.multimixer", "gregtech:gt.blockmachines:811");
        sendHandler("gtpp.recipe.advanced.mixer", "gregtech:gt.blockmachines:811");
        sendHandler("gtpp.recipe.cryogenicfreezer", "gregtech:gt.blockmachines:910");
        sendHandler("gtpp.recipe.matterfab2", "gregtech:gt.blockmachines:799");
        sendHandler("gtpp.recipe.multicentrifuge", "gregtech:gt.blockmachines:790", 1);
        sendHandler("gtpp.recipe.multielectro", "gregtech:gt.blockmachines:796", 1);
        sendHandler("gtpp.recipe.simplewasher", "gregtech:gt.blockmachines:767");
        sendHandler("gtpp.recipe.vacfurnace", "gregtech:gt.blockmachines:995", 1);
        sendHandler("gtpp.recipe.fissionfuel", "gregtech:gt.blockmachines:835", 1);
        sendHandler("gtpp.recipe.lftr", "gregtech:gt.blockmachines:751", 1);
        sendHandler("gtpp.recipe.lftr.sparging", "gregtech:gt.blockmachines:31035", 1);
        sendHandler("gtpp.recipe.oremill", "gregtech:gt.blockmachines:31027", 1);
        sendHandler("gtpp.recipe.flotationcell", "gregtech:gt.blockmachines:31028", 1);
        sendHandler("gtpp.recipe.fluidchemicaleactor", "gregtech:gt.blockmachines:998");

        sendCatalyst("GTPP_Decayables", "miscutils:blockDecayablesChest");

        sendHandler("gtpp.recipe.geothermalfuel", "gregtech:gt.blockmachines:830");
        sendCatalyst("gtpp.recipe.geothermalfuel", "gregtech:gt.blockmachines:830");

        sendHandler("gtpp.recipe.thermalgeneratorfuel", "gregtech:gt.blockmachines:875", 1);
        sendCatalyst("gtpp.recipe.thermalgeneratorfuel", "gregtech:gt.blockmachines:875");

        sendHandler("gtpp.recipe.solartower", "gregtech:gt.blockmachines:863", 1);
        sendCatalyst("gtpp.recipe.solartower", "gregtech:gt.blockmachines:863");

        sendHandler("gtpp.recipe.coldtrap", "gregtech:gt.blockmachines:31034");
        sendCatalyst("gtpp.recipe.coldtrap", "gregtech:gt.blockmachines:31033");
        sendCatalyst("gtpp.recipe.coldtrap", "gregtech:gt.blockmachines:31034");

        sendHandler("gtpp.recipe.reactorprocessingunit", "gregtech:gt.blockmachines:31032");
        sendCatalyst("gtpp.recipe.reactorprocessingunit", "gregtech:gt.blockmachines:31031");
        sendCatalyst("gtpp.recipe.reactorprocessingunit", "gregtech:gt.blockmachines:31032");

        sendHandler("gtpp.recipe.nuclearsaltprocessingplant", "gregtech:gt.blockmachines:749");
        sendCatalyst("gtpp.recipe.nuclearsaltprocessingplant", "gregtech:gt.blockmachines:749");

        sendHandler("gtpp.recipe.treefarm", "gregtech:gt.blockmachines:836");
        sendCatalyst("gtpp.recipe.treefarm", "gregtech:gt.blockmachines:836");

        sendHandler("gtpp.recipe.quantumforcesmelter", "gregtech:gt.blockmachines:31151");
        sendCatalyst("gtpp.recipe.quantumforcesmelter", "gregtech:gt.blockmachines:31151");

        if (AdvancedSolarPanel.isModLoaded()) {
            sendHandler("gtpp.recipe.moleculartransformer", "AdvancedSolarPanel:BlockMolecularTransformer");
            sendCatalyst("gtpp.recipe.moleculartransformer", "gregtech:gt.blockmachines:31072");
        }

        sendCatalyst("gt.recipe.replicator", "gregtech:gt.blockmachines:31050");
        sendCatalyst("gt.recipe.gasturbinefuel", "gregtech:gt.blockmachines:31073", -20);
        sendCatalyst("gt.recipe.plasmageneratorfuels", "gregtech:gt.blockmachines:31074", -20);
        sendCatalyst("gt.recipe.hammer", "gregtech:gt.blockmachines:31075");
        sendCatalyst("gt.recipe.fluidheater", "gregtech:gt.blockmachines:31077");
        sendCatalyst("gt.recipe.compressor", "gregtech:gt.blockmachines:31078");

        sendCatalyst("crafting", "gregtech:gt.blockmachines:31081", -10);
        sendCatalyst("crafting", "gregtech:gt.blockmachines:31082", -10);
        sendCatalyst("crafting", "gregtech:gt.blockmachines:31091", -10);
        sendCatalyst("crafting", "gregtech:gt.blockmachines:31092", -10);
        sendCatalyst("crafting", "gregtech:gt.blockmachines:31093", -10);
        sendCatalyst("crafting", "gregtech:gt.blockmachines:31094", -10);
        sendCatalyst("crafting", "gregtech:gt.blockmachines:31095", -10);
        sendCatalyst("crafting", "gregtech:gt.blockmachines:31096", -10);
        sendCatalyst("crafting", "gregtech:gt.blockmachines:31097", -10);
        sendCatalyst("crafting", "gregtech:gt.blockmachines:31098", -10);

        sendHandler("gtpp.recipe.multidehydrator", "gregtech:gt.blockmachines:995");
        sendCatalyst("gtpp.recipe.multidehydrator", "gregtech:gt.blockmachines:995");
        sendRemoveCatalyst("gtpp.recipe.chemicaldehydrator", "gregtech:gt.blockmachines:995");

        sendCatalyst("gt.recipe.fusionreactor", "gregtech:gt.blockmachines:975");
    }

    private static void sendHandler(String aRecipeName, String aBlock) {
        sendHandler(aRecipeName, aBlock, 2);
    }

    private static void sendHandler(String aRecipeName, String aBlock, int aRecipesPerPage) {
        NBTTagCompound aNBT = new NBTTagCompound();
        aNBT.setString("handler", aRecipeName);
        aNBT.setString("modName", "GT++");
        aNBT.setString("modId", GTPlusPlus.ID);
        aNBT.setBoolean("modRequired", true);
        aNBT.setString("itemName", aBlock);
        aNBT.setInteger("yShift", 6);
        aNBT.setInteger("handlerHeight", 135);
        aNBT.setInteger("handlerWidth", 166);
        aNBT.setInteger("maxRecipesPerPage", aRecipesPerPage);
        FMLInterModComms.sendMessage(NotEnoughItems.ID, "registerHandlerInfo", aNBT);
    }

    private static void sendCatalyst(String aName, String aStack, int aPriority) {
        NBTTagCompound aNBT = new NBTTagCompound();
        aNBT.setString("handlerID", aName);
        aNBT.setString("itemName", aStack);
        aNBT.setInteger("priority", aPriority);
        FMLInterModComms.sendMessage(NotEnoughItems.ID, "registerCatalystInfo", aNBT);
    }

    private static void sendCatalyst(String aName, String aStack) {
        sendCatalyst(aName, aStack, 0);
    }

    private static void sendRemoveCatalyst(String aName, String aStack) {
        NBTTagCompound aNBT = new NBTTagCompound();
        aNBT.setString("handlerID", aName);
        aNBT.setString("itemName", aStack);
        FMLInterModComms.sendMessage(NotEnoughItems.ID, "removeCatalystInfo", aNBT);
    }
}

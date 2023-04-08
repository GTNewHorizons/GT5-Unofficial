package gtPlusPlus.xmod.gregtech.common.computer;

import static gregtech.api.enums.Mods.BartWorks;
import static gregtech.api.enums.Mods.GoodGenerator;
import static gtPlusPlus.xmod.gregtech.common.tileentities.misc.GT_TileEntity_ComputerCube.sReactorList;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

import Ic2ExpReactorPlanner.ComponentFactory;
import gregtech.api.enums.ItemList;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.bartworks.BW_Utils;
import gtPlusPlus.xmod.goodgenerator.GG_Utils;

public class GT_ComputerCube_Setup {

    public static void init() {
        Logger.INFO(
                "[Reactor Simulator] Added " + ComponentFactory.getComponentCount()
                        + " components to ComponentFactory.");
        if (sReactorList == null) {
            sReactorList = new ArrayList<GT_ItemStack>();

            String[] aIc2Items = new String[] { "reactorUraniumSimple", "reactorUraniumDual", "reactorUraniumQuad", /*
                                                                                                                     * "reactorIsotopeCell",
                                                                                                                     */
                    "reactorReflector", "reactorReflectorThick", "reactorCoolantSimple", "reactorCoolantTriple",
                    "reactorCoolantSix", "reactorCondensator", "reactorCondensatorLap", "reactorPlating",
                    "reactorPlatingHeat", "reactorPlatingExplosive", "reactorVent", "reactorVentCore",
                    "reactorVentGold", "reactorVentSpread", "reactorVentDiamond", "reactorHeatSwitch",
                    "reactorHeatSwitchCore", "reactorHeatSwitchSpread",
                    "reactorHeatSwitchDiamond", /* "reactorHeatpack", */
            };

            for (String aItem : aIc2Items) {
                ItemStack aStack = GT_ModHandler.getIC2Item(aItem, 1);
                if (!ItemUtils.checkForInvalidItems(aStack)) {
                    Logger.INFO("Unable to find IC2 Item: " + aItem);
                    CORE.crash("Unable to find IC2 Item: " + aItem);
                } else {
                    sReactorList.add(new GT_ItemStack(aStack.copy()));
                }
            }

            ItemList[] aGtItems = new ItemList[] { ItemList.Neutron_Reflector, ItemList.Moxcell_1, ItemList.Moxcell_2,
                    ItemList.Moxcell_4, /* ItemList.Uraniumcell_1, ItemList.Uraniumcell_2, ItemList.Uraniumcell_4, */
                    ItemList.NaquadahCell_1, ItemList.NaquadahCell_2, ItemList.NaquadahCell_4, ItemList.ThoriumCell_1,
                    ItemList.ThoriumCell_2, ItemList.ThoriumCell_4, ItemList.MNqCell_1, ItemList.MNqCell_2,
                    ItemList.MNqCell_4, ItemList.Reactor_Coolant_He_1, ItemList.Reactor_Coolant_He_3,
                    ItemList.Reactor_Coolant_He_6, ItemList.Reactor_Coolant_NaK_1, ItemList.Reactor_Coolant_NaK_3,
                    ItemList.Reactor_Coolant_NaK_6, ItemList.Reactor_Coolant_Sp_1, ItemList.Reactor_Coolant_Sp_2,
                    ItemList.Reactor_Coolant_Sp_3, ItemList.Reactor_Coolant_Sp_6 };

            for (ItemList aItem : aGtItems) {
                sReactorList.add(new GT_ItemStack(aItem.get(1)));
            }

            if (BartWorks.isModLoaded()) {
                ArrayList<ItemStack> aBartReactorItems = BW_Utils.getAll(1);
                for (ItemStack aReactorItem : aBartReactorItems) {
                    sReactorList.add(new GT_ItemStack(aReactorItem));
                }
            }

            if (GoodGenerator.isModLoaded()) {
                ArrayList<ItemStack> aGlodReactorItems = GG_Utils.getAll(1);
                for (ItemStack aReactorItem : aGlodReactorItems) {
                    sReactorList.add(new GT_ItemStack(aReactorItem));
                }
            }
            Logger.INFO(
                    "[Reactor Simulator] Added " + sReactorList.size() + " components to GT_TileEntity_ComputerCube.");
        }
    }
}

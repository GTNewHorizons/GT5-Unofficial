package com.github.technus.tectech.loader;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.casing.GT_Container_CasingsTT;
import com.github.technus.tectech.thing.item.DebugBuilder;
import com.github.technus.tectech.thing.item.DebugContainer_EM;
import com.github.technus.tectech.thing.machineTT;
import cpw.mods.fml.common.ProgressManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

import static com.github.technus.tectech.TecTech.mainTab;

public final class MainLoader {//TODO add checks for - is mod loaded dreamcraft to enable higher tier machinery. (above UV), or implement a check for GT tier values.
    private static ProgressManager.ProgressBar progressBar;

    public MainLoader(){
        progressBar = ProgressManager.push("TecTech Loader", 6);
    }

    public void run(){
        progressBar.step("Loading Things");
        things();

        progressBar.step("Loading Recipes");
        new RecipeLoader().run();
        TecTech.Logger.info("Recipe Init Done");

        progressBar.step("Registering in Creative Tab");
        mainTab = new CreativeTabs("TecTech") {
            @SideOnly(Side.CLIENT)
            @Override
            public Item getTabIconItem() {
                return DebugContainer_EM.INSTANCE;
            }

            @Override
            public void displayAllReleventItems(List stuffToShow) {
                for (int i = 1; i < GregTech_API.METATILEENTITIES.length; i++) {
                    if (GregTech_API.METATILEENTITIES[i] instanceof machineTT) {
                        stuffToShow.add(new ItemStack(GregTech_API.sBlockMachines, 1, i));
                    }
                }
                super.displayAllReleventItems(stuffToShow);
            }
        };
        registerThingsInTabs();

        ProgressManager.pop(progressBar);
    }

    public void things() {
        progressBar.step("Loading Elemental Things");
        new ElementalLoader().run();
        TecTech.Logger.info("Elemental Init Done");

        progressBar.step("Loading Regular Things");
        new ThingsLoader().run();
        TecTech.Logger.info("Block/Item Init Done");

        progressBar.step("Loading Machine Things");
        new MachineLoader().run();
        TecTech.Logger.info("Machine Init Done");
    }

    public void registerThingsInTabs() {
        QuantumGlassBlock.INSTANCE.setCreativeTab(mainTab);
        GT_Container_CasingsTT.sBlockCasingsTT.setCreativeTab(mainTab);
        DebugContainer_EM.INSTANCE.setCreativeTab(mainTab);
        DebugBuilder.INSTANCE.setCreativeTab(mainTab);
        TecTech.Logger.info("CreativeTab initiation complete");
    }
}

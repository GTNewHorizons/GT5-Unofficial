package com.github.technus.tectech.loader.gui;

import com.github.technus.tectech.thing.CustomItemList;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.casing.TT_Container_Casings;
import com.github.technus.tectech.thing.item.ConstructableTriggerItem;
import com.github.technus.tectech.thing.item.DebugElementalInstanceContainer_EM;
import com.github.technus.tectech.thing.item.ElementalDefinitionScanStorage_EM;
import com.github.technus.tectech.thing.item.ParametrizerMemoryCard;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import java.util.List;

public class CreativeTabTecTech extends CreativeTabs {
    public static CreativeTabTecTech creativeTabTecTech;

    public CreativeTabTecTech(String name) {
        super(name);
        registerThingsInTabs();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Item getTabIconItem() {
        return DebugElementalInstanceContainer_EM.INSTANCE;
    }

    @Override
    public void displayAllReleventItems(List stuffToShow) {
        for(CustomItemList item: CustomItemList.values()){
            if (item.hasBeenSet() && item.getBlock() == GregTech_API.sBlockMachines) {
                stuffToShow.add(item.get(1));
            }
        }
        super.displayAllReleventItems(stuffToShow);
    }

    private static void registerThingsInTabs() {
        QuantumGlassBlock.INSTANCE.setCreativeTab(creativeTabTecTech);
        TT_Container_Casings.sBlockCasingsTT.setCreativeTab(creativeTabTecTech);
        TT_Container_Casings.sHintCasingsTT.setCreativeTab(creativeTabTecTech);
        DebugElementalInstanceContainer_EM.INSTANCE.setCreativeTab(creativeTabTecTech);
        ConstructableTriggerItem.INSTANCE.setCreativeTab(creativeTabTecTech);
        ParametrizerMemoryCard.INSTANCE.setCreativeTab(creativeTabTecTech);
        ElementalDefinitionScanStorage_EM.INSTANCE.setCreativeTab(creativeTabTecTech);
    }
}

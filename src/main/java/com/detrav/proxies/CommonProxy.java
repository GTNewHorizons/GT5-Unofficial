package com.detrav.proxies;

import com.detrav.DetravScannerMod;

import com.detrav.enums.DetravItemList;
import com.detrav.enums.DetravSimpleItems;
import com.detrav.events.DetravBlockBreakEventHandler;
import com.detrav.events.DetravEntityDropEvent;
import com.detrav.events.DetravLevelUpEvent;
import com.detrav.events.DetravLoginEventHandler;
import com.detrav.gui.DetravGuiProPick;
import com.detrav.gui.DetravPortableAnvilGui;
import com.detrav.gui.containers.DetravPortableAnvilContainer;
import com.detrav.gui.containers.DetravPortableChargerContainer;
import com.detrav.gui.DetravPortableChargerGui;
import com.detrav.items.DetravMetaGeneratedTool01;
import cpw.mods.fml.common.network.IGuiHandler;
import gregtech.api.GregTech_API;
import gregtech.api.enums.*;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import ic2.core.Ic2Items;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Created by wital_000 on 19.03.2016.
 */
public class CommonProxy implements IGuiHandler {

    public void onLoad() {

    }

    public void onPostLoad() {
        //long tBits = GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED
                //| GT_ModHandler.RecipeBits.ONLY_ADD_IF_RESULT_IS_NOT_NULL | GT_ModHandler.RecipeBits.NOT_REMOVABLE;
        //for (Materials aMaterial : Materials.values()) {
            //if ((aMaterial.mUnificatable) && (aMaterial.mMaterialInto == aMaterial)) {
                //if (!aMaterial.contains(SubTag.NO_SMASHING)) {
                   // GT_ModHandler.addCraftingRecipe(GT_OreDictUnificator.get(DetravSimpleItems.toolHeadProPick.get(aMaterial), null, 1L), tBits, new Object[]{"PI", "fh",
                            //Character.valueOf('P'), OrePrefixes.plate.get(aMaterial), Character.valueOf('I'), OrePrefixes.ingot.get(aMaterial)});
               // }
            //}
        //}

        //if (!GregTech_API.sSpecialFile.get(ConfigCategories.general, "DisableFlintTools", false)) {
          //  GT_ModHandler.addCraftingRecipe(DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(0, 1, Materials.Flint, Materials.Wood, null), GT_ModHandler.RecipeBits.NOT_REMOVABLE, new Object[]{"FF", "SS", Character.valueOf('S'), OrePrefixes.stick.get(Materials.Wood), Character.valueOf('F'), new ItemStack(Items.flint, 1)});
        //}

        //boiler recipes
        //GT_ModHandler.addCraftingRecipe(DetravItemList.Solar_Boiler_Low.get(1L, new Object[0]), GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"GGG", "BBB", "PMP", Character.valueOf('M'), ItemList.Machine_Bronze_Boiler, Character.valueOf('P'), OrePrefixes.pipeSmall.get(Materials.Bronze), Character.valueOf('B'), OrePrefixes.plate.get(Materials.Silver), Character.valueOf('G'), new ItemStack(Blocks.glass, 1)});
        GT_ModHandler.addCraftingRecipe(DetravItemList.Solar_Boiler_Medium.get(1L, new Object[0]), GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"GSG", "NXN", "PMP", Character.valueOf('M'), ItemList.Machine_Bronze_Boiler_Solar, Character.valueOf('X'), ItemList.Machine_Steel_Boiler, Character.valueOf('S'), ItemList.Cover_SolarPanel, Character.valueOf('P'), OrePrefixes.pipeMedium.get(Materials.Steel), Character.valueOf('N'), OrePrefixes.plate.get(Materials.Electrum), Character.valueOf('G'), GT_ModHandler.getIC2Item("reinforcedGlass", 1L)});
        GT_ModHandler.addCraftingRecipe(DetravItemList.Solar_Boiler_High.get(1L, new Object[0]), GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"GSG", "RXR", "PMP", Character.valueOf('M'), DetravItemList.Solar_Boiler_Medium, Character.valueOf('X'), ItemList.Casing_HV, Character.valueOf('S'), ItemList.Cover_SolarPanel_8V, Character.valueOf('P'), OrePrefixes.pipeLarge.get(Materials.StainlessSteel), Character.valueOf('R'), OrePrefixes.plate.get(Materials.SterlingSilver), Character.valueOf('G'),GT_ModHandler.getIC2Item("reinforcedGlass", 1L),});

        GT_ModHandler.addCraftingRecipe(
                DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(108,1,Materials.Iron, Materials._NULL,null),
                GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[]{ "GGG","dCi","GGG", Character.valueOf('G'), OrePrefixes.plate.get(Materials.Titanium), Character.valueOf('C'), ItemList.Circuit_Master.get(1) });

        GT_ModHandler.addCraftingRecipe(DetravItemList.DetravAdvancedMiner2.get(1L), GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.DISMANTLEABLE , new Object[]{"WWW", "EME", "CCC", 'M', ItemList.OreDrill4, 'W', OrePrefixes.frameGt.get(Materials.Tritanium), 'E', OrePrefixes.circuit.get(Materials.Superconductor), 'C', ItemList.Electric_Motor_UV});


        //Treetap recipes

        GT_ModHandler.addShapelessCraftingRecipe(DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(10,1, Materials.Wood,Materials.Wood, null ),
                new Object[]{Ic2Items.treetap,Ic2Items.rubberWood,Ic2Items.resin});

        GT_ModHandler.addShapelessCraftingRecipe(ItemList.Circuit_Integrated.get(1), new Object[]{ DetravItemList.ConfiguredCircuit } );
        //DetravCraftingEventHandler.register();
        DetravEntityDropEvent.register();
        DetravLevelUpEvent.register();
        DetravBlockBreakEventHandler.register();
        DetravLoginEventHandler.register();
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case DetravGuiProPick.GUI_ID:
                return null;
            case DetravPortableChargerGui.GUI_ID:
                return new DetravPortableChargerContainer(player.inventory,world,player.getCurrentEquippedItem());
            case DetravPortableAnvilGui.GUI_ID:
                return new DetravPortableAnvilContainer(player.inventory,world,player.getCurrentEquippedItem());
            default:
                return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case DetravGuiProPick.GUI_ID:
                return new DetravGuiProPick();
            case DetravPortableChargerGui.GUI_ID:
                return new DetravPortableChargerGui(player.inventory,world,player.getCurrentEquippedItem());
            case DetravPortableAnvilGui.GUI_ID:
                return new DetravPortableAnvilGui(player.inventory,world,player.getCurrentEquippedItem());
            default:
                return null;
        }
    }


    public void openProPickGui()
    {
        //just Client code
    }

    public void openPortableChargerGui(EntityPlayer player)
    {
        player.openGui(DetravScannerMod.instance, DetravPortableChargerGui.GUI_ID,player.worldObj,(int)player.posX,(int)player.posY,(int)player.posZ);
    }

    public void openPortableAnvilGui(EntityPlayer player)
    {
        player.openGui(DetravScannerMod.instance, DetravPortableAnvilGui.GUI_ID,player.worldObj,(int)player.posX,(int)player.posY,(int)player.posZ);
    }



    public void onPreInit()
    {

    }

    public void sendPlayerExeption(String s) {

    }
}

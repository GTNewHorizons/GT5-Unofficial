package gregtech.loaders.postload;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;

public class ScrapboxDropLoader implements Runnable {

    @Override
    public void run() {
        GTLog.out.println("GTMod: (re-)adding Scrapbox Drops.");

        GTModHandler.addScrapboxDrop(9.5F, new ItemStack(Items.wooden_hoe));
        GTModHandler.addScrapboxDrop(2.0F, new ItemStack(Items.wooden_axe));
        GTModHandler.addScrapboxDrop(2.0F, new ItemStack(Items.wooden_sword));
        GTModHandler.addScrapboxDrop(2.0F, new ItemStack(Items.wooden_shovel));
        GTModHandler.addScrapboxDrop(2.0F, new ItemStack(Items.wooden_pickaxe));
        GTModHandler.addScrapboxDrop(2.0F, new ItemStack(Items.sign));
        GTModHandler.addScrapboxDrop(9.5F, new ItemStack(Items.stick));
        GTModHandler.addScrapboxDrop(5.0F, new ItemStack(Blocks.dirt));
        GTModHandler.addScrapboxDrop(3.0F, new ItemStack(Blocks.grass));
        GTModHandler.addScrapboxDrop(3.0F, new ItemStack(Blocks.gravel));
        GTModHandler.addScrapboxDrop(0.5F, new ItemStack(Blocks.pumpkin));
        GTModHandler.addScrapboxDrop(1.0F, new ItemStack(Blocks.soul_sand));
        GTModHandler.addScrapboxDrop(2.0F, new ItemStack(Blocks.netherrack));
        GTModHandler.addScrapboxDrop(1.0F, new ItemStack(Items.bone));
        GTModHandler.addScrapboxDrop(9.0F, new ItemStack(Items.rotten_flesh));
        GTModHandler.addScrapboxDrop(0.4F, new ItemStack(Items.cooked_porkchop));
        GTModHandler.addScrapboxDrop(0.4F, new ItemStack(Items.cooked_beef));
        GTModHandler.addScrapboxDrop(0.4F, new ItemStack(Items.cooked_chicken));
        GTModHandler.addScrapboxDrop(0.5F, new ItemStack(Items.apple));
        GTModHandler.addScrapboxDrop(0.5F, new ItemStack(Items.bread));
        GTModHandler.addScrapboxDrop(0.1F, new ItemStack(Items.cake));
        GTModHandler.addScrapboxDrop(1.0F, ItemList.IC2_Food_Can_Filled.get(1L));
        GTModHandler.addScrapboxDrop(2.0F, ItemList.IC2_Food_Can_Spoiled.get(1L));
        GTModHandler.addScrapboxDrop(0.2F, GTOreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 1L));
        GTModHandler.addScrapboxDrop(1.0F, GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1L));
        GTModHandler.addScrapboxDrop(2.0F, ItemList.Cell_Empty.get(1L));
        GTModHandler.addScrapboxDrop(5.0F, GTOreDictUnificator.get(OrePrefixes.plate, Materials.Paper, 1L));
        GTModHandler.addScrapboxDrop(1.0F, new ItemStack(Items.leather));
        GTModHandler.addScrapboxDrop(1.0F, new ItemStack(Items.feather));
        GTModHandler.addScrapboxDrop(0.7F, GTModHandler.getIC2Item("plantBall", 1L));
        GTModHandler.addScrapboxDrop(3.8F, GTOreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 1L));
        GTModHandler.addScrapboxDrop(0.6F, new ItemStack(Items.slime_ball));
        GTModHandler.addScrapboxDrop(0.8F, GTOreDictUnificator.get(OrePrefixes.dust, Materials.Rubber, 1L));
        GTModHandler.addScrapboxDrop(2.7F, GTModHandler.getIC2Item("suBattery", 1L));
        GTModHandler.addScrapboxDrop(3.6F, ItemList.Circuit_Primitive.get(1L));
        GTModHandler.addScrapboxDrop(0.8F, ItemList.Circuit_Parts_Advanced.get(1L));
        GTModHandler.addScrapboxDrop(1.8F, ItemList.Circuit_Board_Basic.get(1L));
        GTModHandler.addScrapboxDrop(0.4F, ItemList.Circuit_Board_Advanced.get(1L));
        GTModHandler.addScrapboxDrop(0.2F, ItemList.Circuit_Board_Elite.get(1L));
        GTModHandler.addScrapboxDrop(0.9F, GTOreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L));
        GTModHandler.addScrapboxDrop(0.8F, GTOreDictUnificator.get(OrePrefixes.dust, Materials.Glowstone, 1L));
        GTModHandler.addScrapboxDrop(0.8F, GTOreDictUnificator.get(OrePrefixes.dust, Materials.Coal, 1L));
        GTModHandler.addScrapboxDrop(2.5F, GTOreDictUnificator.get(OrePrefixes.dust, Materials.Charcoal, 1L));
        GTModHandler.addScrapboxDrop(1.0F, GTOreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 1L));
        GTModHandler.addScrapboxDrop(1.0F, GTOreDictUnificator.get(OrePrefixes.dust, Materials.Gold, 1L));
        GTModHandler.addScrapboxDrop(0.5F, GTOreDictUnificator.get(OrePrefixes.dust, Materials.Silver, 1L));
        GTModHandler.addScrapboxDrop(0.5F, GTOreDictUnificator.get(OrePrefixes.dust, Materials.Electrum, 1L));
        GTModHandler.addScrapboxDrop(1.2F, GTOreDictUnificator.get(OrePrefixes.dust, Materials.Tin, 1L));
        GTModHandler.addScrapboxDrop(1.2F, GTOreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 1L));
        GTModHandler.addScrapboxDrop(0.5F, GTOreDictUnificator.get(OrePrefixes.dust, Materials.Bauxite, 1L));
        GTModHandler.addScrapboxDrop(0.5F, GTOreDictUnificator.get(OrePrefixes.dust, Materials.Aluminium, 1L));
        GTModHandler.addScrapboxDrop(0.5F, GTOreDictUnificator.get(OrePrefixes.dust, Materials.Lead, 1L));
        GTModHandler.addScrapboxDrop(0.5F, GTOreDictUnificator.get(OrePrefixes.dust, Materials.Nickel, 1L));
        GTModHandler.addScrapboxDrop(0.5F, GTOreDictUnificator.get(OrePrefixes.dust, Materials.Zinc, 1L));
        GTModHandler.addScrapboxDrop(0.5F, GTOreDictUnificator.get(OrePrefixes.dust, Materials.Brass, 1L));
        GTModHandler.addScrapboxDrop(0.5F, GTOreDictUnificator.get(OrePrefixes.dust, Materials.Steel, 1L));
        GTModHandler.addScrapboxDrop(1.5F, GTOreDictUnificator.get(OrePrefixes.dust, Materials.Obsidian, 1L));
        GTModHandler.addScrapboxDrop(1.5F, GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L));
        GTModHandler.addScrapboxDrop(2.0F, GTOreDictUnificator.get(OrePrefixes.dust, Materials.Saltpeter, 1L));
        GTModHandler.addScrapboxDrop(2.0F, GTOreDictUnificator.get(OrePrefixes.dust, Materials.Lazurite, 1L));
        GTModHandler.addScrapboxDrop(2.0F, GTOreDictUnificator.get(OrePrefixes.dust, Materials.Pyrite, 1L));
        GTModHandler.addScrapboxDrop(2.0F, GTOreDictUnificator.get(OrePrefixes.dust, Materials.Calcite, 1L));
        GTModHandler.addScrapboxDrop(2.0F, GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sodalite, 1L));
        GTModHandler.addScrapboxDrop(4.0F, GTOreDictUnificator.get(OrePrefixes.dust, Materials.Netherrack, 1L));
        GTModHandler.addScrapboxDrop(4.0F, GTOreDictUnificator.get(OrePrefixes.dust, Materials.Flint, 1L));
        GTModHandler.addScrapboxDrop(0.03F, GTOreDictUnificator.get(OrePrefixes.dust, Materials.Platinum, 1L));
        GTModHandler.addScrapboxDrop(0.03F, GTOreDictUnificator.get(OrePrefixes.dust, Materials.Tungsten, 1L));
        GTModHandler.addScrapboxDrop(0.03F, GTOreDictUnificator.get(OrePrefixes.dust, Materials.Chrome, 1L));
        GTModHandler.addScrapboxDrop(0.03F, GTOreDictUnificator.get(OrePrefixes.dust, Materials.Titanium, 1L));
        GTModHandler.addScrapboxDrop(0.03F, GTOreDictUnificator.get(OrePrefixes.dust, Materials.Magnesium, 1L));
        GTModHandler.addScrapboxDrop(0.03F, GTOreDictUnificator.get(OrePrefixes.dust, Materials.Endstone, 1L));
        GTModHandler.addScrapboxDrop(0.5F, GTOreDictUnificator.get(OrePrefixes.dust, Materials.GarnetRed, 1L));
        GTModHandler.addScrapboxDrop(0.5F, GTOreDictUnificator.get(OrePrefixes.dust, Materials.GarnetYellow, 1L));
        GTModHandler.addScrapboxDrop(0.05F, GTOreDictUnificator.get(OrePrefixes.gem, Materials.Olivine, 1L));
        GTModHandler.addScrapboxDrop(0.05F, GTOreDictUnificator.get(OrePrefixes.gem, Materials.Ruby, 1L));
        GTModHandler.addScrapboxDrop(0.05F, GTOreDictUnificator.get(OrePrefixes.gem, Materials.Sapphire, 1L));
        GTModHandler.addScrapboxDrop(0.05F, GTOreDictUnificator.get(OrePrefixes.gem, Materials.GreenSapphire, 1L));
        GTModHandler.addScrapboxDrop(0.05F, GTOreDictUnificator.get(OrePrefixes.gem, Materials.Emerald, 1L));
        GTModHandler.addScrapboxDrop(0.05F, GTOreDictUnificator.get(OrePrefixes.gem, Materials.Diamond, 1L));
    }
}

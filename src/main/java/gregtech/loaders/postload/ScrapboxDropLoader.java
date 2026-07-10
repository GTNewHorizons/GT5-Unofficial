package gregtech.loaders.postload;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
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
        GTModHandler.addScrapboxDrop(
            0.2F,
            MaterialLibAPI.getStack(Materials2Materials.Silicon, Materials2Shapes.shapeDust, (int) (1L)));
        GTModHandler.addScrapboxDrop(1.0F, GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1L));
        GTModHandler.addScrapboxDrop(2.0F, ItemList.Cell_Empty.get(1L));
        GTModHandler.addScrapboxDrop(5.0F, GTOreDictUnificator.get(OrePrefixes.plate, Materials.Paper, 1L));
        GTModHandler.addScrapboxDrop(1.0F, new ItemStack(Items.leather));
        GTModHandler.addScrapboxDrop(1.0F, new ItemStack(Items.feather));
        GTModHandler.addScrapboxDrop(0.7F, ItemList.IC2_Plantball.get(1L));
        GTModHandler.addScrapboxDrop(
            3.8F,
            MaterialLibAPI.getStack(Materials2Materials.Wood, Materials2Shapes.shapeDust, (int) (1L)));
        GTModHandler.addScrapboxDrop(0.6F, new ItemStack(Items.slime_ball));
        GTModHandler.addScrapboxDrop(
            0.8F,
            MaterialLibAPI.getStack(Materials2Materials.Rubber, Materials2Shapes.shapeDust, (int) (1L)));
        GTModHandler.addScrapboxDrop(2.7F, ItemList.IC2_SuBattery.get(1L));
        GTModHandler.addScrapboxDrop(3.6F, ItemList.Circuit_Primitive.get(1L));
        GTModHandler.addScrapboxDrop(0.8F, ItemList.Circuit_Parts_Advanced.get(1L));
        GTModHandler.addScrapboxDrop(1.8F, ItemList.Circuit_Board_Basic.get(1L));
        GTModHandler.addScrapboxDrop(0.4F, ItemList.Circuit_Board_Advanced.get(1L));
        GTModHandler.addScrapboxDrop(0.2F, ItemList.Circuit_Board_Elite.get(1L));
        GTModHandler.addScrapboxDrop(0.9F, GTOreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L));
        GTModHandler.addScrapboxDrop(0.8F, GTOreDictUnificator.get(OrePrefixes.dust, Materials.Glowstone, 1L));
        GTModHandler.addScrapboxDrop(
            0.8F,
            MaterialLibAPI.getStack(Materials2Materials.Coal, Materials2Shapes.shapeDust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            2.5F,
            MaterialLibAPI.getStack(Materials2Materials.Charcoal, Materials2Shapes.shapeDust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            1.0F,
            MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.shapeDust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            1.0F,
            MaterialLibAPI.getStack(Materials2Materials.Gold, Materials2Shapes.shapeDust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            0.5F,
            MaterialLibAPI.getStack(Materials2Materials.Silver, Materials2Shapes.shapeDust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            0.5F,
            MaterialLibAPI.getStack(Materials2Materials.Electrum, Materials2Shapes.shapeDust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            1.2F,
            MaterialLibAPI.getStack(Materials2Materials.Tin, Materials2Shapes.shapeDust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            1.2F,
            MaterialLibAPI.getStack(Materials2Materials.Copper, Materials2Shapes.shapeDust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            0.5F,
            MaterialLibAPI.getStack(Materials2Materials.Bauxite, Materials2Shapes.shapeDust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            0.5F,
            MaterialLibAPI.getStack(Materials2Materials.Aluminium, Materials2Shapes.shapeDust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            0.5F,
            MaterialLibAPI.getStack(Materials2Materials.Lead, Materials2Shapes.shapeDust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            0.5F,
            MaterialLibAPI.getStack(Materials2Materials.Nickel, Materials2Shapes.shapeDust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            0.5F,
            MaterialLibAPI.getStack(Materials2Materials.Zinc, Materials2Shapes.shapeDust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            0.5F,
            MaterialLibAPI.getStack(Materials2Materials.Brass, Materials2Shapes.shapeDust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            0.5F,
            MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.shapeDust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            1.5F,
            MaterialLibAPI.getStack(Materials2Materials.Obsidian, Materials2Shapes.shapeDust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            1.5F,
            MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.shapeDust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            2.0F,
            MaterialLibAPI.getStack(Materials2Materials.Saltpeter, Materials2Shapes.shapeDust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            2.0F,
            MaterialLibAPI.getStack(Materials2Materials.Lazurite, Materials2Shapes.shapeDust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            2.0F,
            MaterialLibAPI.getStack(Materials2Materials.Pyrite, Materials2Shapes.shapeDust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            2.0F,
            MaterialLibAPI.getStack(Materials2Materials.Calcite, Materials2Shapes.shapeDust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            2.0F,
            MaterialLibAPI.getStack(Materials2Materials.Sodalite, Materials2Shapes.shapeDust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            4.0F,
            MaterialLibAPI.getStack(Materials2Materials.Netherrack, Materials2Shapes.shapeDust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            4.0F,
            MaterialLibAPI.getStack(Materials2Materials.Flint, Materials2Shapes.shapeDust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            0.03F,
            MaterialLibAPI.getStack(Materials2Materials.Platinum, Materials2Shapes.shapeDust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            0.03F,
            MaterialLibAPI.getStack(Materials2Materials.Tungsten, Materials2Shapes.shapeDust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            0.03F,
            MaterialLibAPI.getStack(Materials2Materials.Chrome, Materials2Shapes.shapeDust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            0.03F,
            MaterialLibAPI.getStack(Materials2Materials.Titanium, Materials2Shapes.shapeDust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            0.03F,
            MaterialLibAPI.getStack(Materials2Materials.Magnesium, Materials2Shapes.shapeDust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            0.03F,
            MaterialLibAPI.getStack(Materials2Materials.Endstone, Materials2Shapes.shapeDust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            0.5F,
            MaterialLibAPI.getStack(Materials2Materials.GarnetRed, Materials2Shapes.shapeDust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            0.5F,
            MaterialLibAPI.getStack(Materials2Materials.GarnetYellow, Materials2Shapes.shapeDust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            0.05F,
            MaterialLibAPI.getStack(Materials2Materials.Olivine, Materials2Shapes.shapeGem, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            0.05F,
            MaterialLibAPI.getStack(Materials2Materials.Ruby, Materials2Shapes.shapeGem, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            0.05F,
            MaterialLibAPI.getStack(Materials2Materials.Sapphire, Materials2Shapes.shapeGem, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            0.05F,
            MaterialLibAPI.getStack(Materials2Materials.GreenSapphire, Materials2Shapes.shapeGem, (int) (1L)));
        GTModHandler.addScrapboxDrop(0.05F, GTOreDictUnificator.get(OrePrefixes.gem, Materials.Emerald, 1L));
        GTModHandler.addScrapboxDrop(0.05F, GTOreDictUnificator.get(OrePrefixes.gem, Materials.Diamond, 1L));
    }
}

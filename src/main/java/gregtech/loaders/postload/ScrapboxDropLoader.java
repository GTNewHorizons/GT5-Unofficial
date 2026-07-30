package gregtech.loaders.postload;

import gregtech.api.enums.materials2.Materials;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
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
            MaterialLibAPI.getStack(Materials.Silicon, Materials2Shapes.dust, (int) (1L)));
        GTModHandler.addScrapboxDrop(1.0F, GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1L));
        GTModHandler.addScrapboxDrop(2.0F, ItemList.Cell_Empty.get(1L));
        GTModHandler.addScrapboxDrop(5.0F, GTOreDictUnificator.get(OrePrefixes.plate, Materials.Paper, 1L));
        GTModHandler.addScrapboxDrop(1.0F, new ItemStack(Items.leather));
        GTModHandler.addScrapboxDrop(1.0F, new ItemStack(Items.feather));
        GTModHandler.addScrapboxDrop(0.7F, ItemList.IC2_Plantball.get(1L));
        GTModHandler.addScrapboxDrop(
            3.8F,
            MaterialLibAPI.getStack(Materials.Wood, Materials2Shapes.dust, (int) (1L)));
        GTModHandler.addScrapboxDrop(0.6F, new ItemStack(Items.slime_ball));
        GTModHandler.addScrapboxDrop(
            0.8F,
            MaterialLibAPI.getStack(Materials.Rubber, Materials2Shapes.dust, (int) (1L)));
        GTModHandler.addScrapboxDrop(2.7F, ItemList.IC2_SuBattery.get(1L));
        GTModHandler.addScrapboxDrop(3.6F, ItemList.Circuit_Primitive.get(1L));
        GTModHandler.addScrapboxDrop(0.8F, ItemList.Circuit_Parts_Advanced.get(1L));
        GTModHandler.addScrapboxDrop(1.8F, ItemList.Circuit_Board_Basic.get(1L));
        GTModHandler.addScrapboxDrop(0.4F, ItemList.Circuit_Board_Advanced.get(1L));
        GTModHandler.addScrapboxDrop(0.2F, ItemList.Circuit_Board_Elite.get(1L));
        GTModHandler.addScrapboxDrop(
            0.9F,
            MaterialLibAPI.getStack(Materials.Redstone, Materials2Shapes.dust, (int) (1)));
        GTModHandler.addScrapboxDrop(
            0.8F,
            MaterialLibAPI.getStack(Materials.Glowstone, Materials2Shapes.dust, (int) (1)));
        GTModHandler.addScrapboxDrop(
            0.8F,
            MaterialLibAPI.getStack(Materials.Coal, Materials2Shapes.dust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            2.5F,
            MaterialLibAPI.getStack(Materials.Charcoal, Materials2Shapes.dust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            1.0F,
            MaterialLibAPI.getStack(Materials.Iron, Materials2Shapes.dust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            1.0F,
            MaterialLibAPI.getStack(Materials.Gold, Materials2Shapes.dust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            0.5F,
            MaterialLibAPI.getStack(Materials.Silver, Materials2Shapes.dust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            0.5F,
            MaterialLibAPI.getStack(Materials.Electrum, Materials2Shapes.dust, (int) (1L)));
        GTModHandler
            .addScrapboxDrop(1.2F, MaterialLibAPI.getStack(Materials.Tin, Materials2Shapes.dust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            1.2F,
            MaterialLibAPI.getStack(Materials.Copper, Materials2Shapes.dust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            0.5F,
            MaterialLibAPI.getStack(Materials.Bauxite, Materials2Shapes.dust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            0.5F,
            MaterialLibAPI.getStack(Materials.Aluminium, Materials2Shapes.dust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            0.5F,
            MaterialLibAPI.getStack(Materials.Lead, Materials2Shapes.dust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            0.5F,
            MaterialLibAPI.getStack(Materials.Nickel, Materials2Shapes.dust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            0.5F,
            MaterialLibAPI.getStack(Materials.Zinc, Materials2Shapes.dust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            0.5F,
            MaterialLibAPI.getStack(Materials.Brass, Materials2Shapes.dust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            0.5F,
            MaterialLibAPI.getStack(Materials.Steel, Materials2Shapes.dust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            1.5F,
            MaterialLibAPI.getStack(Materials.Obsidian, Materials2Shapes.dust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            1.5F,
            MaterialLibAPI.getStack(Materials.Sulfur, Materials2Shapes.dust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            2.0F,
            MaterialLibAPI.getStack(Materials.Saltpeter, Materials2Shapes.dust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            2.0F,
            MaterialLibAPI.getStack(Materials.Lazurite, Materials2Shapes.dust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            2.0F,
            MaterialLibAPI.getStack(Materials.Pyrite, Materials2Shapes.dust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            2.0F,
            MaterialLibAPI.getStack(Materials.Calcite, Materials2Shapes.dust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            2.0F,
            MaterialLibAPI.getStack(Materials.Sodalite, Materials2Shapes.dust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            4.0F,
            MaterialLibAPI.getStack(Materials.Netherrack, Materials2Shapes.dust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            4.0F,
            MaterialLibAPI.getStack(Materials.Flint, Materials2Shapes.dust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            0.03F,
            MaterialLibAPI.getStack(Materials.Platinum, Materials2Shapes.dust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            0.03F,
            MaterialLibAPI.getStack(Materials.Tungsten, Materials2Shapes.dust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            0.03F,
            MaterialLibAPI.getStack(Materials.Chrome, Materials2Shapes.dust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            0.03F,
            MaterialLibAPI.getStack(Materials.Titanium, Materials2Shapes.dust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            0.03F,
            MaterialLibAPI.getStack(Materials.Magnesium, Materials2Shapes.dust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            0.03F,
            MaterialLibAPI.getStack(Materials.Endstone, Materials2Shapes.dust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            0.5F,
            MaterialLibAPI.getStack(Materials.GarnetRed, Materials2Shapes.dust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            0.5F,
            MaterialLibAPI.getStack(Materials.GarnetYellow, Materials2Shapes.dust, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            0.05F,
            MaterialLibAPI.getStack(Materials.Olivine, Materials2Shapes.gem, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            0.05F,
            MaterialLibAPI.getStack(Materials.Ruby, Materials2Shapes.gem, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            0.05F,
            MaterialLibAPI.getStack(Materials.Sapphire, Materials2Shapes.gem, (int) (1L)));
        GTModHandler.addScrapboxDrop(
            0.05F,
            MaterialLibAPI.getStack(Materials.GreenSapphire, Materials2Shapes.gem, (int) (1L)));
        GTModHandler.addScrapboxDrop(0.05F, GTOreDictUnificator.get(OrePrefixes.gem, Materials.Emerald, 1L));
        GTModHandler.addScrapboxDrop(0.05F, GTOreDictUnificator.get(OrePrefixes.gem, Materials.Diamond, 1L));
    }
}

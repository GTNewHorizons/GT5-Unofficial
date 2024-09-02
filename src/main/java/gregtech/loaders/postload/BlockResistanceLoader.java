package gregtech.loaders.postload;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemPickaxe;

import gregtech.api.util.GTUtility;

public class BlockResistanceLoader implements Runnable {

    @Override
    @SuppressWarnings("unchecked")
    public void run() {
        Blocks.stone.setResistance(10.0F);
        Blocks.cobblestone.setResistance(10.0F);
        Blocks.stonebrick.setResistance(10.0F);
        Blocks.brick_block.setResistance(20.0F);
        Blocks.hardened_clay.setResistance(15.0F);
        Blocks.stained_hardened_clay.setResistance(15.0F);

        Blocks.bed.setHarvestLevel("axe", 0);
        Blocks.hay_block.setHarvestLevel("axe", 0);
        Blocks.tnt.setHarvestLevel("pickaxe", 0);
        Blocks.sponge.setHarvestLevel("axe", 0);
        Blocks.monster_egg.setHarvestLevel("pickaxe", 0);

        GTUtility.callMethod(Material.tnt, "func_85158_p", true, false, false);
        GTUtility.callMethod(Material.tnt, "setAdventureModeExempt", true, false, false);

        Set<Block> tSet = (Set<Block>) GTUtility.getFieldContent(ItemAxe.class, "field_150917_c", true, true);
        tSet.add(Blocks.bed);
        tSet.add(Blocks.hay_block);
        tSet.add(Blocks.sponge);

        tSet = (Set<Block>) GTUtility.getFieldContent(ItemPickaxe.class, "field_150915_c", true, true);
        tSet.add(Blocks.monster_egg);
        tSet.add(Blocks.tnt);
    }
}

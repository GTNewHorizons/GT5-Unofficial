package gtPlusPlus.xmod.bop.blocks.rainforest;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenTrees;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GTValues;
import gtPlusPlus.xmod.bop.blocks.base.SaplingBase;
import gtPlusPlus.xmod.bop.world.features.trees.WorldGenBrickuoia;

public class SaplingBrickuoia extends SaplingBase {

    public static class SaplingBrickuoiaItem extends ItemBlock {

        public SaplingBrickuoiaItem(Block p_i45328_1_) {
            super(p_i45328_1_);
        }

        @SideOnly(Side.CLIENT)
        @Override
        public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
            tooltip.add(EnumChatFormatting.ITALIC + "Your scientists were so preoccupied with whether they could,");
            tooltip.add(EnumChatFormatting.ITALIC + "they didn’t stop to think if they should");
            tooltip.add("");
            tooltip.add(EnumChatFormatting.WHITE + "Author: " + GTValues.AuthorHighPressureRaven);
        }
    }

    public SaplingBrickuoia() {
        super("Giant Brickuoia Sapling", "brickuoia", new String[] { "brickuoia" }, SaplingBrickuoiaItem.class);
    }

    @Override
    public void func_149878_d(World world, int x, int y, int z, Random rand) {
        if (!net.minecraftforge.event.terraingen.TerrainGen.saplingGrowTree(world, rand, x, y, z)) return;
        int l = world.getBlockMetadata(x, y, z) & 7;
        rand.nextInt(10);
        new WorldGenBigTree(true);
        new WorldGenTrees(true);
        int i1 = 0;
        int j1 = 0;

        final Block air = Blocks.air;

        world.setBlock(x, y, z, air, 0, 4);
        WorldGenBrickuoia o = new WorldGenBrickuoia(Blocks.brick_block, Blocks.clay, 0, 0, true, 50, 75);

        if (!o.generate(world, rand, x + i1, y, z + j1)) {
            world.setBlock(x, y, z, this, l, 4);
        }
    }
}

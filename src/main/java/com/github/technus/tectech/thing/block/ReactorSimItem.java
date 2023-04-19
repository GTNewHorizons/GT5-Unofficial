package com.github.technus.tectech.thing.block;

import static com.github.technus.tectech.util.CommonValues.TEC_MARK_GENERAL;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

/**
 * Created by danie_000 on 30.09.2017.
 */
public class ReactorSimItem extends ItemBlock {

    public static QuantumGlassItem INSTANCE;

    public ReactorSimItem(Block block) {
        super(block);
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List<String> aList, boolean aF3_H) {
        aList.add(TEC_MARK_GENERAL);
        aList.add(translateToLocal("tile.reactorSim.desc.0")); // Fission Reaction Uncertainty Resolver 9001
        aList.add(
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD
                        + translateToLocal("tile.reactorSim.desc.1")); // Explodes, but not as much...
    }
}

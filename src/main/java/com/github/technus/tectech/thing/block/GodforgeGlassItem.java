package com.github.technus.tectech.thing.block;

import static com.github.technus.tectech.util.CommonValues.GODFORGE_MARK;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class GodforgeGlassItem extends ItemBlock {

    public static GodforgeGlassItem INSTANCE;

    public GodforgeGlassItem(Block block) {
        super(block);
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List<String> aList, boolean aF3_H) {
        aList.add(GODFORGE_MARK);
        aList.add(translateToLocal("tile.godforgeGlass.desc.0"));
        aList.add(
            EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD
                + translateToLocal("tile.godforgeGlass.desc.1"));
    }
}

package tectech.thing.block;

import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import tectech.util.CommonValues;

/**
 * Created by danie_000 on 30.09.2017.
 */
public class ItemReactorSim extends ItemBlock {

    public static ItemQuantumGlass INSTANCE;

    public ItemReactorSim(Block block) {
        super(block);
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List<String> aList, boolean aF3_H) {
        aList.add(CommonValues.TEC_MARK_GENERAL);
        aList.add(translateToLocal("tile.reactorSim.desc.0")); // Fission Reaction Uncertainty Resolver 9001
        aList.add(
            EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + translateToLocal("tile.reactorSim.desc.1")); // Explodes,
                                                                                                                        // but
                                                                                                                        // not
                                                                                                                        // as
                                                                                                                        // much...
    }
}

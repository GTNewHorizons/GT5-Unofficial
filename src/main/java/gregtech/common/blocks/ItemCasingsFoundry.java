package gregtech.common.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

public class ItemCasingsFoundry extends ItemCasings {

    public ItemCasingsFoundry(Block block) {
        super(block);
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List<String> tooltip, boolean aF3_H) {
        super.addInformation(aStack, aPlayer, tooltip, aF3_H);
        switch (aStack.getItemDamage()) {
            case 1 -> tooltip.add(createFoundryFlavorText("gt.blockmachines.multimachine.foundry.chassis1"));
            case 2 -> tooltip.add(createFoundryFlavorText("gt.blockmachines.multimachine.foundry.chassis2"));
            case 3 -> tooltip.add(createFoundryFlavorText("gt.blockmachines.multimachine.foundry.chassis3"));
            case 4 -> tooltip.add(createFoundryFlavorText("gt.blockmachines.multimachine.foundry.timedilation"));
            case 5 -> tooltip.add(createFoundryFlavorText("gt.blockmachines.multimachine.foundry.efficientoc"));
            case 6 -> tooltip.add(createFoundryFlavorText("gt.blockmachines.multimachine.foundry.powerefficient"));
            case 7 -> tooltip
                .add(createFoundryFlavorText("gt.blockmachines.multimachine.foundry.transcendentreinforcement"));
            case 8 -> tooltip.add(createFoundryFlavorText("gt.blockmachines.multimachine.foundry.parallel"));
            case 9 -> tooltip.add(createFoundryFlavorText("gt.blockmachines.multimachine.foundry.hypercooler"));
            case 10 -> tooltip.add(createFoundryFlavorText("gt.blockmachines.multimachine.foundry.speed"));
        }
    }

    private String createFoundryFlavorText(String key) {
        return EnumChatFormatting.RED + "" + EnumChatFormatting.BOLD + StatCollector.translateToLocal(key);
    }
}

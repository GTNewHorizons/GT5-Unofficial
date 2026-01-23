package tectech.thing.casing;

import static com.google.common.math.LongMath.pow;
import static gregtech.api.enums.GTValues.AuthorColen;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import gregtech.common.blocks.ItemCasings;

public class ItemCasingsSpacetime extends ItemCasings {

    public ItemCasingsSpacetime(Block par1) {
        super(par1);
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List<String> tooltip, boolean aF3_H) {
        switch (aStack.getItemDamage()) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                tooltip.add(
                    StatCollector.translateToLocalFormatted(
                        "tt.eoh.spacetime.standard.tooltip.0",
                        formatNumbers(pow(10, 5 + aStack.getItemDamage()))));
                tooltip.add(
                    StatCollector
                        .translateToLocalFormatted("tt.eoh.spacetime.standard.tooltip.1", aStack.getItemDamage() + 1));
                break;
            default:
                tooltip.add(StatCollector.translateToLocal("tt.eoh.time_dilation.error.tooltip"));
        }
        tooltip.add(AuthorColen);
    }
}

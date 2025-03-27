package tectech.thing.casing;

import static gregtech.api.enums.GTValues.AuthorColen;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import gregtech.api.util.GTLanguageManager;
import gregtech.common.blocks.ItemCasings;

public class ItemCasingsStabilisation extends ItemCasings {

    public ItemCasingsStabilisation(Block par1) {
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
                    EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD
                        + GTLanguageManager.addStringLocalization(
                            "EOH.Stability.Tooltip.0",
                            "Increases stability of spacetime field."));
                break;
            default:
                tooltip.add(
                    EnumChatFormatting.RED.toString() + EnumChatFormatting.BOLD
                        + GTLanguageManager
                            .addStringLocalization("EOH.TimeDilation.Error.Tooltip", "Error, report to GTNH team"));
        }
        tooltip.add(AuthorColen);
    }
}

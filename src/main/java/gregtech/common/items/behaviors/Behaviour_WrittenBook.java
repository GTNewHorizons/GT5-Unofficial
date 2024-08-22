package gregtech.common.items.behaviors;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.items.MetaBaseItem;
import gregtech.api.util.GT_Utility;

public class Behaviour_WrittenBook extends Behaviour_None {

    @Override
    @SideOnly(Side.CLIENT)
    public boolean onItemUse(MetaBaseItem aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY,
        int aZ, int ordinalSide, float hitX, float hitY, float hitZ) {
        if ((GT_Utility.isStringValid(GT_Utility.ItemNBT.getBookTitle(aStack)))
            && ((aPlayer instanceof EntityPlayerSP))) {
            Minecraft.getMinecraft()
                .displayGuiScreen(new GuiScreenBook(aPlayer, aStack, false));
        }
        return true;
    }

    @Override
    public List<String> getAdditionalToolTips(MetaBaseItem aItem, List<String> aList, ItemStack aStack) {
        String tTitle = GT_Utility.ItemNBT.getBookTitle(aStack);
        if (GT_Utility.isStringValid(tTitle)) {
            aList.add(tTitle);
            aList.add("by " + GT_Utility.ItemNBT.getBookAuthor(aStack));
        }
        return aList;
    }
}

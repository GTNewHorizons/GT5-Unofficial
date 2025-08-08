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
import gregtech.api.util.GTUtility;

public class BehaviourWrittenBook extends BehaviourNone {

    @Override
    @SideOnly(Side.CLIENT)
    public boolean onItemUse(MetaBaseItem aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY,
        int aZ, int ordinalSide, float hitX, float hitY, float hitZ) {
        if ((GTUtility.isStringValid(GTUtility.ItemNBT.getBookTitle(aStack)))
            && ((aPlayer instanceof EntityPlayerSP))) {
            Minecraft.getMinecraft()
                .displayGuiScreen(new GuiScreenBook(aPlayer, aStack, false));
        }
        return true;
    }

    @Override
    public List<String> getAdditionalToolTips(MetaBaseItem aItem, List<String> aList, ItemStack aStack) {
        String tTitle = GTUtility.ItemNBT.getBookTitle(aStack);
        if (GTUtility.isStringValid(tTitle)) {
            aList.add(tTitle);
            aList.add("by " + GTUtility.ItemNBT.getBookAuthor(aStack));
        }
        return aList;
    }
}

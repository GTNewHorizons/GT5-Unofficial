package gregtech.common.items.behaviors;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.IItemBehaviour;
import gregtech.api.items.GT_MetaBase_Item;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Utility;

public class Behaviour_Scanner extends Behaviour_None {

    public static final IItemBehaviour<GT_MetaBase_Item> INSTANCE = new Behaviour_Scanner();
    private final String mTooltip = GT_LanguageManager
        .addStringLocalization("gt.behaviour.scanning", "Can scan Blocks in World");

    @Override
    public boolean onItemUseFirst(GT_MetaBase_Item aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX,
        int aY, int aZ, ForgeDirection side, float hitX, float hitY, float hitZ) {
        final NBTTagCompound tNBT = aStack.getTagCompound();
        if (((aPlayer instanceof EntityPlayerMP)) && (aItem.canUse(aStack, 20000.0D))) {
            final ArrayList<String> tList = new ArrayList<>();
            if (aItem.use(
                aStack,
                GT_Utility.getCoordinateScan(tList, aPlayer, aWorld, 1, aX, aY, aZ, side, hitX, hitY, hitZ),
                aPlayer)) {
                final int tList_sS = tList.size();
                tNBT.setInteger("dataLinesCount", tList_sS);
                for (int i = 0; i < tList_sS; i++) {
                    tNBT.setString("dataLines" + i, tList.get(i));
                    GT_Utility.sendChatToPlayer(aPlayer, tList.get(i));
                }
            }
            return true;
        }
        GT_Utility.doSoundAtClient(SoundResource.IC2_TOOLS_OD_SCANNER, 1, 1.0F, aX, aY, aZ);
        // doGuiAtClient()
        return aPlayer instanceof EntityPlayerMP;
    }

    @Override
    public List<String> getAdditionalToolTips(GT_MetaBase_Item aItem, List<String> aList, ItemStack aStack) {
        try {
            NBTTagCompound tNBT = aStack.getTagCompound();
            int lines = tNBT.getInteger("dataLinesCount");
            if (lines < 1) throw new Exception();
            aList.add(EnumChatFormatting.BLUE + "Block scan data result:");
            for (int i = 0; i < lines; i++) {
                aList.add(EnumChatFormatting.RESET + tNBT.getString("dataLines" + i));
            }
        } catch (Exception e) {
            aList.add(this.mTooltip);
        }
        return aList;
    }
}

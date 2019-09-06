package gregtech.common.items.behaviors;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.IItemBehaviour;
import gregtech.api.items.GT_MetaBase_Item;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class Behaviour_Scanner
        extends Behaviour_None {
    public static final IItemBehaviour<GT_MetaBase_Item> INSTANCE = new Behaviour_Scanner();
    private final String mTooltip = GT_LanguageManager.addStringLocalization("gt.behaviour.scanning", "Can scan Blocks in World");

    public boolean onItemUseFirst(GT_MetaBase_Item aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int aSide, float hitX, float hitY, float hitZ) {
        NBTTagCompound tNBT = aStack.getTagCompound();
        if (((aPlayer instanceof EntityPlayerMP)) && (aItem.canUse(aStack, 20000.0D))) {
            ArrayList<String> tList = new ArrayList();
            if (aItem.use(aStack, GT_Utility.getCoordinateScan(tList, aPlayer, aWorld, 1, aX, aY, aZ, aSide, hitX, hitY, hitZ), aPlayer)) {
                int tList_sS=tList.size();
                tNBT.setInteger("dataLinesCount",tList_sS);
                for (int i = 0; i < tList_sS; i++) {
                    tNBT.setString("dataLines"+Integer.toString(i),(String) tList.get(i));
                    GT_Utility.sendChatToPlayer(aPlayer, (String) tList.get(i));
                }
            }
            return true;
        }
        GT_Utility.doSoundAtClient((String) GregTech_API.sSoundList.get(Integer.valueOf(108)), 1, 1.0F, aX, aY, aZ);
        //doGuiAtClient()
        return aPlayer instanceof EntityPlayerMP;
    }

    public List<String> getAdditionalToolTips(GT_MetaBase_Item aItem, List<String> aList, ItemStack aStack) {
        try {
            NBTTagCompound tNBT = aStack.getTagCompound();
            int lines = tNBT.getInteger("dataLinesCount");
            if(lines<1) throw new Exception();
            aList.add(EnumChatFormatting.BLUE+"Block scan data result:");
            for (int i = 0; i < lines; i++) {
                aList.add(EnumChatFormatting.RESET+tNBT.getString("dataLines" + Integer.toString(i)));
            }
        }catch(Exception e){
            aList.add(this.mTooltip);
        }
        return aList;
    }

    //public static boolean doGuiAtClient() {
    //    if (!FMLCommonHandler.instance().getEffectiveSide().isClient() || GT.getThePlayer() == null || !GT.getThePlayer().worldObj.isRemote)
    //        return false;
    //    //GUI render start HERE
    //    return true;
    //}
}

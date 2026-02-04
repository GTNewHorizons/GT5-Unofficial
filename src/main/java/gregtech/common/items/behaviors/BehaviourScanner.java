package gregtech.common.items.behaviors;

import java.util.ArrayList;
import java.util.List;

import gregtech.api.util.scanner.ScannerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.IItemBehaviour;
import gregtech.api.items.MetaBaseItem;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTUtility;

public class BehaviourScanner extends BehaviourNone {

    public static final IItemBehaviour<MetaBaseItem> INSTANCE = new BehaviourScanner();
    private final String mTooltip = GTLanguageManager
        .addStringLocalization("gt.behaviour.scanning", "Can scan Blocks in World");

    @Override
    public boolean onItemUseFirst(MetaBaseItem aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX,
        int aY, int aZ, ForgeDirection side, float hitX, float hitY, float hitZ) {
        final NBTTagCompound tNBT = aStack.getTagCompound();
        if (((aPlayer instanceof EntityPlayerMP)) && (aItem.canUse(aStack, 20000.0D))) {
            final List<String> list = new ArrayList<>();
            if (aItem.use(
                aStack,
                ScannerHelper.scan(list, aPlayer, aWorld, 1, aX, aY, aZ, side, hitX, hitY, hitZ),
                aPlayer)) {
                final int tList_sS = list.size();
                tNBT.setInteger("dataLinesCount", tList_sS);
                for (int i = 0; i < tList_sS; i++) {
                    tNBT.setString("dataLines" + i, list.get(i));
                    GTUtility.sendChatToPlayer(aPlayer, list.get(i));
                }
            }
            return true;
        }
        GTUtility.doSoundAtClient(SoundResource.GTCEU_OP_PORTABLE_SCANNER, 1, 1.0F, aX, aY, aZ);
        // doGuiAtClient()
        return aPlayer instanceof EntityPlayerMP;
    }

    @Override
    public List<String> getAdditionalToolTips(MetaBaseItem aItem, List<String> aList, ItemStack aStack) {
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

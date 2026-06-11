package gregtech.common.items.behaviors;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.gtnhlib.item.ItemStackNBT;

import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.IItemBehaviour;
import gregtech.api.items.MetaBaseItem;
import gregtech.api.util.GTUtility;
import gregtech.api.util.scanner.ScannerHelper;

public class BehaviourScanner extends BehaviourNone {

    public static final IItemBehaviour<MetaBaseItem> INSTANCE = new BehaviourScanner();

    private static final byte TY_TEXT = 0;
    private static final byte TY_TRANSLATION = 1;
    /** Arg subtype: plain string value */
    private static final byte ARG_STRING = 0;
    /** Arg subtype: translation key - client will create {@link ChatComponentTranslation} for it */
    private static final byte ARG_KEY = 1;

    /** Serializes an {@link IChatComponent} to NBT recursively, without JSON. */
    private static NBTTagCompound componentToNBT(IChatComponent comp) {
        NBTTagCompound tag = new NBTTagCompound();
        if (comp instanceof ChatComponentTranslation trans) {
            tag.setByte("ty", TY_TRANSLATION);
            tag.setString("k", trans.getKey());
            Object[] args = trans.getFormatArgs();
            if (args != null && args.length > 0) {
                NBTTagList argList = new NBTTagList();
                for (Object arg : args) {
                    NBTTagCompound argTag = new NBTTagCompound();
                    if (arg instanceof ChatComponentTranslation argTrans) {
                        argTag.setByte("at", ARG_KEY);
                        argTag.setString("av", argTrans.getKey());
                    } else {
                        argTag.setByte("at", ARG_STRING);
                        argTag.setString("av", arg.toString());
                    }
                    argList.appendTag(argTag);
                }
                tag.setTag("a", argList);
            }
        } else {
            // Plain text component: store raw text (may contain inline §-codes) and siblings recursively.
            // getUnformattedTextForChat() is declared on IChatComponent, safe to call server-side.
            tag.setByte("ty", TY_TEXT);
            tag.setString("s", comp.getUnformattedTextForChat());
        }
        List<IChatComponent> siblings = comp.getSiblings();
        if (siblings != null && !siblings.isEmpty()) {
            NBTTagList sibList = new NBTTagList();
            for (IChatComponent sib : siblings) {
                sibList.appendTag(componentToNBT(sib));
            }
            tag.setTag("sibs", sibList);
        }
        return tag;
    }

    /** Deserializes a component from NBT, restoring siblings recursively. */
    private static IChatComponent componentFromNBT(NBTTagCompound tag) {
        IChatComponent comp;
        if (tag.getByte("ty") == TY_TRANSLATION) {
            String key = tag.getString("k");
            if (tag.hasKey("a")) {
                NBTTagList argList = tag.getTagList("a", 10);
                Object[] args = new Object[argList.tagCount()];
                for (int i = 0; i < argList.tagCount(); i++) {
                    NBTTagCompound argTag = argList.getCompoundTagAt(i);
                    String val = argTag.getString("av");
                    args[i] = argTag.getByte("at") == ARG_KEY ? new ChatComponentTranslation(val) : val;
                }
                comp = new ChatComponentTranslation(key, args);
            } else {
                comp = new ChatComponentTranslation(key);
            }
        } else {
            comp = new ChatComponentText(tag.getString("s"));
        }
        if (tag.hasKey("sibs")) {
            NBTTagList sibList = tag.getTagList("sibs", 10);
            for (int i = 0; i < sibList.tagCount(); i++) {
                comp.appendSibling(componentFromNBT(sibList.getCompoundTagAt(i)));
            }
        }
        return comp;
    }

    @Override
    public boolean onItemUseFirst(MetaBaseItem aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX,
        int aY, int aZ, ForgeDirection side, float hitX, float hitY, float hitZ) {
        final NBTTagCompound tNBT = aStack.getTagCompound();
        if (((aPlayer instanceof EntityPlayerMP)) && (aItem.canUse(aStack, 20000.0D))) {
            final List<IChatComponent> list = new ArrayList<>();
            if (aItem.use(
                aStack,
                ScannerHelper.scanComponents(list, aPlayer, aWorld, 1, aX, aY, aZ, side, hitX, hitY, hitZ),
                aPlayer)) {
                final int count = list.size();
                tNBT.setInteger("dataLinesCount", count);
                for (int i = 0; i < count; i++) {
                    tNBT.setTag("scanLine" + i, componentToNBT(list.get(i)));
                    aPlayer.addChatComponentMessage(list.get(i));
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
        final int lines = ItemStackNBT.getInteger(aStack, "dataLinesCount");
        if (0 < lines) {
            aList.add(EnumChatFormatting.BLUE + StatCollector.translateToLocal("gt.behaviour.scanning.result"));
            final NBTTagCompound nbt = aStack.getTagCompound();
            for (int i = 0; i < lines; i++) {
                if (nbt != null && nbt.hasKey("scanLine" + i)) {
                    IChatComponent comp = componentFromNBT(nbt.getCompoundTag("scanLine" + i));
                    aList.add(EnumChatFormatting.RESET + comp.getFormattedText());
                } else {
                    // fallback for items scanned before this change
                    aList.add(EnumChatFormatting.RESET + ItemStackNBT.getString(aStack, "dataLines" + i));
                }
            }
        } else {
            aList.add(StatCollector.translateToLocal("gt.behaviour.scanning"));
        }
        return aList;
    }
}

package gregtech.common.items.behaviors;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;

import gregtech.api.covers.CoverRegistry;
import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.IItemBehaviour;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.items.MetaBaseItem;
import gregtech.api.util.GTUtility;
import gregtech.common.covers.Cover;

public class BehaviourCoverTool extends BehaviourNone {

    public static final IItemBehaviour<MetaBaseItem> INSTANCE = new BehaviourCoverTool();
    private static final String NBT_COVER_ID = "mCoverId";
    private static final String NBT_COVER_DATA = "mCoverData";

    @Override
    public boolean shouldInterruptBlockActivation(final EntityPlayer player, final TileEntity tileEntity,
        final ForgeDirection side) {
        return tileEntity instanceof ICoverable;
    }

    @Override
    // Included for Ring of Loki support.
    public boolean onItemUse(final MetaBaseItem aItem, final ItemStack aStack, final EntityPlayer aPlayer,
        final World aWorld, final int aX, final int aY, final int aZ, final int ordinalSide, final float hitX,
        final float hitY, final float hitZ) {
        if (aWorld.getTileEntity(aX, aY, aZ) instanceof ICoverable) {
            final ForgeDirection side = ForgeDirection.getOrientation(ordinalSide);
            return onItemUseFirst(aItem, aStack, aPlayer, aWorld, aX, aY, aZ, side, hitX, hitY, hitZ);
        }
        return false;
    }

    @Override
    public boolean onItemUseFirst(MetaBaseItem aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX,
        int aY, int aZ, ForgeDirection side, float hitX, float hitY, float hitZ) {
        if (aWorld.isRemote) {
            return false;
        }
        final NBTTagCompound tNBT = aStack.getTagCompound();
        final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        final boolean isCopyMode = aPlayer.isSneaking();
        double useCost = isCopyMode ? 100.0D : 25.00;
        if (tNBT != null && ((aPlayer instanceof EntityPlayerMP)) && (aItem.canUse(aStack, useCost))) {
            Cover targetCover = getTargetCover(tTileEntity, side, hitX, hitY, hitZ);
            List<String> chats;
            if (tTileEntity instanceof ICoverable coverable && targetCover != CoverRegistry.NO_COVER) {
                if (isCopyMode) {
                    if (targetCover.allowsCopyPasteTool()) {
                        chats = getCopyText(targetCover, aX, aY, aZ, aWorld.provider.dimensionId);
                        writeChatsToNBT(chats, tNBT);
                        saveCoverToNBT(targetCover, tNBT);
                    } else {
                        chats = ImmutableList.of("Copy unavailable for this cover type");
                    }
                } else {
                    int copiedCoverId = tNBT.getInteger(NBT_COVER_ID);
                    if (copiedCoverId == 0) {
                        chats = ImmutableList.of("Please Copy a Valid Cover First.");
                    } else if (targetCover.getCoverID() == copiedCoverId) {
                        coverable.updateAttachedCover(
                            copiedCoverId,
                            targetCover.getSide(),
                            tNBT.getCompoundTag(NBT_COVER_DATA));
                        chats = ImmutableList.of("Cover Data Pasted.");
                    } else {
                        chats = ImmutableList.of("Not Matched Cover.");
                    }
                }
            } else {
                chats = ImmutableList.of("No Cover Found.");
            }
            sendChatsToPlayer(chats, aPlayer);
            aItem.discharge(aStack, useCost, Integer.MAX_VALUE, true, false, false);
            GTUtility.doSoundAtClient(SoundResource.GTCEU_OP_PORTABLE_SCANNER, 1, 1.0F, aX, aY, aZ);
        }
        return aPlayer instanceof EntityPlayerMP;
    }

    private static Cover getTargetCover(TileEntity tileEntity, ForgeDirection side, float hitX, float hitY,
        float hitZ) {
        if (tileEntity instanceof ICoverable coverable) {
            Cover coverAtSide = coverable.getCoverAtSide(side);
            if (coverAtSide != CoverRegistry.NO_COVER) {
                return coverAtSide;
            }
            return coverable.getCoverAtSide(GTUtility.determineWrenchingSide(side, hitX, hitY, hitZ));
        } else {
            return CoverRegistry.NO_COVER;
        }
    }

    private static @NotNull List<String> getCopyText(@NotNull Cover targetCover, int aX, int aY, int aZ,
        int dimensionId) {
        List<String> chats = new ArrayList<>();
        chats.add(
            EnumChatFormatting.STRIKETHROUGH + "-----"
                + EnumChatFormatting.RESET
                + " X: "
                + EnumChatFormatting.AQUA
                + formatNumber(aX)
                + EnumChatFormatting.RESET
                + " Y: "
                + EnumChatFormatting.AQUA
                + formatNumber(aY)
                + EnumChatFormatting.RESET
                + " Z: "
                + EnumChatFormatting.AQUA
                + formatNumber(aZ)
                + EnumChatFormatting.RESET
                + " D: "
                + EnumChatFormatting.AQUA
                + dimensionId
                + EnumChatFormatting.RESET
                + " "
                + EnumChatFormatting.STRIKETHROUGH
                + "-----");
        ForgeDirection tSide = targetCover.getSide();
        chats.add("Block Side: " + EnumChatFormatting.AQUA + tSide.name() + EnumChatFormatting.RESET);
        String coverDisplayName = targetCover.asItemStack()
            .getDisplayName();
        chats.add("Cover Type: " + EnumChatFormatting.GREEN + coverDisplayName + EnumChatFormatting.RESET);
        return chats;
    }

    private void saveCoverToNBT(Cover cover, NBTTagCompound aNBT) {
        aNBT.setInteger(NBT_COVER_ID, cover.getCoverID());
        aNBT.setTag(NBT_COVER_DATA, cover.writeToNBT(new NBTTagCompound()));
    }

    private void writeChatsToNBT(@NotNull List<String> chats, @NotNull NBTTagCompound aNBT) {
        int tSize = chats.size();
        aNBT.setInteger("dataLinesCount", tSize);
        for (int i = 0; i < tSize; i++) {
            aNBT.setString("dataLines" + i, chats.get(i));
        }
    }

    private void sendChatsToPlayer(@NotNull List<String> chats, @NotNull EntityPlayer aPlayer) {
        for (String chat : chats) {
            GTUtility.sendChatToPlayer(aPlayer, chat);
        }
    }

    @Override
    public List<String> getAdditionalToolTips(MetaBaseItem aItem, List<String> aList, ItemStack aStack) {
        try {
            final NBTTagCompound tNBT = aStack.getTagCompound();
            final int tSize = tNBT.getInteger("dataLinesCount");
            if (tSize < 1) throw new Exception();
            aList.add(EnumChatFormatting.BLUE + StatCollector.translateToLocal("gt.behaviour.cover_copy_paste.store"));
            for (int i = 0; i < tSize; i++) {
                aList.add(EnumChatFormatting.RESET + tNBT.getString("dataLines" + i));
            }
        } catch (Exception e) {
            aList.add(StatCollector.translateToLocal("gt.behaviour.cover_copy_paste"));
        }
        return aList;
    }
}

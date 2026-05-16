package gregtech.common.items.behaviors;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.github.bsideup.jabel.Desugar;
import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.gtnhlib.chat.customcomponents.ChatComponentItemName;

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
            List<IChatComponent> chats;
            if (tTileEntity instanceof ICoverable coverable && targetCover != CoverRegistry.NO_COVER) {
                if (isCopyMode) {
                    if (targetCover.allowsCopyPasteTool()) {
                        CopyData text = new CopyData(
                            targetCover.getSide(),
                            targetCover.asItemStack(),
                            aX,
                            aY,
                            aZ,
                            aWorld.provider.dimensionId);
                        chats = text.getCopyChatList();
                        text.writeToNBT(tNBT);
                        saveCoverToNBT(targetCover, tNBT);
                    } else {
                        chats = ImmutableList
                            .of(new ChatComponentTranslation("GT5U.chat.behaviour.cover_tool.unavailable"));
                    }
                } else {
                    int copiedCoverId = tNBT.getInteger(NBT_COVER_ID);
                    if (copiedCoverId == 0) {
                        chats = ImmutableList
                            .of(new ChatComponentTranslation("GT5U.chat.behaviour.cover_tool.invalid_cover"));
                    } else if (targetCover.getCoverID() == copiedCoverId) {
                        coverable.updateAttachedCover(
                            copiedCoverId,
                            targetCover.getSide(),
                            tNBT.getCompoundTag(NBT_COVER_DATA));
                        chats = ImmutableList
                            .of((new ChatComponentTranslation("GT5U.chat.behaviour.cover_tool.pasted")));
                    } else {
                        chats = ImmutableList
                            .of((new ChatComponentTranslation("GT5U.chat.behaviour.cover_tool.not_match")));
                    }
                }
            } else {
                chats = ImmutableList.of((new ChatComponentTranslation("GT5U.chat.behaviour.cover_tool.no_cover")));
            }
            for (IChatComponent chat : chats) {
                GTUtility.sendChatComp(aPlayer, chat);
            }
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

    @Desugar
    private record CopyData(ForgeDirection side, ItemStack stack, int x, int y, int z, int dimensionId) {

        public void writeToNBT(NBTTagCompound aNBT) {
            aNBT.setInteger("datalines.side", side.ordinal());
            aNBT.setTag("datalines.stack", stack.writeToNBT(new NBTTagCompound()));
            aNBT.setInteger("datalines.x", x);
            aNBT.setInteger("datalines.y", y);
            aNBT.setInteger("datalines.z", z);
            aNBT.setInteger("datalines.dimensionId", dimensionId);
        }

        public static BehaviourCoverTool.CopyData readFromNBT(NBTTagCompound aNBT) {
            if (!aNBT.hasKey("datalines.side")) {
                return null;
            }
            ForgeDirection side = ForgeDirection.getOrientation(aNBT.getInteger("datalines.side"));
            ItemStack stack = ItemStack.loadItemStackFromNBT(aNBT.getCompoundTag("datalines.stack"));
            int x = aNBT.getInteger("datalines.x");
            int y = aNBT.getInteger("datalines.y");
            int z = aNBT.getInteger("datalines.z");
            int dimensionId = aNBT.getInteger("datalines.dimensionId");
            return new CopyData(side, stack, x, y, z, dimensionId);
        }

        public @NotNull List<String> getCopyText() {
            return this.getCopyChatList()
                .stream()
                .map(IChatComponent::getFormattedText)
                .collect(Collectors.toList());
        }

        public @NotNull List<IChatComponent> getCopyChatList() {
            return ImmutableList.of(
                new ChatComponentTranslation(
                    "GT5U.chat.behaviour.cover_tool.position",
                    formatNumber(x),
                    formatNumber(y),
                    formatNumber(z),
                    dimensionId),
                new ChatComponentTranslation("GT5U.chat.behaviour.cover_tool.side", side.name()),
                new ChatComponentTranslation("GT5U.chat.behaviour.cover_tool.type", new ChatComponentItemName(stack)));
        }
    }

    private void saveCoverToNBT(Cover cover, NBTTagCompound aNBT) {
        aNBT.setInteger(NBT_COVER_ID, cover.getCoverID());
        aNBT.setTag(NBT_COVER_DATA, cover.writeToNBT(new NBTTagCompound()));
    }

    @Override
    public List<String> getAdditionalToolTips(MetaBaseItem aItem, List<String> aList, ItemStack aStack) {
        if (aStack.hasTagCompound()) {
            final NBTTagCompound tNBT = aStack.getTagCompound();
            final CopyData data = CopyData.readFromNBT(tNBT);
            if (data != null) {
                aList.add(
                    EnumChatFormatting.BLUE
                        + StatCollector.translateToLocal("GT5U.gui.tooltip.behaviour.cover_tool.data"));
                aList.addAll(data.getCopyText());
                return aList;
            }
        }
        aList.add(StatCollector.translateToLocal("GT5U.gui.tooltip.behaviour.cover_tool.cover_copy_paste"));
        return aList;
    }
}

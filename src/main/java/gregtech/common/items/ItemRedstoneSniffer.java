package gregtech.common.items;

import java.io.IOException;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.factory.GuiData;
import com.cleanroommc.modularui.factory.GuiFactories;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.items.GTGenericItem;
import gregtech.common.covers.CoverPosition;
import gregtech.common.gui.modularui.item.RedstoneSnifferGui;

public class ItemRedstoneSniffer extends GTGenericItem implements IGuiHolder<GuiData> {

    public ItemRedstoneSniffer(String aUnlocalized, String aEnglish, String aEnglishTooltip) {
        super(aUnlocalized, aEnglish, aEnglishTooltip);;
        setMaxStackSize(1);
    }

    @Override
    protected void addAdditionalToolTips(List<String> aList, ItemStack aStack, EntityPlayer aPlayer) {
        aList.add("Author: §9Frosty§4Fire1");
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {

        if (!world.isRemote) {
            GuiFactories.item()
                .open(player);

        }
        return super.onItemRightClick(stack, world, player);
    }

    @Override
    public ModularPanel buildUI(GuiData guiData, PanelSyncManager guiSyncManager, UISettings uiSettings) {
        return new RedstoneSnifferGui(guiData, guiSyncManager).build();
    }

    public static class SnifferEntry {

        public final String owner;
        public final String freq;
        public final boolean isPrivate;
        public final CoverPosition coverPosition;

        public SnifferEntry(String owner, String freq, CoverPosition coverPosition) {
            this.owner = owner;
            this.freq = freq;
            this.isPrivate = false;
            this.coverPosition = coverPosition;
        }

        public SnifferEntry(String freq, boolean isPrivate) {
            this.owner = "";
            this.freq = freq;
            this.isPrivate = isPrivate;
            this.coverPosition = null;
        }

        public static SnifferEntry deserialize(PacketBuffer buffer) throws IOException {
            String owner = buffer.readStringFromBuffer(buffer.readInt());
            String freq = buffer.readStringFromBuffer(buffer.readInt());
            boolean isPrivate = buffer.readBoolean();

            if (buffer.readBoolean()) {
                int x, y, z, dim, side;
                x = buffer.readInt();
                y = buffer.readInt();
                z = buffer.readInt();
                dim = buffer.readInt();
                side = buffer.readInt();
                String dimName = buffer.readStringFromBuffer(buffer.readInt());
                CoverPosition coverPosition = new CoverPosition(new ChunkCoordinates(x, y, z), dimName, dim, side);
                return new SnifferEntry(owner, freq, coverPosition);
            } else {
                return new SnifferEntry(freq, isPrivate);
            }

        }

        public static void serialize(PacketBuffer buffer, SnifferEntry value) throws IOException {
            buffer.writeInt(value.owner.length());
            buffer.writeStringToBuffer(value.owner);

            buffer.writeInt(value.freq.length());
            buffer.writeStringToBuffer(value.freq);

            buffer.writeBoolean(value.isPrivate);
            boolean coverPositionExists = value.coverPosition != null;
            buffer.writeBoolean(coverPositionExists);
            if (coverPositionExists) {
                buffer.writeInt(value.coverPosition.x);
                buffer.writeInt(value.coverPosition.y);
                buffer.writeInt(value.coverPosition.z);
                buffer.writeInt(value.coverPosition.dim);
                buffer.writeInt(value.coverPosition.side);
                buffer.writeInt(value.coverPosition.dimName.length());
                buffer.writeStringToBuffer(value.coverPosition.dimName);
            }
        }

        public static boolean areEqual(@NotNull SnifferEntry t1, @NotNull SnifferEntry t2) {
            if (t1.coverPosition == null && t2.coverPosition != null // ensure both entries are of the same type
                || t1.coverPosition != null && t2.coverPosition == null) return false;
            if (t1.coverPosition == null) return t1.freq.equals(t2.freq) && t1.isPrivate == t2.isPrivate;
            return t1.coverPosition.equals(t2.coverPosition) && t1.owner.equals(t2.owner) && t1.freq.equals(t2.freq);
        }
    }
}

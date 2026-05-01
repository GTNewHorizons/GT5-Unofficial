package gregtech.api.structure.error;

import java.io.IOException;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.Constants;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.github.bsideup.jabel.Desugar;

import gregtech.api.enums.StructureErrorId;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;

@Desugar
public record MissingStructureWrapperCasings(NBTTagList list) implements StructureError {

    @Override
    public StructureErrorId getId() {
        return StructureErrorId.MISSING_STRUCTURE_WRAPPER_CASINGS;
    }

    @Override
    public void serialize(PacketBuffer buffer) throws IOException {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setTag("structureWrapper", list);
        buffer.writeNBTTagCompoundToBuffer(compound);
    }

    @Override
    public StructureError deserialize(PacketBuffer buffer) throws IOException {
        NBTTagCompound compound = buffer.readNBTTagCompoundFromBuffer();
        return new MissingStructureWrapperCasings(compound.getTagList("structureWrapper", Constants.NBT.TAG_COMPOUND));
    }

    @Override
    public IWidget createWidget(MTEMultiBlockBaseGui<?> gui) {

        Flow column = Flow.column()
            .coverChildrenHeight(0)
            .crossAxisAlignment(Alignment.CrossAxis.START);

        for (NBTTagCompound tag : (List<NBTTagCompound>) list.tagList) {
            ItemStack stack = new ItemStack(
                Item.getItemById(tag.getInteger("casingId")),
                1,
                tag.getInteger("casingMeta"));
            column.child(
                IKey.str(
                    GTUtility.translate(
                        "GT5U.gui.missing_casings_specific",
                        stack.getDisplayName(),
                        tag.getInteger("req"),
                        tag.getInteger("pres")))
                    .asWidget());
        }
        return column;
    }

    @Override
    public String getDisplayString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound tag = list.getCompoundTagAt(i);
            ItemStack stack = new ItemStack(
                Item.getItemById(tag.getInteger("casingId")),
                1,
                tag.getInteger("casingMeta"));
            if (sb.length() > 0) sb.append('\n');
            sb.append(
                GTUtility.translate(
                    "GT5U.gui.missing_casings_specific",
                    stack.getDisplayName(),
                    tag.getInteger("req"),
                    tag.getInteger("pres")));
        }
        return sb.toString();
    }

    @Override
    public StructureError copy() {
        return new MissingStructureWrapperCasings((NBTTagList) list.copy());
    }
}

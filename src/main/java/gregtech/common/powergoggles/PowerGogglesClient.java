package gregtech.common.powergoggles;

import java.util.Objects;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import appeng.api.util.DimensionalCoord;

public class PowerGogglesClient {

    private DimensionalCoord lscLink;

    public void updateLscLink(ItemStack itemstack) {
        NBTTagCompound tag = itemstack.getTagCompound();
        DimensionalCoord newLink = null;
        if (tag != null && !tag.hasNoTags()) {
            newLink = new DimensionalCoord(
                tag.getInteger("x"),
                tag.getInteger("y"),
                tag.getInteger("z"),
                tag.getInteger("dim"));
        }

        if (!Objects.equals(lscLink, newLink)) {
            setLscLink(newLink);
        }
    }

    public void setLscLink(DimensionalCoord lscLink) {
        this.lscLink = lscLink;
    }
}

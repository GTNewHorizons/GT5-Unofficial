package gregtech.api.CustomStructureRendering.Trophies;

import gregtech.api.CustomStructureRendering.Base.BaseRenderItemBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Set;

import static gregtech.api.CustomStructureRendering.Base.BaseRenderTESR.MODEL_NAME_NBT_TAG;


public class BaseTrophyItemBlock extends BaseRenderItemBlock {
    public BaseTrophyItemBlock(Block block) {
        super(block);
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemStack) {

        if (itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey(MODEL_NAME_NBT_TAG)) {
            NBTTagCompound tag = itemStack.getTagCompound();

            return tag.getString(MODEL_NAME_NBT_TAG) + " Trophy";
        }

        return "NULL: Report to mod author";
    }

    @Override
    protected Set<String> getModelList() {
        return Trophies.getModelList();
    }


}

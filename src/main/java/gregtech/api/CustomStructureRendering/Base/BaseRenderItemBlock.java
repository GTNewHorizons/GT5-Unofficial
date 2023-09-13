package gregtech.api.CustomStructureRendering.Base;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static gregtech.api.CustomStructureRendering.Base.BaseRenderTESR.MODEL_NAME_NBT_TAG;


public class BaseRenderItemBlock extends ItemBlock {

    public BaseRenderItemBlock(Block block) {
        super(block);
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemStack) {
        return "Base NULL: Report to mod author";
    }

    protected Set<String> getModelList() {
        return new HashSet<>();
    }

    @Override
    public void getSubItems(Item item, CreativeTabs creativeTabs, List<ItemStack> itemList) {
        super.getSubItems(item, creativeTabs, itemList);

        // Removes the default item with no NBT in it.
        itemList.clear();

        // Add one for each model.
        for (String modelName : getModelList()) {
            ItemStack itemStack = new ItemStack(item, 1, 0);

            NBTTagCompound tag = new NBTTagCompound();
            tag.setString(MODEL_NAME_NBT_TAG, modelName);

            itemStack.setTagCompound(tag);

            itemList.add(itemStack);
        }
    }

}

package gregtech.api.ModernMaterials.Blocks.DumbBase;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.ModernMaterials.Blocks.BlocksEnum;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.List;

public abstract class DumbItemBlock extends ItemBlock {

    public DumbItemBlock(Block block) {
        super(block);
        setMaxDamage(0);
        setHasSubtypes(true);
        setCreativeTab(GregTech_API.TAB_GREGTECH); // todo add new gt frame tab.
    }

    @Override
    public boolean getHasSubtypes() {
        return true;
    }

    // Tooltip information.
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List tooltipList, boolean aF3_H)  {
        tooltipList.add("Generic Tooltip");
    }

    @Override
    public abstract String getItemStackDisplayName(ItemStack itemStack);

    public abstract BlocksEnum getBlockEnum();
}

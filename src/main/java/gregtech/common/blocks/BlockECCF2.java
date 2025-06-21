package gregtech.common.blocks;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;

public class BlockECCF2 extends BlockCasingsAbstract {

    public BlockECCF2() {
        super(ItemCasings.class, "gt.blockECCF2", MaterialCasings.INSTANCE, 8);

        register(0, ItemList.ECCF_parallel_block_0, "ECCF Parallel Module Tier 1");
        register(1, ItemList.ECCF_parallel_block_1, "ECCF Parallel Module Tier 2");
        register(2, ItemList.ECCF_parallel_block_2, "ECCF Parallel Module Tier 3");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return switch (aMeta) {
            case 0 -> Textures.BlockIcons.ECCF_PARALLEL_BLOCK_0.getIcon();
            case 1 -> Textures.BlockIcons.ECCF_PARALLEL_BLOCK_1.getIcon();
            default -> Textures.BlockIcons.ECCF_PARALLEL_BLOCK_2.getIcon();
        };
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advancedTooltips) {
        super.addInformation(stack, player, tooltip, advancedTooltips);
        switch (stack.getItemDamage()) {
            case 0 -> tooltip.add(StatCollector.translateToLocalFormatted("tooltip.blockECCFParallel", 4));
            case 1 -> tooltip.add(StatCollector.translateToLocalFormatted("tooltip.blockECCFParallel", 8));
            case 2 -> tooltip.add(StatCollector.translateToLocalFormatted("tooltip.blockECCFParallel", 16));
        }
    }
}

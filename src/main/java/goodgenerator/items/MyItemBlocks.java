package goodgenerator.items;

import static goodgenerator.loader.Loaders.essentiaCell;
import static goodgenerator.loader.Loaders.yottaFluidTankCell;
import static goodgenerator.util.CharExchanger.tierName;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import goodgenerator.blocks.regularBlock.TEBlock;
import goodgenerator.blocks.tileEntity.EssentiaOutputHatch;
import goodgenerator.main.GoodGenerator;
import goodgenerator.util.CharExchanger;
import goodgenerator.util.DescTextLocalization;
import gregtech.api.util.GT_LanguageManager;
import java.util.Arrays;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

public class MyItemBlocks extends ItemBlock {
    private final String mNoMobsToolTip =
            GT_LanguageManager.addStringLocalization("gt.nomobspawnsonthisblock", "Mobs cannot Spawn on this Block");
    private final String mNoTileEntityToolTip =
            GT_LanguageManager.addStringLocalization("gt.notileentityinthisblock", "This is NOT a TileEntity!");

    public MyItemBlocks(Block block) {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.setCreativeTab(GoodGenerator.GG);
    }

    @Override
    public int getMetadata(int aMeta) {
        return aMeta;
    }

    @Override
    public String getUnlocalizedName(ItemStack aStack) {
        return this.field_150939_a.getUnlocalizedName() + "." + this.getDamage(aStack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(ItemStack stack, int pass) {
        return this.field_150939_a.getIcon(0, stack.getItemDamage());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
        return this.getIcon(stack, renderPass);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int p_77618_1_, int p_77618_2_) {
        return this.field_150939_a.getIcon(0, p_77618_2_);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_) {
        if (p_77624_1_ == null) return;
        p_77624_3_.add(mNoMobsToolTip);
        if (Block.getBlockFromItem(p_77624_1_.getItem()) instanceof TEBlock) {
            TEBlock tile = (TEBlock) Block.getBlockFromItem(p_77624_1_.getItem());
            if (tile.getIndex() == 1)
                p_77624_3_.addAll(Arrays.asList(DescTextLocalization.addText("EssentiaHatch.tooltip", 2)));
            if (tile.getIndex() == 2) {
                p_77624_3_.add(StatCollector.translateToLocal("EssentiaOutputHatch.tooltip.0"));
                p_77624_3_.add(StatCollector.translateToLocal("EssentiaOutputHatch.tooltip.1") + " "
                        + EssentiaOutputHatch.CAPACITY);
            }
        } else p_77624_3_.add(mNoTileEntityToolTip);

        if (Block.getBlockFromItem(p_77624_1_.getItem()).equals(yottaFluidTankCell)) {
            StringBuilder cap = new StringBuilder();
            cap.append(" 1000000");
            for (int i = 0; i < p_77624_1_.getItemDamage(); i++) cap.append("00");
            cap.append(" L");
            p_77624_3_.add(StatCollector.translateToLocal("YOTTankCell.tooltip.0")
                    + CharExchanger.formatNumber(cap.toString()));
        }

        if (Block.getBlockFromItem(p_77624_1_.getItem()).equals(essentiaCell)) {
            p_77624_3_.add(StatCollector.translateToLocal("hatchTier.tooltip.0") + " "
                    + tierName[p_77624_1_.getItemDamage() + 4]);
        }
    }
}

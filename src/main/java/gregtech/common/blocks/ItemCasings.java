package gregtech.common.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemCasings extends ItemBlock {

    public final BlockCasingsAbstract blockCasings;

    public ItemCasings(Block block) {
        super(block);
        setMaxDamage(0);
        setHasSubtypes(true);

        this.blockCasings = (BlockCasingsAbstract) block;
    }

    @Override
    public int getMetadata(int aMeta) {
        return aMeta;
    }

    @Override
    public String getUnlocalizedName(ItemStack aStack) {
        return this.field_150939_a.getUnlocalizedName() + "." + blockCasings.damageDropped(getDamage(aStack));
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List<String> tooltip, boolean aF3_H) {
        super.addInformation(aStack, aPlayer, tooltip, aF3_H);
        tooltip.add(BlockCasingsAbstract.NO_MOB_SPAWNING.get());
        tooltip.add(BlockCasingsAbstract.NOT_TILE_ENTITY.get());
        blockCasings.addInformation(aStack, aPlayer, tooltip, aF3_H);
    }
}

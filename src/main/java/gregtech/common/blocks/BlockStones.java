package gregtech.common.blocks;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;

public class BlockStones extends BlockStonesAbstract {

    public BlockStones() {
        super(ItemGranites.class, "gt.blockstones");
        setResistance(60.0F);
        for (int i = 0; i < 16; i++) {
            GTOreDictUnificator
                .registerOre(OrePrefixes.stone, i < 8 ? Materials.Marble : Materials.Basalt, new ItemStack(this, 1, i));
            GTOreDictUnificator
                .registerOre(OrePrefixes.block, i < 8 ? Materials.Marble : Materials.Basalt, new ItemStack(this, 1, i));
            GTOreDictUnificator.registerOre(
                (i < 8 ? Materials.Marble.mName.toLowerCase() : Materials.Basalt.mName.toLowerCase()),
                new ItemStack(this, 1, i));
        }
    }

    @Override
    public int getHarvestLevel(int aMeta) {
        return 2;
    }

    @Override
    public IIcon getIcon(int ordinalSide, int aMeta) {
        if ((aMeta >= 0) && (aMeta < 16)) {
            return gregtech.api.enums.Textures.BlockIcons.STONES[aMeta].getIcon();
        }
        return gregtech.api.enums.Textures.BlockIcons.STONES[0].getIcon();
    }
}

package gregtech.common.blocks;

import static gregtech.api.enums.ModIDs.UndergroundBiomes;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.TextureFactory;

public class GT_Block_Ores_UB1 extends GT_Block_Ores_Abstract {

    Block aUBBlock = GameRegistry.findBlock(UndergroundBiomes.modID, "igneousStone");

    public GT_Block_Ores_UB1() {
        super("gt.blockores.ub1", 8, true, Material.rock);
        if (aUBBlock == null) aUBBlock = Blocks.stone;
    }

    @Override
    public String getUnlocalizedName() {
        return "gt.blockores.ub1";
    }

    @Override
    public OrePrefixes[] getProcessingPrefix() { // Must have 8 entries.
        return new OrePrefixes[] { OrePrefixes.oreRedgranite, OrePrefixes.oreBlackgranite, OrePrefixes.ore,
                OrePrefixes.ore, OrePrefixes.ore, OrePrefixes.oreBasalt, OrePrefixes.ore, OrePrefixes.ore };
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return aUBBlock.getIcon(side, (meta / 1000) % 8);
    }

    @Override
    public int getBaseBlockHarvestLevel(int aMeta) {
        return aUBBlock.getHarvestLevel(aMeta);
    }

    @Override
    public Block getDroppedBlock() {
        return GregTech_API.sBlockOresUb1;
    }

    @Override
    public Materials[] getDroppedDusts() { // Must have 8 entries; can be null.
        return new Materials[] { Materials.Stone, Materials.Stone, Materials.Stone, Materials.Stone, Materials.Stone,
                Materials.Stone, Materials.Stone, Materials.Stone };
    }

    @Override
    public boolean[] getEnabledMetas() {
        return new boolean[] { true, true, true, true, true, true, true, true };
    }

    @Override
    public ITexture[] getTextureSet() { // Must have 16 entries.
        return new ITexture[] { TextureFactory.of(aUBBlock, 0), TextureFactory.of(aUBBlock, 1),
                TextureFactory.of(aUBBlock, 2), TextureFactory.of(aUBBlock, 3), TextureFactory.of(aUBBlock, 4),
                TextureFactory.of(aUBBlock, 5), TextureFactory.of(aUBBlock, 6), TextureFactory.of(aUBBlock, 7),
                TextureFactory.of(aUBBlock, 0), TextureFactory.of(aUBBlock, 1), TextureFactory.of(aUBBlock, 2),
                TextureFactory.of(aUBBlock, 3), TextureFactory.of(aUBBlock, 4), TextureFactory.of(aUBBlock, 5),
                TextureFactory.of(aUBBlock, 6), TextureFactory.of(aUBBlock, 7) };
    }
}

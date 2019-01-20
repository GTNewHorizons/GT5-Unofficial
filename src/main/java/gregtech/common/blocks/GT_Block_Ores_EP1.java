package gregtech.common.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.objects.GT_CopiedBlockTexture;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;

public class GT_Block_Ores_EP1 extends GT_Block_Ores_Abstract {
    Block aMercuryBlock = GameRegistry.findBlock("ExtraPlanets", "tile.mercury");
    Block aVenusBlock = GameRegistry.findBlock("ExtraPlanets", "tile.venus");
    Block aJupiterBlock = GameRegistry.findBlock("ExtraPlanets", "tile.jupiter");
    Block aSaturnBlock = GameRegistry.findBlock("ExtraPlanets", "tile.saturn");
    Block aUranusBlock = GameRegistry.findBlock("ExtraPlanets", "tile.uranus");
    Block aNeptuneBlock = GameRegistry.findBlock("ExtraPlanets", "tile.neptune");
    Block aPlutoBlock = GameRegistry.findBlock("ExtraPlanets", "tile.pluto");
    
    public GT_Block_Ores_EP1() {
        super("gt.blockores.ep1", 7, true, Material.rock);
        if (aMercuryBlock == null) aMercuryBlock = Blocks.stone;
        if (aVenusBlock == null) aVenusBlock = Blocks.stone;
        if (aJupiterBlock == null) aJupiterBlock = Blocks.stone;
        if (aSaturnBlock == null) aSaturnBlock = Blocks.stone;
        if (aUranusBlock == null) aUranusBlock = Blocks.stone;
        if (aNeptuneBlock == null) aNeptuneBlock = Blocks.stone;
        if (aPlutoBlock == null) aPlutoBlock = Blocks.stone;
    }

    @Override
    public String getUnlocalizedName() {
        return "gt.blockores.ep1";
    }

    @Override
    public OrePrefixes[] getProcessingPrefix() { //Must have 8 entries.
        return new OrePrefixes[]{OrePrefixes.ore, OrePrefixes.ore, OrePrefixes.ore, OrePrefixes.ore, OrePrefixes.ore, OrePrefixes.ore, OrePrefixes.ore, OrePrefixes.ore};
    }

    @Override
    public Block getDroppedBlock() {
        return GregTech_API.sBlockOresEP1;
    }

    @Override
    public Materials[] getDroppedDusts() { //Must have 8 entries; can be null.
        return new Materials[]{Materials.Stone, Materials.Stone, Materials.Stone, Materials.Stone, Materials.Stone, Materials.Stone, Materials.Stone, Materials.Stone};
    }

    @Override
    public boolean[] getEnabledMetas() {
        return new boolean[]{true, true, true, true, true, true, true, true};
    }

    @Override
    public ITexture[] getTextureSet() { //Must have 16 entries.
        return new ITexture[]{new GT_CopiedBlockTexture(aMercuryBlock, 0, 2), new GT_CopiedBlockTexture(aVenusBlock, 0, 2), new GT_CopiedBlockTexture(aJupiterBlock, 0, 2), new GT_CopiedBlockTexture(aSaturnBlock, 0, 2), new GT_CopiedBlockTexture(aUranusBlock, 0, 2), new GT_CopiedBlockTexture(aNeptuneBlock, 0, 2), new GT_CopiedBlockTexture(aPlutoBlock, 0, 2), new GT_CopiedBlockTexture(Blocks.stone, 0, 0),new GT_CopiedBlockTexture(aMercuryBlock, 0, 2), new GT_CopiedBlockTexture(aVenusBlock, 0, 2), new GT_CopiedBlockTexture(aJupiterBlock, 0, 2), new GT_CopiedBlockTexture(aSaturnBlock, 0, 2), new GT_CopiedBlockTexture(aUranusBlock, 0, 2), new GT_CopiedBlockTexture(aNeptuneBlock, 0, 2), new GT_CopiedBlockTexture(aPlutoBlock, 0, 2), new GT_CopiedBlockTexture(Blocks.stone, 0, 0)};
    }
}

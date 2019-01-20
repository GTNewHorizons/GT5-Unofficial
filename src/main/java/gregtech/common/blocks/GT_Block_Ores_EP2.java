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

public class GT_Block_Ores_EP2 extends GT_Block_Ores_Abstract {
    Block aCeresBlock = GameRegistry.findBlock("ExtraPlanets", "tile.ceres");
    Block aDeimosBlock = GameRegistry.findBlock("ExtraPlanets", "tile.deimos");
    Block aPhobosBlock = GameRegistry.findBlock("ExtraPlanets", "tile.phobos");
    Block aIoBlock = GameRegistry.findBlock("ExtraPlanets", "tile.io");
    Block aEuropaBlock = GameRegistry.findBlock("ExtraPlanets", "tile.europa");
    Block aGanymedeBlock = GameRegistry.findBlock("ExtraPlanets", "tile.ganymede");
    Block aCallistoBlock = GameRegistry.findBlock("ExtraPlanets", "tile.callisto");
    Block aRheaBlock = GameRegistry.findBlock("ExtraPlanets", "tile.rhea");
    
    public GT_Block_Ores_EP2() {
        super("gt.blockores.ep2", 8, true, Material.rock);
        if (aCeresBlock == null) aCeresBlock = Blocks.stone;
        if (aDeimosBlock == null) aDeimosBlock = Blocks.stone;
        if (aPhobosBlock == null) aPhobosBlock = Blocks.stone;
        if (aIoBlock == null) aIoBlock = Blocks.stone;
        if (aEuropaBlock == null) aEuropaBlock = Blocks.stone;
        if (aGanymedeBlock == null) aGanymedeBlock = Blocks.stone;
        if (aCallistoBlock == null) aCallistoBlock = Blocks.stone;
        if (aRheaBlock == null) aRheaBlock = Blocks.stone;
    }

    @Override
    public String getUnlocalizedName() {
        return "gt.blockores.ep2";
    }

    @Override
    public OrePrefixes[] getProcessingPrefix() { //Must have 8 entries.
        return new OrePrefixes[]{OrePrefixes.ore, OrePrefixes.ore, OrePrefixes.ore, OrePrefixes.ore, OrePrefixes.ore, OrePrefixes.ore, OrePrefixes.ore, OrePrefixes.ore};
    }

    @Override
    public Block getDroppedBlock() {
        return GregTech_API.sBlockOresEP2;
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
        return new ITexture[]{new GT_CopiedBlockTexture(aCeresBlock, 0, 2), new GT_CopiedBlockTexture(aDeimosBlock, 0, 2), new GT_CopiedBlockTexture(aPhobosBlock, 0, 2), new GT_CopiedBlockTexture(aIoBlock, 0, 2), new GT_CopiedBlockTexture(aEuropaBlock, 0, 2), new GT_CopiedBlockTexture(aGanymedeBlock, 0, 2), new GT_CopiedBlockTexture(aCallistoBlock, 0, 2), new GT_CopiedBlockTexture(aRheaBlock, 0, 2),new GT_CopiedBlockTexture(aCeresBlock, 0, 2), new GT_CopiedBlockTexture(aDeimosBlock, 0, 2), new GT_CopiedBlockTexture(aPhobosBlock, 0, 2), new GT_CopiedBlockTexture(aIoBlock, 0, 2), new GT_CopiedBlockTexture(aEuropaBlock, 0, 2), new GT_CopiedBlockTexture(aGanymedeBlock, 0, 2), new GT_CopiedBlockTexture(aCallistoBlock, 0, 2), new GT_CopiedBlockTexture(aRheaBlock, 0, 2)};
    }
}

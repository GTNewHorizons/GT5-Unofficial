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

public class GT_Block_Ores_EP3 extends GT_Block_Ores_Abstract {
    Block aTitanBlock = GameRegistry.findBlock("ExtraPlanets", "tile.titan");
    Block aIapetusBlock = GameRegistry.findBlock("ExtraPlanets", "tile.iapetus");
    Block aTitaniaBlock = GameRegistry.findBlock("ExtraPlanets", "tile.titania");
    Block aOberonBlock = GameRegistry.findBlock("ExtraPlanets", "tile.oberon");
    Block aTritonBlock = GameRegistry.findBlock("ExtraPlanets", "tile.triton");
    Block aErisBlock = GameRegistry.findBlock("ExtraPlanets", "tile.eris");
    
    public GT_Block_Ores_EP3() {
        super("gt.blockores.ep3", 6, true, Material.rock);
        if (aTitanBlock == null) aTitanBlock = Blocks.stone;
        if (aIapetusBlock == null) aIapetusBlock = Blocks.stone;
        if (aTitaniaBlock == null) aTitaniaBlock = Blocks.stone;
        if (aOberonBlock == null) aOberonBlock = Blocks.stone;
        if (aTritonBlock == null) aTritonBlock = Blocks.stone;
        if (aErisBlock == null) aErisBlock = Blocks.stone;
        
    }

    @Override
    public String getUnlocalizedName() {
        return "gt.blockores.ep3";
    }

    @Override
    public OrePrefixes[] getProcessingPrefix() { //Must have 8 entries.
        return new OrePrefixes[]{OrePrefixes.ore, OrePrefixes.ore, OrePrefixes.ore, OrePrefixes.ore, OrePrefixes.ore, OrePrefixes.ore, OrePrefixes.ore, OrePrefixes.ore};
    }

    @Override
    public Block getDroppedBlock() {
        return GregTech_API.sBlockOresEP3;
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
        return new ITexture[]{new GT_CopiedBlockTexture(aTitanBlock, 0, 2), new GT_CopiedBlockTexture(aIapetusBlock, 0, 2), new GT_CopiedBlockTexture(aTitaniaBlock, 0, 2), new GT_CopiedBlockTexture(aOberonBlock, 0, 2), new GT_CopiedBlockTexture(aTritonBlock, 0, 2), new GT_CopiedBlockTexture(aErisBlock, 0, 2), new GT_CopiedBlockTexture(Blocks.stone, 0, 0), new GT_CopiedBlockTexture(Blocks.stone, 0, 0),new GT_CopiedBlockTexture(aTitanBlock, 0, 2), new GT_CopiedBlockTexture(aIapetusBlock, 0, 2), new GT_CopiedBlockTexture(aTitaniaBlock, 0, 2), new GT_CopiedBlockTexture(aOberonBlock, 0, 2), new GT_CopiedBlockTexture(aTritonBlock, 0, 2), new GT_CopiedBlockTexture(aErisBlock, 0, 2), new GT_CopiedBlockTexture(Blocks.stone, 0, 0), new GT_CopiedBlockTexture(Blocks.stone, 0, 0)};
    }
}

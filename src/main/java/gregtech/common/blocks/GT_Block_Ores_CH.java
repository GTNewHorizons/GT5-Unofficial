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

public class GT_Block_Ores_CH extends GT_Block_Ores_Abstract {
    Block aDioriteBlock = GameRegistry.findBlock("chisel", "diorite");
    Block aGraniteBlock = GameRegistry.findBlock("chisel", "granite");
    Block aLimestoneBlock = GameRegistry.findBlock("chisel", "limestone");
    Block aMarbleBlock = GameRegistry.findBlock("chisel", "marble");
    Block aAndesiteBlock = GameRegistry.findBlock("chisel", "andesite");
    
    public GT_Block_Ores_CH() {
        super("gt.blockores.ch", 5, true, Material.rock);
        if (aDioriteBlock == null) aDioriteBlock = Blocks.stone;
        if (aGraniteBlock == null) aGraniteBlock = Blocks.stone;
        if (aLimestoneBlock == null) aLimestoneBlock = Blocks.stone;
        if (aMarbleBlock == null) aMarbleBlock = Blocks.stone;
        if (aAndesiteBlock == null) aAndesiteBlock = Blocks.stone;
        
    }

    @Override
    public String getUnlocalizedName() {
        return "gt.blockores.ch";
    }

    @Override
    public OrePrefixes[] getProcessingPrefix() { //Must have 8 entries.
        return new OrePrefixes[]{OrePrefixes.ore, OrePrefixes.oreRedgranite, OrePrefixes.ore, OrePrefixes.oreMarble, OrePrefixes.ore, OrePrefixes.ore, OrePrefixes.ore, OrePrefixes.ore};
    }

    @Override
    public Block getDroppedBlock() {
        return GregTech_API.sBlockOresCh;
    }

    @Override
    public Materials[] getDroppedDusts() { //Must have 8 entries; can be null.
        return new Materials[]{Materials.Stone, Materials.GraniteRed, Materials.Stone, Materials.Marble, Materials.Stone, Materials.Stone, Materials.Stone, Materials.Stone};
    }

    @Override
    public boolean[] getEnabledMetas() {
        return new boolean[]{true, true, true, true, true, true, true, true};
    }

    @Override
    public ITexture[] getTextureSet() { //Must have 16 entries.
        return new ITexture[]{new GT_CopiedBlockTexture(aDioriteBlock, 0, 0), new GT_CopiedBlockTexture(aGraniteBlock, 0, 0), new GT_CopiedBlockTexture(aLimestoneBlock, 0, 0), new GT_CopiedBlockTexture(aMarbleBlock, 0, 0), new GT_CopiedBlockTexture(aAndesiteBlock, 0, 0), new GT_CopiedBlockTexture(Blocks.stone, 0, 0), new GT_CopiedBlockTexture(Blocks.stone, 0, 0), new GT_CopiedBlockTexture(Blocks.stone, 0, 0),new GT_CopiedBlockTexture(aDioriteBlock, 0, 0), new GT_CopiedBlockTexture(aGraniteBlock, 0, 0), new GT_CopiedBlockTexture(aLimestoneBlock, 0, 0), new GT_CopiedBlockTexture(aMarbleBlock, 0, 0), new GT_CopiedBlockTexture(aAndesiteBlock, 0, 0), new GT_CopiedBlockTexture(Blocks.stone, 0, 0), new GT_CopiedBlockTexture(Blocks.stone, 0, 0), new GT_CopiedBlockTexture(Blocks.stone, 0, 0)};
    }
}
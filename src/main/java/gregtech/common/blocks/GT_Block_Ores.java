package gregtech.common.blocks;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.TextureFactory;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;

import java.util.Arrays;

import static gregtech.api.enums.Textures.BlockIcons.BASALT_STONE;
import static gregtech.api.enums.Textures.BlockIcons.GRANITE_BLACK_STONE;
import static gregtech.api.enums.Textures.BlockIcons.GRANITE_RED_STONE;
import static gregtech.api.enums.Textures.BlockIcons.MARBLE_STONE;
import net.minecraft.util.IIcon;

public class GT_Block_Ores extends GT_Block_Ores_Abstract {
    public GT_Block_Ores() {
        super("gt.blockores", 7, false, Material.rock);
    }

    @Override
    public String getUnlocalizedName() {
        return "gt.blockores";
    }

    @Override
    public OrePrefixes[] getProcessingPrefix() { //Must have 8 entries; an entry can be null to disable automatic recipes.
        return new OrePrefixes[]{OrePrefixes.ore, OrePrefixes.oreNetherrack, OrePrefixes.oreEndstone, OrePrefixes.oreBlackgranite, OrePrefixes.oreRedgranite, OrePrefixes.oreMarble, OrePrefixes.oreBasalt, null};
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        int index = ((meta / 1000) % 16);
        IIcon[] icons = new IIcon[] {
        		Blocks.stone.getIcon(0, 0),
        		Blocks.netherrack.getIcon(0, 0),
        		Blocks.end_stone.getIcon(0, 0),
        		GRANITE_BLACK_STONE.getIcon(),
        		GRANITE_RED_STONE.getIcon(),
        		MARBLE_STONE.getIcon(),
        		BASALT_STONE.getIcon()};
        return icons[index < 0 || index >= icons.length ? 0 : index];
    }

    @Override
    public int getBaseBlockHarvestLevel(int aMeta) {
        switch (aMeta) {
            case 3:
            case 4:return 3;
            case 0:
            case 1:
            case 2:
            case 5:
            case 6:
            default:return 0;
        }
    }

    @Override
    public Block getDroppedBlock() {
        return GregTech_API.sBlockOres1;
    }

    @Override
    public Materials[] getDroppedDusts() { //Must have 8 entries; can be null.
        return new Materials[]{Materials.Stone, Materials.Netherrack, Materials.Endstone, Materials.GraniteBlack, Materials.GraniteRed, Materials.Marble, Materials.Basalt, Materials.Stone};
    }

    @Override
    public boolean[] getEnabledMetas() {
        return new boolean[]{true, true, true, GT_Mod.gregtechproxy.enableBlackGraniteOres, GT_Mod.gregtechproxy.enableRedGraniteOres, GT_Mod.gregtechproxy.enableMarbleOres, GT_Mod.gregtechproxy.enableBasaltOres, true};
    }

    @Override
    public ITexture[] getTextureSet() {
        final ITexture[] rTextures = new ITexture[16]; //Must have 16 entries.
        Arrays.fill(rTextures, TextureFactory.of(Blocks.stone));
        rTextures[1] = TextureFactory.of(Blocks.netherrack);
        rTextures[2] = TextureFactory.of(Blocks.end_stone);
        rTextures[3] = TextureFactory.builder().addIcon(GRANITE_BLACK_STONE).stdOrient().build();
        rTextures[4] = TextureFactory.builder().addIcon(GRANITE_RED_STONE).stdOrient().build();
        rTextures[5] = TextureFactory.builder().addIcon(MARBLE_STONE).stdOrient().build();
        rTextures[6] = TextureFactory.builder().addIcon(BASALT_STONE).stdOrient().build();
        return rTextures;
    }
}

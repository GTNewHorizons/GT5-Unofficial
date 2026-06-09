package gtPlusPlus.xmod.gregtech.common.blocks;

import java.util.HashMap;

import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.common.blocks.MaterialCasings;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GregtechMetaCasingBlocksPipeGearbox extends GregtechMetaCasingBlocksAbstract {

    private static final HashMap<Integer, Integer> sMaterialMapping = new HashMap<>();

    public GregtechMetaCasingBlocksPipeGearbox() {
        super(GregtechMetaCasingItems.class, "gtplusplus.blockcasings.pipesgears", MaterialCasings.INSTANCE);

        int aMappingID = 0;
        sMaterialMapping.put(aMappingID++, MaterialsAlloy.EGLIN_STEEL.getRgbAsHex());
        sMaterialMapping.put(aMappingID++, MaterialsAlloy.INCONEL_792.getRgbAsHex());
        sMaterialMapping.put(aMappingID++, MaterialsAlloy.INCOLOY_MA956.getRgbAsHex());
        sMaterialMapping.put(aMappingID++, MaterialsAlloy.NITINOL_60.getRgbAsHex());
        sMaterialMapping.put(aMappingID++, MaterialsAlloy.ZERON_100.getRgbAsHex());
        sMaterialMapping.put(aMappingID++, MaterialsAlloy.PIKYONIUM.getRgbAsHex());
        sMaterialMapping.put(aMappingID++, MaterialsAlloy.TITANSTEEL.getRgbAsHex());
        sMaterialMapping.put(aMappingID++, MaterialsAlloy.ABYSSAL.getRgbAsHex());
        sMaterialMapping.put(aMappingID++, MaterialsAlloy.BABBIT_ALLOY.getRgbAsHex());
        sMaterialMapping.put(aMappingID++, MaterialsAlloy.INCONEL_690.getRgbAsHex());
        sMaterialMapping.put(aMappingID++, MaterialsAlloy.STELLITE.getRgbAsHex());
        sMaterialMapping.put(aMappingID++, MaterialsAlloy.NITINOL_60.getRgbAsHex());
        sMaterialMapping.put(aMappingID++, MaterialsAlloy.LAFIUM.getRgbAsHex());
        sMaterialMapping.put(aMappingID++, MaterialsAlloy.CINOBITE.getRgbAsHex());
        sMaterialMapping.put(aMappingID++, MaterialsAlloy.TITANSTEEL.getRgbAsHex());
        sMaterialMapping.put(aMappingID++, MaterialsAlloy.ABYSSAL.getRgbAsHex());
    }

    @Override
    public IIcon getIcon(final int ordinalSide, final int meta) {
        if ((meta >= 0) && (meta < 16)) {
            switch (meta) {
                case 0 -> {
                    return TexturesGtBlock.TEXTURE_GEARBOX_EGLINSTEEL.getIcon();
                }
                case 1 -> {
                    return TexturesGtBlock.TEXTURE_GEARBOX_INCONEL792.getIcon();
                }
                case 2 -> {
                    return TexturesGtBlock.TEXTURE_GEARBOX_INCOLOYMA956.getIcon();
                }
                case 3 -> {
                    return TexturesGtBlock.TEXTURE_GEARBOX_NITINOL60.getIcon();
                }
                case 4 -> {
                    return TexturesGtBlock.TEXTURE_GEARBOX_ZERON100.getIcon();
                }
                case 5 -> {
                    return TexturesGtBlock.TEXTURE_GEARBOX_PIKYONIUM.getIcon();
                }
                case 6 -> {
                    return TexturesGtBlock.TEXTURE_GEARBOX_TITANSTEEL.getIcon();
                }
                case 7 -> {
                    return TexturesGtBlock.TEXTURE_GEARBOX_ABYSSALALLOY.getIcon();
                }
                case 8 -> {
                    return TexturesGtBlock.TEXTURE_PIPE_BABBITALLOY.getIcon();
                }
                case 9 -> {
                    return TexturesGtBlock.TEXTURE_PIPE_INCONEL690.getIcon();
                }
                case 10 -> {
                    return TexturesGtBlock.TEXTURE_PIPE_STELLITE.getIcon();
                }
                case 11 -> {
                    return TexturesGtBlock.TEXTURE_PIPE_NITINOL60.getIcon();
                }
                case 12 -> {
                    return TexturesGtBlock.TEXTURE_PIPE_LAFIUM.getIcon();
                }
                case 13 -> {
                    return TexturesGtBlock.TEXTURE_PIPE_CINOBITE.getIcon();
                }
                case 14 -> {
                    return TexturesGtBlock.TEXTURE_PIPE_TITANSTEEL.getIcon();
                }
                case 15 -> {
                    return TexturesGtBlock.TEXTURE_PIPE_ABYSSALALLOY.getIcon();
                }
            }
        }
        return Textures.GlobalIcons.RENDERING_ERROR.getIcon();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int colorMultiplier(final IBlockAccess par1IBlockAccess, final int par2, final int par3, final int par4) {
        return sMaterialMapping.get(par1IBlockAccess.getBlockMetadata(par2, par3, par4));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getRenderColor(final int aMeta) {
        return sMaterialMapping.get(aMeta);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getBlockColor() {
        return super.getBlockColor();
    }
}

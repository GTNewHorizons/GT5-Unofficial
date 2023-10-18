package gtPlusPlus.xmod.gregtech.common.blocks;

import java.util.HashMap;

import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.util.GT_LanguageManager;
import gregtech.common.blocks.GT_Material_Casings;
import gtPlusPlus.core.material.ALLOY;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GregtechMetaCasingBlocksPipeGearbox extends GregtechMetaCasingBlocksAbstract {

    private static HashMap<Integer, Integer> sMaterialMapping = new HashMap<>();

    public GregtechMetaCasingBlocksPipeGearbox() {
        super(GregtechMetaCasingItems.class, "gtplusplus.blockcasings.pipesgears", GT_Material_Casings.INSTANCE);
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".0.name", "Eglin Steel Gear Box Casing");
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".1.name", "Inconel-792 Gear Box Casing");
        GT_LanguageManager
                .addStringLocalization(this.getUnlocalizedName() + ".2.name", "Incoloy MA956 Gear Box Casing");
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".3.name", "Nitinol-60 Gear Box Casing");
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".4.name", "Zeron-100 Gear Box Casing");
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".5.name", "Pikyonium Gear Box Casing");
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".6.name", "Titansteel Gear Box Casing");
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".7.name", "Abyssal Gear Box Casing");
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".8.name", "Babbit Alloy Pipe Casing");
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".9.name", "Inconel-690 Pipe Casing");
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".10.name", "Stellite Pipe Casing");
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".11.name", "Nitinol-60 Pipe Casing");
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".12.name", "Lafium Pipe Casing");
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".13.name", "Cinobite Pipe Casing");
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".14.name", "Titansteel Pipe Casing");
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".15.name", "Abyssal Pipe Casing");

        int aMappingID = 0;
        sMaterialMapping.put(aMappingID++, ALLOY.EGLIN_STEEL.getRgbAsHex());
        sMaterialMapping.put(aMappingID++, ALLOY.INCONEL_792.getRgbAsHex());
        sMaterialMapping.put(aMappingID++, ALLOY.INCOLOY_MA956.getRgbAsHex());
        sMaterialMapping.put(aMappingID++, ALLOY.NITINOL_60.getRgbAsHex());
        sMaterialMapping.put(aMappingID++, ALLOY.ZERON_100.getRgbAsHex());
        sMaterialMapping.put(aMappingID++, ALLOY.PIKYONIUM.getRgbAsHex());
        sMaterialMapping.put(aMappingID++, ALLOY.TITANSTEEL.getRgbAsHex());
        sMaterialMapping.put(aMappingID++, ALLOY.ABYSSAL.getRgbAsHex());
        sMaterialMapping.put(aMappingID++, ALLOY.BABBIT_ALLOY.getRgbAsHex());
        sMaterialMapping.put(aMappingID++, ALLOY.INCONEL_690.getRgbAsHex());
        sMaterialMapping.put(aMappingID++, ALLOY.STELLITE.getRgbAsHex());
        sMaterialMapping.put(aMappingID++, ALLOY.NITINOL_60.getRgbAsHex());
        sMaterialMapping.put(aMappingID++, ALLOY.LAFIUM.getRgbAsHex());
        sMaterialMapping.put(aMappingID++, ALLOY.CINOBITE.getRgbAsHex());
        sMaterialMapping.put(aMappingID++, ALLOY.TITANSTEEL.getRgbAsHex());
        sMaterialMapping.put(aMappingID++, ALLOY.ABYSSAL.getRgbAsHex());
    }

    @Override
    public IIcon getIcon(final int ordinalSide, final int meta) {
        if ((meta >= 0) && (meta < 16)) {
            switch (meta) {
                case 0, 1, 2, 3, 4, 5, 6, 7 -> {
                    return TexturesGtBlock.TEXTURE_GEARBOX_GENERIC.getIcon();
                }
                case 8, 9, 10, 11, 12, 13, 14, 15 -> {
                    return TexturesGtBlock.TEXTURE_PIPE_GENERIC.getIcon();
                }
            }
        }
        return Textures.BlockIcons.RENDERING_ERROR.getIcon();
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

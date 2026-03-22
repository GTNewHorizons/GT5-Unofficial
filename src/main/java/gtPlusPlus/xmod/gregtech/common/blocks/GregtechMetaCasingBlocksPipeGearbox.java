package gtPlusPlus.xmod.gregtech.common.blocks;

import java.util.HashMap;

import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.util.GTLanguageManager;
import gregtech.common.blocks.MaterialCasings;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GregtechMetaCasingBlocksPipeGearbox extends GregtechMetaCasingBlocksAbstract {

    private static final HashMap<Integer, Integer> sMaterialMapping = new HashMap<>();

    public GregtechMetaCasingBlocksPipeGearbox() {
        super(GregtechMetaCasingItems.class, "gtplusplus.blockcasings.pipesgears", MaterialCasings.INSTANCE);
        GTLanguageManager.addStringLocalization(this.getUnlocalizedName() + ".0.name", "Eglin Steel Gear Box Casing");
        GTLanguageManager.addStringLocalization(this.getUnlocalizedName() + ".1.name", "Inconel-792 Gear Box Casing");
        GTLanguageManager.addStringLocalization(this.getUnlocalizedName() + ".2.name", "Incoloy MA956 Gear Box Casing");
        GTLanguageManager.addStringLocalization(this.getUnlocalizedName() + ".3.name", "Nitinol-60 Gear Box Casing");
        GTLanguageManager.addStringLocalization(this.getUnlocalizedName() + ".4.name", "Zeron-100 Gear Box Casing");
        GTLanguageManager.addStringLocalization(this.getUnlocalizedName() + ".5.name", "Pikyonium Gear Box Casing");
        GTLanguageManager.addStringLocalization(this.getUnlocalizedName() + ".6.name", "Titansteel Gear Box Casing");
        GTLanguageManager.addStringLocalization(this.getUnlocalizedName() + ".7.name", "Abyssal Gear Box Casing");
        GTLanguageManager.addStringLocalization(this.getUnlocalizedName() + ".8.name", "Babbit Alloy Pipe Casing");
        GTLanguageManager.addStringLocalization(this.getUnlocalizedName() + ".9.name", "Inconel-690 Pipe Casing");
        GTLanguageManager.addStringLocalization(this.getUnlocalizedName() + ".10.name", "Stellite Pipe Casing");
        GTLanguageManager.addStringLocalization(this.getUnlocalizedName() + ".11.name", "Nitinol-60 Pipe Casing");
        GTLanguageManager.addStringLocalization(this.getUnlocalizedName() + ".12.name", "Lafium Pipe Casing");
        GTLanguageManager.addStringLocalization(this.getUnlocalizedName() + ".13.name", "Cinobite Pipe Casing");
        GTLanguageManager.addStringLocalization(this.getUnlocalizedName() + ".14.name", "Titansteel Pipe Casing");
        GTLanguageManager.addStringLocalization(this.getUnlocalizedName() + ".15.name", "Abyssal Pipe Casing");

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
                case 0, 1, 2, 3, 4, 5, 6, 7 -> {
                    return TexturesGtBlock.TEXTURE_GEARBOX_GENERIC.getIcon();
                }
                case 8, 9, 10, 11, 12, 13, 14, 15 -> {
                    return TexturesGtBlock.TEXTURE_PIPE_GENERIC.getIcon();
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

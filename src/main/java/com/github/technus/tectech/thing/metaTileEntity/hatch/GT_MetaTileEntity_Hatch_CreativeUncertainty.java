package com.github.technus.tectech.thing.metaTileEntity.hatch;

import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.util.EnumChatFormatting;

import com.github.technus.tectech.util.CommonValues;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public class GT_MetaTileEntity_Hatch_CreativeUncertainty extends GT_MetaTileEntity_Hatch_Uncertainty {

    public GT_MetaTileEntity_Hatch_CreativeUncertainty(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public GT_MetaTileEntity_Hatch_CreativeUncertainty(String aName, int aTier, String[] aDescription,
            ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new GT_MetaTileEntity_Hatch_CreativeUncertainty(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public String[] getDescription() {
        return new String[] { CommonValues.TEC_MARK_EM, translateToLocal("gt.blockmachines.debug.tt.certain.desc.0"), // Feeling
                                                                                                                      // certain,
                                                                                                                      // for
                                                                                                                      // sure
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD
                        + translateToLocal("gt.blockmachines.debug.tt.certain.desc.1") // Schrödinger's cat escaped the
                                                                                       // box
        };
    }

    @Override
    public void regenerate() {
        // no-op
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide() && (aTick % 100) == 0) {
            if (mode == 0) {
                aBaseMetaTileEntity.setActive(false);
                status = -128;
            } else {
                aBaseMetaTileEntity.setActive(true);
                compute();
            }
        }
    }
}

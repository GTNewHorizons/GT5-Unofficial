package gregtech.common.tileentities.machines.basic;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_BOTTOM_BOXINATOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_BOTTOM_BOXINATOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_BOTTOM_BOXINATOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_BOTTOM_BOXINATOR_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_BOXINATOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_BOXINATOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_BOXINATOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_BOXINATOR_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SIDE_BOXINATOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SIDE_BOXINATOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SIDE_BOXINATOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SIDE_BOXINATOR_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_BOXINATOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_BOXINATOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_BOXINATOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_BOXINATOR_GLOW;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.render.TextureFactory;

public class GT_MetaTileEntity_Debug_TechAccessor extends GT_MetaTileEntity_BasicMachine {

    public GT_MetaTileEntity_Debug_TechAccessor(int aID, String aName, String aNameRegional, int aTier) {
        // Stolen from boxinator
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            1,
            "Can access the technology screen for debugging (temporarily)",
            2,
            1,
            TextureFactory.of(
                TextureFactory.of(OVERLAY_SIDE_BOXINATOR_ACTIVE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_SIDE_BOXINATOR_ACTIVE_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_SIDE_BOXINATOR),
                TextureFactory.builder()
                    .addIcon(OVERLAY_SIDE_BOXINATOR_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_FRONT_BOXINATOR_ACTIVE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_BOXINATOR_ACTIVE_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_FRONT_BOXINATOR),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_BOXINATOR_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_TOP_BOXINATOR_ACTIVE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_TOP_BOXINATOR_ACTIVE_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_TOP_BOXINATOR),
                TextureFactory.builder()
                    .addIcon(OVERLAY_TOP_BOXINATOR_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_BOTTOM_BOXINATOR_ACTIVE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_BOTTOM_BOXINATOR_ACTIVE_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_BOTTOM_BOXINATOR),
                TextureFactory.builder()
                    .addIcon(OVERLAY_BOTTOM_BOXINATOR_GLOW)
                    .glow()
                    .build()));
    }

    public GT_MetaTileEntity_Debug_TechAccessor(String aName, int aTier, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, 1, aDescription, aTextures, 2, 1);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Debug_TechAccessor(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        // nothing?
        builder.setGuiTint(1);
    }
}

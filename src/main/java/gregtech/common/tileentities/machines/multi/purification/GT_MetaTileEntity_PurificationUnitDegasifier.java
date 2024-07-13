package gregtech.common.tileentities.machines.multi.purification;

import static gregtech.api.enums.GT_Values.AuthorNotAPenguin;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import gregtech.api.enums.Textures;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;

public class GT_MetaTileEntity_PurificationUnitDegasifier
    extends GT_MetaTileEntity_PurificationUnitBase<GT_MetaTileEntity_PurificationUnitDegasifier>
    implements ISurvivalConstructable {

    // TODO
    private static final int CASING_INDEX_MAIN = 0;

    public GT_MetaTileEntity_PurificationUnitDegasifier(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected GT_MetaTileEntity_PurificationUnitDegasifier(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_PurificationUnitDegasifier(mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        if (side == facing) {
            if (active) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_MAIN),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_MAIN),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_MAIN) };
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {

    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_PurificationUnitDegasifier> getStructureDefinition() {
        return null;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Purification Unit")
            .addSeparator()
            .addInfo(AuthorNotAPenguin)
            .toolTipFinisher("GregTech");
        return tt;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public int getWaterTier() {
        return 7;
    }

    @Override
    public long getActivePowerUsage() {
        return TierEU.RECIPE_UHV;
    }
}

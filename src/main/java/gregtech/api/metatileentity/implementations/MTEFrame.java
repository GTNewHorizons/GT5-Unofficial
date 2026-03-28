package gregtech.api.metatileentity.implementations;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.HarvestTool;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.ITemporaryTE;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.ILocalizedMetaPipeEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTSplit;
import gregtech.api.util.GTUtility;
import gregtech.common.blocks.BlockFrameBox;

@IMetaTileEntity.SkipGenerateDescription
public class MTEFrame extends MetaPipeEntity implements ITemporaryTE, ILocalizedMetaPipeEntity {

    public static final String LOCALIZED_DESC_FORMAT = "gt.blockmachines.gt_frame.desc.format";
    public final Materials mMaterial;

    public MTEFrame(int aID, String aName, Materials aMaterial) {
        super(aID, aName, 0);
        mMaterial = aMaterial;
        // Hide TileEntity frame in NEI, since we have the block version now that should always be used
        codechicken.nei.api.API.hideItem(this.getStackForm(1));
    }

    public MTEFrame(String aName, Materials aMaterial) {
        super(aName, 0);
        mMaterial = aMaterial;
    }

    @Override
    public byte getTileEntityBaseType() {
        final int level = (mMaterial == null) ? 0 : GTUtility.clamp(mMaterial.mToolQuality, 0, 3);

        HarvestTool tool = switch (level) {
            case 0 -> HarvestTool.WrenchPipeLevel0;
            case 1 -> HarvestTool.WrenchPipeLevel1;
            case 2 -> HarvestTool.WrenchPipeLevel2;
            case 3 -> HarvestTool.WrenchPipeLevel3;
            default -> throw new IllegalStateException("Unexpected tool quality level: " + level);
        };

        return tool.toTileEntityBaseType();
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEFrame(mName, mMaterial);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection, int connections,
        int colorIndex, boolean active, boolean redstoneLevel) {
        return new ITexture[] { TextureFactory.of(
            mMaterial.mIconSet.mTextures[OrePrefixes.frameGt.getTextureIndex()],
            Dyes.getModulation(colorIndex, mMaterial.mRGBa)) };
    }

    @Override
    public String[] getDescription() {
        return GTSplit.splitLocalized(LOCALIZED_DESC_FORMAT);
    }

    @Override
    public final boolean isFacingValid(ForgeDirection facing) {
        return false;
    }

    @Override
    public final boolean isValidSlot(int aIndex) {
        return false;
    }

    @Override
    public final boolean renderInside(ForgeDirection side) {
        return true;
    }

    @Override
    public final float getThickness() {
        return 1.0F;
    }

    @Override
    public float getCollisionThickness() {
        return 1.0F;
    }

    @Override
    public final void saveNBTData(NBTTagCompound aNBT) {
        /* Do nothing */
    }

    @Override
    public final void loadNBTData(NBTTagCompound aNBT) {
        /* Do nothing */
    }

    @Override
    public final boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public final boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public int connect(ForgeDirection side) {
        return 0;
    }

    @Override
    public void disconnect(ForgeDirection side) {
        /* Do nothing */
    }

    @Override
    public boolean isMachineBlockUpdateRecursive() {
        return true;
    }

    @Override
    public Materials getMaterial() {
        return mMaterial;
    }

    @Override
    public String getPrefixKey() {
        return "gt.oreprefix.material_frame_box_tileentity";
    }

    @Override
    public String getLocalizedName() {
        return StatCollector.translateToLocalFormatted(getPrefixKey(), BlockFrameBox.getLocalizedName(getMaterial()));
    }
}

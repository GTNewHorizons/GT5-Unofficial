package gregtech.api.metatileentity.implementations;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.HarvestTool;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TextureSet;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.ILocalizedMetaPipeEntity;
import gregtech.api.material.MU;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTSplit;
import gregtech.api.util.GTUtility;
import gregtech.common.blocks.FrameShapeBlock;

/// The material-agnostic frame-box tile entity backing the [FrameShapeBlock] shape: identity comes from the
/// hosting block, and the material resolves from the host block's metadata.
@IMetaTileEntity.SkipGenerateDescription
public class MTEFrame extends MetaPipeEntity implements ILocalizedMetaPipeEntity {

    public static final String LOCALIZED_DESC_FORMAT = "gt.blockmachines.gt_frame.desc.format";

    public MTEFrame(int aID, String aName, FrameShapeBlock shape) {
        super(aID, aName, 0, false, shape, 0);
    }

    public MTEFrame(String aName, FrameShapeBlock shape) {
        super(aName, 0, shape, 0);
    }

    @Override
    public boolean needsClientTick() {
        return false;
    }

    @Override
    public byte getTileEntityBaseType() {
        final int level = GTUtility.clamp(MU.toolQuality(shapeMaterial()), 0, 3);

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
        return new MTEFrame(mName, (FrameShapeBlock) getShapeHost());
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection, int connections,
        int colorIndex, boolean active, boolean redstoneLevel) {
        final TextureSet textureSet = MU.textureSetOf(getMaterial());
        final short[] rgba = MU.rgbaOf(getMaterial());
        if (textureSet == null || rgba == null) return Textures.BlockIcons.ERROR_RENDERING;
        return new ITexture[] { TextureFactory
            .of(textureSet.mTextures[OrePrefixes.frameGt.getTextureIndex()], Dyes.getModulation(colorIndex, rgba)) };
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
    public Object getMaterial() {
        return shapeMaterial();
    }

    @Override
    public String getLocalizedName() {
        Material material = shapeMaterial();
        if (material != null) return FrameShapeBlock.displayName(material);
        return ILocalizedMetaPipeEntity.super.getLocalizedName();
    }

    @Override
    public String getPrefixKey() {
        return OrePrefixes.frameGt.getOreprefixKey();
    }
}

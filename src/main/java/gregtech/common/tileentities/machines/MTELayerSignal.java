package gregtech.common.tileentities.machines;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.ILayerProducer;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.IGTHatchAdder;

public class MTELayerSignal extends MTEHatch {

    protected static final IIconContainer TEXTURE_FRONT = Textures.BlockIcons.OVERLAY_HATCH_HEAT_SENSOR;
    protected static final IIconContainer TEXTURE_FRONT_GLOW = Textures.BlockIcons.OVERLAY_HATCH_HEAT_SENSOR_GLOW;

    protected int strength = 0;

    public MTELayerSignal(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 0, "Detects current layer from a machine.");
    }

    public MTELayerSignal(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return false;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean allowGeneralRedstoneOutput() {
        return true;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection Side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public void initDefaultModes(NBTTagCompound aNBT) {
        getBaseMetaTileEntity().setActive(true);
    }

    @Override
    public String[] getDescription() {
        return new String[] { "Detects current layer of a machine.",
            "Outputs a redstone signal based on what input is required." };
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        strength = aNBT.getInteger("strength");
        super.loadNBTData(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("strength", strength);
        super.saveNBTData(aNBT);
    }

    public void setLayerValue(int strength) {
        this.strength = strength;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        ForgeDirection facing = getBaseMetaTileEntity().getFrontFacing();
        if (aBaseMetaTileEntity.isServerSide()) {
            for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
                aBaseMetaTileEntity.setStrongOutputRedstoneSignal(direction, direction == facing ? (byte) strength : 0);
            }
        }
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTELayerSignal(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(TEXTURE_FRONT), TextureFactory.builder()
            .addIcon(TEXTURE_FRONT_GLOW)
            .glow()
            .build() };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(TEXTURE_FRONT) };
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    public enum LayerSignalHatchElement implements IHatchElement<ILayerProducer> {

        LayerSignal(ILayerProducer::addLayerSignalHatchToMachineList, MTELayerSignal.class);

        private final IGTHatchAdder<ILayerProducer> adder;
        private final List<Class<? extends IMetaTileEntity>> mteClasses;

        LayerSignalHatchElement(IGTHatchAdder<ILayerProducer> adder, Class<? extends IMetaTileEntity> mteClasse) {
            this.adder = adder;
            this.mteClasses = Collections.unmodifiableList(Arrays.asList(mteClasse));
        }

        @Override
        public long count(ILayerProducer layerProducer) {
            return layerProducer.getLayerSignalHatches()
                .size();
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return mteClasses;
        }

        public IGTHatchAdder<? super ILayerProducer> adder() {
            return adder;
        }

    }

}

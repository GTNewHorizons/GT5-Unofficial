package tectech.thing.metaTileEntity.hatch;

import static gregtech.api.enums.Dyes.MACHINE_METAL;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechDeviceInformation;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.render.TextureFactory;
import gregtech.mixin.interfaces.accessors.EntityPlayerMPAccessor;
import tectech.mechanics.dataTransport.ALRecipeDataPacket;
import tectech.mechanics.dataTransport.DataPacket;
import tectech.mechanics.dataTransport.QuantumDataPacket;
import tectech.mechanics.pipe.IConnectsToDataPipe;
import tectech.thing.metaTileEntity.pipe.MTEPipeData;
import tectech.util.CommonValues;

/**
 * Created by danie_000 on 11.12.2016.
 */
public abstract class MTEHatchDataConnector<T extends DataPacket<?>> extends MTEHatch implements IConnectsToDataPipe {

    public static final int TICKS_ONE_SECOND = 20;
    public static final int TICKS_CONNECTION_POLLING = 100;
    public static IIconContainer EM_D_SIDES;
    public static IIconContainer EM_D_ACTIVE;
    public static IIconContainer EM_D_CONN;

    private String clientLocale = "en_US";

    public T q;

    public short id = -1;
    protected IConnectsToDataPipe connected = null;

    protected MTEHatchDataConnector(int aID, String aName, String aNameRegional, int aTier, String[] descr) {
        super(aID, aName, aNameRegional, aTier, 0, descr);
    }

    protected MTEHatchDataConnector(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        super.registerIcons(aBlockIconRegister);
        EM_D_ACTIVE = Textures.BlockIcons.custom("iconsets/OVERLAY_EM_D_ACTIVE");
        EM_D_SIDES = Textures.BlockIcons.custom("iconsets/OVERLAY_EM_D_SIDES");
        EM_D_CONN = Textures.BlockIcons.custom("iconsets/EM_DATA_CONN");
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory
            .of(EM_D_ACTIVE, Dyes.getModulation(getBaseMetaTileEntity().getColorization(), MACHINE_METAL.getRGBA())),
            TextureFactory.of(EM_D_CONN) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture,
            TextureFactory
                .of(EM_D_SIDES, Dyes.getModulation(getBaseMetaTileEntity().getColorization(), MACHINE_METAL.getRGBA())),
            TextureFactory.of(EM_D_CONN) };
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setShort("eID", id);
        if (q != null) {
            aNBT.setTag("eDATA", q.toNbt());
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        id = aNBT.getShort("eID");
        if (aNBT.hasKey("eDATA")) {
            q = loadPacketFromNBT(aNBT.getCompoundTag("eDATA"));
        }
    }

    protected abstract T loadPacketFromNBT(NBTTagCompound nbt);

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            aTick = MinecraftServer.getServer()
                .getTickCounter();
            if (CommonValues.MOVE_AT == aTick % TICKS_ONE_SECOND) {
                if (q == null) {
                    getBaseMetaTileEntity().setActive(false);
                    resetHistory();
                } else {
                    getBaseMetaTileEntity().setActive(true);
                    moveAround(aBaseMetaTileEntity, CheckState.NEW_DATA);
                }
            }
            if (CommonValues.MOVE_AT == aTick % TICKS_CONNECTION_POLLING) {
                moveAround(aBaseMetaTileEntity, CheckState.CONNECTION);
            }
        }
    }

    /**
     * Checks for a connection from an output subclass like {@link MTEHatchDataOutput} or {@link MTEHatchDataItemsOutput}
     * to an input subclass like {@link MTEHatchDataInput} or {@link MTEHatchDataItemsInput}.
     * Keeps track of the connected input and updates the data if new data is pushed or the slow polling detects either a new connection or disconnection.
     *
     * @return a resulting check state, used for post moveAround processing of subclasses
     */
    public CheckState moveAround(IGregTechTileEntity aBaseMetaTileEntity, CheckState checkState) {
        // safety check
        if (checkState != CheckState.CONNECTION && checkState != CheckState.NEW_DATA) return CheckState.UNKNOWN;
        IConnectsToDataPipe current = this, source = this, next;
        var oldConnected = connected;
        var result = CheckState.UNKNOWN;
        connected = null;
        int range = 0;
        while ((next = current.getNext(source)) != null && range++ < 1000) {
            if (next instanceof MTEHatchDataItemsInput dataItemsInput) {
                if (checkState == CheckState.NEW_DATA) {
                    dataItemsInput.setContents((ALRecipeDataPacket) q);
                    result = CheckState.NEW_DATA;
                } else {
                    result = CheckState.CONNECTED;
                    dataItemsInput.setContents((ALRecipeDataPacket) q);
                }
                connected = dataItemsInput;
                break;
            } else if (next instanceof MTEHatchDataInput dataInput) {
                if (checkState == CheckState.NEW_DATA) {
                    dataInput.setContents(((QuantumDataPacket) q));
                    result = CheckState.NEW_DATA;
                } else {
                    result = CheckState.CONNECTED;
                    dataInput.setContents(((QuantumDataPacket) q));
                }
                connected = dataInput;
                break;
            }

            source = current;
            current = next;
        }

        if (connected == null) {
            switch (oldConnected) {
                case MTEHatchDataInput dataInput -> dataInput.setContents(null);
                case MTEHatchDataItemsInput dataItemsInput -> dataItemsInput.setContents(null);
                default -> {}
            }
            result = CheckState.DISCONNECTED;
        }
        return result;
    }

    protected void resetHistory() {

    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide()) {
            return true;
        }
        if (aPlayer instanceof EntityPlayerMPAccessor) {
            clientLocale = ((EntityPlayerMPAccessor) aPlayer).gt5u$getTranslator();
        }
        return true;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isLiquidInput(ForgeDirection side) {
        return false;
    }

    @Override
    public boolean isFluidInputAllowed(FluidStack aFluid) {
        return false;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return false;
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        if (id > 0) {
            return new String[] { IGregTechDeviceInformation.encode("tt.keyword.ID.fmt", id),
                IGregTechDeviceInformation.encode("tt.keyword.Content.fmt", q != null ? q.getContentString() : 0),
                IGregTechDeviceInformation.encode("tt.keyword.PacketHistory.fmt", q != null ? q.getTraceSize() : 0), };
        }
        return new String[] {
            IGregTechDeviceInformation.encode("tt.keyword.Content.fmt", q != null ? q.getContentString() : 0),
            IGregTechDeviceInformation.encode("tt.keyword.PacketHistory.fmt", q != null ? q.getTraceSize() : 0), };
    }

    @Override
    public byte getColorization() {
        return getBaseMetaTileEntity().getColorization();
    }

    @Override
    public IConnectsToDataPipe getNext(IConnectsToDataPipe source /* ==this */) {
        IGregTechTileEntity base = getBaseMetaTileEntity();
        byte color = base.getColorization();
        if (color < 0) {
            return null;
        }
        IGregTechTileEntity next = base.getIGregTechTileEntityAtSide(base.getFrontFacing());
        if (next == null) {
            return null;
        }
        IMetaTileEntity meta = next.getMetaTileEntity();
        if (meta instanceof MTEPipeData) {
            ((MTEPipeData) meta).markUsed();
            return (IConnectsToDataPipe) meta;
        } else if (meta instanceof MTEHatchDataInput hatchDataInput && hatchDataInput.getColorization() == color
            && hatchDataInput.canConnectData(
                base.getFrontFacing()
                    .getOpposite())) {
                        return (IConnectsToDataPipe) meta;
                    }
        return null;
    }

    @Override
    public void onBlockDestroyed() {
        super.onBlockDestroyed();
        // when "inputs" aka receivers are broken, no need to update the "upstream" "output" aka sender
        if (canClear() || connected == null) return;
        moveAround(this.getBaseMetaTileEntity(), CheckState.CONNECTION);
    }

    public enum CheckState {
        /**
         * Uses {@link #moveAround(IGregTechTileEntity, CheckState)} api, but doesn't pass data, because the DataInput
         * does
         */
        UNKNOWN,
        /**
         * Indicating that from the DataOutput to the DataInput is only checked for a connection,
         * this state should update the connected input's data on {@link #CONNECTED} or {@link #DISCONNECTED} usually
         * without modifying the DataOutput's data.
         * It's also never returned, only passed as an argument!
         */
        CONNECTION,
        /**
         * Indicating that from the DataOutput to the DataInput is checked for a connection and intentionally for
         * pushing new data.
         * This state is returned after it successfully pushed new data to the connected input.
         * It's returned and passed as an argument!
         */
        NEW_DATA,
        /**
         * This state is returned if the DataOutput is connected to a DataInput and {@link #CONNECTION} was passed as an
         * argument.
         * The DataInput's data has been updated.
         */
        CONNECTED,
        /**
         * This state is returned if the DataOutput is not connected to a DataInput no matter the passed argument.
         * The DataInput's data has been set to null.
         */
        DISCONNECTED
    }
}

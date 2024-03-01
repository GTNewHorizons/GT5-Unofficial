package gregtech.api.multitileentity.base;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;

import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Mods;
import gregtech.api.enums.Textures;
import gregtech.api.enums.GT_Values.NBT;
import gregtech.api.enums.Textures.BlockIcons.CustomIcon;
import gregtech.api.interfaces.ITexture;
import gregtech.api.multitileentity.MultiTileEntityClassContainer;
import gregtech.api.multitileentity.MultiTileEntityRegistry;
import gregtech.api.multitileentity.interfaces.IMultiTileEntity;
import gregtech.api.multitileentity.interfaces.SyncedMultiTileEntity;
import gregtech.api.net.GT_Packet_MultiTileEntity;
import gregtech.api.net.data.CoordinateData;
import gregtech.api.net.data.MultiTileEntityData;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Util;
import gregtech.common.render.MultiTileBasicRender;

public abstract class MultiTileEntity extends TileEntity
    implements MultiTileBasicRender, SyncedMultiTileEntity, IMultiTileEntity {

    // MultTileEntity variables
    private final boolean isTicking; // If this TileEntity is ticking at all
    private int metaId; // The MuTE ID of the entity inside the registry
    private int registryId; // The registry ID of the entity
    @Nonnull
    private ForgeDirection facing = ForgeDirection.WEST; // Default to WEST, so it renders facing Left in the
    @Nonnull
    private final ChunkCoordinates cachedCoordinates = new ChunkCoordinates();

    // MultTileBasicRender variables
    private ITexture baseTexture = null;
    private ITexture topOverlayTexture = null;
    private ITexture bottomOverlayTexture = null;
    private ITexture leftOverlayTexture = null;
    private ITexture rightOverlayTexture = null;
    private ITexture backOverlayTexture = null;
    private ITexture frontOverlayTexture = null;

    // SyncedMultiTileEntity variables
    private final GT_Packet_MultiTileEntity fullPacket = new GT_Packet_MultiTileEntity(false);
    private final GT_Packet_MultiTileEntity timedPacket = new GT_Packet_MultiTileEntity(false);
    private final GT_Packet_MultiTileEntity graphicPacket = new GT_Packet_MultiTileEntity(false);

    public MultiTileEntity(boolean isTicking) {
        this.isTicking = isTicking;
    }

    // TileEntity methods
    @Override
    public boolean canUpdate() {
        return isTicking;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        facing = ForgeDirection.getOrientation(nbt.getInteger("facing"));
        if (getMetaId() == GT_Values.W || getRegistryId() == GT_Values.W) {
            metaId = nbt.getInteger(NBT.MTE_ID);
            registryId = nbt.getInteger(NBT.MTE_REG);
                MultiTileEntityRegistry registry = MultiTileEntityRegistry.getRegistry(registryId);
            MultiTileEntityClassContainer clazz = registry.getClassContainer(metaId);
            nbt = GT_Util.fuseNBT(nbt, clazz.parameters);
        }
        if (nbt.hasKey("x")) xCoord = nbt.getInteger("x");
        if (nbt.hasKey("y")) yCoord = nbt.getInteger("y");
        if (nbt.hasKey("z")) zCoord = nbt.getInteger("z");
            if (nbt.hasKey(NBT.FACING)) facing = ForgeDirection.getOrientation(nbt.getInteger(NBT.FACING));
            if (NetworkUtils.isDedicatedClient()) {
                if (GregTech_API.sBlockIcons == null && nbt.hasKey(NBT.TEXTURE_FOLDER)) {
                    loadTextures(nbt.getString(NBT.TEXTURE_FOLDER));
                } else {
                    copyTextures();
                }
            }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("facing", facing.ordinal());
    }

    // MultiTileEntity methods
    @Override
    public int getRegistryId() {
        return registryId;
    }

    @Override
    public int getMetaId() {
        return metaId;
    }

    @Override
    @Nonnull
    public ForgeDirection getFrontFacing() {
        return facing;
    }

    protected int getXCoord() {
        return xCoord;
    }

    protected int getYCoord() {
        return yCoord;
    }

    protected int getZCoord() {
        return zCoord;
    }

    @Nonnull
    public ChunkCoordinates getCoords() {
        cachedCoordinates.posX = getXCoord();
        cachedCoordinates.posY = getYCoord();
        cachedCoordinates.posZ = getZCoord();
        return cachedCoordinates;
    }

    protected void loadTextures(@Nonnull String folder) {
        // Loading the registry
        for (SidedTextureNames textureName : SidedTextureNames.TEXTURES) {
            ITexture texture;
            try {
                Minecraft.getMinecraft()
                    .getResourceManager()
                    .getResource(
                        new ResourceLocation(
                            Mods.GregTech.ID,
                            "textures/blocks/multitileentity/" + folder + "/" + textureName.getName() + ".png"));
                texture = TextureFactory.of(new CustomIcon("multitileentity/" + folder + "/" + textureName.getName()));
            } catch (IOException ignored) {
                texture = TextureFactory.of(Textures.BlockIcons.VOID);
            }
            switch (textureName) {
                case Top -> topOverlayTexture = texture;
                case Bottom -> bottomOverlayTexture = texture;
                case Back -> backOverlayTexture = texture;
                case Front -> frontOverlayTexture = texture;
                case Left -> leftOverlayTexture = texture;
                case Right -> rightOverlayTexture = texture;
                case Base -> baseTexture = texture;
            }
        }
    }

    protected void copyTextures() {
        // Loading an instance
        MultiTileEntity canonicalEntity = MultiTileEntityRegistry.getRegistry(registryId).getCachedTileEntity(metaId);
        baseTexture = canonicalEntity.baseTexture;
        topOverlayTexture = canonicalEntity.topOverlayTexture;
        bottomOverlayTexture = canonicalEntity.bottomOverlayTexture;
        leftOverlayTexture = canonicalEntity.leftOverlayTexture;
        rightOverlayTexture = canonicalEntity.rightOverlayTexture;
        backOverlayTexture = canonicalEntity.backOverlayTexture;
        frontOverlayTexture = canonicalEntity.frontOverlayTexture;
    }

    protected boolean isServerSide() {
        return !getWorldObj().isRemote;
    }

    @Override
    public void initFromNBT(@Nonnull final NBTTagCompound nbt, final int registryId, final int metaId) {
    if (this.registryId == registryId && this.metaId == metaId) {
            return;
        }
        this.registryId = registryId;
        this.metaId = metaId;
        if (nbt != null) readFromNBT(nbt);
    }

    @Override
    public void addToolTip(@Nonnull final List<String> toolTips) {}

    // MultiTileBasicRender methods
    @Override
    @Nonnull
    public final ITexture getTexture(@Nonnull ForgeDirection side) {
        if (getFrontFacing() == side) {
            return getFrontTexture();
        }

        if (getFrontFacing().getOpposite() == side) {
            return getBackTexture();
        }

        if (getFrontFacing().getRotation(getFrontFacing().getRotation(ForgeDirection.UP)) == side) {
            return getTopTexture();
        }

        if (getFrontFacing().getRotation(getFrontFacing().getRotation(ForgeDirection.DOWN)) == side) {
            return getBottomTexture();
        }

        if (getFrontFacing().getRotation(getFrontFacing().getRotation(ForgeDirection.EAST)) == side) {
            return getRightTexture();
        }

        if (getFrontFacing().getRotation(getFrontFacing().getRotation(ForgeDirection.WEST)) == side) {
            return getLeftTexture();
        }

        return baseTexture;
    }

    @Nonnull
    protected ITexture getFrontTexture() {
        return TextureFactory.of(baseTexture, frontOverlayTexture);
    }

    @Nonnull
    protected ITexture getBackTexture() {
        return TextureFactory.of(baseTexture, backOverlayTexture);
    }

    @Nonnull
    protected ITexture getTopTexture() {
        return TextureFactory.of(baseTexture, topOverlayTexture);
    }

    @Nonnull
    protected ITexture getBottomTexture() {
        return TextureFactory.of(baseTexture, bottomOverlayTexture);
    }

    @Nonnull
    protected ITexture getRightTexture() {
        return TextureFactory.of(baseTexture, rightOverlayTexture);
    }

    @Nonnull
    protected ITexture getLeftTexture() {
        return TextureFactory.of(baseTexture, leftOverlayTexture);
    }

    // SyncedMultiTileEntity methods
    @Override
    public void getFullPacketData(GT_Packet_MultiTileEntity packet) {
        packet.addData(new CoordinateData(getCoords()));
        // packet.addData(new CommonData(mStrongRedstone, color, (byte) 0));
        packet.addData(new MultiTileEntityData(registryId, metaId));
    }

    @Override
    public void getTimedPacketData(GT_Packet_MultiTileEntity packet) {
        packet.addData(new CoordinateData(getCoords()));
        packet.addData(new MultiTileEntityData(registryId, metaId));
    }

    @Override
    public void getGraphicPacketData(GT_Packet_MultiTileEntity packet) {
        packet.addData(new CoordinateData(getCoords()));
        packet.addData(new MultiTileEntityData(registryId, metaId));
    }

    @Override
    public void sendFullPacket(@Nonnull EntityPlayerMP player) {
        if (!isServerSide()) return;
        fullPacket.clearData();
        getFullPacketData(fullPacket);
        GT_Values.NW.sendToPlayer(fullPacket, player);
    }

    @Override
    public void sendTimedPacket() {
        if (!isServerSide()) return;
        timedPacket.clearData();
        getTimedPacketData(timedPacket);
        GT_Values.NW.sendPacketToAllPlayersInRange(getWorldObj(), timedPacket, getXCoord(), getZCoord());
    }

    @Override
    public void sendGraphicPacket() {
        if (!isServerSide()) return;
        graphicPacket.clearData();
        getGraphicPacketData(graphicPacket);
        GT_Values.NW.sendPacketToAllPlayersInRange(getWorldObj(), graphicPacket, getXCoord(), getZCoord());
    }

    // Helper classes/enums
    protected enum SidedTextureNames {

        Base("base"),
        Left("left"),
        Right("right"),
        Top("top"),
        Bottom("bottom"),
        Back("back"),
        Front("front");

        private final String name;
        public static final SidedTextureNames[] TEXTURES = { Base, Left, Right, Top, Bottom, Back, Front };

        SidedTextureNames(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    protected enum StatusTextures {

        Active("active", false),
        ActiveWithGlow("active_glow", true),
        Inactive("inactive", false),
        InactiveWithGlow("inactive_glow", true);

        private final String name;
        private final boolean hasGlow;
        public static final StatusTextures[] TEXTURES = { Active, ActiveWithGlow, Inactive, InactiveWithGlow };

        StatusTextures(String name, boolean hasGlow) {
            this.name = name;
            this.hasGlow = hasGlow;
        }

        public String getName() {
            return name;
        }

        public boolean hasGlow() {
            return hasGlow;
        }
    }
}

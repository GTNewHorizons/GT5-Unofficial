package gregtech.api.multitileentity.base;

import static gregtech.GT_Mod.GT_FML_LOGGER;
import static gregtech.api.enums.GT_Values.VALID_SIDES;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureStrongholdPieces.RightTurn;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.GT_Values.NBT;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.enums.Textures.BlockIcons.CustomIcon;
import gregtech.api.gui.modularui.GT_UIInfos;
import gregtech.api.interfaces.ITexture;
import gregtech.api.metatileentity.CoverableTileEntity;
import gregtech.api.metatileentity.GregTechTileClientEvents;
import gregtech.api.multitileentity.MultiTileEntityBlockInternal;
import gregtech.api.multitileentity.MultiTileEntityClassContainer;
import gregtech.api.multitileentity.MultiTileEntityRegistry;
import gregtech.api.multitileentity.interfaces.IMultiTileEntity;
import gregtech.api.multitileentity.interfaces.SyncedMultiTileEntity;
import gregtech.api.net.GT_Packet_MultiTileEntity;
import gregtech.api.net.GT_Packet_New;
import gregtech.api.net.data.CommonData;
import gregtech.api.net.data.CoordinateData;
import gregtech.api.net.data.MultiTileEntityData;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.objects.XSTR;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Util;
import gregtech.api.util.GT_Utility;
import gregtech.common.render.MultiTileBasicRender;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

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

    @Override
    public boolean canUpdate() {
        return isTicking;
    }

    // MultiTileEntity methods
    @Nonnull
    protected ForgeDirection getFrontFacing() {
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
    protected ChunkCoordinates getCoords() {
        cachedCoordinates.posX = getXCoord();
        cachedCoordinates.posY = getYCoord();
        cachedCoordinates.posZ = getZCoord();
        return cachedCoordinates;
    }

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
        packet.addData(new CommonData(mStrongRedstone, color, (byte) 0));
        packet.addData(new MultiTileEntityData(mteRegistry, mteID));
    }

    @Override
    public void getGraphicPacketData(GT_Packet_MultiTileEntity packet) {
        packet.addData(new CoordinateData(getCoords()));
        packet.addData(new MultiTileEntityData(mteRegistry, mteID));
    }

    @Override
    public void getTimedPacketData(GT_Packet_MultiTileEntity packet) {
        packet.addData(new CoordinateData(getCoords()));
        packet.addData(new MultiTileEntityData(mteRegistry, mteID));
    }

    @Override
    public void sendFullPacket(@Nonnull EntityPlayerMP player) {
        fullPacket.clearData();
        getFullPacketData(fullPacket);
        GT_Values.NW.sendToPlayer(fullPacket, player);
    }

    @Override
    public void sendGraphicPacket() {
        graphicPacket.clearData();
        getGraphicPacketData(graphicPacket);
        GT_Values.NW.sendPacketToAllPlayersInRange(worldObj, graphicPacket, getXCoord(), getZCoord());
    }

    @Override
    public void sendTimedPacket() {
        timedPacket.clearData();
        getTimedPacketData(timedPacket);
        GT_Values.NW.sendPacketToAllPlayersInRange(worldObj, timedPacket, getXCoord(), getZCoord());
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

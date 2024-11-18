package gregtech.common.tileentities.machines.multi.artificialorganisms;

import java.util.HashSet;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GTMod;
import gregtech.api.enums.Textures;
import gregtech.api.enums.ToolModes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.common.GTClient;
import gregtech.common.config.Other;
import gregtech.common.covers.CoverInfo;
import gregtech.common.tileentities.machines.multi.artificialorganisms.hatches.MTEHatchBioOutput;
import gregtech.common.tileentities.machines.multi.artificialorganisms.util.IConnectsToBioPipe;

public class MTEBioPipe extends MetaPipeEntity implements IConnectsToBioPipe {

    public MTEHatchBioOutput networkOutput;

    private static Textures.BlockIcons.CustomIcon pipeTexture;

    public MTEBioPipe(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, 0);
    }

    public MTEBioPipe(String aName) {
        super(aName, 0);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MTEBioPipe(mName);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        if (GTMod.gregtechproxy.gt6Pipe) {
            aNBT.setByte("mConnections", mConnections);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        if (GTMod.gregtechproxy.gt6Pipe) {
            mConnections = aNBT.getByte("mConnections");
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        pipeTexture = new Textures.BlockIcons.CustomIcon("iconsets/BIOPIPE");
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, int aConnections,
        int colorIndex, boolean aConnected, boolean aRedstone) {
        return new ITexture[] { TextureFactory.of(pipeTexture) };
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity iGregTechTileEntity, int i, ForgeDirection side,
        ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity iGregTechTileEntity, int i, ForgeDirection side,
        ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean renderInside(ForgeDirection side) {
        return false;
    }

    @Override
    public byte getTileEntityBaseType() {
        return 4;
    }

    @Override
    public String[] getDescription() {
        return new String[] { StatCollector.translateToLocal("GT5U.pipe.bio.desc.0"),
            StatCollector.translateToLocal("GT5U.pipe.bio.desc.1"),
            StatCollector.translateToLocal("GT5U.pipe.bio.desc.2") };
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World aWorld, int aX, int aY, int aZ) {
        float tSpace = (1f - 0.375f) / 2;
        float tSide0 = tSpace;
        float tSide1 = 1f - tSpace;
        float tSide2 = tSpace;
        float tSide3 = 1f - tSpace;
        float tSide4 = tSpace;
        float tSide5 = 1f - tSpace;

        if (getBaseMetaTileEntity().getCoverIDAtSide(ForgeDirection.DOWN) != 0) {
            tSide0 = tSide2 = tSide4 = 0;
            tSide3 = tSide5 = 1;
        }
        if (getBaseMetaTileEntity().getCoverIDAtSide(ForgeDirection.UP) != 0) {
            tSide2 = tSide4 = 0;
            tSide1 = tSide3 = tSide5 = 1;
        }
        if (getBaseMetaTileEntity().getCoverIDAtSide(ForgeDirection.NORTH) != 0) {
            tSide0 = tSide2 = tSide4 = 0;
            tSide1 = tSide5 = 1;
        }
        if (getBaseMetaTileEntity().getCoverIDAtSide(ForgeDirection.SOUTH) != 0) {
            tSide0 = tSide4 = 0;
            tSide1 = tSide3 = tSide5 = 1;
        }
        if (getBaseMetaTileEntity().getCoverIDAtSide(ForgeDirection.WEST) != 0) {
            tSide0 = tSide2 = tSide4 = 0;
            tSide1 = tSide3 = 1;
        }
        if (getBaseMetaTileEntity().getCoverIDAtSide(ForgeDirection.EAST) != 0) {
            tSide0 = tSide2 = 0;
            tSide1 = tSide3 = tSide5 = 1;
        }

        byte tConn = ((BaseMetaPipeEntity) getBaseMetaTileEntity()).mConnections;
        if ((tConn & 1 << ForgeDirection.DOWN.ordinal()) != 0) {
            tSide0 = 0f;
        }
        if ((tConn & 1 << ForgeDirection.UP.ordinal()) != 0) {
            tSide1 = 1f;
        }
        if ((tConn & 1 << ForgeDirection.NORTH.ordinal()) != 0) {
            tSide2 = 0f;
        }
        if ((tConn & 1 << ForgeDirection.SOUTH.ordinal()) != 0) {
            tSide3 = 1f;
        }
        if ((tConn & 1 << ForgeDirection.WEST.ordinal()) != 0) {
            tSide4 = 0f;
        }
        if ((tConn & 1 << ForgeDirection.EAST.ordinal()) != 0) {
            tSide5 = 1f;
        }

        return AxisAlignedBB
            .getBoundingBox(aX + tSide4, aY + tSide0, aZ + tSide2, aX + tSide5, aY + tSide1, aZ + tSide3);
    }

    @Override
    public float getThickNess() {
        if (GTMod.instance.isClientSide() && GTClient.hideValue == 1) {
            return 0.0625F;
        }
        return 0.375f;
    }

    @Override
    public boolean canConnect(ForgeDirection side) {
        return true;
    }

    // Pipes recursively call this function on other pipes, returning a connections hashset.
    @Override
    public HashSet<IConnectsToBioPipe> getConnected(MTEHatchBioOutput output, HashSet<IConnectsToBioPipe> connections) {
        networkOutput = output;
        connections.add(this);
        for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            if ((mConnections & 1 << side.ordinal()) == 0) {
                continue; // if not connected continue
            }
            TileEntity next = getBaseMetaTileEntity().getTileEntityAtSide(side);
            if (next != null) {
                IMetaTileEntity meta = ((IGregTechTileEntity) next).getMetaTileEntity();
                if (meta instanceof IConnectsToBioPipe && !connections.contains(meta)) {
                    connections.addAll(((IConnectsToBioPipe) meta).getConnected(output, connections));
                }
            }
        }
        return connections;
    }

    @Override
    public boolean isComponentsInputFacing(ForgeDirection side) {
        return true;
    }

    public boolean onWrenchRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer entityPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {

        if (GTMod.gregtechproxy.gt6Pipe) {
            final int mode = MetaGeneratedTool.getToolMode(aTool);
            IGregTechTileEntity currentPipeBase = getBaseMetaTileEntity();
            MTEBioPipe currentPipe = (MTEBioPipe) currentPipeBase.getMetaTileEntity();
            final ForgeDirection tSide = GTUtility.determineWrenchingSide(side, aX, aY, aZ);

            if (mode == ToolModes.REGULAR.get()) {
                currentPipe.connectPipeOnSide(tSide, entityPlayer);
                return true;
            }

            if (mode == ToolModes.WRENCH_LINE.get()) {

                boolean wasActionPerformed = false;

                int limit = Other.pipeWrenchingChainRange;
                for (int connected = 0; connected < limit; connected++) {

                    TileEntity nextPipeBaseTile = currentPipeBase.getTileEntityAtSide(tSide);

                    // if next tile doesn't exist or if next tile is not GT tile
                    if (!(nextPipeBaseTile instanceof IGregTechTileEntity nextPipeBase)) {
                        return wasActionPerformed;
                    }

                    MTEBioPipe nextPipe = nextPipeBase.getMetaTileEntity() instanceof MTEBioPipe
                        ? (MTEBioPipe) nextPipeBase.getMetaTileEntity()
                        : null;

                    // if next tile entity is not a pipe
                    if (nextPipe == null) {
                        return wasActionPerformed;
                    }

                    currentPipe.connectPipeOnSide(tSide, entityPlayer);

                    wasActionPerformed = true;

                    currentPipeBase = (IGregTechTileEntity) nextPipeBase;
                    currentPipe = nextPipe;

                }
                return wasActionPerformed;
            }
        }
        return false;
    }

    public void connectPipeOnSide(ForgeDirection side, EntityPlayer entityPlayer) {
        if (!isConnectedAtSide(side)) {
            if (connect(side) > 0) {
                GTUtility.sendChatToPlayer(entityPlayer, GTUtility.trans("214", "Connected"));
            }
        } else {
            disconnect(side);
            GTUtility.sendChatToPlayer(entityPlayer, GTUtility.trans("215", "Disconnected"));
        }
    }

    @Override
    public int connect(ForgeDirection side) {
        int success = super.connect(side);
        if (success > 0) {
            if (networkOutput != null) networkOutput.queueRescan();
        }
        return success;
    }

    @Override
    public void onBlockDestroyed() {
        if (networkOutput != null) networkOutput.queueRescan();
    }

    @Override
    public boolean letsIn(CoverInfo coverInfo) {
        return true;
    }

    @Override
    public boolean letsOut(CoverInfo coverInfo) {
        return true;
    }

    @Override
    public boolean getGT6StyleConnection() {
        // Yes if GT6 pipes are enabled
        return GTMod.gregtechproxy.gt6Pipe;
    }

    @Override
    public boolean canConnect(ForgeDirection side, TileEntity tileEntity) {
        final IGregTechTileEntity baseMetaTile = getBaseMetaTileEntity();
        TileEntity tTileEntity = baseMetaTile.getTileEntityAtSide(side);
        if (tTileEntity != null) {
            IMetaTileEntity meta = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity();
            if (meta == null) return false;
            return meta instanceof IConnectsToBioPipe;
        }
        return false;
    }
}

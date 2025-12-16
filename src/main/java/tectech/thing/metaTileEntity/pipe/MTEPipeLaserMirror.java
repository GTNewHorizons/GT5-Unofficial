package tectech.thing.metaTileEntity.pipe;

import static gregtech.api.enums.Dyes.MACHINE_METAL;
import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GTMod;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IColoredTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import tectech.TecTech;
import tectech.loader.NetworkDispatcher;
import tectech.mechanics.pipe.IConnectsToEnergyTunnel;
import tectech.mechanics.pipe.PipeActivityMessage;
import tectech.thing.metaTileEntity.hatch.MTEHatchEnergyTunnel;
import tectech.util.CommonValues;

public class MTEPipeLaserMirror extends MTEPipeLaser {

    private static Textures.BlockIcons.CustomIcon EMpipe;
    private final ForgeDirection[] connectedSides = { null, null };
    private ForgeDirection chainedFrontFacing = null;

    private boolean active;

    public MTEPipeLaserMirror(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEPipeLaserMirror(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MTEPipeLaserMirror(mName);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        EMpipe = new Textures.BlockIcons.CustomIcon("iconsets/EM_LASERMIRROR");
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, int aConnections,
        int colorIndex, boolean aConnected, boolean aRedstone) {
        return new ITexture[] { TextureFactory.of(EMpipe), TextureFactory
            .of(getActive() ? EMCandyActive : EMcandy, Dyes.getModulation(colorIndex, MACHINE_METAL.getRGBA())) };
    }

    @Override
    public String[] getDescription() {
        return new String[] { CommonValues.TEC_MARK_EM, translateToLocal("gt.blockmachines.pipe.energymirror.desc.0"),
            EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD
                + translateToLocal("gt.blockmachines.pipe.energystream.desc.1"),
            EnumChatFormatting.AQUA + translateToLocal("gt.blockmachines.pipe.energystream.desc.2"),
            EnumChatFormatting.AQUA + translateToLocal("gt.blockmachines.pipe.energymirror.desc.1") };
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if ((aTick & 31) == 31) {
                if (TecTech.RANDOM.nextInt(15) == 0) {
                    NetworkDispatcher.INSTANCE.sendToAllAround(
                        new PipeActivityMessage.PipeActivityData(this),
                        new NetworkRegistry.TargetPoint(
                            aBaseMetaTileEntity.getWorld().provider.dimensionId,
                            aBaseMetaTileEntity.getXCoord(),
                            aBaseMetaTileEntity.getYCoord(),
                            aBaseMetaTileEntity.getZCoord(),
                            256));
                }
                if (active) {
                    active = false;
                }
                mConnections = 0;
                connectedSides[0] = null;
                connectedSides[1] = null;
                connectionCount = 0;
                if (aBaseMetaTileEntity.getColorization() < 0) {
                    return;
                }
                for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                    // We only allow a single bend
                    if (connectionCount < 2) {
                        final ForgeDirection oppositeSide = side.getOpposite();
                        TileEntity tTileEntity = aBaseMetaTileEntity.getTileEntityAtSide(side);
                        if (tTileEntity instanceof IColoredTileEntity) {
                            byte tColor = ((IColoredTileEntity) tTileEntity).getColorization();
                            if (tColor != aBaseMetaTileEntity.getColorization()) {
                                continue;
                            }
                        }
                        if (tTileEntity instanceof IConnectsToEnergyTunnel
                            && ((IConnectsToEnergyTunnel) tTileEntity).canConnect(oppositeSide)) {
                            mConnections |= 1 << side.ordinal();
                            connectedSides[connectionCount] = side;
                            connectionCount++;
                        } else if (tTileEntity instanceof IGregTechTileEntity && ((IGregTechTileEntity) tTileEntity)
                            .getMetaTileEntity() instanceof IConnectsToEnergyTunnel) {
                                if (((IConnectsToEnergyTunnel) ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())
                                    .canConnect(oppositeSide)) {
                                    mConnections |= 1 << side.ordinal();
                                    connectedSides[connectionCount] = side;
                                    connectionCount++;
                                }
                            }
                    }
                }
            }

        } else if (aBaseMetaTileEntity.isClientSide() && GTMod.clientProxy()
            .changeDetected() == 4) {
                aBaseMetaTileEntity.issueTextureUpdate();
            }
    }

    public ForgeDirection getBendDirection(ForgeDirection dir) {
        if (dir == null) return null;

        if (connectionCount < 2) {
            return null;
        }

        ForgeDirection a = connectedSides[0];
        ForgeDirection b = connectedSides[1];
        if (dir == a) {
            chainedFrontFacing = b.getOpposite();
            return b;
        }
        if (dir == b) {
            chainedFrontFacing = a.getOpposite();
            return a;
        }

        // the input direction is not connected to this mirror
        return null;
    }

    public ForgeDirection getChainedFrontFacing() {
        return chainedFrontFacing;
    }

    public IGregTechTileEntity bendAround(ForgeDirection inputSide) {
        byte color = getBaseMetaTileEntity().getColorization();
        if (color < 0) {
            return null;
        } else {
            ForgeDirection direction = getBendDirection(inputSide);
            if (direction == null) {
                return null;
            }
            ForgeDirection opposite = direction.getOpposite();
            for (short dist = 1; dist < 1000; dist++) {

                IGregTechTileEntity tGTTileEntity = getBaseMetaTileEntity()
                    .getIGregTechTileEntityAtSideAndDistance(direction, dist);
                if (tGTTileEntity != null && tGTTileEntity.getColorization() == color) {
                    IMetaTileEntity aMetaTileEntity = tGTTileEntity.getMetaTileEntity();
                    if (aMetaTileEntity != null) {
                        // If we hit a mirror, use the mirror's view instead
                        if (aMetaTileEntity instanceof MTEPipeLaserMirror tMirror) {
                            tGTTileEntity = tMirror.bendAround(opposite);
                            if (tGTTileEntity == null) {
                                break;
                            } else {
                                aMetaTileEntity = tGTTileEntity.getMetaTileEntity();
                                chainedFrontFacing = tMirror.getChainedFrontFacing();
                                opposite = chainedFrontFacing;
                            }
                        }

                        if (aMetaTileEntity instanceof MTEHatchEnergyTunnel
                            && opposite == tGTTileEntity.getFrontFacing()) {
                            return tGTTileEntity;
                        } else if (aMetaTileEntity instanceof MTEPipeLaser) {
                            if (((MTEPipeLaser) aMetaTileEntity).connectionCount < 2) {
                                return null;
                            } else {
                                ((MTEPipeLaser) aMetaTileEntity).markUsed();
                            }
                        } else {
                            return null;
                        }
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    @Override
    public float getCollisionThickness() {
        return 0.6f;
    }

}

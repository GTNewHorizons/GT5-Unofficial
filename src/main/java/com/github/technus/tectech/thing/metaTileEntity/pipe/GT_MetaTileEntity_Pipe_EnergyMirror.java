package com.github.technus.tectech.thing.metaTileEntity.pipe;

import static gregtech.api.enums.Dyes.MACHINE_METAL;
import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.loader.NetworkDispatcher;
import com.github.technus.tectech.mechanics.pipe.IConnectsToEnergyTunnel;
import com.github.technus.tectech.mechanics.pipe.PipeActivityMessage;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyTunnel;
import com.github.technus.tectech.util.CommonValues;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GT_Mod;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IColoredTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.PowerLogic;
import gregtech.api.logic.interfaces.PowerLogicHost;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.common.GT_Client;

public class GT_MetaTileEntity_Pipe_EnergyMirror extends GT_MetaTileEntity_Pipe_Energy {

    private static Textures.BlockIcons.CustomIcon EMpipe;
    private ForgeDirection[] connectedSides = { null, null };
    private ForgeDirection chainedFrontFacing = null;

    private boolean active;

    public GT_MetaTileEntity_Pipe_EnergyMirror(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_Pipe_EnergyMirror(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new GT_MetaTileEntity_Pipe_EnergyMirror(mName);
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
        return new ITexture[] { new GT_RenderedTexture(EMpipe),
            new GT_RenderedTexture(
                getActive() ? EMCandyActive : EMcandy,
                Dyes.getModulation(colorIndex, MACHINE_METAL.getRGBA())) };
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
                        aBaseMetaTileEntity.getWorld().provider.dimensionId,
                        aBaseMetaTileEntity.getXCoord(),
                        aBaseMetaTileEntity.getYCoord(),
                        aBaseMetaTileEntity.getZCoord(),
                        256);
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
                        if (tTileEntity instanceof PowerLogicHost) {
                            PowerLogic logic = ((PowerLogicHost) tTileEntity).getPowerLogic(oppositeSide);
                            if (logic != null && logic.canUseLaser()) {
                                mConnections |= 1 << side.ordinal();
                                connectedSides[connectionCount] = side;
                                connectionCount++;
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

        } else if (aBaseMetaTileEntity.isClientSide() && GT_Client.changeDetected == 4) {
            aBaseMetaTileEntity.issueTextureUpdate();
        }
    }

    public ForgeDirection getBendDirection(ForgeDirection dir) {
        for (ForgeDirection bendDir : connectedSides) {
            if (bendDir != dir) {
                chainedFrontFacing = bendDir.getOpposite();
                return bendDir;
            }
        }
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
                        if (aMetaTileEntity instanceof GT_MetaTileEntity_Pipe_EnergyMirror tMirror) {
                            tGTTileEntity = tMirror.bendAround(opposite);
                            if (tGTTileEntity == null) {
                                break;
                            } else {
                                aMetaTileEntity = tGTTileEntity.getMetaTileEntity();
                                chainedFrontFacing = tMirror.getChainedFrontFacing();
                                opposite = chainedFrontFacing;
                            }
                        }

                        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_EnergyTunnel
                            && opposite == tGTTileEntity.getFrontFacing()) {
                            return tGTTileEntity;
                        } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Pipe_Energy) {
                            if (((GT_MetaTileEntity_Pipe_Energy) aMetaTileEntity).connectionCount < 2) {
                                return null;
                            } else {
                                ((GT_MetaTileEntity_Pipe_Energy) aMetaTileEntity).markUsed();
                            }
                        } else {
                            return null;
                        }
                    } else {
                        if (tGTTileEntity instanceof PowerLogicHost) {
                            PowerLogic logic = ((PowerLogicHost) tGTTileEntity).getPowerLogic(opposite);
                            if (logic == null || !logic.canUseLaser() || opposite != tGTTileEntity.getFrontFacing()) {
                                return tGTTileEntity;
                            }
                        }
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
    public float getThickNess() {
        if (GT_Mod.instance.isClientSide() && GT_Client.hideValue == 1) {
            return 0.0625F;
        }
        return 0.6f;
    }

}

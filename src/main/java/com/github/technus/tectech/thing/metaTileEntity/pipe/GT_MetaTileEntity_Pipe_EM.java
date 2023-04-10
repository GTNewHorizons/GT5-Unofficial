package com.github.technus.tectech.thing.metaTileEntity.pipe;

import static gregtech.api.enums.Dyes.MACHINE_METAL;
import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.loader.NetworkDispatcher;
import com.github.technus.tectech.mechanics.pipe.IActivePipe;
import com.github.technus.tectech.mechanics.pipe.IConnectsToElementalPipe;
import com.github.technus.tectech.mechanics.pipe.PipeActivityMessage;
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
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Client;

/**
 * Created by Tec on 26.02.2017.
 */
public class GT_MetaTileEntity_Pipe_EM extends MetaPipeEntity implements IConnectsToElementalPipe, IActivePipe {

    private static Textures.BlockIcons.CustomIcon EMpipe;
    static Textures.BlockIcons.CustomIcon EMcandy, EMCandyActive;
    public byte connectionCount = 0;

    private boolean active;

    public GT_MetaTileEntity_Pipe_EM(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, 0);
    }

    public GT_MetaTileEntity_Pipe_EM(String aName) {
        super(aName, 0);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new GT_MetaTileEntity_Pipe_EM(mName);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        EMpipe = new Textures.BlockIcons.CustomIcon("iconsets/EM_PIPE");
        EMcandy = new Textures.BlockIcons.CustomIcon("iconsets/EM_CANDY");
        EMCandyActive = new Textures.BlockIcons.CustomIcon("iconsets/EM_CANDY_ACTIVE");
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aConnections,
            byte aColorIndex, boolean aConnected, boolean aRedstone) {
        return new ITexture[] { new GT_RenderedTexture(EMpipe),
                new GT_RenderedTexture(
                        getActive() ? EMCandyActive : EMcandy,
                        Dyes.getModulation(aColorIndex, MACHINE_METAL.getRGBA())) };
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity iGregTechTileEntity, int i, byte b, ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity iGregTechTileEntity, int i, byte b, ItemStack itemStack) {
        return false;
    }

    @Override
    public void loadNBTData(NBTTagCompound nbtTagCompound) {
        active = nbtTagCompound.getBoolean("eActive");
    }

    @Override
    public void saveNBTData(NBTTagCompound nbtTagCompound) {
        nbtTagCompound.setBoolean("eActive", active);
    }

    @Override
    public boolean renderInside(byte b) {
        return false;
    }

    @Override
    public byte getTileEntityBaseType() {
        return 4;
    }

    @Override
    public String[] getDescription() {
        return new String[] { CommonValues.TEC_MARK_EM,
                translateToLocal("gt.blockmachines.pipe.elementalmatter.desc.0"), // Quantum tunneling device.
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD
                        + translateToLocal("gt.blockmachines.pipe.elementalmatter.desc.1"), // Not a portal!!!
                EnumChatFormatting.AQUA + translateToLocal("gt.blockmachines.pipe.elementalmatter.desc.2"), // Must be
                                                                                                            // painted
                                                                                                            // to work
                EnumChatFormatting.AQUA + translateToLocal("gt.blockmachines.pipe.elementalmatter.desc.3") // Do not
                                                                                                           // cross,
                                                                                                           // split or
                                                                                                           // turn
        };
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        if (getBaseMetaTileEntity().isClientSide()) {
            NetworkDispatcher.INSTANCE.sendToServer(new PipeActivityMessage.PipeActivityQuery(this));
        }
        onPostTick(aBaseMetaTileEntity, 31);
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
                connectionCount = 0;
                if (aBaseMetaTileEntity.getColorization() < 0) {
                    return;
                }
                for (byte b0 = 0, b1; b0 < 6; b0++) {
                    b1 = GT_Utility.getOppositeSide(b0);
                    // if (!aBaseMetaTileEntity.getCoverBehaviorAtSide(b0).alwaysLookConnected(b0,
                    // aBaseMetaTileEntity.getCoverIDAtSide(b0), aBaseMetaTileEntity.getCoverDataAtSide(b0),
                    // aBaseMetaTileEntity)) {
                    TileEntity tTileEntity = aBaseMetaTileEntity.getTileEntityAtSide(b0);
                    if (tTileEntity instanceof IColoredTileEntity) {
                        // if (aBaseMetaTileEntity.getColorization() >= 0) {
                        byte tColor = ((IColoredTileEntity) tTileEntity).getColorization();
                        if (tColor != aBaseMetaTileEntity.getColorization()) {
                            continue;
                        }
                        // }
                    }
                    if (tTileEntity instanceof IConnectsToElementalPipe
                            && ((IConnectsToElementalPipe) tTileEntity).canConnect(b1)) {
                        mConnections |= 1 << b0;
                        connectionCount++;
                    } else if (tTileEntity instanceof IGregTechTileEntity && ((IGregTechTileEntity) tTileEntity)
                            .getMetaTileEntity() instanceof IConnectsToElementalPipe) {
                                if (((IConnectsToElementalPipe) ((IGregTechTileEntity) tTileEntity).getMetaTileEntity()).canConnect(b1)) {
                                    mConnections |= 1 << b0;
                                    connectionCount++;
                                }
                            }
                }
            }
        } else if (aBaseMetaTileEntity.isClientSide() && GT_Client.changeDetected == 4) {
            aBaseMetaTileEntity.issueTextureUpdate();
        }
    }

    @Override
    public boolean canConnect(byte side) {
        return true;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World aWorld, int aX, int aY, int aZ) {
        float tSpace = (1f - 0.5f) / 2;
        float tSide0 = tSpace;
        float tSide1 = 1f - tSpace;
        float tSide2 = tSpace;
        float tSide3 = 1f - tSpace;
        float tSide4 = tSpace;
        float tSide5 = 1f - tSpace;

        if (getBaseMetaTileEntity().getCoverIDAtSide((byte) 0) != 0) {
            tSide0 = tSide2 = tSide4 = 0;
            tSide3 = tSide5 = 1;
        }
        if (getBaseMetaTileEntity().getCoverIDAtSide((byte) 1) != 0) {
            tSide2 = tSide4 = 0;
            tSide1 = tSide3 = tSide5 = 1;
        }
        if (getBaseMetaTileEntity().getCoverIDAtSide((byte) 2) != 0) {
            tSide0 = tSide2 = tSide4 = 0;
            tSide1 = tSide5 = 1;
        }
        if (getBaseMetaTileEntity().getCoverIDAtSide((byte) 3) != 0) {
            tSide0 = tSide4 = 0;
            tSide1 = tSide3 = tSide5 = 1;
        }
        if (getBaseMetaTileEntity().getCoverIDAtSide((byte) 4) != 0) {
            tSide0 = tSide2 = tSide4 = 0;
            tSide1 = tSide3 = 1;
        }
        if (getBaseMetaTileEntity().getCoverIDAtSide((byte) 5) != 0) {
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
        if (GT_Mod.instance.isClientSide() && GT_Client.hideValue == 1) {
            return 0.0625F;
        }
        return 0.5f;
    }

    @Override
    public void markUsed() {
        this.active = true;
    }

    @Override
    public void setActive(boolean state) {
        if (state != active) {
            active = state;
            getBaseMetaTileEntity().issueTextureUpdate();
        }
    }

    @Override
    public boolean getActive() {
        return active;
    }

    @Override
    public void onRemoval() {
        if (getActive()) {
            TecTech.anomalyHandler.addAnomaly(getBaseMetaTileEntity(), 1e10f);
        }
    }
}

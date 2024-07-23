package com.elisis.gtnhlanth.common.beamline;

import static gregtech.api.enums.Dyes.MACHINE_METAL;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GT_Mod;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.common.GT_Client;
import gregtech.common.render.GT_TextureBuilder;

public class TileBeamline extends MetaPipeEntity implements IConnectsToBeamline {

    private static Textures.BlockIcons.CustomIcon pipe;

    private byte connectionCount = 0;

    private boolean active;

    public TileBeamline(int id, String name, String nameRegional) {
        super(id, name, nameRegional, 0);
    }

    public TileBeamline(String name) {
        super(name, 0);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if ((aTick & 31) == 31) {
                mConnections = 0;
                connectionCount = 0;

                for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                    ForgeDirection d1 = dir.getOpposite();
                    TileEntity tTileEntity = aBaseMetaTileEntity.getTileEntityAtSide(dir);
                    if (tTileEntity instanceof IConnectsToBeamline) {
                        if (((IConnectsToBeamline) tTileEntity).canConnect(d1)) {
                            mConnections |= 1 << dir.ordinal();
                            connectionCount++;
                        }
                    } else if (tTileEntity instanceof IGregTechTileEntity) {
                        IMetaTileEntity meta = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity();
                        if (meta instanceof IConnectsToBeamline) {
                            if (((IConnectsToBeamline) meta).canConnect(d1)) {
                                mConnections |= 1 << dir.ordinal();
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

    @Override
    public byte getTileEntityBaseType() {
        return 7;
    }

    @Override
    public void loadNBTData(NBTTagCompound arg0) {}

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity arg0) {
        return new TileBeamline(mName);
    }

    @Override
    public void saveNBTData(NBTTagCompound arg0) {}

    @Override
    public float getThickNess() {
        if (GT_Mod.instance.isClientSide() && GT_Client.hideValue == 1) {
            return 0.0625F;
        }
        return 0.5f;
    }

    @Override
    public boolean renderInside(ForgeDirection arg0) {
        return false;
    }

    @Override
    public boolean canConnect(ForgeDirection side) {
        return true;
    }

    // Largely taken from Tec's DataPipe

    @Override
    public IConnectsToBeamline getNext(IConnectsToBeamline source) {

        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {

            if ((mConnections & 1 << dir.ordinal()) == 0) {
                continue;
            }

            TileEntity next = this.getBaseMetaTileEntity()
                .getTileEntityAtSide(dir);
            if (next instanceof IConnectsToBeamline && next != source) {

                if (((IConnectsToBeamline) next).isDataInputFacing(dir.getOpposite())) {
                    return (IConnectsToBeamline) next;
                }

            } else if (next instanceof IGregTechTileEntity) {

                IMetaTileEntity meta = ((IGregTechTileEntity) next).getMetaTileEntity();
                if (meta instanceof IConnectsToBeamline && meta != source) {

                    if (meta instanceof TileBeamline && (((TileBeamline) meta).connectionCount == 2)) {

                        ((TileBeamline) meta).markUsed();
                        return (IConnectsToBeamline) meta;
                    }

                    if (((IConnectsToBeamline) meta).isDataInputFacing(dir.getOpposite())) {

                        return (IConnectsToBeamline) meta;
                    }
                }
            }
        }

        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        pipe = new Textures.BlockIcons.CustomIcon("iconsets/pipe");
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection aSide, int aConnections,
        int aColorIndex, boolean aConnected, boolean aRedstone) {
        return new ITexture[] { new GT_TextureBuilder().addIcon(pipe)
            .build(),
            new GT_TextureBuilder().addIcon(pipe)
                .setRGBA(Dyes.getModulation((byte) aColorIndex, MACHINE_METAL.getRGBA()))
                .build() };
    }

    public void markUsed() {
        this.active = true;
    }

    @Override
    public boolean isDataInputFacing(ForgeDirection side) {
        return true;
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
    public String[] getDescription() {
        return new String[] { StatCollector.translateToLocal("beamline.pipe.desc.0"), // Beamline pipe
            EnumChatFormatting.AQUA + StatCollector.translateToLocal("beamline.pipe.desc.1"), // Does not cross, split
                                                                                              // or turn
            "Added by " + EnumChatFormatting.GREEN + "GTNH: Lanthanides"

        };
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
}

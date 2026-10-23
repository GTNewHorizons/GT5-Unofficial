package gtnhlanth.common.beamline;

import static gregtech.api.enums.Dyes.MACHINE_METAL;

import gregtech.api.metatileentity.BaseMetaPipeEntity;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.HarvestTool;
import gregtech.api.enums.Mods;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.common.render.GTTextureBuilder;
import tectech.mechanics.boseEinsteinCondensate.BECFactoryElement;

public class MTEBeamlinePipe extends MetaPipeEntity implements IConnectsToBeamline {

    private static IIconContainer pipe;

    public MTEBeamlinePipe(int id, String name, String nameRegional) {
        super(id, name, 0);
    }

    public MTEBeamlinePipe(String name) {
        super(name, 0);
    }

    @Override
    public boolean needsClientTick() {
        return false;
    }

    protected void checkConnections() {
        mCheckConnections = false;
        mConnections = 0;

        IGregTechTileEntity base = getBaseMetaTileEntity();
        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            ForgeDirection d1 = dir.getOpposite();
            TileEntity tTileEntity = base.getTileEntityAtSide(dir);
            if (tTileEntity instanceof IConnectsToBeamline neigh) {
                if (neigh.canConnect(d1)) {
                    mConnections |= 1 << dir.ordinal();
                }
            } else if (tTileEntity instanceof IGregTechTileEntity neighBase) {
                if (neighBase.getMetaTileEntity() instanceof IConnectsToBeamline neigh) {
                    if (neigh.canConnect(d1)) {
                        mConnections |= 1 << dir.ordinal();
                    }
                }
            }
        }

        if (base instanceof BaseMetaPipeEntity pipe) {
            pipe.updateConnections();
            pipe.syncConnectionToClient();
        }
    }

    @Override
    public void onFirstTick(IGregTechTileEntity base) {
        super.onFirstTick(base);
        if (base.isServerSide()) {
            checkConnections();
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (aTick > 1) {
                aBaseMetaTileEntity.tryDisableTicking();
            }
        }
    }

    @Override
    public byte getTileEntityBaseType() {
        return HarvestTool.WrenchPipeLevel3.toTileEntityBaseType();
    }

    @Override
    public void loadNBTData(NBTTagCompound arg0) {}

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity arg0) {
        return new MTEBeamlinePipe(mName);
    }

    @Override
    public void saveNBTData(NBTTagCompound arg0) {}

    @Override
    public float getCollisionThickness() {
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

        /*
         * for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) { if ((mConnections & 1 << dir.ordinal()) == 0) {
         * continue; } TileEntity next = this.getBaseMetaTileEntity() .getTileEntityAtSide(dir); if (next instanceof
         * IConnectsToBeamline && next != source) { if (((IConnectsToBeamline)
         * next).isDataInputFacing(dir.getOpposite())) { return (IConnectsToBeamline) next; } } else if (next instanceof
         * IGregTechTileEntity) { IMetaTileEntity meta = ((IGregTechTileEntity) next).getMetaTileEntity(); if (meta
         * instanceof IConnectsToBeamline && meta != source) { if (meta instanceof TileBeamline && (((TileBeamline)
         * meta).connectionCount == 2)) { ((TileBeamline) meta).markUsed(); return (IConnectsToBeamline) meta; } if
         * (((IConnectsToBeamline) meta).isDataInputFacing(dir.getOpposite())) { return (IConnectsToBeamline) meta; } }
         * } }
         */

        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        pipe = Textures.BlockIcons.custom(Mods.GregTech.resourceDomain, "iconsets/pipe");
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection aSide, int aConnections,
        int aColorIndex, boolean aConnected, boolean aRedstone) {
        return new ITexture[] { new GTTextureBuilder().addIcon(pipe)
            .build(),
            new GTTextureBuilder().addIcon(pipe)
                .setRGBA(Dyes.getModulation(aColorIndex, MACHINE_METAL.getRGBA()))
                .build() };
    }

    @Deprecated
    public void markUsed() {}

    @Override
    public boolean isDataInputFacing(ForgeDirection side) {
        return true;
    }

    @Override
    public String[] getDescription() {
        return new String[] { StatCollector.translateToLocal("beamline.pipe.desc.0"), // Beamline pipe
            EnumChatFormatting.AQUA + StatCollector.translateToLocal("beamline.pipe.desc.1") // Does not cross,
                                                                                             // split
                                                                                             // or turn
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

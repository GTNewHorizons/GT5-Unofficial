package gregtech.api.factory.artificialorganisms;

import java.util.Collection;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
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
import gregtech.api.objects.ArtificialOrganism;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.common.config.Other;
import gregtech.common.covers.CoverInfo;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import tectech.thing.metaTileEntity.pipe.MTEBaseFactoryPipe;

public class MTEBioPipe extends MTEBaseFactoryPipe implements AOFactoryElement {

    private static Textures.BlockIcons.CustomIcon pipeTexture;

    public MTEBioPipe(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEBioPipe(MTEBioPipe prototype) {
        super(prototype);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MTEBioPipe(this);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        ArtificialOrganism AO = network.getAO();

        tag.setString("species", AO != null ? AO.toString() : "INVALID");
        tag.setString("network", network == null ? "null" : network.toString());
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currenttip, accessor, config);
        currenttip.add(
            "Species: " + accessor.getNBTData()
                .getString("species"));
        currenttip.add(
            "Network: " + accessor.getNBTData()
                .getString("network"));
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        getNetwork().initiateHavoc();
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
    public String[] getDescription() {
        return new String[] { StatCollector.translateToLocal("GT5U.pipe.bio.desc.0"),
            StatCollector.translateToLocal("GT5U.pipe.bio.desc.1"),
            StatCollector.translateToLocal("GT5U.pipe.bio.desc.2") };
    }

    @Override
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
        AOFactoryGrid.INSTANCE.addElement(this);
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
    public boolean canConnectOnSide(ForgeDirection side) {
        return true;
    }

    @Override
    public void havocEvent() {
        queuedHavoc = true;
    }

    @Override
    public boolean canConnect(ForgeDirection side, TileEntity tileEntity) {
        final IGregTechTileEntity baseMetaTile = getBaseMetaTileEntity();
        TileEntity tTileEntity = baseMetaTile.getTileEntityAtSide(side);
        if (tTileEntity != null) {
            IMetaTileEntity meta = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity();
            if (meta == null) return false;
            return meta instanceof AOFactoryElement;
        }
        return false;
    }

    boolean queuedHavoc = false;

    @Override
    public void onPostTick(IGregTechTileEntity base, long aTick) {
        super.onPostTick(base, aTick);
        if (queuedHavoc) getBaseMetaTileEntity().setToFire();
    }

    private AOFactoryNetwork network;

    // Only detect connected neighbors instead of any
    @Override
    public void getNeighbours(Collection<AOFactoryElement> neighbours) {
        IGregTechTileEntity base = getBaseMetaTileEntity();

        if (base == null || base.isDead()) return;

        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            if (base.getTileEntityAtSide(dir) instanceof IGregTechTileEntity igte) {
                if (igte.getMetaTileEntity() instanceof AOFactoryElement element) {
                    if (isConnectedAtSide(dir)) neighbours.add(element);
                }
            }
        }
    }

    @Override
    public void onNeighbourChanged(AOFactoryElement neighbour) {
        mCheckConnections = true;
    }

    @Override
    public AOFactoryNetwork getNetwork() {
        return network;
    }

    @Override
    public void setNetwork(AOFactoryNetwork network) {
        this.network = network;
        mCheckConnections = true;
    }

    @Override
    public void onFirstTick(IGregTechTileEntity base) {
        super.onFirstTick(base);

        AOFactoryGrid.INSTANCE.addElement(this);
    }

    @Override
    public void onRemoval() {
        super.onRemoval();

        AOFactoryGrid.INSTANCE.removeElement(this);
    }
}

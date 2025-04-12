package gregtech.api.factory.artificialorganisms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.common.covers.Cover;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
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
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import tectech.thing.metaTileEntity.pipe.MTEBaseFactoryPipe;

import static gregtech.api.objects.XSTR.XSTR_INSTANCE;
import static gtnhlanth.common.register.WerkstoffMaterialPool.Gangue;

public class MTEBioPipe extends MTEBaseFactoryPipe implements AOFactoryElement {

    private static Textures.BlockIcons.CustomIcon pipeTexture;
    private static Textures.BlockIcons.CustomIcon pipeTextureRuined;

    private AOFactoryNetwork network;
    boolean isRuined = false;
    boolean queuedHavoc = false;

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

        tag.setBoolean("ruined", isRuined);
        tag.setString("species", AO != null ? AO.toString() : "INVALID");
        tag.setString("network", network == null ? "null" : network.toString());
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currenttip, accessor, config);
        NBTTagCompound tag = accessor.getNBTData();
        if (!tag.getBoolean("ruined")) {
            currenttip.add(
                "Species: " + tag.getString("species"));
            currenttip.add(
                "Network: " + tag.getString("network"));
        } else {
            currenttip.add(EnumChatFormatting.RED + "DESTROYED");
            currenttip.add("Connections broken...");
        }
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
        pipeTextureRuined = new Textures.BlockIcons.CustomIcon("iconsets/BIOPIPE_RUINED");
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, int aConnections,
        int colorIndex, boolean aConnected, boolean aRedstone) {
        return new ITexture[] { TextureFactory.of(isRuined ? pipeTextureRuined : pipeTexture) };
    }

    //TODO: uncomment when dropped item pr merges

    /*@Override
    public ArrayList<ItemStack> getDroppedItem() {
        if (!isRuined) return null;
        ArrayList<ItemStack> drops = new ArrayList<>();
        drops.add(Gangue.get(OrePrefixes.dust, 8));
        drops.add(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 2));
        return drops;
    }
     */

    @Override
    public void onValueUpdate(byte aValue) {
        boolean oldValue = isRuined;
        isRuined = (aValue & 1) == 1;
        if (oldValue != isRuined) getBaseMetaTileEntity().issueTextureUpdate();
    }

    @Override
    public byte getUpdateData() {
        return (byte) (isRuined ? 1 : 0);
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

    @Override
    public boolean letsIn(Cover cover) {
        return true;
    }

    @Override
    public boolean letsOut(Cover cover) {
        return true;
    }

    @Override
    public boolean allowCoverOnSide(ForgeDirection side, ItemStack coverItem) {
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
    public boolean getGT6StyleConnection() {
        // Yes if GT6 pipes are enabled
        return GTMod.gregtechproxy.gt6Pipe;
    }

    @Override
    public boolean canConnectOnSide(ForgeDirection side) {
        return true;
    }

    @Override
    public void sentienceEvent() {
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

    @Override
    public void onPostTick(IGregTechTileEntity base, long aTick) {
        super.onPostTick(base, aTick);
        if (queuedHavoc) {
            isRuined = true;
        }
    }

    // Only detect connected neighbors instead of any
    // Ruined pipes don't connect to anything and cannot be connected to
    @Override
    public void getNeighbours(Collection<AOFactoryElement> neighbours) {
        IGregTechTileEntity base = getBaseMetaTileEntity();
        if (isRuined || base == null || base.isDead()) return;
        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            if (base.getTileEntityAtSide(dir) instanceof IGregTechTileEntity igte) {
                if (igte.getMetaTileEntity() instanceof AOFactoryElement element) {
                    if (element instanceof MTEBioPipe pipe && pipe.isRuined) return;
                    if (isConnectedAtSide(dir)) neighbours.add(element);
                }
            }
        }
    }

    @Override
    public void onBlockDestroyed() {
        super.onBlockDestroyed();
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

    @Override
    public void saveNBTData(NBTTagCompound nbtTagCompound) {
        super.saveNBTData(nbtTagCompound);
        nbtTagCompound.setBoolean("isRuined", isRuined);
    }

    @Override
    public void loadNBTData(NBTTagCompound nbtTagCompound) {
        super.loadNBTData(nbtTagCompound);
        isRuined = nbtTagCompound.getBoolean("isRuined");
    }
}

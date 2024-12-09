package gregtech.api.factory.artificialorganisms;

import java.util.Collection;
import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.ArtificialOrganism;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import tectech.thing.metaTileEntity.hatch.MTEBaseFactoryHatch;

public class MTEHatchAO extends MTEBaseFactoryHatch implements AOFactoryElement {

    public MTEHatchAO(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, new String[] { "Distributes and Receives Artificial Organisms" });
    }

    public MTEHatchAO(MTEHatchAO prototype) {
        super(prototype);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setString(
            "species",
            network.valid ? network.getSpecies()
                .toString() : "INVALID");
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currenttip, accessor, config);
        currenttip.add(
            "Species: " + accessor.getNBTData()
                .getString("species"));
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[mTier] };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[mTier] };
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchAO(this);
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public ConnectionType getConnectionType() {
        return ConnectionType.BIO;
    }

    @Override
    public boolean canConnectOnSide(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing();
    }

    public void setSpecies(ArtificialOrganism species) {
        network.addSpecies(species);
    }

    // Hatches only have one connectable face, so this will just check that face
    @Override
    public void getNeighbours(Collection<AOFactoryElement> neighbours) {
        IGregTechTileEntity base = getBaseMetaTileEntity();

        if (base == null || base.isDead()) return;
        ForgeDirection dir = base.getFrontFacing();
        if (base.getTileEntityAtSide(dir) instanceof IGregTechTileEntity igte) {
            if (igte.getMetaTileEntity() instanceof AOFactoryElement element) {
                // Handle pipes separately because of connection check
                if (element instanceof MTEBioPipe pipe) {
                    if (pipe.isConnectedAtSide(dir.getOpposite())) {
                        neighbours.add(element);
                    }
                }
                // Hatches can just check facing direction
                else if (element.canConnectOnSide(dir)) {
                    neighbours.add(element);
                }
            }
        }
    }

    private AOFactoryNetwork network;

    @Override
    public AOFactoryNetwork getNetwork() {
        return network;
    }

    @Override
    public void setNetwork(AOFactoryNetwork network) {
        this.network = network;
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);

        AOFactoryGrid.INSTANCE.addElement(this);
    }

    @Override
    public void onRemoval() {
        super.onRemoval();

        AOFactoryGrid.INSTANCE.removeElement(this);
    }

    @Override
    public void onFacingChange() {
        super.onFacingChange();

        AOFactoryGrid.INSTANCE.addElement(this);
    }
}

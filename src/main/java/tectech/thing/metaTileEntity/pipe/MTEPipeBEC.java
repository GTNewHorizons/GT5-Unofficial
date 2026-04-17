package tectech.thing.metaTileEntity.pipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.ArrayUtils;

import com.gtnewhorizon.gtnhlib.util.data.Lazy;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTValues;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.tooltip.MarkdownTooltipLoader;
import tectech.mechanics.boseEinsteinCondensate.BECFactoryElement;
import tectech.mechanics.boseEinsteinCondensate.BECFactoryGrid;
import tectech.mechanics.boseEinsteinCondensate.BECFactoryNetwork;
import tectech.mechanics.boseEinsteinCondensate.BECInventory;

public class MTEPipeBEC extends MTEBaseFactoryPipe implements BECFactoryElement {

    private BECFactoryNetwork network;
    private int oldColour;

    private final Lazy<List<String>> tooltip;

    public MTEPipeBEC(int aID, String aName) {
        super(aID, aName);
        mThickness = 3f / 4f;

        tooltip = new Lazy<>(
            () -> MarkdownTooltipLoader.STANDARD
                .loadStandardPath(new ResourceLocation("gregtech", "bec-pipe"), new HashMap<>()));
    }

    public MTEPipeBEC(MTEPipeBEC prototype) {
        super(prototype);

        tooltip = prototype.tooltip;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MTEPipeBEC(this);
    }

    @Override
    public String[] getDescription() {
        return ArrayUtils.addAll(
            super.getDescription(),
            tooltip.get()
                .toArray(GTValues.emptyStringArray));
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity base, ForgeDirection side, int aConnections, int colorIndex,
        boolean aConnected, boolean aRedstone) {

        List<ITexture> textures = new ArrayList<>(2);

        textures.add(
            TextureFactory.builder()
                .addIcon(EM_PIPE)
                .setRGBA(Dyes.getModulation(colorIndex, new short[] { 0x81, 0xCA, 0xED, 0 }))
                .build());

        if (getActive()) {
            textures.add(
                TextureFactory.builder()
                    .addIcon(EM_BAR)
                    .setRGBA(Dyes.getModulation(colorIndex, new short[] { 0x81, 0xCA, 0xED, 0 }))
                    .build());
        }

        return textures.toArray(new ITexture[0]);
    }

    @Override
    protected void checkActive() {
        mIsActive = network != null && !network.getComponents(BECInventory.class)
            .isEmpty();
    }

    @Override
    public ConnectionType getConnectionOnSide(ForgeDirection side) {
        return ConnectionType.CONNECTABLE;
    }

    @Override
    public void getNeighbours(Collection<BECFactoryElement> neighbours) {
        IGregTechTileEntity base = getBaseMetaTileEntity();

        if (base == null || base.isDead()) return;

        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            if (this.getConnectionOnSide(dir) != ConnectionType.CONNECTABLE) continue;

            if (!(base.getTileEntityAtSide(dir) instanceof IGregTechTileEntity igte)) continue;

            if (igte.getColorization() != base.getColorization()) continue;

            if (!(igte.getMetaTileEntity() instanceof BECFactoryElement element)) continue;

            if (element.getConnectionOnSide(dir.getOpposite()) != ConnectionType.CONNECTABLE) continue;

            neighbours.add(element);
        }
    }

    @Override
    public void onAdjacentBlockChange(int x, int y, int z) {
        mCheckConnections = true;
    }

    @Override
    public void onEdgeChanged(BECFactoryElement adjacent) {
        mCheckConnections = true;
    }

    @Override
    protected void checkConnections() {
        mConnections = 0;

        IGregTechTileEntity base = getBaseMetaTileEntity();

        if (base == null || base.isDead()) return;

        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            if (base.getTileEntityAtSide(side) instanceof IGregTechTileEntity igte) {
                if (igte.getColorization() == base.getColorization()) {
                    if (igte.getMetaTileEntity() instanceof BECFactoryElement element) {
                        if (element.getConnectionOnSide(side.getOpposite()) != ConnectionType.NONE) {
                            mConnections |= side.flag;
                        }
                    }
                }
            }
        }
    }

    @Override
    public String[] getInfoData() {
        List<String> data = new ArrayList<>(Arrays.asList(super.getInfoData()));

        data.add("Network: " + (network == null ? "None" : network.id));

        return data.toArray(new String[0]);
    }

    @Override
    public BECFactoryNetwork getNetwork() {
        return network;
    }

    @Override
    public void setNetwork(BECFactoryNetwork network) {
        this.network = network;
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);

        if (GTUtility.isServer()) {
            BECFactoryGrid.INSTANCE.updateElement(this);
        }
        oldColour = aBaseMetaTileEntity.getColorization();
    }

    @Override
    public void onRemoval() {
        super.onRemoval();

        if (GTUtility.isServer()) {
            BECFactoryGrid.INSTANCE.removeElement(this);
        }
    }

    @Override
    public void onColorChangeServer(byte aColor) {
        IGregTechTileEntity base = getBaseMetaTileEntity();

        if (base == null || base.getTimer() == 0) return;

        if (oldColour == aColor) return;
        oldColour = aColor;

        if (GTUtility.isServer()) {
            BECFactoryGrid.INSTANCE.updateElement(this);
        }
    }
}

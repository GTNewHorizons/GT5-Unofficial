package tectech.thing.metaTileEntity.multi.base;

import static gregtech.api.casing.Casings.MolecularCasing;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;

import gregtech.api.enums.StructureError;
import gregtech.api.enums.Textures;
import gregtech.api.factory.RoutedNode;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.IStructureInstance;
import gregtech.api.structure.IStructureProvider;
import gregtech.api.structure.StructureWrapper;
import gregtech.api.structure.StructureWrapperInstanceInfo;
import gregtech.api.util.GTUtility;
import gregtech.api.util.IGTHatchAdder;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import it.unimi.dsi.fastutil.objects.ObjectIntPair;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import tectech.mechanics.boseEinsteinCondensate.BECFactoryElement;
import tectech.mechanics.boseEinsteinCondensate.BECFactoryGrid;
import tectech.mechanics.boseEinsteinCondensate.BECFactoryNetwork;
import tectech.mechanics.boseEinsteinCondensate.BECRouteInfo;
import tectech.mechanics.boseEinsteinCondensate.NotableBECFactoryElement;
import tectech.thing.metaTileEntity.hatch.bec.MTEHatchBEC;

public abstract class MTEBECMultiblockBase<TSelf extends MTEBECMultiblockBase<TSelf>> extends TTMultiblockBase
    implements ISurvivalConstructable, BECFactoryElement, NotableBECFactoryElement, IStructureProvider<TSelf> {

    protected static final String STRUCTURE_PIECE_MAIN = "main";

    protected final List<MTEHatchBEC> mBECHatches = new ArrayList<>();

    protected BECFactoryNetwork network;

    protected final StructureWrapper<TSelf> structure;
    protected final StructureWrapperInstanceInfo<TSelf> structureInstanceInfo;

    public MTEBECMultiblockBase(int id, String name) {
        super(id, name, GTUtility.translate("gt.blockmachines." + name + ".name"));

        structure = new StructureWrapper<>(this);
        structureInstanceInfo = null;

        structure.loadStructure();
    }

    protected MTEBECMultiblockBase(TSelf base) {
        super(base.mName);

        structure = base.structure;
        structureInstanceInfo = new StructureWrapperInstanceInfo<>(structure);
    }

    @Override
    public boolean shouldCheckMaintenance() {
        return false;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {

        ArrayList<ITexture> textures = new ArrayList<>(2);

        textures.add(getCasingTexture());

        if (side == facing) {
            textures.add(getBackgroundTexture());

            if (active) {
                textures.add(getActiveTexture());
            }
        }

        return textures.toArray(new ITexture[0]);
    }

    protected ITexture getCasingTexture() {
        return MolecularCasing.getCasingTexture();
    }

    protected ITexture getBackgroundTexture() {
        return TextureFactory.builder()
            .addIcon(Textures.BlockIcons.BEC_CONTROLLER_BACKGROUND)
            .extFacing()
            .build();
    }

    protected ITexture getActiveTexture() {
        return TextureFactory.builder()
            .addIcon(TexturesGtBlock.oMCAAdvancedEBFActive)
            .extFacing()
            .glow()
            .build();
    }

    @Override
    public IStructureDefinition<? extends TTMultiblockBase> getStructure_EM() {
        return structure.structureDefinition;
    }

    @Override
    public IStructureInstance getStructureInstance() {
        return structureInstanceInfo;
    }

    private List<MTEHatchBEC> mPreviousBECHatches;

    @Override
    protected void clearHatches_EM() {
        super.clearHatches_EM();

        mPreviousBECHatches = new ArrayList<>(mBECHatches);

        mBECHatches.forEach(h -> h.removeController(this));
        mBECHatches.clear();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structure.construct((TSelf) this, stackSize, hintsOnly);
    }

    @Override
    @SuppressWarnings("unchecked")
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        return structure.survivalConstruct((TSelf) this, stackSize, elementBudget, env);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        boolean success = structure.checkStructure((TSelf) this);

        if (!Objects.equals(mPreviousBECHatches, mBECHatches)) {
            BECFactoryGrid.INSTANCE.updateElement(this);
        }

        return success;
    }

    @Override
    protected void validateStructure(Collection<StructureError> errors, NBTTagCompound context) {
        structureInstanceInfo.validate(errors, context);
    }

    @Override
    protected void localizeStructureErrors(Collection<StructureError> errors, NBTTagCompound context,
        List<String> lines) {
        structureInstanceInfo.localizeStructureErrors(errors, context, lines);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setString("network", network == null ? "None" : String.valueOf(network.id));
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currenttip, accessor, config);
        currenttip.add(
            "Network: " + accessor.getNBTData()
                .getString("network"));
    }

    @Override
    public void getNeighbours(Collection<BECFactoryElement> neighbours) {
        IGregTechTileEntity base = getBaseMetaTileEntity();

        if (base == null || base.isDead()) return;

        neighbours.addAll(mBECHatches);
    }

    protected List<? extends BECFactoryElement> getRoutedDiscoverySeeds() {
        return mBECHatches;
    }

    @Override
    public List<RoutedNode<NotableBECFactoryElement, BECRouteInfo>> getRoutedNeighbours() {
        List<RoutedNode<NotableBECFactoryElement, BECRouteInfo>> nodes = new ArrayList<>();

        HashSet<BECFactoryElement> visited = new HashSet<>();

        for (BECFactoryElement seed : getRoutedDiscoverySeeds()) {

            Deque<ObjectIntPair<BECFactoryElement>> queue = new ArrayDeque<>();

            queue.add(ObjectIntPair.of(seed, 0));

            while (!queue.isEmpty()) {
                ObjectIntPair<BECFactoryElement> curr = queue.pop();

                if (!visited.add(curr.left())) continue;

                if (curr.left() instanceof NotableBECFactoryElement notable) {
                    if (notable != this) {
                        nodes.add(new RoutedNode<>(notable, new BECRouteInfo(curr.rightInt())));
                    }

                    continue;
                }

                Set<BECFactoryElement> edges = BECFactoryGrid.INSTANCE.edges.get(curr.left());
                int dist = curr.rightInt() + 1;

                for (BECFactoryElement conn : edges) {
                    queue.add(ObjectIntPair.of(conn, dist));
                }
            }
        }

        return nodes;
    }

    @Override
    public BECFactoryNetwork getNetwork() {
        return this.network;
    }

    @Override
    public void setNetwork(BECFactoryNetwork network) {
        this.network = network;
    }

    @Override
    public ConnectionType getConnectionOnSide(ForgeDirection side) {
        return ConnectionType.NONE;
    }

    @Override
    public void onFirstTick_EM(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick_EM(aBaseMetaTileEntity);

        if (GTUtility.isServer()) {
            BECFactoryGrid.INSTANCE.updateElement(this);
        }
    }

    @Override
    public void onRemoval() {
        super.onRemoval();

        if (GTUtility.isServer()) {
            BECFactoryGrid.INSTANCE.removeElement(this);
        }
    }

    public enum BECHatches implements IHatchElement<MTEBECMultiblockBase<?>> {

        Hatch(MTEHatchBEC.class) {

            @Override
            public long count(MTEBECMultiblockBase<?> t) {
                return t.mBECHatches.size();
            }
        };

        private final List<? extends Class<? extends IMetaTileEntity>> mteClasses;

        @SafeVarargs
        BECHatches(Class<? extends IMetaTileEntity>... mteClasses) {
            this.mteClasses = Collections.unmodifiableList(Arrays.asList(mteClasses));
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return mteClasses;
        }

        @Override
        public String getDisplayName() {
            return switch (this) {
                case Hatch -> GTUtility.translate("gt.machine.bec.hatch.bec");
            };
        }

        @Override
        public IGTHatchAdder<? super MTEBECMultiblockBase<?>> adder() {
            return (self, igtme, id) -> {
                IMetaTileEntity imte = igtme.getMetaTileEntity();

                if (imte instanceof MTEHatchBEC hatch) {
                    hatch.updateTexture(id);
                    hatch.updateCraftingIcon(self.getMachineCraftingIcon());
                    self.mBECHatches.add(hatch);
                    hatch.addController(self);
                    return true;
                } else {
                    return false;
                }
            };
        }
    }
}

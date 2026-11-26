package gregtech.api.metatileentity;

import static gregtech.common.render.GTRendererBlock.BLOCK_MAX;
import static gregtech.common.render.GTRendererBlock.BLOCK_MIN;
import static net.minecraftforge.common.util.ForgeDirection.DOWN;
import static net.minecraftforge.common.util.ForgeDirection.EAST;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;
import static net.minecraftforge.common.util.ForgeDirection.UP;
import static net.minecraftforge.common.util.ForgeDirection.WEST;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.ApiStatus;

import com.gtnewhorizon.gtnhlib.api.OutlineShape;
import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.ShapeBlock;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IConnectable;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IColoredTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.ILocalizedMetaPipeEntity;
import gregtech.api.render.ISBRInventoryContext;
import gregtech.api.render.ISBRWorldContext;
import gregtech.api.util.GTUtility;
import gregtech.api.util.WorldSpawnedEventBuilder;
import gregtech.common.blocks.PipeShapeBlock;
import gregtech.common.blocks.PipeShapeItemBlock;
import gregtech.common.covers.Cover;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * Extend this Class to add a new MetaPipe Call the Constructor with the desired ID at the load-phase (not preload and
 * also not postload!) Implement the newMetaEntity-Method to return a new ready instance of your MetaTileEntity
 * <p/>
 * Call the Constructor like the following example inside the Load Phase, to register it. "new MTEFurnace(54,
 * "GT_E_Furnace", "Automatic E-Furnace");"
 */
public abstract class MetaPipeEntity extends CommonMetaTileEntity implements IConnectable {

    // a shape depends only on thickness, covers and connections. Client and server threads both write to the cache
    private static final Map<Long, PipeShape> PIPE_SHAPES = new ConcurrentHashMap<>();

    private volatile ITexture[][] inventoryTextureCache;
    private volatile Map<Material, ITexture[][]> shapeInventoryTextureCache;

    /**
     * This variable tells, which directions the Block is connected to. It is a Bitmask.
     */
    public byte mConnections = 0;

    /**
     * Used by cables to provide delayed reconstruction of neighbors
     */
    protected boolean mCheckConnections = false;
    /**
     * accessibility to this Field is no longer given, see below
     */
    private IGregTechTileEntity mBaseMetaTileEntity;

    /// The MaterialLib shape block hosting this pipe, or null for a pipe that is not a material shape, such as a
    /// data pipe. A shape-scoped instance carries no material of its own: the material is the host block's metadata
    /// (the material's global index), resolved through [#shapeMaterial] and cached per tile entity.
    private final ShapeBlock shapeHost;
    /// The size step within the pipe family for a shape-scoped instance, otherwise -1.
    private final int shapeSizeIndex;
    private Material shapeMaterialCache;
    private Material shapeRenderMaterial;

    /**
     * This registers your Machine at the List. Use only ID's larger than 2048 - the ones lower are reserved by GT. See
     * also the list in the API package - it has a description that contains all the reservations.
     * <p>
     * The constructor can be overloaded as follows: <blockquote>
     *
     * <pre>
     *
     * public MTEBench(int id, String name, String nameRegional) {
     *     super(id, name, nameRegional);
     * }
     * </pre>
     *
     * </blockquote>
     *
     * @param aID the machine ID
     */
    public MetaPipeEntity(int aID, String aBasicName, int aInvSlotCount) {
        this(aID, aBasicName, aInvSlotCount, true);
    }

    public MetaPipeEntity(int aID, String aBasicName, int aInvSlotCount, boolean aAddInfo) {
        this(aID, aBasicName, aInvSlotCount, aAddInfo, null, -1);
    }

    protected MetaPipeEntity(int aID, String aBasicName, int aInvSlotCount, boolean aAddInfo, ShapeBlock shapeHost,
        int shapeSizeIndex) {
        super(aID, aBasicName, aInvSlotCount);
        this.shapeHost = shapeHost;
        this.shapeSizeIndex = shapeSizeIndex;
        setBaseMetaTileEntity(new BaseMetaPipeEntity());
        getBaseMetaTileEntity().setMetaTileID((short) aID);

        if (aAddInfo && GTMod.GT.isClientSide()) {
            addInfo(aID);
        }
    }

    protected final void addInfo(int aID) {
        if (!GTMod.GT.isClientSide()) return;

        ItemStack tStack = new ItemStack(GregTechAPI.sBlockMachines, 1, aID);
        Objects.requireNonNull(tStack.getItem())
            .addInformation(tStack, null, new ArrayList<>(), true);
    }

    /**
     * This is the normal Constructor.
     */
    public MetaPipeEntity(String aName, int aInvSlotCount) {
        this(aName, aInvSlotCount, null, -1);
    }

    protected MetaPipeEntity(String aName, int aInvSlotCount, ShapeBlock shapeHost, int shapeSizeIndex) {
        super(aName, aInvSlotCount);
        this.shapeHost = shapeHost;
        this.shapeSizeIndex = shapeSizeIndex;
    }

    /// Whether a MaterialLib shape block hosts this instance. A shape-scoped instance's item form is the shape
    /// block's item, never a machine-block stack at its id.
    public final boolean isShapeScoped() {
        return shapeHost != null;
    }

    protected final ShapeBlock getShapeHost() {
        return shapeHost;
    }

    protected final int getShapeSizeIndex() {
        return shapeSizeIndex;
    }

    /// The material of a shape-scoped instance: the host block's metadata resolved through
    /// [MaterialLibAPI#getMaterialByIndex], cached until the tile entity rebinds, with the transient
    /// material set by [#withShapeMaterial] as the out-of-world fallback. Null while unresolvable.
    protected final Material shapeMaterial() {
        if (shapeMaterialCache != null) return shapeMaterialCache;
        if (getBaseMetaTileEntity() instanceof BaseMetaPipeEntity pipe && pipe.getWorldObj() != null) {
            Material resolved = MaterialLibAPI.getMaterialByIndex(
                pipe.getWorldObj()
                    .getBlockMetadata(pipe.xCoord, pipe.yCoord, pipe.zCoord));
            if (resolved != null) {
                shapeMaterialCache = resolved;
                onShapeMaterialResolved(resolved);
                return resolved;
            }
        }
        return shapeRenderMaterial;
    }

    /// Called once each time [#shapeMaterial] resolves the host block's material anew, after construction or a
    /// rebind. Material-sized state is set up here.
    protected void onShapeMaterialResolved(Material material) {}

    /// Drops the cached [#shapeMaterial] so the next read resolves from the host block again.
    protected final void clearShapeMaterial() {
        shapeMaterialCache = null;
    }

    /// Runs `body` with [#shapeMaterial] answering `material` wherever the host block cannot be consulted.
    /// Only meaningful on the canonical shape-scoped instances.
    public final <T> T withShapeMaterial(Material material, Supplier<T> body) {
        shapeRenderMaterial = material;
        try {
            return body.get();
        } finally {
            shapeRenderMaterial = null;
        }
    }

    @Override
    public String getLocalNameKey() {
        return "gt.blockmachines." + mName + ".name";
    }

    @Override
    public String getLocalName() {
        if (this instanceof ILocalizedMetaPipeEntity localizedPipe) {
            return localizedPipe.getLocalizedName();
        }
        return StatCollector.translateToLocal("gt.blockmachines." + mName + ".name");
    }

    @Override
    public String getInventoryName() {
        // Pipes don't register a translation key for their name, and cloned instances
        // may lack fields needed for dynamic name construction (e.g. mPrefixKey).
        // Use the prototype from the registry which is always fully initialized.
        IMetaTileEntity prototype = GregTechAPI.METATILEENTITIES[getBaseMetaTileEntity().getMetaTileID()];
        if (prototype != null) {
            return prototype.getLocalName();
        }
        return "";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderInInventory(ISBRInventoryContext ctx) {
        final float tThickness = getThickness();
        final float pipeMin = (BLOCK_MAX - tThickness) / 2.0F;
        final float pipeMax = BLOCK_MAX - pipeMin;
        final RenderBlocks renderBlocks = ctx.getRenderBlocks();
        renderBlocks.setRenderBounds(BLOCK_MIN, pipeMin, pipeMin, BLOCK_MAX, pipeMax, pipeMax);

        final ITexture[][] textures = inventoryTextures();
        final ITexture[] sideTexture = textures[0];
        final ITexture[] endTexture = textures[1];
        ctx.renderNegativeYFacing(sideTexture);
        ctx.renderPositiveYFacing(sideTexture);
        ctx.renderNegativeZFacing(sideTexture);
        ctx.renderPositiveZFacing(sideTexture);
        ctx.renderNegativeXFacing(endTexture);
        ctx.renderPositiveXFacing(endTexture);
        return true;
    }

    /// The side and end textures of this pipe's inventory form. A shape-scoped instance renders every material of
    /// its shape through its single registry instance, so it keeps one set per rendered material.
    @SideOnly(Side.CLIENT)
    private ITexture[][] inventoryTextures() {
        if (shapeHost == null) {
            ITexture[][] textures = inventoryTextureCache;
            if (textures == null) {
                textures = buildInventoryTextures();
                inventoryTextureCache = textures;
            }
            return textures;
        }
        final Material material = shapeMaterial();
        if (material == null) return buildInventoryTextures();
        Map<Material, ITexture[][]> cache = shapeInventoryTextureCache;
        if (cache == null) {
            cache = new ConcurrentHashMap<>();
            shapeInventoryTextureCache = cache;
        }
        ITexture[][] textures = cache.get(material);
        if (textures == null) {
            textures = buildInventoryTextures();
            cache.put(material, textures);
        }
        return textures;
    }

    @SideOnly(Side.CLIENT)
    private ITexture[][] buildInventoryTextures() {
        final IGregTechTileEntity mte = getBaseMetaTileEntity();
        return new ITexture[][] { getTexture(mte, DOWN, (CONNECTED_WEST | CONNECTED_EAST), -1, false, false),
            getTexture(mte, WEST, (CONNECTED_WEST | CONNECTED_EAST), -1, true, false) };
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderInWorld(ISBRWorldContext ctx) {
        BaseMetaPipeEntity rte = (BaseMetaPipeEntity) getBaseMetaTileEntity();
        if (rte == null) return false;

        final float thickness = getThickness();
        if (thickness >= 1) {
            ctx.renderNegativeYFacing(rte.getTextureCovered(DOWN));
            ctx.renderPositiveYFacing(rte.getTextureCovered(UP));
            ctx.renderNegativeZFacing(rte.getTextureCovered(NORTH));
            ctx.renderPositiveZFacing(rte.getTextureCovered(SOUTH));
            ctx.renderNegativeXFacing(rte.getTextureCovered(WEST));
            ctx.renderPositiveXFacing(rte.getTextureCovered(EAST));
            return true;
        }

        final PipeShape shape = getShape(thickness);
        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            final ITexture[] texture = rte.getTextureUncovered(side);
            for (PipeShape.Face face : shape.pipeFaces()) {
                if (face.side() == side) renderFace(ctx, face, texture);
            }
        }
        for (ForgeDirection plate : ForgeDirection.VALID_DIRECTIONS) {
            final List<PipeShape.Face> faces = shape.coverFaces()
                .get(plate);
            if (faces == null) continue;
            final ITexture[] texture = rte.getTexture(plate);
            for (PipeShape.Face face : faces) renderFace(ctx, face, texture);
        }
        return true;
    }

    @SideOnly(Side.CLIENT)
    private static void renderFace(ISBRWorldContext ctx, PipeShape.Face face, ITexture[] texture) {
        final AxisAlignedBB box = face.box();
        ctx.getRenderBlocks()
            .setRenderBounds(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ);
        switch (face.side()) {
            case DOWN -> ctx.renderNegativeYFacing(texture);
            case UP -> ctx.renderPositiveYFacing(texture);
            case NORTH -> ctx.renderNegativeZFacing(texture);
            case SOUTH -> ctx.renderPositiveZFacing(texture);
            case WEST -> ctx.renderNegativeXFacing(texture);
            case EAST -> ctx.renderPositiveXFacing(texture);
            case UNKNOWN -> {}
        }
    }

    /**
     * For Pipe Rendering
     */
    public float getThickness() {
        // If we are holding a soldering iron, minimize the rendered thickness of the pipe.
        if (GTMod.GT.isClientSide() && GTMod.clientProxy()
            .shouldHideThings()) return 0.0625F;
        return getCollisionThickness();
    }

    /**
     * For Bounding Box collision checks The bounding box is unaffected in case a soldering iron is held and the render
     * thickness of the pipe is minimized.
     */
    public abstract float getCollisionThickness();

    /**
     * For Pipe Rendering
     */
    public abstract boolean renderInside(ForgeDirection side);

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        return Textures.BlockIcons.ERROR_RENDERING;
    }

    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection, int connections,
        int colorIndex, boolean active, boolean redstoneLevel) {
        return Textures.BlockIcons.ERROR_RENDERING;
    }

    @Override
    // making this method final allows it to be inlined by the JIT compiler
    public final IGregTechTileEntity getBaseMetaTileEntity() {
        return mBaseMetaTileEntity;
    }

    @Override
    public final void setBaseMetaTileEntity(IGregTechTileEntity aBaseMetaTileEntity) {
        final IGregTechTileEntity oldBase = mBaseMetaTileEntity;
        shapeMaterialCache = null;
        if (mBaseMetaTileEntity != null && aBaseMetaTileEntity == null) {
            mBaseMetaTileEntity.getMetaTileEntity()
                .inValidate();
            mBaseMetaTileEntity.setMetaTileEntity(null);
        }
        mBaseMetaTileEntity = aBaseMetaTileEntity;
        if (mBaseMetaTileEntity != null) {
            mBaseMetaTileEntity.setMetaTileEntity(this);
        }
        if (oldBase != aBaseMetaTileEntity && oldBase instanceof CommonBaseMetaTileEntity oldMeta) {
            oldMeta.refreshMetaTileEntityValidity();
        }
    }

    @Override
    public void receiveClientEvent(byte eventID, byte value) {
        super.receiveClientEvent(eventID, value);
        if (eventID == GregTechTileClientEvents.CHANGE_COMMON_DATA) {
            mConnections = value;
        }
    }

    @Override
    public ItemStack getStackForm(long aAmount) {
        if (shapeHost != null) {
            Material material = shapeMaterial();
            if (material != null) return shapeHost.getStack(material, (int) aAmount);
        }
        return new ItemStack(GregTechAPI.sBlockMachines, (int) aAmount, getBaseMetaTileEntity().getMetaTileID());
    }

    /// [#renderInInventory] with the material the rendered item stack carries.
    @SideOnly(Side.CLIENT)
    public final boolean renderInInventory(ISBRInventoryContext ctx, Material material) {
        return withShapeMaterial(material, () -> renderInInventory(ctx));
    }

    /// Swaps a shape-scoped pipe in place onto the shape and material of `handItem`. The same shape swaps by
    /// rewriting the host block's metadata. A different shape of the same pipe kind replaces the block and respawns
    /// the tile entity carrying the old one's NBT (connections, covers, contents). Outside creative mode, takes one
    /// `handItem` and gives the player the replaced pipe. A held item that is not a shape item of this instance's
    /// own class, or a target that is not shape-scoped, is left untouched.
    protected final void trySwapShape(IGregTechTileEntity base, EntityPlayer player, ItemStack handItem) {
        if (!isShapeScoped() || handItem == null) return;
        if (!(handItem.getItem() instanceof PipeShapeItemBlock shapeItem)) return;
        PipeShapeBlock handShape = shapeItem.getShape();
        IMetaTileEntity prototype = GTUtility.getIndexSafe(GregTechAPI.METATILEENTITIES, handShape.getMteId());
        if (prototype == null || prototype.getClass() != getClass()) return;

        World world = base.getWorld();
        int x = base.getXCoord(), y = base.getYCoord(), z = base.getZCoord();
        int newIndex = handItem.getItemDamage();
        Block currentBlock = world.getBlock(x, y, z);
        int currentIndex = world.getBlockMetadata(x, y, z);
        if (currentBlock == handShape && currentIndex == newIndex) return;

        ItemStack returnedStack = null;
        if (currentBlock instanceof PipeShapeBlock currentShape) {
            Material currentMaterial = MaterialLibAPI.getMaterialByIndex(currentIndex);
            if (currentMaterial != null) returnedStack = currentShape.getStack(currentMaterial, 1);
        }

        if (currentBlock == handShape) {
            world.setBlockMetadataWithNotify(x, y, z, newIndex, 3);
            clearShapeMaterial();
            onShapeSwapped();
            base.markDirty();
            base.issueBlockUpdate();
            base.issueTileUpdate();
            base.issueTextureUpdate();
        } else {
            NBTTagCompound tag = new NBTTagCompound();
            ((BaseMetaPipeEntity) base).writeToNBT(tag);
            // Detach the tile entity first: the old block's breakBlock scatters the pipe's inventory, which the
            // captured NBT already carries.
            world.removeTileEntity(x, y, z);
            world.setBlock(x, y, z, handShape, newIndex, 3);
            if (!(world.getTileEntity(x, y, z) instanceof BaseMetaPipeEntity newBase)) return;
            newBase.setInitialValuesAsNBT(tag, (short) handShape.getMteId());
            if (newBase.getMetaTileEntity() instanceof MetaPipeEntity newPipe) {
                newPipe.onShapeSwapped();
            }
            newBase.markDirty();
            newBase.issueBlockUpdate();
            newBase.issueTileUpdate();
            newBase.issueTextureUpdate();
        }

        if (!player.capabilities.isCreativeMode) {
            if (returnedStack != null) {
                boolean added = false;
                for (int i = 0; i < player.inventory.mainInventory.length; i++) {
                    ItemStack slot = player.inventory.mainInventory[i];
                    if (slot != null && slot.getItem() == returnedStack.getItem()
                        && slot.getItemDamage() == returnedStack.getItemDamage()
                        && slot.stackSize < slot.getMaxStackSize()) {
                        slot.stackSize++;
                        added = true;
                        break;
                    }
                }
                if (!added) {
                    added = player.inventory.addItemStackToInventory(returnedStack);
                }
                if (!added) {
                    player.dropPlayerItemWithRandomChoice(returnedStack, false);
                }
            }
            handItem.stackSize--;
            if (handItem.stackSize <= 0) {
                player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
            }
        }
    }

    /// Called on the pipe occupying the position after [#trySwapShape] swaps it, for material-sized state fixups
    /// (inventory resizing, fluid capacity clamping).
    protected void onShapeSwapped() {}

    @Override
    public void initDefaultModes(NBTTagCompound nbt) {
        super.initDefaultModes(nbt);
        if (getBaseMetaTileEntity() instanceof BaseMetaPipeEntity pipe) {
            pipe.updateConnections();
        }
    }

    public boolean isCoverOnSide(BaseMetaPipeEntity aPipe, EntityLivingBase aEntity) {
        ForgeDirection side = ForgeDirection.UNKNOWN;
        double difference = aEntity.posY - (double) aPipe.yCoord;
        if (difference > 0.6 && difference < 0.99) {
            side = ForgeDirection.UP;
        }
        if (difference < -1.5 && difference > -1.99) {
            side = ForgeDirection.DOWN;
        }
        difference = aEntity.posZ - (double) aPipe.zCoord;
        if (difference < -0.05 && difference > -0.4) {
            side = ForgeDirection.NORTH;
        }
        if (difference > 1.05 && difference < 1.4) {
            side = ForgeDirection.SOUTH;
        }
        difference = aEntity.posX - (double) aPipe.xCoord;
        if (difference < -0.05 && difference > -0.4) {
            side = ForgeDirection.WEST;
        }
        if (difference > 1.05 && difference < 1.4) {
            side = ForgeDirection.EAST;
        }
        boolean tCovered = side != ForgeDirection.UNKNOWN && mBaseMetaTileEntity.hasCoverAtSide(side);
        if (isConnectedAtSide(side)) {
            tCovered = true;
        }
        // GT_FML_LOGGER.info("Cover: "+mBaseMetaTileEntity.getCoverIDAtSide(aSide));
        // toDo: filter cover ids that actually protect against temperature (rubber/plastic maybe?, more like asbestos)
        return tCovered;
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {}

    @Override
    public boolean onWrenchRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer, float aX,
        float aY, float aZ, ItemStack aTool) {
        return false;
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        return false;
    }

    @Override
    public boolean onSolderingToolRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        return false;
    }

    @Override
    public void onExplosion() {
        /* Do nothing */
    }

    @Override
    public boolean isLiquidInput(ForgeDirection side) {
        return false;
    }

    @Override
    public boolean isLiquidOutput(ForgeDirection side) {
        return false;
    }

    public int fill_default(ForgeDirection side, FluidStack aFluid, boolean doFill) {
        return fill(aFluid, doFill);
    }

    @Override
    public int fill(ForgeDirection side, FluidStack aFluid, boolean doFill) {
        return fill_default(side, aFluid, doFill);
    }

    @Override
    public float getExplosionResistance(ForgeDirection side) {
        return 5.0F;
    }

    @Override
    public void markDirty() {
        //
    }

    @Override
    public void onColorChangeServer(byte aColor) {
        setCheckConnections();
    }

    public void setCheckConnections() {
        IGregTechTileEntity base = getBaseMetaTileEntity();
        if (base.isServerSide()) {
            if (base instanceof BaseMetaPipeEntity pipe && pipe.isNonTickingCable()) {
                pipe.scheduleConnectionCheck();
            } else if (base.isTickDisabled() || !deferCheckConnection()) {
                checkConnections();
            } else {
                mCheckConnections = true;
            }
        }
    }

    @ApiStatus.OverrideOnly
    protected boolean deferCheckConnection() {
        return true;
    }

    public long injectEnergyUnits(ForgeDirection side, long aVoltage, long aAmperage) {
        return 0;
    }

    @Override
    public void doExplosion(long aExplosionPower) {
        float tStrength = GTValues.getExplosionPowerForVoltage(aExplosionPower);
        int tX = getBaseMetaTileEntity().getXCoord(), tY = getBaseMetaTileEntity().getYCoord(),
            tZ = getBaseMetaTileEntity().getZCoord();
        World tWorld = getBaseMetaTileEntity().getWorld();
        tWorld.setBlock(tX, tY, tZ, Blocks.air);
        if (GregTechAPI.sMachineExplosions) {
            new WorldSpawnedEventBuilder.ExplosionEffectEventBuilder().setStrength(tStrength)
                .setSmoking(true)
                .setPosition(tX + 0.5, tY + 0.5, tZ + 0.5)
                .setWorld(tWorld)
                .run();
        }
    }

    @Override
    public int getLightOpacity() {
        return 0;
    }

    protected boolean connectableColor(TileEntity tTileEntity) {
        // Determine if two entities are connectable based on their colorization:
        // Uncolored can connect to anything
        // If both are colored they must be the same color to connect.
        if (tTileEntity instanceof IColoredTileEntity) {
            if (getBaseMetaTileEntity().getColorization() >= 0) {
                final byte tColor = ((IColoredTileEntity) tTileEntity).getColorization();
                return tColor < 0 || tColor == getBaseMetaTileEntity().getColorization();
            }
        }

        return true;
    }

    @Override
    public int connect(ForgeDirection side) {
        if (side == ForgeDirection.UNKNOWN) return 0;

        final ForgeDirection oppositeSide = side.getOpposite();
        final IGregTechTileEntity baseMetaTile = getBaseMetaTileEntity();
        if (baseMetaTile == null) return 0;

        final Cover cover = baseMetaTile.getCoverAtSide(side);

        final boolean alwaysLookConnected = cover.alwaysLookConnected();
        final boolean letsIn = letsIn(cover);
        final boolean letsOut = letsOut(cover);

        // Careful - tTileEntity might be null, and that's ok -- so handle it
        final TileEntity tTileEntity = baseMetaTile.getTileEntityAtSide(side);
        if (!connectableColor(tTileEntity)) return 0;

        if ((alwaysLookConnected || letsIn || letsOut)) {
            // Are we trying to connect to a pipe? let's do it!
            final IMetaTileEntity tPipe = tTileEntity instanceof IGregTechTileEntity
                ? ((IGregTechTileEntity) tTileEntity).getMetaTileEntity()
                : null;
            if (getClass().isInstance(tPipe) || (tPipe != null && tPipe.getClass()
                .isInstance(this))) {
                connectAtSide(side);
                if (!((IConnectable) tPipe).isConnectedAtSide(oppositeSide)) {
                    // Make sure pipes all get together -- connect back to us if we're connecting to a pipe
                    ((IConnectable) tPipe).connect(oppositeSide);
                }
                return 1;
            } else if ((getGT6StyleConnection() && baseMetaTile.getAirAtSide(side)) || canConnect(side, tTileEntity)) {
                // Allow open connections to Air, if the GT6 style pipe/cables are enabled, so that it'll connect to
                // the next block placed down next to it
                connectAtSide(side);
                return 1;
            }
            if (!baseMetaTile.getWorld()
                .getChunkProvider()
                .chunkExists(baseMetaTile.getOffsetX(side, 1) >> 4, baseMetaTile.getOffsetZ(side, 1) >> 4)) {
                // Target chunk unloaded
                return -1;
            }
        }
        return 0;
    }

    public void checkConnections() {
        mCheckConnections = false;
        // Verify connections around us. If GT6 style cables are not enabled then revert to old behavior and try
        // connecting to everything around us
        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            if ((!getGT6StyleConnection() || isConnectedAtSide(side)) && connect(side) == 0) {
                disconnect(side);
            }
        }
    }

    private void connectAtSide(ForgeDirection side) {
        mConnections |= side.flag;
        if (getBaseMetaTileEntity() instanceof BaseMetaPipeEntity pipe && pipe.isClientSide()) {
            pipe.mConnections = mConnections;
        }
    }

    @Override
    public void disconnect(ForgeDirection side) {
        if (side == ForgeDirection.UNKNOWN) return;
        mConnections &= ~side.flag;
        final ForgeDirection oppositeSide = side.getOpposite();
        IGregTechTileEntity tTileEntity = getBaseMetaTileEntity().getIGregTechTileEntityAtSide(side);
        IMetaTileEntity tPipe = tTileEntity == null ? null : tTileEntity.getMetaTileEntity();
        if ((this.getClass()
            .isInstance(tPipe)
            || (tPipe != null && tPipe.getClass()
                .isInstance(this)))
            && ((IConnectable) tPipe).isConnectedAtSide(oppositeSide)) {
            ((IConnectable) tPipe).disconnect(oppositeSide);
        }
    }

    @Override
    public boolean isConnectedAtSide(ForgeDirection sideDirection) {
        return (mConnections & sideDirection.flag) != 0;
    }

    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB inputAABB,
        List<AxisAlignedBB> outputAABB, Entity collider) {
        for (AxisAlignedBB box : getShape(getCollisionThickness()).boxes()) {
            AxisAlignedBB worldBox = box.getOffsetBoundingBox(x, y, z);
            if (inputAABB.intersectsWith(worldBox)) outputAABB.add(worldBox);
        }
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return getShape(getCollisionThickness()).bounds()
            .getOffsetBoundingBox(x, y, z);
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        if (selectionShouldBeFullBlock()) return AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1);
        return getCollisionBoundingBoxFromPool(world, x, y, z);
    }

    @Override
    public OutlineShape getOutlineShape() {
        return selectionShouldBeFullBlock() ? OutlineShape.EMPTY : getShape(getCollisionThickness()).outline();
    }

    private boolean selectionShouldBeFullBlock() {
        // While holding tool, make it full block. `GTMod.GT.isClientSide()` is true on the integrated server too
        return getBaseMetaTileEntity().isClientSide() && GTMod.clientProxy()
            .forceFullBlockBB();
    }

    public boolean hasCoverPlates() {
        return getCollisionThickness() < 1;
    }

    /// Return the covered side whose plate contains the hit on face `side`, or [ForgeDirection#UNKNOWN].
    public ForgeDirection getCoverSideAt(ForgeDirection side, float x, float y, float z) {
        final float depth = PipeShape.coverDepth(getCollisionThickness());
        for (ForgeDirection plate : ForgeDirection.VALID_DIRECTIONS) {
            if (!getBaseMetaTileEntity().hasCoverAtSide(plate)) continue;
            final float along = plate.offsetX != 0 ? x : plate.offsetY != 0 ? y : z;
            // C08PacketPlayerBlockPlacement rounds hits down to 1/16. A hit at the depth is on a low plate only on the
            // inner face
            final boolean onPlate = plate.offsetX + plate.offsetY + plate.offsetZ > 0 ? along >= 1 - depth
                : along < depth || (along == depth && side == plate.getOpposite());
            if (onPlate) return plate;
        }
        return ForgeDirection.UNKNOWN;
    }

    private PipeShape getShape(float thickness) {
        final BaseMetaPipeEntity baseTE = (BaseMetaPipeEntity) getBaseMetaTileEntity();
        int covers = 0;
        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            if (baseTE.hasCoverAtSide(side)) covers |= side.flag;
        }
        // this.mConnections isn't synced, but BaseMetaPipeEntity.mConnections is for some reason
        final int connections = baseTE.mConnections & CONNECTED_ALL;
        final long key = (long) Float.floatToIntBits(thickness) << 12 | covers << 6 | connections;
        final int coverFlags = covers;
        return PIPE_SHAPES.computeIfAbsent(key, k -> PipeShape.build(thickness, connections, coverFlags));
    }

    /// `boxes` are in block coordinates: the core, then for a thin pipe an arm per connection and a plate per cover.
    private record PipeShape(List<AxisAlignedBB> boxes, AxisAlignedBB bounds, OutlineShape outline,
        List<Face> pipeFaces, Map<ForgeDirection, List<Face>> coverFaces) {

        private PipeShape {
            boxes = List.copyOf(boxes);
            pipeFaces = List.copyOf(pipeFaces);
            coverFaces = Map.copyOf(coverFaces);
        }

        private static PipeShape build(float thickness, int connections, int covers) {
            final float space = (1f - thickness) / 2;
            final AxisAlignedBB core = AxisAlignedBB
                .getBoundingBox(space, space, space, 1f - space, 1f - space, 1f - space);
            final List<AxisAlignedBB> boxes = new ArrayList<>(List.of(core));
            final List<Face> pipeFaces = new ArrayList<>();
            final Map<ForgeDirection, List<Face>> coverFaces = new EnumMap<>(ForgeDirection.class);
            if (space > 0) {
                for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                    if ((connections & side.flag) == 0) {
                        pipeFaces.add(new Face(core, side));
                        continue;
                    }
                    final AxisAlignedBB arm = faceBox(side, space, space);
                    boxes.add(arm);
                    // the arm's inner end is against the core and hidden
                    for (ForgeDirection face : ForgeDirection.VALID_DIRECTIONS) {
                        if (face != side.getOpposite()) pipeFaces.add(new Face(arm, face));
                    }
                }

                final float depth = coverDepth(thickness);
                for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                    if ((covers & side.flag) == 0) continue;
                    final AxisAlignedBB plate = plateBox(side, depth, covers);
                    boxes.add(plate);
                    final List<Face> faces = new ArrayList<>();
                    // apart from the inner face, faces inside the block touch an earlier plate and don't show
                    for (ForgeDirection face : ForgeDirection.VALID_DIRECTIONS) {
                        if (face == side && (connections & side.flag) != 0) {
                            for (AxisAlignedBB panel : holePanels(plate, side, space)) faces.add(new Face(panel, face));
                        } else if (face == side.getOpposite() || isOnBlockFace(plate, face)) {
                            faces.add(new Face(plate, face));
                        }
                    }
                    coverFaces.put(side, List.copyOf(faces));
                }
            }

            final AxisAlignedBB bounds = core.copy();
            for (AxisAlignedBB box : boxes) {
                bounds.minX = Math.min(bounds.minX, box.minX);
                bounds.minY = Math.min(bounds.minY, box.minY);
                bounds.minZ = Math.min(bounds.minZ, box.minZ);
                bounds.maxX = Math.max(bounds.maxX, box.maxX);
                bounds.maxY = Math.max(bounds.maxY, box.maxY);
                bounds.maxZ = Math.max(bounds.maxZ, box.maxZ);
            }
            return new PipeShape(boxes, bounds, OutlineShape.of(boxes), pipeFaces, coverFaces);
        }

        /// Return a box `depth` deep on the `side` face and `inset` from the block's other four faces.
        private static AxisAlignedBB faceBox(ForgeDirection side, float depth, float inset) {
            return AxisAlignedBB.getBoundingBox(
                side.offsetX == 0 ? inset : side.offsetX < 0 ? 0 : 1f - depth,
                side.offsetY == 0 ? inset : side.offsetY < 0 ? 0 : 1f - depth,
                side.offsetZ == 0 ? inset : side.offsetZ < 0 ? 0 : 1f - depth,
                side.offsetX == 0 ? 1f - inset : side.offsetX < 0 ? depth : 1,
                side.offsetY == 0 ? 1f - inset : side.offsetY < 0 ? depth : 1,
                side.offsetZ == 0 ? 1f - inset : side.offsetZ < 0 ? depth : 1);
        }

        private static float coverDepth(float thickness) {
            return Float.min(1f / 8, (1f - thickness) / 2);
        }

        /// Return the part of the `side` cover plate that plates on earlier [ForgeDirection] sides don't overlap.
        private static AxisAlignedBB plateBox(ForgeDirection side, float depth, int covers) {
            AxisAlignedBB plate = faceBox(side, depth, 0);
            for (ForgeDirection earlier : ForgeDirection.VALID_DIRECTIONS) {
                if (earlier == side) break;
                if ((covers & earlier.flag) == 0) continue;
                final float innerFace = earlier.offsetX + earlier.offsetY + earlier.offsetZ > 0 ? 1 - depth : depth;
                plate = clip(plate, earlier.getOpposite(), innerFace);
            }
            return plate;
        }

        /// Return the part of `box` that lies toward `side` from `coordinate` on `side`'s axis.
        private static AxisAlignedBB clip(AxisAlignedBB box, ForgeDirection side, double coordinate) {
            final AxisAlignedBB part = box.copy();
            switch (side) {
                case DOWN -> part.maxY = Math.min(part.maxY, coordinate);
                case UP -> part.minY = Math.max(part.minY, coordinate);
                case NORTH -> part.maxZ = Math.min(part.maxZ, coordinate);
                case SOUTH -> part.minZ = Math.max(part.minZ, coordinate);
                case WEST -> part.maxX = Math.min(part.maxX, coordinate);
                case EAST -> part.minX = Math.max(part.minX, coordinate);
                case UNKNOWN -> {}
            }
            return part;
        }

        /// Return the `side` face of `plate` as four panels around the hole of an arm `inset` from the block's edges.
        private static List<AxisAlignedBB> holePanels(AxisAlignedBB plate, ForgeDirection side, float inset) {
            final List<ForgeDirection> across = new ArrayList<>();
            for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
                if (direction != side && direction != side.getOpposite()) across.add(direction);
            }
            // `across` has two pairs of opposite sides, negative side first. The panels beyond the hole toward the
            // first pair span the face, and the panels toward the second pair span the hole.
            final AxisAlignedBB band = clip(clip(plate, across.get(1), inset), across.get(0), 1 - inset);
            return List.of(
                clip(plate, across.get(0), inset),
                clip(plate, across.get(1), 1 - inset),
                clip(band, across.get(2), inset),
                clip(band, across.get(3), 1 - inset));
        }

        private static boolean isOnBlockFace(AxisAlignedBB box, ForgeDirection face) {
            return switch (face) {
                case DOWN -> box.minY == 0;
                case UP -> box.maxY == 1;
                case NORTH -> box.minZ == 0;
                case SOUTH -> box.maxZ == 1;
                case WEST -> box.minX == 0;
                case EAST -> box.maxX == 1;
                case UNKNOWN -> false;
            };
        }

        private record Face(AxisAlignedBB box, ForgeDirection side) {}
    }

    public boolean letsIn(Cover cover) {
        return false;
    }

    public boolean letsOut(Cover cover) {
        return false;
    }

    public boolean canConnect(ForgeDirection side, TileEntity tTileEntity) {
        return false;
    }

    public boolean getGT6StyleConnection() {
        return false;
    }

    // in case any factory network wants to update in the case this happened.
    protected void onCoverChangedServer() {

    }

    @Override
    public boolean shouldJoinIc2Enet() {
        return false;
    }

    @Override
    public boolean isMachineBlockUpdateRecursive() {
        return false;
    }

    public void reloadLocks() {}

    @Override
    public int getGUIColorization() {
        Dyes dye = Dyes.dyeWhite;
        if (GregTechAPI.sColoredGUI) {
            if (GregTechAPI.sMachineMetalGUI) {
                dye = Dyes.GUI_METAL;
            } else if (getBaseMetaTileEntity() != null) {
                dye = Dyes.getOrDefault(getBaseMetaTileEntity().getColorization(), Dyes.GUI_METAL);
            }
        }
        return dye.toInt();
    }
}

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
import java.util.List;
import java.util.Objects;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

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
import gregtech.api.render.ISBRInventoryContext;
import gregtech.api.render.ISBRWorldContext;
import gregtech.api.util.WorldSpawnedEventBuilder;
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

    /**
     * This variable tells, which directions the Block is connected to. It is a Bitmask.
     */
    public byte mConnections = 0;

    protected boolean mCheckConnections = false;
    /**
     * accessibility to this Field is no longer given, see below
     */
    private IGregTechTileEntity mBaseMetaTileEntity;

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
        super(aID, aBasicName, aInvSlotCount);
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
        super(aName, aInvSlotCount);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderInInventory(ISBRInventoryContext ctx) {
        final float tThickness = getThickness();
        final float pipeMin = (BLOCK_MAX - tThickness) / 2.0F;
        final float pipeMax = BLOCK_MAX - pipeMin;
        final RenderBlocks renderBlocks = ctx.getRenderBlocks();
        renderBlocks.setRenderBounds(BLOCK_MIN, pipeMin, pipeMin, BLOCK_MAX, pipeMax, pipeMax);

        final IGregTechTileEntity mte = getBaseMetaTileEntity();
        final ITexture[] sideTexture = getTexture(mte, DOWN, (CONNECTED_WEST | CONNECTED_EAST), -1, false, false);
        final ITexture[] endTexture = getTexture(mte, WEST, (CONNECTED_WEST | CONNECTED_EAST), -1, true, false);
        ctx.renderNegativeYFacing(sideTexture);
        ctx.renderPositiveYFacing(sideTexture);
        ctx.renderNegativeZFacing(sideTexture);
        ctx.renderPositiveZFacing(sideTexture);
        ctx.renderNegativeXFacing(endTexture);
        ctx.renderPositiveXFacing(endTexture);
        return true;
    }

    @SuppressWarnings("OverlyComplexMethod") // Naturally complex logic
    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderInWorld(ISBRWorldContext ctx) {
        BaseMetaPipeEntity rte = (BaseMetaPipeEntity) getBaseMetaTileEntity();
        if (rte == null) return false;

        final byte aConnections = rte.getConnections();
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
        // Range of block occupied by pipe
        final float pipeMin = (BLOCK_MAX - thickness) / 2.0F;
        final float pipeMax = BLOCK_MAX - pipeMin;

        final ITexture[] textureUp = rte.getTextureUncovered(UP);
        final ITexture[] textureDown = rte.getTextureUncovered(DOWN);
        final ITexture[] textureNorth = rte.getTextureUncovered(NORTH);
        final ITexture[] textureSouth = rte.getTextureUncovered(SOUTH);
        final ITexture[] textureWest = rte.getTextureUncovered(WEST);
        final ITexture[] textureEast = rte.getTextureUncovered(EAST);

        final RenderBlocks renderBlocks = ctx.getRenderBlocks();
        switch (aConnections) {
            case NO_CONNECTION -> {
                renderBlocks.setRenderBounds(pipeMin, pipeMin, pipeMin, pipeMax, pipeMax, pipeMax);

                ctx.renderNegativeYFacing(textureDown);
                ctx.renderPositiveYFacing(textureUp);
                ctx.renderNegativeZFacing(textureNorth);
                ctx.renderPositiveZFacing(textureSouth);
                ctx.renderNegativeXFacing(textureWest);
                ctx.renderPositiveXFacing(textureEast);
            }
            case CONNECTED_EAST | CONNECTED_WEST -> {
                // EAST - WEST Pipe Sides
                renderBlocks.setRenderBounds(BLOCK_MIN, pipeMin, pipeMin, BLOCK_MAX, pipeMax, pipeMax);

                ctx.renderNegativeYFacing(textureDown);
                ctx.renderPositiveYFacing(textureUp);
                ctx.renderNegativeZFacing(textureNorth);
                ctx.renderPositiveZFacing(textureSouth);

                // EAST - WEST Pipe Ends
                ctx.renderNegativeXFacing(textureWest);
                ctx.renderPositiveXFacing(textureEast);
            }
            case CONNECTED_DOWN | CONNECTED_UP -> {
                // UP - DOWN Pipe Sides
                renderBlocks.setRenderBounds(pipeMin, BLOCK_MIN, pipeMin, pipeMax, BLOCK_MAX, pipeMax);

                ctx.renderNegativeZFacing(textureNorth);
                ctx.renderPositiveZFacing(textureSouth);
                ctx.renderNegativeXFacing(textureWest);
                ctx.renderPositiveXFacing(textureEast);

                // UP - DOWN Pipe Ends
                ctx.renderNegativeYFacing(textureDown);
                ctx.renderPositiveYFacing(textureUp);
            }
            case CONNECTED_NORTH | CONNECTED_SOUTH -> {
                // NORTH - SOUTH Pipe Sides
                renderBlocks.setRenderBounds(pipeMin, pipeMin, BLOCK_MIN, pipeMax, pipeMax, BLOCK_MAX);

                ctx.renderNegativeYFacing(textureDown);
                ctx.renderPositiveYFacing(textureUp);
                ctx.renderNegativeXFacing(textureWest);
                ctx.renderPositiveXFacing(textureEast);

                // NORTH - SOUTH Pipe Ends
                ctx.renderNegativeZFacing(textureNorth);
                ctx.renderPositiveZFacing(textureSouth);
            }
            default -> {
                if ((aConnections & CONNECTED_WEST) == 0) {
                    renderBlocks.setRenderBounds(pipeMin, pipeMin, pipeMin, pipeMax, pipeMax, pipeMax);

                } else {
                    renderBlocks.setRenderBounds(BLOCK_MIN, pipeMin, pipeMin, pipeMin, pipeMax, pipeMax);

                    ctx.renderNegativeYFacing(textureDown);
                    ctx.renderPositiveYFacing(textureUp);
                    ctx.renderNegativeZFacing(textureNorth);
                    ctx.renderPositiveZFacing(textureSouth);
                }
                ctx.renderNegativeXFacing(textureWest);
                if ((aConnections & CONNECTED_EAST) == 0) {
                    renderBlocks.setRenderBounds(pipeMin, pipeMin, pipeMin, pipeMax, pipeMax, pipeMax);

                } else {
                    renderBlocks.setRenderBounds(pipeMax, pipeMin, pipeMin, BLOCK_MAX, pipeMax, pipeMax);

                    ctx.renderNegativeYFacing(textureDown);
                    ctx.renderPositiveYFacing(textureUp);
                    ctx.renderNegativeZFacing(textureNorth);
                    ctx.renderPositiveZFacing(textureSouth);
                }
                ctx.renderPositiveXFacing(textureEast);
                if ((aConnections & CONNECTED_DOWN) == 0) {
                    renderBlocks.setRenderBounds(pipeMin, pipeMin, pipeMin, pipeMax, pipeMax, pipeMax);

                } else {
                    renderBlocks.setRenderBounds(pipeMin, BLOCK_MIN, pipeMin, pipeMax, pipeMin, pipeMax);

                    ctx.renderNegativeZFacing(textureNorth);
                    ctx.renderPositiveZFacing(textureSouth);
                    ctx.renderNegativeXFacing(textureWest);
                    ctx.renderPositiveXFacing(textureEast);
                }
                ctx.renderNegativeYFacing(textureDown);
                if ((aConnections & CONNECTED_UP) == 0) {
                    renderBlocks.setRenderBounds(pipeMin, pipeMin, pipeMin, pipeMax, pipeMax, pipeMax);

                } else {
                    renderBlocks.setRenderBounds(pipeMin, pipeMax, pipeMin, pipeMax, BLOCK_MAX, pipeMax);

                    ctx.renderNegativeZFacing(textureNorth);
                    ctx.renderPositiveZFacing(textureSouth);
                    ctx.renderNegativeXFacing(textureWest);
                    ctx.renderPositiveXFacing(textureEast);
                }
                ctx.renderPositiveYFacing(textureUp);
                if ((aConnections & CONNECTED_NORTH) == 0) {
                    renderBlocks.setRenderBounds(pipeMin, pipeMin, pipeMin, pipeMax, pipeMax, pipeMax);

                } else {
                    renderBlocks.setRenderBounds(pipeMin, pipeMin, BLOCK_MIN, pipeMax, pipeMax, pipeMin);

                    ctx.renderNegativeYFacing(textureDown);
                    ctx.renderPositiveYFacing(textureUp);
                    ctx.renderNegativeXFacing(textureWest);
                    ctx.renderPositiveXFacing(textureEast);
                }
                ctx.renderNegativeZFacing(textureNorth);
                if ((aConnections & CONNECTED_SOUTH) == 0) {
                    renderBlocks.setRenderBounds(pipeMin, pipeMin, pipeMin, pipeMax, pipeMax, pipeMax);

                } else {
                    renderBlocks.setRenderBounds(pipeMin, pipeMin, pipeMax, pipeMax, pipeMax, BLOCK_MAX);

                    ctx.renderNegativeYFacing(textureDown);
                    ctx.renderPositiveYFacing(textureUp);
                    ctx.renderNegativeXFacing(textureWest);
                    ctx.renderPositiveXFacing(textureEast);
                }
                ctx.renderPositiveZFacing(textureSouth);
            }
        }
        final boolean hasCoverDown = rte.hasCoverAtSide(DOWN);
        final boolean hasCoverUp = rte.hasCoverAtSide(UP);
        final boolean hasCoverNorth = rte.hasCoverAtSide(NORTH);
        final boolean hasCoverSouth = rte.hasCoverAtSide(SOUTH);
        final boolean hasCoverWest = rte.hasCoverAtSide(WEST);
        final boolean hasCoverEast = rte.hasCoverAtSide(EAST);

        final var coverThickness = Float.min(BLOCK_MAX / 8.0F, (BLOCK_MAX - thickness) / 2.0F);
        final float coverInnerMin = BLOCK_MIN + coverThickness;
        final float coverInnerMax = BLOCK_MAX - coverThickness;

        if (hasCoverDown) {
            final ITexture[] coverTexture = rte.getTexture(DOWN);
            renderBlocks.setRenderBounds(BLOCK_MIN, BLOCK_MIN, BLOCK_MIN, BLOCK_MAX, coverInnerMin, BLOCK_MAX);
            if (!hasCoverNorth) ctx.renderNegativeZFacing(coverTexture);
            if (!hasCoverSouth) ctx.renderPositiveZFacing(coverTexture);
            if (!hasCoverWest) ctx.renderNegativeXFacing(coverTexture);
            if (!hasCoverEast) ctx.renderPositiveXFacing(coverTexture);
            ctx.renderPositiveYFacing(coverTexture);
            if ((aConnections & CONNECTED_DOWN) != 0) {
                // Split outer face to leave hole for pipe
                // Lower panel
                renderBlocks.setRenderBounds(BLOCK_MIN, BLOCK_MIN, BLOCK_MIN, BLOCK_MAX, BLOCK_MIN, pipeMin);
                ctx.renderNegativeYFacing(coverTexture);
                // Upper panel
                renderBlocks.setRenderBounds(BLOCK_MIN, BLOCK_MIN, pipeMax, BLOCK_MAX, BLOCK_MIN, BLOCK_MAX);
                ctx.renderNegativeYFacing(coverTexture);
                // Middle left panel
                renderBlocks.setRenderBounds(BLOCK_MIN, BLOCK_MIN, pipeMin, pipeMin, BLOCK_MIN, pipeMax);
                ctx.renderNegativeYFacing(coverTexture);
                // Middle right panel
                renderBlocks.setRenderBounds(pipeMax, BLOCK_MIN, pipeMin, BLOCK_MAX, BLOCK_MIN, pipeMax);
            }
            ctx.renderNegativeYFacing(coverTexture);
        }

        if (hasCoverUp) {
            final ITexture[] coverTexture = rte.getTexture(UP);
            renderBlocks.setRenderBounds(BLOCK_MIN, coverInnerMax, BLOCK_MIN, BLOCK_MAX, BLOCK_MAX, BLOCK_MAX);
            if (!hasCoverNorth) ctx.renderNegativeZFacing(coverTexture);
            if (!hasCoverSouth) ctx.renderPositiveZFacing(coverTexture);
            if (!hasCoverWest) ctx.renderNegativeXFacing(coverTexture);
            if (!hasCoverEast) ctx.renderPositiveXFacing(coverTexture);
            ctx.renderNegativeYFacing(coverTexture);
            if ((aConnections & CONNECTED_UP) != 0) {
                // Split outer face to leave hole for pipe
                // Lower panel
                renderBlocks.setRenderBounds(BLOCK_MIN, BLOCK_MAX, BLOCK_MIN, BLOCK_MAX, BLOCK_MAX, pipeMin);
                ctx.renderPositiveYFacing(coverTexture);
                // Upper panel
                renderBlocks.setRenderBounds(BLOCK_MIN, BLOCK_MAX, pipeMax, BLOCK_MAX, BLOCK_MAX, BLOCK_MAX);
                ctx.renderPositiveYFacing(coverTexture);
                // Middle left panel
                renderBlocks.setRenderBounds(BLOCK_MIN, BLOCK_MAX, pipeMin, pipeMin, BLOCK_MAX, pipeMax);
                ctx.renderPositiveYFacing(coverTexture);
                // Middle right panel
                renderBlocks.setRenderBounds(pipeMax, BLOCK_MAX, pipeMin, BLOCK_MAX, BLOCK_MAX, pipeMax);
            }
            ctx.renderPositiveYFacing(coverTexture);
        }

        if (hasCoverNorth) {
            final ITexture[] coverTexture = rte.getTexture(NORTH);
            renderBlocks.setRenderBounds(BLOCK_MIN, BLOCK_MIN, BLOCK_MIN, BLOCK_MAX, BLOCK_MAX, coverInnerMin);
            if (!hasCoverDown) ctx.renderNegativeYFacing(coverTexture);
            if (!hasCoverUp) ctx.renderPositiveYFacing(coverTexture);
            if (!hasCoverWest) ctx.renderNegativeXFacing(coverTexture);
            if (!hasCoverEast) ctx.renderPositiveXFacing(coverTexture);
            ctx.renderPositiveZFacing(coverTexture);
            if ((aConnections & CONNECTED_NORTH) != 0) {
                // Split outer face to leave hole for pipe
                // Lower panel
                renderBlocks.setRenderBounds(BLOCK_MIN, BLOCK_MIN, BLOCK_MIN, BLOCK_MAX, pipeMin, BLOCK_MIN);
                ctx.renderNegativeZFacing(coverTexture);
                // Upper panel
                renderBlocks.setRenderBounds(BLOCK_MIN, pipeMax, BLOCK_MIN, BLOCK_MAX, BLOCK_MAX, BLOCK_MIN);
                ctx.renderNegativeZFacing(coverTexture);
                // Middle left panel
                renderBlocks.setRenderBounds(BLOCK_MIN, pipeMin, BLOCK_MIN, pipeMin, pipeMax, BLOCK_MIN);
                ctx.renderNegativeZFacing(coverTexture);
                // Middle right panel
                renderBlocks.setRenderBounds(pipeMax, pipeMin, BLOCK_MIN, BLOCK_MAX, pipeMax, BLOCK_MIN);
            }
            ctx.renderNegativeZFacing(coverTexture);
        }

        if (hasCoverSouth) {
            final ITexture[] coverTexture = rte.getTexture(SOUTH);
            renderBlocks.setRenderBounds(BLOCK_MIN, BLOCK_MIN, coverInnerMax, BLOCK_MAX, BLOCK_MAX, BLOCK_MAX);
            if (!hasCoverDown) ctx.renderNegativeYFacing(coverTexture);
            if (!hasCoverUp) ctx.renderPositiveYFacing(coverTexture);
            if (!hasCoverWest) ctx.renderNegativeXFacing(coverTexture);
            if (!hasCoverEast) ctx.renderPositiveXFacing(coverTexture);
            ctx.renderNegativeZFacing(coverTexture);
            if ((aConnections & CONNECTED_SOUTH) != 0) {
                // Split outer face to leave hole for pipe
                // Lower panel
                renderBlocks.setRenderBounds(BLOCK_MIN, BLOCK_MIN, BLOCK_MAX, BLOCK_MAX, pipeMin, BLOCK_MAX);
                ctx.renderPositiveZFacing(coverTexture);
                // Upper panel
                renderBlocks.setRenderBounds(BLOCK_MIN, pipeMax, BLOCK_MAX, BLOCK_MAX, BLOCK_MAX, BLOCK_MAX);
                ctx.renderPositiveZFacing(coverTexture);
                // Middle left panel
                renderBlocks.setRenderBounds(BLOCK_MIN, pipeMin, BLOCK_MAX, pipeMin, pipeMax, BLOCK_MAX);
                ctx.renderPositiveZFacing(coverTexture);
                // Middle right panel
                renderBlocks.setRenderBounds(pipeMax, pipeMin, BLOCK_MAX, BLOCK_MAX, pipeMax, BLOCK_MAX);
            }
            ctx.renderPositiveZFacing(coverTexture);
        }

        if (hasCoverWest) {
            final ITexture[] coverTexture = rte.getTexture(WEST);
            renderBlocks.setRenderBounds(BLOCK_MIN, BLOCK_MIN, BLOCK_MIN, coverInnerMin, BLOCK_MAX, BLOCK_MAX);
            if (!hasCoverDown) ctx.renderNegativeYFacing(coverTexture);
            if (!hasCoverUp) ctx.renderPositiveYFacing(coverTexture);
            if (!hasCoverNorth) ctx.renderNegativeZFacing(coverTexture);
            if (!hasCoverSouth) ctx.renderPositiveZFacing(coverTexture);
            ctx.renderPositiveXFacing(coverTexture);
            if ((aConnections & CONNECTED_WEST) != 0) {
                // Split outer face to leave hole for pipe
                // Lower panel
                renderBlocks.setRenderBounds(BLOCK_MIN, BLOCK_MIN, BLOCK_MIN, BLOCK_MIN, pipeMin, BLOCK_MAX);
                ctx.renderNegativeXFacing(coverTexture);
                // Upper panel
                renderBlocks.setRenderBounds(BLOCK_MIN, pipeMax, BLOCK_MIN, BLOCK_MIN, BLOCK_MAX, BLOCK_MAX);
                ctx.renderNegativeXFacing(coverTexture);
                // Middle left panel
                renderBlocks.setRenderBounds(BLOCK_MIN, pipeMin, BLOCK_MIN, BLOCK_MIN, pipeMax, pipeMin);
                ctx.renderNegativeXFacing(coverTexture);
                // Middle right panel
                renderBlocks.setRenderBounds(BLOCK_MIN, pipeMin, pipeMax, BLOCK_MIN, pipeMax, BLOCK_MAX);
            }
            ctx.renderNegativeXFacing(coverTexture);
        }

        if (hasCoverEast) {
            final ITexture[] coverTexture = rte.getTexture(EAST);
            renderBlocks.setRenderBounds(coverInnerMax, BLOCK_MIN, BLOCK_MIN, BLOCK_MAX, BLOCK_MAX, BLOCK_MAX);
            if (!hasCoverDown) ctx.renderNegativeYFacing(coverTexture);
            if (!hasCoverUp) ctx.renderPositiveYFacing(coverTexture);
            if (!hasCoverNorth) ctx.renderNegativeZFacing(coverTexture);
            if (!hasCoverSouth) ctx.renderPositiveZFacing(coverTexture);
            ctx.renderNegativeXFacing(coverTexture);

            if ((aConnections & CONNECTED_EAST) != 0) {
                // Split outer face to leave hole for pipe
                // Lower panel
                renderBlocks.setRenderBounds(BLOCK_MAX, BLOCK_MIN, BLOCK_MIN, BLOCK_MAX, pipeMin, BLOCK_MAX);
                ctx.renderPositiveXFacing(coverTexture);
                // Upper panel
                renderBlocks.setRenderBounds(BLOCK_MAX, pipeMax, BLOCK_MIN, BLOCK_MAX, BLOCK_MAX, BLOCK_MAX);
                ctx.renderPositiveXFacing(coverTexture);
                // Middle left panel
                renderBlocks.setRenderBounds(BLOCK_MAX, pipeMin, BLOCK_MIN, BLOCK_MAX, pipeMax, pipeMin);
                ctx.renderPositiveXFacing(coverTexture);
                // Middle right panel
                renderBlocks.setRenderBounds(BLOCK_MAX, pipeMin, pipeMax, BLOCK_MAX, pipeMax, BLOCK_MAX);
            }
            ctx.renderPositiveXFacing(coverTexture);
        }

        return true;
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
    public void setBaseMetaTileEntity(IGregTechTileEntity aBaseMetaTileEntity) {
        if (mBaseMetaTileEntity != null && aBaseMetaTileEntity == null) {
            mBaseMetaTileEntity.getMetaTileEntity()
                .inValidate();
            mBaseMetaTileEntity.setMetaTileEntity(null);
        }
        mBaseMetaTileEntity = aBaseMetaTileEntity;
        if (mBaseMetaTileEntity != null) {
            mBaseMetaTileEntity.setMetaTileEntity(this);
        }
    }

    @Override
    public ItemStack getStackForm(long aAmount) {
        return new ItemStack(GregTechAPI.sBlockMachines, (int) aAmount, getBaseMetaTileEntity().getMetaTileID());
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

    @Override
    public void onColorChangeClient(byte aColor) {
        // Do nothing apparently
    }

    public void setCheckConnections() {
        mCheckConnections = true;
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
        if (baseMetaTile == null || !baseMetaTile.isServerSide()) return 0;

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

    protected void checkConnections() {
        // Verify connections around us. If GT6 style cables are not enabled then revert to old behavior and try
        // connecting to everything around us
        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            if ((!getGT6StyleConnection() || isConnectedAtSide(side)) && connect(side) == 0) {
                disconnect(side);
            }
        }
        mCheckConnections = false;
    }

    private void connectAtSide(ForgeDirection side) {
        mConnections |= side.flag;
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
        if (boundingBoxShouldBeFullBlock()) {
            AxisAlignedBB fullSizeBoundingBox = AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1);
            if (inputAABB.intersectsWith(fullSizeBoundingBox)) outputAABB.add(fullSizeBoundingBox);
        }
        // Even if we're holding a tool, we still want to check for collisions with the regular size
        // of the pipe in case the player is currently in contact with it.
        AxisAlignedBB physicalBoundingBox = getPhysicalCollisionBoundingBox(x, y, z);
        if (inputAABB.intersectsWith(physicalBoundingBox)) outputAABB.add(physicalBoundingBox);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        // This is all we need to return when drawing the interaction box around the pipe.
        if (boundingBoxShouldBeFullBlock()) {
            return AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1);
        }
        // Otherwise, account for attached covers and connections
        return getPhysicalCollisionBoundingBox(x, y, z);
    }

    private boolean boundingBoxShouldBeFullBlock() {
        // While holding tool, make it full block.
        return (GTMod.GT.isClientSide() && GTMod.clientProxy()
            .forceFullBlockBB()) || getCollisionThickness() == 1;
    }

    /**
     * Gets the bounding box that corresponds to the rendered pipe.
     */
    private @NotNull AxisAlignedBB getPhysicalCollisionBoundingBox(int x, int y, int z) {
        final float space = (1f - getCollisionThickness()) / 2;
        float yStart = space;
        float yEnd = 1f - space;
        float zStart = space;
        float zEnd = 1f - space;
        float xStart = space;
        float xEnd = 1f - space;
        final BaseMetaPipeEntity baseTE = (BaseMetaPipeEntity) getBaseMetaTileEntity();

        if (baseTE.hasCoverAtSide(ForgeDirection.DOWN)) {
            yStart = zStart = xStart = 0;
            zEnd = xEnd = 1;
        }
        if (baseTE.hasCoverAtSide(ForgeDirection.UP)) {
            zStart = xStart = 0;
            yEnd = zEnd = xEnd = 1;
        }
        if (baseTE.hasCoverAtSide(ForgeDirection.NORTH)) {
            yStart = zStart = xStart = 0;
            yEnd = xEnd = 1;
        }
        if (baseTE.hasCoverAtSide(ForgeDirection.SOUTH)) {
            yStart = xStart = 0;
            yEnd = zEnd = xEnd = 1;
        }
        if (baseTE.hasCoverAtSide(ForgeDirection.WEST)) {
            yStart = zStart = xStart = 0;
            yEnd = zEnd = 1;
        }
        if (baseTE.hasCoverAtSide(ForgeDirection.EAST)) {
            yStart = zStart = 0;
            yEnd = zEnd = xEnd = 1;
        }

        // this.mConnections isn't synced, but BaseMetaPipeEntity.mConnections is for some reason
        final byte connections = baseTE.mConnections;
        if ((connections & ForgeDirection.DOWN.flag) != 0) {
            yStart = 0f;
        }
        if ((connections & ForgeDirection.UP.flag) != 0) {
            yEnd = 1f;
        }
        if ((connections & ForgeDirection.NORTH.flag) != 0) {
            zStart = 0f;
        }
        if ((connections & ForgeDirection.SOUTH.flag) != 0) {
            zEnd = 1f;
        }
        if ((connections & ForgeDirection.WEST.flag) != 0) {
            xStart = 0f;
        }
        if ((connections & ForgeDirection.EAST.flag) != 0) {
            xEnd = 1f;
        }

        return AxisAlignedBB.getBoundingBox(x + xStart, y + yStart, z + zStart, x + xEnd, y + yEnd, z + zEnd);
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

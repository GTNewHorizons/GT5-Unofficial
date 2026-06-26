package tectech.thing.metaTileEntity.hatch.bec;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.opengl.GL11;

import com.gtnewhorizon.gtnhlib.blockpos.BlockPos;
import com.gtnewhorizon.gtnhlib.color.HSVColor;
import com.gtnewhorizon.gtnhlib.util.data.Lazy;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GTValues;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.util.GTUtility;
import gregtech.api.util.tooltip.MarkdownTooltipLoader;
import gregtech.common.render.IMTERenderer;
import tectech.thing.metaTileEntity.hatch.MTEBaseFactoryHatch;

/// Line of sight connector hatch for observation arrays + teleportation nodes
public class MTEHatchLoS extends MTEBaseFactoryHatch implements IMTERenderer {

    private static final int SCAN_DIST = 128;
    private static final ResourceLocation BEAM_TEXTURE = new ResourceLocation("textures/entity/beacon_beam.png");

    private BlockPos connection = null;
    /// When connected, one LoS hatch will be the renderer, and the other won't
    private boolean isRenderer = false;

    private Lazy<List<String>> tooltip = null;

    @Nullable
    private WeakReference<MetaTileEntity> owner;
    private boolean ownerDirty = false;

    @SideOnly(Side.CLIENT)
    private boolean canRender;

    protected MTEHatchLoS(MTEHatchLoS prototype) {
        super(prototype);

        tooltip = prototype.tooltip;
    }

    public MTEHatchLoS(int aID, String aName, int aTier) {
        super(aID, aName, aTier, null);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity igte) {
        return new MTEHatchLoS(this);
    }

    @Override
    public String[] getDescription() {
        if (tooltip == null) {
            tooltip = new Lazy<>(
                () -> MarkdownTooltipLoader.STANDARD
                    .loadStandardPath(new ResourceLocation("gregtech", "los-connector"), Map.of("range", SCAN_DIST)));
        }
        return ArrayUtils.addAll(
            super.getDescription(),
            tooltip.get()
                .toArray(GTValues.emptyStringArray));
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        List<String> data = new ArrayList<>(Arrays.asList(super.getInfoData()));

        data.add("Connected: " + (connection != null));

        return data.toArray(new String[0]);
    }

    public void disconnectImpl() {
        this.connection = null;
        this.getBaseMetaTileEntity()
            .setActive(false);
        this.getBaseMetaTileEntity()
            .issueTileUpdate();
    }

    public void connectImpl(MTEHatchLoS other) {
        this.connection = other.getBaseMetaTileEntity()
            .getBlockPos();
        this.getBaseMetaTileEntity()
            .setActive(true);
        this.getBaseMetaTileEntity()
            .issueTileUpdate();
    }

    public boolean hasOwner() {
        return getOwner() != null;
    }

    public MetaTileEntity getOwner() {
        if (owner != null) {
            MetaTileEntity mte = owner.get();
            if (mte != null && mte.isValid()) return mte;
            owner = null;
        }
        return null;
    }

    public void setOwner(MetaTileEntity owner) {
        if (getOwner() != owner) {
            this.owner = new WeakReference<>(owner);
            this.ownerDirty = true;
        }
    }

    public void doScan() {
        IGregTechTileEntity igte = getBaseMetaTileEntity();

        World world = igte.getWorld();
        ForgeDirection facing = igte.getFrontFacing();

        BlockPos pos = igte.getBlockPos();
        pos.set(pos.getX() + facing.offsetX, pos.getY() + facing.offsetY, pos.getZ() + facing.offsetZ);

        for (int i = 0; i < SCAN_DIST; i++) {
            var other = igte.getMetaTileEntity(pos, MTEHatchLoS.class);

            if (other != null) {
                if (other.getBaseMetaTileEntity()
                    .getFrontFacing() == facing.getOpposite()) {
                    // Already correctly connected - skip reconnect churn
                    if (other == getConnectedHatch() && other.getConnectedHatch() == this) {
                        return;
                    }

                    // Disconnect this's existing partner before forming new connection
                    var existingThis = getConnectedHatch();
                    if (existingThis != null) {
                        this.disconnectImpl();
                        existingThis.disconnectImpl();
                    }

                    // Disconnect other's existing partner
                    var existingOther = other.getConnectedHatch();
                    if (existingOther != null) {
                        other.disconnectImpl();
                        existingOther.disconnectImpl();
                    }

                    this.connectImpl(other);
                    other.connectImpl(this);

                    this.isRenderer = true;
                    other.isRenderer = false;
                } else {
                    // Hatch found but facing incompatible - clean up any stale connection
                    var existing = getConnectedHatch();
                    if (existing != null) {
                        this.disconnectImpl();
                        existing.disconnectImpl();
                    }
                }

                return;
            }

            if (!world.blockExists(pos.getX(), pos.getY(), pos.getZ())) {
                // Chunk not loaded - unloaded chunks report air, so stop scanning.
                break;
            }

            if (world.getBlockLightOpacity(pos.getX(), pos.getY(), pos.getZ()) > 0) {
                // Not fully transparent, blocks line of sight.
                break;
            }

            pos.set(pos.getX() + facing.offsetX, pos.getY() + facing.offsetY, pos.getZ() + facing.offsetZ);
        }

        var existing = getConnectedHatch();

        if (existing != null) {
            this.disconnectImpl();

	    if (existing.getConnectedHatch() == this) {
                existing.disconnectImpl();
            }
        }
    }

    public MTEHatchLoS getConnectedHatch() {
        if (connection == null) return null;

        IGregTechTileEntity igte = getBaseMetaTileEntity();

        if (igte == null || igte.isDead()) {
            connection = null;
            return null;
        }

        MTEHatchLoS result = igte.getMetaTileEntity(connection, MTEHatchLoS.class);

        if (result == null) {
            connection = null;
        }

        return result;
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);

        if (GTUtility.isServer()) {
            doScan();
        }
    }

    @Override
    public void onRemoval() {
        super.onRemoval();

        if (GTUtility.isServer()) {
            var connection = getConnectedHatch();

            if (connection != null) {
                this.disconnectImpl();
                connection.disconnectImpl();
            }
        }
    }

    @Override
    public void onFacingChange() {
        super.onFacingChange();

        if (GTUtility.isServer()) {
            var connection = getConnectedHatch();

            if (connection != null) {
                this.disconnectImpl();
                connection.disconnectImpl();
            }

            doScan();
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity baseMetaTileEntity, long tick) {
        super.onPostTick(baseMetaTileEntity, tick);

        if (GTUtility.isServer()) {
            if (ownerDirty) {
                ownerDirty = false;
                baseMetaTileEntity.issueTileUpdate();
            }

            // Try to reconnect every 5 seconds in case a chunk loaded or something
            if (tick % 100 == 0) {
                doScan();
            }
        }
    }

    @Override
    public NBTTagCompound getDescriptionData() {
        NBTTagCompound tag = super.getDescriptionData();

        tag.setBoolean("connected", connection != null);
        tag.setBoolean("isRenderer", isRenderer);

        var other = getConnectedHatch();

        tag.setBoolean("canRender", this.hasOwner() && other != null && other.hasOwner());

        if (connection != null) {
            tag.setInteger("connX", connection.getX());
            tag.setInteger("connY", connection.getY());
            tag.setInteger("connZ", connection.getZ());
        }

        return tag;
    }

    @Override
    public void onDescriptionPacket(NBTTagCompound data) {
        super.onDescriptionPacket(data);

        isRenderer = data.getBoolean("isRenderer");
        canRender = data.getBoolean("canRender");

        if (data.getBoolean("connected")) {
            connection = new BlockPos(data.getInteger("connX"), data.getInteger("connY"), data.getInteger("connZ"));
        } else {
            connection = null;
        }
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox(int x, int y, int z) {
        // Angelica refuses to update the TESR's AABB, so we just elide that check here
        return TileEntity.INFINITE_EXTENT_AABB;
    }

    private final float hueOffset = ThreadLocalRandom.current()
        .nextFloat();

    @Override
    public void renderTESR(double x, double y, double z, float timeSinceLastTick) {
        if (!isRenderer || connection == null || !canRender) return;

        IGregTechTileEntity igte = getBaseMetaTileEntity();
        World world = igte.getWorld();
        if (world == null) return;

        double dx = connection.getX() - igte.getXCoord();
        double dy = connection.getY() - igte.getYCoord();
        double dz = connection.getZ() - igte.getZCoord();
        double length = Math.sqrt(dx * dx + dy * dy + dz * dz);

        GL11.glPushMatrix();
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);

        Minecraft.getMinecraft().renderEngine.bindTexture(BEAM_TEXTURE);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10497.0F);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10497.0F);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);

        GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);

        // Rotate local +Y to point toward connected hatch
        double nx = dx / length, ny = dy / length, nz = dz / length;
        double rotAxisX = nz, rotAxisZ = -nx;
        double sinAngle = Math.sqrt(rotAxisX * rotAxisX + rotAxisZ * rotAxisZ);
        if (sinAngle > 1e-6) {
            GL11.glRotated(Math.toDegrees(Math.atan2(sinAngle, ny)), rotAxisX / sinAngle, 0.0, rotAxisZ / sinAngle);
        } else if (ny < 0) {
            GL11.glRotated(180.0, 1.0, 0.0, 0.0);
        }

        float worldTime = (float) world.getTotalWorldTime() + timeSinceLastTick;
        float scroll = -worldTime * 0.2F - (float) Math.floor(-worldTime * 0.1F);
        Tessellator tess = Tessellator.instance;

        // Pass 1: outer rotating prism (no blend, additive)
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
        OpenGlHelper.glBlendFunc(770, 1, 1, 0);

        double outerRadius = 0.6;
        double rot = (double) worldTime * -0.0375;
        double c1x = Math.cos(rot + Math.PI * 0.75) * outerRadius;
        double c1z = Math.sin(rot + Math.PI * 0.75) * outerRadius;
        double c2x = Math.cos(rot + Math.PI * 0.25) * outerRadius;
        double c2z = Math.sin(rot + Math.PI * 0.25) * outerRadius;
        double c3x = Math.cos(rot + Math.PI * 1.25) * outerRadius;
        double c3z = Math.sin(rot + Math.PI * 1.25) * outerRadius;
        double c4x = Math.cos(rot + Math.PI * 1.75) * outerRadius;
        double c4z = Math.sin(rot + Math.PI * 1.75) * outerRadius;

        double vMin1 = -1.0 + scroll;
        double vMax1 = length * (0.5 / outerRadius) + vMin1;

        float hue = ((System.currentTimeMillis() % 10_000) / 10_000f + hueOffset) % 1f;

        HSVColor color = new HSVColor(hue, 0.5f, 0.75f);

        tess.startDrawingQuads();
        tess.setColorRGBA(color.getRed(), color.getGreen(), color.getBlue(), 32);
        addBeamQuad(tess, c1x, c1z, c2x, c2z, length, vMin1, vMax1);
        addBeamQuad(tess, c4x, c4z, c3x, c3z, length, vMin1, vMax1);
        addBeamQuad(tess, c2x, c2z, c4x, c4z, length, vMin1, vMax1);
        addBeamQuad(tess, c3x, c3z, c1x, c1z, length, vMin1, vMax1);
        tess.draw();

        // Pass 2: inner solid prism (blended, semi-transparent)
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glDepthMask(false);

        double inner = 0.6;
        double vMin2 = -1.0 + scroll;
        double vMax2 = length + vMin2;

        tess.startDrawingQuads();
        tess.setColorRGBA(color.getRed(), color.getGreen(), color.getBlue(), 32);
        addBeamQuad(tess, -inner, -inner, inner, -inner, length, vMin2, vMax2);
        addBeamQuad(tess, inner, inner, -inner, inner, length, vMin2, vMax2);
        addBeamQuad(tess, inner, -inner, inner, inner, length, vMin2, vMax2);
        addBeamQuad(tess, -inner, inner, -inner, -inner, length, vMin2, vMax2);
        tess.draw();

        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }

    private static void addBeamQuad(Tessellator tess, double x1, double z1, double x2, double z2, double length,
        double vMin, double vMax) {
        tess.addVertexWithUV(x1, length, z1, 1.0, vMax);
        tess.addVertexWithUV(x1, 0, z1, 1.0, vMin);
        tess.addVertexWithUV(x2, 0, z2, 0.0, vMin);
        tess.addVertexWithUV(x2, length, z2, 0.0, vMax);
    }
}

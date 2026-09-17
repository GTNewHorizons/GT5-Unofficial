package gregtech.api.metatileentity.implementations;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.gtnhlib.blockpos.BlockPos;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IKineticProducer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTSplit;
import gregtech.client.renderer.RenderRotor;
import gregtech.common.config.MachineStats;
import gregtech.common.render.IMTERenderer;
import ic2.api.item.IKineticRotor;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public abstract class MTEKineticRotorBase extends MTETieredMachineBlock implements IMTERenderer, IKineticProducer {

    // config file vales
    private static int SPACE_CHECK_TICK_INTERVAL;
    private static boolean doRender;
    protected static float OUTPUT_MULTI;

    // machine values
    protected long kuOut = 0L;
    protected double obstructedPercent = 1d;
    private boolean rotorBlocked = true;
    private final int spaceFront;
    private final int spaceSides;

    // for rendering, since inventory doesn't sync unless interacted by client
    private int rotorID = -1;
    private double rotorRotation = 0d;

    public MTEKineticRotorBase(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 1, (String) null);
        this.spaceFront = 0;
        this.spaceSides = 0;
    }

    @Override
    public String[] getDescription() {
        Stream<String> base = Stream.of(GTSplit.splitLocalized("gt.blockmachines.kinetic.base_producer.tooltip"));
        Stream<String> extra = Stream.of(getExtraDescriptionInfo());
        Stream<String> kuInfo = Stream.of(
            String.format(
                StatCollector.translateToLocal("gt.blockmachines.kinetic.generator.ku_info"),
                String.format("%,d", maxKU())));
        return Stream.of(base, extra, kuInfo)
            .flatMap(Function.identity())
            .toArray(String[]::new);
    }

    protected abstract String[] getExtraDescriptionInfo();

    public MTEKineticRotorBase(String aName, int aTier, int aInvSlotCount, String[] aDescription,
        ITexture[][][] aTextures, int spaceFront, int spaceSides) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
        this.spaceFront = spaceFront;
        this.spaceSides = spaceSides;
    }

    public long currentKU() {
        return kuOut;
    }

    public boolean isRotorBlocked() {
        return rotorBlocked;
    }

    protected World getWorld() {
        return getBaseMetaTileEntity().getWorld();
    }

    protected float getRotorEfficiency() {
        IKineticRotor rotor = getRotor();
        // getEfficiency requires a parameter that is never used so just pass null
        return rotor != null ? rotor.getEfficiency(null) : 0;
    }

    protected abstract long calculateCurrentKU();

    protected ItemStack getRotorStack() {
        return mInventory[0];
    }

    public IKineticRotor getRotor() {
        if (getRotorStack() == null) return null;
        return getRotorStack().getItem() instanceof IKineticRotor rotor ? rotor : null;
    }

    public int getRotorID() {
        return rotorID;
    }

    protected void doRotorDamage() {
        IKineticRotor rotor = getRotor();
        if (rotor != null) {
            ItemStack rotorItemStack = getRotorStack();
            int currDamage = rotorItemStack.getItemDamage();
            int newDamage = currDamage + tierScalar();
            if (newDamage >= rotorItemStack.getMaxDamage()) {
                mInventory[0] = null;
                rotorID = -1;
                this.getBaseMetaTileEntity()
                    .issueTileUpdate();
                kuOut = calculateCurrentKU();
            } else {
                rotorItemStack.setItemDamage(currDamage + tierScalar());
            }
        }
    }

    // Multi based on tier (HV: 1x, EV: 4x, IV: 16x...)
    protected int tierScalar() {
        if (mTier < 4) return 1;
        return 4 << (2 * (mTier - 4));
    }

    public long maxKU() {
        return GTValues.V[mTier] << 2;
    }

    // Stuff for gui
    public int getSpaceRequiredFront() {
        return spaceFront;
    }

    public int getSpaceRequiredSides() {
        return getRadiusFromID(rotorID) + spaceSides;
    }

    private static final int[] radii = { 2, 3, 4, 5, 4, 5, 6, 7, 3, 3, 3, 4 };

    private static int getRadiusFromID(int rotorID) {
        if (rotorID == -1) return radii[0];
        return radii[rotorID];
    }

    /**
     * Checks for blocks in a specified area around the rotor for the purpose of KU output calculation
     *
     * @param rotorBlock if checking for the area in the rotor's spinning path (collision)
     * @return if ratorBlock is false: ratio of blocks in volume / total volume.
     *         if rotorBlock is true: 1 if block in collision path, 0 otherwise
     */
    protected double checkRotorSpace(boolean rotorBlock) {
        IKineticRotor rotor = getRotor();
        if (rotor == null) return 0;

        ForgeDirection rotorSide = getRotorSide();
        ForgeDirection leftFromRotorSide = rotorSide.getRotation(ForgeDirection.DOWN);
        BlockPos tilePos = getBaseMetaTileEntity().getBlockPos();
        int diameter = rotor.getDiameter(null);
        int radius = diameter / 2;

        int spaceFront = rotorBlock ? 1 : getSpaceRequiredFront();
        int spaceSide = rotorBlock ? 0 : getSpaceRequiredSides();
        int parallelSize = diameter + (spaceSide * 2);

        int sizeY = parallelSize;
        int sizeX = Math.abs((rotorSide.offsetX * spaceFront) + (leftFromRotorSide.offsetX * parallelSize));
        int sizeZ = Math.abs((rotorSide.offsetZ * spaceFront) + (leftFromRotorSide.offsetZ * parallelSize));

        int topLeftY = tilePos.y + radius + spaceSide;
        int topLeftX = tilePos.x + ((radius + spaceSide) * leftFromRotorSide.offsetX)
            + (rotorBlock ? rotorSide.offsetX : 0);
        int topLeftZ = tilePos.z + ((radius + spaceSide) * leftFromRotorSide.offsetZ)
            + (rotorBlock ? rotorSide.offsetZ : 0);

        int bottomRightY = topLeftY - sizeY + 1;
        int bottomRightX = topLeftX + ((rotorSide.offsetX - leftFromRotorSide.offsetX) * (sizeX - 1));
        int bottomRightZ = topLeftZ + ((rotorSide.offsetZ - leftFromRotorSide.offsetZ) * (sizeZ - 1));

        ChunkCache chunkCache = new ChunkCache(
            this.getWorld(),
            Math.min(topLeftX, bottomRightX),
            bottomRightY,
            Math.min(topLeftZ, bottomRightZ),
            Math.max(topLeftX, bottomRightX),
            topLeftY,
            Math.max(topLeftZ, bottomRightZ),
            0);

        int obstructed = 0;
        ForgeDirection rightFromRotorSide = leftFromRotorSide.getOpposite();
        int chkX, chkY, chkZ;
        for (int offsetParallel = 0; offsetParallel < parallelSize; offsetParallel++) {
            for (int offsetVertical = 0; offsetVertical < parallelSize; offsetVertical++) {
                for (int offsetNormal = 0; offsetNormal < spaceFront; offsetNormal++) {
                    chkX = topLeftX + (rightFromRotorSide.offsetX * offsetParallel)
                        + (rotorSide.offsetX * offsetNormal);
                    chkY = topLeftY - offsetVertical;
                    chkZ = topLeftZ + (rightFromRotorSide.offsetZ * offsetParallel)
                        + (rotorSide.offsetZ * offsetNormal);

                    if (offsetNormal == 0 && !rotorBlock) { // so machine can sit flush to wall
                        if (chkX == tilePos.x && chkY == tilePos.y && chkZ == tilePos.z) continue;
                        TileEntity tile = chunkCache.getTileEntity(chkX, chkY, chkZ);
                        if (isTileKineticRotorBase(tile)) return 1d;
                    } else {
                        Block block = chunkCache.getBlock(chkX, chkY, chkZ);
                        if (!isBlockValid(block)) {
                            if (rotorBlock || isTileKineticRotorBase(chunkCache.getTileEntity(chkX, chkY, chkZ))) {
                                return 1d;
                            } else {
                                obstructed++;
                            }
                        }
                    }
                }
            }
        }

        return ((double) obstructed / (double) (sizeX * sizeY * sizeZ));
    }

    protected abstract boolean isBlockValid(Block block);

    private boolean isTileKineticRotorBase(TileEntity tileEntity) {
        return (tileEntity instanceof IGregTechTileEntity gregTile
            && gregTile.getMetaTileEntity() instanceof MTEKineticRotorBase);
    }

    // obstruction follows an exponential decay curve
    public double obstructedOutputMultiplier() {
        return Math.pow((1 - obstructedPercent), 4);
    }

    public ForgeDirection getKUOutSide() {
        return getBaseMetaTileEntity().getFrontFacing();
    }

    public ForgeDirection getRotorSide() {
        return getBaseMetaTileEntity().getBackFacing();
    }

    private boolean renderingAllowed() {
        return doRender && (rotorID != -1) && !rotorBlocked;
    }

    /**
     * for rendering, value to be added to current rotation angle each render cycle
     * @return amount to be added
     */
    protected double getRotationAmount() {
        return rotorBlocked ? 0d : obstructedOutputMultiplier();
    }

    // no consistent way to get an identifier for rotors until they are transferred from other mods in ic2 deprecation
    // for now use this
    private static int rotorIDFromRotor(ItemStack rotor) {
        return switch (rotor.getUnlocalizedName()) {
            case "ic2.itemwoodrotor" -> 0;
            case "ic2.itemironrotor" -> 1;
            case "ic2.itemsteelrotor" -> 2;
            case "ic2.itemwcarbonrotor" -> 3;
            case "ic2.itemEnergeticRotor" -> 4;
            case "ic2.itemTungstenSteel" -> 5;
            case "ic2.itemVibrantRotor" -> 6;
            case "ic2.itemIridiumRotor" -> 7;
            case "item.BW_LeatherRotor" -> 8;
            case "item.BW_WoolRotor" -> 9;
            case "item.BW_PaperRotor" -> 10;
            case "item.BW_CombinedRotor" -> 11;
            default -> -1; // should never occur due to slot restrictions but here as failsafe
        };
    }

    /**
     * Call this to check working space of machine and send necessary values to clients
     */
    private void doRotorChecks(IGregTechTileEntity baseTile) {
        rotorBlocked = checkRotorSpace(true) == 1d;
        obstructedPercent = rotorBlocked ? 1d : checkRotorSpace(false);
        kuOut = rotorBlocked ? 0L : calculateCurrentKU();
        baseTile.issueTileUpdate();
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (getRotorStack() != null) {
                if (rotorID == -1) {
                    rotorID = rotorIDFromRotor(getRotorStack());
                    doRotorChecks(aBaseMetaTileEntity);
                }
                if (!rotorBlocked) {
                    doRotorDamage();
                    if (aTick % 20 == 0) {
                        kuOut = calculateCurrentKU();
                    }
                }
                if (aTick % SPACE_CHECK_TICK_INTERVAL == 0) {
                    doRotorChecks(aBaseMetaTileEntity);
                }
            } else {
                // reset to initial state if rotor removed/broke
                if (rotorID != -1) {
                    this.rotorID = -1;
                    kuOut = 0L;
                    rotorBlocked = true;
                    aBaseMetaTileEntity.issueTileUpdate();
                }
            }
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("rotorID", rotorID);
        aNBT.setBoolean("rotorBlocked", rotorBlocked);
        aNBT.setDouble("obstructedPercent", obstructedPercent);
        aNBT.setLong("kuOut", kuOut);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        rotorID = aNBT.getInteger("rotorID");
        rotorBlocked = aNBT.getBoolean("rotorBlocked");
        obstructedPercent = aNBT.getDouble("obstructedPercent");
        kuOut = aNBT.getLong("kuOut");
    }

    @Override
    public NBTTagCompound getDescriptionData() {
        NBTTagCompound data = super.getDescriptionData();
        if (data == null) data = new NBTTagCompound();

        data.setInteger("rotorID", rotorID);
        data.setBoolean("rotorBlocked", rotorBlocked);
        data.setDouble("obstructedPercent", obstructedPercent);
        data.setLong("kuOut", kuOut);

        return data;
    }

    @Override
    public void onDescriptionPacket(NBTTagCompound data) {
        super.onDescriptionPacket(data);
        rotorID = data.getInteger("rotorID");
        rotorBlocked = data.getBoolean("rotorBlocked");
        obstructedPercent = data.getDouble("obstructedPercent");
        kuOut = data.getLong("kuOut");
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        tag.setLong("kuout", this.kuOut);
        tag.setInteger("rotorID", this.rotorID);
        tag.setBoolean("rotorBlocked", this.rotorBlocked);
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        final NBTTagCompound tag = accessor.getNBTData();
        if (tag.hasKey("rotorID") && tag.getInteger("rotorID") == -1) {
            currenttip.add(EnumChatFormatting.RED + StatCollector.translateToLocal("GT5U.waila.kinetic.no_rotor"));
        } else {
            if (tag.hasKey("rotorBlocked") && tag.getBoolean("rotorBlocked")) {
                currenttip
                    .add(EnumChatFormatting.RED + StatCollector.translateToLocal("GT5U.waila.kinetic.rotor_blocked"));
            } else {
                currenttip.add(EnumChatFormatting.GREEN + StatCollector.translateToLocal("GT5U.waila.generating.on"));
                if (tag.hasKey("kuout")) currenttip.add(
                    StatCollector.translateToLocal("GT5U.waila.kinetic.ku_produced") + " "
                        + EnumChatFormatting.GREEN
                        + formatNumber(tag.getLong("kuout"))
                        + EnumChatFormatting.GRAY
                        + " / "
                        + EnumChatFormatting.YELLOW
                        + formatNumber(this.maxKU())
                        + " KU");
            }
        }
        super.getWailaBody(itemStack, currenttip, accessor, config);
    }

    @Override
    public void renderTESR(double x, double y, double z, float timeSinceLastTick) {
        if (!renderingAllowed()) return;

        ForgeDirection side = getRotorSide();
        if (side.offsetX != 0) {
            z += 0.5f;
            x += (side == ForgeDirection.EAST) ? 1.06f : -0.06f;
        } else {
            x += 0.5f;
            z += (side == ForgeDirection.SOUTH) ? 1.06f : -0.06f;
        }
        y += 0.5f;

        rotorRotation += getRotationAmount();
        if (rotorRotation > 360d) rotorRotation -= 360d;

        RenderRotor.doRender(x, y, z, rotorRotation, side, rotorID);
    }

    @Override
    public void onConfigLoad() {
        SPACE_CHECK_TICK_INTERVAL = MachineStats.kinetics.spaceCheckTickInterval;
        doRender = MachineStats.kinetics.doClientRender;
        OUTPUT_MULTI = (MachineStats.kinetics.kuOutputMultiplier * 10.0f);
    }

    @Override
    public boolean willExplodeInRain() {
        return false;
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

    @Override
    public boolean allowCoverOnSide(ForgeDirection side, ItemStack coverItem) {
        return !(side == getRotorSide() || side == getKUOutSide());
    }

    @Override
    public boolean isFacingValid(ForgeDirection side) {
        return (side != ForgeDirection.DOWN && side != ForgeDirection.UP);
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        return null;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        if (side == facing) {
            return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][colorIndex + 1],
                TextureFactory.of(Textures.BlockIcons.OVERLAY_KINETIC_TRANSFER) };
        }
        if (side == facing.getOpposite()) {
            return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][colorIndex + 1],
                TextureFactory.of(Textures.BlockIcons.OVERLAY_ROTOR_BEARING) };
        }
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][colorIndex + 1] };
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, ForgeDirection side,
        float aX, float aY, float aZ) {
        openGui(aPlayer);
        return true;
    }
}

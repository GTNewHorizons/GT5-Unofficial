package gregtech.api.metatileentity;

import java.util.List;
import java.util.function.Supplier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.utils.item.IItemHandlerModifiable;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;

import appeng.api.crafting.ICraftingIconProvider;
import appeng.api.implementations.IPowerChannelState;
import appeng.api.networking.energy.IEnergyGrid;
import appeng.api.networking.events.MENetworkBootingStatusChange;
import appeng.api.networking.events.MENetworkPowerStatusChange;
import appeng.api.networking.pathing.IPathingGrid;
import appeng.api.util.AECableType;
import appeng.core.localization.WailaText;
import appeng.me.helpers.AENetworkProxy;
import cpw.mods.fml.common.registry.LanguageRegistry;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.SteamVariant;
import gregtech.api.gui.GUIColorOverride;
import gregtech.api.gui.modularui.GUITextureSet;
import gregtech.api.interfaces.ICleanroomReceiver;
import gregtech.api.interfaces.IConfigurationCircuitSupport;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTECable;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTTooltipDataCache;
import gregtech.api.util.GTUtility;
import gregtech.common.capability.CleanroomReference;
import gregtech.mixin.interfaces.accessors.EntityPlayerMPAccessor;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import tectech.mechanics.pipe.IConnectsToDataPipe;
import tectech.mechanics.pipe.IConnectsToEnergyTunnel;
import tectech.thing.metaTileEntity.pipe.MTEPipeData;
import tectech.thing.metaTileEntity.pipe.MTEPipeLaser;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * Extend this Class to add a new MetaMachine Call the Constructor with the desired ID at the load-phase (not preload
 * and also not postload!) Implement the newMetaEntity-Method to return a new ready instance of your MetaTileEntity
 * <p/>
 * Call the Constructor like the following example inside the Load Phase, to register it. "new MTEFurnace(54,
 * "GT_E_Furnace", "Automatic E-Furnace");"
 */
@SuppressWarnings("unused")
public abstract class MetaTileEntity extends CommonMetaTileEntity implements ICraftingIconProvider {

    /**
     * Inventory wrapper for ModularUI
     */
    public final ItemStackHandler inventoryHandler;

    protected GUIColorOverride colorOverride;
    protected GTTooltipDataCache mTooltipCache = new GTTooltipDataCache();

    private static final String[] FACING_DIRECTION_NAMES = new String[] { "GT5U.waila.facing.down",
        "GT5U.waila.facing.up", "GT5U.waila.facing.north", "GT5U.waila.facing.south", "GT5U.waila.facing.west",
        "GT5U.waila.facing.east", "GT5U.waila.facing.unknown" };

    @Override
    public IItemHandlerModifiable getInventoryHandler() {
        return inventoryHandler;
    }

    protected final ICleanroomReceiver cleanroomReference = new CleanroomReference();

    /**
     * accessibility to this Field is no longer given, see below
     */
    private IGregTechTileEntity mBaseMetaTileEntity;

    private String playerLang;

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
    public MetaTileEntity(int aID, String aBasicName, String aRegionalName, int aInvSlotCount) {
        super(aID, aBasicName, aInvSlotCount);
        setBaseMetaTileEntity(GregTechAPI.constructBaseMetaTileEntity());
        getBaseMetaTileEntity().setMetaTileID((short) aID);

        GTLanguageManager.addStringLocalization("gt.blockmachines." + mName + ".name", aRegionalName);

        inventoryHandler = new ItemStackHandler(mInventory) {

            @Override
            protected void onContentsChanged(int slot) {
                MetaTileEntity.this.onContentsChanged(slot);
            }
        };
    }

    /**
     * This is the normal Constructor.
     */
    public MetaTileEntity(String aName, int aInvSlotCount) {
        super(aName, aInvSlotCount);
        inventoryHandler = new ItemStackHandler(mInventory) {

            @Override
            protected void onContentsChanged(int slot) {
                MetaTileEntity.this.onContentsChanged(slot);
            }
        };
        colorOverride = GUIColorOverride.get(getGUITextureSet().getMainBackground().location);
    }

    @Nullable
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

    public boolean isValid() {
        return getBaseMetaTileEntity() != null && getBaseMetaTileEntity().getMetaTileEntity() == this
            && !getBaseMetaTileEntity().isDead();
    }

    /**
     * Override to delegate capability this machine implements to baseMTE. Don't forget to fall back to
     * {@code super.getCapability}.
     *
     * @see com.gtnewhorizon.gtnhlib.capability.CapabilityProvider CapabilityProvider
     * @inheritDoc
     */
    @Nullable
    @Override
    public <T> T getCapability(@NotNull Class<T> capability, @NotNull ForgeDirection side) {
        if (capability == ICleanroomReceiver.class) {
            return capability.cast(cleanroomReference);
        }
        if (capability == ICraftingIconProvider.class) {
            return capability.cast(this);
        }
        return super.getCapability(capability, side);
    }

    @Override
    public ItemStack getStackForm(long aAmount) {
        return new ItemStack(GregTechAPI.sBlockMachines, (int) aAmount, getBaseMetaTileEntity().getMetaTileID());
    }

    @Override
    public String getLocalName() {
        return StatCollector.translateToLocal("gt.blockmachines." + mName + ".name");
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {}

    @Override
    public boolean onWrenchRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer entityPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        final IGregTechTileEntity meta = getBaseMetaTileEntity();
        if (!meta.isValidFacing(wrenchingSide)) return false;
        meta.setFrontFacing(wrenchingSide);

        for (final ForgeDirection s : ForgeDirection.VALID_DIRECTIONS) {
            final IGregTechTileEntity iGregTechTileEntity = meta.getIGregTechTileEntityAtSide(s);
            if (iGregTechTileEntity != null) {
                if (iGregTechTileEntity.getMetaTileEntity() instanceof MTEPipeLaser pipe) pipe.updateNetwork(true);
                if (iGregTechTileEntity.getMetaTileEntity() instanceof MTEPipeData pipe) pipe.updateNetwork(true);
            }
        }
        return true;
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {

        if (!aPlayer.isSneaking()) return false;
        final ForgeDirection oppositeSide = wrenchingSide.getOpposite();
        final TileEntity tTileEntity = getBaseMetaTileEntity().getTileEntityAtSide(wrenchingSide);
        if ((tTileEntity instanceof IGregTechTileEntity gtTE) && (gtTE.getMetaTileEntity() instanceof MTECable)) {

            // The tile entity we're facing is a cable, let's try to connect to it
            return gtTE.getMetaTileEntity()
                .onWireCutterRightClick(wrenchingSide, oppositeSide, aPlayer, aX, aY, aZ, aTool);
        }
        return false;
    }

    @Override
    public boolean onSolderingToolRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {

        if (!aPlayer.isSneaking()) return false;
        final ForgeDirection oppositeSide = wrenchingSide.getOpposite();
        TileEntity tTileEntity = getBaseMetaTileEntity().getTileEntityAtSide(wrenchingSide);
        if ((tTileEntity instanceof IGregTechTileEntity gtTE) && (gtTE.getMetaTileEntity() instanceof MTECable)) {

            // The tile entity we're facing is a cable, let's try to connect to it
            return gtTE.getMetaTileEntity()
                .onSolderingToolRightClick(wrenchingSide, oppositeSide, aPlayer, aX, aY, aZ, aTool);
        }
        return false;
    }

    @Override
    public void onExplosion() {
        GTLog.exp.println(
            "Machine at " + this.getBaseMetaTileEntity()
                .getXCoord()
                + " | "
                + this.getBaseMetaTileEntity()
                    .getYCoord()
                + " | "
                + this.getBaseMetaTileEntity()
                    .getZCoord()
                + " DIMID: "
                + this.getBaseMetaTileEntity()
                    .getWorld().provider.dimensionId
                + " exploded.");
    }

    /**
     * a Player rightclicks the Machine Sneaky rightclicks are not getting passed to this!
     */
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        return false;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, ForgeDirection side,
        float aX, float aY, float aZ) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (aPlayer instanceof EntityPlayerMPAccessor) {
                playerLang = ((EntityPlayerMPAccessor) aPlayer).gt5u$getTranslator();
            }
        }

        return onRightclick(aBaseMetaTileEntity, aPlayer);
    }

    protected String translate(String key) {
        String base;

        if (playerLang != null) {
            // note: playerLang is set in onRightclick, so this may not be 100% correct in some situations
            // on server
            base = LanguageRegistry.instance()
                .getStringLocalization(key, playerLang);
        } else {
            // on client
            base = LanguageRegistry.instance()
                .getStringLocalization(key);
        }

        if (base.isEmpty()) {
            base = LanguageRegistry.instance()
                .getStringLocalization(key, "en_US");
        }

        return base;
    }

    protected String translate(String key, Object... params) {
        return String.format(translate(key), params);
    }

    /**
     * @return true if this Device emits Energy at all
     */
    public boolean isElectric() {
        return true;
    }

    /**
     * @return true if this Device emits Energy at all
     */
    public boolean isPneumatic() {
        return false;
    }

    /**
     * @return true if this Device emits Energy at all
     */
    public boolean isSteampowered() {
        return false;
    }

    /**
     * @return what type of texture does this machine use for GUI, i.e. Bronze, Steel, or Primitive
     */
    public SteamVariant getSteamVariant() {
        return SteamVariant.NONE;
    }

    /**
     * @return true if this Device emits Energy at all
     */
    public boolean isEnetOutput() {
        return false;
    }

    /**
     * @return true if this Device consumes Energy at all
     */
    public boolean isEnetInput() {
        return false;
    }

    /**
     * @return the amount of EU, which can be stored in this Device. Default is 0 EU.
     */
    public long maxEUStore() {
        return 0;
    }

    /**
     * @return the amount of EU/t, which can be accepted by this Device before it explodes.
     */
    public long maxEUInput() {
        return 0;
    }

    /**
     * @return the amount of EU/t, which can be outputted by this Device.
     */
    public long maxEUOutput() {
        return 0;
    }

    /**
     * @return the amount of E-net Impulses of the maxEUOutput size, which can be outputted by this Device. Default is 1
     *         Pulse, this shouldn't be set to smaller Values than 1, as it won't output anything in that Case!
     */
    public long maxAmperesOut() {
        return 1;
    }

    /**
     * How many Amperes this Block can suck at max. Surpassing this value won't blow it up.
     */
    public long maxAmperesIn() {
        return 1;
    }

    /**
     * @return true if that Side is an Output.
     */
    public boolean isOutputFacing(ForgeDirection side) {
        return false;
    }

    /**
     * @return true if that Side is an Input.
     */
    public boolean isInputFacing(ForgeDirection side) {
        return false;
    }

    /**
     * @return true if Transformer Upgrades increase Packet Amount.
     */
    public boolean isTransformingLowEnergy() {
        return true;
    }

    /**
     * This is used to get the internal Energy. I use this for the IDSU.
     */
    public long getEUVar() {
        return ((BaseMetaTileEntity) mBaseMetaTileEntity).mStoredEnergy;
    }

    /**
     * This is used to set the internal Energy to the given Parameter. I use this for the IDSU.
     */
    public void setEUVar(long aEnergy) {
        if (aEnergy != ((BaseMetaTileEntity) mBaseMetaTileEntity).mStoredEnergy) {
            markDirty();
            ((BaseMetaTileEntity) mBaseMetaTileEntity).mStoredEnergy = aEnergy;
        }
    }

    /**
     * This is used to get the internal Steam Energy.
     */
    public long getSteamVar() {
        return ((BaseMetaTileEntity) mBaseMetaTileEntity).mStoredSteam;
    }

    /**
     * This is used to set the internal Steam Energy to the given Parameter.
     */
    public void setSteamVar(long aSteam) {
        if (((BaseMetaTileEntity) mBaseMetaTileEntity).mStoredSteam != aSteam) {
            markDirty();
            ((BaseMetaTileEntity) mBaseMetaTileEntity).mStoredSteam = aSteam;
        }
    }

    /**
     * @return the amount of Steam, which can be stored in this Device. Default is 0 EU.
     */
    public long maxSteamStore() {
        return 0;
    }

    /**
     * @return the amount of EU, which this Device stores before starting to emit Energy. useful if you don't want to
     *         emit stored Energy until a certain Level is reached.
     */
    public long getMinimumStoredEU() {
        return 512;
    }

    /**
     * Determines the Tier of the Machine, used for de-charging Tools.
     */
    public long getInputTier() {
        return GTUtility.getTier(getBaseMetaTileEntity().getInputVoltage());
    }

    /**
     * Determines the Tier of the Machine, used for charging Tools.
     */
    public long getOutputTier() {
        return GTUtility.getTier(getBaseMetaTileEntity().getOutputVoltage());
    }

    /**
     * gets the first RechargerSlot
     */
    public int rechargerSlotStartIndex() {
        return 0;
    }

    /**
     * gets the amount of RechargerSlots
     */
    public int rechargerSlotCount() {
        return 0;
    }

    /**
     * gets the first DechargerSlot
     */
    public int dechargerSlotStartIndex() {
        return 0;
    }

    /**
     * gets the amount of DechargerSlots
     */
    public int dechargerSlotCount() {
        return 0;
    }

    /**
     * gets if this is protected from other Players per default or not
     */
    public boolean ownerControl() {
        return false;
    }

    @Override
    public boolean isLiquidInput(ForgeDirection side) {
        return true;
    }

    @Override
    public boolean isLiquidOutput(ForgeDirection side) {
        return true;
    }

    /**
     * Actual fluid capacity. If your machine has void-overflow feature, you'll want to override this method to make
     * sure correct capacity is shown on GUI.
     */
    public int getRealCapacity() {
        return getCapacity();
    }

    /**
     * When the Facing gets changed.
     */
    public void onFacingChange() {
        /* Do nothing */
    }

    /**
     * if the IC2 Teleporter can drain from this.
     */
    public boolean isTeleporterCompatible() {
        return isEnetOutput() && getBaseMetaTileEntity().getOutputVoltage() >= 128
            && getBaseMetaTileEntity().getUniversalEnergyCapacity() >= 500000;
    }

    /**
     * Flag if this MTE will exploding when its raining
     *
     * @return True if this will explode in rain, else false
     */
    public boolean willExplodeInRain() {
        return true;
    }

    @Override
    public void setInventorySlotContents(int aIndex, ItemStack aStack) {
        if (this instanceof IConfigurationCircuitSupport ccs) {
            if (ccs.allowSelectCircuit() && aIndex == ccs.getCircuitSlot() && aStack != null) {
                mInventory[aIndex] = GTUtility.copyAmount(0, aStack);
                markDirty();
                return;
            }
        }
        super.setInventorySlotContents(aIndex, aStack);
        onContentsChanged(aIndex);
    }

    /**
     * Called when a slot is changed. Note: {@link #setInventorySlotContents} is not called when the player interacts
     * with a {@link gregtech.api.interfaces.modularui.IAddInventorySlots} slot.
     */
    protected void onContentsChanged(int slot) {

    }

    /**
     * Implement {@link #fill(ForgeDirection, FluidStack, boolean)} instead.
     */
    @SuppressWarnings("DeprecatedIsStillUsed")
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public int fill_default(ForgeDirection side, FluidStack aFluid, boolean doFill) {
        int filled = fill(aFluid, doFill);
        if (filled > 0) {
            markDirty();
        }
        return filled;
    }

    @Override
    public int fill(ForgeDirection side, FluidStack aFluid, boolean doFill) {
        if (getBaseMetaTileEntity().isSteampowered() && GTModHandler.isSteam(aFluid) && aFluid.amount > 1) {
            int tSteam = (int) Math.min(
                Integer.MAX_VALUE,
                Math.min(
                    aFluid.amount / 2,
                    getBaseMetaTileEntity().getSteamCapacity() - getBaseMetaTileEntity().getStoredSteam()));
            if (tSteam > 0) {
                markDirty();
                if (doFill) getBaseMetaTileEntity().increaseStoredSteam(tSteam, true);
                return tSteam * 2;
            }
        } else {
            return fill_default(side, aFluid, doFill);
        }
        return 0;
    }

    @Override
    public void markDirty() {
        if (mBaseMetaTileEntity != null) {
            mBaseMetaTileEntity.markDirty();
        }
    }

    @Override
    public float getExplosionResistance(ForgeDirection side) {
        return 10.0F;
    }

    @Override
    public void onBlockDestroyed() {
        final IGregTechTileEntity meta = getBaseMetaTileEntity();
        if (this instanceof IConnectsToEnergyTunnel) {
            for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                final IGregTechTileEntity iGregTechTileEntity = meta.getIGregTechTileEntityAtSide(side);

                if (iGregTechTileEntity != null) {
                    if (iGregTechTileEntity.getMetaTileEntity() instanceof MTEPipeLaser neighbor
                        && neighbor.isConnectedAtSide(side.getOpposite())) {
                        neighbor.mConnections &= ~side.getOpposite().flag;
                        neighbor.connectionCount--;
                    }
                }
            }
        }
        if (this instanceof IConnectsToDataPipe) {
            for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                final IGregTechTileEntity iGregTechTileEntity = meta.getIGregTechTileEntityAtSide(side);

                if (iGregTechTileEntity != null) {
                    if (iGregTechTileEntity.getMetaTileEntity() instanceof MTEPipeData neighbor
                        && neighbor.isConnectedAtSide(side.getOpposite())) {
                        neighbor.mConnections &= ~side.getOpposite().flag;
                        neighbor.connectionCount--;
                    }
                }
            }
        }
    }

    @Override
    public void onColorChangeServer(byte aColor) {
        final IGregTechTileEntity meta = getBaseMetaTileEntity();
        final int aX = meta.getXCoord(), aY = meta.getYCoord(), aZ = meta.getZCoord();
        for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            // Flag surrounding pipes/cables to revaluate their connection with us if we got painted
            final TileEntity tTileEntity = meta.getTileEntityAtSide(side);
            if (tTileEntity instanceof BaseMetaPipeEntity pipe) {
                pipe.onNeighborBlockChange(aX, aY, aZ);
            }

            final IGregTechTileEntity iGregTechTileEntity = meta.getIGregTechTileEntityAtSide(side);
            if (iGregTechTileEntity != null) {
                if (iGregTechTileEntity.getMetaTileEntity() instanceof MTEPipeLaser pipe) pipe.updateNetwork(true);
                if (iGregTechTileEntity.getMetaTileEntity() instanceof MTEPipeData pipe) pipe.updateNetwork(true);
            }
        }
    }

    @Override
    public void onColorChangeClient(byte aColor) {
        // Do nothing apparently
    }

    @Override
    public void doExplosion(long aExplosionPower) {
        float tStrength = GTValues.getExplosionPowerForVoltage(aExplosionPower);
        final int tX = getBaseMetaTileEntity().getXCoord();
        final int tY = getBaseMetaTileEntity().getYCoord();
        final int tZ = getBaseMetaTileEntity().getZCoord();
        final World tWorld = getBaseMetaTileEntity().getWorld();
        GTUtility.sendSoundToPlayers(
            tWorld,
            SoundResource.IC2_MACHINES_MACHINE_OVERLOAD,
            1.0F,
            -1,
            tX + .5,
            tY + .5,
            tZ + .5);
        tWorld.setBlock(tX, tY, tZ, Blocks.air);
        if (GregTechAPI.sMachineExplosions) tWorld.createExplosion(null, tX + 0.5, tY + 0.5, tZ + 0.5, tStrength, true);
    }

    @Override
    public int getLightOpacity() {
        return ((BaseMetaTileEntity) getBaseMetaTileEntity()).getLightValue() > 0 ? 0 : 255;
    }

    @Override
    public boolean shouldJoinIc2Enet() {
        return false;
    }

    public boolean shouldTriggerBlockUpdate() {
        return false;
    }

    // === AE2 compat ===

    public AECableType getCableConnectionType(ForgeDirection forgeDirection) {
        return AECableType.NONE;
    }

    public AENetworkProxy getProxy() {
        return null;
    }

    public void gridChanged() {}

    @Override
    public ItemStack getMachineCraftingIcon() {
        return getStackForm(1);
    }

    // === Waila compat ===

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        currenttip.add(
            StatCollector.translateToLocalFormatted(
                "GT5U.waila.facing",
                getFacingNameLocalized(
                    mBaseMetaTileEntity.getFrontFacing()
                        .ordinal())));

        if (this instanceof IPowerChannelState state) {
            final NBTTagCompound tag = accessor.getNBTData();
            final boolean isActive = tag.getBoolean("isActive");
            final boolean isPowered = tag.getBoolean("isPowered");
            final boolean isBooting = tag.getBoolean("isBooting");
            currenttip.add(WailaText.getPowerState(isActive, isPowered, isBooting));
        }
    }

    protected static @NotNull String getFacingNameLocalized(int id) {
        if (id >= 0 && id < FACING_DIRECTION_NAMES.length) {
            return StatCollector.translateToLocal(FACING_DIRECTION_NAMES[id]);
        }
        return StatCollector.translateToLocal("GT5U.waila.facing.unknown");
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        if (this instanceof IPowerChannelState state) {
            final boolean isActive = state.isActive();
            final boolean isPowered = state.isPowered();
            final boolean isBooting = state.isBooting();
            tag.setBoolean("isActive", isActive);
            tag.setBoolean("isPowered", isPowered);
            tag.setBoolean("isBooting", isBooting);
        }
    }

    protected String getAEDiagnostics() {
        try {
            if (getProxy() == null) return StatCollector.translateToLocal("GT5U.infodata.hatch.me.diagnostics.proxy");
            if (getProxy().getNode() == null)
                return StatCollector.translateToLocal("GT5U.infodata.hatch.me.diagnostics.node");
            if (getProxy().getNode()
                .getGrid() == null) return StatCollector.translateToLocal("GT5U.infodata.hatch.me.diagnostics.grid");
            if (!getProxy().getNode()
                .meetsChannelRequirements())
                return StatCollector.translateToLocal("GT5U.infodata.hatch.me.diagnostics.channels");
            IPathingGrid pg = getProxy().getNode()
                .getGrid()
                .getCache(IPathingGrid.class);
            if (!pg.isNetworkBooting())
                return StatCollector.translateToLocal("GT5U.infodata.hatch.me.diagnostics.booting");
            IEnergyGrid eg = getProxy().getNode()
                .getGrid()
                .getCache(IEnergyGrid.class);
            if (!eg.isNetworkPowered())
                return StatCollector.translateToLocal("GT5U.infodata.hatch.me.diagnostics.power");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public void powerChangeME(MENetworkPowerStatusChange c) {}

    public void bootChangeME(MENetworkBootingStatusChange c) {}

    @Override
    public GUITextureSet getGUITextureSet() {
        return GUITextureSet.DEFAULT;
    }

    @Override
    public int getGUIColorization() {
        Dyes dye = Dyes.dyeWhite;
        if (this.colorOverride.sLoaded()) {
            if (this.colorOverride.sGuiTintingEnabled() && getBaseMetaTileEntity() != null) {
                dye = Dyes.getOrDefault(getBaseMetaTileEntity().getColorization(), Dyes.GUI_METAL);
                return this.colorOverride.getGuiTintOrDefault(dye.mName, dye.toInt());
            }
        } else if (GregTechAPI.sColoredGUI) {
            if (GregTechAPI.sMachineMetalGUI) {
                dye = Dyes.GUI_METAL;
            } else if (getBaseMetaTileEntity() != null) {
                dye = Dyes.getOrDefault(getBaseMetaTileEntity().getColorization(), Dyes.GUI_METAL);
            }
        }
        return dye.toInt();
    }

    @Override
    public int getTextColorOrDefault(String textType, int defaultColor) {
        return colorOverride.getTextColorOrDefault(textType, defaultColor);
    }

    final public byte getColor() {
        return getBaseMetaTileEntity().getColorization();
    }

    protected Supplier<Integer> COLOR_TITLE = () -> getTextColorOrDefault("title", 0x404040);
    protected Supplier<Integer> COLOR_TITLE_WHITE = () -> getTextColorOrDefault("title_white", 0xfafaff);
    protected Supplier<Integer> COLOR_TEXT_WHITE = () -> getTextColorOrDefault("text_white", 0xfafaff);
    protected Supplier<Integer> COLOR_TEXT_GRAY = () -> getTextColorOrDefault("text_gray", 0x404040);
    protected Supplier<Integer> COLOR_TEXT_RED = () -> getTextColorOrDefault("text_red", 0xff0000);

    // For MUI2 guis (which are usually built in a different class).
    public int getTitleColor() {
        return COLOR_TITLE.get();
    }

    public int getColorTitleWhite() {
        return COLOR_TITLE_WHITE.get();
    }

    public int getColorTextWhite() {
        return COLOR_TEXT_WHITE.get();
    }

    public int getColorTextGray() {
        return COLOR_TEXT_GRAY.get();
    }

    public int getColorTextRed() {
        return COLOR_TEXT_RED.get();
    }
}

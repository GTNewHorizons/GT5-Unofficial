package gregtech.common.tileentities.machines.basic;

import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE_OUT;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SIDE_JUKEBOX;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_JUKEBOX;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;

import org.joml.Vector4i;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizons.modularui.api.drawable.FallbackableUITexture;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.CycleButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;
import com.gtnewhorizons.modularui.common.widget.SliderWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import appeng.api.implementations.tiles.ISoundP2PHandler;
import appeng.me.GridAccessException;
import appeng.me.cache.helpers.TunnelCollection;
import appeng.me.helpers.AENetworkProxy;
import appeng.parts.p2p.PartP2PSound;
import gregtech.api.enums.GTVoltageIndex;
import gregtech.api.enums.GT_Values;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.recipe.BasicUIProperties;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_MusicSystem;
import gregtech.common.gui.modularui.UIHelper;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class GT_MetaTileEntity_BetterJukebox extends GT_MetaTileEntity_BasicMachine
    implements IAddUIWidgets, ISoundP2PHandler {

    // Stored state
    public UUID jukeboxUuid = UNSET_UUID;
    public boolean loopMode = true;
    public boolean shuffleMode = false;
    public int playbackSlot = 0;
    public float playbackVolume = BalanceMath.VANILLA_JUKEBOX_RANGE;
    public float p2pVolume = BalanceMath.VANILLA_JUKEBOX_RANGE;
    public long discProgressMs = 0;
    /** Makes all music discs play for 4 seconds */
    public boolean superFastDebugMode = false;
    // Computed state
    private final Vector4i interdimPositionCache = new Vector4i(); // XYZ, Dimension ID
    private GT_MusicSystem.MusicSource musicSource = null;
    private boolean powered = false;
    private long discStartMs = 0;
    public long discDurationMs = 1;
    private ItemRecord currentlyPlaying = null;

    // Constants
    public static final UUID UNSET_UUID = UUID.nameUUIDFromBytes(new byte[] { 0 });
    public static final int INPUT_SLOTS = 21;
    private static final Random SHUFFLER = new Random();

    public enum HeadphoneLimit {

        BLOCK_RANGE,
        INSIDE_DIMENSION,
        BETWEEN_DIMENSIONS;

        public static final ImmutableList<HeadphoneLimit> ENTRIES = ImmutableList.copyOf(values());
    }

    public static final class BalanceMath {

        public static int MAX_TIER = GTVoltageIndex.IV;
        public static float VANILLA_JUKEBOX_RANGE = 4.0f; // 64 blocks

        private static final float[] LISTENING_VOLUME = new float[] { //
            VANILLA_JUKEBOX_RANGE, // ULV (unpowered fallback)
            VANILLA_JUKEBOX_RANGE + 1.0f, // LV, 80 blocks
            VANILLA_JUKEBOX_RANGE + 2.0f, // MV, 96 blocks
            VANILLA_JUKEBOX_RANGE + 4.0f, // HV, 118 blocks
            VANILLA_JUKEBOX_RANGE + 5.0f, // EV, 144 blocks
            VANILLA_JUKEBOX_RANGE + 6.0f, // IV, 160 blocks, equivalent to default load distance of 10 chunks
        };

        private static final int[] HEADPHONE_BLOCK_RANGE = new int[] { //
            64, // ULV (unpowered fallback)
            128, // LV
            160, // MV
            320, // HV
            9001, // EV, alreadu unlimited here - this value is ignored
            9002, // IV, already unlimited here - this value is ignored
        };

        public static float listeningVolume(int tier) {
            tier = MathHelper.clamp_int(tier, 0, MAX_TIER);
            return LISTENING_VOLUME[tier];
        }

        public static int headphoneBlockRange(int tier) {
            tier = MathHelper.clamp_int(tier, 0, MAX_TIER);
            return HEADPHONE_BLOCK_RANGE[tier];
        }

        public static HeadphoneLimit headphoneLimit(int tier) {
            if (tier <= GTVoltageIndex.HV) {
                return HeadphoneLimit.BLOCK_RANGE;
            } else if (tier == GTVoltageIndex.EV) {
                return HeadphoneLimit.INSIDE_DIMENSION;
            } else {
                return HeadphoneLimit.BETWEEN_DIMENSIONS;
            }
        }

        public static float volumeToAttenuationDistance(float range) {
            // SoundManager.playSound logic
            return 16.0f * range;
        }

        public static float attenuationDistanceToVolume(float blockRange) {
            return blockRange / 16.0f;
        }

        public static long eutUsage(int tier) {
            tier = MathHelper.clamp_int(tier, 0, MAX_TIER);
            return V[tier] / 16;
        }
    }

    private static String[] buildDescription(int aTier) {
        ArrayList<String> strings = new ArrayList<>(4);
        strings.add("Plays music better than your average vanilla jukebox.");
        if (BalanceMath.headphoneLimit(aTier) != HeadphoneLimit.BLOCK_RANGE) {
            strings.add(EnumChatFormatting.BLUE + "The raw power of Hatsune Miku in your ears");
        }
        strings.add(
            String.format(
                "Range: %s%.1f blocks",
                EnumChatFormatting.WHITE,
                BalanceMath.volumeToAttenuationDistance(BalanceMath.listeningVolume(aTier))));
        strings.add(switch (BalanceMath.headphoneLimit(aTier)) {
            case BLOCK_RANGE -> String.format(
                "Headphone signal range: %s%d blocks",
                EnumChatFormatting.WHITE,
                BalanceMath.headphoneBlockRange(aTier));
            case INSIDE_DIMENSION -> String
                .format("Headphones work anywhere in %sthe same dimension", EnumChatFormatting.WHITE);
            case BETWEEN_DIMENSIONS -> String
                .format("Headphones work anywhere, in %sany dimension", EnumChatFormatting.WHITE);
        });
        strings.add(String.format("Cost: %s%d EU/t", EnumChatFormatting.WHITE, BalanceMath.eutUsage(aTier)));
        strings.add(GT_Values.AuthorEigenRaven);
        return strings.toArray(new String[0]);
    }

    public GT_MetaTileEntity_BetterJukebox(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 1, buildDescription(aTier), INPUT_SLOTS, 1);
        playbackVolume = BalanceMath.listeningVolume(aTier);
    }

    public GT_MetaTileEntity_BetterJukebox(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 1, aDescription, aTextures, INPUT_SLOTS, 1);
        playbackVolume = BalanceMath.listeningVolume(aTier);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_BetterJukebox(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        if (sideDirection == baseMetaTileEntity.getFrontFacing()) {
            return new ITexture[] { MACHINE_CASINGS[mTier][colorIndex + 1], TextureFactory.of(OVERLAY_PIPE_OUT) };
        }
        if (sideDirection != ForgeDirection.UP) {
            return new ITexture[] { MACHINE_CASINGS[mTier][colorIndex + 1], TextureFactory.of(OVERLAY_SIDE_JUKEBOX) };
        }
        return new ITexture[] { MACHINE_CASINGS[mTier][colorIndex + 1], TextureFactory.builder()
            .addIcon(OVERLAY_TOP_JUKEBOX)
            .extFacing()
            .build() };
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        if (aBaseMetaTileEntity.isClientSide()) {
            return;
        }
        final Vector4i interdimPosition = interdimPositionCache;
        interdimPosition.x = aBaseMetaTileEntity.getXCoord();
        interdimPosition.y = aBaseMetaTileEntity.getYCoord();
        interdimPosition.z = aBaseMetaTileEntity.getZCoord();
        interdimPosition.w = aBaseMetaTileEntity.getWorld().provider.dimensionId;
        if (jukeboxUuid == UNSET_UUID) {
            jukeboxUuid = UUID.randomUUID();
            markDirty();
        }
        if (musicSource == null) {
            musicSource = GT_MusicSystem.ServerSystem.registerOrGetMusicSource(jukeboxUuid);
            musicSource.originPosition.set(interdimPosition);
            musicSource.headphoneLimit = BalanceMath.headphoneLimit(mTier);
            musicSource.headphoneBlockRange = BalanceMath.headphoneBlockRange(mTier);
            musicSource.startedPlayingAtMs = System.currentTimeMillis();
            updateEmitterList();
        }
        if (doesSlotContainValidRecord(playbackSlot)
            && mInventory[playbackSlot].getItem() instanceof ItemRecord record) {
            final ResourceLocation resource = record.getRecordResource(record.recordName);
            currentlyPlaying = record;
            // Assume a safe disc duration of 500 seconds if not known in the registry
            discDurationMs = GT_MusicSystem.getMusicRecordDurations()
                .getOrDefault(resource, 500_000);
            discStartMs = System.currentTimeMillis() - discProgressMs;
            musicSource.setRecord(
                new ResourceLocation(resource.getResourceDomain(), "records." + resource.getResourcePath()),
                discProgressMs);
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        try {
            if (aBaseMetaTileEntity.isClientSide() || !aBaseMetaTileEntity.isAllowedToWork() || musicSource == null) {
                if (currentlyPlaying != null) {
                    stopCurrentSong(System.currentTimeMillis());
                }
                return;
            }
            final Vector4i interdimPosition = interdimPositionCache;
            interdimPosition.x = aBaseMetaTileEntity.getXCoord();
            interdimPosition.y = aBaseMetaTileEntity.getYCoord();
            interdimPosition.z = aBaseMetaTileEntity.getZCoord();
            interdimPosition.w = aBaseMetaTileEntity.getWorld().provider.dimensionId;
            final long now = System.currentTimeMillis();

            if (superFastDebugMode && discDurationMs > 4000) {
                discDurationMs = 4000;
            }

            // power check
            final boolean hasMinimumEU = aBaseMetaTileEntity.isUniversalEnergyStored(getMinimumStoredEU());
            if (currentlyPlaying != null && hasMinimumEU
                && aBaseMetaTileEntity.decreaseStoredEnergyUnits(BalanceMath.eutUsage(mTier), false)) {
                if (!powered) { // just got power again
                    powered = true;
                    musicSource.modified = true;
                    musicSource.headphoneLimit = BalanceMath.headphoneLimit(mTier);
                    musicSource.headphoneBlockRange = BalanceMath.headphoneBlockRange(mTier);
                    updateEmitterList();
                }
            } else if ((!hasMinimumEU || currentlyPlaying != null) && powered) { // was powered, but no longer is
                powered = false;
                musicSource.modified = true;
                musicSource.headphoneLimit = HeadphoneLimit.BLOCK_RANGE;
                musicSource.headphoneBlockRange = BalanceMath.headphoneBlockRange(0);
                updateEmitterList();
            }

            // check if current disc finished
            if (currentlyPlaying != null) {
                discProgressMs = now - discStartMs;
                final boolean hasValidRecord = doesSlotContainValidRecord(playbackSlot);
                final boolean wasDiscSwapped = hasValidRecord
                    && mInventory[getInputSlot() + playbackSlot].getItem() != currentlyPlaying;
                if (discProgressMs >= discDurationMs || !hasValidRecord || wasDiscSwapped) {
                    stopCurrentSong(now);
                    if (!loopMode) {
                        // should be empty, but swap just in case it's not
                        final ItemStack oldOut = mInventory[getOutputSlot()];
                        mInventory[getOutputSlot()] = mInventory[getInputSlot() + playbackSlot];
                        mInventory[getInputSlot() + playbackSlot] = oldOut;
                        markDirty();
                    }
                    if (!(hasValidRecord && wasDiscSwapped)) {
                        // don't switch slots if someone just put a new disc in the active slot
                        pickNextSlot();
                    }
                } else {
                    // keep on playing
                    return;
                }
            }

            if (playbackSlot < 0 || playbackSlot >= INPUT_SLOTS
                || ((aTimer % 10) == 0 && !doesSlotContainValidRecord(playbackSlot))) {
                pickNextSlot();
            }

            final boolean hasValidRecord = doesSlotContainValidRecord(playbackSlot);
            final boolean canStartPlaying = loopMode || isOutputEmpty();
            if (!hasValidRecord) {
                stopCurrentSong(now);
            } else if (canStartPlaying
                && mInventory[getInputSlot() + playbackSlot].getItem() instanceof ItemRecord record) {
                    final ResourceLocation resource = record.getRecordResource(record.recordName);
                    currentlyPlaying = record;
                    musicSource.setRecord(
                        new ResourceLocation(resource.getResourceDomain(), "records." + resource.getResourcePath()));
                    // Assume a safe disc duration of 500 seconds if not known in the registry
                    discDurationMs = GT_MusicSystem.getMusicRecordDurations()
                        .getOrDefault(resource, 500_000);
                    discProgressMs = 0;
                    discStartMs = now;
                }
        } finally {
            super.onPostTick(aBaseMetaTileEntity, aTimer);
        }
    }

    private void stopCurrentSong(long nowMs) {
        if (currentlyPlaying == null) {
            return;
        }
        musicSource.setRecord(null);
        currentlyPlaying = null;
        discDurationMs = 1;
        discProgressMs = 0;
        discStartMs = nowMs;
        markDirty();
    }

    private void pickNextSlot() {
        playbackSlot = MathHelper.clamp_int(playbackSlot, 0, INPUT_SLOTS);
        if (shuffleMode) {
            final int[] validSlots = new int[INPUT_SLOTS];
            int validSlotCount = 0;
            for (int i = 0; i < INPUT_SLOTS; i++) {
                if (i != playbackSlot && doesSlotContainValidRecord(i)) {
                    validSlots[validSlotCount++] = i;
                }
            }
            switch (validSlotCount) {
                case 0 -> {}
                case 1 -> {
                    playbackSlot = validSlots[0];
                }
                default -> {
                    playbackSlot = validSlots[SHUFFLER.nextInt(validSlotCount)];
                }
            }
        } else {
            int attempt = 0;
            int nextSlot = playbackSlot;
            do {
                attempt++;
                nextSlot = (nextSlot + 1) % INPUT_SLOTS;
            } while (!doesSlotContainValidRecord(nextSlot) && attempt <= INPUT_SLOTS);
            if (attempt <= INPUT_SLOTS) {
                playbackSlot = nextSlot;
            }
        }
    }

    public boolean doesSlotContainValidRecord(int slot) {
        return mInventory[getInputSlot() + slot] != null
            && mInventory[getInputSlot() + slot].getItem() instanceof ItemRecord;
    }

    @Override
    public void onRemoval() {
        final IGregTechTileEntity baseTE = getBaseMetaTileEntity();
        if (baseTE == null) {
            return;
        }
        if (!baseTE.isServerSide()) {
            return;
        }
        if (jukeboxUuid == UNSET_UUID) {
            return;
        }
        GT_MusicSystem.ServerSystem.removeMusicSource(jukeboxUuid);
    }

    @Override
    public long getMinimumStoredEU() {
        return BalanceMath.eutUsage(mTier) * 20;
    }

    @Override
    public long maxEUStore() {
        return 512L + BalanceMath.eutUsage(mTier) * 50;
    }

    @Override
    public long maxAmperesIn() {
        return 1;
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        return null;
    }

    public static final String NBTKEY_UUID_LOW = "jukeboxUUIDLow";
    public static final String NBTKEY_UUID_HIGH = "jukeboxUUIDHigh";
    public static final String NBTKEY_LOOP_MODE = "loopMode";
    public static final String NBTKEY_SHUFFLE_MODE = "shuffleMode";
    public static final String NBTKEY_PLAYBACK_SLOT = "playbackSlot";
    public static final String NBTKEY_VOLUME_PLAY = "playbackVolume";
    public static final String NBTKEY_VOLUME_P2P = "p2pVolume";
    public static final String NBTKEY_DISC_PROGRESS_MS = "discProgressMs";

    @Override
    public String[] getInfoData() {
        return new String[] { "Jukebox UUID: " + ((jukeboxUuid == UNSET_UUID) ? "unset" : jukeboxUuid),
            "Loop mode: " + loopMode, "Shuffle mode: " + shuffleMode, "Played the disc for [ms]: " + discProgressMs,
            "Current disc duration [ms]: " + discDurationMs,
            "Playback range [blocks]: " + BalanceMath.volumeToAttenuationDistance(playbackVolume),
            "P2P range [blocks]: " + BalanceMath.volumeToAttenuationDistance(playbackVolume),
            "Raw playback strength: " + playbackVolume, "Raw p2p strength: " + p2pVolume };
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        if (jukeboxUuid != UNSET_UUID) {
            aNBT.setLong(NBTKEY_UUID_LOW, jukeboxUuid.getLeastSignificantBits());
            aNBT.setLong(NBTKEY_UUID_HIGH, jukeboxUuid.getMostSignificantBits());
            aNBT.setBoolean(NBTKEY_LOOP_MODE, loopMode);
            aNBT.setBoolean(NBTKEY_SHUFFLE_MODE, shuffleMode);
            aNBT.setInteger(NBTKEY_PLAYBACK_SLOT, playbackSlot);
            aNBT.setFloat(NBTKEY_VOLUME_PLAY, playbackVolume);
            aNBT.setFloat(NBTKEY_VOLUME_P2P, p2pVolume);
            aNBT.setLong(NBTKEY_DISC_PROGRESS_MS, discProgressMs);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (aNBT.hasKey(NBTKEY_UUID_LOW, Constants.NBT.TAG_ANY_NUMERIC)
            && aNBT.hasKey(NBTKEY_UUID_HIGH, Constants.NBT.TAG_ANY_NUMERIC)) {
            jukeboxUuid = new UUID(aNBT.getLong(NBTKEY_UUID_HIGH), aNBT.getLong(NBTKEY_UUID_LOW));
        }
        if (aNBT.hasKey(NBTKEY_LOOP_MODE, Constants.NBT.TAG_ANY_NUMERIC)) {
            loopMode = aNBT.getBoolean(NBTKEY_LOOP_MODE);
        }
        if (aNBT.hasKey(NBTKEY_SHUFFLE_MODE, Constants.NBT.TAG_ANY_NUMERIC)) {
            shuffleMode = aNBT.getBoolean(NBTKEY_SHUFFLE_MODE);
        }
        if (aNBT.hasKey(NBTKEY_PLAYBACK_SLOT, Constants.NBT.TAG_ANY_NUMERIC)) {
            playbackSlot = aNBT.getInteger(NBTKEY_PLAYBACK_SLOT);
        }
        if (aNBT.hasKey(NBTKEY_VOLUME_PLAY, Constants.NBT.TAG_ANY_NUMERIC)) {
            playbackVolume = aNBT.getFloat(NBTKEY_VOLUME_PLAY);
        }
        if (aNBT.hasKey(NBTKEY_VOLUME_P2P, Constants.NBT.TAG_ANY_NUMERIC)) {
            p2pVolume = aNBT.getFloat(NBTKEY_VOLUME_P2P);
        }
        if (aNBT.hasKey(NBTKEY_DISC_PROGRESS_MS, Constants.NBT.TAG_ANY_NUMERIC)) {
            discProgressMs = aNBT.getLong(NBTKEY_DISC_PROGRESS_MS);
        }

        final float maxVolume = BalanceMath.listeningVolume(mTier);
        playbackVolume = MathHelper.clamp_float(playbackVolume, 0.0f, maxVolume);
        p2pVolume = MathHelper.clamp_float(p2pVolume, 0.0f, maxVolume);
    }

    @Override
    protected BasicUIProperties getUIProperties() {
        return super.getUIProperties().toBuilder()
            .itemInputPositionsGetter(count -> UIHelper.getGridPositions(count, 7, 6, 7, 3))
            .itemOutputPositionsGetter(count -> UIHelper.getGridPositions(count, 153, 24, 1))
            .specialItemPositionGetter(() -> new Pos2d(115, 62))
            .progressBarPos(Pos2d.cartesian(133, 24))
            .progressBarTexture(new FallbackableUITexture(GT_UITextures.PROGRESSBAR_ARROW))
            .build();
    }

    @Override
    protected void addProgressBar(ModularWindow.Builder builder, BasicUIProperties uiProperties) {
        builder.widget(
            setNEITransferRect(
                new ProgressBar().setProgress(() -> discProgressMs / (float) Math.max(1, discDurationMs))
                    .setTexture(uiProperties.progressBarTexture.get(), uiProperties.progressBarImageSize)
                    .setDirection(uiProperties.progressBarDirection)
                    .setPos(uiProperties.progressBarPos)
                    .setSize(uiProperties.progressBarSize)
                    .setUpdateTooltipEveryTick(true)
                    .attachSyncer(
                        new FakeSyncWidget.LongSyncer(() -> this.discProgressMs, val -> this.discProgressMs = val),
                        builder)
                    .attachSyncer(
                        new FakeSyncWidget.LongSyncer(() -> this.discDurationMs, val -> this.discDurationMs = val),
                        builder)
                    .dynamicTooltip(
                        () -> Collections.singletonList(
                            String.format("%,.2f / %,.2f", discProgressMs / 1000.0f, discDurationMs / 1000.0f))),
                uiProperties.neiTransferRectId));
        addProgressBarSpecialTextures(builder, uiProperties);
    }

    @Override
    protected SlotWidget createChargerSlot(int x, int y) {
        return super.createChargerSlot(97, 62);
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);
        final BasicUIProperties props = getUIProperties();
        final List<Pos2d> inputSlots = props.itemInputPositionsGetter.apply(mInputSlotCount);
        // Loop
        builder.widget(
            new CycleButtonWidget().setToggle(() -> loopMode, val -> loopMode = val)
                .setStaticTexture(GT_UITextures.OVERLAY_BUTTON_CYCLIC)
                .setVariableBackground(GT_UITextures.BUTTON_STANDARD_TOGGLE)
                .setGTTooltip(() -> mTooltipCache.getData("GT5U.machines.betterjukebox.loop.tooltip"))
                .setTooltipShowUpDelay(TOOLTIP_DELAY)
                .setPos(153, 6)
                .setSize(18, 18));
        // Shuffle
        builder.widget(new CycleButtonWidget().setToggle(() -> shuffleMode, val -> {
            shuffleMode = val;
            if (shuffleMode) {
                playbackSlot = -1;
            } else {
                playbackSlot = 0;
            }
        })
            .setStaticTexture(GT_UITextures.OVERLAY_BUTTON_SHUFFLE)
            .setVariableBackground(GT_UITextures.BUTTON_STANDARD_TOGGLE)
            .setGTTooltip(() -> mTooltipCache.getData("GT5U.machines.betterjukebox.shuffle.tooltip"))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(153, 42)
            .setSize(18, 18));
        // Currently playing slot highlight using the hotbar active texture
        final DrawableWidget slotHighlight = new DrawableWidget();
        builder.widget(
            slotHighlight
                .setDrawable(
                    new UITexture(
                        new ResourceLocation("minecraft", "textures/gui/widgets.png"),
                        0.0f,
                        22.0f / 256.0f,
                        24.0f / 256.0f,
                        46.0f / 256.0f))
                .setSize(24, 24)
                .attachSyncer(new FakeSyncWidget.IntegerSyncer(() -> this.playbackSlot, val -> {
                    this.playbackSlot = val;
                    slotHighlight.checkNeedsRebuild();
                }), builder)
                .setPosProvider(
                    (screenSize, window, parent) -> inputSlots.get(MathHelper.clamp_int(playbackSlot, 0, INPUT_SLOTS))
                        .add(-3, -3)));
        // Attenuation distance (controls internal "volume")
        // Caching tooltip data caches the formatted p2p range value, so we have to use the uncached variant here.
        builder.widget(
            new SliderWidget()
                .setBounds(0.0f, BalanceMath.volumeToAttenuationDistance(BalanceMath.listeningVolume(mTier)))
                .setGetter(this::getPlaybackBlockRange)
                .setSetter(this::setPlaybackBlockRange)
                .dynamicTooltip(
                    () -> mTooltipCache.getUncachedTooltipData(
                        "GT5U.machines.betterjukebox.attenuationDistance.tooltip",
                        (int) getPlaybackBlockRange()).text)
                .setUpdateTooltipEveryTick(true)
                .setPos(44, 63)
                .setSize(52, 8));
        builder.widget(
            new SliderWidget()
                .setBounds(0.0f, BalanceMath.volumeToAttenuationDistance(BalanceMath.listeningVolume(mTier)))
                .setGetter(this::getP2PBlockRange)
                .setSetter(this::setP2PBlockRange)
                .dynamicTooltip(
                    () -> mTooltipCache.getUncachedTooltipData(
                        "GT5U.machines.betterjukebox.p2pAttenuationDistance.tooltip",
                        (int) getP2PBlockRange()).text)
                .setUpdateTooltipEveryTick(true)
                .setPos(44, 71)
                .setSize(52, 8));
    }

    private float getPlaybackBlockRange() {
        return BalanceMath.volumeToAttenuationDistance(playbackVolume);
    }

    private float getP2PBlockRange() {
        return BalanceMath.volumeToAttenuationDistance(p2pVolume);
    }

    private void setPlaybackBlockRange(float blockRange) {
        float volume = BalanceMath.attenuationDistanceToVolume(blockRange);
        volume = MathHelper.clamp_float(volume, 0.0f, BalanceMath.listeningVolume(mTier));
        if (volume != playbackVolume) {
            playbackVolume = volume;
            if (getBaseMetaTileEntity().isServerSide()) {
                updateEmitterList();
            }
        }
    }

    private void setP2PBlockRange(float blockRange) {
        float volume = BalanceMath.attenuationDistanceToVolume(blockRange);
        volume = MathHelper.clamp_float(volume, 0.0f, BalanceMath.listeningVolume(mTier));
        if (volume != p2pVolume) {
            p2pVolume = volume;
            if (getBaseMetaTileEntity().isServerSide()) {
                updateEmitterList();
            }
        }
    }

    private final EnumMap<ForgeDirection, PartP2PSound> attachedSoundP2P = new EnumMap<>(ForgeDirection.class);
    private final ObjectArrayList<PartP2PSound> combinedOutputsListCache = new ObjectArrayList<>(new PartP2PSound[0]);

    private void updateEmitterList() {
        final GT_MusicSystem.MusicSource target = musicSource;
        if (target == null) {
            return;
        }
        final ObjectArrayList<PartP2PSound> emitters = combinedOutputsListCache;
        emitters.clear();

        attachedSoundP2P.forEach((ignored, p2p) -> {
            if (p2p != null) {
                try {
                    p2p.getOutputs()
                        .forEach(emitters::add);
                } catch (GridAccessException e) {
                    // skip
                }
            }
        });

        IGregTechTileEntity te = getBaseMetaTileEntity();
        if (te == null) {
            return;
        }
        final Vector4i position = new Vector4i();
        target.resizeEmitterArray(1 + emitters.size());
        position.set(te.getXCoord(), te.getYCoord(), te.getZCoord(), te.getWorld().provider.dimensionId);
        final float actualVolume = MathHelper
            .clamp_float(playbackVolume, 0.0f, BalanceMath.listeningVolume(powered ? mTier : 0));
        target.setEmitter(0, position, actualVolume);
        final float actualP2PVolume = MathHelper
            .clamp_float(p2pVolume, 0.0f, powered ? BalanceMath.listeningVolume(mTier) : 0.0f);
        for (int i = 0; i < emitters.size(); i++) {
            final PartP2PSound p2p = emitters.get(i);
            final AENetworkProxy proxy = p2p.getProxy();
            final TileEntity emitterTe = p2p.getTile();
            final ForgeDirection dir = p2p.getSide();
            position.set(
                emitterTe.xCoord + dir.offsetX,
                emitterTe.yCoord + dir.offsetY,
                emitterTe.zCoord + dir.offsetZ,
                emitterTe.getWorldObj().provider.dimensionId);
            final boolean active = proxy.isActive();
            target.setEmitter(1 + i, position, active ? actualP2PVolume : 0);
        }
    }

    @Override
    public boolean allowSoundProxying(PartP2PSound p2p) {
        return false; // the jukebox proxies sounds by itself
    }

    @Override
    public void onSoundP2PAttach(PartP2PSound p2p) {
        attachedSoundP2P.put(p2p.getSide(), p2p);
        updateEmitterList();
    }

    @Override
    public void onSoundP2PDetach(PartP2PSound p2p) {
        attachedSoundP2P.put(p2p.getSide(), null);
        updateEmitterList();
    }

    @Override
    public void onSoundP2POutputUpdate(PartP2PSound p2p, TunnelCollection<PartP2PSound> outputs) {
        updateEmitterList();
    }

}

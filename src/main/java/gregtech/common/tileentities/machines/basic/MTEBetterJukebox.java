package gregtech.common.tileentities.machines.basic;

import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE_OUT;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SIDE_JUKEBOX;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_JUKEBOX;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Random;
import java.util.UUID;

import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4i;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.gtnhlib.api.MusicRecordMetadataProvider;

import appeng.api.implementations.tiles.ISoundP2PHandler;
import appeng.me.GridAccessException;
import appeng.me.cache.helpers.TunnelCollection;
import appeng.me.helpers.AENetworkProxy;
import appeng.parts.p2p.PartP2PSound;
import gregtech.api.enums.GTAuthors;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicMachine;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTMusicSystem;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.hatch.MTEBetterJukeboxGui;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

@IMetaTileEntity.SkipGenerateDescription
public class MTEBetterJukebox extends MTEBasicMachine implements ISoundP2PHandler {

    // Stored state
    public UUID jukeboxUuid = UNSET_UUID;
    public boolean loopMode = true;
    public boolean shuffleMode = false;
    private int playbackSlot = 0;
    public double playbackVolume;
    public double p2pVolume = BalanceMath.VANILLA_JUKEBOX_RANGE;
    private long discProgressMs = 0;
    /** Makes all music discs play for 4 seconds */
    public boolean superFastDebugMode = false;
    // Computed state
    private final Vector4i interdimPositionCache = new Vector4i(); // XYZ, Dimension ID
    private GTMusicSystem.MusicSource musicSource = null;
    private boolean powered = false;
    private long discStartMs = 0;
    private long discDurationMs = 1;
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

        public static int MAX_TIER = VoltageIndex.IV;
        public static double VANILLA_JUKEBOX_RANGE = 4.0f; // 64 blocks

        private static final double[] LISTENING_VOLUME = new double[] { //
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

        public static double listeningVolume(int tier) {
            tier = Math.clamp(tier, 0, MAX_TIER);
            return LISTENING_VOLUME[tier];
        }

        public static int headphoneBlockRange(int tier) {
            tier = Math.clamp(tier, 0, MAX_TIER);
            return HEADPHONE_BLOCK_RANGE[tier];
        }

        public static HeadphoneLimit headphoneLimit(int tier) {
            if (tier <= VoltageIndex.HV) {
                return HeadphoneLimit.BLOCK_RANGE;
            } else if (tier == VoltageIndex.EV) {
                return HeadphoneLimit.INSIDE_DIMENSION;
            } else {
                return HeadphoneLimit.BETWEEN_DIMENSIONS;
            }
        }

        public static double volumeToAttenuationDistance(double range) {
            // SoundManager.playSound logic
            return 16 * range;
        }

        public static double attenuationDistanceToVolume(double blockRange) {
            return blockRange / 16;
        }

        public static long eutUsage(int tier) {
            tier = Math.clamp(tier, 0, MAX_TIER);
            return V[tier] / 16;
        }
    }

    private static String[] buildDescription(int aTier) {
        ArrayList<String> strings = new ArrayList<>(4);
        strings.add(GTUtility.translate("gt.blockmachines.basicmachine.betterjukebox.tooltip.better_than_vanilla"));
        if (BalanceMath.headphoneLimit(aTier) != HeadphoneLimit.BLOCK_RANGE) {
            strings.add(
                EnumChatFormatting.BLUE
                    + GTUtility.translate("gt.blockmachines.basicmachine.betterjukebox.tooltip.hatsune_miku_power"));
        }
        strings.add(
            String.format(
                GTUtility.translate("gt.blockmachines.basicmachine.betterjukebox.tooltip.range"),
                EnumChatFormatting.WHITE,
                BalanceMath.volumeToAttenuationDistance(BalanceMath.listeningVolume(aTier))));
        strings.add(switch (BalanceMath.headphoneLimit(aTier)) {
            case BLOCK_RANGE -> String.format(
                GTUtility.translate("gt.blockmachines.basicmachine.betterjukebox.tooltip.headphone_range_blocks"),
                EnumChatFormatting.WHITE,
                BalanceMath.headphoneBlockRange(aTier));
            case INSIDE_DIMENSION -> String.format(
                GTUtility
                    .translate("gt.blockmachines.basicmachine.betterjukebox.tooltip.headphone_range_same_dimension"),
                EnumChatFormatting.WHITE);
            case BETWEEN_DIMENSIONS -> String.format(
                GTUtility
                    .translate("gt.blockmachines.basicmachine.betterjukebox.tooltip.headphone_range_any_dimension"),
                EnumChatFormatting.WHITE);
        });
        strings.add(
            String.format(
                GTUtility.translate("gt.blockmachines.basicmachine.betterjukebox.tooltip.cost"),
                EnumChatFormatting.WHITE,
                BalanceMath.eutUsage(aTier)));
        strings.add(GTAuthors.buildAuthorsWithFormat(GTAuthors.AuthorEigenRaven));
        return strings.toArray(new String[0]);
    }

    public MTEBetterJukebox(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 1, buildDescription(aTier), INPUT_SLOTS, 1);
        playbackVolume = BalanceMath.listeningVolume(aTier);
    }

    public MTEBetterJukebox(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 1, aDescription, aTextures, INPUT_SLOTS, 1);
        playbackVolume = BalanceMath.listeningVolume(aTier);
    }

    public boolean isLoopMode() {
        return loopMode;
    }

    public void setLoopMode(boolean loopMode) {
        this.loopMode = loopMode;
    }

    public boolean isShuffleMode() {
        return shuffleMode;
    }

    public void setShuffleMode(boolean shuffleMode) {
        this.shuffleMode = shuffleMode;
    }

    public long getDiscProgressMs() {
        return discProgressMs;
    }

    public long getDiscDurationMs() {
        return discDurationMs;
    }

    public int getPlaybackSlot() {
        return playbackSlot;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEBetterJukebox(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Nullable
    @Override
    public <T> T getCapability(@NotNull Class<T> capability, @NotNull ForgeDirection side) {
        if (capability == ISoundP2PHandler.class) {
            return capability.cast(this);
        }
        return super.getCapability(capability, side);
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
            musicSource = GTMusicSystem.ServerSystem.registerOrGetMusicSource(jukeboxUuid);
            musicSource.originPosition.set(interdimPosition);
            musicSource.headphoneLimit = BalanceMath.headphoneLimit(mTier);
            musicSource.headphoneBlockRange = BalanceMath.headphoneBlockRange(mTier);
            musicSource.startedPlayingAtMs = System.currentTimeMillis();
            updateEmitterList();
        }
        if (doesSlotContainValidRecord(playbackSlot)
            && mInventory[getInputSlot() + playbackSlot].getItem() instanceof ItemRecord record) {
            final ResourceLocation resource, playPath;
            if (record instanceof MusicRecordMetadataProvider mrmp) {
                resource = mrmp.getMusicRecordResource(mInventory[getInputSlot() + playbackSlot]);
            } else {
                resource = record.getRecordResource("records." + record.recordName);
            }
            playPath = resource;
            currentlyPlaying = record;
            // Assume a safe disc duration of 500 seconds if not known in the registry
            discDurationMs = GTMusicSystem.getMusicRecordDurations()
                .getOrDefault(resource, 500_000);
            discStartMs = System.currentTimeMillis() - discProgressMs;
            musicSource.setRecord(playPath, discProgressMs);
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
                    final ResourceLocation resource, playPath;
                    if (record instanceof MusicRecordMetadataProvider mrmp) {
                        resource = mrmp.getMusicRecordResource(mInventory[getInputSlot() + playbackSlot]);
                    } else {
                        resource = record.getRecordResource("records." + record.recordName);
                    }
                    playPath = resource;
                    currentlyPlaying = record;
                    musicSource.setRecord(playPath);
                    // Assume a safe disc duration of 500 seconds if not known in the registry
                    discDurationMs = GTMusicSystem.getMusicRecordDurations()
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
        playbackSlot = Math.clamp(playbackSlot, 0, INPUT_SLOTS);
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
        GTMusicSystem.ServerSystem.removeMusicSource(jukeboxUuid);
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
        return new String[] {
            GTUtility.translate(
                "GT5U.infodata.juke_box.uuid",
                (jukeboxUuid == UNSET_UUID) ? GTUtility.translate("GT5U.infodata.juke_box.uuid.unset") : jukeboxUuid),
            GTUtility.translate("GT5U.infodata.juke_box.loop_mode", loopMode),
            GTUtility.translate("GT5U.infodata.juke_box.shuffle_mode", shuffleMode),
            GTUtility.translate("GT5U.infodata.juke_box.played", discProgressMs),
            GTUtility.translate("GT5U.infodata.juke_box.current", discDurationMs),
            GTUtility.translate(
                "GT5U.infodata.juke_box.playback_range",
                BalanceMath.volumeToAttenuationDistance(playbackVolume)),
            GTUtility
                .translate("GT5U.infodata.juke_box.p2p_range", BalanceMath.volumeToAttenuationDistance(playbackVolume)),
            GTUtility.translate("GT5U.infodata.juke_box.raw_playback_strength", playbackVolume),
            GTUtility.translate("GT5U.infodata.juke_box.raw_p2p_strength", p2pVolume) };
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
            aNBT.setDouble(NBTKEY_VOLUME_PLAY, playbackVolume);
            aNBT.setDouble(NBTKEY_VOLUME_P2P, p2pVolume);
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
            playbackVolume = aNBT.getDouble(NBTKEY_VOLUME_PLAY);
        }
        if (aNBT.hasKey(NBTKEY_VOLUME_P2P, Constants.NBT.TAG_ANY_NUMERIC)) {
            p2pVolume = aNBT.getDouble(NBTKEY_VOLUME_P2P);
        }
        if (aNBT.hasKey(NBTKEY_DISC_PROGRESS_MS, Constants.NBT.TAG_ANY_NUMERIC)) {
            discProgressMs = aNBT.getLong(NBTKEY_DISC_PROGRESS_MS);
        }

        final double maxVolume = BalanceMath.listeningVolume(mTier);
        playbackVolume = Math.clamp(playbackVolume, 0.0f, maxVolume);
        p2pVolume = Math.clamp(p2pVolume, 0.0f, maxVolume);
    }

    public double getPlaybackBlockRange() {
        return BalanceMath.volumeToAttenuationDistance(playbackVolume);
    }

    public double getP2PBlockRange() {
        return BalanceMath.volumeToAttenuationDistance(p2pVolume);
    }

    public void setPlaybackBlockRange(double blockRange) {
        double volume = BalanceMath.attenuationDistanceToVolume(blockRange);
        volume = Math.clamp(volume, 0.0f, BalanceMath.listeningVolume(mTier));
        if (volume != playbackVolume) {
            playbackVolume = volume;
            if (getBaseMetaTileEntity().isServerSide()) {
                updateEmitterList();
            }
        }
    }

    public void setP2PBlockRange(double blockRange) {
        double volume = BalanceMath.attenuationDistanceToVolume(blockRange);
        volume = Math.clamp(volume, 0.0f, BalanceMath.listeningVolume(mTier));
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
        final GTMusicSystem.MusicSource target = musicSource;
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
        final double actualVolume = Math.clamp(playbackVolume, 0.0f, BalanceMath.listeningVolume(powered ? mTier : 0));
        target.setEmitter(0, position, actualVolume);
        final double actualP2PVolume = Math.clamp(p2pVolume, 0.0f, powered ? BalanceMath.listeningVolume(mTier) : 0.0f);
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

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEBetterJukeboxGui(this, getUIProperties()).build(guiData, syncManager, uiSettings);
    }
}

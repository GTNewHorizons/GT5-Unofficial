package gregtech.common.tileentities.machines.basic;

import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TELEPORTER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TELEPORTER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TELEPORTER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TELEPORTER_GLOW;
import static gregtech.api.metatileentity.BaseTileEntity.ITEM_TRANSFER_TOOLTIP;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;

import org.joml.Vector4i;

import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.CycleButtonWidget;

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
    private UUID jukeboxUuid = UNSET_UUID;
    public boolean loopMode = true;
    public boolean shuffleMode = false;
    public int nextPlaybackSlot = 0;
    public float playbackVolume = BalanceMath.VANILLA_JUKEBOX_RANGE;
    public float p2pVolume = BalanceMath.VANILLA_JUKEBOX_RANGE;
    // Computed state
    private final Vector4i interdimPositionCache = new Vector4i(); // XYZ, Dimension ID
    private GT_MusicSystem.MusicSource musicSource = null;
    private boolean powered = false;

    // Constants
    public static final UUID UNSET_UUID = UUID.nameUUIDFromBytes(new byte[] { 0 });
    public static final int INPUT_SLOTS = 21;

    public enum HeadphoneLimit {
        BLOCK_RANGE,
        INSIDE_DIMENSION,
        BETWEEN_DIMENSIONS
    }

    public static final class BalanceMath {

        public static int MAX_TIER = GTVoltageIndex.IV;
        public static float VANILLA_JUKEBOX_RANGE = 4.0f; // 64 blocks

        private static float[] LISTENING_VOLUME = new float[] { VANILLA_JUKEBOX_RANGE, // ULV (does not exist)
            VANILLA_JUKEBOX_RANGE + 1.0f, // LV, 80 blocks
            VANILLA_JUKEBOX_RANGE + 2.0f, // MV, 96 blocks
            VANILLA_JUKEBOX_RANGE + 4.0f, // HV, 118 blocks
            VANILLA_JUKEBOX_RANGE + 5.0f, // EV, 144 blocks
            VANILLA_JUKEBOX_RANGE + 6.0f, // IV, 160 blocks, equivalent to default load distance of 10 chunks
        };

        private static float[] HEADPHONE_BLOCK_RANGE = new float[] { 64.0f, // ULV (does not exist)
            128.0f, // LV
            160.0f, // MV
            320.0f, // HV
            9001.0f, // EV, alreadu unlimited here - this value is ignored
            9002.0f, // IV, already unlimited here - this value is ignored
        };

        public static float listeningVolume(int tier) {
            tier = MathHelper.clamp_int(tier, 0, MAX_TIER);
            return LISTENING_VOLUME[tier];
        }

        public static float headphoneBlockRange(int tier) {
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

        public static float rangeToAttenuationDistance(float range) {
            // SoundManager.playSound logic
            if (range >= 1.0f) {
                return 16.0f * range;
            } else {
                return 16.0f;
            }
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
                BalanceMath.rangeToAttenuationDistance(BalanceMath.listeningVolume(aTier))));
        strings.add(switch (BalanceMath.headphoneLimit(aTier)) {
            case BLOCK_RANGE -> String.format(
                "Headphone signal range: %s%.1f blocks",
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
        if (sideDirection != ForgeDirection.UP) return new ITexture[] { MACHINE_CASINGS[mTier][colorIndex + 1] };
        if (active) return new ITexture[] { MACHINE_CASINGS[mTier][colorIndex + 1],
            TextureFactory.of(OVERLAY_TELEPORTER_ACTIVE), TextureFactory.builder()
                .addIcon(OVERLAY_TELEPORTER_ACTIVE_GLOW)
                .glow()
                .build() };
        return new ITexture[] { MACHINE_CASINGS[mTier][colorIndex + 1], TextureFactory.of(OVERLAY_TELEPORTER),
            TextureFactory.builder()
                .addIcon(OVERLAY_TELEPORTER_GLOW)
                .glow()
                .build() };
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        super.onPostTick(aBaseMetaTileEntity, aTimer);
        if (aBaseMetaTileEntity.isClientSide() || !aBaseMetaTileEntity.isAllowedToWork()) {
            return;
        }
        final Vector4i interdimPosition = interdimPositionCache;
        interdimPosition.x = aBaseMetaTileEntity.getXCoord();
        interdimPosition.y = aBaseMetaTileEntity.getYCoord();
        interdimPosition.z = aBaseMetaTileEntity.getZCoord();
        interdimPosition.w = aBaseMetaTileEntity.getWorld().provider.dimensionId;

        if (nextPlaybackSlot < 0 || nextPlaybackSlot >= INPUT_SLOTS) {
            nextPlaybackSlot = shuffleMode ? ThreadLocalRandom.current()
                .nextInt(INPUT_SLOTS) : 0;
        }

        if (aBaseMetaTileEntity.isUniversalEnergyStored(getMinimumStoredEU())
            && aBaseMetaTileEntity.decreaseStoredEnergyUnits(BalanceMath.eutUsage(mTier), false)) {
            if (!powered) {
                powered = true;
                updateEmitterList();
            }
        } else if (powered) { // was powered, but no longer is
            powered = false;
            updateEmitterList();
        }
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
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
            musicSource.currentRecord = new ResourceLocation("minecraft", "records.chirp");
            musicSource.originPosition.set(interdimPosition);
            musicSource.interdimensional = BalanceMath.headphoneLimit(mTier) == HeadphoneLimit.BETWEEN_DIMENSIONS;
            musicSource.startedPlayingAtMs = System.currentTimeMillis();
            updateEmitterList();
        }
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
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isInputFacing(ForgeDirection side) {
        return true;
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
    public long maxEUInput() {
        return V[mTier];
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
    public static final String NBTKEY_NEXT_PLAYBACK_SLOT = "nextPlaybackSlot";
    public static final String NBTKEY_VOLUME_PLAY = "playbackVolume";
    public static final String NBTKEY_VOLUME_P2P = "p2pVolume";

    @Override
    public String[] getInfoData() {
        return new String[] { "Jukebox UUID: " + ((jukeboxUuid == UNSET_UUID) ? "unset" : jukeboxUuid), };
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        if (jukeboxUuid != UNSET_UUID) {
            aNBT.setLong(NBTKEY_UUID_LOW, jukeboxUuid.getLeastSignificantBits());
            aNBT.setLong(NBTKEY_UUID_HIGH, jukeboxUuid.getMostSignificantBits());
            aNBT.setBoolean(NBTKEY_LOOP_MODE, loopMode);
            aNBT.setBoolean(NBTKEY_SHUFFLE_MODE, shuffleMode);
            aNBT.setInteger(NBTKEY_NEXT_PLAYBACK_SLOT, nextPlaybackSlot);
            aNBT.setFloat(NBTKEY_VOLUME_PLAY, playbackVolume);
            aNBT.setFloat(NBTKEY_VOLUME_P2P, p2pVolume);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
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
        if (aNBT.hasKey(NBTKEY_NEXT_PLAYBACK_SLOT, Constants.NBT.TAG_ANY_NUMERIC)) {
            nextPlaybackSlot = aNBT.getInteger(NBTKEY_NEXT_PLAYBACK_SLOT);
        }
        if (aNBT.hasKey(NBTKEY_VOLUME_PLAY, Constants.NBT.TAG_ANY_NUMERIC)) {
            playbackVolume = aNBT.getFloat(NBTKEY_VOLUME_PLAY);
        }
        if (aNBT.hasKey(NBTKEY_VOLUME_P2P, Constants.NBT.TAG_ANY_NUMERIC)) {
            p2pVolume = aNBT.getFloat(NBTKEY_VOLUME_P2P);
        }

        final float maxVolume = BalanceMath.listeningVolume(mTier);
        playbackVolume = MathHelper.clamp_float(playbackVolume, 0.0f, maxVolume);
        p2pVolume = MathHelper.clamp_float(p2pVolume, 0.0f, maxVolume);
    }

    @Override
    public boolean useModularUI() {
        return true;
    }

    @Override
    protected BasicUIProperties getUIProperties() {
        return super.getUIProperties().toBuilder()
            .itemInputPositionsGetter(count -> UIHelper.getGridPositions(count, 7, 6, 7, 3))
            .itemOutputPositionsGetter(count -> UIHelper.getGridPositions(count, 151, 24, 1))
            .progressBarPos(Pos2d.cartesian(133, 24))
            .build();
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);
        // Loop
        builder.widget(
            new CycleButtonWidget().setToggle(() -> loopMode, val -> loopMode = val)
                .setStaticTexture(GT_UITextures.OVERLAY_BUTTON_CYCLIC)
                .setVariableBackground(GT_UITextures.BUTTON_STANDARD_TOGGLE)
                .setGTTooltip(() -> mTooltipCache.getData(ITEM_TRANSFER_TOOLTIP))
                .setTooltipShowUpDelay(TOOLTIP_DELAY)
                .setPos(133, 6)
                .setSize(18, 18));
        // Shuffle
        builder.widget(new CycleButtonWidget().setToggle(() -> shuffleMode, val -> {
            shuffleMode = val;
            if (shuffleMode) {
                nextPlaybackSlot = -1;
            } else {
                nextPlaybackSlot = 0;
            }
        })
            .setStaticTexture(GT_UITextures.OVERLAY_BUTTON_SHUFFLE)
            .setVariableBackground(GT_UITextures.BUTTON_STANDARD_TOGGLE)
            .setGTTooltip(() -> mTooltipCache.getData(ITEM_TRANSFER_TOOLTIP))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(133, 42)
            .setSize(18, 18));
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
        final float actualVolume = MathHelper.clamp_float(
            playbackVolume,
            0.0f,
            powered ? BalanceMath.listeningVolume(mTier) : BalanceMath.VANILLA_JUKEBOX_RANGE);
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

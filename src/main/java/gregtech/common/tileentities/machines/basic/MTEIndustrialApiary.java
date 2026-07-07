package gregtech.common.tileentities.machines.basic;

import static forestry.api.apiculture.BeeManager.beeRoot;
import static gregtech.api.enums.GTAuthors.AuthorKuba;
import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_BOTTOM_INDUSTRIAL_APIARY;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_BOTTOM_INDUSTRIAL_APIARY_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_BOTTOM_INDUSTRIAL_APIARY_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_BOTTOM_INDUSTRIAL_APIARY_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_INDUSTRIAL_APIARY;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_INDUSTRIAL_APIARY_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_INDUSTRIAL_APIARY_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_INDUSTRIAL_APIARY_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SIDE_INDUSTRIAL_APIARY;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SIDE_INDUSTRIAL_APIARY_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SIDE_INDUSTRIAL_APIARY_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SIDE_INDUSTRIAL_APIARY_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_INDUSTRIAL_APIARY;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_INDUSTRIAL_APIARY_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_INDUSTRIAL_APIARY_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_INDUSTRIAL_APIARY_GLOW;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.google.common.collect.ImmutableSet;
import com.mojang.authlib.GameProfile;

import forestry.api.apiculture.EnumBeeChromosome;
import forestry.api.apiculture.EnumBeeType;
import forestry.api.apiculture.FlowerManager;
import forestry.api.apiculture.IAlleleBeeAcceleratableEffect;
import forestry.api.apiculture.IAlleleBeeEffect;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.apiculture.IApiaristTracker;
import forestry.api.apiculture.IBee;
import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeHousing;
import forestry.api.apiculture.IBeeHousingInventory;
import forestry.api.apiculture.IBeeListener;
import forestry.api.apiculture.IBeeModifier;
import forestry.api.apiculture.IBeekeepingLogic;
import forestry.api.apiculture.IBeekeepingMode;
import forestry.api.arboriculture.EnumGermlingType;
import forestry.api.core.BiomeHelper;
import forestry.api.core.EnumHumidity;
import forestry.api.core.EnumTemperature;
import forestry.api.core.ForestryAPI;
import forestry.api.core.IErrorLogic;
import forestry.api.core.IErrorState;
import forestry.api.genetics.IEffectData;
import forestry.api.genetics.IIndividual;
import forestry.apiculture.genetics.Bee;
import forestry.apiculture.genetics.alleles.AlleleEffectThrottled;
import forestry.core.errors.EnumErrorCode;
import forestry.plugins.PluginApiculture;
import gregtech.GTMod;
import gregtech.api.enums.GTAuthors;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicMachine;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTApiaryModifier;
import gregtech.api.util.GTApiaryUpgrade;
import gregtech.api.util.GTItemTransfer;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.singleblock.MTEIndustrialApiaryGui;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTEIndustrialApiary extends MTEBasicMachine
    implements IBeeHousing, IBeeHousingInventory, IErrorLogic, IBeeModifier, IBeeListener {

    public static final int beeCycleLength = 550;
    public static final int baseEUtUsage = 37;
    private static final int queen = 5;
    private static final int drone = 6;
    private static final int upgradeSlot = drone + 1;
    private static final int upgradeSlotCount = 4;

    private int mSpeed = 0;
    private boolean mLockedSpeed = true;
    private boolean mAutoQueen = true;

    private ItemStack usedQueen = null;
    private IBee usedQueenBee = null;
    private IEffectData[] effectData = new IEffectData[2];

    public MTEIndustrialApiary(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            4,
            new String[] { "BEES GOES BRRRR", EnumChatFormatting.GRAY + GTAuthors.buildAuthorsWithFormat(AuthorKuba),
                "Effective production chance as a percent is", "2.8 * b^0.52 * (p + t)^0.52 * s^0.37",
                "where b is the base production chance as a percent,",
                "p is the production modifier (2 w/o upgrades, or 4 * 1.2^n with n production upgrades),",
                "t is 8 for the industrial apiary, and", "s is the speed value for the bee",
                "Outputs are generated at the end of every bee tick (...)",
                "Primary outputs are rolled once with base chance, once with half base" },
            6,
            9,
            TextureFactory.of(
                TextureFactory.of(OVERLAY_SIDE_INDUSTRIAL_APIARY_ACTIVE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_SIDE_INDUSTRIAL_APIARY_ACTIVE_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_SIDE_INDUSTRIAL_APIARY),
                TextureFactory.builder()
                    .addIcon(OVERLAY_SIDE_INDUSTRIAL_APIARY_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_FRONT_INDUSTRIAL_APIARY_ACTIVE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_INDUSTRIAL_APIARY_ACTIVE_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_FRONT_INDUSTRIAL_APIARY),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_INDUSTRIAL_APIARY_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_TOP_INDUSTRIAL_APIARY_ACTIVE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_TOP_INDUSTRIAL_APIARY_ACTIVE_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_TOP_INDUSTRIAL_APIARY),
                TextureFactory.builder()
                    .addIcon(OVERLAY_TOP_INDUSTRIAL_APIARY_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_BOTTOM_INDUSTRIAL_APIARY_ACTIVE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_BOTTOM_INDUSTRIAL_APIARY_ACTIVE_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_BOTTOM_INDUSTRIAL_APIARY),
                TextureFactory.builder()
                    .addIcon(OVERLAY_BOTTOM_INDUSTRIAL_APIARY_GLOW)
                    .glow()
                    .build()));
    }

    public MTEIndustrialApiary(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 4, aDescription, aTextures, 6, 9);
    }

    public boolean isLockedSpeed() {
        return mLockedSpeed;
    }

    public void setLockedSpeed(boolean lockedSpeed) {
        this.mLockedSpeed = lockedSpeed;
    }

    public boolean isAutoQueen() {
        return mAutoQueen;
    }

    public void setAutoQueen(boolean autoQueen) {
        this.mAutoQueen = autoQueen;
    }

    public int getSpeed() {
        return mSpeed;
    }

    public void setSpeed(int speed) {
        this.mSpeed = speed;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEIndustrialApiary(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("mSpeed", mSpeed);
        aNBT.setBoolean("mLockedSpeed", mLockedSpeed);
        aNBT.setBoolean("mAutoQueen", mAutoQueen);
        if (usedQueen != null) aNBT.setTag("usedQueen", usedQueen.writeToNBT(new NBTTagCompound()));
        aNBT.setBoolean("retrievingPollenInThisOperation", retrievingPollenInThisOperation);
        aNBT.setInteger("pollinationDelay", pollinationDelay);
        aNBT.setFloat("usedBeeLife", usedBeeLife);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mSpeed = aNBT.getInteger("mSpeed");
        mLockedSpeed = aNBT.getBoolean("mLockedSpeed");
        if (aNBT.hasKey("mAutoQueen")) mAutoQueen = aNBT.getBoolean("mAutoQueen");
        if (aNBT.hasKey("usedQueen")) usedQueen = ItemStack.loadItemStackFromNBT(aNBT.getCompoundTag("usedQueen"));
        retrievingPollenInThisOperation = aNBT.getBoolean("retrievingPollenInThisOperation");
        pollinationDelay = aNBT.getInteger("pollinationDelay");
        usedBeeLife = aNBT.getFloat("usedBeeLife");
    }

    boolean retrievingPollenInThisOperation = false;
    IIndividual retrievedpollen = null;
    int pollinationDelay = 100;
    float usedBeeLife = 0f;

    @Override
    public int checkRecipe() {
        updateModifiers();
        if (canWork()) {

            final ItemStack queen = getQueen();
            usedQueen = queen.copy();
            if (beeRoot.getType(queen) == EnumBeeType.QUEEN) {
                final IBee bee = beeRoot.getMember(queen);
                usedQueenBee = bee;

                // LIFE CYCLES

                float mod = this.getLifespanModifier(null, null, 1.f);
                final IBeekeepingMode mode = beeRoot.getBeekeepingMode(this.getWorld());
                final IBeeModifier beemodifier = mode.getBeeModifier();
                mod *= beemodifier.getLifespanModifier(null, null, 1.f);
                final int h = bee.getHealth();
                mod = 1.f / mod;
                final float cycles = h / mod;

                // PRODUCTS

                final HashMap<GTUtility.ItemId, ItemStack> pollen = new HashMap<>();

                if (isRetrievingPollen && floweringMod > 0f) {
                    final int icycles = (int) cycles
                        + (getWorld().rand.nextFloat() < (cycles - (float) ((int) cycles)) ? 1 : 0);
                    for (int z = 0; z < icycles; z++) {
                        final IIndividual p = bee.retrievePollen(this);
                        if (p != null) {
                            final ItemStack s = p.getGenome()
                                .getSpeciesRoot()
                                .getMemberStack(p, EnumGermlingType.POLLEN.ordinal());
                            if (s != null) {
                                final GTUtility.ItemId id = GTUtility.ItemId.createNoCopy(s);
                                pollen.computeIfAbsent(id, k -> {
                                    final ItemStack ns = s.copy();
                                    ns.stackSize = 0;
                                    return ns;
                                });
                                pollen.get(id).stackSize += s.stackSize;
                            }
                        }
                    }
                }

                retrievedpollen = null;
                retrievingPollenInThisOperation = isRetrievingPollen;

                final IBeeGenome genome = bee.getGenome();
                final IAlleleBeeSpecies primary = genome.getPrimary();
                final IAlleleBeeSpecies secondary = genome.getSecondary();

                final float speed = genome.getSpeed();
                final float prodMod = getProductionModifier(null, 0f) + beemodifier.getProductionModifier(null, 0f);

                final HashMap<GTUtility.ItemId, Float> drops = new HashMap<>();
                final HashMap<GTUtility.ItemId, ItemStack> dropstacks = new HashMap<>();

                for (Map.Entry<ItemStack, Float> entry : primary.getProductChances()
                    .entrySet()) {
                    final GTUtility.ItemId id = GTUtility.ItemId.createNoCopy(entry.getKey());
                    drops.merge(
                        id,
                        Bee.getFinalChance(entry.getValue(), speed, prodMod, 8f) * (float) entry.getKey().stackSize
                            * cycles,
                        Float::sum);
                    dropstacks.computeIfAbsent(id, k -> entry.getKey());
                }
                for (Map.Entry<ItemStack, Float> entry : secondary.getProductChances()
                    .entrySet()) {
                    final GTUtility.ItemId id = GTUtility.ItemId.createNoCopy(entry.getKey());
                    drops.merge(
                        id,
                        Bee.getFinalChance(entry.getValue() / 2f, speed, prodMod, 8f) * (float) entry.getKey().stackSize
                            * cycles,
                        Float::sum);
                    dropstacks.computeIfAbsent(id, k -> entry.getKey());
                }
                if (primary.isJubilant(genome, this) && secondary.isJubilant(genome, this))
                    for (Map.Entry<ItemStack, Float> entry : primary.getSpecialtyChances()
                        .entrySet()) {
                            final GTUtility.ItemId id = GTUtility.ItemId.createNoCopy(entry.getKey());
                            drops.merge(
                                id,
                                Bee.getFinalChance(entry.getValue(), speed, prodMod, 8f)
                                    * (float) entry.getKey().stackSize
                                    * cycles,
                                Float::sum);
                            dropstacks.computeIfAbsent(id, k -> entry.getKey());
                        }

                int i = 0;
                final int imax = mOutputItems.length;

                final IApiaristTracker breedingTracker = beeRoot.getBreedingTracker(getWorld(), getOwner());

                if (!bee.canSpawn()) {
                    final ItemStack convert = new ItemStack(PluginApiculture.items.beePrincessGE);
                    final NBTTagCompound nbttagcompound = new NBTTagCompound();
                    queen.writeToNBT(nbttagcompound);
                    convert.setTagCompound(nbttagcompound);
                    this.mOutputItems[i++] = convert;
                } else {
                    final IBee b = bee.spawnPrincess(this);
                    if (b != null) {
                        final ItemStack princess = beeRoot.getMemberStack(b, EnumBeeType.PRINCESS.ordinal());
                        breedingTracker.registerPrincess(b);
                        this.mOutputItems[i++] = princess;
                    }
                    final IBee[] d = bee.spawnDrones(this);
                    if (d != null && d.length > 0) {
                        final HashMap<GTUtility.ItemId, ItemStack> drones = new HashMap<>(d.length);
                        for (IBee dr : d) {
                            final ItemStack drone = beeRoot.getMemberStack(dr, EnumBeeType.DRONE.ordinal());
                            breedingTracker.registerDrone(dr);
                            final GTUtility.ItemId drid = GTUtility.ItemId.createNoCopy(drone);
                            if (drones.containsKey(drid)) drones.get(drid).stackSize += drone.stackSize;
                            else {
                                this.mOutputItems[i++] = drone;
                                drones.put(drid, drone);
                            }
                        }
                    }
                }

                final int imin = i;

                setQueen(null);

                for (Map.Entry<GTUtility.ItemId, Float> entry : drops.entrySet()) {
                    final ItemStack s = dropstacks.get(entry.getKey())
                        .copy();
                    s.stackSize = entry.getValue()
                        .intValue()
                        + (getWorld().rand.nextFloat() < (entry.getValue() - (float) entry.getValue()
                            .intValue()) ? 1 : 0);
                    if (s.stackSize > 0 && i < imax) while (true) {
                        if (s.stackSize <= s.getMaxStackSize()) {
                            this.mOutputItems[i++] = s;
                            break;
                        } else this.mOutputItems[i++] = s.splitStack(s.getMaxStackSize());
                        if (i >= imax) break;
                    }
                }

                for (ItemStack s : pollen.values()) if (i < imax) this.mOutputItems[i++] = s;
                else break;

                // Overclock

                usedBeeLife = cycles * (float) beeCycleLength;
                this.mMaxProgresstime = (int) usedBeeLife;
                final int timemaxdivider = this.mMaxProgresstime / 100;
                final int useddivider = 1 << this.mSpeed;
                int actualdivider = useddivider;
                this.mMaxProgresstime /= Math.min(actualdivider, timemaxdivider);
                actualdivider /= Math.min(actualdivider, timemaxdivider);
                for (i--; i >= imin; i--) this.mOutputItems[i].stackSize *= actualdivider;

                pollinationDelay = Math.max((int) (this.mMaxProgresstime / cycles), 20); // don't run too often

                this.mProgresstime = 0;
                this.mEUt = (int) ((float) baseEUtUsage * this.energyMod * useddivider);
                if (useddivider == 2) this.mEUt += 32;
                else if (useddivider > 2) this.mEUt += (32 * (useddivider << (this.mSpeed - 2)));
            } else {
                // Breeding time

                retrievingPollenInThisOperation = true; // Don't pollinate when breeding

                this.mMaxProgresstime = 100;
                this.mProgresstime = 0;
                final int useddivider = Math.min(100, 1 << this.mSpeed);
                this.mMaxProgresstime /= useddivider;
                this.mEUt = (int) ((float) baseEUtUsage * this.energyMod * useddivider);
                if (useddivider == 2) this.mEUt += 32;
                else if (useddivider > 2) this.mEUt += (32 * (useddivider << (this.mSpeed - 2)));

                final IBee princess = beeRoot.getMember(getQueen());
                usedQueenBee = princess;
                final IBee drone = beeRoot.getMember(getDrone());
                princess.mate(drone);
                final NBTTagCompound nbttagcompound = new NBTTagCompound();
                princess.writeToNBT(nbttagcompound);
                this.mOutputItems[0] = new ItemStack(PluginApiculture.items.beeQueenGE);
                this.mOutputItems[0].setTagCompound(nbttagcompound);
                beeRoot.getBreedingTracker(getWorld(), getOwner())
                    .registerQueen(princess);

                setQueen(null);
                getDrone().stackSize -= 1;
                if (getDrone().stackSize == 0) setDrone(null);
            }

            return FOUND_AND_SUCCESSFULLY_USED_RECIPE;
        }

        return DID_NOT_FIND_RECIPE;
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        updateModifiers();
    }

    @Override
    protected boolean hasEnoughEnergyToCheckRecipe() {
        return getBaseMetaTileEntity().isUniversalEnergyStored(V[mSpeed] * 8L);
    }

    @Override
    public long maxAmperesIn() {
        return 4L;
    }

    private void doEffect() {
        final IBeeGenome genome = usedQueenBee.getGenome();
        final IAlleleBeeEffect effect = genome.getEffect();
        if (!(effect instanceof IAlleleBeeAcceleratableEffect)) {
            effectData[0] = effect.validateStorage(effectData[0]);
            effect.doEffect(genome, effectData[0], this);
        }

        if (!effect.isCombinable()) return;

        final IAlleleBeeEffect secondary = (IAlleleBeeEffect) genome.getInactiveAllele(EnumBeeChromosome.EFFECT);
        if (!secondary.isCombinable()) return;

        if (!(secondary instanceof IAlleleBeeAcceleratableEffect)) {
            effectData[1] = secondary.validateStorage(effectData[1]);
            secondary.doEffect(genome, effectData[1], this);
        }
    }

    private void doAcceleratedEffects() {
        final IBeeGenome genome = usedQueenBee.getGenome();
        final IAlleleBeeEffect effect = genome.getEffect();
        if (effect instanceof IAlleleBeeAcceleratableEffect) {
            effectData[0] = effect.validateStorage(effectData[0]);
            effectData[0] = ((IAlleleBeeAcceleratableEffect) effect).doEffectAccelerated(
                genome,
                effectData[0],
                this,
                usedBeeLife
                    / (effect instanceof AlleleEffectThrottled ? (float) ((AlleleEffectThrottled) effect).getThrottle()
                        : 1f));
        }

        if (!effect.isCombinable()) return;

        final IAlleleBeeEffect secondary = (IAlleleBeeEffect) genome.getInactiveAllele(EnumBeeChromosome.EFFECT);
        if (!secondary.isCombinable()) return;

        if (secondary instanceof IAlleleBeeAcceleratableEffect) {
            effectData[1] = secondary.validateStorage(effectData[1]);
            effectData[1] = ((IAlleleBeeAcceleratableEffect) secondary).doEffectAccelerated(
                genome,
                effectData[1],
                this,
                usedBeeLife / (secondary instanceof AlleleEffectThrottled
                    ? (float) ((AlleleEffectThrottled) secondary).getThrottle()
                    : 1f));
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isClientSide()) {
            if (GTMod.clientProxy()
                .changeDetected() == 4) {
                /*
                 * Client tick counter that is set to 5 on hiding pipes and covers. It triggers a texture update next
                 * client tick when reaching 4, with provision for 3 more update tasks, spreading client change
                 * detection related work and network traffic on different ticks, until it reaches 0.
                 */
                aBaseMetaTileEntity.issueTextureUpdate();
            }
            if (aBaseMetaTileEntity.isActive()) {
                if (usedQueen != null) {
                    if (aTick % 2 == 0) {
                        // FX on client, effect on server
                        final IBee bee = beeRoot.getMember(usedQueen);
                        effectData = bee.doFX(effectData, this);
                    }
                }
            }
        }
        if (aBaseMetaTileEntity.isServerSide()) {

            mCharge = aBaseMetaTileEntity.getStoredEU() / 2 > aBaseMetaTileEntity.getEUCapacity() / 3;
            mDecharge = aBaseMetaTileEntity.getStoredEU() < aBaseMetaTileEntity.getEUCapacity() / 3;

            if (!aBaseMetaTileEntity.isActive()) {
                if (aBaseMetaTileEntity.isAllowedToWork()
                    && (aBaseMetaTileEntity.hasInventoryBeenModified() || aTick % 600 == 0
                        || aBaseMetaTileEntity.hasWorkJustBeenEnabled())
                    && hasEnoughEnergyToCheckRecipe()) {
                    final int check = checkRecipe();
                    if (check == FOUND_AND_SUCCESSFULLY_USED_RECIPE) {
                        aBaseMetaTileEntity.setActive(true);
                    }
                }
            } else {

                if (this.mProgresstime < 0) {
                    this.mProgresstime++;
                    return;
                }
                if (this.mStuttering) {
                    if (!aBaseMetaTileEntity.isAllowedToWork()) return;
                    if (aTick % 100 == 0) this.mStuttering = false;
                    return;
                }
                if (this.hasErrors()) {
                    if (!aBaseMetaTileEntity.isAllowedToWork()) return;
                    if (aTick % 100 == 0) if (!canWork(usedQueen)) this.stutterProcess();
                    return;
                }

                if (!drainEnergyForProcess(this.mEUt)) {
                    this.mStuttering = true;
                    this.stutterProcess();
                    return;
                }
                this.mProgresstime++;
                if (usedQueen != null) {
                    if (usedQueenBee == null) usedQueenBee = beeRoot.getMember(usedQueen);
                    doEffect();
                    if (!retrievingPollenInThisOperation && floweringMod > 0f
                        && this.mProgresstime % pollinationDelay == 0) {
                        if (retrievedpollen == null) retrievedpollen = usedQueenBee.retrievePollen(this);
                        if (retrievedpollen != null && (usedQueenBee.pollinateRandom(this, retrievedpollen)
                            || this.mProgresstime % (pollinationDelay * 5) == 0)) retrievedpollen = null;
                    }
                }

                if (this.mProgresstime % 100 == 0) {
                    if (!canWork(usedQueen)) {
                        this.stutterProcess();
                        return;
                    }
                }

                if (this.mProgresstime >= this.mMaxProgresstime) {
                    if (usedQueenBee != null) doAcceleratedEffects();
                    updateModifiers();
                    for (int i = 0; i < mOutputItems.length; i++)
                        if (mOutputItems[i] != null) for (int j = 0; j < mOutputItems.length; j++) {
                            if (j == 0 && isAutomated) {
                                if (beeRoot.isMember(mOutputItems[i], EnumBeeType.QUEEN.ordinal())
                                    || beeRoot.isMember(mOutputItems[i], EnumBeeType.PRINCESS.ordinal())) {
                                    if (aBaseMetaTileEntity.addStackToSlot(queen, mOutputItems[i])) break;
                                } else if (beeRoot.isMember(mOutputItems[i], EnumBeeType.DRONE.ordinal()))
                                    if (aBaseMetaTileEntity.addStackToSlot(drone, mOutputItems[i])) break;
                            } else if (mAutoQueen && i == 0
                                && j == 0
                                && beeRoot.isMember(mOutputItems[0], EnumBeeType.QUEEN.ordinal())
                                && aBaseMetaTileEntity.addStackToSlot(queen, mOutputItems[0])) break;
                            if (aBaseMetaTileEntity
                                .addStackToSlot(getOutputSlot() + ((j + i) % mOutputItems.length), mOutputItems[i]))
                                break;
                        }
                    Arrays.fill(mOutputItems, null);
                    mEUt = 0;
                    mProgresstime = 0;
                    mMaxProgresstime = 0;
                    mStuttering = false;
                    aBaseMetaTileEntity.setActive(false);

                    if (doesAutoOutput() && !isOutputEmpty() && aBaseMetaTileEntity.getFrontFacing() != mMainFacing) {
                        GTItemTransfer transfer = new GTItemTransfer();

                        transfer.outOfMachine(this, aBaseMetaTileEntity.getFrontFacing());
                        transfer.setStacksToTransfer(mOutputItems.length);

                        transfer.transfer();
                    }

                    if (aBaseMetaTileEntity.isAllowedToWork() && checkRecipe() == FOUND_AND_SUCCESSFULLY_USED_RECIPE)
                        aBaseMetaTileEntity.setActive(true);
                }
            }
        }
    }

    public void cancelProcess() {
        if (this.getBaseMetaTileEntity()
            .isActive()
            && this.getBaseMetaTileEntity()
                .isServerSide()
            && usedQueen != null
            && beeRoot.isMember(usedQueen, EnumBeeType.QUEEN.ordinal())) {
            Arrays.fill(mOutputItems, null);
            mEUt = 0;
            mProgresstime = 0;
            mMaxProgresstime = 0;
            mStuttering = false;
            this.getBaseMetaTileEntity()
                .setActive(false);
            setQueen(usedQueen);
            this.getBaseMetaTileEntity()
                .disableWorking();
        }
    }

    @Override
    public boolean isItemValidForSlot(int aIndex, ItemStack aStack) {
        if (aStack == null) return false;
        if (aIndex < getInputSlot()) return true;
        if (aIndex == queen) return beeRoot.isMember(aStack, EnumBeeType.QUEEN.ordinal())
            || beeRoot.isMember(aStack, EnumBeeType.PRINCESS.ordinal());
        else if (aIndex == drone) return beeRoot.isMember(aStack, EnumBeeType.DRONE.ordinal());
        else if (aIndex < getOutputSlot()) {
            if (!GTApiaryUpgrade.isUpgrade(aStack)) return false;
            for (int i = upgradeSlot; i < upgradeSlot + upgradeSlotCount; i++) {
                if (aIndex == i) continue;
                final ItemStack s = getStackInSlot(i);
                if (s == null) continue;
                if (GTUtility.areStacksEqual(getStackInSlot(i), aStack)) return false;
                if (GTApiaryUpgrade.isUpgrade(aStack)) {
                    if (!GTApiaryUpgrade.getUpgrade(aStack)
                        .isAllowedToWorkWith(getStackInSlot(i))) return false;
                } else if (GTApiaryUpgrade.isUpgrade(s)) {
                    if (!GTApiaryUpgrade.getUpgrade(s)
                        .isAllowedToWorkWith(aStack)) return false;
                }
            }
            return true;
        } else return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        if (!super.allowPutStack(aBaseMetaTileEntity, aIndex, side, aStack)) return false;
        return isItemValidForSlot(aIndex, aStack);
    }

    @Override
    public void setInventorySlotContents(int aIndex, ItemStack aStack) {
        if (aIndex == queen && aStack != null && getBaseMetaTileEntity().isClientSide()) usedQueen = aStack.copy();
        super.setInventorySlotContents(aIndex, aStack);
    }

    public ItemStack getUsedQueen() {
        return usedQueen;
    }

    // region IBeeHousing

    @Override
    public Iterable<IBeeModifier> getBeeModifiers() {
        return Collections.singletonList(this);
    }

    @Override
    public Iterable<IBeeListener> getBeeListeners() {
        return Collections.singletonList(this);
    }

    @Override
    public IBeeHousingInventory getBeeInventory() {
        return this;
    }

    @Override
    public IBeekeepingLogic getBeekeepingLogic() {
        return dummylogic;
    }

    @Override
    public int getBlockLightValue() {
        return this.getBaseMetaTileEntity()
            .getLightLevelAtSide(ForgeDirection.UP);
    }

    @Override
    public boolean canBlockSeeTheSky() {
        return this.getBaseMetaTileEntity()
            .getSkyAtSide(ForgeDirection.UP);
    }

    @Override
    public World getWorld() {
        return this.getBaseMetaTileEntity()
            .getWorld();
    }

    GameProfile owner = null;

    @Override
    public GameProfile getOwner() {
        if (owner == null) owner = new GameProfile(
            this.getBaseMetaTileEntity()
                .getOwnerUuid(),
            this.getBaseMetaTileEntity()
                .getOwnerName());
        return owner;
    }

    @Override
    public Vec3 getBeeFXCoordinates() {
        return Vec3.createVectorHelper(
            getBaseMetaTileEntity().getXCoord() + 0.5,
            getBaseMetaTileEntity().getYCoord() + 0.5,
            getBaseMetaTileEntity().getZCoord() + 0.5);
    }

    @Override
    public BiomeGenBase getBiome() {
        if (biomeOverride == null) return this.getBaseMetaTileEntity()
            .getBiome();
        return biomeOverride;
    }

    @Override
    public EnumTemperature getTemperature() {
        if (BiomeHelper.isBiomeHellish(getBiome())) return EnumTemperature.HELLISH;
        return EnumTemperature.getFromValue(getBiome().temperature + temperatureMod);
    }

    @Override
    public EnumHumidity getHumidity() {
        return EnumHumidity.getFromValue(getBiome().rainfall + humidityMod);
    }

    @Override
    public IErrorLogic getErrorLogic() {
        return this;
    }

    @Override
    public ChunkCoordinates getCoordinates() {
        return this.getBaseMetaTileEntity()
            .getCoords();
    }

    // endregion

    // region IBeeHousingInventory
    @Override
    public ItemStack getQueen() {
        return getStackInSlot(queen);
    }

    @Override
    public ItemStack getDrone() {
        return getStackInSlot(drone);
    }

    @Override
    public void setQueen(ItemStack itemStack) {
        setInventorySlotContents(queen, itemStack);
    }

    @Override
    public void setDrone(ItemStack itemStack) {
        setInventorySlotContents(drone, itemStack);
    }

    @Override
    public boolean addProduct(ItemStack itemStack, boolean b) {
        throw new RuntimeException("Should not happen :F");
    }
    // endregion

    // region IErrorLogic

    public HashSet<IErrorState> mErrorStates = new HashSet<>();

    @Override
    public boolean setCondition(boolean b, IErrorState iErrorState) {
        if (b) mErrorStates.add(iErrorState);
        else mErrorStates.remove(iErrorState);
        return b;
    }

    @Override
    public boolean contains(IErrorState iErrorState) {
        return mErrorStates.contains(iErrorState);
    }

    @Override
    public boolean hasErrors() {
        return !mErrorStates.isEmpty();
    }

    @Override
    public void clearErrors() {
        mErrorStates.clear();
    }

    @Override
    public void writeData(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.write(mErrorStates.size());
        for (IErrorState s : mErrorStates) dataOutputStream.writeUTF(s.getUniqueName());
    }

    @Override
    public void readData(DataInputStream dataInputStream) throws IOException {
        for (int i = dataInputStream.readInt(); i > 0; i--)
            mErrorStates.add(ForestryAPI.errorStateRegistry.getErrorState(dataInputStream.readUTF()));
    }

    @Override
    public ImmutableSet<IErrorState> getErrorStates() {
        return ImmutableSet.copyOf(mErrorStates);
    }

    private String flowerType = "";
    private ChunkCoordinates flowercoords = null;
    private Block flowerBlock;
    private int flowerBlockMeta;

    private boolean checkFlower(IBee bee) {
        final World world = getWorld();
        final String flowerType = bee.getGenome()
            .getFlowerProvider()
            .getFlowerType();

        if (flowercoords != null) {
            int x = flowercoords.posX;
            int y = flowercoords.posY;
            int z = flowercoords.posZ;

            if (!this.flowerType.equals(flowerType) || !world.blockExists(x, y, z)) {
                return findFlower(bee, flowerType);
            }

            if (world.getBlock(x, y, z) != flowerBlock || world.getBlockMetadata(x, y, z) != flowerBlockMeta) {
                if (!FlowerManager.flowerRegistry.isAcceptedFlower(flowerType, world, x, y, z)) {
                    return findFlower(bee, flowerType);
                }

                flowerBlock = world.getBlock(x, y, z);
                flowerBlockMeta = world.getBlockMetadata(x, y, z);
            }
        }

        if (flowercoords == null) {
            return findFlower(bee, flowerType);
        }

        return true;
    }

    /** @return true if a flower was found, false otherwise */
    private boolean findFlower(IBee bee, String flowerType) {
        flowercoords = FlowerManager.flowerRegistry.getAcceptedFlowerCoordinates(this, bee, flowerType);
        if (flowercoords != null) {
            flowerBlock = getWorld().getBlock(flowercoords.posX, flowercoords.posY, flowercoords.posZ);
            flowerBlockMeta = getWorld().getBlockMetadata(flowercoords.posX, flowercoords.posY, flowercoords.posZ);
            this.flowerType = flowerType;
        }
        return flowercoords != null;
    }

    private boolean canWork(ItemStack queen) {
        clearErrors();
        if (queen == null) return true; // Reloaded the chunk ?
        if (beeRoot.isMember(queen, EnumBeeType.PRINCESS.ordinal())) return true;
        final IBee bee = beeRoot.getMember(queen);
        for (IErrorState err : bee.getCanWork(this)) setCondition(true, err);
        setCondition(!checkFlower(bee), EnumErrorCode.NO_FLOWER);
        return !hasErrors();
    }

    private boolean canWork() {
        clearErrors();
        final EnumBeeType beeType = beeRoot.getType(getQueen());
        if (beeType == EnumBeeType.PRINCESS) {
            setCondition(!beeRoot.isDrone(getDrone()), EnumErrorCode.NO_DRONE);
            return !hasErrors();
        }
        if (beeType == EnumBeeType.QUEEN) {
            final IBee bee = beeRoot.getMember(getQueen());
            for (IErrorState err : bee.getCanWork(this)) setCondition(true, err);
            setCondition(!checkFlower(bee), EnumErrorCode.NO_FLOWER);
            return !hasErrors();
        } else {
            setCondition(true, EnumErrorCode.NO_QUEEN);
            return false;
        }
    }

    // endregion

    // region IBeeModifier

    private float terrorityMod = 1f;
    private float mutationMod = 1f;
    private float lifespanMod = 1f;
    private float productionMod = 2f;
    private float floweringMod = 1f;
    private float geneticDecayMod = 1f;
    private float energyMod = 1f;
    private boolean sealedMod = false;
    private boolean selfLightedMod = false;
    private boolean selfUnlightedMod = false;
    private boolean sunlightSimulatedMod = false;
    private BiomeGenBase biomeOverride = null;
    private float humidityMod = 0f;
    private float temperatureMod = 0f;
    private boolean isAutomated = false;
    private boolean isRetrievingPollen = false;
    private int maxspeed = 0;

    public void updateModifiers() {
        final GTApiaryModifier mods = new GTApiaryModifier();
        for (int i = 0; i < upgradeSlotCount; i++) {
            final ItemStack s = getStackInSlot(upgradeSlot + i);
            if (s == null) continue;
            if (GTApiaryUpgrade.isUpgrade(s)) {
                final GTApiaryUpgrade upgrade = GTApiaryUpgrade.getUpgrade(s);
                upgrade.applyModifiers(mods, s);
            }
        }

        terrorityMod = mods.territory;
        mutationMod = mods.mutation;
        lifespanMod = mods.lifespan;
        productionMod = mods.production;
        floweringMod = mods.flowering;
        geneticDecayMod = mods.geneticDecay;
        energyMod = mods.energy;
        sealedMod = mods.isSealed;
        selfLightedMod = mods.isSelfLighted;
        selfUnlightedMod = mods.isSelfUnlighted;
        sunlightSimulatedMod = mods.isSunlightSimulated;
        biomeOverride = mods.biomeOverride;
        humidityMod = mods.humidity;
        temperatureMod = mods.temperature;
        isAutomated = mods.isAutomated;
        isRetrievingPollen = mods.isCollectingPollen;
        maxspeed = mods.maxSpeed;

        if (mLockedSpeed) mSpeed = maxspeed;
        else mSpeed = Math.min(mSpeed, maxspeed);
    }

    @Override
    public float getTerritoryModifier(IBeeGenome iBeeGenome, float v) {
        return Math.min(5, terrorityMod);
    }

    @Override
    public float getMutationModifier(IBeeGenome iBeeGenome, IBeeGenome iBeeGenome1, float v) {
        return mutationMod;
    }

    @Override
    public float getLifespanModifier(IBeeGenome iBeeGenome, IBeeGenome iBeeGenome1, float v) {
        return lifespanMod;
    }

    @Override
    public float getProductionModifier(IBeeGenome iBeeGenome, float v) {
        return productionMod;
    }

    @Override
    public float getFloweringModifier(IBeeGenome iBeeGenome, float v) {
        return floweringMod;
    }

    @Override
    public float getGeneticDecay(IBeeGenome iBeeGenome, float v) {
        return geneticDecayMod;
    }

    public float getEnergyModifier() {
        return energyMod;
    }

    @Override
    public boolean isSealed() {
        return sealedMod;
    }

    @Override
    public boolean isSelfLighted() {
        return selfLightedMod;
    }

    @Override
    public boolean isSelfUnlighted() {
        return selfUnlightedMod;
    }

    @Override
    public boolean isSunlightSimulated() {
        return sunlightSimulatedMod;
    }

    @Override
    public boolean isHellish() {
        return getBiome() == BiomeGenBase.hell;
    }

    public int getMaxSpeed() {
        return maxspeed;
    }

    // endregion

    // region IBeeListener

    @Override
    public void wearOutEquipment(int i) {}

    @Override
    public void onQueenDeath() {}

    @Override
    public boolean onPollenRetrieved(IIndividual iIndividual) {
        return false;
    }

    // endregion

    static final IBeekeepingLogic dummylogic = new IBeekeepingLogic() {

        @Override
        public boolean canWork() {
            return true;
        }

        @Override
        public void doWork() {}

        @Override
        public void syncToClient() {}

        @Override
        public void syncToClient(EntityPlayerMP entityPlayerMP) {}

        @Override
        public int getBeeProgressPercent() {
            return 0;
        }

        @Override
        public boolean canDoBeeFX() {
            return false;
        }

        @Override
        public void doBeeFX() {}

        @Override
        public void readFromNBT(NBTTagCompound nbtTagCompound) {}

        @Override
        public void writeToNBT(NBTTagCompound nbtTagCompound) {}
    };

    public int getAcceleration() {
        return 1 << mSpeed;
    }

    public int getAdditionalEnergyUsage() {
        final int accelerated = getAcceleration();
        int energyusage = 0;
        if (accelerated == 2) energyusage = 32;
        else if (accelerated > 2) energyusage = 32 * accelerated << (mSpeed - 2);
        return energyusage;
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currenttip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        if (tag.hasKey("queen")) {
            currenttip.add(
                StatCollector.translateToLocalFormatted(
                    "GT5U.waila.industrial_apiary.current_queen",
                    EnumChatFormatting.GREEN + StatCollector.translateToLocal(tag.getString("queen"))));
        }
        if (tag.hasKey("dummyProduction")) {
            currenttip.add(
                StatCollector.translateToLocalFormatted(
                    "GT5U.waila.industrial_apiary.effective_production",
                    EnumChatFormatting.AQUA + String.format("b^0.52 * %.2f", tag.getFloat("dummyProduction"))));
        }
        if (tag.hasKey("errors")) {
            NBTTagCompound errorNbt = tag.getCompoundTag("errors");
            for (int i = 0; i < errorNbt.getInteger("size"); i++) {
                currenttip.add(
                    StatCollector.translateToLocalFormatted(
                        "GT5U.waila.industrial_apiary.error",
                        EnumChatFormatting.RED + StatCollector.translateToLocal("for." + errorNbt.getString("e" + i))));
            }
        }
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        if (usedQueen != null) {
            IBeeGenome genome = beeRoot.getMember(usedQueen)
                .getGenome();
            tag.setString(
                "queen",
                genome.getPrimary()
                    .getUnlocalizedName());
            float prodModifier = getProductionModifier(genome, 0f);
            prodModifier += beeRoot.getBeekeepingMode(world)
                .getBeeModifier()
                .getProductionModifier(genome, prodModifier);
            float dummyProduction = 100f * Bee.getFinalChance(0.01f, genome.getSpeed(), prodModifier, 8f);
            tag.setFloat("dummyProduction", dummyProduction);
        }
        if (hasErrors()) {
            NBTTagCompound errorNbt = new NBTTagCompound();
            int errorCounter = 0;
            for (IErrorState error : mErrorStates) {
                errorNbt.setString("e" + errorCounter++, error.getDescription());
            }
            errorNbt.setInteger("size", errorCounter);
            tag.setTag("errors", errorNbt);
        }
    }

    @Override
    public int getStackSizeLimit(int slot, @Nullable ItemStack stack) {
        if (drone < slot && slot < getOutputSlot() && GTApiaryUpgrade.isUpgrade(stack)) return Math.min(
            GTApiaryUpgrade.getUpgrade(stack)
                .getMaxNumber(),
            super.getStackSizeLimit(slot, stack));
        return super.getStackSizeLimit(slot, stack);
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEIndustrialApiaryGui(this, getUIProperties()).build(guiData, syncManager, uiSettings);
    }
}

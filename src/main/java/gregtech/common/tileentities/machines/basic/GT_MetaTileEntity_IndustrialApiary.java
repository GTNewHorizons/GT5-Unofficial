package gregtech.common.tileentities.machines.basic;

import static gregtech.api.enums.GT_Values.AuthorKuba;
import static gregtech.api.enums.GT_Values.V;
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
import static gregtech.api.metatileentity.BaseTileEntity.STALLED_STUTTERING_TOOLTIP;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static gregtech.api.util.GT_Utility.moveMultipleItemStacks;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Slot;
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

import com.google.common.collect.ImmutableSet;
import com.gtnewhorizons.modularui.api.drawable.FallbackableUITexture;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.forge.IItemHandlerModifiable;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularUIContext;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.builder.UIInfo;
import com.gtnewhorizons.modularui.common.internal.wrapper.BaseSlot;
import com.gtnewhorizons.modularui.common.internal.wrapper.ModularUIContainer;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.CycleButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotGroup;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import com.mojang.authlib.GameProfile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.apiculture.BeeManager;
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
import forestry.api.apiculture.IBeeRoot;
import forestry.api.apiculture.IBeekeepingLogic;
import forestry.api.apiculture.IBeekeepingMode;
import forestry.api.arboriculture.EnumGermlingType;
import forestry.api.core.BiomeHelper;
import forestry.api.core.EnumHumidity;
import forestry.api.core.EnumTemperature;
import forestry.api.core.ForestryAPI;
import forestry.api.core.IErrorLogic;
import forestry.api.core.IErrorState;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IEffectData;
import forestry.api.genetics.IIndividual;
import forestry.apiculture.genetics.Bee;
import forestry.apiculture.genetics.alleles.AlleleEffectThrottled;
import forestry.core.errors.EnumErrorCode;
import forestry.plugins.PluginApiculture;
import gregtech.GT_Mod;
import gregtech.api.gui.modularui.GT_UIInfos;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.recipe.BasicUIProperties;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_ApiaryModifier;
import gregtech.api.util.GT_ApiaryUpgrade;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Client;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class GT_MetaTileEntity_IndustrialApiary extends GT_MetaTileEntity_BasicMachine
    implements IBeeHousing, IBeeHousingInventory, IErrorLogic, IBeeModifier, IBeeListener, IAddUIWidgets {

    public static final int beeCycleLength = 550;
    public static final int baseEUtUsage = 37;
    static final int queen = 5;
    static final int drone = 6;
    private static Field AlleleBeeEffectThrottledField;

    final IBeeRoot beeRoot = (IBeeRoot) AlleleManager.alleleRegistry.getSpeciesRoot("rootBees");

    public int mSpeed = 0;
    public boolean mLockedSpeed = true;
    public boolean mAutoQueen = true;

    private ItemStack usedQueen = null;
    private IBee usedQueenBee = null;
    private IEffectData[] effectData = new IEffectData[2];

    public GT_MetaTileEntity_IndustrialApiary(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            4,
            new String[] { "BEES GOES BRRRR", EnumChatFormatting.GRAY + AuthorKuba,
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

    public GT_MetaTileEntity_IndustrialApiary(String aName, int aTier, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, 4, aDescription, aTextures, 6, 9);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_IndustrialApiary(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide()) return true;
        if (!GT_Mod.gregtechproxy.mForceFreeFace) {
            openGUI(aBaseMetaTileEntity, aPlayer);
            return true;
        }
        for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            if (aBaseMetaTileEntity.getAirAtSide(side)) {
                openGUI(aBaseMetaTileEntity, aPlayer);
                return true;
            }
        }
        GT_Utility.sendChatToPlayer(aPlayer, "No free Side!");
        return true;
    }

    private void openGUI(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        IndustrialApiaryUI.open(
            aPlayer,
            aBaseMetaTileEntity.getWorld(),
            aBaseMetaTileEntity.getXCoord(),
            aBaseMetaTileEntity.getYCoord(),
            aBaseMetaTileEntity.getZCoord());
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

                final HashMap<GT_Utility.ItemId, ItemStack> pollen = new HashMap<>();

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
                                final GT_Utility.ItemId id = GT_Utility.ItemId.createNoCopy(s);
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

                final HashMap<GT_Utility.ItemId, Float> drops = new HashMap<>();
                final HashMap<GT_Utility.ItemId, ItemStack> dropstacks = new HashMap<>();

                for (Map.Entry<ItemStack, Float> entry : primary.getProductChances()
                    .entrySet()) {
                    final GT_Utility.ItemId id = GT_Utility.ItemId.createNoCopy(entry.getKey());
                    drops.merge(
                        id,
                        Bee.getFinalChance(entry.getValue(), speed, prodMod, 8f) * (float) entry.getKey().stackSize
                            * cycles,
                        Float::sum);
                    dropstacks.computeIfAbsent(id, k -> entry.getKey());
                }
                for (Map.Entry<ItemStack, Float> entry : secondary.getProductChances()
                    .entrySet()) {
                    final GT_Utility.ItemId id = GT_Utility.ItemId.createNoCopy(entry.getKey());
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
                            final GT_Utility.ItemId id = GT_Utility.ItemId.createNoCopy(entry.getKey());
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
                        final HashMap<GT_Utility.ItemId, ItemStack> drones = new HashMap<>(d.length);
                        for (IBee dr : d) {
                            final ItemStack drone = beeRoot.getMemberStack(dr, EnumBeeType.DRONE.ordinal());
                            breedingTracker.registerDrone(dr);
                            final GT_Utility.ItemId drid = GT_Utility.ItemId.createNoCopy(drone);
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

                for (Map.Entry<GT_Utility.ItemId, Float> entry : drops.entrySet()) {
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
        try {
            if (AlleleBeeEffectThrottledField == null) {
                AlleleBeeEffectThrottledField = AlleleEffectThrottled.class.getDeclaredField("throttle");
                AlleleBeeEffectThrottledField.setAccessible(true);
            }
            if (effect instanceof IAlleleBeeAcceleratableEffect) {
                effectData[0] = effect.validateStorage(effectData[0]);
                effectData[0] = ((IAlleleBeeAcceleratableEffect) effect).doEffectAccelerated(
                    genome,
                    effectData[0],
                    this,
                    usedBeeLife / (effect instanceof AlleleEffectThrottled
                        ? (float) AlleleBeeEffectThrottledField.getInt(effect)
                        : 1f));
            }

            if (!effect.isCombinable()) return;

            final IAlleleBeeEffect secondary = (IAlleleBeeEffect) genome.getInactiveAllele(EnumBeeChromosome.EFFECT);
            if (!secondary.isCombinable()) return;

            if (secondary instanceof IAlleleBeeAcceleratableEffect) {
                effectData[1] = secondary.validateStorage(effectData[1]);
                effectData[1] = ((IAlleleBeeAcceleratableEffect) secondary).doEffectAccelerated(
                    genome,
                    effectData[0],
                    this,
                    usedBeeLife / (secondary instanceof AlleleEffectThrottled
                        ? (float) AlleleBeeEffectThrottledField.getInt(secondary)
                        : 1f));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isClientSide()) {
            if (GT_Client.changeDetected == 4) {
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

            doDisplayThings();

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
                        final TileEntity tTileEntity2 = aBaseMetaTileEntity
                            .getTileEntityAtSide(aBaseMetaTileEntity.getFrontFacing());
                        final long tStoredEnergy = aBaseMetaTileEntity.getUniversalEnergyStored();
                        int tMaxStacks = (int) (tStoredEnergy / 64L);
                        if (tMaxStacks > mOutputItems.length) tMaxStacks = mOutputItems.length;

                        moveMultipleItemStacks(
                            aBaseMetaTileEntity,
                            tTileEntity2,
                            aBaseMetaTileEntity.getFrontFacing(),
                            aBaseMetaTileEntity.getBackFacing(),
                            null,
                            false,
                            (byte) 64,
                            (byte) 1,
                            (byte) 64,
                            (byte) 1,
                            tMaxStacks);
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
            if (!GT_ApiaryUpgrade.isUpgrade(aStack)) return false;
            for (int i = drone + 1; i < drone + 1 + 4; i++) {
                if (aIndex == i) continue;
                final ItemStack s = getStackInSlot(i);
                if (s == null) continue;
                if (GT_Utility.areStacksEqual(getStackInSlot(i), aStack)) return false;
                if (GT_ApiaryUpgrade.isUpgrade(aStack)) {
                    if (!GT_ApiaryUpgrade.getUpgrade(aStack)
                        .isAllowedToWorkWith(getStackInSlot(i))) return false;
                } else if (GT_ApiaryUpgrade.isUpgrade(s)) {
                    if (!GT_ApiaryUpgrade.getUpgrade(s)
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

    // Gets called on slot click //
    public void onInventoryUpdate(int aIndex) {
        if (aIndex > drone && aIndex < getOutputSlot()) updateModifiers();
    }

    @SideOnly(Side.CLIENT)
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
        final String flowerType = bee.getGenome()
            .getFlowerProvider()
            .getFlowerType();
        if (!this.flowerType.equals(flowerType)) flowercoords = null;
        if (flowercoords != null) {
            if (getWorld().getBlock(flowercoords.posX, flowercoords.posY, flowercoords.posZ) != flowerBlock
                || getWorld().getBlockMetadata(flowercoords.posX, flowercoords.posY, flowercoords.posZ)
                    != flowerBlockMeta)
                if (!FlowerManager.flowerRegistry
                    .isAcceptedFlower(flowerType, getWorld(), flowercoords.posX, flowercoords.posY, flowercoords.posZ))
                    flowercoords = null;
                else {
                    flowerBlock = getWorld().getBlock(flowercoords.posX, flowercoords.posY, flowercoords.posZ);
                    flowerBlockMeta = getWorld()
                        .getBlockMetadata(flowercoords.posX, flowercoords.posY, flowercoords.posZ);
                }
        }
        if (flowercoords == null) {
            flowercoords = FlowerManager.flowerRegistry.getAcceptedFlowerCoordinates(this, bee, flowerType);
            if (flowercoords != null) {
                flowerBlock = getWorld().getBlock(flowercoords.posX, flowercoords.posY, flowercoords.posZ);
                flowerBlockMeta = getWorld().getBlockMetadata(flowercoords.posX, flowercoords.posY, flowercoords.posZ);
                this.flowerType = flowerType;
            }
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
        final GT_ApiaryModifier mods = new GT_ApiaryModifier();
        for (int i = 2; i < 2 + 4; i++) {
            final ItemStack s = getInputAt(i);
            if (s == null) continue;
            if (GT_ApiaryUpgrade.isUpgrade(s)) {
                final GT_ApiaryUpgrade upgrade = GT_ApiaryUpgrade.getUpgrade(s);
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

    private static final String POWER_SOURCE_POWER = "GT5U.machines.powersource.power",
        CANCEL_PROCESS_TOOLTIP = "GT5U.machines.industrialapiary.cancel.tooltip",
        SPEED_TOOLTIP = "GT5U.machines.industrialapiary.speed.tooltip",
        SPEED_LOCKED_TOOLTIP = "GT5U.machines.industrialapiary.speedlocked.tooltip",
        INFO_TOOLTIP = "GT5U.machines.industrialapiary.info.tooltip",
        INFO_WITH_BEE_TOOLTIP = "GT5U.machines.industrialapiary.infoextended.tooltip",
        UPGRADE_TOOLTIP = "GT5U.machines.industrialapiary.upgradeslot.tooltip",
        AUTOQUEEN_TOOLTIP = "GT5U.machines.industrialapiary.autoqueen.tooltip";

    @Override
    public boolean useModularUI() {
        return true;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder
            .widget(
                new SlotWidget(new ApiarySlot(inventoryHandler, queen))
                    .setBackground(getGUITextureSet().getItemSlot(), GT_UITextures.OVERLAY_SLOT_BEE_QUEEN)
                    .setPos(36, 21))
            .widget(
                new SlotWidget(new ApiarySlot(inventoryHandler, drone))
                    .setBackground(getGUITextureSet().getItemSlot(), GT_UITextures.OVERLAY_SLOT_BEE_DRONE)
                    .setPos(36, 41))
            .widget(
                SlotGroup.ofItemHandler(inventoryHandler, 2)
                    .startFromSlot(7)
                    .endAtSlot(10)
                    .slotCreator(i -> new ApiarySlot(inventoryHandler, i))
                    .applyForWidget(
                        widget -> widget.setGTTooltip(() -> mTooltipCache.getData(UPGRADE_TOOLTIP))
                            .setTooltipShowUpDelay(TOOLTIP_DELAY))
                    .build()
                    .setPos(61, 23));

        super.addUIWidgets(builder, buildContext);

        builder.widget(
            new ButtonWidget().setOnClick((clickData, widget) -> cancelProcess())
                .setBackground(GT_UITextures.BUTTON_STANDARD, GT_UITextures.OVERLAY_BUTTON_CROSS)
                .setGTTooltip(() -> mTooltipCache.getData(CANCEL_PROCESS_TOOLTIP))
                .setTooltipShowUpDelay(TOOLTIP_DELAY)
                .setPos(7, 26)
                .setSize(18, 18))
            .widget(
                new CycleButtonWidget().setToggle(() -> mAutoQueen, x -> mAutoQueen = x)
                    .setTextureGetter(
                        i -> i == 0 ? GT_UITextures.OVERLAY_BUTTON_CROSS : GT_UITextures.OVERLAY_BUTTON_CHECKMARK)
                    .setGTTooltip(() -> mTooltipCache.getData(AUTOQUEEN_TOOLTIP))
                    .setTooltipShowUpDelay(TOOLTIP_DELAY)
                    .setPos(7, 44)
                    .setSize(18, 18)
                    .setBackground(GT_UITextures.BUTTON_STANDARD, GT_UITextures.OVERLAY_SLOT_BEE_QUEEN))
            .widget(
                new DrawableWidget().setDrawable(GT_UITextures.PICTURE_INFORMATION)
                    .setGTTooltip(() -> {
                        final String energyreq = GT_Utility.formatNumbers(
                            (int) ((float) GT_MetaTileEntity_IndustrialApiary.baseEUtUsage * getEnergyModifier()
                                * getAcceleration()) + getAdditionalEnergyUsage());
                        final String Temp = StatCollector.translateToLocal(getTemperature().getName());
                        final String Hum = StatCollector.translateToLocal(getHumidity().getName());
                        if (getUsedQueen() != null
                            && BeeManager.beeRoot.isMember(getUsedQueen(), EnumBeeType.QUEEN.ordinal())) {
                            final IBee bee = BeeManager.beeRoot.getMember(getUsedQueen());
                            if (bee.isAnalyzed()) {
                                final IBeeGenome genome = bee.getGenome();
                                final IBeeModifier mod = BeeManager.beeRoot.getBeekeepingMode(getWorld())
                                    .getBeeModifier();
                                final float tmod = getTerritoryModifier(null, 1f) * mod.getTerritoryModifier(null, 1f);
                                final int[] t = Arrays.stream(genome.getTerritory())
                                    .map(i -> (int) ((float) i * tmod))
                                    .toArray();
                                return mTooltipCache.getUncachedTooltipData(
                                    INFO_WITH_BEE_TOOLTIP,
                                    energyreq,
                                    Temp,
                                    Hum,
                                    genome.getSpeed(),
                                    getProductionModifier(null, 0f) + mod.getProductionModifier(null, 0f),
                                    Math.round(
                                        getFloweringModifier(null, 1f) * genome.getFlowering()
                                            * mod.getFloweringModifier(null, 1f)),
                                    Math.round(
                                        getLifespanModifier(null, null, 1f) * genome.getLifespan()
                                            * mod.getLifespanModifier(null, null, 1f)),
                                    t[0],
                                    t[1],
                                    t[2]);
                            }
                        }
                        return mTooltipCache.getUncachedTooltipData(INFO_TOOLTIP, energyreq, Temp, Hum);
                    })
                    .attachSyncer(
                        new FakeSyncWidget.ItemStackSyncer(() -> usedQueen, val -> usedQueen = val),
                        builder,
                        (widget, val) -> widget.notifyTooltipChange())
                    .setPos(163, 5)
                    .setSize(7, 18))
            .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                if (clickData.mouseButton == 0) {
                    if (mLockedSpeed) return;
                    if (!clickData.shift) {
                        mSpeed++;
                        if (mSpeed > getMaxSpeed()) mSpeed = 0;
                    } else {
                        mSpeed--;
                        if (mSpeed < 0) mSpeed = getMaxSpeed();
                    }
                } else if (clickData.mouseButton == 1) {
                    mLockedSpeed = !mLockedSpeed;
                    if (mLockedSpeed) mSpeed = getMaxSpeed();
                }
            })
                .setGTTooltip(
                    () -> mTooltipCache.getUncachedTooltipData(
                        mLockedSpeed ? SPEED_LOCKED_TOOLTIP : SPEED_TOOLTIP,
                        getAcceleration(),
                        GT_Utility.formatNumbers(getAdditionalEnergyUsage())))
                .attachSyncer(
                    new FakeSyncWidget.IntegerSyncer(() -> mSpeed, val -> mSpeed = val),
                    builder,
                    (widget, val) -> widget.notifyTooltipChange())
                .attachSyncer(
                    new FakeSyncWidget.BooleanSyncer(() -> mLockedSpeed, val -> mLockedSpeed = val),
                    builder,
                    (widget, val) -> widget.notifyTooltipChange())
                .setTooltipShowUpDelay(TOOLTIP_DELAY)
                .setBackground(GT_UITextures.PICTURE_SQUARE_LIGHT_GRAY)
                .setPos(25, 62)
                .setSize(18, 18))
            .widget(
                new TextWidget("x").setDefaultColor(COLOR_TEXT_GRAY.get())
                    .setPos(30, 63))
            .widget(
                TextWidget.dynamicString(() -> String.valueOf(1 << mSpeed))
                    // mSpeed is already synced
                    .setSynced(false)
                    .setDefaultColor(COLOR_TEXT_GRAY.get())
                    .setPos(26, 72));
    }

    private static final FallbackableUITexture progressBarTexture = GT_UITextures
        .fallbackableProgressbar("iapiary", GT_UITextures.PROGRESSBAR_ARROW);

    @Override
    protected BasicUIProperties getUIProperties() {
        return super.getUIProperties().toBuilder()
            .progressBarTexture(progressBarTexture)
            .progressBarPos(new Pos2d(70, 3))
            .build();
    }

    @Override
    protected SlotWidget createItemInputSlot(int index, IDrawable[] backgrounds, Pos2d pos) {
        // we have custom input slots
        return null;
    }

    @Override
    protected CycleButtonWidget createItemAutoOutputButton() {
        return (CycleButtonWidget) super.createItemAutoOutputButton().setPos(7, 62);
    }

    @Override
    protected CycleButtonWidget createFluidAutoOutputButton() {
        return null;
    }

    @Override
    protected SlotWidget createChargerSlot(int x, int y, String tooltipKey, Object[] tooltipArgs) {
        return (SlotWidget) super.createChargerSlot(x, y, tooltipKey, tooltipArgs).setPos(79, 62);
    }

    @Override
    protected DrawableWidget createErrorStatusArea(ModularWindow.Builder builder, IDrawable picture) {
        return (DrawableWidget) super.createErrorStatusArea(builder, picture).setPos(100, 62)
            .attachSyncer(
                new FakeSyncWidget.ListSyncer<>(() -> Arrays.asList(mErrorStates.toArray(new IErrorState[0])), val -> {
                    mErrorStates.clear();
                    mErrorStates.addAll(new HashSet<>(val));
                },
                    (buffer, val) -> buffer.writeShort(val.getID()),
                    buffer -> ForestryAPI.errorStateRegistry.getErrorState(buffer.readShort())),
                builder,
                (widget, val) -> widget.notifyTooltipChange());
    }

    @Override
    protected List<String> getErrorDescriptions() {
        if (!mErrorStates.isEmpty()) {
            return mErrorStates.stream()
                .map(state -> EnumChatFormatting.RED + StatCollector.translateToLocal("for." + state.getDescription()))
                .collect(Collectors.toList());
        } else if (mStuttering) {
            return mTooltipCache
                .getData(STALLED_STUTTERING_TOOLTIP, StatCollector.translateToLocal(POWER_SOURCE_POWER)).text;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    protected List<String> getErrorDescriptionsShift() {
        // Don't show shift tooltip of "Progress was lost"
        // as this machine does not lose progress
        return getErrorDescriptions();
    }

    private int getAcceleration() {
        return 1 << mSpeed;
    }

    private int getAdditionalEnergyUsage() {
        final int accelerated = getAcceleration();
        int energyusage = 0;
        if (accelerated == 2) energyusage = 32;
        else if (accelerated > 2) energyusage = 32 * accelerated << (mSpeed - 2);
        return energyusage;
    }

    private class ApiarySlot extends BaseSlot {

        public ApiarySlot(IItemHandlerModifiable inventory, int index) {
            super(inventory, index);
        }

        @Override
        public boolean isItemValidPhantom(ItemStack stack) {
            return super.isItemValidPhantom(stack) && getBaseMetaTileEntity().isItemValidForSlot(getSlotIndex(), stack);
        }

        @Override
        public void onSlotChanged() {
            super.onSlotChanged();
            onInventoryUpdate(getSlotIndex());
        }
    }

    private static final UIInfo<?, ?> IndustrialApiaryUI = GT_UIInfos.GTTileEntityUIFactory
        .apply(GT_ModularUIContainer_IndustrialApiary::new);

    private static class GT_ModularUIContainer_IndustrialApiary extends ModularUIContainer {

        public GT_ModularUIContainer_IndustrialApiary(ModularUIContext context, ModularWindow mainWindow) {
            super(context, mainWindow);
        }

        private final int playerInventorySlot = 36;

        @Override
        public ItemStack slotClick(int aSlotNumber, int aMouseclick, int aShifthold, EntityPlayer aPlayer) {
            if (!(aSlotNumber >= playerInventorySlot + 2 && aSlotNumber < playerInventorySlot + 2 + 4))
                return super.slotClick(aSlotNumber, aMouseclick, aShifthold, aPlayer);
            if (aShifthold == 5) return null;
            if (aShifthold != 0) return super.slotClick(aSlotNumber, aMouseclick, aShifthold, aPlayer);
            if (aMouseclick > 1) return super.slotClick(aSlotNumber, aMouseclick, aShifthold, aPlayer);
            final ItemStack s = aPlayer.inventory.getItemStack();
            if (s == null) return super.slotClick(aSlotNumber, aMouseclick, aShifthold, aPlayer);
            final Slot slot = getSlot(aSlotNumber);
            final ItemStack slotStack = slot.getStack();
            if (slotStack != null && !GT_Utility.areStacksEqual(slotStack, s)) return null; // super would replace item
            if (slotStack == null && !slot.isItemValid(s))
                return super.slotClick(aSlotNumber, aMouseclick, aShifthold, aPlayer);
            if (!GT_ApiaryUpgrade.isUpgrade(s)) return super.slotClick(aSlotNumber, aMouseclick, aShifthold, aPlayer);
            int max = GT_ApiaryUpgrade.getUpgrade(s)
                .getMaxNumber();
            if (slotStack != null) max = Math.max(0, max - slotStack.stackSize);
            max = Math.min(max, s.stackSize);
            if (max == 0) return null;
            if (aMouseclick == 1) max = 1;
            if (max == s.stackSize) return super.slotClick(aSlotNumber, aMouseclick, aShifthold, aPlayer);
            final ItemStack newStack = s.splitStack(s.stackSize - max);
            final ItemStack result = super.slotClick(aSlotNumber, aMouseclick, aShifthold, aPlayer);
            aPlayer.inventory.setItemStack(newStack);
            return result;
        }

        @Override
        public ItemStack transferStackInSlot(EntityPlayer aPlayer, int aSlotIndex) {
            final Slot s = getSlot(aSlotIndex);
            if (s == null) return super.transferStackInSlot(aPlayer, aSlotIndex);
            if (aSlotIndex >= playerInventorySlot) return super.transferStackInSlot(aPlayer, aSlotIndex);
            final ItemStack aStack = s.getStack();
            if (aStack == null) return super.transferStackInSlot(aPlayer, aSlotIndex);
            if (!GT_ApiaryUpgrade.isUpgrade(aStack)) return super.transferStackInSlot(aPlayer, aSlotIndex);
            for (int i = playerInventorySlot + 2; i < playerInventorySlot + 2 + 4; i++) {
                final Slot iSlot = getSlot(i);
                final ItemStack iStack = iSlot.getStack();
                if (iStack == null) {
                    if (!iSlot.isItemValid(aStack)) continue;
                } else {
                    if (!GT_Utility.areStacksEqual(aStack, iStack)) continue;
                }
                int max = GT_ApiaryUpgrade.getUpgrade(aStack)
                    .getMaxNumber();
                if (iStack == null) {
                    max = Math.min(max, aStack.stackSize);
                    final ItemStack newstack = aStack.splitStack(max);
                    iSlot.putStack(newstack);
                } else {
                    max = Math.max(0, max - iStack.stackSize);
                    max = Math.min(max, aStack.stackSize);
                    iStack.stackSize += max;
                    aStack.stackSize -= max;
                    iSlot.onSlotChanged();
                }
                if (aStack.stackSize == 0) s.putStack(null);
                else s.onSlotChanged();
                break;
            }
            return null;
        }
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currenttip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        if (tag.hasKey("queen")) {
            currenttip.add(
                "Current Queen: " + EnumChatFormatting.GREEN + StatCollector.translateToLocal(tag.getString("queen")));
        }
        if (tag.hasKey("dummyProduction")) {
            currenttip.add(
                "Effective Production: " + EnumChatFormatting.AQUA
                    + String.format("b^0.52 * %.2f", tag.getFloat("dummyProduction")));
        }
        if (tag.hasKey("errors")) {
            NBTTagCompound errorNbt = tag.getCompoundTag("errors");
            for (int i = 0; i < errorNbt.getInteger("size"); i++) {
                currenttip.add(
                    "Error: " + EnumChatFormatting.RED
                        + StatCollector.translateToLocal("for." + errorNbt.getString("e" + i)));
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
}

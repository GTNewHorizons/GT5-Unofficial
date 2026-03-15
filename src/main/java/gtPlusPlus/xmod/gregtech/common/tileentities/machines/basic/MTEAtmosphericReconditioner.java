package gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic;

import static gregtech.api.enums.GTValues.V;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.ArrayUtils;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicMachine;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gregtech.common.items.IDMetaTool01;
import gregtech.common.items.MetaGeneratedTool01;
import gregtech.common.pollution.Pollution;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.item.general.ItemAirFilter;
import gtPlusPlus.core.item.general.ItemBasicScrubberTurbine;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.gregtech.api.gui.GTPPUITextures;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTEAtmosphericReconditioner extends MTEBasicMachine {

    public int mPollutionReduction = 0;
    protected int mBaseEff = 2500;
    protected int mOptimalAirFlow = 0;
    protected boolean mHasPollution = false;
    protected int SLOT_ROTOR = 5;
    protected int SLOT_FILTER = 6;

    protected boolean mSaveRotor = false;

    public MTEAtmosphericReconditioner(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            2,
            "Making sure you don't live in Gwalior - Uses 2A",
            3,
            0,
            TextureFactory.of(
                TextureFactory.of(Textures.BlockIcons.OVERLAY_SIDE_MASSFAB_ACTIVE),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.OVERLAY_SIDE_MASSFAB_ACTIVE_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(Textures.BlockIcons.OVERLAY_SIDE_MASSFAB),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.OVERLAY_SIDE_MASSFAB_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(TexturesGtBlock.Overlay_MatterFab_Active),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.Overlay_MatterFab_Active_Glow)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(TexturesGtBlock.Overlay_MatterFab),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.Overlay_MatterFab_Glow)
                    .glow()
                    .build()),
            TextureFactory.of(TexturesGtBlock.Overlay_Machine_Vent_Fast),
            TextureFactory.of(TexturesGtBlock.Overlay_Machine_Vent),
            TextureFactory.of(
                TextureFactory.of(Textures.BlockIcons.OVERLAY_BOTTOM_MASSFAB_ACTIVE),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.OVERLAY_BOTTOM_MASSFAB_ACTIVE_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(Textures.BlockIcons.OVERLAY_BOTTOM_MASSFAB),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.OVERLAY_BOTTOM_MASSFAB_GLOW)
                    .glow()
                    .build()));
    }

    public MTEAtmosphericReconditioner(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 2, aDescription, aTextures, 2, 0);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEAtmosphericReconditioner(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public String[] getDescription() {

        boolean highTier = this.mTier >= 7;

        String[] A = ArrayUtils.addAll(
            this.mDescriptionArray,
            highTier ? "Will attempt to remove 1/4 pollution from 8 surrounding chunks" : "",
            highTier ? "If these chunks are not loaded, they will be ignored" : "",
            "Requires a turbine rotor and an Air Filter [T1/T2] to run.",
            "The turbine rotor must be manually inserted/replaced",
            "Can be configured with a soldering iron to change modes",
            "Low Efficiency: Removes half pollution, Turbine takes 50% dmg",
            "High Efficiency: Removes full pollution, Turbine takes 100% dmg",
            "Turbine Rotor will not break in LE mode",
            "Insert an equal tier Conveyor Module to enable automation");
        return A;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("mOptimalAirFlow", this.mOptimalAirFlow);
        aNBT.setBoolean("mSaveRotor", mSaveRotor);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.mOptimalAirFlow = aNBT.getInteger("mOptimalAirFlow");
        this.mSaveRotor = aNBT.getBoolean("mSaveRotor");
    }

    @Override
    public long maxAmperesIn() {
        return 2;
    }

    @Override
    public long getMinimumStoredEU() {
        return V[mTier] * 2;
    }

    @Override
    public long maxEUStore() {
        return V[mTier] * 256;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide()) {

            // Get Current Pollution Amount.
            int mCurrentPollution = getCurrentChunkPollution();
            boolean isIdle = true;

            // Get Inventory Item
            ItemStack stackRotor = this.mInventory[SLOT_ROTOR];
            ItemStack stackFilter = this.mInventory[SLOT_FILTER];

            // Power Drain
            long drainEU = maxEUInput() * maxAmperesIn();
            if (aBaseMetaTileEntity.isActive() && aBaseMetaTileEntity.getStoredEU() >= drainEU) {
                if (aBaseMetaTileEntity.decreaseStoredEnergyUnits(drainEU, false)) {
                    isIdle = false;
                } else {
                    aBaseMetaTileEntity.setActive(false);
                    this.sendSound((byte) -122);
                }
            } else if (!aBaseMetaTileEntity.isActive() && aBaseMetaTileEntity.getStoredEU() >= drainEU / 4) {
                if (aBaseMetaTileEntity.decreaseStoredEnergyUnits((drainEU / 4), false)) {
                    isIdle = false;
                } else {
                    aBaseMetaTileEntity.setActive(false);
                    this.sendSound((byte) -122);
                }
            } else {
                aBaseMetaTileEntity.setActive(false);
                this.sendSound((byte) -122);
            }

            // Only try once/sec.
            if (!isIdle && aTick % 20L == 0L) {

                for (int i = 0; i < this.mInventory.length; i++) {
                    ItemStack aSlotContent = this.mInventory[i];
                    if (aSlotContent != null) {
                        Logger.INFO("Found " + aSlotContent.getDisplayName() + " in slot " + i);
                    }
                }

                for (int i = 0; i < this.mInventory.length; i++) {
                    if (hasRotor(this.mInventory[i])) {
                        Logger.INFO("Found Rotor in slot " + i);
                        break;
                    }
                }
                for (int i = 0; i < this.mInventory.length; i++) {
                    if (hasAirFilter(this.mInventory[i])) {
                        Logger.INFO("Found Filter in slot " + i);
                        break;
                    }
                }

                // Check if machine can work.
                if ((aBaseMetaTileEntity.isAllowedToWork())) {
                    Logger.INFO("Can work.");

                    // Enable machine animation/graphic
                    if (hasRotor(stackRotor) && hasAirFilter(stackFilter) && this.mHasPollution) {
                        if (!this.getBaseMetaTileEntity()
                            .isActive()) {
                            Logger.INFO("Set Active.");
                            aBaseMetaTileEntity.setActive(true);
                        }
                    } else if (!this.mHasPollution || mCurrentPollution <= 0
                        || stackRotor == null
                        || stackFilter == null
                        || !hasRotor(stackRotor)
                        || !hasAirFilter(stackFilter)) {
                            if (!this.getBaseMetaTileEntity()
                                .isActive()) {
                                Logger.INFO("Set Inactive.");
                                aBaseMetaTileEntity.setActive(false);
                                this.sendSound((byte) -122);
                            }
                        }

                    // If Active.
                    if (aBaseMetaTileEntity.isActive()) {
                        Logger.INFO("Doing something.");

                        // Do nothing if there is no pollution.
                        if (this.mHasPollution && mCurrentPollution > 0) {
                            Logger
                                .INFO("Has Pollution? " + mHasPollution + ", Current Pollution: " + mCurrentPollution);

                            // Use a Turbine
                            if (hasRotor(stackRotor) && hasAirFilter(stackFilter)) {
                                Logger.INFO("Found Turbine.");

                                mBaseEff = getBaseEfficiency(stackRotor);
                                mOptimalAirFlow = getOptimalAirFlow(stackRotor);

                                // Make sure we have a valid Turbine and Eff/Airflow
                                if (this.mBaseEff > 0 && this.mOptimalAirFlow > 0) {
                                    // Utils.LOG_WARNING("Pollution Cleaner [5]");

                                    // Log Debug information.
                                    Logger.INFO("mBaseEff[1]:" + mBaseEff);
                                    Logger.INFO("mOptimalAirFlow[1]:" + mOptimalAirFlow);

                                    // Calculate The Voltage we are running
                                    byte tTier = (byte) Math.max(1, GTUtility.getTier(drainEU));

                                    // Check Sides for Air,
                                    // More air means more pollution processing.
                                    int mAirSides = getFreeSpaces();

                                    int reduction = 0;

                                    // If no sides are free, how will you process the atmosphere?
                                    if (mAirSides > 0) {
                                        reduction += (((Math.max((tTier - 2), 1) * 2) * 50) * mAirSides); // Was
                                                                                                          // originally
                                                                                                          // *100
                                        Logger.INFO("mPollutionReduction[1]:" + reduction);

                                        // I stole this code
                                        reduction = (MathUtils.safeInt((long) reduction * this.mBaseEff) / 100000)
                                            * mAirSides
                                            * Math.max((tTier - 2), 1);
                                        Logger.INFO("reduction[2]:" + reduction);
                                        reduction = MathUtils.safeInt(((long) reduction / 100) * this.mOptimalAirFlow);
                                        Logger.INFO("reduction[3]:" + reduction);

                                        mPollutionReduction = reduction;

                                        // Set a temp to remove variable to aleviate duplicate code.
                                        int toRemove = 0;

                                        Logger.INFO("mCurrentPollution[4]:" + mCurrentPollution);
                                        Logger.INFO("mCurrentPollution[5]:" + reduction);

                                        toRemove = Math.min(reduction, mCurrentPollution) / 2;
                                        Logger.INFO("mCurrentPollution[6]:" + toRemove);

                                        // We are good to clean
                                        if (toRemove > 0) {
                                            if (damageTurbineRotor() && damageAirFilter()) {
                                                Logger.INFO("Removing " + toRemove + " pollution");
                                                removePollution(mSaveRotor ? (toRemove / 2) : toRemove);
                                                Logger.INFO("mNewPollution[4]:" + getCurrentChunkPollution());
                                            } else {
                                                Logger.INFO("Could not damage turbine rotor or Air Filter.");
                                                aBaseMetaTileEntity.setActive(false);
                                            }
                                        } // End of pollution removal block.
                                    } // End of valid air sides block.
                                } // End of valid toolstats block.
                            } // End of correct inventory item block.
                        }
                    } else if (!aBaseMetaTileEntity.isActive()) {
                        return;
                    }
                } // End of can work block.

            } // End of 1/sec action block.
            else {

                if (hasRotor(stackRotor) && hasAirFilter(stackFilter)
                    && this.mHasPollution
                    && !isIdle
                    && aBaseMetaTileEntity.isAllowedToWork()) {
                    aBaseMetaTileEntity.setActive(true);
                } else if (isIdle || !this.mHasPollution
                    || mCurrentPollution <= 0
                    || stackRotor == null
                    || stackFilter == null
                    || !hasRotor(stackRotor)
                    || !hasAirFilter(stackFilter)) {
                        aBaseMetaTileEntity.setActive(false);
                    }
            }
            if (this.getBaseMetaTileEntity()
                .isActive()) {
                if (MathUtils.randInt(0, 5) <= 2) {
                    this.sendSound((byte) -120);
                }
            }
        } // End of is serverside block.
    }

    public int getCurrentChunkPollution() {
        int mCurrentChunkPollution = 0;
        if (this.mTier < 7) {
            mCurrentChunkPollution = Pollution.getPollution(getBaseMetaTileEntity());
        } else {
            ArrayList<Chunk> aSurrounding = new ArrayList<>();
            World aWorld = this.getBaseMetaTileEntity()
                .getWorld();
            int xPos = this.getBaseMetaTileEntity()
                .getXCoord();
            int zPos = this.getBaseMetaTileEntity()
                .getZCoord();
            Chunk a1 = aWorld.getChunkFromBlockCoords(xPos - 32, zPos - 32);
            Chunk a2 = aWorld.getChunkFromBlockCoords(xPos - 32, zPos);
            Chunk a3 = aWorld.getChunkFromBlockCoords(xPos - 32, zPos + 32);
            Chunk b1 = aWorld.getChunkFromBlockCoords(xPos, zPos - 32);
            Chunk b2 = aWorld.getChunkFromBlockCoords(xPos, zPos);
            Chunk b3 = aWorld.getChunkFromBlockCoords(xPos, zPos + 32);
            Chunk c1 = aWorld.getChunkFromBlockCoords(xPos + 32, zPos - 32);
            Chunk c2 = aWorld.getChunkFromBlockCoords(xPos + 32, zPos);
            Chunk c3 = aWorld.getChunkFromBlockCoords(xPos + 32, zPos + 32);
            aSurrounding.add(a1);
            aSurrounding.add(a2);
            aSurrounding.add(a3);
            aSurrounding.add(b1);
            aSurrounding.add(b2);
            aSurrounding.add(b3);
            aSurrounding.add(c1);
            aSurrounding.add(c2);
            aSurrounding.add(c3);
            for (Chunk r : aSurrounding) {
                mCurrentChunkPollution += getPollutionInChunk(r);
            }
        }
        mHasPollution = mCurrentChunkPollution > 0;
        return mCurrentChunkPollution;
    }

    public int getPollutionInChunk(Chunk aChunk) {
        int mCurrentChunkPollution = Pollution.getPollution(aChunk);
        mHasPollution = mCurrentChunkPollution > 0;
        return mCurrentChunkPollution;
    }

    public boolean hasRotor(ItemStack rotorStack) {
        if (rotorStack != null) {
            if (rotorStack.getItem() instanceof ItemBasicScrubberTurbine) {
                // Logger.INFO("Found Basic Turbine Rotor.");
                return true;
            } else if (rotorStack.getItem() instanceof MetaGeneratedTool && rotorStack.getItemDamage() >= 170
                && rotorStack.getItemDamage() <= 179) {
                    // Logger.INFO("Found Turbine Rotor.");
                    return true;
                }
        }
        return false;
    }

    public boolean damageTurbineRotor() {
        try {

            boolean creativeRotor = false;
            ItemStack rotorStack = this.mInventory[SLOT_ROTOR];
            if (rotorStack == null) {
                return false;
            } else if (rotorStack.getItem() instanceof ItemBasicScrubberTurbine) {
                long currentUse = ItemBasicScrubberTurbine.getFilterDamage(rotorStack);
                // Remove broken Filter
                if (rotorStack.getItemDamage() == 0 && currentUse >= 2000 - 10) {
                    Logger.INFO("Depleting ItemBasicScrubberTurbine T1");
                    this.mInventory[this.SLOT_FILTER] = null;
                    return false;
                } else if (rotorStack.getItemDamage() == 1 && currentUse >= 4000 - 10) {
                    Logger.INFO("Depleting ItemBasicScrubberTurbine T2");
                    this.mInventory[this.SLOT_FILTER] = null;
                    return false;
                } else if (rotorStack.getItemDamage() == 2 && currentUse >= 6000 - 10) {
                    Logger.INFO("Depleting ItemBasicScrubberTurbine T3");
                    this.mInventory[this.SLOT_FILTER] = null;
                    return false;
                } else {
                    // Do Damage
                    Logger.INFO("Damaging ItemBasicScrubberTurbine");
                    ItemBasicScrubberTurbine.setFilterDamage(rotorStack, currentUse + 10);
                    Logger.INFO("Rotor Damage: " + currentUse);
                    return true;
                }
            } else if (rotorStack.getItem() instanceof MetaGeneratedTool01) {
                Materials t1 = MetaGeneratedTool.getPrimaryMaterial(rotorStack);
                Materials t2 = MetaGeneratedTool.getSecondaryMaterial(rotorStack);
                if (t1 == Materials._NULL && t2 == Materials._NULL) {
                    Logger.INFO("Found creative rotor.");
                    creativeRotor = true;
                }
            } else {
                Logger.INFO("Bad item in rotor slot.");
                return false;
            }

            if (mInventory[SLOT_ROTOR].getItem() instanceof MetaGeneratedTool01
                && ((MetaGeneratedTool) mInventory[SLOT_ROTOR].getItem()).getToolStats(mInventory[SLOT_ROTOR])
                    .getSpeedMultiplier() > 0
                && MetaGeneratedTool.getPrimaryMaterial(mInventory[SLOT_ROTOR]).mToolSpeed > 0) {

                long damageValue = (long) Math
                    .floor(Math.abs(MathUtils.randFloat(1, 2) - MathUtils.randFloat(1, 3)) * (1 + 3 - 1) + 1);
                double fDam = Math
                    .floor(Math.abs(MathUtils.randFloat(1f, 2f) - MathUtils.randFloat(1f, 2f)) * (1f + 2f - 1f) + 1f);
                damageValue -= fDam;

                // Logger.INFO("Trying to do "+damageValue+" damage to the rotor. ["+fDam+"]");
                /*
                 * Materials M1 = GT_MetaGenerated_Tool.getPrimaryMaterial(this.mInventory[this.SLOT_ROTOR]); Materials
                 * M2 = GT_MetaGenerated_Tool.getSecondaryMaterial(this.mInventory[this.SLOT_ROTOR]);
                 * Logger.INFO("Trying to do "+damageValue+" damage to the rotor. [2]");
                 */

                // Damage Rotor
                // int rotorDurability = this.mInventory[this.SLOT_ROTOR].getItemDamage();
                long rotorDamage = creativeRotor ? 0
                    : MetaGeneratedTool.getToolDamage(this.mInventory[this.SLOT_ROTOR]);
                long rotorDurabilityMax = creativeRotor ? Integer.MAX_VALUE
                    : MetaGeneratedTool.getToolMaxDamage(this.mInventory[this.SLOT_ROTOR]);
                long rotorDurability = (rotorDurabilityMax - rotorDamage);
                Logger.INFO(
                    "Rotor Damage: " + rotorDamage
                        + " | Max Durability: "
                        + rotorDurabilityMax
                        + " | "
                        + " Remaining Durability: "
                        + rotorDurability);
                if (rotorDurability >= damageValue) {

                    if (!mSaveRotor) {
                        Logger.INFO("Damaging Rotor.");

                        if (!creativeRotor) GTModHandler
                            .damageOrDechargeItem(this.mInventory[this.SLOT_ROTOR], (int) damageValue, 0, null);

                        long tempDur = MetaGeneratedTool.getToolDamage(this.mInventory[this.SLOT_ROTOR]);
                        if (tempDur < rotorDurabilityMax) {
                            return true;
                        } else {
                            rotorDurability = 0;
                        }
                    } else {
                        Logger.INFO("Damaging Rotor.");
                        if (rotorDurability > 1000) {
                            if (!creativeRotor) GTModHandler
                                .damageOrDechargeItem(this.mInventory[this.SLOT_ROTOR], (int) damageValue / 2, 0, null);
                            long tempDur = MetaGeneratedTool.getToolDamage(this.mInventory[this.SLOT_ROTOR]);
                            if (tempDur < rotorDurabilityMax) {
                                return true;
                            } else {
                                rotorDurability = 0;
                            }
                        }
                    }
                }

                if (rotorDurability <= 0 && !mSaveRotor && !creativeRotor) {
                    Logger.INFO("Destroying Rotor.");
                    this.mInventory[this.SLOT_ROTOR] = null;
                    return false;
                } else if (rotorDurability <= 0 && mSaveRotor) {
                    Logger.INFO("Saving Rotor.");
                    return false;
                }

            } else {
                Logger.INFO("Bad Rotor.");
                return false;
            }
        } catch (Exception t) {
            t.printStackTrace();
        }
        return false;
    }

    public int getFreeSpaces() {
        int mAir = 0;
        IGregTechTileEntity aBaseMetaTileEntity = this.getBaseMetaTileEntity();
        if (aBaseMetaTileEntity.getAirOffset(1, 0, 0)) {
            mAir++;
        }
        if (aBaseMetaTileEntity.getAirOffset(-1, 0, 0)) {
            mAir++;
        }
        if (aBaseMetaTileEntity.getAirOffset(0, 0, 1)) {
            mAir++;
        }
        if (aBaseMetaTileEntity.getAirOffset(0, 0, -1)) {
            mAir++;
        }
        if (aBaseMetaTileEntity.getAirOffset(0, 1, 0)) {
            mAir++;
        }
        if (aBaseMetaTileEntity.getAirOffset(0, -1, 0)) {
            mAir++;
        }
        return mAir;
    }

    public boolean removePollution(int toRemove) {

        if (this.getBaseMetaTileEntity() == null || this.getBaseMetaTileEntity()
            .getWorld() == null) {
            return false;
        }

        if (this.mTier < 7) {
            int startPollution = getCurrentChunkPollution();
            Logger.INFO("Current Chunk Pollution: " + startPollution);
            Pollution.addPollution(this.getBaseMetaTileEntity(), -toRemove);
            int after = getCurrentChunkPollution();
            Logger.INFO("Current Chunk Pollution: " + after);
            return (after < startPollution);
        } else {
            int chunksWithRemoval = 0;
            int totalRemoved = 0;
            ArrayList<Chunk> aSurrounding = new ArrayList<>();
            Chunk aThisChunk = this.getBaseMetaTileEntity()
                .getWorld()
                .getChunkFromBlockCoords(
                    this.getBaseMetaTileEntity()
                        .getXCoord(),
                    this.getBaseMetaTileEntity()
                        .getZCoord());
            int mainChunkX = aThisChunk.xPosition;
            int mainChunkZ = aThisChunk.zPosition;

            World aWorld = this.getBaseMetaTileEntity()
                .getWorld();
            int xPos = this.getBaseMetaTileEntity()
                .getXCoord();
            int zPos = this.getBaseMetaTileEntity()
                .getZCoord();

            Chunk a1 = aWorld.getChunkFromBlockCoords(xPos - 32, zPos - 32);
            Chunk a2 = aWorld.getChunkFromBlockCoords(xPos - 32, zPos);
            Chunk a3 = aWorld.getChunkFromBlockCoords(xPos - 32, zPos + 32);
            Chunk b1 = aWorld.getChunkFromBlockCoords(xPos, zPos - 32);
            Chunk b2 = aWorld.getChunkFromBlockCoords(xPos, zPos);
            Chunk b3 = aWorld.getChunkFromBlockCoords(xPos, zPos + 32);
            Chunk c1 = aWorld.getChunkFromBlockCoords(xPos + 32, zPos - 32);
            Chunk c2 = aWorld.getChunkFromBlockCoords(xPos + 32, zPos);
            Chunk c3 = aWorld.getChunkFromBlockCoords(xPos + 32, zPos + 32);

            aSurrounding.add(a1);
            aSurrounding.add(a2);
            aSurrounding.add(a3);
            aSurrounding.add(b1);
            aSurrounding.add(b2);
            aSurrounding.add(b3);
            aSurrounding.add(c1);
            aSurrounding.add(c2);
            aSurrounding.add(c3);

            for (Chunk r : aSurrounding) {
                if (!r.isChunkLoaded) {
                    continue;
                }

                int startPollution = getPollutionInChunk(r);
                if (startPollution == 0) {
                    continue;
                }

                Logger.INFO(
                    "Trying to remove pollution from chunk " + r.xPosition
                        + ", "
                        + r.zPosition
                        + " | "
                        + startPollution);
                int after = 0;
                boolean isMainChunk = r.isAtLocation(mainChunkX, mainChunkZ);

                int removal = Math.max(0, !isMainChunk ? (toRemove / 4) : toRemove);
                if (removePollution(r, removal)) {
                    chunksWithRemoval++;
                    after = getPollutionInChunk(r);
                } else {
                    after = 0;
                }
                if (startPollution - after > 0) {
                    totalRemoved += (startPollution - after);
                }
                Logger.INFO(
                    "Removed " + (startPollution - after)
                        + " pollution from chunk "
                        + r.xPosition
                        + ", "
                        + r.zPosition
                        + " | "
                        + after);
            }
            return totalRemoved > 0 && chunksWithRemoval > 0;
        }
    }

    public boolean removePollution(Chunk aChunk, int toRemove) {
        int before = getCurrentChunkPollution();
        Pollution.addPollution(aChunk, -toRemove);
        int after = getCurrentChunkPollution();
        return (after < before);
    }

    public boolean hasAirFilter(ItemStack filter) {
        if (filter == null) {
            return false;
        }
        return filter.getItem() instanceof ItemAirFilter;
    }

    public boolean damageAirFilter() {
        ItemStack filter = this.mInventory[this.SLOT_FILTER];
        if (filter == null) {
            return false;
        }

        boolean creativeRotor = false;
        ItemStack rotorStack = this.mInventory[SLOT_ROTOR];
        if (rotorStack != null) {
            if (rotorStack.getItem() instanceof MetaGeneratedTool01) {
                Materials t1 = MetaGeneratedTool.getPrimaryMaterial(rotorStack);
                Materials t2 = MetaGeneratedTool.getSecondaryMaterial(rotorStack);
                if (t1 == Materials._NULL && t2 == Materials._NULL) {
                    creativeRotor = true;
                }
            }
        }

        if (creativeRotor) {
            return true;
        }

        if (filter.getItem() instanceof ItemAirFilter) {

            long currentUse = ItemAirFilter.getFilterDamage(filter);

            // Remove broken Filter
            if (filter.getItemDamage() == 0 && currentUse >= 50 - 1) {
                this.mInventory[this.SLOT_FILTER] = null;
                return false;
            } else if (filter.getItemDamage() == 1 && currentUse >= 2500 - 1) {
                this.mInventory[this.SLOT_FILTER] = null;
                return false;
            } else {
                // Do Damage
                ItemAirFilter.setFilterDamage(filter, currentUse + 1);
                Logger.INFO("Filter Damage: " + currentUse);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canInsertItem(int aIndex, ItemStack aStack, int ordinalSide) {
        if (aIndex == SLOT_FILTER) {
            if (aStack.getItem() instanceof ItemAirFilter) {
                Logger.INFO("Inserting Air Filter into " + aIndex);
                return true;
            }
        }
        if (aIndex == SLOT_ROTOR) {
            if (this.mInventory[7] != null) {
                Logger.INFO("Found conveyor, can automate turbines. Inserting into " + aIndex);
                if (aStack.getItem() instanceof ItemBasicScrubberTurbine) {
                    return true;
                }
                return aStack.getItem() instanceof MetaGeneratedTool && aStack.getItemDamage() >= 170
                    && aStack.getItemDamage() <= 179;
            }
        }
        return false;
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        super.onScrewdriverRightClick(side, aPlayer, aX, aY, aZ, aTool);
    }

    @Override
    public void doSound(byte aIndex, double aX, double aY, double aZ) {
        if (aIndex == -120) {
            GTUtility.doSoundAtClient(SoundResource.IC2_TOOLS_BATTERY_USE, MathUtils.randInt(5, 50), 0.05F, aX, aY, aZ);
        } else {
            super.doSound((byte) 0, aX, aY, aZ);
        }
    }

    @Override
    public String[] getInfoData() {
        ArrayList<String> aTooltipSuper = new ArrayList<>(Arrays.asList(super.getInfoData()));
        int mAirSides = getFreeSpaces();
        int reduction = 0;

        try {
            long tVoltage = maxEUInput();
            byte tTier = (byte) Math.max(1, GTUtility.getTier(tVoltage));
            reduction += (((Math.max((tTier - 2), 1) * 2) * 50) * mAirSides);
            reduction = (MathUtils.safeInt((long) reduction * this.mBaseEff) / 100000) * mAirSides
                * Math.max((tTier - 2), 1);
            reduction = MathUtils.safeInt(((long) reduction / 100) * this.mOptimalAirFlow);

            aTooltipSuper.add(
                StatCollector.translateToLocalFormatted(
                    "gtpp.infodata.atmospheric_reconditioner.maximum_pollution_removed",
                    reduction));
        } catch (Exception t) {
            aTooltipSuper.add(
                StatCollector.translateToLocalFormatted(
                    "gtpp.infodata.atmospheric_reconditioner.maximum_pollution_removed",
                    mPollutionReduction));
        }
        aTooltipSuper.add(
            StatCollector.translateToLocalFormatted("gtpp.infodata.atmospheric_reconditioner.air_sides", mAirSides));

        String[] mBuiltOutput = new String[aTooltipSuper.size()];
        int aIndex = 0;
        for (String i : aTooltipSuper) {
            mBuiltOutput[aIndex++] = i;
        }

        return mBuiltOutput;
    }

    @Override
    public boolean allowCoverOnSide(ForgeDirection side, ItemStack coverItem) {
        if (side.offsetY != 0) {
            return false;
        }
        return super.allowCoverOnSide(side, coverItem);
    }

    @Override
    public ITexture[] getTopFacingInactive(byte aColor) {
        return super.getTopFacingInactive(aColor);
    }

    @Override
    public void setItemNBT(NBTTagCompound aNBT) {
        if (mOptimalAirFlow > 0) aNBT.setInteger("mOptimalAirFlow", this.mOptimalAirFlow);
        if (mSaveRotor) aNBT.setBoolean("mSaveRotor", true);
        super.setItemNBT(aNBT);
    }

    private static ItemStack[] sGregTurbines;

    public static ItemStack getTieredTurbine(int aTier) {
        if (sGregTurbines == null) {
            sGregTurbines = new ItemStack[3];
            sGregTurbines[0] = MetaGeneratedTool.sInstances.get("gt.metatool.01")
                .getToolWithStats(IDMetaTool01.TURBINE_SMALL.ID, 1, Materials.Iron, Materials.Iron, null);
            sGregTurbines[1] = MetaGeneratedTool.sInstances.get("gt.metatool.01")
                .getToolWithStats(IDMetaTool01.TURBINE_SMALL.ID, 1, Materials.Bronze, Materials.Bronze, null);
            sGregTurbines[2] = MetaGeneratedTool.sInstances.get("gt.metatool.01")
                .getToolWithStats(IDMetaTool01.TURBINE_SMALL.ID, 1, Materials.Steel, Materials.Steel, null);
        } else {
            return sGregTurbines[aTier];
        }

        return null;
    }

    public int getBaseEfficiency(ItemStack aStackRotor) {
        if (aStackRotor.getItem() instanceof ItemBasicScrubberTurbine) {
            return getBaseEfficiency(getTieredTurbine(aStackRotor.getItemDamage()));
        }
        return (int) ((50.0F + (10.0F * ((MetaGeneratedTool) aStackRotor.getItem()).getToolCombatDamage(aStackRotor)))
            * 100);
    }

    public int getOptimalAirFlow(ItemStack aStackRotor) {
        if (aStackRotor.getItem() instanceof ItemBasicScrubberTurbine) {
            return getOptimalAirFlow(getTieredTurbine(aStackRotor.getItemDamage()));
        }
        return (int) Math.max(
            Float.MIN_NORMAL,
            ((MetaGeneratedTool) aStackRotor.getItem()).getToolStats(aStackRotor)
                .getSpeedMultiplier() * MetaGeneratedTool.getPrimaryMaterial(aStackRotor).mToolSpeed * 50);
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(new SlotWidget(inventoryHandler, SLOT_ROTOR).setFilter(stack -> {
            if (stack.getItem() instanceof ItemBasicScrubberTurbine) {
                return true;
            }
            return stack.getItem() instanceof MetaGeneratedTool && stack.getItemDamage() >= 170
                && stack.getItemDamage() <= 179;
        })
            .setBackground(getGUITextureSet().getItemSlot(), GTPPUITextures.OVERLAY_SLOT_TURBINE)
            .setPos(52, 24))
            .widget(
                new SlotWidget(inventoryHandler, SLOT_FILTER)
                    .setFilter(stack -> stack.getItem() instanceof ItemAirFilter)
                    .setBackground(getGUITextureSet().getItemSlot(), GTUITextures.OVERLAY_SLOT_RECYCLE)
                    .setPos(106, 24))
            .widget(
                new SlotWidget(inventoryHandler, 7).setFilter(stack -> checkConveyor(stack, mTier))
                    .setPos(124, 62));
        builder.widget(
            new DrawableWidget().setDrawable(GTUITextures.PICTURE_INFORMATION)
                .dynamicTooltip(
                    () -> Collections.singletonList(
                        StatCollector.translateToLocalFormatted(
                            "gtpp.gui.atmospheric_reconditioner.tooltip.reduction",
                            mPollutionReduction)))
                .attachSyncer(
                    new FakeSyncWidget.IntegerSyncer(() -> mPollutionReduction, val -> mPollutionReduction = val),
                    builder,
                    (widget, val) -> widget.notifyTooltipChange())
                .setPos(163, 5)
                .setSize(7, 18));
    }

    private static boolean checkConveyor(ItemStack stack, int tier) {
        return (switch (tier) {
            case 1 -> ItemList.Conveyor_Module_LV;
            case 2 -> ItemList.Conveyor_Module_MV;
            case 3 -> ItemList.Conveyor_Module_HV;
            case 4 -> ItemList.Conveyor_Module_EV;
            case 5 -> ItemList.Conveyor_Module_IV;
            case 6 -> ItemList.Conveyor_Module_LuV;
            case 7 -> ItemList.Conveyor_Module_ZPM;
            case 8 -> ItemList.Conveyor_Module_UV;
            case 9 -> ItemList.Conveyor_Module_UHV;
            default -> throw new IllegalArgumentException("Tier not allowed for Atmospheric Reconditioner!");
        }).isStackEqual(stack, false, true);
    }
}

package gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic;

import static gregtech.api.enums.GT_Values.V;

import java.util.Collections;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.ArrayUtils;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gregtech.common.items.GT_MetaGenerated_Tool_01;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.item.general.ItemAirFilter;
import gtPlusPlus.core.item.general.ItemBasicScrubberTurbine;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.gregtech.PollutionUtils;
import gtPlusPlus.xmod.gregtech.api.gui.GTPP_UITextures;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GregtechMetaAtmosphericReconditioner extends GT_MetaTileEntity_BasicMachine {

    public int mPollutionReduction = 0;
    protected int mBaseEff = 2500;
    protected int mOptimalAirFlow = 0;
    protected boolean mHasPollution = false;
    protected int SLOT_ROTOR = 5;
    protected int SLOT_FILTER = 6;
    protected static boolean mPollutionEnabled = true;

    protected boolean mSaveRotor = false;

    public GregtechMetaAtmosphericReconditioner(int aID, String aName, String aNameRegional, int aTier) {
        super(
                aID,
                aName,
                aNameRegional,
                aTier,
                2,
                "Making sure you don't live in Gwalior - Uses 2A",
                3,
                0,
                new ITexture[] { new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_SIDE_MASSFAB_ACTIVE),
                        new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_SIDE_MASSFAB),
                        new GT_RenderedTexture(TexturesGtBlock.Overlay_MatterFab_Active),
                        new GT_RenderedTexture(TexturesGtBlock.Overlay_MatterFab),
                        new GT_RenderedTexture(TexturesGtBlock.Overlay_Machine_Vent_Fast),
                        new GT_RenderedTexture(TexturesGtBlock.Overlay_Machine_Vent),
                        new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_BOTTOM_MASSFAB_ACTIVE),
                        new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_BOTTOM_MASSFAB) });
        mPollutionEnabled = PollutionUtils.isPollutionEnabled();
    }

    public GregtechMetaAtmosphericReconditioner(String aName, int aTier, String[] aDescription,
            ITexture[][][] aTextures) {
        super(aName, aTier, 2, aDescription, aTextures, 2, 0);
        mPollutionEnabled = PollutionUtils.isPollutionEnabled();
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GregtechMetaAtmosphericReconditioner(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
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
        if (!mPollutionEnabled) {
            String[] B = new String[] { "===============================================",
                    "Pollution is disabled, scrubbers will now have a bonus use",
                    "They are now able to remove ALL lingering pollution as GT ignores it",
                    "and it will linger forever!", "===============================================", };
            A = ArrayUtils.addAll(A, B);
        }
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
    public long maxEUInput() {
        return V[mTier];
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
                        if (!this.getBaseMetaTileEntity().isActive()) {
                            Logger.INFO("Set Active.");
                            aBaseMetaTileEntity.setActive(true);
                        }
                    } else if (!this.mHasPollution || mCurrentPollution <= 0
                            || stackRotor == null
                            || stackFilter == null
                            || !hasRotor(stackRotor)
                            || !hasAirFilter(stackFilter)) {
                                if (!this.getBaseMetaTileEntity().isActive()) {
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
                            Logger.INFO(
                                    "Has Pollution? " + mHasPollution + ", Current Pollution: " + mCurrentPollution);

                            // Only check every 30s.
                            if (!isIdle && aTick % (20L * 30) == 0L) {
                                mPollutionEnabled = PollutionUtils.isPollutionEnabled();
                                // Clear out pollution if it's disabled, because I am a nice gal.
                                if (!PollutionUtils.isPollutionEnabled()) {
                                    PollutionUtils.nullifyPollution(this.getBaseMetaTileEntity());
                                }
                            }

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
                                    long tVoltage = drainEU;
                                    byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));

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
                                        if (reduction <= mCurrentPollution) {
                                            // Clean some Air.
                                            toRemove = reduction;
                                        } else {
                                            // Makes sure we don't get negative pollution.
                                            toRemove = mCurrentPollution;
                                        }

                                        toRemove = toRemove / 2;
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
                            else {
                                // Utils.LOG_WARNING("Wrong Tool metaitem Found.");
                            }
                        }
                    } else if (!aBaseMetaTileEntity.isActive()) {
                        return;
                    }
                } // End of can work block.
                else { // Disable Machine.
                       // aBaseMetaTileEntity.setActive(false);
                }
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
            if (this.getBaseMetaTileEntity().isActive()) {
                if (MathUtils.randInt(0, 5) <= 2) {
                    this.sendSound((byte) -120);
                }
            }
        } // End of is serverside block.
    }

    public int getCurrentChunkPollution() {
        int mCurrentChunkPollution = 0;
        if (this.mTier < 7) {
            mCurrentChunkPollution = PollutionUtils.getPollution(getBaseMetaTileEntity());
        } else {
            AutoMap<Chunk> aSurrounding = new AutoMap<>();
            World aWorld = this.getBaseMetaTileEntity().getWorld();
            int xPos = this.getBaseMetaTileEntity().getXCoord();
            int zPos = this.getBaseMetaTileEntity().getZCoord();
            Chunk a1 = aWorld.getChunkFromBlockCoords(xPos - 32, zPos - 32);
            Chunk a2 = aWorld.getChunkFromBlockCoords(xPos - 32, zPos);
            Chunk a3 = aWorld.getChunkFromBlockCoords(xPos - 32, zPos + 32);
            Chunk b1 = aWorld.getChunkFromBlockCoords(xPos, zPos - 32);
            Chunk b2 = aWorld.getChunkFromBlockCoords(xPos, zPos);
            Chunk b3 = aWorld.getChunkFromBlockCoords(xPos, zPos + 32);
            Chunk c1 = aWorld.getChunkFromBlockCoords(xPos + 32, zPos - 32);
            Chunk c2 = aWorld.getChunkFromBlockCoords(xPos + 32, zPos);
            Chunk c3 = aWorld.getChunkFromBlockCoords(xPos + 32, zPos + 32);
            aSurrounding.put(a1);
            aSurrounding.put(a2);
            aSurrounding.put(a3);
            aSurrounding.put(b1);
            aSurrounding.put(b2);
            aSurrounding.put(b3);
            aSurrounding.put(c1);
            aSurrounding.put(c2);
            aSurrounding.put(c3);
            for (Chunk r : aSurrounding) {
                mCurrentChunkPollution += getPollutionInChunk(r);
            }
        }
        if (mCurrentChunkPollution > 0) {
            mHasPollution = true;
        } else {
            mHasPollution = false;
        }
        return mCurrentChunkPollution;
    }

    public int getPollutionInChunk(Chunk aChunk) {
        int mCurrentChunkPollution = PollutionUtils.getPollution(aChunk);
        if (mCurrentChunkPollution > 0) {
            mHasPollution = true;
        } else {
            mHasPollution = false;
        }
        return mCurrentChunkPollution;
    }

    public boolean hasRotor(ItemStack rotorStack) {
        if (rotorStack != null) {
            if (rotorStack.getItem() instanceof ItemBasicScrubberTurbine) {
                // Logger.INFO("Found Basic Turbine Rotor.");
                return true;
            } else if (rotorStack.getItem() instanceof GT_MetaGenerated_Tool && rotorStack.getItemDamage() >= 170
                    && rotorStack.getItemDamage() <= 179) {
                        // Logger.INFO("Found Turbine Rotor.");
                        return true;
                    } else {
                        // Logger.INFO("Found: "+rotorStack.getDisplayName()+":"+rotorStack.getItemDamage());
                    }
        }
        // Logger.INFO("Found No Turbine Rotor.");
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
            } else if (rotorStack.getItem() instanceof GT_MetaGenerated_Tool_01) {
                Materials t1 = GT_MetaGenerated_Tool.getPrimaryMaterial(rotorStack);
                Materials t2 = GT_MetaGenerated_Tool.getSecondaryMaterial(rotorStack);
                if (t1 == Materials._NULL && t2 == Materials._NULL) {
                    Logger.INFO("Found creative rotor.");
                    creativeRotor = true;
                }
            } else {
                Logger.INFO("Bad item in rotor slot.");
                return false;
            }

            if (mInventory[SLOT_ROTOR].getItem() instanceof GT_MetaGenerated_Tool_01
                    && ((GT_MetaGenerated_Tool) mInventory[SLOT_ROTOR].getItem()).getToolStats(mInventory[SLOT_ROTOR])
                            .getSpeedMultiplier() > 0
                    && GT_MetaGenerated_Tool.getPrimaryMaterial(mInventory[SLOT_ROTOR]).mToolSpeed > 0) {

                long damageValue = (long) Math
                        .floor(Math.abs(MathUtils.randFloat(1, 2) - MathUtils.randFloat(1, 3)) * (1 + 3 - 1) + 1);
                double fDam = Math.floor(
                        Math.abs(MathUtils.randFloat(1f, 2f) - MathUtils.randFloat(1f, 2f)) * (1f + 2f - 1f) + 1f);
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
                        : GT_MetaGenerated_Tool.getToolDamage(this.mInventory[this.SLOT_ROTOR]);
                long rotorDurabilityMax = creativeRotor ? Integer.MAX_VALUE
                        : GT_MetaGenerated_Tool.getToolMaxDamage(this.mInventory[this.SLOT_ROTOR]);
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

                        if (!creativeRotor) GT_ModHandler
                                .damageOrDechargeItem(this.mInventory[this.SLOT_ROTOR], (int) damageValue, 0, null);

                        long tempDur = GT_MetaGenerated_Tool.getToolDamage(this.mInventory[this.SLOT_ROTOR]);
                        if (tempDur < rotorDurabilityMax) {
                            return true;
                        } else {
                            rotorDurability = 0;
                        }
                    } else {
                        Logger.INFO("Damaging Rotor.");
                        if (rotorDurability > 1000) {
                            if (!creativeRotor) GT_ModHandler.damageOrDechargeItem(
                                    this.mInventory[this.SLOT_ROTOR],
                                    (int) damageValue / 2,
                                    0,
                                    null);
                            long tempDur = GT_MetaGenerated_Tool.getToolDamage(this.mInventory[this.SLOT_ROTOR]);
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
        } catch (Throwable t) {
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

        if (this == null || this.getBaseMetaTileEntity() == null || this.getBaseMetaTileEntity().getWorld() == null) {
            return false;
        }

        if (this.mTier < 7) {
            int startPollution = getCurrentChunkPollution();
            Logger.INFO("Current Chunk Pollution: " + startPollution);
            PollutionUtils.removePollution(this.getBaseMetaTileEntity(), toRemove);
            int after = getCurrentChunkPollution();
            Logger.INFO("Current Chunk Pollution: " + after);
            return (after < startPollution);
        } else {
            int chunksWithRemoval = 0;
            int totalRemoved = 0;
            AutoMap<Chunk> aSurrounding = new AutoMap<>();
            Chunk aThisChunk = this.getBaseMetaTileEntity().getWorld().getChunkFromBlockCoords(
                    this.getBaseMetaTileEntity().getXCoord(),
                    this.getBaseMetaTileEntity().getZCoord());
            int mainChunkX = aThisChunk.xPosition;
            int mainChunkZ = aThisChunk.zPosition;

            World aWorld = this.getBaseMetaTileEntity().getWorld();
            int xPos = this.getBaseMetaTileEntity().getXCoord();
            int zPos = this.getBaseMetaTileEntity().getZCoord();

            Chunk a1 = aWorld.getChunkFromBlockCoords(xPos - 32, zPos - 32);
            Chunk a2 = aWorld.getChunkFromBlockCoords(xPos - 32, zPos);
            Chunk a3 = aWorld.getChunkFromBlockCoords(xPos - 32, zPos + 32);
            Chunk b1 = aWorld.getChunkFromBlockCoords(xPos, zPos - 32);
            Chunk b2 = aWorld.getChunkFromBlockCoords(xPos, zPos);
            Chunk b3 = aWorld.getChunkFromBlockCoords(xPos, zPos + 32);
            Chunk c1 = aWorld.getChunkFromBlockCoords(xPos + 32, zPos - 32);
            Chunk c2 = aWorld.getChunkFromBlockCoords(xPos + 32, zPos);
            Chunk c3 = aWorld.getChunkFromBlockCoords(xPos + 32, zPos + 32);

            aSurrounding.put(a1);
            aSurrounding.put(a2);
            aSurrounding.put(a3);
            aSurrounding.put(b1);
            aSurrounding.put(b2);
            aSurrounding.put(b3);
            aSurrounding.put(c1);
            aSurrounding.put(c2);
            aSurrounding.put(c3);

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
        PollutionUtils.removePollution(aChunk, toRemove);
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
            if (rotorStack.getItem() instanceof GT_MetaGenerated_Tool_01) {
                Materials t1 = GT_MetaGenerated_Tool.getPrimaryMaterial(rotorStack);
                Materials t2 = GT_MetaGenerated_Tool.getSecondaryMaterial(rotorStack);
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
                if (aStack.getItem() instanceof GT_MetaGenerated_Tool && aStack.getItemDamage() >= 170
                        && aStack.getItemDamage() <= 179) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        super.onScrewdriverRightClick(side, aPlayer, aX, aY, aZ);
    }

    @Override
    public void doSound(byte aIndex, double aX, double aY, double aZ) {
        if (aIndex == -120) {
            GT_Utility
                    .doSoundAtClient(SoundResource.IC2_TOOLS_BATTERY_USE, MathUtils.randInt(5, 50), 0.05F, aX, aY, aZ);
        } else {
            super.doSound((byte) 0, aX, aY, aZ);
        }
    }

    @Override
    public String[] getInfoData() {
        AutoMap<String> aTooltipSuper = new AutoMap<>();
        for (String s : super.getInfoData()) {
            aTooltipSuper.put(s);
        }
        int mAirSides = getFreeSpaces();
        int reduction = 0;

        try {
            long tVoltage = maxEUInput();
            byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));
            reduction += (((Math.max((tTier - 2), 1) * 2) * 50) * mAirSides);
            reduction = (MathUtils.safeInt((long) reduction * this.mBaseEff) / 100000) * mAirSides
                    * Math.max((tTier - 2), 1);
            reduction = MathUtils.safeInt(((long) reduction / 100) * this.mOptimalAirFlow);

            aTooltipSuper.put("Maximum pollution removed per second: " + reduction);
        } catch (Throwable t) {
            aTooltipSuper.put("Maximum pollution removed per second: " + mPollutionReduction);
        }
        aTooltipSuper.put("Air Sides: " + mAirSides);

        String[] mBuiltOutput = new String[aTooltipSuper.size()];
        int aIndex = 0;
        for (String i : aTooltipSuper) {
            mBuiltOutput[aIndex++] = i;
        }

        return mBuiltOutput;
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public boolean allowCoverOnSide(ForgeDirection side, GT_ItemStack aCoverID) {
        if (side.offsetY != 0) {
            return false;
        }
        return super.allowCoverOnSide(side, aCoverID);
    }

    @Override
    public ITexture[] getTopFacingInactive(byte aColor) {
        return super.getTopFacingInactive(aColor);
    }

    @Override
    public void setItemNBT(NBTTagCompound aNBT) {
        aNBT.setInteger("mOptimalAirFlow", this.mOptimalAirFlow);
        aNBT.setBoolean("mSaveRotor", mSaveRotor);
        super.setItemNBT(aNBT);
    }

    private static ItemStack[] sGregTurbines;

    public static ItemStack getTieredTurbine(int aTier) {
        if (sGregTurbines == null) {
            sGregTurbines = new ItemStack[3];
            sGregTurbines[0] = GT_MetaGenerated_Tool.sInstances.get("gt.metatool.01")
                    .getToolWithStats(GT_MetaGenerated_Tool_01.TURBINE_SMALL, 1, Materials.Iron, Materials.Iron, null);
            sGregTurbines[1] = GT_MetaGenerated_Tool.sInstances.get("gt.metatool.01").getToolWithStats(
                    GT_MetaGenerated_Tool_01.TURBINE_SMALL,
                    1,
                    Materials.Bronze,
                    Materials.Bronze,
                    null);
            sGregTurbines[2] = GT_MetaGenerated_Tool.sInstances.get("gt.metatool.01").getToolWithStats(
                    GT_MetaGenerated_Tool_01.TURBINE_SMALL,
                    1,
                    Materials.Steel,
                    Materials.Steel,
                    null);
        } else {
            return sGregTurbines[aTier];
        }

        return null;
    }

    public int getBaseEfficiency(ItemStack aStackRotor) {
        if (aStackRotor.getItem() instanceof ItemBasicScrubberTurbine) {
            return getBaseEfficiency(getTieredTurbine(aStackRotor.getItemDamage()));
        }
        return (int) ((50.0F
                + (10.0F * ((GT_MetaGenerated_Tool) aStackRotor.getItem()).getToolCombatDamage(aStackRotor))) * 100);
    }

    public int getOptimalAirFlow(ItemStack aStackRotor) {
        if (aStackRotor.getItem() instanceof ItemBasicScrubberTurbine) {
            return getOptimalAirFlow(getTieredTurbine(aStackRotor.getItemDamage()));
        }
        return (int) Math.max(
                Float.MIN_NORMAL,
                ((GT_MetaGenerated_Tool) aStackRotor.getItem()).getToolStats(aStackRotor).getSpeedMultiplier()
                        * GT_MetaGenerated_Tool.getPrimaryMaterial(aStackRotor).mToolSpeed
                        * 50);
    }

    @Override
    public boolean useModularUI() {
        return true;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(new SlotWidget(inventoryHandler, SLOT_ROTOR).setFilter(stack -> {
            if (stack.getItem() instanceof ItemBasicScrubberTurbine) {
                return true;
            }
            return stack.getItem() instanceof GT_MetaGenerated_Tool && stack.getItemDamage() >= 170
                    && stack.getItemDamage() <= 179;
        }).setBackground(getGUITextureSet().getItemSlot(), GTPP_UITextures.OVERLAY_SLOT_TURBINE).setPos(52, 24))
                .widget(
                        new SlotWidget(inventoryHandler, SLOT_FILTER)
                                .setFilter(stack -> stack.getItem() instanceof ItemAirFilter)
                                .setBackground(getGUITextureSet().getItemSlot(), GT_UITextures.OVERLAY_SLOT_RECYCLE)
                                .setPos(106, 24))
                .widget(
                        new SlotWidget(inventoryHandler, 7)
                                .setFilter(stack -> GT_Utility.areStacksEqual(stack, CI.getConveyor(mTier, 1), true))
                                .setPos(124, 62));
        builder.widget(
                new DrawableWidget().setDrawable(GT_UITextures.PICTURE_INFORMATION)
                        .dynamicTooltip(() -> Collections.singletonList("Reduction: " + mPollutionReduction + "/s"))
                        .attachSyncer(
                                new FakeSyncWidget.IntegerSyncer(
                                        () -> mPollutionReduction,
                                        val -> mPollutionReduction = val),
                                builder,
                                (widget, val) -> widget.notifyTooltipChange())
                        .setPos(163, 5).setSize(7, 18));
    }
}

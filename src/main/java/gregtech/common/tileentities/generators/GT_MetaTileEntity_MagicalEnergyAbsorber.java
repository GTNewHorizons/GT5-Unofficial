package gregtech.common.tileentities.generators;

import com.google.common.base.Enums;
import cpw.mods.fml.common.Loader;
import gregtech.api.GregTech_API;
import gregtech.api.enums.TC_Aspects;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicGenerator;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDragonEgg;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectSourceHelper;
import thaumcraft.api.aspects.IAspectSource;
import thaumcraft.api.visnet.VisNetHandler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static gregtech.api.enums.ConfigCategories.machineconfig;
import static gregtech.api.enums.GT_Values.MOD_ID_TC;
import static gregtech.api.enums.GT_Values.V;
import static net.minecraft.util.EnumChatFormatting.*;

interface MagicalEnergyBBListener {
    void onMagicalEnergyBBUpdate();
}

public class GT_MetaTileEntity_MagicalEnergyAbsorber extends GT_MetaTileEntity_BasicGenerator implements MagicalEnergyBBListener {

    private static final boolean THAUMCRAFT_LOADED = Loader.isModLoaded(MOD_ID_TC);
    private static final ConcurrentHashMap<UUID, GT_MetaTileEntity_MagicalEnergyAbsorber> sSubscribedCrystals = new ConcurrentHashMap<>(4);
    private static List<Aspect> sPrimalAspects = (THAUMCRAFT_LOADED) ? Aspect.getPrimalAspects() : new ArrayList<Aspect>();
    private static boolean sAllowMultipleEggs = false;
    private static GT_MetaTileEntity_MagicalEnergyAbsorber sActiveSiphon = null;
    private static int sEnergyPerEndercrystal = 512;
    private static int sEnergyFromVis = 20;
    private static int sEnergyPerEssentia = 320;
    private static Map<Aspect, Integer> sAspectsEnergy = new HashMap<>();
    private static int sDragonEggEnergyPerTick = 2048;
    private int mEfficiency;
    private int mMaxVisPerDrain;
    private MagicalEnergyBB mMagicalEnergyBB = new MagicalEnergyBB(this, mTier, mTier + 2);
    private long mNextGenerateTickRate = 1;
    private int mNoGenerationTicks = 0;

    public GT_MetaTileEntity_MagicalEnergyAbsorber(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, "Feasts on magic close to it:");
        onConfigLoad(GregTech_API.sMachineFile);
    }

    private GT_MetaTileEntity_MagicalEnergyAbsorber(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
        onConfigLoad(GregTech_API.sMachineFile);
    }

    /**
     * Populates static variables dependant on config settings
     *
     * @param aConfig GT_Config
     */
    private static void sharedConfigLoad(GT_Config aConfig) {
        sAllowMultipleEggs = aConfig.get(machineconfig, "MagicEnergyAbsorber.AllowMultipleEggs", false);
        sDragonEggEnergyPerTick = aConfig.get(machineconfig, "MagicEnergyAbsorber.EnergyPerTick.DragonEgg", 2048);
        sEnergyPerEndercrystal = aConfig.get(machineconfig, "MagicEnergyAbsorber.EnergyPerTick.EnderCrystal", 512);
        if (THAUMCRAFT_LOADED) {
            sEnergyFromVis = aConfig.get(machineconfig, "MagicEnergyAbsorber.EnergyPerVis", 20);
            sEnergyPerEssentia = aConfig.get(machineconfig, "MagicEnergyAbsorber.EnergyPerEssentia", 320);
            for (Aspect tAspect : Aspect.aspects.values()) {
                sAspectsEnergy.put(tAspect,
                        Enums.getIfPresent(TC_Aspects.class,
                                tAspect.getTag().toUpperCase(Locale.ENGLISH)).or(TC_Aspects.AER).mValue
                                * sEnergyPerEssentia);
            }
        }
    }

    private static void setActiveSiphon(GT_MetaTileEntity_MagicalEnergyAbsorber aSiphon) {
        sActiveSiphon = aSiphon;
    }

    @Override
    public void onConfigLoad(GT_Config aConfig) {
        sharedConfigLoad(aConfig);
        mEfficiency = aConfig.get(machineconfig, "MagicEnergyAbsorber.efficiency.tier." + mTier, 100 - mTier * 10);
        mMaxVisPerDrain = (int) Math.round(Math.sqrt(V[mTier] * 10000 / (sEnergyFromVis * (getEfficiency() != 0 ? getEfficiency() : 100))));
        if (Math.pow(mMaxVisPerDrain, 2) * sEnergyFromVis * (getEfficiency() != 0 ? getEfficiency() : 100) < V[mTier]) {
            mMaxVisPerDrain += 1;
        }
    }

    @Override
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (aPlayer.isSneaking()) mMagicalEnergyBB.decreaseTier();
        else mMagicalEnergyBB.increaseTier();
        GT_Utility.sendChatToPlayer(aPlayer, String.format(GT_LanguageManager.addStringLocalization("Interaction_DESCRIPTION_MagicalEnergyAbsorber_Screwdriver", "Absorption range: %s blocks"), mMagicalEnergyBB.getRange(), true));
        mMagicalEnergyBB.update();
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        if (!aBaseMetaTileEntity.isServerSide()) return;
        mMagicalEnergyBB.update();
    }

    @Override
    public void onRemoval() {
        super.onRemoval();
        releaseEgg();
        unsubscribeCrystals();
    }

    private void releaseEgg() {
        if (sActiveSiphon == this) {
            setActiveSiphon(null);
        }
    }

    private void unsubscribeCrystals() {
        for (UUID tCrystalID : sSubscribedCrystals.keySet()) {
            sSubscribedCrystals.remove(tCrystalID, this);
        }
    }

    /**
     * Call-back from the Bounding Box when its content is updated
     */
    @Override
    public void onMagicalEnergyBBUpdate() {
        List<UUID> tCrystalIDsInRange = mMagicalEnergyBB.getLivingCrystalIDs();
        // Release unreachable Crystals subscriptions
        for (UUID tSubscribedCrystalID : sSubscribedCrystals.keySet()) {
            if (!tCrystalIDsInRange.contains(tSubscribedCrystalID)) {
                sSubscribedCrystals.remove(tSubscribedCrystalID, this);
            }
        }
        // Subscribe to available and not already subscribed Crystals
        for (UUID tCrystalID : tCrystalIDsInRange) {
            sSubscribedCrystals.putIfAbsent(tCrystalID, this);
        }
    }

    @Override
    public String[] getDescription() {
        final String LI = "-%%%";
        final String EU_PER = "%%%EU per ";
        List<String> description = new ArrayList<>();
        description.add(UNDERLINE + "Feasts on " + LIGHT_PURPLE + UNDERLINE + "magic" + GRAY + UNDERLINE + " close to it:");
        description.add(LI + (sAllowMultipleEggs ? "A " : "An " + YELLOW + UNDERLINE + "EXCLUSIVE" + RESET) + GRAY + " " + LIGHT_PURPLE + "Dragon Egg" + GRAY + " atop");
        if (sEnergyPerEndercrystal > 0) {
            description.add(LI + sEnergyPerEndercrystal + EU_PER + LIGHT_PURPLE + "Ender Crystal" + GRAY + " in range");
        }
        if (THAUMCRAFT_LOADED) {
            description.add(LI + mMaxVisPerDrain + "%%%CV/t from an " + LIGHT_PURPLE + "Energised Node" + GRAY);
            description.add(LI + (sEnergyPerEssentia * getEfficiency()) / 100 + EU_PER + LIGHT_PURPLE + "Essentia" + GRAY + " Aspect-Value from containers in range");
        }
        description.add(" ");
        description.add(UNDERLINE + "Lookup range (Use Screwdriver to change):");
        description.add("Default:%%%" + GREEN + mMagicalEnergyBB.getDefaultRange());
        description.add("Max:%%%" + GREEN + mMagicalEnergyBB.getMaxRange());
        description.add(" ");
        description.add(UNDERLINE + "Fuels on " + LIGHT_PURPLE + UNDERLINE + "enchantments" + GRAY + UNDERLINE + " input:");
        description.add("- Item:%%%" + (10000 * getEfficiency()) / 100 + EU_PER + LIGHT_PURPLE + "enchant" + GRAY + " weight × level / max");
        description.add("- Book:%%%" + 10000 + EU_PER + LIGHT_PURPLE + "enchant" + GRAY + " weight × level / max");
        description.add(" ");
        description.add("Efficiency:%%%" + GREEN + getEfficiency() + "%");
        return description.toArray(new String[0]);
    }

    @Override
    public ITexture[] getFront(byte aColor) {
        return new ITexture[]{super.getFront(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_MAGIC),
                Textures.BlockIcons.OVERLAYS_ENERGY_OUT[mTier]};
    }

    @Override
    public ITexture[] getBack(byte aColor) {
        return new ITexture[]{super.getBack(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_MAGIC_FRONT)};
    }

    @Override
    public ITexture[] getBottom(byte aColor) {
        return new ITexture[]{super.getBottom(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_MAGIC)};
    }

    @Override
    public ITexture[] getTop(byte aColor) {
        return new ITexture[]{super.getTop(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_DRAGONEGG)};
    }

    @Override
    public ITexture[] getSides(byte aColor) {
        return new ITexture[]{super.getSides(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_MAGIC)};
    }

    @Override
    public ITexture[] getFrontActive(byte aColor) {
        return new ITexture[]{super.getFrontActive(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_MAGIC_ACTIVE),
                Textures.BlockIcons.OVERLAYS_ENERGY_OUT[mTier]};
    }

    @Override
    public ITexture[] getBackActive(byte aColor) {
        return new ITexture[]{super.getBackActive(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_MAGIC_FRONT_ACTIVE)};
    }

    @Override
    public ITexture[] getBottomActive(byte aColor) {
        return new ITexture[]{super.getBottomActive(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_MAGIC_ACTIVE)};
    }

    @Override
    public ITexture[] getTopActive(byte aColor) {
        return new ITexture[]{super.getTopActive(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_DRAGONEGG)};
    }

    @Override
    public ITexture[] getSidesActive(byte aColor) {
        return new ITexture[]{super.getSidesActive(aColor)[0], new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_MAGIC_ACTIVE)};
    }

    @Override
    public boolean isOutputFacing(byte aSide) {
        return aSide == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public long maxEUStore() {
        return Math.max(getEUVar(), V[mTier] * 16000 + getMinimumStoredEU());
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (!aBaseMetaTileEntity.isServerSide()) return;
        if (!aBaseMetaTileEntity.isAllowedToWork()) return;
        if ((aBaseMetaTileEntity.getUniversalEnergyStored() >= aBaseMetaTileEntity.getEUCapacity())) return;

        long tGeneratedEU;

        if (aTick % 100 == 0) mMagicalEnergyBB.update();

        // Adaptive EU Generation Ticking
        if (aTick % mNextGenerateTickRate == 0) {
            tGeneratedEU = generateEU();
            if (tGeneratedEU > 0) {
                mNoGenerationTicks = 0;
                if (tGeneratedEU >= 2 * V[mTier])
                    mNextGenerateTickRate = (long) (1.0D / ((2.0D * (double) (V[mTier])) / (double) tGeneratedEU));
                else mNextGenerateTickRate = 1;
                mInventory[getStackDisplaySlot()] = new ItemStack(Blocks.fire, 1);
                mInventory[getStackDisplaySlot()].setStackDisplayName("Generating: " + tGeneratedEU + " EU");
            } else {
                mInventory[getStackDisplaySlot()] = null;
                mNoGenerationTicks += 1;
            }
            if (mNoGenerationTicks > 20) {
                mNoGenerationTicks = 0;
                mNextGenerateTickRate = 20;
            }
            aBaseMetaTileEntity.increaseStoredEnergyUnits(tGeneratedEU, true);
            aBaseMetaTileEntity.setActive(aBaseMetaTileEntity.isAllowedToWork() && aBaseMetaTileEntity.getUniversalEnergyStored() >= maxEUOutput() + getMinimumStoredEU());
        }
    }

    @Override
    public int getPollution() {
        return 0;
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipes() {
        return GT_Recipe.GT_Recipe_Map.sMagicFuels;
    }

    @Override
    public int getEfficiency() {
        return mEfficiency;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        // Restrict input to disenchantable items or enchanted books
        return (isDisenchantableItem(aStack) || isEnchantedBook(aStack));
    }

    @Override
    public int getCapacity() {
        return 16000;
    }

    private boolean isDisenchantableItem(ItemStack aStack) {
        return ((aStack.isItemEnchanted()) && (aStack.getItem().getItemEnchantability() > 0));
    }

    private boolean isEnchantedBook(ItemStack aStack) {
        return (aStack.getItem() instanceof ItemEnchantedBook);
    }

    private long generateEU() {
        long tEU;

        if ((tEU = absorbFromEgg()) > 0) return tEU;
        if ((tEU = absorbFromEnderCrystals()) > 0) return tEU;
        if ((tEU = absorbFromEnchantedItems()) > 0) return tEU;
        if ((tEU = absorbFromVisNet()) > 0) return tEU;
        if ((tEU = absorbFromEssentiaContainers()) > 0) return tEU;
        return 0;
    }

    private long absorbFromEnchantedItems() {
        ItemStack tStack = getBaseMetaTileEntity().getStackInSlot(getInputSlot());
        if (tStack == null) return 0;
        if (tStack.stackSize == 0) return 0;
        if (!(isDisenchantableItem(tStack) || isEnchantedBook(tStack))) return 0;
        long tEU = 0;
        // Convert enchantments to their EU Value
        Map<?, ?> tMap = EnchantmentHelper.getEnchantments(tStack);
        for (Map.Entry<?, ?> e : tMap.entrySet()) {
            if ((Integer) e.getKey() < Enchantment.enchantmentsList.length) {
                Enchantment tEnchantment = Enchantment.enchantmentsList[(Integer) e.getKey()];
                Integer tLevel = (Integer) e.getValue();
                tEU += 1000000 * tLevel / (tEnchantment.getMaxLevel() * tEnchantment.getWeight());
            }
        }
        if (isDisenchantableItem(tStack)) {
            EnchantmentHelper.setEnchantments(new HashMap(), tStack);
            tEU = tEU * getEfficiency() / 100;
        } else if (isEnchantedBook(tStack)) {
            tStack = new ItemStack(Items.book, 1);
        }

        // Only consume input if can store EU and push output
        if ((getBaseMetaTileEntity().getStoredEU() + tEU) < getBaseMetaTileEntity().getEUCapacity()
                    && getBaseMetaTileEntity().addStackToSlot(getOutputSlot(), tStack)) {
            decrStackSize(getInputSlot(), 1);
        } else {
            tEU = 0;
        }
        return tEU;
    }

    private boolean hasEgg() {
        Block above = getBaseMetaTileEntity().getBlockOffset(0, 1, 0);
        return isEgg(above);
    }

    private long absorbFromEgg() {
        if (!hasEgg()) return 0;
        if (!sAllowMultipleEggs) {
            if (sActiveSiphon != null
                        && sActiveSiphon != this
                        && sActiveSiphon.getBaseMetaTileEntity() != null
                        && !sActiveSiphon.getBaseMetaTileEntity().isInvalidTileEntity()
                        && sActiveSiphon.isChunkLoaded()
                        && sActiveSiphon.hasEgg()) {
                getBaseMetaTileEntity().doExplosion(Integer.MAX_VALUE);
            } else {
                setActiveSiphon(this);
            }
        }
        return sDragonEggEnergyPerTick;
    }

    private long absorbFromEnderCrystals() {
        if (sEnergyPerEndercrystal <= 0) return 0;
        long tEU = 0;
        for (GT_MetaTileEntity_MagicalEnergyAbsorber tSubscriber : sSubscribedCrystals.values()) {
            if (tSubscriber == this) { // This Crystal is for me
                tEU += sEnergyPerEndercrystal;
            }
        }
        return tEU;
    }

    private long absorbFromVisNet() {
        if (!THAUMCRAFT_LOADED) return 0;

        long tEU;
        IGregTechTileEntity tBaseMetaTileEntity = getBaseMetaTileEntity();
        World tWorld = tBaseMetaTileEntity.getWorld();
        int tX = tBaseMetaTileEntity.getXCoord();
        int tY = tBaseMetaTileEntity.getYCoord();
        int tZ = tBaseMetaTileEntity.getZCoord();

        // Attempt to drain as much Vis as needed for max EU/t, from all primal aspects.
        int toDrain = mMaxVisPerDrain;

        for (int i = sPrimalAspects.size() - 1; i >= 0 && toDrain > 0; i--) {
            toDrain -= VisNetHandler.drainVis(tWorld, tX, tY, tZ, sPrimalAspects.get(i), toDrain);
        }

        int drained = mMaxVisPerDrain - toDrain;
        tEU = (long) Math.min(maxEUOutput(), (Math.pow(drained, 2) * sEnergyFromVis * getEfficiency() / 10000));

        return tEU;
    }

    private long absorbFromEssentiaContainers() {
        if (!THAUMCRAFT_LOADED) return 0;

        long tEU = 0;

        long tEUtoGen = getBaseMetaTileEntity().getEUCapacity() - getBaseMetaTileEntity().getUniversalEnergyStored();
        List<Aspect> mAvailableEssentiaAspects = mMagicalEnergyBB.getAvailableAspects();

        // try to drain 1 of whatever aspect available in containers within RANGE
        for (int i = mAvailableEssentiaAspects.size() - 1; i >= 0 && tEUtoGen > 0; i--) {
            Aspect aspect = mAvailableEssentiaAspects.get(i);
            long tAspectEU = (sAspectsEnergy.get(aspect) * getEfficiency()) / 100;
            if (tAspectEU <= tEUtoGen
                        && AspectSourceHelper.drainEssentia((TileEntity) getBaseMetaTileEntity(), aspect, ForgeDirection.UNKNOWN, mMagicalEnergyBB.getRange())) {
                tEUtoGen -= tAspectEU;
                tEU += tAspectEU;
            }
        }
        return tEU;
    }

    private boolean isEgg(Block aBlock) {
        if (aBlock == null) return false;
        if (aBlock == Blocks.air) return false;
        if (aBlock == Blocks.dragon_egg) return true;
        if (aBlock instanceof BlockDragonEgg) return true;
        return (aBlock.getUnlocalizedName().equals("tile.dragonEgg"));
    }

    private boolean isChunkLoaded() {
        IGregTechTileEntity tBaseMetaTileEntity = getBaseMetaTileEntity();
        int tX = tBaseMetaTileEntity.getXCoord();
        int tY = tBaseMetaTileEntity.getYCoord();
        World tWorld = tBaseMetaTileEntity.getWorld();
        Chunk tChunk = tWorld.getChunkFromBlockCoords(tX, tY);
        return tChunk.isChunkLoaded;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_MagicalEnergyAbsorber(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("mMagicalEnergyBBTier", mMagicalEnergyBB.getTier());
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mMagicalEnergyBB.setTier(aNBT.getInteger("mMagicalEnergyBBTier"));
    }

    /**
     * Handles Bounding Box ranged operations for Magic sources
     */
    static class MagicalEnergyBB {
        private GT_MetaTileEntity_MagicalEnergyAbsorber mAbsorber;
        private MagicalEnergyBBListener mListener;
        private int mDefaultTier;
        private int mTier;
        private int mMaxTier;
        private List<UUID> mLivingCrystalIDs = new ArrayList<>();
        private List<Aspect> mAvailableAspects;

        /**
         * @param aAbsorber    user and subscriber for updated BB content
         * @param aDefaultTier Initial tier value
         * @param aMaxTier     Maximum allowed tier
         */
        MagicalEnergyBB(GT_MetaTileEntity_MagicalEnergyAbsorber aAbsorber, int aDefaultTier, int aMaxTier) {
            mAbsorber = aAbsorber;
            mListener = aAbsorber;
            mMaxTier = Math.max(aMaxTier > 0 ? aMaxTier : 0, aDefaultTier > 0 ? aDefaultTier : 0);
            mDefaultTier = Math.min(aDefaultTier, mMaxTier);
            mTier = mDefaultTier;
            if (THAUMCRAFT_LOADED) mAvailableAspects = new ArrayList<>(Aspect.aspects.size());
        }

        int getTier() {
            return mTier;
        }

        /**
         * Set Bounding Box Tier within allowed bounds
         *
         * @param aTier new tier value
         * @return effective new tier
         */
        int setTier(int aTier) {
            if (aTier >= 0) {
                if (aTier <= mMaxTier) {
                    mTier = aTier;
                } else {
                    mTier = mMaxTier;
                }
            } else {
                mTier = 0;
            }
            return mTier;
        }

        int getRange() {
            return getRange(mTier);
        }

        int getRange(int aTier) {
            return 1 << aTier;
        }

        int getDefaultTier() {
            return mDefaultTier;
        }

        int getDefaultRange() {
            return getRange(getDefaultTier());
        }

        int getMaxTier() {
            return mMaxTier;
        }

        int getMaxRange() {
            return getRange(getMaxTier());
        }

        private AxisAlignedBB getAxisAlignedBB() {
            double tRange = getRange();
            IGregTechTileEntity tBaseMetaTileEntity = mAbsorber.getBaseMetaTileEntity();
            double tX = tBaseMetaTileEntity.getXCoord();
            double tY = tBaseMetaTileEntity.getYCoord();
            double tZ = tBaseMetaTileEntity.getZCoord();
            return AxisAlignedBB.getBoundingBox(tX - tRange, tY - tRange, tZ - tRange,
                    tX + tRange, tY + tRange, tZ + tRange);
        }

        private void scanLivingCrystals() {
            World tWorld = mAbsorber.getBaseMetaTileEntity().getWorld();
            mLivingCrystalIDs.clear();
            for (Object o : tWorld.getEntitiesWithinAABB(EntityEnderCrystal.class,
                    getAxisAlignedBB())) {
                if (((EntityEnderCrystal) o).isEntityAlive()) {
                    mLivingCrystalIDs.add(((EntityEnderCrystal) o).getPersistentID());
                }
            }
        }

        private void scanAvailableAspects() {
            if (!THAUMCRAFT_LOADED) return;
            IGregTechTileEntity tBaseMetaTileEntity = mAbsorber.getBaseMetaTileEntity();
            if (tBaseMetaTileEntity.isInvalidTileEntity()) return;
            int tRange = getRange();
            mAvailableAspects.clear();
            for (int rX = -tRange; rX <= tRange; rX++) {
                for (int rZ = -tRange; rZ <= tRange; rZ++) {
                    // rY < tRange is not a bug. See: thaumcraft.common.lib.events.EssentiaHandler.getSources()
                    for (int rY = -tRange; rY < tRange; rY++) {
                        TileEntity tTile = tBaseMetaTileEntity.getTileEntityOffset(rX, rY, rZ);
                        if (tTile instanceof IAspectSource) {
                            Set<Aspect> tAspects = ((IAspectSource) tTile).getAspects().aspects.keySet();
                            mAvailableAspects.addAll(tAspects);
                        }
                    }
                }
            }
        }

        /**
         * @return List of Living Ender Crystal Entity IDs in range
         */
        List<UUID> getLivingCrystalIDs() {
            return mLivingCrystalIDs;
        }

        /**
         * @return List of drainable Essentia Aspects from containers in range
         */
        List<Aspect> getAvailableAspects() {
            return mAvailableAspects;
        }

        /**
         * Scan range for magic sources
         */
        void update() {
            if (mAbsorber == null) return;
            if (mAbsorber.getBaseMetaTileEntity() == null) return;
            if (mAbsorber.getBaseMetaTileEntity().isInvalidTileEntity()) return;
            if (mAbsorber.getBaseMetaTileEntity().getWorld() == null) return;
            scanLivingCrystals();
            scanAvailableAspects();
            if (mListener != null) {
                mListener.onMagicalEnergyBBUpdate();
            }
        }

        void increaseTier() {
            offsetTier(1);
        }

        void decreaseTier() {
            offsetTier(-1);
        }

        /**
         * Change the Bounding Box tier relatively to offset
         * with wrapping at tier limits
         *
         * @param aOffset relative tier change
         */
        void offsetTier(int aOffset) {
            int tNumTiers = mMaxTier + 1;
            int tTier = (mTier + aOffset + tNumTiers) % tNumTiers;
            int tTrueTier = setTier(tTier);
            if (tTier != tTrueTier) {
                GT_Log.out.format("Absorber's BB Tier set to %d was capped to %d", tTier, tTrueTier);
            }
        }
    }
}

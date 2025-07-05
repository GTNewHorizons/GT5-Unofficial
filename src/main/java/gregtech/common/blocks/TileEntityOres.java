package gregtech.common.blocks;

import static gregtech.api.enums.TextureSet.SET_NONE;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;

import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IAllSidedTexturedTileEntity;
import gregtech.api.objects.XSTR;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class TileEntityOres extends TileEntity implements IAllSidedTexturedTileEntity {

    public short mMetaData = 0;
    protected static boolean shouldFortune = false;
    protected static boolean shouldSilkTouch = false;
    public boolean mNatural = false;
    public boolean mBlocked = true;
    public boolean mBlockedChecked = false;
    private short mMetadataForCachedTexture = -1;
    private ITexture[] mCachedTexture;

    public static byte getHarvestData(short aMetaData, int aBaseBlockHarvestLevel) {
        Materials aMaterial = GregTechAPI.sGeneratedMaterials[(aMetaData % 1000)];
        byte tByte = aMaterial == null ? 0
            : (byte) Math
                .max(aBaseBlockHarvestLevel, Math.min(7, aMaterial.mToolQuality - (aMetaData < 16000 ? 0 : 1)));
        if (GTMod.proxy.mChangeHarvestLevels) {
            tByte = aMaterial == null ? 0
                : (byte) Math.max(
                    aBaseBlockHarvestLevel,
                    Math.min(
                        GTMod.proxy.mMaxHarvestLevel,
                        GTMod.proxy.mHarvestLevel[aMaterial.mMetaItemSubID] - (aMetaData < 16000 ? 0 : 1)));
        }
        return tByte;
    }

    public static boolean setOreBlock(World aWorld, int aX, int aY, int aZ, int aMetaData, boolean isSmallOre) {
        return setOreBlock(aWorld, aX, aY, aZ, aMetaData, isSmallOre, true);
    }

    public static boolean setOreBlock(World aWorld, int aX, int aY, int aZ, int aMetaData, boolean isSmallOre,
        boolean air) {
        if (!air) {
            aY = Math.min(aWorld.getActualHeight(), Math.max(aY, 1));
        }
        Block tBlock = aWorld.getBlock(aX, aY, aZ);
        Block tOreBlock = GregTechAPI.sBlockOres1;
        int BlockMeta = aWorld.getBlockMetadata(aX, aY, aZ);
        String BlockName = tBlock.getUnlocalizedName();
        aMetaData += isSmallOre ? 16000 : 0;
        if ((aMetaData > 0) && ((tBlock != Blocks.air) || air)) {
            if (tBlock.isReplaceableOreGen(aWorld, aX, aY, aZ, Blocks.stone)) {
                // Do nothing, stone background is default background.
                // Do this comparison first since stone is most common
            } else if (tBlock.isReplaceableOreGen(aWorld, aX, aY, aZ, Blocks.netherrack)) {
                aMetaData += 1000;
            } else if (tBlock.isReplaceableOreGen(aWorld, aX, aY, aZ, Blocks.end_stone)) {
                aMetaData += 2000;
            } else if (tBlock.isReplaceableOreGen(aWorld, aX, aY, aZ, GregTechAPI.sBlockGranites)) {
                if (tBlock == GregTechAPI.sBlockGranites) {
                    if (aWorld.getBlockMetadata(aX, aY, aZ) < 8) {
                        aMetaData += 3000;
                    } else {
                        aMetaData += 4000;
                    }
                } else {
                    aMetaData += 3000;
                }
            } else if (tBlock.isReplaceableOreGen(aWorld, aX, aY, aZ, GregTechAPI.sBlockStones)) {
                if (tBlock == GregTechAPI.sBlockStones) {
                    if (aWorld.getBlockMetadata(aX, aY, aZ) < 8) {
                        aMetaData += 5000;
                    } else {
                        aMetaData += 6000;
                    }
                } else {
                    aMetaData += 5000;
                }
            } else if (BlockName.equals("tile.igneousStone")) {
                if (GregTechAPI.sBlockOresUb1 != null) {
                    tOreBlock = GregTechAPI.sBlockOresUb1;
                    aMetaData += (BlockMeta * 1000);
                    // GT_FML_LOGGER.info("Block changed to UB1");
                }
            } else if (BlockName.equals("tile.metamorphicStone")) {
                if (GregTechAPI.sBlockOresUb2 != null) {
                    tOreBlock = GregTechAPI.sBlockOresUb2;
                    aMetaData += (BlockMeta * 1000);
                    // GT_FML_LOGGER.info("Block changed to UB2");
                }
            } else if (BlockName.equals("tile.sedimentaryStone")) {
                if (GregTechAPI.sBlockOresUb3 != null) {
                    tOreBlock = GregTechAPI.sBlockOresUb3;
                    aMetaData += (BlockMeta * 1000);
                    // GT_FML_LOGGER.info("Block changed to UB3");
                }
            } else {
                return false;
            }
            // GT_FML_LOGGER.info(tOreBlock);
            aWorld.setBlock(
                aX,
                aY,
                aZ,
                tOreBlock,
                getHarvestData(
                    (short) aMetaData,
                    ((BlockOresAbstract) tOreBlock).getBaseBlockHarvestLevel(aMetaData % 16000 / 1000)),
                0);
            TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
            if ((tTileEntity instanceof TileEntityOres)) {
                ((TileEntityOres) tTileEntity).mMetaData = ((short) aMetaData);
                ((TileEntityOres) tTileEntity).mNatural = true;
            }
            return true;
        }
        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound aNBT) {
        super.readFromNBT(aNBT);
        this.mMetaData = aNBT.getShort("m");
        this.mNatural = aNBT.getBoolean("n");
    }

    @Override
    public void writeToNBT(NBTTagCompound aNBT) {
        super.writeToNBT(aNBT);
        aNBT.setShort("m", this.mMetaData);
        aNBT.setBoolean("n", this.mNatural);
    }

    public void onUpdated() {
        if ((!this.worldObj.isRemote) && (this.mBlocked)) {
            this.mBlocked = false;
            GTValues.NW.sendPacketToAllPlayersInRange(
                this.worldObj,
                new PacketOres(this.xCoord, (short) this.yCoord, this.zCoord, this.mMetaData),
                this.xCoord,
                this.zCoord);
        }
    }

    @Override
    public Packet getDescriptionPacket() {
        if (!this.worldObj.isRemote) {
            boolean sendUpdate = mBlockedChecked ? !mBlocked : checkBlocked();
            if (sendUpdate) {
                GTValues.NW.sendPacketToAllPlayersInRange(
                    this.worldObj,
                    new PacketOres(this.xCoord, (short) this.yCoord, this.zCoord, this.mMetaData),
                    this.xCoord,
                    this.zCoord);
            }
        }
        return null;
    }

    private boolean checkBlocked() {
        // this is called very frequently and is performance critical. unroll the loop.
        mBlockedChecked = true;
        if (!worldObj.blockExists(xCoord + 1, yCoord, zCoord)) {
            mBlockedChecked = false;
        } else if (!GTUtility.isOpaqueBlock(worldObj, xCoord + 1, yCoord, zCoord)) {
            mBlocked = false;
            return true;
        }
        if (!worldObj.blockExists(xCoord - 1, yCoord, zCoord)) {
            mBlockedChecked = false;
        } else if (!GTUtility.isOpaqueBlock(worldObj, xCoord - 1, yCoord, zCoord)) {
            mBlocked = false;
            return true;
        }
        if (!worldObj.blockExists(xCoord, yCoord + 1, zCoord)) {
            mBlockedChecked = false;
        } else if (!GTUtility.isOpaqueBlock(worldObj, xCoord, yCoord + 1, zCoord)) {
            mBlocked = false;
            return true;
        }
        if (!worldObj.blockExists(xCoord, yCoord - 1, zCoord)) {
            mBlockedChecked = false;
        } else if (!GTUtility.isOpaqueBlock(worldObj, xCoord, yCoord - 1, zCoord)) {
            mBlocked = false;
            return true;
        }
        if (!worldObj.blockExists(xCoord, yCoord, zCoord + 1)) {
            mBlockedChecked = false;
        } else if (!GTUtility.isOpaqueBlock(worldObj, xCoord, yCoord, zCoord + 1)) {
            mBlocked = false;
            return true;
        }
        if (!worldObj.blockExists(xCoord, yCoord, zCoord - 1)) {
            mBlockedChecked = false;
        } else if (!GTUtility.isOpaqueBlock(worldObj, xCoord, yCoord, zCoord - 1)) {
            mBlocked = false;
            return true;
        }
        mBlocked = true;
        return false;
    }

    public void overrideOreBlockMaterial(Block aOverridingStoneBlock, byte aOverridingStoneMeta) {
        if (this.worldObj == null || blockType == null) return;
        this.mMetaData = ((short) (int) (this.mMetaData % 1000L + this.mMetaData / 16000L * 16000L));
        if (aOverridingStoneBlock
            .isReplaceableOreGen(this.worldObj, this.xCoord, this.yCoord, this.zCoord, Blocks.netherrack)) {
            this.mMetaData = ((short) (this.mMetaData + 1000));
        } else if (aOverridingStoneBlock
            .isReplaceableOreGen(this.worldObj, this.xCoord, this.yCoord, this.zCoord, Blocks.end_stone)) {
                this.mMetaData = ((short) (this.mMetaData + 2000));
            } else if (aOverridingStoneBlock.isReplaceableOreGen(
                this.worldObj,
                this.xCoord,
                this.yCoord,
                this.zCoord,
                GregTechAPI.sBlockGranites)) {
                    if (aOverridingStoneBlock == GregTechAPI.sBlockGranites) {
                        if (aOverridingStoneMeta < 8) {
                            this.mMetaData = ((short) (this.mMetaData + 3000));
                        } else {
                            this.mMetaData = ((short) (this.mMetaData + 4000));
                        }
                    } else {
                        this.mMetaData = ((short) (this.mMetaData + 3000));
                    }
                } else if (aOverridingStoneBlock.isReplaceableOreGen(
                    this.worldObj,
                    this.xCoord,
                    this.yCoord,
                    this.zCoord,
                    GregTechAPI.sBlockStones)) {
                        if (aOverridingStoneBlock == GregTechAPI.sBlockStones) {
                            if (aOverridingStoneMeta < 8) {
                                this.mMetaData = ((short) (this.mMetaData + 5000));
                            } else {
                                this.mMetaData = ((short) (this.mMetaData + 6000));
                            }
                        } else {
                            this.mMetaData = ((short) (this.mMetaData + 5000));
                        }
                    }
        this.worldObj.setBlockMetadataWithNotify(
            this.xCoord,
            this.yCoord,
            this.zCoord,
            getHarvestData(
                this.mMetaData,
                ((BlockOresAbstract) blockType).getBaseBlockHarvestLevel(mMetaData % 16000 / 1000)),
            0);
    }

    public void convertOreBlock(World aWorld, int aX, int aY, int aZ) {
        short aMeta = ((short) (this.mMetaData % 1000 + (this.mMetaData / 16000 * 16000)));
        aWorld.setBlock(aX, aY, aZ, GregTechAPI.sBlockOres1);
        TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof TileEntityOres) {
            ((TileEntityOres) tTileEntity).mMetaData = aMeta;
            this.worldObj.setBlockMetadataWithNotify(
                this.xCoord,
                this.yCoord,
                this.zCoord,
                getHarvestData(
                    aMeta,
                    ((BlockOresAbstract) tTileEntity.blockType).getBaseBlockHarvestLevel(aMeta % 16000 / 1000)),
                0);
        }
    }

    public short getMetaData() {
        return this.mMetaData;
    }

    @Override
    public boolean canUpdate() {
        return false;
    }

    public ArrayList<ItemStack> getDrops(Block aDroppedOre, int aFortune) {
        ArrayList<ItemStack> rList = new ArrayList<>();
        if (this.mMetaData <= 0) {
            rList.add(new ItemStack(Blocks.cobblestone, 1, 0));
            return rList;
        }
        Materials aOreMaterial = GregTechAPI.sGeneratedMaterials[(this.mMetaData % 1000)];
        if (this.mMetaData < 16000) {
            boolean tIsRich = false;

            // For Sake of god of balance!

            // Dense ore

            // NetherOre
            if (GTMod.proxy.mNetherOreYieldMultiplier && !tIsRich) {
                tIsRich = (this.mMetaData >= 1000 && this.mMetaData < 2000);
            }
            // EndOre
            if (GTMod.proxy.mEndOreYieldMultiplier && !tIsRich) {
                tIsRich = (this.mMetaData >= 2000 && this.mMetaData < 3000);
            }

            // Silk Touch
            if (shouldSilkTouch) {
                rList.add(new ItemStack(aDroppedOre, 1, this.mMetaData));

            } else {
                switch (GTMod.proxy.oreDropSystem) {
                    case Item -> {
                        rList.add(GTOreDictUnificator.get(OrePrefixes.rawOre, aOreMaterial, (tIsRich ? 2 : 1)));
                    }
                    // TODO: Test
                    case FortuneItem -> {
                        // if shouldFortune and isNatural then get fortune drops
                        // if not shouldFortune or not isNatural then get normal drops
                        // if not shouldFortune and isNatural then get normal drops
                        // if shouldFortune and not isNatural then get normal drops
                        if (shouldFortune && this.mNatural && aFortune > 0) {
                            // Max applicable fortune
                            if (aFortune > 3) aFortune = 3;
                            int addedDrops = worldObj.rand.nextInt(aFortune + 2) - 1;
                            if (addedDrops < 0) addedDrops = 0;

                            int amount = (tIsRich ? 2 : 1) * (addedDrops + 1);
                            for (int i = 0; i < amount; i++) {
                                rList.add(GTOreDictUnificator.get(OrePrefixes.rawOre, aOreMaterial, 1));
                            }
                        } else {
                            for (int i = 0; i < (tIsRich ? 2 : 1); i++) {
                                rList.add(GTOreDictUnificator.get(OrePrefixes.rawOre, aOreMaterial, 1));
                            }
                        }
                    }
                    case UnifiedBlock -> {
                        // Unified ore
                        for (int i = 0; i < (tIsRich ? 2 : 1); i++) {
                            rList.add(new ItemStack(aDroppedOre, 1, this.mMetaData % 1000));
                        }
                    }
                    case PerDimBlock -> {
                        // Per Dimension ore
                        if (tIsRich) {
                            rList.add(new ItemStack(aDroppedOre, 1, this.mMetaData));
                        } else {
                            rList.add(new ItemStack(aDroppedOre, 1, this.mMetaData % 1000));
                        }
                    }
                    case Block -> {
                        // Regular ore
                        rList.add(new ItemStack(aDroppedOre, 1, this.mMetaData));
                    }
                }
            }
            return rList;
        }

        // Everyone gets a free small fortune boost
        aFortune += 1;
        if (!this.mNatural) {
            aFortune = 0;
        }
        if (aOreMaterial != null) {
            Random tRandom = new XSTR(this.xCoord ^ this.yCoord ^ this.zCoord);
            ArrayList<ItemStack> tSelector = new ArrayList<>();

            Materials aMaterial = aOreMaterial.mOreReplacement;

            ItemStack tStack = GTOreDictUnificator
                .get(OrePrefixes.gemExquisite, aMaterial, GTOreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L), 1L);
            if (tStack != null) {
                for (int i = 0; i < 1; i++) {
                    tSelector.add(tStack);
                }
            }
            tStack = GTOreDictUnificator
                .get(OrePrefixes.gemFlawless, aMaterial, GTOreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L), 1L);
            if (tStack != null) {
                for (int i = 0; i < 2; i++) {
                    tSelector.add(tStack);
                }
            }
            tStack = GTOreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L);
            if (tStack != null) {
                for (int i = 0; i < 12; i++) {
                    tSelector.add(tStack);
                }
            }
            tStack = GTOreDictUnificator
                .get(OrePrefixes.gemFlawed, aMaterial, GTOreDictUnificator.get(OrePrefixes.crushed, aMaterial, 1L), 1L);
            if (tStack != null) {
                for (int i = 0; i < 5; i++) {
                    tSelector.add(tStack);
                }
            }
            tStack = GTOreDictUnificator.get(OrePrefixes.crushed, aMaterial, 1L);
            if (tStack != null) {
                for (int i = 0; i < 10; i++) {
                    tSelector.add(tStack);
                }
            }
            tStack = GTOreDictUnificator.get(
                OrePrefixes.gemChipped,
                aMaterial,
                GTOreDictUnificator.get(OrePrefixes.dustImpure, aMaterial, 1L),
                1L);
            if (tStack != null) {
                for (int i = 0; i < 5; i++) {
                    tSelector.add(tStack);
                }
            }
            tStack = GTOreDictUnificator.get(OrePrefixes.dustImpure, aMaterial, 1L);
            if (tStack != null) {
                for (int i = 0; i < 10; i++) {
                    tSelector.add(tStack);
                }
            }
            if (!tSelector.isEmpty()) {
                int i = 0;
                for (int j = Math.max(
                    1,
                    aMaterial.mOreMultiplier
                        + (aFortune > 0 ? tRandom.nextInt(1 + aFortune * aMaterial.mOreMultiplier) : 0) / 2); i
                            < j; i++) {
                    rList.add(GTUtility.copyAmount(1, tSelector.get(tRandom.nextInt(tSelector.size()))));
                }
            }
            if (tRandom.nextInt(3 + aFortune) > 1) {
                Materials dustMat = ((BlockOresAbstract) aDroppedOre).getDroppedDusts()[this.mMetaData / 1000 % 16];
                if (dustMat != null) rList.add(
                    GTOreDictUnificator
                        .get(tRandom.nextInt(3) > 0 ? OrePrefixes.dustImpure : OrePrefixes.dust, dustMat, 1L));
            }
        }
        return rList;
    }

    public ArrayList<ItemStack> getSilkTouchDrops(Block aDroppedOre) {
        final boolean tShouldSilkTouch = TileEntityOres.shouldSilkTouch;
        TileEntityOres.shouldSilkTouch = true;
        final ArrayList<ItemStack> tDrops = getDrops(aDroppedOre, 0);
        TileEntityOres.shouldSilkTouch = tShouldSilkTouch;
        return tDrops;
    }

    @Override
    public ITexture[] getTexture(Block aBlock) {
        if (mMetadataForCachedTexture == mMetaData && mCachedTexture != null) return mCachedTexture;

        mMetadataForCachedTexture = mMetaData;
        mCachedTexture = getTextureInternal(aBlock);
        return mCachedTexture;
    }

    private ITexture @NotNull [] getTextureInternal(Block aBlock) {
        Materials aMaterial = GregTechAPI.sGeneratedMaterials[(this.mMetaData % 1000)];
        if ((aMaterial != null) && (this.mMetaData < 32000) && (aBlock instanceof BlockOresAbstract)) {
            ITexture iTexture = TextureFactory.builder()
                .addIcon(
                    aMaterial.mIconSet.mTextures[this.mMetaData / 16000 == 0 ? OrePrefixes.ore.mTextureIndex
                        : OrePrefixes.oreSmall.mTextureIndex])
                .setRGBA(aMaterial.mRGBa)
                .stdOrient()
                .build();
            return new ITexture[] { ((BlockOresAbstract) aBlock).getTextureSet()[((this.mMetaData / 1000) % 16)],
                iTexture };
        }
        return new ITexture[] { TextureFactory.of(Blocks.stone, 0), TextureFactory.builder()
            .addIcon(SET_NONE.mTextures[OrePrefixes.ore.mTextureIndex])
            .stdOrient()
            .build() };
    }
}

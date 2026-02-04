package gregtech.common.tileentities.machines.basic;

import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.Mods.GregTech;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTETieredMachineBlock;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTLog;

public class MTEWorldAccelerator extends MTETieredMachineBlock {

    // simple name is rather expensive to compute and it's not cached
    // see https://stackoverflow.com/q/17369304
    private static final ClassValue<String> simpleNameCache = new ClassValue<>() {

        @Override
        protected String computeValue(Class<?> type) {
            return type.getSimpleName();
        }
    };
    private static final HashSet<Class<? extends TileEntity>> _mBlacklistedTiles = new HashSet<>();

    public static boolean addTileToBlacklist(Class<? extends TileEntity> clazz) {
        return _mBlacklistedTiles.add(clazz);
    }

    public static boolean addTileToBlacklist(TileEntity tileEntity) {
        return _mBlacklistedTiles.add(tileEntity.getClass());
    }

    public static HashSet<Class<? extends TileEntity>> get_mBlacklistedTiles() {
        return _mBlacklistedTiles;
    }

    private int _mRadiusTierOverride = -1;
    private int _mSpeedTierOverride = -1;

    public final int getRadiusTierOverride() {
        if (_mRadiusTierOverride == -1) _mRadiusTierOverride = mTier;
        return _mRadiusTierOverride;
    }

    public final int getSpeedTierOverride() {
        if (_mSpeedTierOverride == -1) _mSpeedTierOverride = mTier;
        return _mSpeedTierOverride;
    }

    private int incSpeedTierOverride() {
        _mSpeedTierOverride = getSpeedTierOverride() + 1;
        if (_mSpeedTierOverride > mTier) _mSpeedTierOverride = 1;

        return _mSpeedTierOverride;
    }

    private int incRadiusTierOverride() {
        // Make sure we get the Override value first, as we check it for initial -1
        _mRadiusTierOverride = getRadiusTierOverride() + 1;
        if (_mRadiusTierOverride > mTier) _mRadiusTierOverride = 1;

        return _mRadiusTierOverride;
    }

    private byte mMode = 1; // 0: RandomTicks around 1: TileEntities with range 1
    private static IIconContainer _mGTIco_Norm_Idle;
    private static IIconContainer _mGTIco_Norm_Active;
    private static IIconContainer _mGTIco_TE_Idle;
    private static IIconContainer _mGTIco_TE_Active;
    public static final int[] mAccelerateStatic = { 1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 512, 512, 512, 512, 512,
        512 };
    private static final int AMPERAGE_NORMAL = 3;
    private static final int AMPERAGE_TE = 6;

    @Override
    public void registerIcons(IIconRegister aBlockIconRegister) {
        super.registerIcons(aBlockIconRegister);
        _mGTIco_Norm_Idle = Textures.BlockIcons.customOptional("iconsets/OVERLAY_ACCELERATOR");
        _mGTIco_Norm_Active = Textures.BlockIcons.customOptional("iconsets/OVERLAY_ACCELERATOR_ACTIVE");
        _mGTIco_TE_Idle = Textures.BlockIcons.customOptional("iconsets/OVERLAY_ACCELERATOR_TE");
        _mGTIco_TE_Active = Textures.BlockIcons.customOptional("iconsets/OVERLAY_ACCELERATOR_TE_ACTIVE");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void onValueUpdate(byte aValue) {
        mMode = aValue;
    }

    @Override
    public byte getUpdateData() {
        return mMode;
    }

    public MTEWorldAccelerator(int pID, String pName, String pNameRegional, int pTier) {
        super(pID, pName, pNameRegional, pTier, 0, "");
    }

    @Override
    public String[] getDescription() {
        return new String[] { "Machine Type: " + EnumChatFormatting.YELLOW + "World Accelerator, WA",
            "Max Speed Bonus " + EnumChatFormatting.GREEN + String.format("x%d", mAccelerateStatic[mTier]),
            EnumChatFormatting.GOLD + "Blocks Mode: "
                + EnumChatFormatting.RESET
                + String.format("Range: 1-%d blocks to each side | Amps \u2264%s", mTier, AMPERAGE_NORMAL),
            EnumChatFormatting.GOLD + "TileEntity Mode: "
                + EnumChatFormatting.RESET
                + String.format("Adjacent blocks only | Amps \u2264%s", AMPERAGE_TE),
            "Use a screwdriver to change mode, sneak to change range", "Use a wrench to change speed",
            "Power consumption increases with speed/range" };
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        List<String> tInfoDisplay = new ArrayList<>();

        tInfoDisplay.add(
            StatCollector.translateToLocalFormatted(
                "GT5U.infodata.world_accelerator.mode",
                StatCollector.translateToLocal(mUnlocalizedModeStr[mMode])));
        tInfoDisplay.add(
            StatCollector.translateToLocalFormatted(
                "GT5U.infodata.world_accelerator.speed",
                mAccelerateStatic[getSpeedTierOverride()],
                mAccelerateStatic[mTier]));
        tInfoDisplay.add(
            StatCollector.translateToLocalFormatted(
                "GT5U.infodata.world_accelerator.consuming",
                getEnergyDemand(getSpeedTierOverride(), getRadiusTierOverride(), mMode == 1)));

        // Don't show radius setting if in TE Mode
        if (mMode == 0) tInfoDisplay.add(
            StatCollector
                .translateToLocalFormatted("GT5U.infodata.world_accelerator.radius", getRadiusTierOverride(), mTier));

        return tInfoDisplay.toArray(new String[0]);
    }

    public MTEWorldAccelerator(String pName, int pTier, int pInvSlotCount, String[] pDescription,
        ITexture[][][] pTextures) {
        super(pName, pTier, pInvSlotCount, pDescription, pTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity pTileEntity) {
        return new MTEWorldAccelerator(mName, mTier, mInventory.length, mDescriptionArray, mTextures);
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] pTextures) {
        return null;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity pBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean pActive, boolean pRedstone) {
        if (mMode == 0) {
            return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][colorIndex + 1], side.offsetY != 0 ? null
                : pActive ? TextureFactory.of(_mGTIco_Norm_Active) : TextureFactory.of(_mGTIco_Norm_Idle) };
        } else {
            return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][colorIndex + 1], side.offsetY != 0 ? null
                : pActive ? TextureFactory.of(_mGTIco_TE_Active) : TextureFactory.of(_mGTIco_TE_Idle) };
        }
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity pBaseMetaTileEntity, int pIndex, ForgeDirection side,
        ItemStack pStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity pBaseMetaTileEntity, int pIndex, ForgeDirection side,
        ItemStack pStack) {
        return false;
    }

    @Override
    public void saveNBTData(NBTTagCompound pNBT) {
        pNBT.setByte("mAccelMode", mMode);

        // SpeedOverride can never be larger than mTier; Which will never exceed 255, so it's safe to cast here
        pNBT.setByte("mSpeed", (byte) getSpeedTierOverride());
        pNBT.setByte("mRadius", (byte) getRadiusTierOverride());
    }

    public long getEnergyDemand(int pSpeedTier, int pRangeTier, boolean pIsAcceleratingTEs) {
        // TE mode does not need to consider range setting
        if (pIsAcceleratingTEs) return V[pSpeedTier] * AMPERAGE_TE;

        // Include range setting into power calculation
        float multiplier = 100.0F / (float) mTier * (float) pRangeTier / 100.0F;
        long demand = V[pSpeedTier] * AMPERAGE_NORMAL;

        float tDemand = demand * multiplier;

        return (int) tDemand;
    }

    @Override
    public void loadNBTData(NBTTagCompound pNBT) {
        mMode = pNBT.getByte("mAccelMode");

        // Make sure we're not crashing with old Accelerator Machines
        if (pNBT.hasKey("mSpeed")) _mSpeedTierOverride = pNBT.getByte("mSpeed");
        if (pNBT.hasKey("mRadius")) _mRadiusTierOverride = pNBT.getByte("mRadius");
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isEnetInput() {
        return true;
    }

    @Override
    public boolean isInputFacing(ForgeDirection side) {
        return true;
    }

    @Override
    public boolean isTeleporterCompatible() {
        return false;
    }

    @Override
    public long maxEUStore() {
        return 512 + V[mTier] * 50;
    }

    @Override
    public long maxEUInput() {
        return V[mTier];
    }

    @Override
    public long maxAmperesIn() {
        return 8;
    }

    private static final String[] mUnlocalizedModeStr = { "GT5U.word_accelerator.mode.blocks",
        "GT5U.word_accelerator.mode.tile_entities" };

    // This uses the Wrench as second tool to cycle speeds
    @Override
    public boolean onWrenchRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer pPlayer, float aX,
        float aY, float aZ, ItemStack aTool) {
        incSpeedTierOverride();
        getBaseMetaTileEntity().issueTileUpdate();

        markDirty();
        if (pPlayer instanceof EntityPlayerMP playerMP) {
            playerMP.addChatMessage(
                new ChatComponentTranslation(
                    "tt.block.world_accelerator.set_speed",
                    mAccelerateStatic[getSpeedTierOverride()]));
        }

        return true;
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer pPlayer, float pX, float pY, float pZ,
        ItemStack aTool) {
        if (pPlayer.isSneaking()) {
            if (mMode == 0) {
                incRadiusTierOverride();

                markDirty();
                if (pPlayer instanceof EntityPlayerMP playerMP) {
                    playerMP.addChatMessage(
                        new ChatComponentTranslation("tt.block.world_accelerator.set_range", getRadiusTierOverride()));
                }
            } else {
                if (pPlayer instanceof EntityPlayerMP playerMP) {
                    playerMP.addChatMessage(
                        new ChatComponentTranslation("tt.block.world_accelerator.set_range_fail")
                            .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
                }
            }
        } else {
            mMode = (byte) (mMode == 0x00 ? 0x01 : 0x00);
            markDirty();
            if (pPlayer instanceof EntityPlayerMP playerMP) {
                playerMP.addChatMessage(
                    new ChatComponentTranslation(
                        "tt.block.world_accelerator.set_mode",
                        new ChatComponentTranslation(mUnlocalizedModeStr[mMode])));
            }
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity pBaseMetaTileEntity, long pTick) {
        try {
            if (!pBaseMetaTileEntity.isServerSide()) {
                return;
            }

            long tEnergyDemand = getEnergyDemand(getSpeedTierOverride(), getRadiusTierOverride(), mMode == 1);

            // Do we have enough energy to run? Or are we not allowed to run?
            if (pBaseMetaTileEntity.getStoredEU() < tEnergyDemand || !pBaseMetaTileEntity.isAllowedToWork()) {
                // Check if machine was active before
                if (pBaseMetaTileEntity.isActive()) {
                    pBaseMetaTileEntity.setActive(false); // Then disable it now
                }
            } else {
                // Continue to drain power
                if (pBaseMetaTileEntity.decreaseStoredEnergyUnits(tEnergyDemand, false)) {
                    World tWorld = pBaseMetaTileEntity.getWorld();
                    // Limit the random ticks to once per second
                    if (mMode == 0) {
                        if (pTick % 20 == 0) {
                            doAccelerateNormalBlocks(pBaseMetaTileEntity, tWorld);
                        }
                    } else {
                        doAccelerateTileEntities(pBaseMetaTileEntity, tWorld);
                    }

                } else {
                    // Energy drain failed. Disable machine
                    if (pBaseMetaTileEntity.isActive()) {
                        pBaseMetaTileEntity.setActive(false); // Then disable it now
                    }
                }
            }
        } catch (Exception e) {
            GTLog.err.println("MTEWorldAccelerator.onPostTick.crash\n" + e.getMessage());
        }
    }

    /**
     * Send the acceleration value to the client
     */
    @Override
    public NBTTagCompound getDescriptionData() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("acceleration", getSpeedTierOverride());
        return tag;
    }

    @Override
    public void onDescriptionPacket(NBTTagCompound data) {
        this._mSpeedTierOverride = data.getInteger("acceleration");
    }

    private void doAccelerateTileEntities(IGregTechTileEntity pBaseMetaTileEntity, World pWorld) {
        try {
            if (!pBaseMetaTileEntity.isActive()) {
                getBaseMetaTileEntity().setActive(true);
            }

            for (ForgeDirection tDir : ForgeDirection.VALID_DIRECTIONS) {
                TileEntity tTile = pBaseMetaTileEntity.getTileEntityAtSide(tDir);
                if (isTEBlackListed(tTile)) {
                    continue;
                }

                final long tMaxTime = System.nanoTime() + 1000000;
                final int iterations = mAccelerateStatic[getSpeedTierOverride()];
                for (int j = 0; j < iterations; j++) {
                    tTile.updateEntity();
                    if (System.nanoTime() > tMaxTime) {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            GTLog.err.println("MTEWorldAccelerator.doAccelerateTileEntities.crash\n" + e.getMessage());
        }
    }

    // Inspired by ChromatiCraft's TileAccelerator
    public static boolean isTEBlackListed(TileEntity pTile) {
        if (pTile == null) {
            return true; // Obvious
        }
        if (!pTile.canUpdate()) {
            return true; // Skip if TE can't update at all
        }
        if (pTile.isInvalid()) {
            return true; // Obvious
        }

        String tSimpleClassName = simpleNameCache.get(pTile.getClass());
        String tCanonicalName = pTile.getClass()
            .getCanonicalName()
            .toLowerCase();
        if (tSimpleClassName.contains("conduit") || tSimpleClassName.contains("wire")
            || tSimpleClassName.contains("cable")) {
            return true;
        }
        if (tCanonicalName.contains("appeng") || tCanonicalName.contains(GregTech.ID)) // Don't accelerate ANY gregtech
        // machines
        {
            return true;
        }
        if (tSimpleClassName.contains("solar") || tCanonicalName.contains("solar")) // Don't accelerate ANY solars
        {
            return true;
        }

        for (String tS : GTValues.blacklistedTileEntiyClassNamesForWA) {
            if (tCanonicalName.equalsIgnoreCase(tS)) {
                return true;
            }
        }

        return MTEWorldAccelerator._mBlacklistedTiles.stream()
            .map(Class::getCanonicalName)
            .map(String::toLowerCase)
            .anyMatch(tCanonicalName::equalsIgnoreCase);
    }

    /**
     * Accelerate normal blocks. Eats some power and adds randomTicks to every block within its working area
     * (Tier-Number = radius) This does only affect blocks that implement the "RandomTick" method; Which is mostly used
     * for grass growth and plants.
     *
     * @param pBaseMetaTileEntity
     */
    private void doAccelerateNormalBlocks(IGregTechTileEntity pBaseMetaTileEntity, World pWorld) {
        if (!pBaseMetaTileEntity.isActive()) {
            getBaseMetaTileEntity().setActive(true);
        }

        Random rnd = new Random();
        int tX = pBaseMetaTileEntity.getXCoord();
        int tY = pBaseMetaTileEntity.getYCoord();
        int tZ = pBaseMetaTileEntity.getZCoord();

        int tX1 = tX - getRadiusTierOverride();
        int tX2 = tX + getRadiusTierOverride();
        int tY1 = Math.max(tY - getRadiusTierOverride(), 0); // Limit to bedrock
        int tY2 = Math.min(tY + getRadiusTierOverride(), 255); // Limit to build height
        int tZ1 = tZ - getRadiusTierOverride();
        int tZ2 = tZ + getRadiusTierOverride();

        for (int xi = tX1; xi <= tX2; xi++) {
            for (int yi = tY1; yi <= tY2; yi++) {
                for (int zi = tZ1; zi <= tZ2; zi++) {
                    tryTickBlock(pWorld, xi, yi, zi, rnd);
                }
            }
        }
    }

    /**
     * Send a tick to the target block
     *
     * @param pWorld
     * @param pX
     * @param pY
     * @param pZ
     * @param pRnd
     */
    private void tryTickBlock(World pWorld, int pX, int pY, int pZ, Random pRnd) {
        try {
            for (int j = 0; j < getSpeedTierOverride(); j++) {
                Block tBlock = pWorld.getBlock(pX, pY, pZ);
                if (tBlock.getTickRandomly()) {
                    tBlock.updateTick(pWorld, pX, pY, pZ, pRnd);
                }
            }
        } catch (Exception e) {
            GTLog.err.println("MTEWorldAccelerator.tryTickBlock.crash\n" + e.getMessage());
        }
    }

    /**
     * This method makes it easier for other mods to get the WA's bonus
     *
     * @return the acceleration bonus of the TE
     */
    public static int getAccelerationForTE(TileEntity pTile) {
        return isTEBlackListed(pTile) ? 0 : getAccelerationForTEUnsafe(pTile);
    }

    /**
     * Same as the method above, but now without the TE check. Only use this is you're 100% sure what type the TE is.
     *
     * @return the acceleration bonus of the TE
     */
    public static int getAccelerationForTEUnsafe(TileEntity pTile) {
        final int x = pTile.xCoord;
        final int y = pTile.yCoord;
        final int z = pTile.zCoord;
        final World worldObj = pTile.getWorldObj();

        int acceleration = 0;
        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            final TileEntity te = worldObj.getTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
            if (te instanceof BaseMetaTileEntity mte) {
                if (mte.getMetaTileEntity() instanceof MTEWorldAccelerator accelerator && mte.isActive()
                    && accelerator.mMode != 0) {
                    acceleration += mAccelerateStatic[accelerator.getSpeedTierOverride()];
                }
            }
        }
        return acceleration;
    }
}

package gregtech.common.tileentities.boilers;

import static gregtech.api.enums.Textures.BlockIcons.BOILER_LAVA_FRONT;
import static gregtech.api.enums.Textures.BlockIcons.BOILER_LAVA_FRONT_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.BOILER_LAVA_FRONT_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.BOILER_LAVA_FRONT_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.FLUID_IN_SIGN;
import static gregtech.api.enums.Textures.BlockIcons.FLUID_OUT_SIGN;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_STEELBRICKS_BOTTOM;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_STEELBRICKS_SIDE;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_STEELBRICKS_TOP;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DRAIN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE_OUT;
import static gregtech.api.objects.XSTR.XSTR_INSTANCE;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;

import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.FluidSlotWidget;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GT_Mod;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.ParticleFX;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.SteamVariant;
import gregtech.api.gui.modularui.GT_UIInfos;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.WorldSpawnedEventBuilder.ParticleEventBuilder;

public class GT_MetaTileEntity_Boiler_Lava extends GT_MetaTileEntity_Boiler {

    public static final int COOLDOWN_INTERVAL = 20;
    public static final int ENERGY_PER_LAVA = 1;
    public static final int CONSUMPTION_PER_HEATUP = 3;
    public static final int PRODUCTION_PER_SECOND = 600;
    private final FluidTank lavaTank = new LavaTank(null, getCapacity());
    private int mCooledLava = 0;

    public GT_MetaTileEntity_Boiler_Lava(int aID, String aName, String aNameRegional) {
        super(
            aID,
            aName,
            aNameRegional,
            new String[] { "A Boiler running off Lava", "Produces " + PRODUCTION_PER_SECOND + "L of Steam per second",
                "Causes " + GT_Mod.gregtechproxy.mPollutionHighPressureLavaBoilerPerSecond + " Pollution per second",
                "Consumes " + ((double) CONSUMPTION_PER_HEATUP / ENERGY_PER_LAVA)
                    + "L of Lava every "
                    + COOLDOWN_INTERVAL
                    + " ticks when fully heat up" });
    }

    public GT_MetaTileEntity_Boiler_Lava(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    public GT_MetaTileEntity_Boiler_Lava(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        final ForgeDirection rearDirection = facingDirection.getOpposite();
        final ITexture[] tmp;
        if ((sideDirection.flag & (ForgeDirection.UP.flag | ForgeDirection.DOWN.flag)) == 0) {
            if (sideDirection == facingDirection) {
                if (active) tmp = mTextures[4][colorIndex + 1];
                else tmp = mTextures[3][colorIndex + 1];
            } else if (sideDirection == rearDirection) {
                tmp = mTextures[5][colorIndex + 1];
            } else {
                tmp = mTextures[2][colorIndex + 1];
            }
        } else tmp = mTextures[sideDirection.ordinal()][colorIndex + 1];
        if (sideDirection != facingDirection && tmp.length == 2) {
            return new ITexture[] { tmp[0] };
        }
        return tmp;
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        ITexture[][][] rTextures = new ITexture[6][17][];
        for (byte color = -1; color < 16; color++) {
            int i = color + 1;
            short[] colorModulation = Dyes.getModulation(color, Dyes._NULL.mRGBa);
            rTextures[0][i] = new ITexture[] { TextureFactory.of(MACHINE_STEELBRICKS_BOTTOM, colorModulation) };
            rTextures[1][i] = new ITexture[] { TextureFactory.of(MACHINE_STEELBRICKS_TOP, colorModulation),
                TextureFactory.of(OVERLAY_DRAIN), TextureFactory.of(FLUID_IN_SIGN) };
            rTextures[2][i] = new ITexture[] { TextureFactory.of(MACHINE_STEELBRICKS_SIDE, colorModulation),
                TextureFactory.of(OVERLAY_PIPE_OUT), TextureFactory.of(FLUID_IN_SIGN) };
            rTextures[3][i] = new ITexture[] { TextureFactory.of(MACHINE_STEELBRICKS_SIDE, colorModulation),
                TextureFactory.of(BOILER_LAVA_FRONT, colorModulation), TextureFactory.of(BOILER_LAVA_FRONT_GLOW) };
            rTextures[4][i] = new ITexture[] { TextureFactory.of(MACHINE_STEELBRICKS_SIDE, colorModulation),
                TextureFactory.of(BOILER_LAVA_FRONT_ACTIVE), TextureFactory.builder()
                    .addIcon(BOILER_LAVA_FRONT_ACTIVE_GLOW)
                    .glow()
                    .build() };
            rTextures[5][i] = new ITexture[] { TextureFactory.of(MACHINE_STEELBRICKS_SIDE, colorModulation),
                TextureFactory.of(OVERLAY_PIPE_OUT), TextureFactory.of(FLUID_OUT_SIGN) };
        }
        return rTextures;
    }

    @Override
    public int maxProgresstime() {
        return 1000;
    }

    @Override
    public int getCapacity() {
        return 32000;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Boiler_Lava(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    protected int getPollution() {
        return GT_Mod.gregtechproxy.mPollutionHighPressureLavaBoilerPerSecond;
    }

    @Override
    protected int getProductionPerSecond() {
        return PRODUCTION_PER_SECOND;
    }

    @Override
    protected int getMaxTemperature() {
        return 1000;
    }

    @Override
    protected int getEnergyConsumption() {
        this.mCooledLava += CONSUMPTION_PER_HEATUP;
        return CONSUMPTION_PER_HEATUP;
    }

    @Override
    protected int getCooldownInterval() {
        return COOLDOWN_INTERVAL;
    }

    /**
     * Attempts to fill an {@link IFluidTank} from the {@link FluidStack} content of an {@link ItemStack}
     *
     * @param destinationIFluidTank The destination {@link IFluidTank} to fill
     * @param SourceItemStack       The source {@link ItemStack} containing the Fluid
     * @return The {@link ItemStack} of the Empty version of the source {@link ItemStack} or {@code null} if none
     */
    public static ItemStack fillIFluidTankFromItemStack(IFluidTank destinationIFluidTank, ItemStack SourceItemStack) {
        if (destinationIFluidTank == null || SourceItemStack == null) return null;

        final FluidStack containedFluidStack = GT_Utility.getFluidForFilledItem(SourceItemStack, true);
        if (containedFluidStack == null || containedFluidStack.amount == 0) return null;

        final int fillableAmount = destinationIFluidTank.fill(containedFluidStack, false);
        if (fillableAmount <= 0) return null;

        final Item containerItem = SourceItemStack.getItem();
        if (containerItem instanceof IFluidContainerItem equippedIFluidContainerItem) {
            destinationIFluidTank.fill(equippedIFluidContainerItem.drain(SourceItemStack, fillableAmount, true), true);
            return null;
        } else {
            final ItemStack emptyContainerItemStack = GT_Utility.getContainerForFilledItem(SourceItemStack, false);
            destinationIFluidTank.fill(containedFluidStack, true);
            return emptyContainerItemStack;
        }
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide() || aPlayer == null) return true;

        final ItemStack equippedItemStack = aPlayer.getCurrentEquippedItem();
        final FluidStack equippedContainerFluidStack = GT_Utility.getFluidForFilledItem(equippedItemStack, true);
        final ItemStack returnedItemStack;
        final IFluidTank tank;

        if (GT_ModHandler.isWater(equippedContainerFluidStack)) {
            tank = this;
        } else if (GT_ModHandler.isLava(equippedContainerFluidStack)) {
            tank = lavaTank;
        } else {
            GT_UIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
            return true;
        }
        returnedItemStack = fillIFluidTankFromItemStack(tank, equippedItemStack);
        if (returnedItemStack != null && !aPlayer.capabilities.isCreativeMode) {
            if (equippedItemStack.stackSize > 1) {
                if (!aPlayer.inventory.addItemStackToInventory(returnedItemStack)) {
                    aBaseMetaTileEntity.getWorld()
                        .spawnEntityInWorld(
                            new EntityItem(
                                aBaseMetaTileEntity.getWorld(),
                                (double) aBaseMetaTileEntity.getXCoord() + 0.5D,
                                (double) aBaseMetaTileEntity.getYCoord() + 1.5D,
                                (double) aBaseMetaTileEntity.getZCoord() + 0.5D,
                                equippedItemStack));
                } else if (aPlayer instanceof EntityPlayerMP) {
                    ((EntityPlayerMP) aPlayer).sendContainerToPlayer(aPlayer.inventoryContainer);
                }
                aPlayer.inventory.decrStackSize(aPlayer.inventory.currentItem, 1);
            } else {
                aPlayer.inventory.setInventorySlotContents(aPlayer.inventory.currentItem, returnedItemStack);
            }
        }
        return true;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        if (lavaTank.getFluid() != null) aNBT.setTag(
            "mLava",
            lavaTank.getFluid()
                .writeToNBT(new NBTTagCompound()));
        aNBT.setInteger("mCooledLava", this.mCooledLava);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        lavaTank.setFluid(FluidStack.loadFluidStackFromNBT(aNBT.getCompoundTag("mLava")));
        this.mCooledLava = aNBT.getInteger("mCooledLava");
    }

    /**
     * Pushes steam to Fluid inventory at the rear.
     *
     * @param aBaseMetaTileEntity The tile-entity instance of this Lava Boiler
     */
    @Override
    protected void pushSteamToInventories(IGregTechTileEntity aBaseMetaTileEntity) {
        if (mSteam == null || mSteam.amount == 0) return;
        pushSteamToSide(
            aBaseMetaTileEntity,
            aBaseMetaTileEntity.getFrontFacing()
                .getOpposite());
    }

    /**
     * Drains Lava from Fluid inventory on top
     *
     * @param aBaseMetaTileEntity The tile-entity instance of this Lava Boiler
     */
    protected void drainLava(IGregTechTileEntity aBaseMetaTileEntity) {
        final IFluidHandler upTank = aBaseMetaTileEntity.getITankContainerAtSide(ForgeDirection.UP);
        if (upTank == null) return;
        // Simulates drain of maximum lava amount up to 1000L that can fit the internal tank
        final FluidStack drainableLavaStack = upTank.drain(
            ForgeDirection.DOWN,
            FluidRegistry.getFluidStack(
                "lava",
                Math.min(
                    this.lavaTank.getCapacity()
                        - (this.lavaTank.getFluid() != null ? this.lavaTank.getFluid().amount : 0),
                    1000)),
            false);
        if (!GT_ModHandler.isLava(drainableLavaStack) || drainableLavaStack.amount <= 0) return;
        // Performs actual drain up and fill internal tank
        this.lavaTank.fill(upTank.drain(ForgeDirection.DOWN, drainableLavaStack, true), true);
    }

    /**
     * Processes cooled Lava into Obsidian
     *
     * @return success | failure when cannot output
     */
    private boolean lavaToObsidian() {
        if (this.mCooledLava >= 1000) {
            if (getBaseMetaTileEntity().addStackToSlot(3, new ItemStack(Blocks.obsidian, 1))) {
                this.mCooledLava -= 1000;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * Draws random flames and smoke particles in front of this Lava Boiler when it is active
     *
     * @param aBaseMetaTileEntity The entity that will handle the {@link Block#randomDisplayTick}
     */
    @SideOnly(Side.CLIENT)
    @Override
    public void onRandomDisplayTick(IGregTechTileEntity aBaseMetaTileEntity) {
        if (aBaseMetaTileEntity.isActive()) {

            final ForgeDirection frontFacing = aBaseMetaTileEntity.getFrontFacing();

            if ((frontFacing.flag & (ForgeDirection.UP.flag | ForgeDirection.DOWN.flag)) == 0
                && aBaseMetaTileEntity.getCoverIDAtSide(frontFacing) == 0
                && !aBaseMetaTileEntity.getOpacityAtSide(frontFacing)) {

                final double oX = aBaseMetaTileEntity.getOffsetX(frontFacing, 1) + 8D / 16D;
                final double oY = aBaseMetaTileEntity.getOffsetY(frontFacing, 1);
                final double oZ = aBaseMetaTileEntity.getOffsetZ(frontFacing, 1) + 8D / 16D;
                final double offset = -0.48D;
                final double horizontal = XSTR_INSTANCE.nextFloat() * 10D / 16D - 5D / 16D;

                final double x, y, z;

                y = oY + XSTR_INSTANCE.nextFloat() * 6D / 16D;

                switch (frontFacing) {
                    case WEST -> {
                        x = oX - offset;
                        z = oZ + horizontal;
                    }
                    case EAST -> {
                        x = oX + offset;
                        z = oZ + horizontal;
                    }
                    case NORTH -> {
                        x = oX + horizontal;
                        z = oZ - offset;
                    }
                    default -> { // case SOUTH:
                        x = oX + horizontal;
                        z = oZ + offset;
                    }
                }

                ParticleEventBuilder particleEventBuilder = (new ParticleEventBuilder()).setMotion(0D, 0D, 0D)
                    .setPosition(x, y, z)
                    .setWorld(getBaseMetaTileEntity().getWorld());
                particleEventBuilder.setIdentifier(ParticleFX.SMOKE)
                    .run();
                particleEventBuilder.setIdentifier(ParticleFX.FLAME)
                    .run();
            }
        }
    }

    @Override
    public boolean isFluidInputAllowed(FluidStack aFluid) {
        return GT_ModHandler.isWater(aFluid) || GT_ModHandler.isLava(aFluid);
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (!aBaseMetaTileEntity.isServerSide()) return;
        final FluidStack containedFluidStack = GT_Utility.getFluidForFilledItem(mInventory[getInputSlot()], true);
        if (GT_ModHandler.isWater(containedFluidStack)) super.onPreTick(aBaseMetaTileEntity, aTick);
        if (GT_ModHandler.isLava(containedFluidStack)
            && lavaTank.fill(containedFluidStack, false) == containedFluidStack.amount
            && aBaseMetaTileEntity.addStackToSlot(
                getOutputSlot(),
                GT_Utility.getContainerForFilledItem(mInventory[getInputSlot()], true),
                1)) {
            lavaTank.fill(containedFluidStack, true);
            aBaseMetaTileEntity.decrStackSize(getInputSlot(), 1);
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aTick % 20 == 0) drainLava(aBaseMetaTileEntity);
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return true;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return true;
    }

    @Override
    public void doSound(byte aIndex, double aX, double aY, double aZ) {
        if (aIndex != GT_MetaTileEntity_Boiler.SOUND_EVENT_LET_OFF_EXCESS_STEAM) return;

        final ForgeDirection rearDirection = getBaseMetaTileEntity().getFrontFacing()
            .getOpposite();
        GT_Utility.doSoundAtClient(
            SoundResource.RANDOM_FIZZ,
            2,
            1.0F,
            // Sound emitted from center of rear face (Steam Output)
            aX + 0.5 * rearDirection.offsetX,
            aY,
            aZ + 0.5 * rearDirection.offsetZ);

        new ParticleEventBuilder().setIdentifier(ParticleFX.CLOUD)
            .setWorld(getBaseMetaTileEntity().getWorld())
            // Particles emitted with a 1 block/s velocity toward rear
            .setMotion(rearDirection.offsetX / 20D, 0D, rearDirection.offsetZ / 20D)
            .<ParticleEventBuilder>times(
                8,
                // Particles emitted from center of rear face (Steam Output)
                x -> x.setPosition(aX + rearDirection.offsetX / 2D, aY, aZ + rearDirection.offsetZ / 2D)
                    .run());
    }

    @Override
    protected void updateFuel(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (!lavaToObsidian()) return;
        if (lavaTank.getFluid() == null || lavaTank.getFluid().amount <= 0) return;
        final int amountToDrain = Math.min(lavaTank.getFluid().amount, 1000);
        final FluidStack drainedLava = lavaTank.drain(amountToDrain, false);
        if (drainedLava == null || drainedLava.amount == 0) return;
        lavaTank.drain(amountToDrain, true);
        this.mProcessingEnergy += drainedLava.amount * ENERGY_PER_LAVA;
    }

    @Override
    public SteamVariant getSteamVariant() {
        return SteamVariant.STEEL;
    }

    @Override
    public int fill(FluidStack aFluid, boolean doFill) {
        if (GT_ModHandler.isWater(aFluid)) return super.fill(aFluid, doFill);
        if (GT_ModHandler.isLava(aFluid)) return lavaTank.fill(aFluid, doFill);
        return 0;
    }

    @Override
    public FluidStack getDisplayedFluid() {
        return lavaTank.getFluid();
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection side) {
        return new FluidTankInfo[] { super.getTankInfo(side)[0],
            new FluidTankInfo(this.lavaTank.getFluid(), this.lavaTank.getCapacity()),
            new FluidTankInfo(getDrainableStack(), getCapacity()) };
    }

    @Override
    protected IDrawable[] getAshSlotBackground() {
        return new IDrawable[] { getGUITextureSet().getItemSlot(),
            GT_UITextures.OVERLAY_SLOT_BLOCK_STEAM.get(getSteamVariant()) };
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(
            new SlotWidget(inventoryHandler, 0).setPos(43, 25)
                .setBackground(getGUITextureSet().getItemSlot(), getOverlaySlotIn()))
            .widget(
                new SlotWidget(inventoryHandler, 1).setAccess(true, false)
                    .setPos(43, 61)
                    .setBackground(getGUITextureSet().getItemSlot(), getOverlaySlotOut()))
            .widget(
                new FluidSlotWidget(lavaTank).setBackground(getGUITextureSet().getFluidSlot(), getOverlaySlotIn())
                    .setPos(115, 61))
            .widget(createAshSlot())
            .widget(
                new ProgressBar().setProgress(() -> mSteam == null ? 0 : (float) mSteam.amount / getCapacity())
                    .setTexture(getProgressbarEmpty(), GT_UITextures.PROGRESSBAR_BOILER_STEAM, 10)
                    .setDirection(ProgressBar.Direction.UP)
                    .setPos(70, 25)
                    .setSize(10, 54))
            .widget(
                new ProgressBar().setProgress(() -> mFluid == null ? 0 : (float) mFluid.amount / getCapacity())
                    .setTexture(getProgressbarEmpty(), GT_UITextures.PROGRESSBAR_BOILER_WATER, 10)
                    .setDirection(ProgressBar.Direction.UP)
                    .setPos(83, 25)
                    .setSize(10, 54))
            .widget(
                new ProgressBar().setProgress(() -> (float) mTemperature / maxProgresstime())
                    .setTexture(getProgressbarEmpty(), GT_UITextures.PROGRESSBAR_BOILER_HEAT, 10)
                    .setDirection(ProgressBar.Direction.UP)
                    .setPos(96, 25)
                    .setSize(10, 54))
            .widget(
                new ProgressBar()
                    // cap minimum so that one can easily see there's fuel remaining
                    .setProgress(() -> mProcessingEnergy > 0 ? Math.max((float) mProcessingEnergy / 1000, 1f / 5) : 0)
                    .setTexture(getProgressbarFuel(), 14)
                    .setDirection(ProgressBar.Direction.UP)
                    .setPos(116, 45)
                    .setSize(14, 14))
            .widget(
                new DrawableWidget().setDrawable(getOverlaySlotCanister())
                    .setPos(43, 43)
                    .setSize(18, 18));
    }

    static class LavaTank extends FluidTank {

        public LavaTank(FluidStack stack, int capacity) {
            super(stack, capacity);
        }

        @Override
        public int fill(FluidStack resource, boolean doFill) {
            return GT_ModHandler.isLava(resource) ? super.fill(resource, doFill) : 0;
        }
    }
}

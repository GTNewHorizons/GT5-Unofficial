package gregtech.common.tileentities.machines.basic;

import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TELEPORTER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TELEPORTER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TELEPORTER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TELEPORTER_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TELEPORTER_SIDES;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TELEPORTER_SIDES_GLOW;

import java.util.List;
import java.util.function.Consumer;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.effect.EntityWeatherEffect;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.enums.ConfigCategories;
import gregtech.api.gui.modularui.GT_UIInfos;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.gui.modularui.GUITextureSet;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.modularui.IAddGregtechLogo;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicTank;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Config;
import gregtech.api.util.GT_Utility;

public class GT_MetaTileEntity_Teleporter extends GT_MetaTileEntity_BasicTank
    implements IAddGregtechLogo, IAddUIWidgets {

    private static boolean sInterDimensionalTeleportAllowed = true;
    private static int sPassiveEnergyDrain = 2048;
    private static int sPowerMultiplyer = 100;
    private static double sFPowerMultiplyer = 1.0;
    public int mTargetX = 0;
    public int mTargetY = 0;
    public int mTargetZ = 0;
    public int mTargetD = Integer.MIN_VALUE;
    public boolean mDebug = false;
    // public boolean hasEgg = false;

    public GT_MetaTileEntity_Teleporter(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            3,
            new String[] { "Teleport long distances with this little device.", "Use a Dragon Egg or Nitrogen Plasma",
                "for Inter-dimensional transmission" });
    }

    public GT_MetaTileEntity_Teleporter(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 3, aDescription, aTextures);
    }

    public GT_MetaTileEntity_Teleporter(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 3, aDescription, aTextures);
    }

    private static float weightCalculation(Entity aEntity) {
        try {
            if ((aEntity instanceof EntityFX)) {
                return -1.0F;
            }
        } catch (Throwable e) {}
        if ((aEntity instanceof EntityFishHook)) {
            return -1.0F;
        }
        if ((aEntity instanceof EntityDragonPart)) {
            return -1.0F;
        }
        if ((aEntity instanceof EntityWeatherEffect)) {
            return -1.0F;
        }
        if ((aEntity instanceof EntityPlayer tPlayer)) {
            int tCount = 64;
            for (int i = 0; i < 36; i++) {
                if (tPlayer.inventory.getStackInSlot(i) != null) {
                    tCount += (tPlayer.inventory.getStackInSlot(i)
                        .getMaxStackSize() > 1 ? tPlayer.inventory.getStackInSlot(i).stackSize : 64);
                }
            }
            for (int i = 0; i < 4; i++) {
                if (tPlayer.inventory.armorInventory[i] != null) {
                    tCount += 256;
                }
            }
            return Math.min(5.0F, tCount / 666.6F);
        }
        if (GT_Utility.getClassName(aEntity)
            .equals("EntityItnt")) {
            return 5.0F;
        }
        if (GT_Utility.getClassName(aEntity)
            .equals("EntityNuke")) {
            return 50.0F;
        }
        if ((aEntity instanceof EntityArrow)) {
            return 0.001F;
        }
        if ((aEntity instanceof EntityBoat)) {
            return 0.1F;
        }
        if ((aEntity instanceof EntityEnderCrystal)) {
            return 2.0F;
        }
        if ((aEntity instanceof EntityEnderEye)) {
            return 0.001F;
        }
        if ((aEntity instanceof EntityFireball)) {
            return 0.001F;
        }
        if ((aEntity instanceof EntityFireworkRocket)) {
            return 0.001F;
        }
        if ((aEntity instanceof EntityHanging)) {
            return 0.005F;
        }
        if ((aEntity instanceof EntityItem)) {
            return 0.001F;
        }
        if ((aEntity instanceof EntityLiving)) {
            return 0.5F;
        }
        if ((aEntity instanceof EntityMinecart)) {
            return 0.1F;
        }
        if ((aEntity instanceof EntityThrowable)) {
            return 0.001F;
        }
        if ((aEntity instanceof EntityTNTPrimed)) {
            return 5.0F;
        }
        if ((aEntity instanceof EntityXPOrb)) {
            return 0.001F;
        }
        return -1.0F;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide()) return true;
        // this.hasEgg = checkForEgg();
        GT_UIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
        return true;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Teleporter(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        return new String[] { "Coordinates:",
            "X: " + EnumChatFormatting.GREEN + GT_Utility.formatNumbers(this.mTargetX) + EnumChatFormatting.RESET,
            "Y: " + EnumChatFormatting.GREEN + GT_Utility.formatNumbers(this.mTargetY) + EnumChatFormatting.RESET,
            "Z: " + EnumChatFormatting.GREEN + GT_Utility.formatNumbers(this.mTargetZ) + EnumChatFormatting.RESET,
            "Dimension: " + EnumChatFormatting.GREEN + this.mTargetD + EnumChatFormatting.RESET,
            "Dimension Valid: " + (GT_Utility.isRealDimension(this.mTargetD)
                ? EnumChatFormatting.GREEN + "Yes" + EnumChatFormatting.RESET
                : EnumChatFormatting.RED + "No" + EnumChatFormatting.RESET),
            "Dimension Registered: " + (DimensionManager.isDimensionRegistered(this.mTargetD)
                ? EnumChatFormatting.GREEN + "Yes" + EnumChatFormatting.RESET
                : EnumChatFormatting.RED + "No" + EnumChatFormatting.RESET) };
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side != this.getBaseMetaTileEntity()
            .getFrontFacing())
            return new ITexture[] { MACHINE_CASINGS[mTier][colorIndex + 1], TextureFactory.of(OVERLAY_TELEPORTER_SIDES),
                TextureFactory.builder()
                    .addIcon(OVERLAY_TELEPORTER_SIDES_GLOW)
                    .glow()
                    .build() };
        if (aActive) return new ITexture[] { MACHINE_CASINGS[mTier][colorIndex + 1],
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
    public void saveNBTData(NBTTagCompound aNBT) {
        if (mFluid != null) aNBT.setTag("mFluid", mFluid.writeToNBT(new NBTTagCompound()));
        aNBT.setInteger("mTargetX", this.mTargetX);
        aNBT.setInteger("mTargetY", this.mTargetY);
        aNBT.setInteger("mTargetZ", this.mTargetZ);
        aNBT.setInteger("mTargetD", this.mTargetD);
        aNBT.setBoolean("mDebug", this.mDebug);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        mFluid = FluidStack.loadFluidStackFromNBT(aNBT.getCompoundTag("mFluid"));
        this.mTargetX = aNBT.getInteger("mTargetX");
        this.mTargetY = aNBT.getInteger("mTargetY");
        this.mTargetZ = aNBT.getInteger("mTargetZ");
        this.mTargetD = aNBT.getInteger("mTargetD");
        this.mDebug = aNBT.getBoolean("mDebug");
    }

    @Override
    public void onConfigLoad(GT_Config aConfig) {
        sInterDimensionalTeleportAllowed = aConfig
            .get(ConfigCategories.machineconfig, "Teleporter.Interdimensional", true);
        sPassiveEnergyDrain = aConfig
            .get(ConfigCategories.machineconfig, "Teleporter.PassiveDrain", sPassiveEnergyDrain);
        sPowerMultiplyer = aConfig.get(ConfigCategories.machineconfig, "Teleporter.PowerMultipler", sPowerMultiplyer);
        sFPowerMultiplyer = sPowerMultiplyer / 100.0;
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        if (getBaseMetaTileEntity().isServerSide()) {
            if ((this.mTargetX == 0) && (this.mTargetY == 0)
                && (this.mTargetZ == 0)
                && (this.mTargetD == Integer.MIN_VALUE)) {
                this.mTargetX = aBaseMetaTileEntity.getXCoord();
                this.mTargetY = aBaseMetaTileEntity.getYCoord();
                this.mTargetZ = aBaseMetaTileEntity.getZCoord();
                this.mTargetD = aBaseMetaTileEntity.getWorld().provider.dimensionId;
            }
            // this.hasEgg = checkForEgg();
        }
    }

    // public boolean checkForEgg() {
    // for (byte i = -5; i <= 5; i = (byte) (i + 1)) {
    // for (byte j = -5; j <= 5; j = (byte) (j + 1)) {
    // for (byte k = -5; k <= 5; k = (byte) (k + 1)) {
    // if (getBaseMetaTileEntity().getBlockOffset(i, j, k) == Blocks.dragon_egg) {
    // return true;
    // }
    // }
    // }
    // }
    // return false;
    // }

    public boolean hasDimensionalTeleportCapability() {
        return this.mDebug ||
        // (
            sInterDimensionalTeleportAllowed // &&
        // (
        // this.hasEgg ||
        // mFluid.isFluidEqual(Materials.Nitrogen.getPlasma(1)) && mFluid.amount >= 1000
        // )
        // )
        ;
    }

    public boolean isDimensionalTeleportAvailable() {
        return this.mDebug || (hasDimensionalTeleportCapability() && GT_Utility.isRealDimension(this.mTargetD)
            && GT_Utility.isRealDimension(getBaseMetaTileEntity().getWorld().provider.dimensionId));
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {

        if (mFluid != null) { // Was if null -> Materials.Nitrogen.getPlasma(0);
            mFluid = null;
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (getBaseMetaTileEntity().isServerSide()) {
            if ((getBaseMetaTileEntity().isAllowedToWork()) && (getBaseMetaTileEntity().getRedstone())) {
                if (getBaseMetaTileEntity().decreaseStoredEnergyUnits(sPassiveEnergyDrain, false)) {
                    int tDistance = distanceCalculation();
                    if (mInventory[0] != null) {
                        TileEntity tTile = null;
                        if (this.mTargetD == getBaseMetaTileEntity().getWorld().provider.dimensionId) {
                            tTile = getBaseMetaTileEntity().getTileEntity(this.mTargetX, this.mTargetY, this.mTargetZ);
                        } else {
                            World tWorld = DimensionManager.getWorld(this.mTargetD);
                            if (tWorld != null) {
                                tTile = tWorld.getTileEntity(this.mTargetX, this.mTargetY, this.mTargetZ);
                            }
                        }
                        if (tTile instanceof IInventory) {
                            int tStacksize = mInventory[0].stackSize;
                            GT_Utility.moveOneItemStack(
                                this,
                                tTile,
                                ForgeDirection.DOWN,
                                ForgeDirection.DOWN,
                                null,
                                false,
                                (byte) 64,
                                (byte) 1,
                                (byte) 64,
                                (byte) 1);
                            if (mInventory[0] == null || mInventory[0].stackSize < tStacksize) {
                                getBaseMetaTileEntity().decreaseStoredEnergyUnits(
                                    (long) (Math.pow(tDistance, 1.5) * tDistance
                                        * (tStacksize - (mInventory[0] == null ? 0 : mInventory[0].stackSize))
                                        * sFPowerMultiplyer),
                                    false);
                            }
                        }
                    }
                    List<Entity> entities_in_box = getBaseMetaTileEntity().getWorld()
                        .getEntitiesWithinAABB(
                            Entity.class,
                            AxisAlignedBB.getBoundingBox(
                                getBaseMetaTileEntity().getOffsetX(getBaseMetaTileEntity().getFrontFacing(), 2) - 1,
                                getBaseMetaTileEntity().getOffsetY(getBaseMetaTileEntity().getFrontFacing(), 2) - 1,
                                getBaseMetaTileEntity().getOffsetZ(getBaseMetaTileEntity().getFrontFacing(), 2) - 1,
                                getBaseMetaTileEntity().getOffsetX(getBaseMetaTileEntity().getFrontFacing(), 2) + 2,
                                getBaseMetaTileEntity().getOffsetY(getBaseMetaTileEntity().getFrontFacing(), 2) + 2,
                                getBaseMetaTileEntity().getOffsetZ(getBaseMetaTileEntity().getFrontFacing(), 2) + 2));

                    for (Object tObject : entities_in_box) {
                        if (((tObject instanceof Entity tEntity)) && (!((Entity) tObject).isDead)) {
                            // GT_FML_LOGGER.info("teleport"+(Math.pow(tDistance, 1.5)));
                            if (getBaseMetaTileEntity().decreaseStoredEnergyUnits(
                                (long) (Math.pow(tDistance, 1.5) * weightCalculation(tEntity) * sFPowerMultiplyer),
                                false)) {

                                if (tEntity.ridingEntity != null) {
                                    tEntity.mountEntity(null);
                                }
                                if (tEntity.riddenByEntity != null) {
                                    tEntity.riddenByEntity.mountEntity(null);
                                }
                                if ((this.mTargetD == getBaseMetaTileEntity().getWorld().provider.dimensionId)
                                    || (!isDimensionalTeleportAvailable())
                                    || (!GT_Utility.moveEntityToDimensionAtCoords(
                                        tEntity,
                                        this.mTargetD,
                                        this.mTargetX + 0.5D,
                                        this.mTargetY + 0.5D,
                                        this.mTargetZ + 0.5D))) {
                                    if ((tEntity instanceof EntityLivingBase)) {
                                        ((EntityLivingBase) tEntity).setPositionAndUpdate(
                                            this.mTargetX + 0.5D,
                                            this.mTargetY + 0.5D,
                                            this.mTargetZ + 0.5D);
                                    } else {
                                        tEntity.setPosition(
                                            this.mTargetX + 0.5D,
                                            this.mTargetY + 0.5D,
                                            this.mTargetZ + 0.5D);
                                    }
                                }
                            }
                        }
                    }
                }
                getBaseMetaTileEntity().setActive(true);
            } else {
                getBaseMetaTileEntity().setActive(false);
            }
        }
    }

    private int distanceCalculation() {
        return Math.abs(
            ((this.mTargetD != getBaseMetaTileEntity().getWorld().provider.dimensionId)
                && (isDimensionalTeleportAvailable())
                    ? 4000
                    : (int) Math.sqrt(
                        Math.pow(getBaseMetaTileEntity().getXCoord() - this.mTargetX, 2.0D)
                            + Math.pow(getBaseMetaTileEntity().getYCoord() - this.mTargetY, 2.0D)
                            + Math.pow(getBaseMetaTileEntity().getZCoord() - this.mTargetZ, 2.0D))));
    }

    @Override
    public boolean isSimpleMachine() {
        return false;
    }

    @Override
    public boolean isOverclockerUpgradable() {
        return false;
    }

    @Override
    public boolean isTransformerUpgradable() {
        return false;
    }

    @Override
    public boolean isElectric() {
        return true;
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
    public boolean isOutputFacing(ForgeDirection side) {
        return false;
    }

    @Override
    public boolean isTeleporterCompatible() {
        return false;
    }

    @Override
    public long getMinimumStoredEU() {
        return V[mTier] * 16;
    }

    @Override
    public long maxEUStore() {
        return 100000000;
    }

    @Override
    public long maxEUInput() {
        return V[mTier];
    }

    @Override
    public long maxSteamStore() {
        return maxEUStore();
    }

    @Override
    public long maxAmperesIn() {
        return 2;
    }

    @Override
    public int getStackDisplaySlot() {
        return 2;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public int getInputSlot() {
        return 0;
    }

    @Override
    public int getOutputSlot() {
        return 0;
    }

    @Override
    public int getCapacity() {
        return 64000;
    }

    @Override
    public boolean doesFillContainers() {
        return false;
    }

    @Override
    public boolean doesEmptyContainers() {
        return false;
    }

    @Override
    public boolean canTankBeFilled() {
        return true;
    }

    @Override
    public boolean canTankBeEmptied() {
        return true;
    }

    @Override
    public boolean displaysItemStack() {
        return false;
    }

    @Override
    public boolean displaysStackSize() {
        return false;
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        return null;
    }

    @Override
    public boolean useModularUI() {
        return true;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(
            new DrawableWidget().setDrawable(GT_UITextures.PICTURE_SCREEN_BLACK)
                .setSize(90, 72)
                .setPos(43, 4))
            .widget(
                TextWidget.dynamicString(() -> "X: " + GT_Utility.parseNumberToString(mTargetX))
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setPos(46, 8))
            .widget(
                TextWidget.dynamicString(() -> "Y: " + GT_Utility.parseNumberToString(mTargetY))
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setPos(46, 16))
            .widget(
                TextWidget.dynamicString(() -> "Z: " + GT_Utility.parseNumberToString(mTargetZ))
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setPos(46, 24))
            .widget(
                TextWidget.dynamicString(() -> "Dim: " + GT_Utility.parseNumberToString(mTargetD))
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setPos(46, 32))
            .widget(
                TextWidget.dynamicString(() -> "Dim Valid: " + (GT_Utility.isRealDimension(mTargetD) ? "Yes" : "No"))
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> hasDimensionalTeleportCapability())
                    .setPos(46, 40))
            .widget(new FakeSyncWidget.FluidStackSyncer(() -> mFluid, val -> mFluid = val));

        addChangeNumberButtons(builder, GT_UITextures.OVERLAY_BUTTON_MINUS_LARGE, -512, -64, 7);
        addChangeNumberButtons(builder, GT_UITextures.OVERLAY_BUTTON_MINUS_SMALL, -16, -1, 25);
        addChangeNumberButtons(builder, GT_UITextures.OVERLAY_BUTTON_PLUS_SMALL, 16, 1, 133);
        addChangeNumberButtons(builder, GT_UITextures.OVERLAY_BUTTON_PLUS_LARGE, 512, 64, 151);

        addChangeNumberButton(
            builder,
            GT_UITextures.OVERLAY_BUTTON_MINUS_LARGE,
            val -> mTargetD += val,
            -16,
            -8,
            7,
            58);
        addChangeNumberButton(
            builder,
            GT_UITextures.OVERLAY_BUTTON_MINUS_SMALL,
            val -> mTargetD += val,
            -4,
            -1,
            25,
            58);
        addChangeNumberButton(builder, GT_UITextures.OVERLAY_BUTTON_PLUS_SMALL, val -> mTargetD += val, 4, 1, 133, 58);
        addChangeNumberButton(builder, GT_UITextures.OVERLAY_BUTTON_PLUS_LARGE, val -> mTargetD += val, 16, 8, 151, 58);
    }

    private void addChangeNumberButtons(ModularWindow.Builder builder, IDrawable overlay, int addNumberShift,
        int addNumber, int xPos) {
        addChangeNumberButton(builder, overlay, val -> mTargetX += val, addNumberShift, addNumber, xPos, 4);
        addChangeNumberButton(builder, overlay, val -> mTargetY += val, addNumberShift, addNumber, xPos, 22);
        addChangeNumberButton(builder, overlay, val -> mTargetZ += val, addNumberShift, addNumber, xPos, 40);
    }

    private void addChangeNumberButton(ModularWindow.Builder builder, IDrawable overlay, Consumer<Integer> setter,
        int addNumberShift, int addNumber, int xPos, int yPos) {
        builder.widget(
            new ButtonWidget()
                .setOnClick((clickData, widget) -> setter.accept(clickData.shift ? addNumberShift : addNumber))
                .setBackground(GT_UITextures.BUTTON_STANDARD, overlay)
                .setSize(18, 18)
                .setPos(xPos, yPos));
    }

    @Override
    public GUITextureSet getGUITextureSet() {
        return new GUITextureSet().setGregTechLogo(GT_UITextures.PICTURE_GT_LOGO_17x17_TRANSPARENT_GRAY);
    }

    @Override
    public void addGregTechLogo(ModularWindow.Builder builder) {
        builder.widget(
            new DrawableWidget().setDrawable(getGUITextureSet().getGregTechLogo())
                .setSize(17, 17)
                .setPos(113, 56));
    }
}

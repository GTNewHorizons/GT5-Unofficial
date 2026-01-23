package gregtech.common.tileentities.generators;

import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAYS_ENERGY_OUT;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.internal.wrapper.BaseSlot;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;
import com.gtnewhorizons.modularui.common.widget.SlotGroup;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddGregtechLogo;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTETieredMachineBlock;
import gregtech.api.render.TextureFactory;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTESolarGenerator extends MTETieredMachineBlock implements IAddUIWidgets, IAddGregtechLogo {

    public MTESolarGenerator(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            4,
            new String[] { "Generates EU From Solar Power", "Does not generate power when raining",
                "Cleans itself automatically", "Does not explode in rain!" });
    }

    public MTESolarGenerator(String aName, int aTier, int aInvSlotCount, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        if (sideDirection == ForgeDirection.UP) {
            return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][colorIndex + 1],
                TextureFactory.of(Textures.BlockIcons.OVERLAY_SOLAR_PANEL) };
        }
        if (sideDirection == facingDirection) {
            return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][colorIndex + 1],
                OVERLAYS_ENERGY_OUT[mTier + 1] };
        }
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][colorIndex + 1] };
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        return null;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTESolarGenerator(
            this.mName,
            this.mTier,
            this.mInventory.length,
            this.mDescriptionArray,
            this.mTextures);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {}

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {}

    @Override
    public boolean willExplodeInRain() {
        return false;
    }

    // No logic for charge vs decharge because generator should not be chargeable
    @Override
    public int rechargerSlotCount() {
        return 4;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, ForgeDirection side,
        float aX, float aY, float aZ) {
        openGui(aPlayer);
        return true;
    }

    protected long clientEU;

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        addGregTechLogo(builder);
        addConditionalImages(builder);
        builder.widget(
            SlotGroup.ofItemHandler(inventoryHandler, 2)
                .startFromSlot(0)
                .endAtSlot(3)
                .slotCreator(index -> new BaseSlot(inventoryHandler, index) {

                    @Override
                    public int getSlotStackLimit() {
                        return 1;
                    }
                })
                .background(getGUITextureSet().getItemSlot(), GTUITextures.OVERLAY_SLOT_CHARGER)
                .build()
                .setPos(100, 15))
            .widget(
                new ProgressBar()
                    .setProgress(
                        () -> (float) getBaseMetaTileEntity().getStoredEU() / getBaseMetaTileEntity().getEUCapacity())
                    .setDirection(ProgressBar.Direction.RIGHT)
                    .setTexture(GTUITextures.PROGRESSBAR_STORED_EU, 147)
                    .setPos(14, 74)
                    .setSize(147, 5))
            .widget(
                new TextWidget()
                    .setStringSupplier(
                        () -> formatNumbers(clientEU) + "/"
                            + formatNumbers(getBaseMetaTileEntity().getEUCapacity())
                            + " EU")
                    .setTextAlignment(Alignment.Center)
                    .setPos(14, 66)
                    .setSize(147, 5))
            .widget(new FakeSyncWidget.LongSyncer(() -> getBaseMetaTileEntity().getStoredEU(), val -> clientEU = val));
    }

    public void addConditionalImages(ModularWindow.Builder builder) {
        builder
            .widget(
                new DrawableWidget()
                    .setDrawable(
                        () -> dayTime ? GTUITextures.OVERLAY_BUTTON_CHECKMARK : GTUITextures.OVERLAY_BUTTON_CROSS)
                    .setPos(5, 10)
                    .setSize(16, 16))
            .widget(new TextWidget(StatCollector.translateToLocal("GT5U.machines.solarindicator1")).setPos(21, 15))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> dayTime, val -> dayTime = val))
            .widget(
                new DrawableWidget()
                    .setDrawable(
                        () -> noRain ? GTUITextures.OVERLAY_BUTTON_CHECKMARK : GTUITextures.OVERLAY_BUTTON_CROSS)
                    .setPos(5, 26)
                    .setSize(16, 16))
            .widget(new TextWidget(StatCollector.translateToLocal("GT5U.machines.solarindicator2")).setPos(21, 31))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> noRain, val -> noRain = val))
            .widget(
                new DrawableWidget()
                    .setDrawable(
                        () -> seesSky ? GTUITextures.OVERLAY_BUTTON_CHECKMARK : GTUITextures.OVERLAY_BUTTON_CROSS)
                    .setPos(5, 42)
                    .setSize(16, 16))
            .widget(new TextWidget(StatCollector.translateToLocal("GT5U.machines.solarindicator3")).setPos(21, 47))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> seesSky, val -> seesSky = val));
    }

    @Override
    public void addGregTechLogo(ModularWindow.Builder builder) {
        builder.widget(
            new DrawableWidget().setDrawable(GTUITextures.PICTURE_GT_LOGO_17x17_TRANSPARENT)
                .setSize(17, 17)
                .setPos(154, 5));
    }

    private boolean valid = true;

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            // Check every 5 seconds for world conditions
            if (aTick % 100 == 0) {
                doWorldChecks(aBaseMetaTileEntity.getWorld(), aBaseMetaTileEntity);
            }
            if (aTick % 20 == 0 && valid) {
                aBaseMetaTileEntity.increaseStoredEnergyUnits(maxEUOutput() * 20, false);
            }
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        doWorldChecks(aBaseMetaTileEntity.getWorld(), aBaseMetaTileEntity);
        super.onFirstTick(aBaseMetaTileEntity);
    }

    // Checks are independent for the sake of the ui indicators
    private boolean noRain = false;
    private boolean dayTime = false;
    private boolean seesSky = false;

    private void doWorldChecks(World world, IGregTechTileEntity aBaseMetaTileEntity) {
        noRain = !(world.isRaining() && aBaseMetaTileEntity.getBiome().rainfall > 0.0F);
        dayTime = world.isDaytime();
        seesSky = aBaseMetaTileEntity.getSkyAtSide(ForgeDirection.UP);

        valid = noRain && dayTime && seesSky;
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        IGregTechTileEntity aBase = getBaseMetaTileEntity();
        tag.setBoolean("valid", valid);
        tag.setLong("storedeu", aBase.getStoredEU());
        tag.setLong("maxeu", aBase.getEUCapacity());
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        final NBTTagCompound tag = accessor.getNBTData();
        if (tag.hasKey("valid")) currenttip.add(
            tag.getBoolean("valid")
                ? EnumChatFormatting.GREEN + StatCollector.translateToLocal("GT5U.waila.generating.on")
                : EnumChatFormatting.RED + StatCollector.translateToLocal("GT5U.waila.generating.off"));
        if (tag.hasKey("storedeu") && tag.hasKey("maxeu")) currenttip.add(
            EnumChatFormatting.GREEN + formatNumbers(tag.getLong("storedeu"))
                + EnumChatFormatting.GRAY
                + " / "
                + EnumChatFormatting.YELLOW
                + formatNumbers(tag.getLong("maxeu"))
                + EnumChatFormatting.GRAY
                + " EU");
        super.getWailaBody(itemStack, currenttip, accessor, config);
    }

    @Override
    public long maxEUStore() {
        return V[mTier] * 10000;
    }

    @Override
    public long maxEUOutput() {
        return GTValues.V[mTier];
    }

    @Override
    public boolean isEnetOutput() {
        return true;
    }

    @Override
    public boolean isFacingValid(ForgeDirection side) {
        return side != ForgeDirection.UP;
    }

    @Override
    public boolean isOutputFacing(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }
}

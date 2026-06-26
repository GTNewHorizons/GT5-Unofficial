package gregtech.common.tileentities.generators;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
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

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTETieredMachineBlock;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.hatch.MTESolarGeneratorGui;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

@IMetaTileEntity.SkipGenerateDescription
public class MTESolarGenerator extends MTETieredMachineBlock {

    public MTESolarGenerator(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 4, (String) null);
    }

    @Override
    public String[] getDescription() {
        return GTUtility.translateMultiline("gt.blockmachines.basicgenerator.solarpanel.tooltip");
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

    public boolean isNoRain() {
        return noRain;
    }

    public boolean isDayTime() {
        return dayTime;
    }

    public boolean isSeesSky() {
        return seesSky;
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
            EnumChatFormatting.GREEN + formatNumber(tag.getLong("storedeu"))
                + EnumChatFormatting.GRAY
                + " / "
                + EnumChatFormatting.YELLOW
                + formatNumber(tag.getLong("maxeu"))
                + EnumChatFormatting.GRAY
                + " EU");
        super.getWailaBody(itemStack, currenttip, accessor, config);
    }

    @Override
    public long maxEUStore() {
        if (mTier == 0) {
            return 2;
        }
        return V[mTier] * 10000;
    }

    @Override
    public long maxEUOutput() {
        if (mTier == 0) {
            return 1;
        }
        return GTValues.V[mTier];
    }

    @Override
    public long getMinimumStoredEU() {
        if (mTier == 0) {
            return 0;
        }
        return super.getMinimumStoredEU();
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

    @Override
    public ModularPanel buildUI(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTESolarGeneratorGui(this).build(guiData, syncManager, uiSettings);
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }
}

package tectech.thing.metaTileEntity.hatch;

import static gregtech.api.enums.GTValues.V;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.render.TextureFactory;
import gregtech.common.gui.modularui.hatch.MTEHatchCapacitorGui;
import tectech.Reference;
import tectech.TecTech;
import tectech.loader.ConfigHandler;
import tectech.util.CommonValues;
import tectech.util.TTUtility;

/**
 * Created by Tec on 03.04.2017.
 */
public class MTEHatchCapacitor extends MTEHatch implements IAddUIWidgets {

    private static Textures.BlockIcons.CustomIcon TM_H;
    private static Textures.BlockIcons.CustomIcon TM_H_ACTIVE;
    public static final Map<String, MTEHatchCapacitor.CapacitorComponent> componentBinds = new HashMap<>();

    public MTEHatchCapacitor(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            16,
            new String[] { CommonValues.THETA_MOVEMENT, translateToLocal("gt.blockmachines.hatch.capacitor.desc.0"),
                EnumChatFormatting.AQUA + translateToLocal("gt.blockmachines.hatch.capacitor.desc.1") });
    }

    public MTEHatchCapacitor(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 16, aDescription, aTextures);
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        super.registerIcons(aBlockIconRegister);
        TM_H_ACTIVE = new Textures.BlockIcons.CustomIcon("iconsets/TM_TESLA_CAPS_ACTIVE");
        TM_H = new Textures.BlockIcons.CustomIcon("iconsets/TM_TESLA_CAPS");
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(TM_H_ACTIVE) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(TM_H) };
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchCapacitor(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return true;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return side == aBaseMetaTileEntity.getFrontFacing();
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return side == aBaseMetaTileEntity.getFrontFacing();
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        openGui(aPlayer);
        return true;
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEHatchCapacitorGui(this).build(data, syncManager, uiSettings);
    }

    @Override
    public int getSizeInventory() {
        return getBaseMetaTileEntity().isActive() ? 0 : mInventory.length;
    }

    public long[] getCapacitors() {
        long tier = -1;
        long tCurrent = 0;
        long tEnergyMax = 0;
        for (ItemStack stack : mInventory) {
            if (stack != null && stack.stackSize == 1) {
                CapacitorComponent cap = componentBinds.get(TTUtility.getUniqueIdentifier(stack));
                if (cap != null && cap.tier > tier) {
                    tier = cap.tier;
                }
            }
        }
        if (tier >= 0) {
            for (int i = 0; i < mInventory.length; i++) {
                if (mInventory[i] == null || mInventory[i].stackSize != 1) {
                    continue;
                }
                CapacitorComponent cap = componentBinds.get(TTUtility.getUniqueIdentifier(mInventory[i]));
                if (cap == null) {
                    continue;
                }
                if (cap.tier < tier) {
                    if (getBaseMetaTileEntity().isActive()) {
                        mInventory[i] = null;
                        getBaseMetaTileEntity().setOnFire();
                    }
                } else {
                    tCurrent += cap.current;
                    tEnergyMax += cap.energyMax;
                }
            }
        }
        return new long[] { tier, tCurrent, tEnergyMax };
    }

    public static void run() {
        new MTEHatchCapacitor.CapacitorComponent(Reference.MODID + ":item.tm.teslaCoilCapacitor.0", 0, 1, V[1] * 512); // LV
                                                                                                                       // Capacitor
        new MTEHatchCapacitor.CapacitorComponent(Reference.MODID + ":item.tm.teslaCoilCapacitor.1", 1, 1, V[2] * 512); // MV
                                                                                                                       // Capacitor
        new MTEHatchCapacitor.CapacitorComponent(Reference.MODID + ":item.tm.teslaCoilCapacitor.2", 2, 1, V[3] * 512); // HV
                                                                                                                       // Capacitor
        new MTEHatchCapacitor.CapacitorComponent(Reference.MODID + ":item.tm.teslaCoilCapacitor.3", 3, 1, V[4] * 512); // EV
                                                                                                                       // Capacitor
        new MTEHatchCapacitor.CapacitorComponent(Reference.MODID + ":item.tm.teslaCoilCapacitor.4", 4, 1, V[5] * 512); // IV
                                                                                                                       // Capacitor
        new MTEHatchCapacitor.CapacitorComponent(Reference.MODID + ":item.tm.teslaCoilCapacitor.5", 5, 1, V[6] * 512); // LuV
                                                                                                                       // Capacitor
        new MTEHatchCapacitor.CapacitorComponent(Reference.MODID + ":item.tm.teslaCoilCapacitor.6", 6, 1, V[7] * 512); // ZPM
                                                                                                                       // Capacitor
    }

    public static class CapacitorComponent implements Comparable<MTEHatchCapacitor.CapacitorComponent> {

        private final String unlocalizedName;
        private final long tier, current, energyMax;

        CapacitorComponent(String is, long tier, long current, long energyMax) {
            unlocalizedName = is;
            this.tier = tier;
            this.current = current;
            this.energyMax = energyMax;
            componentBinds.put(unlocalizedName, this);
            if (ConfigHandler.debug.DEBUG_MODE) {
                TecTech.LOGGER.info("Tesla Capacitor registered: " + unlocalizedName);
            }
        }

        @Override
        public int compareTo(CapacitorComponent o) {
            return unlocalizedName.compareTo(o.unlocalizedName);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof CapacitorComponent) {
                return compareTo((CapacitorComponent) obj) == 0;
            }
            return false;
        }
    }
}

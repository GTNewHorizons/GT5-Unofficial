package com.github.technus.tectech.thing.metaTileEntity.hatch;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.Reference;
import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.Util;
import com.github.technus.tectech.thing.metaTileEntity.hatch.gui.GT_Container_Capacitor;
import com.github.technus.tectech.thing.metaTileEntity.hatch.gui.GT_GUIContainer_Capacitor;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.objects.GT_RenderedTexture;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.util.HashMap;
import java.util.Map;

import static com.github.technus.tectech.CommonValues.V;
import static com.github.technus.tectech.Util.getUniqueIdentifier;
import static com.github.technus.tectech.loader.TecTechConfig.DEBUG_MODE;
import static net.minecraft.util.StatCollector.translateToLocal;

/**
 * Created by Tec on 03.04.2017.
 */
public class GT_MetaTileEntity_Hatch_Capacitor extends GT_MetaTileEntity_Hatch {
    private static Textures.BlockIcons.CustomIcon TM_H;
    private static Textures.BlockIcons.CustomIcon TM_H_ACTIVE;
    private static Map<String, GT_MetaTileEntity_Hatch_Capacitor.CapacitorComponent> componentBinds = new HashMap<>();

    public GT_MetaTileEntity_Hatch_Capacitor(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 16, "");
        Util.setTier(aTier,this);
    }

    public GT_MetaTileEntity_Hatch_Capacitor(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
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
        return new ITexture[]{aBaseTexture, new GT_RenderedTexture(TM_H_ACTIVE)};
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[]{aBaseTexture, new GT_RenderedTexture(TM_H)};
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_Capacitor(mName, mTier, mDescription, mTextures);
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return true;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return true;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return aSide == aBaseMetaTileEntity.getFrontFacing();
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return aSide == aBaseMetaTileEntity.getFrontFacing();
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_Capacitor(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_Capacitor(aPlayerInventory, aBaseMetaTileEntity, "Capacitor Hatch");//Capacitor Hatch
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide()) {
            return true;
        }
        aBaseMetaTileEntity.openGUI(aPlayer);
        return true;
    }

    @Override
    public int getSizeInventory() {
        return getBaseMetaTileEntity().isActive() ? 0 : mInventory.length;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.BASS_MARK,
                translateToLocal("gt.blockmachines.hatch.capacitor.desc.0"),//For Tesla Tower
                EnumChatFormatting.AQUA + translateToLocal("gt.blockmachines.hatch.capacitor.desc.1")//Stores 'nergy! (for a while)
        };
    }

    public long[] getCapacitors() {
        long tier = -1;
        long tCurrent = 0;
        long tEnergyMax = 0;
        for (int i = 0; i < mInventory.length; i++) {
            if (mInventory[i] == null || mInventory[i].stackSize != 1) {
                continue;
            }
            CapacitorComponent cap = componentBinds.get(getUniqueIdentifier(mInventory[i]));
            if (cap != null && cap.tier > tier) {
                tier = cap.tier;
            }
        }
        if (tier >= 0) {
            for (int i = 0; i < mInventory.length; i++) {
                if (mInventory[i] == null || mInventory[i].stackSize != 1) {
                    continue;
                }
                CapacitorComponent cap = componentBinds.get(getUniqueIdentifier(mInventory[i]));
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
        return new long[]{tier, tCurrent, tEnergyMax};
    }

    public static void run() {
        new GT_MetaTileEntity_Hatch_Capacitor.CapacitorComponent(Reference.MODID+":item.tm.teslaCoilCapacitor.0", 0, 1, V[1]*512);//LV Capacitor
        new GT_MetaTileEntity_Hatch_Capacitor.CapacitorComponent(Reference.MODID+":item.tm.teslaCoilCapacitor.1", 1, 1, V[2]*512);//MV Capacitor
        new GT_MetaTileEntity_Hatch_Capacitor.CapacitorComponent(Reference.MODID+":item.tm.teslaCoilCapacitor.2", 2, 1, V[3]*512);//HV Capacitor
        new GT_MetaTileEntity_Hatch_Capacitor.CapacitorComponent(Reference.MODID+":item.tm.teslaCoilCapacitor.3", 3, 1, V[4]*512);//EV Capacitor
        new GT_MetaTileEntity_Hatch_Capacitor.CapacitorComponent(Reference.MODID+":item.tm.teslaCoilCapacitor.4", 4, 1, V[5]*512);//IV Capacitor
    }

    public static class CapacitorComponent implements Comparable<GT_MetaTileEntity_Hatch_Capacitor.CapacitorComponent> {
        private final String unlocalizedName;
        private final long tier, current, energyMax;

        CapacitorComponent(ItemStack is, long tier, long current, long energyMax) {
            this(getUniqueIdentifier(is), tier, current, energyMax);
        }

        CapacitorComponent(String is, long tier, long current, long energyMax) {
            unlocalizedName = is;
            this.tier = tier;
            this.current = current;
            this.energyMax = energyMax;
            componentBinds.put(unlocalizedName, this);
            if (DEBUG_MODE) {
                TecTech.LOGGER.info("Tesla Capacitor registered: " + unlocalizedName);
            }
        }

        @Override
        public int compareTo(CapacitorComponent o) {
            return unlocalizedName.compareTo(o.unlocalizedName);
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof CapacitorComponent) {
                return compareTo((CapacitorComponent) obj) == 0;
            }
            return false;
        }
    }
}



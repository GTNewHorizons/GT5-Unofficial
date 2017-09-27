package com.github.technus.tectech.thing.metaTileEntity.hatch;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.thing.metaTileEntity.hatch.gui.GT_Container_Rack;
import com.github.technus.tectech.thing.metaTileEntity.hatch.gui.GT_GUIContainer_Rack;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

import java.util.HashMap;
import java.util.Map;

import static com.github.technus.tectech.CommonValues.MULTI_CHECK_AT;
import static com.github.technus.tectech.Util.getUniqueIdentifier;
import static com.github.technus.tectech.auxiliary.TecTechConfig.DEBUG_MODE;

/**
 * Created by Tec on 03.04.2017.
 */
public class GT_MetaTileEntity_Hatch_Rack extends GT_MetaTileEntity_Hatch {
    private static Textures.BlockIcons.CustomIcon EM_R;
    private static Textures.BlockIcons.CustomIcon EM_R_ACTIVE;
    public int heat = 0;
    private float overClock = 1, overVolt = 1;
    private static Map<String, component> componentBinds = new HashMap<>();

    public GT_MetaTileEntity_Hatch_Rack(int aID, String aName, String aNameRegional, int aTier, String descr) {
        super(aID, aName, aNameRegional, aTier, 4, descr);
    }

    public GT_MetaTileEntity_Hatch_Rack(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 4, aDescription, aTextures);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("eHeat", heat);
        aNBT.setFloat("eOverClock", overClock);
        aNBT.setFloat("eOverVolt", overVolt);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        heat = aNBT.getInteger("eHeat");
        overClock = aNBT.getFloat("eOverClock");
        overVolt = aNBT.getFloat("eOverVolt");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        super.registerIcons(aBlockIconRegister);
        EM_R_ACTIVE = new Textures.BlockIcons.CustomIcon("iconsets/EM_RACK_ACTIVE");
        EM_R = new Textures.BlockIcons.CustomIcon("iconsets/EM_RACK");
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[]{aBaseTexture, new GT_RenderedTexture(EM_R_ACTIVE)};
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[]{aBaseTexture, new GT_RenderedTexture(EM_R)};
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_Rack(mName, mTier, mDescription, mTextures);
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return aFacing >= 2;
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
        if (aBaseMetaTileEntity.isActive() || heat > 500) return false;
        return aSide == aBaseMetaTileEntity.getFrontFacing();
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        if (aBaseMetaTileEntity.isActive() || heat > 500) return false;
        return aSide == aBaseMetaTileEntity.getFrontFacing();
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_Rack(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_Rack(aPlayerInventory, aBaseMetaTileEntity, "Computer Rack");
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide()) return true;
        //if(aBaseMetaTileEntity.isActive())
        //    aPlayer.addChatComponentMessage(new ChatComponentText("It is still active..."));
        //else if(heat>0)
        //    aPlayer.addChatComponentMessage(new ChatComponentText("It is still warm..."));
        //else
        aBaseMetaTileEntity.openGUI(aPlayer);
        return true;
    }

    private int getComputationPower(float overclock, float overvolt, boolean tickingComponents) {
        float computation = 0, heat = 0;
        for (int i = 0; i < mInventory.length; i++) {
            if (mInventory[i] == null || mInventory[i].stackSize != 1) continue;
            component comp = componentBinds.get(getUniqueIdentifier(mInventory[i]));
            if (comp == null) continue;
            if (tickingComponents) {
                if (this.heat > comp.maxHeat) {
                    mInventory[i] = null;
                    continue;
                } else if (comp.subZero || this.heat >= 0) {
                    heat += (1f + (comp.coEff * this.heat / 10000f)) * (comp.heat > 0 ? comp.heat * overclock * overclock * overvolt : comp.heat);
                    //=MAX(0;MIN(MIN($B4;1*C$3+C$3-0,25);1+RAND()+(C$3-1)-($B4-1)/2))
                    if (overvolt * 10f > 7f + TecTech.Rnd.nextFloat())
                        computation += comp.computation * Math.max(0, Math.min(Math.min(overclock, overvolt + overvolt - 0.25), 1 + TecTech.Rnd.nextFloat() + (overvolt - 1) - (overclock - 1) / 2));
                }
            } else {
                computation += comp.computation * overclock;
            }
        }
        if (tickingComponents) {
            this.heat += Math.ceil(heat);
        }
        return (int) Math.floor(computation);
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    public int tickComponents(float oc, float ov) {
        if (oc > 3 + TecTech.Rnd.nextFloat() || ov > 2 + TecTech.Rnd.nextFloat()) getBaseMetaTileEntity().setToFire();
        overClock = oc;
        overVolt = ov;
        return getComputationPower(overClock, overVolt, true);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (aTick % 20 == MULTI_CHECK_AT) {
                if (this.heat > 0) {
                    float heatC = 0;
                    for (int i = 0; i < mInventory.length; i++) {
                        if (mInventory[i] == null || mInventory[i].stackSize != 1) continue;
                        component comp = componentBinds.get(getUniqueIdentifier(mInventory[i]));
                        if (comp == null) continue;
                        if (this.heat > comp.maxHeat) {
                            mInventory[i] = null;
                        } else if (comp.heat < 0) {
                            heatC += comp.heat * (this.heat / 10000);
                        }
                    }
                    this.heat += Math.max(-this.heat, Math.ceil(heatC));
                }

                if (heat > 0) heat -= Math.max(heat / 1000, 1);
                else if (heat < 0) heat -= Math.min(heat / 1000, -1);

                if (heat > 9000) aBaseMetaTileEntity.setOnFire();
                else if (heat > 10000) aBaseMetaTileEntity.setToFire();
                else if (heat < -10000) this.heat = -10000;
            }
        }
    }

    //@Override
    //public void onRemoval() {
    //    if(mInventory!=null && (heat>0 || (getBaseMetaTileEntity()!=null && getBaseMetaTileEntity().isActive())))
    //        for(int i=0;i<mInventory.length;i++)
    //            mInventory[i]=null;
    //}

    @Override
    public int getSizeInventory() {//HACK TO NOT DROP CONTENTS!!!
        return heat > 500 || getBaseMetaTileEntity().isActive() ? 0 : mInventory.length;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.TEC_MARK_EM,
                mDescription,
                EnumChatFormatting.AQUA + "Holds Computer Components"
        };
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        return new String[]{
                "Base computation: " + EnumChatFormatting.AQUA + getComputationPower(1, 0, false),
                "After overclocking: " + EnumChatFormatting.AQUA + getComputationPower(overClock, 0, false),
                "Heat Accumulated: " + EnumChatFormatting.RED + ((heat + 99) / 100) + EnumChatFormatting.RESET + " %"};
        //heat==0? --> ((heat+9)/10) = 0
        //Heat==1-10? -->  1
    }

    public static void run() {//20k heat cap max!
        new component(ItemList.Circuit_Primitive.get(1), 1, 4, 0, 500, true);
        new component(ItemList.Circuit_Basic.get(1), 4, 8, 0, 1000, true);
        new component(ItemList.Circuit_Good.get(1), 6, 9, -.05f, 1500, true);
        new component(ItemList.Circuit_Parts_Advanced.get(1), 1, 2, -.05f, 2000, true);
        new component(ItemList.Circuit_Advanced.get(1), 8, 10, -.1f, 2500, true);
        new component(ItemList.Circuit_Data.get(1), 1, 1, -.1f, 3000, true);
        new component(ItemList.Circuit_Master.get(1), 12, 10, -.2F, 5000, true);
        new component(ItemList.Circuit_Elite.get(1), 16, 12, -.15F, 3500, true);

        new component("IC2:ic2.reactorVent", 0, -1, 10f, 1000, false);
        new component("IC2:ic2.reactorVentCore", 0, -1, 20f, 2500, false);
        new component("IC2:ic2.reactorVentGold", 0, -1, 40f, 5000, false);
        new component("IC2:ic2.reactorVentDiamond", 0, -1, 80f, 10000, false);//2x oc

        if (Loader.isModLoaded("dreamcraft")) {
            new component("dreamcraft:item.HighEnergyCircuitParts", 3, 2, -.1f, 9001, true);
            new component("dreamcraft:item.HighEnergyFlowCircuit", 24, 16, -.25f, 10000, true);
            new component("dreamcraft:item.NanoCircuit", 32, 20, -.15f, 8000, true);
            new component("dreamcraft:item.PikoCircuit", 64, 32, -.2f, 8500, true);
            new component("dreamcraft:item.QuantumCircuit", 128, 48, -.3f, 9000, true);
        }
        if (Loader.isModLoaded("OpenComputers")) {
            new component("OpenComputers:item.23", 0, 1, 0f, 100, true);//Transistor
            new component("OpenComputers:item.24", 7, 12, -.05f, 1500, true);//chip t1
            new component("OpenComputers:item.25", 18, 20, -.1f, 3000, true);//chip t2
            new component("OpenComputers:item.26", 25, 22, -.15f, 4500, true);//chip t3
            new component("OpenComputers:item.27", 10, 15, -.05f, 3000, true);//alu
            new component("OpenComputers:item.28", 25, 18, -.05f, 1500, true);//cu

            new component("OpenComputers:item.70", 42, 30, -.05f, 1500, true);//bus t1
            new component("OpenComputers:item.71", 70, 50, -.1f, 3000, true);//bus t2
            new component("OpenComputers:item.72", 105, 72, -.15f, 4500, true);//bus t3

            new component("OpenComputers:item.29", 106, 73, -.1f, 1500, true);//cpu t1
            new component("OpenComputers:item.42", 226, 153, -.15f, 3000, true);//cpu t2
            new component("OpenComputers:item.43", 374, 241, -.2f, 4500, true);//cpu t3

            new component("OpenComputers:item.8", 20, 27, -.1f, 1500, true);//gpu t1
            new component("OpenComputers:item.9", 62, 67, -.2f, 3000, true);//gpu t2
            new component("OpenComputers:item.10", 130, 111, -.3f, 4500, true);//gpu t3

            new component("OpenComputers:item.101", 350, 234, -.1f, 1500, true);//apu t1
            new component("OpenComputers:item.102", 606, 398, -.2f, 4500, true);//apu t2
            new component("OpenComputers:item.103", 1590, 1006, -.3f, 9000, true);//apu tC
        }
    }

    public static class component implements Comparable<component> {
        private final String unlocalizedName;
        private final float heat, coEff, computation, maxHeat;
        private final boolean subZero;

        component(ItemStack is, float computation, float heat, float coEff, float maxHeat, boolean subZero) {
            this(getUniqueIdentifier(is), computation, heat, coEff, maxHeat, subZero);
        }

        component(String is, float computation, float heat, float coEff, float maxHeat, boolean subZero) {
            unlocalizedName = is;
            this.heat = heat;
            this.coEff = coEff;
            this.computation = computation;
            this.maxHeat = maxHeat;
            this.subZero = subZero;
            componentBinds.put(unlocalizedName, this);
            if (DEBUG_MODE)
                TecTech.Logger.info("Component registered: " + unlocalizedName);
        }

        @Override
        public int compareTo(component o) {
            return unlocalizedName.compareTo(o.unlocalizedName);
        }
    }
}



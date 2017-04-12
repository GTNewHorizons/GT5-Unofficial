package com.github.technus.tectech.thing.metaTileEntity.hatch;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.thing.machineTT;
import com.github.technus.tectech.thing.metaTileEntity.hatch.gui.GT_Container_Rack;
import com.github.technus.tectech.thing.metaTileEntity.hatch.gui.GT_GUIContainer_Rack;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_Container_2by2;
import gregtech.api.gui.GT_GUIContainer_2by2;
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
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.TreeMap;

import static com.github.technus.tectech.CommonValues.multiCheckAt;

/**
 * Created by Tec on 03.04.2017.
 */
public class GT_MetaTileEntity_Hatch_Rack extends GT_MetaTileEntity_Hatch implements machineTT {
    private static Textures.BlockIcons.CustomIcon EM_R;
    private static Textures.BlockIcons.CustomIcon EM_R_ACTIVE;
    public int heat=0;
    private float overClock =1, overVolt =1;
    private static TreeMap<String,component> componentBinds=new TreeMap<>();

    public GT_MetaTileEntity_Hatch_Rack(int aID, String aName, String aNameRegional, int aTier, String descr) {
        super(aID, aName, aNameRegional, aTier, 4, descr);
    }

    public GT_MetaTileEntity_Hatch_Rack(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 4, aDescription, aTextures);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("eHeat",heat);
        aNBT.setFloat("eOverClock", overClock);
        aNBT.setFloat("eOverVolt", overVolt);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        heat=aNBT.getInteger("eHeat");
        overClock =aNBT.getFloat("eOverClock");
        overVolt =aNBT.getFloat("eOverVolt");
    }

    @Override
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
        return new GT_MetaTileEntity_Hatch_Rack(mName,mTier,mDescription,mTextures);
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return aFacing>=2;
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
        if(aBaseMetaTileEntity.isActive() || heat>0) return false;
        return aSide == aBaseMetaTileEntity.getFrontFacing();
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        if(aBaseMetaTileEntity.isActive() || heat>0) return false;
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

    private int getComputationPower(float overclock, float overvolt, boolean tickingComponents){
        float computation=0,heat=0;
        for(int i=0;i<mInventory.length;i++){
            if(mInventory[i]==null || mInventory[i].stackSize!=1) continue;
            component comp=componentBinds.get(mInventory[i].getUnlocalizedName());
            if(comp==null) continue;
            if(tickingComponents) {
                if (this.heat > comp.maxHeat) {
                    mInventory[i] = null;
                    continue;
                } else if (comp.subZero || this.heat > 0)
                    heat += comp.heat > 0 ? comp.heat * overclock * overclock * overvolt: comp.heat;
                //=MAX(0;MIN(MIN($B4;1*C$3+C$3-0,25);1+RAND()+(C$3-1)-($B4-1)/2))
                if(overvolt*10f>7f+TecTech.Rnd.nextFloat())
                    computation+=comp.computation*Math.max(0,Math.min(Math.min(overclock,overvolt+overvolt-0.25),1+TecTech.Rnd.nextFloat()+(overvolt-1)-(overclock-1)/2));
            }else{
                computation+=comp.computation*overclock;
            }
        }
        if(tickingComponents) {
            this.heat+=Math.ceil(heat);
        }
        return (int)Math.floor(computation);
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    public int tickComponents(float oc,float ov) {
        if(oc>3+TecTech.Rnd.nextFloat() || ov>2+TecTech.Rnd.nextFloat()) getBaseMetaTileEntity().setToFire();
        overClock =oc; overVolt =ov;
        return getComputationPower(overClock, overVolt,true);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if(aBaseMetaTileEntity.isServerSide()){
            if(aTick%20==multiCheckAt){
                if(heat>0)heat-=Math.max(heat/1000,1);
                else if(heat<0)heat-=Math.min(heat/1000,-1);

                if(heat>9000) aBaseMetaTileEntity.setOnFire();
                else if(heat>10000) aBaseMetaTileEntity.setToFire();
                else if(heat<-20000)this.heat=-20000;
            }
        }
    }

    @Override
    public void onRemoval() {
        if(mInventory!=null && (heat>0 || (getBaseMetaTileEntity()!=null && getBaseMetaTileEntity().isActive())))
            for(int i=0;i<mInventory.length;i++)
                mInventory[i]=null;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.tecMark,
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
                "Base computation: "+ EnumChatFormatting.AQUA +getComputationPower(1,0,false),
                "After overclocking: "+ EnumChatFormatting.AQUA +getComputationPower(overClock,0,false),
                "Heat Accumulated: "+ EnumChatFormatting.RED + ((heat+99)/100) +EnumChatFormatting.RESET+" %"};
                                                                //heat==0? --> ((heat+9)/10) = 0
                                                                //Heat==1-10? -->  1
    }

    public static void run(){
        new component(ItemList.Circuit_Elite.get(1),4,32,5000,true);
        new component(ItemList.Circuit_Advanced.get(1),1,2,2000,true);
        new component(ItemList.Circuit_Basic.get(1),2,1,1000,true);
    }

    public static class component implements Comparable<component>{
        private final String unlocalizedName;
        private final float heat,computation,maxHeat;
        private final boolean subZero;

        component(ItemStack is,float heat,float computation,float maxHeat, boolean subZero){
            unlocalizedName=is.getUnlocalizedName();
            this.heat=heat;
            this.computation=computation;
            this.maxHeat=maxHeat;
            this.subZero=subZero;
            componentBinds.put(unlocalizedName,this);
            if(TecTech.ModConfig.DEBUG_MODE) TecTech.Logger.info("Component registered: "+unlocalizedName);
        }

        @Override
        public int compareTo(component o) {
            return unlocalizedName.compareTo(o.unlocalizedName);
        }
    }
}



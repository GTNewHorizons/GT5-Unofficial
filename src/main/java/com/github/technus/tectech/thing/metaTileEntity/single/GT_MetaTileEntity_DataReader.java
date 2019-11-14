package com.github.technus.tectech.thing.metaTileEntity.single;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.Util;
import com.github.technus.tectech.thing.metaTileEntity.single.gui.GT_Container_DataReader;
import com.github.technus.tectech.thing.metaTileEntity.single.gui.GT_GUIContainer_DataReader;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_Slot_Holo;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Utility;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.github.technus.tectech.CommonValues.V;
import static com.github.technus.tectech.Reference.MODID;
import static com.github.technus.tectech.recipe.TT_recipeAdder.nullItem;
import static net.minecraft.util.StatCollector.translateToLocal;

/**
 * Created by Tec on 23.03.2017.
 */
public class GT_MetaTileEntity_DataReader extends GT_MetaTileEntity_BasicMachine {
    private static final HashMap<Util.ItemStack_NoNBT,ArrayList<IDataRender>> RENDER_REGISTRY =new HashMap<>();
    public static GT_RenderedTexture READER_ONLINE, READER_OFFLINE;

    public GT_MetaTileEntity_DataReader(int aID, String aName, String aNameRegional, int aTier) {
        super(aID,aName,aNameRegional,aTier,1,"",1,1,"dataReader.png","");
        Util.setTier(aTier,this);
    }

    public GT_MetaTileEntity_DataReader(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName,aTier,1,aDescription,aTextures,1,1,"dataReader.png","");
        Util.setTier(aTier,this);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_DataReader(mName, mTier, mDescription, mTextures);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        super.registerIcons(aBlockIconRegister);
        READER_ONLINE = new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("iconsets/READER_ONLINE"));
        READER_OFFLINE = new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("iconsets/READER_OFFLINE"));
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if(aBaseMetaTileEntity.getWorld()==null){
            if(aSide==aFacing){
                return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][aColorIndex + 1], aActive ? READER_ONLINE : READER_OFFLINE};
            }
            return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][aColorIndex + 1]};
        }
        if(aSide==mMainFacing){
            return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][aColorIndex + 1], aActive ? READER_ONLINE : READER_OFFLINE};
        }else if(aSide==aFacing){
            return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][aColorIndex + 1], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_PIPE_OUT)};
        }
        return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][aColorIndex + 1]};
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        return null;
    }

    @Override
    public int checkRecipe() {
        if(getOutputAt(0)!=null){
            return DID_NOT_FIND_RECIPE;
        }
        ItemStack input=getInputAt(0);
        ArrayList<IDataRender> renders=getRenders(new Util.ItemStack_NoNBT(input));
        for(IDataRender render:renders){
            if(render.canRender(input,mTier)){
                mOutputItems[0]=input.copy();
                input.stackSize-=1;
                calculateOverclockedNess(render.getReadingEUt(),render.getReadingTime());
                if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
                    return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
                return FOUND_AND_SUCCESSFULLY_USED_RECIPE;
            }
        }
        return DID_NOT_FIND_RECIPE;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        aBaseMetaTileEntity.setActive(getOutputAt(0)!=null || mMaxProgresstime>0);
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_DataReader(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_DataReader(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), mGUIName, GT_Utility.isStringValid(mNEIName) ? mNEIName : getRecipeList() != null ? getRecipeList().mUnlocalizedName : "");
    }

    @Override
    public boolean isSimpleMachine() {
        return false;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.TEC_MARK_GENERAL,
                translateToLocal("gt.blockmachines.machine.tt.datareader.desc.0"),//Reads Data Sticks and Orbs
                EnumChatFormatting.BLUE + translateToLocal("gt.blockmachines.machine.tt.datareader.desc.1"),//Power it up and
                EnumChatFormatting.BLUE + translateToLocal("gt.blockmachines.machine.tt.datareader.desc.2")//Put the data storage in
        };
    }

    @Override
    public boolean isElectric() {
        return true;
    }

    @Override
    public boolean isEnetInput() {
        return true;
    }

    @Override
    public boolean isInputFacing(byte aSide) {
        return aSide!=getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean isOutputFacing(byte aSide) {
        return aSide!=getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public long maxEUInput() {
        return V[mTier];
    }

    @Override
    public long maxEUStore() {
        return maxEUInput()*16L;
    }

    @Override
    public long getMinimumStoredEU() {
        return maxEUInput()*4L;
    }

    public static void addDataRender(Util.ItemStack_NoNBT stack, IDataRender render){
        ArrayList<IDataRender> renders = RENDER_REGISTRY.computeIfAbsent(stack, k -> new ArrayList<>());
        if(FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            render.loadResources();
        }
        renders.add(render);
    }

    public static ArrayList<IDataRender> getRenders(Util.ItemStack_NoNBT stack){
        return RENDER_REGISTRY.get(stack);
    }

    public interface IDataRender {
        @SideOnly(Side.CLIENT)
        void loadResources();
        @SideOnly(Side.CLIENT)
        void initRender(ItemStack itemStack);
        @SideOnly(Side.CLIENT)
        void renderTooltips(ItemStack itemStack,int mouseX,int mouseY,GT_GUIContainer_DataReader gui);
        @SideOnly(Side.CLIENT)
        void renderForeground(ItemStack itemStack,int mouseX,int mouseY,GT_GUIContainer_DataReader gui, FontRenderer font);
        @SideOnly(Side.CLIENT)
        void renderBackgroundOverlay(ItemStack itemStack, int mouseX, int mouseY, int X, int Y, GT_GUIContainer_DataReader gui);
        boolean canRender(ItemStack itemStack, byte tier);
        int getReadingEUt();
        int getReadingTime();
    }

    public static void run(){
        addDataRender(new Util.ItemStack_NoNBT(ItemList.Tool_DataStick.get(1)),new IDataRender() {
            @SideOnly(Side.CLIENT)
            private ResourceLocation bg;
            @SideOnly(Side.CLIENT)
            private HashMap<GT_Slot_Holo,ItemStack> slots;
            private HashMap<GT_Slot_Holo,ItemStack[]> slots2;

            @Override
            @SideOnly(Side.CLIENT)
            public void loadResources() {
                bg =new ResourceLocation(MODID+":textures/gui/assLineRender.png");
            }

            @Override
            public void initRender(ItemStack itemStack) {
                slots=new HashMap<>();
                slots2=new HashMap<>();

                slots.put(new GT_Slot_Holo(null,0,143,55,false,false,1),ItemList.Tool_DataStick.getWithName(1,"Research data"));
                ItemStack output=ItemStack.loadItemStackFromNBT(itemStack.stackTagCompound.getCompoundTag("output"));
                if(output!=null){
                    slots.put(new GT_Slot_Holo(null,0,143,19,false,false,64),output);
                }

                for (int i = 0; i < 16; i++) {
                    ArrayList<ItemStack> array=new ArrayList<>();
                    ItemStack input=ItemStack.loadItemStackFromNBT(itemStack.stackTagCompound.getCompoundTag(Integer.toString(i)));
                    if(input!=null){
                        array.add(input);
                    }
                    for (int k = 0; k < itemStack.stackTagCompound.getInteger("a"+i); k++) {
                        input=ItemStack.loadItemStackFromNBT(itemStack.stackTagCompound.getCompoundTag("a" + i + ":" + k));
                        if(input!=null){
                            array.add(input);
                        }
                    }
                    if(array.size()>0){
                        slots2.put(new GT_Slot_Holo(null,0,17+(i&0x3)*18,19+(i>>2)*18,false,false,64),
                                array.toArray(nullItem));
                    }
                }
                for (int i = 0; i < 4; i++) {
                    FluidStack fs=FluidStack.loadFluidStackFromNBT(itemStack.stackTagCompound.getCompoundTag("f"+i));
                    if(fs!=null){
                        slots.put(new GT_Slot_Holo(null,0,107,19+i*18,false,false,1),
                                GT_Utility.getFluidDisplayStack(fs, true));
                    }
                }
            }

            @Override
            public void renderTooltips(ItemStack itemStack, int mouseX, int mouseY, GT_GUIContainer_DataReader gui) {
                for(Map.Entry<GT_Slot_Holo,ItemStack> entry:slots.entrySet()){
                    gui.renderTooltipSimple(mouseX, mouseY, entry.getKey(),entry.getValue());
                }
                int time=(int)(System.currentTimeMillis()/2000);
                for(Map.Entry<GT_Slot_Holo,ItemStack[]> entry:slots2.entrySet()){
                    gui.renderTooltipSimple(mouseX, mouseY, entry.getKey(),entry.getValue()[time%entry.getValue().length]);
                }
            }

            @Override
            @SideOnly(Side.CLIENT)
            public void renderForeground(ItemStack itemStack, int mouseX, int mouseY, GT_GUIContainer_DataReader gui, FontRenderer font) {
                int time=itemStack.stackTagCompound.getInteger("time");
                int EUt=itemStack.stackTagCompound.getInteger("eu");
                font.drawString("Assembly Line Recipe", 7, 8, 0x80a0ff);
                font.drawString(GT_Utility.trans("152","Total: ") + ((long)time * EUt) + " EU",7,93, 0x80a0ff);
                font.drawString(GT_Utility.trans("153","Usage: ") + EUt + " EU/t",7,103, 0x80a0ff);
                font.drawString(GT_Utility.trans("154","Voltage: ") + EUt + " EU",7,113, 0x80a0ff);
                font.drawString(GT_Utility.trans("155","Amperage: ") + 1 ,7,123, 0x80a0ff);
                font.drawString( GT_Utility.trans("158","Time: ")+String.format("%.2f " + GT_Utility.trans("161"," secs"), 0.05F * time), 7,133, 0x80a0ff);

                for(Map.Entry<GT_Slot_Holo,ItemStack> entry:slots.entrySet()){
                    gui.renderItemSimple(entry.getKey(),entry.getValue());
                }
                time=(int)(System.currentTimeMillis()/2000);
                for(Map.Entry<GT_Slot_Holo,ItemStack[]> entry:slots2.entrySet()){
                    gui.renderItemSimple(entry.getKey(),entry.getValue()[time%entry.getValue().length]);
                }
            }

            @Override
            @SideOnly(Side.CLIENT)
            public void renderBackgroundOverlay(ItemStack itemStack, int mouseX, int mouseY, int X, int Y, GT_GUIContainer_DataReader gui) {
                //176/83
                gui.mc.getTextureManager().bindTexture(bg);
                gui.drawTexturedModalRect(X,Y,0,0,176, 151);
            }

            @Override
            public boolean canRender(ItemStack itemStack,byte tier) {
                NBTTagCompound nbtTagCompound=itemStack.stackTagCompound;
                return nbtTagCompound != null && nbtTagCompound.hasKey("output");
            }

            @Override
            public int getReadingEUt() {
                return (int)V[4];
            }

            @Override
            public int getReadingTime() {
                return 128;
            }
        });
    }
}

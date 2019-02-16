package com.github.technus.tectech.thing.item;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.thing.metaTileEntity.IConstructable;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_ElectricBlastFurnace;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

import java.util.HashMap;
import java.util.List;

import static com.github.technus.tectech.Reference.MODID;
import static com.github.technus.tectech.Util.StructureBuilder;
import static gregtech.api.GregTech_API.sBlockCasings1;

/**
 * Created by Tec on 15.03.2017.
 */
public final class ConstructableTriggerItem extends Item {
    public static ConstructableTriggerItem INSTANCE;

    private static HashMap<String,MultiblockInfoContainer> multiblockMap= new HashMap<>();

    private ConstructableTriggerItem() {
        setUnlocalizedName("em.constructable");
        setTextureName(MODID + ":itemConstructable");
    }

    @Override
    public boolean onItemUseFirst(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int aSide, float hitX, float hitY, float hitZ) {
        TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if(tTileEntity==null || aPlayer instanceof FakePlayer) {
            return aPlayer instanceof EntityPlayerMP;
        }
        if (aPlayer instanceof EntityPlayerMP) {
            //struct gen
            if (aPlayer.isSneaking() && aPlayer.capabilities.isCreativeMode) {
                if (tTileEntity instanceof IGregTechTileEntity) {
                    IMetaTileEntity metaTE = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity();
                    if (metaTE instanceof IConstructable) {
                        ((IConstructable) metaTE).construct(aStack.stackSize, false);
                    } else if (multiblockMap.containsKey(metaTE.getClass().getCanonicalName())) {
                        multiblockMap.get(metaTE.getClass().getCanonicalName()).construct(aStack.stackSize, false, tTileEntity, ((IGregTechTileEntity) tTileEntity).getFrontFacing());
                    }
                } else if (tTileEntity instanceof IConstructable) {
                    ((IConstructable) tTileEntity).construct(aStack.stackSize, false);
                } else if (multiblockMap.containsKey(tTileEntity.getClass().getCanonicalName())) {
                    multiblockMap.get(tTileEntity.getClass().getCanonicalName()).construct(aStack.stackSize, false, tTileEntity, aSide);
                }
            }
            return true;
        }else if (aPlayer instanceof EntityClientPlayerMP){//particles and text client side
            //if ((!aPlayer.isSneaking() || !aPlayer.capabilities.isCreativeMode)) {
                if(tTileEntity instanceof IGregTechTileEntity) {
                    IMetaTileEntity metaTE = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity();
                    if (metaTE instanceof IConstructable) {
                        ((IConstructable) metaTE).construct(aStack.stackSize, true);
                        TecTech.proxy.printInchat(((IConstructable) metaTE).getStructureDescription(aStack.stackSize));
                        return false;
                    } else if(multiblockMap.containsKey(metaTE.getClass().getCanonicalName())){
                        multiblockMap.get(metaTE.getClass().getCanonicalName()).construct(aStack.stackSize,true,tTileEntity,((IGregTechTileEntity) tTileEntity).getFrontFacing());
                        TecTech.proxy.printInchat(multiblockMap.get(metaTE.getClass().getCanonicalName()).getDescription(aStack.stackSize));
                        return false;
                    }
                } else if(tTileEntity instanceof IConstructable){
                    ((IConstructable) tTileEntity).construct(aStack.stackSize,true);
                    TecTech.proxy.printInchat(((IConstructable) tTileEntity).getStructureDescription(aStack.stackSize));
                    return false;
                } else if(multiblockMap.containsKey(tTileEntity.getClass().getCanonicalName())){
                    multiblockMap.get(tTileEntity.getClass().getCanonicalName()).construct(aStack.stackSize,true,tTileEntity, aSide);
                    TecTech.proxy.printInchat(multiblockMap.get(tTileEntity.getClass().getCanonicalName()).getDescription(aStack.stackSize));
                    return false;
                }
            //} else {
            //    if(tTileEntity instanceof IGregTechTileEntity) {
            //        IMetaTileEntity metaTE = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity();
            //        if (metaTE instanceof IConstructable) {
            //            TecTech.proxy.printInchat(((IConstructable) metaTE).getStructureDescription(aStack.stackSize));
            //            return false;
            //        } else if(multiblockMap.containsKey(metaTE.getClass().getCanonicalName())){
            //            TecTech.proxy.printInchat(multiblockMap.get(metaTE.getClass().getCanonicalName()).getDescription(aStack.stackSize));
            //            return false;
            //        }
            //    } else if(tTileEntity instanceof IConstructable){
            //        TecTech.proxy.printInchat(((IConstructable) tTileEntity).getStructureDescription(aStack.stackSize));
            //        return false;
            //    } else if(multiblockMap.containsKey(tTileEntity.getClass().getCanonicalName())){
            //        TecTech.proxy.printInchat(multiblockMap.get(tTileEntity.getClass().getCanonicalName()).getDescription(aStack.stackSize));
            //        return false;
            //    }
            //}
        }
        return false;
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer ep, List aList, boolean boo) {
        aList.add(CommonValues.TEC_MARK_GENERAL);
        aList.add("Triggers Constructable Interface");
        aList.add(EnumChatFormatting.BLUE + "Shows multiblock construction details,");
        aList.add(EnumChatFormatting.BLUE + "just Use on a multiblock controller.");
        aList.add(EnumChatFormatting.BLUE + "(Sneak Use in creative to build)");
        aList.add(EnumChatFormatting.BLUE + "Quantity affects tier/mode/type");
    }

    public static void run() {
        INSTANCE = new ConstructableTriggerItem();
        GameRegistry.registerItem(INSTANCE, INSTANCE.getUnlocalizedName());

        registerMetaClass(GT_MetaTileEntity_ElectricBlastFurnace.class, new MultiblockInfoContainer() {
            //region Structure
            private final String[][] shape = new String[][]{
                    {"000","\"\"\"","\"\"\""," . ",},
                    {"0!0","\"A\"","\"A\"","   ",},
                    {"000","\"\"\"","\"\"\"","   ",},
            };
            private final Block[] blockType = new Block[]{sBlockCasings1};
            private final byte[] blockMeta = new byte[]{11};
            private final String[] desc=new String[]{
                    EnumChatFormatting.AQUA+"Hint Details:",
                    "1 - Classic Hatches or Heat Proof Casing",
                    "2 - Muffler ParameterGroup",
                    "3 - Coil blocks"
            };
            //endregion

            @Override
            public void construct(int stackSize, boolean hintsOnly, TileEntity tileEntity, int aSide) {
                StructureBuilder(shape, blockType, blockMeta, 1, 3, 0, tileEntity, aSide, hintsOnly);
            }

            @Override
            public String[] getDescription(int stackSize) {
                return desc;
            }
        });
    }

    public interface MultiblockInfoContainer {
        void construct(int stackSize, boolean hintsOnly, TileEntity tileEntity, int aSide);
        @SideOnly(Side.CLIENT)
        String[] getDescription(int stackSize);
    }

    public static void registerTileClass(Class<? extends TileEntity> clazz,MultiblockInfoContainer info){
        multiblockMap.put(clazz.getCanonicalName(),info);
    }

    public static void registerMetaClass(Class<? extends IMetaTileEntity> clazz,MultiblockInfoContainer info){
        multiblockMap.put(clazz.getCanonicalName(),info);
    }
}

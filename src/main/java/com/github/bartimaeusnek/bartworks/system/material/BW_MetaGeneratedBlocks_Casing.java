/*
 * Copyright (c) 2018-2020 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.system.material;

import com.github.technus.tectech.mechanics.structure.ICustomBlockSetting;
import gregtech.api.GregTech_API;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_LanguageManager;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

@cpw.mods.fml.common.Optional.Interface(modid = "tectech", striprefs = true, iface = "com.github.technus.tectech.mechanics.structure.ICustomBlockSetting")
public class BW_MetaGeneratedBlocks_Casing extends BW_MetaGenerated_Blocks implements ICustomBlockSetting {

    public BW_MetaGeneratedBlocks_Casing(Material p_i45386_1_, Class<? extends TileEntity> tileEntity, String blockName, OrePrefixes prefixes) {
        super(p_i45386_1_, tileEntity, blockName, prefixes);
    }

    @Override
    public String getHarvestTool(int aMeta) {
        return "wrench";
    }

    @Override
    public int getHarvestLevel(int aMeta) {
        return 2;
    }

    @Override
    public float getBlockHardness(World aWorld, int aX, int aY, int aZ) {
        return Blocks.iron_block.getBlockHardness(aWorld, aX, aY, aZ);
    }

    @Override
    public float getExplosionResistance(Entity aTNT) {
        return Blocks.iron_block.getExplosionResistance(aTNT);
    }

    @Override
    protected boolean canSilkHarvest() {
        return false;
    }

    @Override
    public void breakBlock(World aWorld, int aX, int aY, int aZ, Block aBlock, int aMetaData) {
        GregTech_API.causeMachineUpdate(aWorld, aX, aY, aZ);
    }

    @Override
    public void onBlockAdded(World aWorld, int aX, int aY, int aZ) {
        GregTech_API.causeMachineUpdate(aWorld, aX, aY, aZ);
    }

    @Override
    protected void doRegistrationStuff(Werkstoff tMaterial) {
        GregTech_API.registerMachineBlock(this, -1);
        Optional.ofNullable(tMaterial)
                .filter(pMaterial -> pMaterial.hasItemType(OrePrefixes.plate) && pMaterial.hasItemType(OrePrefixes.screw) && pMaterial.hasItemType(OrePrefixes.plateDouble) )
                .ifPresent(pMaterial ->
                            GT_LanguageManager.addStringLocalization(
                                    this.getUnlocalizedName() + "." + pMaterial.getmID() + ".name",
                                    _prefixes.mLocalizedMaterialPre + pMaterial.getDefaultName() + _prefixes.mLocalizedMaterialPost
                            )
                );
    }

    @Override
    public String getUnlocalizedName() {
        if (_prefixes == WerkstoffLoader.blockCasing)
            return "bw.werkstoffblockscasing.01";
        else if (_prefixes == WerkstoffLoader.blockCasingAdvanced)
            return "bw.werkstoffblockscasingadvanced.01";
        return "";
    }

    @Override
    @SuppressWarnings("unchecked")
    public void getSubBlocks(Item aItem, CreativeTabs aTab, List aList) {
        Werkstoff.werkstoffHashSet.stream()
                .filter(pMaterial ->
                           pMaterial.hasItemType(OrePrefixes.plate)
                        && pMaterial.hasItemType(OrePrefixes.screw)
                        && pMaterial.hasItemType(OrePrefixes.plateDouble)
                        )
                .map(pMaterial -> new ItemStack(aItem, 1, pMaterial.getmID()))
                .forEach(aList::add);
    }

    /**
     * DEBUG Method for TT-Blueprints!
     */
    @cpw.mods.fml.common.Optional.Method(modid = "tectech")
    public void setBlock(World world, int x, int y, int z, int meta) {
        world.setBlock(x, y, z,this, meta,2);
        try {
            Thread.sleep(1);
            //Fucking Minecraft TE settings.
        } catch (InterruptedException ignored) {}
        Optional.ofNullable(world.getTileEntity(x,y,z))
                .filter(te -> te instanceof BW_MetaGeneratedBlocks_Casing_TE)
                .map(te -> (BW_MetaGeneratedBlocks_Casing_TE) te)
                .ifPresent(te -> te.mMetaData = (short) meta);
    }
}

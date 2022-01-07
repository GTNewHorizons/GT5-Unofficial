package gtPlusPlus.core.item.tool.misc;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import gregtech.api.enums.Materials;

import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.base.CoreItem;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;

public class FakeGregtechTool extends CoreItem{

	public final int componentColour;
	public Object extraData;

	protected IIcon base[] = new IIcon[6];
	protected IIcon overlay[] = new IIcon[6];

	public FakeGregtechTool() {
		super("GregeriousT's Display Tool", AddToCreativeTab.tabTools, 1);
		short[] tempCol = Materials.TungstenSteel.getRGBA();
		this.componentColour = Utils.rgbtoHexValue(tempCol[0], tempCol[1], tempCol[2]);
	}

	@Override
	public void registerIcons(final IIconRegister i) {		
			//ScrewDriver
			this.base[0] = i.registerIcon("gregtech" + ":" + "materialicons/METALLIC/" + "toolHeadScrewdriver");
			this.overlay[0] = i.registerIcon("gregtech" + ":" + "iconsets/" + "HANDLE_SCREWDRIVER");			
			//Soldering iron
			this.base[1] = i.registerIcon("gregtech" + ":" + "materialicons/METALLIC/" + "toolHeadSoldering");
			this.overlay[1] = i.registerIcon("gregtech" + ":" + "iconsets/" + "HANDLE_SOLDERING");			
			//Mallet
			this.base[2] = i.registerIcon("gregtech" + ":" + "materialicons/METALLIC/" + "handleMallet");
			this.overlay[2] = i.registerIcon("gregtech" + ":" + "materialicons/METALLIC/" + "toolHeadMallet");			
			//Hammer
			this.base[3] = i.registerIcon("gregtech" + ":" + "materialicons/METALLIC/" + "stick");
			this.overlay[3] = i.registerIcon("gregtech" + ":" + "materialicons/METALLIC/" + "toolHeadHammer");			
			//Wrench
			this.base[4] = i.registerIcon("gregtech" + ":" + "iconsets/" + "WRENCH");
			this.overlay[4] = i.registerIcon("gregtech" + ":" + "iconsets/" + "WRENCH_OVERLAY");
			//Crowbar
			this.base[5] = i.registerIcon("gregtech" + ":" + "iconsets/" + "CROWBAR");
			this.overlay[5] = i.registerIcon("gregtech" + ":" + "iconsets/" + "CROWBAR_OVERLAY");	
	}

	@Override
	public int getColorFromItemStack(final ItemStack stack, final int renderPass) {
		if (renderPass == 1){
			return Utils.rgbtoHexValue(230, 230, 230);
		}
		return this.componentColour;
	}

	@Override
	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for (int i = 0; i < 6; i ++) {
			list.add(new ItemStack(item, 1, i));
		}
	}

}

package gtPlusPlus.xmod.reliquary.item;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import xreliquary.Reliquary;

public class ReliquaryItems {

	private static Class<?> CLASS_MAIN = ReflectionUtils.getClass("xreliquary.Reliquary");
	private static Field FIELD_CONTENT = ReflectionUtils.getField(CLASS_MAIN, "CONTENT");
	private static Object OBJECT_CONTENT = ReflectionUtils.getFieldValue(FIELD_CONTENT);
	private static Method METHOD_GETITEM = ReflectionUtils.getMethod(OBJECT_CONTENT, "getItem", new Class[] {String.class});

	public static Item getItem(String name) {		
		try {
			return (Item) METHOD_GETITEM.invoke(OBJECT_CONTENT, name);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
		}
		return null;
	}

	public static ItemStack emptyVoidTear() {
		return new ItemStack(getItem("void_tear_empty"), 1, 0);
	}
	
	public static ItemStack glowingWater() {
		return new ItemStack(getItem("glowing_water"));
	}
	
	public static ItemStack emptyVial() {
		return new ItemStack(Reliquary.CONTENT.getItem("potion"), 1, 0);
	}
	
	public static ItemStack emperorChalice() {
		return new ItemStack(getItem("emperor_chalice"));
	}
	
	public static ItemStack infernalChalice() {
		return new ItemStack(getItem("infernal_chalice"));
	}
	
	public static ItemStack witherSkull() {
		return new ItemStack(Items.skull, 1, 1);
	}

	public static ItemStack roseBush() {
		return new ItemStack(Blocks.double_plant, 1, 4);
	}

	public static ItemStack blackWool() {
		return new ItemStack(Blocks.wool, 1, 15);
	}

	public static ItemStack lapis() {
		return new ItemStack(Items.dye, 1, 4);
	}

	public static ItemStack gunPart(int i, int m) {
		return new ItemStack(getItem("gun_part"), i, m);
	}

	public static ItemStack magazine(int m) {
		return magazine(1, m);
	}

	public static ItemStack magazine(int i, int m) {
		return new ItemStack(getItem("magazine"), i, m);
	}

	public static ItemStack bullet(int m) {
		return bullet(1, m);
	}

	public static ItemStack bullet(int i, int m) {
		return new ItemStack(getItem("bullet"), i, m);
	}

	public static ItemStack ingredient(int m) {
		return new ItemStack(getItem("mob_ingredient"), 1, m);
	}

	public static ItemStack enderHeart() {
		return ingredient(11);
	}

	public static ItemStack creeperGland() {
		return ingredient(3);
	}

	public static ItemStack slimePearl() {
		return ingredient(4);
	}

	public static ItemStack batWing() {
		return ingredient(5);
	}

	public static ItemStack ribBone() {
		return ingredient(0);
	}

	public static ItemStack witherRib() {
		return ingredient(1);
	}

	public static ItemStack stormEye() {
		return ingredient(8);
	}

	public static ItemStack fertileEssence() {
		return ingredient(9);
	}

	public static ItemStack frozenCore() {
		return ingredient(10);
	}

	public static ItemStack moltenCore() {
		return ingredient(7);
	}

	public static ItemStack zombieHeart() {
		return ingredient(6);
	}

	public static ItemStack infernalClaw() {
		return ingredient(13);
	}

	public static ItemStack shellFragment() {
		return ingredient(14);
	}

	public static ItemStack squidBeak() {
		return ingredient(12);
	}

	public static ItemStack spiderFangs() {
		return ingredient(2);
	}

	public static ItemStack heartPearl(int m) {
		return new ItemStack(getItem("heart_pearl"), 1, m);
	}

	public static ItemStack nianZhu(int m) {
		return new ItemStack(getItem("heart_zhu"), 1, m);
	}

	public static ItemStack emptyVoidSatchel() {
		return new ItemStack(getItem("void_tear_empty"), 1, 0);
	}

}

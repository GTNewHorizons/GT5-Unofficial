package gtPlusPlus.core.world.darkworld.world;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.world.WorldType;

public class CustomWorldType extends WorldType{

	public CustomWorldType(String name) {
		super(name);
	}

	public CustomWorldType(int p_i1959_1_, String p_i1959_2_){
		this("test");
		try {
			//System.out.println(Arrays.toString(getClass().getSuperclass().getMethods()));
			Method m = getClass().getSuperclass().getDeclaredMethod("WorldType", new Class<?>[]{});
			m.setAccessible(true);
			m.invoke(this, p_i1959_1_, p_i1959_2_, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public CustomWorldType(int p_i1960_1_, String p_i1960_2_, int p_i1960_3_){
		this("test2");
		try {
			//System.out.println(Arrays.toString(getClass().getSuperclass().getMethods()));
			Method m = getClass().getSuperclass().getDeclaredMethod("WorldType", new Class<?>[]{});
			m.setAccessible(true);
			m.invoke(this, p_i1960_1_, p_i1960_2_, p_i1960_3_);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private WorldType getMC(){
		try {
			Constructor<WorldType> c = WorldType.class.getDeclaredConstructor();
			c.setAccessible(true); // solution
			return c.newInstance();

			// production code should handle these exceptions more gracefully
		} catch (InvocationTargetException x) {
			x.printStackTrace();
		} catch (NoSuchMethodException x) {
			x.printStackTrace();
		} catch (InstantiationException x) {
			x.printStackTrace();
		} catch (IllegalAccessException x) {
			x.printStackTrace();
		}
		return null;
	}
}


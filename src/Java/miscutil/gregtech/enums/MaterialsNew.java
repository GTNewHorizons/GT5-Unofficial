package miscutil.gregtech.enums;

import miscutil.core.util.reflection.EnumBuster;


public class MaterialsNew {
	
	EnumBuster EB = new EnumBuster(null, null);
/*
	public static void getGregMaterials() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, SecurityException{
	Utils.LOG_WARNING("Stepping through the process of Greg's materials.");
		
		Constructor<?> con = Materials.class.getDeclaredConstructors()[0]; 
		Utils.LOG_WARNING("Logging Value for Variable "+"Constructor"+":"+con.getName());
	java.lang.reflect.Method[] methods = con.getClass().getDeclaredMethods();
	Utils.LOG_WARNING("Logging Value for Variable "+"methods"+":"+methods.toString());
	for (java.lang.reflect.Method m1 : methods) { 
		Utils.LOG_WARNING("Logging Value for Variable "+"m1"+":"+m1.getName()+"| Accessible? "+m1.isAccessible());
		if (m1.getName().equals("acquireConstructorAccessor")) { 
			Utils.LOG_WARNING("Logging Value for Variable "+"m1"+":"+m1.getName()+"| Accessible? "+m1.isAccessible());
			m1.setAccessible(true);
			Utils.LOG_WARNING("Logging Value for Variable "+"m1"+":"+m1.toGenericString());
			m1.invoke(con, new Object[0]);}
	} 
	Field[] fields = con.getClass().getDeclaredFields(); 
	Utils.LOG_WARNING("Logging Value for Variable "+"fields"+":"+fields.toString()+"|"+fields.getClass());
	Object ca = null;
	for (Field f : fields) { 
		Utils.LOG_WARNING("Logging Value for Variable "+"f"+":"+f.getName()+"|"+f.getModifiers()+"|"+f.isAccessible());
		if (f.getName().equals("constructorAccessor")) {
		Utils.LOG_WARNING("Logging Value for Variable "+"f"+":"+f.isAccessible());
		f.setAccessible(true); 
		ca = f.get(con); 
		Utils.LOG_WARNING("Logging Value for Variable "+"ca"+":"+ca.toString()+"|"+ca.getClass());
		} 
	} 
	Method m = ca.getClass().getMethod( "newInstance", new Class[] { Object[].class }); 
	Utils.LOG_WARNING("Logging Value for Variable "+"m"+":"+m.getModifiers()+"|"+m.getName()+"|"+m.toGenericString()+"|"+m.isAccessible());
	m.setAccessible(true);
	Materials v = (Materials) m.invoke(ca, new Object[] { new Object[] { "NEWMATERIAL", Integer.MAX_VALUE } }); 
	System.out.println(v.getClass() + ":" + v.name() + ":" + v.ordinal());

}*/
}

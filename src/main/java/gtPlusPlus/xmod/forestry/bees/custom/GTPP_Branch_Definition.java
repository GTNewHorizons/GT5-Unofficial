package gtPlusPlus.xmod.forestry.bees.custom;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import forestry.api.genetics.IAllele;
import forestry.api.genetics.IClassification;
import gtPlusPlus.api.objects.Logger;

public enum GTPP_Branch_Definition {

	ORGANIC("ORGANIC"),
	GEM("GEM"),
	METAL("METAL"),
	RAREMETAL("RAREMETAL"),
	RADIOACTIVE("RADIOACTIVE");

	final String mFieldName;
	final Enum mActualValues;
	GTPP_Branch_Definition(String mValue){
		this.mFieldName = mValue;
		this.mActualValues = setEnumVar(mValue);
	}

	public final IAllele[] getTemplate() {		
		Class gtBranchDefClass;
		try {
			gtBranchDefClass = Class.forName("gregtech.loaders.misc.GT_BranchDefinition");
			Enum enumA = mActualValues;
			Method methodMyMethod = gtBranchDefClass.getMethod("getTemplate");

			Logger.INFO("[Bees] gtBranchDefClass: "+(gtBranchDefClass != null));
			Logger.INFO("[Bees] enumA: "+(enumA != null));
			Logger.INFO("[Bees] methodMyMethod: "+(methodMyMethod != null));
			
			return (IAllele[]) methodMyMethod.invoke(enumA);
		}
		catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			Logger.INFO("[Bees] Bad Reflection. getTemplate()");
			e.printStackTrace();
			//gregtech.loaders.misc.GT_BranchDefinition.getTemplate()
			return null;
		}		
	}

	public final IClassification getBranch() {
		Class gtBranchDefClass;
		try {
			gtBranchDefClass = Class.forName("gregtech.loaders.misc.GT_BranchDefinition");
			Enum enum_MY_SAMPLE_ENUM = mActualValues;
			Method methodMyMethod = gtBranchDefClass.getMethod("getBranch");
			

			Logger.INFO("[Bees] gtBranchDefClass: "+(gtBranchDefClass != null));
			Logger.INFO("[Bees] enum_MY_SAMPLE_ENUM: "+(enum_MY_SAMPLE_ENUM != null));
			Logger.INFO("[Bees] methodMyMethod: "+(methodMyMethod != null));
			
			return (IClassification) methodMyMethod.invoke(enum_MY_SAMPLE_ENUM);
		}
		catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			Logger.INFO("[Bees] Bad Reflection. getBranch()");
			e.printStackTrace();
			return null;
		}
	}	

	private Enum setEnumVar(String value){
		try {
			Class gtBranchDefClass = Class.forName("gregtech.loaders.misc.GT_BranchDefinition");
			Enum branchDef = Enum.valueOf(gtBranchDefClass, value);
			return branchDef;
		}
		catch (ClassNotFoundException  e){
			return null;
		}
	}



}

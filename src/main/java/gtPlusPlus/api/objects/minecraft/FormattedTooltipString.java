package gtPlusPlus.api.objects.minecraft;

public class FormattedTooltipString {

	public final String mText;
	public final boolean mPrefix;
	
	public FormattedTooltipString(String aText, boolean aPrefix) {
		mText = aText;
		mPrefix = aPrefix;
	}
	
	public String getTooltip(Object aTagValue) {
		String aTip;
		
		if (mPrefix) {
			aTip = mText+": "+aTagValue.toString();
		}
		else {
			aTip = ""+aTagValue.toString()+": "+mText;			
		}		
		return aTip;
	}
	
}

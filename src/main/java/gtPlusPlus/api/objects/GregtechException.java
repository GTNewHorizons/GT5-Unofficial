package gtPlusPlus.api.objects;

public class GregtechException extends Throwable {

	private static final long serialVersionUID = 3601884582161841486L;

	public GregtechException(String aError) {
		this(aError, true);
	}
	
	public GregtechException(String aError, boolean aIsVerbose) {
		Logger.ERROR("Throwing GT++ Exception!");
		Logger.ERROR("[EXCEPTION] "+aError);
		if (aIsVerbose) {
			Logger.INFO("Throwing GT++ Exception!");
			Logger.INFO("[EXCEPTION] "+aError);
			printStackTrace();
		}
	}
	
	@Override
	public void printStackTrace() {
		super.printStackTrace();
	}
	
	

}

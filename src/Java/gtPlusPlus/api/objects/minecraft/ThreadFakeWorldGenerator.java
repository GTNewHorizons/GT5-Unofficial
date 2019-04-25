package gtPlusPlus.api.objects.minecraft;

import gtPlusPlus.xmod.gregtech.common.helpers.treefarm.TreeGenerator;

public class ThreadFakeWorldGenerator extends Thread {

	public boolean canRun = true;   
    public boolean isRunning = false;
    
    private static final long INIT_TIME;
    private static long internalTickCounter = 0;
    
    public TreeGenerator mGenerator;

	private static final ThreadFakeWorldGenerator mThread;
    	
	static {
		mThread = new ThreadFakeWorldGenerator();
		INIT_TIME = (System.currentTimeMillis());
	}
	
	public ThreadFakeWorldGenerator() {
        setName("gtpp.handler.fakeworldtrees");
        run();
	}
	
	public static ThreadFakeWorldGenerator getInstance() {
		return mThread;
	}
	
	public static void stopThread() {
		mThread.canRun = false;
	}
	
	
	@Override
	public void run() {
		
		if (!isRunning) {
			isRunning = true;
		}
		else {
			return;
		}

		if (canRun){
			if (mGenerator == null) {
				mGenerator = new TreeGenerator();
			}
		}
		
		while (mGenerator == null) {
			if (mGenerator != null) {
				break;
			}
		}
		stopThread();
	}		
	
	
}

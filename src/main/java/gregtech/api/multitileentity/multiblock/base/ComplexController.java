package gregtech.api.multitileentity.multiblock.base;

public abstract class ComplexController<T extends ComplexController<T>> extends PowerController<T> {
    
    private int maxComplexParallels = 0;
    private int currentComplexParallels = 0;
    private int onParallel = 0;
    private long[] maxProgressTimes;
    private long[] progressTimes;

    protected void setMaxComplexParallels(int parallel) {
        this.maxComplexParallels = parallel;
        maxProgressTimes = new long[parallel];
        progressTimes = new long[parallel];
    }

    
}

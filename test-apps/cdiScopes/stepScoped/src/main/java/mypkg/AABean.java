package mypkg;

import jakarta.batch.api.BatchProperty;
import jakarta.batch.runtime.context.StepContext;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

@Dependent
public class AABean {
	
    @Inject
    @BatchProperty(name = "aa")
    String aa;
    
    @Inject StepContext stepCtx;
    
	private int count = 0;

	public void getCount() {
		System.out.println("SKSK: In AABean, aa = " + aa);
		System.out.println("SKSK: In AABean, count = " + ++count);
		System.out.println("SKSK: In AABean, step name = " + stepCtx.getStepName());
	}
}

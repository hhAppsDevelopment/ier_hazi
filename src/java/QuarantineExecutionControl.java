import jason.control.ExecutionControl;

public class QuarantineExecutionControl extends ExecutionControl  {

	protected void allAgsFinished() {
		try {
          Thread.sleep(100);
        } catch (Exception e) {}
    }
	
}

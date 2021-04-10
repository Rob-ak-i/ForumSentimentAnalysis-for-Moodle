package common;

import java.util.HashMap;

public class ObjectProcessorThread implements Runnable{
	public static final int PROCESS_DONE_SUCCESSFUL = 2;
	public static final int PROCESS_DONE_NOT_SUCCESS = 3;
	String method=null;
	HashMap<String, Object> processorParameters=null;
	ObjectProcessor processor=null;
	Thread thread=null;
	int workDone=0;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(workDone==0)
			workDone=1;
		processor.process(method, processorParameters);
		workDone=2;
		thread=null;
	}
	public boolean init(ObjectProcessor processor, String method, HashMap<String, Object> processorParameters) {
		this.method = method;
		this.processorParameters = processorParameters;
		this.processor = processor;
		if(processor == null)return false;
		return true;
	}
	public synchronized void start() {
		thread = new Thread(this);
    	thread.setName("processor:"+processor.getClass().getSimpleName()+";procedure="+method+";data="+processorParameters.toString());
    	thread.start();
	}
	public synchronized int isWorkDone() {return workDone;}

}

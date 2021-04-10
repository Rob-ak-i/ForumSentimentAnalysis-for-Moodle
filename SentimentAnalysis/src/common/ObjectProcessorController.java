package common;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class ObjectProcessorController implements Runnable{
	public static int WORKDONESUCCESSTIMEOUT = 1000;
	public static int WORKDONENOTSUCCESSTIMEOUT = 5000;
	int maxSleepTime;
	int statement;
	int statementNew;
	boolean updated;
	int ticks;
	public static Thread thread;
	public static boolean threadState;
	ArrayList<String> operationsStack;
	HashMap<ObjectProcessor,ObjectProcessorThread> processes;
	
	ArrayList<HashMap<String, Object>> parametersQueueList;
	ArrayList<String> methodQueueList;
	ArrayList<ObjectProcessor> processorQueueList;
	
	ArrayList<String> workDoneList;
	ArrayList<Boolean> workDoneStatementList;
	ArrayList<Integer> workDoneTimeList;
	
	public ObjectProcessorController() {
		maxSleepTime=100;
		statement=0;
		statementNew=0;
		ticks=0;
		processes = new HashMap<ObjectProcessor,ObjectProcessorThread>();
		
		parametersQueueList = new ArrayList<HashMap<String, Object>>();
		methodQueueList = new ArrayList<String>();
		processorQueueList = new ArrayList<ObjectProcessor>();

		workDoneList = new ArrayList<String>();
		workDoneStatementList = new ArrayList<Boolean>();
		workDoneTimeList = new ArrayList<Integer>();
		
		updated=false;
		threadState=true;
	}
	public boolean addOperation(ObjectProcessor processor, String methodName, HashMap<String, Object> processorParameters) {
		String processorName=processor.getClass().getSimpleName();
		//if(operations.containsKey(operationCode)!=operationState)return false;
		if(processes.containsKey(processor)) {
			processorQueueList.add(processor);
			methodQueueList.add(methodName);
			parametersQueueList.add(processorParameters);
		}else {
			ObjectProcessorThread newJob = new ObjectProcessorThread();
			boolean result = newJob.init(processor, methodName, processorParameters);
			if(result==false)newJob=null;
			processes.put(processor, newJob);
			newJob.start();
		}
		//operationsExecutors.put(operationCode, null);
		updated=true;
		return true;
	}
	private boolean removeOperation(ObjectProcessor operation, String operationCaption) {
		//if(operations.containsKey(operationCode)!=operationState)return false;
		updated=true;
		return true;
	}
	private static Color[] colors = {Color.black, Color.gray, Color.orange, Color.green,   Color.red};
	@Override
	public void run() {String method;ObjectProcessor proc;HashMap<String, Object>pars;
		while(threadState) {
			long startProcess=System.nanoTime();
			//duration of the frame rendering in ms :
			if(workDoneList.size()>0) {
				CommonData.frame.clearColoredText();
				for(int i=0;i<workDoneList.size();) {
					workDoneTimeList.set(i,workDoneTimeList.get(i)-maxSleepTime);
					if(workDoneTimeList.get(i)<=0) {
						workDoneTimeList.remove(i);
						workDoneList.remove(i);
						workDoneStatementList.remove(i);
						updated=true;
						continue;
					}
					if(workDoneStatementList.get(i))
						CommonData.frame.addColoredText(workDoneList.get(i),colors[3]);
					else
						CommonData.frame.addColoredText(workDoneList.get(i),colors[4]);
					++i;
				}
				if(workDoneList.size()==0)CommonData.frame.clearColoredText();
			}
			if(updated) {
				int colorIndex=0;
				for(ObjectProcessor key:processes.keySet()) {
					//colorIndex=3;//=statements.get(key);
					colorIndex=1+processes.get(key).isWorkDone();
					if(colorIndex<0)colorIndex=0;if(colorIndex>=colors.length)colorIndex=0;
					CommonData.frame.addColoredText(processes.get(key).processor.getClass().getSimpleName()+"."+processes.get(key).method+"("+processes.get(key).processorParameters.toString()+")", colors[colorIndex]);
				}
				CommonData.frame.addColoredText("||", Color.black);
				for(int i=0;i<processorQueueList.size();++i) {
					method = methodQueueList.get(i);
					proc = processorQueueList.get(i);
					pars = parametersQueueList.get(i);
					colorIndex=1;//=statements.get(key);
					//if(operationsExecutors.get(key).isWorkDone())colorIndex=1;
					//if(colorIndex<0)colorIndex=0;if(colorIndex>=colors.length)colorIndex=0;
					CommonData.frame.addColoredText(processorQueueList.get(i).getClass().getSimpleName()+"."+method+";", colors[colorIndex]);
				}
				updated=false;
			}
			if(!updated&&(processes.size()>0)){
				ArrayList<ObjectProcessor>processesMarkedForRemove = new ArrayList<ObjectProcessor>();
				for(ObjectProcessor key:processes.keySet()) {
					if(processes.get(key).isWorkDone()>=2) {
						updated=true;
						//adding finished job to workDoneList
						workDoneList.add(processes.get(key).getClass().getSimpleName());
						
						if(processes.get(key).isWorkDone()==ObjectProcessorThread.PROCESS_DONE_SUCCESSFUL) {
							workDoneStatementList.add(true);
							workDoneTimeList.add(WORKDONESUCCESSTIMEOUT);
						}else {
							workDoneStatementList.add(false);
							workDoneTimeList.add(WORKDONENOTSUCCESSTIMEOUT);
						}
						//delectingFinishedJob
						processesMarkedForRemove.add(key);
						//processes.remove(key);
					}
				}
				ObjectProcessor key;int keyIndex;
				for(int i=0;i<processesMarkedForRemove.size();++i) {
					key=processesMarkedForRemove.get(i);
					processes.remove(key);
					//adding new job from queue
					if(processorQueueList.size()>0) { 
						keyIndex=processorQueueList.indexOf(key);
						if(keyIndex>=0) {
							ObjectProcessorThread newJob = new ObjectProcessorThread();
							boolean result = newJob.init(processorQueueList.get(keyIndex), methodQueueList.get(keyIndex), parametersQueueList.get(keyIndex));
							if(result==false)newJob=null;
							processes.put(processorQueueList.get(keyIndex), newJob);
							newJob.start();
							processorQueueList.remove(keyIndex);
							methodQueueList.remove(keyIndex);
							parametersQueueList.remove(keyIndex);
						}
					}
				}
				processesMarkedForRemove.clear();
				processesMarkedForRemove=null;
			}
			long durationMs=TimeUnit.NANOSECONDS.toMillis(System.nanoTime()-startProcess);
			if (durationMs < maxSleepTime) {
				try {
					Thread.sleep(maxSleepTime - durationMs);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	public void addToQueue() {
		
	}
    public synchronized void start() {
    	thread = new Thread(this);
    	thread.setName("operationsState thread");
    	thread.start();
    	threadState = true;
    }
    public synchronized void stop() {
        thread = null;
        threadState = false;
    }
	
}


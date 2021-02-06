package common;
//MY RENDERING SYSTEM EXPLAINED IN JAVA AND WITH ONLY ONE CLASS.
//DOES NOT INCLUDE TICKING JUST RENDERING, TICKING IS A DIFFERENT THREAD.
//(so they can run on different frames per second)
//main() method is at the very bottom.

/*
 * @Author
 *  CodyOrr4
 */
import javax.swing.*;

import parts.GenericPhantom;
import parts.PartBasic;
import util.Colors;
import util.Dot;

import java.awt.*;
import java.awt.image.*;
import java.util.concurrent.TimeUnit;
//import com.sun.beans.util.Cache;

public class EntityRenderer implements Runnable{
	public static int isShowPartNamesAlways=0;
	
	//public static Cache cache;
	private static JFrame frame;
	private static JPanel panel;
	//public static JViewport viewport;
	public static Canvas canvas;
	public static BufferedImage bi;
	public static BufferStrategy bs;
	public static Graphics g;
	public static Thread renderingThread;
	public static boolean renderingSwitch;
	public static int renderingMethod=0;
	public static int maxSleepTime;

	private static int width=800;
	private static int height=600;
	public static double scalex=1.;
	public static double scaley=1.;
	public static int windowLeft=0;
	public static int windowRight=0;
	public static int windowUp=0;
	public static int windowDown=0;
	
	public static double camposx=350;
	public static double camposy=250;
    //turns this runnable into an object.
	public static Dot getImageDotFromMathCoords(double x,double y){
		int xi=(int)(((double)x-camposx)*scalex+((double)(width>>>1)));
		int yi=(int)(((double)y-camposy)*scaley+((double)(height>>>1)));
		return Dot.getDot(xi,yi);
	}
	public static Dot getRealDotFromImageCoords(double xi,double yi){
		int x=(int)((xi-((double)(width>>>1)))/scalex+camposx);
		int y=(int)((yi-((double)(height>>>1)))/scaley+camposy);
		return Dot.getDot(x,y);
	}
	public static void updateWindow(boolean resized){
		/*
		if(resized){
			scalex=(scalex*((double)(panel.getWidth())))/((double)width);
			scaley=(scaley*((double)(panel.getHeight())))/((double)height);
		}
		*/
		width=panel.getWidth();
		height=panel.getHeight();
		windowLeft=(int)(camposx-((double)(width>>>1))/scalex);
		windowRight=(int)(camposx+((double)(width>>>1))/scalex);
		windowUp=(int)(camposy-((double)(height>>>1))/scaley);
		windowDown=(int)(camposy+((double)(height>>>1))/scaley);
	}
    public EntityRenderer(JFrame frameIn, JPanel panelIn, Canvas canvas, int renderMethodIn) {
    	frame=frameIn;
    	panel=panelIn;
        renderingMethod= renderMethodIn;
		updateWindow(false);
        //frame = new JFrame("Rendering System Example");
        //frame.setSize(new Dimension(800, 600));
        //frame.setDefaultCloseOperation(1); - simply hides frame hen i tried to close window 
        //frame.setVisible(true);
        //panel = new JPanel();
        //panel.setBackground(Color.DARK_GRAY);
        if(renderingMethod==0) {
	        this.canvas = canvas;
	        canvas.setBackground(Color.white);
	        canvas.setPreferredSize(new Dimension(width, height));
	        canvas.setMinimumSize(new Dimension(100, 100));
	        canvas.setMaximumSize(new Dimension(2000, 2000));
	        panel.add(canvas);
	        //frame.getContentPane().add(panel);
        }
        if(renderingMethod==1) {
    		//panel.setBounds(0,30,CommonData.WIDTH, CommonData.HEIGHT);
    		//panel.setBackground(Color.white);
    		panel.setOpaque(true);
    		//frame.getContentPane().add(panel);//frame.add(panel);
        }
        maxSleepTime=100;
    }

    /**used to run things that are not meant to be run in a loop;*/
    private void init() {
        //cache.initCache(); //can now grab sprites (including names/ids) and other types within cache.
    }
    public static BufferedImage getCanvasImage() {
    	if(renderingMethod==1)
    		return bi;
    	return (BufferedImage) canvas.createImage(width, height);
    }
    //runs the runnable
    public void createCanvasComponents(boolean withoutNullCheck) {
        System.out.println("creating canvas components: renderingMethod = "+Integer.toString(renderingMethod));
    	if(renderingMethod==1 && bi == null
    			|| renderingMethod==1 && withoutNullCheck ) {
    		width=panel.getWidth();
    		height=panel.getHeight();
        	bi=new  BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			g = bi.createGraphics();
			PartBasic.g=g;
            
			Graphics2D d2 = (Graphics2D) g;
			d2.setColor(Color.white);
			d2.fillRect(0, 0, width, height);
        }
        if(renderingMethod==0 && bs == null
    			|| renderingMethod==1 && withoutNullCheck ) {
    		width=panel.getWidth();
    		height=panel.getHeight();
    		canvas.setSize(width, height);
            canvas.createBufferStrategy(3);//should only need a max of 3.
            bs = canvas.getBufferStrategy();
            g = bs.getDrawGraphics();

			Graphics2D d2 = (Graphics2D) g;
			d2.setColor(Color.white);
			d2.fillRect(0, 0, width, height);
            
			PartBasic.g=g;
			


        }
    }
    public void run() {
        init();
        while(renderingSwitch) {
        	
            if(bi == null && bs==null) {
            	this.createCanvasComponents(false);
            }

            long startRendering=System.nanoTime();
            g.clearRect(0, 0, width, height);
            //g=bs.getDrawGraphics();

			if(EntityEditorMouseListener.mode==EntityEditorMouseListener.MODE_PUT_PHANTOM_MODE)
				GenericPhantom.draw(g);
            //if(animationPhase%3==0)
            	render(g);//drawing with methods
            
    		animationPhase=animationPhase+1;if(animationPhase==animationPhasesCount)animationPhase=0;
    		if(animationPhase==0)
    			renderMap();
            bs.show();
            //g.dispose();
            
            //duration of the frame rendering in ms :
            long durationMs=TimeUnit.NANOSECONDS.toMillis(System.nanoTime()-startRendering);
            // now waits 
            if (durationMs < maxSleepTime) 
            {
            	try {
					Thread.sleep(maxSleepTime - durationMs);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }     
        }
    }
    
    private static int animationPhase=0;
    private static int animationPhasesCount=16;
    private void renderMap(){
    	parts.SchematicManager.drawLogicMap(windowLeft, windowRight, windowUp, windowDown, camposx, camposy, scalex, scaley);
    }
    /**renders everything (this method is used in a while() loop based on a boolean, within the run() method);*/
    private void render(Graphics g) {
    	g.setColor(Colors.colorMain);
    	parts.SchematicManager.drawScene(g, windowLeft, windowRight, windowUp, windowDown, camposx, camposy, scalex, scaley);
    	
    	//panel.requestFocusInWindow();
        //g.drawImage(cache.getSprite(0), 400, 300, 25, 25, null);
    	g.setColor(Colors.colorBackground);
    }

    //starts the run method and creates a thread for this 
    public synchronized void start() {
        renderingThread = new Thread(this);
        renderingThread.setName("Game Rendering Thread");
        renderingThread.start();
        renderingSwitch = true;
    }

    //stops the while loop by setting the boolean to false and the thread is now null
    public synchronized void stop() {
        renderingThread = null;
        renderingSwitch = false;
    }


   ///public static void main(String[] args) {        Renderer gameExample = new Renderer();        gameExample.start();    }

}
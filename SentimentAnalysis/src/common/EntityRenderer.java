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

import parts.PartBasic;

import java.awt.*;
import java.awt.image.*;
import java.util.concurrent.TimeUnit;
//import com.sun.beans.util.Cache;

public class EntityRenderer implements Runnable{
	//public static Cache cache;
	public static JFrame frame;
	public static JPanel panel;
	//public static JViewport viewport;
	public static Canvas canvas;
	public static BufferedImage bi;
	public static BufferStrategy bs;
	public static Graphics g;
	public static Thread renderingThread;
	public static boolean renderingSwitch;
	public static int renderingMethod=0;
	public static int fps;
	private static int width=800;
	private static int height=600;

	public static double camposx=0;
	public static double camposy=0;
	public static double camvisionradius=400;
    //turns this runnable into an object.
    public EntityRenderer(JFrame frameIn, JPanel panelIn, int renderMethodIn) {
    	frame=frameIn;
    	panel=panelIn;
        renderingMethod= renderMethodIn;
		width=panel.getWidth();
		height=panel.getHeight();
        //frame = new JFrame("Rendering System Example");
        //frame.setSize(new Dimension(800, 600));
        //frame.setDefaultCloseOperation(1); - simply hides frame hen i tried to close window 
        //frame.setVisible(true);
        //panel = new JPanel();
        //panel.setBackground(Color.DARK_GRAY);
        if(renderingMethod==0) {
	        canvas = new Canvas();
	        canvas.setBackground(Color.white);
	        canvas.setPreferredSize(new Dimension(width, height));
	        canvas.setMinimumSize(new Dimension(100, 100));
	        canvas.setMaximumSize(new Dimension(2000, 2000));
	        panel.add(canvas);
	        frame.getContentPane().add(panel);
        }
        if(renderingMethod==1) {
    		panel.setBounds(0,30,CommonData.WIDTH, CommonData.HEIGHT);
    		panel.setBackground(Color.white);
    		panel.setOpaque(true);
    		frame.add(panel);
        }
        fps=60;
    }

    /**used to run things that are not meant to be run in a loop;*/
    private void init() {
        //cache.initCache(); //can now grab sprites (including names/ids) and other types within cache.
    }
    private static boolean flipflop=false;
    public static BufferedImage getCanvasImage() {
    	if(renderingMethod==1)
    		return bi;
    	return (BufferedImage) canvas.createImage(width, height);
    }
    //runs the runnable
    public void createCanvasComponents(boolean withoutNullCheck) {
    	if(renderingMethod==1 && bi == null
    			|| renderingMethod==1 && withoutNullCheck ) {
    		width=panel.getWidth();
    		height=panel.getHeight();
        	bi=new  BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			g = bi.createGraphics();
			PartBasic.g=g;
            System.out.println("creating canvas components...");
            
			Graphics2D d2 = (Graphics2D) g;
			d2.setColor(Color.white);
			d2.fillRect(0, 0, width, height);
            
        	EntityEditor_Helper.graphics=g;
        	EntityEditor_Helper.graphics2d=(Graphics2D)g;
        	EntityEditor_Helper.frame=frame;
        	EntityEditor_Helper.panel=panel;
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
            System.out.println("creating canvas components...");
            
        	EntityEditor_Helper.graphics=g;
        	EntityEditor_Helper.graphics2d=(Graphics2D)g;
        	EntityEditor_Helper.frame=frame;
        	EntityEditor_Helper.panel=panel;
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
            render(g);//drawing with methods

            bs.show();
            //g.dispose();
            
            //duration of the frame rendering in ms :
            long durationMs=TimeUnit.NANOSECONDS.toMillis(System.nanoTime()-startRendering);
            // now waits 
            if (durationMs < fps) 
            {
            	try {
					Thread.sleep(fps - durationMs);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }     
        }
    }
    /**renders everything (this method is used in a while() loop based on a boolean, within the run() method);*/
    private void render(Graphics g) {
    	parts.SchematicManager.drawScene();
    	g.setColor(Color.black);
        //g.drawImage(cache.getSprite(0), 400, 300, 25, 25, null);
		//flipflop=!flipflop;
    	if(flipflop)
    		g.drawRect(-50, -50, 500, 500);
    	else
    		g.drawRect(-100, -100, 500, 500);

    	g.setColor(Color.white);
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


    /*main method obv.
    public static void main(String[] args) {
        Renderer gameExample = new Renderer();
        gameExample.start();
    }
	*/

}
/**
For this, you can, for each frame :

//a the start of rendering process
long startRendering=System.nanoTime();

... rendering here...

//duration of the frame rendering in ms :
long durationMs=TimeUnit.NANOSECONDS.toMillis(System.nanoTime()-startRendering);

// now waits 
if (durationMs < fps) 
{
	renderingThread.sleep(fps - durationMs);
}

System.nanoTime allows to measure time with a good accuracy : 
if you take the difference between two calls of System.nanoTime(), 
you have the time elapsed between the calls in nanoseconds. 
I sugest to not use System.currentTimeMillis which is less accurate

*/

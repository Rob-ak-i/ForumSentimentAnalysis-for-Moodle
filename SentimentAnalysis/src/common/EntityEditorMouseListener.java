package common;


import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

import parts.GenericPhantom;
import parts.PartBinaryBasic;
import parts.PartNode;
import util.Dot;

public class EntityEditorMouseListener extends MouseInputAdapter{

	public static MouseWheelListener mouseWheelListener = (new MouseWheelListener() {
		@Override
		public void mouseWheelMoved(MouseWheelEvent arg0) {
			// TODO Auto-generated method stub
			double value=arg0.getPreciseWheelRotation();
			if(EntityEditorMouseListener.isLControlPressed) {
				EntityRenderer.scalex*=1-value*0.1;
				EntityRenderer.scaley*=1-value*0.1;
			}else {
				if(EntityEditorMouseListener.isLShiftPressed) {
					EntityRenderer.camposx*=1+value*EntityRenderer.scalex*0.1;
				}else
					EntityRenderer.camposy*=1+value*EntityRenderer.scaley*0.1;
			}
			EntityRenderer.updateWindow(false);
		}
	});
	public static parts.PartNode nodeFrom=null, nodeTo=null;public static int partClassIndex=0;
	public static parts.PartBasic tempelement=null;
	public static JComboBox partsList;
	

	static boolean pressed=false;
	
	
	//public static final int MODE_DELETE=-1;
	public static final int MODE_OBSERVE=0;
	public static final int MODE_SELECT=1;
	public static final int MODE_MOVE=2;
	
	public static final int MODE_DRAW_LINE=4;
	public static final int MODE_SETUP_NODE=10;
	public static final int MODE_PUT_PHANTOM_MODE=11;
	public static final int MODE_SETUP_PART_STAGE1_selectNodeFrom=30;
	public static final int MODE_SETUP_PART_STAGE2_selectNodeTo=31;
	public static final int MODE_SETUP_PART_STAGE3_selectPosition=32;
	public static final int MODE_SELECT_WITH_CONTROL=40;
	public static final int MODE_SETUP_GenBy_STAGE1_selectNodeFrom=41;
	public static final int MODE_SETUP_GenBy_STAGE2_selectNodeTo=42;
	public static final int MODE_SETUP_GenBy_STAGE1_selectElementCurrence=43;
	
	public static int mode=MODE_OBSERVE;//public void setMode(int arg) {mode=arg;}

	public static boolean isLShiftPressed=false;
	public static boolean isLControlPressed=false;
	private static Point cur=null;
	
	JFrame frame;//EntityEditorGlassPane glassPane;
	Container contentPane;
    JMenuBar menuBar;
    JToolBar toolbar;
	public EntityEditorMouseListener(
			JToolBar toolbar,
			JMenuBar menuBar,
			JFrame frame,//EntityEditorGlassPane glassPane,
			Container contentPane){
		this.toolbar=toolbar;
		this.menuBar=menuBar;
		this.frame=frame;
		this.contentPane=contentPane;
	}
	
    public void mouseMoved(MouseEvent e) {
		if(!redispatchMouseEvent(e))return;

		if(mode==MODE_PUT_PHANTOM_MODE){
			GenericPhantom.xi=e.getX();//
			GenericPhantom.yi=e.getY();//cur.y;
		}
    }
    
    public void mouseDragged(MouseEvent e) {
		if(!redispatchMouseEvent(e))return;
		if(mode==MODE_PUT_PHANTOM_MODE){
			GenericPhantom.xi=e.getX();
			GenericPhantom.yi=e.getY();
		}
		if (pressed==true) {
			switch (mode) {
			
			case MODE_SELECT:
				break;
				
			}
		}
	}
	public void mouseClicked(MouseEvent e) {
		if(!redispatchMouseEvent(e))return;
		
		pressed=true;
	}
 	/*
    public void mouseEntered(MouseEvent e) {
		if(!redispatchMouseEvent(e))return;
    }
 
    public void mouseExited(MouseEvent e) {
		if(!redispatchMouseEvent(e))return;
    }*/
    /*
    public void mousePressed(MouseEvent e) {
		if(!redispatchMouseEvent(e))return;
    }
 	*/
    public void mouseReleased(MouseEvent e) {
		if(!redispatchMouseEvent(e))return;
		int xScr=e.getX();//cur.x;
		int yScr=e.getY();//cur.y;
		Dot  realCoords= EntityRenderer.getRealDotFromImageCoords(xScr, yScr);
		int xMath=realCoords.x;//cur.x;
		int yMath=realCoords.y;//cur.y;
		Dot MousePosOnRealMap=EntityRenderer.getRealDotFromImageCoords(xScr, yScr);
		//common computations for oval and rectangle
		switch(mode) {
		case MODE_PUT_PHANTOM_MODE:
			CommonData.scheme.printForum(xMath,yMath);
			mode=MODE_OBSERVE;
			GenericPhantom.flush();
			break;
		// line
		case MODE_DRAW_LINE:
			System.out.println("Attempting to make line with EntityEditor_Helper");
			//g.drawLine(xf, yf, e.getX(), e.getY());
			break;
		case MODE_MOVE:
			if(CommonData.partSelected==null)break;
			if(CommonData.scheme.whatElementIndexInZone(xScr,yScr,PartBinaryBasic.SIZE_WIDTH_STANDART)!=-1)break;
			//CommonData.partSelected.selected=0;//.drawWithColor(colorBackground);
			//if(EntityEditor_Helper.isShowPartNamesAlways==0)	CommonData.partSelected.drawName(colorBackground);
			//CommonData.partSelected.selected=0;//drawWithColor(colorBackground);
			CommonData.partSelected.setPos(MousePosOnRealMap.x,MousePosOnRealMap.y);
			//CommonData.scheme.drawElementOnMatrix(CommonData.partSelected, false);
			//CommonData.partSelected.selected=1;//.drawWithColor(colorSelected);
			//if(EntityEditor_Helper.isShowPartNamesAlways==0)	CommonData.partSelected.drawName(colorSelected);
			//CommonData.scheme.redrawAll();
			break;
		case MODE_SELECT:
			if(CommonData.partSelected!=null){
				CommonData.partSelected.selected=0;
			}
			
			tempelement=CommonData.scheme.whatElementInZone(xScr, yScr, 10);
			if(tempelement==null) {
				CommonData.partSelected=null;
				CommonData.nameedit.setText(common.Lang.InnerTable.Item.itemNameEditFantomCaptionName);
				CommonData.captionedit.setText(common.Lang.InnerTable.Item.itemCaptionEditFantomCaptionName);
				break;
			}

			CommonData.nameedit.setText(tempelement.partName);
			CommonData.captionedit.setText(tempelement.getCaption());
			CommonData.partSelected=tempelement;
			CommonData.partSelected.selected=1;
			//if(CommonData.partSelected.getClass().getSimpleName().compareTo(PartBinaryBasic.classesList[6].getSimpleName())==0)
			//	((parts.PartBinaryRelation)CommonData.partSelected).drawCurrent(Color.blue, 1, 1);
			break;
			
		case MODE_SETUP_NODE:
			tempelement=CommonData.scheme.whatElementInZone(xScr, yScr, 10);
			if(tempelement!=null)break;
			CommonData.scheme.addNode(xMath, yMath);
			break;
		case MODE_SELECT_WITH_CONTROL:
			if(CommonData.partSelected==null)break;
			tempelement=CommonData.scheme.whatElementInZone(xScr, yScr, 10);
			if(tempelement==null)break;
			//((PartBinaryControlledBasic)CommonData.partSelected).getNextElement(tempelement);
			break;
			
			
			
			
		case MODE_SETUP_PART_STAGE1_selectNodeFrom:
			/**setting up double port
			 * part 1 - basic node selection*/
			tempelement=CommonData.scheme.whatElementInZone(xScr, yScr, 10);
			if(tempelement==null)break;
			if(tempelement.getClass().getSimpleName().compareTo(PartNode.class.getSimpleName())!=0)break;
			nodeFrom=(PartNode) tempelement;
			nodeFrom.selected=2;
			mode=MODE_SETUP_PART_STAGE2_selectNodeTo;
			break;
		case MODE_SETUP_PART_STAGE2_selectNodeTo:
			/**setting up double port
			 * part 2 - second node selection*/
			tempelement=CommonData.scheme.whatElementInZone(xScr, yScr, 10);
			if(tempelement==null)break;
			if(tempelement.getClass().getSimpleName().compareTo(PartNode.class.getSimpleName())!=0)break;
			nodeTo=(PartNode) tempelement;
			if(nodeTo==nodeFrom)break;
			nodeTo.selected=3;
			mode=MODE_SETUP_PART_STAGE3_selectPosition;
			break;
		case MODE_SETUP_PART_STAGE3_selectPosition:
			/**setting up double port
			 * part 3 - positioning and setting*/
			CommonData.scheme.addPart(xMath, yMath, nodeFrom, nodeTo, partsList.getSelectedIndex());
			nodeFrom.selected=0;
			nodeTo.selected=0;
			nodeFrom=null;
			nodeTo=null;
			
			mode=MODE_SETUP_PART_STAGE1_selectNodeFrom;
			break;
			
			
			
			
		}
		pressed=false;
    }

    //A basic implementation of redispatching events.
    private boolean redispatchMouseEvent(MouseEvent e) {
        cur=e.getPoint();
    	boolean result =true;
        Point glassPanePoint = e.getPoint();
        Container container = contentPane;
        Point canvasPoint = SwingUtilities.convertPoint(
                                        frame,
                                        glassPanePoint,
                                        CommonData.canvas);
        //cur=canvasPoint;
        if (canvasPoint.y < 0) { //we're not in the content pane
            if (canvasPoint.y + menuBar.getHeight() >= 0) { 
                //The mouse event is over the menu bar.
                //Could handle specially.
            } else { 
                //The mouse event is over non-system window 
                //decorations, such as the ones provided by
                //the Java look and feel.
                //Could handle specially.
            }
            result=false;
        } else {
            //The mouse event is probably over the content pane.
            //Find out exactly which component it's over.  
            Component component = 
                SwingUtilities.getDeepestComponentAt(
                                        container,
                                        canvasPoint.x,
                                        canvasPoint.y);
                             
            if ((component != null) 
                && (component.equals(toolbar))) {
                //Forward events over the check box.
                Point componentPoint = SwingUtilities.convertPoint(
                                            frame,
                                            glassPanePoint,
                                            component);
                component.dispatchEvent(new MouseEvent(component,
                                                     e.getID(),
                                                     e.getWhen(),
                                                     e.getModifiers(),
                                                     componentPoint.x,
                                                     componentPoint.y,
                                                     e.getClickCount(),
                                                     e.isPopupTrigger()));
                result=false;
            }
        }
        return result;
    }
    public static KeyAdapter keyAdapter = (new  KeyAdapter() {
		public void keyReleased(KeyEvent e) {
			int keyCode=e.getExtendedKeyCode();
			switch(keyCode) {
			case KeyEvent.VK_CONTROL:
				isLControlPressed=false;
				if(mode==MODE_SELECT_WITH_CONTROL)
					mode=MODE_SELECT;
				break;
			case KeyEvent.VK_SHIFT:
				isLShiftPressed=false;
				break;
			default:
				System.out.println(keyCode);
				break;
			}
			//System.out.println(keyCode);
			// setting focus on panel,
			// for writing text on it
			//panel.requestFocus();
		}
		public void keyPressed(KeyEvent e) {
			int keyCode=e.getExtendedKeyCode();
			switch(keyCode) {
			case KeyEvent.VK_ESCAPE:
				if(mode==MODE_PUT_PHANTOM_MODE){
					mode=MODE_OBSERVE;
					GenericPhantom.flush();
				}
				break;
			case KeyEvent.VK_CONTROL:
				isLControlPressed=true;
				if(mode==MODE_SELECT)
					mode=MODE_SELECT_WITH_CONTROL;
				break;
			case KeyEvent.VK_SHIFT:
				isLShiftPressed=true;
				break;
			case 45:case 61:
				if(!isLControlPressed)break;
				int value=1;
				if(keyCode==45)value=-1;
				EntityRenderer.scalex*=1+value*0.1;
				EntityRenderer.scaley*=1+value*0.1;
				EntityRenderer.updateWindow(false);
				break;
			case 37:case 39://left-right
				if(keyCode==39)EntityRenderer.camposx+=1.*EntityRenderer.scalex*0.1;
				else EntityRenderer.camposx-=1.*EntityRenderer.scalex*0.1;
				EntityRenderer.updateWindow(false);
				break;
			case 38:case 40://up-down
				if(keyCode==38)EntityRenderer.camposy-=1*EntityRenderer.scalex*0.1;
				else EntityRenderer.camposy+=1*EntityRenderer.scalex*0.1;
				EntityRenderer.updateWindow(false);
				break;
			default:
				System.out.println(keyCode);
				break;	
			}
			//panel.requestFocus();
		}
		public void keyTyped(KeyEvent e) {
			int keyCode=e.getExtendedKeyCode();
			//System.out.println(keyCode);
			switch(keyCode){

			case 37:case 39://left-right
				if(keyCode==39)EntityRenderer.camposx+=1.*EntityRenderer.scalex*0.1;
				else EntityRenderer.camposx-=1.*EntityRenderer.scalex*0.1;
				break;
			case 38:case 40://up-down
				if(keyCode==38)EntityRenderer.camposy-=1*EntityRenderer.scalex*0.1;
				else EntityRenderer.camposy+=1*EntityRenderer.scalex*0.1;
				break;
			}
			if(mode==MODE_SELECT && CommonData.partSelected!=null){
				if(CommonData.partSelected.getClass().getSimpleName().compareTo(PartNode.class.getSimpleName())!=0) {
					if(e.getKeyChar()=='=') {
						//TODO keyCharAction
					}						
				} 
			}
			//panel.requestFocus();
		}
	});
}

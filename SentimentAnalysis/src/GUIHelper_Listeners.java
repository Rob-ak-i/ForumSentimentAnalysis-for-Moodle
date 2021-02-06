

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import common.CommonData;
import common.EntityEditorMouseListener;
import common.EntityRenderer;

import parts.GenericPhantom;
import parts.PartBinaryBasic;
import parts.PartNode;
import parts.SchematicManager;
import util.Dot;

public class GUIHelper_Listeners {
	
	/**if we load picture*/
	public static boolean loading=false;
	public static void addListeners(JFrame frame){		
		frame.addComponentListener(new  ComponentAdapter() {
			public void componentResized(java.awt.event.ComponentEvent evt) {
				/**if loading, then change forms size*/
				/**making this in loading code section(bottom)*/
				if(CommonData.frame==null||CommonData.panel==null)return;
				if(loading==false) {
					loading=true;
					CommonData.WIDTH=CommonData.frame.getWidth();
					CommonData.HEIGHT=CommonData.frame.getHeight()-CommonData.HEIGHTFRAMEDIFFERENCE;
					CommonData.panel.setSize(CommonData.WIDTH, CommonData.HEIGHT);
					CommonData.renderer.updateWindow(true);
					CommonData.renderer.createCanvasComponents(true);
					SchematicManager.resize();
					//CommonData.frame.pack();
				}
				
				loading=false;
			}
		});
	}
}

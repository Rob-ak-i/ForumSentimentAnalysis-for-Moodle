import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import common.CommonData;
import common.EntityEditorMouseListener;
import parts.PartBinaryBasic;
import parts.PartNode;
import util.Colors;

public class GUIHelper {
	public static void toolbarButtonsAdd(JToolBar toolbar) {

		JButton arrowbutton = new  JButton(new  ImageIcon(CommonData.dataSubDir+"arrow.png"));
		//arrowbutton.setBounds(15, 0, 25, 24);
		arrowbutton.addActionListener(new  ActionListener() {
			public void actionPerformed(ActionEvent event) { 
				EntityEditorMouseListener.mode=EntityEditorMouseListener.MODE_OBSERVE;
			}
		});
		toolbar.add(arrowbutton);
		JButton selectbutton = new  JButton(new  ImageIcon(CommonData.dataSubDir+"selector.png"));
		selectbutton.setBounds(40, 0, 25, 24);
		selectbutton.addActionListener(new  ActionListener() {
			public void actionPerformed(ActionEvent event) { 
				EntityEditorMouseListener.mode=EntityEditorMouseListener.MODE_SELECT;
			}
		});
		toolbar.add(selectbutton);
		

		/*
		JButton textbutton = new  JButton(new  ImageIcon(CommonData.dataSubDir+"text.png"));
		//textbutton.setBounds(90, 0, 25, 24);
		textbutton.addActionListener(new  ActionListener() {
			public void actionPerformed(ActionEvent event) { 
				EntityEditor_Helper.mode=EntityEditor_Helper.MODE_PRINT_TEXT;
			}
		});
		toolbar.add(textbutton);
		*/
		JButton linebutton = new  JButton(new  ImageIcon(CommonData.dataSubDir+"line.png"));
		//linebutton.setBounds(115, 0, 25, 24);
		linebutton.addActionListener(new  ActionListener() {
			public void actionPerformed(ActionEvent event) { 
				EntityEditorMouseListener.mode=EntityEditorMouseListener.MODE_DRAW_LINE;
		      }
		});
		toolbar.add(linebutton);


		JButton buttonBrushAddNode = new  JButton(new  ImageIcon(CommonData.dataSubDir+"button_brush_addnode.png"));
		//buttonBrushAddNode.setBounds(190, 0, 25, 24);
		buttonBrushAddNode.addActionListener(new  ActionListener() {
			public void actionPerformed(ActionEvent event) { 
				EntityEditorMouseListener.mode=EntityEditorMouseListener.MODE_SETUP_NODE;
			}
		});
		toolbar.add(buttonBrushAddNode);

		JButton buttonBrushAddPart = new  JButton(new  ImageIcon(CommonData.dataSubDir+"button_brush_addpart.png"));
		buttonBrushAddPart.setHorizontalAlignment(SwingConstants.LEFT );
		//buttonBrushAddPart.setBounds(215, 0, 25, 24);
		buttonBrushAddPart.addActionListener(new  ActionListener() {
			public void actionPerformed(ActionEvent event) { 
				EntityEditorMouseListener.mode=EntityEditorMouseListener.MODE_SETUP_PART_STAGE1_selectNodeFrom;
			}
		});
		toolbar.add(buttonBrushAddPart);
		EntityEditorMouseListener.partsList = new JComboBox(PartBinaryBasic.partsList);
		//partsList.setBounds(240, 0, 100, 24);
		toolbar.add(EntityEditorMouseListener.partsList);
		
	}

	public static void toolbar_PartsButtonsAdd(JToolBar toolbar) {

		/**�������������� ����� �������*/
		CommonData.nameedit = new JEditorPane();
		toolbar.add(CommonData.nameedit);
		/**������ �������� �������������� ����� �������*/
		JButton buttonApplyName = new  JButton(new  ImageIcon(CommonData.dataSubDir+"okay.png"));
		//penbutton.setBounds(15, 360, 25, 24);
		buttonApplyName.addActionListener(new  ActionListener() {
			public void actionPerformed(ActionEvent event) { 
				if(CommonData.partSelected==null)return;
				CommonData.partSelected.partName=CommonData.nameedit.getText();
				//CommonData.scheme.redrawAllOnGraph();
			}
		});
		toolbar.add(buttonApplyName);
		
		
		/**�������������� �������� �������*/
		CommonData.captionedit = new JEditorPane();
		toolbar.add(CommonData.captionedit);

		/**������ �������� �������������� �������� �������*/
		JButton buttonApplyCaption = new  JButton(new  ImageIcon(CommonData.dataSubDir+"okay.png"));
		//penbutton.setBounds(15, 360, 25, 24);
		buttonApplyCaption.addActionListener(new  ActionListener() {
			public void actionPerformed(ActionEvent event) { 
				if(CommonData.partSelected==null)return;
				CommonData.partSelected.setCaption(Double.valueOf(CommonData.captionedit.getText()));
			}
		});
		toolbar.add(buttonApplyCaption);
		CommonData.nameedit.setText(common.Lang.InnerTable.Dialog.GUIHelper_nameedit_defaultCaption);
		CommonData.captionedit.setText(common.Lang.InnerTable.Dialog.GUIHelper_captionedit_defaultCaption);
		
		
		
		/**������ ��������� �������*/
		JButton buttonSwapPart = new  JButton(new  ImageIcon(CommonData.dataSubDir+"swap.png"));
		//penbutton.setBounds(15, 360, 25, 24);
		buttonSwapPart.addActionListener(new  ActionListener() {
			public void actionPerformed(ActionEvent event) { 
				if(CommonData.partSelected==null)return;
				if(CommonData.partSelected.getClass().getSimpleName().compareTo(PartNode.class.getSimpleName())==0)return;
				CommonData.partSelected.selected=0;
				((PartBinaryBasic)CommonData.partSelected).changeDirection();
				CommonData.partSelected.selected=1;
				System.out.println(common.Lang.InnerTable.Console.in_test+" Procedure:"+"buttonSwapPart.actionPerformed");
			}
		});
		toolbar.add(buttonSwapPart);

		/**������ ����������� �������*/
		JButton buttonMovePart = new  JButton(new  ImageIcon(CommonData.dataSubDir+"move.png"));
		//penbutton.setBounds(15, 360, 25, 24);
		buttonMovePart.addActionListener(new  ActionListener() {
			public void actionPerformed(ActionEvent event) { 
				if(CommonData.partSelected==null)return;
				EntityEditorMouseListener.mode=EntityEditorMouseListener.MODE_MOVE;
			}
		});
		toolbar.add(buttonMovePart);
		
		/**������ �������� �������*/
		JButton buttonRemovePart = new  JButton(new  ImageIcon(CommonData.dataSubDir+"remove.png"));
		//penbutton.setBounds(15, 360, 25, 24);
		buttonRemovePart.addActionListener(new  ActionListener() {
			public void actionPerformed(ActionEvent event) { 
				if(CommonData.partSelected==null)return;
				CommonData.scheme.removeElement(CommonData.partSelected, true, true, true);
				CommonData.partSelected=null;
			}
		});
		toolbar.add(buttonRemovePart);

	}
}
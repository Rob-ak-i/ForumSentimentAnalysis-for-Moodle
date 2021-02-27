package gui;
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
import common.Lang;
import parts.PartBasic;
import parts.PartBinaryBasic;
import parts.PartNode;
import util.Colors;

public class GUIHelper {
	public static JButton arrowbutton;
	public static JButton selectbutton;
	public static JButton buttonBrushAddNode;
	public static JButton buttonBrushAddPart;
	
	public static JButton buttonMovePart;
	public static JButton SelectedButton=null;
	public static JButton PreviousSelectedButton=null;
	public static void selectButton(JButton newSelectedButton) {
		GUIHelper.PreviousSelectedButton=GUIHelper.SelectedButton;
		GUIHelper.SelectedButton=newSelectedButton;
		GUIHelper.SelectedButton.setSelected(true);
		if(GUIHelper.PreviousSelectedButton!=null)GUIHelper.PreviousSelectedButton.setSelected(false);
	}
	public static void toolbarButtonsAdd(JToolBar toolbar) {

		arrowbutton = new  JButton(new  ImageIcon(CommonData.dataSubDir+"arrow.png"));
		arrowbutton.setSelectedIcon(new ImageIcon(CommonData.dataSubDir+"arrow_ok.png"));
		arrowbutton.setToolTipText(Lang.InnerTable.Tooltip.arrowbutton);
		//arrowbutton.setBounds(15, 0, 25, 24);
		arrowbutton.addActionListener(new  ActionListener() {
			public void actionPerformed(ActionEvent event) { 
				EntityEditorMouseListener.setMode(EntityEditorMouseListener.MODE_OBSERVE);
			}
		});
		toolbar.add(arrowbutton);
		selectbutton = new  JButton(new  ImageIcon(CommonData.dataSubDir+"selector.png"));
		selectbutton.setSelectedIcon(new ImageIcon(CommonData.dataSubDir+"selector_ok.png"));
		selectbutton.setToolTipText(Lang.InnerTable.Tooltip.selectbutton);
		selectbutton.setBounds(40, 0, 25, 24);
		selectbutton.addActionListener(new  ActionListener() {
			public void actionPerformed(ActionEvent event) { 
				EntityEditorMouseListener.setMode(EntityEditorMouseListener.MODE_SELECT);
			}
		});
		toolbar.add(selectbutton);
		
		buttonBrushAddNode = new  JButton(new  ImageIcon(CommonData.dataSubDir+"button_brush_addnode.png"));
		buttonBrushAddNode.setSelectedIcon(new ImageIcon(CommonData.dataSubDir+"button_brush_addnode_ok.png"));
		buttonBrushAddNode.setToolTipText(Lang.InnerTable.Tooltip.buttonBrushAddNode);
		//buttonBrushAddNode.setBounds(190, 0, 25, 24);
		buttonBrushAddNode.addActionListener(new  ActionListener() {
			public void actionPerformed(ActionEvent event) { 
				EntityEditorMouseListener.setMode(EntityEditorMouseListener.MODE_SETUP_NODE);
			}
		});
		toolbar.add(buttonBrushAddNode);

		buttonBrushAddPart = new  JButton(new  ImageIcon(CommonData.dataSubDir+"button_brush_addpart.png"));
		buttonBrushAddPart.setSelectedIcon(new ImageIcon(CommonData.dataSubDir+"button_brush_addpart_ok.png"));
		buttonBrushAddPart.setToolTipText(Lang.InnerTable.Tooltip.buttonBrushAddPart);
		buttonBrushAddPart.setHorizontalAlignment(SwingConstants.LEFT );
		//buttonBrushAddPart.setBounds(215, 0, 25, 24);
		buttonBrushAddPart.addActionListener(new  ActionListener() {
			public void actionPerformed(ActionEvent event) { 
				EntityEditorMouseListener.setMode(EntityEditorMouseListener.MODE_SETUP_PART_STAGE1_selectNodeFrom);
			}
		});
		toolbar.add(buttonBrushAddPart);
		EntityEditorMouseListener.partsList = new JComboBox(PartBinaryBasic.partsList);
		EntityEditorMouseListener.partsList.setToolTipText(Lang.InnerTable.Tooltip.partsListComboBox);
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
		buttonApplyName.setToolTipText(Lang.InnerTable.Tooltip.buttonApplyName);
		buttonApplyName.addActionListener(new  ActionListener() {
			public void actionPerformed(ActionEvent event) { 
				if(CommonData.partsSelected.size()==0)return;
				for(int i=0;i<CommonData.partsSelected.size();++i)
					CommonData.partsSelected.get(i).partName=CommonData.nameedit.getText();
					
				//CommonData.scheme.redrawAllOnGraph();
			}
		});
		toolbar.add(buttonApplyName);
		
		
		/**�������������� �������� �������*/
		CommonData.captionedit = new JEditorPane();
		toolbar.add(CommonData.captionedit);

		/**������ �������� �������������� �������� �������*/
		JButton buttonApplyCaption = new  JButton(new  ImageIcon(CommonData.dataSubDir+"okay.png"));
		buttonApplyCaption.setToolTipText(Lang.InnerTable.Tooltip.buttonApplyCaption);
		//penbutton.setBounds(15, 360, 25, 24);
		buttonApplyCaption.addActionListener(new  ActionListener() {
			public void actionPerformed(ActionEvent event) { 
				if(CommonData.partsSelected.size()==0)return;
				for(int i=0;i<CommonData.partsSelected.size();++i)
					CommonData.partsSelected.get(i).setCaption(CommonData.captionedit.getText());
			}
		});
		toolbar.add(buttonApplyCaption);
		CommonData.nameedit.setText(common.Lang.InnerTable.Dialog.GUIHelper_nameedit_defaultCaption);
		CommonData.captionedit.setText(common.Lang.InnerTable.Dialog.GUIHelper_captionedit_defaultCaption);
		
		
		
		/**������ ��������� �������*/
		JButton buttonSwapPart = new  JButton(new  ImageIcon(CommonData.dataSubDir+"swap.png"));
		buttonSwapPart.setToolTipText(Lang.InnerTable.Tooltip.buttonSwapPart);
		//penbutton.setBounds(15, 360, 25, 24);
		buttonSwapPart.addActionListener(new  ActionListener() {
			public void actionPerformed(ActionEvent event) { 
				PartBasic partSelected=null;
				if(CommonData.partsSelected.size()==0)return;
				for(int i=0;i<CommonData.partsSelected.size();++i) {
					partSelected=CommonData.partsSelected.get(i);
					if(partSelected.getClass().getSimpleName().compareTo(PartNode.class.getSimpleName())==0)continue;
					((PartBinaryBasic)partSelected).changeDirection();
				}
			}
		});
		toolbar.add(buttonSwapPart);

		/**������ ����������� �������*/
		buttonMovePart = new  JButton(new  ImageIcon(CommonData.dataSubDir+"move.png"));
		buttonMovePart.setSelectedIcon(new ImageIcon(CommonData.dataSubDir+"move_ok.png"));
		buttonMovePart.setToolTipText(Lang.InnerTable.Tooltip.buttonMovePart);
		//penbutton.setBounds(15, 360, 25, 24);
		buttonMovePart.addActionListener(new  ActionListener() {
			public void actionPerformed(ActionEvent event) {
				EntityEditorMouseListener.setMode(EntityEditorMouseListener.MODE_MOVE);
			}
		});
		toolbar.add(buttonMovePart);
		
		/**������ �������� �������*/
		JButton buttonRemovePart = new  JButton(new  ImageIcon(CommonData.dataSubDir+"remove.png"));
		buttonRemovePart.setToolTipText(Lang.InnerTable.Tooltip.buttonRemovePart);
		//penbutton.setBounds(15, 360, 25, 24);
		buttonRemovePart.addActionListener(new  ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if(CommonData.partsSelected.size()==0)return;
				if(CommonData.partsSelected.size()==1) {
					CommonData.scheme.removeElement(CommonData.partsSelected.get(0), true);
					CommonData.partsSelected.clear();
				}else
					CommonData.scheme.removeElements(CommonData.partsSelected, true);
				if(CommonData.partsSelected.size()!=0)System.out.println("fixMe, groupDeletion failed");
			}
		});
		toolbar.add(buttonRemovePart);

	}
}
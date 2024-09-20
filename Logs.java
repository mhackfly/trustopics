/*
 * Projet : Trustopics
 * Version : 0.2.1
 * Fichier : Logs.java
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Logs {
	
	private static String glb_lastText = "";
	private static String glb_lastTab = "";
	private static JTextArea glb_textArea;
	private static JFrame glb_frame;
	private RoundButton glb_roundButtonOk;
	private RoundViewPort glb_roundViewPort;
	
	public Logs() {}
	
	public void CreateWin() {
		glb_frame = new JFrame("Logs");
		MouseActions lcl_mouseActions = new MouseActions();
		glb_frame.setBackground(Defines.UIcolBackGDrawing);
		glb_frame.getContentPane().setLayout(new BorderLayout());
		glb_frame.setSize(750,500);
		glb_frame.setLocationRelativeTo(null);
		glb_textArea = new JTextArea();
		glb_textArea.setEditable(false);
		glb_textArea.setBackground(Defines.UIcolBackGDrawing);
		glb_textArea.setForeground(Color.ORANGE);
		glb_textArea.setTabSize(4);
		glb_textArea.setFont(new Font("monospaced",1,10));
		glb_roundViewPort = new RoundViewPort(glb_textArea);
		glb_frame.getContentPane().add(glb_roundViewPort,BorderLayout.CENTER);
		JPanelBottom lcl_bottomPanel = new JPanelBottom(lcl_mouseActions);
		lcl_bottomPanel.setLayout(null);
		lcl_bottomPanel.setBackground(Defines.UIcolForeGDrawing);
		lcl_bottomPanel.setPreferredSize(new Dimension(1,40));
		glb_frame.getContentPane().add(lcl_bottomPanel,BorderLayout.SOUTH);
		glb_frame.addWindowListener(new WindowAdapter() { 
			public void windowClosing(WindowEvent evt) { glb_frame.dispose(); }
		});
	}

	public void OpenWin() {
		glb_frame.setVisible(true);
	}
	
	public void HideWin() {
		glb_frame.setVisible(false);
	}
	
	public void Clear() {
		glb_textArea.setText("");
	}
	
	public void Add(String prm_text) {
		glb_lastText = prm_text;
		glb_textArea.append(glb_lastTab + prm_text + "\n");
	}
	
	public void AddTab(int prm_nbTab) {
		glb_lastTab = "";
		for (int i = 0 ; i < prm_nbTab ; i++) glb_lastTab += "\t";
	}
	
	public void AddLine(String lcl_char) {
		String lcl_line = "";
		for (int i = 0 ; i < glb_lastText.length() ; i++) lcl_line += lcl_char;
		glb_textArea.append(glb_lastTab + lcl_line + "\n");
	}
	
	public void AddEmpty() {
		glb_textArea.append("\n");
	}
	
	public class JPanelBottom extends JPanel {
		public JPanelBottom(MouseActions prm_mouseActions) {
			glb_roundButtonOk = new RoundButton(" Ok ");
			glb_roundButtonOk.setBackground(Defines.UIcolBackGDrawing);
			glb_roundButtonOk.setActionCommand("ok");
			glb_roundButtonOk.addActionListener(prm_mouseActions);
			add(glb_roundButtonOk);
		}
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setPaint(Defines.UIcolBackGDrawing);
			g2.fillRoundRect(200,0,getWidth()-400,getHeight(),getHeight(),getHeight());
			int lcl_wOk = glb_roundButtonOk.GetWidthText();
			int lcl_hOk = glb_roundButtonOk.GetHeightText();
			glb_roundButtonOk.setBounds((getWidth()/2)-(lcl_wOk/2),(getHeight()/2)-(lcl_hOk/2),lcl_wOk,lcl_hOk);
		}
	}
	
	public class MouseActions implements ActionListener {
		public void actionPerformed(ActionEvent prm_ev) {
			String lcl_cmd = prm_ev.getActionCommand();
			if (lcl_cmd.equals("ok")) {
				glb_roundButtonOk.ClearButton();
				glb_frame.dispose();
			}
		}
	}
}

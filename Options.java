/*
 * Projet : Trustopics
 * Version : 0.2.1
 * Fichier : Options.java
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Options {

	private static JFrame glb_frame;
	private RoundButton glb_roundButtonOk;
	private RoundTextField glb_roundTextFieldBrowser;
	
	public Options() {}
	
	public void CreateWin() {
		glb_frame = new JFrame("Options");
		MouseActions lcl_mouseActions = new MouseActions();
		glb_frame.getContentPane().setLayout(new BorderLayout());
		glb_frame.setSize(350,200);
		glb_frame.setLocationRelativeTo(null);
		JPanelCenter lcl_CenterPanel = new JPanelCenter();
		lcl_CenterPanel.setLayout(null);
		lcl_CenterPanel.setBackground(Defines.UIcolBackGDrawing);
		glb_frame.getContentPane().add(lcl_CenterPanel,BorderLayout.CENTER);
		JPanelBottom lcl_bottomPanel = new JPanelBottom(lcl_mouseActions);
		lcl_bottomPanel.setLayout(null);
		lcl_bottomPanel.setBackground(Defines.UIcolForeGDrawing);
		lcl_bottomPanel.setPreferredSize(new Dimension(1,40));
		glb_frame.getContentPane().add(lcl_bottomPanel,BorderLayout.SOUTH);
		glb_frame.addWindowListener(new WindowAdapter() { 
			public void windowClosing(WindowEvent evt) {
				SaveBrowser();
				glb_frame.dispose();
			}
		});
	}
	
	public void OpenWin() {
		glb_frame.setVisible(true);
	}
	
	public void HideWin() {
		glb_frame.setVisible(false);
	}
	
	public class MouseActions implements ActionListener {
		public void actionPerformed(ActionEvent prm_ev) {
			String lcl_cmd = prm_ev.getActionCommand();
			if (lcl_cmd.equals("ok")) {
				glb_roundButtonOk.ClearButton();
				SaveBrowser();
				glb_frame.dispose();
			}
		}
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
			g2.fillRoundRect(100,0,getWidth()-200,getHeight(),getHeight(),getHeight());
			int lcl_wOk = glb_roundButtonOk.GetWidthText();
			int lcl_hOk = glb_roundButtonOk.GetHeightText();
			glb_roundButtonOk.setBounds((getWidth()/2)-(lcl_wOk/2),(getHeight()/2)-(lcl_hOk/2),lcl_wOk,lcl_hOk);
		}
	}
	
	public class JPanelCenter extends JPanel {
		int glb_w,glb_h;
		public JPanelCenter() {
			glb_roundTextFieldBrowser = new RoundTextField("Navigateur >",new ConfigFile().GetValue("browser"),100);
			add(glb_roundTextFieldBrowser);
		}
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			glb_w = glb_roundTextFieldBrowser.GetWidthText();
			glb_h = glb_roundTextFieldBrowser.GetHeightText();
			glb_roundTextFieldBrowser.setBounds((getWidth()-glb_w)/2,20,glb_w,glb_h);
		}
	}
	
	private void SaveBrowser() {
		try { 
			ConfigFile lcl_cf = new ConfigFile();
			String lcl_browser = glb_roundTextFieldBrowser.GetText();
			if (lcl_browser.equals("")) {
				glb_roundTextFieldBrowser.SetText("aucun");
				lcl_cf.SetValue("browser","aucun");
			}
			else {
				lcl_cf.SetValue("browser",glb_roundTextFieldBrowser.GetText());
			}
			lcl_cf.Save();
		}
		catch (TrustException prm_ev) { prm_ev.Action(); }
	}
}

/*
 * Projet : Trustopics
 * Version : 0.2.1
 * Fichier : TrustException.java
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

public class TrustException extends Exception {
	
	private String glb_error;
	private boolean glb_exit;
	private JTextPane glb_textPane;
	private JDialog glb_dialog;
	
	public TrustException(String prm_error) {
		glb_error = prm_error;
		glb_exit = false;
	}

	public TrustException(String prm_error,boolean prm_exit) {
		glb_error = prm_error;
		glb_exit = prm_exit;
	}
	
	public void Action() {
		new WebWaitExtra().Stop();
		JFrame lcl_frame = new JFrame();
		glb_dialog = new JDialog(lcl_frame,"Message",true);
		glb_dialog.getContentPane().setLayout(new BorderLayout());
		glb_dialog.setSize(400,200);
		glb_dialog.setResizable(false);
		glb_dialog.setLocationRelativeTo(null);
		glb_dialog.addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent evt) { glb_dialog.dispose(); }});
		MouseActions lcl_mouseActions = new MouseActions();
		JPanelCenter lcl_centerPanel = new JPanelCenter();
		lcl_centerPanel.setBackground(Defines.UIcolBackGDrawing);
		glb_dialog.getContentPane().add(lcl_centerPanel,BorderLayout.CENTER);
		JPanelBottom lcl_bottomPanel = new JPanelBottom(lcl_mouseActions);
		lcl_bottomPanel.setLayout(null);
		lcl_bottomPanel.setBackground(Defines.UIcolForeGDrawing);
		lcl_bottomPanel.setPreferredSize(new Dimension(1,40));
		glb_dialog.getContentPane().add(lcl_bottomPanel,BorderLayout.SOUTH);
		glb_dialog.setVisible(true);
	}
	
	public String GetMessage() { return(glb_error); }

	public class JPanelCenter extends JPanel {
		public JPanelCenter() {
			HTMLEditorKit kit = new HTMLEditorKit();
			HTMLDocument doc = (HTMLDocument) (kit.createDefaultDocument());
			glb_textPane = new JTextPane();
			glb_textPane.addMouseListener(new MouseAdapter() {
				public void mouseExited(MouseEvent evt) {
				}
			});
			glb_textPane.setEditable(false);
			glb_textPane.setEditorKit(kit);
			glb_textPane.setDocument(doc);
			glb_textPane.setContentType("text/html");
			glb_textPane.setBackground(Defines.UIcolBackGDrawing);
			glb_textPane.setText(GetText());
			add(glb_textPane);
		}
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			glb_textPane.setBounds(0,0,getWidth(),getHeight());
		}
	}

	public String GetText() {
		String lcl_text;
		lcl_text = "<html><font face=\"Dialog\" color=\"#ffffff\" size=2><body><center>";
		lcl_text += "<br><b>" + glb_error + "</b><br>";
		lcl_text += "</center>";
		lcl_text += "</body></html>";
		return (lcl_text);
	}
	
	public class JPanelBottom extends JPanel {
		RoundButton glb_roundButtonContinuer;
		RoundButton glb_roundButtonFermer;
		public JPanelBottom(MouseActions prm_mouseActions) {
			glb_roundButtonContinuer = new RoundButton(" Continuer ");
			glb_roundButtonContinuer.setBackground(Defines.UIcolBackGDrawing);
			glb_roundButtonContinuer.setActionCommand("Continuer");
			glb_roundButtonContinuer.addActionListener(prm_mouseActions);
			add(glb_roundButtonContinuer);
			glb_roundButtonFermer = new RoundButton(" Fermer l'application ");
			glb_roundButtonFermer.setBackground(Defines.UIcolBackGDrawing);
			glb_roundButtonFermer.setActionCommand("Fermer");
			glb_roundButtonFermer.addActionListener(prm_mouseActions);
			add(glb_roundButtonFermer);
		}
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			int lcl_wContinuer = glb_roundButtonContinuer.GetWidthText();
			int lcl_hContinuer = glb_roundButtonContinuer.GetHeightText();
			int lcl_wFermer = glb_roundButtonFermer.GetWidthText();
			int lcl_hFermer = glb_roundButtonFermer.GetHeightText();
			int lcl_decalage = (getHeight() - (lcl_hContinuer + lcl_hFermer)) / 3;
			if (!glb_exit) {
				glb_roundButtonContinuer.setBounds((getWidth()/2)-(lcl_wContinuer/2),lcl_decalage,lcl_wContinuer,lcl_hContinuer);
				glb_roundButtonFermer.setBounds((getWidth()/2)-(lcl_wFermer/2),getHeight() - (lcl_decalage + lcl_hFermer),lcl_wFermer,lcl_hFermer);
			}
			else {
				glb_roundButtonFermer.setBounds((getWidth()/2)-(lcl_wFermer/2),(getHeight() - lcl_hFermer) / 2,lcl_wFermer,lcl_hFermer);
			}
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setPaint(Defines.UIcolBackGDrawing);
			g2.fillRoundRect(50,0,getWidth()-100,getHeight(),getHeight(),getHeight());
		}
	}
	
	public class MouseActions implements ActionListener {
		public void actionPerformed(ActionEvent prm_ev) {
			String lcl_cmd = prm_ev.getActionCommand();
			if (lcl_cmd.equals("Continuer")) { glb_dialog.dispose(); }
			if (lcl_cmd.equals("Fermer")) { glb_dialog.dispose(); System.exit(1); }
		}
	}
}

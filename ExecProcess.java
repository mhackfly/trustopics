/*
 * Projet : Trustopics
 * Version : 0.2.1
 * Fichier : ExecProcess.java
 */

public class ExecProcess extends Thread {
	
	private String glb_url;

	public ExecProcess(String prm_url) {
		glb_url = prm_url;
		start();
	}
	
	public void run() {
		Runtime lcl_rt = Runtime.getRuntime();
		Process lcl_pc = null;
		try {
			ConfigFile lcl_cf = new ConfigFile();
			String lcl_browser = lcl_cf.GetValue("browser");
			if (lcl_browser.equals("aucun")) {
				new TrustException("Le navigateur n'a pas été défini.").Action();
			}
			else {
				lcl_pc = lcl_rt.exec(lcl_browser + " " + glb_url);
			}
		}
		catch (Exception prm_except) {
			new TrustException("L'exécution du navigateur a échoué.").Action();
		}
	}
}

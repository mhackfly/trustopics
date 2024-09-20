/*
 * Projet : Trustopics
 * Version : 0.2.1
 * Fichier : WebWaitExtra.java
 */

public class WebWaitExtra {
	
	public WebWaitExtra() {}
	
	public void Start() {
		Defines.connectionIsOk = false;
		new WebWait();
	}
	
	public void Stop() {
		Defines.connectionIsOk = true;
	}
}

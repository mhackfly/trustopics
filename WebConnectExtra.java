/*
 * Projet : Trustopics
 * Version : 0.2.1
 * Fichier : WebConnectExtra.java
 */

public class WebConnectExtra {
	
	public WebConnectExtra() {
	}

	public String GetUrlTopicPage(int prm_categorie,int prm_topic) throws TrustException {
		boolean lcl_urlFound = false;
		TrustDatas lcl_trustDatas = new TrustDatas();
		WebParser lcl_webFiles = new WebParser();
		String lcl_topicValue;
		String lcl_urlPage;
		String lcl_url = lcl_trustDatas.GetUrlTopicsPage();
		for (int i = 0 ; i < (Defines.MaxPage + 1) ; i++) {
			lcl_urlPage = lcl_webFiles.SetParameter(lcl_trustDatas.GetTopicsFile(),lcl_trustDatas.GetKeyCategorie(),String.valueOf(prm_categorie));
			if (lcl_webFiles.LoadPage(lcl_webFiles.SetParameter(lcl_urlPage,lcl_trustDatas.GetKeyPage(),String.valueOf(i + 1))))
				throw new TrustException("Problème de lecture d'une page de topics (cat=" + prm_categorie + ";page=" + (i + 1) + ").");
			if (lcl_webFiles.SeekUrlLines(lcl_trustDatas.GetDatUrlTopicsPage(1),lcl_trustDatas.UrlKeys(lcl_trustDatas.GetDatUrlTopicsPage(1))))
				throw new TrustException("Aucune ligne de type URL n'a été trouvée.");
			for (int j = 0 ; j < lcl_webFiles.GetResultsPage().size() ; j++) {
				lcl_topicValue = lcl_webFiles.GetParameter(lcl_webFiles.GetResultLine(j),lcl_trustDatas.GetKeyTopic());
				if (lcl_topicValue.equals(String.valueOf(prm_topic)) && !lcl_urlFound) {
					lcl_url = lcl_webFiles.SetParameter(lcl_url,lcl_trustDatas.GetKeyCategorie(),String.valueOf(prm_categorie));
					lcl_url = lcl_webFiles.SetParameter(lcl_url,lcl_trustDatas.GetKeyTopic(),lcl_topicValue);
					lcl_url = lcl_webFiles.SetParameter(lcl_url,lcl_trustDatas.GetKeyPage(),lcl_webFiles.GetParameter(lcl_webFiles.GetResultLine(j),lcl_trustDatas.GetKeyPage()));
					lcl_urlFound = true;
				}
			}
			if (lcl_urlFound) break;
		}
		
		if (!lcl_urlFound) lcl_url = "";
		return(lcl_url);
	}
}

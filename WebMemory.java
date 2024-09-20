/*
 * Projet : Trustopics
 * Version : 0.2.1
 * Fichier : WebMemory.java
 */

public class WebMemory {
	
	private static String glb_categories[];
	private static String glb_topicsMessages[];
	private static String glb_topicsValues[];
	private static String glb_topicsAnswers[];
	private static String glb_topicsViews[];
	private static String glb_topicsAuthors[];
	private static String glb_topicsAuthorsAnswers[];
	
	public WebMemory() {
	}
	
	public void UpdateCategories() {
		try { glb_categories = new WebConnect().GetCategories(); }
		catch (TrustException prm_ev) { prm_ev.Action(); }
	}

	public String GetCategories(int prm_indice) {
		if (glb_categories == null) {
			new TrustException("-= Null Pointer Exception =-",true).Action();
			System.exit(1);
		}
		return(glb_categories[prm_indice]);
	}
	
	public int GetCategoriesSize() {
		if (glb_categories == null) {
			new TrustException("-= Null Pointer Exception =-",true).Action();
			System.exit(1);
		}
		return(glb_categories.length);
	}
	
	public void PrintCategories() {
		for (int i = 0 ; i < GetCategoriesSize() ; i++)
			System.out.println(GetCategories(i));
	}
	
	public void UpdateTopics(int prm_categories,int prm_page) {
		try { 
			WebConnect lcl_trustConnect = new WebConnect();
			lcl_trustConnect.GetTopicsFile(prm_categories,prm_page);
			glb_topicsMessages = lcl_trustConnect.GetTopicsMessages();
			glb_topicsValues = lcl_trustConnect.GetTopicsValues();
			glb_topicsAnswers = lcl_trustConnect.GetTopicsAnswers();
			glb_topicsViews = lcl_trustConnect.GetTopicsViews();
			glb_topicsAuthors = lcl_trustConnect.GetTopicsAuthors();
			glb_topicsAuthorsAnswers = lcl_trustConnect.GetTopicsAnswersAuthors();
		}
		catch (TrustException prm_ev) { prm_ev.Action(); }
	}
	
	public String GetTopicMessage(int prm_indice) { return(glb_topicsMessages[prm_indice]); }
	public int GetTopicsMessagesSize() { return(glb_topicsMessages.length); }
	public void PrintTopicsMessages() {
		for (int i = 0 ; i < GetTopicsMessagesSize() ; i++)
			System.out.println(GetTopicMessage(i));
	}
	
	public String GetTopicValue(int prm_indice) { return(glb_topicsValues[prm_indice]); }
	public int GetTopicsValuesSize() { return(glb_topicsValues.length); }
	public void PrintTopicsValues() {
		for (int i = 0 ; i < GetTopicsValuesSize() ; i++)
			System.out.println(GetTopicValue(i));
	}
	
	public String GetTopicAnswer(int prm_indice) { return(glb_topicsAnswers[prm_indice]); }
	public int GetTopicsAnswersSize() { return(glb_topicsAnswers.length); }
	public void PrintTopicsAnswers() {
		for (int i = 0 ; i < GetTopicsAnswersSize() ; i++)
			System.out.println(GetTopicAnswer(i));
	}
	
	public String GetTopicView(int prm_indice) { return(glb_topicsViews[prm_indice]); }
	public int GetTopicsViewsSize() { return(glb_topicsViews.length); }
	public void PrintTopicsViews() {
		for (int i = 0 ; i < GetTopicsViewsSize() ; i++)
			System.out.println(GetTopicView(i));
	}

	public String GetTopicAuthor(int prm_indice) { return(glb_topicsAuthors[prm_indice]); }
	public int GetTopicsAuthorsSize() { return(glb_topicsAuthors.length); }
	public void PrintTopicsAuthors() {
		for (int i = 0 ; i < GetTopicsAuthorsSize() ; i++)
			System.out.println(GetTopicAuthor(i));
	}

	public String GetTopicAuthorAnswer(int prm_indice) { return(glb_topicsAuthorsAnswers[prm_indice]); }
	public int GetTopicsAuthorsAnswerSize() { return(glb_topicsAuthorsAnswers.length); }
	public void PrintTopicsAuthorsAnswers() {
		for (int i = 0 ; i < GetTopicsAuthorsAnswerSize() ; i++)
			System.out.println(GetTopicAuthorAnswer(i));
	}
}

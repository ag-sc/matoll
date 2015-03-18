import de.citec.sc.matoll.utils.StanfordLemmatizer;


public class TestLemmatizer {

	public static void main(String[] args) throws Exception {
		StanfordLemmatizer sl = new StanfordLemmatizer("de");

		System.out.println(sl.getLemma("casado"));
	}
}

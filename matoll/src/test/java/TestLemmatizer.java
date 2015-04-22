import de.citec.sc.matoll.utils.StanfordLemmatizer;


public class TestLemmatizer {

	public static void main(String[] args) throws Exception {
		StanfordLemmatizer sl = new StanfordLemmatizer("en");

		System.out.println(sl.getLemma("returned"));
	}
}

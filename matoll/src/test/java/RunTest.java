/**
 *
 * @author cunger
 */
public class RunTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        CoreferenceTest coreftest = new CoreferenceTest("en");
        
        coreftest.loadTestFile("coref/coref_en_ex1.ttl");
        coreftest.run();
        
        coreftest.loadTestFile("coref/coref_en_ex2.ttl");
        coreftest.run();
        
    }
    
}

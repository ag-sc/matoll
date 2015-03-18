import java.io.File;

import de.tudarmstadt.ukp.jwktl.JWKTL;
import de.tudarmstadt.ukp.jwktl.api.IQuotation;
import de.tudarmstadt.ukp.jwktl.api.IWikiString;
import de.tudarmstadt.ukp.jwktl.api.IWiktionaryEdition;
import de.tudarmstadt.ukp.jwktl.api.IWiktionaryEntry;
import de.tudarmstadt.ukp.jwktl.api.IWiktionaryPage;
import de.tudarmstadt.ukp.jwktl.api.IWiktionaryRelation;
import de.tudarmstadt.ukp.jwktl.api.IWiktionarySense;
import de.tudarmstadt.ukp.jwktl.api.IWiktionaryWordForm;
import de.tudarmstadt.ukp.jwktl.api.RelationType;


public class TestWiktionary {
	private static final String PATH_TO_DUMP_FILE = "/Users/swalter/Downloads/";
	private static final String TARGET_DIRECTORY = "/Users/swalter/Desktop/Wiktionary/English";
	private static final boolean OVERWRITE_EXISTING_FILES = true;


	public static void main(String[] args) throws Exception {
		/*
		 * Create Database
		 */
	   /* File dumpFile = new File(PATH_TO_DUMP_FILE);
	    File outputDirectory = new File(TARGET_DIRECTORY);
	    boolean overwriteExisting = OVERWRITE_EXISTING_FILES;
	      
	    JWKTL.parseWiktionaryDump(dumpFile, outputDirectory, overwriteExisting);*/
		
		
		
		IWiktionaryEdition wkt = JWKTL.openEdition(new File(TARGET_DIRECTORY));
		  
		 for (IWiktionaryEntry entry : wkt.getEntriesForWord("wife",true)){
			 String etymology = "";
			String word = entry.getWord(); 
			String part_of_speech = entry.getPartOfSpeech().toString();
			/*for(IQuotation sense:entry.getQuotations()){
				System.out.println(sense.getSource());
			}*/
			String language =  entry.getWordLanguage().getISO639_1().toString();
			try{
				etymology = entry.getWordEtymology().toString();
			}
			catch(Exception e){}
			String entry_header = entry.getHeader().toString();
				        ;
			 
			
			System.out.println("  " + word 
				        + "/" + part_of_speech 
				        + "/" + language
				        + "/" + etymology
				        + "/" + entry_header)
				        ;
			 
			
			 
			 /*try{
				 for(IWiktionaryWordForm x:entry.getWordForms()){
						System.out.println("  "+x.getWordForm()
								+"/"+x.getAspect()
								+"/"+x.getCase()
								+"/"+x.getTense());
					}
				 
			 }
			 catch (Exception e){
				 e.printStackTrace();
			 }*/
			 System.out.println("-----");
			
		 }
		 

	  }
}

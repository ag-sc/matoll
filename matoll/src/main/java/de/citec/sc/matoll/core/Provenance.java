package de.citec.sc.matoll.core;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class Provenance {

    String Agent;

    Date StartedAtTime;
    Date EndedAtTime;

    Double Confidence;

    String POS;


    Integer Frequency;
    HashSet<String> patternset = new HashSet<String>();

    Integer Annotation = 0;

    Double OveralLabelRatio = 0.0;

    /*
    iterates over all entries and sums up all frequencys for a certain property.
    This ratio is then the frequency of the current provenance, divided by the number from above
    */
    Double OverallPropertyEntryRatio = 0.0; 

    List<String> Sentences = new ArrayList<>();

    public HashSet<String> getPatternset() {
        return patternset;
    }
    
    
    public Double getAvaerage_lenght() {
        if(Sentences.isEmpty()) return 0.0;
        else{
            double avg = 0.0;
            for(String sentence:Sentences)avg+=sentence.length();
            avg = avg/Sentences.size();

            return avg;
        }
        
    }

    public void setPatternset(HashSet<String> pattern) {
        this.patternset = pattern;
    }

    public void addPattern(String pattern) {
        this.patternset.add(pattern);
    }
    
    public void addAllPattern(HashSet<String> patternlist) {
        this.patternset.addAll(patternlist);
    }
    
        public Integer getFrequency() {
            return Frequency;
        }

        public void setFrequency(Integer frequency) {
            this.Frequency = frequency;
        }
        
        public void increaseFrequency(Integer frequency){
            this.Frequency += frequency;
        }

	public String getAgent()
	{
		return Agent;
	}
	
	public void setAgent(String agent)
	{
		Agent = agent;
	}
	
	public Double getConfidence()
	{
		return Confidence;
	}
	
	public void setConfidence(Double confidence)
	{
		Confidence = confidence;
	}
	
	public void setStartedAtTime(Date date)
	{
		StartedAtTime = date;
	}
	
	public void setEndedAtTime(Date date)
	{
		EndedAtTime = date;
	}
	
	public Date getStartedAtTime()
	{
		return StartedAtTime;
	}
	
	public Date getEndedAtTime()
	{
		return EndedAtTime;
	}
        
        public List<String> getSentences() {
		return Sentences;
	}


	public void addSentences(List<String> sentences) {
		Sentences.addAll(sentences);
		
	}


	public void setSentences(List<String> sentences) {
		Sentences = sentences;
		
	}
        
        public void addSentence(String sentence) {
		Sentences.add(sentence);
		
	}
        
        /**
         * Returns the k longest sentences as a list
         * @param k number of sentences
         * @return 
         */
        public List<String> getLongestSentences(int k){
            List<String> sentences = new ArrayList<>();
            Map<String, Integer> map = new HashMap<String, Integer>();
            Sentences.stream().forEach((sentence) -> { map.put(sentence, sentence.length());});
            Map<String,Integer> sortedMap = sortByComparator(map);
            int counter = 0;
            for(String key : sortedMap.keySet()){
                if(counter<k)sentences.add(key);
                counter++;
            }
            return sentences;
        }
        
        //http://www.mkyong.com/java/how-to-sort-a-map-in-java/
        private static Map<String, Integer> sortByComparator(Map<String, Integer> unsortMap) {
 
		// Convert Map to List
		List<Map.Entry<String, Integer>> list = 
			new LinkedList<Map.Entry<String, Integer>>(unsortMap.entrySet());
 
		// Sort list with comparator, to compare the Map values
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1,
                                           Map.Entry<String, Integer> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});
 
		// Convert sorted map back to a Map
		Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
		for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it.hasNext();) {
			Map.Entry<String, Integer> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
        
        public Integer getAnnotation() {
            return Annotation;
        }

        public void setAnnotation(Integer Annotation) {
            this.Annotation = Annotation;
        }
        
        public Double getOveralLabelRatio() {
            return OveralLabelRatio;
        }

        public void setOveralLabelRatio(Double OveralLabelRatio) {
            this.OveralLabelRatio = OveralLabelRatio;
        }
        public Double getOverallPropertyEntryRatio() {
            return OverallPropertyEntryRatio;
        }

        public void setOverallPropertyEntryRatio(Double OverallPropertyEntryRatio) {
            this.OverallPropertyEntryRatio = OverallPropertyEntryRatio;
        }
        public String getPOS() {
            return POS;
        }

        public void setPOS(String POS) {
            this.POS = POS;
        }
        
    
	
}

package de.citec.sc.matoll.core;
import java.util.Date;
import java.util.HashSet;


public class Provenance {

	String Agent;
	
	Date StartedAtTime;
	Date EndedAtTime;
	
	Double Confidence;
	
        Integer Frequency;
        HashSet<String> patternset = new HashSet<String>();

    public HashSet<String> getPatternset() {
        return patternset;
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
            System.out.println("Old Frequency:"+Frequency);
            System.out.println("add:"+frequency);
            this.Frequency += frequency;
            System.out.println("New Frequency:"+Frequency);
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
	
}

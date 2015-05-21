package de.citec.sc.matoll.core;
import java.util.Date;


public class Provenance {

	String Agent;
	
	Date StartedAtTime;
	Date EndedAtTime;
	
	Double Confidence;
	
        Integer Frequency = 1;

        public Integer getFrequency() {
            return Frequency;
        }

        public void setFrequency(Integer frequency) {
            Frequency = frequency;
        }
        
        public void increaseFrequency(Integer frequency){
            this.Frequency += frequency;
            //System.out.println("New Frequency:"+Frequency);
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

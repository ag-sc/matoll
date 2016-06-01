package de.citec.sc.matoll.LabelApproach;
public class AdjectiveObject {
	private String adjectiveTerm = "";
	private String object = "";
	private String pattern = "";
	private String pos_Pattern = "";
	private String pos_adj_Pattern = "";
	private String uri = "";
	private boolean isAdjective = false;
	private String sublabel = "";
	private double ratio = 0.0;
	private double ratio_pattern = 0.0;
	private double ratio_pos_pattern = 0.0;
	private boolean firstPosition = false;
	private boolean lastPosition = false;
	private int position = 0;
	private double nld = 0.0;
	private int frequency = 1;
	private double normalizedFrequency = 0.0;
	private double normalizedObjectFrequency = 0.0;
	private double normalizedObjectOccurrences=0.0;
	private String annotation = "";
	private double entropy = 0.0;
	private String sublabel_2 = "";
	private String objectURI = "";
	
	public String getAdjectiveTerm() {
		return adjectiveTerm;
	}
	public void setAdjectiveTerm(String adjective) {
		this.adjectiveTerm = adjective;
	}
	public String getObject() {
		return object;
	}
	public void setObject(String object) {
		this.object = object;
	}
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	public String getPos_Pattern() {
		return pos_Pattern;
	}
	public void setPos_Pattern(String pos_Pattern) {
		this.pos_Pattern = pos_Pattern;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public boolean isAdjective() {
		return isAdjective;
	}
	public void setAdjective(boolean isAdjective) {
		this.isAdjective = isAdjective;
	}
	public String getSublabel() {
		return sublabel;
	}
	public void setSublabel(String sublabel) {
		this.sublabel = sublabel;
	}
	
	public String toString(){
		return adjectiveTerm+" "+object+" "+uri+" "+pattern+" "+pos_Pattern;
	}
	public double getRatio() {
		return ratio;
	}
	public void setRatio(double ratio) {
		this.ratio = ratio;
	}
	public boolean isFirstPosition() {
		return firstPosition;
	}
	public void setFirstPosition(boolean firstPosition) {
		this.firstPosition = firstPosition;
	}
	public boolean isLastPosition() {
		return lastPosition;
	}
	public void setLastPosition(boolean lastPosition) {
		this.lastPosition = lastPosition;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public double getNld() {
		return nld;
	}
	public void setNld(double nld) {
		this.nld = nld;
	}
	public double getRatio_pattern() {
		return ratio_pattern;
	}
	public void setRatio_pattern(double ratio_pattern) {
		this.ratio_pattern = ratio_pattern;
	}
	public double getRatio_pos_pattern() {
		return ratio_pos_pattern;
	}
	public void setRatio_pos_pattern(double ratio_pos_pattern) {
		this.ratio_pos_pattern = ratio_pos_pattern;
	}
	public int getFrequency() {
		return frequency;
	}
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	public double getNormalizedFrequency() {
		return normalizedFrequency;
	}
	public void setNormalizedFrequency(double normalizedFrequency) {
		this.normalizedFrequency = normalizedFrequency;
	}
	public double getNormalizedObjectFrequency() {
		return normalizedObjectFrequency;
	}
	public void setNormalizedObjectFrequency(double normalizedObjectFrequency) {
		this.normalizedObjectFrequency = normalizedObjectFrequency;
	}
	public double getNormalizedObjectOccurrences() {
		return normalizedObjectOccurrences;
	}
	public void setNormalizedObjectOccurrences(double normalizedObjectOccurrences) {
		this.normalizedObjectOccurrences = normalizedObjectOccurrences;
	}
	public String getPos_adj_Pattern() {
		return pos_adj_Pattern;
	}
	public void setPos_adj_Pattern(String pos_adj_Pattern) {
		this.pos_adj_Pattern = pos_adj_Pattern;
	}
	public String getAnnotation() {
		return annotation;
	}
	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}
	public double getEntropy() {
		return entropy;
	}
	public void setEntropy(double entropy) {
		this.entropy = entropy;
	}
	public String getSublabel_2() {
		return sublabel_2;
	}
	public void setSublabel_2(String sublabel_2) {
		this.sublabel_2 = sublabel_2;
	}
	public String getObjectURI() {
		return objectURI;
	}
	public void setObjectURI(String objectURI) {
		this.objectURI = objectURI;
	}
	
}

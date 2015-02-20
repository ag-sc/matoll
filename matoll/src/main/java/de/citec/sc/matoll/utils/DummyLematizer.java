package de.citec.sc.matoll.utils;

public class DummyLematizer implements Lemmatizer {

	public String getLemma(String word) {
		return word;
	}

}

package de.citec.sc.matoll.adjectiveApproach;

import java.io.FileNotFoundException;

public class Process {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String path_annotatedFiles = "/Users/swalter/Documents/annotatedAdjectives/";
		String path_raw_files = "/Users/swalter/Documents/plainAdjectives/";
		String path_to_write_arff = "/Users/swalter/Desktop/";
		
		/*
		 * Create Raw Data
		 */
		
		
		/*
		 * Generate ARFF File
		 */
		try {
			GenerateArff.run(path_annotatedFiles, path_to_write_arff, path_to_write_arff);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

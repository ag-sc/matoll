
import de.citec.sc.matoll.core.Language;
import de.citec.sc.matoll.utils.Dbnary;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author swalter
 */
public class TestDbnary {
    public static void main(String[] args){
        Dbnary dbnary = new Dbnary(Language.EN);
        System.out.println("Loaded");
        System.out.println(dbnary.getURI("female", "adjective"));
    }
}

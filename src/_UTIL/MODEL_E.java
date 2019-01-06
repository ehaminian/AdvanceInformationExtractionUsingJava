package _UTIL;

import edu.stanford.nlp.quoteattribution.Person;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.*;
import org.apache.jena.vocabulary.OWL.*;
import org.apache.jena.vocabulary.OWL2.*;
import org.apache.jena.util.*;

import java.io.File;
import java.io.PrintStream;

import static org.apache.jena.rdf.model.ModelFactory.createDefaultModel;

public class MODEL_E {
    public MODEL_E()
    {
        model = createDefaultModel();
    }
    private Model model;
    public Model getModel()
    {
        return  model;
    }
    public void createModel_E(){
        model = createDefaultModel();


    }

    public void addStatement(String s, String p, String o){
        System.out.println("subject is: "+s);
        Resource subject = model.createResource(s);
        System.out.println("2");
        Property predicate = model.createProperty(p);
        System.out.println("3");
        RDFNode object = model.createResource(o);
        System.out.println("4");
        Statement stmt = model.createStatement(subject, predicate, object);
        System.out.println("5");
        model.add(stmt);
    }

    public void writeModel(){
        model.write(System.out, "TTL");
    }

     public Model readModel(String file){
       Model model = FileManager.get().loadModel(file);
         return model;
         }

    public void writeModel(Model model, String format,String fileName){
        PrintStream o=null;
        PrintStream console = System.out;

        try {
             o = new PrintStream(new File(_UTIL.TOOLES.getWorkspace()+fileName));
            System.setOut(o);
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
             RDFWriter writer = model.getWriter(format);
             writer.setProperty("showDoctypeDeclaration","true");
             writer.write(model, System.out, null);
             System.setOut(console);
             System.out.println("Writed...");
         }



}

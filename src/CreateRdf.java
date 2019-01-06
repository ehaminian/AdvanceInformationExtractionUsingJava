import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.*;
import java.io.*;
import  org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.Query;

public class CreateRdf extends Object{
    static String personURI    = "http://somewhere/JohnSmith";
    static String givenName    = "John";
    static String familyName   = "Smith";
    static String fullName     = givenName + " " + familyName;


    public static void main (String args[]) {
        // create an empty model
        Model model = ModelFactory.createDefaultModel();





        // create the resource
        //Resource johnSmith = model.createResource(personURI);
        Resource johnSmith
                = model.createResource(personURI)
                .addProperty(VCARD.FN, fullName)
                .addProperty(VCARD.N,
                        model.createResource()
                                .addProperty(VCARD.Given, givenName)
                                .addProperty(VCARD.Family, familyName));
        //model=_UTIL.TOOLS2.RDF_ReadToModel(model,"RDF.rdf");
        //model.write(System.out);
        Resource Re = model.getResource(personURI);
        //Resource ReN = (Resource) Re.getRequiredProperty(VCARD.N).getObject();
        //String fullName = Re.getRequiredProperty(VCARD.FN).getString();
        //Re.addProperty(VCARD.NICKNAME, "Smithy")
                //.addProperty(VCARD.NICKNAME, "Adman");
        model=model.add(Re,VCARD.NICKNAME, "Smithy");
        model=model.add(Re,VCARD.NICKNAME, "Adman");
        System.out.println("###############################");
        try {
            //PrintStream o = new PrintStream(new File(_UTIL.TOOLES.getWorkspace()+ "RDF.txt"));
            model.write(System.out, "RDF/XML-ABBREV");
            //model.write(o, "RDF/XML-ABBREV");
        }catch( Exception e) {
            System.out.println("Error");
        }
        System.out.println("################################");


        /*ParameterizedSparqlString queryStr = new ParameterizedSparqlString();
        queryStr.setNsPrefix("sw", "http://skunkworks.example.com/redacted#");
        queryStr.append("SELECT ?a ?b ?c ?d");
        queryStr.append("{");
        queryStr.append("   ?rawHit sw:key");
        queryStr.appendNode(someKey);
        queryStr.append(".");
        queryStr.append("  ?rawHit sw:a ?a .");
        queryStr.append("  ?rawHit sw:b ?b .");
        queryStr.append("  ?rawHit sw:c ?c . ");
        queryStr.append("  ?rawHit sw:d ?d .");
        queryStr.append("} ORDER BY DESC(d)");

        Query q = queryStr.asQuery();*/
/*
        // set up the output
        System.out.println("The nicknames of \"" + fullName + "\" are:");
        // list the nicknames
        StmtIterator iter = vcard.listProperties(VCARD.NICKNAME);
        while (iter.hasNext()) {
            System.out.println("    " + iter.nextStatement().getObject()
                                            .toString());
}
 */

        // add the property
        //johnSmith.addProperty(VCARD.FN, fullName);
/*
        // list the statements in the graph
        StmtIterator iter = model.listStatements();
        // print out the predicate, subject and object of each statement
        while (iter.hasNext()) {
            Statement stmt      = iter.nextStatement();         // get next statement
            Resource  subject   = stmt.getSubject();   // get the subject
            Property  predicate = stmt.getPredicate(); // get the predicate
            RDFNode   object    = stmt.getObject();    // get the object

            System.out.print(subject.toString());
            System.out.print(" " + predicate.toString() + " ");
            if (object instanceof Resource) {
                System.out.print(object.toString());
            } else {
                // object is a literal
                System.out.print(" \"" + object.toString() + "\"");
            }
            System.out.println(" .");
        }*/
    }
}

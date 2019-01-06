import _UTIL.MODEL_E;
import _UTIL.TOOLES;
import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.logging.Redwood;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import edu.stanford.nlp.quoteattribution.Person;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.*;
import org.apache.jena.vocabulary.OWL.*;
import org.apache.jena.vocabulary.OWL2.*;
import org.apache.jena.util.*;
import org.apache.commons.lang3.StringUtils;


import java.io.File;
import java.io.PrintStream;

import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class FA1 {
    private static final Redwood.RedwoodChannels log = Redwood.channels(FA1.class);

    public static void main(String[] args) {



        org.apache.log4j.Logger.getLogger("ac.biu.nlp.nlp.engineml").setLevel(Level.OFF);
        org.apache.log4j.Logger.getLogger("org.BIU.utils.logging.ExperimentLogger").setLevel(Level.OFF);
        Logger.getRootLogger().setLevel(Level.OFF);

        String person=VCARD.getURI();
        String ns = "http://www.example.org#";
        String nsRDFS = "http://www.w3.org/2000/01/rdf-schema#";
        String org=ORG.getURI();
        String event=OWL2.getURI();
        String C="http://jena.hpl.hp.com/2004/08/location-mapping#";
        String COD="http://www.w3.org/2002/07/owl#";
        //String Literal=

        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,depparse,natlog,openie"); //
        // add additional rules
        props.setProperty("ner.additional.regexner.mapping", TOOLES.getWorkspace()+"NERREG.txt");
        props.setProperty("ner.additional.regexner.ignorecase", "true");
        //props.setProperty("regexner.mapping", TOOLES.getWorkspace()+"NERREG.txt");
        props.setProperty("file", TOOLES.getWorkspace()+"input.txt");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        Annotation doc =null;
        System.out.println("Reading from input file....");
        try {doc=new Annotation(TOOLES.readFileAsString("input.txt"));}catch (Exception e){System.out.println("Error happens...");System.out.println(e.getMessage()); }
        System.out.println("Annotating the text....");
        pipeline.annotate(doc);
        List<CoreLabel> result=null;
        int sentNo = 0;
        MODEL_E MAINMODEL=new MODEL_E();
        System.out.println("Creating RDF:....");
        for (CoreMap sentence : doc.get(CoreAnnotations.SentencesAnnotation.class)) {
            System.out.println("Sentence #" + ++sentNo + " : " + sentence.get(CoreAnnotations.TextAnnotation.class));
            System.out.println("Get triples (using OpenIE)....");
            Collection<RelationTriple> triples = sentence.get(NaturalLogicAnnotations.RelationTriplesAnnotation.class);
            System.out.println("Show the triples and NER of all tokens#########################");
            for (RelationTriple triple : triples) {
                System.out.println(triple.subjectLemmaGloss() + "\t" +
                        triple.relationLemmaGloss() + "\t" +
                        triple.objectLemmaGloss());


             for(CoreLabel token1: triple.allTokens())
             {
                 System.out.println(token1.ner());
             }
                System.out.println("@@@@@@@@@@@@@@@@@@@");
            }


            System.out.println("################################################################");
            List<RelationTriple> PP = triples.stream()
                    .filter( triple ->
                            triple.subject.stream().anyMatch(token -> "DESCONUTRY".equals(token.ner())))
                    .filter( triple ->
                            triple.object.stream().anyMatch(token -> "DESCONUTRY".equals(token.ner())))
                    .collect(Collectors.toList());
            for (RelationTriple triple : PP) {
                System.out.println(triple.subjectLemmaGloss() + "\t" +
                        triple.relationLemmaGloss() + "\t" +
                        triple.objectLemmaGloss());
                String sub="",obj="",rel="";
                result=triple.subject.stream().filter(token -> "DESCONUTRY".equals(token.ner())).collect(Collectors.toList());
                if(!result.isEmpty()) {sub=result.get(0).toString().split("-")[0];}
                result=triple.object.stream().filter(token -> "DESCONUTRY".equals(token.ner())).collect(Collectors.toList());
                if(!result.isEmpty()) {obj=result.get(0).toString().split("-")[0];}
                rel=triple.relationLemmaGloss();
                sub=sub.replaceAll("\\s+","");
                rel=rel.replaceAll("\\s+","");
                obj=obj.replaceAll("\\s+","");
                if(!sub.trim().isEmpty()&&!rel.trim().isEmpty()&&!obj.trim().isEmpty()) {
                    MAINMODEL.addStatement(C + sub, ns + rel, C + obj);
                }
            }
            System.out.println("################################################################");

            System.out.println("################################################################");
            PP = triples.stream()
                    .filter( triple ->
                            triple.subject.stream().anyMatch(token -> "DESCONUTRY".equals(token.ner())))
                    .filter( triple ->
                            triple.object.stream().anyMatch(token -> "USPRESIDENT".equals(token.ner())))
                    .collect(Collectors.toList());
            for (RelationTriple triple : PP) {
                System.out.println(triple.subjectLemmaGloss() + "\t" +
                        triple.relationLemmaGloss() + "\t" +
                        triple.objectLemmaGloss());
                String sub="",obj="",rel="";
                result=triple.subject.stream().filter(token -> "DESCONUTRY".equals(token.ner())).collect(Collectors.toList());
                if(!result.isEmpty()) {sub=result.get(0).toString().split("-")[0];}
                result=triple.object.stream().filter(token -> "USPRESIDENT".equals(token.ner())).collect(Collectors.toList());
                if(!result.isEmpty()) {obj=result.get(0).toString().split("-")[0];}
                rel=triple.relationLemmaGloss();
                sub=sub.replaceAll("\\s+","");
                rel=rel.replaceAll("\\s+","");
                obj=obj.replaceAll("\\s+","");
                if(!sub.trim().isEmpty()&&!triple.relationLemmaGloss().trim().isEmpty()&&!obj.trim().isEmpty()){
                    MAINMODEL.addStatement(C+sub,ns+rel,person+obj);}
            }
            System.out.println("################################################################");

            System.out.println("################################################################");
            PP = triples.stream()
                    .filter( triple ->
                            triple.subject.stream().anyMatch(token -> "USPRESIDENT".equals(token.ner())))
                    .filter( triple ->
                            triple.object.stream().anyMatch(token -> "DESCONUTRY".equals(token.ner())))
                    .collect(Collectors.toList());
            for (RelationTriple triple : PP) {
                System.out.println(triple.subjectLemmaGloss() + "\t" +
                        triple.relationLemmaGloss() + "\t" +
                        triple.objectLemmaGloss());
                String sub="",obj="",rel="";
                result=triple.subject.stream().filter(token -> "USPRESIDENT".equals(token.ner())).collect(Collectors.toList());
                if(!result.isEmpty()) {sub=result.get(0).toString().split("-")[0];}
                result=triple.object.stream().filter(token -> "DESCONUTRY".equals(token.ner())).collect(Collectors.toList());
                if(!result.isEmpty()) {obj=result.get(0).toString().split("-")[0];}
                rel=triple.relationLemmaGloss();
                sub=sub.replaceAll("\\s+","");
                rel=rel.replaceAll("\\s+","");
                obj=obj.replaceAll("\\s+","");
                if(!sub.trim().isEmpty()&&!triple.relationLemmaGloss().trim().isEmpty()&&!obj.trim().isEmpty()){
                MAINMODEL.addStatement(person+sub,ns+rel,C+obj);}
            }
            System.out.println("################################################################");


            System.out.println("################################################################");
            PP = triples.stream()
                    .filter( triple ->
                            triple.subject.stream().anyMatch(token -> "DESCONUTRY".equals(token.ner())))
                    .filter( triple ->
                            triple.object.stream().anyMatch(token -> "SRYCITY".equals(token.ner())))
                    .collect(Collectors.toList());
            for (RelationTriple triple : PP) {
                System.out.println(triple.subjectLemmaGloss() + "\t" +
                        triple.relationLemmaGloss() + "\t" +
                        triple.objectLemmaGloss());
                String sub="",obj="",rel="";
                result=triple.subject.stream().filter(token -> "DESCONUTRY".equals(token.ner())).collect(Collectors.toList());
                if(!result.isEmpty()) {sub=result.get(0).toString().split("-")[0];}
                result=triple.object.stream().filter(token -> "SRYCITY".equals(token.ner())).collect(Collectors.toList());
                if(!result.isEmpty()) {obj=result.get(0).toString().split("-")[0];}
                rel=triple.relationLemmaGloss();
                sub=sub.replaceAll("\\s+","");
                rel=rel.replaceAll("\\s+","");
                obj=obj.replaceAll("\\s+","");
                if(!sub.trim().isEmpty()&&!triple.relationLemmaGloss().trim().isEmpty()&&!obj.trim().isEmpty()){
                MAINMODEL.addStatement(C+sub,ns+rel,C+obj);}
            }
            System.out.println("################################################################");

            System.out.println("################################################################");
            PP = triples.stream()
                    .filter( triple ->
                            triple.subject.stream().anyMatch(token -> "SRYCITY".equals(token.ner())))
                    .filter( triple ->
                            triple.object.stream().anyMatch(token -> "DESCONUTRY".equals(token.ner())))
                    .collect(Collectors.toList());
            for (RelationTriple triple : PP) {
                System.out.println(triple.subjectLemmaGloss() + "\t" +
                        triple.relationLemmaGloss() + "\t" +
                        triple.objectLemmaGloss());
                String sub="",obj="",rel="";
                result=triple.subject.stream().filter(token -> "SRYCITY".equals(token.ner())).collect(Collectors.toList());
                if(!result.isEmpty()) {sub=result.get(0).toString().split("-")[0];}
                result=triple.object.stream().filter(token -> "DESCONUTRY".equals(token.ner())).collect(Collectors.toList());
                if(!result.isEmpty()) {obj=result.get(0).toString().split("-")[0];}
                rel=triple.relationLemmaGloss();
                sub=sub.replaceAll("\\s+","");
                rel=rel.replaceAll("\\s+","");
                obj=obj.replaceAll("\\s+","");
                if(!sub.trim().isEmpty()&&!triple.relationLemmaGloss().trim().isEmpty()&&!obj.trim().isEmpty()){
                MAINMODEL.addStatement(C+sub,ns+rel,C+obj);}
            }
            System.out.println("################################################################");

            System.out.println("################################################################");
            PP = triples.stream()
                    .filter( triple ->
                            triple.subject.stream().anyMatch(token -> "CAUSE_OF_DEATH".equals(token.ner())))
                    .filter( triple ->
                            triple.object.stream().anyMatch(token -> "DESCONUTRY".equals(token.ner())))
                    .collect(Collectors.toList());
            for (RelationTriple triple : PP) {
                System.out.println(triple.subjectLemmaGloss() + "\t" +
                        triple.relationLemmaGloss() + "\t" +
                        triple.objectLemmaGloss());
                String sub="",obj="",rel="";
                result=triple.subject.stream().filter(token -> "CAUSE_OF_DEATH".equals(token.ner())).collect(Collectors.toList());
                if(!result.isEmpty()) {sub=result.get(0).toString().split("-")[0];}
                result=triple.object.stream().filter(token -> "DESCONUTRY".equals(token.ner())).collect(Collectors.toList());
                if(!result.isEmpty()) {obj=result.get(0).toString().split("-")[0];}
                rel=triple.relationLemmaGloss();
                sub=sub.replaceAll("\\s+","");
                rel=rel.replaceAll("\\s+","");
                obj=obj.replaceAll("\\s+","");
                if(!sub.trim().isEmpty()&&!triple.relationLemmaGloss().trim().isEmpty()&&!obj.trim().isEmpty())
                MAINMODEL.addStatement(COD+sub,ns+rel,C+obj);
            }
            System.out.println("################################################################");

            System.out.println("################################################################");
            PP = triples.stream()
                    .filter( triple ->
                            triple.subject.stream().anyMatch(token -> "DESCONUTRY".equals(token.ner())))
                    .filter( triple ->
                            triple.object.stream().anyMatch(token -> "CAUSE_OF_DEATH".equals(token.ner())))
                    .collect(Collectors.toList());
            for (RelationTriple triple : PP) {
                System.out.println(triple.subjectLemmaGloss() + "\t" +
                        triple.relationLemmaGloss() + "\t" +
                        triple.objectLemmaGloss());
                String sub="",obj="",rel="";
                result=triple.subject.stream().filter(token -> "DESCONUTRY".equals(token.ner())).collect(Collectors.toList());
                if(!result.isEmpty()) {sub=result.get(0).toString().split("-")[0];}
                result=triple.object.stream().filter(token -> "CAUSE_OF_DEATH".equals(token.ner())).collect(Collectors.toList());
                if(!result.isEmpty()) {obj=result.get(0).toString().split("-")[0];}
                rel=triple.relationLemmaGloss();
                sub=sub.replaceAll("\\s+","");
                rel=rel.replaceAll("\\s+","");
                obj=obj.replaceAll("\\s+","");
                if(!sub.trim().isEmpty()&&!triple.relationLemmaGloss().trim().isEmpty()&&!obj.trim().isEmpty()){
                MAINMODEL.addStatement(C+sub,ns+rel,COD+obj);}
            }
            System.out.println("################################################################");

            System.out.println("################################################################");
            PP = triples.stream()
                    .filter( triple ->
                            triple.subject.stream().anyMatch(token -> "DESCONUTRY".equals(token.ner())))
                    .filter( triple ->
                            triple.relation.stream().anyMatch(token -> "CAUSE_OF_DEATH".equals(token.ner())))
                    .filter( triple ->
                            triple.relation.stream().anyMatch(token -> "DESCONUTRY".equals(token.ner())))
                    .collect(Collectors.toList());
            for (RelationTriple triple : PP) {
                System.out.println(triple.subjectLemmaGloss() + "\t" +
                        triple.relationLemmaGloss() + "\t" +
                        triple.objectLemmaGloss());
                String sub="",obj="",rel="";
                result=triple.subject.stream().filter(token -> "DESCONUTRY".equals(token.ner())).collect(Collectors.toList());
                if(!result.isEmpty()) {sub=result.get(0).toString().split("-")[0];}
                result=triple.relation.stream().filter(token -> "CAUSE_OF_DEATH".equals(token.ner())).collect(Collectors.toList());
                if(!result.isEmpty()) {obj=result.get(0).toString().split("-")[0];}
                result=triple.object.stream().filter(token -> "DESCONUTRY".equals(token.ner())).collect(Collectors.toList());
                if(!result.isEmpty()) {obj=result.get(0).toString().split("-")[0];}
                rel=triple.relationLemmaGloss();
                sub=sub.replaceAll("\\s+","");
                rel=rel.replaceAll("\\s+","");
                obj=obj.replaceAll("\\s+","");
                if(!sub.trim().isEmpty()&&!triple.relationLemmaGloss().trim().isEmpty()&&!obj.trim().isEmpty()){
                MAINMODEL.addStatement(C+sub,ns+rel,C+obj);}
            }
            System.out.println("################################################################");



            System.out.println("################################################################");
            PP = triples.stream()
                    .filter( triple ->
                            triple.subject.stream().anyMatch(token -> "DESCONUTRY".equals(token.ner())))
                    .filter( triple ->
                            triple.object.stream().anyMatch(token -> !"O".equals(token.ner())))
                    .filter( triple ->
                            triple.relation.stream().anyMatch(token -> !"O".equals(token.ner())))
                    .filter( triple ->
                            triple.object.stream().allMatch(token -> !"DESCONUTRY".equals(token.ner())))
                    .filter( triple ->
                            triple.object.stream().allMatch(token -> !"USPRESIDENT".equals(token.ner())))
                    .filter( triple ->
                            triple.object.stream().allMatch(token -> !"CAUSE_OF_DEATH".equals(token.ner())))
                    .collect(Collectors.toList());
            for (RelationTriple triple : PP) {
                System.out.println(triple.subjectLemmaGloss() + "\t" +
                        triple.relationLemmaGloss() + "\t" +
                        triple.objectLemmaGloss());
                String sub="",obj="",rel;
                result=triple.subject.stream().filter(token -> "DESCONUTRY".equals(token.ner())).collect(Collectors.toList());
                if(!result.isEmpty()) {sub=result.get(0).toString().split("-")[0];}
                rel=triple.relationLemmaGloss();
                obj=triple.objectLemmaGloss();
                sub=sub.replaceAll("\\s+","");
                rel=rel.replaceAll("\\s+","");
                obj=obj.replaceAll("\\s+","");
                if(!sub.trim().isEmpty()&&!triple.relationLemmaGloss().trim().isEmpty()&&!obj.trim().isEmpty()){
                MAINMODEL.addStatement(C+sub,ns+rel,ns+obj);}
            }
            System.out.println("################################################################");


            System.out.println("################################################################");
            PP = triples.stream()
                    .filter( triple ->
                            triple.subject.stream().anyMatch(token -> "USPRESIDENT".equals(token.ner())))
                    .filter( triple ->
                            triple.object.stream().anyMatch(token -> !"O".equals(token.ner())))
                    .filter( triple ->
                            triple.relation.stream().anyMatch(token -> !"O".equals(token.ner())))
                    .filter( triple ->
                            triple.object.stream().allMatch(token -> !"DESCONUTRY".equals(token.ner())))
                    .filter( triple ->
                            triple.object.stream().allMatch(token -> !"USPRESIDENT".equals(token.ner())))
                    .filter( triple ->
                            triple.object.stream().allMatch(token -> !"CAUSE_OF_DEATH".equals(token.ner())))
                    .collect(Collectors.toList());
            for (RelationTriple triple : PP) {
                System.out.println(triple.subjectLemmaGloss() + "\t" +
                        triple.relationLemmaGloss() + "\t" +
                        triple.objectLemmaGloss());
                String sub="",obj="",rel="";
                result=triple.subject.stream().filter(token -> "DESCONUTRY".equals(token.ner())).collect(Collectors.toList());
                if(!result.isEmpty()) {sub=result.get(0).toString().split("-")[0];}
                rel=triple.relationLemmaGloss();
                obj=triple.objectLemmaGloss();
                sub=sub.replaceAll("\\s+","");
                rel=rel.replaceAll("\\s+","");
                obj=obj.replaceAll("\\s+","");
                if(!sub.trim().isEmpty()&&!triple.relationLemmaGloss().trim().isEmpty()&&!obj.trim().isEmpty()){
                MAINMODEL.addStatement(person+sub,ns+rel,ns+obj);}
            }
            System.out.println("################################################################");

            System.out.println("################################################################");
            PP = triples.stream()
                    .filter( triple ->
                            triple.subject.stream().anyMatch(token -> "SRYCITY".equals(token.ner())))
                    .filter( triple ->
                            triple.object.stream().anyMatch(token -> !"O".equals(token.ner())))
                    .filter( triple ->
                            triple.relation.stream().anyMatch(token -> !"O".equals(token.ner())))
                    .filter( triple ->
                            triple.object.stream().allMatch(token -> !"DESCONUTRY".equals(token.ner())))
                    .filter( triple ->
                            triple.object.stream().allMatch(token -> !"USPRESIDENT".equals(token.ner())))
                    .filter( triple ->
                            triple.object.stream().allMatch(token -> !"CAUSE_OF_DEATH".equals(token.ner())))
                    .collect(Collectors.toList());
            for (RelationTriple triple : PP) {
                System.out.println(triple.subjectLemmaGloss() + "\t" +
                        triple.relationLemmaGloss() + "\t" +
                        triple.objectLemmaGloss());
                String sub="",obj="",rel="";
                result=triple.subject.stream().filter(token -> "DESCONUTRY".equals(token.ner())).collect(Collectors.toList());
                if(!result.isEmpty()) {sub=result.get(0).toString().split("-")[0];}
                rel=triple.relationLemmaGloss();
                obj=triple.objectLemmaGloss();
                sub=sub.replaceAll("\\s+","");
                rel=rel.replaceAll("\\s+","");
                obj=obj.replaceAll("\\s+","");
                if(!sub.trim().isEmpty()&&!triple.relationLemmaGloss().trim().isEmpty()&&!obj.trim().isEmpty()){
                MAINMODEL.addStatement(C+sub,ns+rel,ns+obj);}
            }
            System.out.println("################################################################");

            System.out.println("################################################################");
            PP = triples.stream()
                    .filter( triple ->
                            triple.subject.stream().anyMatch(token -> "CAUSE_OF_DEATH".equals(token.ner())))
                    .filter( triple ->
                            triple.object.stream().anyMatch(token -> !"O".equals(token.ner())))
                    .filter( triple ->
                            triple.relation.stream().anyMatch(token -> !"O".equals(token.ner())))
                    .filter( triple ->
                            triple.object.stream().allMatch(token -> !"DESCONUTRY".equals(token.ner())))
                    .filter( triple ->
                            triple.object.stream().allMatch(token -> !"USPRESIDENT".equals(token.ner())))
                    .filter( triple ->
                            triple.object.stream().allMatch(token -> !"CAUSE_OF_DEATH".equals(token.ner())))
                    .collect(Collectors.toList());
            for (RelationTriple triple : PP) {
                System.out.println(triple.subjectLemmaGloss() + "\t" +
                        triple.relationLemmaGloss() + "\t" +
                        triple.objectLemmaGloss());
                String sub="",obj="",rel="";
                result=triple.subject.stream().filter(token -> "DESCONUTRY".equals(token.ner())).collect(Collectors.toList());
                if(!result.isEmpty()) {sub=result.get(0).toString().split("-")[0];}
                rel=triple.relationLemmaGloss();
                obj=triple.objectLemmaGloss();
                sub=sub.replaceAll("\\s+","");
                rel=rel.replaceAll("\\s+","");
                obj=obj.replaceAll("\\s+","");
                if(!sub.trim().isEmpty()&&!triple.relationLemmaGloss().trim().isEmpty()&&!obj.trim().isEmpty()){
                MAINMODEL.addStatement(COD+sub,ns+rel,ns+obj);}
            }
            System.out.println("################################################################");

        }
        MAINMODEL.writeModel(MAINMODEL.getModel(),"RDF/XML","Emodel.rdf");
    }
}

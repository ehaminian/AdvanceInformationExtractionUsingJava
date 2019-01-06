import _UTIL.TOOLES;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import  org.apache.log4j.Level;
import org.apache.log4j.Logger;
import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;
import edu.stanford.nlp.naturalli.OpenIE;
import edu.stanford.nlp.naturalli.SentenceFragment;
import edu.stanford.nlp.pipeline.*;
import java.util.*;

import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import twitter4j.Twitter;
import twitter4j.*;
import _UTIL.*;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.parser.nndep.DependencyParser;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.util.logging.Redwood;
import java.io.StringReader;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;
import java.io.File;
import java.io.StringReader;
import java.util.Collection;
import java.util.List;

import java.util.Properties;

public class FA2 {

    public static void main(String[] args) {
        Logger.getLogger("ac.biu.nlp.nlp.engineml").setLevel(Level.OFF);
        Logger.getLogger("org.BIU.utils.logging.ExperimentLogger").setLevel(Level.OFF);
        Logger.getRootLogger().setLevel(Level.OFF);
        System.out.println("Start");
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,depparse,natlog,openie");
        props.setProperty("coref.algorithm", "neural");
        props.setProperty("file", TOOLES.getWorkspace()+"input.txt");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        CoreDocument doc =null;
        System.out.println("Reading from input file....");
        try {
            doc=new CoreDocument(TOOLES.readFileAsString("input.txt"));
        }catch (Exception e)
        {
            System.out.println("Error happens...");
            System.out.println(e.getMessage());
        }
        System.out.println("Annoting TEXT....");
        pipeline.annotate(doc);
        int NOS=doc.sentences().size();
        System.out.println("There are "+NOS+" sentences in the input file");
        CoreSentence sentence=null;

        for(int i=0;i<2;i++)
        {
            System.out.println("#################################################");
            sentence = doc.sentences().get(i);
            System.out.println("#1: "+sentence.text());
            List<String> nerTags = sentence.nerTags();
            System.out.println("NER tags: ");
            System.out.println(nerTags);
            System.out.println("Print relations........ ");


            List<RelationTriple> relations =sentence.relations();
            int j=relations.size();
            System.out.println("Number of relations is: "+ j);
            for(int z=0;z<j;z++) {
                System.out.println("------------");
                System.out.println("The relation is : "+relations.get(z));
                System.out.println("Number of subject is : "+ relations.get(z).subject.size());
                System.out.println("Number of canonicalSubject  is : "+ relations.get(z).canonicalSubject.size());
                System.out.println("Number of relation  is : "+ relations.get(z).relation.size());
                System.out.println("Number of object  is : "+ relations.get(z).object.size());
                System.out.println("Number of canonicalObject  is : "+ relations.get(z).canonicalObject.size());
            }


        }


    }
}

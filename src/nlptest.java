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






public class nlptest {



    private static final Redwood.RedwoodChannels log = Redwood.channels(nlptest.class);

    public static void main(String[] args) {

        /*String test=_UTIL.TOOLES.Sampletext;
        if(!_UTIL.TOOLES.write2file(test,"input.txt",false))
        {
            System.out.println("Unsuccessfull");
            return;
        }*/
        Properties props = new Properties();

        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,depparse,natlog,openie"); //
        props.setProperty("file", TOOLES.getWorkspace()+"input.txt");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        Annotation doc =null;
        System.out.println("Reading from input file....");
        try {
            doc=new Annotation(TOOLES.readFileAsString("input.txt"));
        }catch (Exception e)
        {
            System.out.println("Error happens...");
            System.out.println(e.getMessage());
        }
        System.out.println("Annotating the text....");
        //System.out.println(doc);
        pipeline.annotate(doc);


        System.out.println("Saving in the XML file....");
        _UTIL.TOOLES.SaveXML(pipeline,doc,"OUTPUT.xml");
        System.out.println("OUTPUT.xml in the workspace contains annotations. You can open it by Excel to see the structure..");
        System.out.println("Now let's create Triples....");
        System.out.println("################################################################");
        // Loop over sentences in the document

        int sentNo = 0;
        for (CoreMap sentence : doc.get(CoreAnnotations.SentencesAnnotation.class)) {
            System.out.println("Sentence #" + ++sentNo + " : " + sentence.get(CoreAnnotations.TextAnnotation.class));

            // Print SemanticGraph  Obama trump aleppo russia
            //System.out.println("Print SemanticGraph:....");
            //System.out.println(sentence.get(SemanticGraphCoreAnnotations.EnhancedDependenciesAnnotation.class).toString(SemanticGraph.OutputFormat.LIST));



            // Get the OpenIE triples for the sentence
            System.out.println("Get triples (using OpenIE)....");
            Collection<RelationTriple> triples = sentence.get(NaturalLogicAnnotations.RelationTriplesAnnotation.class);



            // Print the triples
            System.out.println("Print triples:....");
            for (RelationTriple triple : triples) {
                System.out.println(triple.confidence + "\t" +
                        triple.subjectLemmaGloss() + "\t" +
                        triple.relationLemmaGloss() + "\t" +
                        triple.objectLemmaGloss());
            }
            // Dependency Parser
            //System.out.println("Dependency Parser ....");
            //_UTIL.TOOLS2.DependencyParserfun(sentence.get(CoreAnnotations.TextAnnotation.class));

            // Visualise data
            //System.out.println("Let's create some visualization....");
            //String fName="Sentece"+sentNo+".png";
           // _UTIL.TOOLS2.Visualization(sentence.get(CoreAnnotations.TextAnnotation.class),_UTIL.TOOLES.getWorkspace()+fName);
            //System.out.println("An image with name "+fName+" has been created at workspace");
            System.out.println("################################################################");

            // Alternately, to only run e.g., the clause splitter:
           /* List<SentenceFragment> clauses = new OpenIE(props).clausesInSentence(sentence);
            for (SentenceFragment clause : clauses) {
                System.out.println(clause.parseTree.toString(SemanticGraph.OutputFormat.LIST));
            }
            System.out.println();*/
        }



    }


}

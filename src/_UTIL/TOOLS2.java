package _UTIL;

import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.parser.nndep.DependencyParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.*;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.RDFS;

import java.io.*;

public class TOOLS2 {

    public static void DependencyParserfun(String text)
    {
        String modelPath = DependencyParser.DEFAULT_MODEL;
        String taggerPath = "edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger";
        MaxentTagger tagger = new MaxentTagger(taggerPath);
        DependencyParser parser = DependencyParser.loadFromModelFile(modelPath);
        DocumentPreprocessor tokenizer = new DocumentPreprocessor(new StringReader(text));
        for (List<HasWord> sentence : tokenizer) {
            List<TaggedWord> tagged = tagger.tagSentence(sentence);
            GrammaticalStructure gs = parser.predict(tagged);

            // Print typed dependencies
            //log.info(gs);
            System.out.println(gs);
        }
    }

    public static void Visualization(String text,String path)
    {
        TreebankLanguagePack tlp = new PennTreebankLanguagePack();
        GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
        LexicalizedParser lp = LexicalizedParser.loadModel();
        String[] p=new String[]{"-maxLength", "500", "-retainTmpSubcategories"};
        lp.setOptionFlags(p);
        TokenizerFactory<CoreLabel> tokenizerFactory =
                PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
        List<CoreLabel> wordList = tokenizerFactory.getTokenizer(new StringReader(text)).tokenize();
        Tree tree = lp.apply(wordList);
        GrammaticalStructure gs = gsf.newGrammaticalStructure(tree);
        Collection<TypedDependency> tdl = gs.typedDependenciesCCprocessed(true);
        try {
            Main.writeImage(tree, tdl, path, 3);
        }catch (Exception e)
        {
            System.out.println("Error is: ");
            System.out.println(e.getMessage());
        }
    }

    public static Model RDF_ReadToModel(Model model,String FileName)
    {
        // model = ModelFactory.createDefaultModel();

        InputStream in = FileManager.get().open( _UTIL.TOOLES.getWorkspace()+FileName );
        if (in == null) {
            throw new IllegalArgumentException( "File: " + _UTIL.TOOLES.getWorkspace()+FileName + " not found");
        }
        model.read(in, "");
        return model;
    }



    public static Model RDF_AddToexistingFile(Model model, RelationTriple triple)
    {

        //Model model = model1.union(model2);
        return model;
    }
    public static boolean RDF_ShouldBeConsidered(RelationTriple triple)
    {
        boolean flag=false;

        List<String> result = new ArrayList<String>();
        for(CoreLabel ts:triple.allTokens())
        {
            result.add(ts.ner().toUpperCase());
        }
        if(result.contains("PERSON")||result.contains("COUNTRY")||
                result.contains("NATIONALITY")||result.contains("ORGANIZATION")||
                result.contains("LOCATION")||result.contains("DATE"))
        {
            flag=true;
        }
        return flag;
    }






}

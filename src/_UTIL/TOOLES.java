package _UTIL;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;




import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.file.*;
import org.jsoup.Jsoup;

public class TOOLES {

    public static String Sampletext = "Joe Smith was born in California. " +
            "In 2017, he went to Paris, France in the summer. " +
            "His flight left at 3:00pm on July 10th, 2017. " +
            "After eating some escargot for the first time, Joe said, \"That was delicious!\" " +
            "He sent a postcard to his sister Jane Smith. " +
            "After hearing about Joe's trip, Jane decided she might go to France one day.";


    private static String workspace="C:\\Users\\Ehsan\\Desktop\\AIE\\Workspace\\";
    // For General search (All Tweets)
    public static String TW_querystring="Brasil";
    public static String TW_Out_ResultFileName="TW_gatheredInformation.txt";
    public static boolean FetchRelatedsites=false;
    public static int number_of_considered_sites=20;


    // For Data streaming (Recent Tweets)
    public static  String[] Keyworads=new String[]{"#USA"};
    public static double[][] Location= new double[][]{new double[]{41.179278, -8.595412},new double[]{-61.171875,44.087585}};
    public static String[] Language= new String[]{"en"};
    public static boolean isLocationUsed=false;

    public static String getWorkspace()
    {
        return workspace;
    }


    private static final Pattern REMOVE_TAGS = Pattern.compile("<.+?>");
    public static String removeTags(String string) {
        if (string == null || string.length() == 0) {
            return string;
        }
        //Matcher m = REMOVE_TAGS.matcher(string);
        //return m.replaceAll("");
        return Jsoup.parse(string).text();
    }
    static public String readurl(String address)
    {
        try {
            URL url = new URL(address);
            URLConnection con = url.openConnection();
            Pattern p = Pattern.compile("text/html;\\s+charset=([^\\s]+)\\s*");
            Matcher m = p.matcher(con.getContentType());
            String charset = m.matches() ? m.group(1) : "ISO-8859-1";
            Reader r = new InputStreamReader(con.getInputStream(), charset);
            StringBuilder buf = new StringBuilder();
            while (true) {
                int ch = r.read();
                if (ch < 0)
                    break;
                buf.append((char) ch);
            }
            String str = buf.toString();
            str=removeTags(str);
            return str;

        }catch(Exception ee)
        {
            System.out.println(ee.getMessage());
            return null;
        }
    }

    public static boolean write2file(String inputstring,String filename,Boolean append)
    {
        try {
            FileWriter fileWriter = new FileWriter(getWorkspace()+filename,append);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println(inputstring);
            printWriter.close();
            return  true;
        }
        catch (Exception ee)
        {
            System.out.println("Error: "+ee.getMessage());
            return  false;
        }

    }

    public static String convertArrayofstring2string(String[] inputstring,char spiliter)
    {
        String s="";
        for(int i=0;i<inputstring.length;i++)
        {
            s+=spiliter;
            s+=inputstring[i];
        }
        return s;

    }

    public static String readFileAsString(String fileName)
            throws Exception
    {
        String data = null;
        data = new String(Files.readAllBytes(Paths.get(getWorkspace()+fileName)));
        return data;
    }


    public static void showoutput(CoreDocument exampleDocument)
    {
        int y=exampleDocument.sentences().size();
        for(int i=1;i<y;i++) {
            List<CoreLabel> firstSentenceTokens = exampleDocument.sentences().get(i).tokens();
            for (CoreLabel token : firstSentenceTokens) {
                System.out.println(token.word() + "\t" + token.beginPosition() + "\t" + token.endPosition());
            }
        }
    }

    public static void showoutput(Annotation exampleAnnotation)
    {
        for (CoreMap sentence : exampleAnnotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                System.out.println(token.word() + "\t" + token.ner());
            }
        }
    }

    public  static boolean SaveXML(StanfordCoreNLP pipeline,Annotation exampleAnnotation,String XMLFilrName)
    {
        try {
            FileOutputStream os = new FileOutputStream(new File(_UTIL.TOOLES.getWorkspace(), XMLFilrName));
            pipeline.xmlPrint(exampleAnnotation, os);
            return true;
        }
        catch (Exception e)
        {
            System.out.println("Error happens...");
            System.out.println(e.getMessage());
            return false;
        }

    }


    public static boolean TW_fetchInfoStream(String[] Keyworads,double[][] Location,String[] Language,boolean isLocationUsed)
    {
        TwitterStream twitterStream=_UTIL.TOOLES.TW_createStreamTwitterFactory();
        twitterStream.addListener(_UTIL.TOOLES.listener1);
        FilterQuery tweetFilterQuery = new FilterQuery(); // See
        tweetFilterQuery.track(Keyworads); //
        // tweetFilterQuery.locations(Location);
        tweetFilterQuery.language(Language); // Note that language does not work properly on Norwegian tweets
        twitterStream.filter(tweetFilterQuery);
        return true;
    }

    public static Twitter TW_createTwitterFactory()
    {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("LDEplYQrMbg46wfaHbd0QY2Jm")
                .setOAuthConsumerSecret("4Hem6UWbrdFbiD8C9rBdSQyhELHdwl41NAZZUdLjjVHas8S5wX")
                .setOAuthAccessToken("2801646263-bFrNv8z6I3DHhb4DIcI1zWOBCTs8W5ZCuNPNAj7")
                .setOAuthAccessTokenSecret("wIYT5nnDMQtrNX8pIIPLrMI2sKcYbBwW3n8VHZZUkyoF6");
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        return twitter;
    }

    public static TwitterStream TW_createStreamTwitterFactory()
    {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("LDEplYQrMbg46wfaHbd0QY2Jm")
                .setOAuthConsumerSecret("4Hem6UWbrdFbiD8C9rBdSQyhELHdwl41NAZZUdLjjVHas8S5wX")
                .setOAuthAccessToken("2801646263-bFrNv8z6I3DHhb4DIcI1zWOBCTs8W5ZCuNPNAj7")
                .setOAuthAccessTokenSecret("wIYT5nnDMQtrNX8pIIPLrMI2sKcYbBwW3n8VHZZUkyoF6");
        TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
        return twitterStream;
    }

    public static void TW_showTwitterTimeline(Twitter twitter)
    {
        try
        {
            List<Status> status=twitter.getHomeTimeline();
            for(Status st:status)
            {
                System.out.println(st.getUser().getName()+"..........."+st.getText());
            }
        }catch (Exception e)
        {
            System.out.println("Error: "+e.getMessage());
        }
    }

    public static void TW_newPost(Twitter twitter,String Post)
    {
        try {
            twitter.updateStatus(Post);
        }catch (Exception e)
        {
            System.out.println("Error: "+e.getMessage());
        }

    }

    public static boolean TW_fetchInfo(Twitter twitter,String querystring,int numberOfTweets,String outputfileName,boolean SCshow)
    {
        Query query = new Query(querystring);

        long lastID = Long.MAX_VALUE;
        ArrayList<Status> tweets = new ArrayList<Status>();
        while (tweets.size () < numberOfTweets) {
            if (numberOfTweets - tweets.size() > 100)
                query.setCount(100);
            else
                query.setCount(numberOfTweets - tweets.size());
            try {
                QueryResult result = twitter.search(query);
                tweets.addAll(result.getTweets());
                System.out.println("Gathered " + tweets.size() + " tweets"+"\n");
                for (Status t: tweets)
                    if(t.getId() < lastID)
                        lastID = t.getId();
            }
            catch (TwitterException te) {
                System.out.println("Error : Couldn't connect: " + te);
                return false;
            }
            query.setMaxId(lastID-1);
        }
        for (int i = 0; i < tweets.size(); i++) {
            Status t = (Status) tweets.get(i);
            String user = t.getUser().getScreenName();
            String msg = t.getText();
            if(SCshow) {
                System.out.println(i + " USER: " + user + " wrote: " + msg + "\n");
            }
            _UTIL.TOOLES.write2file(msg,outputfileName,true);
        }
        return true;
    }

    public static   StatusListener listener1 = new StatusListener() {
        @Override
        public void onStatus(Status status) {
            System.out.println(status.getUser().getName() +" ##  "+status.getGeoLocation()+ " : " + status.getText());
            write2file(status.getText(),TW_Out_ResultFileName,true);
        }

        @Override
        public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}

        @Override
        public void onTrackLimitationNotice(int limit) {}

        @Override
        public void onScrubGeo(long user, long upToStatus) {}

        @Override
        public void onStallWarning(StallWarning warning) {}

        @Override
        public void onException(Exception e) {
            System.out.println("Error: "+e.getMessage());
        }
    };




    public static boolean TW_fetchSitesContents(String TW_Out_ResultFileName,int number_of_considered_sites)
    {
        try {
            String Source = _UTIL.TOOLES.readFileAsString(TW_Out_ResultFileName);
            List<String> extractedUrls = _UTIL.TOOLES.ExtractUrls(Source);
            int correctltRead_counter = 0;
            int total_counter = 0;
            for (String url : extractedUrls) {
                url = url.trim();
                total_counter++;
                _UTIL.TOOLES.write2file(url, "URLLISTS.TXT", true);
                String SiteText = _UTIL.TOOLES.readurl(url);
                if (SiteText != null && !SiteText.isEmpty()) {
                    SiteText = _UTIL.TOOLES.removeTags(SiteText);
                    System.out.println("Adding thecontent of " + url + " ...");
                    _UTIL.TOOLES.write2file(SiteText, TW_Out_ResultFileName, true);
                    correctltRead_counter++;
                } else {
                    System.out.println("Could not fetch from the " + url + ". The next site will be considered");
                }

                if (!(total_counter <= extractedUrls.size() && correctltRead_counter < number_of_considered_sites)) {
                    break;
                }
            }
            return true;
        }catch (Exception e)
        {
            System.out.println("Error: "+e.getMessage());
            return false;
        }
    }

    public static List<String> ExtractUrls(String text)
    {
        List<String> containedUrls = new ArrayList<String>();
        String urlRegex = "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
                + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
                + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find())
        {
            containedUrls.add(text.substring(urlMatcher.start(0),
                    urlMatcher.end(0)));
        }

        return containedUrls;
    }

    public static boolean FetchWebsites(String Sourcefile,int number_of_considered_sites,String Outputfile)
    {
        try {
            String Source = _UTIL.TOOLES.readFileAsString(Sourcefile);
            List<String> extractedUrls = _UTIL.TOOLES.ExtractUrls(Source);
            if(number_of_considered_sites==-1){number_of_considered_sites=extractedUrls.size(); System.out.println(number_of_considered_sites+" Will be gathered");}
            int correctltRead_counter = 0;
            int total_counter = 0;
            for (String url : extractedUrls) {
                url = url.trim();
                total_counter++;
                _UTIL.TOOLES.write2file(url, "URLLISTS.TXT", true);
                String SiteText = _UTIL.TOOLES.readurl(url);
                if (SiteText != null && !SiteText.isEmpty()) {
                    SiteText = _UTIL.TOOLES.removeTags(SiteText);
                    System.out.println("Adding thecontent of " + url + " ...");
                    _UTIL.TOOLES.write2file(SiteText, Outputfile, true);
                    correctltRead_counter++;
                } else {
                    System.out.println("Could not fetch from the " + url + ". The next site will be considered");
                }

                if (!(total_counter <= extractedUrls.size() && correctltRead_counter < number_of_considered_sites)) {
                    break;
                }
            }
            return true;
        }catch (Exception e)
        {
            System.out.println("Error: "+e.getMessage());
            return false;
        }
    }

}

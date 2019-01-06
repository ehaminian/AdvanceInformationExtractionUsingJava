
import twitter4j.Twitter;
import twitter4j.*;

public class twitterFetch {
    public static void main(String[] args) {
        try
        {
            Twitter twitter=_UTIL.TOOLES.TW_createTwitterFactory();
            TwitterStream twitterStream=_UTIL.TOOLES.TW_createStreamTwitterFactory();
            System.out.println("#####################################################");
            System.out.println("Accunt Name: "+twitter.getScreenName());
            System.out.println("##################################################");
            //_UTIL.TOOLES.TW_fetchInfo(twitter,TW_querystring,TW_numberOfTweets,TW_Out_ResultFileName,false);
            _UTIL.TOOLES.TW_fetchInfoStream( _UTIL.TOOLES.Keyworads, _UTIL.TOOLES.Location, _UTIL.TOOLES.Language,  _UTIL.TOOLES.isLocationUsed);
            if( _UTIL.TOOLES.FetchRelatedsites) { _UTIL.TOOLES.TW_fetchSitesContents( _UTIL.TOOLES.TW_Out_ResultFileName, _UTIL.TOOLES.number_of_considered_sites );}
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}

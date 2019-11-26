package com.mejesticpay.stputil.config;

import com.mejesticpay.stputil.Track;
import com.mejesticpay.util.JSONHelper;
import org.apache.commons.io.IOUtils;
import java.io.InputStream;
import java.util.HashMap;

public class TracksConfig
{
    private static HashMap<String,Track> tracksMap = new HashMap();
    static
    {
            try
            {
                InputStream is = TracksConfig.class.getResourceAsStream("/tracks.json");
                String jsonStr = IOUtils.toString(is,"UTF-8");
                Track[] tracks = JSONHelper.convertToObjectFromJson(jsonStr,Track[].class);
                for(Track track: tracks)
                {
                    tracksMap.put(track.getName(),track);
                    System.out.println("####### TRACKS !!!!!!!!");
                    System.out.println(track);
                }
            }catch(Exception e)
            {
                e.printStackTrace();
            }

    }

    public static HashMap<String, Track> getTracksMap() {
        return tracksMap;
    }


}

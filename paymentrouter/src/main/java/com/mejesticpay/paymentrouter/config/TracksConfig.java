package com.mejesticpay.paymentrouter.config;

import com.mejesticpay.paymentrouter.Track;
import com.mejesticpay.util.JSONHelper;
import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.util.HashMap;
@Configuration
public class TracksConfig
{
    @Bean
    public Tracks getTracks()
    {
        return new Tracks();
    }

    public static class Tracks
    {
        private HashMap<String,Track> tracksMap = new HashMap();

        Tracks()
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

        public HashMap<String, Track> getTracksMap() {
            return tracksMap;
        }
    }
}

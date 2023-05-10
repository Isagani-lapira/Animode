package com.example.animode;

public class MyAnime {
    private final String IMG_URL;
    private final String ANIME_NAME;
    private final String EPISODES;

    public MyAnime(String img_url, String anime_name, String episodes) {
        IMG_URL = img_url;
        ANIME_NAME = anime_name;
        EPISODES = episodes;
    }

    public String getIMG_URL() {
        return IMG_URL;
    }

    public String getANIME_NAME() {
        return ANIME_NAME;
    }

    public String getEPISODES() {
        return EPISODES;
    }
}

package com.kwave.android.httpurlconnection.domain;

/**
 * Created by kwave on 2017-06-13.
 */

public class Data
{
    // json은 변수명이 object명이 된다.
    private SearchPublicToiletPOIService SearchPublicToiletPOIService;

    public SearchPublicToiletPOIService getSearchPublicToiletPOIService ()
    {
        return SearchPublicToiletPOIService;
    }

    public void setSearchPublicToiletPOIService (SearchPublicToiletPOIService SearchPublicToiletPOIService)
    {
        this.SearchPublicToiletPOIService = SearchPublicToiletPOIService;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [SearchPublicToiletPOIService = "+SearchPublicToiletPOIService+"]";
    }
}
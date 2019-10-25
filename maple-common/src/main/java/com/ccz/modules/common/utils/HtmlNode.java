package com.ccz.modules.common.utils;

import lombok.Data;
import lombok.Getter;

@Getter
public class HtmlNode {
	private String webScrapId;
    private String title = "", subTitle = "";
    private final String url;
    private final String imageUrl;
    private String shortBody = "";
    
    private final int BODY_LIMIT = 100;
    
    private HtmlNode thisObj = null;
    private static int sec = 1;
    
    public HtmlNode() {
    	url = imageUrl = shortBody = null;
    }
    
    public HtmlNode(String url, String title, String body, String imageUrl) {
    	this.webScrapId = KeyGen.makeKey("webScrap:", sec++);
        this.url = url;
        this.imageUrl = imageUrl.split("\\?")[0];
        
        if(title.contains("-") == true)
        	splitTitle(title, "-");
        else if(title.contains("|") == true)
        	splitTitle(title, "\\|");
        else if(title.contains(":") == true)
        	splitTitle(title, ":");
        else if(title != null)
        	this.title = title;
        if(body.length()>0)
        	this.shortBody = body.length()<=100 ? body : body.substring(0, BODY_LIMIT);
        this.thisObj = this;
        
        title = SqlUtils.escape(title);
        subTitle = SqlUtils.escape(subTitle);
        shortBody = SqlUtils.escape(shortBody);
    }
    
    public boolean isEmpty() {
    	return thisObj==null;
    }
    
    private void splitTitle(String title, String div) {
    	String[] split = title.split(div, -1);
    	title = split[0].trim();
    	subTitle = split[1].trim();
    }
}

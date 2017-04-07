package com.justin.epicnews;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by lejus on 25/03/2017.
 */

public class News {
    private String title;
    private String description;
    private String link;
    private String category;
    private Date date;

    public News(String title, String description, String link, String category, String date) throws ParseException {
        setTitle(title);
        setDescription(description);
        setLink(link);
        setCategory(category);
        setDate(date);
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof News))
            return false;
        return (o.hashCode() == this.hashCode());

    }

    @Override
    public int hashCode() {
      return getTitle().hashCode();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(String date) throws ParseException{
        this.date = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss", Locale.US).parse(date);
    }

}

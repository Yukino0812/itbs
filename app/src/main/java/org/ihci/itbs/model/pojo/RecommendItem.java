package org.ihci.itbs.model.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Yukino Yukinoshita
 */

public class RecommendItem implements Cloneable, Serializable {

    private String title;
    private String content;
    private String source;
    private String link;
    private Date updateDate;

    public RecommendItem() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public RecommendItem clone() throws CloneNotSupportedException {
        return (RecommendItem) super.clone();
    }

}

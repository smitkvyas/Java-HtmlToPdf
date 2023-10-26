package org.htmltopdf.page;

public class Page {

    private String source;
    private String filePath;
    private PageType type;

    public Page(String source, PageType type) {
        this.source = source;
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public PageType getType() {
        return type;
    }

    public void setType(PageType type) {
        this.type = type;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
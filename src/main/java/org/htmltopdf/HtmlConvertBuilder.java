package org.htmltopdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import org.htmltopdf.page.*;

public class HtmlConvertBuilder {

    // TODO: 10/25/2023 convert more params to methods

    private boolean isInstallationAvailable = false;
    private final HtmlConvert htmlConvert;

    public HtmlConvertBuilder() {
        htmlConvert = new HtmlConvert();
    }

    /**
     * Finds wkhtmltopdf lib installation based on OS (windows/linux)
     * <p>
     * Use manuallySetPath() to manually set installation path
     * <p>
     * If autoDetect is not working, try following in cmd/terminal
     * <p>for windows : "where wkhtmltopdf"
     * <p>for linux   : "which wkhtmltopdf"
     *
     * @return -
     */
    public HtmlConvertBuilder autoDetect() {
        try {
            htmlConvert.findInstallation();
            isInstallationAvailable = true;
        } catch (Exception e) {
            throw new RuntimeException("\"WKHTMLTOPDF\" command was not found in your classpath. Check your installation or use manuallySetPath() method");
        }
        return this;
    }

    /**
     * Manually set installation path for wkhtmltopdf
     * <p>
     * Use autoDetect() to automatically detect installation path based on OS (windows/linux)
     *
     * @param installationPath - wkhtmltopdf installation path
     * @return -
     */
    public HtmlConvertBuilder manuallySetPath(String installationPath) {
        if (isNotNullOrEmpty(installationPath, "Installation path")) {
            htmlConvert.setInstallPath(installationPath);
            isInstallationAvailable = true;
        }
        return this;
    }

    /**
     * Fetches HTML content from a URL and adds to PDF
     *
     * @param url URL of website
     * @return -
     */
    public HtmlConvertBuilder addURLPages(String url) {
        if (isNotNullOrEmpty(url, "URL")) htmlConvert.addPage(url, PageType.url);
        return this;
    }

    /**
     * Adds HTML pages from all give file paths in sequence
     *
     * @param filePath html file paths to add to PDF  in sequence
     * @return -
     */
    public HtmlConvertBuilder addFilePages(String... filePath) {
        for (String path : filePath) {
            if (isNotNullOrEmpty(path, "File path", false)) htmlConvert.addPage(path, PageType.file);
        }
        return this;
    }

    /**
     * Adds HTML code as a org.htmltopdf.page into PDF
     *
     * @param htmlData html code string to add into PDF
     * @return -
     */
    public HtmlConvertBuilder addStringData(String htmlData) {
        if (isNotNullOrEmpty(htmlData, "HTML string")) htmlConvert.addPage(htmlData, PageType.htmlAsString);
        return this;
    }

    /**
     * Default A4 org.htmltopdf.page
     *
     * @param pageSize Set PDF org.htmltopdf.page size
     * @return -
     */
    public HtmlConvertBuilder setPageSize(PageSize pageSize) {
        htmlConvert.addParams("--org.htmltopdf.page-size", pageSize != null ? pageSize.name() : PageSize.A4.name());
        return this;
    }

    /**
     * Default portrait
     *
     * @param pageOrientation Set org.htmltopdf.page orientation
     * @return -
     */
    public HtmlConvertBuilder setPageOrientation(PageOrientation pageOrientation) {
        htmlConvert.addParams("--orientation", pageOrientation != null ? pageOrientation.name() : PageOrientation.PORTRAIT.name());
        return this;
    }

    /**
     * Default false
     *
     * @return -
     */
    public HtmlConvertBuilder hasToc() {
        htmlConvert.setHasToc(true);
        return this;
    }

    public byte[] convert() {
        if (isInstallationAvailable)
            return htmlConvert.convert();
        else
            throw new RuntimeException("Please use autoDetect() or manuallySetPath() method to get installation before calling convert()");
    }

    public boolean convert(String outputFilePath) {
        if (isInstallationAvailable) {
            byte[] convertedBytes = htmlConvert.convert();
            File file = new File(outputFilePath);
            try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                fileOutputStream.write(convertedBytes);
                return true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else
            throw new RuntimeException("Please use autoDetect() or manuallySetPath() method to get installation before calling convert()");
    }

    private boolean isNotNullOrEmpty(String data, String typeOfData, boolean throwException) {
        if (Objects.nonNull(data) && !data.isEmpty()) return true;
        else {
            if (throwException) throw new RuntimeException(typeOfData + " must not be null or empty");
            else return false;
        }
    }

    private boolean isNotNullOrEmpty(String data, String typeOfData) {
        return isNotNullOrEmpty(data, typeOfData, true);
    }

    public HtmlConvertBuilder disableJavaScript() {
        htmlConvert.addParams("--disable-javascript");
        return this;
    }

    public HtmlConvertBuilder doNotLoadImages() {
        htmlConvert.addParams("--no-images");
        return this;
    }

    public HtmlConvertBuilder disableExternalLinks() {
        htmlConvert.addParams("--disable-external-links");
        return this;
    }

    public HtmlConvertBuilder enablePlugins() {
        htmlConvert.addParams("--enable-plugins");
        return this;
    }

    public HtmlConvertBuilder generateGrayScale() {
        htmlConvert.addParams("--grayscale");
        return this;
    }

    public HtmlConvertBuilder generateLowQuality() {
        htmlConvert.addParams("--lowquality");
        return this;
    }

    /**
     * @param top    - top margin in 'mm'
     * @param bottom - bottom margin in 'mm'
     * @param left   - left margin in 'mm' - default 10mm
     * @param right  - right margin in 'mm' - default 10mm
     * @return -
     */
    public HtmlConvertBuilder setMargin(float top, float bottom, float left, float right) {
        htmlConvert.addParams("--margin-top", top + "mm");
        htmlConvert.addParams("--margin-bottom", bottom + "mm");
        htmlConvert.addParams("--margin-left", left + "mm");
        htmlConvert.addParams("--margin-right", right + "mm");
        return this;
    }

    public HtmlConvertBuilder setPDFTitle(String title) {
        htmlConvert.addParams("--title", title);
        return this;
    }

    public HtmlConvertBuilder allowLocalFileAccess() {
        htmlConvert.addParams("--enable-local-file-access");
        return this;
    }

    public HtmlConvertBuilder addCustomOption(String key, String... values) {
        htmlConvert.addParams(key, values);
        return this;
    }

    public HtmlConvertBuilder addTocOption(String key, String... values) {
        htmlConvert.addTocParams(key, values);
        return this;
    }

}

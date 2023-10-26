import org.htmltopdf.HtmlConvertBuilder;
import org.htmltopdf.page.PageOrientation;
import org.htmltopdf.page.PageSize;
import org.junit.Test;

public class ConversionTest {

    @Test
    public void fileConversionTest() {
        new HtmlConvertBuilder()
                .manuallySetPath("C:\\Program Files\\wkhtmltopdf\\bin\\wkhtmltopdf.exe")
                .addFilePages("C:\\Users\\Smit\\Desktop\\file.html")
                .setPageSize(PageSize.A4)
                .setPageOrientation(PageOrientation.PORTRAIT)
                .convert("D:\\out.pdf");
    }

    @Test
    public void multipleFileConversionTest() {
        new HtmlConvertBuilder()
                .manuallySetPath("C:\\Program Files\\wkhtmltopdf\\bin\\wkhtmltopdf.exe")
                .addFilePages("C:\\Users\\Smit\\Desktop\\file.html")
                .addURLPages("https://www.google.com")
                .addFilePages("C:\\Users\\Smit\\Desktop\\smit_launguage_test_html.html")
                .setPageSize(PageSize.A4)
                .setPageOrientation(PageOrientation.LANDSCAPE)
                .convert("D:\\out.pdf");
    }

    @Test
    public void tocTest() {
        new HtmlConvertBuilder()
                .manuallySetPath("C:\\Program Files\\wkhtmltopdf\\bin\\wkhtmltopdf.exe")
                .addURLPages("https://www.google.com")
                .setPageSize(PageSize.A4)
                .hasToc()
                .setPageOrientation(PageOrientation.LANDSCAPE)
                .convert("D:\\out.pdf");
    }

    @Test
    public void marginTest() {
        new HtmlConvertBuilder()
                .manuallySetPath("C:\\Program Files\\wkhtmltopdf\\bin\\wkhtmltopdf.exe")
                .addURLPages("https://www.google.com")
                .setPageSize(PageSize.A4)
                .setMargin(100, 100, 100, 100)
                .setPageOrientation(PageOrientation.LANDSCAPE)
                .convert("D:\\out.pdf");
    }

}

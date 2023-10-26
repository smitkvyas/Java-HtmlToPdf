# Java-HtmlToPdf

A Java based wrapper for the [WKHTMLTOPDF](https://wkhtmltopdf.org/) command line tool.

Requirements
------------
**[wkhtmltopdf](http://wkhtmltopdf.org/) must be installed and working on your system.**

Installation guide
------------------

To be added

Usage
-----

```
// AutoDetect lib installation
new HtmlConvertBuilder()
    
    // Lib installation detection
    .autoDetect()

    // Adding html pages in sequence
    .addFilePages("Path to input HTML file")
    .addFilePages("Path to second input HTML file")
    
    // Configure PDF page
    .setPageSize(PageSize.A4)
    .setPageOrientation(PageOrientation.PORTRAIT)
    .setMargin(100, 100, 100, 100)
    
    // convert and save document to ouput path
    .convert("Path to output PDF file");
```

```
// Manually provide lib installation path
new HtmlConvertBuilder()

    // Set lib installation directory path
    .manuallySetPath("Path to installation directory")
    
    // Adding html pages in sequence
    .addFilePages("Path to input HTML file")
    .addFilePages("Path to second input HTML file")
    
    // Configure PDF page
    .setPageSize(PageSize.A4)
    .setPageOrientation(PageOrientation.PORTRAIT)
    .setMargin(100, 100, 100, 100)
    
    // convert and save document to ouput path
    .convert("Path to output PDF file");
```

> Note : you can use .addCustomOption() method to include more [options](https://wkhtmltopdf.org/usage/wkhtmltopdf.txt) supported by "WKHTMLTOPDF"

Credit
------
To be added
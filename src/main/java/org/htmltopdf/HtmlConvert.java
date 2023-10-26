package org.htmltopdf;

import org.htmltopdf.page.Page;
import org.htmltopdf.page.PageType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Use org.htmltopdf.HtmlConvertBuilder for conversion
 */
public class HtmlConvert {

    private String installPath = "wkhtmltopdf";
    private List<Page> pageList;
    private HashMap<String, List<String>> params;
    private boolean hasToc = false;
    private HashMap<String, List<String>> tocParams;
    private List<Integer> successValues = List.of(0);
    private int timeout = 10;

    // TODO: 10/25/2023 set xvbf flow
    // TODO: 10/25/2023 set timeout
    // TODO: 10/25/2023 set custom success values

    /**
     * @return installation path of wkhtmltopdf lib based on OS (windows/linux)
     */
    protected void findInstallation() {
        try {
            String osName = System.getProperty("os.name").toLowerCase();
            String cmd = osName.contains("windows") ? "where.exe wkhtmltopdf" : "which wkhtmltopdf";
            Process p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
            String installationPath = new String(p.getInputStream().readAllBytes(), Charset.defaultCharset()).trim();
            if (installationPath.isEmpty())
                throw new RuntimeException("wkhtmltopdf command was not found in your classpath. " +
                        "Verify its installation or initialize wrapper configurations with correct path/to/wkhtmltopdf");

            this.installPath = installationPath;
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException("Failed while getting wkhtmltopdf executable.", e);
        }
    }

    /**
     * @param installPath path to wkhtmltopdf installed directory, for eg. /bin/wkhtmltopdf for linux
     * @return -
     */
    protected void setInstallPath(String installPath) {
        this.installPath = installPath;
    }

    protected void addPage(String source, PageType pageType) {
        if (pageList == null) pageList = new ArrayList<>();
        pageList.add(new Page(source, pageType));
    }

    protected void addParams(String key, String... values) {
        if (params == null) params = new HashMap<>();
        params.put(key, values.length > 0 ? Arrays.asList(values) : null);
    }

    protected void setHasToc(boolean hasToc) {
        this.hasToc = hasToc;
    }

    protected void addTocParams(String key, String... values) {
        if (tocParams == null) tocParams = new HashMap<>();
        tocParams.put(key, values.length > 0 ? Arrays.asList(values) : null);
    }

    protected byte[] convert() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        String[] command = getConversionCommand();

        try {
            Process process = Runtime.getRuntime().exec(command);
            Future<byte[]> inputStreamToByteArray = executorService.submit(() -> process.getInputStream().readAllBytes());
            Future<byte[]> outputStreamToByteArray = executorService.submit(() -> process.getErrorStream().readAllBytes());

            process.waitFor();

            if (!successValues.contains(process.exitValue())) {
                byte[] errorStream = getFuture(outputStreamToByteArray);
                throw new RuntimeException("Process (" + Arrays.toString(command) + ") exited with status code " + process.exitValue() + ":\n" + new String(errorStream));
            } else {
                System.out.println("Wkhtmltopdf output: " + new String(getFuture(outputStreamToByteArray)));
            }

            return getFuture(inputStreamToByteArray);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            executorService.shutdownNow();
            clearTempFiles();
        }
    }

    private byte[] getFuture(Future<byte[]> future) {
        try {
            return future.get(this.timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String[] getConversionCommand() {
        List<String> commands = new ArrayList<>();
        commands.add("\"" + installPath + "\"");

        params.forEach((k, v) -> {
            commands.add(k);
            if (v != null) commands.addAll(v);
        });

        if (hasToc) {
            commands.add("toc");
            if (Objects.nonNull(tocParams)) {
                tocParams.forEach((k, v) -> {
                    commands.add(k);
                    if (v != null) commands.addAll(v);
                });
            }
        }

        for (Page page : pageList) {
            switch (page.getType()) {
                case url:
                case file: {
                    commands.add(page.getSource());
                    break;
                }
                case htmlAsString: {
                    String tempFilePath = getTempFilePathForContent(page.getSource());
                    page.setFilePath(tempFilePath);
                    commands.add(tempFilePath);
                    break;
                }
            }
        }

        commands.add("-");

        System.out.println(commands);
        return commands.toArray(new String[commands.size()]);
    }

    private String getTempFilePathForContent(String htmlData) {
        try {
            var tempFile = File.createTempFile(UUID.randomUUID().toString(), ".html");
            var fileOutputStream = new FileOutputStream(tempFile);
            fileOutputStream.write(htmlData.getBytes(StandardCharsets.UTF_8));
            fileOutputStream.close();
            return tempFile.getAbsolutePath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void clearTempFiles() {
        pageList.stream().filter(page -> page.getType().equals(PageType.htmlAsString)).forEach(page -> {
            try {
                Files.deleteIfExists(Path.of(page.getFilePath()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
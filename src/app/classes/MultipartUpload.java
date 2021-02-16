package app.classes;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class MultipartUpload {
    private final String boundary;
    private HttpURLConnection httpURLConnection;
    private OutputStream outputStream;
    private PrintWriter printWriter;
    private static final String LINE_FEED = "\r\n";

    public MultipartUpload(String requestURL, String charset) throws IOException {
        boundary = "===" + System.currentTimeMillis() + "===";
        URL url = new URL(requestURL);
        httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setDoInput(true);
        httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        outputStream = httpURLConnection.getOutputStream();
        printWriter = new PrintWriter(new OutputStreamWriter(outputStream, charset), true);
    }

    public void addFile(String fieldName, File uploadFile) throws IOException {
        String fileName = uploadFile.getName();
        printWriter.append("--" + boundary).append(LINE_FEED);
        printWriter.append(
                "Content-Disposition: form-data; name=\"" + fieldName
                        + "\"; filename=\"" + fileName + "\"")
                .append(LINE_FEED);
        printWriter.append(
                "Content-Type: "
                        + URLConnection.guessContentTypeFromName(fileName))
                .append(LINE_FEED);
        printWriter.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
        printWriter.append(LINE_FEED);
        printWriter.flush();
    }

    public void writeFileBytes(byte[] bytes, int offset, int length)
            throws IOException {
        outputStream.write(bytes, offset, length);
    }

    public void finish() throws IOException {
        outputStream.flush();
        printWriter.append(LINE_FEED);
        printWriter.flush();

        printWriter.append(LINE_FEED).flush();
        printWriter.append("--" + boundary + "--").append(LINE_FEED);
        printWriter.close();

        // check server's status code first
        int status = httpURLConnection.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    httpURLConnection.getInputStream()));
            while (reader.readLine() != null) {
                // do nothing, but necessary to consume response from the server
            }
            reader.close();
            httpURLConnection.disconnect();
        } else {
            throw new IOException("Server returned non-OK status: " + status);
        }
    }
}

package app.classes;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class UploadTask {
    private String uploadURL;
    private File uploadFile;

    public interface Finish{
        void finish();
    }

    public UploadTask(String uploadURL, File uploadFile) {
        this.uploadURL = uploadURL;
        this.uploadFile = uploadFile;
    }

    public void upload(Finish finish){
        // TO-DO graficki interfejs za upload fileova :)
        new Thread(() -> {
            try{
                MultipartUpload util = new MultipartUpload(uploadURL,
                        "UTF-8");
                util.addFile("file", uploadFile);

                FileInputStream inputStream = new FileInputStream(uploadFile);
                byte[] buffer = new byte[4096];
                int bytesRead = -1;
                long totalBytesRead = 0;
                int percentCompleted = 0;
                long fileSize = uploadFile.length();

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    util.writeFileBytes(buffer, 0, bytesRead);
                    totalBytesRead += bytesRead;
                    percentCompleted = (int) (totalBytesRead * 100 / fileSize);
                    //setProgress(percentCompleted);
                }

                inputStream.close();
                util.finish();
                finish.finish();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();
    }

}
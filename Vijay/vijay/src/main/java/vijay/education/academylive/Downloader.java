package vijay.education.academylive;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;


public class Downloader {

		
			//	For < 9 Android API 
	    public static int  DownloadFile(String fileURL, File directory) {
	    	 int fileLength = 0;
	        try {

	        	OutputStream  f = new FileOutputStream(directory);
	            URL u = new URL(fileURL);
	            URLConnection  c = u.openConnection();
	            c.connect();
	             fileLength = c.getContentLength();
	            InputStream input = new BufferedInputStream(u.openStream());

	            byte data[] = new byte[1024];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    f.write(data, 0, count);
	            }
                f.flush();
	            f.close();
	            input.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
			return fileLength;

	    }


 //its work in >= 9 Android API.

@SuppressLint("NewApi") 
public static void file_download(String uRl, File directory,Context context) {

       try{

        if (!directory.exists()) {
        	directory.mkdirs();
        }

        DownloadManager mgr = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        Uri downloadUri = Uri.parse(uRl);
        DownloadManager.Request request = new DownloadManager.Request(
                downloadUri);

        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI
                        | DownloadManager.Request.NETWORK_MOBILE)
                 .setAllowedOverRoaming(false).setTitle("Brochure")
                .setDescription("Vijay Education Academy.")
                .setDestinationInExternalPublicDir("/Vijay", "Brochure.pdf");

        mgr.enqueue(request);
       }catch(Exception e)
       {
    	   e.printStackTrace();
       }

    }
	    
}

package com.link;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class Session {
    public String session_id = "none";
    HttpClient client = new DefaultHttpClient();

    public String readPage(String url_string){
        String result = "failed page load";
        try {
            URL send_url = new URL(url_string);
            InputStream input = send_url.openStream();
            StringBuilder buffer = new StringBuilder();

            byte read_buffer[] = new byte[256];
            int read_byte = 0;
            while (read_byte != -1) {
                read_byte = input.read(read_buffer);
                for (int bytes = 0; bytes < read_byte; bytes++) {
                    buffer.append((char) read_buffer[bytes]);
                }
            }
            result = buffer.toString();
        } catch (IOException e){
        }
        return result;
    }

    public boolean getFile(String url, String path){
        boolean success = true;
        try {
            URL dl_link = new URL(url);
            ReadableByteChannel rbc = Channels.newChannel(dl_link.openStream());
            FileOutputStream fos = new FileOutputStream(path);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        }
        catch(Exception e){
            System.out.println("download failed");
            success = false;
        }
        return success;
    }

    public void asyncReadPage(final String url){
        Thread client_thread = new Thread(){
            public void run(){
                readPage(url);
            }
        };
        client_thread.setDaemon(true);
        client_thread.start();
    }

    public String getBlobURL(String session_id) throws IOException{
        HttpGet get_request = new HttpGet("http://tuneusserv.appspot.com/blob/upload_blob.py?id=" + session_id);
        HttpResponse response = client.execute(get_request);
        HttpEntity entity = response.getEntity();
        InputStream in = entity.getContent();
        String str = "";
        while (true) {
            int ch = in.read();
            if (ch == -1)
                break;
            str += (char) ch;
        }
        return str;
    }

    public String getVideoTitle(String id){
        String title = "unknown";
        if(id.startsWith("yt")){
            id = id.split(";")[1];
        }
        String page_src = readPage(String.format("http://gdata.youtube.com/feeds/api/videos/%s?v=2&alt=jsonc&prettyprint=true", id));
        title = page_src.split("title\": \"")[1].split("\",")[0]; //lol this is so ghetto
        return title;
    }

    public String getUrl(String key) throws IllegalArgumentException{
        //Gets API urls by key
        String url = "";
        switch(key){
            case "CREATE_SESSION":
                url = "http://tuneusserv.appspot.com/create_session.py";
                break;
            case "SEND_MESSAGE":
                url = "http://tuneusserv.appspot.com/poll_chat.py";
                break;
            case "ADD_VIDEO":
                url = "http://tuneusserv.appspot.com/add_video.py";
                break;
            case "SEARCH_BY":
                url = "http://tuneusserv.appspot.com/search_by.py";
                break;
            case "CHECK_QUEUE":
                url = "http://tuneusserv.appspot.com/check_queue.py";
                break;
            case "CHECK_VERSION":
                url = "http://tuneusserv.appspot.com/update/latest_version.txt";
                break;
            case "GET_JAR":
                url = "http://tuneusserv.appspot.com/update/jars/";
                break;
            default:
                throw new IllegalArgumentException("Url key not valid");
        }
        return url;
    }

    public void uploadBlob(String file_path, String session_id, int song_length) throws IOException
    {
        String blob_url = getBlobURL(session_id);
        blob_url = blob_url.replace("\n", "");
        HttpPost post_request = new HttpPost(blob_url);

        File audio_file = new File(file_path);
        FileBody file_body = new FileBody(audio_file);
        MultipartEntityBuilder multipart = MultipartEntityBuilder.create();
        multipart.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipart.addPart("file", file_body);
        multipart.addTextBody("id", session_id);
        multipart.addTextBody("fi_name", audio_file.getName());
        multipart.addTextBody("length", Integer.toString(song_length));
        post_request.setEntity(multipart.build());
        HttpResponse response = client.execute(post_request);
        HttpEntity ent = response.getEntity();
        InputStream in = ent.getContent();
        String str = "";
        while (true) {
            int ch = in.read();
            if (ch == -1)
                break;
            str += (char) ch;
        }
    }
}

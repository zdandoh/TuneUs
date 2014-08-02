package com.link;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Session {
    public String readPage(String url_string){
        String result = "failed";
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
}

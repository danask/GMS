package com.gms.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class JSONHandler {

    // for sending params
    public static String putParamTogether(String fields[], String... values) {
        StringBuilder sb = new StringBuilder();


        // the key must have the same spelling and case when extracted by the web app
        for (int i = 0; i < fields.length; i++ ) {
            try {
                // https://developer.android.com/reference/java/net/URLEncoder
                values[i] = URLEncoder.encode(values[i], "UTF-8");
            } catch (UnsupportedEncodingException e) {

            }

            if (sb.length() > 0)
                sb.append("&"); // to separate each entry

            // add condition (where)
            sb.append(fields[i]).append("=").append(""+values[i]+"");
        }

        return sb.toString();
    }
}

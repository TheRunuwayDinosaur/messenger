package messengerClient;

import javafx.fxml.FXML;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class DataReceiver {

    public static final String SERVER_URL = "http://localhost:8080/";
    public static final String ERROR = "error";


    public static JSONObject doPostRequest(String address, List<NameValuePair> params) {


        try {


            HttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost(address);

// Request parameters and other properties.

            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

//Execute and get the response.
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                try (InputStream instream = entity.getContent()) {
                    JSONParser jsonParser = new JSONParser();

                    return (JSONObject) jsonParser.parse(
                            new InputStreamReader(instream, StandardCharsets.UTF_8));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            //e.printStackTrace();
        }


        return null;
    }





    public static String login(String login, String password) {
        List<NameValuePair> params = new ArrayList<>(2);
        params.add(new BasicNameValuePair("login", login));
        params.add(new BasicNameValuePair("passwd", password));
        JSONObject result = doPostRequest(SERVER_URL + "login", params);
        if (result != null) {
            if (result.get("code").toString().equals("200")){
                User user = User.getInstance();
                user.setLogin(login);
                user.setPasswd(password);
                user.setToken(result.get("token").toString());
                user.chats = new ArrayList<>();

            }
                return result.get("status").toString();


        }
        return ERROR;

    }

    public static String createChat(String token) {
        List<NameValuePair> params = new ArrayList<>(1);
        params.add(new BasicNameValuePair("token", token));
        JSONObject result = doPostRequest(SERVER_URL + "createChat", params);
        if (result != null) {
            if (result.get("code").toString().equals("200")){
                User user = User.getInstance();
                user.chats.add((result.get("chatId").toString()));
            }
            return result.get("status").toString();

        }
        return ERROR;
    }


    public static String addUserToChat(String token,String chatId,String userName) {
        List<NameValuePair> params = new ArrayList<>(3);
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("chatId",chatId));
        params.add(new BasicNameValuePair("userName", userName));
        JSONObject result = doPostRequest(SERVER_URL + "addUserToChat", params);
        if (result != null) {
            if (result.get("code").toString().equals("200")){
                User user = User.getInstance();
                if (result.get("chatId") != null)
                    user.chats.add((result.get("chatId").toString()));
            }
            return result.get("status").toString();

        }
        return ERROR;
    }
    public static String registration(String login, String password) {
        List<NameValuePair> params = new ArrayList<>(2);
        params.add(new BasicNameValuePair("login", login));
        params.add(new BasicNameValuePair("passwd", password));
        JSONObject result = doPostRequest(SERVER_URL + "register", params);
        if (result !=null) {
            if (result.get("code").toString().equals("200")){
                User user = User.getInstance();
                user.setLogin(login);
                user.setPasswd(password);
                user.setToken(result.get("token").toString());
                user.chats = new ArrayList<>();
            }
            return result.get("status").toString();


        }
        return ERROR;

    }

}

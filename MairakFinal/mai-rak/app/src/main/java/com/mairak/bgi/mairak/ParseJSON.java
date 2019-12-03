package com.mairak.bgi.mairak;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class ParseJSON {

    public static String[] location;


    public static final String JSON_ARRAY = "results";
    public static final String KEY_LOCATION = "address_components";
    public static final String KEY_LOCATION1 = "long_name";
    String[] s;

    private JSONArray users = null;

    private String json;

    public ParseJSON(String json){

        this.json = json;
    }

    protected void parseJSON(){
        JSONObject jsonObject=null;
        try {
            jsonObject = new JSONObject(json);

            users = jsonObject.getJSONArray(JSON_ARRAY);

            location = new String[users.length()];

            JSONObject jo = users.getJSONObject(0);
            JSONArray array= jo.getJSONArray(KEY_LOCATION);
            // JSONObject joo = array.getJSONObject(0);


            for(int i=0;i<users.length();i++){
                //JSONObject jo = users.getJSONObject(i);
                //JSONArray array= jo.getJSONArray(KEY_LOCATION);
                // location[i] = jo.getString(KEY_LOCATION);
                //location[i]=array[i];
                JSONObject joo = array.getJSONObject(i);

                location[i] = joo.getString(KEY_LOCATION1);




            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

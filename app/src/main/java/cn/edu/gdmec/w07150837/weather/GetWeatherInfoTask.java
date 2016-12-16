package cn.edu.gdmec.w07150837.weather;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by weiruibo on 12/15/16.
 */

public class GetWeatherInfoTask extends AsyncTask<String, Void, List<Map<String, Object>>> {

    private Activity context;
    private ProgressDialog progressDialog;
    private String errorMsg = "网络错误!";
    private ListView weather_info;

    private static String BASE_URL = "http://v.juhe.cn/weather/index?format=2&cityname=";
    private static String key = "&key=8f84518b532c613b61db0fc6b9051d24";


    public GetWeatherInfoTask(Activity context) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("正在获取天气,请稍后...");
        progressDialog.setCancelable(false);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(List<Map<String, Object>> maps) {
        super.onPostExecute(maps);
        progressDialog.dismiss();
        if (maps.size() > 0) {
            weather_info = (ListView) context.findViewById(R.id.weather_info);

            SimpleAdapter simpleAdapter = new SimpleAdapter(context, maps, R.layout.weather_item,
                    new String[]{"temperature", "weather", "date", "week", "weather_icon"}, new int[]
                    {R.id.temperature, R.id.weather, R.id.date
                            , R.id.week, R.id.weather_icon});
            weather_info.setAdapter(simpleAdapter);
        } else {
            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected List<Map<String, Object>> doInBackground(String... strings) {

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        try {

//            HttpClient httpClient = new DefaultHttpClient();

            String url = null;

            url = BASE_URL + URLEncoder.encode(strings[0], "UTF-8") + key;

            URL url1 = new URL(url);
            HttpURLConnection httpUrlConn = (HttpURLConnection) url1.openConnection();
//            HttpGet httpget = new HttpGet(url);
//            httpUrlConn.getResponseCode();
//            HttpResponse response = httpClient.execute(httpget);

            if (httpUrlConn.getResponseCode() == 200) {
                Log.d("text", "网络连接成功");
                BufferedReader read = new BufferedReader(new InputStreamReader(httpUrlConn.getInputStream()));
                String content = "";
                if ((content = read.readLine()) != null) {
                    Log.d("text", content);
                }
//                String jsonString = EntityUtils.toString(httpUrlConn.getContentEncoding(), "UTF-8");

                JSONObject jsondata = new JSONObject(content);

                if (jsondata.getInt("resultcode") == 200) {

                    JSONObject result = jsondata.getJSONObject("result");
                    JSONArray weatherList = result.getJSONArray("future");

                    for (int i = 0; i < 7; i++) {
                        Map<String, Object> item = new HashMap<String, Object>();

                        JSONObject weatObject = weatherList.getJSONObject(i);

                        item.put("temperature", weatObject.getString("temperature"));
                        item.put("weather", weatObject.getString("weather"));
                        item.put("date", weatObject.getString("date"));
                        item.put("week", weatObject.getString("week"));
                        item.put("wind", weatObject.getString("wind"));

                        JSONObject wid = weatObject.getJSONObject("weather_id");

                        int weather_icon = wid.getInt("fa");
                        item.put("weather_icon", WeathIcon.weather_icons[weather_icon]);
                        list.add(item);
                    }

                } else {
                    errorMsg = "非常抱歉,本应用暂不支持您所请求的城市!!";
                }

            } else {
                errorMsg = "网络错误,请检查手机是否开启了网络";
            }


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}

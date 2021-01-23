package com.example.pblflasktest;

import android.os.Handler;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.os.Looper.getMainLooper;

public class FileUploadUtils {
    public static void send2Server(File file){
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", file.getName(), RequestBody.create(MediaType.parse("image/*"), file))
                .build();

        Request request = new Request.Builder()
                .url("http://1.229.131.101:5000/detections")
                .addHeader("Content-Type", "multipart/form-data")
                .post(requestBody)
                .build();

        OkHttpClient client = new OkHttpClient();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("요청", "onFailure()");
                Log.e("error", String.valueOf(e));
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                //Log.e("onResponse", "Response Body is " + response.body().string());
                Log.e("onResponse()", "실행");

                new Handler(getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            JSONObject jsonObject = new JSONObject(response.body().string());
                            String response = jsonObject.getString("response");
                            JSONArray jsonArray1 = new JSONArray(response);
                            JSONObject jsonObject1 = jsonArray1.getJSONObject(0);
                            String detections = jsonObject1.getString("detections");
                            JSONArray jsonArray = new JSONArray(detections);

                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject subJSONObject =jsonArray.getJSONObject(i);
                                String boxes = subJSONObject.getString("boxes");
                                String Class = subJSONObject.getString("class");

                                Log.e("boxes", boxes);
                                Log.e("class", Class);

                            }



                            /*JSONObject jsonObject = new JSONObject(response.body().string());
                            JSONArray jArray = jsonObject.getJSONArray("response");
                            JSONArray jArray2 = jArray.getJSONArray(0);

                            for(int i=0;i<jArray.length();i++){
                                JSONObject obj = jArray2.getJSONObject(i);
                                String boxes = String.valueOf(obj.getJSONArray("boxes"));
                                String Class = obj.getString("class");
                                Log.e("boxes", boxes);
                                Log.e("class", Class);
                                //result += (boxes + "\n" + words + "\n\n");
                            }

                            //tv_result.setText(result);*/

                        }  /*catch (IOException e) {
                            e.printStackTrace();
                        } */catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });




            }
        });
    }
}


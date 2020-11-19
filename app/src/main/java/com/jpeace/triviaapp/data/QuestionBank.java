package com.jpeace.triviaapp.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.jpeace.triviaapp.controller.AppController;
import com.jpeace.triviaapp.model.OnGetDataListener;
import com.jpeace.triviaapp.model.Question;
import com.jpeace.triviaapp.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class QuestionBank {

    public static void getQuestions(String difficulty, int category, int amount, String type, final OnGetDataListener onGetDataListener){
        StringBuilder builder=new StringBuilder("https://opentdb.com/api.php?");
        builder.append("amount=").append((amount==0?50:(Math.min(amount, 50))));
        builder.append("&category=").append(category);
        if (difficulty!=null) builder.append("&difficulty=").append(difficulty);
        if (type!=null) builder.append("&type=").append(type);

        Log.d("TADA: ", builder.toString());

        JsonObjectRequest mRequest=new JsonObjectRequest(
                Request.Method.GET, builder.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            ArrayList<Question> mQuestions=new ArrayList<>();
                            JSONArray jsonArray=response.getJSONArray(Util.RES);
                            Question question;
                            int length;

                            for (int i=0; i<jsonArray.length(); i++) {
                                JSONObject object=jsonArray.getJSONObject(i);
                                question=new Question(
                                        object.getString(Util.CATEGORY),
                                        object.getString(Util.TYPE),
                                        object.getString(Util.DIFFICULTY),
                                        object.getString(Util.QUESTION),
                                        object.getString(Util.CORRECT_ANS)
                                );

                                JSONArray array=object.getJSONArray(Util.INCORRECT_ANS);
                                length=array.length();
                                String[] incorrect_answer=new String[length];
                                for (int j=0; j<length; j++){
                                    incorrect_answer[j]=array.getString(j);
                                }
                                question.setIncorrectAnswer(incorrect_answer);
                                mQuestions.add(question);

                            }

                            if (onGetDataListener!=null) onGetDataListener.onDone(mQuestions);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (onGetDataListener!=null) onGetDataListener.onError(error.getMessage());
                    }
                }
        );
        AppController.getInstance().addRequest(mRequest);
    }
}

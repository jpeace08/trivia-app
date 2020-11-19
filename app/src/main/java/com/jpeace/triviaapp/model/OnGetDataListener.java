package com.jpeace.triviaapp.model;

import java.util.ArrayList;

public interface OnGetDataListener {
    void onDone(ArrayList<Question> questions);
    void onError(String message);
}

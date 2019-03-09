package hanas.dnidomatury.converters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import hanas.dnidomatury.model.matura.Exam;
import hanas.dnidomatury.model.matura.ExamsList;

public class  GithubTypeConverters {



    public static <T> String someObjectListToString(T someObjects) {
        Gson gson = new Gson();
        return gson.toJson(someObjects);
    }
}
package hanas.dnidomatury.converters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import hanas.dnidomatury.matura.task.Task;

public class GithubTypeConvertersTask {

    private static final Gson gson = new Gson();

    public static List<Task> stringToSomeObjectList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Task>>() {}.getType();
        return gson.fromJson(data, listType);
    }

    public static String someObjectListToString(List<Task> someObjects) {
        Type listType = new TypeToken<List<Task>>() {}.getType();
        return gson.toJson(someObjects, listType);
    }
}
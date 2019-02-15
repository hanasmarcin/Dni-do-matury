package hanas.dnidomatury.converters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import hanas.dnidomatury.matura.Matura;

public class  GithubTypeConverters {

    private static final Gson gson = new Gson();

    public static List<Matura> stringToSomeObjectList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Matura>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    public static String someObjectListToString(List<Matura> someObjects) {
        return gson.toJson(someObjects);
    }
}
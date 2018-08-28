package hanas.dnidomatury;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GithubTypeConverters {

    static Gson gson = new Gson();

    public static List<Matura> stringToSomeObjectList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Matura>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    public static String someObjectListToString(List<Matura> someObjects) {
        return gson.toJson((List<Matura>)someObjects);
    }
}
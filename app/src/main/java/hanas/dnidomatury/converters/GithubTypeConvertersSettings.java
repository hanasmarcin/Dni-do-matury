package hanas.dnidomatury.converters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import hanas.dnidomatury.matura.Matura;
import hanas.dnidomatury.settingsActivity.SettingsData;

public class GithubTypeConvertersSettings {

    private static final Gson gson = new Gson();

    public static SettingsData stringToSomeObject(String data) {
        if (data == null) {
            return null;
        }

        Type objType = new TypeToken<SettingsData>() {}.getType();

        return gson.fromJson(data, objType);
    }

    public static String someObjectListToString(SettingsData someObject) {
        return gson.toJson((SettingsData)someObject);
    }
}
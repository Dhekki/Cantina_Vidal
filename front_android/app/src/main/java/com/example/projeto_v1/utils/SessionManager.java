package com.example.projeto_v1.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Context context;

    private static final String PREF_NAME = "CantinaPrefs";
    private static final String KEY_ACCESS_TOKEN = "jwt_token";
    private static final String KEY_USER_NAME = "user_name";

    public SessionManager(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    /**
     * Salva o token de acesso e o nome do usuário após o login
     */
    public void saveAuthToken(String token, String name) {
        editor.putString(KEY_ACCESS_TOKEN, token); // Salva o token "puro"
        editor.putString(KEY_USER_NAME, name);
        editor.apply(); // Salva assincronamente
    }

    /**
     * Recupera o token salvo
     * Retorna null se não existir
     */
    public String fetchAuthToken() {
        return prefs.getString(KEY_ACCESS_TOKEN, null);
    }

    /**
     * Recupera o nome do usuário salvo
     */
    public String fetchUserName() {
        return prefs.getString(KEY_USER_NAME, "");
    }

    /**
     * Limpa a sessão (Logout)
     */
    public void clearSession() {
        editor.clear();
        editor.apply();
    }
}
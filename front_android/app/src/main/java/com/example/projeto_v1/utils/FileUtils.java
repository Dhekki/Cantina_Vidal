package com.example.projeto_v1.utils;



import android.content.Context;

import android.database.Cursor;

import android.net.Uri;

import android.provider.OpenableColumns;

import android.util.Log;



import java.io.File;

import java.io.FileOutputStream;

import java.io.InputStream;

import java.io.OutputStream;



public class FileUtils {



    // Método principal: Copia o conteúdo da URI para um arquivo temporário

    public static File getFileFromUri(Context context, Uri uri) {

        try {

            // 1. Descobrir o nome do arquivo (opcional, mas bom para debug)

            String fileName = getFileName(context, uri);

            // 2. Criar um arquivo temporário no cache do app

            File tempFile = new File(context.getCacheDir(), fileName != null ? fileName : "temp_image.jpg");

            // 3. Abrir streams para copiar os dados

            InputStream inputStream = context.getContentResolver().openInputStream(uri);

            OutputStream outputStream = new FileOutputStream(tempFile);

            if (inputStream == null) return null;



            // 4. Copiar bytes

            byte[] buffer = new byte[4 * 1024]; // buffer de 4kb

            int read;

            while ((read = inputStream.read(buffer)) != -1) {

                outputStream.write(buffer, 0, read);

            }

            outputStream.flush();

            outputStream.close();

            inputStream.close();

            return tempFile;



        } catch (Exception e) {

            Log.e("FileUtils", "Erro ao converter URI para File", e);

            return null;

        }

    }



    // Helper para pegar o nome original do arquivo na galeria

    private static String getFileName(Context context, Uri uri) {

        String result = null;

        if (uri.getScheme().equals("content")) {

            try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {

                if (cursor != null && cursor.moveToFirst()) {

                    int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);

                    if (index >= 0) {

                        result = cursor.getString(index);

                    }

                }

            }

        }

        if (result == null) {

            result = uri.getPath();

            int cut = result.lastIndexOf('/');

            if (cut != -1) {

                result = result.substring(cut + 1);

            }

        }

        return result;

    }

}
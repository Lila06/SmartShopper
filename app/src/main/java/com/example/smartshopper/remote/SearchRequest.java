package com.example.smartshopper.remote;

import android.util.Log;

import com.example.smartshopper.remote.RemoteDatabaseAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class SearchRequest implements Callback<String> {

    private static final String TAG = "SearchRequest";
    private static final String BASE_URL = "http://opengtindb.org/";

    private RemoteDatabaseAPI databaseAPI;
    private ResultCallback resultCallback;

    public SearchRequest(ResultCallback callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        databaseAPI = retrofit.create(RemoteDatabaseAPI.class);
        resultCallback = callback;
    }

    public void search(String ean) {
        Call<String> call = databaseAPI.searchProduct(ean);
        call.enqueue(this);
    }


    @Override
    public void onResponse(Call<String> call, Response<String> response) {
        Log.e("Debug", "search response: successful: " + response.isSuccessful() + " >>" + response.body());

        ErrorCode errorCode = extractErrorCode(response.body());
        if (errorCode == ErrorCode.CODE_0) {
            resultCallback.onResponse(response.body());
        } else {
            resultCallback.onResponseError(errorCode, response.body());
        }
    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {
        Log.e("Debug", "search error: ", t);
        resultCallback.onResponseError(ErrorCode.CODE_15, "");
    }

    private ErrorCode extractErrorCode(String response) {
        for (String responsePart : response.split("\\n")) {
            if (responsePart.trim().startsWith("error")) {
                String codeIdAsString = (responsePart.split("=")[1]);
                return ErrorCode.fromCodeId(codeIdAsString);
            }
        }
        return ErrorCode.CODE_16;
    }

    public interface ResultCallback {
        void onResponse(String response);

        void onResponseError(ErrorCode code, String response);
    }

    public enum ErrorCode {
        CODE_0(0, "OK ", "Operation war erfolgreich"),
        CODE_1(1, "not found", "die EAN konnte nicht gefunden werden"),
        CODE_2(2, "checksum ", "die EAN war fehlerhaft (Checksummenfehler)"),
        CODE_3(3, "EAN-format", "die EAN war fehlerhaft (ungültiges Format / fehlerhafte Ziffernanzahl)"),
        CODE_4(4, "not a global, unique EAN", "es wurde eine für interne Anwendungen reservierte EAN eingegeben (In-Store, Coupon etc.)"),
        CODE_5(5, "access limit exceeded", "Zugriffslimit auf die Datenbank wurde überschritten"),
        CODE_6(6, "no product name", "es wurde kein Produktname angegeben"),
        CODE_7(7, "product name too long", "der Produktname ist zu lang (max. 20 Zeichen)"),
        CODE_8(8, "no or wrong main category id", "die Nummer für die Hauptkategorie fehlt oder liegt außerhalb des erlaubten Bereiches"),
        CODE_9(9, "no or wrong sub category id", "die Nummer für die zugehörige Unterkategorie fehlt oder liegt außerhalb des erlaubten Bereiches"),
        CODE_10(10, "illegal data in vendor field", " unerlaubte Daten im Herstellerfeld"),
        CODE_11(11, "illegal data in description field", "unerlaubte Daten im Beschreibungsfeld"),
        CODE_12(12, "data already submitted", "Daten wurden bereits übertragen"),
        CODE_13(13, "queryid missing or wrong", "die UserID/queryid fehlt in der Abfrage oder ist für diese Funktion nicht freigeschaltet"),
        CODE_14(14, "unknown command", "es wurde mit dem Parameter \"cmd\" ein unbekanntes Kommando übergeben"),

        CODE_15(15, "search request error", "Fehler bei der Ausführung der Suchanfrage"),
        CODE_16(16, "response extraction error", "Fehler bei der Extrahierung der Daten aus dem Suchergebnis"),
        ;

        public int id;
        public String title;
        public String description;

        ErrorCode(int id, String title, String description) {
            this.id = id;
            this.title = title;
            this.description = description;
        }

        public static ErrorCode fromCodeId(String codeIdAsString) {
            try {
                int codeId = Integer.valueOf(codeIdAsString);
                for (ErrorCode code : values()) {
                    if (code.id == codeId) {
                        return code;
                    }
                }
            } catch (NumberFormatException e) {
                Log.e(TAG, "code conversion failed >>" + codeIdAsString + "<<", e);
            }

            return ErrorCode.CODE_16;
        }
    }
}

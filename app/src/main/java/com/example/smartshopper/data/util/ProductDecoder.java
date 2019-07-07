package com.example.smartshopper.data.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ProductDecoder {

    private static final String TAG = "ProductDecoder";

    public static List<String> decodePackaging(String codeAsString) {
        List<String> result = new ArrayList<>();
        int code = 0;

        try {
            code = Integer.valueOf(codeAsString);
        } catch (NumberFormatException e) {
            Log.e(TAG, "Code conversion failed >> " + codeAsString + " <<", e);
        }

        PackagingCode[] packagingCodes = PackagingCode.values();
        for (int i = packagingCodes.length - 1; i >= 0; i--) {
            PackagingCode packagingCode = packagingCodes[i];

            if (code > 0 && code >= packagingCode.code) {
                result.add(packagingCode.description);
                code -= packagingCode.code;
            }
        }

        return result;
    }

    public static List<String> decodeAllergen(String codeAsString) {
        List<String> result = new ArrayList<>();
        int code = 0;

        try {
            code = Integer.valueOf(codeAsString);
        } catch (NumberFormatException e) {
            Log.e(TAG, "Code conversion failed >> " + codeAsString + " <<", e);
        }

        AllergenCode[] allergenCodes = AllergenCode.values();
        for (int i = allergenCodes.length - 1; i >= 0; i--) {
            AllergenCode allergenCode = allergenCodes[i];

            if (code > 0 && code >= allergenCode.code) {
                result.add(allergenCode.description);
                code -= allergenCode.code;
            }
        }

        return result;
    }


    private enum PackagingCode {
        CODE_1(1, "die Verpackung besteht überwiegend aus Plastik"),
        CODE_2(2, "die Verpackung besteht überwiegend aus Verbundmaterial"),
        CODE_4(4, "die Verpackung besteht überwiegend aus Papier/Pappe"),
        CODE_8(8, "die Verpackung besteht überwiegend aus Glas/Keramik/Ton"),
        CODE_16(16, "die Verpackung besteht überwiegend aus Metall"),
        CODE_32(32, "ist unverpackt"),
        CODE_64(64, "die Verpackung ist komplett frei von Plastik"),
        CODE_128(128, "Artikel ist übertrieben stark verpackt"),
        CODE_256(256, "Artikel ist angemessen sparsam verpackt"),
        CODE_512(512, "Pfandsystem / Mehrwegverpackung"),
        ;

        int code;
        String description;

        PackagingCode(int code, String description) {
            this.code = code;
            this.description = description;
        }
    }

    private enum AllergenCode {
        CODE_1(1, "laktosefrei"),
        CODE_2(2, "koffeeinfrei"),
        CODE_4(4, "diätetisches Lebensmittel"),
        CODE_8(8, "glutenfrei"),
        CODE_16(16, "fruktosefrei"),
        CODE_32(32, "BIO-Lebensmittel nach EU-Ökoverordnung"),
        CODE_64(64, "fair gehandeltes Produkt nach FAIRTRADE™-Standard"),
        CODE_128(128, "vegetarisch"),
        CODE_256(256, "vegan"),
        CODE_512(512, "Warnung vor Mikroplastik"),
        CODE_1024(1024, "Warnung vor Mineralöl"),
        CODE_2048(2048, "Warnung vor Nikotin"),
        ;

        int code;
        String description;

        AllergenCode(int code, String description) {
            this.code = code;
            this.description = description;
        }
    }

}

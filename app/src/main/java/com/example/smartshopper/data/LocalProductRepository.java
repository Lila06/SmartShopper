package com.example.smartshopper.data;

import android.annotation.SuppressLint;

import java.util.HashMap;
import java.util.Map;

public class LocalProductRepository {

    @SuppressLint("UseSparseArrays")
    private Map<Long, String> localDb = new HashMap<>();

    public LocalProductRepository() {
        setupLocalDB();
    }

    public String findProduct(Long ean) {
        return localDb.get(ean);
    }

    private void setupLocalDB() {
        localDb.put(5060223965147L, "error=0\n" +
                "    ---\n" +
                "    asin=B00G26XWDI\n" +
                "    name=\n" +
                "    detailname=MediaDevil Simdevil 3-in-1 SIM-Karten Adapter Set (Nano/Mikro/Standard)\n" +
                "    vendor=MediaDevil\n" +
                "    maincat=\n" +
                "    subcat=\n" +
                "    maincatnum=-1\n" +
                "    subcatnum=\n" +
                "    contents=\n" +
                "    pack=\n" +
                "    origin=Gro�britannien\n" +
                "    descr=Unser Simdevil Adapter Set kann Ihre Nano SIM-Karte in eine Mikro SIM- oder Standard SIM-Karte konvertieren, oder eine Mikro SIM- in eine Standard SIM-Karte. Dies ist vollkommen reversibel und erlaubt Ihnen zwischen mehreren Ger�ten zu wechseln. Simdevil ist perfekt, wenn Sie Ihre moderne (d.h. kleine) SIM-Karte in eine gr�ssere Karte konvertieren m�chten, z.B. wenn Sie im Urlaub oder auf einem Festival ein �lteres Handy verwenden wollen oder Ihr prim�res Handy defekt ist. Wie auch f�r andere Kartenadapter empfehlen wir, Simdevil nicht leer in Ihr Ger�t einzusetzen und die Adapter nur mit vom Hersteller vorgefertigten SIM-Karten zu verwenden. Jeder Adapter wurde durch CNC-Maschinen mit pr�ziser Passform produziert und inkludiert ein St�ck Halbselbstklebepapier, das zus�tzlich sicherstellt, dass die SIM-Karte fest in Position gehalten wird, wenn Simdevil eingesetzt/entfernt wird. 50% billiger als die Mediamarkt Version\n" +
                "    name_en=\n" +
                "    detailname_en=\n" +
                "    descr_en=\n" +
                "    validated=50 %\n" +
                "    ---");
    }

}

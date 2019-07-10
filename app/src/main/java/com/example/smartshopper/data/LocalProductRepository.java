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
        localDb.put(4038375024372L, "error=0\n" +
                "    ---\n" +
                "    asin=B000NWCNJG\n" +
                "    name=Reisdrink\n" +
                "    detailname=Reis-Drink mit Calcium\n" +
                "    vendor=NATUMI\n" +
                "    maincat=Getr�nke, Alkohol\n" +
                "    subcat=Frucht-und Gem�ses�fte\n" +
                "    maincatnum=11\n" +
                "    subcatnum=3\n" +
                "    contents=425\n" +
                "    pack=2\n" +
                "    origin=Deutschland\n" +
                "    descr=Inhalt: 1l\n" +
                "    name_en=\n" +
                "    detailname_en=\n" +
                "    descr_en=\n" +
                "    validated=100 %\n" +
                "    ---");

        localDb.put(8001620001004L, "error=0\n" +
                "    ---\n" +
                "    asin=\n" +
                "    name=Grapefruitlimonade\n" +
                "    detailname=Guizza pompelmo\n" +
                "    vendor=Aqua Minerale San Benedetto S.p.A.\n" +
                "    maincat=Getr�nke, Alkohol\n" +
                "    subcat=Limonaden\n" +
                "    maincatnum=11\n" +
                "    subcatnum=7\n" +
                "    contents=387\n" +
                "    pack=257\n" +
                "    origin=Italien\n" +
                "    descr=\n" +
                "    name_en=\n" +
                "    detailname_en=\n" +
                "    descr_en=\n" +
                "    validated=50 %\n" +
                "    ---");

        localDb.put(717285397748L, "error=0\n" +
                "    ---\n" +
                "    asin=\n" +
                "    name=Gin\n" +
                "    detailname=Albrecht Gin 0,5 L 43,5 % vol Bio\n" +
                "    vendor=Spirituosen Manufaktur Beelitz\n" +
                "    maincat=Getr�nke, Alkohol\n" +
                "    subcat=Spirituosen\n" +
                "    maincatnum=11\n" +
                "    subcatnum=10\n" +
                "    contents=416\n" +
                "    pack=8\n" +
                "    origin=USA und Kanada\n" +
                "    descr=Eine Erinnerung an den Gr�nder der Mark Brandenburg, Albrecht der B�r ~ \\n\\n� 15 handverlesene Zutaten aus kontrolliert biologischem Anbau \\n�Nach traditionellen Brennverfahren schonend in kleinen Chargen von Hand destilliert und abgef�llt \\n�Ohne k�nstliche Zus�tze und ohne Zusatz von Zucker \\n�Frische-reife �pfel, frische Zitronen und aromatischer Wacholder pr�gen den Geschmack \\n�Geschmacksintensiv und ausgewogen. Damit unser Albrecht im Cocktail nicht untergeht, er aber auch pur ein Genu� ist.\n" +
                "    name_en=\n" +
                "    detailname_en=\n" +
                "    descr_en=\n" +
                "    validated=100 %\n" +
                "    ---");

        localDb.put(4006040054429L, "error=0\n" +
                "    ---\n" +
                "    asin=B00KGRCKH8\n" +
                "    name=Kokosriegel\n" +
                "    detailname=Kokos Happen Zartbitter\n" +
                "    vendor=Rapunzel Naturkost GmbH\n" +
                "    maincat=S�sswaren, Snacks\n" +
                "    subcat=Getreide, Schokoriegel, Waffeln\n" +
                "    maincatnum=20\n" +
                "    subcatnum=5\n" +
                "    contents=416\n" +
                "    pack=257\n" +
                "    origin=Deutschland\n" +
                "    descr=Die Zutaten f�r die F�llung werden zu einer homogenen Masse gemischt, �ber Walzen ausgeformt, in kleine St�cke geschnitten und mit Zartbitterschokolade �berzogen.\\n\\nInhalt 50g\n" +
                "    name_en=\n" +
                "    detailname_en=\n" +
                "    descr_en=\n" +
                "    validated=100 %\n" +
                "    ---");

        localDb.put(4100060012220L, "error=0\n" +
                "    ---\n" +
                "    asin=\n" +
                "    name=Multivitaminsaft\n" +
                "    detailname=WeserGold, Multivitamin - Fruchtsaftgetr�nk\n" +
                "    vendor=WeserGold\n" +
                "    maincat=Getr�nke, Alkohol\n" +
                "    subcat=Frucht-und Gem�ses�fte\n" +
                "    maincatnum=11\n" +
                "    subcatnum=3\n" +
                "    contents=27\n" +
                "    pack=0\n" +
                "    origin=Deutschland\n" +
                "    descr=WeserGold Multivitamin, Multivitamin-12-Fruchtsaftgetr�nk,\\n\\nTeilweise aus Fruchtsaftkonzentraten, mit S��ungsmitteln, Fruchtgehalt: 20%, kalorienarm, angereichert mit Vitaminen\n" +
                "    name_en=\n" +
                "    detailname_en=\n" +
                "    descr_en=\n" +
                "    validated=50 %\n" +
                "    ---");
    }

}

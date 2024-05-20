# Manuális kód átvizsgálás az alkalmazás egy részére

## Cél
A manuális kód átvizsgálás célja a projekt kódminőségének javítása, hibák azonosítása, valamint a kód olvashatóságának és karbantarthatóságának növelése volt.

## Talált Problémák
Az átvizsgálás során több problémával is szembesültünk, ezek magukba foglalták:

- Túl hosszú/bonyolult függvények (magas kognitív komplexitás)
- Kódismétlések
- Potenciális veszélyforrások
- Felesleges kikommentezett kódrészek
- Nem implementált metódusok és tesztek
- Interfész implementációs hibák

Az ezután alkalmazott statikus analízis eszköz, azaz a SonarCloud is igazolta ezeket a problémákat, melyek számottevő, kritikus részét kijavítottuk.

# Statikus analízis eszköz futtatása és jelzett hibák átnézése

## Eszköz
A kód automatikus átvizsgálásához a SonarCloudot alkalmaztuk. Ez először 30 súlyos hibát, 57 közepes súlyú hibát, és 74 alacsonyat detektált.

## Hibajavítási Sorrend
Ez a súlyossági sorrend határozta meg azt, hogy a hibákat milyen sorrendben kezdtük el javítani. Elsősorban a legsúlyosabb hibák kijavítására koncentráltunk. Ezeket a következő csoportokba lehet sorolni:

- Konstans bevezetése string többszöri ismétlése helyett
- Random objektum újrafelhasználása ahelyett, hogy mindig újat hozunk létre belőle
- File bezárása try-with-resources eszközzel
- Függvények refaktorálása, optimalizálása, egyszerűsítése/rövidítése, szétbontása
- Üres tesztek implementálása, assertionok hozzáadása
- Serializable interfészt implementáló osztályban lévő adattagok szerializálhatóvá tétele
- Felesleges, sosem lefutó ágak törlése a kódból
- Azonos nevű változó átnevezése osztály tulajdonságban és függvény scope-ban
- Feleslegesen újradefiniált getterek, metódusok megszüntetése leszármazott osztályokban (pl.: `getId()` függvény `Field`-ben és leszármazott `Pipe`-ban is)

Miután kijavítottuk a kritikus hibákat, a Reliability problémához kapcsolódó közepes és enyhe hibák javításával folytattuk.

## Biztonsági Hibák
Összesen 10 Security típusú hibánk volt, amiből 9 a `Random` osztály használatával volt kapcsolatos, 1 pedig azzal, hogy `FileWriter` megnyitása esetén a kivétel teljes stack trace-ét kiírtuk a konzolra. Mivel nem volt biztonsági aspektusa a szoftvernek, ezért jeleztük a SonarCloud oldalán, hogy az első 9 nem probléma, az utolsót pedig kijavítottuk úgy, hogy a `printStackTrace()` helyett megfelelő logolási mechanizmust alkalmaztunk.

# Eredmények
Az átvizsgálás és hibajavítás eredményeként a kód átláthatósága és karbantarthatósága javult. Több helyen átszerveztük, refaktoráltuk a kódot, hogy optimálisabb, olvashatóbb és könnyebben érthető legyen. A kódismétlések előfordulását is csökkentettük, valamint biztonsági hibákat is javítottunk.

A SonarCloud átlátható felülete kiváló volt a hibák javításának egyszerű kiosztására a csapattagok között és annak nyomon követésére, hogy mely hibák lettek javítva és melyek várnak még javításra. A már meglévő GitHub repositoryba való egyszerű és gyors bekötés után az eszköz nagyon hasznosnak bizonyult a rejtett hibák felfedésében, a kódminőség javításában, a biztonság és a karbantarthatóság növelésében.

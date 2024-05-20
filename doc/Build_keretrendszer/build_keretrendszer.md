# Build Keretrendszer Kialakítása

## Cél
A projekt automatikus fordításának és buildelésének megvalósítása a Github Actions segítségével.

## Megvalósítás

### 1. pom.xml Létrehozása

A projekt alapvető konfigurációs fájljának, a pom.xml-nek a létrehozása, amely tartalmazza a szükséges függőségeket és pluginokat. Ennek keretében az alábbi lépéseket végeztük el:

- Külső Függőségek Hozzáadása: Meghatároztuk a projekt számára szükséges külső könyvtárakat és modulokat, mint például a JUnit a teszteléshez és a Maven központi könyvtárából származó egyéb szükséges komponenseket.
- Verziók Kikeresése: A stabil és megfelelően működő függőségek verzióinak azonosítása és használata. Különösen figyeltünk arra, hogy az egyes verziók kompatibilisek legyenek egymással és a projekttel.

### 2. Teszteléshez Szükséges Pluginok Beállítása

A tesztelési környezet kialakításához a következő pluginokat konfiguráltuk a pom.xml-ben:

- Surefire Plugin: A Maven Surefire plugin beállítása, amely lehetővé teszi a tesztek futtatását különböző konfigurációkkal.

### 3. Forráskód és Tesztkód Szervezése

A projekt kódjának megfelelő csomagokba szervezése annak érdekében, hogy az automatikus fordítás és buildelés zökkenőmentesen működjön:

- Forráskód Szervezése: A fő programkód rendezése logikus csomagokba, biztosítva, hogy a modulok jól elkülönüljenek és átláthatóak legyenek.
- Tesztkód Szervezése: A tesztkódok elkülönítése a forráskódtól, külön tesztcsomagok létrehozása és a megfelelő mappa struktúra kialakítása.

### 4. Github Actions Létrehozása

Az automatikus buildelési folyamat beállítása a Github Actions segítségével:

- Workflow Fájl Létrehozása: Egy .github/workflows könyvtárban elhelyezett YAML fájl segítségével meghatároztuk a build és tesztelési folyamat lépéseit.
Lépések Meghatározása: A workflow fájlban részleteztük a szükséges lépéseket, mint például a környezet előkészítése, a projekt buildelése, a tesztek futtatása, és az eredmények riportolása.
- Eredmények Ellenőrzése: A build és teszt eredmények áttekintése a Github felületén, hogy megbizonyosodjunk arról, hogy minden megfelelően működik.

## Konklúzió

A projekt automatikus buildelésének beállítása a Github Actions segítségével lehetővé teszi a folyamatos integrációt és a hibák korai észlelését, ami hozzájárul a fejlesztési folyamat hatékonyságának növeléséhez. A megfelelően konfigurált pom.xml, a jól szervezett forrás- és tesztkód, valamint a működőképes Github Actions workflow révén biztosítottuk, hogy a projekt minden egyes változtatása után automatikusan lefussanak a szükséges build és teszt folyamatok.
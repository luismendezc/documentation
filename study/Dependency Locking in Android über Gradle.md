![](https://wiki.bconnect.barmer.de/plugins/servlet/confluence/placeholder/macro?definition=e3RvY30&locale=en_GB&version=2)

# Intension und Herausforderung

Durch das Sperren oder Festlegen von Abhängigkeiten (Dependency Locking / Pinning) soll vor allem verhindert werden, dass ungewollt neue oder geänderte Versionen von Bibliotheken in den Build der BARMER-App aufgenommen werden. Dies hat u.a. folgende Gründe:

- Reproduzierbare Builds  
    Wird ein und der selbe Commit zwei mal gebaut, soll er immer zu den exakt gleichen Abhängigkeiten führen. Sind Abhängigkeiten nicht fest definiert kann etwa der Fall auftreten, dass zwischen zwei Builds auf dem selben Git-Commit sich Versionen ändern und in den zweiten folglich weitere bzw. andere Abhängigkeiten einfließen.
- Es soll vermieden werden, dass unkontrollier neue (oder auch nicht explizit definierte), möglicherweise fehlerhafte Versionen von Bibliotheken automatisch in das Projekt integriert werden. Dies ist besonders wichtig um sicherzustellen, dass ein funktionierender Build nicht plötzlich aufgrund von unvorhergesehenen Änderungen in einer externen Bibliothek fehlschlägt.

Neue Versionen von Abhängigkeiten können Sicherheitslücken einführen. Mit Dependency Locking hat das Entwicklungsteam die Kontrolle darüber, wann und welche Updates eingespielt werden. Dies ermöglicht es, Sicherheitsüberprüfungen durchzuführen und sicherzustellen, dass nur geprüfte und sichere Versionen verwendet werden.

Darüber hinaus ermöglicht es ein gezieltes und kontrolliertes Aktualisieren von Abhängigkeiten. Entwicklende können neue Versionen in einer kontrollierten Umgebung testen, bevor sie diese in die Produktion einführen. Dies minimiert das Risiko von Inkompatibilitäten und anderen Problemen, die durch ungetestete Updates entstehen können.

Da die Android-App eine Vielzahl von React Native Komponnenten und weitere Module wie z.B. Authenticator(Verimi) einsetzt muss das Dependency Locking auf alle Unterprojekte angewendet werden, vorallem auch auf nativen Module, die von React Native in die App integriert werden sowie von React Native selbst aus dem Netz geladen werden und nur lokal im Filesystem liegen.

# Aktueller Lösungsansatz

Folgende Anpassungen wurden vorgenommen, damit das Dependency Locking auf das komplette Projekt angewendet werden kann. Bei der Implementierung wurde das aktuell integrierte Gradle 8.6 und AGP 8.2.2 genutzt. Insofern Gradle Updates vorgenommen wurden, so sollte die Funktionalität geprüft werden

### **Locking aktivieren**

|   |
|---|
|dependencyLocking {<br>    lockAllConfigurations()<br>    lockMode = LockMode.STRICT<br>}|

Dieser Block aktiviert die Abhängigkeits-Sperrung auf allen Konfigurationen und verwendet den **strikten Locking-Modus** (`LockMode.STRICT`). Dies bedeutet, dass nur die exakt in der Lock-Datei definierten Versionen der Abhängigkeiten verwendet werden, und wenn etwas nicht im Lockfile steht, schlägt der Build fehl. Das sorgt für maximale Konsistenz.

- **`lockAllConfigurations()`**: Dies sperrt alle Konfigurationen, also sowohl Compile- als auch Runtime-Konfigurationen, und erfasst auch transitive Abhängigkeiten.
    
- **`lockMode = LockMode.STRICT`**: Erzwingt, dass nur gesperrte Versionen genutzt werden.
    

### Locking für Unterprojekte anwenden

|                                                                                                                                |
| ------------------------------------------------------------------------------------------------------------------------------ |
| subprojects {<br>    configurations.configureEach {<br>        it.resolutionStrategy.activateDependencyLocking()<br>    }<br>} |

Hier wird sichergestellt, dass ebenso für alle Unterprojekte das Locking derer Abhängigkeiten aktiviert ist.

- **`subprojects {}`**: Dies gilt für alle Unterprojekte im Build.
    
- **`configurations.configureEach`**: Dies stellt sicher, dass für jede Konfiguration des Unterprojekts die Sperrung aktiviert wird.
    
- **`it.resolutionStrategy.activateDependencyLocking()`**: Aktiviert die Abhängigkeits-Sperrung für jede Konfiguration des Unterprojekts, sodass auch die Abhängigkeiten dieser Projekte gesperrt werden.
    

### BuildScripte locken

|   |
|---|
|buildscript {<br>    configurations.configureEach {<br>        resolutionStrategy.activateDependencyLocking()<br>    }<br>}|

Dieser Block bezieht sich auf die Build-Skript-spezifischen Abhängigkeiten. In Gradle gibt es die `buildscript`-Konfiguration für Abhängigkeiten, die beim Aufbau des Gradle-Builds selbst verwendet werden (z.B. Plugins).

In Gradle wird der `buildscript`-Block verwendet, um die Abhängigkeiten und Repositories zu definieren, die **zum Aufbau des Gradle-Skripts selbst** benötigt werden. Das bedeutet, dass alle Plugins oder Bibliotheken, die im `build.gradle` verwendet werden (z.B. Gradle-Plugins, zusätzliche Tools), über diesen Block geladen werden. Es handelt sich dabei um die **Build-Skript-spezifischen** Abhängigkeiten.

- **`resolutionStrategy.activateDependencyLocking()`**: Aktiviert auch hier die Dependency-Sperrung, sodass die Abhängigkeiten für den Gradle-Build-Prozess ebenfalls gesperrt werden.

Dieser Block durchläuft daher alle Konfigurationen im `buildscript`-Block und aktiviert für jede Konfiguration die Abhängigkeits-Sperrung.

## Rolle des Lockfiles

Im Folgenden ist die allgemeine Logik beschrieben, für welche das Lockfile zum Einsatz kommt:

- **Gradle löst die Abhängigkeiten normal auf**, basierend auf den angegebenen Bibliotheken und Versionen in der `build.gradle`.
- Das **Lockfile** wird **nach der Auflösung** der Abhängigkeiten herangezogen, um zu prüfen, ob die aufgelösten Versionen mit den gesperrten Versionen übereinstimmen.
- **Wenn die Versionen übereinstimmen**, läuft der Build weiter. **Wenn nicht**, bricht der Build (im STRICT-Modus) ab, um ungewollte Abhängigkeitsänderungen zu verhindern.

# Beschreibung der Lösung

## Authenticator

Für das Locking des Authenticators sind zwei Varianten erzeit denkbar:

- Globales Locking über das Lockfile des App-Projekts (android/app-Verzeichnis)
- eigenes Lockfile im Authenticator-Projekt.

  

Die Entscheidung, an welcher Stelle gelockt werden soll, ist davon abhängig, ob der Authenticator als eigenständiges Modul bzw. Projekt zu sehen ist und ein reproduzierbares Build erzeugen soll oder ob diese Eigenschaft erst bei der Integration in eine (Barmer)App entstehen soll. Im Rahmen des Austauschs am 16 Sep 2024 wurde gemeinsam entschieden, für den Authenticator ein eigenes Lockfile zu verwenden.

## React Native

React Native integriert beim Erstellen der App die Abhängigkeiten im Dateisystem mithilfe eigener Logik in das Projekt. Dabei wird beispielsweise über `yarn` oder den Befehl `yarn add "modul_name"` das entsprechende npm-Paket samt nativem Anteil über Artifactory geladen und im Dateisystem (node-modules) abgelegt. Es entsteht also ein natives (Unter-)Projekt auf der Festplatte, das eigenständig kompiliert werden kann. Der Android-Anteil befindet sich im Verzeichnis `android` und hat eine eigene `build.gradle`-Datei, die wiederum weitere Abhängigkeiten definieren kann. Diese Abhängigkeiten liegen im `node_modules`-Ordner.

Die Integration der Abhängigkeit wird zunächst vom React Native Core in der `settings.gradle`-Datei zum Projekt hinzugefügt:

apply from: file("../../../node_modules/@react-native-community/cli-platform-android/native_modules.gradle")  
  
Diese Datei hat bereits Logik integriert, die beim Erstellen die Abhängigkeiten zum Projekt hinzufügt. Dabei sind u.A. die folgenden Methoden von React Native verantwortlich:

addReactNativeModuleProjects sowie addReactNativeModuleDependencies  
  
Auszug:

|   |
|---|
|void addReactNativeModuleProjects(DefaultSettings defaultSettings) {<br>    reactNativeModules.forEach { reactNativeModule -><br>      String nameCleansed = reactNativeModule["nameCleansed"]<br>      String androidSourceDir = reactNativeModule["androidSourceDir"]<br>      defaultSettings.include(":${nameCleansed}")<br>      defaultSettings.project(":${nameCleansed}").projectDir = new File("${androidSourceDir}")<br>    }<br>  }|

Das hat letztendlich den gleichen Effekt als wenn alle nativen Anteile, die durch React Native in die App integriert werden in der settings.gradle definiert wurden. Zum Bespiel sieht die Scanbot integration so aus:

|   |
|---|
|include ':react-native-scanbot-sdk'<br>project(':react-native-scanbot-sdk').projectDir = new File(rootProject.projectDir, '../../../node_modules/react-native-scanbot-sdk/android/app')|

Der oben aufgeführte Code (Codestand 31.07.2024) fügt aktuell folgende Projekte hinzu:

|   |
|---|
|Modul / Projekt|
|notifee_react-native|
|react-native-async-storage_async-storage|
|react-native-community_datetimepicker|
|react-native-community_netinfo|
|react-native-community_progress-bar-android|
|react-native-community_progress-view|
|react-native-firebase_app|
|react-native-firebase_messaging|
|react-native-masked-view_masked-view|
|sentry_react-native|
|lottie-react-native|
|react-native-blob-util|
|react-native-config|
|react-native-device-info|
|react-native-document-picker|
|react-native-exception-handler|
|react-native-fs|
|react-native-gesture-handler|
|react-native-image-picker|
|react-native-pager-view|
|react-native-pdf|
|react-native-rate|
|react-native-reanimated|
|react-native-safe-area-context|
|react-native-scanbot-sdk|
|react-native-screens|
|react-native-share|
|react-native-svg|
|react-native-vector-icons|
|react-native-webview|

Demnach sind mehr als ein Dutzend React Native Packete mit eigenem nativen Android Code im Projekt, die idR eigenen Abhängigkeiten mitsich bringen

Letztendlich ist die Art der Integration als Auto Linkung zu verstehen, das via Plugin in Gradle durchgeführt wird, das automatisch alle native Module, die im `node_modules`-Verzeichnis gefunden werden, in das Gradle-Build-System einbindet. Hier sind die Schritte, die dabei ablaufen:

- **React Native Plugin für Gradle**: React Native bringt ein Gradle-Plugin mit, das für das Auto-Linking zuständig ist. Dieses Plugin sucht nach native Modulen in `node_modules` und fügt sie dem Gradle-Build-Prozess hinzu.
    
- **`react-native.config.js`**: Diese Datei kann im Wurzelverzeichnis des React Native-Projekts vorhanden sein und enthält Konfigurationen für das Auto-Linking. Das Plugin verwendet diese Konfigurationen, um festzustellen, welche Module eingebunden werden sollen und welche Pfade dabei verwendet werden.
    
- **Modul-Integrationen**: Beim Build-Prozess wird durch das Gradle-Plugin für jedes native Modul eine entsprechende Konfiguration generiert. Das Plugin sorgt dafür, dass die entsprechenden `.aar`-Dateien (Android Archive) oder die notwendigen Quellcode-Dateien in das Gradle-Build-System integriert werden
    
- **Automatische Updates**: Wenn neue native Module installiert oder bestehende aktualisiert werden, wird das Auto-Linking-Plugin beim nächsten Build automatisch die Änderungen erkennen und die entsprechenden Konfigurationen aktualisieren.
    

### Yarn.lock für native RN Module

Die Yarn.lock lockt lediglich React Native Module, die bei der Installation (yarn add LIB_NAME) oder beim Update einer neue React Native Library diese von einrm Server laden und die geladenen Daten im node-modules Ordner ablegen.

Es wird also ein konkrete Library einer React Native Library gesetzt, wobei sich yarn nicht mit native Abhängigkeiten auseinandersetzt.

Da die React Native Library auf dem Entwicklersystem als 'Kopie' liegt sind im node-modues Ordner auch allle notwendigen Daten, d.h  ein natives Projekt enthalten.

![](https://wiki.bconnect.barmer.de/download/thumbnails/533038501/image-2024-8-21_7-14-29.png?version=1&modificationDate=1724217269414&api=v2 "BARMER-App > Dependency Locking in Android über Gradle > image-2024-8-21_7-14-29.png")

Zum Beispiel bringt die Bibliothek React-Native-PDF  einen nativen Anteil mit, der dann Abhängigkeiten zu folgender nativen Lib hat:

com.github.TalbotGooday:AndroidPdfViewer:3.1.0-beta.3  

Damit die Android App kompiliert werden kann muss die native Abhängigkeit in der richtigen Version im gradle.lockfile enthalten sein.

Sobald sich durch ein Update einer Bibliothek von React Native eine Änderungn an nativen Libs ergibt, muss das gradle.lockfile neu erstellt werden. Dies geschieht via:

|   |
|---|
|gradlew :app:dependencies --write-locks|

## ORT / Dependency Track

- Wird das Dependency Locking im Anschluss bei der Analyse mit ORT auch korrekt ausgewertet? Heisst, landen die gelockten Abhängigkeiten auch korrekt in der BOM-Datei oder sind dort andere oder mehrere Versionen enthalten?
- Klären via ![](https://wiki.bconnect.barmer.de/plugins/servlet/confluence/placeholder/macro?definition=e2ppcmE6a2V5PUFQUC0xNTAwNH0=&locale=de_DE)

  

## Test der aktuellen Lösung

Das Testen von Änderungen am Build ist mit einem höheren Zeitaufwand verbunden, da die App jeweils neu gebaut werden muss. Daher werden einzelnen Tests an einem Test Repository durchgeführt, um die Bearbeitungszeit zu erhöhen.

Dependencie Locking wird durchgeführt via

|   |
|---|
|./gradlew :app:dependencies --write-locks|

Dadurch entsteht eine Datei android/app/gradle.lockfile die folgenden Inhalt (Beispiel hat):

|   |
|---|
|com.google.errorprone:error_prone_annotations:2.11.0=_internal-unified-test-platform-android-device-provider-ddmlib,_internal-unified-test-platform-android-device-provider-gradle,_internal-unified-test-platform-android-test-plugin-host-additional-test-output,_internal-unified-test-platform-android-test-plugin-host-apk-installer,_internal-unified-test-platform-android-test-plugin-host-coverage,_internal-unified-test-platform-android-test-plugin-host-device-info,_internal-unified-test-platform-android-test-plugin-host-emulator-control,_internal-unified-test-platform-android-test-plugin-host-logcat,_internal-unified-test-platform-android-test-plugin-host-retention,_internal-unified-test-platform-android-test-plugin-result-listener-gradle<br>com.google.errorprone:error_prone_annotations:2.15.0=cloneDigidentDebugCompileClasspath,cloneDigidentDebugRuntimeClasspath,cloneDigidentDebugUnitTestCompileClasspath,cloneDigidentDebugUnitTestRuntimeClasspath,cloneDigidentReleaseCompileClasspath,cloneDigidentReleaseRuntimeClasspath,cloneDigidentReleaseUnitTestCompileClasspath,cloneDigidentReleaseUnitTestRuntimeClasspath,cloneEasyloginDebugCompileClasspath,cloneEasyloginDebugRuntimeClasspath,cloneEasyloginDebugUnitTestCompileClasspath,cloneEasyloginDebugUnitTestRuntimeClasspath,cloneEasyloginReleaseCompileClasspath,cloneEasyloginReleaseRuntimeClasspath,cloneEasyloginReleaseUnitTestCompileClasspath,cloneEasyloginReleaseUnitTestRuntimeClasspath,dev2EasyloginDebugCompileClasspath,dev2EasyloginDebugRuntimeClasspath,dev2EasyloginDebugUnitTestCompileClasspath,dev2EasyloginDebugUnitTestRuntimeClasspath,dev2EasyloginReleaseCompileClasspath,dev2EasyloginReleaseRuntimeClasspath,dev2EasyloginReleaseUnitTestCompileClasspath,dev2EasyloginReleaseUnitTestRuntimeClasspath,dev3DigidentDebugCompileClasspath,dev3DigidentDebugRuntimeClasspath,dev3DigidentDebugUnitTestCompileClasspath,dev3DigidentDebugUnitTestRuntimeClasspath,dev3DigidentReleaseCompileClasspath,dev3DigidentReleaseRuntimeClasspath,dev3DigidentReleaseUnitTestCompileClasspath,dev3DigidentReleaseUnitTestRuntimeClasspath,dev3EasyloginDebugCompileClasspath,dev3EasyloginDebugRuntimeClasspath,dev3EasyloginDebugUnitTestCompileClasspath,dev3EasyloginDebugUnitTestRuntimeClasspath,dev3EasyloginReleaseCompileClasspath,dev3EasyloginReleaseRuntimeClasspath,dev3EasyloginReleaseUnitTestCompileClasspath,dev3EasyloginReleaseUnitTestRuntimeClasspath,int2DigidentDebugCompileClasspath,int2DigidentDebugRuntimeClasspath,int2DigidentDebugUnitTestCompileClasspath,int2DigidentDebugUnitTestRuntimeClasspath,int2DigidentReleaseCompileClasspath,int2DigidentReleaseRuntimeClasspath,int2DigidentReleaseUnitTestCompileClasspath,int2DigidentReleaseUnitTestRuntimeClasspath,int2EasyloginDebugCompileClasspath,int2EasyloginDebugRuntimeClasspath,int2EasyloginDebugUnitTestCompileClasspath,int2EasyloginDebugUnitTestRuntimeClasspath,int2EasyloginReleaseCompileClasspath,int2EasyloginReleaseRuntimeClasspath,int2EasyloginReleaseUnitTestCompileClasspath,int2EasyloginReleaseUnitTestRuntimeClasspath,int4DigidentDebugCompileClasspath,int4DigidentDebugRuntimeClasspath,int4DigidentDebugUnitTestCompileClasspath,int4DigidentDebugUnitTestRuntimeClasspath,int4DigidentReleaseCompileClasspath,int4DigidentReleaseRuntimeClasspath,int4DigidentReleaseUnitTestCompileClasspath,int4DigidentReleaseUnitTestRuntimeClasspath,int4EasyloginDebugCompileClasspath,int4EasyloginDebugRuntimeClasspath,int4EasyloginDebugUnitTestCompileClasspath,int4EasyloginDebugUnitTestRuntimeClasspath,int4EasyloginReleaseCompileClasspath,int4EasyloginReleaseRuntimeClasspath,int4EasyloginReleaseUnitTestCompileClasspath,int4EasyloginReleaseUnitTestRuntimeClasspath,intDigidentDebugCompileClasspath,intDigidentDebugRuntimeClasspath,intDigidentDebugUnitTestCompileClasspath,intDigidentDebugUnitTestRuntimeClasspath,intDigidentReleaseCompileClasspath,intDigidentReleaseRuntimeClasspath,intDigidentReleaseUnitTestCompileClasspath,intDigidentReleaseUnitTestRuntimeClasspath,intEasyloginDebugCompileClasspath,intEasyloginDebugRuntimeClasspath,intEasyloginDebugUnitTestCompileClasspath,intEasyloginDebugUnitTestRuntimeClasspath,intEasyloginReleaseCompileClasspath,intEasyloginReleaseRuntimeClasspath,intEasyloginReleaseUnitTestCompileClasspath,intEasyloginReleaseUnitTestRuntimeClasspath,liveDigidentDebugCompileClasspath,liveDigidentDebugRuntimeClasspath,liveDigidentDebugUnitTestCompileClasspath,liveDigidentDebugUnitTestRuntimeClasspath,liveDigidentReleasestoreCompileClasspath,liveDigidentReleasestoreRuntimeClasspath,liveDigidentReleasestoreUnitTestCompileClasspath,liveDigidentReleasestoreUnitTestRuntimeClasspath<br>com.google.errorprone:error_prone_annotations:2.9.0=cloneDigidentDebugAndroidTestCompileClasspath,cloneEasyloginDebugAndroidTestCompileClasspath,dev2EasyloginDebugAndroidTestCompileClasspath,dev3DigidentDebugAndroidTestCompileClasspath,dev3EasyloginDebugAndroidTestCompileClasspath,int2DigidentDebugAndroidTestCompileClasspath,int2EasyloginDebugAndroidTestCompileClasspath,int4DigidentDebugAndroidTestCompileClasspath,int4EasyloginDebugAndroidTestCompileClasspath,intDigidentDebugAndroidTestCompileClasspath,intEasyloginDebugAndroidTestCompileClasspath,liveDigidentDebugAndroidTestCompileClasspath<br>com.google.firebase:firebase-annotations:16.2.0=cloneDigidentDebugAndroidTestCompileClasspath,cloneDigidentDebugCompileClasspath,cloneDigidentDebugRuntimeClasspath,cloneDigidentDebugUnitTestCompileClasspath,cloneDigidentDebugUnitTestRuntimeClasspath,cloneDigidentReleaseCompileClasspath,cloneDigidentReleaseRuntimeClasspath,cloneDigidentReleaseUnitTestCompileClasspath,cloneDigidentReleaseUnitTestRuntimeClasspath,cloneEasyloginDebugAndroidTestCompileClasspath,cloneEasyloginDebugCompileClasspath,cloneEasyloginDebugRuntimeClasspath,cloneEasyloginDebugUnitTestCompileClasspath,cloneEasyloginDebugUnitTestRuntimeClasspath,cloneEasyloginReleaseCompileClasspath,cloneEasyloginReleaseRuntimeClasspath,cloneEasyloginReleaseUnitTestCompileClasspath,cloneEasyloginReleaseUnitTestRuntimeClasspath,dev2EasyloginDebugAndroidTestCompileClasspath,dev2EasyloginDebugCompileClasspath,dev2EasyloginDebugRuntimeClasspath,dev2EasyloginDebugUnitTestCompileClasspath,dev2EasyloginDebugUnitTestRuntimeClasspath,dev2EasyloginReleaseCompileClasspath,dev2EasyloginReleaseRuntimeClasspath,dev2EasyloginReleaseUnitTestCompileClasspath,dev2EasyloginReleaseUnitTestRuntimeClasspath,dev3DigidentDebugAndroidTestCompileClasspath,dev3DigidentDebugCompileClasspath,dev3DigidentDebugRuntimeClasspath,dev3DigidentDebugUnitTestCompileClasspath,dev3DigidentDebugUnitTestRuntimeClasspath,dev3DigidentReleaseCompileClasspath,dev3DigidentReleaseRuntimeClasspath,dev3DigidentReleaseUnitTestCompileClasspath,dev3DigidentReleaseUnitTestRuntimeClasspath,dev3EasyloginDebugAndroidTestCompileClasspath,dev3EasyloginDebugCompileClasspath,dev3EasyloginDebugRuntimeClasspath,dev3EasyloginDebugUnitTestCompileClasspath,dev3EasyloginDebugUnitTestRuntimeClasspath,dev3EasyloginReleaseCompileClasspath,dev3EasyloginReleaseRuntimeClasspath,dev3EasyloginReleaseUnitTestCompileClasspath,dev3EasyloginReleaseUnitTestRuntimeClasspath,int2DigidentDebugAndroidTestCompileClasspath,int2DigidentDebugCompileClasspath,int2DigidentDebugRuntimeClasspath,int2DigidentDebugUnitTestCompileClasspath,int2DigidentDebugUnitTestRuntimeClasspath,int2DigidentReleaseCompileClasspath,int2DigidentReleaseRuntimeClasspath,int2DigidentReleaseUnitTestCompileClasspath,int2DigidentReleaseUnitTestRuntimeClasspath,int2EasyloginDebugAndroidTestCompileClasspath,int2EasyloginDebugCompileClasspath,int2EasyloginDebugRuntimeClasspath,int2EasyloginDebugUnitTestCompileClasspath,int2EasyloginDebugUnitTestRuntimeClasspath,int2EasyloginReleaseCompileClasspath,int2EasyloginReleaseRuntimeClasspath,int2EasyloginReleaseUnitTestCompileClasspath,int2EasyloginReleaseUnitTestRuntimeClasspath,int4DigidentDebugAndroidTestCompileClasspath,int4DigidentDebugCompileClasspath,int4DigidentDebugRuntimeClasspath,int4DigidentDebugUnitTestCompileClasspath,int4DigidentDebugUnitTestRuntimeClasspath,int4DigidentReleaseCompileClasspath,int4DigidentReleaseRuntimeClasspath,int4DigidentReleaseUnitTestCompileClasspath,int4DigidentReleaseUnitTestRuntimeClasspath,int4EasyloginDebugAndroidTestCompileClasspath,int4EasyloginDebugCompileClasspath,int4EasyloginDebugRuntimeClasspath,int4EasyloginDebugUnitTestCompileClasspath,int4EasyloginDebugUnitTestRuntimeClasspath,int4EasyloginReleaseCompileClasspath,int4EasyloginReleaseRuntimeClasspath,int4EasyloginReleaseUnitTestCompileClasspath,int4EasyloginReleaseUnitTestRuntimeClasspath,intDigidentDebugAndroidTestCompileClasspath,intDigidentDebugCompileClasspath,intDigidentDebugRuntimeClasspath,intDigidentDebugUnitTestCompileClasspath,intDigidentDebugUnitTestRuntimeClasspath,intDigidentReleaseCompileClasspath,intDigidentReleaseRuntimeClasspath,intDigidentReleaseUnitTestCompileClasspath,intDigidentReleaseUnitTestRuntimeClasspath,intEasyloginDebugAndroidTestCompileClasspath,intEasyloginDebugCompileClasspath,intEasyloginDebugRuntimeClasspath,intEasyloginDebugUnitTestCompileClasspath,intEasyloginDebugUnitTestRuntimeClasspath,intEasyloginReleaseCompileClasspath,intEasyloginReleaseRuntimeClasspath,intEasyloginReleaseUnitTestCompileClasspath,intEasyloginReleaseUnitTestRuntimeClasspath,liveDigidentDebugAndroidTestCompileClasspath,liveDigidentDebugCompileClasspath,liveDigidentDebugRuntimeClasspath,liveDigidentDebugUnitTestCompileClasspath,liveDigidentDebugUnitTestRuntimeClasspath,liveDigidentReleasestoreCompileClasspath,liveDigidentReleasestoreRuntimeClasspath,liveDigidentReleasestoreUnitTestCompileClasspath,liveDigidentReleasestoreUnitTestRuntimeClasspath<br>com.google.firebase:firebase-bom:31.3.0=cloneDigidentDebugRuntimeClasspath,cloneDigidentDebugUnitTestRuntimeClasspath,cloneDigidentReleaseRuntimeClasspath,cloneDigidentReleaseUnitTestRuntimeClasspath,cloneEasyloginDebugRuntimeClasspath,cloneEasyloginDebugUnitTestRuntimeClasspath,cloneEasyloginReleaseRuntimeClasspath,cloneEasyloginReleaseUnitTestRuntimeClasspath,dev2EasyloginDebugRuntimeClasspath,dev2EasyloginDebugUnitTestRuntimeClasspath,dev2EasyloginReleaseRuntimeClasspath,dev2EasyloginReleaseUnitTestRuntimeClasspath,dev3DigidentDebugRuntimeClasspath,dev3DigidentDebugUnitTestRuntimeClasspath,dev3DigidentReleaseRuntimeClasspath,dev3DigidentReleaseUnitTestRuntimeClasspath,dev3EasyloginDebugRuntimeClasspath,dev3EasyloginDebugUnitTestRuntimeClasspath,dev3EasyloginReleaseRuntimeClasspath,dev3EasyloginReleaseUnitTestRuntimeClasspath,int2DigidentDebugRuntimeClasspath,int2DigidentDebugUnitTestRuntimeClasspath,int2DigidentReleaseRuntimeClasspath,int2DigidentReleaseUnitTestRuntimeClasspath,int2EasyloginDebugRuntimeClasspath,int2EasyloginDebugUnitTestRuntimeClasspath,int2EasyloginReleaseRuntimeClasspath,int2EasyloginReleaseUnitTestRuntimeClasspath,int4DigidentDebugRuntimeClasspath,int4DigidentDebugUnitTestRuntimeClasspath,int4DigidentReleaseRuntimeClasspath,int4DigidentReleaseUnitTestRuntimeClasspath,int4EasyloginDebugRuntimeClasspath,int4EasyloginDebugUnitTestRuntimeClasspath,int4EasyloginReleaseRuntimeClasspath,int4EasyloginReleaseUnitTestRuntimeClasspath,intDigidentDebugRuntimeClasspath,intDigidentDebugUnitTestRuntimeClasspath,intDigidentReleaseRuntimeClasspath,intDigidentReleaseUnitTestRuntimeClasspath,intEasyloginDebugRuntimeClasspath,intEasyloginDebugUnitTestRuntimeClasspath,intEasyloginReleaseRuntimeClasspath,intEasyloginReleaseUnitTestRuntimeClasspath,liveDigidentDebugRuntimeClasspath,liveDigidentDebugUnitTestRuntimeClasspath,liveDigidentReleasestoreRuntimeClasspath,liveDigidentReleasestoreUnitTestRuntimeClasspath<br>com.google.firebase:firebase-common-ktx:20.4.2=cloneDigidentDebugAndroidTestCompileClasspath,cloneDigidentDebugCompileClasspath,cloneDigidentDebugRuntimeClasspath,cloneDigidentDebugUnitTestCompileClasspath,cloneDigidentDebugUnitTestRuntimeClasspath,cloneDigidentReleaseCompileClasspath,cloneDigidentReleaseRuntimeClasspath,cloneDigidentReleaseUnitTestCompileClasspath,cloneDigidentReleaseUnitTestRuntimeClasspath,cloneEasyloginDebugAndroidTestCompileClasspath,cloneEasyloginDebugCompileClasspath,cloneEasyloginDebugRuntimeClasspath,cloneEasyloginDebugUnitTestCompileClasspath,cloneEasyloginDebugUnitTestRuntimeClasspath,cloneEasyloginReleaseCompileClasspath,cloneEasyloginReleaseRuntimeClasspath,cloneEasyloginReleaseUnitTestCompileClasspath,cloneEasyloginReleaseUnitTestRuntimeClasspath,dev2EasyloginDebugAndroidTestCompileClasspath,dev2EasyloginDebugCompileClasspath,dev2EasyloginDebugRuntimeClasspath,dev2EasyloginDebugUnitTestCompileClasspath,dev2EasyloginDebugUnitTestRuntimeClasspath,dev2EasyloginReleaseCompileClasspath,dev2EasyloginReleaseRuntimeClasspath,dev2EasyloginReleaseUnitTestCompileClasspath,dev2EasyloginReleaseUnitTestRuntimeClasspath,dev3DigidentDebugAndroidTestCompileClasspath,dev3DigidentDebugCompileClasspath,dev3DigidentDebugRuntimeClasspath,dev3DigidentDebugUnitTestCompileClasspath,dev3DigidentDebugUnitTestRuntimeClasspath,dev3DigidentReleaseCompileClasspath,dev3DigidentReleaseRuntimeClasspath,dev3DigidentReleaseUnitTestCompileClasspath,dev3DigidentReleaseUnitTestRuntimeClasspath,dev3EasyloginDebugAndroidTestCompileClasspath,dev3EasyloginDebugCompileClasspath,dev3EasyloginDebugRuntimeClasspath,dev3EasyloginDebugUnitTestCompileClasspath,dev3EasyloginDebugUnitTestRuntimeClasspath,dev3EasyloginReleaseCompileClasspath,dev3EasyloginReleaseRuntimeClasspath,dev3EasyloginReleaseUnitTestCompileClasspath,dev3EasyloginReleaseUnitTestRuntimeClasspath,int2DigidentDebugAndroidTestCompileClasspath,int2DigidentDebugCompileClasspath,int2DigidentDebugRuntimeClasspath,int2DigidentDebugUnitTestCompileClasspath,int2DigidentDebugUnitTestRuntimeClasspath,int2DigidentReleaseCompileClasspath,int2DigidentReleaseRuntimeClasspath,int2DigidentReleaseUnitTestCompileClasspath,int2DigidentReleaseUnitTestRuntimeClasspath,int2EasyloginDebugAndroidTestCompileClasspath,int2EasyloginDebugCompileClasspath,int2EasyloginDebugRuntimeClasspath,int2EasyloginDebugUnitTestCompileClasspath,int2EasyloginDebugUnitTestRuntimeClasspath,int2EasyloginReleaseCompileClasspath,int2EasyloginReleaseRuntimeClasspath,int2EasyloginReleaseUnitTestCompileClasspath,int2EasyloginReleaseUnitTestRuntimeClasspath,int4DigidentDebugAndroidTestCompileClasspath,int4DigidentDebugCompileClasspath,int4DigidentDebugRuntimeClasspath,int4DigidentDebugUnitTestCompileClasspath,int4DigidentDebugUnitTestRuntimeClasspath,int4DigidentReleaseCompileClasspath,int4DigidentReleaseRuntimeClasspath,int4DigidentReleaseUnitTestCompileClasspath,int4DigidentReleaseUnitTestRuntimeClasspath,int4EasyloginDebugAndroidTestCompileClasspath,int4EasyloginDebugCompileClasspath,int4EasyloginDebugRuntimeClasspath,int4EasyloginDebugUnitTestCompileClasspath,int4EasyloginDebugUnitTestRuntimeClasspath,int4EasyloginReleaseCompileClasspath,int4EasyloginReleaseRuntimeClasspath,int4EasyloginReleaseUnitTestCompileClasspath,int4EasyloginReleaseUnitTestRuntimeClasspath,intDigidentDebugAndroidTestCompileClasspath,intDigidentDebugCompileClasspath,intDigidentDebugRuntimeClasspath,intDigidentDebugUnitTestCompileClasspath,intDigidentDebugUnitTestRuntimeClasspath,intDigidentReleaseCompileClasspath,intDigidentReleaseRuntimeClasspath,intDigidentReleaseUnitTestCompileClasspath,intDigidentReleaseUnitTestRuntimeClasspath,intEasyloginDebugAndroidTestCompileClasspath,intEasyloginDebugCompileClasspath,intEasyloginDebugRuntimeClasspath,intEasyloginDebugUnitTestCompileClasspath,intEasyloginDebugUnitTestRuntimeClasspath,intEasyloginReleaseCompileClasspath,intEasyloginReleaseRuntimeClasspath,intEasyloginReleaseUnitTestCompileClasspath,intEasyloginReleaseUnitTestRuntimeClasspath,liveDigidentDebugAndroidTestCompileClasspath,liveDigidentDebugCompileClasspath,liveDigidentDebugRuntimeClasspath,liveDigidentDebugUnitTestCompileClasspath,liveDigidentDebugUnitTestRuntimeClasspath,liveDigidentReleasestoreCompileClasspath,liveDigidentReleasestoreRuntimeClasspath,liveDigidentReleasestoreUnitTestCompileClasspath,liveDigidentReleasestoreUnitTestRuntimeClasspath<br>com.google.firebase:firebase-common:20.4.2=cloneDigidentDebugAndroidTestCompileClasspath,cloneDigidentDebugCompileClasspath,cloneDigidentDebugRuntimeClasspath,cloneDigidentDebugUnitTestCompileClasspath,cloneDigidentDebugUnitTestRuntimeClasspath,cloneDigidentReleaseCompileClasspath,cloneDigidentReleaseRuntimeClasspath,cloneDigidentReleaseUnitTestCompileClasspath,cloneDigidentReleaseUnitTestRuntimeClasspath,cloneEasyloginDebugAndroidTestCompileClasspath,cloneEasyloginDebugCompileClasspath,cloneEasyloginDebugRuntimeClasspath,cloneEasyloginDebugUnitTestCompileClasspath,cloneEasyloginDebugUnitTestRuntimeClasspath,cloneEasyloginReleaseCompileClasspath,cloneEasyloginReleaseRuntimeClasspath,cloneEasyloginReleaseUnitTestCompileClasspath,cloneEasyloginReleaseUnitTestRuntimeClasspath,dev2EasyloginDebugAndroidTestCompileClasspath,dev2EasyloginDebugCompileClasspath,dev2EasyloginDebugRuntimeClasspath,dev2EasyloginDebugUnitTestCompileClasspath,dev2EasyloginDebugUnitTestRuntimeClasspath,dev2EasyloginReleaseCompileClasspath,dev2EasyloginReleaseRuntimeClasspath,dev2EasyloginReleaseUnitTestCompileClasspath,dev2EasyloginReleaseUnitTestRuntimeClasspath,dev3DigidentDebugAndroidTestCompileClasspath,dev3DigidentDebugCompileClasspath,dev3DigidentDebugRuntimeClasspath,dev3DigidentDebugUnitTestCompileClasspath,dev3DigidentDebugUnitTestRuntimeClasspath,dev3DigidentReleaseCompileClasspath,dev3DigidentReleaseRuntimeClasspath,dev3DigidentReleaseUnitTestCompileClasspath,dev3DigidentReleaseUnitTestRuntimeClasspath,dev3EasyloginDebugAndroidTestCompileClasspath,dev3EasyloginDebugCompileClasspath,dev3EasyloginDebugRuntimeClasspath,dev3EasyloginDebugUnitTestCompileClasspath,dev3EasyloginDebugUnitTestRuntimeClasspath,dev3EasyloginReleaseCompileClasspath,dev3EasyloginReleaseRuntimeClasspath,dev3EasyloginReleaseUnitTestCompileClasspath,dev3EasyloginReleaseUnitTestRuntimeClasspath,int2DigidentDebugAndroidTestCompileClasspath,int2DigidentDebugCompileClasspath,int2DigidentDebugRuntimeClasspath,int2DigidentDebugUnitTestCompileClasspath,int2DigidentDebugUnitTestRuntimeClasspath,int2DigidentReleaseCompileClasspath,int2DigidentReleaseRuntimeClasspath,int2DigidentReleaseUnitTestCompileClasspath,int2DigidentReleaseUnitTestRuntimeClasspath,int2EasyloginDebugAndroidTestCompileClasspath,int2EasyloginDebugCompileClasspath,int2EasyloginDebugRuntimeClasspath,int2EasyloginDebugUnitTestCompileClasspath,int2EasyloginDebugUnitTestRuntimeClasspath,int2EasyloginReleaseCompileClasspath,int2EasyloginReleaseRuntimeClasspath,int2EasyloginReleaseUnitTestCompileClasspath,int2EasyloginReleaseUnitTestRuntimeClasspath,int4DigidentDebugAndroidTestCompileClasspath,int4DigidentDebugCompileClasspath,int4DigidentDebugRuntimeClasspath,int4DigidentDebugUnitTestCompileClasspath,int4DigidentDebugUnitTestRuntimeClasspath,int4DigidentReleaseCompileClasspath,int4DigidentReleaseRuntimeClasspath,int4DigidentReleaseUnitTestCompileClasspath,int4DigidentReleaseUnitTestRuntimeClasspath,int4EasyloginDebugAndroidTestCompileClasspath,int4EasyloginDebugCompileClasspath,int4EasyloginDebugRuntimeClasspath,int4EasyloginDebugUnitTestCompileClasspath,int4EasyloginDebugUnitTestRuntimeClasspath,int4EasyloginReleaseCompileClasspath,int4EasyloginReleaseRuntimeClasspath,int4EasyloginReleaseUnitTestCompileClasspath,int4EasyloginReleaseUnitTestRuntimeClasspath,intDigidentDebugAndroidTestCompileClasspath,intDigidentDebugCompileClasspath,intDigidentDebugRuntimeClasspath,intDigidentDebugUnitTestCompileClasspath,intDigidentDebugUnitTestRuntimeClasspath,intDigidentReleaseCompileClasspath,intDigidentReleaseRuntimeClasspath,intDigidentReleaseUnitTestCompileClasspath,intDigidentReleaseUnitTestRuntimeClasspath,intEasyloginDebugAndroidTestCompileClasspath,intEasyloginDebugCompileClasspath,intEasyloginDebugRuntimeClasspath,intEasyloginDebugUnitTestCompileClasspath,intEasyloginDebugUnitTestRuntimeClasspath,intEasyloginReleaseCompileClasspath,intEasyloginReleaseRuntimeClasspath,intEasyloginReleaseUnitTestCompileClasspath,intEasyloginReleaseUnitTestRuntimeClasspath,liveDigidentDebugAndroidTestCompileClasspath,liveDigidentDebugCompileClasspath,liveDigidentDebugRuntimeClasspath,liveDigidentDebugUnitTestCompileClasspath,liveDigidentDebugUnitTestRuntimeClasspath,liveDigidentReleasestoreCompileClasspath,liveDigidentReleasestoreRuntimeClasspath,liveDigidentReleasestoreUnitTestCompileClasspath,liveDigidentReleasestoreUnitTestRuntimeClasspath|

### Test: Änderung in der build.gradle der App ![(tick)](https://wiki.bconnect.barmer.de/s/qf7xsg/8804/1jpfuow/_/images/icons/emoticons/check.svg "(tick)")

von 

implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.0.0")

nach

implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")  
  

|   |
|---|
|Could not determine the dependencies of task ':app:compileDebugJavaWithJavac'.<br>> Could not resolve all task dependencies for configuration ':app:debugCompileClasspath'.<br>   > Could not resolve androidx.swiperefreshlayout:swiperefreshlayout:1.1.0.<br>     Required by:<br>         project :app<br>      > Cannot find a version of 'androidx.swiperefreshlayout:swiperefreshlayout' that satisfies the version constraints:<br>           Dependency path 'LockAndroidGradleDeps:app:unspecified' --> 'androidx.swiperefreshlayout:swiperefreshlayout:1.1.0'<br>           Constraint path 'LockAndroidGradleDeps:app:unspecified' --> 'androidx.swiperefreshlayout:swiperefreshlayout:{strictly 1.0.0}' because of the following reason: dependency was locked to version '1.0.0'<br>           Dependency path 'LockAndroidGradleDeps:app:unspecified' --> 'com.facebook.react:react-android:0.71.10' (debugVariantDefaultApiPublication) --> 'androidx.swiperefreshlayout:swiperefreshlayout:1.0.0'<br><br>* Try:|

### Test: Änderung einer React Native Library Version via package.json / yarn ![(tick)](https://wiki.bconnect.barmer.de/s/qf7xsg/8804/1jpfuow/_/images/icons/emoticons/check.svg "(tick)")

und aufruf via yarn && sowie kompilieren der App mit nativer Abhängigkeit und geänderte native Lib Version

"react-native-pdf": "6.7.4, 

Abhängigkeiten:

|   |
|---|
|dependencies {<br>    if (isNewArchitectureEnabled() && getReactNativeMinorVersion() < 71) {<br>        implementation project(":ReactAndroid")<br>    } else {<br>        implementation 'com.facebook.react:react-native:+'<br>    }<br>    // NOTE: The original repo at com.github.barteksc is abandoned by the maintainer; there will be no more updates coming from that repo.<br>    //       It was taken over by com.github.TalbotGooday; from now on please use this repo until (if ever) the Barteksc repo is resumed.<br>    implementation 'com.github.TalbotGooday:AndroidPdfViewer:3.1.0-beta.3'<br>    implementation 'com.google.code.gson:gson:2.8.5'<br>}|

nach

"react-native-pdf": "6.7.5",

|   |
|---|
|dependencies {<br>    if (isNewArchitectureEnabled() && getReactNativeMinorVersion() < 71) {<br>        implementation project(":ReactAndroid")<br>    } else {<br>        implementation 'com.facebook.react:react-native:+'<br>    }<br>    // NOTE: The original repo at com.github.barteksc is abandoned by the maintainer; there will be no more updates coming from that repo.<br>    // The repo from zacharee is based on PdfiumAndroidKt, a much newer fork of PdfiumAndroid, with better maintenance and updated native libraries.<br>    implementation 'com.github.zacharee:AndroidPdfViewer:4.0.1'<br>    // Depend on PdfiumAndroidKt directly so this can be updated independently of AndroidPdfViewer as updates are provided.<br>    implementation 'io.legere:pdfiumandroid:1.0.19'<br>    implementation 'com.google.code.gson:gson:2.8.5'<br>}|

Ergebnis:

|   |
|---|
|> Could not resolve all task dependencies for configuration ':app:debugRuntimeClasspath'.<br>   > Did not resolve 'com.github.TalbotGooday:AndroidPdfViewer:3.1.0-beta.3' which is part of the dependency lock state<br>   > Resolved 'com.github.zacharee:AndroidPdfViewer:4.0.1' which is not part of the dependency lock state<br>   > Resolved 'org.jetbrains.kotlinx:kotlinx-coroutines-bom:1.7.3' which is not part of the dependency lock state<br>   > Resolved 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3' which is not part of the dependency lock state<br>   > Did not resolve 'com.github.TalbotGooday:PdfiumAndroid:1.0.1' which is part of the dependency lock state<br>   > Resolved 'org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3' which is not part of the dependency lock state<br>   > Resolved 'io.legere:pdfiumandroid:1.0.19' which is not part of the dependency lock state<br>   > Resolved 'org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.7.3' which is not part of the dependency lock state|

### Test: Hinzufügen einer neue RN Lib mit neuen Abhängigkeiten ![(tick)](https://wiki.bconnect.barmer.de/s/qf7xsg/8804/1jpfuow/_/images/icons/emoticons/check.svg "(tick)")

yarn add react-native-fast-image  

und kompilieren der App via gradlew build

|   |
|---|
|Could not determine the dependencies of task ':app:processDebugResources'.<br>> Could not resolve all task dependencies for configuration ':app:debugRuntimeClasspath'.<br>   > Resolved 'com.github.bumptech.glide:okhttp3-integration:4.12.0' which is not part of the dependency lock state<br>   > Resolved 'com.github.bumptech.glide:annotations:4.12.0' which is not part of the dependency lock state<br>   > Resolved 'androidx.exifinterface:exifinterface:1.2.0' which is not part of the dependency lock state<br>   > Resolved 'com.github.bumptech.glide:disklrucache:4.12.0' which is not part of the dependency lock state<br>   > Resolved 'com.github.bumptech.glide:glide:4.12.0' which is not part of the dependency lock state|

### Test: Hinzufügen einer neuen Abhängigkeit in gradle.build der App ![(tick)](https://wiki.bconnect.barmer.de/s/qf7xsg/8804/1jpfuow/_/images/icons/emoticons/check.svg "(tick)")

implementation 'net.openid:appauth:0.11.1'  

|   |
|---|
|> Could not resolve all task dependencies for configuration ':app:debugCompileClasspath'.<br>   > Resolved 'net.openid:appauth:0.11.1' which is not part of the dependency lock state<br>   > Resolved 'androidx.browser:browser:1.3.0' which is not part of the dependency lock state<br>   > Resolved 'com.google.guava:listenablefuture:1.0' which is not part of the dependency lock state|

### Test: Lokales ändern eine Lib Version im React Native Projekt innerhalb einer build.gradle ![(error)](https://wiki.bconnect.barmer.de/s/qf7xsg/8804/1jpfuow/_/images/icons/emoticons/error.svg "(error)")

wird nicht bemerkt. Allerdings sollte das kein üblicher Anwendungsfall sein, das nach dem Download bzw Installation eines NPM Packetes dieses auf der Festplatte geändert wird

# Auswirkungen auf Software Updates

## Bessere Ermittlung der Integration

Anhand des **lockfiles** kann besser ermittelt werden, wodurch eine spezielle Abhängigkeit in der App vorhanden ist, da diese nach Verwendungsart und Grund ablesbar ist.

## Erstellte Lockfile

Letzendlich werden mehrere Files beim Locking erstellt:

- app/gradle.lockfile
    - Lockfile aller Resourcen, die in die App eingehen
- barmer-app-authenticator/gradle.lockfile
    - Resourcen des Moduls/Projektes des Authenticators
- barmer-app-lint-rules/gradle.lockfile
    - Resourcen des Modules/Projekt des Linters
- buildscript-gradle.lockfile
    - Alle Resourcen für das erstellen der App, insbesondere Gradle Plugins wie AGP oder Sentry
- settings-gradle.lockfile
    - Aktuelle ohne Inhalt

Details sind in folgedem [MR](https://gitlab.entw.bconnect.barmer.de/ogs-frontends/barmer-frontends/-/merge_requests/4348) ersichtlicht

## React Native Updates

Beim aktualisieren oder hinzufügen von NPM Libs mit nativen Anteil muss ggfs die gradle.lockfile aktualisiert werden

## Updates der LockFiles

Es reicht, das Script  

  

|   |
|---|
|update-lockfiles.sh|

  

aufzurufen, dass sich im Ordner helperScripts/android befindet und die geänderten *.lockfiles nach erfolgreicher Prüfung auf unerwünschter Änderungen in GIT einzuchecken.  

Es muss darauf geachtet werden, dass dieses Erfolgreich durchläuft.

Insofern es einmal Probleme geben sollte, z.b Release kann nicht erstellt werden, aufgrund von Problemen mit dem Lockfile, so reicht aus, das Locking in der build.gradle abzuschalten oder das Lockfile zu löschen.

# TOML vs Lockfiles

TOML ist ein Dateiformat, das in Gradle zur Verwaltung von Abhängigkeitskatalogen verwendet wird, um Versionsinformationen und Abhängigkeiten zentral zu definieren. Es wird ab **Gradle 7.6** unterstützt (wir nutzen bereits 8.6) und ermöglicht es, Abhängigkeiten übersichtlich in einer `libs.versions.toml`-Datei zu **organisieren**. Dies verbessert die Wartbarkeit und Übersichtlichkeit beim Verwalten von Projektabhängigkeiten. Weitere Infos sind bei [Google](https://developer.android.com/build/migrate-to-catalogs?hl=de#groovy) abrufbar  

1. **Reproduzierbare Builds sichern**:
    
    - **Lockfiles** garantieren, dass bei der Installation und Verwendung von Abhängigkeiten exakt die gleichen Versionen verwendet werden, die zum Zeitpunkt des Lockfile-Erstellens aufgelöst wurden. Das bedeutet, dass auch transitive Abhängigkeiten (die indirekt eingebundenen Abhängigkeiten) in einem Lockfile gesichert werden.
    - **TOML-Dateien** definieren zwar Versionen und Aliase für Hauptabhängigkeiten, aber sie speichern **keine aufgelösten Versionen von transitiven Abhängigkeiten**. Das bedeutet, dass bei jeder Auflösung neue (ggf. andere) transitive Abhängigkeiten aufgelöst werden können, es sei denn, man verwendet einen Lockfile zusätzlich.
2. **Transitive Abhängigkeiten fixieren**:
    
    - Lockfiles speichern neben den Hauptabhängigkeiten auch die genauen Versionen der **transitiven Abhängigkeiten**, was hilft, unvorhergesehene Probleme durch automatisch aufgelöste Abhängigkeiten zu vermeiden.
    - TOML-Dateien fokussieren sich nur auf die Verwaltung von **direkten Abhängigkeiten** und können keine explizite Kontrolle über alle transitiven Abhängigkeiten gewährleisten.
3. **Automatische Versionserkennung für dynamische Abhängigkeiten**:
    
    - In Gradle können dynamische Versionen verwendet werden (z. B. 6.`1.+`, um automatisch die neueste Patch-Version zu erhalten). Ein **Lockfile** friert die tatsächlich verwendete Version ein, wenn eine solche dynamische Abhängigkeit aufgelöst wird.
    - **TOML-Dateien** unterstützen zwar die Definition von Abhängigkeiten, haben aber keine Mechanismen, um dynamische Versionen zu fixieren. TOML ist also nicht in der Lage, die tatsächlich aufgelöste Version einer dynamischen Abhängigkeit zu sichern.

Lockfiles sind entscheidend, um **reproduzierbare Builds** zu gewährleisten, da sie sicherstellen, dass bei jedem Build exakt die gleichen Versionen von Abhängigkeiten, einschließlich der transitiven Abhängigkeiten, verwendet werden. Ohne Lockfiles könnten dynamische Versionen oder Änderungen in den Abhängigkeiten zu unvorhersehbarem Verhalten führen, was die Stabilität eines Projekts gefährden kann.

**Zusammengefasst**

- **Lockfile**:
    - **Sichert** die exakte Version aller Abhängigkeiten (einschließlich **transitiver** Abhängigkeiten).
    - Gewährleistet **reproduzierbare** Builds.
    - Unterstützt das **Fixieren** dynamischer Versionen.
- **TOML**:
    - **Zentralisiert** die Versionen und Definitionen von Abhängigkeiten, macht das Versionsmanagement übersichtlicher.
    - Verwaltet nur **direkte** Abhängigkeiten und legt keine Informationen über transitive Abhängigkeiten oder dynamische Auflösungen ab.

Daher wird der Ansatz via TOML File bei dem Depdendency Locking nicht weiter verfolgt.

# Meetings

|                              |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               |
| ---------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Austausch am** 12 Aug 2024 |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               |
| **Teilnehmende**             | [Danny Werner](https://wiki.bconnect.barmer.de/display/~be30737) [Ulrich Deiters](https://wiki.bconnect.barmer.de/display/~BE83369) [Gisbert Amm](https://wiki.bconnect.barmer.de/display/~be83372) [Hartmut Lege](https://wiki.bconnect.barmer.de/display/~be04294) [Michael Kuhardt](https://wiki.bconnect.barmer.de/display/~be04295)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
| **Inhalt**                   | - Beim im PoC verfolgten Ansatz iterieren wir über alle Tasks innerhalb des TaskGraph. Somit bekommen wir im Ergebnis alle Abhängigkeiten, die in jedem Task enthalten sind. Man sieht somit, welche Konfiguration welche Abhängigkeiten enthält.<br>- Es ist auch erkennbar, dass einzelne Bibliotheken in unterschiedlichen Versionen in die Konfigurationen einfließt.<br>- Gegenüber einem früheren Ansatz haben wir im PoC nur eine lock-Datei verwendet<br>- Derzeit gehen wir davon aus, dass man nicht oder nur sehr schwer einzelne Bibliotheken isoliert hochziehen kann, da nicht sämtliche transitiven Abhängigkeiten vorhanden sind. Das bedeutet, bei einer Aktualisierung einer Bibliothek würden alle transitien Abhängigkeiten wieder neu aufgelöst und ggbf. in anderen Versionen einfliessen.  <br>     → Derzeit müsste also das lock-File neu geschrieben werden und man hat maximal (aufwändige) Option, nur ausgewählte Änderungen zu comitten.<br>    - Es kann sein, dass wir über force wirklich eine Forcierung definieren können<br>    - Gradle empfiehlt, das lock-File nicht zu modifizieren<br>- Weiterhin gehen wir davon aus, dass Gradle bei mehreren festgelegten Versionen noch immer die neueste Version verwendet<br>- Wir haben Probleme gehabt, das Verimi SDK einzubinden. Workaround: Definition als Abhängigkeit der BARMER-App. Dadurch ist es aber auch eine Abhängigkeit von EasyLogin. Annahme: Wenn es nicht genutzt wird, sollte ProGuard es entfernen<br>- Integration des Authenticator Moduls auch noch unklar<br>    - Eigentlich bräuchten wir einen EasyLogin- und einen DigIdent-Authenticator<br>    - Aktuell ist unklar, ob das zugehörige Projekt richtig aufgesetzt ist oder ob `digidentImplementation`  nicht korrekt funktioniert.<br>    - Annahme: Die Abhängigkeiten des Projekts werden aktuell gar nicht ermittelt. Potenziell läuft das Locking der BaseApp und diese kennt das Modul vermutlich nicht |
| **Todos**                    | - In welchen Dateien muss bei einem Update einer Bibliothek was geändert werden (Scope Android only sowie RN)  <br>    - Keine Änderung im klassischen Sinne sondern es muss das gradle.lockfile neu geschrieben und commit werden  <br>        → [APP-15097](https://jira.bconnect.barmer.de/browse/APP-15097)  <br>        <br>- Analyse: Wie genau funktioniert force als resolution strategy? Überschreibt dies andere Anforderungen? Welche Auswirkungen hat dies auf Laufzeit-Abhängigkeiten, die zur Compile-Time Gradle zur Analyse nicht zur Verfügung stehen  <br>    → [APP-15427](https://jira.bconnect.barmer.de/browse/APP-15427)  <br>    <br>- Prüfung: Was für Versionen fließen bei nicht definierten Abhängigkeite ein? Fließen Versionen a la alpha / snapshot etc ein?  <br>    → [APP-15453](https://jira.bconnect.barmer.de/browse/APP-15453)  <br>    <br>- Prüfung: Wie würde ein DependencyLocking in einem Multiprojekt in Android korrekt funktionieren?  <br>    → Sie Analyse Authenticator Module vs. BARMER-App  <br>    <br>- Prüfung: Bringt DependencyLocking auf dem Authenticator etwas? Welche Auswirkungen hat dies? Einschränkung: Das kannt derzeit nur DigIdent → Geklärt und beschrieben im Meeting vom 19 Aug 2024                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                |

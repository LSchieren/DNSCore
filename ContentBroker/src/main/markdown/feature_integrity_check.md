# Leistungsmerkmal: Integritätsprüfung

Dieser Test kann nicht nur durch eine Person mit vollen Administrationsrechten auf das Gesamtsystem durchgeführt werden.

#### Kontext:

* [ATIntegrityCheck](../../test/java/de/uzk/hki/da/at/ATIntegrityCheck.java)

## Hintergrund:

Gilt für alle nachfolgenden Szenarien!

#### Testpakete:

```
  (GitHub) ATUseCaseIngest1.tgz
  (GitHub) verändertes_tar.pack_1.tar
```

#### Vorbedingungen:

* Dieses Feature kann momentan nur durch testweise deaktivierung der Lizenzüberprüfung getestet werden.
* Der Tester ist unter der Rolle "Contractor" angemeldet/eingeloggt in der "DAWeb"
* Der Tester hat volle Administrationsrechte: Rolle: "DA-Admin,Knotenadmin"

#### Durchführung:

* Als "Contractor": Das Paket wird in den Vertragspartner-Eingangsordner abgelegt.
* Als "Contractor": Die Paketverarbeitung wird gestartet über die Maske "Verarbeitung für abgelieferte SIP starten".
* Als "Contractor": Warten auf die Email mit dem Einlieferungsbeleg.

## Szenario AT-IP-1 Eine fremde Kopie wird beschädigt

#### Durchführung:

* Siehe Hintergrund.
* Als "Administrator": Beschädigung der Kopie: Die auf einem der fremden Knoten liegende Kopie wird ausgetauscht durch verändertes_tar.pack_1.tar (Nach Impl entfernen: Im AT Die Checksumme der fremden Kopie wird manipuliert, um einen geänderten Stand anzuzeigen.)
* Als "Contractor": Ca. 30 Sekunden warten. Dann Einsichtnahme in die DA-Web "eingelieferte Objekte".

#### Akzeptanzkriterien:

* Das Objekt wird als invalide gemeldet (konkret : button Achtung in der Spalte X)

## Szenario AT-IP-2 Eine eigene Kopie wird beschädigt

#### Durchführung:

* Siehe Hintergrund.
* Als "Administrator": Beschädigung der lokalen Kopie: Die auf einem der lokealen Knoten liegende Kopie wird ausgetauscht durch verändertes_tar.pack_1.tar
* Als "Contractor": Ca. 30 Sekunden warten. Dann Einsichtnahme in die DA-Web "eingelieferte Objekte".

#### Akzeptanzkriterien:

* Das Objekt wird als invalide gemeldet (konkret : button Achtung in der Spalte X)

## Szenario AT-IP-3 Alle Kopien werden beschädigt

* Siehe Hintergrund.
* Als "Administrator": Beschädigung der lokalen Kopie: Die auf einem der lokealen Knoten liegende Kopie wird ausgetauscht durch verändertes_tar.pack_1.tar
* Als "Administrator": Beschädigung der Kopie: Die auf einem der fremden Knoten liegende Kopie wird ausgetauscht durch verändertes_tar.pack_1.tar (Nach Impl entfernen: Im AT Die Checksumme der fremden Kopie wird manipuliert, um einen geänderten Stand anzuzeigen.)
* Als "Contractor": Ca. 30 Sekunden warten. Dann Einsichtnahme in die DA-Web "eingelieferte Objekte".

#### Akzeptanzkriterien:

* Das Objekt wird als invalide gemeldet (konkret : button Achtung in der Spalte X)

## Szenario AT-IP-4 Alle Kopien bleiben intakt

* Siehe Hintergrund.
* Als "Contractor": Ca. 30 Sekunden warten. Dann Einsichtnahme in die DA-Web "eingelieferte Objekte".

#### Akzeptanzkriterien:

* Das Objekt wird als valide gemeldet (konkret : kein button Achtung in der Spalte X)

## Szenario AT-IP-5 Die Checksummen der fremden Knoten sind veraltet

* Siehe Hintergrund.
* Die Checksummenneuerstellung ist älter als ein Jahr (der DB Eintrag in der Tabelle Copies für checksumDate wird verändert). 

#### Akzeptanzkriterien:

* Das Objekt wird als invalide gemeldet (konkret : button Achtung in der Spalte X)


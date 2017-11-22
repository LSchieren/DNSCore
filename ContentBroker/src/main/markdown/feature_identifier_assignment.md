# Leistungsmerkmal: Vergabe von Identifiern

In DNSCore gibt es verschiedene Objektbezogene Identifier.

* Der technische Identifier, der systemweit eindeutig ist und automatisch vom System vergeben wird.
* Der Originalname des Paketes, der vom Vertragspartner vergeben wird. Dieser ist für den jeweiligen Vertragspartner eindeutig und ermöglicht eine Zuordnung von Deltas zu einem Objekt.
* Die URN. Diese wird entweder vom System automatisch generiert oder vom Vertragspartner vergeben.

Die URN wird in jedem Fall nur einmal vergeben. Im Falle von Deltas wird die URN nicht abgeändert.

#### Kontext:

* Dokumentation: SIPSpezifikation [URN-Vergabe](specification_sip.de.md#urn-vergabe)

## Hintergrund:

#### Vorbedingungen:

* Dieses Feature kann momentan nur durch testweise deaktivierung der Lizenzüberprüfung getestet werden.
* Der Nutzer ist unter der Rolle "Contractor" angemeldet/eingeloggt in der "DAWeb"

#### Durchführung:

1. Das Paket wird in den Vertragspartner-Eingangsordner abgelegt.
1. Die Paketverarbeitung wird gestartet über die Maske "Verarbeitung für abgelieferte SIP starten".
1. Warten auf die Email mit dem Einlieferungsbeleg.

## Szenario AT-IV-1 Automatische Vergabe der URN

#### Kontext

* [ATIdentifierAssignment](../../test/java/de/uzk/hki/da/at/ATIdentifierAssignment.java).urnBasedOnTechnicalIdentifier()

#### Testpaket(e):

```
(GitHub) ATUseCaseIngest1.tgz
  data/premis.xml
  data/(Primärdaten)
```

#### Vorbedingungen:

* siehe Hintergrund.

#### Durchführung:

1. siehe Hintergrund.
1. In der Maske "Eingelieferte Objekte" das Objekt per technischem Identifier recherchieren.

#### Akkzeptanzkriterien:

* Die Ziffernfolge des technischen Identifier ist vollständig in der URN enthalten. D.h. die URN urn:nbn:de:xyz-1-2013100836773 muss für den Identfier 1-2013100836773 gebildet worden sein.

## Szenario AT-IV-2 Nutzergesteuerte URN-Vergabe

#### Kontext:

* [ATIdentifierAssignment](../../test/java/de/uzk/hki/da/at/ATIdentifierAssignment.java).urnByUserAssignment()

#### Testpaket(e):

```
(GitHub) ATReadURNFromSIP.tgz
  data/premis.xml
  data/(Weitere Primärdaten)
```


Inhalt premis.xml

```xml
  <object xsi:type="representation">
  <objectIdentifier>
      <objectIdentifierType>URN</objectIdentifierType>
      <objectIdentifierValue>urn:nbn:de:xyz-1-20131008367735</objectIdentifierValue>
  </objectIdentifier>
  </object>
```

#### Vorbedingungen:

* siehe Hintergrund.

#### Durchführung:

1. Siehe Hintergrund.
1. In der Maske "Eingelieferte Objekte" das Objekt per technischem Identifier recherchieren.

#### Akzeptanzkriterien:

* In der Maske "Eingelieferte Objekte" wird das Objekt mit der URN&nbsp;*urn:nbn:de:xyz-1-20131008367735* gelistet.
* Der Einlieferungsbeleg enthält den Hinweis, dass dem Paket die URN&nbsp;*urn:nbn:de:xyz-1-20131008367735* zugewiesen wurde.


## Szenario AT-IV-3 Nutzergesteuerte URN-Vergabe per METS - Datei

Dieses Szenario ist implementiert.

Das oberste Objekt im METS-Baum wird durch eine dmdSec mit der entsprechenden ID beschreiben. Innerhalb dieser dmdSec findet man über mets:mdWrap\-{-}mets:xmlData{-}\-mods:identifier type=urn die entsprechende URN. Es wird diejenige dmdSec berücksichtitgt, welche dem obersten hierarchischen Element (siehe structMap) der METS-Datei entspricht. 

### Kontext:

 [ATReadUrnFromMets](../../test/java/de/uzk/hki/da/at/ATReadUrnFromMets.java).readUrnFromMets()

#### Testpaket(e): 

[ATReadUrnFromMets.tgz](https://cdn.rawgit.com/da-nrw/DNSCore/master/ContentBroker/src/test/resources/at/ATReadUrnFromMets.tgz) 

``` 
(GitHub) ATReadUrnFromMets.tgz enthält
  data/mets.xml
  data/premis.xml
  data/(Weitere Primärdaten)
```

Inhalt mets.xml

Die im Februar 2015 (Mail&nbsp;WG: DA NRW / hier: Testszenario für Digitalisate aus dem LAV) vorgeschlagene Unterbringung der METS lautet wie folgt:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<mets:mets xmlns:mets="http://www.loc.gov/METS/" xmlns:mods="http://www.loc.gov/mods/v3" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <mets:dmdSec ID="dmd35716">
        <mets:mdWrap MDTYPE="MODS">
        <mets:xmlData>
          <mods:mods>
            <mods:identifier type="urn">urn:nbn:de:danrw:de2190-2ddee995-9878-4a76-8a7a-3d135dbded198</mods:identifier>
          </mods:mods>
        </mets:xmlData>
        </mets:mdWrap>
    </mets:dmdSec>
```

#### Vorbedingungen:

* siehe Hintergrund.

#### Durchführung:

1. Siehe Hintergrund.
1. In der Maske "Eingelieferte Objekte" das Objekt per technischem Identifier recherchieren.

#### Akzeptanzkriterien:

* In der Maske "Eingelieferte Objekte" wird das Objekt mit der URN urn:nbn:de:danrw:de2190-2ddee995-9878-4a76-8a7a-3d135dbded198 gelistet.
* Der Einlieferungsbeleg enthält den Hinweis, dass dem Paket die URN urn:nbn:de:danrw:de2190-2ddee995-9878-4a76-8a7a-3d135dbded198 zugewiesen wurde.

## Szenario AT-IV-4 Präzedenzregelung bei mitgelieferter URN in METS und PREMIS

Eine PREMIS-URN wird der METS-URN vorgezogen. 

### Kontext:

 [ATReadUrnFromMets](../../test/java/de/uzk/hki/da/at/ATReadUrnFromMets.java).ignoreUrnInMetsReadPremisUrn()

#### Testpaket(e):

[ATReadUrnFromPremisIgnoreMets.tgz](https://cdn.rawgit.com/da-nrw/DNSCore/master/ContentBroker/src/test/resources/at/ATReadUrnFromPremisIgnoreMets.tgz) 

```
(GitHub) ATReadUrnFromPremisIgnoreMets.tgz enhält
  data/mets.xml
  data/premis.xml
  data/(Weitere Primärdaten)
```

Inhalt export_mets.xml

```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <mets:mets xmlns:mets="http://www.loc.gov/METS/" xmlns:mods="http://www.loc.gov/mods/v3" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <mets:dmdSec ID="dmd35716">
        <mets:mdWrap MDTYPE="MODS">
        <mets:xmlData>
        <mods:mods>
        <mods:titleInfo>
            <mods:title>Nr. 1</mods:title>
        </mods:titleInfo>
        <mods:identifier type="urn">urn:nbn:de:danrw:de2190-f30cfb5b-f914-4973-a5cf-04e110ad55c9</mods:identifier>
        </mods:mods>
        </mets:xmlData>
        </mets:mdWrap>
    </mets:dmdSec>
```

Inhalt premis.xml

```xml
    <object xsi:type="representation">
    <objectIdentifier>
        <objectIdentifierType>URN</objectIdentifierType>
        <objectIdentifierValue>urn:nbn:de:xyz-1-20131008367735</objectIdentifierValue>
    </objectIdentifier>
    </object>
```

#### Vorbedingungen:

* siehe Hintergrund.

#### Durchführung:

1. Siehe Hintergrund.

#### Akzeptanzkriterien:

* In der Maske "Eingelieferte Objekte" wird das Objekt mit der URN urn:nbn:de:xyz-1-20131008367735 gelistet.
* Der Einlieferungsbeleg enthält den Hinweis, dass dem Paket die URN urn:nbn:de:xyz-1-20131008367735 zugewiesen wurde.


## Szenario AT-IV-5 Fehlerhafte Prüfziffer bei nutzergesteuerter URN-Übermittlung

Anforderung unklar.

#### Kontext

#### Testpaket(e): 

#### Vorbedingungen

* siehe Hintergrund.

#### Durchführung

* siehe Hintergrund.

#### Akzeptanzkriterien


## Szenario AT-IV-6 URN-Vergabe bei Deltas

Das Szenario beschreibt den Fall, in dem eine Delta abgeliefert wird, in dem der Nutzer eine URN vergibt. Diese vergebene URN stimmt jedoch nicht mit der URN des Objektes überein, welche in der Erstanlieferung auf Basis des technischen Identifier vergeben wurde. Die URN kann nur einmal vergeben werden.

* Derzeitige Implementation: Die neue URN wird ignoriert.

#### Kontext:

* [ATIdentifierAssignment](../../test/java/de/uzk/hki/da/at/ATIdentifierAssignment.java).keepURNOnDeltaIngest()

#### Testpaket(e):

```
(GitHub) Testpaket 1: ATUseCaseIngest1.tgz
  data/premis.xml
  data/   (Primärdaten)
```

```
(GitHub) Testpaket 2: ATReadURNFromSIP.tgz
  data/premis.xml
  data/(Weitere Primärdaten)
```

Inhalt premis.xml des 2. Paketes

```
    <object xsi:type="representation">
    <objectIdentifier>
        <objectIdentifierType>URN</objectIdentifierType>
        <objectIdentifierValue>urn:nbn:de:xyz-1-20131008367735</objectIdentifierValue>
    </objectIdentifier>
    </object>
```

#### Vorbedingungen:

* siehe Hintergrund.

#### Durchführung:

1. Das erste Paket wird eingeliefert unter einem frei zu wählenden Originalnamen abgelegt. Warten auf Bestätigungsmail (Die Bestätigungsmail enthält eine URN die, auf dem technische Identifier basiert und nicht die urn:nbn:de:xyz-1-20131008367735).
1. Das zweite Paket wird eingeliefert, unter demselben Originalnamen wie das erste Paket.&nbsp;
1. Warten auf Mail.
1. Mailinhalt prüfen.

#### Akzeptanzkriterien:

* Die URN in der zweiten Mail ist mit der URN aus der ersten Mail identisch. Sie ist nicht urn:nbn:de:xyz-1-20131008367735.
* Alternativer Vorschlag: Der Nutzer wird in einer Fehlermeldung darauf hingewiesen, dass die im Paket übergebene URN nicht mit der ursprünglichen Paket URN übereinstimmt. Der weitere Ingest des Delta wird abgelehnt.





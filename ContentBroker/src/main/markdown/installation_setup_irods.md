# iRODS setup für das Presentation Repository

## Vorbedingungen

### iRODS

Zur Datenübertragung der PIPs auf den Server, auf dem das Presentation Repository läuft wird iRODS eingesetzt.

iRODS (Version 3.2) muss daher installiert sein. Siehe:
* [iRODS installation](https://docs.irods.org/4.1.2/manual/installation/)
* [Preparing iRODS for DNSCore](installation_irods_cb.md)
* Die Fileberechtigungen $defFileMode  und $defDirMode müssen in iRODS angepaßt werden. In Script: iRODS/scripts/perl/irodsctl.pl auf 
    
   
     $defFileMode = 0644
     $defDirMode = 0755


### ContentBroker

Die Workflow-Steuerung für die Übertragung, den Ingest und Weiterverarbeitung der PIPs im Presentation Repository wird mit Hilfe des ContentBrokers realisiert.

Für die Installation siehe:
* [Getting started with the ContentBroker](https://github.com/da-nrw/DNSCore/blob/master/ContentBroker/src/main/markdown/getting_started.md)

Achten Sie darauf, dass der ContentBroker mit dem Feature Set "(p)res" installiert wird.

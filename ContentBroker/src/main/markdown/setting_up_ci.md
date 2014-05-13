# Setting up a continuous integration node

## Prerequisites

* Python > 2.7
* Postgres > 9.0
* Oracle Java 1.6
* iRODS = 3.2
* git
* Maven

## Prepare the database

Create 2 databases

### irods db

    name: ICAT
    user: irods

### contentbroker db

    name: CI-CB
    user: cb_usr
    password: 

## Prepare iRODS

Set up iRODS

... TODO

    zonename = c-i
    irodsuser = rods
    irodspassword = 

Create 2 resources 

    WorkResource. name: ciWorkingResource. vaultPath: /ci/storage/WorkArea (irods resource type: cache)
    ArchiveResource. name: ciArchiveResource ... vaultPath: /ci/archiveStorage (irods resource type: archive)

Create a resource group and add archive resource to it.

    ciArchiveResourceGroup 

Modify server/config/irodsHost so that our irods server hostname is cihost

    localhost cihost

## Preparing the directory structure

Create the following folders

    /ci/ContentBroker
    /ci/BuildRepository
    /ci/DNSCore (clone the repo into this folder)
    /ci/storage/UserArea/
                        TEST/
                            incoming/
                            outgoing/
                IngestArea/
                        TEST/
                WorkArea/TEST
    /ci/storage/GridCacheArea/
                         TEST/
    /ci/archiveStorage/
    /ci/iRODS/
                         
As opposed to a regular node install or an installation on a local development workstation,
the dirs are fixed and correspond to the settings in src/main/conf/config.properties.vm3


## Performing a testrun by hand

* mvn test -Pvm3





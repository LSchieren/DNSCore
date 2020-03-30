	/*
	  DA-NRW Software Suite | ContentBroker
	  Copyright (C) 2014 LVRInfoKom
	  Landschaftsverband Rheinland
	
	  This program is free software: you can redistribute it and/or modify
	  it under the terms of the GNU General Public License as published by
	  the Free Software Foundation, either version 3 of the License, or
	  (at your option) any later version.
	
	  This program is distributed in the hope that it will be useful,
	  but WITHOUT ANY WARRANTY; without even the implied warranty of
	  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	  GNU General Public License for more details.
	
	  You should have received a copy of the GNU General Public License
	  along with this program.  If not, see <http://www.gnu.org/licenses/>.
	*/

## Administration of DNS in iRODS-Federation

### Introduction

There are two modes in which you might run DNScore with iRODS as a storage layer.

First, so called "one-zone-approach" (aka integrated mode) in which you decide to use one ICAT-enabled server and serveral resoure hosts. In this approach resources are managed in a more centralized way. Second, 
in multiple "zone-approach" (aka federated mode) in which the iRODS servers have mostly their own resource management at each site. 

In order to do "load balancing" between nodes, having more nodes then just three - and  to avoid overhead of administration at the Master ICAT Zone Server in the single zone 
mode, you could decide to run DNSCore and iRODS in the so called "federated mode". In the federated mode you have several distinct Master ICAT Zones 
forming a releativley loosley coupled Federation in terms of iRODS Servers, with all the advantages and disadvantages this may impose to you. 

In this topology your nodes admins should be able to administer iRODS Master servers and the federation itself
(See iRODS documentation about this: https://docs.irods.org/4.2.3/system_overview/federation). 

Most of the effort has to be done once during setup. The daily maintenance is quite easy and does not differ by scales from the single zone administration effort at the nodes.

Although the iRODS servers are more separated, they still share some common infrastructure (e.g. Object-DB, Formats to convert), they form still a "domain" of shared functionalities.

The feature "iRODS as federated storage layer" described below are compatible to a landscape in which "integrated", "one zone" approach has worked before, though the "federated" mode sits "on top" the integrated mode.

But you can't mix both modes in one Domain yet. Each node will become a "zone" with its own Postgres DB. 

### Definitions

The node on which the itmes are stored first is the "primary copy node", or "the responsible node" for that dedicated item. It's supposed to be the primary node for inquires about data, sending deltas to etc.

All other nodes having copies of the stored items are therefore called "secondary copy nodes". They serve as backup in case of data loss or bit courruption at the primary one. 

### Prerequisites

As needed by iRODs Installation you will need PostgreSQL Database support at each node.

1. Running iRODS Server > 3.2 at zoneA ([here](https://github.com/da-nrw/DNSCore/blob/master/ContentBroker/src/main/markdown/installation_irods_cb.md))
1. Running iRODS Server > 3.2 at zoneB
1. Running ContentBroker 
1. Running Federation between zoneA and zoneB https://docs.irods.org/4.2.3/system_overview/federation) 
1. Federated RuleSet dns.re from ([here](https://github.com/da-nrw/DNSCore/blob/master/ContentBroker/src/main/rules/irodsFederatedGridFacade/dns.re)) activated 
in server.config. 

### Changes needed to ContentBroker

Change implementation of grid drivers to:

    cb.implementation.grid=federatedGridFacade
    cb.implementation.distributedConversion=irodsFederatedDistributedConversionAdapter
 
### Changes needed to iRODS

Create directory for federated items of other zones at zoneA

    imkdir /zoneA/federated

Set the rights for federated copies at least to "own" for the federated folders
e.g.

     ichmod -r own rods#zoneB /zoneA/federated/CONTRACTOR_ZONEB
     
Please consider the most restrictive permissions you are able to set for this.

You should have a RescGroup "lza" containing your long term storage resource at each node. The name of the RescGroup must be same for all nodes. 

     atrg lza <yourLongtermResourceName>

Please give a full "own" rights to user rods#zoneB recursively to folder /zoneA/pips . This folder contains all locally produced Presentation Information Package PIP for the time they needed to be replicated to the presentation node. 
   
    ichmod -r own rods#zoneB /zoneA/pips

Please set inherit mode to enabled
 
 	ichmod -r inherit /zoneA/pips
 
Load the federated dns.re file from ([here](https://github.com/da-nrw/DNSCore/blob/master/ContentBroker/src/main/rules/irodsFederatedGridFacade/dns.re))
and install it to folder 

	iRODS/server/config/reConfigs 
	
Make sure you have enabled the reSet in the server.config as described ([here](https://github.com/da-nrw/DNSCore/blob/master/ContentBroker/src/main/markdown/installation_irods_cb.md)) and you have tested your still working installation by typing at least an 
"ils" command. 

You should be at least familiar with basic icommands such as :

	irsync 
	imeta
	iadmin mkresc
	ichmod
	ils -A
	ils -L
	itrim
	irule
	
Please refer to the iRODS Docs as well!

Please don't forget do do the itrim on your cache devices after some time at each node!

       itrim -age 2000 -N1 -S name_of_cache_resc_at_zone -r /zoneA/aip
       

### How does it work?

Imagine having an AIP in logical namespace

	/zoneA/aip/TEST/123545/123545_pack.1.tar

While ingesting items with ContentBroker, it will initiate the request to fulfill the federation to all connected and writable other zones. It's the iRODS server's obligation to the copies in loosely coupled manner. All information about this process are being logged in CB's grid.log.
	
	/zoneB/federated/zoneA/aip/TEST/123545/123545_pack.1.tar
	
As you might already noticed: The path beneath folder federated is (logically) same as on zoneA.

### Synchronizing Service

Synchronizing is now performed by extra tables named "copy", "CopyJob" and "cooperating_node" of CB. Any rules and cronjobs aren't used anymore. Foreach cooperating node entry, there is an entry in "copyjob" for that AIP and node. When synching has successfully executed with the storage layer (iRODS), and the number of minimum copies has been reached. A "Copy" of 
that AIP is being created. Please refer to the grid.log and rodsLog for debugging. Stored "CopyJobs" are executed ad infinitum per node.  

### Audit Infrastructure

To perform Audit (integrity checking) of AIP iRODS each node must at least provide the time based check 
service of recieved federated copies (aka "Secondary Copies"). This is some kind of "trust".

The Intgegrity check is now performed by contentbroker ([feature]https://github.com/da-nrw/DNSCore/blob/master/ContentBroker/src/main/markdown/feature_integrity_check.md)

### AVU Metadata of iRODS Objects in DNS (AIP/DataObjects) 

Attribute Value Unit (AVU) Metadata are stored in each ICAT. They could be listed with 

	imeta ls -d 1-20141007788.pack_1.tar
	attribute: FEDERATED
	value: 1
	units: 
	----
	attribute: chksum
	value: 37996b3bedcfabf476a5b9c44b90d45b
	units: 
	----
	attribute: SYNCHRONIZED_TO
	value: lvr,
	units: 
	----
	attribute: replicate_to
	value: lza
	units: 
	----
	attribute: MIN_COPIES
	value: 2
	units: 
	----
	attribute: SYNCHRONIZE_EVENT
	value: 01412675041
	units: 

**attribute: FEDERATED**
AVU indicates if AIP was synchronized successfully 

You might be able to trigger re-federation if desired, just by setting this to 0

     imeta mod -d 1-20141007788.pack_1.tar FEDERATE 0

**attribute: chksum**
stores originally computed checksum, backup for security purposes
This value has never to be changed!



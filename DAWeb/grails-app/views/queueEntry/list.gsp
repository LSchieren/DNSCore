<%@ page import="daweb3.QueueEntry" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'queueEntry.label', default: 'QueueEntry')}" />
		<title>Bearbeitungsübersicht</title>
 		<r:require modules="periodicalupdater, jqueryui"/> 
 		<jqui:resources/>
 		<asset:stylesheet src="accordion.css" />
		<g:javascript>
			var order = "desc";
			var sort = "createdAt";
				
			<g:if test="${ !params.search }">		
				var obj = $.PeriodicalUpdater("./listSnippet",
					{
						method: "get",
						minTimeout: 5000,
						maxTimeout: 5000,						
						data: function() {
							return { order: order, sort: sort}
						},
						success: function(data) {
							console.log("success - sort: "+sort+", order: "+order);
							$("#entry-list").html(data);
							$("#entry-list th.field-"+sort).addClass("sorted "+order);
						}
					}
				);
			</g:if>
			function stopUpdater() {		
				obj.stop();
				document.getElementById("starter").disabled=false;
			}
			
			function startUpdater() {
				obj.restart();
				document.getElementById("stopper").disabled=false;
			}
		
			function sortQueue(field) {
				console.log("sortQueue: "+field);
				if (field == sort && order == "asc") order = "desc";
				else order = "asc";
				sort = field;
				console.log("sortQueue - sort: "+sort+", order: "+order);
				return false;
			}
			function test(field) {
				console.log(field);
				return false;
			}
		</g:javascript>	
	</head>
	<body>
		<div class="page-body">
			<div class="blue-box"></div>
			<h2 id="page-header">Bearbeitungsübersicht</h2> 
			<div>	
			<g:form controller="queueEntry" action="deleteSip">
				<g:if test="${!params.search} || ${params.search}">
  					<div class="page-body-input page-body-input-position">  
						<i>Aktualisieren der Seite:&nbsp; </i>
						<input id="stopper" class="style-start-stop" type="button" onclick="stopUpdater();this.disabled=true;" value="stoppen"/>
						 &nbsp;
						<input id="starter" class="style-start-stop" type="button" onclick="startUpdater();this.disabled=true;" disabled value="starten"/>
					</div>
					<button class="style-buttons bearbeitungsuebersicht-extra-btn" onclick="return confirm ('Wirklich alle  fehlerhaften SIP löschen?')" >Fehlerhafte SIP's löschen </button>
					<br>
				</g:if> <br>
			</g:form>
			</div>
			<button class="accordion">Filter
				<g:if test="${params.search }"><br>
		    		<g:if test="${!params.search?.status.isEmpty()}">
		    			<span style="margin-right: 25px"><i>Status: ${params.search?.status} </i></span>
		    		</g:if> 
		    		<g:if test="${!params.search?.obj?.origName.isEmpty()}">
		    			<span style="margin-right: 25px"><i>Originalname: ${params.search?.obj?.origName}</i></span>
		    		</g:if> 
		    		<g:if test="${params.search?.obj?.urn}">
			    		<g:if test="${!params.search?.obj?.urn.isEmpty()}">
			    			 <span style="margin-right: 25px"><i>URN: ${params.search?.obj?.urn}</i></span>
			    		</g:if> 
			    	</g:if>
		    		<g:if test="${!params.search?.obj?.identifier.isEmpty()}">
		    			<span style="margin-right: 25px"><i>Identifier: ${params.search?.obj?.identifier}</i></span>
		    		</g:if> 
		    	</g:if> 
			</button>
			<div class="panel">
	          <g:form name="searchForm" id="filterform" action="list"> 
	           	<table>
	           		<tr>
	           			<td>Status:</td>
	           			<td><g:textField name="search.status" value="${params.search?.status}" size="5" class="input-hoehe"/></td>
	              	</tr>  		
	           	  	<tr>
	           			<td>Originalname:</td>
	           			<td><g:textField name="search.obj.origName" value="${params.search?.obj?.origName}" size="50" class="input-hoehe"/></td>
	           		</tr>
	           		<tr>
	           			<td>URN:</td>
	           			<td><g:textField name="search.obj.urn" value="${params.search?.obj?.urn}" size="50" class="input-hoehe"/></td>
	           		</tr>
	           		<tr>
	           			<td>Identifier:</td>
	           			<td><g:textField name="search.obj.identifier" value="${params.search?.obj?.identifier}" size="50" class="input-hoehe"/></td>
	           		</tr>
	           		<tr>
	           			<td></td>
	           			<td>
		           			<g:submitButton class="style-buttons" name="submit" value="Filter anwenden"/>
		           			<g:submitButton class="style-buttons" name="loeschen" type="submit" value="Filter löschen"/>
	           			</td>
	           			<g:javascript>
		           			$(document).ready(function(){
	           				 	$("#loeschen").click(function() {                				 
			            			$('#searchForm').find(':input').each(function() {
			            	            switch(this.type) {
			                            case 'text':
			                            	$(this).val('');
			                                break;                      
			                            case 'textarea':
			                                $(this).val('');
			                                break;
			            			 	case 'hidden':
			                                $(this).val('0');
			                                break;
			                            }
			            			});
	           				    });
		           			});
		           		</g:javascript>
	           		</tr>
	         	</table>     
	          </g:form>
	          <script>
					var acc = document.getElementsByClassName("accordion");
					var i;
					for (i = 0; i < acc.length; i++) {
					    acc[i].onclick = function(){
					        this.classList.toggle("active", true);
					        var panel = this.nextElementSibling;
					        if (panel.style.display == "block") {
					            panel.style.display = "none";
					        } else {
					            panel.style.display = "block";
					        }		
					    }
					}
				</script>
	        </div>
	    	<div id="list-queueEntry" class="content scaffold-list" role="main">
				<!-- This div is updated through the periodical updater -->
				<div id="entry-list">
					<g:include action="listSnippet" />
				</div>
			</div>
			<button class="accordionStatus">Hinweise zu den Statuscodes: </button>
			<div class="panel abstand-oben">
				<div>
					<p style="font-style:italic">xx000 bedeutet "wartend", xx2: bezeichnet "arbeitend" - hingegen bezeichnen xx1,xx3,xx4,xx5,xx6,xx7,xx8 einen bestimmten Fehler, Genaueres hier: <a href="https://github.com/da-nrw/DNSCore/blob/master/ContentBroker/src/main/markdown/administration-troubleshooting.de.md" target="err" >Fehlercodes</a></p> 
					<table>
					<!-- ingest-->
						<tr><th>Statuscode</th><th>Kurzbezeichnung</th><th>Beschreibung</th></tr>
						<tr><td>110</td><td>IngestUnpackAction</td><td>Auspacken & Vollständigkeitstests</td></tr>
						<tr><td>120</td><td>IngestRegisterObjectAction</td><td>Objekt- oder Deltaerkennung, Typerkennung</td></tr>
						<tr><td>220</td><td>IngestScanAction</td><td>Formaterkennung</td></tr>
						<tr><td>230</td><td>IngestConvertAction</td><td>LZA Konvertierung</td></tr>
						<tr><td>250</td><td>IngestMetadataUpdateAction</td><td>Update der Metadaten</td></tr>
						<tr><td>260</td><td>IngestCheckFormatAction</td><td>Überprüfung der LZA Konvertierung</td></tr>
						<tr><td>270</td><td>IngestQualityLevelCheckAction</td><td>Ermittlung einer Qualitätsstufe</td></tr>		
						<tr><td>290</td><td>IngestCreatePremisAction</td><td>Bearbeitung der PREMIS-Datei</td></tr>
						<tr><td>310</td><td>IngestScanForPresentationAction</td><td>Formaterkennung für Präsentation auf Basis der LZA Formate</td></tr>
						<tr><td>320</td><td>IngestConvertForPresentationAction</td><td>Bildung der PIPs (Präsentationsderivate)</td></tr>
						<tr><td>330</td><td>IngestPreProcessForPresentationAction</td><td>Verschieben der PIPs</td></tr>
						<tr><td>340</td><td>IngestShortenFilenamesAction</td><td>Kürzung der PIP Dateinamen</td></tr>
						<tr><td>350</td><td>IngestPreUpdateMetadataAction</td><td>Update der Metadaten nach PIP Erstellung</td></tr>
						<tr><td>360</td><td>IngestPrepareSendToPresenterAction</td><td>Anmeldung der PIP zur Übertragung ans Pres. Repository</td></tr>
						<tr><td>370</td><td>IngestBuildAIPAction</td><td>AIP Erstellung</td></tr>
						<tr><td>380</td><td>IngestTarAction</td><td>AIP Erstellung als TAR-Archiv</td></tr>
						<tr><td>400</td><td>ArchiveReplicationAction</td><td>Ablage auf LZA Medien und Replikation</td></tr>
						<tr><td>440</td><td>ArchiveReplicationCheckAction</td><td>Prüfung der Replikationen</td></tr>
						<!-- presentation-->
						<tr><td>540</td><td>FetchPIPsAction</td><td>Replikation der PIP an den Presentation Repository Knoten</td></tr>
						<tr><td>550</td><td>SendToPresenterAction</td><td>Einspielung der PIP in das Presentation Repository</td></tr>
						<tr><td>560</td><td>CreateEDMAction</td><td>EDM Metadaten-Erstellung</td></tr>
						<tr><td>570</td><td>IndexESAction</td><td>Indizierung im Elasticsearch Suchindex</td></tr>			
						<tr><td>580</td><td>FriendshipConversionAction</td><td>Konvertierung auf anderem Knoten</td></tr>			
						<tr><td>600</td><td>RestartIngestWorkflowAction</td><td>Zurücksetzung des Ingestworkflows</td></tr>			
						<tr><td>645</td><td>RestartIngestWorkflowAction</td><td>Warten auf Rückfrage</td></tr>
						<!-- pipgen-->
						<tr><td>700</td><td>PIPGenObjectToWorkareaAction</td><td>Übertragung von AIP an das Knotenarbeitsverzeichnis</td></tr>
						<tr><td>710</td><td>PIPGenScanForPresentationAction</td><td>Scannen der Präsentationsformate</td></tr>
						<tr><td>720</td><td>PIPGenConvertForPrestationAction</td><td>Bildung der PIPs (Präsentationsderivate)</td></tr>
						<tr><td>730</td><td>PIPGenPreProcessForPresentationAction</td><td>Verschieben der PIPs</td></tr>
						<tr><td>740</td><td>PIPGenShortenFilenamesAction</td><td>Kürzung der PIP Dateinamen</td></tr>
						<tr><td>750</td><td>PIPGenPreUpdateMetadataAction</td><td>Update der Metadaten nach PIP Erstellung</td></tr>
						<tr><td>760</td><td>PIPGenPrepareSendToPresenterAction</td><td>Anmeldung der PIP zur Übertragung ans Pres. Repository</td></tr>
						<tr><td>770</td><td>PIPGenCleanWorkareaAction</td><td>Säuberung des Arbeitsverzeichnisses</td></tr>
						<tr><td>800</td><td>DeleteSIPPackageAction</td><td>Löschung des SIP vom Arbeitsverzeichnis</td></tr>
						<tr><td>900</td><td>RetrievalObjectToWorkAreaAction</td><td>Auslesen eines AIP von den LZA Medien</td></tr>
						<tr><td>910</td><td>RetrievalAction</td><td>Bildung des DIP, Übertragung an das Ausgabeverzeichnis des Contractor</td></tr>
						<tr><td>950</td><td>RetrievalDeliveredDIPAction</td><td>Warten auf Abholung durch Contractor</td></tr>
						<tr><td>960</td><td>PostRetrievaAction</td><td>Wurde abgeholt, vorbereiten auf Löschung DIP</td></tr>
						<tr><td>5000</td><td>AuditAction</td><td>Überprüfung des AIP</td></tr>
					</table>
<!-- 					
					<table>

			<tr><th>Statuscode</th><th>Kurzbezeichnung</th><th>Beschreibung</th></tr>
			<tr><td>110</td><td>IngestUnpackAction</td><td>Auspacken & Vollständigkeitstests</td></tr>
		<tr><td>130</td><td>IngestRestructureAction</td><td>Virus-Scan und Reorganisation der Ordnerstruktur</td></tr>
		<tr><td>140</td><td>IngestValidateMetadataAction</td><td>Validierung der Metadaten</td></tr>
		<tr><td>150</td><td>IngestScanAction</td><td>Formaterkennung</td></tr>
		<tr><td>160</td><td>IngestRegisterURNAction</td><td>Register URN in DNS</td></tr>
			<tr><td>230</td><td>IngestConvertAction</td><td>LZA Konvertierung</td></tr>
			<tr><td>250</td><td>IngestUpdateMetadataAction</td><td>Update der Metadaten</td></tr>
			<tr><td>260</td><td>IngestCheckFormatAction</td><td>Überprüfung der LZA Konvertierung</td></tr>	
		<tr><td>270</td><td>IngestQualityLevelCheckAction</td><td>Ermittlung einer Qualitätsstufe</td></tr>		
			<tr><td>290</td><td>IngestCreatePremisAction</td><td>Bearbeitung der PREMIS-Datei</td></tr>
			<tr><td>310</td><td>IngestScanForPresentationAction</td><td>Formaterkennung für Präsentation auf Basis der LZA Formate</td></tr>
			<tr><td>320</td><td>IngestConvertForPresentationAction</td><td>Bildung der PIPs (Präsentationsderivate)</td></tr>
			<tr><td>340</td><td>IngestShortenFilenamesAction</td><td>Kürzung der PIP Dateinamen</td></tr>
			<tr><td>350</td><td>IngestPresUpdateMetadataAction</td><td>Update der Metadaten nach PIP Erstellung</td></tr>
			<tr><td>360</td><td>IngestPrepareSendToPresenterAction</td><td>Anmeldung der PIP zur Übertragung ans Pres. Repository</td></tr>
			<tr><td>370</td><td>IngestBuildAIPAction</td><td>AIP Erstellung</td></tr>
			<tr><td>380</td><td>IngestTarAction</td><td>AIP Erstellung als TAR-Archiv</td></tr>
			<tr><td>400</td><td>IngestArchiveReplicationAction</td><td>Ablage auf LZA Medien und Replikation</td></tr>
			<tr><td>440</td><td>IngestArchiveReplicationCheckAction</td><td>Prüfung der Replikationen</td></tr>
			
			<tr><td>540</td><td>FetchPIPsAction</td><td>Replikation der PIP an den Presentation Repository Knoten</td></tr>
			<tr><td>550</td><td>CreateDCAction</td><td>DC Metadaten-Erstellung</td></tr>
			<tr><td>560</td><td>SendToPresenterAction</td><td>Einspielung der PIP in das Presentation Repository</td></tr>
			<tr><td>570</td><td>CreateEDMAction</td><td>EDM Metadaten-Erstellung</td></tr>
			<tr><td>580</td><td>IndexMetadataAction</td><td>Indizierung im Elasticsearch Suchindex</td></tr>			
			<tr><td>600</td><td>RestartIngestWorkflowAction</td><td>Zurücksetzung des Ingestworkflows</td></tr>			
			<tr><td>640</td><td>ProcessUserDecisionsAction</td><td>Warten auf Nutzerentscheidung</td></tr>	
		
			<tr><td>700</td><td>PIPGenObjectToWorkareaAction</td><td>Übertragung von AIP an das Knotenarbeitsverzeichnis</td></tr>
			<tr><td>710</td><td>PIPGenScanForPresentationAction</td><td>Scannen der Präsentationsformate</td></tr>
			<tr><td>720</td><td>PIPGenConvertForPrestationAction</td><td>Bildung der PIPs (Präsentationsderivate)</td></tr>
			<tr><td>730</td><td>PIPGenPreProcessForPresentationAction</td><td>Verschieben der PIPs</td></tr>
			<tr><td>740</td><td>PIPGenShortenFilenamesAction</td><td>Kürzung der PIP Dateinamen</td></tr>
			<tr><td>750</td><td>PIPGenPreUpdateMetadataAction</td><td>Update der Metadaten nach PIP Erstellung</td></tr>
			<tr><td>760</td><td>PIPGenPrepareSendToPresenterAction</td><td>Anmeldung der PIP zur Übertragung ans Pres. Repository</td></tr>
			<tr><td>770</td><td>PIPGenCleanWorkareaAction</td><td>Säuberung des Arbeitsverzeichnisses</td></tr>
			<tr><td>800</td><td>DeleteSIPPackageAction</td><td>Löschung des SIP vom Arbeitsverzeichnis</td></tr>
			<tr><td>900</td><td>RetrievalObjectToWorkAreaAction</td><td>Auslesen eines AIP von den LZA Medien</td></tr>
			<tr><td>910</td><td>RetrievalAction</td><td>Bildung des DIP, Übertragung an das Ausgabeverzeichnis des Contractor</td></tr>
			<tr><td>950</td><td>RetrievalDeliveredDIPAction</td><td>Warten auf Abholung durch Contractor</td></tr>
			<tr><td>960</td><td>PostRetrievaAction</td><td>Wurde abgeholt, vorbereiten auf Löschung DIP</td></tr>
			
			<tr><td>5000</td><td>AuditAction</td><td>Überprüfung des AIP</td></tr>
			</table>
				-->	
					
					<a target="liste" href="https://docs.google.com/drawings/d/1qEd_LVNXKiiHmAW_LKNL0hEkS6ixcVf8TgO9-5a9WrM/edit?pli=1">(Quelle: Workflowdiagramm)</a>
				</div>
			
				<script>
					var acc = document.getElementsByClassName("accordionStatus");
					var i;
					for (i = 0; i < acc.length; i++) {
					    acc[i].onclick = function(){
					        this.classList.toggle("active", true);
					        var panel = this.nextElementSibling;
					        if (panel.style.display == "block") {
					            panel.style.display = "none";
					        } else {
					            panel.style.display = "block";
					        }		
					    }
					}
				</script>
			</div>
		</div>
	</body>
</html>

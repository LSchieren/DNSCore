<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="daweb3.SystemEvent" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'systemEvent.label', default: 'System-Eventsteuerung')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<div class="page-body">
			<div id="list-systemEvent" class="content scaffold-list" role="main">
				<div class="blue-box"></div>
				<h2><g:message code="default.list.label" args="[entityName]" /></h2>
				<g:if test="${flash.message}">
					<div class="message" role="status">${flash.message}</div>
				</g:if>
				<a href="#list-systemEvent" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
				<div class="nav" role="navigation">
					<ul>
						<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
					</ul>
				</div>
				<div class="table-style">
<!-- 			    <div style="overflow:auto; height: 200px"> -->
				  <table>
					<thead class="thead-line">
						<tr>
							<g:sortableColumn property="parameter" title="${message(code: 'systemEvent.parameter.id', default: 'Id')}" />
							<g:sortableColumn property="last_executed" title="${message(code: 'systemEvent.last_executed.label', default: 'Lastexecuted')}" />
							<g:sortableColumn property="type" title="${message(code: 'systemEvent.type.label', default: 'Type')}" />
						</tr>
					</thead>
					<tbody>
					<g:each in="${systemEventInstanceList}" status="i" var="systemEventInstance">
						<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
						
							<td><g:link action="show" id="${systemEventInstance.id}">${fieldValue(bean: systemEventInstance, field: "id")}</g:link></td>
						
							<td><g:formatDate date="${systemEventInstance.last_executed}" /></td>
						
							<td>${fieldValue(bean: systemEventInstance, field: "type")}</td>
						</tr>
					</g:each>
					</tbody>
				</table>
				</div>
				<div class="pagination">
					<g:paginate total="${systemEventInstanceCount ?: 0}" />
				</div>
			</div>
		</div>
	</body>
</html>

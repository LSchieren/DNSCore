<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="daweb3.SystemEvent" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'systemEvent.label', default: 'SystemEvent')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<div class="page-body">
			<div class="blue-box"></div>
			<h2><g:message code="default.show.label" args="[entityName]" /></h2>
			<a href="#show-systemEvent" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
			<div class="nav" role="navigation">
				<ul>
					<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
				</ul>
			</div>
			<div id="show-systemEvent" class="content scaffold-show" role="main">
				<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
				</g:if>
				<ol class="property-list systemEvent">
				
					<g:if test="${systemEventInstance?.id}">
					<li class="fieldcontain">
						<span id="id-label" class="property-label"><g:message code="systemEvent.id.label" default="Id" /></span>
						
							<span class="property-value" aria-labelledby="id-label">${systemEventInstance?.id?.encodeAsHTML()}</span>
						
					</li>
					</g:if>
				
					<g:if test="${systemEventInstance?.parameter}">
					<li class="fieldcontain">
						<span id="parameter-label" class="property-label"><g:message code="systemEvent.parameter.label" default="Parameter" /></span>
						
							<span class="property-value" aria-labelledby="parameter-label"><g:fieldValue bean="${systemEventInstance}" field="parameter"/></span>
						
					</li>
					</g:if>
				
					<g:if test="${systemEventInstance?.period}">
					<li class="fieldcontain">
						<span id="period-label" class="property-label"><g:message code="systemEvent.period.label" default="Period" /></span>
						
							<span class="property-value" aria-labelledby="period-label"><g:fieldValue bean="${systemEventInstance}" field="period"/></span>
						
					</li>
					</g:if>
				
					<g:if test="${systemEventInstance?.last_executed}">
					<li class="fieldcontain">
						<span id="last_executed-label" class="property-label"><g:message code="systemEvent.last_executed.label" default="Lastexecuted" /></span>
						
							<span class="property-value" aria-labelledby="last_executed-label"><g:formatDate date="${systemEventInstance?.last_executed}" /></span>
						
					</li>
					</g:if>
				
					<g:if test="${systemEventInstance?.node}">
					<li class="fieldcontain">
						<span id="node-label" class="property-label"><g:message code="systemEvent.node.label" default="Node" /></span>
						
							<span class="property-value" aria-labelledby="node-label"><g:link controller="cbNode" action="show" id="${systemEventInstance?.node?.id}" params="['sysid' : systemEventInstance?.id]">${systemEventInstance?.node?.encodeAsHTML()}</g:link></span>
						
					</li>
					</g:if>
				
					<g:if test="${systemEventInstance?.type}">
					<li class="fieldcontain">
						<span id="type-label" class="property-label"><g:message code="systemEvent.type.label" default="Type" /></span>
						
							<span class="property-value" aria-labelledby="type-label"><g:fieldValue bean="${systemEventInstance}" field="type"/></span>
						
					</li>
					</g:if>
				
					<g:if test="${systemEventInstance?.user}">
					<li class="fieldcontain">
						<span id="user-label" class="property-label"><g:message code="systemEvent.user.label" default="User" /></span>
						
							<span class="property-value" aria-labelledby="user-label"><g:link controller="user" action="show" id="${systemEventInstance?.user?.id}" params="['sysid' : systemEventInstance?.id]">${systemEventInstance?.user?.encodeAsHTML()}</g:link></span>
						
					</li>
					</g:if>
				
				</ol>
				<g:form url="[resource:systemEventInstance, action:'delete']" method="DELETE">
					<fieldset class="buttons">
						<g:actionSubmit class="edit" action="edit" resource="${systemEventInstance}" value="${message(code: 'default.button.edit.label', default: 'Edit')}" />
						<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
					</fieldset>
				</g:form>
			</div>
		</div>
	</body>
</html>

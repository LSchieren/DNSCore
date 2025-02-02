
<%@ page import="daweb3.User" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'user.label', default: 'Benutzer')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<div class="page-body">
			<div class="blue-box"></div>
			<h2><g:message code="default.show.label" args="[entityName]" /></h2>
			<a href="#show-user" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
			<div class="nav" role="navigation">
				<ul>
					<g:if test="${params.sysid}" >
						<li><g:link class="show" controller="SystemEvent" action="show" id="${params.sysid}">SystemEvent anzeigen</g:link></li>
					</g:if>
					<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
					<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
				</ul>
			</div>
			<div id="show-user" class="content scaffold-show" role="main">
				<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
				</g:if>
				<ol class="property-list user">
				
					<g:if test="${userInstance?.email_contact}">
					<li class="fieldcontain">
						<span id="email_contact-label" class="property-label"><g:message code="user.email_contact.label" default="Emailcontact" /></span>
						
							<span class="property-value" aria-labelledby="email_contact-label"><g:fieldValue bean="${userInstance}" field="email_contact"/></span>
						
					</li>
					</g:if>
				
					<g:if test="${userInstance?.shortName}">
					<li class="fieldcontain">
						<span id="shortName-label" class="property-label"><g:message code="user.shortName.label" default="Short Name" /></span>
						
							<span class="property-value" aria-labelledby="shortName-label"><g:fieldValue bean="${userInstance}" field="shortName"/></span>
						
					</li>
					</g:if>
				
					<g:if test="${userInstance?.username}">
					<li class="fieldcontain">
						<span id="username-label" class="property-label"><g:message code="user.username.label" default="Username" /></span>
						
							<span class="property-value" aria-labelledby="username-label"><g:fieldValue bean="${userInstance}" field="username"/></span>
						
					</li>
					</g:if>
				
					<g:if test="${userInstance?.password}">
					<li class="fieldcontain">
						<span id="password-label" class="property-label"><g:message code="user.password.label" default="Password" /></span>
						
							<span class="property-value" aria-labelledby="password-label"><g:fieldValue bean="${userInstance}" field="password"/></span>
						
					</li>
					</g:if>
				
					<g:if test="${userInstance?.description}">
					<li class="fieldcontain">
						<span id="description-label" class="property-label"><g:message code="user.description.label" default="Description" /></span>
						
							<span class="property-value" aria-labelledby="description-label"><g:fieldValue bean="${userInstance}" field="description"/></span>
						
					</li>
					</g:if>
				
					<g:if test="${userInstance?.forbidden_nodes}">
					<li class="fieldcontain">
						<span id="forbidden_nodes-label" class="property-label"><g:message code="user.forbidden_nodes.label" default="Forbiddennodes" /></span>
						
							<span class="property-value" aria-labelledby="forbidden_nodes-label"><g:fieldValue bean="${userInstance}" field="forbidden_nodes"/></span>
						
					</li>
					</g:if>
				
					<g:if test="${userInstance?.accountExpired}">
					<li class="fieldcontain">
						<span id="accountExpired-label" class="property-label"><g:message code="user.accountExpired.label" default="Account Expired" /></span>
						
							<span class="property-value" aria-labelledby="accountExpired-label"><g:formatBoolean boolean="${userInstance?.accountExpired}" /></span>
						
					</li>
					</g:if>
				
					<g:if test="${userInstance?.accountLocked}">
					<li class="fieldcontain">
						<span id="accountLocked-label" class="property-label"><g:message code="user.accountLocked.label" default="Account Locked" /></span>
						
							<span class="property-value" aria-labelledby="accountLocked-label"><g:formatBoolean boolean="${userInstance?.accountLocked}" /></span>
						
					</li>
					</g:if>
				
					<g:if test="${userInstance?.enabled}">
					<li class="fieldcontain">
						<span id="enabled-label" class="property-label"><g:message code="user.enabled.label" default="Enabled" /></span>
						
							<span class="property-value" aria-labelledby="enabled-label"><g:formatBoolean boolean="${userInstance?.enabled}" /></span>
						
					</li>
					</g:if>
					
					<g:if test="${userInstance?.enabled}">
					<li class="fieldcontain">
						<span id="enabled-label" class="property-label">
							<g:message code="user.minIngestQualityLevel.label" default="Mindest-Qualitätsstufe" />
						</span>
						<span class="property-value" aria-labelledby="minIngestQualityLevel-label">
							<g:fieldValue bean="${userInstance}" field="minIngestQualityLevel"/>
						</span>
						
					</li>
					</g:if>
				
					<g:if test="${userInstance?.passwordExpired}">
					<li class="fieldcontain">
						<span id="passwordExpired-label" class="property-label"><g:message code="user.passwordExpired.label" default="Password Expired" /></span>
						<span class="property-value" aria-labelledby="passwordExpired-label"><g:formatBoolean boolean="${userInstance?.passwordExpired}" /></span>
						
					</li>
					</g:if>
				
				</ol>
				<g:form url="[resource:userInstance, action:'delete']" method="DELETE">
					<fieldset class="buttons">
						<g:actionSubmit class="edit" action="edit" resource="${userInstance}" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /> 
						<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
					</fieldset>
				</g:form>
			</div>
		</div>
	</body>
</html>

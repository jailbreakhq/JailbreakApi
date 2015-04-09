<#-- @ftlvariable name="" type="org.jailbreak.service.views.HomeView" -->
<html>
    <body>
        <!-- calls getTeams() and sanitizes it -->
        <h1>Teams</h1>
        <ul>
        <#list teams as team>
        	<li>${team.names}</li>
        </#list>
        </ul>
    </body>
</html>
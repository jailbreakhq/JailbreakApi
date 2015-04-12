<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>JailbreakHQ 2015 a global race in aid of Amnesty International and Society St. Vincent de Paul</title>
    <meta name="description" content="Jailbreak 2015 is a student-run charity event that sends students racing across the world to raise €100,000 for Amnesty International and Society St. Vincent de Paul">
    <meta name="viewport" content="width=device-width, initial-scale=1">
     
    <!-- CSS-->
    <link rel="stylesheet" href="http://localhost:8080/build/styles/main.css">
    <link href="http://maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css" rel="stylesheet">
  </head>
  <body class="f-topbar-fixed">
    <div id="wrapper">
      <div class="contain-to-grid fixed">
        <nav data-topbar="" role="navigation" class="top-bar">
          <ul class="title-area">
            <li class="name">
              <h1 id="logo-title"><a href="/html/home">JailbreakHQ 2015: The Race is On!</a></h1>
            </li>
          </ul>
          
        <section class="top-bar-section">
            <!-- Right Nav Section-->
            <ul class="right">
              <li id="spin-topbar-logo" class="topbar-logo"><a href="http://www.spin1038.com/" target="_blank">Spin 1038</a></li>
              <li id="law-soc-topbar-logo" class="topbar-logo"><a href="http://tcdlawsoc.com/" target="_blank">TCD Law Soc</a></li>
              <li id="amnsety-topbar-logo" class="topbar-logo"><a href="http://www.amnesty.ie/" target="_blank">Amnesty</a></li>
              <li id="svp-topbar-logo" class="topbar-logo"><a href="http://www.svp.ie/Home.aspx" target="_blank">SVP</a></li>
            </ul>
            <!-- Left Nav Section-->
            <ul class="left">
              <li class="divider"></li>
              <li><a href="http://qa.jailbreakhq.org/teams">Teams</a></li>
              <li class="divider"></li>
              <li><a href="https://about.jailbreakhq.org/">About</a></li>
              <li class="divider"></li>
              <li><a href="https://about.jailbreakhq.org/contact">Contact</a></li>
            </ul>
          </section></nav>
      </div>
      <div id="body-container">
          <div class="row">
              <div class="small-12 columns">
                  <h2 class="page-header">Teams</h2>
                  <div id="filterbar">
                      <ul class="filters">
                          <li>
                              <label>College</label>
                              <select id="university">
                              	<#if university = "ALL">
                                    <option value="ALL" selected>All Colleges</option>
                                <#else>
                                  <option value="ALL">All Colleges</option>
                                </#if>
                                <#if university = "TCD">
                                    <option value="TCD" selected>TCD</option>
                                <#else>
                                  <option value="TCD">TCD</option>
                                </#if>
                                <#if university = "UCD">
                                    <option value="UCD" selected>UCD</option>
                                <#else>
                                  <option value="UCD">UCD</option>
                                </#if>
                                <#if university = "UCC">
                                    <option value="UCC" selected>UCC</option>
                                <#else>
                                  <option value="UCC">UCC</option>
                                </#if>
                                <#if university = "NUIG">
                                    <option value="NUIG" selected>NUIG</option>
                                <#else>
                                  <option value="NUIG">NUIG</option>
                                </#if>
                                <#if university = "NUIM">
                                    <option value="NUIM" selected>NUIM</option>
                                <#else>
                                  <option value="NUIM">NUIM</option>
                                </#if>
                                <#if university = "NCI">
                                    <option value="NCI" selected>NCI</option>
                                <#else>
                                  <option value="NCI">NCI</option>
                                </#if>
                                <#if university = "CIT">
                                    <option value="CIT" selected>CIT</option>
                                <#else>
                                  <option value="CIT">CIT</option>
                                </#if>
                                <#if university = "ITT">
                                    <option value="ITT" selected>NUIM</option>
                                <#else>
                                  <option value="ITT">ITT</option>
                                </#if>
                              </select>
                          </li>
                          <li>
                              <label>Order By</label>
                              <select id="order-by">
                              	<#if orderBy = "POSITION">
                                    <option value="POSITION" selected>Position</option>
                                <#else>
                                  <option value="POSITION">Position</option>
                                </#if>
                                <#if orderBy = "AMOUNT_RAISED">
                                    <option value="AMOUNT_RAISED" selected>Amount Raised</option>
                                <#else>
                                  <option value="AMOUNT_RAISED">Amount Raised</option>
                                </#if>
                                <#if orderBy = "TEAM_NUMBER">
                                    <option value="TEAM_NUMBER" selected>Team Number</option>
                                <#else>
                                  <option value="TEAM_NUMBER">Team Number</option>
                                </#if>
                              </select>
                          </li>
                      </ul>
                  </div>
                  <div id="teamsList">
                      <ul id="teams" class="teams-list small-block-grid-1 medium-block-grid-3 large-block-grid-4">
                          <#list teams as team>
                      		<li class="team">
                      		  <#if team.hasAvatar()>
                              	<a href="/teams/${team.slug}" class="th radius"><img src="${team.avatar}" width="150" height="150"></a>
                              <#else>
                              	<a href="/teams/${team.slug}" class="th radius"><img src="https://static.jailbreakhq.org/avatars/jb-default-avatar.jpg" width="150" height="150"></a>
                              </#if>
                              <h3><a href="/teams/${team.slug}">${team.names?html}<br><span class="label round position">${team.position}</span><span class="label round position ${team.university?lower_case}">${team.university}</span></a></h3>
                              <div><span class="amount-raised">&euro;${((team.amountRaisedOnline+team.amountRaisedOffline)/100)?int}</span></div>
                          	</li>
                          </#list>
                      </ul>
                  </div>
                  <div id="teams-pagination">
                  	<div class="pagination-centered">
					  <ul class="pagination">
					  	<#if page = 1>
					    	<li class="arrow unavailable"><a>&laquo; Previous</a></li>
					    <#else>
					    	<li class="arrow"><a href="?page=${page-1}${filtersParams}">&laquo; Previous</a></li>
					    </#if>
					    <#list pageRange as pageNumber>
					    	<#if pageNumber = page>
					    		<li class="current"><a href="?page=${pageNumber}${filtersParams}">${pageNumber}</a></li>
					    	<#else>
					    		<li><a href="?page=${pageNumber}${filtersParams}">${pageNumber}</a></li>
					    	</#if>
					    </#list>
					    <#if page = numberPages>
					    	<li class="arrow unavailable"><a>Next &raquo;</a></li>
					    <#else>
					    	<li class="arrow"><a href="?page=${page+1}${filtersParams}">Next &raquo;</a></li>
					    </#if>
					  </ul>
					</div>
                  </div>
              </div>
          </div>
      </div>
      <footer class="main-footer">
        <div class="row">
          <div class="small-12 medium-6 columns">
            <ul>
              <li><a href="https://twitter.com/jailbreakhq"><i class="fa fa-twitter">@JailbreakHQ</i></a></li>
              <li><a href="https://facebook.com/JailbreakHQ/"><i class="fa fa-facebook">JailbreakHQ</i></a></li>
              <li><a href="https://www.youtube.com/user/Jailbreak14Videos"><i class="fa fa-youtube">Channel</i></a></li>
              <li><a href="https://about.jailbreakhq.org/privacy-policy" target="_blank">Privacy Policy</a></li>
            </ul>
          </div>
          <div class="small-12 medium-6 columns">
            <ul class="footer-right">
              <li><a href="http://www.svp.ie/Home.aspx" target="_blank" class="hide-link">SVP: CHY 6892</a></li>
              <li><a href="http://www.amnesty.ie/" target="_blank" class="hide-link">Amnesty: CHY 5890</a></li>
              <li><span class="copy-statement">© JailbreakHQ</span></li>
            </ul>
          </div>
        </div>
      </footer>
    </div>
    
    <!-- JS -->
    <script src="http://localhost:8080/build/scripts/components.js"></script>
    <script src="http://localhost:8080/build/scripts/teams.js"></script>
</body>
</html>
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
            <div class="small-12 columns padding-less">
              <div id="index-map-canvas" class="map"></div>
            </div>
          </div>
            <div class="cta-box">
              <div class="row">
                <div class="small-12 columns">
                  <h3>Help us raise €100,000 for Amnesty International and St. Vincent de Paul</h3>
                </div>
              </div>
              <div id="index-stats">
                  <div class="row">
                    <div class="small-12 medium-10 columns">
                      <div class="progress-info">
                        <div class="progress radius"><span style="width: ${settings.amountRaised/10000}%" class="meter">€${settings.amountRaised/100} raised</span>€100,000 goal</div>
                      </div>
                    </div>
                    <div class="small-12 medium-2 columns"><a href="/donate" class="button round expand donate-button">Donate Now</a></div>
                  </div>
              </div>
            </div>
            <div class="row">
              <div class="small-12 medium-8 columns">
                <div id="events-stream">
                    <h3 class="section-header">Updates from Jailbreak 2015</h3>
                    <ul id="events-stream-list" class="items">
                      	<#list events as event>
                      		<#if event.type = "CHECKIN">
					        	<li class="feed-item checkin">
		                          <i class="fa fa-globe"></i>
		                          <span class="team-header">
		                            <img src="${event.checkin.team.avatar}" width="30" height="30" class="radius">
		                            <h4><a href="/teams/${event.checkin.team.slug}">${event.checkin.team.names}<span class="label round ${event.checkin.team.university?lower_case}">${event.checkin.team.university}</span><span class="label round">${event.checkin.team.position}</span></a></h4>
		                          </span>
		                          <span class="time">${(event.checkin.time*1000)?number_to_date}</span>
		                          <p> <strong>${event.checkin.location}</strong>${event.checkin.status}</p>
		                        </li>
	                        </#if>
			        	</#list>
                    </ul>
                </div>
              </div>
              <div class="small-12 medium-4 columns">
                <ul id="all-donations" class="donations-list">
                    <h3 class="section-header">Recent Donations</h3>
                    <ul id="donations" class="donations-list">
                    	<#list donations as donation>
			        		<li><span><em>&euro;${donation.amount}</em> from ${donation.name}
			        		<#if donation.hasTeam()>
			        			to <a href="/teams/${donation.team.slug}">${donation.team.names}</a>
			        		</#if>
			        		</span><span class="time">${(donation.time*1000)?number_to_date}</span></li>
				        </#list>
                   	</ul>
                    <p class="and-more">and ${donationsCount - 10} other donations</p>
                </ul>
              </div>
            </div>
            <div class="row">
			<div class="small-12 columns">
			    <h3 class="section-header">Featured Jailbreak Videos</h3>
			    <p class="section-header-desc">Here are some of our favourite videos</p>
			    <div class="video-slick">
			      <div><a href="https://www.youtube.com/watch?v=MW5DhQPTqMY" target="_blank" class="th radius video"><img src="//static.jailbreakhq.org/video/promo-video.png" height="250"/>
			          <h5>Announcing Jailbreak 2015</h5></a></div>
			      <div><a href="https://www.youtube.com/watch?v=zQxTo9NQ5zc" target="_blank" class="th radius video"><img src="//static.jailbreakhq.org/video/about-jailbreak-video.png" height="250"/>
			          <h5>Jailbreak 2015 in 90 Seconds</h5></a></div>
			      <div><a href="https://www.youtube.com/watch?v=_W_4x8q0fiE" target="_blank" class="th radius video"><img src="//static.jailbreakhq.org/video/team-75-video.png" height="250"/>
			          <h5>Top and Bottom</h5></a></div>
			      <div><a href="https://www.youtube.com/watch?v=GKLoZgbfKtQ" target="_blank" class="th radius video"><img src="//static.jailbreakhq.org/video/team-73-video.png" height="250"/>
			          <h5>Doctors Without Borders</h5></a></div>
			      <div><a href="https://www.youtube.com/watch?v=WMhZiSfYB54" target="_blank" class="th radius video"><img src="//static.jailbreakhq.org/video/team-72-video.png" height="250"/>
			          <h5>Evan &amp; Luke</h5></a></div>
			      <div><a href="https://www.youtube.com/watch?v=zd7I-mgBO9M" target="_blank" class="th radius video"><img src="//static.jailbreakhq.org/video/team-61-video.png" height="250"/>
			          <h5>Judy &amp; Rachel</h5></a></div>
			      <div><a href="https://www.youtube.com/watch?v=SVjUZCrvUCU" target="_blank" class="th radius video"><img src="//static.jailbreakhq.org/video/team-54-video.png" height="250"/>
			          <h5>The Adventurers</h5></a></div>
			      <div><a href="https://www.youtube.com/watch?v=UPbOPk6V7ms" target="_blank" class="th radius video"><img src="//static.jailbreakhq.org/video/team-50-video.png" height="250"/>
			          <h5>JAM</h5></a></div>
			      <div><a href="https://www.youtube.com/watch?v=1BLl6dhwrYs" target="_blank" class="th radius video"><img src="//static.jailbreakhq.org/video/team-3-video.png" height="250"/>
			          <h5>Imogen &amp; Sadhbh</h5></a></div>
			      <div><a href="https://www.youtube.com/watch?v=sm0tZwEIVVQ" target="_blank" class="th radius video"><img src="//static.jailbreakhq.org/video/how-to-jailbreak.png" height="250"/>
			          <h5>How to Jailbreak </h5></a></div>
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
    <script src="http://localhost:8080/build/scripts/slick.js"></script>
    <script src="http://localhost:8080/build/scripts/index.js"></script>
    <script src="http://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false"></script>
    <script>
		function initialize() {
			var mapOptions = {
				zoom: 6,
		        center: new google.maps.LatLng(53.349, -6.260),
		        mapTypeId: google.maps.MapTypeId.ROADMAP,
		        streetViewControl: false,
		        mapTypeControl: false,
		        panControl: false,
		        zoomControl: true
			};
			var map = new google.maps.Map(document.getElementById('index-map-canvas'), mapOptions);
			var markerBounds = new google.maps.LatLngBounds;
			var infowindow = new google.maps.InfoWindow({
			  content: 'Loading...'
			});
			
			var startMarker = new google.maps.Marker({
			  position: new google.maps.LatLng(${settings.startLocationLat}, ${settings.startLocationLon}),
			  map: map,
			  icon: {
			    path: google.maps.SymbolPath.CIRCLE,
			    scale: 6,
			    strokeColor: '#b21c26'
			  },
			  title: 'Start Point',
			  html: '<div class="info-window"><h3>Collins Barracks Dublin</h3><p>The start point of Jailbreak 2015 race</p></div>'
			});
			
			google.maps.event.addListener(startMarker, 'click', function(startMarker) {
			    infowindow.setContent(startMarker.html);
			    return infowindow.open(map, startMarker);
			});
			
			markerBounds.extend(startMarker.position);
			
			var endMarker = new google.maps.Marker({
			  position: new google.maps.LatLng(${settings.finalLocationLat}, ${settings.finalLocationLon}),
			  map: map,
			  icon: {
			    path: google.maps.SymbolPath.CIRCLE,
			    scale: 6,
			    strokeColor: '#b21c26'
			  },
			  title: 'Location X',
			  html: '<div class="info-window"><h3>Location X</h3><p>Bled Castle overlooking Lake Bled, Slovenia!</p><br /><img src="https://static.jailbreakhq.org/bled-castle.jpg" /></div>'
			});
			
			google.maps.event.addListener(endMarker, 'click', function(endMarker) {
			    infowindow.setContent(endMarker.html);
			    return infowindow.open(map, endMarker);
			});
			
			markerBounds.extend(endMarker.position);
			
			map.fitBounds(markerBounds);
			
			// teams
			var marker;
			var markers = [];
			<#list teams as team>
				marker = new google.maps.Marker({
				  position: new google.maps.LatLng(${team.lastCheckin.lat}, ${team.lastCheckin.lon}),
				  map: map,
				  title: '${team.teamNumber} - ${team.names}',
				  html: '',
				  animation: google.maps.Animation.DROP
				}); 
				
				markers.push(marker);
			</#list>
			
			for(var i = 0; i < markers.length; i++) {
				var marker = markers[i];
				google.maps.event.addListener(marker, 'click', function(marker) {
				    infowindow.setContent(this.html);
				    return infowindow.open(map, this);
				});
				
				markerBounds.extend(marker.position);
			}
      
      		map.fitBounds(markerBounds);
		}
		google.maps.event.addDomListener(window, 'load', initialize);
    </script>
</body>
</html>
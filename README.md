# Misc
Miscellaneous Java servlets
# PingImageServlet
Returns a green image if IP address is PING'able and return red when it is not PING'able. Use it to add visual indications of device status on web pages. HTML example: <code>&lt;img src=&quot;http://pi.pla1.net?ip=8.8.8.8&quot;&gt; 8.8.8.8&lt;br&gt;</code>

<img src="http://pi.pla1.net?ip=8.8.8.8"> 8.8.8.8<br>
<img src="http://pi.pla1.net?ip=8.8.4.4"> 8.8.4.4<br>
<img src="http://pi.pla1.net?ip=127.0.0.1"> 127.0.0.1<br>
<img src="http://pi.pla1.net?ip=169.254.254.254"> 169.254.254.254<br>
<img src="http://pi.pla1.net?ip=169.254.254.253"> 169.254.254.253<br>
<img src="http://pi.pla1.net?ip=208.67.222.222"> 208.67.222.222<br>
<img src="http://pi.pla1.net?ip=208.67.220.220"> 208.67.220.220<br>

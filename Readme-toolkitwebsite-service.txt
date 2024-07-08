# In /etc/systemd/system create a file named oapen-memo-client-website.service with the following content:
# https://www.baeldung.com/linux/run-java-application-as-service

[Unit]
Description=OAPEN MEMO Client Website
After=syslog.target network.target

[Service]
SuccessExitStatus=143
User=oapen
Group=oapen

Type=simple

ExecStart=java -Xmx512m -jar /home/oapen/oapenmemo/clientweb.jar
ExecStop=/bin/kill -15 $MAINPID

Restart=always
RestartSec=5s

[Install] 
WantedBy=multi-user.target

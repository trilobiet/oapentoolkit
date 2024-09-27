
Have a directory 'data' alongside the docker-compose.yml file.
This directory must be r/w accessible to user 1032 (nogroup).

    chown 1032:1032 ./data/

First run:

    docker-compose up -d --build

About 10G of language files will be downloaded to directory 'data' which may take around 30 minutes.
These files will remain on your server even when you recreate the docker container.

Recommended specs: >= 4G memory, 16+ cores   

Currently running on 8G with 4 cores (using 85%, see docker-compose.yml), which seems to be ok, 
combined with a Java caching mechanism.
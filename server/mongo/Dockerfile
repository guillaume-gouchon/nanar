FROM mongo

# File Author / Maintainer
MAINTAINER Guillaume Gouchon

RUN mkdir -p /var/mongodb/nanar

# Expose port 27017 from the container to the host
EXPOSE 27018

# Set usr/bin/mongod as the dockerized entry-point application
ENTRYPOINT ["/usr/bin/mongod"]

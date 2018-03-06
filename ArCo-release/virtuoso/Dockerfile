FROM ubuntu:16.04

# Set Virtuoso commit SHA to Virtuoso 7.2.4 release (25/04/2016)
ENV VIRTUOSO_COMMIT 96055f6a70a92c3098a7e786592f4d8ba8aae214

# Build virtuoso from source and clean up afterwards
RUN apt-get update \
        && apt-get install -y build-essential autotools-dev autoconf automake unzip wget net-tools libtool flex bison gperf gawk m4 libssl-dev libreadline-dev openssl crudini \
        && wget https://github.com/openlink/virtuoso-opensource/archive/${VIRTUOSO_COMMIT}.zip \
        && unzip ${VIRTUOSO_COMMIT}.zip \
        && rm ${VIRTUOSO_COMMIT}.zip \
        && cd virtuoso-opensource-${VIRTUOSO_COMMIT} \
        && ./autogen.sh \
        && export CFLAGS="-O2 -m64" && ./configure --disable-bpel-vad --enable-conductor-vad --enable-fct-vad --disable-dbpedia-vad --disable-demo-vad --disable-isparql-vad --disable-ods-vad --disable-sparqldemo-vad --disable-syncml-vad --disable-tutorial-vad --with-readline --program-transform-name="s/isql/isql-v/" \
        && make && make install \
        && ln -s /usr/local/virtuoso-opensource/var/lib/virtuoso/ /var/lib/virtuoso \
        && ln -s /var/lib/virtuoso/db /data \
        && cd .. \
        && rm -r /virtuoso-opensource-${VIRTUOSO_COMMIT} 

# Add Virtuoso bin to the PATH
ENV PATH /usr/local/virtuoso-opensource/bin/:$PATH

# Add Virtuoso config
COPY virtuoso.ini /virtuoso.ini

# Add dump_nquads_procedure
COPY dump_nquads_procedure.sql /dump_nquads_procedure.sql

# Add Virtuoso log cleaning script
COPY clean-logs.sh /clean-logs.sh

# Add startup script
COPY virtuoso.sh /virtuoso.sh

# Add data
#COPY rdf/arco_dataset.nt.tar.gz /data/dumps/arco_dataset.nt.tar.gz 
#COPY rdf/ontologies/* /usr/local/virtuoso-opensource/share/virtuoso/vad/ontologies/
#COPY rdf/dbunico/dataset-luoghi.ttl.tar.gz /usr/local/virtuoso-opensource/share/virtuoso/vad/dbunico/
ADD http://stlab.istc.cnr.it/arco/docker_data/ontologies.tar.gz /usr/local/virtuoso-opensource/share/virtuoso/vad/ontologies/
ADD http://stlab.istc.cnr.it/arco/docker_data/dataset-luoghi.ttl.tar.gz /usr/local/virtuoso-opensource/share/virtuoso/vad/dbunico/
ADD http://stlab.istc.cnr.it/arco/docker_data/arco_dataset.nt.tar.gz /usr/local/virtuoso-opensource/share/virtuoso/vad/arco/
RUN cd /usr/local/virtuoso-opensource/share/virtuoso/vad/ontologies/ \
		&& tar -xvzf ontologies.tar.gz \
		&& rm ontologies.tar.gz


VOLUME /data
WORKDIR /data
EXPOSE 8890
EXPOSE 1111

CMD ["/bin/bash", "/virtuoso.sh"]
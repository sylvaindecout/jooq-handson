FROM postgres:15.3

RUN mkdir -p /tools
COPY health-check.sh /tools/

HEALTHCHECK --interval=1s --retries=10 CMD ["/tools/health-check.sh"]

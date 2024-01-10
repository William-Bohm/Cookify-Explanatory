# ELK Log Aggregate Directory

This directory is dedicated to managing our logging and monitoring services using the ELK (Elasticsearch, Logstash, Kibana) stack along with Filebeat and optionally Prometheus. Our ELK setup collects, aggregates, indexes, and visualizes log data from our entire application cluster.

## Overview of ELK and Filebeat

The ELK Stack is an acronym for a combination of three open-source projects: Elasticsearch, Logstash, and Kibana. Elasticsearch is a search and analytics engine. Logstash is a server-side data processing pipeline that ingests data from multiple sources simultaneously, transforms it, and then sends it to a "stash" like Elasticsearch. Kibana lets users visualize data with charts and graphs in Elasticsearch.

Filebeat is a log data shipper for local files. Installed as an agent on the server, Filebeat monitors the log files or locations that you specify, collects log events, and forwards them either to Elasticsearch or Logstash for indexing.

## Current Implementation in Our Cluster

Each pod within our cluster is fitted with a Filebeat sidecar. The Filebeat sidecar is responsible for forwarding the logs of the container within its pod to our ELK stack. The ELK stack then takes all the logs, organizes them, and helps us build meaningful insights from them, thanks to the power of Kibana and the optional addition of Prometheus for metrics.

We have currently set our system to store logs for one week. This is mainly a cost-saving measure. In an enterprise scenario, it's advisable to retain logs for a more extended period or archive them for historical insights or security reasons. Archiving could involve taking regular snapshots of logs and storing them in a service like AWS Glacier.

## Why ELK and Filebeat?

The ELK stack, combined with Filebeat, provides a robust, scalable, and flexible solution for our logging needs. Elasticsearch's powerful search capabilities help us quickly find specific log data. Logstash allows us to collect data from different sources and transform it to suit our needs. Kibana enables us to visualize our data and understand trends, patterns, and issues.

Filebeat, being lightweight and efficient, is an excellent choice for collecting and forwarding log data. Its compatibility with the ELK stack makes it even more beneficial for our setup.

The ability to integrate Prometheus adds the benefit of powerful metrics collection and alerting. This comprehensive logging and monitoring solution provides valuable insights into our application's performance and health, assisting us in diagnosing and addressing issues promptly.

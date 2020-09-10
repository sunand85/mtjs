package com.newminds.mtqs.broker.core;

import com.newminds.mtqs.common.consumer.BrokerInfo;
import com.newminds.mtqs.common.job.Job;
import com.newminds.mtqs.common.job.SimpleJob;
import com.newminds.mtqs.common.job.Stats;
import com.newminds.mtqs.common.job.Topic;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Sunand on 02/09/20
 **/
public class Broker {
  private static final Broker BROKER = new Broker();
  public static void main(String[] args) {

    //Load Jobs From DB
    //Load metadata from DB
    //ReBalance when more Consumers are added
    //Start the worker with certain strategy decided upfront on how to distribute jobs
    //Pub Sub vs Point to Point
    //Offset
    //Retry Capabilities
    //Does this make a mini Kafka
    //Consumer Registration
    //Heartbeat
    //Retry Policy

    //HA Setup of broker
    //Metadata of partitions which consumer is handling which tenants job etc.

    //Throttling
    //Priority

    //Socket based consumers and have retry mechanisms around consumer connection
    //Rest Api based consumers with discovery

    bootstrap();
  }

  private Broker() {
  }

  public static Broker getInstance() {
    if(BROKER == null) {
      return new Broker();
    } else {
      return BROKER;
    }
  }

  public static void bootstrap() {
    //Broker Discovery
    //Should the consumer itself act as a broker and manage metadata???

    //Since its multi-tenant we should have stats around how many tenant jobs are coming our way
    //Jobs will not have a permanent store where as job history will have
    //There will be a global jobs table with tenant id discriminated
    //There will be a separate global tenant list by topic or message type
    //For Each topic will have to maintain Tenant Partition List to distribute job across consumers
    //Add Priority per job for a topic within a tenant (future use case)

    //By Tenant By Partition The JOB will be ordered. Guarantee

    //Ack/Nack concepts
    //Lets code everything in a single file
  }

  private final List<Topic> topics = Lists.newArrayList();
  private final List<Job> jobs = Lists.newArrayList();
  private final Map<String, List<Job>> jobsByTenantId = Maps.newConcurrentMap();
  private final Map<String, List<Job>> jobsByTopic = Maps.newConcurrentMap();
  private final Map<String, List<Job>> jobsByTopicByTenantId = Maps.newConcurrentMap();
  private final Map<String, Set<String>> consumerTopicPartitionMap = Maps.newConcurrentMap();
  private final Map<String, Set<String>> consumerTopicByTenantIdPartitionMap = Maps.newConcurrentMap();

  private final List<String> consumers = Lists.newArrayList(); //includes total since few could have become offline
  private final List<BrokerInfo> activeConsumers = Lists.newArrayList();
  private final Map<String, BrokerInfo> activeConsumerMap = Maps.newConcurrentMap();
  private final Map<String, Stats> consumerStats = Maps.newConcurrentMap();

  private final Map<String, List<Job>> jobsByConsumer = Maps.newConcurrentMap();

  public void addConsumer(BrokerInfo consumer) {
    activeConsumers.add(consumer);
//    activeConsumerMap.put(consumer.getId(), consumer);

    //Trigger a process to see if we can assign jobs to this consumer.
    assignJobsToThisConsumer();
  }

  private void assignJobsToThisConsumer() {

  }

  public void removeConsumer(String consumerId) {

  }

  public void createTopic(Topic topic) {
    topics.add(topic);
  }

  //TBD
  public void fetchJobs(String consumerId) {
//    consumerTopicByTenantIdPartitionMap.get(;
  }

  public void fetchJobsByTopic(String topic, String consumerId) {
    consumerTopicByTenantIdPartitionMap.get(topic);
  }

  public void deleteTopic(Topic topic) {
    topics.remove(topic);

    List<Job> jobList = jobsByTopic.get(topic.getName());
    jobList.forEach(job -> jobs.remove(job));

    jobsByTenantId.values().forEach(jlist -> jlist.forEach(job -> {
      if(jobList.contains(job))
      jlist.remove(job);
    }));

    consumerTopicPartitionMap.remove(topic.getName());
  }

  public void deleteTopicByTenantId(Topic topic, String tenantId) {
    //topic.getTenantId() should ideally come from RCP or from the source request and should not be stored inside Topic class
    String key = topic.getName() + "_" + tenantId;
    List<Job> removeJobsIfEligible = jobsByTenantId.get(tenantId);
    List<Job> jobList = jobsByTopicByTenantId.get(key);

    jobList.forEach(job -> {
      this.jobs.remove(job);
      removeJobsIfEligible.remove(job);
    });

    jobsByTopicByTenantId.remove(key);
    consumerTopicByTenantIdPartitionMap.remove(key);
  }

  public void createJob(SimpleJob job) {
    jobs.add(job);

    //By Tenant ID
    String tenantId = job.getHeader().getTenantId();
    List<Job> byTenantId = jobsByTenantId.getOrDefault(tenantId, Lists.newArrayList());
    byTenantId.add(job);
    jobsByTenantId.put(tenantId, byTenantId);


    //Should it be by topic by tenant - consumer list or as said below
    //By Topic Consumer List
    String topic = job.getHeader().getTopic();
    //The below is for topics where Partition Key is Tenant Id
    String key = topic + "_" + tenantId;
    List<Job> byTopic = jobsByTopic.getOrDefault(topic, Lists.newArrayList());
    byTopic.add(job);
    jobsByTopic.put(topic, byTopic);

    List<Job> byTopicByTenant = jobsByTopicByTenantId.getOrDefault(key, Lists.newArrayList());
    byTopicByTenant.add(job);
    jobsByTopicByTenantId.put(key, byTopicByTenant);

    //Random Selection on consumers
    //Or number of jobs handled by current consumer
    //if the jobs needs to be ordered per tenant per topic then the jobs should go to the same consumer
    //The above should be parameters for a topic

    Set<String> consumersByTopic = consumerTopicPartitionMap.getOrDefault(topic, Sets.newConcurrentHashSet());
    //The below is for topics where messages are not ordered so by tenant partition is not required just basic QOS needs to be honoured here
    if (consumersByTopic.isEmpty() || consumersByTopic.size() < activeConsumers.size()) {
      String consumerByQOS = pickConsumerByQOS(topic, consumersByTopic);
      //Now assign consumer
      //ToDo: Handle NULL case
      consumersByTopic.add(consumerByQOS);
      consumerTopicPartitionMap.put(topic, consumersByTopic);
    }


    //The below is for topics where Partition Key is Tenant Id
    @SuppressWarnings("DuplicatedCode")
    Set<String> consumersByTopicByTenant = consumerTopicByTenantIdPartitionMap.getOrDefault(key, Sets.newConcurrentHashSet());
    if(consumersByTopicByTenant.isEmpty() || consumersByTopicByTenant.size() < activeConsumers.size()) {
      String consumerByQOS = pickConsumerByQOS(key, consumersByTopicByTenant);
      //Now assign consumer
      //ToDo: Handle NULL case
      consumersByTopicByTenant.add(consumerByQOS);
      consumerTopicByTenantIdPartitionMap.put(key, consumersByTopicByTenant);
    }
  }

  //take already assigned Consumers as input so that we can decide if the current consumer is enough or new consumer has to be chosen.
  //Also if parallelism is enabled then choose another probable consumer.
  // In case of the current consumers cannot handle the traffic divert to another potential consumer of course with some way to start consuming the jobs only after the current ones are finished.
  //It could very well be that there are no consumers online in that case we will return null.
  private String pickConsumerByQOS(String topic, Set<String> consumersByTopic) {

    return null;
  }
}

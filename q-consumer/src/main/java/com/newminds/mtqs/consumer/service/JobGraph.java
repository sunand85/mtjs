package com.newminds.mtqs.consumer.service;

/**
 * Created by Sunand on 12/09/20
 **/
public class JobGraph {

  public static void main(String[] args) {
    //This code should accept the job and create an in memory graph of jobs that it needs to execute
    //Traverse the jobs and execute them if possible in parallel.
    //Have threshold on how many jobs this machine can process.
    //Read the statistics of the number of jobs handled by this consumer and execution time.
    //Using which adjust the backpressure or threshold accordingly. Huge USP !!!

    //Refresh the graph with newly accepted jobs by this consumer

    //How should the graph data look like?
    //topic1 by tenant jobs or topic and jobs below it

    //Do we need to handle priority jobs in the same consumer? In that case should the graph needs to be re-ordered?
    //Or handle that in a separate consumer

    //Number of root nodes could be based on the number of threads available in the thread pool

    //Abort a job, no pause functionality

    //Greedy

    //Work Stealing

    //Graceful Shutdown


  }
}

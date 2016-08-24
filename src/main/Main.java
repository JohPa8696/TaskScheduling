package main;
import node.Node;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import output_processor.OutputProcessor;
import schedulers.IDAStar;
import schedulers.Scheduler;
import schedulers.SimpleScheduler;
import utils.InvalidArgumentException;
import input_processor.InputProcessor;
import dag.Dag;

/**
 * This class implements an application that finds a schedules with the shortest schedule length
 *
 */
public class Main {
	

	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, InvalidArgumentException{
		
		long StartTime = System.currentTimeMillis();
		
		ArrayList<Node> list= new ArrayList<Node> ();
		ArrayList<Boolean> available; 
		int numProc; 
		
		// Process Input.
		InputProcessor ip=new InputProcessor(args);
		ip.processInput();
		list=ip.getGraph();
		available=ip.getNextAvailableNodes();
		numProc = ip.getNumberOfProcessors();

		// Creates Schedule
//		Scheduler s = new SimpleScheduler(list);
		Scheduler s = new IDAStar(list, available, numProc, 1);
		
		// visuals are displayed if set to true
		if(ip.getVisualisation()){
			Dag dag = new Dag(list,numProc);
			dag.createDag();
			s.setVisual(dag);
		}
		
		// start schedule
		s.schedule();

		// Create output file
		if(ip.getOutputFileName() != null){
			OutputProcessor op = new OutputProcessor(ip.getFileName(), s.getSchedule(), ip.getOutputFileName());
			op.processOutput();
		}else{
			OutputProcessor op = new OutputProcessor(ip.getFileName(), s.getSchedule());
			op.processOutput();
		}
		
		long EndTime = System.currentTimeMillis();
		System.out.print(EndTime - StartTime);
		
		
	}
}

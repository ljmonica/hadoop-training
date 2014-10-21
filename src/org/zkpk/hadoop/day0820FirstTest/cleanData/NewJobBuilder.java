package org.zkpk.hadoop.day0820FirstTest.cleanData;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;

public class NewJobBuilder {
	public static Job parseInputAndOutput(Tool tool,Configuration conf,String [] args) throws IOException{
		if(args.length!=2){
			printUsage(tool,"<input><output>");
			return null;
		}
		Job job=new Job();
		FileInputFormat.setInputPaths(job,new Path(args[0]));
		FileOutputFormat.setOutputPath(job,new Path(args[1]));
		return job;
		}
		public static void printUsage(Tool tool,String extraArgsUsage){
			System.err.printf("Usage: %s [genericOptions] %s\n\n",tool.getClass().getSimpleName(),extraArgsUsage);
			GenericOptionsParser.printGenericCommandUsage(System.err);
		}
}

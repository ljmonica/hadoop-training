package org.zkpk.hadoop.day0813.ex;

import java.io.IOException;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile.CompressionType;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class SortDataPreprocessor extends Configured implements Tool {
	static class CleanerMapper extends Mapper<LongWritable,Text,IntWritable,Text>{
		private NcdcRecordParser parser=new NcdcRecordParser();

		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			parser.parse(value);
			if(parser.isValidTemperature()){
				context.write(new IntWritable(parser.getAirTemperature()), value);
			}
		}
		
	}
	@Override
	public int run(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Job job=NewJobBuilder.parseInputAndOutput(this,getConf(), args);
		if(job==null){
			return -1;
		}
		job.setJarByClass(SortDataPreprocessor.class);
		job.setMapperClass(CleanerMapper.class);
		job.setNumReduceTasks(0);
		job.setOutputKeyClass(IntWritable.class);
		job.setOutputValueClass(Text.class);
		job.setOutputFormatClass(SequenceFileOutputFormat.class);
		SequenceFileOutputFormat.setCompressOutput(job,true);
		SequenceFileOutputFormat.setOutputCompressorClass(job, GzipCodec.class);
		SequenceFileOutputFormat.setOutputCompressionType(job,CompressionType.BLOCK);
		return job.waitForCompletion(true)?0:1;
	}

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		int exitCode=ToolRunner.run(new SortDataPreprocessor(), args);
		System.exit(exitCode);
	}

}

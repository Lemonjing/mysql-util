package com.ryan.boot;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * this class make all of small files(e.g. imgs) -> one sequence file
 * 
 * input:one single kv file(included more file paths) 
 * output:one single sequence file
 * 
 * @author Ryan Tao
 * @github lemonjing
 */
public class ToHAFile extends Configured implements Tool {

	private static Logger logger = LoggerFactory.getLogger(ToHAFile.class);

	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.err.println("Usage: ToSequenceFile <in path for kv file> <out path for sequence file>");
			System.exit(2);
		}

		Configuration conf = new Configuration();
		conf.set("fs.default.name", "hdfs://Master.Hadoop:9000");
		// make sure client side replication equals 1
		// if not, even if the hdfs-site.xml replication equals 1,
		// the replication of this file in hdfs equals 3 either
		// enhancement:because eclipse-plugin has cache
		conf.set("dfs.replication", "1");
		int res = ToolRunner.run(conf, new ToHAFile(), args);

		System.exit(res);
	}

	@Override
	public int run(String[] args) throws Exception {
		long time1 = System.currentTimeMillis();
		
		Configuration conf = getConf();

		Job job = new Job(conf, "Job_CreateHAFileMapper");
		job.setJarByClass(ToHAFile.class);
		job.setMapperClass(ToHAFileMapper.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(BytesWritable.class);
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(SequenceFileOutputFormat.class);
		
		job.waitForCompletion(true);
		
		long time2 = System.currentTimeMillis();
		logger.debug("hafile consume time: " + (time2-time1)*1.0/1000 + "s");
		
		return job.isSuccessful() ? 0 : 1;
	}

	enum Counter {
		LINESKIP,
	}

	private static class ToHAFileMapper extends
			Mapper<Object, Text, Text, BytesWritable> {

		@Override
		protected void map(Object key, Text value, Context context)
				throws IOException, InterruptedException {

			logger.info("ToHAFileMapper - map method called:");

			String uri = value.toString();
			Configuration conf = new Configuration();
			final FileSystem fs = FileSystem.get(URI.create(uri), conf);
			FSDataInputStream fsin = null;
			try {
				fsin = fs.open(new Path(uri));
				ByteArrayOutputStream bout = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024 * 1024];

				while (fsin.read(buffer, 0, buffer.length) >= 0) {
					bout.write(buffer);
				}
				context.write(value, new BytesWritable(bout.toByteArray()));
				bout.close();
			} catch (Exception e) {
				context.getCounter(Counter.LINESKIP).increment(1);
			} finally {
				IOUtils.closeStream(fsin);
			}
		}
	}
}

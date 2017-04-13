package com.ryan.boot;

import com.ryan.core.FSPFileInputFormat;
import com.ryan.pojo.ChunkInfo;
import com.ryan.security.Digest;
import com.ryan.security.Digests;
import com.ryan.util.Constant;
import com.ryan.util.HBaseUtil;
import com.ryan.util.HDFSFileUtil;

import com.ryan.util.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class FSPChunkLevelDedup {
    private static final Logger log = LoggerFactory.getLogger(FSPChunkLevelDedup.class);
    private static final String HDFS_PATH = "hdfs://Master.Hadoop:9000/usr/local/hadoop";

    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
        long start = System.currentTimeMillis();

        Configuration conf = new Configuration();

        //TODO config 60MB fraction
        // calculate input split
        // Math.max(minSize, Math.min(maxSize, blockSize));
        conf.set("mapred.min.split.size", "62914560");//minSize=60MB
        conf.set("mapred.max.split.size", "62914560");//maxSize=60MB
        conf.set("dfs.permissions", "false");

        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
        if (otherArgs.length != 2) {
            System.err.println("Usage: FSPChunkLevelDedup <in> <out>");
            System.exit(2);
        }

        log.debug("=========job start=========");

        Job job = new Job(conf, "Job_FSPChunkLevelDedup");
        job.setJarByClass(FSPChunkLevelDedup.class);
        job.setMapperClass(FSPMapper.class);
        job.setReducerClass(FSPReducer.class);

        FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
        FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(ChunkInfo.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputKeyClass(NullWritable.class);

        job.setInputFormatClass(FSPFileInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        log.debug("=========job end=========");
        job.waitForCompletion(true);

        long end = System.currentTimeMillis();

        log.debug("consume time:{} ", end - start);
        System.exit(job.isSuccessful() ? 0 : 1);
    }

    enum Counter {
        LINESKIP,
    }

    private static class FSPMapper extends Mapper<IntWritable, ChunkInfo, Text, ChunkInfo> {
        @Override
        protected void map(IntWritable key, ChunkInfo value, Context context) throws IOException, InterruptedException {
            log.debug("================map start============");

            String hash = null;
//            hash = StringUtils.getKeccak(value.getBuffer());
            try {
                hash = StringUtils.getSHA224(value.getBuffer());
            } catch (Exception e) {
                //do nothing
            }
            Text reduceKey = new Text(hash);
            value.setHash(hash);

            StringBuilder preValue = new StringBuilder();
            // hbase
            Result result = HBaseUtil.getResultByQualifier(Constant.DEFAULT_HBASE_TABLE_NAME,
                    value.getFileName(), "fileFamily", "chunksQualifier");
            if (result != null) {
                preValue.append(Bytes.toString(result.list().get(0).getValue()));
            } else {
                preValue.append("");
            }
            String curValue = preValue.append(value.getId()).append(",").toString();
//            HBaseUtil.put(Constant.DEFAULT_HBASE_TABLE_NAME, value.getFileName()
//                    , "fileFamily", "chunksQualifier", curValue);

            log.info("===block file has been written to hbase successfully======");

            context.write(reduceKey, new ChunkInfo(value.getId(), value.getSize()
                    , value.getFileNum(), value.getChunkNum(), value.getBuffer()
                    , value.getHash(), value.getFileName(), value.getOffset(), value.getBlockAddress()));

            log.debug("=============map end============");
        }
    }

    private static class FSPReducer extends Reducer<Text, ChunkInfo, Text, NullWritable> {
        private int id = 1;

        @Override
        protected void reduce(Text key, Iterable<ChunkInfo> values, Context context) throws IOException, InterruptedException {
            int fileNumCounter = 0;
            int chunkNumCounter = 0;
            int offset = -1;
            byte[] buffer = new byte[Constant.DEFAULT_CHUNK_SIZE];
            boolean flag = true;
            String fileName = Constant.DEFAULT_FILE_NAME;
            String blockAddress = Constant.DEFAULT_BLOCK_ADDRESS;
            // duplicate chunks
            for (ChunkInfo chunk : values) {
                chunkNumCounter++;
                if (flag) {
                    fileName = chunk.getFileName();
                    offset = chunk.getOffset();
                    buffer = chunk.getBuffer();
                    try {
                        blockAddress = HDFS_PATH + "/chunk/" + key.toString() + ".blob";
                        Path chunkPath = new Path(blockAddress);
                        HDFSFileUtil.createHDFSFile(chunkPath, buffer);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    fileNumCounter++;
                }
                if (!chunk.getFileName().equals(fileName)) {
                    fileNumCounter++;
                }
                log.debug("ChunkInfo:{}", chunk.toString());

                flag = false;
            }

            ChunkInfo chunkInfo = new ChunkInfo();
            chunkInfo.setId(id);
            chunkInfo.setSize(Constant.DEFAULT_CHUNK_SIZE);
            chunkInfo.setFileNum(fileNumCounter);
            chunkInfo.setChunkNum(chunkNumCounter);
            chunkInfo.setBuffer(buffer);
            chunkInfo.setHash(key.toString());
            chunkInfo.setFileName(fileName);
            chunkInfo.setOffset(offset);
            chunkInfo.setBlockAddress(blockAddress);
            id++;
            context.write(new Text(chunkInfo.toString()), NullWritable.get());
        }
    }
}

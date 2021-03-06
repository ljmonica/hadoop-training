package org.zkpk.hadoop.day0923.hw01;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.util.Bytes;

public class HBaseSogou extends Thread{
	
	/**
	 * @param args
	 */
	public Configuration config;
	public HTable table;
	public HBaseAdmin admin;
	
	public HBaseSogou() {
		config = HBaseConfiguration.create();
		config.set("hbase.master", "master:60000");
		config.set("hbase.zookeeper.property.clientPort", "2181");
		config.set("hbase.zookeeper.quorum","master");
		try {
			table = new HTable(config, Bytes.toBytes("test"));
			admin = new HBaseAdmin(config);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void createTable() throws IOException{
		HTableDescriptor tableDesc = new HTableDescriptor("sogou_100w_java");
		tableDesc.addFamily(new HColumnDescriptor("s1"));
		admin.createTable(tableDesc);
	}
	public void insert() throws IOException{
		long startTime=System.currentTimeMillis();
		BufferedReader Reader=new BufferedReader(new InputStreamReader(new FileInputStream("/home/zkpk/exercise/0904/sogou.100w.uid")));
		String line;
		ArrayList<Put> list=new ArrayList();
		while((line=Reader.readLine())!= null){
			String [] arr=line.toString().split("\t",-1);
			String rowKey=arr[0];
			String time=arr[1];
			String keyword=arr[2];
			String rank=arr[3];
			String order=arr[4];
			String url=arr[5];
			Put put = put = new Put(Bytes.toBytes(rowKey));
			put.add(Bytes.toBytes("s1"), Bytes.toBytes("time"),Bytes.toBytes(time));	
			put.add(Bytes.toBytes("s1"), Bytes.toBytes("keyword"),Bytes.toBytes(keyword));
			put.add(Bytes.toBytes("s1"), Bytes.toBytes("rank"),Bytes.toBytes(rank));
			put.add(Bytes.toBytes("s1"), Bytes.toBytes("order"),Bytes.toBytes(order));
			put.add(Bytes.toBytes("s1"), Bytes.toBytes("url"),Bytes.toBytes(url));
			list.add(put);
			if(list.size()==100000){
				table.put(list);
				list.clear();
			}
		}
		long endTime=System.currentTimeMillis();
		System.out.print(startTime-endTime);
	}
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		HBaseSogou hbs=new HBaseSogou();
		//hbs.createTable();
		//hbs.insert();
	}
}

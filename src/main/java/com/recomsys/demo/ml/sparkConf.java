package com.recomsys.demo.ml;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

public class sparkConf {
    static SparkConf conf = new SparkConf().setMaster("local[2]").setAppName("Job");

    public static JavaSparkContext sc = new JavaSparkContext(conf);

}

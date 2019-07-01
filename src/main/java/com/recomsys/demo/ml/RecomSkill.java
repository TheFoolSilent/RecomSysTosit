package com.recomsys.demo.ml;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.fpm.FPGrowth;
import org.apache.spark.mllib.fpm.FPGrowthModel;
import org.spark_project.guava.base.Joiner;
import org.springframework.util.ResourceUtils;

import java.util.Arrays;

public class RecomSkill {

    public static SparkConf conf;
    public static JavaSparkContext jsc;
    private String path = "data/sample_fpgrowth.txt";
//    private String path = ResourceUtils.CLASSPATH_URL_PREFIX + "data/sample_fpgrowth.txt";

//    public RecomSkill() {
//        this.conf = new SparkConf().setAppName("RecomSkill").setMaster("local[2]");
//        this.jsc = new JavaSparkContext(this.conf);
//    }

    public static void main(String[] args) {
        conf = new SparkConf().setAppName("RecomSkill").setMaster("local[2]");
        jsc = new JavaSparkContext(conf);

        RecomSkill recomSkill = new RecomSkill();
        recomSkill.FPG_Algorithm(recomSkill.path);
    }

    public void FPG_Algorithm(String args){
        String input_file = args;
        double minSupport = 0.3;

//        int numPartition = -1;

        JavaRDD<Iterable<String>> rdd = jsc.textFile(input_file).map((x) -> Arrays.asList(x.split(" ")));
        FPGrowthModel<String> model = new FPGrowth()
                .setMinSupport(minSupport)
                .run(rdd);

        for (FPGrowth.FreqItemset<String> s: model.freqItemsets().toJavaRDD().collect()) {
            System.out.println("[" + Joiner.on(",").join(s.javaItems()) + "], " + s.freq());
        }

        jsc.stop();

    }


}

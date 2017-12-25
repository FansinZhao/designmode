package com.fansin.designmode;

import java.lang.ref.WeakReference;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created with IntelliJ IDEA.
 *
 * @author fansin
 * @version 1.0
 * @date 17-12-25 下午11:23
 */
public class FlyWeightDemo {
    /**
     * 享元模式和单例模式区别
     * 单例模式:强制保证全局只有一个对象,实现资源共享,节约资源.
     * 享元模式:可能有多个对象,但是相同的对象就只有一个,通过保证系统不存在重复对象,节约内存资源.
     *
     * 业务应用,配合工厂模式:
     * 缓存:集合中适合缓存有:WeakHashMap,LinkedHashMap,DelayQueue(未使用演示)
     * 为用的比较多银联工厂添加缓存功能功能
     * */

    public static void main(String[] args) {
        CacheUnionPayTransStatisticsFactory cacheUnionPayTransStatisticsFactory = new CacheUnionPayTransStatisticsFactory();
        cacheUnionPayTransStatisticsFactory.createTransStatistics("leader");
        cacheUnionPayTransStatisticsFactory.createTransStatistics("appearance");
        cacheUnionPayTransStatisticsFactory.createTransStatistics("clerk");
        cacheUnionPayTransStatisticsFactory.createTransStatistics("leader");
        cacheUnionPayTransStatisticsFactory.createTransStatistics("leader");
        cacheUnionPayTransStatisticsFactory.createTransStatistics("leader");
        cacheUnionPayTransStatisticsFactory.createTransStatistics("leader");
        System.out.println("cacheUnionPayTransStatisticsFactory = " + cacheUnionPayTransStatisticsFactory.size());
        WeakCacheUnionPayTransStatisticsFactory weakCacheUnionPayTransStatisticsFactory = new WeakCacheUnionPayTransStatisticsFactory();
        weakCacheUnionPayTransStatisticsFactory.createTransStatistics("leader");
        weakCacheUnionPayTransStatisticsFactory.createTransStatistics("appearance");
        weakCacheUnionPayTransStatisticsFactory.createTransStatistics("clerk");
        weakCacheUnionPayTransStatisticsFactory.createTransStatistics("leader");
        weakCacheUnionPayTransStatisticsFactory.createTransStatistics("leader");
        weakCacheUnionPayTransStatisticsFactory.createTransStatistics("leader");
        weakCacheUnionPayTransStatisticsFactory.createTransStatistics("leader");
        System.out.println("weakCacheUnionPayTransStatisticsFactory = " + weakCacheUnionPayTransStatisticsFactory.size());
    }



}

/**
 *这个不是很好的体现缓存,如果产品类型是有限的,并且产品可以有完全重复
 *
 * */
class CacheUnionPayTransStatisticsFactory extends UnionPayTransStatisticsFactory{


    /**LRU
     *
     */
    private LinkedHashMap<String,TransStatistics> cache = new LinkedHashMap(50){
        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {
            return size() > 50;
        }
    };
//    private Map<String,TransStatistics> cache = new WeakHashMap<>(50);//依靠GC调整缓存

    @Override
    public TransStatistics createTransStatistics(String type) {

        //针对信息固定的人员,做缓存
        if (leader.equalsIgnoreCase(type)){
            TransStatistics transStatistics = cache.get(type);
            if (transStatistics != null){
                return transStatistics;
            }
        }
        cache.putIfAbsent(type,super.createTransStatistics(type));
        return cache.get(type);
    }

    public int size(){
        return cache.size();
    }

}

class WeakCacheUnionPayTransStatisticsFactory extends UnionPayTransStatisticsFactory{
    /**依靠GC调整缓存*/
    private Map<String, WeakReference<TransStatistics>> cache = new WeakHashMap<>(50);

    @Override
    public TransStatistics createTransStatistics(String type) {

        //针对信息固定的人员,做缓存
        if (leader.equalsIgnoreCase(type)){
            WeakReference<TransStatistics> transStatistics = cache.get(type);
            if (transStatistics != null){
                return transStatistics.get();
            }
        }
        cache.putIfAbsent(type,new WeakReference<>(super.createTransStatistics(type)) );
        return cache.get(type).get();
    }

    public int size(){
        return cache.size();
    }

}




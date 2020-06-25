package LocalCache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Date;
import java.util.Iterator;
import LocalCache.cacheInfo;
import java.io.*;

/**
 *	 执行本地缓存相关操作的类
 *
 * @author WuYuan
 */
public class localCache {
	
	private long Timeout = 86400*1000; // 设置默认缓存过期时间为1天
	
	// 设置相关Map储存和获取缓存信息
	private ConcurrentHashMap<String, cacheInfo> mCache = new ConcurrentHashMap<String, cacheInfo>();
	
	// 默认构造器
	public localCache() {
		
	}
	
	// 设置过期时间
	public localCache(long Timeout) {
		this.Timeout = Timeout;
	}
	
	/**
	 * 	添加新的缓存信息
	 * 
	 * @param Key 相关Map中获取缓存的键
	 * @param Info 缓存信息
	 * @return 操作无异常，返回true
	 */
	public synchronized boolean setCache(String Key,String Info) {
		cacheInfo cache = new cacheInfo(Info);
		mCache.put(Key,cache);
		return true;
	}
	
	/**
	 * 	获取缓存信息并更新访问记录
	 * 
	 * @param Key 相关Map中获取缓存的键
	 * @return 返回存储缓存信息的类
	 */
	public synchronized cacheInfo getCache(String Key) {
		cacheInfo cache = mCache.get(Key);
		cache.setAccessRecord();
		return cache;
	}
	
	/**
	 *	 判断缓存是否超出存活期（TTL）
	 * 
	 * @param cache 用于获取缓存创建时间
	 * @return 超时返回true，否则返回false
	 */
	public boolean isTimeout(cacheInfo cache) {
		Date nowTime = new Date();
		Date cTime = cache.getCreateTime();
		
		// 计算时间间隔
		long span = nowTime.getTime() - cTime.getTime();
		
		if(span > Timeout) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 *	删除超出存活期的缓存，先进先出(FIFO)
	 */
	public void removeTimeout() {
		//遍历存储缓存的Map
		for(Iterator<ConcurrentHashMap.Entry<String,cacheInfo>> it = mCache.entrySet().iterator(); it.hasNext();) {
			
			ConcurrentHashMap.Entry<String,cacheInfo> item = it.next();
			
			cacheInfo cacheItem = (cacheInfo) item.getValue();
			
			if(isTimeout(cacheItem)) {
				it.remove();
			}
		}
	}
	
	/**
	 * 	返回存储缓存的Map
	 * 
	 * @return mCache
	 */
	public ConcurrentHashMap<String, cacheInfo> getMap(){
		return mCache;
	}
	
	/**
	 * 	将缓存写入磁盘，实现数据持久化
	 * 
	 * @throws IOException
	 */
	public void writeToDisk() throws IOException{
		// 存储键的流		
	    FileOutputStream k1 = new FileOutputStream(new File("Key.txt"));
	    OutputStreamWriter k2 = new OutputStreamWriter(k1);
	
	    // 存储值的流
	    ObjectOutputStream cI = new ObjectOutputStream(new FileOutputStream("Cache.dat"));
	    
	    // 写入
	    for(ConcurrentHashMap.Entry<String,cacheInfo> item : mCache.entrySet()) {
		    k2.write(item.getKey() + '\n');
		    cI.writeObject(item.getValue());
	    }
	    
	    
	    k2.close();
		k1.close();
		cI.close();		
	}
	
	public ConcurrentHashMap<String, cacheInfo> readFromDisk() throws IOException, ClassNotFoundException{
		// 读取键的流
		FileInputStream k1=new FileInputStream("Key.txt");
        InputStreamReader k2 = new InputStreamReader(k1);
        BufferedReader k3 = new BufferedReader(k2);
        
        // 读取值的流
        ObjectInputStream cI = new ObjectInputStream(new FileInputStream("Cache.dat"));
        
        // 存储获取的键值
        String key = "";
        ConcurrentHashMap<String, cacheInfo> rCache = new ConcurrentHashMap<String, cacheInfo>();
        
        //读取
        while((key=k3.readLine()) != null) {
        	rCache.put(key, (cacheInfo) cI.readObject());
        }
        
        k3.close();
        k2.close();
        k1.close();
        cI.close();
        
        return rCache;
	}
			
}

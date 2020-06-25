package LocalCache;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.io.IOException;

public class localCacheTest {

	@Test
	// 测试存储和获取内存中的缓存
	public void getCacheTest() {
		localCache t = new localCache();
		t.setCache("A", "Test-1");
		cacheInfo cache = t.getCache("A");
		assertEquals("noEquals","Test-1",cache.getInfo());
		System.out.println("setCache(), getCache() 可正常调用");
	}
	
	@Test
	// 测试判断超时的方法
	public void isTimeoutTest() {
		long timeout = 60000;
		localCache t = new localCache(timeout); //设置缓存存活期为60秒
		t.setCache("A", "Test-1"); //设置超时的缓存
		
		// 暂停程序61秒
		try{
			TimeUnit.SECONDS.sleep(61);
		}catch(InterruptedException e) {
			System.out.println(e);
		}
		
		t.setCache("B", "Test-2"); //设置未超时的缓存
		cacheInfo cache1 = t.getCache("A");
		cacheInfo cache2 = t.getCache("B");
		assertTrue("Exception1",t.isTimeout(cache1));
		assertFalse("Exception2",t.isTimeout(cache2));
		System.out.println("isTimout() 可正常调用");		
	}
	
	@Test
	// 测试删除超时缓存的方法
	public void removeTimeoutTest() {
		int num = 0; // 记录缓存数目
		
		long timeout = 60000;
		localCache t = new localCache(timeout);
		
		t.setCache("A", "Test-1");
		t.setCache("B", "Test-2");
		t.setCache("C", "Test-3");
		
		// 暂停程序61秒
		try{
			TimeUnit.SECONDS.sleep(61);
		}catch(InterruptedException e) {
			System.out.println(e);
		}
		
		t.setCache("D", "Test-4");
		t.setCache("E", "Test-5");
		
		t.removeTimeout();
		
		for(ConcurrentHashMap.Entry<String,cacheInfo> item : t.getMap().entrySet()) {
			//System.out.println("k: " + item.getKey() + " V: " + item.getValue());
			num++;
		}
		
		assertEquals(2,num);
		System.out.println("removeTimeout() 可正常调用");
	}
	
	@Test
	public void readFromDiskTest() throws IOException,ClassNotFoundException{
		localCache t = new localCache();
		t.setCache("A","Test-1");
		t.setCache("B","Test-2");
		t.setCache("C","Test-3");
		// 写入
		t.writeToDisk();
		
		//读取
		ConcurrentHashMap<String, cacheInfo> tCache = new ConcurrentHashMap<String, cacheInfo>();
		
		tCache = t.readFromDisk();
		assertEquals("Read1 Not Equals","Test-1",tCache.get("A").getInfo());
		assertEquals("Read2 Not Equals","Test-2",tCache.get("B").getInfo());
		assertEquals("Read3 Not Equals","Test-3",tCache.get("C").getInfo());
		
		System.out.println("weritToDisk(), readFromDisk可正常调用");
	}

}

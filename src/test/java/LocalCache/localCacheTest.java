package LocalCache;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.io.IOException;

public class localCacheTest {

	@Test
	// ���Դ洢�ͻ�ȡ�ڴ��еĻ���
	public void getCacheTest() {
		localCache t = new localCache();
		t.setCache("A", "Test-1");
		cacheInfo cache = t.getCache("A");
		assertEquals("noEquals","Test-1",cache.getInfo());
		System.out.println("setCache(), getCache() ����������");
	}
	
	@Test
	// �����жϳ�ʱ�ķ���
	public void isTimeoutTest() {
		long timeout = 60000;
		localCache t = new localCache(timeout); //���û�������Ϊ60��
		t.setCache("A", "Test-1"); //���ó�ʱ�Ļ���
		
		// ��ͣ����61��
		try{
			TimeUnit.SECONDS.sleep(61);
		}catch(InterruptedException e) {
			System.out.println(e);
		}
		
		t.setCache("B", "Test-2"); //����δ��ʱ�Ļ���
		cacheInfo cache1 = t.getCache("A");
		cacheInfo cache2 = t.getCache("B");
		assertTrue("Exception1",t.isTimeout(cache1));
		assertFalse("Exception2",t.isTimeout(cache2));
		System.out.println("isTimout() ����������");		
	}
	
	@Test
	// ����ɾ����ʱ����ķ���
	public void removeTimeoutTest() {
		int num = 0; // ��¼������Ŀ
		
		long timeout = 60000;
		localCache t = new localCache(timeout);
		
		t.setCache("A", "Test-1");
		t.setCache("B", "Test-2");
		t.setCache("C", "Test-3");
		
		// ��ͣ����61��
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
		System.out.println("removeTimeout() ����������");
	}
	
	@Test
	public void readFromDiskTest() throws IOException,ClassNotFoundException{
		localCache t = new localCache();
		t.setCache("A","Test-1");
		t.setCache("B","Test-2");
		t.setCache("C","Test-3");
		// д��
		t.writeToDisk();
		
		//��ȡ
		ConcurrentHashMap<String, cacheInfo> tCache = new ConcurrentHashMap<String, cacheInfo>();
		
		tCache = t.readFromDisk();
		assertEquals("Read1 Not Equals","Test-1",tCache.get("A").getInfo());
		assertEquals("Read2 Not Equals","Test-2",tCache.get("B").getInfo());
		assertEquals("Read3 Not Equals","Test-3",tCache.get("C").getInfo());
		
		System.out.println("weritToDisk(), readFromDisk����������");
	}

}

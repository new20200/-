package LocalCache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Date;
import java.util.Iterator;
import LocalCache.cacheInfo;
import java.io.*;

/**
 *	 ִ�б��ػ�����ز�������
 *
 * @author WuYuan
 */
public class localCache {
	
	private long Timeout = 86400*1000; // ����Ĭ�ϻ������ʱ��Ϊ1��
	
	// �������Map����ͻ�ȡ������Ϣ
	private ConcurrentHashMap<String, cacheInfo> mCache = new ConcurrentHashMap<String, cacheInfo>();
	
	// Ĭ�Ϲ�����
	public localCache() {
		
	}
	
	// ���ù���ʱ��
	public localCache(long Timeout) {
		this.Timeout = Timeout;
	}
	
	/**
	 * 	����µĻ�����Ϣ
	 * 
	 * @param Key ���Map�л�ȡ����ļ�
	 * @param Info ������Ϣ
	 * @return �������쳣������true
	 */
	public synchronized boolean setCache(String Key,String Info) {
		cacheInfo cache = new cacheInfo(Info);
		mCache.put(Key,cache);
		return true;
	}
	
	/**
	 * 	��ȡ������Ϣ�����·��ʼ�¼
	 * 
	 * @param Key ���Map�л�ȡ����ļ�
	 * @return ���ش洢������Ϣ����
	 */
	public synchronized cacheInfo getCache(String Key) {
		cacheInfo cache = mCache.get(Key);
		cache.setAccessRecord();
		return cache;
	}
	
	/**
	 *	 �жϻ����Ƿ񳬳�����ڣ�TTL��
	 * 
	 * @param cache ���ڻ�ȡ���洴��ʱ��
	 * @return ��ʱ����true�����򷵻�false
	 */
	public boolean isTimeout(cacheInfo cache) {
		Date nowTime = new Date();
		Date cTime = cache.getCreateTime();
		
		// ����ʱ����
		long span = nowTime.getTime() - cTime.getTime();
		
		if(span > Timeout) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 *	ɾ����������ڵĻ��棬�Ƚ��ȳ�(FIFO)
	 */
	public void removeTimeout() {
		//�����洢�����Map
		for(Iterator<ConcurrentHashMap.Entry<String,cacheInfo>> it = mCache.entrySet().iterator(); it.hasNext();) {
			
			ConcurrentHashMap.Entry<String,cacheInfo> item = it.next();
			
			cacheInfo cacheItem = (cacheInfo) item.getValue();
			
			if(isTimeout(cacheItem)) {
				it.remove();
			}
		}
	}
	
	/**
	 * 	���ش洢�����Map
	 * 
	 * @return mCache
	 */
	public ConcurrentHashMap<String, cacheInfo> getMap(){
		return mCache;
	}
	
	/**
	 * 	������д����̣�ʵ�����ݳ־û�
	 * 
	 * @throws IOException
	 */
	public void writeToDisk() throws IOException{
		// �洢������		
	    FileOutputStream k1 = new FileOutputStream(new File("Key.txt"));
	    OutputStreamWriter k2 = new OutputStreamWriter(k1);
	
	    // �洢ֵ����
	    ObjectOutputStream cI = new ObjectOutputStream(new FileOutputStream("Cache.dat"));
	    
	    // д��
	    for(ConcurrentHashMap.Entry<String,cacheInfo> item : mCache.entrySet()) {
		    k2.write(item.getKey() + '\n');
		    cI.writeObject(item.getValue());
	    }
	    
	    
	    k2.close();
		k1.close();
		cI.close();		
	}
	
	public ConcurrentHashMap<String, cacheInfo> readFromDisk() throws IOException, ClassNotFoundException{
		// ��ȡ������
		FileInputStream k1=new FileInputStream("Key.txt");
        InputStreamReader k2 = new InputStreamReader(k1);
        BufferedReader k3 = new BufferedReader(k2);
        
        // ��ȡֵ����
        ObjectInputStream cI = new ObjectInputStream(new FileInputStream("Cache.dat"));
        
        // �洢��ȡ�ļ�ֵ
        String key = "";
        ConcurrentHashMap<String, cacheInfo> rCache = new ConcurrentHashMap<String, cacheInfo>();
        
        //��ȡ
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

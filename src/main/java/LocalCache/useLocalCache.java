package LocalCache;

/**
 *	��ӡ���������Ϣ
 * 
 * @author WuYuan
 */
public class useLocalCache {
	
	public static void main(String[] args) {
		localCache use = new localCache();
		use.setCache("A","cacheInformation-1");
		
		for(int i=0; i<10; i++) {
			use.getCache("A");
		}
		
		cacheInfo cache = use.getCache("A");
		System.out.println("������Ϣ��" + cache.getInfo());
		System.out.println("����ʱ�䣺" + cache.getCreateTime());
		System.out.println("�������ʱ�䣺" + cache.getAccessTime());
		System.out.println("���ʴ�����" + cache.getCount());
	}
}

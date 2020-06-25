package LocalCache;

/**
 *	打印缓存类的信息
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
		System.out.println("缓存信息：" + cache.getInfo());
		System.out.println("创建时间：" + cache.getCreateTime());
		System.out.println("最近访问时间：" + cache.getAccessTime());
		System.out.println("访问次数：" + cache.getCount());
	}
}

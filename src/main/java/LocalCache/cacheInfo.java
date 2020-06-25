package LocalCache;

import java.util.Date;
import java.io.Serializable;

/**
 * 	储存本地缓存信息的类
 *
 * @author WuYuan
 */
public class cacheInfo implements Serializable{
	
	private String Info; //存储缓存信息
	
	private final Date createTime; //记录创建时间
	
	private Date accessTime; //记录访问时间
	
	private int count = 0; //记录调用次数
	
	public cacheInfo(String Info) {
		this.Info = Info;
		createTime = new Date();
	}
	
	// 更新访问记录
	public void setAccessRecord() {
		accessTime = new Date();
		count++;
	}
	
	// 获取缓存信息
	public String getInfo() {
		return Info;
	}
	
	// 获取缓存创建时间
	public Date getCreateTime() {
		return createTime;
	}
	
	// 获取最近一次访问记录
	public Date getAccessTime() {
		return accessTime;
	}
	
	// 获取访问次数
	public int getCount() {
		return count;
	}
}

package LocalCache;

import java.util.Date;
import java.io.Serializable;

/**
 * 	���汾�ػ�����Ϣ����
 *
 * @author WuYuan
 */
public class cacheInfo implements Serializable{
	
	private String Info; //�洢������Ϣ
	
	private final Date createTime; //��¼����ʱ��
	
	private Date accessTime; //��¼����ʱ��
	
	private int count = 0; //��¼���ô���
	
	public cacheInfo(String Info) {
		this.Info = Info;
		createTime = new Date();
	}
	
	// ���·��ʼ�¼
	public void setAccessRecord() {
		accessTime = new Date();
		count++;
	}
	
	// ��ȡ������Ϣ
	public String getInfo() {
		return Info;
	}
	
	// ��ȡ���洴��ʱ��
	public Date getCreateTime() {
		return createTime;
	}
	
	// ��ȡ���һ�η��ʼ�¼
	public Date getAccessTime() {
		return accessTime;
	}
	
	// ��ȡ���ʴ���
	public int getCount() {
		return count;
	}
}

package com.team.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.team.domain.SysUser;
import com.team.persistence.SysUserMapper;

/***
 * 处理线程的类
 * @author Administrator
 */
public class Handle {
	
	private List<SysUser> list;// 存放待处理的数据
	
	private List<SqlSession> sessions = new ArrayList<SqlSession>();
	
	private int threadQuantity = 10;// 定义处理的线程个数
	
	public int next = 0;
	
	public void initList() {
		 	
		    //1、 初始化待处理的数据
			SqlSession session = MyBatisUtil.createSession();
			SysUserMapper mapper = session.getMapper(SysUserMapper.class);
			list = mapper.selectUser();
			MyBatisUtil.closeSession(session);
			
			//2、为每个线程缓存一个session
			for (int i = 0; i < threadQuantity; i++) {
				sessions.add(MyBatisUtil.createSession());
			}
	}

	public synchronized SysUser getNext() {

		if (next >= list.size())
			return null;
		next++;
		return list.get(next - 1);
	}// 同步，防止多次处理

	public void handle() {

		next = 0;
		initList();
		
		//启动线程
		for (int i = 0; i < threadQuantity; i++) {
			new Thread(new HandleThread(this,sessions.get(i))).start();
		}
	}
}
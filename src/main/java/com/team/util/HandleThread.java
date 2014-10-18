package com.team.util;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.team.domain.SysUser;
import com.team.persistence.SysUserMapper;

/***
 * 线程处理类
 * @author Administrator
 */
public class HandleThread implements Runnable {
	
	private Handle handle;
	private Logger logger;
	private SqlSession session;

	public HandleThread(Handle h,SqlSession session) {
		this.handle = h;
		this.logger = Logger.getLogger(HandleThread.class);
		this.session = session;
	}

	public void run() {

		SysUser msg = handle.getNext();
		
		while (msg != null) {
			myHandle(msg);
			msg = handle.getNext();
		}
	}

	private boolean myHandle(SysUser msg) {
		
		//数据库中修改数据
		logger.info("更新 tbid: "+msg.getTbid());
		SysUserMapper mapper = session.getMapper(SysUserMapper.class);
		mapper.updateUser(msg);
		mapper.updateUserDetail(msg);
		//session 只提交 不要close
		session.commit();
		return true;
	}
}
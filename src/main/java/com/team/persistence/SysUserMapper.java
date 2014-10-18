package com.team.persistence;

import java.util.List;

import com.team.domain.SysUser;

public interface SysUserMapper {
	
	List<SysUser> selectUser();
	
	int updateUser(SysUser user);
	
	int updateUserDetail(SysUser user);
	
	int batchUpdate(List<SysUser> users);
}
package com.example.slackproject.mapper;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.slackproject.dto.UserDTO;

@Service
public class MemberServiceMapper {
	
	private final SqlSession sqlSession;
	
	@Autowired
    public MemberServiceMapper(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public UserDTO findUsernameById(String userId) {
		return sqlSession.selectOne("findUsernameById", userId);
	}
}

package com.mycom.sixclub.service.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Param;

import com.mycom.sixclub.service.vo.CommentsVO;
import com.mycom.sixclub.service.vo.PostsVO;

public interface PostsDAO {

	int getPostCount();

	ArrayList<PostsVO> getAllContents(@Param("startRow") int startRow, @Param("endRow") int endRow);

	PostsVO getContents(@Param("pid") int pid);

	void insertPost(PostsVO postVO);

	void updatePost(PostsVO postVO);

	void deletePost(int pid);

	ArrayList<CommentsVO> getComments(@Param("pid") int pid);

	void insertComment(CommentsVO commentVO);

	void deleteComment(int cid);

	int getCommentCount(int pid);

}

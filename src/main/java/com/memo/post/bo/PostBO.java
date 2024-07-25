package com.memo.post.bo;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.memo.common.FileManagerService;
import com.memo.post.domain.Post;
import com.memo.post.mapper.PostMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PostBO {
	// private Logger log = LoggerFactory.getLogger(PostBO.class);
	// private Logger log = LoggerFactory.getLogger(this.getClass());
	
	
	@Autowired
	private PostMapper postMapper;
	
	@Autowired
	private FileManagerService fileManagerService;
	
	// input: 로그인 된 사람의 userId
	// output: List<Post>
	public List<Post> getPostListByUserId(int userId){
		return postMapper.selectPostListByUserId(userId);
	}
	
	public void addPost(int userId, String userLoginId, 
			String subject, String content, MultipartFile file) {
		
		String imagePath = null;
		
		if (file != null) {
			// 업로드 할 이미지가 있을 때에만 업로드
			imagePath = fileManagerService.uploadFile(file, userLoginId);
		}
		
		postMapper.insertPost(userId, subject, content, imagePath);
	}
	
	// input : userId, postId
	// output : Post or null
	public Post getPostByPostIdUserId(int userId, int postId) {
		
		return postMapper.selectPostByPostIdUserId(userId, postId);
	}
	
	// input : 파라미터들
	// output : X
	public void updatePostByPostId(
			int userId, String userLoginId,
			int postId, String subject, String content,
			MultipartFile file) {
		// 기존 글 가져온다. (1. 이미지 교체시 삭제하기 위해 / 2.업데이트 대상이 있는지 확인)
		Post post = postMapper.selectPostByPostIdUserId(userId, postId);
		if (post == null) {
			log.warn("[글 수정] post is null. userId:{}, postId: {}", userId, postId);
			return;
		}
		
		// 파일이 있으면
		// 1) 새 이미지를 업로드
		// 2) 1번 단계가 성공하면 기존 이미지가 있을 때 삭제
		String imagePath = null;
		
		if (file != null) {
			// 새 이미지 업로드
			imagePath = fileManagerService.uploadFile(file, userLoginId);
			
			// 업로드 성공 시 (imagePath != null) 기존 이미지가 있으면 제거
			if(imagePath != null && post.getImagePath() != null) {
				// 폴더와 이미지 제거
				fileManagerService.deleteFile(post.getImagePath());
			}
		}
		
		// db update
		postMapper.updatePostByPostId(postId, subject, content, imagePath);
	}
	
	// input : 파라미터들
	// output : X
	public void deletePostByPostId(
			int userId, String userLoginId,
			int postId, MultipartFile file) {
		
		Post post = postMapper.selectPostByPostIdUserId(userId, postId);
		if (post == null) {
			log.warn("[글 삭제] post is null. userId:{}, postId: {}", userId, postId);
			return;
		}
		
		String imagePath = post.getImagePath();
		
		if (imagePath != null) {
			fileManagerService.deleteFile(imagePath);
		} 
		
		postMapper.deletePostByPostId(postId);
	}
	
}

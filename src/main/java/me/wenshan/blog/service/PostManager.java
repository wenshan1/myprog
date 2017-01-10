package me.wenshan.blog.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import me.wenshan.blog.backend.form.PostForm;
import me.wenshan.blog.backend.form.PostVO;
import me.wenshan.blog.domain.Post;
import me.wenshan.blog.domain.PostComment;
import me.wenshan.blog.domain.PostStatus;
import me.wenshan.blog.domain.Tag;

@Component
public class PostManager {
	@Autowired 
	private PostService postService;
	@Autowired 
	private TagService tagService;
	
	public ArrayList<Tag> getTag(String strTag) {
		ArrayList<Tag> lst = new ArrayList<Tag> ();
		
		return lst;
	}
	public boolean save (PostForm form) {
		long id = form.getId();
		Post post = postService.getById(id);
		Calendar cal = Calendar.getInstance();
		boolean bNotExist = (post == null);  
		if (bNotExist) { 
			post = new Post ();
	        post.setCreateTime(cal.getTime());
		}
		String[] tagsArray = form.getStrtag().split(",");
		List<Tag> postTags = post.getTags();
		postTags.clear();
		
		for (String tag : tagsArray) {
			String tagName = tag.trim();
			if (!tagName.isEmpty() && !tagService.exists(tagName) )
				tagService.save(new Tag(tagName));
			if (tagService.exists(tagName)) {
				Tag foundTag = tagService.getOne(tagName);
				if (!postTags.contains(foundTag))
					postTags.add(foundTag);
			}
		}
		
		post.setTitle(form.getTitle());
		post.setContent(form.getContent());
		post.setPstatus(form.getPstatus()==0?PostStatus.publish:PostStatus.secret);
		post.setCstatus(form.getCstatus()==0?PostComment.open:PostComment.close);
		post.setLastUpdate(cal.getTime());
		
		boolean bRet;
		if (bNotExist) {
		    bRet = postService.save(post);
		    if (bRet)
		        form.setId(postService.getTopId ());
		}
		else {
		    bRet = postService.update(post);
		}

		
		return bRet;
	}
	
    public List<PostVO> getPageData(int first, int pageSize) {
        List<Post>  lst = postService.getPageData (first, pageSize);
        List<PostVO> lstVO = new ArrayList<PostVO> ();
        for (int i = 0; i < lst.size(); i ++) {
            PostVO vo = new PostVO ();
            vo.setPost(lst.get(i));
            vo.setStrTags(lst.get(i).getTagsStr());
            lstVO.add(vo);
            
        }
        return lstVO;
    }
}

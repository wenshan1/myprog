package me.wenshan.blog.backend.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import me.wenshan.blog.backend.form.PostForm;
import me.wenshan.blog.backend.form.PostVO;
import me.wenshan.blog.domain.Post;
import me.wenshan.blog.domain.PostStatus;
import me.wenshan.blog.service.PostManager;
import me.wenshan.blog.service.PostService;
import me.wenshan.util.LBPage;

@Controller
@RequestMapping("/blog/backend/posts")
public class PostController {
	@Autowired
	private PostManager postManager;
	@Autowired 
	private PostService postService;
	
	  @RequestMapping(method = RequestMethod.GET)
	  //public String index(@RequestParam(value = "curPage", defaultValue = "1") int curPage, Model model){
	  
	  public String index(@RequestParam(value = "curPage", required = false) Integer curPage, Model model, 
	          HttpSession session){
	      if (curPage == null) {
	          if (session.getAttribute("postsCurPage") == null) {
	              curPage = 1;
	          }
	          else {
	              curPage = (Integer) session.getAttribute("postsCurPage");
	          }
	      }
	      session.setAttribute("postsCurPage", curPage);
	      
	      LBPage<PostVO> page = new LBPage<PostVO>();
	      page.setTotalrecord(postService.count());
	      page.setMaxresult(15);
	      page.setCurrentpage(curPage);
	      if (page.getTotalrecord() % page.getMaxresult() == 0)
	          page.setTotalpage(page.getTotalrecord() / page.getMaxresult());
	      else
	          page.setTotalpage(page.getTotalrecord() / page.getMaxresult() + 1);
	      
	      java.util.List<PostVO> lst = postManager.getPageData (page.getFirstResult(),
                  page.getMaxresult());
	      
	      page.setRecords(lst);
	      model.addAttribute("page", page);
	      return "blog/backend/post/list";
	      }

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String create(Model model) {
	    
		model.addAttribute("postForm", new PostForm ());
		
		return "blog/backend/post/edit";
	}
	
	@RequestMapping(value = "/{pid}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable Integer pid, Model model) {
	    Post post = null;
	    if (pid == null || (post = postService.getById(pid)) == null) {
	        model.addAttribute("postForm", new PostForm ());   
	    }
	    else {
	        model.addAttribute("postForm", new PostForm (post));
	    }
	    
	    return "blog/backend/post/edit";
	    }
	
	@RequestMapping(value = "/{pid}/trash", method = RequestMethod.GET)
	public String trash(@PathVariable Integer pid, Model model) {
	    Post post = null;
	    if (pid == null || (post = postService.getById(pid)) == null) {
	           
	        }
	    else {
	        post.setPstatus(PostStatus.trash);
	        postService.update(post);
	        }
	    return "redirect:/blog/backend/posts";
	    }
	   
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String updatePost( @ModelAttribute PostForm form, Model model, HttpServletRequest request) {
		String contenc = request.getParameter("myEditor");
		form.setContent(contenc);
		model.addAttribute("postForm", form);
		
		boolean bRet = postManager.save(form);
		model.addAttribute("success", bRet?"保存成功":"保存失败");
		
		return "blog/backend/post/edit";
	}
	
}

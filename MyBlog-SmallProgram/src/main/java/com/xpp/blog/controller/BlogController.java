package com.xpp.blog.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.xpp.blog.controller.ex.FileIOException;
import com.xpp.blog.controller.ex.FileSizeException;
import com.xpp.blog.controller.ex.FileStateException;
import com.xpp.blog.entity.Blog;
import com.xpp.blog.entity.User;
import com.xpp.blog.service.BlogService;
import com.xpp.blog.service.UserService;
import com.xpp.blog.util.JsonResult;

@RestController
@RequestMapping("blog")
public class BlogController extends BaseController {
	@Autowired
	BlogService blogService;
	@Autowired
	UserService userService;

	/*
	 * Springboot默认对上传文件的大小进行前置验证，默认上限为10MB， 如果文件直接超过这个大小，会被前置验证拒绝，子控制器中的验证 逻辑不会被执行。
	 */
	// 设置上传文件大小限制
	private static final long AVATAR_MAX_SIZE = 1 * 1024 * 1024;
	// 设置上传文件类型限制
//	private static final List<String> AVATAR_TYPES = new ArrayList<>();
//	// 静态初始化器，用于初始化本类的静态成员
//	static {
//		AVATAR_TYPES.add("image/jpeg");
//		AVATAR_TYPES.add("image/png");
//		AVATAR_TYPES.add("image/gif");
//	}

	@PostMapping("image")
	public Map<String, String> uploadImage(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
		if (!file.isEmpty()) {
			// 验证上传文件的大小是否超过限制
			Long size = file.getSize();
			if (size > AVATAR_MAX_SIZE) {
				throw new FileSizeException("文件上传异常，文件大小超过" + (AVATAR_MAX_SIZE / 1024) + "kb");
			}
//			// 验证上传文件的类型是否符合,即设置的类型里是否包含上传文件的类型
//			if (!AVATAR_TYPES.contains(file.getContentType())) {
//				throw new FileTypeException("文件上传异常，文件类型不正确，允许的类型有:" + AVATAR_TYPES);
//			}

			// 获取上传文件的原文件名
			String oldFileName = file.getOriginalFilename();

			// 上传文件没有后缀名的情况
			String suffix = "";

			// 上传文件有后缀名的情况
			Integer index = oldFileName.lastIndexOf(".");
			if (index != -1) {
				// 基于文件最后一个点所在的下标获取后缀名
				suffix = oldFileName.substring(index);
			}
			// 基于UUID(通用唯一识别码)给上传文件设置新的文件名，以保证存入数据库中的文件不重名
			String newFileName = UUID.randomUUID().toString() + suffix;

			// 生成上传文件所保存的路径(目录)
			String filePath = request.getServletContext().getRealPath("upload");

			// 生成一个代表文件目录(filePath)的File对象(parent)
			File parent = new File(filePath);

			// 如果目录不存在，创建对应目录
			if (!parent.exists()) {
				parent.mkdirs();
			}

			// 生成一个File对象(destfile:目标文件)代表目录parent下的上传文件newFileName
			File destfile = new File(parent, newFileName);

			// 将用户上传的头像保存到服务器上
			try {// void transferTo(File dest)：将上传的文件保存到目标路径下
				file.transferTo(destfile);
			} catch (IllegalStateException e) {
				throw new FileStateException("文件上传异常！" + e.getMessage());
				// throw new FileStateException("文件上传异常！"+e.getMessage(),e);
			} catch (IOException e) {
				throw new FileIOException("文件上传异常！" + e.getMessage());
			}

			// 将头像在服务器的路径保存到数据库
			String imagePath = "/upload/" + newFileName;

			Map<String, String> map = new HashMap<String, String>();
			map.put("state", SUCCESS.toString());
			map.put("imagePath", imagePath);

			return map;
		}
		return null;
	}

	@PostMapping("inputBlog")
	public void inputBlog(HttpServletRequest request) {
		System.err.println(request.getParameter("image"));
		Blog blog = new Blog();
		String openId = request.getParameter("openid");
		User user = userService.getByOpenId(openId);
		blog.setOpenId(openId);
		blog.setTitle(request.getParameter("title"));
		blog.setContent(request.getParameter("content"));
		blog.setImage(request.getParameter("image"));
		blog.setViews(1);
		blog.setPraise(0);
		blog.setIsPraise(0);
		blog.setIsCollect(0);
		blog.setCreatedUser(user.getNickName());
		blog.setCreatedTime(new Date());
		blogService.saveNewBlog(blog);
	}

	@PostMapping("getBlog")
	public JsonResult<List<Blog>> getAllBlog() {
		List<Blog> blog = blogService.getAllBlog();
		return new JsonResult<List<Blog>>(blog);
	}

	@GetMapping("praise")
	public void updatePraiseNum(
			@RequestParam("id") String id,
			@RequestParam("praise") String praise,
			@RequestParam("isPraise") String isPraise) {
		blogService.modifiedPraise(Long.valueOf(id), Integer.valueOf(praise),Integer.valueOf(isPraise));
	}
	
	@GetMapping("collect")
	public void updateCollect(
			@RequestParam("id")String id,
			@RequestParam("isCollect") String isCollect) {
		blogService.modifiedCollect(Long.valueOf(id), Integer.valueOf(isCollect));
	}
	
	@PostMapping("getBlogDetail")
	public JsonResult<Blog> getBlogDetail(@RequestParam("id") String id, @RequestParam("openid") String openId) {
		Blog blog = blogService.getBlogByIdAndOpenid(Long.valueOf(id), openId);
		blogService.modifiedViews(Long.valueOf(id), openId);
		return new JsonResult<Blog>(blog);
	}
	
	@PostMapping("getMyCollect")
	public JsonResult<List<Blog>> getMyCollect(@RequestParam("openid")String openId){
		if(!openId.isEmpty()) {
			List<Blog> blogs = blogService.getCollectBlog(openId);
			return new JsonResult<List<Blog>>(blogs);
		}
		return null;
	}

}

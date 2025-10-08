package com.mycom.sixclub;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mycom.sixclub.service.SixclubService;
import com.mycom.sixclub.service.vo.CommentsVO;
import com.mycom.sixclub.service.vo.PostsVO;
import com.mycom.sixclub.service.vo.SubscribeVO;
import com.mycom.sixclub.service.vo.UsersVO;

/**
 * Handles requests for the application home page.
 */
@Controller
public class QnaController {

	@Resource(name = "sixclubService") // @Autowired : type
	// ����X spring�� ������
	private SixclubService sixclubService;

	@RequestMapping("/goQna.do")
	public String goQna(@SessionAttribute(value = "userNO", required = false) Integer userNO,
			@RequestParam(value = "spage", required = false, defaultValue = "1") int spage, Model model) {
		if (userNO == null || userNO == 0) {
			model.addAttribute("msg", "로그인이 필요한 서비스입니다");
			model.addAttribute("url", "login.do");
			return "alert";
		}

		UsersVO user = sixclubService.getUserinfo(userNO);

		if (user == null) {
			model.addAttribute("msg", "사용자 정보를 찾을 수 없습니다");
			model.addAttribute("url", "login.do");
			return "alert";
		}

		int postsPerPage = 10; // 한 페이지에 보여줄 게시글 수
		int totalPosts = sixclubService.getPostCount();
		int totalPages = (int) Math.ceil((double) totalPosts / postsPerPage);

		int startRow = (spage - 1) * postsPerPage + 1;
		int endRow = spage * postsPerPage;

		ArrayList<PostsVO> postLists = sixclubService.getAllContents(startRow, endRow);
		for (PostsVO pvo : postLists) {
			pvo.setSubscribe(sixclubService.isSubscribe(pvo.getPuser_no()));
			if(pvo.isSubscribe()) {
				SubscribeVO svo = sixclubService.getSubscribe(pvo.getPuser_no());
				svo.setMonthBetween();
				pvo.setMonthBetween(svo.getMonthBetween());
			}
			pvo.setCommentCount(sixclubService.getCommentCount(pvo.getPid()));
		}

		// 이동식 페이지 블럭 처리 (spage 중심으로 최대 10개만 보이게)
		int blockSize = 10;
		int startPage = Math.max(1, spage - blockSize / 2);
		int endPage = startPage + blockSize - 1;

		if (endPage > totalPages) {
			endPage = totalPages;
			startPage = Math.max(1, endPage - blockSize + 1);
		}

		// 페이지 번호 리스트 생성 (1부터 totalPages까지)
		ArrayList<Integer> pageNumbers = new ArrayList<Integer>();
		for (int i = startPage; i <= endPage; i++) {
			pageNumbers.add(i);
		}

		model.addAttribute("postLists", postLists);
		model.addAttribute("currentPage", spage);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("pageNumbers", pageNumbers);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);

		return "/qna/qna";
	}

	@RequestMapping("/showDetailPost.do")
	public String showDetailPost(@SessionAttribute(value = "userNO", required = false) int userNO,
			@RequestParam("pid") int pid, Model model) {

		PostsVO post = sixclubService.getContents(pid);
		post.setCommentCount(sixclubService.getCommentCount(pid));
		boolean isOwner = userNO == post.getPuser_no();

		if (!isOwner && post.getSecret() == 1 && userNO != 99999) {
			model.addAttribute("msg", "공개되지 않은 글입니다");
			model.addAttribute("url", "goQna.do");
			return "alert";
		}

		ArrayList<CommentsVO> comLists = sixclubService.getComments(pid);
		for (CommentsVO comment : comLists) {
			comment.setIsOwner(comment.getCuser_no() == userNO); // 댓글 작성자와 로그인 사용자 비교
		}
		model.addAttribute("comments", comLists);

		model.addAttribute("owner", isOwner);
		model.addAttribute("post", post);
		return "/qna/showDetailPost";
	}

	@RequestMapping("/goWritePost.do")
	public String goWritePost(@SessionAttribute(value = "userNO", required = false) int userNO,
			@RequestParam(value = "pid", required = false, defaultValue = "-1") int spid, Model model) {
		if (spid != -1) {
			int pid = spid;

			PostsVO pvo = sixclubService.getContents(pid);
			model.addAttribute("post", pvo);
		}
		model.addAttribute("userId", sixclubService.getUserinfo(userNO).getUser_id());
		return "/qna/goWritePost";
	}

	@Value("${file.upload-dir}")
	private String uploadDir;

	@PostMapping("/insertPost.do")
	public String insertPost(HttpServletRequest request,
			@SessionAttribute(value = "userNO", required = false) Integer userNO, String title, String pcontents,
			Integer secret, MultipartFile attachment, Model model) {
		if (title == null || title.trim().isEmpty()) {
			model.addAttribute("msg", "제목을 입력해주세요.");
			return "/qna/goWritePost";
		}
		if (pcontents == null || pcontents.trim().isEmpty()) {
			model.addAttribute("msg", "내용을 입력해주세요.");
			return "/qna/goWritePost";
		}

		// 첨부파일 처리
		String filename = null;
		String postDir = uploadDir + "/post";
		PostsVO postVO = new PostsVO();

		if (attachment != null && !attachment.isEmpty()) {
			File uploadPath = new File(postDir);
			if (!uploadPath.exists()) {
				uploadPath.mkdirs();
			}

			filename = attachment.getOriginalFilename();
			try {
				File dest = new File(postDir, filename);
				attachment.transferTo(dest);
			} catch (IOException e) {
				e.printStackTrace();
				model.addAttribute("msg", "파일 업로드 중 오류가 발생했습니다.");
				return "/qna/goWritePost";
			}

			postVO.setAttachment(filename);
		}

		postVO.setTitle(title);
		postVO.setPcontents(pcontents);
		postVO.setSecret(secret != null ? secret : 0);
		postVO.setPuser_no(userNO);

		sixclubService.insertPost(postVO);

		model.addAttribute("msg", "게시글 작성을 완료했습니다.");
		model.addAttribute("url", "goQna.do");
		return "alert";
	}

	@PostMapping("/updatePost.do")
	public String updatePost(HttpServletRequest request, String title, String pcontents, Integer secret,
			MultipartFile attachment, Integer pid, Model model) {
		if (title == null || title.trim().isEmpty()) {
			model.addAttribute("msg", "제목을 입력해주세요.");
			return "/qna/goWritePost";
		}
		if (pcontents == null || pcontents.trim().isEmpty()) {
			model.addAttribute("msg", "내용을 입력해주세요.");
			return "/qna/goWritePost";
		}

		// 첨부파일 처리
		String filename = null;
		String postDir = uploadDir + "/post";
		if (attachment != null && !attachment.isEmpty()) {
			File uploadPath = new File(postDir);
			if (!uploadPath.exists()) {
				uploadPath.mkdirs();
			}

			filename = attachment.getOriginalFilename();
			try {
				File dest = new File(postDir, filename);
				attachment.transferTo(dest);
			} catch (IOException e) {
				e.printStackTrace();
				model.addAttribute("msg", "파일 업로드 중 오류가 발생했습니다.");
				return "/qna/goWritePost";
			}
		} else {
			// 첨부파일이 수정되지 않았을 경우 기존 파일명 유지 필요
			filename = sixclubService.getContents(pid).getAttachment(); // 또는 이전 attachment 값
		}

		PostsVO postVO = new PostsVO();
		postVO.setPid(pid);
		postVO.setTitle(title);
		postVO.setPcontents(pcontents);
		postVO.setSecret(secret != null ? secret : 0);
		postVO.setAttachment(filename);

		sixclubService.updatePost(postVO);
		return "redirect:/showDetailPost.do?pid=" + postVO.getPid();
	}

	// 파일 다운로드
	@GetMapping("/downloadFile")
	public void downloadFile(@RequestParam("filename") String filename, HttpServletResponse response)
			throws IOException {
		String postDir = uploadDir + "/post";
		File file = new File(postDir, filename);
		if (!file.exists()) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		response.setContentType("application/octet-stream");
		response.setContentLength((int) file.length());
		response.setHeader("Content-Disposition",
				"attachment; filename=\"" + URLEncoder.encode(filename, "UTF-8").replace("+", "%20") + "\"");

		BufferedInputStream in = null;
		OutputStream out = null;
		try {
			in = new BufferedInputStream(new FileInputStream(file));
			out = response.getOutputStream();
			byte[] buffer = new byte[8192];
			int len;
			while ((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@RequestMapping("/deletePost.do")
	public String deletePost(@RequestParam("pid") int pid, Model model) {
		sixclubService.deletePost(pid);
		model.addAttribute("msg", "게시글 삭제가 완료되었습니다");
		model.addAttribute("url", "goQna.do");
		return "alert";
	}

	// 댓글 컨트롤
	@RequestMapping("/insertComment.do")
	public String insertComment(@SessionAttribute(value = "userNO", required = false) int userNO,
			@ModelAttribute("commentVO") CommentsVO commentVO, Model model) {
		commentVO.setCuser_no(userNO);

		if (commentVO.getCcontents() == null || commentVO.getCcontents().trim().equals("")) {
			model.addAttribute("msg", "내용을 입력해주세요");
			return "redirect:/showDetailPost.do?pid=" + commentVO.getPid();
		}

		if (commentVO.getCcid() == 0)
			commentVO.setCcid(null);

		sixclubService.insertComment(commentVO);

		return "redirect:/showDetailPost.do?pid=" + commentVO.getPid();
	}

	@RequestMapping("/deleteComment.do")
	public String deleteComment(@RequestParam("cid") int cid, @RequestParam("pid") int pid,
			RedirectAttributes redirectAttributes) {
		sixclubService.deleteComment(cid);
		redirectAttributes.addFlashAttribute("msg", "댓글 삭제가 완료되었습니다");
		return "redirect:/showDetailPost.do?pid=" + pid;
	}

}

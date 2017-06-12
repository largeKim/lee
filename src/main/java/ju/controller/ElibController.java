package ju.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ju.dto.ElibDTO;
import ju.elib.model.ElibDAO;
import ju.modul.BookCateModul;
import ju.modul.ElibPaging;

@Controller
public class ElibController {
	
	@Autowired
	private ElibDAO elibDAO;
	
	/** 전자도서관 메인 */
	@RequestMapping(value="ebookMain.ju")
	public ModelAndView eMain() {
		ModelAndView mav=new ModelAndView();
		mav.setViewName("ebook/ebookMain");
		return mav;
	}
	
	/**ebook 메인*/
	@RequestMapping(value="ebook.ju")
	public ModelAndView ebook() {
		BookCateModul bcm=new BookCateModul();
		String bookLgSelect=bcm.BookLgSelect(0, 7, true);
		
		ModelAndView mav=new ModelAndView();
		mav.addObject("bookLgSelect", bookLgSelect);
		mav.setViewName("ebook/ebook");
		return mav;
	}
	
	/**eMagazine 메인*/
	@RequestMapping(value="eMagazine.ju")
	public ModelAndView eMagazine() {
		BookCateModul bcm=new BookCateModul();
		String bookLgSelect=bcm.BookLgSelect(8, 8, false);
		
		ModelAndView mav=new ModelAndView();
		mav.addObject("bookLgSelect", bookLgSelect);
		mav.setViewName("ebook/eMagazine");
		return mav;
	}
	
	/**eEdu 메인*/
	@RequestMapping(value="eEdu.ju")
	public ModelAndView eEdu() {
		BookCateModul bcm=new BookCateModul();
		String bookLgSelect=bcm.BookLgSelect(9, 9, false);
		
		ModelAndView mav=new ModelAndView();
		mav.addObject("bookLgSelect", bookLgSelect);
		mav.setViewName("ebook/eEdu");
		return mav;
	}
	
	/**소분류 고침 Ajax*/
	@RequestMapping(value="elibCate.ju")
	public ModelAndView ebookCate(@RequestParam(value="cateLgVal", defaultValue="0")int cateLgVal) {
		BookCateModul bcm=new BookCateModul();
		ArrayList<String> cateMd=bcm.BookMdArr(cateLgVal);
		
		ModelAndView mav=new ModelAndView();
		mav.addObject("cateMd", cateMd);
		mav.setViewName("juJson");
		return mav;
	}
	
	/**최초 접근 검색내용*/
	@RequestMapping(value="elibFirst.ju")
	public ModelAndView elibFirst(
		@RequestParam(value="page", defaultValue="1")int page
		, @RequestParam(value="orderName", defaultValue="new")String orderName 
		, @RequestParam(value="idxParam", defaultValue="EB" )String idxParam
		) {
		orderName="new".equals(orderName)?"el_idx DESC":"el_recocount DESC, el_idx DESC";
		
		int startNum=(page-1)*ElibPaging.CONTENTSIZE+1;
		int endNum=startNum+ElibPaging.CONTENTSIZE-1;
		
		List<ElibDTO> elibArr=elibDAO.elibFirst(orderName, startNum, endNum, idxParam);
		int contentMax=elibDAO.elibFirstCount(idxParam);
		String paging=ElibPaging.getPaging(page, contentMax);
		
		ModelAndView mav=new ModelAndView();
		mav.addObject("elibArr", elibArr);
		mav.addObject("paging", paging);
		mav.setViewName("juJson");
		return mav;
	}
	
	/**단순 검색*/
	@RequestMapping(value="elibSimpleSearch.ju")
	public ModelAndView elibSimpleSearch(
		@RequestParam(value="simpleSearchText", defaultValue="" )String simpleSearchText
		, @RequestParam(value="page", defaultValue="1" )int page
		, @RequestParam(value="orderName", defaultValue="new" )String orderName
		, @RequestParam(value="idxParam", defaultValue="EB" )String idxParam
		) {
		orderName="new".equals(orderName)?"el_idx DESC":"el_recocount DESC, el_idx DESC";
		
		int startNum=(page-1)*ElibPaging.CONTENTSIZE+1;
		int endNum=startNum+ElibPaging.CONTENTSIZE-1;
		
		List<ElibDTO> elibArr=elibDAO.elibSimple(simpleSearchText, orderName, startNum, endNum, idxParam);
		int contentMax=elibDAO.elibSimpleCount(simpleSearchText, orderName, idxParam);
		String paging=ElibPaging.getPaging(page, contentMax);
		
		ModelAndView mav=new ModelAndView();
		mav.addObject("elibArr", elibArr);
		mav.addObject("paging", paging);
		mav.setViewName("juJson");
		return mav;
	}
	
	/**상세 검색*/
	@RequestMapping(value="elibDetailSearch.ju")
	public ModelAndView elibDetailSearch(
		@RequestParam(value="detailSubject", defaultValue="" )String detailSubject
		, @RequestParam(value="detailWrite", defaultValue="" )String detailWrite
		, @RequestParam(value="detailPub", defaultValue="" )String detailPub
		, @RequestParam(value="cateLg", defaultValue="99" )String cateLg
		, @RequestParam(value="cateMd", defaultValue="99" )String cateMd
		, @RequestParam(value="page", defaultValue="1" )int page
		, @RequestParam(value="orderName", defaultValue="new" )String orderName
		, @RequestParam(value="idxParam", defaultValue="EB" )String idxParam
		) {
		
		orderName="new".equals(orderName)?"el_idx DESC":"el_recocount DESC, el_idx DESC";
		
		String where="";
		
		detailSubject="".equals(detailSubject)?"":"el_subject LIKE '%" + detailSubject + "%' ";
		detailWrite="".equals(detailWrite)?"":"el_writer LIKE '%" + detailWrite + "%' ";
		detailPub="".equals(detailPub)?"":"el_pub LIKE '%" + detailPub + "%' ";
		cateLg="99".equals(cateLg)?"":"el_lg='" + cateLg + "' ";
		cateMd="99".equals(cateMd)?"":"el_md='" + cateMd + "' ";
		
		if(!"".equals(detailSubject)) where+=detailSubject;
		if(!"".equals(detailWrite)){
			if("".equals(where)) where+=detailWrite;
			else where+="AND "+detailWrite;
		}
		if(!"".equals(detailPub)){
			if("".equals(where)) where+=detailPub;
			else where+="AND "+detailPub;
		}
		if(!"".equals(cateLg)){
			if("".equals(where)) where+=cateLg;
			else where+="AND "+cateLg;
		}
		if(!"".equals(cateMd)){
			if("".equals(where)) where+=cateMd;
			else where+="AND "+cateMd;
		}
		if(!"".equals(where)){
			where="AND "+where;
		}
		
		int startNum=(page-1)*ElibPaging.CONTENTSIZE+1;
		int endNum=startNum+ElibPaging.CONTENTSIZE-1;
		
		List<ElibDTO> elibArr=elibDAO.elibDetail(where, orderName, startNum, endNum, idxParam);
		
		int contentMax=elibDAO.elibDetailCount(where, orderName, idxParam);
		String paging=ElibPaging.getPaging(page, contentMax);
		
		ModelAndView mav=new ModelAndView();
		mav.addObject("elibArr", elibArr);
		mav.addObject("paging", paging);
		mav.setViewName("juJson");
		return mav;
	}
	
	/**전자도서 컨텐츠 선택*/
	@RequestMapping(value="elibContent.ju")
	public ModelAndView elibContent(@RequestParam(value="el_idx", defaultValue="0")String el_idx, HttpSession session) {
		String mem_id=null;
		if( !( (String)session.getAttribute("sid")==null || "".equals((String)session.getAttribute("sid")) ) ){
			mem_id=(String)session.getAttribute("sid");
		}
		
		List<ElibDTO> elibArr=elibDAO.elibContent(el_idx);
		ModelAndView mav=new ModelAndView();
		
		mav.addObject("elibArr", elibArr.get(0));
		mav.addObject("mem_id", mem_id);
		mav.setViewName("juJson");
		return mav;
	}
	
	/**추천 기능 Ajax*/
	@RequestMapping(value="elibRecommend.ju")
	public ModelAndView elibRecommend(
		@RequestParam(value="el_idx", defaultValue="0")String el_idx
		, HttpServletRequest request
		) {
		String mem_idx=(String)request.getAttribute("sidx");
		if(mem_idx==null){
			mem_idx="";
		}
		List<ElibDTO> elibArr=elibDAO.elibContent(el_idx);
		int resultCount=-1;
		int recommend=elibArr.get(0).getEl_recocount();
		if(mem_idx==null || "".equals(mem_idx)){
		}
		else{
			String el_recommend=null;
			if(elibArr.get(0).getEl_recocount()==0){
				el_recommend=mem_idx;
			}
			else{
				String[] recoMem=elibArr.get(0).getEl_recommend().split("~");
				boolean before=true;
				for(int i=0 ; i<recoMem.length ; i++){
					if(recoMem[i].equals(el_idx)){ before=false; break;}
				}
				if(before){
					el_recommend="~" + mem_idx;
				}
			}
			if(el_recommend!=null){
				resultCount=elibDAO.elibRecommend(el_idx, el_recommend); // 업데이트 확인
			}
			recommend=elibDAO.elibContent(el_idx).get(0).getEl_recocount(); // 업뎃 후 다시 가져옴
		}
		
		ModelAndView mav=new ModelAndView();
		mav.addObject("recommend", recommend);
		mav.addObject("resultCount", resultCount);
		mav.setViewName("juJson");
		return mav;
	}
	
	/**전자도서 새로고침 기능 Ajax*/
	@RequestMapping(value="ebookRefresh.ju")
	public ModelAndView ebookRefresh(@RequestParam(value="el_idx", defaultValue="0")int el_idx) {
		/*
		 * 1. el_idx를 가지고 loan DB를 가서 현재 대여수를 받아온다
		 * 2. 5권 까지이므로 계산 후 리턴한다
		 */
		int randomNum=(int)(Math.random()*10)+1;
		ModelAndView mav=new ModelAndView();
		if(randomNum>7){
			mav.addObject("msg", "불가?");
		}
		else{
			mav.addObject("msg", "가능!");
		}
		mav.setViewName("juJson");
		return mav;
	}
	
	/**전자도서 대출신청 기능 Ajax*/
	@RequestMapping(value="ebookLoan.ju")
	public ModelAndView ebookLoan(@RequestParam(value="el_idx", defaultValue="0")int el_idx) {
		/*
		 * 1. 세션에서 ID받기
		 * 	1-1 세션이 없으면 미 로그인 로그인 하라고 리턴 
		 * 	1-2 세션이 있으면 2번으로
		 * 2. 해당 ID로 IDX 찾기
		 * 3. loan DB에서 빌릴수 있는지 확인한다 (해당 회원 최대치, 해당 권 최대치 2가지 경우)
		 *  3-1 빌릴 수 없다면 리턴
		 *  3-2 빌릴 수 있다면 4번으로
		 * 4. loan DB에 등록
		 * 5. 기간
		 */
		System.out.println("대출 신청 : " + el_idx);
		ModelAndView mav=new ModelAndView();
		mav.addObject("loan", "대출 신청 : " + el_idx);
		mav.setViewName("juJson");
		return mav;
	}

}
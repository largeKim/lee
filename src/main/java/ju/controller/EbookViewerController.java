package ju.controller;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ju.dto.ElibDTO;
import ju.elib.model.ElibDAO;

@Controller
public class EbookViewerController{
	
	@Autowired
	private ElibDAO elibDAO;
	
	/**뷰어*/
	@RequestMapping(value="eViewer.ju")
	public ModelAndView eViewer(@RequestParam(value="el_idx")String el_idx, HttpServletRequest request) {
		System.out.println(el_idx);
		System.out.println("el_idx.indexOf('AB') : " + el_idx.indexOf("AB"));
		System.out.println("el_idx.indexOf('EB') : " + el_idx.indexOf("EB"));
		System.out.println("el_idx.indexOf('EM') : " + el_idx.indexOf("EM"));
		System.out.println("el_idx.indexOf('EE') : " + el_idx.indexOf("EE"));
		
		List<ElibDTO> elibArr=null;
		String path=request.getSession().getServletContext().getRealPath("/")+"resources\\elib\\";
		String viewImgPath="\\lee\\resources\\elib\\";
		
		if(el_idx.indexOf("AB")==0){
			/* 돌려 보내기*/
		}
		else if(el_idx.indexOf("EB")==0){
			elibArr=elibDAO.elibViewer(el_idx);
			path+="eBook\\"+elibArr.get(0).getEl_idx()+"\\";
			viewImgPath+="eBook\\";
		}
		else if(el_idx.indexOf("EM")==0){
			elibArr=elibDAO.elibViewer(el_idx);
			path+="eMagazine\\"+elibArr.get(0).getEl_idx()+"\\";
			viewImgPath+="eMagazine\\";
		}
		else if(el_idx.indexOf("EE")==0){
			elibArr=elibDAO.elibViewer(el_idx);
			path+="eEdu\\"+elibArr.get(0).getEl_idx()+"\\";
			viewImgPath+="eEdu\\";
		}
		ArrayList<String> imgPath=new ArrayList<String>();
		
		File elibFile=new File(path);
		File[] contentPath=elibFile.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return ( name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".gif"));
			}
		});
		for(int i=0 ; i<contentPath.length ; i++){
			imgPath.add(viewImgPath+elibArr.get(0).getEl_idx()+"\\"+contentPath[i].getName());
		}
		String endPage=Integer.toString(imgPath.size()%2==0?imgPath.size()+1:imgPath.size()+2);
		
		ModelAndView mav=new ModelAndView();
		mav.addObject("elibArr", elibArr.get(0));
		mav.addObject("imgPath", imgPath);
		mav.addObject("endPage", endPage);
		mav.setViewName("ebook/eViewer");
		return mav;
	}
	
	/**뷰어 북마크 등록*/
	@RequestMapping(value="eViewerBookMakerAdd.ju")
	public ModelAndView eViewerBookMakerAdd(
		@RequestParam(value="page", defaultValue="1")String page
		, @RequestParam(value="el_idx", defaultValue="1")String el_idx
		, @RequestParam(value="lb_idx", defaultValue="1")String lb_idx
		) {
		if(el_idx.indexOf("EB")==0){
			/*
			 * 1. SELECT * FROM loanbook WHERE lb_idx=#{lb_idx}; 을 가져온다
			 * 2. String lb_etc=lbArr.get(0).getLb_etc;
			 * 3. 
			 */
			String lb_etc="";
			lb_etc+="~"+page;
			/*newLb_etc 업데이트 본내기
			 * */
		} // 아닌경우는 필요없음
		ModelAndView mav=new ModelAndView();
		mav.setViewName("juJson");
		return mav;
	}
	
	/**뷰어 북마크 삭제*/
	@RequestMapping(value="eViewerBookMakerDel.ju")
	public ModelAndView eViewerBookMakerDel(@RequestParam(value="page", defaultValue="1")String page) {
		//준비
		System.out.println("삭제 : " + page);
		ModelAndView mav=new ModelAndView();
		mav.setViewName("juJson");
		return mav;
	}
	
	/**뷰어 마지막 페이지 저장*/
	@RequestMapping(value="eViewerEndMaker.ju")
	public ModelAndView eViewerEndMaker(
		@RequestParam(value="endPage", defaultValue="#/page/1")String endPage
		, @RequestParam(value="el_idx", defaultValue="")String el_idx
		, @RequestParam(value="lb_idx", defaultValue="")String lb_idx
		) {
		if(el_idx.indexOf("EB")==0){
			System.out.println("마지막 : " + endPage);
			/*
			 * 1. SELECT * FROM loanbook WHERE lb_idx=#{lb_idx}; 을 가져온다
			 * 2. String lb_etc=lbArr.get(0).getLb_etc;
			 * 3. 
			 */
			String lb_etc="";
			String[] etcs=lb_etc.split("~");
			etcs[0]=endPage;
			String newLb_etc=etcs[0];
			for(int i=1 ; i<etcs.length ; i++){
				newLb_etc+="~"+etcs[i];
			}
			/*newLb_etc 업데이트 본내기
			 * */
		} // 아닌경우는 필요없음
		ModelAndView mav=new ModelAndView();
		mav.setViewName("juJson");
		return mav;
	}

}
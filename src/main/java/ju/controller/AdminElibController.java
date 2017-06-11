package ju.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import ju.dto.ElibDTO;
import ju.elib.model.ElibDAO;
import ju.modul.BookCateModul;
import ju.modul.ElibPaging;

@Controller
public class AdminElibController {
	
	@Autowired
	private ElibDAO elibDAO;
	
	/**업로드 최초 로딩*/
	@RequestMapping(value="elibUpload.ju")
	public ModelAndView adminElib(){
		ModelAndView mav=new ModelAndView();
		mav.setViewName("elibManage/elibUpload");
		return mav;
	}

	/**그룹에 따른 대분류 가져오기 업로드용*/
	@RequestMapping(value="elibGrupLg.ju")
	public ModelAndView adminElibGrupLg(@RequestParam(value="groupNum", defaultValue="7")int groupNum){
		BookCateModul bcm=new BookCateModul();
		String cateLg=null;
		if(groupNum==7){
			cateLg=bcm.BookLgSelect(0, 7, false);
		}
		else{
			cateLg=bcm.BookLgSelect(groupNum, groupNum, false);
		}
		ModelAndView mav=new ModelAndView();
		mav.addObject("cateLg", cateLg);
		mav.setViewName("juJson");
		return mav;
	}
	
	
	/** 내용 업로드 */
	@RequestMapping(value="adminUpload.ju", method=RequestMethod.POST)
	public ModelAndView adminUpload(
			@RequestParam(value="readyIdx")String readyIdx
			, @RequestParam(value="cateLg", defaultValue="0")int cateLg
			, HttpServletRequest request){
		System.out.println("여긴 컨트롤러");
		
		MultipartHttpServletRequest mphsr=(MultipartHttpServletRequest)request;
		List<MultipartFile> files = mphsr.getFiles("files");
		String path=request.getSession().getServletContext().getRealPath("/")+"resources\\elib\\";
		
		String elGroup=null;
		if(cateLg==8){
			elGroup="EM";
			path+="eMagazine\\";
		}
		else if(cateLg==9){
			elGroup="EE";
			path+="eEdu\\";
		}
		else{
			elGroup="EB";
			path+="eBook\\";
		}
		String el_idx=elGroup+readyIdx;
		System.out.println("el_idx : " + el_idx);
		System.out.println(path);
		
		File bookFolder=new File(path + el_idx);
		for(int i=0 ; i<files.size() ; i++){
			if(bookFolder.isDirectory()){
				/*두번째 이후*/
				File[] fileCount=bookFolder.listFiles(new FilenameFilter() {
					public boolean accept(File dir, String name) {
						return ( name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".gif"));
					}
				});
				
				String changeName=Integer.toString(fileCount.length+1);
				
				while(changeName.length()!=4){
					changeName="0"+changeName;
				}
				System.out.println(changeName);
				
				copyInto(files.get(i), path+el_idx+"\\", changeName);
			}
			else{
				/*최초*/
				bookFolder.mkdirs();
				copyInto(files.get(i), path+el_idx+"\\", "0001");
			}
		}
		
		
		ModelAndView mav=new ModelAndView();
		mav.setViewName("juJson");
		return mav;
	}
	
	/**관리자 전자도서 등록 후 이동*/
	@RequestMapping(value="elibList.ju", method=RequestMethod.POST)
	public ModelAndView adminElibList(
			@ModelAttribute("elibDTO")ElibDTO elibDTO
			, @RequestParam(value="cateLg", defaultValue="0")int cateLg
			, @RequestParam(value="cateMd", defaultValue="0")int cateMd
			, @RequestParam(value="cover")MultipartFile cover
			, HttpServletRequest request
		){
		
		String el_idx=elibDTO.getEl_idx();
		String path=request.getSession().getServletContext().getRealPath("/")+"resources\\elib\\cover\\";
		String elGroup="EB";
		if(cateLg==8){
			elGroup="EM";
		}
		else if(cateLg==9){
			elGroup="EE";
		}
		el_idx=elGroup+el_idx;
		elibDTO.setEl_idx(el_idx);
		
		elibDTO.setEl_lg(Integer.toString(cateLg));
		elibDTO.setEl_md(Integer.toString(cateMd));
		
		String[] coverName=cover.getOriginalFilename().split("\\.");
		elibDTO.setEl_path(request.getContextPath()+"\\resources\\elib\\cover\\"+elibDTO.getEl_idx()+"."+coverName[coverName.length-1]);
		
		elibDTO.setEl_recommend("~");
		elibDTO.setEl_recocount(0);
		
		copyInto(cover, path, el_idx);
		
		int resultCount=elibDAO.elibInsert(elibDTO);
		System.out.println(resultCount);
		
		ModelAndView mav=new ModelAndView();
		mav.setViewName("elibManage/elibList");
		return mav;
	}
	
	/**리스트 최초 로딩*/
	@RequestMapping(value="elibList.ju", method=RequestMethod.GET)
	public ModelAndView elibList(){
		ModelAndView mav=new ModelAndView();
		mav.setViewName("elibManage/elibList");
		return mav;
	}
	
	/**최초 로딩 미검색 ajax*/
	@RequestMapping(value="elibNoList.ju")
	public ModelAndView elibNoList(@RequestParam(value="page", defaultValue="1")int page){
		
		int startNum=(page-1)*ElibPaging.CONTENTSIZE+1;
		int endNum=startNum+ElibPaging.CONTENTSIZE-1;
		int contentMax=elibDAO.elibAdminNoListCount();
		
		List<ElibDTO> elibArr=elibDAO.elibAdminNoList(startNum, endNum);
		String paging=ElibPaging.getPaging(page, contentMax);
		
		ModelAndView mav=new ModelAndView();
		mav.addObject("elibArr", elibArr);
		mav.addObject("paging", paging);
		mav.setViewName("juJson");
		return mav;
	}
	
	/**삭제*/
	@RequestMapping(value="elibDelete.ju")
	public ModelAndView elibDelete(@RequestParam(value="el_idx")String el_idx, HttpServletRequest request) {
		List<ElibDTO> elibArr=elibDAO.elibContent(el_idx);
		String pathCover=request.getSession().getServletContext().getRealPath("/");
		pathCover=pathCover.substring(0, pathCover.length()-5)+elibArr.get(0).getEl_path();
		String pathImg=request.getSession().getServletContext().getRealPath("/")+"resources\\elib\\";

		if(el_idx.indexOf("AB")==0){
			/* 돌려 보내기*/
		}
		else if(el_idx.indexOf("EB")==0){
			pathImg+="eBook\\"+elibArr.get(0).getEl_idx();
		}
		else if(el_idx.indexOf("EM")==0){
			pathImg+="eMagazine\\"+elibArr.get(0).getEl_idx();
		}
		else if(el_idx.indexOf("EE")==0){
			pathImg+="eEdu\\"+elibArr.get(0).getEl_idx();
		}
		System.out.println("pathCover : " + pathCover);
		System.out.println("pathImg : " + pathImg);
		
		File cover=new File(pathCover);
		cover.delete();
		
		File imgFolder=new File(pathImg);
		File imgFiles[]=imgFolder.listFiles();
		for(int i=0 ; i<imgFiles.length ; i++){
			imgFiles[i].delete();
		}
		imgFolder.delete();
		
		int resultCount=elibDAO.elibDelete(el_idx);
		
		ModelAndView mav=new ModelAndView();
		mav.addObject("resultCount", resultCount);
		mav.setViewName("juJson");
		return mav;
	}
	
	/*파일 업로드 하는 메소드*/
	public void copyInto(MultipartFile files, String path, String changeName) {
		try {
			byte bytes[]=files.getBytes();
			String[] fileName=files.getOriginalFilename().split("\\.");
			fileName[0]=changeName;
			File outFile=new File(path+fileName[0]+"."+ fileName[fileName.length-1]);
			FileOutputStream fos=new FileOutputStream(outFile);
			fos.write(bytes);
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
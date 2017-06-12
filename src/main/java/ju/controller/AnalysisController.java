package ju.controller;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AnalysisController {

	@RequestMapping(value="/analSearch.ju")
	public ModelAndView analysis(){
		// 검색어 빈도
		File f = new File("../LOG/book/searchList.log");
		Map<String, Integer> map = new HashMap<String, Integer>();
		ValueComparator bvc =  new ValueComparator(map);
        TreeMap<String,Integer> sorted_map = new TreeMap<String,Integer>(bvc);
        
		if(f.exists()){
			try {
				BufferedReader in = new BufferedReader(new FileReader(f));
				String s;
				while ((s = in.readLine()) != null) {
					String arr[] = s.split("/");
					for (int i = 0; i < arr.length; i++) {
						if(map.get(arr[i])==null){
							map.put(arr[i], 1);
						}else{
							int count = map.get(arr[i])+1;
							map.put(arr[i], count);
						}
					}
				}
				in.close();
			} catch (Exception e) {
			}
		}
		sorted_map.putAll(map);
        
		String json = "[";
		int count = 0;
        for (Map.Entry<String,Integer> entry : sorted_map.entrySet()) {
            //정렬한 리스트에서 순번을 배열번호로 변경하여 원본 리스트에서 추출
        	count++;
        	if(count>7){
        		break;
    		}else{
    			json += "{text:'" + entry.getKey() + "', count:'" + map.get(entry.getKey()) + "'},";
    		}
        }
        json = json.substring(0, json.length()-1);
        json += "]";
		
		ModelAndView mav = new ModelAndView("/anal/analMain");
		mav.addObject("list", json);
		return mav;
	}
	
	@RequestMapping(value="/analUser.ju")
	public ModelAndView analUser(){
		// 사용자 빈도
		File f = new File("../LOG/member/memberInfo.log");
		Map<String, Integer> map = new HashMap<String, Integer>();
		ValueComparator bvc =  new ValueComparator(map);
        TreeMap<String,Integer> sorted_map = new TreeMap<String,Integer>(bvc);
        
		if(f.exists()){
			try {
				BufferedReader in = new BufferedReader(new FileReader(f));
				String s;
				while ((s = in.readLine()) != null) {
					String arr[] = s.split("/");
					for (int i = 0; i < arr.length; i++) {
						if(map.get(arr[i])==null){
							map.put(arr[i], 1);
						}else{
							int count = map.get(arr[i])+1;
							map.put(arr[i], count);
						}
					}
				}
				in.close();
			} catch (Exception e) {
			}
		}
		sorted_map.putAll(map);
        
		String json = "[";
		int count = 0;
        for (Map.Entry<String,Integer> entry : sorted_map.entrySet()) {
            //정렬한 리스트에서 순번을 배열번호로 변경하여 원본 리스트에서 추출
        	count++;
        	if(count>7){
        		break;
    		}else{
    			json += "{text:'" + entry.getKey() + "', count:'" + map.get(entry.getKey()) + "'},";
    		}
        }
        json = json.substring(0, json.length()-1);
        json += "]";
		ModelAndView mav = new ModelAndView("/anal/analUser");
		
		
		return mav;
	}
	
	@RequestMapping(value="/analLogin.ju")
	public ModelAndView analLogin(){
		ModelAndView mav = new ModelAndView("/anal/analLogin");
		return mav;
	}
	
	
	
	// 오름차순정렬
	class ValueComparator implements Comparator<String> {
		 
	    Map<String, Integer> base;
	     
	    public ValueComparator(Map<String, Integer> base) {
	        this.base = base;
	    }
	 
	    // Note: this comparator imposes orderings that are inconsistent with equals.    
	    public int compare(String a, String b) {
	        if (base.get(a) >= base.get(b)) { //반대로 하면 오름차순 <=
	            return -1;
	        } else {
	            return 1;
	        } // returning 0 would merge keys
	    }
	}
	
}

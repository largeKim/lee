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

	@RequestMapping(value="/analysis.ju")
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
        
        // 회원정보 빈도
        File f2 = new File("../LOG/member/DAU.log");
        Map<String, Integer> map2 = new HashMap<String, Integer>();
        ValueComparator bvc2 =  new ValueComparator(map2);
        TreeMap<String,Integer> sorted_map2 = new TreeMap<String,Integer>(bvc2);
        
        if(f2.exists()){
        	try {
        		BufferedReader in = new BufferedReader(new FileReader(f2));
        		String s;
        		while ((s = in.readLine()) != null) {
        			//여기부터 데이터 파싱
        		}
        		in.close();
        	} catch (Exception e) {
        	}
        }
        sorted_map.putAll(map);
        
        String json2 = "[";
        int count2 = 0;
        for (Map.Entry<String,Integer> entry : sorted_map.entrySet()) {
        	//정렬한 리스트에서 순번을 배열번호로 변경하여 원본 리스트에서 추출
        	count++;
        	if(count>7){
        		break;
        	}else{
        		json2 += "{text:'" + entry.getKey() + "', count:'" + map.get(entry.getKey()) + "'},";
        	}
        }
        json2 = json2.substring(0, json2.length()-1);
        json2 += "]";
		
		
		ModelAndView mav = new ModelAndView("/anal/analMain");
		mav.addObject("list", json);
		mav.addObject("list2", json2);
		return mav;
	}
	
	@RequestMapping(value="/test.ju")
	public ModelAndView test(){
		ModelAndView mav = new ModelAndView("/anal/test");
		return mav;
	}
	
	
	
	
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

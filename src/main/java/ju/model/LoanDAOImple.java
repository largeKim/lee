package ju.model;

import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;

import ju.dto.LoanDTO;

public class LoanDAOImple implements LoanDAO {

	private SqlSessionTemplate sqlMap;
	
	public LoanDAOImple(SqlSessionTemplate sqlMap) {
		super();
		this.sqlMap = sqlMap;
	}
	
	public List<LoanDTO> loanList() {
		List<LoanDTO> list = sqlMap.selectList("loanSELList");
		return list;
	}
	
	public List<LoanDTO> loanListAfter() {
		List<LoanDTO> list = sqlMap.selectList("loanSELAfter");
		return list;
	}
	
	public List<LoanDTO> loanList(String mem_idx) {
		List<LoanDTO> list = sqlMap.selectList("loanSELListmem",mem_idx);
		return list;
	}
	
	public int yeyakNum(String bk_isbn) {
		int result = sqlMap.selectOne("yeSELSun",bk_isbn);
		return result;
	}
	
	public int loanOne(String bk_idx) {
		int count = sqlMap.selectOne("loanSELOne",bk_idx);
		return count;
	}
	
	public int checkOutGo(LoanDTO dto) {
		int result = sqlMap.insert("loanINSAdd",dto);
		return result;
	}
	
	public int checkInGo(String bk_idx) {
		int result = sqlMap.insert("loanUPRe",bk_idx);
		return result;
	}
	
	/**정보 가져오기*/
	public List<LoanDTO> loanInfo(String lb_idx) {
		HashMap<String, String> hmap=new HashMap<String, String>();
		hmap.put("lb_idx", lb_idx);
		List<LoanDTO> loanArr=sqlMap.selectList("loanSELinfo", hmap);
		return loanArr;
	}
	
	/**마지막 읽은 쪽 북마크 등록&삭제*/
	public int loanBookMarkUp(String lb_idx, String lb_etc) {
		HashMap<String, String> hmap=new HashMap<String, String>();
		hmap.put("lb_idx", lb_idx);
		hmap.put("lb_etc", lb_etc);
		int resultCount=sqlMap.update("loanUPDbookMarkUp", hmap);
		return resultCount;
	}

	
}

package ju.model;
import java.util.List;

import ju.dto.LoanDTO;

public interface LoanDAO {

	public List<LoanDTO> loanList();
	public List<LoanDTO> loanListAfter();
	public List<LoanDTO> loanList(String mem_idx);
	public int yeyakNum(String bk_isbn);
	public int loanOne(String bk_idx);
	public int checkOutGo(LoanDTO dto);
	public int checkInGo(String bk_idx);
	
	/**정보 가져오기*/
	public List<LoanDTO> loanInfo(String lb_idx);
	/**마지막 읽은 쪽 북마크 등록&삭제*/
	public int loanBookMarkUp(String lb_idx, String lb_etc);
	
	
}

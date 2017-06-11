<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>Insert title here</title>
	<meta charset="UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    
	<link rel="stylesheet" href="/lee/resources/bootstrapk/css/bootstrap.min.css">
	<style type="text/css">
		table>tbody>tr>th{
			text-align: right;
		}
		#cateMd{
			display: none;
		}
	</style>
    
	<script type="text/javascript" src="/lee/resources/js/jquery-3.2.1.min.js"></script>
	<script type="text/javascript" src="/lee/resources/bootstrapk/js/bootstrap.min.js"></script>
	<script type="text/javascript">
	$(function() {
		var el_subject="";
		var el_writer="";
		var el_pub="";
		var el_lg="";
		var el_md="";
		var order="";
		
		changeGrup();
		noList(1);
		
		$("#searchButton").click(
			function() {
				search();
			}
		);
	}); // 기본 함수
	
	/*검색 그룹에 따른 대분류 ajax*/
	function changeGrup() {
		$("input[name=group]").change(
			function() {
				var groupNum=$("input[name=group]:checked").val();
				$.ajax({
					type : "GET"
					, url : "elibGrupLg.ju"
					, data : {groupNum : groupNum}
					, dataType : "json"
					, success: function(data){
						$("#cateMd").parent().parent().children("td").eq(0).html(data.cateLg);
						changeLg();
					}
				});
			}
		);
		$("input[name=group]").eq(0).click();
	}
	/*검색 소분류 ajax*/
	function changeLg() {
		$("#cateLg").change(
			function() {
				var cateLgVal=$("#cateLg").val();
				$.ajax({
					type : "GET"
					, url : "elibCate.ju"
					, data : {cateLgVal : cateLgVal}
					, dataType : "json"
					, success: function(data){
						var cateMdArr=data.cateMd;
						var cateMdOption=""
						for(var i=0 ; i<cateMdArr.length ; i++){
							cateMdOption+="<option value='" + i + "'>" + cateMdArr[i] + "</option>";
						}
						$("#cateMd").css("display", "inline-block").html(cateMdOption);
					}
				});
			}
		);
		$("#cateLg").change();
	}
	
	/*미검색*/
	function noList(page) {
		$.ajax({
			type : "GET"
			, url : "elibNoList.ju"
			, data : {page : page}
			, dataType : "json"
			, success: function(data){
				var elibArr=data.elibArr;
				var intoHTML="";
				if(elibArr.length==0){
					intoHTML=""; // 내용 없음 넣을 것!!	
				}
				for(var i=0 ; i<elibArr.length ; i++){
					intoHTML+='<div class="panel panel-default">';
					intoHTML+='	<div class="panel-heading" role="tab" id="' + elibArr[i].el_idx + 'Header">';
					intoHTML+='		<h4 class="panel-title">';
					intoHTML+='			<a data-toggle="collapse" data-parent="#accordion" href="#' + elibArr[i].el_idx + '" aria-expanded="true" aria-controls="' + elibArr[i].el_idx + '">';
					intoHTML+=elibArr[i].el_subject;
					intoHTML+='			</a>';
					intoHTML+='		</h4>';
					intoHTML+='	</div>';
					intoHTML+='	<div id="' + elibArr[i].el_idx + '" class="panel-collapse collapse " role="tabpanel" aria-labelledby="' + elibArr[i].el_idx + 'Header">';
					intoHTML+='		<div class="panel-body">';
					intoHTML+='			<div class="row">';
					intoHTML+='				<div class="col-md-3">';
					intoHTML+='					<img src="' + elibArr[i].el_path + '" style="width: 200px; height: auto;">';
					intoHTML+='				</div>';
					intoHTML+='				<div class="col-md-9">';
					intoHTML+='					<table class="table table-bordered table-hover">';
					intoHTML+='						<tr>';
					intoHTML+='							<th>IDX</th>';
					intoHTML+='							<td>' + elibArr[i].el_idx + '</td>';
					intoHTML+='						</tr>';
					intoHTML+='						<tr>';
					intoHTML+='							<th>제목</th>';
					intoHTML+='							<td>' + elibArr[i].el_subject + '</td>';
					intoHTML+='						</tr>';
					intoHTML+='						<tr>';
					intoHTML+='							<th>저자</th>';
					intoHTML+='							<td>' + elibArr[i].el_writer + '</td>';
					intoHTML+='						</tr>';
					intoHTML+='						<tr>';
					intoHTML+='							<th>출판사</th>';
					intoHTML+='							<td>' + elibArr[i].el_pub + '</td>';
					intoHTML+='						</tr>';
					intoHTML+='						<tr>';
					intoHTML+='							<th>간략소개</th>';
					intoHTML+='							<td>' + elibArr[i].el_info + '</td>';
					intoHTML+='						</tr>';
					intoHTML+='						<tr>';
					intoHTML+='							<th>구분</th>';
					intoHTML+='							<td>';
					intoHTML+='								라디오 버튼s';
					intoHTML+='							</td>';
					intoHTML+='						</tr>';
					intoHTML+='						<tr>';
					intoHTML+='							<td>cateLg</td>';
					intoHTML+='							<td>cateMd</td>';
					intoHTML+='						</tr>';
					intoHTML+='						<tr>';
					intoHTML+='							<th>추천인</th>';
					intoHTML+='							<td>' + elibArr[i].el_recommend + '</td>';
					intoHTML+='						</tr>';
					intoHTML+='						<tr>';
					intoHTML+='							<th>추천수</th>';
					intoHTML+='							<td>' + elibArr[i].el_recocount + '</td>';
					intoHTML+='						</tr>';
					intoHTML+='					</table>';
					intoHTML+='				</div> <!-- table 위 div -->';
					intoHTML+='			</div> <!-- 표지, 표 row -->';
					intoHTML+='			<div class="row text-center">';
					intoHTML+='				<button type="button" class="btn btn-default" onClick="eViewer(idx)">뷰어로 보기</button>';
					intoHTML+='				<button type="button" class="btn btn-warning">수정</button>';
					intoHTML+='				<button type="button" class="btn btn-success" disabled="disabled">완료</button>';
					intoHTML+='				<button type="button" class="btn btn-danger" data-idx="' +elibArr[i].el_idx + '">삭제</button>';
					intoHTML+='			</div>';
					intoHTML+='		</div> <!-- panel-body -->';
					intoHTML+='	</div> <!-- collapse -->';
					intoHTML+='</div> <!-- panel-default -->';
				}
				$("#accordion").html(intoHTML);
				$("#pagingDiv").html(data.paging);
				
				$("#pagingDiv>ul>li").removeClass("active");
				var pagingLength=$("#pagingDiv>ul>li").length;
				for(var i=0 ; i<pagingLength ; i++){
					if( $("#pagingDiv>ul>li").eq(i).data("page")==page ){
						$("#pagingDiv>ul>li").eq(i).addClass("active");
					}
				}
				
				$("#pagingDiv>ul>li").click(
					function() {
						var page=$(this).data("page");
						/*해당 버튼 사용 불가*/
						if($(this).hasClass("disabled")==true || $(this).hasClass("active")==true){
							return null;
						}
						$("body").scrollTop(0);
						/*<< >> 판단*/
						if( page=="before" || page=="after" ){
							if( page=="before" ){ page=$(this).next().data("page")-1; }
							else if( page=="after" ){ page=$(this).prev().data("page")+1; }
						}
						else{
							$("#pagingDiv>ul>li").removeClass("active");
							$(this).addClass("active");
						}
						noList(page);
					} // click function
				); // 페이징 click
				
				// 삭제 후 다시 자기 페이지로
				$(".btn-danger").click(
					function() {
						var page=$("#pagingDiv>ul>li.active").data("page");
						var num=$(".btn-danger").index(this);
						var el_idx=$(".btn-danger").eq(num).data("idx");
						if(confirm("해당 전자도서가 삭제됩니다. \n삭제 하시겠습니까?")){
							$.ajax({
								type : "GET"
								, url : "elibDelete.ju"
								, data : {el_idx : el_idx}
								, dataType : "json"
								, success: function(data){
									noList(page);
								}
							});
						}
						else{ /*취소함*/ }
					} // danger function
				); // danger click
				
				// 컨텐츠 수정
				$(".btn-warning").click(
					function() {
						var num=$(".btn-warning").index(this);
						$(".btn-warning").eq(num).prop("disabled", "disabled");
						$(".btn-success").eq(num).prop("disabled", "");
					} // warning function
				); // warning click
				
				// 컨텐츠 완료
				$(".btn-success").click(
					function() {
						var num=$(".btn-success").index(this);
						$(".btn-success").eq(num).prop("disabled", "disabled");
						$(".btn-warning").eq(num).prop("disabled", "");
					} // warning function
				); // warning click
				
			} // success function
		});
	}
	
	/*검색 실행*/
	function search() {
		el_subject=$("input[name=el_subject]").val();
		el_writer=$("input[name=el_writer]").val();
		el_pub=$("input[name=el_pub]").val();
		el_lg=$("#cateLg").val();
		el_md=$("#cateMd").val();
		order=$("input[name=order]:checked").val();
		
		console.log(el_subject);
		console.log(el_writer);
		console.log(el_pub);
		console.log(el_lg);
		console.log(el_md);
		console.log(order);
		//ajax로 가져오기
	}
	
	function modify() {
	}
	
	</script>
	
</head>
<body>
	
	<table class="table table-striped">
		<thead>
			<tr>
				<th class="text-center" colspan="2">전자 도서 검색</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<th>제목</th>
				<td>
					<input type="text" class="form-control" placeholder="제목" name="el_subject">
				</td>
			</tr>
			<tr>
				<th>저자</th>
				<td>
					<input type="text" class="form-control" placeholder="저자" name="el_writer">
				</td>
			</tr>
			<tr>
				<th>출판사</th>
				<td>
					<input type="text" class="form-control" placeholder="출판사" name="el_pub">
				</td>
			</tr>
			<tr>
				<th>구분</th>
				<td>
					<label class="checkbox-inline">
						<input type="radio" name="group"  value="0"> 전체
					</label>
					<label class="checkbox-inline">
						<input type="radio" name="group"  value="7"> 전자도서
					</label>
					<label class="checkbox-inline">
						<input type="radio" name="group" value="8"> 전자잡지
					</label>
					<label class="checkbox-inline">
						<input	type="radio" name="group" value="9"> e-교육
					</label>
				</td>
			</tr>
			<tr>
				<td>cateLg</td>
				<td>
					<select id="cateMd" name="cateMd" class="form-control"></select>
				</td>
			</tr>
			<tr>
				<th>정렬</th>
				<td>
					<label class="checkbox-inline">
						<input type="radio" name="order"  checked="checked"  value="new"> 등록순
					</label>
					<label class="checkbox-inline">
						<input type="radio" name="order" value="recommend"> 추천순
					</label>
				</td>
			</tr>
		</tbody>
		<tfoot>
			<tr>
				<th class="text-center" colspan="2">
					<button type="button" class="btn btn-default" id="searchButton">검색</button>
				</th>
			</tr>
		</tfoot>
	</table>

	<div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
	</div> <!-- 아코디언 -->
	
	<div class="text-center" id="pagingDiv">
	</div>

</body>
</html>
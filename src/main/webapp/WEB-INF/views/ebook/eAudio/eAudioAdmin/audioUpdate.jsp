<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Audio admin</title>
<link rel="stylesheet" href="/lee/resources/bootstrapk/css/bootstrap.min.css">
<!-- jQuery import -->
<script src="/lee/resources/js/jquery-3.2.1.min.js"></script>
<!-- jQuery Form Plugin import -->
<script src="/lee/resources/audio/js/jquery.form.min.js"></script>
<!-- jQuery MultiFile Plugin import -->
<script src="/lee/resources/audio/js/jquery.MultiFile.js"></script>

<script>
$(document).ready(function(){
	
	
	$("#cateLg").change(
			function() {
				var cateLgVal=$("#cateLg").val();
				if(cateLgVal==99){
					$("#cateMd").css("display", "none");
				}
				else{
					$.ajax({
						type : "GET"
						, url : "audioCate.ju"
						, data : {cateLgVal : cateLgVal}
						, dataType : "json"
						, success: function(data){
							console.log(data);
							var cateMdArr=data.cateMd;
							var cateMdOption=""
							for(var i=0 ; i<cateMdArr.length ; i++){
								cateMdOption+="<option value='" + i + "'>" + cateMdArr[i] + "</option>";
							}
							$("#cateMd").css("display", "block").html(cateMdOption);
						}
					});
				}
		});
	
	$('#cateLg').change();
	
	//오디오 파일 제한 올리기 전용 업로드
	$('#multiform input[name=audio]').MultiFile({
		max: '@', //업로드 최대 파일 갯수 (지정하지 않으면 무한대)
		accept: 'mp3', //허용할 확장자(지정하지 않으면 모든 확장자 허용)
		maxfile: '@', //각 파일 최대 업로드 크기 1GB?
		maxsize: '@',  //전체 파일 최대 업로드 크기 10GB?
		STRING: { //Multi-lingual support : 메시지 수정 가능
		    remove : "제거", //추가한 파일 제거 문구, 이미지태그를 사용하면 이미지사용가능
		    duplicate : "$file 은 이미 선택된 파일입니다.", 
		    denied : "$ext 는(은) 업로드 할수 없는 파일확장자입니다.",
		    selected:'$file 을 선택했습니다.', 
		    toomuch: "업로드할 수 있는 최대크기를 초과하였습니다.($size)", 
		    toomany: "업로드할 수 있는 최대 갯수는 $max개 입니다.",
		    toobig: "$file 은 크기가 매우 큽니다. (max $size)"
		},
		list:"#afile3-list" //파일목록을 출력할 요소 지정가능
	});
	
	//이미지 파일 제한 올리기 전용 업로드
	$('#multiform input[name=abimg]').MultiFile({
		max: '@', //업로드 최대 파일 갯수 (지정하지 않으면 무한대)
		accept: 'jpg|png|gif', //허용할 확장자(지정하지 않으면 모든 확장자 허용)
		maxfile: 1000000000, //각 파일 최대 업로드 크기 1GB?
		maxsize: 10000000000,  //전체 파일 최대 업로드 크기 10GB?
		STRING: { //Multi-lingual support : 메시지 수정 가능
		    remove : "제거", //추가한 파일 제거 문구, 이미지태그를 사용하면 이미지사용가능
		    duplicate : "$file 은 이미 선택된 파일입니다.", 
		    denied : "$ext 는(은) 업로드 할수 없는 파일확장자입니다.",
		    selected:'$file 을 선택했습니다.', 
		    toomuch: "업로드할 수 있는 최대크기를 초과하였습니다.($size)", 
		    toomany: "업로드할 수 있는 최대 갯수는 $max개 입니다.",
		    toobig: "$file 은 크기가 매우 큽니다. (max $size)"
		},
		list:"#afile3-list" //파일목록을 출력할 요소 지정가능
	});
});
</script>
</head>
<body>

<h3>오디오 정보 수정</h3>
<form name="multiform" id="multiform" action="eAudioUpdateGo.ju" >
	<!-- 책 정보 입력 -->
	<table class="table">
		<tbody>
			<tr>
				<th>책 이름</th>
				<td><input type="text" class="form-control" placeholder="책이름" name="el_subject" required="required"></td>
			</tr>
			<tr>
				<th>저자</th>
				<td><input type="text" class="form-control" placeholder="저자" name="el_writer" required="required"></td>
			</tr>
			<tr>
				<th>출판사</th>
				<td><input type="text" class="form-control" placeholder="출판사" name="el_pub" required="required"></td>
			</tr>
			<tr>
				<td>${cateLg }</td>
				<td><select id="cateMd" name="cateMd" class="form-control" required="required"></select></td>
			</tr>
			<tr>
				<th>책 정보</th>
				<td><textarea class="form-control" rows="3" name="el_info" required="required"></textarea></td>
			</tr>
			
			<tr>
				<td>
					<input type="submit" id="btnSubmit" value="수정" />
					<br/>
				</td>
			</tr>
		</tbody>
	</table>
</form>	

</body>
</html>
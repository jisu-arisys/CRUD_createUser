  
	//JSON 데이터구조 : JSON중첩 말고 JSON배열로 작성
	// const ex = [ 
  //   {id: "id" , name: "name", age:"20", gender: "남", job:"영업직"},
  //   {id: "id" , name: "name", age:"20", gender: "남", job:"영업직"} ];
  const itemName = "users";
  const formElementsName = ["id", "name", "age", "gender", "job"];
  let users = [];
  let mode = "in"; // "up";
  const titleEle = document.querySelector('h1');
  const idEle = document.getElementById('id');
  const submitEle = document.getElementById('submitBtn');
  const modeEle = document.getElementById('insertMode');
  
  //html 로딩 즉시 실행되는 이벤트
	document.addEventListener("DOMContentLoaded", ()=>{
    getJSON_inLocalStorage();
    printTable();
	})
  //form 이벤트관리 : 수정버튼의 작동
  document.getElementById('inputForm').addEventListener('submit', function(event) {
    event.preventDefault();

    if(mode === "in"){ if(! (isValidID() && isValidName()) ){return;} }
    if(mode === "up"){ if(!isValidName()){return;} }

    inputToJSON();
    saveJSON_toLocalStorage(); //변경된 사용자 목록을 저장
    printTable(); //목록 재출력
    })

	//삭제버튼의 작동
	function deleteBtnFunction(id){
		deleteUser(id);
		saveJSON_toLocalStorage(); //삭제된 사용자 목록을 저장
		printTable(); //목록 재출력
	}
  //수정모드로 전환 : 페이지 이동
  function updateModeBtnFunction(id){
    mode = "up";
    titleEle.innerHTML = "사용자 수정";
    inputSetUser(id);
    idEle.readOnly = true;
    submitEle.value = "수정";
    modeEle.hidden = false;
    console.log("update Mode");
  }
    //등록모드로 전환 : 페이지 이동
  function insertModeBtnFunction(){
    mode = "in";
    titleEle.innerHTML = "사용자 등록";
    idEle.readOnly = false;
    document.getElementById('inputForm').reset();
    submitEle.value = "등록";
    modeEle.hidden = true;
  }

	//localStorage에서 users 정보 불러오기, null이면 빈객체
function getJSON_inLocalStorage(){
	let jsonString = localStorage.getItem(itemName);
  users = jsonString ? JSON.parse(jsonString) : [];
  console.log(users);
}

	//JSON을 localStorage에 저장
function saveJSON_toLocalStorage(){
  //undefiend 제거
  let undefinedIndex = users.findIndex(obj => obj.id=== undefined);
      if(undefinedIndex !== -1){
        users.splice(undefinedIndex, 1);
      }

	let jsonString = JSON.stringify(users);
	localStorage.setItem(itemName, jsonString);
}

// JSON을 table 태그에 추가
function printTable(){
  let tbodyEle = document.getElementById("tableBody");
  let htmlString = ""; //""초기화 하지 않으면 undefined가 입력됨.

   //users 정보 없는 경우
  if(users.length === 0){
    console.log("users 정보 없음");
    tbodyEle.innerHTML = "<tr><td colspan='6' style='text-align: center;'>"
        + "user 정보가 없습니다.</td></tr>";
    return;
  }
   //users 정보 있는 경우
  users.forEach((user) => {
    htmlString += "<tr>"
    htmlString += "<td>" + user.id + "</td>";
    htmlString += "<td>" + user.name + "</td>";
    htmlString += "<td>" + user.age + "</td>";
    htmlString += "<td>" + user.gender + "</td>";
    htmlString += "<td>" + user.job + "</td>";
    htmlString += "<td><button onclick='updateModeBtnFunction(\""+user.id+"\")'>수정</button> ";
    htmlString += "<button onclick='deleteBtnFunction(\""+user.id+"\")'>삭제</button></td>";
    htmlString += "</tr>";
  });

  tbodyEle.innerHTML = htmlString;
  console.log("add table list");
}

  //유효성 검사
  function isValidID(){
    let id = document.getElementById('id').value;

    if(users.some(obj => obj.id === id)){
      alert(id + "가 이미 존재합니다. 다른 id를 입력하세요.");
      document.getElementById('id').focus();
      return false;
    }

    return true;
  }
    //유효성 검사
    function isValidName(){
      let name = document.getElementById('name').value;
      let regex = /^[a-zA-Z가-힣]+$/;

      if(!regex.test(name)){
        alert("문자만 입력 가능합니다. 다시 입력해주세요.");
        document.getElementById('name').focus();
        return false;
      }
    
      return true;
    }


  
	//valied input을 JSON에 담아 등록/수정
function inputToJSON(){
  let inputUser = {};
  for(let element of formElementsName){
    if(element !=="gender"){
      inputUser[element] = document.getElementById(element).value;
    }else{
      inputUser[element] = document.querySelector('input[name="gender"]:checked').value;
    }
  }
  console.log("inputUser", inputUser);

  //form Reset
  document.getElementById('inputForm').reset();
  
  if(mode === "in"){users.push(inputUser)};
  if(mode === "up"){
    let index = users.findIndex(obj => obj.id === inputUser.id);
    users[index] = inputUser };
}

//삭제
function deleteUser(id){
  let index = users.findIndex(obj => obj.id === id);
  if(index === -1){
    alert(id + "가 존재하지 않습니다.");
  }else{
    if(confirm(id + "를 삭제하시겠습니까?")){
      users.splice(index, 1);
      console.log("삭제확인" , users); //json 제거 확인
    }
  }
}
  //수정할 json input에 출력
function inputSetUser(id){
  let user = users.find(obj => obj.id === id);
  console.log("수정", user);

  for(let element of formElementsName){
    if(element !=="gender"){
      document.getElementById(element).value = user[element];
    }
  }

  if(user.gender === "여"){
    document.getElementById("gender_f").checked = true;
  }else{
    document.getElementById("gender_m").checked = true;
  }
}





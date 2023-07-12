  
const serverURL = "http://localhost:8080/users/";

//JSON 데이터구조 : JSON중첩 말고 JSON배열로 작성
// const ex = [ 
//   {id: "id" , name: "name", age:"20", gender: "남", job:"영업직"},
//   {id: "id" , name: "name", age:"20", gender: "남", job:"영업직"} ];
const itemName = "users";
const formElementsName = ["id", "name", "age", "gender", "job"];
const titleEle = document.querySelector('h1');
const idEle = document.getElementById('id');
const submitEle = document.getElementById('submitBtn');
const modeEle = document.getElementById('insertMode');
const searchEle = document.getElementById("searchEle");
const categoryEle = document.getElementById("category");
const sortTypeEle = document.getElementById("sortType");
const sendingEle = document.getElementById("sending");
let users = [];
let mode = "in"; // "up";
let currentPage = 1;
let totalPage = 1;  
let search = "";
let category = "id";  
let sending = "A";  
let sortType = "id";  


  //html 로딩 즉시 실행되는 이벤트
document.addEventListener("DOMContentLoaded", ()=>{
  getJSON_list_axios_restAPI_withKeyword(currentPageProxy.value);
})
  //form 이벤트관리 : 등록 및 수정버튼의 작동
document.getElementById('inputForm').addEventListener('submit', (event)=> {
  event.preventDefault();

  if(mode === "in"){ 
    if(! (isValidID() && isValidName()) ){return;}
    let inputUser = inputToJSON();
    saveJSON_axios_restAPI(inputUser);
  }

  if(mode === "up"){
    if(!isValidName()){return;}
    let inputUser = inputToJSON();
    setJSON_axios_restAPI(inputUser.id , inputUser);
    insertModeBtnFunction();       //수정완료시 등록으로 자동전환
  }
})

	//삭제버튼 axios 호출
function deleteBtnFunction(id){
  if(isValidDeleteId(id)){
    deleteId_axios_restAPI(id);
  }
}
  //모드로 전환
function updateModeBtnFunction(id){
  mode = "up";
  titleEle.innerHTML = "사용자 수정";
  inputSetUser(id);
  idEle.readOnly = true;
  submitEle.value = "수정";
  modeEle.hidden = false;
  console.log("update Mode");
}
function insertModeBtnFunction(){
  mode = "in";
  titleEle.innerHTML = "사용자 등록";
  idEle.readOnly = false;
  document.getElementById('inputForm').reset();
  submitEle.value = "등록";
  modeEle.hidden = true;
}

  //페이지네이션 클릭시 axios & Proxy 호출
function changePage(page){
  if (currentPageProxy.value !== page) {
    currentPageProxy.value = page;
    postJSON_list_axios_restAPI_withKeyword(page);  }
}
function prevPage(){
  if (currentPageProxy.value > 1) {
    changePage(currentPageProxy.value - 1 );
  }
}
function nextPage(){
  if (currentPageProxy.value < totalPage) {
    changePage(currentPageProxy.value + 1 );
  }
}
  //클래스 속성을 부여해 버튼 디자인 변경
function cssPageButton(before, affter){
  console.log("cssPageButton : ", before, affter);
  let beforeBtn = document.getElementById("btn" + before); 
  let affterBtn = document.getElementById("btn" + affter);
  beforeBtn.classList.remove("highLight");
  affterBtn.classList.add("highLight");
  affterBtn.setAttribute("class", "highLight");
}

  //currentPage 변경시 자동으로 버튼디자인 변경&페이지 불러오기
let currentPageProxy = new Proxy(
  {value: currentPage},{set: (target, key, value)=>{
    let before = target[key];
    if(before !== value){
      target[key] = value;
      console.log("currentPageProxy : ",before,">", value);
      cssPageButton(before, value);
    }
    return true;
  }});

  //검색어 목록 조회
function searchList(){
  search = searchEle.value;
  category = categoryEle.value;
  console.log("searchList : ",category, search);
  postJSON_list_axios_restAPI_withKeyword(1);
}

function getJSON_list_axios_restAPI_withKeyword(page){
    let urlPath = 'page/' + page;
  axios.get(serverURL + urlPath, {headers : {'Content-Type':'application/json'}}
  ).then((response)=> {
    users = response.data.content;
    currentPageProxy.value = response.data.number +1;
    totalPage = response.data.totalPages;

    console.log(users, "불러오기 완료", totalPage);
    printTable();
    printBtn();

    })
    .catch(function(error) {
    console.log(error, "불러올 값이 없음");
    });
}
function postJSON_list_axios_restAPI_withKeyword(setPage){
  axios.post(serverURL +"searchCondition",
    {page : setPage -1 ,
     search : search,
     category : category,
     sending : sending,
     sortType : sortType},
    {headers : {'Content-Type':'application/json'}}
  ).then((response)=> {
    users = response.data.content;
    currentPageProxy.value = response.data.number +1;
    totalPage = response.data.totalPages;

    console.log(users, "search 완료", totalPage);
    printTable();
    printBtn();

    })
    .catch(function(error) {
    console.log(error, "불러올 값이 없음");
    });
}
function getJSON_user_axios_restAPI(id){
  return new Promise((resolve, reject) => {
    axios.get(serverURL + id, {headers: {'Content-Type' : 'application/json'}}
    ).then((response)=>{
      let user = response.data;
      console.log(id,"조회성공",user);
      resolve(user);

    }).catch((error)=>{
      console.log(error, "불러올 값이 없음");
      reject(null);
    
    });
  });
}
function saveJSON_axios_restAPI(inputUser){
  axios.post(serverURL, inputUser, {headers : {'Content-Type':'application/json'}}
    ).then(function(response) {
    console.log(response.data, "저장완료");
    getJSON_list_axios_restAPI_withKeyword(1);
    })
    .catch(function(error) {
    console.log(error);
    });
}
function deleteId_axios_restAPI(id){
  axios.delete(serverURL + id, {headers : {'Content-Type':'application/json'}}
  ).then(function(response) {
    console.log(response.data, "삭제완료");
    getJSON_list_axios_restAPI_withKeyword(1);
    })
    .catch(function(error) {
    console.log(error);
    });
}
function setJSON_axios_restAPI(id, inputUser){
  axios.patch(serverURL + id, inputUser, {headers : {'Content-Type':'application/json'}}
    ).then(function(response) {
    console.log(response.data, "수정완료");
    getJSON_list_axios_restAPI_withKeyword(1);
    })
    .catch(function(error) {
    console.log(error);
    });
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
    htmlString += "<button onclick='deleteBtnFunction(\"" + user.id + "\")'>삭제</button></td>";
    htmlString += "</tr>";
  });

  tbodyEle.innerHTML = htmlString;
  console.log("add table list");
}

//버튼 출력
function printBtn(){
  //10 ~ totalpage-1 까지 hidden, 나머지 show
  for(let i=10; i>totalPage; i--){
    let hiddenBtn = document.getElementById("btn" + i); 
    hiddenBtn.hidden = true;
  }
  for(let i=1; i<=totalPage; i++){
    let hiddenBtn = document.getElementById("btn" + i); 
    hiddenBtn.hidden = false;
  }
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
  return inputUser;
}
  //수정할 json input에 출력
function inputSetUser(id){
  getJSON_user_axios_restAPI(id).then( user => {
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
  }).catch( error =>{ 
    alert("수정할" + id + "가 존재하지 않습니다.");
    console.log(error);}
  );
    
}
  //삭제 유효성검사
function isValidDeleteId(id){
  getJSON_user_axios_restAPI(id).then( user => {
      if(!(confirm(user.id + "를 삭제하시겠습니까?"))){
        return false;
      }
    }
  ).catch( error =>{ 
    alert(id + "가 존재하지 않습니다.");
    console.log(error);
    return false;    }
  );
  return true;
}
  //정렬
function reSort(){
  sending = sendingEle.value;
  sortType = sortTypeEle.value;
  postJSON_list_axios_restAPI_withKeyword(1);
}





function downloadExcel(){
  axios.post(serverURL + "excel", {
    search : search,
    category : category,
    sending : sending,
    sortType : sortType
  },{'responseType':'blob'}
  ).then((response)=> {
    const url = window.URL.createObjectURL(new Blob([response.data]));
    const link = document.getElementById('downLink');
    link.href = url;
    link.setAttribute('download', 'searchUserList.xlsx');
    link.hidden = false;
    console.log("downloadExcel : ", url);

    })
    .catch(function(error) {
    console.log(error, "downloadExcel 실패");
    });
}
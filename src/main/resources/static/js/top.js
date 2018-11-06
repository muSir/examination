function logout() {
    $.ajax({
        url: "/users/logout",
        type: "get",
        success: function(data){
            alert("   ");
        }
    });
}
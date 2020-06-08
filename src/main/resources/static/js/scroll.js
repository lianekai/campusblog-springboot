$(function() {
    window.onscroll = function () {
        if (document.documentElement.scrollTop + document.body.scrollTop > 10) {
            document.getElementById("back-to-top").style.display = "block";
        }
        else {
            if (document.getElementById("back-to-top") != null)
                document.getElementById("back-to-top").style.display = "none";
        }
    }
});
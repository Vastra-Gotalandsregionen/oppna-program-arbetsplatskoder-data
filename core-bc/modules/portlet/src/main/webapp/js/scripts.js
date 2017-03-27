function getFormattedClientTime() {
    var d = new Date();
    return d.getHours() + ":" + d.getMinutes() + ":" + d.getSeconds();
}
function putCurrentTimeIntoDisplay() {
    var placeOfCurrentTime = document.getElementById('current-time');
    if (console && !placeOfCurrentTime)
        console.log('placeOfCurrentTime was not found');
    placeOfCurrentTime.innerHTML = getFormattedClientTime();
}

